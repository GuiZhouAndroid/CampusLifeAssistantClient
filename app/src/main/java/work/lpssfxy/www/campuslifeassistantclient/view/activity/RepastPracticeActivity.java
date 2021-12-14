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
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen.CanteenChaoYangFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen.CanteenLongSHanFragment;

/**
 * created by on 2021/12/14
 * 描述：食堂跑腿代购
 *
 * @author ZSAndroid
 * @create 2021-12-14-20:22
 */
@SuppressLint("NonConstantResourceId")
public class RepastPracticeActivity extends BaseActivity {

    @BindView(R2.id.iv_repast_back) ImageView mIvRepastBack;//返回
    @BindView(R.id.tab_layout_repast) TabLayout mTabLayoutRepast;//顶部导航栏
    @BindView(R.id.view_pager_repast) ViewPager mViewPagerRepast;//左右滑动


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
        return R.layout.activity_practice_repast;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {

        //StatusBarUtil.setPaddingSmart(RepastPracticeActivity.this, toolbar);

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

    @OnClick({R2.id.iv_repast_back})
    public void onRepastViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_repast_back://点击返回
                RepastPracticeActivity.this.finish();
                break;
        }
    }

    private void initTabLayoutAndViewPager() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> tabTitleList = new ArrayList<>();
        Collections.addAll(tabTitleList, "朝阳食堂", "龙山食堂");
        Collections.addAll(fragmentArrayList, CanteenChaoYangFragment.newInstance(), CanteenLongSHanFragment.newInstance());
        mTabLayoutRepast.setTabMode(TabLayout.MODE_FIXED);//设置TabLayout的模式
        //为TabLayout添加tab名称
        for (int i = 0; i < tabTitleList.size(); i++) {
            mTabLayoutRepast.addTab(mTabLayoutRepast.newTab().setText(tabTitleList.get(i)));
        }
        mViewPagerRepast.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList, tabTitleList));//设置适配器
        mTabLayoutRepast.setupWithViewPager(mViewPagerRepast); //TabLayout绑定ViewPager
    }

}
