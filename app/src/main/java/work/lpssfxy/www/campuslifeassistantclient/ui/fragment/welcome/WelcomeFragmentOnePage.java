package work.lpssfxy.www.campuslifeassistantclient.ui.fragment.welcome;

import android.os.Bundle;
import android.widget.ImageView;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.welcome.LazyLoadFragment;

/**
 * created by on 2021/8/21
 * 描述：欢迎第一页
 *
 * @author ZSAndroid
 * @create 2021-08-21-17:49
 */
public class WelcomeFragmentOnePage extends LazyLoadFragment {
    ImageView mBgView;
    ImageView mShowView;

    public static WelcomeFragmentOnePage newInstance() {
        WelcomeFragmentOnePage page = new WelcomeFragmentOnePage();
        Bundle args = new Bundle();
        page.setArguments(args);
        return page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBgView = findViewById(R.id.image_one_bg);
        mShowView = findViewById(R.id.image_one_show);
    }

    @Override
    protected int setContentView() {
        return R.layout.welcome_frgament_one_page;
    }

    @Override
    protected void lazyLoad() {

    }
}