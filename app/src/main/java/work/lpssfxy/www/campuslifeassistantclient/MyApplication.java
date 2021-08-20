package work.lpssfxy.www.campuslifeassistantclient;

import android.app.Application;
import android.content.Context;

/**
 * created by on 2021/8/18
 * 描述：Android应用启动单例类
 *
 * @author ZSAndroid
 * @create 2021-08-18-15:52
 */
public class MyApplication extends Application {


    public MyApplication ()  {
        super();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
