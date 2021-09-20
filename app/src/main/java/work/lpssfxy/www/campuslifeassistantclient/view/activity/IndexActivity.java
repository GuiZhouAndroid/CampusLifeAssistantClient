package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
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
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.App;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.bottom.BottomBarItem;
import work.lpssfxy.www.campuslifeassistantclient.base.bottom.BottomBarLayout;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.coding.FileCodeUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.LoadingDialog;
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
    @BindView(R2.id.bbl) BottomBarLayout mBbl;
    /** 四个主功能Fragment界面 */
    public Fragment[] fragments =null;
    /** 创建Fragment集合，ViewPager适配器遍历绑定数组fragments*/
    public List<Fragment> fragmentList = new ArrayList<>();
    /** Toolbar弹窗 */
    private CustomPopWindow mCustomPopWindow;
    /** 侧滑顶部头像 */
    private ImageView ivNavHeaderIcon;
    /** 侧滑顶部昵称 */
    private TextView tvNavHeaderName;


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
        /** 针对登录界面LoginActivity QQ回调数据处理时，没有关闭进度框，解决生效一次的问题。目的：仿真登录加载。 */
        LoadingDialog.closeSimpleLD();
        /** 初始化DrawerLayout侧滑抽屉 */
        initDrawerLayout();
        /** 初始化底部导航 */
        initBottomNaviCation();
        /** 初始化获取权限 */
        initPermission();
        /** 注册广播接收者——更新UI*/
        IntentFilter filter = new IntentFilter(LoginActivity.action);
        registerReceiver(broadcastReceiver, filter);
        /** 测试打印当前首页是否成功获取APP创建的Tencent实例*/
        Log.i(TAG, "首页的TencentQQ: "+ Constant.mTencent);
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        /** IndexActivity赋值单例静态全局变量，此处用于LoginActivity指定销毁当前IndexActivity*/
        App.appActivity = IndexActivity.this;
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
        /** 判断是否已登录 */
        initNotLoginGoLogin();
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
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                mDrawer_layout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.drawer_menu_school:
                        Snackbar.make(mDrawer_layout, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                        startActivityAnimLeftToRight(new Intent(IndexActivity.this,waimai.class));
                        return true;
                    case R.id.drawer_menu_see_calendar:
                        Snackbar.make(mDrawer_layout, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_menu_setting:
                        Snackbar.make(mDrawer_layout, "点宝宝11干啥", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_menu_about_author:
                        Snackbar.make(mDrawer_layout, "点宝宝1干啥", Snackbar.LENGTH_SHORT).show();
                        return true;
                    case R.id.drawer_menu_logout://退出QQ登录
                        Snackbar snackbar= Snackbar.make(mDrawer_layout,"已退出QQ登录~",Snackbar.LENGTH_SHORT)
                                .setActionTextColor(getResources().getColor(R.color.colorAccent));
                        //设置Snackbar上提示的字体颜色
                        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();

                        /** 开启一个子线程 */
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //发送方式一 直接发送一个空的Message
                                QQHandler.sendEmptyMessage(1);
                            }
                        }).start();
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
    }

    /**
     * 初始化底部导航
     */
    private void initBottomNaviCation() {
        //创建Fragment类型的数组，适配ViewPager，添加四个功能页
        fragments = new Fragment[]{new BottomHomeFragment(), new BottomCategoryFragment(), new BottomShopFragment(), new BottomMineFragment()};
        fragmentList.addAll(Arrays.asList(fragments));
        //ViewPager设置MyAdapter适配器，遍历List<Fragment>集合，填充Fragment页面
        mVp_content.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(),fragments,fragmentList));
        //底部导航加载ViewPager
        mBbl.setViewPager(mVp_content);
        //position：底部导航索引，unreadNum：页签未读数
        mBbl.setUnread(0, 20);//设置第一个页签的未读数为20,
        mBbl.setUnread(1, 1001);//设置第二个页签的未读数
        mBbl.showNotify(2);//设置第三个页签显示提示的小红点
        mBbl.setMsg(3, "未登录");//设置第四个页签显示NEW提示文字
        mBbl.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition, final int currentPosition) {
                Log.i(TAG, "position: " + currentPosition);
                if (currentPosition == 0) {
                    //如果是第一个，即首页
                    if (previousPosition == currentPosition) {
                        //bottomBarItem.setSelectedIcon(R.mipmap.tab_loading);//更换成加载图标
                        Snackbar.make(mNav_view, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                    }
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
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(IndexActivity.this, permissions, 1);
        }
    }

    /**
     * 接受QQ登录广播，数据来源于广播，不是本地持久化数据文件，因此需要严格区分
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("CheckResult")
        @Override
        public void onReceive(Context context, Intent intent) {
            /** 根据Gson转化后的Java对象 Intent序列化的键获取广播消息内容*/
            Constant.qqUser = (QQUserBean) intent.getSerializableExtra("QQUserBean");
            Log.i(TAG, "BroadcastReceiver的QQUserBean: "+Constant.qqUser);
            if (Constant.mTencent != null && Constant.mTencent.isSessionValid()) {
                if (Constant.qqUser != null) { //QQUserBean有数据时
                    /** 仿真效果，打开侧滑菜单 */
                    mDrawer_layout.openDrawer(GravityCompat.START);
                    //Constant.qqUser = SharePreferenceUtil.getObject(IndexActivity.this,QQUserBean.class);
                    /** 标题栏字体加粗 */
                    Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
                    /** 创建Glide圆形图像实例*/
                    RequestOptions options = new RequestOptions();
                    options.circleCrop();
                    /** 设置QQ登录成功需要设置加载信息的控件 */
                    setQQAlreadyLoginState(font,options,Constant.qqUser);

                    Snackbar snackbar= Snackbar.make(mDrawer_layout,"登录成功~",Snackbar.LENGTH_SHORT)
                            .setActionTextColor(getResources().getColor(R.color.colorAccent));
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                } else { //QQUserBean无数据时
                    /** 设置未登录默认头像和点击头像登录 */
                    setNotLoginQQUserInfo();
                }
            }else {
                Snackbar snackbar= Snackbar.make(mDrawer_layout,"登录失效",Snackbar.LENGTH_SHORT)
                        .setActionTextColor(getResources().getColor(R.color.colorAccent));
                //设置Snackbar上提示的字体颜色
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
            }
        }
    };

    /**
     * 判断是否已登录
     * 已登录：持久化文件获取信息并更新UI
     * 未登录：拉起QQ授权界面，获取授权后的回掉信息，并存入持久化文件
     * 持久化文件与LoginActivity为同一文件
     */
    private void initNotLoginGoLogin(){
        /** 创建JSONObject实例，重组Json数据顺序，提供给Constant.mTencent.initSessionCache(jsonObject)，初始化Tencent实例会话*/
        JSONObject jsonObject = new JSONObject();
        /** 获取本地Session会话Java对象持久化数据*/
        Constant.qqUserSessionBean = SharePreferenceUtil.getObject(IndexActivity.this,QQUserSessionBean.class);
        Constant.qqUser= SharePreferenceUtil.getObject(IndexActivity.this,QQUserBean.class);
        Log.i(TAG, "首页Constant.qqUserSessionBean: "+Constant.qqUserSessionBean);
        Log.i(TAG, "首页Constant.qqUser: "+Constant.qqUser);
        if (Constant.qqUserSessionBean!=null){//本地持久化xml文件有数据时才满足重组条件
            try {
                jsonObject.put("ret",Constant.qqUserSessionBean.getRet());
                jsonObject.put("openid",Constant.qqUserSessionBean.getOpenid());
                jsonObject.put("access_token",Constant.qqUserSessionBean.getAccess_token());
                jsonObject.put("pay_token",Constant.qqUserSessionBean.getPay_token());
                jsonObject.put("expires_in",Constant.qqUserSessionBean.getExpires_in());
                jsonObject.put("pf",Constant.qqUserSessionBean.getPf());
                jsonObject.put("pfkey",Constant.qqUserSessionBean.getPfkey());
                jsonObject.put("msg",Constant.qqUserSessionBean.getMsg());
                jsonObject.put("login_cost",Constant.qqUserSessionBean.getLogin_cost());
                jsonObject.put("query_authority_cost",Constant.qqUserSessionBean.getQuery_authority_cost());
                jsonObject.put("authority_cost",Constant.qqUserSessionBean.getAuthority_cost());
                jsonObject.put("pfkey",Constant.qqUserSessionBean.getPfkey());
                jsonObject.put("expires_time",Constant.qqUserSessionBean.getExpires_time());
                /** 初始化设置上次授权登录的Session信息——来自持久化重组JSon数据顺序*/
                Constant.mTencent.initSessionCache(jsonObject);
                Log.i(TAG, "mTencent初始化后会话Session是否有效: "+Constant.mTencent.isSessionValid());//true
            } catch (JSONException e) {//异常捕捉
                e.printStackTrace();
            }
            /** 本地文件有数据且Session不为空有效时，提取QQUser持久化用户信息，设置QQ头像+QQ用户名，就不用拉起登录界面*/
            if (Constant.mTencent != null && Constant.mTencent.isSessionValid()){
                initLoginUserData();//初始化QQ登录持久化数据
            }
        }else {
            /** 设置未登录默认头像和点击头像登录 */
            setNotLoginQQUserInfo();
            Snackbar snackbar= Snackbar.make(mDrawer_layout,"温馨提示：您没有登录哟~",Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                    .setAction("点击登录QQ", new View.OnClickListener() {  //设置点击按钮
                        @Override
                        public void onClick(View v) {
                            Constant.mTencent.logout(IndexActivity.this);
                            Constant.mTencent.login(IndexActivity.this, "all", loginListener);

                        }
                    });
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
        //Gson gson = new GsonBuilder().registerTypeAdapter(QQUserSessionBean.class, new QQUserTypeAdapter()).create();
        //String strJson =gson.toJson(SharePreferenceUtil.getObject(IndexActivity.this, QQUserSessionBean.class));
        Log.i(TAG, "(String)QQUserSessionBean重组Adapter排序前== "+ SharePreferenceUtil.getObject(IndexActivity.this, QQUserSessionBean.class));
        Log.i(TAG, "(JSONObject)QQUserSessionBean重组排序后== "+jsonObject.toString());
        Log.i(TAG, "mTencent会话: "+Constant.mTencent.isSessionValid());

    }

    /**
     * 打开APP，进入首页，获取持久化数据进行QQ自动登录，初始化上次保存的QQ头像和QQ昵称
     * 持久化数据文件无数据时，调用未登录默认设置的方法
     */
    @SuppressLint("CheckResult")
    private void initLoginUserData(){
        /** 调用SharePreference工具类获取Gson转化后的Java对象，持久化文件名“ZSAndroid”的本地数据 */
        Constant.qqUser = SharePreferenceUtil.getObject(IndexActivity.this, QQUserBean.class);
        Log.i(TAG, "initLoginUserData: "+Constant.qqUser);
        if (Constant.qqUser != null) {
            /** 标题栏字体加粗 */
            Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
            /** 创建Glide圆形图像实例*/
            RequestOptions options = new RequestOptions();
            options.circleCrop();
            /** 设置QQ登录成功需要设置加载信息的控件 */
            setQQAlreadyLoginState(font,options,Constant.qqUser);
            Snackbar snackbar= Snackbar.make(mDrawer_layout,"已为您自动登录~",Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        } else {
            /** 设置未登录默认头像和点击头像登录 */
            setNotLoginQQUserInfo();
        }
    }

    /**
     * 拉起QQ登录授权请求界面
     */
    private class BaseUiListener extends DefaultUiListener {
        @Override
        public void onComplete(Object response) {
            Log.d(TAG, "onComplete:QQ登录回调成功");
            if (null == response) {
                FileCodeUtil.showResultDialog(IndexActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                FileCodeUtil.showResultDialog(IndexActivity.this, "返回为空", "登录失败");
                return;
            }
            doComplete((JSONObject) response);

        }
        protected void doComplete(JSONObject values) {

        }


        @Override
        public void onError(UiError e) {
            Toast.makeText(IndexActivity.this,"QQ授权登录出错！" + e.errorDetail, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(IndexActivity.this,"取消QQ授权登录！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * QQ登录授权监听回调数据处理
     */
    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            LoadingDialog.showSimpleLD(IndexActivity.this,getString(R.string.indexLoadLoginInfo));
            Log.d(TAG, "回调成功，会话消息===" + values.toString());
            /** 初始化OPENID和TOKEN值（为了得到用户的信息） */
            initOpenidAndTokenAndGsonGetParseQQUserInfo(values);
            /** 回掉的会话Session信息持久化保存，用于自动登录使用 */
            initSaveSessionDataToLocalFile(values);
            /** 回调成功会话信息，保存到Constant.mTencent中，不做持久化操作时，仅当前APP启动--有效时间--结束
             * 调用Constant.mTencent.logout(上下文) 可以使得当前会话在APP结束之前失效——即注销当前授权登录QQ的Session信息*/
            Constant.mTencent.saveSession(values);

        }
    };

    /**
     * 初始化OPENID和TOKEN值（为了得到用户的信息）
     * @param jsonObject
     */
    public void initOpenidAndTokenAndGsonGetParseQQUserInfo(JSONObject jsonObject) {
        try {
            /**获取Constant.mTencent.login(监听器loginListener) 回调成功有效Session中的 openid值、access_token值、expires_in值*/
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);//用户授权令牌
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);//用户应用唯一标识
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);//有效时间
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                Log.i(TAG, "回调后设置会话前是否有效:"+Constant.mTencent.isSessionValid());//false
                /** 下面两行set设置方法非常关键重要，若没有设置，导致Constant.mTencent的Session无效，将不能对QQ授权用户的信息列表执行请求与回调 */
                Constant.mTencent.setAccessToken(token, expires);//授权令牌设置至Tencent实例
                Constant.mTencent.setOpenId(openId);//应用唯一标识设置至Tencent实例
                Log.i(TAG, "回调后设置会话后是否有效:"+Constant.mTencent.isSessionValid());//true
                /** 会话Session有效时进行QQ授权用户的信息列表请求与回调 */
                GsonParseJsonDataToLocalAndToHandlerOrThread();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Constant.mTencent的Session有效为true时进行QQ授权用户的信息列表请求与回调
     * Gson解析Json数据存入SharePreference+通过Handler发送消息+子线程并更新IndexActivity首页UI
     */
    private void GsonParseJsonDataToLocalAndToHandlerOrThread() {
        if (Constant.mTencent != null && Constant.mTencent.isSessionValid()) {
            IUiListener listener = new DefaultUiListener() {

                /** 以下进行对获取授权用户信息使用业务，这里存入本地，以及通过Handler发送消息+子线程并更新IndexActivity首页UI */
                @Override
                public void onComplete(final Object response) {
                    Log.d(TAG, "请求回调用户信息列表= " + response.toString());
                    /** 调用Gson工具类，回掉的JSON数据，转化为Java对象*/
                    Constant.qqUser = GsonUtil.gsonToBean(response.toString(), QQUserBean.class);
                    /** 调用SharePreference工具类把Gson转化后的Java对象实现数据持久化，文件名为“ZSAndroid”的本地数据*/
                    SharePreferenceUtil.putObject(IndexActivity.this,Constant.qqUser);
                    /** 获取本地Session会话Java对象持久化数据*/
                    Log.i(TAG, "qqUser全部数据: "+Constant.qqUser);
                    Message msg = new Message();
                    msg.obj = Constant.qqUser;
                    msg.what = 0;
                    QQHandler.sendMessage(msg);
                    /** 关闭正在加载登录信息友好进度条*/
                    LoadingDialog.closeSimpleLD();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(IndexActivity.this, "取消获取授权用户信息", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError e) {
                    Toast.makeText(IndexActivity.this, "获取授权用户信息出错："+e.errorDetail, Toast.LENGTH_SHORT).show();
                }
            };
            /** 根据Constant.mTencent会话中TOKEN值，请求回调授权用户信息列表*/
            UserInfo info = new UserInfo(this, Constant.mTencent.getQQToken());
            /** 开始监听请求回调操作*/
            info.getUserInfo(listener);

        } else {
            Toast.makeText(this, "QQ无效Session", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登录回调成功后保存Session信息本地XML数据文件中
     * @param jsonObject
     */
    private void initSaveSessionDataToLocalFile(JSONObject jsonObject) {
        Log.i(TAG, "回调成功Gson解析Json前会话Session数据: "+jsonObject.toString());
        //Gson解析并序列号至Java对象中
        QQUserSessionBean qqUserSessionBean = GsonUtil.gsonToBean(jsonObject.toString(), QQUserSessionBean.class);
        Log.i(TAG, "回调成功Gson解析Json后会话Session数据(持久化存入此解析数据到xml): "+qqUserSessionBean);
        /** Gson解析后Java对象持久化数据保存本地，IndexActivity首页调用JSONObject的put()方法重组顺序，提供给Constant.mTencent.initSessionCache(JSONObject实例) */
        SharePreferenceUtil.putObject(IndexActivity.this,qqUserSessionBean);
    }

    /**
     * 线程刷新UI数据
     */
    @SuppressLint("HandlerLeak")
    public Handler QQHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    /** 判断本地取出的QQUser对象是否有数据*/
                    if (Constant.qqUser != null) { //QQUserBean有数据时
                        //设置本地持久化接收的QQ昵称和QQ头像
                        QQUserBeanIsValidSetTextAndGlideUserHead();
                        /** 获取首页界面拉起QQ登录授权存入持久化文件的会话信息，用于判断当前首页登录，避免导致跳转LoginActivity*/
                        Constant.qqUserSessionBean = SharePreferenceUtil.getObject(IndexActivity.this,QQUserSessionBean.class);
                        Log.i(TAG, "QQ回调的首页Constant.qqUserSessionBean: "+Constant.qqUserSessionBean);
                    } else { //QQUserBean无数据时
                        //设置未登录默认头像和点击头像登录
                        setNotLoginQQUserInfo();
                    }
                    break;
                case 1:
                    /** 未登录状态设置默认的头像和文本 */
                    setNotLoginQQUserInfo();
                    /**  删除持久化数据文件 */
                    setQQUserLoginLogout();
                    break;
            }

        }
    };

    /**
     * QQUserBean有数据时设置本地持久化或广播消息接收的QQ昵称和QQ头像
     */
    @SuppressLint("CheckResult")
    private void QQUserBeanIsValidSetTextAndGlideUserHead() {
        /** 标题栏字体加粗 */
        Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
        Constant.qqUser = SharePreferenceUtil.getObject(IndexActivity.this,QQUserBean.class);
        /** 创建Glide圆形图像实例*/
        RequestOptions options = new RequestOptions();
        options.circleCrop();

        /** 设置昵称动态开始——动态结束的字体样式 */
        mCollapsing_toolbar_layout.setExpandedTitleTypeface(font);
        mCollapsing_toolbar_layout.setCollapsedTitleTypeface(font);
        /** 设置ToolbarQQ昵称 */
        mCollapsing_toolbar_layout.setTitle(Constant.qqUser.getNickname());
        /** 设置侧滑的QQ昵称 */
        tvNavHeaderName.setText(Constant.qqUser.getNickname());
        /** 设置友好时间状态可见*/
        mIndex_tv_user_hello.setVisibility(View.VISIBLE);
        /** 设置友好时间提示*/
        getDate(mIndex_tv_user_hello);
        /** 设置圆形QQ头像 */
        Glide.with(IndexActivity.this)
                .load(Constant.qqUser.getFigureurl_qq_1())
                .apply(options)
                .into(mIndex_iv_user_head);

        /** 设置侧滑的圆形QQ头像 */
        Glide.with(IndexActivity.this)
                .load(Constant.qqUser.getFigureurl_qq_1())
                .apply(options)
                .into(ivNavHeaderIcon);

        Snackbar snackbar= Snackbar.make(mDrawer_layout,"登录成功~",Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(R.color.colorAccent));
        //设置Snackbar上提示的字体颜色
        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    /**
     * 未登录状态设置默认的头像和文本
     */
    @SuppressLint("CheckResult")
    private void setNotLoginQQUserInfo(){
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
                .apply(options)
                .into(ivNavHeaderIcon);
    }

    /**
     * 仿真注销登录QQ
     * 1.删除持久化数据文件
     * 2.Java对象设置为null
     * 3.Tencent实例的Session设置注销，使其失效
     */
    private void setQQUserLoginLogout(){
        /** 退出登录，注销mTencent为null */
        Constant.qqUser = null;
        SharePreferenceUtil.removeObject(IndexActivity.this,QQUserBean.class);//类似于Constant.qqUser = null;
        Log.i(TAG, "注销登录的QQUserBean: " + Constant.qqUser);

        Constant.qqUserSessionBean = null;
        SharePreferenceUtil.removeObject(IndexActivity.this,QQUserSessionBean.class);//类似于Constant.qqUserSessionBean = null;
        Log.i(TAG, "注销登录的QQUserSessionBean: " + Constant.qqUserSessionBean);
        Constant.mTencent.logout(IndexActivity.this);
        Log.i(TAG, "注销登录的会话Tencent: "+Constant.mTencent.isSessionValid());
    }

    /**
     * 设置QQ登录成功需要设置加载信息的控件
     * @param font 加粗字体
     * @param options Glide
     * @param qqUser 用户
     */
    private void setQQAlreadyLoginState(Typeface font, RequestOptions options, QQUserBean qqUser){
        /** 设置昵称动态开始——动态结束的字体样式 */
        mCollapsing_toolbar_layout.setExpandedTitleTypeface(font);
        mCollapsing_toolbar_layout.setCollapsedTitleTypeface(font);
        /** 设置Toolbar的QQ昵称 */
        mCollapsing_toolbar_layout.setTitle(qqUser.getNickname());
        /** 设置侧滑的QQ昵称 */
        tvNavHeaderName.setText(qqUser.getNickname());
        /** 设置友好时间状态可见*/
        mIndex_tv_user_hello.setVisibility(View.VISIBLE);
        /** 设置Toolbar的圆形QQ头像 */
        Glide.with(IndexActivity.this)
                .load(qqUser.getFigureurl_qq_1())
                .apply(options)
                .into(mIndex_iv_user_head);
        /** 设置侧滑的圆形QQ头像 */
        Glide.with(IndexActivity.this)
                .load(qqUser.getFigureurl_qq_1())
                .apply(options)
                .into(ivNavHeaderIcon);
        /** 设置友好时间提示*/
        getDate(mIndex_tv_user_hello);
    }

    /**
     * 判断当前时间处于那个时间
     * @param tv
     */
    private void getDate(TextView tv){
        Typeface font = Typer.set(IndexActivity.this).getFont(Font.ROBOTO_BOLD);
        tv.setTypeface(font);
        Date d = new Date();
        if(d.getHours()<4){
            tv.setText("早点休息哟~,");
        }else if(d.getHours()<11){
            tv.setText("早上好,");
        }else if(d.getHours()<14){
            tv.setText("中午好,");
        }else if(d.getHours()<18){
            tv.setText("下午好,");
        }else if(d.getHours()<24){
            tv.setText("晚上好,");
        }
    }

    /**
     * 判断检查QQ登录是否有效
     */
    private void checkQQLoginIfValid() {
        if (Constant.qqUser== null){
            startActivityAnimLeftToRight(new Intent(IndexActivity.this, LoginActivity.class));
            return;
        }
        if (Constant.mTencent !=null && Constant.mTencent.isSessionValid() && Constant.qqUserSessionBean != null) {
            Snackbar snackbar = Snackbar.make(mDrawer_layout, R.string.index_already_login_qq, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
    }


    /**
     *  Toolbar头像单击事件+浮动按钮单击事件
     * @param view
     */
    @OnClick({R2.id.index_iv_user_head,R2.id.floating_action_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.index_iv_user_head://Toolbar头像
                /** 判断检查QQ登录是否有效 */
                checkQQLoginIfValid();
                break;
            case R.id.floating_action_btn:
                Snackbar snackbar= Snackbar.make(mDrawer_layout,"点宝宝干啥",Snackbar.LENGTH_SHORT)
                        .setActionTextColor(getResources().getColor(R.color.colorAccent));
                //设置Snackbar上提示的字体颜色
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                break;
        }
    }

    /**
     * 打开标题栏弹窗
     */
    private void OpenToolbarPopup() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_pop_menu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("TAG","onDismiss");
                    }
                })
                .create()
                .showAsDropDown(findViewById(R.id.search),0,20);
    }
    /**
     * 处理弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.item1:
                        Toast.makeText(IndexActivity.this, "点击 Item菜单1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        Toast.makeText(IndexActivity.this, "点击 Item菜单2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item3:
                        Toast.makeText(IndexActivity.this, "点击 Item菜单3", Toast.LENGTH_SHORT).show();
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
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }

    /**
     * 监听Toolbar菜单项item
     * 添加点击事件,用于打开或关闭侧滑抽屉
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
                //new ToastUtil().ToastLocation(MainActivity.this,"搜索待实现", Toast.LENGTH_SHORT, Gravity.CENTER);
                OpenToolbarPopup();//打开标题栏弹窗
                break;
//            case R.id.qr:
//                Toast.makeText(this, "扫一扫待实现~", Toast.LENGTH_SHORT).show();
//                break;
        }
        return true;
    }

    /**
     * 回调数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 防触碰处理
     * 再按一次退出程序
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 3000) {
                    BaseActivity.showToast("再按一次退出程序！");
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
     * 注销广播
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
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