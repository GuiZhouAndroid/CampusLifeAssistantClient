package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.ViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods.GoodsAddFragment;
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
    @BindView(R.id.tab_layout_goods) TabLayout mTabLayoutGoods;//顶部导航栏
    @BindView(R.id.view_pager_goods) ViewPager mViewPagerGoods;//左右滑动

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
    @OnClick({R2.id.iv_goods_back})
    public void onMyGoodsViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_goods_back://点击返回
                MyGoodsManagerActivity.this.finish();
                break;
        }
    }

    private void initTabLayoutAndViewPager() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> tabTitleList = new ArrayList<>();
        Collections.addAll(tabTitleList, "全部商品", "添加商品", "删除商品", "更新商品");
        Collections.addAll(fragmentArrayList, GoodsAllFragment.newInstance(), GoodsAddFragment.newInstance(), GoodsDeleteFragment.newInstance(), GoodsUpdateFragment.newInstance());
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
}
