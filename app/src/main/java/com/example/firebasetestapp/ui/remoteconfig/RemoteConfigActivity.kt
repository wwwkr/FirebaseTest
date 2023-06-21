package com.example.firebasetestapp.ui.remoteconfig

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.example.firebasetestapp.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfigActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_config)


        remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }

        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.setDefaultsAsync(mapOf(
            REMOTE_CONFIG_VALUE1 to "default_value"
        ))


        findViewById<Button>(R.id.btn_show).setOnClickListener {
            fetchAppVersion()
        }


    }


    private fun fetchAppVersion() {
        // val appVersion = remoteConfig[REMOTE_KEY_APP_VERSION].asString()


        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val appVersion = remoteConfig.getString(REMOTE_CONFIG_VALUE1)
                    
                    AlertDialog.Builder(this)
                        .setTitle("Remote Config")
                        .setMessage("App version :: $appVersion")
                        .show()

                    // fetch and activate 성공
                } else {
                    // fetch and activate 실패
                }
            }
    }

    companion object {
        private const val REMOTE_CONFIG_VALUE1 = "remote_config_value1"
    }
}