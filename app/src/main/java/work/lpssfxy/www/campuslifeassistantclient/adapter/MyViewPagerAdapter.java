package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomCategoryFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomHomeFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomMineFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomShopFragment;

/**
 * created by on 2021/8/23
 * 描述：ViewPager适配器
 *
 * @author ZSAndroid
 * @create 2021-08-23-23:49
 */
public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    /** 四个主功能Fragment界面 */
    private Fragment[] fragments =null;
    /** 创建Fragment集合，ViewPager适配器遍历绑定数组fragments*/
    private List<Fragment> fragmentList =null;

    public MyViewPagerAdapter(@NonNull FragmentManager fm, Fragment[] fragments, List<Fragment> fragmentList) {
        super(fm);
        this.fragments = fragments;
        this.fragmentList = fragmentList;
    }

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}