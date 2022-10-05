package com.solvabit.otpviewer.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.solvabit.otpviewer.R
import com.solvabit.otpviewer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.homeToolbar)
        binding.homeToolbar.title = "Messages"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        setHasOptionsMenu(true)

        val contentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            null, null, null, null
        )
        val homeViewModelFactory = HomeViewModelFactory(requireContext(), cursor!!)
        viewModel = ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.senderRecyclerView.adapter = HomeAdapter(HomeAdapterListener {
            this.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAllMessagesFragment(
                    viewModel.getList(it.address)
                )
            )
        })

        createChannel(
            getString(R.string.message_notification_channel_id),
            getString(R.string.message_notification_channel_name)
        )

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_filter_menu, menu)

        val searchItem = menu.findItem(R.id.app_bar_search_message)
        val searchView = searchItem.actionView as SearchView
        initializeSearch(searchView)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "This gives notifications for OTP"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun initializeSearch(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchQuery(newText)
                }
                return false
            }

        })
    }
}