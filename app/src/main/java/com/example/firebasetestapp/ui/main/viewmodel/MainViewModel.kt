package com.example.firebasetestapp.ui.main.viewmodel

import android.app.Activity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasetestapp.common.MutableEventFlow
import com.example.firebasetestapp.common.asEventFlow
import com.example.firebasetestapp.ui.barcode.MLBarcodeScannerActivity
import com.example.firebasetestapp.ui.remoteconfig.RemoteConfigActivity
import kotlinx.coroutines.launch

class MainViewModel : ViewModel()  {


    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()

    fun showToast() {
        event(Event.ShowToast("토스트"))
    }

    fun startBarcodeActivity() {
        event(Event.Start(MLBarcodeScannerActivity::class.java))
    }

    fun startRemoteConfigActivity() {
        event(Event.Start(RemoteConfigActivity::class.java))
    }



    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class ShowToast(val text: String) : Event()
        data class Start(val toActivity: Class<out Activity>) : Event()
    }

}