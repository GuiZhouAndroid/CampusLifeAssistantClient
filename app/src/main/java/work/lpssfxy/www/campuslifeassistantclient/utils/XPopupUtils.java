package work.lpssfxy.www.campuslifeassistantclient.utils;

import android.app.Activity;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

/**
 * created by on 2021/11/5
 * 描述： XPopup自定义工具类，请求进度条的延迟关闭，仿真网络请求
 *
 * @author ZSAndroid
 * @create 2021-11-05-23:25
 */
public class XPopupUtils {
    private static BasePopupView popupView;

    public static void setShowDialog(Activity activity,String msg){
        popupView = new XPopup.Builder(activity)
                .asLoading(msg)
                .show();
    }

    public static void setSmartDisDialog(){
        popupView.smartDismiss();
    }

    public static void setTimerDisDialog(){
        popupView.delayDismiss(500);
    }
}
