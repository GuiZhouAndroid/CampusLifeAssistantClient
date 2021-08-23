package work.lpssfxy.www.campuslifeassistantclient.utils.okhttp;

import okhttp3.Call;

/**
 * created by on 2021/8/23
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:14
 */
public interface  OkHttpResultCallback {

    void onError(Call call, Exception e);

    void onResponse(byte[] bytes);
}

