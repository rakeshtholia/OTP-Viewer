package com.solvabit.otpviewer.ui.allMessages

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.solvabit.otpviewer.databinding.FragmentAllMessagesBinding


class AllMessagesFragment : Fragment() {

    private lateinit var binding: FragmentAllMessagesBinding
    private lateinit var viewModel: AllMessagesViewModel
    private val args: AllMessagesFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = args.messages[0].address
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllMessagesBinding.inflate(inflater)

        val messagesViewModelFactory =
            AllMessagesViewModelFactory(args.messages.toList(), requireContext())
        viewModel =
            ViewModelProvider(this, messagesViewModelFactory)[AllMessagesViewModel::class.java]
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.allMessagesRecyclerView.adapter = AllMessagesAdapter(OTPClickListener {
            val clipboard: ClipboardManager =
                activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", it.otp.otpCode.toString())
            clipboard.setPrimaryClip(clip)
            Snackbar.make(binding.root, "Code copied - ${it.otp.otpCode}", Snackbar.LENGTH_SHORT)
                .show()
        })

        binding.allMessagesBackButton.setOnClickListener {
            this.findNavController().popBackStack()
        }

        return binding.root
    }

}