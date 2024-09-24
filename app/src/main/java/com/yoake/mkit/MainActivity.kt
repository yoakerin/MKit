package com.yoake.mkit

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import com.yoake.graphic.utils.load
import com.yoake.graphic.utils.loadRoundCorner
import com.yoake.location.R2LocationManager
import com.yoake.tools.R2Log
import com.yoake.tools.kit.onClick
import com.yoake.tools.permissions.R2PermissionLauncher
import com.yoake.tools.utils.R2StorageUtils
import com.yoake.umeng_share.R2SharePanelDialog
import com.yoake.umeng_share.ShareInfoImp
import com.yoake.umeng_share.UmengKit
import com.yoake.widgets.dialog.R2AlertDialog
import com.yoake.widgets.dialog.R2LoadingDialog
import com.yoake.widgets.popup.HorizontalGravity
import com.yoake.widgets.popup.R2BasePopup
import com.yoake.widgets.popup.VerticalGravity
import com.yoake.widgets.progress.R2CountDownProgressView
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        findViewById<R2CountDownProgressView>(R.id.testCountTimeProgressView).startCountTimeAnimation()
        //
        val container: GridLayout = findViewById(R.id.container)
        val linearLayout: LinearLayout = findViewById(R.id.linearLayout)


        val infoTv: TextView = findViewById(R.id.infoTv)

        (linearLayout[0] as ImageView).loadRoundCorner(
            "https://img0.baidu.com/it/u=1298002161,2550912603&fm=253&fmt=auto&app=138&f=JPEG?w=1423&h=800",
            10
        )
        container[0].onClick {
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

        container[1].onClick {
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
        container[2].onClick {

            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.QQ, object : UMAuthListener {
                override fun onStart(p0: SHARE_MEDIA?) {

                }

                @SuppressLint("SetTextI18n")
                override fun onComplete(
                    p0: SHARE_MEDIA?,
                    p1: Int,
                    p2: MutableMap<String, String>?
                ) {
                    (linearLayout[1] as ImageView).load(p2?.get("iconurl"))
                }

                override fun onError(p0: SHARE_MEDIA?, p1: Int, p2: Throwable?) {

                }

                override fun onCancel(p0: SHARE_MEDIA?, p1: Int) {
                }

            })
        }
        container[3].onClick {
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
                                    infoTv.text = "定位信息：${it.getAddressLine(0)}"
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
                    arrayOf("获取当前位置"),
                )
        }

        container[4].onClick {
            R2PermissionLauncher()
                .with(this)
                .denied {}
                .granted {
                    R2StorageUtils.saveNetworkImageToPicturePublicFolder(
                        this,
                        "https://img0.baidu.com/it/u=1298002161,2550912603&fm=253&fmt=auto&app=138&f=JPEG?w=1423&h=800"
                    )
                }
                .request(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    arrayOf("存储写入授权提示"),
                    arrayOf(getString(com.yoake.umeng_share.R.string.share_panel_download_permission_tip)),
                )
        }
        container[5].onClick {
            val dialog = R2LoadingDialog(this)
            dialog.show()
            container[5].postDelayed({ dialog.dismiss() }, 3000)
        }
        container[6].onClick {
            val popup = R2BasePopup(this)
                .setContentView(R.layout.test_layout_1)
                .setOutsideTouchable(false)
                .setBackgroundDimEnable(true)
                .setFocusAndOutsideEnable(true)
                .createPopup()
            popup.showAsDropDown(container[6], VerticalGravity.CENTER, HorizontalGravity.CENTER)
            container[6].postDelayed({ popup.dismiss() }, 3000)

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UmengKit.release(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UmengKit.onActivityResult(this, requestCode, resultCode, data)
    }

}