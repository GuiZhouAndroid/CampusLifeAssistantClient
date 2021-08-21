package work.lpssfxy.www.campuslifeassistantclient.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        startActivity(new Intent(GuideActivity.this, WelcomeActivity.class)); //执行跳转
        overridePendingTransition(R.anim.activity_common_anim_out, R.anim.activity_common_anim_in);//跳转动画
        finish();//跳转成功，结束当前Activity
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
