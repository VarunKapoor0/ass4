package com.example.assignment4.views

import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.R
import com.example.assignment4.models.HomePortfolioCardData
import com.example.assignment4.models.News
import org.w3c.dom.Text

class PortfolioCardAdapter(private var dataList: List<HomePortfolioCardData>) : RecyclerView.Adapter<PortfolioCardAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val stockName = itemView.findViewById<TextView>(R.id.home_stock_name)
        val sharesOwned = itemView.findViewById<TextView>(R.id.home_shares_number)
        val totalValue = itemView.findViewById<TextView>(R.id.total_value)
        val changeValue = itemView.findViewById<TextView>(R.id.change_value)
        val imageTrend: ImageView = itemView.findViewById(R.id.trending_image_port)

        fun bind(cardData: HomePortfolioCardData) {

            stockName.text = cardData.name
            sharesOwned.text = cardData.quantity.toString()+" shares"
            totalValue.text = "$${cardData.totalValue.toString()}"
            if(cardData.change>0){
                imageTrend.setImageResource(R.drawable.trending_up)
                changeValue.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                changeValue.setTextColor(Color.parseColor("#00FF00"))
            }else{
                imageTrend.setImageResource(R.drawable.trending_up)
                changeValue.gravity = Gravity.END or Gravity.CENTER_VERTICAL
                changeValue.setTextColor(Color.parseColor("#ff0000"))
            }
            changeValue.text = "${cardData.change}%"
            // Bind data to views
            itemView.setOnClickListener {
                // Handle click event, navigate to another fragment
                val action = HomeFragmentDirections.actionHomeFragmentToSearchResultsFragment(cardData.symbol, false)
                itemView.findNavController().navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.portfolio_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    fun updatePortfolioValues(items: List<HomePortfolioCardData>){
        this.dataList = items
        notifyDataSetChanged()
        //Log.d(TAG, "The adapter with the new data size is : ${newsItems.size}")
    }

}