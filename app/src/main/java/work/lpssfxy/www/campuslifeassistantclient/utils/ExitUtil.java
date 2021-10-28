package work.lpssfxy.www.campuslifeassistantclient.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * created by on 2021/8/23
 * 描述：退出工具类
 *
 * @author ZSAndroid
 * @create 2021-08-23-13:57
 */
public class ExitUtil {
    private static long mFirstTime = 0;

    @SuppressLint("WrongConstant")
    public static void exitCheck(Activity activity, View view) {
        if (System.currentTimeMillis() - mFirstTime > 2000) {
            Snackbar.make(view, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mFirstTime = System.currentTimeMillis();
        } else {
            activity.finish();
            System.exit(0);
        }
    }
}
