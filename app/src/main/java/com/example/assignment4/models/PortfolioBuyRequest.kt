package com.example.assignment4.models

data class PortfolioBuyRequest(val name: String, val symbol: String, val quantity: Int, val averagePrice: Double,
val current_price: Double, val change: Double, val marketValue: Double, val totalValue: Double, val walletBalance: Double)
