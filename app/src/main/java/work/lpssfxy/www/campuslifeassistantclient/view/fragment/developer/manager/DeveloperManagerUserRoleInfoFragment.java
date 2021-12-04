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
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPageUserRoleTitle;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectHaveRoleUserInfoByRoleIdFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectUserRoleByUserNameFragment;

/**
 * created by on 2021/11/27
 * 描述：开发者管理用户角色模块
 *
 * @author ZSAndroid
 * @create 2021-11-27-22:21
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperManagerUserRoleInfoFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    private static final String TAG = "DeveloperManagerUserRoleInfoFragment";

    //顶部TabLayout
    @BindView(R2.id.tab_layout_user_role_info) TabLayout mTabLayoutUserRoleInfo;
    //TabLayout展开文本
    @BindView(R2.id.tv_onclick_open_user_role_info) TextView mTvOnclickOpenUserRoleInfo;
    //TabLayout展开图标
    @BindView(R2.id.iv_switch_user_role_info) AppCompatImageView mIvSwitchUserRoleInfo;
    //ViewPager2左右滑动
    @BindView(R2.id.view_pager_user_role_info) ViewPager2 mViewPager2UserRoleInfo;

    //展开状态
    private boolean mNavigationIsShowUserRoleInfo;
    //ViewPager2动态加载适配器
    private MyFragmentStateViewPager2Adapter mUserRoleInfoAdapter;
    //TabLayout适配的Fragment集合
    public Fragment[] userRoleInfoFragments = null;


    /**
     * @return 单例对象
     */
    public static DeveloperManagerUserRoleInfoFragment newInstance() {
        return new DeveloperManagerUserRoleInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_manager_user_role_info;
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
        refreshStatus(mNavigationIsShowUserRoleInfo = !mNavigationIsShowUserRoleInfo);
    }

    private void initTabLayout() {
        mUserRoleInfoAdapter = new MyFragmentStateViewPager2Adapter(this);
        mTabLayoutUserRoleInfo.setTabMode(MODE_SCROLLABLE);
        mTabLayoutUserRoleInfo.addOnTabSelectedListener(this);
        mViewPager2UserRoleInfo.setAdapter(mUserRoleInfoAdapter);
        // 设置缓存的数量
        mViewPager2UserRoleInfo.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayoutUserRoleInfo, mViewPager2UserRoleInfo, (tab, position) -> tab.setText(mUserRoleInfoAdapter.getPageTitle(position))).attach();
    }

    /**
     * 收缩 + 展开 刷新ViewPager2 实现动态加载
     *
     * @param view 控件id
     */
    @OnClick({R.id.iv_switch_user_role_info, R.id.tv_onclick_open_user_role_info})
    public void onViewUserRoleInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_user_role_info:
            case R.id.tv_onclick_open_user_role_info:
                refreshStatus(mNavigationIsShowUserRoleInfo = !mNavigationIsShowUserRoleInfo);
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
            rotation = ObjectAnimator.ofFloat(mIvSwitchUserRoleInfo, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutUserRoleInfo, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenUserRoleInfo, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(mIvSwitchUserRoleInfo, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutUserRoleInfo, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenUserRoleInfo, "alpha", 0, 1);
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
        ViewUtils.setVisibility(mTvOnclickOpenUserRoleInfo, !isShow);
        ViewUtils.setVisibility(mViewPager2UserRoleInfo, isShow);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshAdapter(boolean isShow) {
        //TabLayout适配的Fragment集合
        userRoleInfoFragments = new Fragment[]{
                DeveloperSelectUserRoleByUserNameFragment.newInstance(),
                DeveloperSelectHaveRoleUserInfoByRoleIdFragment.newInstance()
        };
        //装载List集合，提供给ViewPager2适配器使用
        List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(this.userRoleInfoFragments));
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        List<String> titleList = new ArrayList<>(Arrays.asList(MultiPageUserRoleTitle.getPageNames()));
        if (mViewPager2UserRoleInfo == null) {
            return;
        }
        if (isShow) { //打开按钮动态加载选项卡内容
            for (Fragment fragment : fragmentList) { //遍历Fragment集合，开始适配
                mUserRoleInfoAdapter.addFragment(fragment, titleList);
            }
            mUserRoleInfoAdapter.notifyDataSetChanged();
            mViewPager2UserRoleInfo.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(mTabLayoutUserRoleInfo);
        } else { // 清空适配全部数据
            mUserRoleInfoAdapter.clear();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        XToastUtils.toast("新选中了:" + tab.getText());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        XToastUtils.toast("未选中:" + tab.getText());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        XToastUtils.toast("再次选中了:" + tab.getText());
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
