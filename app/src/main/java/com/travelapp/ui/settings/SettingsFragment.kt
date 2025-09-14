package com.travelapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.travelapp.databinding.FragmentSettingsBinding
import java.text.SimpleDateFormat
import java.util.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    private fun setupClickListeners() {
        binding.syncNowButton.setOnClickListener {
            settingsViewModel.syncNow()
        }

        binding.autoSyncSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setAutoSync(isChecked)
        }

        binding.locationServicesSwitch.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setLocationServices(isChecked)
        }
    }

    private fun observeViewModel() {
        settingsViewModel.isSignedIn.observe(viewLifecycleOwner) { isSignedIn ->
            if (isSignedIn) {
                binding.syncStatusText.text = getString(com.travelapp.R.string.google_connected)
                binding.syncStatusIcon.setImageResource(com.travelapp.R.drawable.ic_check_circle_24)
            } else {
                binding.syncStatusText.text = getString(com.travelapp.R.string.google_not_connected)
                binding.syncStatusIcon.setImageResource(com.travelapp.R.drawable.ic_error_24)
            }
        }

        settingsViewModel.lastSyncTime.observe(viewLifecycleOwner) { lastSync ->
            val dateFormat = SimpleDateFormat("MMM d, yyyy 'at' h:mm a", Locale.getDefault())
            binding.lastSyncText.text = "Last sync: ${dateFormat.format(lastSync)}"
        }

        settingsViewModel.isSyncing.observe(viewLifecycleOwner) { isSyncing ->
            binding.syncNowButton.isEnabled = !isSyncing
            binding.syncNowButton.text = if (isSyncing) "Syncing..." else getString(com.travelapp.R.string.sync_now)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
