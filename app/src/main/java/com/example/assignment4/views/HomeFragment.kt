package com.example.assignment4.views

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.assignment4.R
import com.example.assignment4.databinding.FragmentHomeBinding
import com.example.assignment4.services.util.RetrofitClient
import com.example.assignment4.models.WalletResponse
import com.example.assignment4.services.repositories.NetworkRepository
import com.example.assignment4.services.util.ResponseCallback
import com.example.assignment4.util.Utils
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.models.FavouriteDataResponse
import com.example.assignment4.models.HomeFavouriteCardData
import com.example.assignment4.models.HomePortfolioCardData
import com.example.assignment4.models.News
import com.example.assignment4.models.PortfolioRetrieveResponse
import okhttp3.ResponseBody

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private val TAG: String = "HomeFragment"
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var networkRepository: NetworkRepository

    private lateinit var portfolioCardAdapter: PortfolioCardAdapter
    private lateinit var portfolioList: List<HomePortfolioCardData>

    private lateinit var homeFavouritePortfolioCardAdapter: HomeFavouriteCardAdapter
    private lateinit var favouriteList: MutableList<HomeFavouriteCardData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val retrofitClient = RetrofitClient.instance
        networkRepository = NetworkRepository(retrofitClient)
        setViews()
        fragmentHomeBinding.spinner.visibility = View.VISIBLE
        portfolioCardAdapter = PortfolioCardAdapter(emptyList())
        fragmentHomeBinding.portfolioRecyclerView.adapter = portfolioCardAdapter
        fragmentHomeBinding.portfolioRecyclerView.layoutManager = LinearLayoutManager(context)

        homeFavouritePortfolioCardAdapter = HomeFavouriteCardAdapter(mutableListOf())
        fragmentHomeBinding.homeFavouritesRecyclerView.adapter = homeFavouritePortfolioCardAdapter
        fragmentHomeBinding.homeFavouritesRecyclerView.layoutManager = LinearLayoutManager(context)

        netWorkCalls()
        val footerText = "Powered by Finnhub"


        val spannableString = SpannableString(footerText)

        spannableString.setSpan(
            StyleSpan(Typeface.ITALIC),
            0,
            footerText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val hyperlinkStartIndex = footerText.indexOf("Finnhub")
        val hyperlinkEndIndex = hyperlinkStartIndex + "Finnhub".length

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.light_grey)),
            hyperlinkStartIndex,
            hyperlinkEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://finnhub.io/"))
                startActivity(intent)
            }
        }

        spannableString.setSpan(
            clickableSpan,
            hyperlinkStartIndex,
            hyperlinkEndIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        fragmentHomeBinding.footerText.text = spannableString

        fragmentHomeBinding.footerText.movementMethod = LinkMovementMethod.getInstance()


        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Stocks"

    }

    private fun netWorkCalls(){
        walletBalanceCall()
        portfolioRetrieve()
        favouritesRetrieve()
    }
    fun favouritesRetrieve(){
        networkRepository.getFavourites(object: ResponseCallback<List<FavouriteDataResponse>> {
            override fun onSuccess(result: List<FavouriteDataResponse>) {
                val favouriteList: MutableList<HomeFavouriteCardData> = mutableListOf()
                result.forEach {
                    val individualPort = HomeFavouriteCardData(it.name, it.symbol, it.price, it.change, it.changePercent,true)
                    favouriteList.add(individualPort)
                }

                homeFavouritePortfolioCardAdapter.updateFavouriteValues(favouriteList)


                val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback())

                itemTouchHelper.attachToRecyclerView(fragmentHomeBinding.homeFavouritesRecyclerView)

            }
            override fun onError(error: String) {
                Log.d(TAG, "The error for search is : $error")
            }
        })
    }


    inner class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            homeFavouritePortfolioCardAdapter.removeItem(position)

        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val deleteIcon = context?.let { ContextCompat.getDrawable(it, R.drawable.delete) }
            val background = ColorDrawable(Color.RED)
            val iconMargin = (itemView.height - deleteIcon!!.intrinsicHeight) / 2
            val iconTop = itemView.top + (itemView.height - deleteIcon.intrinsicHeight) / 2
            val iconBottom = iconTop + deleteIcon.intrinsicHeight

            if (dX < 0) {
                val iconLeft = itemView.right - iconMargin - deleteIcon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
            }

            background.draw(c)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }









    private fun portfolioRetrieve(){
        val portfolioValues = mutableListOf<HomePortfolioCardData>()
        networkRepository.getPortfolio(object: ResponseCallback<List<PortfolioRetrieveResponse>> {
            override fun onSuccess(result: List<PortfolioRetrieveResponse>) {
                var portfolioList: List<HomePortfolioCardData>? = null

                result.forEach {
                    val individualPort = HomePortfolioCardData(it.name, it.symbol, it.quantity, it.change, it.totalValue)
                    portfolioValues.add(individualPort)
                }
                portfolioList = portfolioValues.toList()
                portfolioCardAdapter.updatePortfolioValues(portfolioList)

            }
            override fun onError(error: String) {
                Log.d(TAG, "The error for search is : $error")
            }
        })
    }

    private fun walletBalanceCall(){
        networkRepository.getWalletBalanceCall(object: ResponseCallback<WalletResponse>{
            override fun onSuccess(result: WalletResponse) {
                Log.d(TAG, "The result for wallet is : $result")
                fragmentHomeBinding.spinner.visibility = View.GONE
                fragmentHomeBinding.cashBalance.text="$${String.format("%.2f", result.wallet.toDouble())}"
            }

            override fun onError(error: String) {
                Log.d(TAG, "The error for search is : $error")
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem?.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    navigateToSearchResults(it)
                }
                searchView.clearFocus() // optional, to close the keyboard
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun navigateToSearchResults(query: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment(query, false)
        findNavController().navigate(action)
    }

    private fun setViews(){
        fragmentHomeBinding.date.text = Utils.currentDate()
    }

    override fun onResume() {
        super.onResume()
        fragmentHomeBinding.cashBalance.text = "$24,134.85"
    //walletBalanceCall()
    }
    companion object
}