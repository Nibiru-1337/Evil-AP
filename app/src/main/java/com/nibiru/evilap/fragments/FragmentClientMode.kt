package com.nibiru.evilap.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.nibiru.evilap.R


class FragmentClientMode: android.support.v4.app.Fragment(){
    /**************************************CLASS FIELDS********************************************/
    private val TAG = javaClass.simpleName
    private var lbm: LocalBroadcastManager? = null
    private val ClientModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i(TAG, "Got intent, action = " + intent?.action)
        }
    }
    /**************************************CLASS METHODS*******************************************/
    companion object { // never use fragment constructors with args, AOS will not use them
        fun newInstance(): FragmentClientMode = FragmentClientMode()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context == null) return
        lbm = LocalBroadcastManager.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_client_mode, container, false)
    }
}