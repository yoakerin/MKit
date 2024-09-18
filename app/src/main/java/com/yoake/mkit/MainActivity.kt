package com.yoake.mkit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yoake.tools.R2Log


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        R2Log.setDelegate(object : R2Log.LogDelegate {
            override fun e(tag: String?, msg: String?, vararg obj: Any?) {
                Log.e(tag, msg ?: "")
            }

            override fun w(tag: String?, msg: String?, vararg obj: Any?) {
                Log.w(tag, msg ?: "")
            }

            override fun i(tag: String?, msg: String?, vararg obj: Any?) {
                Log.i(tag, msg ?: "")
            }

            override fun d(tag: String?, msg: String?, vararg obj: Any?) {
                Log.d(tag, msg ?: "")
            }

            override fun printErrStackTrace(
                tag: String?,
                tr: Throwable?,
                format: String?,
                vararg obj: Any?
            ) {

            }
        })

    }


}