package com.example.zolpe_05

import android.app.Application

class App:Application() {
    companion object {
        lateinit var user:String
        const val botUser = "bot"
    }
}