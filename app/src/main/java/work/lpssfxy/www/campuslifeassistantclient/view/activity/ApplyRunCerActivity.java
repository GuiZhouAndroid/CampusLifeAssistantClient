package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyRunBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/12/4
 * 描述：申请跑腿资格
 *
 * @author ZSAndroid
 * @create 2021-12-04-20:38
 */
@SuppressLint("NonConstantResourceId")
public class ApplyRunCerActivity extends BaseActivity {

    private static final String TAG = "ApplyRunCerActivity";

    /** 返回按钮 */
    @BindView(R2.id.iv_apply_run_back) ImageView mIvApplyRunBack;
    /** Toolbar */
    @BindView(R2.id.toolbar_apply_run) Toolbar mApplyRunToolbar;
    /** 状态控件 */
    @BindView(R.id.apply_run_status_view) MultipleStatusView mApplyRunStatusView;

    //多图片选择路径List集合
    public static List<String> imgPathList;

    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return false;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_apply_run_cer;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        /**判断Toolbar，开启主图标并显示title*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        /** 设置Toolbar */
        setSupportActionBar(mApplyRunToolbar);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {
        initNowUserApplyRunInfo();
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_apply_run_back})
    public void onApplyRunViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_apply_run_back://点击获取验证码
                finish();
                break;
        }
    }

    /**
     * 初始化当前用户跑腿认证信息
     */
    private void initNowUserApplyRunInfo() {
//        OkGo.<String>post(Constant.USER_DO_APPLY_RUN_BY_MY_INFO)
//                .tag("申请跑腿认证")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onStart(Request<String, ? extends Request> request) {
//                        MyXPopupUtils.getInstance().setShowDialog(ApplyRunCerActivity.this, "正在请求认证信息...");
//                    }
//
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        OkGoApplyRunBean okGoApplyRunBean = GsonUtil.gsonToBean(response.body(), OkGoApplyRunBean.class);
//                        Log.i("okGoApplyRunBean", okGoApplyRunBean.toString());
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "跑腿认证审核中".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "跑腿认证已审核".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "跑腿认证审核失败".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "查询失败，用户不存在".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "跑腿信息提交失败".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "跑腿信息提交成功".equals(okGoApplyRunBean.getMsg())) {
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        MyXPopupUtils.getInstance().setSmartDisDialog();
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        //OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyRunCerActivity.this, goTopNestedScrollView, "请求错误，服务器连接失败！");
//                    }
//                });

        OkGo.<String>post(Constant.USER_SELECT_APPLY_RUN_BY_SA_TOKEN_SESSION)
                .tag("当前用户申请信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        mApplyRunStatusView.showLoading();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoApplyRunBean okGoApplyRunBean = GsonUtil.gsonToBean(response.body(), OkGoApplyRunBean.class);
                        Log.i("okGoApplyRunBean", okGoApplyRunBean.toString());
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "查询失败，用户不存在".equals(okGoApplyRunBean.getMsg())) {
                            return;
                        }
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "无申请信息".equals(okGoApplyRunBean.getMsg())) {
                            notApplyInfo();//无申请跑腿历史认证信息
                            return;
                        }
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "有历史申请信息".equals(okGoApplyRunBean.getMsg())) {
                            Log.i("有历史申请信息", okGoApplyRunBean.getData().toString());
                            haveApplyInfo(okGoApplyRunBean.getData());
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyRunCerActivity.this, goTopNestedScrollView, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 无申请跑腿历史认证信息
     */
    private void notApplyInfo() {
        //加载状态当前View自定义空布局
        mApplyRunStatusView.showEmpty(R.layout.custom_apply_empty_layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //子View的自定义空布局
        MultipleStatusView mMultipleEmptyStatusView = mApplyRunStatusView.getEmptyView().findViewById(R.id.multiple_empty_status_view);
        mMultipleEmptyStatusView.showEmpty();

        //子View中图标view + 图片点击事件
        ImageView imageView = mMultipleEmptyStatusView.getEmptyView().findViewById(R.id.iv_empty_apply_run_state);
        imageView.setImageResource(R.mipmap.go_apply);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(ApplyRunCerActivity.this, new Intent(ApplyRunCerActivity.this, ApplyRunCommitActivity.class));//执行动画跳转
            }
        });
        //子View中父布局view + 父布局点击事件
        RelativeLayout relativeLayout = mMultipleEmptyStatusView.getEmptyView().findViewById(R.id.rl_empty_apply_info);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(ApplyRunCerActivity.this, new Intent(ApplyRunCerActivity.this, ApplyRunCommitActivity.class));//执行动画跳转
            }
        });
    }

    /**
     * 处理认证信息，初始化查询申请跑腿认证的状态
     *
     * @param data 申请跑腿认证信息
     */
    private void haveApplyInfo(OkGoApplyRunBean.Data data) {
        /* 1.加载状态自定义内容布局 */
        mApplyRunStatusView.showContent();
        /* 2.内容布局设置标题栏 */
        Toolbar toolbar = mApplyRunStatusView.getContentView().findViewById(R.id.toolbar_apply_run_detail_info);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        setSupportActionBar(toolbar);

        /* 3.内容布局设置认证申请 */

        //(1)获取内容布局中所有view
        //申请人
        SuperTextView superApplyUser = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_user);
        //申请类型
        SuperTextView superApplyType = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_type);
        //申请日期
        SuperTextView superApplyData = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_data);
        //申请状态
        SuperTextView superApplyState = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_state);
        //申请结果
        SuperTextView superApplyResult = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_result);
        //所属车辆
        SuperTextView superApplyCar = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_car);
        //毕业日期
        SuperTextView superApplyGraduationData = mApplyRunStatusView.getContentView().findViewById(R.id.super_apply_graduation_data);
        //核酸监测
        SuperTextView superNucleicPic = mApplyRunStatusView.getContentView().findViewById(R.id.super_nucleic_pic);
        //健康码
        SuperTextView superHealCode = mApplyRunStatusView.getContentView().findViewById(R.id.super_heal_code);
        //行程码
        SuperTextView superRunCode = mApplyRunStatusView.getContentView().findViewById(R.id.super_run_code);
        //学生证
        SuperTextView superStuCard = mApplyRunStatusView.getContentView().findViewById(R.id.super_stu_card);

        //(2)调用data种的用户ID查询真实姓名
        OkGo.<String>post(Constant.USER_SELECT_USER_INFO_BY_USER_ID + "/" + data.getArUserId())
                .tag("ID查用户信息")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
                        if (200 == okGoUserBeanData.getCode() && null != okGoUserBeanData.getData() && "success".equals(okGoUserBeanData.getMsg())) {
                            OkGoUserBean.Data userInfo = okGoUserBeanData.getData();
                            superApplyUser.setCenterTopString(userInfo.getUlRealname());//真实姓名
                            superApplyUser.setCenterBottomString(userInfo.getUlIdcard().replaceAll("(\\d{4})\\d{8}(\\w{6})", "$1*****$2"));//身份证号
                            superApplyUser.setRightString(userInfo.getUlTel());//联系电话
                        }
                    }
                });
        int applyResult = data.getArState();
        if (applyResult == 0) { //审核中
            superApplyState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checking));
        } else if (applyResult == 1) { //审核通过
            superApplyState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checkok));
            superApplyState.setCenterBottomString("已审核认证");//失败原因
        } else if (applyResult == -1) { //审核失败
            superApplyState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checkno));
            superApplyState.setCenterBottomString(data.getArPostscript());//失败原因
        }

        // (4)申请日期
        superApplyData.setRightString(data.getCreateTime());
        // (5)申请结果
        superApplyResult.setRightString(data.getArPostscript());
        // (6)所属车辆
        superApplyCar.setRightString(data.getArCar());
        // (7)毕业日期
        superApplyGraduationData.setRightString(data.getArGraduationData());

        // (8)Glide加载OSS网络图片 + 本机图片 (占位图 + 无缓存)
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE);

        // 申请类型：加载跑腿本机图片
        if (data.getArType() == 1) {
            Glide.with(this).load(R.mipmap.run).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    superApplyType.setRightTvDrawableRight(resource);
                }
            });
        }

        // 加载核酸检测URL
        Glide.with(this).load(data.getArNucleicPic()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superNucleicPic.setRightTvDrawableRight(resource);
            }
        });

        // 加载健康码URL
        Glide.with(this).load(data.getArHealthCode()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superHealCode.setRightTvDrawableRight(resource);
            }
        });

        // 加载行程码URL
        Glide.with(this).load(data.getArRunCode()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superRunCode.setRightTvDrawableRight(resource);
            }
        });

        // 加载学生证URL
        Glide.with(this).load(data.getArStuCard()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superStuCard.setRightTvDrawableRight(resource);
            }
        });

    }
}
