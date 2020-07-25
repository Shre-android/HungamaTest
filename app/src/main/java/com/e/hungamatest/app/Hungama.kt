package com.e.hungamatest.app

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class Hungama : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        Fresco.initialize(this)
    }
    companion object{
        lateinit var appContext: Context
    }
}