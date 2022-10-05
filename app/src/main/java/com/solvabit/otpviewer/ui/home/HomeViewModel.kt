package com.solvabit.otpviewer.ui.home

import android.content.Context
import android.database.Cursor
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.solvabit.otpviewer.model.Message
import com.solvabit.otpviewer.model.OTP
import java.lang.IllegalArgumentException

private const val TAG = "HomeViewModel"

class HomeViewModel(private val context: Context, private val cursor: Cursor) : ViewModel() {

    private val mutableMsgList = mutableListOf<Message>()

    private val _allMessages = MutableLiveData<List<Message>>()
    private val _msgList = MutableLiveData<List<Message>>()
    val msgList: LiveData<List<Message>>
        get() = _msgList

    init {
        readSms()
    }

    private fun readSms() {
        while (cursor.moveToNext()) {
            val msg = Message().apply {
                _id = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
                address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                person = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.PERSON))
                date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                date_sent = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE_SENT))
                read = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.READ))
                seen = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.SEEN))
                replyPathPresent =
                    cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.REPLY_PATH_PRESENT))
                subject = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SUBJECT))
                body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                serviceCenter =
                    cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.SERVICE_CENTER))
                creator = -1
                type = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Sms.SUBSCRIPTION_ID))
            }
            mutableMsgList.add(msg)
        }
        _allMessages.value = mutableMsgList
        _msgList.value = mutableMsgList.distinctBy {
            it.address
        }
        cursor.close()
    }

    fun getList(address: String): Array<Message> {
        return _allMessages.value?.filter {
            it.address == address
        }?.toTypedArray() ?: arrayOf()
    }

    fun searchQuery(query: String) {
        val copyList = _allMessages.value
        _msgList.value = copyList?.filter {
            it.address.contains(query, true) or it.body.contains(query, true)
        }
        if(query.isEmpty())
            _msgList.value = _allMessages.value?.distinctBy { it.address }
    }

}

class HomeViewModelFactory(private val context: Context, private val cursor: Cursor): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(context, cursor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}
