package com.solvabit.otpviewer.ui.allMessages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.solvabit.otpviewer.databinding.MessageCardBinding
import com.solvabit.otpviewer.model.Message
import com.solvabit.otpviewer.ui.home.HomeDiffCallback

class AllMessagesAdapter(private val OTPClickListener: OTPClickListener):
    ListAdapter<Message, AllMessagesAdapter.ViewHolder>(HomeDiffCallback()){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllMessagesAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AllMessagesAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position), OTPClickListener)
    }

    class ViewHolder private constructor(private val binding: MessageCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message, OTPClickListener: OTPClickListener) {
            binding.message = item
            binding.clickListener = OTPClickListener
            binding.executePendingBindings()
        }
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MessageCardBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

}

class OTPClickListener(val clickListener: (message: Message) -> Unit) {
    fun onClick(message: Message) = clickListener(message)
}