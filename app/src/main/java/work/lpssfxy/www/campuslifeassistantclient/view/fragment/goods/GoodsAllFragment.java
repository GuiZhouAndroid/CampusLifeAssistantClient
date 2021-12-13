package work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.GoodsCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.ShopCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.StoreGoodsInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllGoodsCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllGoodsInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllShopCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.DynamicTimeFormat;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
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
                        super.onError(response);
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
        //设置自定义动画
//        userAddressInfoAdapter.setAdapterAnimation(new BaseAnimation() {
//            @NonNull
//            @Override
//            public Animator[] animators(@NonNull View view) {
//                return new Animator[]{
//                        ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
//                        ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
//                };
//            }
//        });
    }

    @Override
    protected void stopLoad() {
        Log.d(TAG, "Fragment1" + "已经对用户不可见，可以停止加载数据");
    }
}
