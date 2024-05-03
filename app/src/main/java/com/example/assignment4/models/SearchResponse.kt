package com.example.assignment4.models

data class SearchResponse(
    val name: String,
    val ticker: String,
    val exchange: String,
    val weburl: String,
    val ipo: String,
    val logo: String,
    val peers: List<String>,
                          val news: List<News>,
                          val high_price: Double,
                          val low_price: Double,
                          val open_price: Double,
                          val previous_close: Double,
                          val timestamp: Int,
                          val current_price: Double,
                          val change: Double,
                          val change_percentage: Double,
                          val insider_sentiment: InsiderSentiment)
