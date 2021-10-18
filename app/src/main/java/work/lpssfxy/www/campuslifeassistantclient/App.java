package work.lpssfxy.www.campuslifeassistantclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import work.lpssfxy.www.campuslifeassistantclient.base.BasicLibInit;

/**
 * created by on 2021/8/18
 * 描述：Android应用启动单例类，APP生命周期：开始——>结束的过程（A-->--B-->C）
 * 此过程中，Activity上下文，始终都能访问APP类的静态资源。
 * 避免：内存泄露——>内存溢出
 *
 * @author ZSAndroid
 * @create 2021-08-18-15:52
 */
@SuppressLint("StaticFieldLeak")
public class App extends Application {
    public static final String TAG = "Application";
    /** 应用程序单例模式App全局变量 */
    private static App myApp;
    public static Activity appActivity =null;

    public App() {
    }

    /**
     * 外部调用APP全局变量实例方法
     *
     * @return
     */

    public static App getInstance() {
        return myApp;
    }

    /**
     * 生命周期组成部分B
     * 次于App()之后执行
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    /**
     * 生命周期组成部分C
     * 次于attachBaseContext(Context base)之后执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
        /** Application全局静态单例模式 */
        myApp = this;
        /** 初始化第三方资源SDK包 */

        BasicLibInit.init(myApp);
    }

    /**
     * XPage 开启DEBUG调试信息
     * @return
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}

