package com.yoake.tools.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.yoake.tools.kit.load2
import com.yoake.tools.kit.then
import com.yoake.tools.kit.toast
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

object R2StorageUtils {
    val TAG: String = R2StorageUtils::class.java.simpleName
    private const val APP_FOLDER_NAME = "image_download"
    private fun getAppDownloadPath(): String =
        "${Environment.DIRECTORY_DOWNLOADS}/$APP_FOLDER_NAME/"

    /**
     * 将在bitmap保存到本地
     * 与文件下载保存十分相似
     * @param displayName 文件名字
     * @param mimeType
     * @param compressFormat
     * @param refreshGallery 是否刷新到相册
     */
    fun saveBitmapToPicturePublicFolder(
        context: Context,
        bitmap: Bitmap?,
        displayName: String = "img_${System.currentTimeMillis()}.jpeg",
        mimeType: String = "image/jpeg",
        compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
        refreshGallery: Boolean = true
    ) {
        if (bitmap == null) {
            context.toast("资源不存在，添加失败")
            return
        }
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            generatorSavePicToPublicFolderContentValues(displayName, mimeType)
        )
        uri?.also {
            val outputStream = context.contentResolver.openOutputStream(it)
            outputStream?.also { os ->
                bitmap.compress(compressFormat, 100, os)
                os.close()
                if (refreshGallery) {
                    val path = getAppPicturePath()
                    MediaScannerConnection.scanFile(
                        context.applicationContext, arrayOf("$path/${displayName}"), null, null
                    )
                }
                context.toast("添加图片成功")
            }
        }
    }

    /**
     * 将在线图片保存到本地
     * 与文件下载保存十分相似
     */
    fun saveNetworkImageToPicturePublicFolder(
        context: Context,
        photoUrl: String?,
        photoName: String = "img_${System.currentTimeMillis()}.jpeg",
        mimeType: String = "image/jpeg",
        refreshGallery: Boolean = true,
        result: (Boolean) -> Unit
    ) {
        if (photoUrl == null) {
            context.toast("资源不存在，下载失败")
            result.invoke(false)
            return
        }
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            generatorSavePicToPublicFolderContentValues(photoName, mimeType)
        )
        context.load2 {
            downloadAndSaveUri(context, uri, photoUrl)
        } then {
            if (refreshGallery) {
                val path = getAppPicturePath()
                MediaScannerConnection.scanFile(
                    context, arrayOf("$path/${photoName}"), null, null
                )
            }
            context.toast("图片下载成功")
            result.invoke(true)
        }
    }


    private fun downloadAndSaveUri(context: Context, uri: Uri?, downloadUrl: String) {
        val url = URL(downloadUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val inputStream = connection.inputStream
        val bis = BufferedInputStream(inputStream)
        uri?.also {
            val outputStream = context.contentResolver.openOutputStream(uri)
            outputStream?.let {
                val bos = BufferedOutputStream(outputStream)
                val buffer = ByteArray(1024)
                var bytes = bis.read(buffer)
                while (bytes >= 0) {
                    bos.write(buffer, 0, bytes)
                    bos.flush()
                    bytes = bis.read(buffer)
                }
                bos.close()
            }
        }
        bis.close()
    }

    fun downloadApkAndInstall(context: Context, fileUrl: String, apkName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.toast("开始下载...")
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, apkName)
                put(MediaStore.MediaColumns.RELATIVE_PATH, getAppDownloadPath())
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
            )
            context.load2 {
                downloadAndSaveUri(context, uri, fileUrl)
            } then {
                installAPK(context, uri)
            }
        } else {
            context.toast("请使用原始方式")
        }
    }

    private fun installAPK(context: Context, uri: Uri?) {
        uri?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            context.startActivity(intent)
        }
    }

    private fun generatorSavePicToPublicFolderContentValues(
        displayName: String, mimeType: String
    ): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        val path = getAppPicturePath()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, path)
        } else {
            val fileDir = File(path)
            if (!fileDir.exists()) {
                fileDir.mkdir()
            }
            contentValues.put(MediaStore.MediaColumns.DATA, path + displayName)
        }
        return contentValues
    }

    private fun getAppPicturePath(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // relative path
            "${Environment.DIRECTORY_PICTURES}/$APP_FOLDER_NAME/"
        } else {
            // full path
            "${Environment.getExternalStorageDirectory().absolutePath}/${Environment.DIRECTORY_PICTURES}/$APP_FOLDER_NAME/"

        }
    }
}