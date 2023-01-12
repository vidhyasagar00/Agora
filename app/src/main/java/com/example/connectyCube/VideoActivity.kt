package com.example.connectyCube

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas

class VideoActivity : AppCompatActivity() {

    private var engine: RtcEngine? = null
    private var channelName: String? = null
    private var role = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        channelName = intent.getStringExtra("id")
        role = intent.getIntExtra("role", -1)

        initAgoraAndJoinChannel()
        listeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        engine!!.leaveChannel()
        RtcEngine.destroy()
        engine = null
    }

    private fun initAgoraAndJoinChannel() {
        initAgora()

        engine!!.setChannelProfile(io.agora.rtc2.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        engine!!.setClientRole(role)

        engine!!.enableVideo()

        if (role == 1)
            setLocalVideo()
        else {
            val localVideoCanvas = findViewById<View>(R.id.localVideoView)
            localVideoCanvas.isVisible = false
        }
        joinChannel()
    }

    private fun listeners() {
        findViewById<ImageView>(R.id.btnMute).setOnClickListener { onLocalAudioMuteClicked(it as ImageView) }
        findViewById<ImageView>(R.id.btnEndCall).setOnClickListener { onEndCallClicked() }
        findViewById<ImageView>(R.id.btnVideoStatus).setOnClickListener { onSwitchCameraView() }
    }

    private fun setLocalVideo() {
        val container = findViewById<View>(R.id.localVideoView) as FrameLayout
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        engine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel() {
        engine!!.joinChannel(Constants.TOKEN, channelName, null, 0)
    }

    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread { setUpRemoteVideo(uid) }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread { println("join channel successfully : $uid") }
        }
    }

    private fun onRemoteUserLeft() {
        val container = findViewById<View>(R.id.remoteVideoView) as FrameLayout
        container.removeAllViews()
    }

    private fun setUpRemoteVideo(uid: Int) {
        val container = findViewById<View>(R.id.remoteVideoView) as FrameLayout

        if (container.childCount >= 1)
            return

        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        engine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
        surfaceView.tag = uid
    }

    private fun onLocalAudioMuteClicked(view: ImageView) {
        if (view.isSelected) {
            view.isSelected = false
            view.setImageResource(R.drawable.ic_volume_up)
        } else {
            view.isSelected = true
            view.setImageResource(R.drawable.ic_volume_off)
        }

        engine!!.muteLocalAudioStream(view.isSelected)
    }

    private fun onSwitchCameraView() {
        engine!!.switchCamera()
    }

    private fun onEndCallClicked() {
        finish()
    }

    private fun initAgora() {
        try {
            engine = RtcEngine.create(baseContext, Constants.APP_ID, mRtcEventHandler)
        } catch (e: java.lang.Exception) {
            println(e.message)
        }
    }
}