package com.solvabit.otpviewer.ui.allMessages

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solvabit.otpviewer.model.Message
import com.solvabit.otpviewer.utils.findOTPCode
import java.lang.IllegalArgumentException

private const val TAG = "AllMessagesViewModel"

class AllMessagesViewModel(private val messages: List<Message>, private var requireContext: Context) :
    ViewModel() {

    private val _messagesList = MutableLiveData<List<Message>>()
    val messagesList: LiveData<List<Message>>
        get() = _messagesList

    init {
        checkForOTP()
    }

    private fun checkForOTP() {
        val tempMessages = messages.reversed()
        tempMessages.forEach {
            when (val otpCode = findOTPCode(it)) {
                "NA" -> Log.i(TAG, "checkForOTP: No otp available")
                else -> {
                    it.otp.containsOTP = true
                    it.otp.otpCode = otpCode.toInt()
                }
            }
        }
        _messagesList.value = tempMessages
    }

}

class AllMessagesViewModelFactory(
    private val messages: List<Message>,
    private val requireContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllMessagesViewModel::class.java))
            return AllMessagesViewModel(messages, requireContext) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
