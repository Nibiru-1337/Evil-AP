package com.nibiru.evilap.pki

import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import javax.net.ssl.SNIMatcher
import javax.net.ssl.SNIServerName
import javax.net.ssl.StandardConstants

@RequiresApi(Build.VERSION_CODES.N)
class EvilSniMatcher: SNIMatcher(StandardConstants.SNI_HOST_NAME) {
    private val TAG = javaClass.simpleName

    override fun matches(serverName: SNIServerName?): Boolean {
        Log.d(TAG, "SNI=($serverName)")
        return true
    }

}