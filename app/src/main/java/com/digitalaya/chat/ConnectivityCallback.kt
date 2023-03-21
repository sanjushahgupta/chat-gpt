package com.digitalaya.chat


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class ConnectivityCallback (private val context: Context) : ConnectivityManager.NetworkCallback() {

    private var isNetworkAvailable = false

    override fun onAvailable(network: Network) {
        isNetworkAvailable = true
        // internet is available, do something
    }

    override fun onLost(network: Network) {
        isNetworkAvailable = false
        // internet is unavailable, do something
    }

    fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }
}





