package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyVerticalTabLayoutViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.VerticalPager;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperBannedAccountUserName;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperKickOffLineToken;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperKickOffLineRealName;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectBannedStateUserName;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectBannedTimeUserName;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperUntieBannedAccountUserName;

/**
 * created by on 2021/11/16
 * 描述：开发者后台安全中心
 *
 * @author ZSAndroid
 * @create 2021-11-16-9:43
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSystemSafeActivity extends BaseActivity {
    private static final String TAG = "DeveloperSystemSafeActivity";
    /** 父部局控件 */
    @BindView(R2.id.rl_developer_safe_show) RelativeLayout mRlDeveloperSafeShow;
    /** 返回按钮 */
    @BindView(R2.id.iv_developer_back) ImageView mIvDeveloperBack;
    /** Toolbar */
    @BindView(R2.id.toolbar_developer_safe) Toolbar mDeveloperSafeToolbar;
    /** 垂直TabLayout */
    @BindView(R2.id.vertical_tab_layout_developer_safe) VerticalTabLayout mDeveloperSafeVerticalTabLayout;
    /** 垂直ViewPager */
    @BindView(R2.id.vertical_viewpager_developer_safe) VerticalPager mDeveloperSafeVerticalViewPager;
    /** Fragment集合 */
    public Fragment[] fragments = null;
    /** ViewPager适配器遍历绑定数组Fragment集合 */
    private List<Fragment> fragmentList;

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
        return R.layout.developer_system_safe_activity;
    }

    @Override
    protected void prepareData() {

    }

    @SuppressLint("LongLogTag")
    @Override
    protected void initView() {
        /**判断Toolbar，开启主图标并显示title*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        /** 设置Toolbar */
        setSupportActionBar(mDeveloperSafeToolbar);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        initViewPager();
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
     * 初始化ViewPager + TabLayout + Fragment
     */
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        //创建Fragment类型的数组，适配ViewPager，添加四个功能页
        fragments = new Fragment[]{new DeveloperKickOffLineRealName(), new DeveloperKickOffLineToken(), new DeveloperBannedAccountUserName(), new DeveloperSelectBannedStateUserName(), new DeveloperSelectBannedTimeUserName(), new DeveloperUntieBannedAccountUserName()};
        //ViewPager设置MyAdapter适配器，遍历List<Fragment>集合，填充Fragment页面
        mDeveloperSafeVerticalViewPager.setAdapter(new MyVerticalTabLayoutViewPagerAdapter(getSupportFragmentManager(), fragments, fragmentList));
        //进行TabLayout 关联 ViewPager
        mDeveloperSafeVerticalTabLayout.setupWithViewPager(mDeveloperSafeVerticalViewPager);
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_developer_back})
    public void onDeveloperViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_developer_back://点击返回
                DeveloperSystemSafeActivity.this.finish();
                break;
        }
    }
}
