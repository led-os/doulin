package com.lixin.amuseadjacent.app.ui.mine.adapter

import android.app.Activity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.lixin.amuseadjacent.R
import com.lixin.amuseadjacent.app.MyApplication
import com.lixin.amuseadjacent.app.ui.dialog.ProgressDialog
import com.lixin.amuseadjacent.app.ui.find.activity.DynamicDetailsActivity
import com.lixin.amuseadjacent.app.ui.find.model.FindModel
import com.lixin.amuseadjacent.app.ui.mine.request.Myinteraction_161162
import com.lixin.amuseadjacent.app.util.StaticUtil
import com.nostra13.universalimageloader.core.ImageLoader
import com.xiao.nicevideoplayer.NiceVideoPlayer
import com.xiao.nicevideoplayer.TxVideoPlayerController

/**
 * 个人主页动态
 * Created by Slingge on 2018/8/18
 */
class DynamicAdapter(val context: Activity, val dynaList: ArrayList<FindModel.dynamicModel>) : RecyclerView.Adapter<DynamicAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_personal_dynamic, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dynaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = dynaList[position]

        if (!TextUtils.isEmpty(model.dynamicVideo)) {
            holder.player.visibility = View.VISIBLE
            holder.image.visibility = View.GONE
            holder.tv_info.visibility = View.GONE

            holder.player.setUp(model.dynamicVideo, null)
            val controller = TxVideoPlayerController(context)
            ImageLoader.getInstance().displayImage(model.dynamicImg, controller.imageView())
            holder.player.setController(controller)
        } else {
            holder.player.visibility = View.GONE
            holder.image.visibility = View.VISIBLE
            holder.tv_info.visibility = View.VISIBLE

            if (model.dynamicImgList.isNotEmpty()) {
                ImageLoader.getInstance().displayImage(model.dynamicImgList[0], holder.image)
            }
            holder.tv_info.text = model.dynamicContent
        }

        holder.tv_zan.text = model.zanNum
        holder.tv_comment.text = model.commentNum

        try {
            holder.tv_day.text = model.time.substring(8, 10)
            holder.tv_month.text = model.time.substring(5, 7).replace("0", "") + "月"
        } catch (e: Exception) {
        }

        if (StaticUtil.uid == model.dynamicUid) {
            holder.tv_del.visibility = View.VISIBLE
        } else {
            holder.tv_del.visibility = View.GONE
        }

        holder.tv_del.setOnClickListener { v ->
            ProgressDialog.showDialog(context)
            Myinteraction_161162.DelInteraction("2", model.dynamicId, object : Myinteraction_161162.DelInteractionCallBack {
                override fun del() {
                    dynaList.removeAt(position)
                    notifyDataSetChanged()
                }
            })
        }

        holder.cl.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.putString("flag", "0")
            bundle.putString("id", dynaList[position].dynamicId)
            MyApplication.openActivity(context, DynamicDetailsActivity::class.java, bundle)
        }

    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val player: NiceVideoPlayer = view.findViewById(R.id.player)

        val tv_day = view.findViewById<TextView>(R.id.tv_day)
        val tv_month = view.findViewById<TextView>(R.id.tv_month)
        val tv_info = view.findViewById<TextView>(R.id.tv_info)
        val tv_comment = view.findViewById<TextView>(R.id.tv_comment)
        val tv_zan = view.findViewById<TextView>(R.id.tv_zan)
        val tv_del = view.findViewById<TextView>(R.id.tv_del)

        val image = view.findViewById<ImageView>(R.id.image)

        val cl = view.findViewById<ConstraintLayout>(R.id.cl)

        init {
            player.setPlayerType(NiceVideoPlayer.TYPE_IJK) // IjkPlayer or MediaPlayer
        }
    }

}