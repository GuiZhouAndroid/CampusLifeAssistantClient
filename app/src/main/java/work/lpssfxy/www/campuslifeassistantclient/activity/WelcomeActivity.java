package work.lpssfxy.www.campuslifeassistantclient.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.circleprogress.CircleProgress;

/**
 * created by on 2021/8/13
 * 描述：欢迎页
 *
 * @author ZSAndroid
 * @create 2021-08-13-5:03
 */
public class WelcomeActivity extends BaseActivity {
    //3秒倒计时
    private CircleProgress circleprogress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        //初始化View
        circleprogress = findViewById(R.id.circleprogress);
        //开始倒计时
        circleprogress.startCountDown();
        //点击跳过
        circleprogress.setAddCountDownListener(new CircleProgress.OnCountDownFinishListener() {
            @Override
            public void countDownFinished() {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
                overridePendingTransition(R.anim.anim_out, R.anim.anim_in);
            }
        });
    }
}
