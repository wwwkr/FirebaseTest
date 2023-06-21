package com.example.firebasetestapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasetestapp.common.repeatOnStarted
import com.example.firebasetestapp.common.start
import com.example.firebasetestapp.databinding.ActivityMainBinding
import com.example.firebasetestapp.ui.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            binding.apply {
                lifecycleOwner = this@MainActivity
                viewModel = this@MainActivity.viewModel
        }.root)


        repeatOnStarted {
            viewModel.eventFlow.collect { event -> handleEvent(event) }
        }
    }

    private fun handleEvent(event: MainViewModel.Event) = when (event) {
        is MainViewModel.Event.ShowToast -> Toast.makeText(this, event.text, Toast.LENGTH_SHORT).show()
        is MainViewModel.Event.Start -> start(event.toActivity)
    }


}