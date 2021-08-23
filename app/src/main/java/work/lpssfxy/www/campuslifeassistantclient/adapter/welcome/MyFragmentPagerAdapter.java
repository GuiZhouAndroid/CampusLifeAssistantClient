package work.lpssfxy.www.campuslifeassistantclient.adapter.welcome;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import work.lpssfxy.www.campuslifeassistantclient.view.fragment.welcome.WelcomeFragmentOnePage;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.welcome.WelcomeFragmentThreePage;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.welcome.WelcomeFragmentTwoPage;

/**
 * created by on 2021/8/21
 * 描述：引导页图片嵌入ViewPager适配器
 *
 * @author ZSAndroid
 * @create 2021-08-21-17:43
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment itemFragment = null;
        switch (position) {
            case 0:
                itemFragment = WelcomeFragmentOnePage.newInstance();//获取Fragment第一页实例
                break;
            case 1:
                itemFragment = WelcomeFragmentTwoPage.newInstance();//获取Fragment第二页实例
                break;
            case 2:
                itemFragment = WelcomeFragmentThreePage.newInstance();//获取Fragment第三页实例
                break;
            default:
                break;
        }
        return itemFragment;//返回Fragment实例
    }

    /**
     * 适配数据
     *
     * @return Fragment数量
     */
    @Override
    public int getCount() {
        return 3;
    }
}
