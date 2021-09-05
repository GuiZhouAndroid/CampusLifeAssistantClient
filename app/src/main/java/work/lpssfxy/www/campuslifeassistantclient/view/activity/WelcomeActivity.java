package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.welcome.MyFragmentPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.welcome.TextPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.welcome.FixedSpeedScroller;
import work.lpssfxy.www.campuslifeassistantclient.base.welcome.MyInterceptViewPager;

/**
 * created by on 2021/8/13
 * 描述：左右滑动欢迎页
 *
 * @author ZSAndroid
 * @create 2021-08-13-5:03
 */
@SuppressLint("NonConstantResourceId")
public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {
    private static final String TAG = "WelcomeActivity";
    //绑定控件id
    @BindView(R2.id.main_text_pager)
    MyInterceptViewPager main_text_pager; //ViewPager文字
    @BindView(R2.id.main_image_pager)
    MyInterceptViewPager main_image_pager; //ViewPager图片
    @BindView(R2.id.main_touch_layout)
    RelativeLayout main_touch_layout; //点击分发
    @BindView(R2.id.main_indicator_one)
    ImageView main_indicator_one; //第一个圆点
    @BindView(R2.id.main_indicator_two)
    ImageView main_indicator_two; //第二个圆点
    @BindView(R2.id.main_indicator_three)
    ImageView main_indicator_three; //第三个圆点
    @BindView(R2.id.welcome_tv_go_right)
    TextView mWelcome_tv_go_right; //右边进入
    @BindView(R2.id.welcome_tv_go_login)
    TextView mWlcome_tv_go_login; //登录+注册
    @BindView(R2.id.welcome_tv_go_index)
    TextView mWlcome_tv_go_index; //进入首页
    @BindDrawable(R2.drawable.welcome_circle_gray)
    Drawable welcome_circle_gray;//页签小圆点主题默认样式
    @BindDrawable(R2.drawable.welcome_circle_main)
    Drawable welcome_circle_main;//页签小圆点主题默认样式

    MyFragmentPagerAdapter fragmentPagerAdapter = null;//图片嵌入Fragment适配器
    TextPagerAdapter textPagerAdapter = null;//文字ViewPager适配器

    public static boolean SHOW_TWO_ANIM = true;//第二个界面是否展示动画：3->2时 2没展示动画效果
    int pageIndex = 0;//ViewPager当前索引标识
    private float startX, endX, startY, endY;//计算手势滑动坐标

    /** 防触碰使用的变量 */
    private long firstTime;


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
        return R.layout.welcome_activity;
    }

    /**
     * 准备数据
     */
    @Override
    protected void prepareData() {
        setTextScrollDurationTime();//设置文字滑动时间参数
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        //App.AppContext = WelcomeActivity.this;
    }

    /**
     * 获取 MyFragmentPagerAdapter 适配器中的Fragment界面
     *
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());//创建图片嵌入Fragment适配器实例
        textPagerAdapter = new TextPagerAdapter();//创建文字ViewPager适配器实例
    }

    /**
     * 设置事件
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEvent() {
        main_image_pager.setAdapter(fragmentPagerAdapter);//设置Fragment适配器
        main_text_pager.setAdapter(textPagerAdapter);//设置文字适配器
        main_image_pager.addOnPageChangeListener(this);//设置Fragment适配器监听器
        main_text_pager.addOnPageChangeListener(this);//设置文字适配器监听器
        main_touch_layout.setOnTouchListener(this);//设置手势滑动联动监听
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        setCustomScrollStyle(position);//自定义设置滑动样式
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置控件单击事件
     */
    @OnClick({R2.id.welcome_tv_go_right, R2.id.welcome_tv_go_login, R2.id.welcome_tv_go_index})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.welcome_tv_go_right:

                break;
            case R.id.welcome_tv_go_login:
                startActivityAnimLeftToRight(new Intent(WelcomeActivity.this, loginAc.class));
                //finish(); 登录成功后销毁当前 WelcomeActivity ，否则暂时不销毁 WelcomeActivity
                break;
            case R.id.welcome_tv_go_index:
                startActivityAnimLeftToRight(new Intent(WelcomeActivity.this, IndexActivity.class));
                finish(); //直接手动单击进入首页后，同时销毁当前 WelcomeActivity
                break;
        }
    }

    /**
     * 设置文字滑动时间参数
     */
    private void setTextScrollDurationTime() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");//反射
            field.setAccessible(true);
            FixedSpeedScroller scrollerText = new FixedSpeedScroller(this, new AccelerateInterpolator());
            field.set(main_text_pager, scrollerText);
            scrollerText.setmDuration(350);//设置文字延时滑动
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义设置滑动样式
     *
     * @param position 滑动当前的页面索引值
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void setCustomScrollStyle(int position) {
        pageIndex = position;//当前页签索引值，赋值全局变量pageIndex，作为联动唯一依据（图片+文字）


        //设置页签小圆点，默认样式颜色：welcome_circle_gray
        main_indicator_one.setImageDrawable(welcome_circle_gray);//设置第一页滑动圆点样式
        main_indicator_two.setImageDrawable(welcome_circle_gray);//设置第二页滑动圆点样式
        main_indicator_three.setImageDrawable(welcome_circle_gray);//设置第三页滑动圆点样式

        switch (position) {
            case 0://第一页
                mWelcome_tv_go_right.setText("右滑进入>>");//设置文本
                mWelcome_tv_go_right.setVisibility(View.VISIBLE);//设置可见，避免第二页右滑切换回来状态：失效Gone+空文本“”
                SHOW_TWO_ANIM = true;//第一页展示动画，同步过渡至第二页，因此第二页不设置此属性
                //设置第一页滑动圆点颜色红色
                main_indicator_one.setImageDrawable(welcome_circle_main);
                break;
            case 1://第二页
                // 第三页切换至第二页，文本属性+可见状态——相反
                if (mWelcome_tv_go_right.getText().toString().isEmpty() //右滑进入文本内容为空
                        || !mWlcome_tv_go_login.getText().toString().isEmpty() //登录+注册文本内容非空
                        && !mWlcome_tv_go_index.getText().toString().isEmpty()) {//进入主页文本内容非空

                    mWelcome_tv_go_right.setText("右滑进入>>");//空就设置有文本
                    mWelcome_tv_go_right.setVisibility(View.VISIBLE);//设置可见

                    mWlcome_tv_go_login.setText("");//非空设置空文本
                    mWlcome_tv_go_login.setVisibility(View.GONE);//设置失效

                    mWlcome_tv_go_index.setText("");//非空设置空文本
                    mWlcome_tv_go_index.setVisibility(View.GONE);//设置失效

                }
                //改变页签圆点，颜色为红色
                main_indicator_two.setImageDrawable(welcome_circle_main);//第二页页签圆点
                break;
            case 2://第二页
                if (!mWelcome_tv_go_right.getText().toString().isEmpty()//右滑进入文本内容非空
                        || mWlcome_tv_go_login.getText().toString().isEmpty()//登录+注册文本内容非空
                        && mWlcome_tv_go_index.getText().toString().isEmpty()) {//进入主页文本内容非空

                    mWelcome_tv_go_right.setText("");//非空设置空文本
                    mWelcome_tv_go_right.setVisibility(View.GONE);//设置失效

                    mWlcome_tv_go_login.setText("登录/注册");//空就设置有文本
                    mWlcome_tv_go_login.setVisibility(View.VISIBLE);//设置可见

                    mWlcome_tv_go_index.setText("进入主页");//空就设置有文本
                    mWlcome_tv_go_index.setVisibility(View.VISIBLE);//设置可见

                }
                SHOW_TWO_ANIM = false;
                main_indicator_three.setImageDrawable(welcome_circle_main);//第三页滑动圆点

                break;
            default:
                break;
        }
    }

    /**
     * 手势滑动联动监听
     *
     * @param view        监听RelativeLayout控件
     * @param motionEvent 滑动方向事件
     * @return boolean ture：匹配事件，执行生效
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //匹配监听事件
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN://
                startX = motionEvent.getX();
                //startY = motionEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                endX = motionEvent.getX();
                //endY = motionEvent.getY();
                WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

                Point size = new Point();
                windowManager.getDefaultDisplay().getSize(size);
                int width = size.x;
                if (startX - endX >= (width / 8)) {// startX - endX 大于0 且大于宽的1/8 往后翻页(往左滑)
                    if (pageIndex == 0) { //第一页时，左滑动一次
                        main_image_pager.setCurrentItem(1, true); //图片动画——跳转第二页
                        main_text_pager.setCurrentItem(1, true);//文字滑动——跳转第二页
                    } else if (pageIndex == 1) { //第二页时，左滑动一次
                        main_image_pager.setCurrentItem(2, true);//图片动画——跳转第三页
                        main_text_pager.setCurrentItem(2, true);//文字滑动——跳转第三页
                    } else if (pageIndex == 2) { //第三页时——最后一页ViewPager滑动之后，跳转到主页面
                        startActivityAnimLeftToRight(new Intent(WelcomeActivity.this, IndexActivity.class));
                        finish();
                    }
                } else if (endX - startX >= (width / 8)) { // endX - startX   大于0 且大于宽的1/8 往前翻页(往右滑)
                    if (pageIndex == 2) { //第三页时，右滑动一次
                        main_image_pager.setCurrentItem(1, true);//图片动画——跳转第二页
                        main_text_pager.setCurrentItem(1, true);//文字滑动——跳转第二页
                    } else if (pageIndex == 1) {//第二页时，右滑动一次
                        main_image_pager.setCurrentItem(0, true);//图片动画——跳转第一页
                        main_text_pager.setCurrentItem(0, true);//文字滑动——跳转第一页
                    }
                }
                break;
        }
        return true;
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
     * 销毁 WelcomeActivity时，全局变量赋null值
     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (App.AppContext != null) {
//            App.AppContext = null;
//        }
//    }

}
