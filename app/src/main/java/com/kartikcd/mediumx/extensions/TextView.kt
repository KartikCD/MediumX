package com.kartikcd.mediumx.extensions

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

val appDateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

var TextView.timeStamp: String
    set(value) {
        val date = isoDateFormat.parse(value)
        text = appDateFormat.format(date)
    }
    get() {
        val date = appDateFormat.parse(text.toString())
        return isoDateFormat.format(date)
    }