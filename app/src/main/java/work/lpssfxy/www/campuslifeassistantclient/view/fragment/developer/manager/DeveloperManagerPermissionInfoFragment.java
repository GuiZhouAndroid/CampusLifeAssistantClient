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
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPagePermissionTitle;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectAllPermissionInfoFragment;

/**
 * created by on 2021/11/27
 * 描述：开发者管理权限模块
 *
 * @author ZSAndroid
 * @create 2021-11-27-13:54
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperManagerPermissionInfoFragment extends BaseFragment{

    private static final String TAG = "DeveloperManagerPermissionInfoFragment";

    //顶部TabLayout
    @BindView(R2.id.tab_layout_permission_info) TabLayout mTabLayoutPermissionInfo;
    //TabLayout展开文本
    @BindView(R2.id.tv_onclick_open_permission_info) TextView mTvOnclickOpenPermissionInfo;
    //TabLayout展开图标
    @BindView(R2.id.iv_switch_permission_info) AppCompatImageView mIvSwitchPermissionInfo;
    //ViewPager2左右滑动
    @BindView(R2.id.view_pager_permission_info) ViewPager2 mViewPager2PermissionInfo;

    //展开状态
    private boolean mNavigationIsShowPermissionInfo;
    //ViewPager2动态加载适配器
    private MyFragmentStateViewPager2Adapter mPermissionInfoAdapter;
    //TabLayout适配的Fragment集合
    public Fragment[] permissionInfoFragments = null;

    /**
     * @return 单例对象
     */
    public static DeveloperManagerPermissionInfoFragment newInstance() {
        return new DeveloperManagerPermissionInfoFragment();
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_manager_permission_info;
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
        refreshStatus(mNavigationIsShowPermissionInfo = !mNavigationIsShowPermissionInfo);
    }

    private void initTabLayout() {
        mPermissionInfoAdapter = new MyFragmentStateViewPager2Adapter(this);
        mTabLayoutPermissionInfo.setTabMode(MODE_SCROLLABLE);
        mViewPager2PermissionInfo.setAdapter(mPermissionInfoAdapter);
        // 设置缓存的数量
        mViewPager2PermissionInfo.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayoutPermissionInfo, mViewPager2PermissionInfo, (tab, position) -> tab.setText(mPermissionInfoAdapter.getPageTitle(position))).attach();
    }

    /**
     * 收缩 + 展开 刷新ViewPager2 实现动态加载
     *
     * @param view 控件id
     */
    @OnClick({R.id.iv_switch_permission_info, R.id.tv_onclick_open_permission_info})
    public void onViewPermissionInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_permission_info:
            case R.id.tv_onclick_open_permission_info:
                refreshStatus(mNavigationIsShowPermissionInfo = !mNavigationIsShowPermissionInfo);
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
            rotation = ObjectAnimator.ofFloat(mIvSwitchPermissionInfo, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutPermissionInfo, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenPermissionInfo, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(mIvSwitchPermissionInfo, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutPermissionInfo, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenPermissionInfo, "alpha", 0, 1);
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
        ViewUtils.setVisibility(mTvOnclickOpenPermissionInfo, !isShow);
        ViewUtils.setVisibility(mViewPager2PermissionInfo, isShow);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshAdapter(boolean isShow) {
        //TabLayout适配的Fragment集合
        permissionInfoFragments = new Fragment[]{
                DeveloperSelectAllPermissionInfoFragment.newInstance()
        };
        //装载List集合，提供给ViewPager2适配器使用
        List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(this.permissionInfoFragments));
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        List<String> titleList = new ArrayList<>(Arrays.asList(MultiPagePermissionTitle.getPageNames()));
        if (mViewPager2PermissionInfo == null) {
            return;
        }
        if (isShow) { //打开按钮动态加载选项卡内容
            for (Fragment fragment : fragmentList) { //遍历Fragment集合，开始适配
                mPermissionInfoAdapter.addFragment(fragment, titleList);
            }
            mPermissionInfoAdapter.notifyDataSetChanged();
            mViewPager2PermissionInfo.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(mTabLayoutPermissionInfo);
        } else { // 清空适配全部数据
            mPermissionInfoAdapter.clear();
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
