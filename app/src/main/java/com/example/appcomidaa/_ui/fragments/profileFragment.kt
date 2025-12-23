package com.example.appcomidaa._ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appcomidaa.R
import com.example.appcomidaa.databinding.FragmentProfileBinding
import com.example.appcomidaa.repository.SettingsRepository
import com.example.appcomidaa.viewmodel.ProfileViewModel
import com.example.appcomidaa.viewmodel.ProfileViewModelFactory
import kotlinx.coroutines.launch

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(SettingsRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        observeViewModel()
    }

    private fun initListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->

            viewModel.setDarkMode(isChecked)
        }
        binding.switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setBluetooth(isChecked)
        }
        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setVibration(isChecked)
        }
        binding.rsVolume.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                viewModel.setVolume(value)
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.darkMode.collect { isEnabled ->
                        binding.switchDarkMode.isChecked = isEnabled
                    }
                }
                launch {
                    viewModel.bluetooth.collect { isEnabled ->
                        binding.switchBluetooth.isChecked = isEnabled
                    }
                }
                launch {
                    viewModel.vibration.collect { isEnabled ->
                        binding.switchVibration.isChecked = isEnabled
                    }
                }
                launch {
                    viewModel.volume.collect { value ->
                        binding.rsVolume.setValues(value)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}