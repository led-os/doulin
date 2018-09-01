package com.lixin.amuseadjacent.app.ui.mine.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.lixin.amuseadjacent.R
import com.lixin.amuseadjacent.app.MyApplication
import com.lixin.amuseadjacent.app.ui.base.BaseActivity
import com.lixin.amuseadjacent.app.ui.message.adapter.FragmentPagerAdapter
import com.lixin.amuseadjacent.app.ui.mine.adapter.ImageAdapter
import com.lixin.amuseadjacent.app.ui.mine.fragment.DataFragment
import com.lixin.amuseadjacent.app.ui.mine.fragment.DesignerFragment
import com.lixin.amuseadjacent.app.ui.mine.fragment.DynamicFragment
import com.lixin.amuseadjacent.app.ui.mine.fragment.InteractionFragment
import kotlinx.android.synthetic.main.activity_personal_home_page.*
import kotlinx.android.synthetic.main.include_basetop.*
import java.util.ArrayList

/**
 * Created by Slingge on 2018/8/16
 */
class PersonalHomePageActivity : BaseActivity() {

    private var flag = 0//0自己查看自己的，1查看别人的

    private var imgaeAdapter: ImageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_home_page)
        init()
    }


    private fun init() {
        inittitle("个人主页")
        StatusBarWhiteColor()
        view_staus.visibility = View.GONE

        if (flag == 0) {
            iv_right.visibility = View.VISIBLE
            iv_right.setImageResource(R.drawable.ic_edit2)
            iv_right.setOnClickListener { v ->
                MyApplication.openActivity(this, PersonalDataActivity::class.java)
            }
            cl_3.visibility = View.GONE
        }

        val tabList = ArrayList<String>()
        tabList.add("资料")
        tabList.add("动态")
        tabList.add("达人")
        tabList.add("互动")

        val list = ArrayList<Fragment>()
        val fragment = DataFragment()
        list.add(fragment)

        val fragment2 = DynamicFragment()
        list.add(fragment2)

        val fragment3 = DesignerFragment()
        list.add(fragment3)

        val fragment4 = InteractionFragment()
        list.add(fragment4)


        val adapter = FragmentPagerAdapter(supportFragmentManager, list, tabList)
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)

        val gridLayoutManager = GridLayoutManager(this, 4)
        rv_album.layoutManager = gridLayoutManager
        imgaeAdapter = ImageAdapter(this)
        rv_album.adapter = imgaeAdapter
    }

}