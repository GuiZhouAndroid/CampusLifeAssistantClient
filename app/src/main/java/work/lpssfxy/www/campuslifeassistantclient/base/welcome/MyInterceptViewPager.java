package work.lpssfxy.www.campuslifeassistantclient.base.welcome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * created by on 2021/8/21
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-21-16:53
 */
public class MyInterceptViewPager extends ViewPager {
    private boolean isScrollable = true;

    public MyInterceptViewPager(Context context) {
        super(context);
    }

    public MyInterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);//表示切换的时候，不需要切换时间。
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }
}

