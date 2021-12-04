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
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPageAccountSafeTitle;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperBannedAccountRealNameFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperKickOffLineRealNameFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperKickOffLineTokenFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectBannedStateRealNameFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectBannedStateUserIdFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectBannedTimeRealNameFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperUntieBannedAccountUserNameFragment;

/**
 * created by on 2021/11/26
 * 描述：开发者管理账户封禁模块
 *
 * @author ZSAndroid
 * @create 2021-11-26-17:32
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperManagerBannedAccountInfoFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    private static final String TAG = "DeveloperManagerBannedAccountInfoFragment";

    //顶部TabLayout
    @BindView(R2.id.tab_layout_ban_account_info) TabLayout mTabLayoutBanAccountInfo;
    //TabLayout展开文本
    @BindView(R2.id.tv_onclick_open_ban_account_info) TextView mTvOnclickOpenBanAccountInfo;
    //TabLayout展开图标
    @BindView(R2.id.iv_switch_ban_account_info) AppCompatImageView mIvSwitchBanAccountInfo;
    //ViewPager2左右滑动
    @BindView(R2.id.view_pager_ban_account_info) ViewPager2 mViewPager2BanAccountInfo;

    //展开状态
    private boolean mNavigationIsShowBanAccountInfo;
    //ViewPager2动态加载适配器
    private MyFragmentStateViewPager2Adapter mBanAccountInfoAdapter;
    //TabLayout适配的Fragment集合
    public Fragment[] banAccountInfoFragments = null;

    /**
     * @return 单例对象
     */
    public static DeveloperManagerBannedAccountInfoFragment newInstance() {
        return new DeveloperManagerBannedAccountInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_manager_developer_ban_account_info;
    }

    /**
     * 准备适配数据
     */
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
        refreshStatus(mNavigationIsShowBanAccountInfo = !mNavigationIsShowBanAccountInfo);
    }

    private void initTabLayout() {
        mBanAccountInfoAdapter = new MyFragmentStateViewPager2Adapter(this);
        mTabLayoutBanAccountInfo.setTabMode(MODE_SCROLLABLE);
        mTabLayoutBanAccountInfo.addOnTabSelectedListener(this);
        mViewPager2BanAccountInfo.setAdapter(mBanAccountInfoAdapter);
        // 设置缓存的数量
        mViewPager2BanAccountInfo.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayoutBanAccountInfo, mViewPager2BanAccountInfo, (tab, position) -> tab.setText(mBanAccountInfoAdapter.getPageTitle(position))).attach();
    }


    /**
     * 收缩 + 展开 刷新ViewPager2 实现动态加载
     *
     * @param view 控件id
     */
    @OnClick({R.id.iv_switch_ban_account_info, R.id.tv_onclick_open_ban_account_info})
    public void onViewBanAccountInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_ban_account_info:
            case R.id.tv_onclick_open_ban_account_info:
                refreshStatus(mNavigationIsShowBanAccountInfo = !mNavigationIsShowBanAccountInfo);
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
            rotation = ObjectAnimator.ofFloat(mIvSwitchBanAccountInfo, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutBanAccountInfo, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenBanAccountInfo, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(mIvSwitchBanAccountInfo, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutBanAccountInfo, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenBanAccountInfo, "alpha", 0, 1);
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
        ViewUtils.setVisibility(mTvOnclickOpenBanAccountInfo, !isShow);
        ViewUtils.setVisibility(mViewPager2BanAccountInfo, isShow);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshAdapter(boolean isShow) {
        //TabLayout适配的Fragment集合
        banAccountInfoFragments = new Fragment[]{
                DeveloperKickOffLineRealNameFragment.newInstance(),
                DeveloperKickOffLineTokenFragment.newInstance(),
                DeveloperSelectBannedStateRealNameFragment.newInstance(),
                DeveloperSelectBannedStateUserIdFragment.newInstance(),
                DeveloperBannedAccountRealNameFragment.newInstance(),
                DeveloperSelectBannedTimeRealNameFragment.newInstance(),
                DeveloperUntieBannedAccountUserNameFragment.newInstance()
        };
        //装载List集合，提供给ViewPager2适配器使用
        //装载List集合，提供给ViewPager2适配器使用
        List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(banAccountInfoFragments));
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        List<String> titleList = new ArrayList<>(Arrays.asList(MultiPageAccountSafeTitle.getPageNames()));
        if (mViewPager2BanAccountInfo == null) {
            return;
        }
        if (isShow) { //打开按钮动态加载选项卡内容
            for (Fragment fragment : fragmentList) { //遍历Fragment集合，开始适配
                mBanAccountInfoAdapter.addFragment(fragment, titleList);
            }
            mBanAccountInfoAdapter.notifyDataSetChanged();
            mViewPager2BanAccountInfo.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(mTabLayoutBanAccountInfo);
        } else { // 清空适配全部数据
            mBanAccountInfoAdapter.clear();
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
