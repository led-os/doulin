package com.lixin.amuseadjacent.app.ui.service.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PointF
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.lixin.amuseadjacent.R
import com.lixin.amuseadjacent.app.MyApplication
import com.lixin.amuseadjacent.app.ui.base.BaseActivity
import com.lixin.amuseadjacent.app.ui.dialog.ProgressDialog
import com.lixin.amuseadjacent.app.ui.dialog.ScreenFriendsPop.flag
import com.lixin.amuseadjacent.app.ui.dialog.ShopCartDialog
import com.lixin.amuseadjacent.app.ui.mine.activity.WebViewActivity
import com.lixin.amuseadjacent.app.ui.service.adapter.ShopLeftAdapter
import com.lixin.amuseadjacent.app.ui.service.adapter.ShopRightAdapter
import com.lixin.amuseadjacent.app.ui.service.model.DelCarModel
import com.lixin.amuseadjacent.app.ui.service.model.ShopCarModel
import com.lixin.amuseadjacent.app.ui.service.model.ShopGoodsListModel
import com.lixin.amuseadjacent.app.ui.service.model.ShopGoodsModel
import com.lixin.amuseadjacent.app.ui.service.request.OfficialShopGoodsList_35
import com.lixin.amuseadjacent.app.ui.service.request.ShopCar_12412537
import com.lixin.amuseadjacent.app.util.AbStrUtil
import com.lixin.amuseadjacent.app.util.BezierTypeEvaluator
import com.lixin.amuseadjacent.app.util.DoubleCalculationUtil
import com.lixin.amuseadjacent.app.util.RecyclerItemTouchListener
import com.lxkj.linxintechnologylibrary.app.util.ToastUtil
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_shop.*
import kotlinx.android.synthetic.main.include_basetop.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * 新鲜果蔬
 * Created by Slingge on 2018/8/30
 */
class OfficialShopActivity : BaseActivity(), View.OnClickListener, ShopRightAdapter.AddShopCar
        , ShopRightAdapter.ReduceShopCar, ShopCartDialog.PlusCallBack, ShopCartDialog.ReduceCallBack,
        ShopCartDialog.DelCallBack, ShopCartDialog.SettlementCallBack, ShopRightAdapter.ShopOnClickListtener {

    private var type = ""//0新鲜果蔬 1洗衣洗鞋 2超市便利

    private var shopLeftAdapter: ShopLeftAdapter? = null
    private var leftList = ArrayList<ShopGoodsListModel.dataModel>()

    private var rightAdapter: ShopRightAdapter? = null
    private var rightList = ArrayList<ShopGoodsModel.dataModel>()
    private var carList = ArrayList<ShopGoodsModel.dataModel>()
    private var linearLayoutManager2: LinearLayoutManager? = null

    private var bannerUrl = ""//顶部广告跳转
    private var topImgDetailUrlState = ""//图片点击详情链接状态 0 有效 1无效

    private var search = ""//搜索内容

    private var shopCartDialog: ShopCartDialog? = null//小购物车
    private var totalMoney = 0.0
    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        context = this
        this.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        EventBus.getDefault().register(this)
        init()
    }


    @SuppressLint("SetTextI18n")
    private fun init() {
        type = intent.getStringExtra("type")
        if (type == "0") {
            inittitle("新鲜果蔬")
        } else {
            inittitle("超市便利")
        }

        StatusBarWhiteColor()
        view_staus.visibility = View.GONE

        tv_right.visibility = View.VISIBLE
        tv_right.text = "详情"
        tv_right.setOnClickListener(this)

        tv_settlement.setOnClickListener(this)
        iv_car.setOnClickListener(this)
        tv_msgNum.setOnClickListener(this)
        iv_bg.setOnClickListener(this)
        tv_search.setOnClickListener(this)

        var linearLayoutManager1 = LinearLayoutManager(this)
        linearLayoutManager1.orientation = LinearLayoutManager.VERTICAL
        rv_left.layoutManager = linearLayoutManager1

        shopLeftAdapter = ShopLeftAdapter(this, leftList)
        rv_left.adapter = shopLeftAdapter

        rv_left.addOnItemTouchListener(object : RecyclerItemTouchListener(rv_left) {
            override fun onItemClick(vh: RecyclerView.ViewHolder?) {
                val i = vh!!.adapterPosition
                if (i < 0) {
                    return
                }
                search = ""
                title = leftList[i].firstCategoryName
                ProgressDialog.showDialog(this@OfficialShopActivity)
                OfficialShopGoodsList_35.ShopGoods(type, leftList[i].firstCategoryId, search, null)
//                smoothMoveToPosition(linearLayoutManager2!!, i)
                shopLeftAdapter!!.setSelect(i)
            }
        })

        linearLayoutManager2 = LinearLayoutManager(this)
        linearLayoutManager2!!.orientation = LinearLayoutManager.VERTICAL
        rv_right.layoutManager = linearLayoutManager2

        tv_money.text = "合计：￥ $totalMoney"

        shopCartDialog = ShopCartDialog(this, this, this, this)
        (shopCartDialog as ShopCartDialog).setType(type)
        ProgressDialog.showDialog(this)
//        OfficialShopGoodsList_35.shop(type)
        ShopCar_12412537.getCar() //先加载购物车数据，在加载商品数据，以便更改商品选择数
    }


    //添加到小购物车
    override fun addCar(position: Int) {
        rightList[position].isSelect = true
        rightList[position].goodsNum += 1
        rightAdapter!!.notifyItemChanged(position)

        var num = -1//没有在小购物车
        for (i in 0 until carList.size) {//增加数量
            if (carList[i].goodsId == rightList[position].goodsId) {
                num = i
                break
            }
        }
        if (num == -1) {
            carList.add(rightList[position])
        } else {
            carList[num].goodsNum = rightList[position].goodsNum
            carList[num].money = DoubleCalculationUtil.mul(carList[num].goodsNum.toDouble(), carList[num].UnitPrice)
        }
        carNum()
    }

    //从适配器中减少数量
    override fun reduceCar(position: Int) {
        rightList[position].isSelect = true
        rightList[position].goodsNum -= 1
        rightAdapter!!.notifyItemChanged(position)

        for (i in 0 until carList.size) {//增加数量
            if (carList[i].goodsId == rightList[position].goodsId) {
                carList[i].goodsNum = rightList[position].goodsNum
                if (carList[i].goodsNum == 0) {
                    carList.removeAt(i)
                } else {
                    carList[i].money = DoubleCalculationUtil.mul(carList[i].goodsNum.toDouble(), carList[i].UnitPrice)
                }
                break
            }
        }
        carNum()
    }


    //从购物车中增加
    override fun plus(position: Int, num: Int, money: Double, goodId: String) {
        super.plus(position, num, money, goodId)
        for (i in 0 until rightList.size) {
            if (rightList[i].goodsId == goodId) {
                rightList[i].goodsNum = num
                rightList[i].money = money
                rightAdapter!!.notifyItemChanged(i)
            }
        }
        carList[position].goodsNum = num
        carList[position].money = money

        carNum()
    }

    //从购物车中减少
    override fun reduce(position: Int, num: Int, money: Double, goodId: String) {
        super.reduce(position, num, money, goodId)
        for (i in 0 until rightList.size) {
            if (rightList[i].goodsId == goodId) {
                rightList[i].goodsNum = num
                rightList[i].money = money
                rightAdapter!!.notifyItemChanged(i)
                break
            }
        }
        carList[position].goodsNum = num
        carList[position].money = money

        carNum()
    }

    //从购物车中删除
    override fun del(position: Int, goodId: String) {
        super.del(position, goodId)
        val model = DelCarModel()
        model.type = "1"
        model.objId.add(carList[position].cartId)
        ShopCar_12412537.DelCar(model, object : ShopCar_12412537.DelCarCallback {
            override fun delCar() {
                for (i in 0 until rightList.size) {
                    if (rightList[i].goodsId == goodId) {
                        rightList[i].isSelect = false
                        rightList[i].goodsNum = 0
                        rightAdapter!!.notifyItemChanged(i)
                        break
                    }
                }
                carList.removeAt(position)
                if (carList.isEmpty()) {
                    shopCartDialog!!.dismiss()
                } else {
                    shopCartDialog!!.setGoodList(context as Activity, carList)
                }
                carNum()
            }

        })
    }

    //结算
    override fun Settlement() {
        super.Settlement()
        GoSettlement()
    }

    //购物车数量
    private fun carNum() {
        var num = 0
        totalMoney = 0.0
        for (i in 0 until carList.size) {
            num += carList[i].goodsNum
            totalMoney = DoubleCalculationUtil.add(carList[i].money, totalMoney)
        }
        tv_money.text = "合计：￥ $totalMoney"
        MyApplication.setRedNum(tv_msgNum, num)
    }

    /**
     * 接收购物车中删除动作
     */
    @Subscribe
    fun onEvent(goodsId: String) {
        for (i in 0 until rightList.size) {
            if (rightList[i].goodsId == goodsId) {
                rightList[i].isSelect = false
                rightList[i].goodsNum = 0
                rightAdapter!!.notifyItemChanged(i)
                break
            }
        }
        var position = 0
        for (i in 0 until carList.size) {
            if (TextUtils.equals(goodsId, carList[i].goodsId)) {
                position = i
            }
        }
        carList.removeAt(position)
        if (carList.isEmpty()) {
            shopCartDialog!!.dismiss()
        } else {
            shopCartDialog!!.setGoodList(context as Activity, carList)
        }
        carNum()
    }

    private var title = ""
    @Subscribe
    fun onEvent(model: ShopGoodsListModel) {
        leftList.addAll(model.dataList)
        var controller = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_from_bottom)
        rv_left.layoutAnimation = controller
        shopLeftAdapter!!.notifyDataSetChanged()
        rv_left.scheduleLayoutAnimation()
        if (leftList.isNotEmpty()) {
            title = leftList[0].firstCategoryName
            ProgressDialog.showDialog(this)
            OfficialShopGoodsList_35.ShopGoods(type, leftList[0].firstCategoryId, search, null)
        }

        bannerUrl = model.bannerList[0].topImgDetailUrl
        topImgDetailUrlState = model.bannerList[0].topImgDetailUrlState
        ImageLoader.getInstance().displayImage(model.bannerList[0].topImgUrl, iv_bg)
    }

    @Subscribe
    fun onEvent(model: ShopGoodsModel) {
        rightList.clear()
        rightList = model.dataList
        if (rightList.isEmpty()) {
            ToastUtil.showToast("暂无商品")
        }
        if (!carList.isEmpty()) {
            for (i in 0 until carList.size) {
                val goodsId = carList[i].goodsId
                for (j in 0 until rightList.size) {
                    if (TextUtils.equals(goodsId, rightList[j].goodsId)) {
                        rightList[j].goodsNum = carList[i].goodsNum
                        break
                    }
                }
            }
        }
        rightAdapter = ShopRightAdapter(this, title, rightList, this, this)
        if (rightAdapter!!.shoponclickListtener == null) {
            rightAdapter!!.setShopOnClickListtener(this)
        }
        rv_right.adapter = rightAdapter
        rightAdapter!!.setType(type)
    }

    @Subscribe
    fun onEvent(model: ShopCarModel) {
        OfficialShopGoodsList_35.shop(type)
        when (type) {
            "0" -> {
                if (model.fruitsList.size > 0) {
                    for (i: Int in 0 until model.fruitsList.size) {
                        val carInfo = model.fruitsList[i]
                        val goodsinfo = ShopGoodsModel.dataModel()
                        goodsinfo.cartId = carInfo.cartId
                        goodsinfo.goodsNum = carInfo.count.toInt()
                        goodsinfo.goodsCuprice = carInfo.goodsCuprice
                        goodsinfo.goodsPrice = carInfo.goodsPrice
                        goodsinfo.goodsId = carInfo.goodsId
                        goodsinfo.goodsName = carInfo.goodsTitle
                        goodsinfo.goodsImg = carInfo.goodsImage
                        goodsinfo.optimizationid = carInfo.optimizationid
                        goodsinfo.isSelect = true
                        if (TextUtils.isEmpty(carInfo.goodsCuprice)) {
                            goodsinfo.UnitPrice = carInfo.goodsPrice.toDouble()
                            goodsinfo.money = DoubleCalculationUtil.mul(carInfo.count.toDouble(), carInfo.goodsPrice.toDouble())
                        } else {
                            goodsinfo.UnitPrice = carInfo.goodsCuprice.toDouble()
                            goodsinfo.money = DoubleCalculationUtil.mul(carInfo.count.toDouble(), carInfo.goodsCuprice.toDouble())
                        }
                        if (goodsinfo.goodsNum > 0) {
                            carList.add(goodsinfo)
                        }
                    }
                }
            }

            "2" -> {
                if (model.marketList.size > 0) {
                    for (i: Int in 0 until model.marketList.size) {
                        val carInfo = model.marketList[i]
                        val goodsinfo = ShopGoodsModel.dataModel()
                        goodsinfo.cartId = carInfo.cartId
                        goodsinfo.goodsNum = carInfo.count.toInt()
                        goodsinfo.goodsPrice = carInfo.goodsPrice
                        goodsinfo.goodsCuprice = carInfo.goodsCuprice
                        goodsinfo.goodsId = carInfo.goodsId
                        goodsinfo.goodsName = carInfo.goodsTitle
                        goodsinfo.goodsImg = carInfo.goodsImage
                        goodsinfo.optimizationid = carInfo.optimizationid
                        goodsinfo.isSelect = true
                        if (TextUtils.isEmpty(carInfo.goodsCuprice)) {
                            goodsinfo.UnitPrice = carInfo.goodsPrice.toDouble()
                            goodsinfo.money = DoubleCalculationUtil.mul(carInfo.count.toDouble(), carInfo.goodsPrice.toDouble())
                        } else {
                            goodsinfo.UnitPrice = carInfo.goodsCuprice.toDouble()
                            goodsinfo.money = DoubleCalculationUtil.mul(carInfo.count.toDouble(), carInfo.goodsCuprice.toDouble())
                        }
                        if (goodsinfo.goodsNum > 0) {
                            carList.add(goodsinfo)
                        }
                    }
                }
            }
        }
        carNum()
    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.iv_car, R.id.tv_msgNum -> {
                if (carList.isEmpty()) {
                    ToastUtil.showToast("购物车还是空的")
                    return
                }
                shopCartDialog!!.shopCar(this, carList)
            }
            R.id.iv_bg -> {
                if (topImgDetailUrlState == "1") {
                    return
                }
                val bundle = Bundle()
                bundle.putString("title", "详情")
                bundle.putString("url", bannerUrl)
                MyApplication.openActivity(this, WebViewActivity::class.java, bundle)
            }
            R.id.tv_search -> {
                search = AbStrUtil.etTostr(et_search)
                if (TextUtils.isEmpty(search)) {
                    return
                }
                title = leftList[0].firstCategoryName
                ProgressDialog.showDialog(this)
                OfficialShopGoodsList_35.ShopGoods(type, "", search, null)
            }
            R.id.tv_right -> {
                val bundle = Bundle()
                bundle.putInt("flag", type.toInt())
                MyApplication.openActivity(this, OfficialShopDetailsActivity::class.java, bundle)
            }
            R.id.tv_settlement -> {
                GoSettlement()
            }
        }
    }

    private fun GoSettlement() {
        if (carList.isEmpty()) {
            ToastUtil.showToast("购物车还是空的")
            return
        }
        val bundle = Bundle()
        bundle.putString("type", type)
        bundle.putSerializable("list", carList)
        MyApplication.openActivity(this, SubmissionOrderActivity::class.java, bundle)

        carList.clear()
        shopCartDialog!!.setGoodList(this, carList)
        if (shopCartDialog != null) {
            shopCartDialog!!.dismiss()
        }
        tv_money.text = "合计：￥0.0"
        MyApplication.setRedNum(tv_msgNum, 0)
        for (i in 0 until rightList.size) {
            rightList[i].isSelect = false
            rightList[i].goodsNum = 0
            rightList[i].UnitPrice = if (TextUtils.isEmpty(rightList[i].goodsCuprice)) {
                rightList[i].goodsPrice.toDouble()
            } else {
                rightList[i].goodsCuprice.toDouble()
            }
        }
        rightAdapter!!.notifyDataSetChanged()
    }


    private var imageView: ImageView? = null
    //加入购物车
    override fun add(view: View, position: Int) {
        //贝塞尔起始数据点
        val startPosition = IntArray(2)
        //贝塞尔结束数据点
        val endPosition = IntArray(2)
        //控制点
        val recyclerPosition = IntArray(2)

        view.getLocationInWindow(startPosition)
        iv_car.getLocationInWindow(endPosition)
        coordinatorLayout.getLocationInWindow(recyclerPosition)

        val startF = PointF()
        val endF = PointF()
        val controllF = PointF()

        startF.x = startPosition[0].toFloat()
        startF.y = (startPosition[1] - recyclerPosition[1] + 30).toFloat()
        endF.x = endPosition[0].toFloat() + 45
        endF.y = (endPosition[1] - recyclerPosition[1] + 30).toFloat()
        controllF.x = endF.x
        controllF.y = startF.y

        if (imageView == null) {
            imageView = ImageView(this)
            rl_main.addView(imageView)
            imageView!!.setImageResource(R.drawable.ic_dian)
            imageView!!.layoutParams.width = 25
            imageView!!.layoutParams.height = 25
        }

        imageView!!.x = startF.x
        imageView!!.y = startF.y

        val valueAnimator = ValueAnimator.ofObject(BezierTypeEvaluator(controllF), startF, endF)
        valueAnimator.addUpdateListener { animation ->
            val pointF = animation.animatedValue as PointF
            imageView!!.x = pointF.x
            imageView!!.y = pointF.y
        }

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                imageView!!.visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
                imageView!!.visibility = View.VISIBLE
            }
        })

        val set = AnimatorSet()
        set.play(valueAnimator)
        set.duration = 800
        set.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        shopCartDialog!!.destroy()
        EventBus.getDefault().unregister(this)
    }


}