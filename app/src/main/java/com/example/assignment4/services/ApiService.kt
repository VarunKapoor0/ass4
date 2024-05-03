package com.example.assignment4.services

import com.example.assignment4.models.FavouriteDataResponse
import com.example.assignment4.models.PortfolioBuyRequest
import com.example.assignment4.models.PortfolioRetrieveResponse
import com.example.assignment4.models.SearchResponse
import com.example.assignment4.models.UpdateFavouriteRequest
import com.example.assignment4.models.WalletResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("search/{stock}")
    fun getStockDetails(@Path("stock") stock: String): Call<SearchResponse>

    @GET("wallet/retrieve")
    fun getWalletBalance(): Call<WalletResponse>

    @GET("portfolio/retrieve")
    fun getPortfolio(): Call<List<PortfolioRetrieveResponse>>

    @POST("portfolio/update")
    fun buyPortfolioUpdate(@Body request: PortfolioBuyRequest): Call<ResponseBody>

    @POST("portfolio/sell")
    fun sellPortfolioUpdate(@Body request: PortfolioBuyRequest): Call<ResponseBody>

    @POST("watchlist/update")
    fun updateFavourites(@Body request: UpdateFavouriteRequest): Call<ResponseBody>

    @GET("watchlist/retrieve")
    fun getFavourites(): Call<List<FavouriteDataResponse>>

    @POST("watchlist/delete")
    fun deleteFavourite(@Body symbol: String): Call<ResponseBody>

}