package work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.xuexiang.xui.widget.button.ButtonView;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/12/13
 * 描述：商家删除商品
 *
 * @author ZSAndroid
 * @create 2021-12-13-16:20
 */

@SuppressLint("NonConstantResourceId")
public class GoodsDeleteFragment extends BaseFragment {

    private static final String TAG = "GoodsDeleteFragment";
    //父布局
    @BindView(R2.id.rl_delete_goods_info) RelativeLayout mRlDeleteGoodsInfo;
    //待删除商品ID
    @BindView(R2.id.edit_delete_goods_by_id) PowerfulEditText mEditDeleteGoodsById;
    //待删除商品名称
    @BindView(R2.id.edit_delete_goods_by_name) PowerfulEditText mEditDeleteGoodsByName;
    //确定删除(ID)
    @BindView(R2.id.btn_delete_goods_by_id) ButtonView mBtnDeleteGoodsById;
    //确定删除(名称)
    @BindView(R2.id.btn_delete_goods_by_name) ButtonView mBtnDeleteGoodsByName;

    /**
     * @return 单例对象
     */
    public static GoodsDeleteFragment newInstance() {
        return new GoodsDeleteFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_goods_delete;
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
     * @param view 视图View
     */
    @OnClick({R2.id.btn_delete_goods_by_id, R2.id.btn_delete_goods_by_name})
    public void onBannedAccountTimeRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete_goods_by_id://确定执行ID
                String strDeleteGoodsById = mEditDeleteGoodsById.getText().toString().trim();
                doDeleteGoodsById(strDeleteGoodsById);
                break;
            case R.id.btn_delete_goods_by_name://确定执行名称
                String strDeleteGoodsByName = mEditDeleteGoodsByName.getText().toString().trim();
                doDeleteGoodsByName(strDeleteGoodsByName);
                break;
        }
    }

    /**
     * 通过商品ID删除商品信息
     *
     * @param strDeleteGoodsById 商品ID
     */
    private void doDeleteGoodsById(String strDeleteGoodsById) {
        //判空处理
        if (TextUtils.isEmpty(strDeleteGoodsById)) {
            mEditDeleteGoodsById.startShakeAnimation();//抖动输入框
            XToastUtils.error("请填入商品编号");
            return;
        }
        OkGo.<String>post(Constant.SHOP_DELETE_GOODS_INFO_BY_STORE_ID_AND_GOODS_ID + "/" + strDeleteGoodsById)
                .tag("ID删除商品信息")
                .execute(new StringCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg()) && "商品信息删除成功".equals(okGoResponseBean.getData())) {
                            XToastUtils.success("商品删除成功");
                        } else {
                            XToastUtils.error(okGoResponseBean.getData());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDeleteGoodsInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 通过商品名称删除商品信息
     *
     * @param strDeleteGoodsByName 商品名称
     */
    private void doDeleteGoodsByName(String strDeleteGoodsByName) {
        //判空处理
        if (TextUtils.isEmpty(strDeleteGoodsByName)) {
            mEditDeleteGoodsByName.startShakeAnimation();//抖动输入框
            XToastUtils.error("请填入商品名称");
            return;
        }
        OkGo.<String>post(Constant.SHOP_DELETE_GOODS_INFO_BY_STORE_ID_AND_GOODS_NAME + "/" + strDeleteGoodsByName)
                .tag("名称删除商品信息")
                .execute(new StringCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg()) && "商品信息删除成功".equals(okGoResponseBean.getData())) {
                            XToastUtils.success("商品删除成功");
                        } else {
                            XToastUtils.error(okGoResponseBean.getData());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDeleteGoodsInfo, "请求错误，服务器连接失败！");
                    }
                });
    }
}
