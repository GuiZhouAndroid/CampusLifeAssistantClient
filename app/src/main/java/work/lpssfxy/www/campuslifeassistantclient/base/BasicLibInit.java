package work.lpssfxy.www.campuslifeassistantclient.base;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.tencent.open.log.SLog;
import com.tencent.tauth.Tencent;
import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.base.XPageActivity;
import com.xuexiang.xui.XUI;
import com.xuexiang.xutil.XUtil;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

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
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;


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
    }

    /**
     * 创建TencentSDK实例
     *
     * @param application APP全局上下文
     */
    private static void initQQTencent(Application application) {
        Constant.mTencent = Tencent.createInstance(Constant.APP_ID, application, "work.lpssfxy.www.campuslifeassistantclient.fileprovider");
        if (Constant.mTencent != null) {
            SLog.i(App.TAG, "腾讯实例创建成功==" + Constant.mTencent);
        } else {
            SLog.e(App.TAG, "腾讯实例创建失败==" + Constant.mTencent);
        }
    }

    /**
     * 初始化全局适配AndroidAutoSize
     *
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
     *
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
     *
     * @param application APP全局上下文
     */
    private static void initOkHttpSDK(Application application) {
        // 网络请求框架初始化

//        cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(application));
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .cookieJar(cookie)
//                //其他配置
//                .build();
        //okGo网络框架初始化和全局配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY); //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO); //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor); //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
        //builder.addInterceptor(new ChuckInterceptor(this));
        //自动管理cookie（或者叫session的保持）
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(application))); //使用sp保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));//使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));//使用内存保持cookie，app退出后，cookie消失
        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS); //全局的连接超时时间
        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //builder.hostnameVerifier(new SafeHostnameVerifier());
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");//header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");//param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
//-------------------------------------------------------------------------------------//
        //配置OkGo
        OkGo.getInstance().init(application) //必须调用初始化
                .setOkHttpClient(builder.build()) //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE) //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE) //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3) //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers) //全局公共头
                .addCommonParams(params); //全局公共参数
//        OkHttpUtils.initClient(okHttpClient);
    }

    private static class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */

    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }
}
