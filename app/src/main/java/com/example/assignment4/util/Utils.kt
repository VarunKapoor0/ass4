package com.example.assignment4.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    fun currentDate(): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date());
    }
}