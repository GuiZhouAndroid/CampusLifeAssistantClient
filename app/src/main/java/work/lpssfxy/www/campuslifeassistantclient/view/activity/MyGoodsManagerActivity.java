package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.ViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.custominterface.UpdateFragmentUIFromActivity;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods.GoodsAllFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods.GoodsDeleteFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods.GoodsUpdateFragment;

/**
 * created by on 2021/12/12
 * 描述：商品管理
 *
 * @author ZSAndroid
 * @create 2021-12-12-20:05
 */
@SuppressLint("NonConstantResourceId")
public class MyGoodsManagerActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @BindView(R2.id.iv_goods_back) ImageView mIvGoodsBack;//返回
    @BindView(R2.id.ll_goods_add) LinearLayout mLlGoodsAdd;//返回
    @BindView(R.id.tab_layout_goods) TabLayout mTabLayoutGoods;//顶部导航栏
    @BindView(R.id.view_pager_goods) ViewPager mViewPagerGoods;//左右滑动
    /** 商品添加回调成果发送空消息更新GoodsAllFragment的UI ---> */
    private Handler mHandler; //共享Handler实例
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return false;
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
        return R.layout.activity_goods_manager;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        initTabLayoutAndViewPager();

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
    @OnClick({R2.id.iv_goods_back,R2.id.ll_goods_add})
    public void onMyGoodsViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_goods_back://点击返回
                MyGoodsManagerActivity.this.finish();
                break;
            case R.id.ll_goods_add://点击添加商品信息
                IntentUtil.startActivityForResultAnimLeftToRight(MyGoodsManagerActivity.this, new Intent(MyGoodsManagerActivity.this, GoodAddActivity.class),Constant.REQUEST_CODE_VALUE);
                break;
        }
    }

    private void initTabLayoutAndViewPager() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> tabTitleList = new ArrayList<>();
        Collections.addAll(tabTitleList, "全部商品", "删除商品", "更新商品");
        Collections.addAll(fragmentArrayList, GoodsAllFragment.newInstance(), GoodsDeleteFragment.newInstance(), GoodsUpdateFragment.newInstance());
        mTabLayoutGoods.setTabMode(TabLayout.MODE_FIXED);//设置TabLayout的模式
        //为TabLayout添加tab名称
        for (int i = 0; i < tabTitleList.size(); i++) {
            mTabLayoutGoods.addTab(mTabLayoutGoods.newTab().setText(tabTitleList.get(i)));
        }
        mViewPagerGoods.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList, tabTitleList));//设置适配器
        mTabLayoutGoods.setupWithViewPager(mViewPagerGoods); //TabLayout绑定ViewPager
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //商品添加完成回调显示
        if (requestCode == Constant.REQUEST_CODE_VALUE && resultCode == Constant.RESULT_CODE_ADD_GOODS_SUCCESS) {
            initTabLayoutAndViewPager();//商品添加完成，执行重载控件实现刷新布局
            Snackbar snackbar = Snackbar.make(mViewPagerGoods, data.getStringExtra("BackGoodsName") + "商品已自动添加到店铺里了，快去管理吧~", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .setDuration(5000);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            //startNotHaveDataGoodsAllFragmentFragment();//向Fragment发送空消息--->没使用
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 商品添加回调成果 共享Handler实例
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        // 3.在HandlerFragment中通过调用此方法，即可实现共享Handler消息对象
        mHandler = handler;
    }

    /**
     * 发送空消息更新GoodsAllFragment的UI
     */
    public void startNotHaveDataGoodsAllFragmentFragment() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(1);
        }
    }
}
