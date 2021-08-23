package work.lpssfxy.www.campuslifeassistantclient.view.fragment.welcome;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.welcome.LazyLoadFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.WelcomeActivity;

/**
 * created by on 2021/8/21
 * 描述：欢迎第三页
 *
 * @author ZSAndroid
 * @create 2021-08-21-17:51
 */
public class WelcomeFragmentThreePage extends LazyLoadFragment {
    ImageView mBgView;
    ImageView mShowView;
    Animation mShowAnim, mAlphaAnim;

    public static WelcomeFragmentThreePage newInstance() {
        WelcomeFragmentThreePage page = new WelcomeFragmentThreePage();
        Bundle args = new Bundle();
        page.setArguments(args);
        return page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBgView = findViewById(R.id.image_three_bg);
        mShowView = findViewById(R.id.image_three_show);
        mShowAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_trans_three_bottom_up);
        mAlphaAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_alpha);
        mBgView.setVisibility(View.INVISIBLE);
        mShowView.setVisibility(View.INVISIBLE);

    }

    @Override
    protected int setContentView() {
        return R.layout.welcome_frgament_three_page;
    }

    @Override
    protected void lazyLoad() {
        WelcomeActivity.SHOW_TWO_ANIM = false;
        mBgView.post(new Runnable() {
            @Override
            public void run() {
                mBgView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBgView.startAnimation(mAlphaAnim);
                        mBgView.setVisibility(View.VISIBLE);
                    }
                }, 250);

                mShowView.startAnimation(mShowAnim);
                mShowView.setVisibility(View.VISIBLE);
            }
        });
    }
}