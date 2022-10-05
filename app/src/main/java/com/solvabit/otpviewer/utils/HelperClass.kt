package com.solvabit.otpviewer.utils

import android.util.Log
import com.solvabit.otpviewer.model.Message
import java.util.regex.Pattern

private const val TAG = "HelperClass"

fun findOTPCode(message: Message): String {

    val pattern = Pattern.compile("(\\b\\d{6}\\b|\\b\\d{5}\\b|\\b\\d{4}\\b)")
    val matcher = pattern.matcher(message.body)
    return when(matcher.find()) {
        true -> matcher.group(0)
        false -> "NA"
    }
}