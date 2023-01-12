package com.example.connectyCube

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class VideoCallHome : AppCompatActivity() {

    private var url = "https:///"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_call_home)
        val queue = Volley.newRequestQueue(applicationContext)

        val channelName: String? = "null"
        val role: String = io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER.toString()
        val tokenType: String? = "null"
        val uid: String? = "0"
        val expireTime = "60000"
        url += "rtc/$channelName/$role/$tokenType/$uid/?expiry=$expireTime"

        findViewById<Button>(R.id.genToken).setOnClickListener {
            val request = StringRequest(
                Request.Method.GET,
                url,
                {
                    Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                },
                {
                    Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
                }
            )
            queue.add(request)
        }
    }
}