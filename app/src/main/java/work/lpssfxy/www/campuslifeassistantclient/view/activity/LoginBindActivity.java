package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.StringDialogCallback;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;

/**
 * created by on 2021/10/27
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-27-10:22
 */
@SuppressLint("NonConstantResourceId")
public class LoginBindActivity extends BaseActivity{
    @BindView(R2.id.tv_qq) TextView textView;
    /** 绑定用户名 */
    @BindView(R2.id.edit_bind_username) EditText mEdit_bind_username;
    /** 绑定密码 */
    @BindView(R2.id.edit_bind_password) EditText mEdit_bind_password;
    /** 立即绑定QQ */
    @BindView(R2.id.btn_start_bind) Button mBtn_start_bind;

    private static final String TAG = "LoginBindActivity";
    private String strQQJsonData,strEditUsername,strEditPassword;//QQ会话数据、用户名数据、密码数据

    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationState() {
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return true;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    public int bindLayout() {
        return R.layout.login_activity_bind;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void prepareData() {
        strQQJsonData = getIntent().getStringExtra("QQJsonData");//登录页拉起QQ授权传递QQ会话的Json数据
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        textView.setText(getIntent().getStringExtra("QQJsonData"));
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {

    }

    /**
     * QQ绑定用户信息
     * @param view
     */
    @OnClick({R2.id.btn_start_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_bind: //点击立即绑定
                strEditUsername = mEdit_bind_username.getText().toString().trim();//获取输入的用户名值
                strEditPassword = mEdit_bind_password.getText().toString().trim();//获取输入的密码值
                doStartQQBindUser(strEditUsername,strEditPassword);
                break;
        }
    }

    /**
     * 开始立即绑定业务逻辑
     * @param strEditUsername 用户名
     * @param strEditPassword 密码
     */
    private void doStartQQBindUser(String strEditUsername, String strEditPassword) {
        OkGo.<String>post(Constant.LOGIN_USERNAME_PASSWORD)
                .tag(this)
                .params("ulUsername",strEditUsername).params("ulPassword",strEditPassword)
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //注意这里已经是在主线程了
                        String data = response.body();//这个就是返回来的结果
                        Log.i(TAG, "onSuccess: "+data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }
}
