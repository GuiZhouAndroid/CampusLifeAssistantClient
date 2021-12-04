package work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * created by on 2021/11/15
 * 描述：图片加载工具类
 *
 * @author ZSAndroid
 * @create 2021-11-15-22:32
 */
public class ImageLoaderUtils {

    public static boolean assertValidRequest(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !isDestroy(activity);
        } else if (context instanceof ContextWrapper){
            ContextWrapper contextWrapper = (ContextWrapper) context;
            if (contextWrapper.getBaseContext() instanceof Activity){
                Activity activity = (Activity) contextWrapper.getBaseContext();
                return !isDestroy(activity);
            }
        }
        return true;
    }

    private static boolean isDestroy(Activity activity) {
        if (activity == null) {
            return true;
        }
        return activity.isFinishing() || activity.isDestroyed();
    }

}
