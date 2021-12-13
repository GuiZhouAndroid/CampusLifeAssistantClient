package work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllGoodsInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.LazyLoadFragment;

/**
 * created by on 2021/12/13
 * 描述：商家查询全部商品
 *
 * @author ZSAndroid
 * @create 2021-12-13-16:20
 */
@SuppressLint("NonConstantResourceId")
public class GoodsAllFragment extends BaseFragment {

    @BindView(R2.id.refreshLayout_store_all_goods) RefreshLayout mRefreshLayoutStoreAllGoods;
    @BindView(R2.id.ryc_store_all_goods) RecyclerView mRycStoreAllGoods;

    /**
     * @return 单例对象
     */
    public static GoodsAllFragment newInstance() {
        return new GoodsAllFragment();
    }

    @Override
    protected void lazyLoad() {
        mRefreshLayoutStoreAllGoods.autoRefresh(3000);
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

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {

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
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(),"正在加载商品信息...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoAllGoodsInfoBean okGoAllGoodsInfoBean = GsonUtil.gsonToBean(response.body(),OkGoAllGoodsInfoBean.class);
                        if (200 == okGoAllGoodsInfoBean.getCode() && "全部商品查询成功".equals(okGoAllGoodsInfoBean.getMsg())){
                            ToastUtils.show(okGoAllGoodsInfoBean.getData());
                            return;
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    @Override
    protected void stopLoad() {
        Log.d(TAG, "Fragment1" + "已经对用户不可见，可以停止加载数据");
    }
}
