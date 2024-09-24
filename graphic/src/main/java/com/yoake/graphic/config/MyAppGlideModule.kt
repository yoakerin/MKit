//package com.yoake.graphic.config
//
//import android.annotation.SuppressLint
//import android.app.ActivityManager
//import android.content.Context
//import android.graphics.drawable.PictureDrawable
//import com.bumptech.glide.Glide
//import com.bumptech.glide.GlideBuilder
//import com.bumptech.glide.Registry
//import com.bumptech.glide.annotation.GlideModule
//import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
//import com.bumptech.glide.load.DecodeFormat
//import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
//import com.bumptech.glide.load.engine.cache.LruResourceCache
//import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.module.AppGlideModule
//import com.bumptech.glide.request.RequestOptions
//import com.caverock.androidsvg.SVG
//import com.yoake.graphic.svg.SvgDecoder
//import com.yoake.graphic.svg.SvgDrawableTranscoder
//import okhttp3.OkHttpClient
//import java.io.InputStream
//import java.security.SecureRandom
//import java.security.cert.X509Certificate
//import javax.net.ssl.SSLContext
//import javax.net.ssl.SSLSocketFactory
//import javax.net.ssl.X509TrustManager
//
///**
// * MyAppGlideModule 配置glide缓存
// */
////应用程序可能依赖多个程序库，而它们每一个都可能包含一个或更多的 LibraryGlideModules 。
////在极端情况下，这些 LibraryGlideModules 可能定义了相互冲突的选项，或者包含了应用程序希望避免的行为。
////应用程序可以通过给他们的 AppGlideModule 添加一个 @Excludes 注解来解决这种冲突，或避免不需要的依赖。
////@Excludes(com.example.unwanted.GlideModule)
//@GlideModule
//class MyAppGlideModule : AppGlideModule() {
//
//    override fun isManifestParsingEnabled() = false
//    override fun applyOptions(context: Context, builder: GlideBuilder) {
//        //设置内存缓存大小：根据机器自动计算
//        val memorySizeCalculator = MemorySizeCalculator.Builder(context).build()
//        builder.setMemoryCache(LruResourceCache(memorySizeCalculator.memoryCacheSize.toLong()))
//        //设置磁盘缓存大小：500M 默认250M 设置磁盘缓存文件夹名称 "factory_image" 默认 "image_manager_disk_cache"
//        //修改磁盘缓存的文件夹和磁盘大小
//        builder.setDiskCache(
//            InternalCacheDiskCacheFactory(
//                context,
//                "factory_image",
//                500 * 1024 * 1024
//            )
//        )
//
//        val manager: ActivityManager? =
//            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        manager?.let {
//            val memoryInfo = ActivityManager.MemoryInfo()
//            manager.getMemoryInfo(memoryInfo)
//            builder.setDefaultRequestOptions(
//                if (memoryInfo.lowMemory) {
//                    RequestOptions().format(DecodeFormat.PREFER_RGB_565)
//                } else {
//                    RequestOptions().format(DecodeFormat.PREFER_ARGB_8888)
//                }
//            )
//        }
//
//    }
//
//    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
//        val client = OkHttpClient.Builder()
//            .sslSocketFactory(sSLSocketFactory, trustManager)
//            .build()
//        registry.replace(
//            GlideUrl::class.java,
//            InputStream::class.java,
//            OkHttpUrlLoader.Factory(client)
//        )
//            .register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
//            .append(InputStream::class.java, SVG::class.java, SvgDecoder())
//    }
//
//    /**
//     * 获取一个SSLSocketFactory
//     * */
//    private val sSLSocketFactory: SSLSocketFactory
//        get() = try {
//            val sslContext = SSLContext.getInstance("SSL")
//            sslContext.init(null, arrayOf(trustManager), SecureRandom())
//            sslContext.socketFactory
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//
//    /**
//     * 获取一个忽略证书的X509TrustManager
//     */
//    private val trustManager: X509TrustManager
//        get() = @SuppressLint("CustomX509TrustManager")
//        object : X509TrustManager {
//            @SuppressLint("TrustAllX509TrustManager")
//            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
//            }
//
//            @SuppressLint("TrustAllX509TrustManager")
//            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
//            }
//
//            override fun getAcceptedIssuers(): Array<X509Certificate> {
//                return arrayOf()
//            }
//        }
//
//}
