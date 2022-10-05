package com.solvabit.otpviewer.utils

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.solvabit.otpviewer.MainActivity
import com.solvabit.otpviewer.R
import com.solvabit.otpviewer.model.Message

// Notification ID.
private const val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

private const val TAG = "NotificationUtils"


fun NotificationManager.sendNotification(message: Message, applicationContext: Context) {

    val bundle = Bundle()
    bundle.putParcelable("message", message)
    val contentIntent = NavDeepLinkBuilder(applicationContext)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.allMessagesFragment)
        .setArguments(bundle)
        .createPendingIntent()

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.message_notification_channel_id)
    )

    val notificationLayout: RemoteViews = when (message.otp.containsOTP) {
        true -> {
            Log.i(TAG, "sendNotification: true - $message")
            val tempLayout = RemoteViews("com.solvabit.otpviewer", R.layout.notification_otp_found)
            tempLayout.setTextViewText(R.id.small_notification_otp_text,  message.otp.otpCode.toString())
            builder.priority = NotificationCompat.PRIORITY_HIGH
            tempLayout
        }
        else -> {
            Log.i(TAG, "sendNotification: false - $message")
            val tempLayout =
                RemoteViews("com.solvabit.otpviewer", R.layout.notification_otp_found)
            tempLayout.setTextViewText(
                R.id.small_notification_otp_text,
                "Unsafe Percentage: " + message.address + "%"
            )
            builder.priority = NotificationCompat.PRIORITY_HIGH
            tempLayout
        }

    }

    builder
        .setSmallIcon(R.drawable.account_default_icon)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(notificationLayout)
        .setContentIntent(contentIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}


fun NotificationManager.cancelNotifications() {
    cancelAll()
}