package com.example.assignment4.views.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.assignment4.R
import com.example.assignment4.models.News
import com.example.assignment4.models.PortfolioBuyRequest
import com.example.assignment4.models.PortfolioRetrieveResponse
import com.example.assignment4.models.SearchResponse
import com.example.assignment4.models.WalletResponse
import com.example.assignment4.services.repositories.NetworkRepository
import com.example.assignment4.services.util.ResponseCallback
import com.example.assignment4.services.util.RetrofitClient
import com.google.android.material.button.MaterialButton
import okhttp3.ResponseBody

class TradeCustomDialog(context: Context, private val searchResponse: SearchResponse) : Dialog(context) {
    private lateinit var networkRepository: NetworkRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_trade_layout)
        val retrofitClient = RetrofitClient.instance
        networkRepository = NetworkRepository(retrofitClient)
        val tradeHeader = findViewById<TextView>(R.id.trade_header)
        val shares = findViewById<EditText>(R.id.trade_user_input)
        val buyButton = findViewById<MaterialButton>(R.id.buy_button)
        val sellButton = findViewById<MaterialButton>(R.id.sell)
        tradeHeader.text = "Trade ${searchResponse.name} shares"
        buyButton.setOnClickListener {
            Log.d("TradeDialog", "The edittext value is : ${shares.text}")
            if(shares.text.trim().isEmpty() || shares.text.trim()== "0"){
                Toast.makeText(context, "Please enter a valid amount" , Toast.LENGTH_SHORT ).show()
            }
            else{
                val confirmDialog = ConfirmDialog(context, shares.text.toString(), searchResponse.name, true)
                buyWalletBalanceCall(shares.text.toString())
                confirmDialog.show()
                this.dismiss()


            }
        }

        sellButton.setOnClickListener {
            Log.d("TradeDialog", "The edittext value is : ${shares.text}")
            if(shares.text.trim().isEmpty() || shares.text.trim()== "0"){
                Toast.makeText(context, "Please enter a valid amount" , Toast.LENGTH_SHORT ).show()
            }
            else{
                val confirmDialog = ConfirmDialog(context, shares.text.toString(), searchResponse.name, false)
                sellWalletBalanceCall(shares.text.toString())
                confirmDialog.show()
                this.dismiss()

            }
        }
    }

    private fun buyWalletBalanceCall(shares: String){
        networkRepository.getWalletBalanceCall(object: ResponseCallback<WalletResponse> {
            override fun onSuccess(result: WalletResponse) {
                Log.d("Dialog", "The result for wallet is : $result")
                val portfolioBuyRequest = PortfolioBuyRequest(searchResponse.name, searchResponse.ticker, shares.toInt(), searchResponse.current_price, searchResponse.current_price,
                    searchResponse.change, (shares.toIntOrNull()!! * searchResponse.current_price).toDouble(),
                    (shares.toIntOrNull()!! * searchResponse.current_price).toDouble(),
                    (result.wallet.toDouble() - (shares.toIntOrNull()!! * searchResponse.current_price)))

                networkRepository.postPortfolioBuy(portfolioBuyRequest, object: ResponseCallback<ResponseBody> {
                    override fun onSuccess(result: ResponseBody) {
                        Log.d("Bought", "The portfolio value ")

                    }
                    override fun onError(error: String) {
                        Log.d("Dialog", "The error for search is : $error")
                    }
                })


            }

            override fun onError(error: String) {
                Log.d("Dialog", "The error is : $error")
            }
        })
    }

    private fun sellWalletBalanceCall(shares: String){
        networkRepository.getWalletBalanceCall(object: ResponseCallback<WalletResponse> {
            override fun onSuccess(result: WalletResponse) {
                Log.d("Dialog", "The result for wallet is : $result")
                val portfolioBuyRequest = PortfolioBuyRequest(searchResponse.name, searchResponse.ticker, shares.toInt(), searchResponse.current_price, searchResponse.current_price,
                    searchResponse.change, (shares.toIntOrNull()!! * searchResponse.current_price).toDouble(),
                    (shares.toIntOrNull()!! * searchResponse.current_price).toDouble(),
                    (result.wallet.toDouble() + (shares.toIntOrNull()!! * searchResponse.current_price)))

                networkRepository.postPortfolioSell(portfolioBuyRequest, object: ResponseCallback<ResponseBody> {
                    override fun onSuccess(result: ResponseBody) {
                        Log.d("Bought", "The portfolio value ")

                    }
                    override fun onError(error: String) {
                        Log.d("Dialog", "The error for search is : $error")
                    }
                })


            }

            override fun onError(error: String) {
                Log.d("Dialog", "The error is : $error")
            }
        })
    }

}