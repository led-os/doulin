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

/**
 * Created by Slingge on 2018/8/16
 */
class CommentNewsAdapter(val context: Context) : RecyclerView.Adapter<CommentNewsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment_news, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        MyApplication.setRedNum(holder.tv_msgNum, 100)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_msgNum = view.findViewById<BGABadgeTextView>(R.id.tv_msgNum)
        val tv_see = view.findViewById<TextView>(R.id.tv_see)
        val tv_time = view.findViewById<TextView>(R.id.tv_time)
    }


}