package com.yoake.mkit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yoake.location.R2LocationManager
import com.yoake.tools.R2Log
import com.yoake.tools.kit.onClick
import com.yoake.tools.permissions.R2PermissionLauncher
import com.yoake.umeng_share.R2SharePanelDialog
import com.yoake.umeng_share.ShareInfoImp
import com.yoake.umeng_share.UmengKit
import com.yoake.widgets.dialog.R2AlertDialog
import com.yoake.widgets.progress.R2CountDownProgressView


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        findViewById<R2CountDownProgressView>(R.id.testCountTimeProgressView).startCountTimeAnimation()
        //
        val btn1: Button = findViewById(R.id.but1)
        val btn2: Button = findViewById(R.id.but2)
        val btn3: Button = findViewById(R.id.but3)
        btn1.onClick {
            val myDialog = R2AlertDialog(this).builder()
            myDialog.setGone().setTitle("提示").setMsg("仿iOS的弹窗")
                .setNegativeButton("取消") {
                    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show()
                }.setPositiveButton(
                    "确定"
                ) {
                    Toast.makeText(this, "确认", Toast.LENGTH_SHORT).show()
                }.show()
        }
        //

        btn2.onClick {
            val dialog = R2SharePanelDialog(this)
            dialog.onItemClick = { position, item -> false }
            val shareInfo = ShareInfoImp(
                "男生开学发现同桌是奥运冠军袁心玥：当时感觉很不可思议",
                "https://img2.baidu.com/it/u=354117429,2499711403&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313",
                "https://m.toutiao.com/article/7413310219100455435/?log_from=677fcf6c6799_1726106424738&wid=1726106469197&upstream_biz=toutiao_pc",
                "9月10日，北京体育大学一男生上课时发现同桌竟是奥运冠军袁心玥。男生称，上课时突然一抬头看到了奥运冠军坐在自己的旁边，当时感觉很不可思议，后来一看确实是袁心玥，还聊了一会，和奥运冠军坐在一块上课真的是一种很好的体验。"
            )
            dialog.show()

            val posterView = ImageView(this@MainActivity).apply {
                setImageResource(com.yoake.tools.R.drawable.tools_preview_logo)
            }
            dialog.setDate(shareInfo, posterView = posterView)
        }

        btn3.onClick {
            R2PermissionLauncher()
                .with(this)
                .denied { }
                .granted {
                    R2LocationManager.getInstance(this)
                        .getCurrentLocation { location, error ->
                            R2Log.d(
                                "",
                                "${location?.latitude} ${location?.longitude} $error"
                            )
                            location?.let {
                                val address = R2LocationManager.getInstance(this)
                                    .getAddressFromLocation(location)
                                address?.let {
                                    for (i in 0 until it.maxAddressLineIndex)
                                        R2Log.d("", it.getAddressLine(i))
                                }
                            }
                        }

                }.request(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ),
                    arrayOf("定位授权提示"),
                    arrayOf(getString(com.yoake.umeng_share.R.string.share_panel_download_permission_tip)),
                )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UmengKit.onActivityResult(this, requestCode, resultCode, data)
    }
}