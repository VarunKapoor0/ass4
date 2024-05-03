package com.example.assignment4.services.util

interface ResponseCallback<T> {

    fun onSuccess(result: T)
    fun onError(error: String)
}