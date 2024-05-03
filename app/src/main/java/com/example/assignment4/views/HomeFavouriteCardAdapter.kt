package com.example.assignment4.views

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.R
import com.example.assignment4.models.HomeFavouriteCardData
import com.example.assignment4.services.util.ResponseCallback
import okhttp3.ResponseBody


class HomeFavouriteCardAdapter(private var itemList: MutableList<HomeFavouriteCardData>) : RecyclerView.Adapter<HomeFavouriteCardAdapter.YourViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourite_card_home_layout, parent, false)
        return YourViewHolder(view)
    }

    override fun onBindViewHolder(holder: YourViewHolder, position: Int) {
        holder.bind(itemList[position])
        // Bind data to the views in your ViewHolder
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    fun updateFavouriteValues(items: MutableList<HomeFavouriteCardData>){
        this.itemList = items
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class YourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder implementation
        val stockName = itemView.findViewById<TextView>(R.id.fav_stock_name)
        val stockSymbol = itemView.findViewById<TextView>(R.id.fav_stock_symbol)
        val price = itemView.findViewById<TextView>(R.id.fav_stock_price)
        val changeValue = itemView.findViewById<TextView>(R.id.fav_stock_change)
        val trendingImage = itemView.findViewById<ImageView>(R.id.trending_image)
        fun bind(cardData: HomeFavouriteCardData) {

            stockName.text = cardData.name
            stockSymbol.text = cardData.symbol
            price.text = "$${cardData.price}"
            if(cardData.change>0){
                trendingImage.setImageResource(R.drawable.trending_up)
                changeValue.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                changeValue.setTextColor(Color.parseColor("#00FF00"))
            }else{
                trendingImage.setImageResource(R.drawable.trending_up)
                changeValue.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                changeValue.setTextColor(Color.parseColor("#ff0000"))
            }
            changeValue.text = "${cardData.change}(${cardData.changePercent}%)"
            // Bind data to views
            itemView.setOnClickListener {
                // Handle click event, navigate to another fragment
                val action = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment(cardData.symbol, true)
                itemView.findNavController().navigate(action)
            }
        }
    }
}