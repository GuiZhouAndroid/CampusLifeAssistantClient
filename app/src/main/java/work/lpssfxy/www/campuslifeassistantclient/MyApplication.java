package work.lpssfxy.www.campuslifeassistantclient;

import android.app.Activity;
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
    //Activity全局变量
    public static Activity AppContext = null;

    /**
     * MyApplication全局构造方法，创建Application实例
     */
    public MyApplication() {
        super();
    }

    /**
     * 生命周期：在Application构造函数之后+onCreate()之前执行
     * @param base 上下文
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }
}
