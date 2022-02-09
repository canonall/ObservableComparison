package com.canonal.observablecomparison

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.canonal.observablecomparison.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.btnLiveData.setOnClickListener {
            mainViewModel.triggerLiveData()
        }

        binding.btnStateFlow.setOnClickListener {
            mainViewModel.triggerStateFlow()
        }

        binding.btnFlow.setOnClickListener {
            //we dont do it inside subscribeToObservables because we want to trigger it
            //when we actually click the button
            lifecycleScope.launch {
                mainViewModel.triggerFlow().collectLatest {
                    binding.tvFlow.text = it
                }
            }
        }

        binding.btnSharedFlow.setOnClickListener {
            mainViewModel.triggerSharedFlow()
        }

        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        mainViewModel.liveData.observe(this) {
            binding.tvLiveData.text = it
        }
        //use "launchWhenStarted" insted of "launch" with StateFlow
        lifecycleScope.launchWhenStarted {
            mainViewModel.stateFlow.collectLatest {
                binding.tvStateFlow.text = it
//                Snackbar.make(
//                    binding.root,
//                    it,
//                    Snackbar.LENGTH_LONG
//                ).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.sharedFlow.collectLatest {
                binding.tvSharedFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}