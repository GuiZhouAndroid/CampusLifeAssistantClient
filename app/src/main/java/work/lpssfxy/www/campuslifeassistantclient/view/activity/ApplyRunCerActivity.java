package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
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
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.backfragment.BackHandlerHelper;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyRunBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.DynamicTimeFormat;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.ApplyImgLookFragment;

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

    /** 父布局*/
    @BindView(R2.id.rl_apply_info) RelativeLayout mRlApplyInfo;
    /** 状态控件 */
    @BindView(R.id.apply_run_status_view) MultipleStatusView mApplyRunStatusView;
    /** Android振动器 */
    private Vibrator vibrator;

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
     * Android振动器
     */
    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    /**
     * 初始化当前用户跑腿认证信息
     */
    private void initNowUserApplyRunInfo() {
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
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "查询失败，用户不存在".equals(okGoApplyRunBean.getMsg())) {
                            return;
                        }
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "无申请信息".equals(okGoApplyRunBean.getMsg())) {
                            notApplyInfo();//无申请跑腿历史认证信息
                            return;
                        }
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "有历史申请信息".equals(okGoApplyRunBean.getMsg())) {
                            haveApplyInfo(okGoApplyRunBean.getData());//有申请跑腿历史认证信息
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyRunCerActivity.this, mRlApplyInfo, "请求错误，服务器连接失败！");
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
                finish();
            }
        });
        //子View中父布局view + 父布局点击事件
        RelativeLayout relativeLayout = mMultipleEmptyStatusView.getEmptyView().findViewById(R.id.rl_empty_apply_info);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(ApplyRunCerActivity.this, new Intent(ApplyRunCerActivity.this, ApplyRunCommitActivity.class));//执行动画跳转
                finish();
            }
        });
    }

    /**
     * 处理认证信息，初始化查询申请跑腿认证的状态
     *
     * @param data 申请跑腿认证信息
     */
    @SuppressLint("UseCompatLoadingForDrawables")
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
        // 点击标题栏返回
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplyRunCerActivity.this.finish();
            }
        });

        /* 3.内容布局设置认证申请 */

        //(1)获取下拉刷新view
        RefreshLayout mRefreshLayout = mApplyRunStatusView.getContentView().findViewById(R.id.refreshLayout);
        // 获取下拉刷新父View的上拉加载子view
        ClassicsHeader mClassicsHeader = (ClassicsHeader) mRefreshLayout.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()));//上次更新时间
        //mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM月dd日 HH时mm分", Locale.CHINA));//当前更新时间
        // 设置刷新参数和主题颜色
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        setThemeColor(mRefreshLayout, R.color.Light_Blue);
        //自动开启下拉刷新，调用接口业务+处理数据
        mRefreshLayout.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startVibrator();
                doApplyInfoRefresh(data, mRefreshLayout);
                ToastUtils.show("申请认证信息已刷新");
            }
        });
        // 执行上拉加载更多业务，此处无更多，演示效果
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //1秒自动关闭 + 延迟0.5秒吐司提示
                mRefreshLayout.finishLoadMore(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.show("没有更多申请信息啦");
                    }
                }, 500);
            }
        });
    }

    /**
     * 刷新申请信息
     *
     * @param data           个人信息数据
     * @param mRefreshLayout 下拉刷新
     */
    private void doApplyInfoRefresh(OkGoApplyRunBean.Data data, RefreshLayout mRefreshLayout) {
        /* 1.获取内容布局中所有view */
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

        /* 2.调用data种的用户ID查询真实姓名 */
        OkGo.<String>post(Constant.USER_SELECT_USER_INFO_BY_USER_ID + "/" + data.getArUserId())
                .tag("ID查用户信息")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
                        if (200 == okGoUserBeanData.getCode() && null != okGoUserBeanData.getData() && "success".equals(okGoUserBeanData.getMsg())) {
                            OkGoUserBean.Data userInfo = okGoUserBeanData.getData();
                            superApplyUser.setCenterTopString(userInfo.getUlRealname() + "(" + userInfo.getUlSex() + ")");//真实姓名
                            superApplyUser.setCenterBottomString(userInfo.getUlIdcard().replaceAll("(\\d{4})\\d{8}(\\w{6})", "$1*****$2"));//身份证号
                            superApplyUser.setRightTopString(userInfo.getUlUsername() + "(用户名)");//用户名
                            superApplyUser.setRightBottomString(userInfo.getUlTel());//联系电话
                        }
                    }
                });

        /* 3.申请日期 */
        superApplyData.setRightString(data.getCreateTime());
        /* 4.申请状态 */
        int applyResult = data.getArState();
        if (applyResult == 0) { //审核中
            superApplyState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checking));
            superApplyState.setCenterTopString("超管暂未处理");
            superApplyState.setCenterBottomString("请您耐心等待");
            superApplyState.setCenterTopTextColor(getResources().getColor(R.color.sienna));
            superApplyState.setCenterBottomTextColor(getResources().getColor(R.color.sienna));
        } else if (applyResult == 1) { //审核通过
            superApplyState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checkok));
            superApplyState.setCenterTopString("超管已处理");
            superApplyState.setCenterBottomString(data.getUpdateTime());//审核时间
            superApplyState.setCenterTopTextColor(getResources().getColor(R.color.Green));
            superApplyState.setCenterBottomTextColor(getResources().getColor(R.color.Green));
        } else if (applyResult == -1) { //审核失败
            superApplyState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checkno));
            superApplyState.setCenterTopString("超管已处理");
            superApplyState.setCenterBottomString(data.getUpdateTime());//审核时间
            superApplyState.setCenterTopTextColor(getResources().getColor(R.color.Red));
            superApplyState.setCenterBottomTextColor(getResources().getColor(R.color.Red));
        }
        /* 5.审核回复 */
        superApplyResult.setRightString(data.getArPostscript());
        /* 6.车辆信息 */
        superApplyCar.setRightString(data.getArCar());
        /* 7.毕业日期 */
        superApplyGraduationData.setRightString(data.getArGraduationData());
        /* 8.Glide加载OSS网络图片 + 本机图片 (占位图 + 无缓存) */
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE);

        // 申请类型：加载跑腿本机图片
        if (data.getArType() == 3) {
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
        // 查看核酸检测大图
        superNucleicPic.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将核酸检测URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_apply_show, ApplyImgLookFragment.getInstance("urlData", data.getArNucleicPic())).commit();
            }
        });
        // 加载健康码URL
        Glide.with(this).load(data.getArHealthCode()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superHealCode.setRightTvDrawableRight(resource);
            }
        });
        // 查看健康码大图
        superHealCode.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将健康码URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_apply_show, ApplyImgLookFragment.getInstance("urlData", data.getArHealthCode())).commit();
            }
        });

        // 加载行程码URL
        Glide.with(this).load(data.getArRunCode()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superRunCode.setRightTvDrawableRight(resource);
            }
        });

        // 查看行程码URL大图
        superRunCode.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将行程码URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_apply_show, ApplyImgLookFragment.getInstance("urlData", data.getArRunCode())).commit();
            }
        });

        // 加载学生证URL
        Glide.with(this).load(data.getArStuCard()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superStuCard.setRightTvDrawableRight(resource);
            }
        });
        // 查看学生证大图
        superStuCard.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将学生证URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_apply_show, ApplyImgLookFragment.getInstance("urlData", data.getArStuCard())).commit();
            }
        });
        /* 9.个人信息和图片加载完成后自动关系刷新 */
        mRefreshLayout.finishRefresh();

    }

    @Override
    protected void onDestroy() {
        //释放振动资源
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
    }

    /**
     * 监听Fragment分发事件，查看大图，返回上一页，而不是直接finish活动
     */
    @Override
    public void onBackPressed() {
        if (!BackHandlerHelper.handleBackPress(this)) {
            super.onBackPressed();
        }
    }
}
