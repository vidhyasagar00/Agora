package com.example.connectyCube

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.agora.agorauikit_android.AgoraButton
import io.agora.agorauikit_android.AgoraConnectionData
import io.agora.agorauikit_android.AgoraSettings
import io.agora.agorauikit_android.AgoraVideoViewer

class DefaultVideoActivity : AppCompatActivity() {
    @OptIn(ExperimentalUnsignedTypes::class)
    var agoraVideoView: AgoraVideoViewer? = null

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_video)

//        agoraVideoView = AgoraVideoViewer(this, AgoraConnectionData(Constants.APP_ID, Constants.TOKEN))

//        customize default ui
        agoraVideoView = AgoraVideoViewer(this, AgoraConnectionData(Constants.APP_ID, Constants.TOKEN), agoraSettings = this.agoraSettings())

        val v = findViewById<FrameLayout>(R.id.frameLayout)
        v.addView(agoraVideoView)
        agoraVideoView!!.join("demo", role = io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER)


        // styles : GRID, FLOATING and COLLECTION
        agoraVideoView!!.style = AgoraVideoViewer.Style.GRID

        // effects
        agoraVideoView!!.agkit.setBeautyEffectOptions(true, this.agoraVideoView!!.beautyOptions)

    }

    private fun agoraSettings(): AgoraSettings {
        val settings = AgoraSettings()

        settings.colors.buttonBackgroundColor = Color.BLUE
        settings.colors.buttonBackgroundAlpha = Color.TRANSPARENT
        settings.colors.micFlag = Color.RED


//        add some default buttons
        settings.enabledButtons = mutableSetOf(
            AgoraSettings.BuiltinButton.CAMERA,
            AgoraSettings.BuiltinButton.END,
            AgoraSettings.BuiltinButton.MIC,
        )
//        add extra buttons
        val extraButton = AgoraButton(this)
        extraButton.clickAction = {
            Toast.makeText(this, "working", Toast.LENGTH_SHORT).show()

        }
        extraButton.setImageResource(R.drawable.ic_videocam)
        settings.extraButtons = mutableListOf(extraButton)

        return settings
    }
}