package com.lixin.amuseadjacent.app.ui.find.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.lixin.amuseadjacent.R
import com.lixin.amuseadjacent.app.util.ImageFileUtil
import com.luck.picture.lib.entity.LocalMedia
import com.lxkj.linxintechnologylibrary.app.util.SelectPictureUtil
import com.lxkj.linxintechnologylibrary.app.util.ToastUtil
import com.lxkj.runproject.app.view.SquareImage
import com.nostra13.universalimageloader.core.ImageLoader


/**
 *
 * Created by Slingge on 2017/5/3 0003.
 */

class ReleaseAdapter(val context: Activity, val list: ArrayList<LocalMedia>, val maxNum: Int, val imageRemoveCallback: ImageRemoveCallback?) : RecyclerView.Adapter<ReleaseAdapter.MyViewHolder>() {

    interface ImageRemoveCallback {
        fun imageRemove(i: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_album_image, parent, false)
        return MyViewHolder(view)
    }

    private var flag = 0//0可以发视频，1不可以发视频
    fun setFlag(flag: Int) {
        this.flag = flag
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (position == list.size - 1) {
            holder.image.setImageResource(R.drawable.ic_add2)
            holder.image.scaleType = ImageView.ScaleType.CENTER_CROP
            holder.iv_del.visibility = View.GONE
            holder.image.setOnClickListener { v ->
                if (flag == 0) {
                    val builder = AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                    builder.setMessage("选择发布内容").setPositiveButton("图片") { p0, p1 ->
                        SelectPictureUtil.selectPicture(context, maxNum - list.size + 1, 0, false)
                        builder.create().dismiss()
                    }.setNegativeButton("视频（10秒以内）"
                    ) { p0, p1 ->
                        SelectPictureUtil.selectVodeoPicture(context, 1, 0)
                        builder.create().dismiss()
                    }.create().show()
                } else {
                    SelectPictureUtil.selectPicture(context, maxNum - list.size + 1, 0, false)
                }
            }
        } else {
            holder.iv_del.visibility = View.VISIBLE

            if (!list[position].path.contains("http://")) {  //本地图片
                val bitmap = ImageFileUtil.getBitmapFromPath(list[position].compressPath)//压缩的路径
                holder.image.scaleType = ImageView.ScaleType.CENTER_CROP

                holder.image.setImageBitmap(bitmap)
            } else {//网络图片
//                holder.iv_del.visibility = View.GONE
                ImageLoader.getInstance().displayImage(list[position].path, holder.image)
            }
        }


        holder.iv_del.setOnClickListener { v ->
            imageRemoveCallback!!.imageRemove(position)
        }

    }


    override fun getItemCount(): Int {
        if (list.size - 1 == maxNum) {
            return list.size - 1
        } else {
            return list.size
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image = itemView.findViewById<SquareImage>(R.id.image)
        var iv_del = itemView.findViewById<ImageView>(R.id.iv_del)
    }


}
