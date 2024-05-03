package com.example.assignment4.services.repositories

import com.example.assignment4.models.PortfolioBuyRequest
import com.example.assignment4.models.FavouriteDataResponse
import com.example.assignment4.models.PortfolioRetrieveResponse
import com.example.assignment4.models.SearchResponse
import com.example.assignment4.models.UpdateFavouriteRequest
import com.example.assignment4.models.WalletResponse
import com.example.assignment4.services.ApiService
import com.example.assignment4.services.util.ResponseCallback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkRepository(val apiService: ApiService) {

    fun getStockDetailsCall(stock: String, callback: ResponseCallback<SearchResponse>){
        apiService.getStockDetails(stock).enqueue(object: Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callback.onSuccess(it.copy())
                    } ?: callback.onError("Nothing in json. ")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
            }
        })
    }

    fun getWalletBalanceCall(callback: ResponseCallback<WalletResponse>){
        apiService.getWalletBalance().enqueue(object: Callback<WalletResponse>{
            override fun onResponse(
                call: Call<WalletResponse>,
                response: Response<WalletResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callback.onSuccess(it.copy())
                    } ?: callback.onError("Nothing in json. ")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<WalletResponse>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
                TODO("Not yet implemented")
            }
        })

    }

    fun getPortfolio(callback: ResponseCallback<List<PortfolioRetrieveResponse>>){
        apiService.getPortfolio().enqueue(object: Callback<List<PortfolioRetrieveResponse>>{
            private lateinit var portfolioData: List<PortfolioRetrieveResponse>
            override fun onResponse(
                call: Call<List<PortfolioRetrieveResponse>>,
                response: Response<List<PortfolioRetrieveResponse>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        portfolioData = it.map { it }
                        callback.onSuccess(portfolioData)
                    } ?: callback.onError("Nothing in json. ")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<List<PortfolioRetrieveResponse>>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
                TODO("Not yet implemented")
            }
        })
    }
    fun postPortfolioBuy(stockData: PortfolioBuyRequest, callback: ResponseCallback<ResponseBody>) {
        apiService.buyPortfolioUpdate(stockData).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Nothing in JSON.")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
            }
        })
    }

    fun postPortfolioSell(stockData: PortfolioBuyRequest, callback: ResponseCallback<ResponseBody>) {
        apiService.sellPortfolioUpdate(stockData).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Nothing in JSON.")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
            }
        })
    }

    fun postUpdateFavourite(favouriteRequest: UpdateFavouriteRequest, callback: ResponseCallback<ResponseBody>) {
        apiService.updateFavourites(favouriteRequest).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Nothing in JSON.")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
            }
        })
    }

    fun postDeleteFavourite(symbol: String, callback: ResponseCallback<ResponseBody>) {
        apiService.deleteFavourite(symbol).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Nothing in JSON.")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
            }
        })
    }

    fun getFavourites(callback: ResponseCallback<List<FavouriteDataResponse>>){
        apiService.getFavourites().enqueue(object: Callback<List<FavouriteDataResponse>>{
            private lateinit var favouriteData: List<FavouriteDataResponse>
            override fun onResponse(
                call: Call<List<FavouriteDataResponse>>,
                response: Response<List<FavouriteDataResponse>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        favouriteData = it.map { it }
                        callback.onSuccess(favouriteData)
                    } ?: callback.onError("Nothing in json. ")
                } else{
                    callback.onError(response.errorBody()?.string() ?: "Unknown error.")
                }
            }

            override fun onFailure(call: Call<List<FavouriteDataResponse>>, t: Throwable) {
                callback.onError(t.message ?: "Network error.")
                TODO("Not yet implemented")
            }
        })
    }
}