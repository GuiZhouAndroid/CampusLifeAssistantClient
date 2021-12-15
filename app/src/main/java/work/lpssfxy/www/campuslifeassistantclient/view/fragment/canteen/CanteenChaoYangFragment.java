package work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
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
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseStoreInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.CustomLoadMoreView;
import work.lpssfxy.www.campuslifeassistantclient.base.custompopup.CustomChaoYangFilterDrawerPopupView;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.StoreInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllShopInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XXPermissionsAction;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/12/14
 * 描述：朝阳食堂
 *
 * @author ZSAndroid
 * @create 2021-12-14-21:30
 */
@SuppressLint("NonConstantResourceId")
public class CanteenChaoYangFragment extends BaseFragment {

    @BindView(R2.id.ll_chao_yang_show) LinearLayout mLlChaoYangShow;//父布局
    @BindView(R2.id.tv_all_chao_yang) TextView mTvAllChaoYang;//综合
    @BindView(R2.id.tv_price_chao_yang) TextView mTvPriceChaoYang;//价格
    @BindView(R2.id.tv_sales_chao_yang) TextView mTvSalesChaoYang;//销量
    @BindView(R2.id.tv_filter_chao_yang) TextView mTvFilterChaoYang;//筛选

    @BindView(R2.id.refreshLayout_chao_yang) RefreshLayout mRefreshLayoutChaoYang;
    @BindView(R2.id.recyclerView_chao_yang) RecyclerView mRecyclerViewChaoYang;

    private BaseStoreInfoAdapter storeInfoAdapter;//店铺信息适配器

    /**
     * @return 单例对象
     */
    public static CanteenChaoYangFragment newInstance() {
        return new CanteenChaoYangFragment();
    }


    @Override
    protected void lazyLoad() {
        startGetAllShopStoreInfo();//进入页面获取全部店铺信息
    }

    @Override
    protected int bindLayout() {

        return R.layout.fragment_cantten_chao_yang;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        initSelectAllStoreInfoRecyclerView();//初始化全部店铺内容适配器
        StatusBarUtil.darkMode(getActivity());//状态栏透明和间距处理
    }

    /**
     * 初始化全部店铺内容适配器
     */
    private void initSelectAllStoreInfoRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewChaoYang.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {
        initSetRefreshListener();//初始化下拉刷新店铺信息
    }

    @OnClick({R2.id.tv_all_chao_yang,R2.id.tv_price_chao_yang,R2.id.tv_sales_chao_yang,R2.id.tv_filter_chao_yang})
    public void onChaoYangViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_chao_yang://点击综合
                doAllClick();
                break;
            case R.id.tv_price_chao_yang://点击价格
                doPriceClick();
                break;
            case R.id.tv_sales_chao_yang://点击销量
                doSalesClick();
                break;
            case R.id.tv_filter_chao_yang://点击筛选
                doFilterClick();
                break;
        }
    }
    //进入页面获取全部店铺信息
    private void startGetAllShopStoreInfo() {
        OkGo.<String>post(Constant.SELECT_ALL_SHOP_STORE_INFO_BY_SHOP_DESC + "/" + "龙山校区")
                .tag("全部店铺信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在加载信息...");
                    }
                    @Override
                    public void onSuccess(Response<String> response) {
                        doResponseJsonData(response);
                    }

                    @Override
                    public void onFinish() {
                        if (mRefreshLayoutChaoYang.getState().isOpening) {
                            mRefreshLayoutChaoYang.finishRefresh();
                        }
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }
                });
    }

    /**
     * 初始化下拉刷新店铺信息
     */
    private void initSetRefreshListener() {
        mRefreshLayoutChaoYang.setEnableFooterFollowWhenNoMoreData(true);
        mRefreshLayoutChaoYang.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayoutChaoYang.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (storeInfoAdapter == null) {
                    startGetAllShopStoreInfo();
                }
                if (storeInfoAdapter != null && mRefreshLayoutChaoYang.getState().isOpening) {
                    mRefreshLayoutChaoYang.finishRefresh();
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
                .asCustom(new CustomChaoYangFilterDrawerPopupView(getActivity()))
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
            storeInfoAdapter = new BaseStoreInfoAdapter(R.layout.activity_canteen_chao_yang_recycler_view_item, storeInfoBeanList);
            //为列表控件设置适配器，并为执行适配操作
            mRecyclerViewChaoYang.setAdapter(storeInfoAdapter);
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
    private void initAdapterAnimation(BaseStoreInfoAdapter baseStoreInfoAdapter) {
        //创建适配器.动画
        baseStoreInfoAdapter.setAnimationEnable(true);//打开动画
        baseStoreInfoAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn);//设置动画类型
        baseStoreInfoAdapter.setAnimationFirstOnly(false);//设置始终显示动画
    }

    /**
     * 初始化适配器上拉加载更多
     *
     * @param baseStoreInfoAdapter 全部店铺适配器
     * @param loadMoreModule        全部商品适配器下拉加载基类
     */
    private void initAdapterLoadMore(BaseStoreInfoAdapter baseStoreInfoAdapter, BaseLoadMoreModule loadMoreModule) {
        loadMoreModule.setEnableLoadMore(true);//打开上拉加载
        loadMoreModule.setAutoLoadMore(true);//自动加载
        loadMoreModule.setPreLoadNumber(1);//设置滑动到倒数第几个条目时自动加载，默认为1
        loadMoreModule.setEnableLoadMoreIfNotFullPage(true);//当数据不满一页时继续自动加载
        loadMoreModule.setLoadMoreView(new CustomLoadMoreView());//设置自定义加载布局
        loadMoreModule.setOnLoadMoreListener(new OnLoadMoreListener() {//设置加载更多监听事件
            @Override
            public void onLoadMore() {
                mRecyclerViewChaoYang.postDelayed(new Runnable() {//延迟以提升用户体验
                    @Override
                    public void run() {
                        //userAddressInfoAdapter.addData(userAddressInfoBeanList);
                        ToastUtils.show("没有更多商铺信息了");
                        baseStoreInfoAdapter.getLoadMoreModule().loadMoreEnd();//加载完毕
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
    private void initSetAdapterOnItemClickListener(BaseStoreInfoAdapter storeInfoAdapter) {
        storeInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                StoreInfoBean storeInfoBean = (StoreInfoBean) adapter.getItem(position);
                doIntoStore(storeInfoBean);//进入店铺
            }
        });
    }

    /**
     * 初始化适配器适配器item中的子view单击事件--->联系商家 + 导航店铺
     *
     * @param storeInfoAdapter 全部店铺适配器
     */
    private void initSetAdapterOnItemChildClickListener(BaseStoreInfoAdapter storeInfoAdapter) {
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
}
