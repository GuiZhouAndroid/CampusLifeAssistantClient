package work.lpssfxy.www.campuslifeassistantclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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
                startActivity(new Intent(LaunchActivity.this, GuideActivity.class));
                overridePendingTransition(R.anim.launch_fade_in, R.anim.launch_fade_out);
                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                    }
                }, 1000);
            }
        }, 1000);
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
