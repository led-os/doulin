package com.lixin.amuseadjacent.app.ui.message.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.bingoogolapple.badgeview.BGABadgeTextView
import com.lixin.amuseadjacent.R
import com.lixin.amuseadjacent.app.MyApplication
import com.lixin.amuseadjacent.app.ui.message.model.MyMsgListModel

/**
 * Created by Slingge on 2018/8/15
 */
class MyMsgAdapter(val context: Context, val list: ArrayList<MyMsgListModel.dataModel>) : RecyclerView.Adapter<MyMsgAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_msg, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        MyApplication.setRedNum(holder.tv_msgNum, 10)
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.image)
        val tv_type = view.findViewById<TextView>(R.id.tv_type)
        val tv_info = view.findViewById<TextView>(R.id.tv_info)
        val tv_time = view.findViewById<TextView>(R.id.tv_time)
        val tv_msgNum = view.findViewById<BGABadgeTextView>(R.id.tv_msgNum)
    }

}