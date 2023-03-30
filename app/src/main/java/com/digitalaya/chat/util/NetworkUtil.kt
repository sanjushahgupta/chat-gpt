package com.digitalaya.chat.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtil {
    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val networkInfo =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

            return networkInfo.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkInfo.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        } catch (e: Exception) {
            return false
        }
    }
}