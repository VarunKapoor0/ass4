package com.example.assignment4.views

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignment4.R
import com.example.assignment4.databinding.FragmentSearchResultsBinding
import com.example.assignment4.models.FavouriteDataResponse
import com.example.assignment4.models.InsiderDataCalculated
import com.example.assignment4.models.News
import com.example.assignment4.models.PortfolioRetrieveResponse
import com.example.assignment4.services.util.RetrofitClient
import com.example.assignment4.models.SearchResponse
import com.example.assignment4.models.SentimentData
import com.example.assignment4.models.UpdateFavouriteRequest
import com.example.assignment4.services.repositories.NetworkRepository
import com.example.assignment4.services.util.ResponseCallback
import com.example.assignment4.views.dialog.NewsCustomDialog
import com.example.assignment4.views.dialog.TradeCustomDialog
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import kotlin.properties.Delegates
import kotlin.reflect.typeOf

class SearchResultsFragment : Fragment() {

    private val TAG: String = "SearchResultsFragment"
    private var isFavourite = false
    lateinit var query: String
    private var isFavGl by Delegates.notNull<Boolean>()
    private lateinit var binding: FragmentSearchResultsBinding
    private lateinit var networkRepository: NetworkRepository
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsList: List<News>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = SearchResultsFragmentArgs.fromBundle(it)
            query = args.query
            isFavGl = args.isFavourite
        }
        Log.d(TAG, "The search query from fragment is : $query")

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        (activity as? AppCompatActivity)?.supportActionBar?.title = query.uppercase()
        val retrofitClient = RetrofitClient.instance
        networkRepository = NetworkRepository(retrofitClient)
        binding.newsRecyclerView.layoutManager = LinearLayoutManager(context)
        newsAdapter = NewsAdapter(emptyList())
        binding.newsRecyclerView.adapter = newsAdapter
        if (query != null) {
            binding.spinner.visibility= View.VISIBLE
            binding.spinner.bringToFront()
            netWorkCalls(query)
        }
        if(isFavGl){

        }


        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val menuItem = menu.findItem(R.id.favourite_button)
        // Check the fragment state or arguments
        if (isFavGl) {
            // Modify the icon here
            menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.star_filled)
        } else {
            // Use the default icon or another icon
            menuItem.icon = ContextCompat.getDrawable(requireContext(), R.drawable.star_border)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.favourite_outline_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "The menu item click name is : ${item.icon}")
        if(item.itemId == R.id.favourite_button){
            addToFavourite(item)
        }
        return super.onOptionsItemSelected(item)
    }
    fun addToFavourite(item: MenuItem){

        isFavourite = !isFavourite
        if (isFavourite || isFavGl) {
            item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.star_filled)



        } else {
            item.icon = ContextCompat.getDrawable(requireContext(), R.drawable.star_border)
        }
        updateFavourites(isFavourite)
        //add removing and adding to favourite
        Log.d(TAG, "The fav value is : $isFavourite")
    }
    private fun netWorkCalls(query: String){
        stockDetailsCall(stock = query)
        portfolioRetrieve(query)
    }
    private fun stockDetailsCall(stock: String){
        networkRepository.getStockDetailsCall(stock, object: ResponseCallback<SearchResponse> {
            override fun onSuccess(result: SearchResponse) {
                Log.d(TAG, "The result for $stock is : $result")

                setStockViews(result)
                tradeButton(result)
                binding.spinner.visibility = View.GONE

            }
            override fun onError(error: String) {
                Log.d(TAG, "The error for search is : $error")
                binding.spinner.visibility = View.GONE
            }
        })
    }
    private fun updateFavourites(isFavourite: Boolean){

            networkRepository.getStockDetailsCall(query, object: ResponseCallback<SearchResponse> {
                override fun onSuccess(searchResponse: SearchResponse) {
                    if(isFavourite){
                        val updateFavouriteRequest = UpdateFavouriteRequest(searchResponse.name, searchResponse.ticker, searchResponse.current_price, searchResponse.change, searchResponse.change_percentage)
                        //Log.d(TAG, "The result for $query is : $result")
                        networkRepository.postUpdateFavourite(updateFavouriteRequest, object: ResponseCallback<ResponseBody> {
                            override fun onSuccess(result: ResponseBody) {
                                Log.d("Bought", "The portfolio value ")
                                Toast.makeText(context, "${searchResponse.ticker} is added to favorites" , Toast.LENGTH_SHORT ).show()


                            }
                            override fun onError(error: String) {
                                Log.d("Dialog", "The error is : $error")
                            }
                        })

                    }else{
                        networkRepository.postDeleteFavourite(searchResponse.ticker, object: ResponseCallback<ResponseBody> {
                            override fun onSuccess(result: ResponseBody) {
                                Log.d("Bought", "The portfolio value ")
                                Toast.makeText(context, "${searchResponse.ticker} is removed from Favourites" , Toast.LENGTH_SHORT ).show()


                            }
                            override fun onError(error: String) {
                                Log.d("Dialog", "The error is : $error")
                            }
                        })

                    }

                }
                override fun onError(error: String) {
                    Log.d(TAG, "The error is : $error")
                }
            })





    }
    private fun portfolioRetrieve(query: String){
        var portfolioValues: PortfolioRetrieveResponse? = null
        networkRepository.getPortfolio(object: ResponseCallback<List<PortfolioRetrieveResponse>> {
            override fun onSuccess(result: List<PortfolioRetrieveResponse>) {
                result.forEach {
                    if(query.equals(it.symbol, ignoreCase = true)){
                        portfolioValues = it
                        Log.d(TAG, "The values are the same. ")
                    }
                }
                portfolioValues?.let { setPortfolioViews(it) }
            }
            override fun onError(error: String) {
                Log.d(TAG, "The error for search is : $error")
            }
        })
    }
    fun setStockViews(searchResponse: SearchResponse){
        //setting the recycler view for news
        newsList = searchResponse.news
        newsList = newsList.filter { it.image!="" }
        val newsWithoutFirst = newsList.drop(1)
        //newsWithoutFirst = newsWithoutFirst.filter { it.image!="" }

        Picasso.get().load(newsList[0].image).into(binding.newsImage1)
        binding.newsSource1.text = newsList[0].source
        binding.newsHeadline1.text = newsList[0].headline
        Log.d(TAG, "The response for the news is : $newsList")
        newsAdapter.updateNews(newsWithoutFirst)

        binding.stockHeader.text = searchResponse.ticker
        binding.stockName.text = searchResponse.name
        binding.currentPrice.text = "$${searchResponse.current_price}"
        Log.d(TAG, "The current price is : ${searchResponse.current_price}")
        if(searchResponse.change_percentage>0){
            binding.change.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trending_up, 0, 0, 0)
            binding.change.setTextColor(Color.parseColor("#00FF00"))
        }else{
            binding.change.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trending_down, 0, 0, 0)
            binding.change.setTextColor(Color.parseColor("#ff0000"))
        }
        binding.change.text = "$${String.format("%.2f", searchResponse.change)}($${
            String.format(
                "%.2f",
                searchResponse.change_percentage
            )
        }%)"
        //setting stats
        binding.statsOpenPrice.text = "$${String.format("%.2f", searchResponse.open_price)}"
        binding.lowPrice.text = "$${String.format("%.2f", searchResponse.low_price)}"
        binding.statsHighPrice.text = "$${String.format("%.2f", searchResponse.high_price)}"
        binding.previousClose.text = "$${String.format("%.2f", searchResponse.previous_close)}"
        //setting about
        binding.ipo.text = "${searchResponse.ipo}"
        //binding.industry.text = "${searchResponse.in}"
        binding.webpage.text = "${searchResponse.weburl}"
        binding.webpage.setTextColor(Color.parseColor("#0099CC"))
        binding.webpage.movementMethod = LinkMovementMethod.getInstance()
        val linearLayout = binding.linearLayoutPeers
        searchResponse.peers.forEachIndexed { index, peer ->
            val spannableString = SpannableString(peer)
            spannableString.setSpan(UnderlineSpan(), 0, spannableString.length, 0)

            val textView = TextView(context).apply {
            id = TextView.generateViewId()
            text = spannableString
            textSize = 14f
            isClickable = true;
            setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark))

            setOnClickListener{
            val action = SearchResultsFragmentDirections.reopenSearchResultsFragment(peer, false)
            findNavController().navigate(action)
            }
        }
            linearLayout.addView(textView)

            // Add comma unless it's the last item
            if (index < searchResponse.peers.size - 1) {
                val commaView = TextView(context).apply {
                    text = ", "
                    textSize = 16f
                }
                linearLayout.addView(commaView)
            }
        }
        Log.d(TAG, "The company peers are : ${searchResponse.peers}")
        //binding.peers.text = "${searchResponse.peers.}"

        //Setting social sentiments
        val insiderDataCalculated = calculateInsiderSentiment(searchResponse.insider_sentiment.data)
        binding.companyNameTitle.text = "${searchResponse.name}"
        binding.totalMsrp.text = String.format("%.2f", insiderDataCalculated.totalMsrp)
        binding.insightsTotalChange.text = String.format("%.2f", insiderDataCalculated.totalChange)
        binding.insightsPositiveMsrp.text = String.format("%.2f", insiderDataCalculated.totalPositiveMsrp)
        binding.insightsPositiveChange.text = String.format("%.2f", insiderDataCalculated.totalPositiveChange)
        binding.insightsNegativeMsrp.text = String.format("%.2f", insiderDataCalculated.totalNegativeMsrp)
        binding.insightsNegativeChange.text = String.format("%.2f", insiderDataCalculated.totalNegativeChange)


    }
    fun setPortfolioViews(portfolioValue: PortfolioRetrieveResponse){
        Log.d(TAG, "The portfolio quantity is : ${portfolioValue.quantity}")
        binding.sharesOwned.text = portfolioValue.quantity.toString()
        binding.avgCostShares.text = "$"+String.format("%.2f", portfolioValue.averagePrice)
        binding.totalCost.text = "$"+String.format("%.2f", portfolioValue.totalValue)
        if(portfolioValue.change<0){
            binding.change.setTextColor(Color.parseColor("#ff0000"))
            binding.marketValue.setTextColor(Color.parseColor("#ff0000"))
        }else if(portfolioValue.change>0){
            binding.change.setTextColor(Color.parseColor("#00FF00"))
            binding.marketValue.setTextColor(Color.parseColor("#00FF00"))
        }
        binding.change.text = "$"+String.format("%.2f", portfolioValue.change)
        binding.marketValue.text = "$"+String.format("%.2f", portfolioValue.marketValue)
    }

    fun calculateInsiderSentiment(sentimentData: List<SentimentData>): InsiderDataCalculated {
        var totalChange = 0.0;
        var totalMsrp = 0.0
        var totalPositiveMsrp = 0.0
        var totalNegativeMsrp = 0.0
        var totalPositiveChange = 0.0
        var totalNegativeChange = 0.0
        Log.d(TAG, "Going Inside foreach for insider sentiment. ")
        sentimentData.forEachIndexed { _, it ->

//            totalChange += it.change
//            totalMsrp += it.mspr
            if (it.change < 0) {
                totalNegativeChange += it.change
            } else if (it.change > 0) {
                totalPositiveChange += it.change
            }
            if (it.mspr < 0) {
                totalNegativeMsrp += it.mspr
            } else if (it.mspr > 0) {
                totalPositiveMsrp += it.mspr
            }
        }
        totalChange = totalPositiveChange+totalNegativeChange
        totalMsrp = totalPositiveMsrp+totalNegativeMsrp
        return InsiderDataCalculated(
            totalMsrp,
            totalChange,
            totalNegativeMsrp,
            totalPositiveMsrp,
            totalNegativeChange,
            totalPositiveChange
        )
    }

    fun tradeButton(searchResponse: SearchResponse){
        binding.tradeButton.setOnClickListener {
            val dialog = context?.let { it1 -> TradeCustomDialog(it1, searchResponse) }
            dialog?.show()
        }

    }
    companion object
}