package work.lpssfxy.www.campuslifeassistantclient.base.bottom;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:54
 */
public class BottomUIUtils {
    /**
     * dip-->px
     */
    public static int dip2Px(Context context, int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context,float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getColor(Context context,int colorId){
        return context.getResources().getColor(colorId);
    }

    public static Drawable getDrawable(Context context, int resId){
        return  context.getResources().getDrawable(resId);
    }
}

