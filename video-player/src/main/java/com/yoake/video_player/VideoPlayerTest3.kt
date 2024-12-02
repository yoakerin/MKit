package com.yoake.video_player

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import com.yoake.video_player.controller.StandardVideoController
import com.yoake.video_player.util.PIPManager
import com.yoake.video_player.util.VideoInfo


internal class VideoPlayerTest3 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v_player_test3)
        val multiPlayer = findViewById<MultiVideoPlayer>(R.id.player)
        multiPlayer.initCore()
        multiPlayer.setBackgroundThumb("https://img2.baidu.com/it/u=4256758710,3144398338&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500")

        val videoInfo = VideoInfo(
            "主机位",
            "https://img2.baidu.com/it/u=4256758710,3144398338&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500"
        ).apply {
            lines.add(VideoInfo.Line("标准", "https://v-cdn.zjol.com.cn/276985.mp4"))
        }
        val controller = StandardVideoController(this).apply {
            addDefaultControlComponent(false)
        }
        controller.setVideoInfo(videoInfo)

        multiPlayer.setVideoController(controller)
        if (PIPManager.getInstance(this).isShowing) {
            multiPlayer.switchNormalWindow()
        } else {
            PIPManager.getInstance(this).actClass = VideoPlayerTest3::class.java
            multiPlayer.setVideoInfo("0", videoInfo)
        }

        findViewById<Button>(R.id.button2).setOnClickListener {

            val videoInfo = VideoInfo(
                "子机位1",
                "https://img0.baidu.com/it/u=1208734652,1931649008&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500"
            ).apply {
                lines.add(VideoInfo.Line("标准", "https://v-cdn.zjol.com.cn/280443.mp4"))
            }
            multiPlayer.addPlayer("1", videoInfo)
        }


        findViewById<Button>(R.id.button3).setOnClickListener {

            val videoInfo = VideoInfo(
                "子机位3",
                "https://img2.baidu.com/it/u=457821052,3621597667&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500"
            ).apply {
                lines.add(VideoInfo.Line("标准", "https://v-cdn.zjol.com.cn/276982.mp4"))
            }
            multiPlayer.addPlayer("2", videoInfo)
        }

    }

    override fun onBackPressed() {
        if (PIPManager.getInstance(this).onBackPress()) return
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIPManager.REQUEST_OVERLAY_CODE) {
            PIPManager.getInstance(this).onOverlayPermission(this)
        }
    }
}