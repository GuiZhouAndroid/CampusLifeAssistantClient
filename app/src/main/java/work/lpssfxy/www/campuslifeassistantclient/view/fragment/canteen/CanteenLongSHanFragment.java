package work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseCanteenStoreInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.CustomLoadMoreView;
import work.lpssfxy.www.campuslifeassistantclient.base.custompopup.CustomLongSHanFilterDrawerPopupView;
import work.lpssfxy.www.campuslifeassistantclient.base.openmap.AddressInfo;
import work.lpssfxy.www.campuslifeassistantclient.base.openmap.BottomSheetPop;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.StoreInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllShopInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XXPermissionsAction;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.CanteenRunBuyActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.RepastPracticeActivity;

/**
 * created by on 2021/12/14
 * 描述：龙山食堂
 *
 * @author ZSAndroid
 * @create 2021-12-14-21:31
 */
@SuppressLint("NonConstantResourceId")
public class CanteenLongSHanFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R2.id.ll_long_sHan_show) LinearLayout mLlLongSHanShow;//父布局
    @BindView(R2.id.tv_all_long_sHan) TextView mTvAllLongSHan;//综合
    @BindView(R2.id.tv_price_long_sHan) TextView mTvPriceLongSHan;//价格
    @BindView(R2.id.tv_sales_long_sHan) TextView mTvSalesLongSHan;//销量
    @BindView(R2.id.tv_filter_long_sHan) TextView mTvFilterLongSHan;//筛选
    @BindView(R2.id.refreshLayout_long_sHan) RefreshLayout mRefreshLayoutLongSHan;
    @BindView(R2.id.recyclerView_long_sHan) RecyclerView mRecyclerViewLongSHan;

    private BaseCanteenStoreInfoAdapter storeInfoAdapter;//店铺信息适配器

    /** 设置导航经纬度 */
    AddressInfo mInfo = new AddressInfo();
    /** 底部弹出——第三方地图 */
    private BottomSheetPop mBottomSheetPop;
    private View openBottomView;
    private LinearLayout mLl_bottom_baidu,mLl_bottom_gaode,mLl_bottom_tencent,mLl_bottom_cancel;//百度+高德+腾讯+取消

    /**
     * @return 单例对象
     */
    public static CanteenLongSHanFragment newInstance() {
        return new CanteenLongSHanFragment();
    }

    @Override
    protected void lazyLoad() {
        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在加载信息...");
        startGetAllShopStoreInfo();//进入页面获取全部店铺信息
        MyXPopupUtils.getInstance().setSmartDisDialog();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_cantten_long_shan;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        //设置你目的地的经纬度
        mInfo.setLat(26.576731);//纬度
        mInfo.setLng(104.820488);//经度
    }

    @Override
    protected void initView(View rootView) {
        initSelectAllStoreInfoRecyclerView();//初始化全部店铺内容适配器
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(getActivity());

    }

    /**
     * 初始化全部店铺内容适配器
     */
    private void initSelectAllStoreInfoRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewLongSHan.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        //3个地图id
        openBottomView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_index_open_bottom_map_navagation, null);
        mLl_bottom_baidu = openBottomView.findViewById(R.id.ll_bottom_baidu);//底部百度
        mLl_bottom_gaode = openBottomView.findViewById(R.id.ll_bottom_gaode);//底部高德
        mLl_bottom_tencent = openBottomView.findViewById(R.id.ll_bottom_tencent);//底部腾讯
        mLl_bottom_cancel = openBottomView.findViewById(R.id.ll_bottom_cancel);//取消
    }

    @Override
    protected void initEvent() {
        /** 设置底部弹窗监听事件 */
        mLl_bottom_baidu.setOnClickListener(this);
        mLl_bottom_gaode.setOnClickListener(this);
        mLl_bottom_tencent.setOnClickListener(this);
        mLl_bottom_cancel.setOnClickListener(this);
    }

    @Override
    protected void doBusiness(Context context) {
        initSetRefreshListener();//初始化下拉刷新店铺信息
    }

    @OnClick({R2.id.tv_all_long_sHan,R2.id.tv_price_long_sHan,R2.id.tv_sales_long_sHan,R2.id.tv_filter_long_sHan})
    public void onLongSHanViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_long_sHan://点击综合
                doAllClick();
                break;
            case R.id.tv_price_long_sHan://点击价格
                doPriceClick();
                break;
            case R.id.tv_sales_long_sHan://点击销量
                doSalesClick();
                break;
            case R.id.tv_filter_long_sHan://点击筛选
                doFilterClick();
                break;
        }
    }

    /**
     * 监听单击底部弹窗第三方地图导航
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        mBottomSheetPop.dismiss();
        switch (view.getId()) {
            case R.id.ll_bottom_baidu://打开百度地图
                openMapBaidu();
                break;
            case R.id.ll_bottom_gaode://打开高德地图
                openMapGaoDe();
                break;
            case R.id.ll_bottom_tencent://打开腾讯地图
                openMapTencent();
                break;
            case R.id.ll_bottom_cancel://取消打开地图
                if (mBottomSheetPop != null) {
                    mBottomSheetPop.dismiss();
                }
                break;
        }
    }

    /**
     * 打开第三方地图，并设置经纬度定位
     */
    private void openBottomMapNaviCation() {
        mBottomSheetPop = new BottomSheetPop(getActivity());
        mBottomSheetPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomSheetPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mBottomSheetPop.setContentView(openBottomView);
        mBottomSheetPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mBottomSheetPop.setOutsideTouchable(true);
        mBottomSheetPop.setFocusable(true);
        mBottomSheetPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    //进入页面获取全部店铺信息
    private void startGetAllShopStoreInfo() {
        OkGo.<String>post(Constant.SELECT_ALL_SHOP_STORE_INFO_BY_SHOP_DESC + "/" + "龙山校区")
                .tag("全部店铺信息")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        doResponseJsonData(response);
                    }

                    @Override
                    public void onFinish() {
                        if (mRefreshLayoutLongSHan.getState().isOpening) {
                            mRefreshLayoutLongSHan.finishRefresh();
                        }

                    }
                });
    }

    /**
     * 初始化下拉刷新店铺信息
     */
    private void initSetRefreshListener() {
        mRefreshLayoutLongSHan.setEnableFooterFollowWhenNoMoreData(true);
        mRefreshLayoutLongSHan.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayoutLongSHan.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (storeInfoAdapter == null) {
                    startGetAllShopStoreInfo();
                }
                if (storeInfoAdapter != null && mRefreshLayoutLongSHan.getState().isOpening) {
                    mRefreshLayoutLongSHan.finishRefresh();
                    storeInfoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 点击综合
     */
    private void doAllClick() {

    }

    /**
     * 点击价格
     */
    private void doPriceClick() {
    }

    /**
     * 点击销量
     */
    private void doSalesClick() {
    }

    /**
     * 点击筛选
     */
    private void doFilterClick() {
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true)
                .popupPosition(PopupPosition.Right)//右边
                //.hasStatusBarShadow(true) //启用状态栏阴影
                .asCustom(new CustomLongSHanFilterDrawerPopupView(getActivity()))
                .show();
    }
    /**
     * 处理全部商品数据
     *
     * @param response JSON字符串
     */
    private void doResponseJsonData(Response<String> response) {
        OkGoAllShopInfoBean okGoAllShopInfoBean = GsonUtil.gsonToBean(response.body(),OkGoAllShopInfoBean.class);
        if (200 == okGoAllShopInfoBean.getCode()&& "success".equals(okGoAllShopInfoBean.getMsg())){

            List<StoreInfoBean> storeInfoBeanList = new ArrayList<>();
            for (OkGoAllShopInfoBean.Data data : okGoAllShopInfoBean.getData()) {
                storeInfoBeanList.add(new StoreInfoBean(
                        data.getCreateTime(), data.getScId(), data.getSsAddress(), data.getSsBeginTime(),
                        data.getSsDesc(), data.getSsEndTime(), data.getSsId(), data.getSsLogo(),data.getSsMobile(),
                        data.getSsName(), data.getSsNotice(), data.getSsPic(),data.getSsRecommend(),data.getUpdateTime(),data.getUserId()));
            }
            /* 商品信息信息列表适配器 */
            storeInfoAdapter = new BaseCanteenStoreInfoAdapter(R.layout.activity_canteen_recycler_view_item, storeInfoBeanList);
            //为列表控件设置适配器，并为执行适配操作
            mRecyclerViewLongSHan.setAdapter(storeInfoAdapter);
            //初始化适配器动画
            initAdapterAnimation(storeInfoAdapter);
            //初始化适配器上拉加载更多
            initAdapterLoadMore(storeInfoAdapter, storeInfoAdapter.getLoadMoreModule());
            //初始化适配器item全部单击事件--->进入店铺
            initSetAdapterOnItemClickListener(storeInfoAdapter);
            //初始化适配器适配器item中的子view单击事件--->联系商家 + 导航店铺
            initSetAdapterOnItemChildClickListener(storeInfoAdapter);
        }

    }

    /**
     * 初始化适配器动画
     *
     * @param baseStoreInfoAdapter 全部店铺适配器
     */
    private void initAdapterAnimation(BaseCanteenStoreInfoAdapter baseStoreInfoAdapter) {
        //创建适配器.动画
        baseStoreInfoAdapter.setAnimationEnable(true);//打开动画
        baseStoreInfoAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn);//设置动画类型
        baseStoreInfoAdapter.setAnimationFirstOnly(false);//设置始终显示动画
    }

    /**
     * 初始化适配器上拉加载更多
     *
     * @param baseCanteenStoreInfoAdapter 全部店铺适配器
     * @param loadMoreModule        全部商品适配器下拉加载基类
     */
    private void initAdapterLoadMore(BaseCanteenStoreInfoAdapter baseCanteenStoreInfoAdapter, BaseLoadMoreModule loadMoreModule) {
        loadMoreModule.setEnableLoadMore(true);//打开上拉加载
        loadMoreModule.setAutoLoadMore(true);//自动加载
        loadMoreModule.setPreLoadNumber(1);//设置滑动到倒数第几个条目时自动加载，默认为1
        loadMoreModule.setEnableLoadMoreIfNotFullPage(true);//当数据不满一页时继续自动加载
        loadMoreModule.setLoadMoreView(new CustomLoadMoreView());//设置自定义加载布局
        loadMoreModule.setOnLoadMoreListener(new OnLoadMoreListener() {//设置加载更多监听事件
            @Override
            public void onLoadMore() {
                mRecyclerViewLongSHan.postDelayed(new Runnable() {//延迟以提升用户体验
                    @Override
                    public void run() {
                        //userAddressInfoAdapter.addData(userAddressInfoBeanList);
                        ToastUtils.show("没有更多商铺信息了");
                        baseCanteenStoreInfoAdapter.getLoadMoreModule().loadMoreEnd();//加载完毕
                        //userAddressInfoAdapter.getLoadMoreModule().loadMoreComplete();//加载完毕
                        //userAddressInfoAdapter.getLoadMoreModule().loadMoreFail()//加载失败
                    }
                }, 1000);
            }
        });
    }

    /**
     * 初始化适配器item全部单击事件--->进入店铺
     *
     * @param storeInfoAdapter 全部店铺适配器
     */
    private void initSetAdapterOnItemClickListener(BaseCanteenStoreInfoAdapter storeInfoAdapter) {
        storeInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                StoreInfoBean storeInfoBean = (StoreInfoBean) adapter.getItem(position);
                doIntoStore(storeInfoBean);//进入店铺
                IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), CanteenRunBuyActivity.class));
            }
        });
    }

    /**
     * 初始化适配器适配器item中的子view单击事件--->联系商家 + 导航店铺
     *
     * @param storeInfoAdapter 全部店铺适配器
     */
    private void initSetAdapterOnItemChildClickListener(BaseCanteenStoreInfoAdapter storeInfoAdapter) {
        storeInfoAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                StoreInfoBean storeInfoBean = (StoreInfoBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.tv_canteen_store_mobile: //联系商家
                        XXPermissionsAction.getInstance().callPhone(getContext(), storeInfoBean.getSsMobile());
                        break;
                    case R.id.ll_canteen_address://导航店铺
                    case R.id.iv_canteen_address://导航店铺
                    case R.id.tv_canteen_address://导航店铺
                        openBottomMapNaviCation();
                        String strAddress = storeInfoBean.getSsAddress();
                        ToastUtils.show(strAddress);
                        break;
                }
            }
        });
    }

    /**
     * 进入店铺
     *
     * @param storeInfoBean 店铺信息
     */
    private void doIntoStore(StoreInfoBean storeInfoBean) {
        ToastUtils.show(storeInfoBean);
    }

    /**
     * Intent打开百度地图导航：我的位置——六盘水师范学院
     */
    private void openMapBaidu() {
        if (isAvilible(getActivity(), "com.baidu.BaiduMap")) {//传入指定应用包名
            try {
                Intent intent = Intent.getIntent("intent://map/direction?" +
                        "destination=latlng:" + mInfo.getLat() + "," + mInfo.getLng() + "|name:六盘水师范学院" +        //终点
                        "&mode=driving&" +          //导航路线方式
                        "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent); //启动调用
                ToastUtil.showToast("校园生活助手后台运行中！");
            } catch (URISyntaxException e) {
                Log.e("intent", e.getMessage());
            }
        } else {//未安装
            //market为路径，id为包名
            //显示手机上所有的market商店
            Toast.makeText(getActivity(), "您尚未安装百度地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    /**
     * Intent打开高德地图导航：我的位置——六盘水师范学院
     */
    private void openMapGaoDe() {
        if (isAvilible(getActivity(), "com.autonavi.minimap")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            //将功能Scheme以URI的方式传入data
            Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&poiname=六盘水师范学院&lat=" + mInfo.getLat() + "&lon=" + mInfo.getLng() + "&dev=1&style=2");
            intent.setData(uri);

            //启动该页面即可
            startActivity(intent);
            ToastUtil.showToast("校园生活助手后台运行中！");
        } else {
            Toast.makeText(getActivity(), "您尚未安装高德地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    /**
     * Intent打开腾讯地图导航：我的位置——六盘水师范学院
     */
    private void openMapTencent() {
        if (isAvilible(getActivity(), "com.tencent.map")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            //将功能Scheme以URI的方式传入data
            Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&to=六盘水师范学院&tocoord=" + mInfo.getLat() + "," + mInfo.getLng());
            intent.setData(uri);

            //启动该页面即可
            startActivity(intent);
            ToastUtil.showToast("校园生活助手后台运行中！");
        } else {
            Toast.makeText(getActivity(), "您尚未安装腾讯地图", Toast.LENGTH_LONG).show();
            Uri uri = Uri.parse("market://details?id=com.tencent.map");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}

