package work.lpssfxy.www.campuslifeassistantclient.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtils;

/**
 * created by on 2021/8/12
 * 描述：Activity基类
 *
 * @author ZSAndroid
 * @create 2021-08-12-2:08
 */
public abstract class BaseActivity extends SwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "BaseActivity";
    /** 防触碰使用的变量 */
    private long firstTime;
    /** 解绑ButterKnife */
    private Unbinder mUnbinder;
    /** 滑动返回 */
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 初始化绑定布局 */
        setContentView(bindLayout());
        /** 初始化滑动返回实例 */
        mSwipeBackLayout = getSwipeBackLayout();
        /** 初始化ButterKnife实例 */
        mUnbinder = ButterKnife.bind(this);

        /** 初始化滑动返回 */
        initSwipeBackLayoutState(isSetSwipeBackLayout());
        /** 初始化沉浸式状态栏 */
        initImmersiveStatusBar(isSetStatusBarState());
        /** 初始化底部导航栏 */
        initImmersiveBottomNaCation(isSetBottomNaviCationState());
        /** 初始化底部导航栏颜色 */
        initBottomNaCationColor(isSetBottomNaviCationColor());
        /** 初始化全屏沉浸 */
        initImmersiveFullScreen(isSetImmersiveFullScreen());
        /** 初始化准备数据 */
        prepareData();//
        /** 初始化标题栏 */
        //initToolbar();
        /** 初始化视图 */
        initView();
        /** 初始化数据 */
        initData(savedInstanceState);
        /** 初始化监听事件 */
        initEvent();
    }
    /**
     * @return 是否滑动返回
     */
    protected abstract Boolean isSetSwipeBackLayout();
    /**
     * 初始化滑动返回
     *
     * @return true:开启滑动返回 false:关闭滑动返回
     */
    public void initSwipeBackLayoutState(Boolean state) {
        if (state) {
            mSwipeBackLayout.setEnableGesture(true);
        }else {
            mSwipeBackLayout.setEnableGesture(false);
        }
    }
    /**
     * @return 是否沉浸状态栏
     */
    protected abstract Boolean isSetStatusBarState();

    /**
     * 初始化沉浸式状态栏
     *
     * @return true:开启沉浸状态栏 false:开启半透明状态栏
     */
    public void initImmersiveStatusBar(Boolean state) {
        if (state) {
            StatusBarUtils.setStatusTransparent(this);
        }else {
            StatusBarUtils.setStatusTranslucent(this);
        }
    }

    /**
     * @return 是否自动固定隐藏底部导航栏
     */
    protected abstract Boolean isSetBottomNaviCationState();

    /**
     * 初始化底部导航栏
     *
     * @return true:开启自动固定隐藏底部导航栏
     */
    public void initImmersiveBottomNaCation(Boolean state) {
        if (state) {
            StatusBarUtils.setAutoFixHideNaviCation(this);//触屏有效
        }else {
            Log.i(TAG, "固定隐藏底部导航栏--->返回值不正确！！！");
        }
    }

    /**
     * @return 是否默认底部导航栏White颜色
     */
    protected abstract Boolean isSetBottomNaviCationColor();

    /**
     * 初始化底部导航栏White颜色,关闭时默认半透明状态
     *
     * @return true:开启底部导航白色无透明 false:开启底部导航黑色半透明
     */
    public void initBottomNaCationColor(Boolean state) {
        if (state) {
            StatusBarUtils.setBottomNavigationSingleColor(this, R.color.white);
        }else {
            StatusBarUtils.setBottomNavigationTranslucent(this);
        }
    }
    /**
     * @return 是否全屏沉浸：顶部状态栏透明+底部导航栏透明
     */
    protected abstract Boolean isSetImmersiveFullScreen();

    /**
     * 初始化全屏沉浸
     *
     * @return true:全屏沉浸
     */
    public void initImmersiveFullScreen(Boolean state) {
        if (state) {
            StatusBarUtils.fullScreen(this);
        }else {
            Log.i(TAG, "全屏沉浸失败--->返回值不正确！！！");
        }
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
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

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
