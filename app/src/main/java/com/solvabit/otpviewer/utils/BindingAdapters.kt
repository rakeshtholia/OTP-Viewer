package com.solvabit.otpviewer.utils

import android.graphics.Color
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.otpviewer.R
import com.solvabit.otpviewer.model.Message
import com.solvabit.otpviewer.ui.allMessages.AllMessagesAdapter
import com.solvabit.otpviewer.ui.home.HomeAdapter
import java.awt.font.NumericShaper
import java.util.*

private const val TAG = "BindingAdapters"

@BindingAdapter("bindAllSenders")
fun bindAllSenders(recyclerView: RecyclerView, data: List<Message>?) {
    val adapter = recyclerView.adapter as HomeAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindAllMessages")
fun bindAllMessages(recyclerView: RecyclerView, data: List<Message>?) {
    val adapter = recyclerView.adapter as AllMessagesAdapter
    adapter.submitList(data)
}

@BindingAdapter("bindSenderImage")
fun bindSenderImage(imageView: ImageView, message: Message?) {
    message?.let {
        imageView.setImageResource(R.drawable.account_default_icon)
        imageView.setColorFilter(getColor(message.address))
    }
}

fun getColor(string: String): Int {
    Log.i(TAG, "getColor: $string")
    val color = when(string[0].lowercaseChar()) {
        in 'a' .. 'e' -> "#0583EB"
        in 'e' .. 'm' -> "#34CAAD"
        in 'm' .. 'r' -> "#E4420F"
        in 'r' .. 'z' -> "#06BFD1"
        in '0' .. '9' -> "#FE2F74"
        else -> "#4F92AB"
    }
    return Color.parseColor(color)
}

@BindingAdapter("bindTextColor")
fun bindTextColor(textView: TextView, int: Int?) {
    int?.let {
        when(it) {
            0 -> {
                textView.setTextColor(Color.parseColor("#000000"))

            }
            else -> {
                textView.setTextColor(Color.parseColor("#99000000"))

            }
        }
    }
}

@BindingAdapter("bindFormatDate")
fun bindDate(textView: TextView, date: Long?) {
    date?.let {
        val messageDate = Date(date).toString()
        val todayDate = Calendar.getInstance().time.toString()

        if(messageDate.substring(0, 10) == todayDate.substring(0, 10))
            textView.text = messageDate.substring(11, 16)
        else
            textView.text = messageDate.substring(4, 10)


    }
}