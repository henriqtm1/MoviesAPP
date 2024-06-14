package com.example.moviesapp.utils

import android.content.Context
import android.widget.Toast

class ToastMessage {

    companion object{
        fun showToastMessage_SHORT(aContext: Context, aMessage: String) {
            Toast.makeText(aContext, aMessage, Toast.LENGTH_SHORT).show()
        }

        fun showToastMessage_LONG(aContext: Context, aMessage: String) {
            Toast.makeText(aContext, aMessage, Toast.LENGTH_LONG).show()
        }
    }

}