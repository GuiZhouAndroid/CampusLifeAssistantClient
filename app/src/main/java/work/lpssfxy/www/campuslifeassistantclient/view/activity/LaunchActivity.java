package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtils;

/**
 * created by on 2021/8/22
 * 描述：启动页，你好同学！
 *
 * @author ZSAndroid
 * @create 2021-08-22-18:28
 */

public class LaunchActivity extends AppCompatActivity {
    /**
     * 防触碰使用的变量
     */
    private long firstTime;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 自动固定隐藏底部导航栏 :监听手势——上滑弹出=不失效*/
        StatusBarUtils.setAutoFixHideNaviCation(this);
        setContentView(R.layout.launch_activity);
        LaunchOneSecondCountDown();//开启线程
    }

    /**
     * 1秒倒计时，进入视频引导页
     */
    private void LaunchOneSecondCountDown() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseActivity.startActivityAnimInAndOut(LaunchActivity.this,new Intent(LaunchActivity.this, GuideActivity.class));
                finish();
            }
        }, 1000);
    }

    /**
     * 屏蔽物理返回按钮
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 销毁线程
     * 清除Message和Runnable资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
