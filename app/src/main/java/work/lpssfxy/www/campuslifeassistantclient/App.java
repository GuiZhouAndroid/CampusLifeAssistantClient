package work.lpssfxy.www.campuslifeassistantclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.tencent.open.log.SLog;
import com.tencent.tauth.Tencent;

import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.AutoSizeLog;
import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkHttpUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.LoginActivity;

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
    private static final String TAG = "App";
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
        /** Application全局静态实例 */
        myApp = this;
        /** 初始化全局OkHttp3工具类 */
        OkHttpUtil.init(this);
        /** 初始化百度地图加载so文件 */
        SDKInitializer.initialize(this);
        /** 初始化全局适配AndroidAutoSize */
        initAutoSizeConfig();
        /** 初始化换肤框架 */
        initSkinCompatManager();
        /** 初始化QQ互联SDK实例 */
        initQQTencent();
    }


    /**
     * 初始化全局适配AndroidAutoSize
     */
    private void initAutoSizeConfig() {
        //就需要在 App多进程，适配所有的进程 初始化AutoSizeConfig时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this);
        /** 获取框架实例 */
        AutoSizeConfig.getInstance()
                //开启框架支持自定义Fragment的适配参数
                .setCustomFragment(true)
                //开启屏蔽系统字体大小对AndroidAutoSize适配的影响
                .setExcludeFontScale(true)
                //是否打印 AutoSize 的内部日志, 默认为 true
                //.setLog(false/true)
                //屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        //使用以下代码, 可以解决横竖屏切换时的屏幕适配问题
                        //使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
                        //系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
                        //AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                        //AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptBefore!", target.getClass().getName()));
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                        AutoSizeLog.d(String.format(Locale.ENGLISH, "%s onAdaptAfter!", target.getClass().getName()));
                    }
                });
        //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
        //AutoSize 会将屏幕总高度减去状态栏高度来做适配
        //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
        //在全面屏或刘海屏幕设备中, 获取到的屏幕高度可能不包含状态栏高度, 所以在全面屏设备中不需要减去状态栏高度，所以可以 setUseDeviceSize(true)
        //.setUseDeviceSize(true)

        //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
        //.setBaseOnWidth(false)

        //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
        //.setAutoAdaptStrategy(new AutoAdaptStrategy())
    }

    /**
     * 初始化换肤框架
     */
    private void initSkinCompatManager() {
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater()) // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())// ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())// CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)// 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)// 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }

    /**
     * 创建TencentSDK实例
     */
    private void initQQTencent() {
        Constant.mTencent = Tencent.createInstance("101965703", getApplicationContext(), "work.lpssfxy.www.campuslifeassistantclient.fileprovider");
        if (Constant.mTencent != null) {
            SLog.i(TAG, "腾讯实例创建成功=="+Constant.mTencent);
        }else {
            SLog.e(TAG, "腾讯实例创建失败=="+ Constant.mTencent);
        }
    }
}

