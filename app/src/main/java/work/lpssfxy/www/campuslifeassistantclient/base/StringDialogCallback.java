package work.lpssfxy.www.campuslifeassistantclient.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/10/18
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-18-13:03
 */
public abstract class StringDialogCallback extends StringCallback {

    private ProgressDialog dialog;

    public StringDialogCallback(Activity activity) {
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(activity.getString(R.string.loading_request_service));
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
//            Log.i(TAG, "onStart请求URL地址: " + request.getBaseUrl());//获取请求URL地址
//            Log.i(TAG, "onStart请求URL地址: " + request.getUrl());//获取请求URL地址
//            Log.i(TAG, "onStart请求参数: " + request.getUrlParam("openid"));//获取key对应的请求参数，这里直接放在URL，因此返回为Null
//            Log.i(TAG, "onStart请求参数: " + request.getParams());//获取全部的请求参数
//            Log.i(TAG, "onStart请求缓存模式: " + request.getCacheMode());//获取请求缓存模式
//            Log.i(TAG, "onStart请求请求头: " + request.getHeaders());//获取请求头
//            Log.i(TAG, "onStart请求请求方法: " + request.getMethod());//获取请求方法->post
//            Log.i(TAG, "onStart获取缓存时间: " + request.getCacheTime());//获取缓存时间 -1代表永久
//            Log.i(TAG, "onStart获取请求标记名称: " + request.getTag());//获取请求标记名称
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
