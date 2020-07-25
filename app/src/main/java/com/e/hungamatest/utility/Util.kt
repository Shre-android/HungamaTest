package com.e.hungamatest.utility

fun buildImageURL(path: String?): String {
    return "https://image.tmdb.org/t/p/w500${path}"
}

fun getYear(date:String?): String {
    return if(!date.isNullOrBlank()){
        date.substring(0,4)
    }else{
        ""
    }
}

fun getHrMin(min:Int): String {
    return if(min!=0)
        "${min/60}H:${min%60}M"
    else
        "00H:00M"
}
