package work.lpssfxy.www.campuslifeassistantclient.ui.fragment.welcome;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.welcome.LazyLoadFragment;
import work.lpssfxy.www.campuslifeassistantclient.ui.activity.WelcomeActivity;

/**
 * created by on 2021/8/21
 * 描述：欢迎第二页
 *
 * @author ZSAndroid
 * @create 2021-08-21-17:50
 */
public class WelcomeFragmentTwoPage extends LazyLoadFragment {
    ImageView mBgView;
    ImageView mShowView;
    ImageView mHeadView;
    Animation mShowAnim, mAlphaAnim;

    public static WelcomeFragmentTwoPage newInstance() {
        WelcomeFragmentTwoPage page = new WelcomeFragmentTwoPage();
        Bundle args = new Bundle();
        page.setArguments(args);
        return page;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBgView = findViewById(R.id.image_two_bg);
        mShowView = findViewById(R.id.image_two_show);
        mHeadView = findViewById(R.id.image_two_head);
        mShowAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_trans_two_bottom_up);
        mAlphaAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.welcome_alpha);
    }

    @Override
    protected int setContentView() {
        return R.layout.welcome_frgament_two_page;
    }

    @Override
    protected void lazyLoad() {
        if (WelcomeActivity.SHOW_TWO_ANIM) {
            mBgView.setVisibility(View.INVISIBLE);
            mShowView.setVisibility(View.INVISIBLE);
            mHeadView.setVisibility(View.INVISIBLE);
            mBgView.post(new Runnable() {
                @Override
                public void run() {
                    mBgView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mBgView.startAnimation(mAlphaAnim);
                            mBgView.setVisibility(View.VISIBLE);
                            mHeadView.startAnimation(mAlphaAnim);
                            mHeadView.setVisibility(View.VISIBLE);
                        }
                    }, 250);
                    mShowView.startAnimation(mShowAnim);
                    mShowView.setVisibility(View.VISIBLE);

                }
            });
        } else {
            mBgView.setVisibility(View.VISIBLE);
            mShowView.setVisibility(View.VISIBLE);
            mHeadView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}