package com.example.assignment4.views.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.example.assignment4.R
import com.example.assignment4.models.News
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsCustomDialog(context: Context, private val newsItem: News) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_news_layout)

        val source = findViewById<TextView>(R.id.source)
        val date: TextView = findViewById(R.id.news_date)
        val headlineTextView = findViewById<TextView>(R.id.headlines)
        val description = findViewById<TextView>(R.id.description)
        val fb = findViewById<ImageView>(R.id.facebookIcon)
        val twitter = findViewById<ImageView>(R.id.twitterIcon)
        val chrome = findViewById<ImageView>(R.id.chromeIcon)

        chrome.setOnClickListener {
            openChrome()
        }




        fb.setOnClickListener {
            shareToFacebook()
        }
        twitter.setOnClickListener {
            shareToTwitter()
        }


        //val summaryTextView = findViewById<TextView>(R.id.summa)
        date.text = convertToDate(newsItem.datetime.toLong())
        source.text = newsItem.source
        headlineTextView.text = newsItem.headline
        description.text = newsItem.summary
    }
    fun convertToDate(timeStamp: Long): String{
        val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = Date(timeStamp * 1000)
        return sdf.format(date)



    }

    private fun openChrome(){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.url))
        intent.setPackage("com.android.chrome")
        context.startActivity(intent)
    }
    private fun shareToTwitter() {
        val url = "https://twitter.com/intent/tweet?url=${newsItem.url}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
    private fun shareToFacebook() {
        val url = "https://www.facebook.com/sharer/sharer.php?u=${newsItem.url}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}