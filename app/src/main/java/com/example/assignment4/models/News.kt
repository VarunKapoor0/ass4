package com.example.assignment4.models

data class News(val category: String,
                val datetime: Int,
                val headline: String,
                val id: Int,
                val image: String,
                val related: String,
                val source: String,
                val summary: String,
                val url: String)
