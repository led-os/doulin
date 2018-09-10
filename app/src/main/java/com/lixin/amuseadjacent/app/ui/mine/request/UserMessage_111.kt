package com.lixin.amuseadjacent.app.ui.mine.request

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.lixin.amuseadjacent.app.ui.mine.model.UserInfoModel
import com.lixin.amuseadjacent.app.util.StaticUtil
import com.lxkj.huaihuatransit.app.util.StrCallback
import com.lxkj.linxintechnologylibrary.app.util.ToastUtil
import com.zhy.http.okhttp.OkHttpUtils
import org.json.JSONObject

/**
 * 修改用户资料
 * Created by Slingge on 2018/9/10
 */
object UserMessage_111 {


    /**
     * 昵称
     * 性别
     * 生日(年月日)
     * 个人签名
     * 职业
     * */
    fun userInfo(context: Activity, nickname: String, sex: String, birthday: String, autograph: String, occupation: String) {
        val json = "{\"cmd\":\"editUserMessage\",\"uid\":\"" + StaticUtil.uid + "\",\"nickname\":\"" + nickname +
                "\",\"sex\":\"" + sex + "\",\"birthday\":\"" + birthday + "\",\"autograph\":\"" + autograph + "\",\"occupation\":\"" + occupation + "\"}";
        OkHttpUtils.post().url(StaticUtil.Url).addParams("json", json).build().execute(object : StrCallback() {
            override fun onResponse(response: String, id: Int) {
                super.onResponse(response, id)
                val obj = JSONObject(response)
                if (obj.getString("result") == "0") {
                    ToastUtil.showToast("修改成功")
                    StaticUtil.nickName=nickname
                    context.finish()
                } else {
                    ToastUtil.showToast(obj.getString("resultNote"))
                }
            }
        })
    }


}