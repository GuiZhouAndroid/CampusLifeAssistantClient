package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoShopStoreBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserStoreInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.DynamicTimeFormat;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.ApplyImgLookFragment;

/**
 * created by on 2021/12/12
 * 描述：店铺管理
 *
 * @author ZSAndroid
 * @create 2021-12-12-12:02
 */
@SuppressLint("NonConstantResourceId")
public class MyStoreManagerActivity extends BaseActivity {

    private static final String TAG = "MyStoreManagerActivity";
    /** 父布局*/
    @BindView(R2.id.rl_my_store_manager_info) RelativeLayout mRlMyStoreManagerInfo;
    /** 状态控件 */
    @BindView(R.id.my_store_manager_status_view) MultipleStatusView mMyStoreManagerStatusView;
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
        return R.layout.activity_my_store_manager;
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
        initNowUserShopStoreInfo();//初始化当前用户店铺信息
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
     * 初始化店铺信息
     */
    private void initNowUserShopStoreInfo() {
        OkGo.<String>post(Constant.SHOP_SELECT_STORE_INFO_BY_SA_TOKEN)
                .tag("当前用户店铺信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        mMyStoreManagerStatusView.showLoading();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoShopStoreBean okGoShopStoreBean = GsonUtil.gsonToBean(response.body(), OkGoShopStoreBean.class);
                        if (200 == okGoShopStoreBean.getCode() && okGoShopStoreBean.getData() == null && "无商铺信息".equals(okGoShopStoreBean.getMsg())) {
                            notShopStoreInfo();//无商铺信息
                            return;
                        }
                        if (200 == okGoShopStoreBean.getCode() && okGoShopStoreBean.getData() != null && "有商铺信息".equals(okGoShopStoreBean.getMsg())) {
                            OkGo.<String>post(Constant.SELECT_USER_SHOP_STORE_INFO_BY_SA_TOKEN_TO_USERID)
                                    .tag("商家用户ID查询该用户全部商铺信息")
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            OkGoUserStoreInfoBean okGoUserStoreInfoBean = GsonUtil.gsonToBean(response.body(), OkGoUserStoreInfoBean.class);
                                            if (200 == okGoUserStoreInfoBean.getCode() && null != okGoUserStoreInfoBean.getData() && "success".equals(okGoUserStoreInfoBean.getMsg())) {
                                                haveShopStoreInfo(okGoUserStoreInfoBean.getData());
                                            }
                                        }
                                        @Override
                                        public void onError(Response<String> response) {
                                            OkGoErrorUtil.CustomFragmentOkGoError(response, MyStoreManagerActivity.this, mRlMyStoreManagerInfo, "请求错误，服务器连接失败！");
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, MyStoreManagerActivity.this, mRlMyStoreManagerInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 无商铺信息
     */
    private void notShopStoreInfo() {
        //加载状态当前View自定义空布局
        mMyStoreManagerStatusView.showEmpty(R.layout.custom_my_store_empty_layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //子View的自定义空布局
        MultipleStatusView mMultipleEmptyMyStoreStatusView = mMyStoreManagerStatusView.getEmptyView().findViewById(R.id.multiple_empty_my_store_status_view);
        mMultipleEmptyMyStoreStatusView.showEmpty();

        //子View中父布局view + 父布局点击事件
        RelativeLayout mrlEmptyMyStoreInfo = mMultipleEmptyMyStoreStatusView.getEmptyView().findViewById(R.id.rl_empty_my_store_info);
        mrlEmptyMyStoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(MyStoreManagerActivity.this, new Intent(MyStoreManagerActivity.this, MyCreateStoreActivity.class));//执行动画跳转
                finish();
            }
        });
    }

    /**
     * 有商铺信息
     * @param data 商铺信息
     */
    private void haveShopStoreInfo(OkGoUserStoreInfoBean.Data data) {
        ToastUtils.show(data);
        /* 1.加载状态自定义内容布局 */
        mMyStoreManagerStatusView.showContent();
        //点击标题栏返回
        mMyStoreManagerStatusView.getContentView().findViewById(R.id.iv_store_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyStoreManagerActivity.this.finish();
            }
        });

        /* 2.下拉刷新 */
        //(1)获取下拉刷新view
        RefreshLayout mRefreshLayoutStore = mMyStoreManagerStatusView.getContentView().findViewById(R.id.refreshLayout_store);
        // 获取下拉刷新父View的上拉加载子view
        ClassicsHeader mClassicsHeader = (ClassicsHeader) mRefreshLayoutStore.getRefreshHeader();
        //mClassicsHeader.setLastUpdateTime(new Date(System.currentTimeMillis()));//上次更新时间
        //mClassicsHeader.setTimeFormat(new SimpleDateFormat("更新于 MM月dd日 HH时mm分", Locale.CHINA));//当前更新时间
        // 设置刷新参数和主题颜色
        mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        setThemeColor(mRefreshLayoutStore, R.color.welcome_colorMain);
        //自动开启下拉刷新，调用接口业务+处理数据
        mRefreshLayoutStore.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayoutStore.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startVibrator();
                doStoreInfoRefresh(data, mRefreshLayoutStore);
                ToastUtils.show("商铺信息已刷新");
            }
        });
        // 执行上拉加载更多业务，此处无更多，演示效果
        mRefreshLayoutStore.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //1秒自动关闭 + 延迟0.5秒吐司提示
                mRefreshLayoutStore.finishLoadMore(500);
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
     * 刷新商铺信息
     *
     * @param data           商铺信息数据
     * @param mRefreshLayoutStore 下拉刷新
     */
    private void doStoreInfoRefresh(OkGoUserStoreInfoBean.Data data, RefreshLayout mRefreshLayoutStore) {
        /* 1.获取内容布局中所有view */

        //店主信息
        SuperTextView superMyInfo = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_my_info);
        //门店实拍图
        SuperTextView superStorePic = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_pic);
        //门店Logo图
        SuperTextView superStoreLogo = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_logo);
        //店铺分类
        SuperTextView superStoreCategory = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_category);
        //商铺名称
        SuperTextView superStoreName = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_name);
        //商铺公告
        SuperTextView superStoreNotice = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_notice);
        //商铺具体地点
        SuperTextView superStoreAddress = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_address);
        //联系电话
        SuperTextView superStoreMobile = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_mobile);
        //推荐商品
        SuperTextView superStoreRecommend = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_recommend);
        //商铺所属校区
        SuperTextView superStoreDesc = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_desc);
        //营业开始时间
        SuperTextView superStoreBeginTime = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_begin_time);
        //营业结束时间
        SuperTextView superStoreEndTime = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_end_time);
        //开店日期
        SuperTextView superStoreCreate = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_create);
        //更新日期
        SuperTextView superStoreUpdate = mMyStoreManagerStatusView.getContentView().findViewById(R.id.super_store_update);

        /* 2.设置信息 */

        //设置店主信息
        superMyInfo.setCenterTopString(data.getUlRealname());//真实姓名
        superMyInfo.setCenterBottomString(data.getUlIdcard().replaceAll("(\\d{4})\\d{8}(\\w{6})", "$1*****$2"));//身份证号
        superMyInfo.setRightTopString(data.getUlUsername() + "(用户名)");//用户名
        superMyInfo.setRightBottomString(data.getUlTel());//联系电话

        //设置门店实图
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.placeholder).diskCacheStrategy(DiskCacheStrategy.NONE);
        // 加载门店实图URL
        Glide.with(this).load(data.getSsPic()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superStorePic.setRightTvDrawableRight(resource);
            }
        });
        //查看门店实图
        superStorePic.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将门店实图URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_store_show, ApplyImgLookFragment.getInstance("urlData", data.getSsPic())).commit();
            }
        });

        //设置店铺Logo图
        // 加载店铺Logo图URL
        Glide.with(this).load(data.getSsLogo()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                superStoreLogo.setRightTvDrawableRight(resource);
            }
        });
        //查看店铺Logo图
        superStoreLogo.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                //单例模式将店铺Logo图URL传到Fragment中
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_store_show, ApplyImgLookFragment.getInstance("urlData", data.getSsLogo())).commit();
            }
        });

        /* 设置店铺分类 */
        superStoreCategory.setRightString("[编号" + data.getScId()+"]"+data.getScName());

        /* 设置商铺名称 */
        superStoreName.setRightString(data.getSsName());

        /* 设置商铺公告 */
        superStoreNotice.setRightString(data.getSsNotice());

        /* 设置商铺详细地址 */
        superStoreAddress.setRightString(data.getSsAddress());

        /* 设置商铺联系电话 */
        superStoreMobile.setRightString(data.getSsMobile());

        /* 设置商铺推荐商品 */
        superStoreRecommend.setRightString(data.getSsRecommend());

        /* 设置商铺所属校区 */
        superStoreDesc.setRightString(data.getSsDesc());

        /* 设置商铺开始营业时间 */
        superStoreBeginTime.setRightString(data.getSsBeginTime());

        /* 设置商铺结束营业时间 */
        superStoreEndTime.setRightString(data.getSsEndTime());

        /* 设置商铺开店日期 */
        superStoreCreate.setRightString(data.getCreateTime());

        /* 设置商铺梗系日期 */
        superStoreUpdate.setRightString(data.getUpdateTime());

        /* 3.商铺信息加载完成后自动关闭刷新 */
        mRefreshLayoutStore.finishRefresh();

        /* 4.设置item监听事件，修改商铺信息 */

        //修改店铺分类
        superStoreCategory.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改商铺名称
        superStoreName.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改商铺公告
        superStoreNotice.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改店铺详细地址
        superStoreAddress.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改店铺联系电话
        superStoreMobile.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改店铺推荐商品
        superStoreRecommend.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改店铺所属校区
        superStoreDesc.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改店铺开始营业时间
        superStoreBeginTime.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
        //修改店铺结束营业时间
        superStoreEndTime.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                ToastUtils.show(superTextView.getRightString());
            }
        });
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
