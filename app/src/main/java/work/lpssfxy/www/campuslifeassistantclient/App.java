package work.lpssfxy.www.campuslifeassistantclient;

import android.app.Application;
import android.content.Context;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkHttpUtil;

/**
 * created by on 2021/8/18
 * 描述：Android应用启动单例类
 *
 * @author ZSAndroid
 * @create 2021-08-18-15:52
 */
public class App extends Application {
    private static App sTinkerApp;

    @Override
    public void onCreate() {
        super.onCreate();
        //换肤框架
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
        OkHttpUtil.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        sTinkerApp = this;
    }

    public static Application getApplication() {
        if (sTinkerApp == null) {
            throw new NullPointerException("appcontext not create or destroy");
        }
        return sTinkerApp;
    }
}

