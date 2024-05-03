package com.example.assignment4.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeFragmentViewModel(application: Application): AndroidViewModel(application) {


    private val _balance = MutableLiveData<String>()
    fun balance(): LiveData<String> {
        return _balance
    }

}