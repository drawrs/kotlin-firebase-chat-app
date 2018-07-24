package com.khilman.www.firebasechatkotlintest.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast

class Util {

    fun initToast(c: Context?, msg: String?){
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show()
    }

    fun isConnectionAvaiable(c: Context?): Boolean {
        val connectivyManager = c?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val isOnline =(connectivyManager.getActiveNetworkInfo() != null
                && connectivyManager.getActiveNetworkInfo().isAvailable()
                && connectivyManager.getActiveNetworkInfo().isConnected())
        return isOnline

    }
}