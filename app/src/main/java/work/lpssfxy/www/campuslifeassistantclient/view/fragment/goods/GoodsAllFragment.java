package work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseUserAddressInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.welcome.BaseStoreGoodsInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.CustomLoadMoreView;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.GoodsCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.ShopCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.StoreGoodsInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllGoodsCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllGoodsInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllShopCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.DynamicTimeFormat;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.GoodAddActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.MyGoodsManagerActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.UserAddressActivity;

/**
 * created by on 2021/12/13
 * 描述：商家查询全部商品
 *
 * @author ZSAndroid
 * @create 2021-12-13-16:20
 */
@SuppressLint("NonConstantResourceId")
public class GoodsAllFragment extends BaseFragment {

    @BindView(R2.id.refreshLayout_store_all_goods) RefreshLayout mRefreshLayoutStoreAllGoods;//下拉刷新
    @BindView(R2.id.ryc_store_all_goods) RecyclerView mRycStoreAllGoods;//列表控件
    private BaseStoreGoodsInfoAdapter storeGoodsInfoAdapter;//商品信息适配器

    /**
     * @return 单例对象
     */
    public static GoodsAllFragment newInstance() {
        return new GoodsAllFragment();
    }

    @Override
    protected void lazyLoad() {
        startGetAllGoodsInfo();//进入页面获取全部商品信息
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_goods_all;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        initSelectStoreAllGoodsInfoRecyclerView();//初始化全部商品内容适配器
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {
        //初始化下拉刷新全部商品信息
        initSetRefreshListener(context);
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({})
    public void onStoreAllGoodsViewClick(View view) {
        switch (view.getId()) {

        }
    }

    /**
     * 初始化RecyclerView列表控件
     */
    private void initSelectStoreAllGoodsInfoRecyclerView() {
        // 1.创建布局管理实例对象
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 2.设置RecyclerView布局方式为垂直方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 3.RecyclerView绑定携带垂直方向参数的布局管理实例对象
        mRycStoreAllGoods.setLayoutManager(layoutManager);
    }

    /**
     *
     * @param context
     */
    private void initSetRefreshListener(Context context) {
        // 设置下拉刷新参数和主题颜色
        ClassicsHeader mClassicsHeader = (ClassicsHeader) mRefreshLayoutStoreAllGoods.getRefreshHeader();
        if (mClassicsHeader != null) {
            mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        }
        setThemeColor(mRefreshLayoutStoreAllGoods, R.color.welcome_colorMain);
        mRefreshLayoutStoreAllGoods.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayoutStoreAllGoods.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startGetAllGoodsInfo();//进入页面获取全部商品信息
            }
        });
    }

    /**
     * 进入页面获取全部商品信息
     */
    private void startGetAllGoodsInfo() {
        OkGo.<String>post(Constant.SHOP_SELECT_ALL_GOODS_INFO_BY_SHOP_STORE_ID)
                .tag("有全部商品信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        //MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在加载商品信息...");

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        doResponseJsonData(response);

                    }

                    @Override
                    public void onFinish() {
                        mRefreshLayoutStoreAllGoods.finishRefresh();
                        //MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRycStoreAllGoods, "请求错误，服务器连接失败！");

                    }
                });
    }

    /**
     * 处理全部商品数据
     *
     * @param response JSON字符串
     */
    private void doResponseJsonData(Response<String> response) {
        OkGoAllGoodsInfoBean okGoAllGoodsInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllGoodsInfoBean.class);
        if (200 == okGoAllGoodsInfoBean.getCode() && "全部商品查询成功".equals(okGoAllGoodsInfoBean.getMsg())) {
            List<StoreGoodsInfoBean> storeGoodsInfoBeanList = new ArrayList<>();
            for (OkGoAllGoodsInfoBean.Data data : okGoAllGoodsInfoBean.getData()) {
                storeGoodsInfoBeanList.add(new StoreGoodsInfoBean(
                        data.getCategoryId(), data.getCreateTime(), data.getGoodsDesc(), data.getGoodsFlag(),
                        data.getGoodsId(), data.getGoodsName(), data.getGoodsPic(), data.getGoodsPrice(),
                        data.getGoodsRepertory(), data.getSsId(), data.getUpdateTime()
                ));
            }
            /* 商品信息信息列表适配器 */
            storeGoodsInfoAdapter = new BaseStoreGoodsInfoAdapter(R.layout.activity_store_goods_recycler_view_item, storeGoodsInfoBeanList);
            //为列表控件设置适配器，并为执行适配操作
            mRycStoreAllGoods.setAdapter(storeGoodsInfoAdapter);
            //初始化适配器动画
            initAdapterAnimation();
            //初始化适配器上拉加载更多
            initAdapterLoadMore(storeGoodsInfoAdapter, storeGoodsInfoAdapter.getLoadMoreModule());
            //初始化适配器侧滑删除
            initAdapterDragDelete(storeGoodsInfoAdapter, storeGoodsInfoAdapter.getDraggableModule());
            //初始化适配器空布局 + 监听事件--->新增收货地址（在setAdapter后使用，否则报错）
            initSetAdapterEmptyView(storeGoodsInfoAdapter);

        }
    }

    /**
     * 初始化适配器动画
     */
    private void initAdapterAnimation() {
        //创建适配器.动画
        storeGoodsInfoAdapter.setAnimationEnable(true);//打开动画
        storeGoodsInfoAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn);//设置动画类型
        storeGoodsInfoAdapter.setAnimationFirstOnly(false);//设置始终显示动画
    }

    /**
     * 初始化适配器上拉加载更多
     *
     * @param storeGoodsInfoAdapter 全部商品适配器
     * @param loadMoreModule        全部商品适配器下拉加载基类
     */
    private void initAdapterLoadMore(BaseStoreGoodsInfoAdapter storeGoodsInfoAdapter, BaseLoadMoreModule loadMoreModule) {
        loadMoreModule.setEnableLoadMore(true);//打开上拉加载
        loadMoreModule.setAutoLoadMore(true);//自动加载
        loadMoreModule.setPreLoadNumber(1);//设置滑动到倒数第几个条目时自动加载，默认为1
        loadMoreModule.setEnableLoadMoreIfNotFullPage(true);//当数据不满一页时继续自动加载
        loadMoreModule.setLoadMoreView(new CustomLoadMoreView());//设置自定义加载布局
        loadMoreModule.setOnLoadMoreListener(new OnLoadMoreListener() {//设置加载更多监听事件
            @Override
            public void onLoadMore() {
                mRycStoreAllGoods.postDelayed(new Runnable() {//延迟以提升用户体验
                    @Override
                    public void run() {
                        //userAddressInfoAdapter.addData(userAddressInfoBeanList);
                        ToastUtils.show("没有更多商品信息了");
                        storeGoodsInfoAdapter.getLoadMoreModule().loadMoreEnd();//加载完毕
                        //userAddressInfoAdapter.getLoadMoreModule().loadMoreComplete();//加载完毕
                        //userAddressInfoAdapter.getLoadMoreModule().loadMoreFail()//加载失败
                    }
                }, 1000);
            }
        });
    }

    /**
     * 初始化适配器空布局 + 监听事件--->新增商品信息
     *
     * @param storeGoodsInfoAdapter 全部商品适配器
     */
    private void initSetAdapterEmptyView(BaseStoreGoodsInfoAdapter storeGoodsInfoAdapter) {
        storeGoodsInfoAdapter.setEmptyView(R.layout.custom_all_goods_empty_view);
        storeGoodsInfoAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//商家添加商品信息
                IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), GoodAddActivity.class));
            }
        });
    }

    /**
     * 初始化适配器侧滑删除商品
     *
     * @param storeGoodsInfoAdapter
     * @param draggableModule       全部商品适配器适配器拖拽基类
     */
    private void initAdapterDragDelete(BaseStoreGoodsInfoAdapter storeGoodsInfoAdapter, BaseDraggableModule draggableModule) {
        draggableModule.setSwipeEnabled(true);//启动侧滑删除
        draggableModule.getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START);//侧滑删除方向
        draggableModule.setOnItemSwipeListener(new OnItemSwipeListener() {
            private int goodsId;

            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int position) { //当滑动动作开始时调用，提取商品信息ID
                goodsId = storeGoodsInfoAdapter.getData().get(position).getGoodsId();
                Log.i(TAG, "onItemSwipeStart: " + storeGoodsInfoAdapter.getData().get(position).toString());
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int position) {//当item滑动删除之前，松手之时调用

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int position) { //当item滑动时视图从适配器中删除之后调用
                deleteGoodsInfoInfo(goodsId);//删除商品信息
                //移除list中的数据后，并没有紧接着告知adapter有数据已经移除，就会导致后面操作的报错
                //解决方法是，在list做完remove或者add操作后，紧跟着notifyItemInserted(notifyItemRangeInserted)或notifyDataSetChanged
                storeGoodsInfoAdapter.notifyDataSetChanged();//移出后，同步List收获地址数量，避免产生下标越界等错误--->必须在主线程
            }

            @Override //滑动时的背景，正在滑动时调用，一般用于绘制背景色
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(Color.parseColor("#fff75446"));
            }
        });
    }

    /**
     * 商家删除商品信息
     *
     * @param goodsId 商品ID
     */
    private void deleteGoodsInfoInfo(int goodsId) {
        OkGo.<String>post(Constant.SHOP_DELETE_GOODS_INFO_BY_STORE_ID_AND_GOODS_ID + "/" + goodsId)
                .tag("删除商品信息")
                .execute(new StringCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())&& "商品信息删除成功".equals(okGoResponseBean.getData())) {
                            XToastUtils.success("商品删除成功");
                        } else {
                            XToastUtils.error(okGoResponseBean.getData());
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRycStoreAllGoods, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    protected void stopLoad() {
        Log.d(TAG, "Fragment1" + "已经对用户不可见，可以停止加载数据");
    }
}
