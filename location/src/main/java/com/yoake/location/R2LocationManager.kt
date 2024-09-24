package com.yoake.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import java.util.Locale


class R2LocationManager private constructor(private val context: Context) {

    private var lastLocationTime: Long = 0L
    private var cacheLocation: Location? = null
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // 检查权限是否授予
    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    /**
     * 获取设备的当前位置
     */
    fun getCurrentLocation(
        cache: Long = 5 * 60_000,
        onResult: (location: Location?, error: String?) -> Unit
    ) {
        if (!checkLocationPermission()) {
            onResult(null, "Location permission is not granted")
            return
        }

        if (cacheLocation != null && System.currentTimeMillis() - lastLocationTime < cache) {
            onResult(cacheLocation, null)
            return
        }

        // 尝试通过网络获取位置，若失败则回退到 GPS
        getLocationFromProvider(LocationManager.NETWORK_PROVIDER) { location, error ->
            location?.let {
                onResult(it, null)
            } ?: run {
                getLocationFromProvider(LocationManager.GPS_PROVIDER, onResult)
            }
        }
    }

    /**
     * 通用方法：根据提供者类型获取位置
     */
    private fun getLocationFromProvider(
        provider: String,
        onResult: (location: Location?, error: String?) -> Unit
    ) {
        try {
            if (!locationManager.isProviderEnabled(provider)) {
                onResult(null, "$provider 被禁用")
                return
            }

            locationManager.requestLocationUpdates(provider, 0L, 0f, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    lastLocationTime = System.currentTimeMillis()
                    cacheLocation = location
                    locationManager.removeUpdates(this)
                    onResult(location, null)
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {
                    locationManager.removeUpdates(this)
                    onResult(null, "$provider 被禁用")
                }
            })
        } catch (e: SecurityException) {
            onResult(null, "权限错误: ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            onResult(cacheLocation, "获取位置时出错: ${e.message}")
        }
    }


    // 将经纬度转换为地址
    fun getAddressFromLocation(location: Location): Address? {
        return getAddressFromLocation(location.latitude, location.longitude)
    }

    // 将经纬度转换为地址
    fun getAddressFromLocation(latitude: Double, longitude: Double): Address? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.isNullOrEmpty()) {
                null
            } else {
                addresses[0]  // 返回第一条地址
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // 将地址转换为经纬度
    fun getLocationFromAddress(address: String): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocationName(address, 1)
            if (!addresses.isNullOrEmpty()) {
                val location: Address = addresses[0]
                Pair(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: R2LocationManager? = null

        // 获取单例实例
        fun getInstance(context: Context): R2LocationManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: R2LocationManager(context.applicationContext)
                    .also { INSTANCE = it }
            }
        }
    }
}
