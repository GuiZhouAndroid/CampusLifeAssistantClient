package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xuexiang.xui.widget.button.RippleView;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/12
 * 描述：我的商铺中心
 *
 * @author ZSAndroid
 * @create 2021-12-12-14:51
 */
@SuppressLint("NonConstantResourceId")
public class MyShopCenterActivity extends BaseActivity {

    private static final String TAG = "MyShopCenterActivity";

    @BindView(R2.id.iv_my_shop_store_back) ImageView mIvMyShopStoreBack;//返回
    @BindView(R2.id.ripple_store_manager) RippleView mRippleStoreManager;//店铺管理
    @BindView(R2.id.ripple_goods_manager) RippleView mRippleGoodsManager;//商品管理
    @BindView(R2.id.ripple_sales_status) RippleView mRippleSalesStatus;//销售详情

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
        return R.layout.activity_my_shop_center;
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

    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_my_shop_store_back,R2.id.ripple_store_manager,R2.id.ripple_goods_manager,R2.id.ripple_sales_status})
    public void onMyShopCenterViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_shop_store_back://点击返回
                MyShopCenterActivity.this.finish();
                break;
            case R.id.ripple_store_manager://店铺管理
                IntentUtil.startActivityAnimLeftToRight(MyShopCenterActivity.this, new Intent(MyShopCenterActivity.this, MyStoreManagerActivity.class));//执行动画跳转
                break;
            case R.id.ripple_goods_manager://商品管理
                IntentUtil.startActivityAnimLeftToRight(MyShopCenterActivity.this, new Intent(MyShopCenterActivity.this, MyGoodsManagerActivity.class));//执行动画跳转
                break;
            case R.id.ripple_sales_status://销售详情
                IntentUtil.startActivityAnimLeftToRight(MyShopCenterActivity.this, new Intent(MyShopCenterActivity.this, MyGoodsManagerActivity.class));//执行动画跳转
                break;
        }
    }
}
