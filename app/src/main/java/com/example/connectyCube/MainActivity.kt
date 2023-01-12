package com.example.connectyCube

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()
        listeners()
    }

    private fun listeners() {
        findViewById<Button>(R.id.createChannel).setOnClickListener { onSubmit(1) }
        findViewById<Button>(R.id.joinChannel).setOnClickListener { onSubmit(0) }
    }

    fun onSubmit(i: Int) {
        val channelName = findViewById<EditText>(R.id.channelId)

        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("id", channelName.text.toString())
        intent.putExtra("role", i)
        startActivity(intent)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
            ),
            22
        )
    }
}