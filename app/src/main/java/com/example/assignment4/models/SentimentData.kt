package com.example.assignment4.models

data class SentimentData(val symbol: String,
                         val year: Int,
                         val month: Int,
                         val change: Int,
                         val mspr: Double)
