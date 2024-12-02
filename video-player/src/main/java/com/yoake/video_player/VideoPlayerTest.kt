package com.yoake.video_player

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class VideoPlayerTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.v_player_test)
        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(this, VideoPlayerTest2::class.java)
            intent.putExtra("VR_MODE", findViewById<CheckBox>(R.id.vr_mode).isChecked)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button2).setOnClickListener {
            val intent = Intent(this, VideoPlayerTest3::class.java)
            startActivity(intent)
        }
    }

}