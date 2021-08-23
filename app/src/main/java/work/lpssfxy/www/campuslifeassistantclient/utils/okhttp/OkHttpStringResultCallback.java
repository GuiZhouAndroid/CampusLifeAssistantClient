package work.lpssfxy.www.campuslifeassistantclient.utils.okhttp;

import okhttp3.Call;

/**
 * created by on 2021/8/23
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:15
 */
public interface OkHttpStringResultCallback {

    void onError(Call call, Exception e);

    void onResponse(String data);
}
