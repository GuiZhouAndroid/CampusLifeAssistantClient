package work.lpssfxy.www.campuslifeassistantclient.utils.okhttp;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.model.Response;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.CustomAlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;

/**
 * created by on 2021/11/22
 * 描述：OkGo自定义OkGo网络请求框架错误提示工具类
 *
 * @author ZSAndroid
 * @create 2021-11-22-19:00
 */
public class OkGoErrorUtil {

    /**
     * 自定义OkGo网络请求框架错误提示
     *
     * @param response OKGo错误响应字符串参数
     * @param activity 上下文
     * @param viewShow 显示的 Snackbar 视图
     * @param strMsg   提示信息
     */
    public static void CustomFragmentOkGoError(Response<String> response, Activity activity, View viewShow, String strMsg) {
        if (response.getException().toString().equals("com.lzy.okgo.exception.HttpException: network error! http response code is 404 or 5xx!")) {
            //一般为用户名已被占用，导致修改失败
            DialogPrompt dialogPrompt = new DialogPrompt(activity, strMsg, 10);
            dialogPrompt.showAndFinish(activity);
            return;
        }
        if (response.getException().toString().equals("java.net.UnknownHostException: Unable to resolve host \"www.lpssfxy.work\": No address associated with hostname")) {
            //其它情况，通常为网络不通导致
            new CustomAlertDialog(activity)
                    .builder()
                    .setCancelable(false)
                    .setTitle("超管提示")
                    .setTitleTextColor("#FF0000")
                    .setTitleTextSizeSp(18)
                    .setTitleTextBold(true)
                    .setMsg("网络异常，服务器连接失败！")
                    .setMsgTextBold(true)
                    .setOkButton("我知道了", 0, "#FF0000", "", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar snackbar = Snackbar.make(viewShow, "请检查当前设备网络情况是否畅通", Snackbar.LENGTH_SHORT).setActionTextColor(activity.getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                        }
                    })
                    .show();
        }
    }

    /**
     * 设置Snackbar上提示的字体颜色
     *
     * @param snackbar
     * @param color
     */
    public static void setSnackBarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
}
