package work.lpssfxy.www.campuslifeassistantclient.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.circleprogress.CircleProgress;
import work.lpssfxy.www.campuslifeassistantclient.base.guide.GuideFullVideoView;

/**
 * created by on 2021/8/21
 * 描述：视频引导页
 *
 * @author ZSAndroid
 * @create 2021-08-21-0:27
 */
@SuppressLint("NonConstantResourceId")
public class GuideActivity extends BaseActivity implements MediaPlayer.OnCompletionListener, CircleProgress.OnCountDownFinishListener {
    //Video播放器
    @BindView(R2.id.Guide_Video_View)
    GuideFullVideoView videoView;
    //圆形倒计时
    @BindView(R2.id.circleprogress)
    CircleProgress circleprogress;

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
     * @return
     */
    @Override
    public int bindLayout() {
        return R.layout.guide_activity;
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
        circleprogress.startCountDown();//开始倒计时
        circleprogress.bringToFront();//：设置CircleProgress 优先级> GuideFullVideoView
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
    }

    /**
     * @param savedInstanceState 界面非正常销毁时保存的数据
     */
    @Override
    protected void initData(Bundle savedInstanceState) {
        playVideoView();//加载视频+播放
    }

    /**
     * 设置监听事件
     */
    @Override
    protected void initEvent() {
        videoView.setOnCompletionListener(this);//监听视频播放
        circleprogress.setAddCountDownListener(this);//监听倒计时
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
     * 倒计时结束，跳转欢迎页
     */
    @Override
    public void countDownFinished() {
        SharedPreferences preferences= getSharedPreferences("count", Context.MODE_PRIVATE); // 存在则打开它，否则创建新的Preferences
        int count = preferences.getInt("count", 0); // 取出数据
        /**
         *如果用户不是第一次使用则直接调转到显示界面,否则调转到引导界面
         */
        if (count == 0) {
            Intent intent1 = new Intent();
            intent1.setClass(GuideActivity.this, WelcomeActivity.class);
            startActivity(intent1);
            finish();
        } else {
            Intent intent2 = new Intent();
            intent2.setClass(GuideActivity.this, MainActivity.class);
            startActivity(intent2);
            finish();
        }
        finish();
        //实例化Editor对象
        SharedPreferences.Editor editor = preferences.edit();
        //存入数据
        editor.putInt("count", 1); // 存入数据
        //提交修改
        editor.commit();
//        startActivity(new Intent(GuideActivity.this, WelcomeActivity.class)); //执行跳转
//        overridePendingTransition(R.anim.activity_common_anim_out, R.anim.activity_common_anim_in);//跳转动画
//        finish();//跳转成功，结束当前Activity
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
}
