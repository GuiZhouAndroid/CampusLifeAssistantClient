package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;

/**
 * created by on 2021/11/11
 * 描述：用户注册
 *
 * @author ZSAndroid
 * @create 2021-11-11-8:47
 */

@SuppressLint("NonConstantResourceId")
public class RegisterActivity extends BaseActivity{
    /** 父布局 */
    @BindView(R2.id.rl_register_show) RelativeLayout mRl_register_show;
//    /** 用户名输入控件 */
//    @BindView(R2.id.til_login_username) TextInputLayout mTil_login_username;
//    /** 密码输入控件 */
//    @BindView(R2.id.til_login_password) TextInputLayout mTil_login_password;

    /** 用户名 */
    @BindView(R2.id.edit_register_username) EditText mRegisterUsername;
    /** 密码 */
    @BindView(R2.id.edit_register_password) EditText mRegisterPassword;
    /** 真实姓名 */
    @BindView(R2.id.edit_register_real_name) EditText mRegisterRealName;
    /** 性别 */
    @BindView(R2.id.edit_register_sex) EditText mRegisterSex;
    /** 身份证号 */
    @BindView(R2.id.edit_register_id_card) EditText mRegisterIdCard;
    /** 学号 */
    @BindView(R2.id.edit_register_stu_no) EditText mRegisterStuNo;
    /** 手机号 */
    @BindView(R2.id.edit_register_tel) EditText mRegisterTel;
    /** QQ邮箱 */
    @BindView(R2.id.edit_register_email) EditText mRegisterEmail;
    /** 输入的短信验证码 */
    @BindView(R2.id.edit_register_phone_code) EditText mRegisterPhoneCode;
    /** 点击获取验证码 */
    @BindView(R2.id.btn_register_phone_code) Button mBtnRegisterPhoneCode;
    /** 注册动画 */
    @BindView(R2.id.ptn_register) ProgressButton mPtnRegister;
    /** 注册输入的信息值 */
    private String strUsername,strPassword,strRealName,strSex,strIdCard,strStuNo,strTel,strEmail,strPhoneCode;
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
        return false;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return false;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    public int bindLayout() {
        return R.layout.register_activity;
    }

    @Override
    protected void prepareData() {
        initPtnRegisterState();//初始化注册按钮状态动画
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

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
     * 初始化注册按钮状态动画
     */
    public void initPtnRegisterState() {
        mPtnRegister.setEnabled(true);
        mPtnRegister.setBgColor(Color.RED);
        mPtnRegister.setTextColor(Color.WHITE);
        mPtnRegister.setProColor(Color.WHITE);
        mPtnRegister.setButtonText("注  册");
    }

    @OnClick({R2.id.btn_register_phone_code, R2.id.ptn_register})
    public void onRegisterViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_phone_code://点击获取验证码
                closeKeyboard();//隐藏软键盘
                break;
            case R.id.ptn_register://点击注册账户
                doUserRegisterAccount();
                break;
        }
    }

    /**
     * 注册账户
     */
    private void doUserRegisterAccount() {

    }
}
