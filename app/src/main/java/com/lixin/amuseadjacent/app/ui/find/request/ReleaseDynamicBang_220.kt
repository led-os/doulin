package com.lixin.amuseadjacent.app.ui.find.request

import android.app.Activity
import android.content.Intent
import android.os.Environment
import android.text.TextUtils
import android.widget.TextView
import com.google.gson.Gson
import com.lixin.amuseadjacent.app.util.ImageFileUtil
import com.lixin.amuseadjacent.app.util.StaticUtil
import com.lixin.amuseadjacent.app.util.abLog
import com.luck.picture.lib.entity.LocalMedia
import com.lxkj.huaihuatransit.app.util.StrCallback
import com.lxkj.linxintechnologylibrary.app.util.ToastUtil
import com.zhy.http.okhttp.OkHttpUtils
import okhttp3.Call
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.io.File

/**
 * 发布动态帮帮
 * type 0 动态 1帮帮
 * Created by Slingge on 2018/9/17
 */
object ReleaseDynamicBang_220 {

    fun release(context: Activity, type: String, content: String, imageList: ArrayList<LocalMedia>, videoPath: String,
                address: String, height: String, width: String) {

        val listfile = arrayListOf<File>() //map是无序集合尽量用list
        val videoFile = File(videoPath)
        abLog.e("发布动态帮帮", Gson().toJson(imageList))
        if (TextUtils.isEmpty(videoPath)) {
            for (i in 0 until imageList.size - 1) {//图片路径集合
                val file = File(imageList[i].compressPath)//保存压缩
                abLog.e("压缩路径.....", imageList[i].compressPath)
                listfile.add(file)
            }

            OkHttpUtils.post().url(StaticUtil.ReleaseDynamicBang).addParams("uid", StaticUtil.uid)//发布图片不传videoFile
                    .addParams("type", type).addParams("content", content).files("file", listfile)
                    .addParams("address", address)
                    .addParams("height", height).addParams("width", width)//视频高度、宽度
                    .build().execute(object : StrCallback() {
                        override fun onResponse(response: String, id: Int) {
                            super.onResponse(response, id)
                            val obj = JSONObject(response)
                            if (obj.getString("result") == "0") {
                                ToastUtil.showToast("发布成功")
                                val intent = Intent()
                                context.setResult(1, intent)
                                context.finish()
                            } else {
                                ToastUtil.showToast(obj.getString("resultNote"))
                            }
                        }

                        override fun onError(call: Call, e: Exception, id: Int) {
                            super.onError(call, e, id)
                            EventBus.getDefault().post("onclick")
                        }
                    })
        } else {
            val file = ImageFileUtil.saveFilePath(Environment.getExternalStorageDirectory().path
                    + "/Android/data/com.lixin.amuseadjacent/image" + "/PreviewingVideo.jpg")//预览图
            listfile.add(file)

            OkHttpUtils.post().url(StaticUtil.ReleaseDynamicBang).addParams("uid", StaticUtil.uid)
                    .addParams("type", type).addParams("content", content).files("file", listfile)
                    .addFile("file1", videoFile.name, videoFile).addParams("address", address)
                    .addParams("height", height).addParams("width", width)
                    .build().execute(object : StrCallback() {
                        override fun onResponse(response: String, id: Int) {
                            super.onResponse(response, id)
                            val obj = JSONObject(response)
                            if (obj.getString("result") == "0") {
                                ToastUtil.showToast("发布成功")
                                val intent = Intent()
                                context.setResult(1, intent)
                                context.finish()
                            } else {
                                ToastUtil.showToast(obj.getString("resultNote"))
                            }
                        }

                        override fun onError(call: Call, e: Exception, id: Int) {
                            super.onError(call, e, id)
                            EventBus.getDefault().post("onclick")
                        }
                    })
        }

    }


}