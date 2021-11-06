package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.CustomDialog;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;
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
    /** 解绑ButterKnife */
    private Unbinder mUnbinder;
    /** 滑动返回 */
    private SwipeBackLayout mSwipeBackLayout;
    /** 自定义对话框 */
    private AlertDialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 初始化绑定布局 */
        setContentView(bindLayout());
        /** 初始化运行时动态权限 */
        initPermissionState();
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
        /** 初始化事件 */
        initEvent();
        /** 初始化监听 */
        initListener();
        /** 业务操作 */
        doBusiness();

    }

    /**
     * 初始化运行时动态权限
     *
     */
    public void initPermissionState() {
//        XXPermissions.with(this)
//                // 申请安装包权限
//                .permission(Permission.REQUEST_INSTALL_PACKAGES)
//                // 申请悬浮窗权限
//                .permission(Permission.SYSTEM_ALERT_WINDOW)
//                // 申请通知栏权限
//                .permission(Permission.NOTIFICATION_SERVICE)
//                // 申请系统设置权限
//                .permission(Permission.WRITE_SETTINGS)
//                // 申请单个权限
//                .permission(Permission.RECORD_AUDIO)
//                // 申请多个权限
//                .permission(Permission.Group.CALENDAR)
//                .request(new OnPermissionCallback() {
//                    @Override
//                    public void onGranted(List<String> permissions, boolean all) {
//                        if (all) {
//                            ToastUtil.showToast("获取录音和日历权限成功");
//                        } else {
//                            ToastUtil.showToast("获取部分权限成功，但部分权限未正常授予");
//                        }
//                    }
//                    @Override
//                    public void onDenied(List<String> permissions, boolean never) {
//                        if (never) {
//                            ToastUtil.showToast("被永久拒绝授权，请手动授予录音和日历权限");
//                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                            XXPermissions.startPermissionActivity(BaseActivity.this, permissions);
//                        } else {
//                            ToastUtil.showToast("获取录音和日历权限失败");
//                        }
//                    }
//                });
//        // 判断一个或多个权限是否全部授予了
//        XXPermissions.isGranted(Context context, String... permissions);
//
//// 获取没有授予的权限
//        XXPermissions.getDenied(Context context, String... permissions);
//
//// 判断某个权限是否为特殊权限
//        XXPermissions.isSpecial(String permission);
//
//// 判断一个或多个权限是否被永久拒绝了
//        XXPermissions.isPermanentDenied(Activity activity, String... permissions);
//
//// 跳转到应用权限设置页
//        XXPermissions.startPermissionActivity(Context context, String... permissions);
//        XXPermissions.startPermissionActivity(Activity activity, String... permissions);
//        XXPermissions.startPermissionActivity(Fragment fragment, String... permissions);
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
            //StatusBarUtil.setTransparent(this);
        }else {
            StatusBarUtils.setStatusTranslucent(this);
            //StatusBarUtil.setTranslucent(this);
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
     * 初始化事件，setOnClickListener等等
     */
    protected abstract void initEvent();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 业务操作
     *
     */
    protected abstract void doBusiness();

    /**
     * Activity销毁时清理资源
     */
    @Override
    protected void onDestroy() {
        Log.d(TAG, "-->onDestroy");
        super.onDestroy();
        //解绑ButterKnife
        mUnbinder.unbind();
        //取消网络请求，避免内存泄露
        OkGo.getInstance().cancelAll();
    }

    /**
     * Toast提示信息
     * @param s
     */
    public static void showToast(String s) {
        ToastUtil.showToast(s);
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
     * 隐藏键盘
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 软键盘的自动弹出
     *
     * @param editText
     */
    public void showKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    /**
     * 软键盘的关闭
     */
    public void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Activity动画跳转过渡
     * @param intent
     */
    public void startActivityAnimActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.activity_common_anim_out, R.anim.activity_common_anim_in);
    }

    /**
     * 深入浅出：启动动画
     *
     * @param intent
     */
    public static void startActivityAnimInAndOut(Activity activity,Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.launch_anim_fade_in, R.anim.launch_anim_fade_out);
    }
    /**
     * 子类Activity调用
     * 左————>右：启动动画
     *
     * @param intent
     */
    public void startActivityAnimLeftToRight(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    /**
     * 子类Fragment调用
     * 左————>右：启动动画
     * @param activity
     * @param intent
     */
    public static void startActivityAnimLeftToRight(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }
    /**
     * 传值
     * 左————>右：启动动画
     * @param intent
     * @param code
     */
    public void startActivityForResultAnimLeftToRight(Intent intent, int code) {
        startActivityForResult(intent, code);
        overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
    }

    /**
     * 右————>左：启动动画
     * 不传值
     * @param intent
     */
    public void startActivityAnimRightToLeft(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }
    /**
     * 右————>左：启动动画
     * 传值
     * @param intent
     */
    public void startActivityForResultAnimRightToLeft(Intent intent, int code) {
        startActivity(intent);
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
    }
    /**
     *
     * 物理键返回键执行上一页
     * 右————>左：，启动动画
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
        //finish();
        //overridePendingTransition(R.anim.anim_left, R.anim.anim_right);
    }

    /**
     * 初始化换肤框架
     * @return
     */
    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }

    /**
     * 防止快速点击
     * @return
     */
    public boolean fastClick() {
        long lastClick = 0;
        if (System.currentTimeMillis() - lastClick <= 1000) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    /**
     * 设置Snackbar上提示的字体颜色
     * @param snackbar
     * @param color
     */
    public void setSnackBarMessageTextColor(Snackbar snackbar, int color){
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }


    //自定义的弹窗（提示框）
    public void notification() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示框")//这里设置标题
                .setMessage("提示框可以自定义布局样式，只有一个按钮")//这里设置提示信息
                .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                .setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    //自定义的弹窗（两个按钮的选择框）
    public void notification2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("两个按钮的选择框")
                .setMessage("选择可以自定义布局样式，有两个按钮")
                .setTopImage(R.drawable.icon_tanchuang_wenhao)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    //自定义的弹窗（一个按钮没有顶部图标）
    public void notification3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("没有提示信息，没有顶部图标")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.dismiss();
                    }
                });
        mDialog = builder.create();
        mDialog.show();
    }

    //自定义的弹窗（鲜艳版）
    public void notification4() {
        CustomDialog.Builder builder = new CustomDialog.Builder(BaseActivity.this);
        builder.setMessage("这个就是自定义的提示框");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //设置你的操作事项
                Toast.makeText(BaseActivity.this,"queding",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(BaseActivity.this,"queding",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
