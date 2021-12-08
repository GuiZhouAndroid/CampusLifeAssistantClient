package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.manager;

import static com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xuexiang.xui.utils.ViewUtils;
import com.xuexiang.xui.utils.WidgetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyFragmentStateViewPager2Adapter;
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPageUserTitle;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectAllUserInfoFragment;

/**
 * created by on 2021/11/29
 * 描述：开发者管理用户模块
 *
 * @author ZSAndroid
 * @create 2021-11-29-12:27
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperManagerUserInfoFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    private static final String TAG = "DeveloperManagerUserInfoFragment";

    //顶部TabLayout
    @BindView(R2.id.tab_layout_user_info) TabLayout mTabLayoutUserInfo;
    //TabLayout展开文本
    @BindView(R2.id.tv_onclick_open_user_info) TextView mTvOnclickOpenUserInfo;
    //TabLayout展开图标
    @BindView(R2.id.iv_switch_user_info) AppCompatImageView mIvSwitchUserInfo;
    //ViewPager2左右滑动
    @BindView(R2.id.view_pager_user_info) ViewPager2 mViewPager2UserInfo;

    //展开状态
    private boolean mNavigationIsShowUserInfo;
    //ViewPager2动态加载适配器
    private MyFragmentStateViewPager2Adapter mUserInfoAdapter;
    //TabLayout适配的Fragment集合
    public Fragment[] userInfoFragments = null;

    /**
     * @return 单例对象
     */
    public static DeveloperManagerUserInfoFragment newInstance() {
        return new DeveloperManagerUserInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_manager_user_info;
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
        initTabLayout();
        refreshStatus(mNavigationIsShowUserInfo = !mNavigationIsShowUserInfo);
    }

    private void initTabLayout() {
        mUserInfoAdapter = new MyFragmentStateViewPager2Adapter(this);
        mTabLayoutUserInfo.setTabMode(MODE_SCROLLABLE);
        mTabLayoutUserInfo.addOnTabSelectedListener(this);
        mViewPager2UserInfo.setAdapter(mUserInfoAdapter);
        // 设置缓存的数量
        mViewPager2UserInfo.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayoutUserInfo, mViewPager2UserInfo, (tab, position) -> tab.setText(mUserInfoAdapter.getPageTitle(position))).attach();
    }

    /**
     * 收缩 + 展开 刷新ViewPager2 实现动态加载
     *
     * @param view 控件id
     */
    @OnClick({R.id.iv_switch_user_info, R.id.tv_onclick_open_user_info})
    public void onViewUserInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_user_info:
            case R.id.tv_onclick_open_user_info:
                refreshStatus(mNavigationIsShowUserInfo = !mNavigationIsShowUserInfo);
                break;
        }
    }

    /**
     * 展示开关动画控制View的透明度
     *
     * @param isShow 开关动画
     */
    private void refreshStatus(final boolean isShow) {
        ObjectAnimator rotation;
        ObjectAnimator tabAlpha;
        ObjectAnimator textAlpha;
        if (isShow) {
            rotation = ObjectAnimator.ofFloat(mIvSwitchUserInfo, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutUserInfo, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenUserInfo, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(mIvSwitchUserInfo, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutUserInfo, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenUserInfo, "alpha", 0, 1);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotation).with(tabAlpha).with(textAlpha);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                refreshAdapter(isShow);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                switchContainer(isShow);
            }
        });
        animatorSet.setDuration(500).start();
    }

    private void switchContainer(boolean isShow) {
        ViewUtils.setVisibility(mTvOnclickOpenUserInfo, !isShow);
        ViewUtils.setVisibility(mViewPager2UserInfo, isShow);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshAdapter(boolean isShow) {
        //TabLayout适配的Fragment集合
        userInfoFragments = new Fragment[]{
                DeveloperSelectAllUserInfoFragment.newInstance()
        };
        //装载List集合，提供给ViewPager2适配器使用
        List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(this.userInfoFragments));
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        List<String> titleList = new ArrayList<>(Arrays.asList(MultiPageUserTitle.getPageNames()));
        if (mViewPager2UserInfo == null) {
            return;
        }
        if (isShow) { //打开按钮动态加载选项卡内容
            for (Fragment fragment : fragmentList) { //遍历Fragment集合，开始适配
                mUserInfoAdapter.addFragment(fragment, titleList);
            }
            mUserInfoAdapter.notifyDataSetChanged();
            mViewPager2UserInfo.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(mTabLayoutUserInfo);
        } else { // 清空适配全部数据
            mUserInfoAdapter.clear();
        }
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
    public boolean onBackPressed() {
        return false;
    }
}
