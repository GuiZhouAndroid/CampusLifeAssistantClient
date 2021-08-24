package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.scrollview.GoTopNestedScrollView;


/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
public class BottomHomeFragment extends Fragment {


    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.index_fragment_bottom_home, container, false);
        Toolbar toolbar = getActivity().findViewById(R.id.appbar_constant_toolbar);
        GoTopNestedScrollView goTopNestedScrollView = view.findViewById(R.id.go_top_scrollview);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appbar);
        CollapsingToolbarLayout collapsing_toolbar_layout = getActivity().findViewById(R.id.collapsing_toolbar_layout);
        FloatingActionButton floating_action_btn = getActivity().findViewById(R.id.floating_action_btn);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int toolbarHeight = appBarLayout.getTotalScrollRange();
                int dy = Math.abs(verticalOffset);
                if (dy <= toolbarHeight) {
                    float scale = (float) dy / toolbarHeight;
                    float alpha = scale * 255;


                    toolbar.setBackgroundColor(Color.argb((int) alpha, 100, 200, 200));
                    collapsing_toolbar_layout.setBackgroundColor(Color.argb((int) alpha, 100, 200, 200));
                }
            }
        });
        //设置点击置顶的ImageView
        goTopNestedScrollView.setImageViewOnClickGoToFirst(floating_action_btn);
        //ScrollView滑动超过屏幕高度则显示置顶按钮,不设置的话就会使用自定义View中的默认高度
        DisplayMetrics metric = new DisplayMetrics();//获取屏幕高度
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        goTopNestedScrollView.setScreenHeight(metric.heightPixels);//设置高度

        return view;
    }
}
