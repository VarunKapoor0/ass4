package com.example.assignment4.views

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment4.R
import com.example.assignment4.models.News
import com.example.assignment4.util.RoundedCornersTransformation
import com.example.assignment4.views.dialog.NewsCustomDialog
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale
import kotlin.math.abs

class NewsAdapter(private var newsList: List<News>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val TAG = "NewsAdapter"
    val limit = 19

    inner class NewsViewHolder(view: View): RecyclerView.ViewHolder(view){
        var newsImage: ImageView = view.findViewById(R.id.news_image)
        var source: TextView = view.findViewById(R.id.news_source)
        var headline: TextView = view.findViewById(R.id.news_headline)
        var newsCard: CardView = view.findViewById(R.id.news_card)
        val hours: TextView = view.findViewById(R.id.hours_ago)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(newsItem: News){
            val epochTime: Long = newsItem.datetime.toLong() * 1000
            val instant: Instant = Instant.ofEpochMilli(epochTime)
            val dateTime: LocalDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)

            val currentTime: LocalDateTime = LocalDateTime.now()

            val duration: Duration = Duration.between(dateTime, currentTime)
            val hoursDifference: Long = abs(duration.toHours())
            hours.text = "$hoursDifference hours ago"

            Picasso.get().load(newsItem.image).transform(RoundedCornersTransformation(16f)).into(newsImage)
            source.text = newsItem.source
            headline.text = newsItem.headline

            newsCard.setOnClickListener {
                //dialog box
                val dialog = NewsCustomDialog(itemView.context, newsItem)
                dialog.show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_news_card, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if(newsList.size>limit){
            limit
        }else{
            newsList.size
        }
    }
    fun updateNews(newsItems: List<News>){
        this.newsList = newsItems
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.bind(newsItem)
//        Picasso.get().load(newsItem.image).into(holder.newsImage)
//
//        holder.source.text = newsItem.source
//        holder.headline.text = newsItem.headline
    }


}