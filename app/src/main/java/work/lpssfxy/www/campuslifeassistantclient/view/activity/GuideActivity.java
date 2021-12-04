package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.circleprogress.CircleProgress;
import work.lpssfxy.www.campuslifeassistantclient.base.guide.GuideFullVideoView;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;

/**
 * created by on 2021/8/21
 * 描述：视频引导页
 *
 * @author ZSAndroid
 * @create 2021-08-21-0:27
 */
@SuppressLint("NonConstantResourceId")
public class GuideActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, CircleProgress.OnCountDownFinishListener {
    /** Video播放器 */
    @BindView(R2.id.Guide_Video_View) GuideFullVideoView videoView;
    /** 圆形倒计时 */
    @BindView(R2.id.circleprogress) CircleProgress mCircleProgress;
    /** 跳过按钮 */
    @BindView(R2.id.btn_guide_skip) Button mBtn_guide_skip;


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
     * 关闭沉浸状态栏
     *
     * @return true:顶部状态栏全透明 false:顶部状态栏半透明
     */
    @Override
    protected Boolean isSetStatusBarState() {
        return false;
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
     * 开启设置底部导航栏黑色半透明
     * 此页面为视频全屏沉浸，已设置开启全屏沉浸FullScreen，因此此处设置true或false没有影响
     *
     * @return true:底部导航栏白色 false:底部导航栏黑色半透明
     */
    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return false;
    }

    /**
     * 开启全屏沉浸
     *
     * @return true:顶部状态栏隐藏+底部导航栏隐藏  false:log打印日志“返回值不正确”
     */
    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return true;
    }

    /**
     * 绑定布局
     *
     * @return guide_activity.xml
     */
    @Override
    public int bindLayout() {
        return R.layout.activity_guide;
    }

    /**
     * 加载播放视频
     */
    @Override
    protected void prepareData() {
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guide_video));
    }

    /**
     * 初始化View
     */
    @Override
    protected void initView() {
        mCircleProgress.startCountDown();//开始倒计时
        mCircleProgress.bringToFront();//：设置CircleProgress 优先级> GuideFullVideoView
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);竖屏
    }

    /**
     * 开始播放
     *
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
        playVideoView();//加载视频+播放
    }

    /**
     * 设置事件
     */
    @Override
    protected void initEvent() {
        videoView.setOnCompletionListener(this);//监听视频播放
        mCircleProgress.setAddCountDownListener(this);//监听倒计时
        mCircleProgress.setOnClickListener(this);//圆环跳过
        mBtn_guide_skip.setOnClickListener(this);//按钮跳过
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
     * 关闭自动监听倒计时，执行点击跳过
     */
    public void onViewBtnSkipOnClick() {
        //videoView.stopPlayback();//停止播放视频
        mCircleProgress.setAddCountDownListener(null);//关闭自动监听倒计时
        int count = SharePreferenceUtil.getInstance().getInt("first", 0); // 取出数据
        //如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
        if (count == 0) {
            IntentUtil.startActivityAnimActivity(GuideActivity.this,new Intent(GuideActivity.this, WelcomeActivity.class));
        } else {
            IntentUtil.startActivityAnimActivity(GuideActivity.this,new Intent(GuideActivity.this, IndexActivity.class));
        }
        finish();
        //存入数据
        SharePreferenceUtil.getInstance().putInt("first", 1); // 存入数据
    }

    /**
     * 循环播放
     *
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        videoView.start();
    }

    /**
     * 倒计时结束，欢迎页跳转判断
     * 首次进入App跳转-滑动欢迎页(WelcomeActivity.xml)
     * 非首次进入App跳转-应用首页(IndexActivity.xml)
     */
    @Override
    public void countDownFinished() {
        int count = SharePreferenceUtil.getInstance().getInt("first", 0); // 取出数据
        //如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
        if (count == 0) {
            IntentUtil.startActivityAnimActivity(GuideActivity.this,new Intent(GuideActivity.this, WelcomeActivity.class));
        } else {
            IntentUtil.startActivityAnimActivity(GuideActivity.this,new Intent(GuideActivity.this, IndexActivity.class));
        }
        finish();
        //存入数据
        SharePreferenceUtil.getInstance().putInt("first", 1); // 存入数据
    }

    /**
     * 播放视频
     */
    private void playVideoView() {
        videoView.start();
    }


    /**
     * 重启App加载播放
     */
    @Override
    protected void onRestart() {
        playVideoView();
        super.onRestart();
    }

    /**
     * 锁屏或者切出时，播放的视频终止
     */
    @Override
    protected void onStop() {
        videoView.stopPlayback();
        super.onStop();
    }

    /**
     * 屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击跳过
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.circleprogress://圆环跳过
            case R.id.btn_guide_skip://按钮跳过
                onViewBtnSkipOnClick();
                break;
        }
    }
}
