package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * created by on 2021/8/23
 * 描述：ViewPager适配器
 *
 * @author ZSAndroid
 * @create 2021-08-23-23:49
 */
public class MyFragmentStateViewPagerAdapter extends FragmentStatePagerAdapter {
    /**
     * 四个主功能Fragment界面
     */
    public Fragment[] fragments = null;
    /**
     * 创建Fragment集合，ViewPager适配器遍历绑定数组fragments
     */
    public List<Fragment> fragmentList = null;

    public MyFragmentStateViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentStateViewPagerAdapter(@NonNull FragmentManager fm, Fragment[] fragments, List<Fragment> fragmentList) {
        super(fm);
        this.fragments = fragments;
        this.fragmentList = fragmentList;
        //Fragment需要填充的Fragment页面对象，例如new oneFragment()、 new Two Fragment()，放进List集合中，进行自动适配填充
        this.fragmentList.addAll(Arrays.asList(this.fragments));
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