package com.lixin.amuseadjacent.app.ui.find.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.lixin.amuseadjacent.R
import com.lixin.amuseadjacent.app.MyApplication
import com.lixin.amuseadjacent.app.ui.find.activity.EventDetailsActivity
import com.lixin.amuseadjacent.app.ui.find.model.EventModel
import com.lixin.amuseadjacent.app.util.AbStrUtil
import com.lixin.amuseadjacent.app.util.ImageLoaderUtil
import com.lixin.amuseadjacent.app.view.CircleImageView
import com.nostra13.universalimageloader.core.ImageLoader
import java.util.ArrayList

/**
 * 活动
 * Created by Slingge on 2018/8/25.
 */
class EventAdapter(val context: Context,val eventList : ArrayList<EventModel.dataModel>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_find, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cl_2.visibility = View.VISIBLE

        val model = eventList!![position]
        ImageLoader.getInstance().displayImage(model.activityImg, holder.image)
        if (model.issignup == "0") {//0未报名 1已报名
            holder.tv_type.text = "未报名"
        } else {
            holder.tv_type.text = "已报名"
        }

        if (model.isAttention == "0") {// 0未关注 1已关注
            holder.tv_follow.text = "关注"
        } else {
            holder.tv_follow.text = "已关注"
        }

        if (model.isZan == "0") {//0未赞过 1已赞过
            AbStrUtil.setDrawableLeft(context, R.drawable.ic_zan, holder.tv_zan, 5)
        } else {
            AbStrUtil.setDrawableLeft(context, R.drawable.ic_zan_hl, holder.tv_zan, 5)
        }

        holder.tv_actiivtyname.text = model.activityName
        holder.tv_price.text = model.activityMoney + "元/人"
        holder.tv_activitytime.text = "时间：" + model.activityTime
        holder.tv_address.text = "地点：" + model.activityAddress
        holder.tv_num.text = "人数：" + model.activityAllnum

        ImageLoader.getInstance().displayImage(model.userIcon, holder.iv_header, ImageLoaderUtil.HeaderDIO())
        holder.tv_name.text = model.userName
        holder.tv_effect.text = "影响力" + model.userEffectNum
        holder.tv_info.visibility = View.GONE

        holder.tv_time.text = model.time
        holder.tv_comment.text = model.commentNum
        holder.tv_zan.text = model.zanNum

        holder.itemView.setOnClickListener { v ->
            MyApplication.openActivity(context, EventDetailsActivity::class.java)
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ll_image = view.findViewById<LinearLayout>(R.id.ll_image)
        val cl_2 = view.findViewById<ConstraintLayout>(R.id.cl_2)
        val tv_info = view.findViewById<TextView>(R.id.tv_info)


        val iv_header = view.findViewById<CircleImageView>(R.id.iv_header)
        val tv_name = view.findViewById<TextView>(R.id.tv_name)
        val tv_effect = view.findViewById<TextView>(R.id.tv_effect)
        val tv_follow = view.findViewById<TextView>(R.id.tv_follow)

        val tv_time = view.findViewById<TextView>(R.id.tv_time)
        val tv_comment = view.findViewById<TextView>(R.id.tv_comment)
        val tv_zan = view.findViewById<TextView>(R.id.tv_zan)

        val image = view.findViewById<ImageView>(R.id.image)
        val tv_type = view.findViewById<TextView>(R.id.tv_type)
        val tv_actiivtyname = view.findViewById<TextView>(R.id.tv_actiivtyname)
        val tv_price = view.findViewById<TextView>(R.id.tv_price)
        val tv_activitytime = view.findViewById<TextView>(R.id.tv_activitytime)
        val tv_address = view.findViewById<TextView>(R.id.tv_address)
        val tv_num = view.findViewById<TextView>(R.id.tv_num)

        init {
            ll_image.visibility = View.GONE
            cl_2.visibility = View.VISIBLE
            tv_info.visibility = View.GONE
        }


    }


}