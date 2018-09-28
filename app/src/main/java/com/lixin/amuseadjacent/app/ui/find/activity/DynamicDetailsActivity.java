package com.lixin.amuseadjacent.app.ui.find.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixin.amuseadjacent.R;
import com.lixin.amuseadjacent.app.MyApplication;
import com.lixin.amuseadjacent.app.ui.base.BaseActivity;
import com.lixin.amuseadjacent.app.ui.dialog.ProgressDialog;
import com.lixin.amuseadjacent.app.ui.find.adapter.DynamicCommentAdapter;
import com.lixin.amuseadjacent.app.ui.find.model.ActivityCommentModel1;
import com.lixin.amuseadjacent.app.ui.find.model.DynamiclDetailsModel;
import com.lixin.amuseadjacent.app.ui.find.request.DynaComment_133134;
import com.lixin.amuseadjacent.app.ui.find.request.Event_221222223224;
import com.lixin.amuseadjacent.app.ui.message.request.Mail_138139;
import com.lixin.amuseadjacent.app.ui.mine.activity.PersonalHomePageActivity;
import com.lixin.amuseadjacent.app.ui.mine.adapter.ImageAdapter;
import com.lixin.amuseadjacent.app.util.*;
import com.lixin.amuseadjacent.app.view.CircleImageView;
import com.lixin.amuseadjacent.zxing.decoding.Intents;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 使用的实体类中有 “object”kotlin关键字，所以只能用java类
 * 动态、帮帮详情
 * Created by Slingge on 2018/9/15
 */
public class DynamicDetailsActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView rv_comment, rv_image;
    private DynamicCommentAdapter commentAdapter;
    private ArrayList commentList = new ArrayList<ActivityCommentModel1.commModel>();

    private ArrayList imageList = new ArrayList<String>();

    private ImageView image0, image1, image2;
    private TextView tv_more, tv_send;

    private CircleImageView iv_header;
    private TextView tv_name, tv_effect, tv_follow, tv_info;
    private LinearLayout ll_image;
    private NiceVideoPlayer player;

    private TextView tv_time, tv_comment, tv_zan;
    private TextView tv_right, tv_title;
    private ImageView iv_back;

    private EditText et_comment;

    private String dynaId;
    private int zanNUm = 0;
    private String isZan = "0";//0未赞过 1已赞过
    private int commNum = 0;

    private String flag;// 0动态，1帮帮

    private DynamiclDetailsModel model;

    private int position = -1;//列表所在位置

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_details);
        AndroidBug5497Workaround.assistActivity(this);//底部EditText不能被软键盘顶起
        EventBus.getDefault().register(this);
        init();
    }


    private void init() {
        flag = getIntent().getStringExtra("flag");
        position = getIntent().getIntExtra("position", -1);
        dynaId = getIntent().getStringExtra("id");

        tv_right = findViewById(R.id.tv_right);
        tv_title = findViewById(R.id.tv_title);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        if (flag.equals("0")) {
            tv_title.setText("动态详情");
        } else {
            tv_right.setVisibility(View.VISIBLE);
            tv_title.setText("帮帮详情");
            tv_right.setText("收藏");
        }

        StatusBarWhiteColor();

        rv_comment = findViewById(R.id.rv_comment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_comment.setLayoutManager(linearLayoutManager);

        commentAdapter = new DynamicCommentAdapter(this, commentList);
        rv_comment.setAdapter(commentAdapter);
        commentAdapter.setId(dynaId);

        tv_zan = findViewById(R.id.tv_zan);
        image0 = findViewById(R.id.image0);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        tv_more = findViewById(R.id.tv_more);
        tv_send = findViewById(R.id.tv_send);

        tv_zan.setOnClickListener(this);
        image0.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);

        tv_more.setOnClickListener(this);
        tv_send.setOnClickListener(this);

        ConstraintLayout cl_1 = findViewById(R.id.cl_1);
        ConstraintLayout cl_2 = findViewById(R.id.cl_2);
        cl_1.setVisibility(View.VISIBLE);
        cl_2.setVisibility(View.GONE);

        iv_header = findViewById(R.id.iv_header);
        tv_name = findViewById(R.id.tv_name);
        tv_effect = findViewById(R.id.tv_effect);
        tv_follow = findViewById(R.id.tv_follow);
        tv_info = findViewById(R.id.tv_info);
        ll_image = findViewById(R.id.ll_image);
        player = findViewById(R.id.player);
        ll_image = findViewById(R.id.ll_image);
        tv_time = findViewById(R.id.tv_time);
        tv_comment = findViewById(R.id.tv_comment);

        et_comment = findViewById(R.id.et_comment);

        tv_follow.setOnClickListener(this);

        rv_image = findViewById(R.id.rv_image);
        rv_image.setLayoutManager(new GridLayoutManager(this, 3));

        rv_image.addOnItemTouchListener(new RecyclerItemTouchListener(rv_image) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                int i = vh.getAdapterPosition();
                if (i < 0 || i >= imageList.size()) {
                    return;
                }
                PreviewPhoto.INSTANCE.preview(DynamicDetailsActivity.this, imageList, i);
            }
        });

        ProgressDialog.INSTANCE.showDialog(this);
        DynaComment_133134.INSTANCE.dynamicDetail(dynaId);
    }


    @Subscribe
    public void onEvent(DynamiclDetailsModel model) {
        this.model = model;

        if (model.object.type.equals("0")) {//0动态 1帮帮
            tv_title.setText("动态详情");
        } else {
            tv_right.setVisibility(View.VISIBLE);
            tv_title.setText("帮帮详情");
            tv_right.setText("收藏");
        }

        ImageLoader.getInstance().displayImage(model.object.dynamicIcon, iv_header, ImageLoaderUtil.HeaderDIO());
        iv_header.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("auid", model.object.dynamicUid);
            bundle.putString("isAttention", model.object.isAttention);
            MyApplication.openActivity(DynamicDetailsActivity.this, PersonalHomePageActivity.class, bundle);
        });

        tv_name.setText(model.object.dynamicName);
        tv_effect.setText("影响力" + model.object.userEffectNum);
        tv_info.setText(model.object.dynamicContent);

        tv_time.setText(model.object.time);
        tv_comment.setText(model.object.commentNum);
        commNum = Integer.valueOf(model.object.commentNum);
        zanNUm = Integer.valueOf(model.object.zanNum);
        tv_zan.setText(model.object.zanNum);

        if (model.object.isAttention.equals("0")) {// 0未关注 1已关注
            tv_follow.setText("关注");
        } else {
            tv_follow.setText("已关注");
        }

        if (StaticUtil.INSTANCE.getUid() == model.object.dynamicUid) {
            tv_follow.setVisibility(View.INVISIBLE);
        }

        if (flag.equals("0")) {//0帮帮
            if (model.object.dynamicUid.equals(StaticUtil.INSTANCE.getUid())) {
                tv_right.setVisibility(View.VISIBLE);
                if (model.object.state.equals("0")) {//0正常显示 1隐藏
                    tv_right.setText("隐藏");
                } else {
                    tv_right.setText("已隐藏");
                }
            }
        } else {
            if (model.object.iscang.equals("0")) {//0未收藏 1已收藏
                tv_right.setText("收藏");
            } else {
                tv_right.setText("已收藏");
            }
        }

        isZan = model.object.isZan;
        if (model.object.isZan.equals("0")) {//0未赞过 1已赞过
            AbStrUtil.setDrawableLeft(this, R.drawable.ic_zan, tv_zan, 5);
        } else {
            AbStrUtil.setDrawableLeft(this, R.drawable.ic_zan_hl, tv_zan, 5);
        }

        if (TextUtils.isEmpty(model.object.dynamicVideo)) {
            player.setVisibility(View.GONE);
            if (model.object.dynamicImgList.size() > 0) {
                if (model.object.dynamicImgList.size() < 3) {
                    rv_image.setVisibility(View.GONE);
                    ll_image.setVisibility(View.VISIBLE);
                    if (model.object.dynamicImgList.size() == 1) {
                        image0.setVisibility(View.VISIBLE);

                        image1.setVisibility(View.GONE);
                        image2.setVisibility(View.GONE);

                        ImageLoader.getInstance().displayImage(model.object.dynamicImgList.get(0), image0);
                    } else {
                        image0.setVisibility(View.GONE);

                        image1.setVisibility(View.VISIBLE);
                        image2.setVisibility(View.VISIBLE);

                        ImageLoader.getInstance().displayImage(model.object.dynamicImgList.get(0), image1);
                        ImageLoader.getInstance().displayImage(model.object.dynamicImgList.get(1), image2);
                    }
                } else {
                    ll_image.setVisibility(View.GONE);
                    rv_image.setVisibility(View.VISIBLE);
                    ImageAdapter imageAdapter = new ImageAdapter(this, model.object.dynamicImgList, 0);
                    rv_image.setAdapter(imageAdapter);
                }
            } else {
                rv_image.setVisibility(View.GONE);
                ll_image.setVisibility(View.GONE);
            }
        } else {
            player.setVisibility(View.VISIBLE);
            player.setUp(model.object.dynamicVideo, null);
            TxVideoPlayerController controller = new TxVideoPlayerController(this);
            ImageLoader.getInstance().displayImage(model.object.dynamicImg, controller.imageView());
            player.setController(controller);
        }
        imageList = model.object.dynamicImgList;

        commentList.addAll(model.dataList);
        commentAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_follow:
                ProgressDialog.INSTANCE.showDialog(this);
                Mail_138139.INSTANCE.follow(model.object.dynamicUid, () -> {
                    if (model.object.isAttention.equals("0")) {// 0未关注 1已关注
                        model.object.isAttention = "1";
                        tv_follow.setText("已关注");
                    } else {
                        model.object.isAttention = "0";
                        tv_follow.setText("关注");
                    }
                });
                break;
            case R.id.tv_right:
                ProgressDialog.INSTANCE.showDialog(this);
                if (model.object.type.equals("0")) {//0动态 1帮帮
                    DynaComment_133134.INSTANCE.hide(dynaId, () -> {//隐藏自己的动态
                        if (model.object.state.equals("0")) {////0正常显示 1隐藏
                            tv_right.setText("已隐藏");
                            model.object.state = "1";
                        } else {
                            tv_right.setText("隐藏");
                            model.object.state = "0";
                        }
                    });
                } else {
                    Event_221222223224.INSTANCE.EventCollect("0", dynaId, () -> {
                        if (model.object.iscang.equals("0")) {//没有收藏
                            tv_right.setText("已收藏");
                            model.object.iscang = "1";
                        } else {
                            tv_right.setText("收藏");
                            model.object.iscang = "0";
                        }
                    });
                }
                break;
            case R.id.iv_back:
                back();
                break;
            case R.id.image0:
                PreviewPhoto.INSTANCE.preview(DynamicDetailsActivity.this, imageList, 0);
                break;
            case R.id.image1:
                PreviewPhoto.INSTANCE.preview(DynamicDetailsActivity.this, imageList, 0);
                break;
            case R.id.image2:
                PreviewPhoto.INSTANCE.preview(DynamicDetailsActivity.this, imageList, 1);
                break;
            case R.id.tv_zan:
                if (isZan.equals("1")) {
                    return;
                }
                ProgressDialog.INSTANCE.showDialog(this);
                DynaComment_133134.INSTANCE.zan(dynaId, "", () -> {
                    if (model.object.isZan.equals("1")) {
                        return;
                    }
                    zanNUm++;
                    model.object.isZan = "1";
                    model.object.zanNum = zanNUm + "";
                    tv_zan.setText(zanNUm + "");
                    AbStrUtil.setDrawableLeft(DynamicDetailsActivity.this, R.drawable.ic_zan_hl, tv_zan, 5);
                });
                break;
            case R.id.tv_send:
                String content = AbStrUtil.etTostr(et_comment);
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                ProgressDialog.INSTANCE.showDialog(this);
                DynaComment_133134.INSTANCE.comment(dynaId, "", content, (commentId) -> {
                    commNum++;
                    tv_comment.setText(commNum + "");
                    et_comment.setText("");

                    model.object.commentNum = commNum + "";
                    tv_comment.setText(commNum+"");

                    ActivityCommentModel1.commModel model = new ActivityCommentModel1.commModel();
                    model.setCommentContent(content);
                    model.setCommentIcon(StaticUtil.INSTANCE.getHeaderUrl());
                    model.setCommentTime(GetDateTimeUtil.getYMDHMS());
                    model.setSecondNum("0");
                    model.setCommentName(StaticUtil.INSTANCE.getNickName());
                    model.setZanNum("0");
                    model.setCommentId(commentId);
                    model.setCommentUid(StaticUtil.INSTANCE.getUid());
                    commentList.add(0, model);
                    commentAdapter.notifyDataSetChanged();
                });
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                back();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back() {
        Intent intent = new Intent();
        intent.putExtra("model", model);
        intent.putExtra("position", position);
        setResult(3, intent);
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        player.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.releasePlayer();
        EventBus.getDefault().unregister(this);
    }

}
