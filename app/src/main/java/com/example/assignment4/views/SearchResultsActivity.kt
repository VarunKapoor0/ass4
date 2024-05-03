package com.example.assignment4.views

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.assignment4.R
import com.example.assignment4.databinding.ActivitySearchResultsBinding
import com.example.assignment4.models.PortfolioRetrieveResponse
import com.example.assignment4.services.util.RetrofitClient
import com.example.assignment4.models.SearchResponse
import com.example.assignment4.services.repositories.NetworkRepository
import com.example.assignment4.services.util.ResponseCallback

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResultsBinding
    private val TAG: String = "SearchResultsActivity"
    private lateinit var networkRepository: NetworkRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_results)
        //Log.d("SearchResultsActivity", "Inside handle intent. ")
        val retrofitClient = RetrofitClient.instance
        networkRepository = NetworkRepository(retrofitClient)
        val query = intent.getStringExtra("query")
        Log.d(TAG, "The search is: $query")
        if (query != null) {
            binding.spinner.visibility= View.VISIBLE
            netWorkCalls(query)
        }
        supportActionBar?.title = query
    }

    private fun netWorkCalls(query: String){

        stockDetailsCall(stock = query)
        portfolioRetrieve(query)


    }

    private fun stockDetailsCall(stock: String){
        networkRepository.getStockDetailsCall(stock, object: ResponseCallback<SearchResponse> {
            override fun onSuccess(result: SearchResponse) {
                Log.d(TAG, "The result for $stock is : $result")
                binding.spinner.visibility = View.GONE
                setStockViews(result)

            }
            override fun onError(error: String) {
                Log.d(TAG, "The error for search is : $error")
                binding.spinner.visibility = View.GONE
            }
        })
    }
    private fun portfolioRetrieve(query: String){
        var portfolioValues: PortfolioRetrieveResponse? = null
        networkRepository.getPortfolio(object: ResponseCallback<List<PortfolioRetrieveResponse>> {
            override fun onSuccess(result: List<PortfolioRetrieveResponse>) {
                Log.d(TAG, "The portfolio value is : ${result[0]}")
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

        binding.stockHeader.text = searchResponse.ticker
        binding.stockName.text = searchResponse.name
        Log.d(TAG, "The current price is : ${searchResponse.current_price}")
        if(searchResponse.change_percentage>0){
            binding.currentPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trending_up, 0, 0, 0)
            binding.currentPrice.setTextColor(Color.parseColor("#00FF00"))
        }else{
            binding.currentPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.trending_down, 0, 0, 0)
            binding.currentPrice.setTextColor(Color.parseColor("#ff0000"))
        }
        binding.currentPrice.text = "$${searchResponse.current_price}(${searchResponse.change_percentage}%)"
    }
    fun setPortfolioViews(portfolioValue: PortfolioRetrieveResponse){
        Log.d(TAG, "The portfolio quantity is : ${portfolioValue.quantity}")
        binding.sharesOwned.text = portfolioValue.quantity.toString()


    }

}