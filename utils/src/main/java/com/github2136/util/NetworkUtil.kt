package com.github2136.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build

/**
 * Created by YB on 2019/5/6
 */
class NetworkUtil private constructor(context: Context) {
    private val connectivityManager: ConnectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    //是否有网络
    fun isNetworkAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) ?: false
        } else {
            isLinkable(connectivityManager.activeNetworkInfo)
        }
    }


    //是否连接wifi
    fun isWifiAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
        } else {
            isLinkable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI))
        }
    }

    //是否连接手机网络
    fun isMobileAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false
        } else {
            isLinkable(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE))
        }

    }

    private fun isLinkable(info: NetworkInfo?): Boolean {
        return info?.isConnected ?: false
    }

    companion object {

        @Volatile
        private var instance: NetworkUtil? = null

        fun getInstance(context: Context): NetworkUtil {
            if (instance == null) {
                synchronized(NetworkUtil::class) {
                    if (instance == null) {
                        instance = NetworkUtil(context)
                    }
                }
            }
            return instance!!
        }
    }
}