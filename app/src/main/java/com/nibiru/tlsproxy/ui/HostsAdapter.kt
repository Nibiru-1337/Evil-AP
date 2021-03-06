package com.nibiru.tlsproxy.ui

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.nibiru.tlsproxy.AppService
import com.nibiru.tlsproxy.R
import com.nibiru.tlsproxy.R.layout.rv_host_item_row
import com.nibiru.tlsproxy.RxEventBus
import com.nibiru.tlsproxy.inflate
import kotlinx.android.synthetic.main.rv_host_item_row.view.*

// https://www.raywenderlich.com/170075/android-recyclerview-tutorial-kotlin
class HostsAdapter(private val hosts: ArrayList<AppService.Host>) : RecyclerView.Adapter<HostsAdapter.HostHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostHolder{
        val inflatedView = parent.inflate(rv_host_item_row, false)
        return HostHolder(inflatedView)
    }

    override fun getItemCount(): Int = hosts.size

    override fun onBindViewHolder(holder: HostHolder, position: Int) {
        val hostItem = hosts[position]
        holder.bindHost(hostItem)
    }

    class HostHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private lateinit var host: AppService.Host

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            Log.d("HostsAdapter", "CLICK on $host")
            view.rvCheckBox.isChecked = !view.rvCheckBox.isChecked
            RxEventBus.INSTANCE.send2BackEnd(AppService.EventHostChecked(host, view.rvCheckBox.isChecked))
        }

        fun bindHost(host: AppService.Host) {
            this.host = host
            with(view){
                rvHostIP.text = host.ip
                rvHostMAC.text = host.mac
                when(host.type){
                    "this" -> rvHostIcon.setImageResource(R.drawable.ic_phone_white_24dp)
                    "gateway" -> rvHostIcon.setImageResource(R.drawable.ic_router_white_24dp)
                    "host" -> rvHostIcon.setImageResource(R.drawable.ic_laptop_white_24dp)
                }
            }
        }
    }


}