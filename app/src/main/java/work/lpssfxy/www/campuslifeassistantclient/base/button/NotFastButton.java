package work.lpssfxy.www.campuslifeassistantclient.base.button;

import android.view.View;

/**
 * created by on 2021/11/22
 * 描述：自定义OnClickListener：3秒内禁止重复触发单击事件
 *
 * @author ZSAndroid
 * @create 2021-11-22-17:39
 */
public class NotFastButton {

    public abstract static class NotFastClickListener implements View.OnClickListener {
        // 两次点击按钮之间的点击间隔不能少于1000毫秒
        private static final int MIN_CLICK_DELAY_TIME = 3000;
        private static long lastClickTime;

        public abstract void onNotFastClick(View v);

        @Override
        public void onClick(View v) {
            long curClickTime = System.currentTimeMillis();

            if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
                // 超过点击间隔后再将lastClickTime重置为当前点击时间
                lastClickTime = curClickTime;
                onNotFastClick(v);
            }
        }
    }
}
