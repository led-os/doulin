package com.lixin.amuseadjacent.app.ui.dialog

import android.app.Activity
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import com.lixin.amuseadjacent.R
import android.view.Gravity
import android.widget.TextView
import com.lixin.amuseadjacent.app.MyApplication
import com.lixin.amuseadjacent.app.ui.mine.activity.EffectCommunityActivity


/**
 * 充值
 * Created by Slingge on 2018/8/15
 */
object RechargeDialog {

      var builder: AlertDialog? = null

    fun communityDialog(context: Activity) {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_recharge, null)
        if (builder == null) {
            builder = AlertDialog.Builder(context, R.style.Dialog).create() // 先得到构造器
        }
        builder!!.show()
        builder!!.window.setContentView(view)

        val tv_cancel=view.findViewById<TextView>(R.id.tv_cancel)
        tv_cancel.setOnClickListener { v->
            builder!!.cancel()
        }
        val tv_enter=view.findViewById<TextView>(R.id.tv_enter)
        tv_enter.setOnClickListener { v->

        }

        val dialogWindow = builder!!.window
        dialogWindow.setGravity(Gravity.CENTER)//显示在底部
        val m = context.windowManager
        val d = m.defaultDisplay // 获取屏幕宽、高用
        val p = dialogWindow.attributes // 获取对话框当前的参数值
//        p.height = (d.height * 0.5).toInt() // 高度设置为屏幕的0.5
        p.width = (d.width * 0.92).toInt() // 宽度设置为屏幕宽
        dialogWindow.attributes = p

    }


}