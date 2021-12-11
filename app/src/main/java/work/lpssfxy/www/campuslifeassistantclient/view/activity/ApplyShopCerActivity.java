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
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyShopBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.DynamicTimeFormat;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.ApplyImgLookFragment;

/**
 * created by on 2021/12/11
 * 描述：申请商家入驻资格
 *
 * @author ZSAndroid
 * @create 2021-12-11-19:03
 */
@SuppressLint("NonConstantResourceId")
public class ApplyShopCerActivity extends BaseActivity {

    private static final String TAG = "ApplyShopCerActivity";

    /** 父布局*/
    @BindView(R2.id.rl_apply_shop_info) RelativeLayout mRlApplyShopInfo;
    /** 状态控件 */
    @BindView(R.id.apply_shop_status_view) MultipleStatusView mApplyShopStatusView;
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
        return R.layout.activity_apply_shop_cer;
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
        initNowUserApplyShopInfo();
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
     * 初始化当前用户商家入驻认证信息
     */
    private void initNowUserApplyShopInfo() {
        OkGo.<String>post(Constant.USER_SELECT_APPLY_SHOP_BY_SA_TOKEN_SESSION)
                .tag("当前用户申请信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        mApplyShopStatusView.showLoading();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoApplyShopBean okGoApplyShopBean = GsonUtil.gsonToBean(response.body(), OkGoApplyShopBean.class);
                        if (200 == okGoApplyShopBean.getCode() && okGoApplyShopBean.getData() == null && "无申请信息".equals(okGoApplyShopBean.getMsg())) {
                            notApplyShopInfo();//无申请商家入驻历史认证信息
                            return;
                        }
                        if (200 == okGoApplyShopBean.getCode() && okGoApplyShopBean.getData() != null && "有历史申请信息".equals(okGoApplyShopBean.getMsg())) {
                            haveApplyShopInfo(okGoApplyShopBean.getData());//有申请商家入驻历史认证信息
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyShopCerActivity.this, mRlApplyShopInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 无申请商家入驻历史认证信息
     */
    private void notApplyShopInfo() {
        //加载状态当前View自定义空布局
        mApplyShopStatusView.showEmpty(R.layout.custom_apply_run_or_shop_empty_layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //子View的自定义空布局
        MultipleStatusView mMultipleEmptyStatusView = mApplyShopStatusView.getEmptyView().findViewById(R.id.multiple_empty_status_view);
        mMultipleEmptyStatusView.showEmpty();

        //子View中图标view + 图片点击事件
        ImageView imageView = mMultipleEmptyStatusView.getEmptyView().findViewById(R.id.iv_empty_apply_run_state);
        imageView.setImageResource(R.mipmap.go_apply);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(ApplyShopCerActivity.this, new Intent(ApplyShopCerActivity.this, ApplyShopCommitActivity.class));//执行动画跳转
                finish();
            }
        });
        //子View中父布局view + 父布局点击事件
        RelativeLayout relativeLayout = mMultipleEmptyStatusView.getEmptyView().findViewById(R.id.rl_empty_apply_info);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(ApplyShopCerActivity.this, new Intent(ApplyShopCerActivity.this, ApplyShopCommitActivity.class));//执行动画跳转
                finish();
            }
        });
    }

    /**
     * 有申请商家入驻历史认证信息
     *
     * @param data 历史认证信息
     */
    private void haveApplyShopInfo(OkGoApplyShopBean.Data data) {
        /* 1.加载状态自定义内容布局 */
        mApplyShopStatusView.showContent();
        /* 2.内容布局设置标题栏 */
        Toolbar toolbar = mApplyShopStatusView.getContentView().findViewById(R.id.toolbar_apply_shop_detail_info);
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
                ApplyShopCerActivity.this.finish();
            }
        });

        /* 3.内容布局设置认证申请 */

        //(1)获取下拉刷新view
        RefreshLayout mRefreshLayoutShop = mApplyShopStatusView.getContentView().findViewById(R.id.refreshLayout_shop);
        // 获取下拉刷新父View的上拉加载子view
        ClassicsHeader mClassicsHeader = (ClassicsHeader) mRefreshLayoutShop.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()));//上次更新时间
        //mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM月dd日 HH时mm分", Locale.CHINA));//当前更新时间
        // 设置刷新参数和主题颜色
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        setThemeColor(mRefreshLayoutShop, R.color.Light_Blue);
        //自动开启下拉刷新，调用接口业务+处理数据
        mRefreshLayoutShop.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayoutShop.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startVibrator();
                doApplyShopInfoRefresh(data, mRefreshLayoutShop);
                ToastUtils.show("申请认证信息已刷新");
            }
        });
        // 执行上拉加载更多业务，此处无更多，演示效果
        mRefreshLayoutShop.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //1秒自动关闭 + 延迟0.5秒吐司提示
                mRefreshLayoutShop.finishLoadMore(500);
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
    private void doApplyShopInfoRefresh(OkGoApplyShopBean.Data data, RefreshLayout mRefreshLayout) {
        /* 1.获取内容布局中所有view */

        //申请人
        SuperTextView superApplyShopUser = mApplyShopStatusView.getContentView().findViewById(R.id.super_apply_shop_user);
        //申请类型
        SuperTextView superApplyShopType = mApplyShopStatusView.getContentView().findViewById(R.id.super_apply_shop_type);
        //申请日期
        SuperTextView superApplyShopData = mApplyShopStatusView.getContentView().findViewById(R.id.super_apply_shop_data);
        //申请状态
        SuperTextView superApplyShopState = mApplyShopStatusView.getContentView().findViewById(R.id.super_apply_shop_state);
        //餐饮许可证
        SuperTextView superShopLicencePic = mApplyShopStatusView.getContentView().findViewById(R.id.super_shop_licence_pic);

        /* 2.调用data种的用户ID查询真实姓名 */
        OkGo.<String>post(Constant.USER_SELECT_USER_INFO_BY_USER_ID + "/" + data.getAsUserId())
                .tag("ID查用户信息")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
                        if (200 == okGoUserBeanData.getCode() && null != okGoUserBeanData.getData() && "success".equals(okGoUserBeanData.getMsg())) {
                            OkGoUserBean.Data userInfo = okGoUserBeanData.getData();
                            superApplyShopUser.setCenterTopString(userInfo.getUlRealname() + "(" + userInfo.getUlSex() + ")");//真实姓名
                            superApplyShopUser.setCenterBottomString(userInfo.getUlIdcard().replaceAll("(\\d{4})\\d{8}(\\w{6})", "$1*****$2"));//身份证号
                            superApplyShopUser.setRightTopString(userInfo.getUlUsername() + "(用户名)");//用户名
                            superApplyShopUser.setRightBottomString(userInfo.getUlTel());//联系电话
                        }
                    }
                });

        /* 3.申请日期 */
        superApplyShopData.setRightString(data.getCreateTime());
        /* 4.申请状态 */
        int applyResult = data.getAsState();
        if (applyResult == 0) { //审核中
            superApplyShopState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checking));
            superApplyShopState.setCenterTopString("超管暂未处理");
            superApplyShopState.setCenterBottomString("请您耐心等待");
            superApplyShopState.setCenterTopTextColor(getResources().getColor(R.color.sienna));
            superApplyShopState.setCenterBottomTextColor(getResources().getColor(R.color.sienna));
        } else if (applyResult == 1) { //审核通过
            superApplyShopState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checkok));
            superApplyShopState.setCenterTopString("超管已处理");
            superApplyShopState.setCenterBottomString(data.getUpdateTime());//审核时间
            superApplyShopState.setCenterTopTextColor(getResources().getColor(R.color.Green));
            superApplyShopState.setCenterBottomTextColor(getResources().getColor(R.color.Green));
        } else if (applyResult == -1) { //审核失败
            superApplyShopState.setRightTvDrawableRight(getResources().getDrawable(R.mipmap.checkno));
            superApplyShopState.setCenterTopString("超管已处理");
            superApplyShopState.setCenterBottomString(data.getUpdateTime());//审核时间
            superApplyShopState.setCenterTopTextColor(getResources().getColor(R.color.Red));
            superApplyShopState.setCenterBottomTextColor(getResources().getColor(R.color.Red));
        }

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE);
        // 申请类型：加载跑腿本机图片
        if (data.getAsType() == 4) {
            Glide.with(this).load(R.mipmap.shop).apply(options).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    superApplyShopType.setRightTvDrawableRight(resource);
                }
            });
        }

        // 加载餐饮许可证URL
        Glide.with(this).load(data.getAsLicence()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superShopLicencePic.setRightTvDrawableRight(resource);
            }
        });
        // 查看餐饮许可证大图
        superShopLicencePic.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将餐饮许可证URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_apply_shop_show, ApplyImgLookFragment.getInstance("urlData", data.getAsLicence())).commit();
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
