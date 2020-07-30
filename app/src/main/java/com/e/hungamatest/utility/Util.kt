package com.e.hungamatest.utility

import android.app.ProgressDialog
import android.view.View

//Method to build the image url
fun buildImageURL(path: String?): String {
    return "https://image.tmdb.org/t/p/w500${path}"
}

// Getting year from date string
fun getYear(date:String?): String {
    return if(!date.isNullOrBlank()){
        date.substring(0,4)
    }else{
        ""
    }
}

//Getting hour of the movie
fun getHrMin(min:Int): String {
    return if(min!=0)
        "${min/60}H:${min%60}M"
    else
        "00H:00M"
}

