package com.solvabit.otpviewer.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.solvabit.otpviewer.model.Message

private const val TAG = "MessageReceiver"

class MessageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent!!.action) {
            for (smsMessage in Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                val notificationMessage = Message().apply {
                    body = smsMessage.messageBody
                    address = smsMessage.originatingAddress.toString()
                    _id = smsMessage.indexOnIcc
                }

                Log.i(TAG, "onReceive: $notificationMessage")

                val notificationManager = ContextCompat.getSystemService(
                    context,
                    NotificationManager::class.java
                ) as NotificationManager

                notificationManager.sendNotification(
                    checkForOTP(notificationMessage),
                    context
                )

            }
        }
    }

    private fun checkForOTP(message: Message): Message {
        when (val otpCode = findOTPCode(message)) {
            "NA" -> Log.i(TAG, "checkForOTP: No otp available")
            else -> {
                message.otp.containsOTP = true
                message.otp.otpCode = otpCode.toInt()
            }
        }
        return message
    }

    fun sendNotification(context: Context?) {
        Toast.makeText(context, "API Hit", Toast.LENGTH_LONG).show()
    }

}