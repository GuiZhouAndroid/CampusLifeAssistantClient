package work.lpssfxy.www.campuslifeassistantclient.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtil;

/**
 * created by on 2021/8/12
 * 描述：Activity基类
 *
 * @author ZSAndroid
 * @create 2021-08-12-2:08
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";
    //防触碰使用的变量
    private long firstTime;
    //解绑ButterKnife
    private Unbinder mUnbinder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(bindLayout());
        //初始化ButterKnife注解
        mUnbinder = ButterKnife.bind(this);
        //设置全局状态栏透明
//        setStatusBar();
        // 准备数据
        prepareData();
        // 初始化标题栏
//        initToolbar();
        // 初始化视图
        initView();
        // 初始化数据
        initData(savedInstanceState);
        // 初始化事件监听
        initEvent();
    }

    /**
     * 绑定子类布局
     *
     * @return 布局文件的资源ID
     */
    public abstract int bindLayout();

    /**
     * 准备数据（从Intent获取上一个界面传过来的数据或其他需要初始化的数据）
     */
    protected abstract void prepareData();

    /**
     * 初始化视图，findViewById等等
     */
    protected abstract void initView();

    /**
     * 初始化数据，从本地或服务器开始获取数据
     *
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化事件监听，setOnClickListener等等
     */
    protected abstract void initEvent();


    /**
     * Activity启动时
     */
    @Override
    protected void onStart() {
        Log.d(TAG, "-->onStart");
        super.onStart();
    }

    /**
     * Activity中断重新开始
     */
    @Override
    protected void onResume() {
        Log.d(TAG, "-->onResume");
        // 有奖分享处理
        super.onResume();
    }

    /**
     * Activity暂停时
     */
    @Override
    protected void onPause() {
        Log.d(TAG, "-->onPause");
        super.onPause();
    }

    /**
     * Activity停止时
     */
    @Override
    protected void onStop() {
        Log.d(TAG, "-->onStop");
        super.onStop();
    }

    /**
     * Activity重启时
     */
    @Override
    protected void onRestart() {
        Log.d(TAG, "-->onRestart:");
        super.onRestart();
    }

    /**
     * Activity销毁时清理资源
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "-->onDestroy");
        super.onDestroy();
        //解绑ButterKnife
        mUnbinder.unbind();
    }

    /**
     * 实现BaseView的showToast(CharSequence msg)
     *
     * @param msg 吐司显示的信息
     */
    public void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 设置状态栏透明
     */
    public void setStatusBar(Activity activity) {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(activity, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(activity);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(activity, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(activity, 0x55000000);
        }
    }

    /**
     * 监听onClick单击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

    }

    /**
     * 防触碰处理
     * 再按一次退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 3000) {
                    Toast.makeText(this, "再按一次退出程序！", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 全屏沉浸隐藏底部导航栏
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
