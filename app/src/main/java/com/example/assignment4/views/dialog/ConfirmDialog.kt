package com.example.assignment4.views.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.assignment4.R
import com.example.assignment4.models.SearchResponse
import com.google.android.material.button.MaterialButton

class ConfirmDialog(context: Context, private val shares: String, private val name: String, private val isBought: Boolean) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_confirm_layout)

        val done = findViewById<MaterialButton>(R.id.done_button)
        val confirmText = findViewById<TextView>(R.id.confirm_text)
        if(isBought){
            confirmText.text = "You have successfully bought $shares shares of $name"
        }else {
            confirmText.text = "You have successfully sold $shares shares of $name"

        }
        done.setOnClickListener {
            dismiss()
        }



    }

}