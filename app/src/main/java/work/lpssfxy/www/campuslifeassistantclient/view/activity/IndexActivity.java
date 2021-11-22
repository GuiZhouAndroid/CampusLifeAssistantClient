package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.xuexiang.xui.widget.toast.XToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.App;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.custominterface.ActivityInteraction;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.RoleOrPermissionListBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.SessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.CustomAlertDialogUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomCategoryFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomHomeFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomMineFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomShopFragment;

@SuppressLint("NonConstantResourceId")
public class IndexActivity extends BaseActivity {
    //@BindArray(R2.array.city)
    //String[] strArray;//绑定资源文件中string字符串数组
    @BindBitmap(R.mipmap.index_not_login) Bitmap mIndex_not_login;//绑定资源文件中mipmap中的ic_launcher图片
    //@BindColor(R2.color.purple_200)
    private static final String TAG = "IndexActivity";
    /** 定制Toolbar */
    @BindView(R2.id.appbar_constant_toolbar) Toolbar mAppbar_constant_toolbar;
    /** Toolbar折叠布局 */
    @BindView(R2.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsing_toolbar_layout;
    /** ToolbarQQ头像图标 */
    @BindView(R2.id.index_iv_user_head) ImageView mIndex_iv_user_head;
    /** Toolbar名字标签 */
    @BindView(R2.id.index_tv_user_hello) TextView mIndex_tv_user_hello;
    /** 侧滑主体 */
    @BindView(R2.id.drawer_layout) DrawerLayout mDrawer_layout;
    /** 抽屉抽屉 */
    @BindView(R2.id.nav_view) NavigationView mNav_view;
    /** 悬浮按钮 */
    @BindView(R2.id.floating_action_btn) FloatingActionButton mFloating_action_btn;
    /** 绑定ViewPager */
    @BindView(R2.id.vp_content) ViewPager mVp_content;
    /** 防触碰使用的变量 */
    private long firstTime;
    /** 底部导航 */
    @BindView(R2.id.bottomBar) BottomBar mBottomBar;
    /** 四个主功能Fragment界面 */
    public Fragment[] fragments =null;
    /** 声明Fragment集合，ViewPager适配器遍历绑定数组fragments*/
    private List<Fragment> fragmentList;
    /** Toolbar弹窗 */
    private CustomPopWindow mCustomPopWindow;
    /** 侧滑顶部头像 */
    private ImageView ivNavHeaderIcon;
    /** 侧滑顶部昵称 + 上次QQ授权时间 + 上次账户登录时间 */
    private TextView tvNavHeaderName,tv_QQLastLoginTime,tv_userLastLoginTime;
    /** 侧滑背景图片 */
    private LinearLayout ll_drawer_bg;
    /** 自定义对话框 */
    private AlertDialog mDialog;
    //用于数据传递到底部导航第四个页面--->用户管理信息
    private Handler mHandler;
    //接口传递用户信息数据
    private ActivityInteraction mActivityInteraction;

    /**
     * 关闭滑动返回
     *
     * @return false:右滑返回失效
     */
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return false;
    }

    /**
     * 开启沉浸状态栏
     *
     * @return true:顶部状态栏全透明 false:顶部状态栏半透明
     */
    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    /**
     * 关闭自动隐藏底部导航栏
     * 须知：true时，必须关闭沉浸状态栏，false:必须开启沉浸式状态栏
     *
     * @return true:隐藏顶部状态栏+挤压底部导航栏 false:log打印日志“返回值不正确”
     */
    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    /**
     * 开启设置底部导航栏白色
     *
     * @return true:底部导航栏白色 false:底部导航栏黑色半透明
     */
    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return true;
    }

    /**
     * 关闭全屏沉浸
     *
     * @return true:顶部状态栏隐藏+底部导航栏隐藏  false:log打印日志“返回值不正确”
     */
    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    /**
     * 绑定布局
     *
     * @return
     */
    @Override
    public int bindLayout() {
        return R.layout.index_activity;
    }


    /**
     * 准备数据
     */
    @Override
    protected void prepareData() {
        /** 初始化DrawerLayout侧滑抽屉 */
        initDrawerLayout();
        initViewPager();
        /** 初始化底部导航 */
        initBottomNaviCation();
        /** 初始化获取权限 */
        initPermission();
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        /** IndexActivity赋值单例静态全局变量，此处用于LoginActivity指定销毁当前IndexActivity*/
        App.appActivity = this;
        /**判断Toolbar，开启主图标并显示title*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            //actionBar.setTitle("主标题");
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        /** 设置Toolbar */
        setSupportActionBar(mAppbar_constant_toolbar);
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
        /** 获取本地QQSession+用户个人信息持久化Java对象数据 */
        initQQSessionAndUserInfo();
    }

    /**
     * 设置事件
     */
    @Override
    protected void initEvent() {
        /** 设置侧滑顶部头像单击事件 */
        ivNavHeaderIcon.setOnClickListener(this);
        /** 设置侧滑顶部昵称单击事件 */
        tvNavHeaderName.setOnClickListener(this);
    }

    /**
     * 开始监听
     */
    @Override
    protected void initListener() {
    }

    /**
     * 业务操作
     */
    @Override
    protected void doBusiness() {
    }

    /**
     * 单击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_iv_nav_header_icon://侧滑顶部头像
                /** 判断检查QQ登录是否有效 */
                checkQQLoginIfValid();
                break;
            case R.id.index_tv_nav_header_name://侧滑顶部昵称
                break;
        }
    }

    /**
     * 初始化DrawerLayout侧滑抽屉
     */
    private void initDrawerLayout() {
        /** 推动DrawerLayout主布局+隐藏布局 */
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer_layout, mAppbar_constant_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取mDrawerLayout中的第一个子布局，也就是布局中的RelativeLayout
                //获取抽屉的view
                View mContent = mDrawer_layout.getChildAt(0);
                float scale = 1 - slideOffset;
                float endScale = 0.8f + scale * 0.2f;
                float startScale = 1 - 0.3f * scale;
                //设置左边菜单滑动后的占据屏幕大小
                drawerView.setScaleX(startScale);
                drawerView.setScaleY(startScale);
                //设置菜单透明度
                drawerView.setAlpha(0.6f + 0.4f * (1 - scale));
                //设置内容界面水平和垂直方向偏转量
                //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
                mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate();
                //设置右边菜单滑动后的占据屏幕大小
                mContent.setScaleX(endScale);
                mContent.setScaleY(endScale);
            }
        };
        toggle.syncState();
        mDrawer_layout.addDrawerListener(toggle);
        /** 给DrawerLayout侧滑的Navigation导航菜单添加点击事件 */
        mNav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                mDrawer_layout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.drawer_menu_school:
                        Snackbar.make(mDrawer_layout, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                        IntentUtil.startActivityAnimLeftToRight(IndexActivity.this,new Intent(IndexActivity.this, waimai.class));
                        return true;
                    case R.id.drawer_menu_setting:
                        Snackbar.make(mDrawer_layout, "点宝宝11干啥", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_menu_developer_system_safe: //开发者后台安全中心

                        OkGo.<String>post(Constant.SA_TOKEN_CHECK_LOGIN)
                                .tag("检查登录")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onStart(Request<String, ? extends Request> request) {
                                        XPopupUtils.getInstance().setShowDialog(IndexActivity.this, "正在验证身份...");
                                    }

                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                        if (401 == responseBean.getCode() && "未提供Token".equals(responseBean.getData()) && "验证失败，禁止访问".equals(responseBean.getMsg())) {
                                            CustomAlertDialogUtil.notification1(IndexActivity.this,"温馨提示","您还没有登录呀~","朕知道了");
                                            return;
                                        }

                                        if (401 == responseBean.getCode() && "验证失败，禁止访问".equals(responseBean.getMsg()) && "已被系统强制下线".equals(responseBean.getData())) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(IndexActivity.this);
                                            builder.setTitle("超管提示")//这里设置标题
                                                    .setMessage("已被系统超管强制下线！")//这里设置提示信息
                                                    .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                                                    .setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            mDialog.dismiss();
                                                            Snackbar snackbar = Snackbar.make(mDrawer_layout, "超过3次提醒，将被永久封号！", Snackbar.LENGTH_INDEFINITE)
                                                                    .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                                                                    .setAction("我知道了", new View.OnClickListener() {  //设置点击按钮
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            /** 开启一个子线程注销QQ登录 + 清空UI数据 + 本地持久化文件 */
                                                                            new Thread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    //发送方式一 直接发送一个空的Message
                                                                                    QQHandler.sendEmptyMessage(1);
                                                                                }
                                                                            }).start();
                                                                            Toast.makeText(IndexActivity.this, "别撒谎喔~", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                            //设置Snackbar上提示的字体颜色
                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                            snackbar.show();
                                                        }
                                                    });
                                            mDialog = builder.create();
                                            mDialog.show();
                                            return;
                                        }
                                        if (200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "当前账户已登录".equals(responseBean.getMsg())) {
                                            OkGo.<String>post(Constant.SELECT_NOW_USERNAME_ROLE_LIST)
                                                    .tag("当前登录会话角色集合")
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onSuccess(Response<String> response) {
                                                            RoleOrPermissionListBean roleOrPermissionListBean = GsonUtil.gsonToBean(response.body(), RoleOrPermissionListBean.class);
                                                            Log.i(TAG, "RoleOrPermissionListBean: " + roleOrPermissionListBean.getData());
                                                            if (roleOrPermissionListBean.getData().contains("超管")) {
                                                                //List<String> 集合中，包含角色"超级管理员"即当前登录账户为开发者认证账户。然后执行认证通过，跳转后台安全页面
                                                                IntentUtil.startActivityAnimLeftToRight(IndexActivity.this,new Intent(IndexActivity.this, DeveloperSystemSafeActivity.class));
                                                            } else {//无认证权限，提示信息
                                                                CustomAlertDialogUtil.notification1(IndexActivity.this,"超管提示","《后台安全》仅开发者使用，您无权访问！谢谢合作~","朕知道了");
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Response<String> response) {
                                                            OkGoErrorUtil.CustomFragmentOkGoError(response,IndexActivity.this, mDrawer_layout, "请求错误，服务器连接失败！");
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        XPopupUtils.getInstance().setSmartDisDialog();
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        OkGoErrorUtil.CustomFragmentOkGoError(response,IndexActivity.this, mDrawer_layout, "请求错误，服务器连接失败！");
                                    }
                                });
                        return true;
                    case R.id.drawer_menu_about_author:
                        Snackbar.make(mDrawer_layout, "点宝宝1干啥", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_menu_logout://注销账户：本地持久数据 + 服务器Session数据
                        /** 1.开启一个子线程注销QQ登录 + 清空UI数据 + 本地持久化文件 */
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //发送方式一 直接发送一个空的Message
                                QQHandler.sendEmptyMessage(1);
                            }
                        }).start();
                        /** 2.同时注销后端服务器中的账户Session信息*/
                        OkGo.<String>post(Constant.SA_TOKEN_DO_LOGOUT)
                                .tag("注销登录")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onStart(Request<String, ? extends Request> request) {
                                        XPopupUtils.getInstance().setShowDialog(IndexActivity.this, "正在注销...");
                                    }

                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                        if (200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "当前登录账户注销成功".equals(responseBean.getMsg())) {
                                            Snackbar snackbar = Snackbar.make(mDrawer_layout, responseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                            snackbar.show();
                                            return;
                                        }
                                        if (200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "注销失败，未登录".equals(responseBean.getMsg())) {
                                            Snackbar snackbar = Snackbar.make(mDrawer_layout, "您还未登录呢~请先登录！", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                            snackbar.show();
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        XPopupUtils.getInstance().setSmartDisDialog();
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        OkGoErrorUtil.CustomFragmentOkGoError(response,IndexActivity.this, mDrawer_layout, "请求错误，服务器连接失败！");
                                    }
                                });
                        return true;
                }
                return false;
            }
        });

        /** 获取侧滑头部用户名或QQ昵称、QQ头像、个性签名控件，并分别设置监听单击事件*/
        //侧滑顶部头像
        ivNavHeaderIcon = mNav_view.getHeaderView(0).findViewById(R.id.index_iv_nav_header_icon);
        //侧滑顶部昵称
        tvNavHeaderName = mNav_view.getHeaderView(0).findViewById(R.id.index_tv_nav_header_name);
        //侧滑QQ授权
        tv_QQLastLoginTime = mNav_view.getHeaderView(0).findViewById(R.id.tv_QQLastLoginTime);
        //侧滑账户验证
        tv_userLastLoginTime = mNav_view.getHeaderView(0).findViewById(R.id.tv_userLastLoginTime);
        //侧滑背景图片
        ll_drawer_bg = mNav_view.getHeaderView(0).findViewById(R.id.ll_drawer_bg);
    }

    /**
     * 接受QQ登录广播，数据来源于广播，不是本地持久化数据文件，因此需要严格区分
     */

//    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @SuppressLint("CheckResult")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            /** 根据Gson转化后的Java对象 Intent序列化的键获取广播消息内容*/
//            UserQQSessionBean.Data.UserInfo userInfo = (UserQQSessionBean.Data.UserInfo) intent.getSerializableExtra("userInfo");
//            Toast.makeText(IndexActivity.this,userInfo.toString(),Toast.LENGTH_SHORT).show();
//        }
//    };
    /**
     * 初始化QQSession并获取QQ个人信息
     * 有效：取出的持久化文件的Java对象参数，重组JSON的Session数据，调用QQ的initSessionCache(JSON)来使得会话有效+个人用户信息
     * 无效：使用默认头像+无登录相关信息展示
     * 本地持久化文件与LoginActivity为同一xml文件---> UserQQSessionBean(并集信息的Java对象数据)
     */
    private void initQQSessionAndUserInfo() {
        /** 获取本地QQSession持久化Java对象数据*/
        Constant.userQQSession = SharePreferenceUtil.getObject(IndexActivity.this, UserQQSessionBean.class);
        Log.i(TAG, "首页UserQQSessionBean: " + Constant.userQQSession);
        /** 创建JSONObject实例，重组Json数据顺序，提供给initSessionCache(jsonObject)，实现QQSession有效*/
        JSONObject jsonObject = new JSONObject();
        if (Constant.userQQSession != null) {//本地持久化xml文件有数据时才满足重组条件 + 已登录有持久化数据
            XPopupUtils.getInstance().setShowDialog(this,getString(R.string.indexLoadLoginInfo));
            try {
                jsonObject.put("ret", Constant.userQQSession.getData().getRet());
                jsonObject.put("openid", Constant.userQQSession.getData().getOpenid());
                jsonObject.put("access_token", Constant.userQQSession.getData().getAccessToken());
                jsonObject.put("pay_token",Constant.userQQSession.getData().getPayToken());
                jsonObject.put("expires_in", Constant.userQQSession.getData().getExpiresIn());
                jsonObject.put("pf", Constant.userQQSession.getData().getPf());
                jsonObject.put("pfkey", Constant.userQQSession.getData().getPfkey());
                jsonObject.put("msg", Constant.userQQSession.getData().getMsg());
                jsonObject.put("login_cost",Constant.userQQSession.getData().getLoginCost());
                jsonObject.put("query_authority_cost", Constant.userQQSession.getData().getQueryAuthorityCost());
                jsonObject.put("authority_cost", Constant.userQQSession.getData().getAuthorityCost());
                jsonObject.put("expires_time", Constant.userQQSession.getData().getExpiresTime());
                /** 初始化设置上次授权登录的Session信息——来自持久化重组JSon数据顺序*/
                Constant.mTencent.initSessionCache(jsonObject);
                Log.i(TAG, "首页Tencent初始化后会话Session是否有效: " + Constant.mTencent.isSessionValid());//true
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** 本地文件有用户数据且Session不为空有效时，提取持久化用户信息，设置QQ头像+QQ用户名+个人用户信息 */
            if (Constant.mTencent != null && Constant.mTencent.isSessionValid()) {
                //1.发送用户全部信息到handler进行UI更新，包括上次登录时间
                Message msg = new Message();
                msg.obj = Constant.userQQSession.getData().getUserInfo();
                msg.what = 2;
                QQHandler.sendMessage(msg);

                //2.发送QQ上次授权时间到handler进行UI更新
                Message msg1 = new Message();
                msg1.obj = Constant.userQQSession.getData();
                msg1.what = 3;
                QQHandler.sendMessage(msg1);
                //3.初始化已登录持久化数据
                initValidLoginUserData();
            }
        } else {//未登录无持久化数据
            Log.i(TAG, "首页Tencent初始化后会话Session是否有效: " + Constant.mTencent.isSessionValid());//false
            Snackbar snackbar = Snackbar.make(mDrawer_layout, "温馨提示：您没有登录哟~", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                    .setAction("去登录", new View.OnClickListener() {  //设置点击按钮
                        @Override
                        public void onClick(View v) {
                            IntentUtil.startActivityAnimLeftToRight(IndexActivity.this,new Intent(IndexActivity.this, LoginActivity.class));
                        }
                    });
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            //发送方式一 直接发送一个空的Message，
            QQHandler.sendEmptyMessage(1);
        }
    }

    /**
     * 初始化ViewPage+适配器填充4个Fragment+左右滑动ViewPager根据当前的item与底部导航BottomBar的item实现动画联动切换
     */
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        //创建Fragment类型的数组，适配ViewPager，添加四个功能页
        fragments = new Fragment[]{new BottomHomeFragment(), new BottomCategoryFragment(), new BottomShopFragment(), new BottomMineFragment()};
        //ViewPager设置MyAdapter适配器，遍历List<Fragment>集合，填充Fragment页面
        mVp_content.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragments, fragmentList));
        mVp_content.setOffscreenPageLimit(fragmentList.size());//viewPager单次预加载Fragment页数
        //ViewPager滑动监听
        mVp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //选择新页面时调用
            @Override
            public void onPageSelected(int position) {
                mBottomBar.selectTabAtPosition(position, true);
            }

            //当滚动状态改变时调用，用于发现用户何时开始拖动
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //必须再这个地方写，要ViewPager适配填充Fragment页面后，才有效，执行顺序原因
        mVp_content.setCurrentItem(0);//默认第1页
    }

    /**
     * 初始化底部导加载+根据当前item值，在点击对应图标时，与ViewPager联动切换
     * 设置底部导航切换点击事件和再次点击事件
     */
    private void initBottomNaviCation() {
        //在tab3上，添加红色角标数字5
        mBottomBar.getTabWithId(R.id.tab3).setBadgeCount(5);
        //选定的BottomBarTab更改时被触发——当前处于Item时点击其它Item
        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab1:
                        mVp_content.setCurrentItem(0);
                        Toast.makeText(IndexActivity.this, "tab1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab2:
                        mVp_content.setCurrentItem(1);
                        Toast.makeText(IndexActivity.this, "tab2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab3:
                        mVp_content.setCurrentItem(2);
                        Toast.makeText(IndexActivity.this, "tab3", Toast.LENGTH_SHORT).show();
                        mBottomBar.getTabWithId(R.id.tab3).removeBadge();
                        break;
                    case R.id.tab4:
                        mVp_content.setCurrentItem(3);
                        Toast.makeText(IndexActivity.this, "tab4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        // 当前选择的BottomBarTab被单击时触发——当前处于Item时再次点击当前Item
        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab1:
                        //           viewPager.setCurrentItem(0);
                        Toast.makeText(IndexActivity.this, "再次点击了tab1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab2:
                        //           viewPager.setCurrentItem(1);
                        Toast.makeText(IndexActivity.this, "再次点击了tab2", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.tab3:
                        //       viewPager.setCurrentItem(2);
                        Toast.makeText(IndexActivity.this, "再次点击了tab3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.tab4:
                        //        viewPager.setCurrentItem(3);
                        Toast.makeText(IndexActivity.this, "再次点击了tab4", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * 权限申请
     * 百度定位检查权限/获取文件位置+写入外部存储器+读取手机状态
     */
    private void initPermission() {
        List<String> permissionList = new ArrayList<>();  //创建空List集合
        //运行时权限申请
        if (ContextCompat.checkSelfPermission(IndexActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(IndexActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(IndexActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(IndexActivity.this, permissions, 1);
        }
    }

    /**
     * 打开APP，进入首页，获取QQSession持久化数据调用，实现DefaultUiListener接口
     * 回调new UserInfo(this,Constant.mTencent.getQQToken()) 获取用户信息
     * 持久化数据文件无数据时，调用未登录默认设置的方法
     */
    @SuppressLint("CheckResult")
    private void initValidLoginUserData() {

        IUiListener listener = new DefaultUiListener() {
            /** 以下进行实时联网获取授权QQ用户信息 + 以及发送Handler 更新UI */
            @Override
            public void onComplete(final Object response) {
                Log.d(TAG, "请求回调用户信息列表= " + response.toString());
                /** 调用Gson工具类，回掉的JSON数据，转化为Java对象*/
                Constant.qqUser = GsonUtil.gsonToBean(response.toString(), QQUserBean.class);
                Log.i(TAG, "qqUser全部数据: " + Constant.qqUser);
                Message msg = new Message();
                msg.obj = Constant.qqUser;
                msg.what = 0;
                QQHandler.sendMessage(msg);
            }

            @Override
            public void onCancel() {
                Toast.makeText(IndexActivity.this, "取消获取授权用户信息", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError e) {
                Toast.makeText(IndexActivity.this, "获取授权用户信息出错：" + e.errorDetail, Toast.LENGTH_SHORT).show();
            }
        };
        /** 根据Constant.mTencent会话中TOKEN值，请求回调授权用户信息列表*/
        UserInfo info = new UserInfo(this, Constant.mTencent.getQQToken());
        Log.i(TAG, "首页QQToken====: " + Constant.mTencent.getQQToken().toString());
        /** 开始监听请求回调操作*/
        info.getUserInfo(listener);

        /** 调用SharePreference工具类获取Gson转化后的Java对象，持久化文件名“ZSAndroid”的本地数据 */

        /** 标题栏字体加粗 */
        Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
    }

    /**
     * 登录回调成功后保存Session信息本地XML数据文件中
     *
     * @param jsonObject
     */
    private void initSaveSessionDataToLocalFile(JSONObject jsonObject) {
        Log.i(TAG, "回调成功Gson解析Json前会话Session数据: " + jsonObject.toString());
        //Gson解析并序列号至Java对象中
        SessionBean sessionBean = GsonUtil.gsonToBean(jsonObject.toString(), SessionBean.class);
        Log.i(TAG, "回调成功Gson解析Json后会话Session数据(持久化存入此解析数据到xml): " + sessionBean);
        Log.i(TAG, "Access_token: " + sessionBean.getAccess_token());
        /** Gson解析后Java对象持久化数据保存本地，IndexActivity首页调用JSONObject的put()方法重组顺序，提供给Constant.mTencent.initSessionCache(JSONObject实例) */
        SharePreferenceUtil.putObject(IndexActivity.this, sessionBean);
    }

    /**
     * 线程刷新UI数据
     */
    @SuppressLint("HandlerLeak")
    public Handler QQHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    /** 回调QQ授权用户的个人资料信息有数据时，通过UI展示信息，这里Constant.qqUser 不可能为null，不需要设置默认显示setNotLoginQQUserInfo(); */
                    if (Constant.qqUser != null) {
                        Constant.qqUser = (QQUserBean) msg.obj;
                        Log.i(TAG, "实时回调的QQ用户信息=== " + Constant.qqUser);
                        QQUserBeanIsValidSetTextAndGlideUserHead(Constant.qqUser);//加载在线动态QQ昵称和QQ头像
                        startHaveDataQQUserInfoToMineFragment(Constant.qqUser);
                        /** 关闭正在加载登录信息友好进度条*/
                        XPopupUtils.getInstance().setSmartDisDialog();
                    }
                    break;
                case 1: //未登录
                    /** 状态设置默认的头像和文本 */
                    setNotLoginQQUserInfo();
                    /** 清空本机持久化--->QQ会话失效 */
                    setQQUserLoginLogout();
                    /** 无数据时点击开始传递数据到Fragment《我的》 */
                    //方式一：Activity与Fragment接口回调--->生命周期问题，只能生效一次
                    startNotHaveDataAllUserInfoToMineFragment();
                    //方式一：Activity与Fragment接口回调--->生命周期问题，只能生效一次
                    if (mActivityInteraction != null){
                        mActivityInteraction.userAllInfoPutMineFragment(null);
                    }
                    break;
                case 2: //用户登录成功信息，开始传递数据到Fragment《我的》
                    Constant.userInfo = (UserQQSessionBean.Data.UserInfo) msg.obj;
                    //方式一：Activity与Fragment共享Handler--->生命周期问题，只能生效一次
                    startHaveDataUserInfoToMineFragment(Constant.userInfo);
                    Log.i(TAG, "用户登录成功信息: "+Constant.userInfo);
                    //方式一：Activity与Fragment接口回调--->生命周期问题，只能生效一次
                    if (mActivityInteraction != null){
                        mActivityInteraction.userAllInfoPutMineFragment(Constant.userInfo);
                    }
                    //UI更新用户上次登录时间
                    tv_userLastLoginTime.setText(Constant.userInfo.getLastLoginTime());
                    /** 设置Toolbar的真实名字 */
                    mCollapsing_toolbar_layout.setTitle(Constant.userInfo.getUlRealname());
                    break;
                case 3:
                    //UI更新QQ上次授权时间
                    Constant.userQQSessionData = (UserQQSessionBean.Data) msg.obj;
                    if (Constant.userQQSessionData != null) {
                        tv_QQLastLoginTime.setText(Constant.userQQSessionData.getUpdateTime());
                    }
                    break;
            }
        }
    };

    /**
     * MainActivity 与 底部导航BottomMineFragment 共享Handler实例
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        // 3.在HandlerFragment中通过调用此方法，即可实现共享Handler消息对象
        mHandler = handler;
    }

    /**
     * 写一个对外公开的方法，传递数据
     * @param interaction
     */
    public void setStartPushUserInfoListener(ActivityInteraction interaction){
        this.mActivityInteraction = interaction;
    }

    /**
     * 用户数据开始传递数据到Fragment《我的》
     *
     * @param userInfo
     */
    public void startHaveDataUserInfoToMineFragment(UserQQSessionBean.Data.UserInfo userInfo) {
        Log.i(TAG, "Hadnler用户信息: "+ userInfo);
        //4.创建发送消息实例，在HandlerFragment中接收此消息，即可得到传输的数据信息
        Message msg = new Message();
        //5.携带数据为输入框文本数据
        msg.obj = userInfo;
        //5.消息标记为1
        msg.what = 1;
        Log.i(TAG, "mHandler:== "+ mHandler);
        //5.开始发送消息
        if (mHandler!=null){
            mHandler.sendMessage(msg);
        }
    }

    /**
     * QQ数据开始传递数据到Fragment《我的》
     *
     * @param qqUserInfo
     */
    public void startHaveDataQQUserInfoToMineFragment(QQUserBean qqUserInfo) {
        //4.创建发送消息实例，在HandlerFragment中接收此消息，即可得到传输的数据信息
        Message msg = new Message();
        //5.携带数据为输入框文本数据
        msg.obj = qqUserInfo;
        //5.消息标记为1
        msg.what = 2;
        //5.开始发送消息
        if (mHandler!=null){
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 无数据时点击开始传递数据到Fragment《我的》
     *
     */
    public void startNotHaveDataAllUserInfoToMineFragment() {
        if (mHandler != null) {
            mHandler.sendEmptyMessage(3);
        }
    }

    /**
     * 有数据获取时：加载在线动态QQ昵称和QQ头像（QQ相关信息）
     * @param qqUser
     */
    private void QQUserBeanIsValidSetTextAndGlideUserHead(QQUserBean qqUser) {
        /** 标题栏字体加粗 */
        Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
        /** 创建Glide圆形图像实例*/
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        /** 设置昵称动态开始——动态结束的字体样式 */
        mCollapsing_toolbar_layout.setExpandedTitleTypeface(font);
        mCollapsing_toolbar_layout.setCollapsedTitleTypeface(font);
        /** 设置侧滑的QQ昵称 */
        tvNavHeaderName.setText(qqUser.getNickname());
        /** 设置友好时间状态可见*/
        mIndex_tv_user_hello.setVisibility(View.VISIBLE);
        /** 设置友好时间提示*/
        getDate(mIndex_tv_user_hello);
        /** 设置圆形QQ头像 */
        Glide.with(IndexActivity.this)
                .load(qqUser.getFigureurl_qq_2())
                .apply(options)
                .into(mIndex_iv_user_head);
        /** 设置侧滑的圆形QQ头像 */
        Glide.with(IndexActivity.this)
                .load(qqUser.getFigureurl_qq_2())
                .apply(options)
                .into(ivNavHeaderIcon);
        /** 温馨提示 */
        Snackbar snackbar = Snackbar.make(mDrawer_layout, "已为您自动登录~", Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorAccent));
        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    /**
     * 未登录状态设置默认的头像和文本
     */
    @SuppressLint("CheckResult")
    private void setNotLoginQQUserInfo() {
        /** 标题栏字体加粗 */
        Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
        /** 创建Glide圆形图像实例*/
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        /** 设置动态开始——结束的字体 */
        mCollapsing_toolbar_layout.setExpandedTitleTypeface(font);
        mCollapsing_toolbar_layout.setCollapsedTitleTypeface(font);
        /** 设置Toolbar开始——结束文本标签 */
        mCollapsing_toolbar_layout.setTitle("点击头像登录");
        /** 设置侧滑默认文本标签 */
        tvNavHeaderName.setText("点击头像登录");
        /** 友好时间提示状态失效*/
        mIndex_tv_user_hello.setVisibility(View.GONE);
        /** 设置默认的圆形头像 */
        Glide.with(IndexActivity.this)
                .load(mIndex_not_login)
                .apply(options)
                .into(mIndex_iv_user_head);

        /** 设置侧滑菜单默认的圆形头像 */
        Glide.with(IndexActivity.this)
                .load(mIndex_not_login)
                .into(ivNavHeaderIcon);

        tv_userLastLoginTime.setText("请先登录");
        tv_QQLastLoginTime.setText("请先登录");
    }

    /**
     * 仿真退出登录
     * 1.删除持久化数据文件
     * 2.Java对象设置为null
     * 3.Tencent实例的Session设置注销，使其失效
     */
    private void setQQUserLoginLogout() {
        //清空持久化用户并集xml文件，使得QQ会话无法有效初始化，用户信息无法读取
        Constant.userQQSession = null;//清空当前对象数据
        SharePreferenceUtil.removeObject(IndexActivity.this, UserQQSessionBean.class);//清空持久化xml文件
        Log.i(TAG, "注销登录的UserQQSessionBean: " + Constant.userQQSession);
        //注销mTencent
        Constant.mTencent.logout(IndexActivity.this);
        Log.i(TAG, "注销QQ登录的会话Tencent: " + Constant.mTencent.isSessionValid());
    }

    /**
     * 判断当前时间处于那个时间
     *
     * @param tv
     */
    private void getDate(TextView tv) {
        Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
        tv.setTypeface(font);
        Date d = new Date();
        if (d.getHours() < 4) {
            tv.setText("早点休息哟~,");
        } else if (d.getHours() < 11) {
            tv.setText("早上好,");
        } else if (d.getHours() < 14) {
            tv.setText("中午好,");
        } else if (d.getHours() < 18) {
            tv.setText("下午好,");
        } else if (d.getHours() < 24) {
            tv.setText("晚上好,");
        }
    }

    /**
     * 判断检查QQ登录是否有效
     */
    private void checkQQLoginIfValid() {
        if (Constant.userQQSession == null) {
            IntentUtil.startActivityAnimLeftToRight(IndexActivity.this,new Intent(IndexActivity.this, LoginActivity.class));//执行动画跳转
            return;
        }
        if (Constant.mTencent != null && Constant.mTencent.isSessionValid() && Constant.userQQSession != null) {
            XPopupUtils.getInstance().setShowDialog(this,"请求跳转中...");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    XPopupUtils.getInstance().setSmartDisDialog();
                    IntentUtil.startActivityAnimLeftToRight(IndexActivity.this,new Intent(IndexActivity.this,MineInfoActivity.class));
                }
            }, 500);
            Snackbar snackbar = Snackbar.make(mDrawer_layout, R.string.index_already_login_qq, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
    }

    /**
     * Toolbar头像单击事件+浮动按钮单击事件
     *
     * @param view
     */
    @OnClick({R2.id.index_iv_user_head, R2.id.floating_action_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.index_iv_user_head://Toolbar头像
                /** 判断检查QQ登录是否有效 */
                checkQQLoginIfValid();
                break;
            case R.id.floating_action_btn://貌似没有效果，大概被置顶功能取代
                Snackbar snackbar = Snackbar.make(mDrawer_layout, "点宝宝干啥", Snackbar.LENGTH_SHORT)
                        .setActionTextColor(getResources().getColor(R.color.colorAccent));
                //设置Snackbar上提示的字体颜色
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                break;
        }
    }

    /**
     * 监听Toolbar菜单项item
     * 添加点击事件,用于打开或关闭侧滑抽屉
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                OpenToolbarPopup();//打开标题栏弹窗
                break;
//            case R.id.qr:
//                Toast.makeText(this, "扫一扫待实现~", Toast.LENGTH_SHORT).show();
//                break;
        }
        return true;
    }

    /**
     * 打开标题栏弹窗
     */
    private void OpenToolbarPopup() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_pop_menu, null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("TAG", "onDismiss");
                    }
                })
                .create()
                .showAsDropDown(findViewById(R.id.search), 0, 20);
    }

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.item1:
                        OkGo.<String>post(Constant.SA_TOKEN_IS_LOGIN)
                                .tag("判断当前登录状态")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onStart(Request<String, ? extends Request> request) {
                                        XPopupUtils.getInstance().setShowDialog(IndexActivity.this, "正在验证身份...");
                                    }

                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                        if (200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())) {
                                            Toast.makeText(IndexActivity.this, responseBean.getData(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        if (200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())) {
                                            Toast.makeText(IndexActivity.this, responseBean.getData(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        XPopupUtils.getInstance().setSmartDisDialog();
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        OkGoErrorUtil.CustomFragmentOkGoError(response,IndexActivity.this, mDrawer_layout, "请求错误，服务器连接失败！");
                                    }
                                });
                        break;
                    case R.id.item2:
                        OkGo.<String>post(Constant.SA_TOKEN_CHECK_LOGIN)
                                .tag("检查登录")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onStart(Request<String, ? extends Request> request) {
                                        XPopupUtils.getInstance().setShowDialog(IndexActivity.this, "正在检查登录...");
                                    }

                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                        if (200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "当前账户已登录".equals(responseBean.getMsg())) {
                                            Toast.makeText(IndexActivity.this, responseBean.getData(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(IndexActivity.this, responseBean.getData(), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        XPopupUtils.getInstance().setSmartDisDialog();
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        OkGoErrorUtil.CustomFragmentOkGoError(response,IndexActivity.this, mDrawer_layout, "请求错误，服务器连接失败！");
                                    }
                                });
                        break;
                    case R.id.item3:

                        break;
                    case R.id.item4:
                        Toast.makeText(IndexActivity.this, "点击 Item菜单4", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item5:
                        Toast.makeText(IndexActivity.this, "点击 Item菜单5", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        contentView.findViewById(R.id.item1).setOnClickListener(listener);
        contentView.findViewById(R.id.item2).setOnClickListener(listener);
        contentView.findViewById(R.id.item3).setOnClickListener(listener);
        contentView.findViewById(R.id.item4).setOnClickListener(listener);
        contentView.findViewById(R.id.item5).setOnClickListener(listener);
    }

    /**
     * 初始化Toolbar菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    /**
     * 回调数据
     *
     * @param requestCode 请求码，即调用startActivityForResult()传递过去的值
     * @param resultCode  结果码，结果码用于标识返回数据来自指定的Activity
     * @param data        返回数据，存放了返回数据的Intent，使用第三个输入参数可以取出新Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 防触碰处理
     * 再按一次退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 3000) {
                ToastUtils.show("再按一次退出程序！");
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 注销广播+释放线程UI
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (QQHandler != null){
            QQHandler.removeCallbacksAndMessages(null);
        }
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    //多个控件对应公共事件
//    @OnClick({R2.id.btn, R2.id.btn1})
//    public void sayHi(Button btn) {
//        btn.setText("Success!");
//    }

//    @OnLongClick(R2.id.btn)
//    public void onViewOneLongClicked(){
//        Toast.makeText(this, "我是单个Btn长按事件", Toast.LENGTH_SHORT).show();
//    }

//    @OnLongClick({R2.id.btn, R2.id.tv})
//    public void onViewLongClicked(View view) {
//        switch (view.getId()) {
////            case R.id.btn:
////                Toast.makeText(this, "我是多个长按Btn事件", Toast.LENGTH_SHORT).show();
////                break;
////            case R.id.tv:
////                Toast.makeText(this, "我是多个长按tv事件", Toast.LENGTH_SHORT).show();
////                break;
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_demo, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id) {
//            case R.id.action_clear_unread:
//                mBottomBarLayout.setUnread(0, 0);
//                mBottomBarLayout.setUnread(1, 0);
//                break;
//            case R.id.action_clear_notify:
//                mBottomBarLayout.hideNotify(2);
//                break;
//            case R.id.action_clear_msg:
//                mBottomBarLayout.hideMsg(3);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}