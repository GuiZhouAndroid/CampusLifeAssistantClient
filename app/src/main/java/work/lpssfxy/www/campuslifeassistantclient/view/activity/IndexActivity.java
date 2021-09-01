package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.example.zhouwei.library.CustomPopWindow;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.bottom.BottomBarItem;
import work.lpssfxy.www.campuslifeassistantclient.base.bottom.BottomBarLayout;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomCategoryFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomHomeFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomMineFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom.BottomShopFragment;

@SuppressLint("NonConstantResourceId")
public class IndexActivity extends BaseActivity {
    //@BindArray(R2.array.city)
    //String[] strArray;//绑定资源文件中string字符串数组
    //@BindBitmap(R.mipmap.ic_launcher)
    //Bitmap bitmap;//绑定资源文件中mipmap中的ic_launcher图片
    //@BindColor(R2.color.purple_200)
    private static final String TAG = "IndexActivity";
    /** 定制Toolbar */
    @BindView(R2.id.appbar_constant_toolbar) Toolbar mAppbar_constant_toolbar;
    /** Toolbar折叠布局 */
    @BindView(R2.id.collapsing_toolbar_layout) CollapsingToolbarLayout mCollapsing_toolbar_layout;
    /** Toolbar头像图标 */
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
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);

            //actionBar.setTitle("主标题");
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        setSupportActionBar(mAppbar_constant_toolbar);
    }

    /**
     * 初始化数据
     *
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
        String str = "张松";
        //创建Fragment类型的数组，适配ViewPager，添加四个功能页
        fragments = new Fragment[]{new BottomHomeFragment(), new BottomCategoryFragment(), new BottomShopFragment(), new BottomMineFragment()};
        fragmentList.addAll(Arrays.asList(fragments));

        //标题栏字体加粗
        Typeface font = Typer.set(this).getFont(Font.ROBOTO_BOLD);
        mCollapsing_toolbar_layout.setExpandedTitleTypeface(font);
        mCollapsing_toolbar_layout.setCollapsedTitleTypeface(font);
        mCollapsing_toolbar_layout.setTitle(str);

    }

    /**
     * 设置事件
     */
    @Override
    protected void initEvent() {

    }

    /**
     * 开始监听
     */
    @Override
    protected void initListener() {
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
                Log.i("MainActivity", "position: " + currentPosition);
                if (currentPosition == 0) {
                    //如果是第一个，即首页
                    if (previousPosition == currentPosition) {
//                        bottomBarItem.setSelectedIcon(R.mipmap.tab_loading);//更换成加载图标
                        Snackbar.make(mNav_view, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

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

        /**
         * 给DrawerLayout侧滑的Navigation导航菜单添加点击事件
         */
        mNav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_menu_school:
                        Snackbar.make(mNav_view, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                        startActivityAnim(new Intent(IndexActivity.this,waimai.class));
                        mDrawer_layout.closeDrawers();//关闭侧滑
                        return true;
                    case R.id.drawer_menu_see_calendar:
                        Snackbar.make(mNav_view, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
                        mDrawer_layout.closeDrawers();//关闭侧滑
                        return true;
                    case R.id.drawer_menu_setting:
                        Snackbar.make(mNav_view, "点宝宝11干啥", Snackbar.LENGTH_SHORT).show();
                        mDrawer_layout.closeDrawers();//关闭侧滑
                        return true;
                    case R.id.drawer_menu_about_author:
                        Snackbar.make(mNav_view, "点宝宝1干啥", Snackbar.LENGTH_SHORT).show();
                        mDrawer_layout.closeDrawers();//关闭侧滑
                        return true;
                    case R.id.drawer_menu_logout:
                        mDrawer_layout.closeDrawers();//关闭侧滑
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 浮动按钮单击事件
     */
    @OnClick(R2.id.floating_action_btn)
    public void onViewOneClicked() {
        Snackbar.make(mFloating_action_btn, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
    }

    /**
     *  头像+名字单击事件
     * @param view
     */
    @OnClick({R2.id.index_iv_user_head,R2.id.index_tv_user_hello})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.index_iv_user_head://头像
                startActivityAnim(new Intent(IndexActivity.this, LoginActivity.class));
                break;
            case R.id.index_tv_user_hello://名字
                Toast.makeText(this, "我是多个tv点击事件", Toast.LENGTH_SHORT).show();
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
            case R.id.qr:
                Toast.makeText(this, "扫一扫待实现~", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
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
}