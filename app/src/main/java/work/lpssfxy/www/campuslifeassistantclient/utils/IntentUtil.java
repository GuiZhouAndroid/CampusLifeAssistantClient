package work.lpssfxy.www.campuslifeassistantclient.utils;

import android.app.Activity;
import android.content.Intent;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/11/22
 * 描述：Activity 或 Fragment 页面动画跳转工具类
 *
 * @author ZSAndroid
 * @create 2021-11-22-19:52
 */
public class IntentUtil {

    /**
     * Activity 或 Fragment
     * 浅入深出动画
     *
     * @param intent 跳转意图
     */
    public static void startActivityAnimActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_common_anim_out, R.anim.activity_common_anim_in);
    }

    /**
     * Activity 或 Fragment
     * 深入浅出动画
     *
     * @param intent 跳转意图
     */
    public static void startActivityAnimInAndOut(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.launch_anim_fade_in, R.anim.launch_anim_fade_out);
    }

    /**
     * Activity 或 Fragment 调用
     * 左————>右：启动动画
     *
     * @param intent 跳转意图
     */
    public static void startActivityAnimLeftToRight(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    /**
     * Activity 或 Fragment 调用
     * 左————>右：启动动画
     * 请求码+返回码 ---> 传值交互
     *
     * @param intent 跳转意图
     * @param code   请求码
     */
    public static void startActivityForResultAnimLeftToRight(Activity activity, Intent intent, int code) {
        activity.startActivityForResult(intent, code);
        activity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    /**
     * Activity 或 Fragment 调用
     * 右————>左：启动动画
     *
     * @param intent 跳转意图
     */
    public static void startActivityAnimRightToLeft(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }

    /**
     * Activity 或 Fragment 调用
     * 右————>左：启动动画
     * 请求码+返回码 ---> 传值交互
     *
     * @param intent 跳转意图
     * @param code   请求码
     */
    public static void startActivityForResultAnimRightToLeft(Activity activity, Intent intent, int code) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }
}
