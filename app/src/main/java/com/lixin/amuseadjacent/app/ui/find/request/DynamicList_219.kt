package com.lixin.amuseadjacent.app.ui.find.request

import com.google.gson.Gson
import com.lixin.amuseadjacent.app.ui.find.model.DynamiclDetailsModel
import com.lixin.amuseadjacent.app.ui.find.model.DynamiclModel
import com.lixin.amuseadjacent.app.ui.find.model.FindModel
import com.lixin.amuseadjacent.app.util.StaticUtil
import com.lixin.amuseadjacent.app.util.abLog
import com.lxkj.huaihuatransit.app.util.StrCallback
import com.lxkj.linxintechnologylibrary.app.util.ToastUtil
import com.zhy.http.okhttp.OkHttpUtils
import org.greenrobot.eventbus.EventBus

/**
 * 动态帮帮列表
 * Created by Slingge on 2018/9/15
 */
object DynamicList_219 {

    interface DynamicListCallBack{
        fun  dynamicList(model:DynamiclModel)
    }

    /**
     * @param state 0动态 1帮帮
     * @param type 0全部 1关注
     * */
    fun dynamic(state: String, type: Int, nowPage: Int,dynamicCallBack: DynamicListCallBack?) {
        val json = "{\"cmd\":\"findDynamicList\",\"uid\":\"" + StaticUtil.uid + "\",\"communityId\":\"" + StaticUtil.communityId +
                "\",\"state\":\"" + state + "\",\"type\":\"" + type + "\",\"nowPage\":\"" + nowPage + "\",\"pageCount\":\"" + "15" + "\"}"
        abLog.e("动态、帮帮.................", json)
        OkHttpUtils.post().url(StaticUtil.Url).addParams("json", json).build().execute(object : StrCallback() {
            override fun onResponse(response: String, id: Int) {
                super.onResponse(response, id)
                val model = Gson().fromJson(response, DynamiclModel::class.java)
                if (model.result == "0") {
                    if(dynamicCallBack==null){
                        EventBus.getDefault().post(model)
                    }else{
                        dynamicCallBack.dynamicList(model)
                    }
                } else {
                    ToastUtil.showToast(model.resultNote)
                }
            }
        })
    }


}