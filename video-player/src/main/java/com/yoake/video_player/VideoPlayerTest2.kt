package com.yoake.video_player

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

import com.yoake.video_player.controller.StandardVideoController
import com.yoake.video_player.util.PIPManager
import com.yoake.video_player.util.VideoInfo
import xyz.doikki.videoplayer.player.VideoViewManager


internal class VideoPlayerTest2 : AppCompatActivity() {
    private lateinit var mPIPManager: PIPManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.v_player_test2)
        val playerContainer = findViewById<FrameLayout>(R.id.playerContainer)
        mPIPManager = PIPManager.getInstance(this)
        mPIPManager.bindLifecycleOwner(this)
        val player: VideoPlayer = VideoViewManager.instance().get(PIPManager.PIP) as VideoPlayer
        player.autoPlay = true
        player.openCache = false
        val videoInfo = VideoInfo(
            "测试",
            "https://img2.baidu.com/it/u=4256758710,3144398338&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500"
        )
        // videoInfo.lines.add(VideoInfo.Line("标准", "https://v-cdn.zjol.com.cn/276984.mp4"))
        videoInfo.lines.add(
            VideoInfo.Line(
                "标准",
                "https://lvs.pull.hebtv.com/live/test1.m3u8"
            )
        )

        val controller = StandardVideoController(this)
        player.setVideoController(controller)
        controller.addDefaultControlComponent(true)
        controller.setFloatWindowEnabled(true)
        controller.setPlayerState(player.currentPlayerState)
        controller.setPlayState(player.currentPlayState)
        controller.setVideoInfo(videoInfo)
        player.setVideoInfo(videoInfo)

        if (mPIPManager.isShowing) {
            mPIPManager.stopFloatWindow()
        } else {
            mPIPManager.actClass = VideoPlayerTest2::class.java
            if (intent.getBooleanExtra("VR_MODE", false)) {
                player.set360Mode()
            }
        }

        playerContainer.addView(player)
        findViewById<Button>(R.id.button2).setOnClickListener {

            val videoInfo = VideoInfo(
                "测试2",
                "http://pic1.win4000.com/wallpaper/2/53d72db3ebb03.jpg"
            ).apply {
                this.lines.add(VideoInfo.Line("标准", "https://v-cdn.zjol.com.cn/276985.mp4"))
            }
            controller.setVideoInfo(videoInfo)
            player.setVideoInfo(videoInfo)
        }

    }


    override fun onBackPressed() {
        if (mPIPManager.onBackPress()) return
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIPManager.REQUEST_OVERLAY_CODE) {
            PIPManager.getInstance(this).onOverlayPermission(this)
        }
    }

}