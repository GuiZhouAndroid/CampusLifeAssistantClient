package work.lpssfxy.www.campuslifeassistantclient.base.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * created by on 2021/8/24
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-24-23:25
 */
public class GoTopNestedScrollView extends NestedScrollView implements View.OnClickListener {

    private FloatingActionButton floatingActionButton;//展示置顶的图片按钮
    private int screenHeight = 0;//屏幕高度 没有设置则默认50

    public GoTopNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    //设置滑动到多少出现
    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    //设置滚动置顶按钮以及其点击监听事件,
    public void setImageViewOnClickGoToFirst(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
        this.floatingActionButton.setOnClickListener(this);
    }


    //重写滚动改变返回的回调
    // l oldl 分别代表水平位移
    // t oldt 代表当前左上角距离Scrollview顶点的距离
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        /**
         * 滑动距离超过500px,出现置顶按钮,可以做为自定义属性
         * 滑动距离如果用户设置了使用用户的 如果用户没有设置使用默认的
         */
        //当 当前的左上角距离顶点距离 大于某个值的时候就显现置顶按钮出来 如果小于某个值就隐藏
        if (screenHeight != 0) {
            if (t > screenHeight) {
                floatingActionButton.setVisibility(VISIBLE);
            } else {
                floatingActionButton.setVisibility(GONE);
            }
        }

    }

    //置顶按钮的点击事件监听
    @Override
    public void onClick(View view) {
        //滑动到ScrollView的顶点
        this.smoothScrollTo(0, 0);
    }

}
