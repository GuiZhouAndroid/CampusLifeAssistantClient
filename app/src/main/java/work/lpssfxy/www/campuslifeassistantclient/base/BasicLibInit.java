package work.lpssfxy.www.campuslifeassistantclient.base;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.hjq.http.EasyConfig;
import com.hjq.http.config.IRequestApi;
import com.hjq.http.config.IRequestInterceptor;
import com.hjq.http.config.IRequestServer;
import com.hjq.http.model.HttpHeaders;
import com.hjq.http.model.HttpParams;
import com.tencent.mmkv.MMKV;
import com.tencent.open.log.SLog;
import com.tencent.tauth.Tencent;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xui.XUI;
import com.xuexiang.xutil.XUtil;

import java.util.Locale;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.AutoSizeLog;
import okhttp3.OkHttpClient;
import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import work.lpssfxy.www.campuslifeassistantclient.App;
import work.lpssfxy.www.campuslifeassistantclient.BuildConfig;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.RequestHandler;
import work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.ReleaseServer;
import work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.TestServer;


/**
 * created by on 2021/10/17
 * 描述：初始化第三方资源
 *
 * @author ZSAndroid
 * @create 2021-10-17-19:44
 */
public class BasicLibInit {

    public static ClearableCookieJar cookie;

    private BasicLibInit() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化基础库
     */
    public static void init(Application application) {
        /** 初始化百度地图加载so文件 */
        SDKInitializer.initialize(application);
        /** 初始化全局适配AndroidAutoSize */
        initAutoSizeConfig(application);
        /** 初始化换肤框架 */
        initSkinCompatManager(application);
        /** 初始化QQ互联SDK实例 */
        initQQTencent(application);
        /** 初始化UI框架 */
        XUI.init(application);
        XUI.debug(true);
        /** 初始化XPage页面框架 */
        initPage(application);
        /** 初始化工具类 */
        initUtils(application);
        /** 初始化Okhttp3网络请求框架 */
        initOkHttpSDK(application);

        MMKV.initialize(application);


    }

    /**
     * 创建TencentSDK实例
     * @param application APP全局上下文
     */
    private static void initQQTencent(Application application) {
        Constant.mTencent = Tencent.createInstance(Constant.APP_ID, application, "work.lpssfxy.www.campuslifeassistantclient.fileprovider");
        if (Constant.mTencent != null) {
            SLog.i(App.TAG, "腾讯实例创建成功=="+Constant.mTencent);
        }else {
            SLog.e(App.TAG, "腾讯实例创建失败=="+ Constant.mTencent);
        }
    }

    /**
     * 初始化全局适配AndroidAutoSize
     * @param application APP全局上下文
     */
    private static void initAutoSizeConfig(Application application) {
        //就需要在 App多进程，适配所有的进程 初始化AutoSizeConfig时调用 initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(application);
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
     * @param application APP全局上下文
     */
    private static void initSkinCompatManager(Application application) {
        SkinCompatManager.withoutActivity(application)
                .addInflater(new SkinAppCompatViewInflater()) // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())// ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())// CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)// 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)// 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }

    /**
     * 初始化XPage页面框架
     *
     * @param application APP全局上下文
     */
    private static void initPage(Application application) {
        PageConfig.getInstance()
      //页面注册,默认不设置的话使用的就是自动注册
      //.setPageConfiguration(new AutoPageConfiguration())
                .debug("PageLog") //开启XPage构建配置文件DEBUG调试信息
                .setContainActivityClazz(XPageActivity.class) //设置默认的容器Activity
                .init(application);//初始化页面配置
    }

    /**
     * 初始化工具类
     *
     * @param application APP全局上下文
     */
    private static void initUtils(Application application) {
        XUtil.init(application);
        XUtil.debug(App.isDebug());
    }

    /**
     * 初始化Okhttp3网络请求框架
     * @param application APP全局上下文
     */
    private static void initOkHttpSDK(Application application) {
        // 网络请求框架初始化
        IRequestServer server;
        if (BuildConfig.DEBUG) {
            server = new TestServer();
        } else {
            server = new ReleaseServer();
        }

        cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(application));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookie)
                //其他配置
                .build();

        EasyConfig.with(okHttpClient)
                // 是否打印日志
//                .setLogEnabled(BuildConfig.DEBUG)
                // 设置服务器配置
                .setServer(server)
                // 设置请求处理策略
                .setHandler(new RequestHandler(application))
                // 设置请求参数拦截器
                .setInterceptor(new IRequestInterceptor() {
                    @Override
                    public void interceptArguments(IRequestApi api, HttpParams params, HttpHeaders headers) {
                        headers.put("timestamp", String.valueOf(System.currentTimeMillis()));
                    }
                })
                // 设置请求重试次数
                .setRetryCount(1)
                // 设置请求重试时间
                .setRetryTime(2000)
                // 添加全局请求参数
                .addParam("token", "6666666")
                // 添加全局请求头
                //.addHeader("date", "20191030")
                .into();

//        OkHttpUtils.initClient(okHttpClient);
    }
}
