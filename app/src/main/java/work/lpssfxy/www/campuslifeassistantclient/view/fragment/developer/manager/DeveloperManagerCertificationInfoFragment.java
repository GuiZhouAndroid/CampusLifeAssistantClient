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
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPageCertificationTitle;
import work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab.MultiPageUserTitle;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectAllCertificationInfoFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperSelectAllUserInfoFragment;

/**
 * created by on 2021/12/1
 * 描述：开发者认证管理模块
 *
 * @author ZSAndroid
 * @create 2021-12-01-22:45
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperManagerCertificationInfoFragment extends BaseFragment implements TabLayout.OnTabSelectedListener{

    //顶部TabLayout
    @BindView(R2.id.tab_layout_certification_info)TabLayout mTabLayoutCertificationInfo;
    //TabLayout展开文本
    @BindView(R2.id.tv_onclick_open_certification_info) TextView mTvOnclickOpenCertificationInfo;
    //TabLayout展开图标
    @BindView(R2.id.iv_switch_certification_info) AppCompatImageView mIvSwitchCertificationInfo;
    //ViewPager2左右滑动
    @BindView(R2.id.view_pager_certification_info) ViewPager2 mViewPager2CertificationInfo;

    //展开状态
    private boolean mNavigationIsShowCertificationInfo;
    //ViewPager2动态加载适配器
    private MyFragmentStateViewPager2Adapter mCertificationInfoAdapter;
    //TabLayout适配的Fragment集合
    public Fragment[] certificationInfoFragments = null;

    /**
     * @return 单例对象
     */
    public static DeveloperManagerCertificationInfoFragment newInstance() {
        return new DeveloperManagerCertificationInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_manager_fragment_certification_info;
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
        refreshStatus(mNavigationIsShowCertificationInfo = !mNavigationIsShowCertificationInfo);
    }

    private void initTabLayout() {
        mCertificationInfoAdapter = new MyFragmentStateViewPager2Adapter(this);
        mTabLayoutCertificationInfo.setTabMode(MODE_SCROLLABLE);
        mTabLayoutCertificationInfo.addOnTabSelectedListener(this);
        mViewPager2CertificationInfo.setAdapter(mCertificationInfoAdapter);
        // 设置缓存的数量
        mViewPager2CertificationInfo.setOffscreenPageLimit(1);
        new TabLayoutMediator(mTabLayoutCertificationInfo, mViewPager2CertificationInfo, (tab, position) -> tab.setText(mCertificationInfoAdapter.getPageTitle(position))).attach();
    }
    /**
     * 收缩 + 展开 刷新ViewPager2 实现动态加载
     *
     * @param view 控件id
     */
    @OnClick({R.id.iv_switch_certification_info, R.id.tv_onclick_open_certification_info})
    public void onViewCertificationInfoClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_switch_certification_info:
            case R.id.tv_onclick_open_certification_info:
                refreshStatus(mNavigationIsShowCertificationInfo = !mNavigationIsShowCertificationInfo);
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
            rotation = ObjectAnimator.ofFloat(mIvSwitchCertificationInfo, "rotation", 0, -45);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutCertificationInfo, "alpha", 0, 1);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenCertificationInfo, "alpha", 1, 0);
        } else {
            rotation = ObjectAnimator.ofFloat(mIvSwitchCertificationInfo, "rotation", -45, 0);
            tabAlpha = ObjectAnimator.ofFloat(mTabLayoutCertificationInfo, "alpha", 1, 0);
            textAlpha = ObjectAnimator.ofFloat(mTvOnclickOpenCertificationInfo, "alpha", 0, 1);
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
        ViewUtils.setVisibility(mTvOnclickOpenCertificationInfo, !isShow);
        ViewUtils.setVisibility(mViewPager2CertificationInfo, isShow);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshAdapter(boolean isShow) {
        //TabLayout适配的Fragment集合
        certificationInfoFragments = new Fragment[]{
                DeveloperSelectAllCertificationInfoFragment.newInstance()
        };
        //装载List集合，提供给ViewPager2适配器使用
        List<Fragment> fragmentList = new ArrayList<>(Arrays.asList(this.certificationInfoFragments));
        //装载TabLayout标题集合，提供给ViewPager2适配器使用
        List<String> titleList = new ArrayList<>(Arrays.asList(MultiPageCertificationTitle.getPageNames()));
        if (mViewPager2CertificationInfo == null) {
            return;
        }
        if (isShow) { //打开按钮动态加载选项卡内容
            for (Fragment fragment : fragmentList) { //遍历Fragment集合，开始适配
                mCertificationInfoAdapter.addFragment(fragment, titleList);
            }
            mCertificationInfoAdapter.notifyDataSetChanged();
            mViewPager2CertificationInfo.setCurrentItem(0, false);
            WidgetUtils.setTabLayoutTextFont(mTabLayoutCertificationInfo);
        } else { // 清空适配全部数据
            mCertificationInfoAdapter.clear();
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
