package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

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
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyFragmentStateViewPager2Adapter;
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPageRoleTitle;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/26
 * 描述：开发者管理角色模块
 *
 * @author ZSAndroid
 * @create 2021-11-26-11:13
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperManagerRoleInfoFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tab_layout_role_info) TabLayout mTabLayoutRoleInfo;
    @BindView(R.id.tv_onclick_open_role_info) TextView mTvOnclickOpenRoleInfo;
    @BindView(R.id.iv_switch_role_info) AppCompatImageView mIvSwitchRoleInfo;
    @BindView(R.id.view_pager_role_info) ViewPager2 mViewPager2RoleInfo;

    private boolean mNavigationIsShowRoleInfo;
    //ViewPager2动态加载适配器
    private MyFragmentStateViewPager2Adapter mAdapter;

    //TabLayout适配的Fragment集合
    public Fragment[] fragments = null;

    /**
     * @return 单例对象
     */
    public static DeveloperManagerRoleInfoFragment newInstance() {
        return new DeveloperManagerRoleInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_manager_fragment_role_info;
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
        refreshStatus(mNavigationIsShowRoleInfo = !mNavigationIsShowRoleInfo);
    }

    private void initTabLayout() {
        mAdapter = new MyFragmentStateViewPager2Adapter(this);
        mTabLayoutRoleInfo.setTabMode(MODE_SCROLLABLE);
        mTabLayoutRoleInfo.addOnTabSelectedListener(this);
        mViewPager2RoleInfo.setAdapter(mAdapter);
        // 设置缓存的数量
        mViewPager2RoleInfo.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayoutRoleInfo, mViewPager2RoleInfo, (tab, position) -> tab.setText(mAdapter.getPageTitle(position))).attach();
    }


    /**
     * 收缩 + 展开 刷新ViewPager2 实现动态加载
     * @param view 控件id
     */
    @OnClick({R.id.iv_switch_role_info,R.id.tv_onclick_open_role_info})
    public void onViewRoleInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_role_info:
            case R.id.tv_onclick_open_role_info:
                refreshStatus(mNavigationIsShowRoleInfo = !mNavigationIsShowRoleInfo);
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
            rotation = ObjectAnimator.ofFloat(mIvSwitchRoleInfo, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutRoleInfo, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenRoleInfo, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(mIvSwitchRoleInfo, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutRoleInfo, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenRoleInfo, "alpha", 0, 1);
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
        ViewUtils.setVisibility(mTvOnclickOpenRoleInfo, !isShow);
        ViewUtils.setVisibility(mViewPager2RoleInfo, isShow);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshAdapter(boolean isShow) {
        //TabLayout适配的Fragment集合
        fragments = new Fragment[]{
                DeveloperSelectAllRoleInfoFragment.newInstance(),
                DeveloperAddRoleInfoFragment.newInstance(),
                DeveloperSelectUserRoleByUserNameFragment.newInstance(),
                DeveloperSelectHaveRoleUserInfoByRoleIdFragment.newInstance()
        };
        //装载List集合，提供给ViewPager2适配器使用
        List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(this.fragments));
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        List<String> titleList = new ArrayList<>(Arrays.asList(MultiPageRoleTitle.getPageNames()));
        if (mViewPager2RoleInfo == null) {
            return;
        }
        if (isShow) { //打开按钮动态加载选项卡内容
            for (Fragment fragment: fragmentList) { //遍历Fragment集合，开始适配
                mAdapter.addFragment(fragment, titleList);
            }
            mAdapter.notifyDataSetChanged();
            mViewPager2RoleInfo.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(mTabLayoutRoleInfo);
        } else { // 清空适配全部数据
            mAdapter.clear();
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
