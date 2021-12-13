package work.lpssfxy.www.campuslifeassistantclient.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * created by on 2021/12/13
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-13-16:40
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list;
    private List<String> list_Title;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> list, List<String> list_Title) {
        super(fm);
        this.list = list;
        this.list_Title = list_Title;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    //重写这个方法，将设置每个Tab的标题
    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return list_Title.get(position % list_Title.size());
    }
}
