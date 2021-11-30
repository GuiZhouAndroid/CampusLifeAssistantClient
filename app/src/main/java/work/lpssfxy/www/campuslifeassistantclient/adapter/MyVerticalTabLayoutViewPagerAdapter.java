package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * created by on 2021/11/16
 * 描述：开发者后台安全中心，垂直TabLayout + ViewPager+ Fragment 适配器
 *
 * @author ZSAndroid
 * @create 2021-11-16-11:42
 */
public class MyVerticalTabLayoutViewPagerAdapter extends FragmentStatePagerAdapter {


    private final List<String> TabLayoutTitleList = new ArrayList<>();

    //四个主功能Fragment界面
    public Fragment[] fragments = null;
    //创建Fragment集合，ViewPager适配器遍历绑定数组fragments

    public List<Fragment> fragmentList = null;

    public MyVerticalTabLayoutViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //主要调用找个构造函数来使用，在创建改适配器时，初始化TabLayout标题和遍历添加Fragment对象到List集合中
    public MyVerticalTabLayoutViewPagerAdapter(@NonNull FragmentManager fm, Fragment[] fragments, List<Fragment> fragmentList) {
        super(fm);
        // 1.添加TabLayout标题到String的List集合中
        TabLayoutTitleList.add("用户管理");
        TabLayoutTitleList.add("角色管理");
        TabLayoutTitleList.add("权限管理");
        TabLayoutTitleList.add("用户角色");
        TabLayoutTitleList.add("用户权限");
        TabLayoutTitleList.add("账户封禁");
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

    @Override
    public CharSequence getPageTitle(int position) {
        return TabLayoutTitleList.get(position);
    }
}
