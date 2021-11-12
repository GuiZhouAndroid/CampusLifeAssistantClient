package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.xuexiang.xutil.common.RegexUtils;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyRegexUtils;

/**
 * created by on 2021/11/11
 * 描述：用户注册--->性别使用选择器，因此不需要正则表达式 + 不需要动态判断提示信息
 *
 * @author ZSAndroid
 * @create 2021-11-11-8:47
 */

@SuppressLint("NonConstantResourceId")
public class RegisterActivity extends BaseActivity{
    /****************** 1.获取TextInputLayout父控件 *******************/

    /** 父布局 */
    @BindView(R2.id.rl_register_show) RelativeLayout mRl_register_show;
    /** 用户名输入控件 */
    @BindView(R2.id.til_input_register_username) TextInputLayout mTilRegisterUsername;
    /** 密码输入控件 */
    @BindView(R2.id.til_input_register_password) TextInputLayout mTilRegisterPassword;
    /** 真实姓名输入控件 */
    @BindView(R2.id.til_input_real_name) TextInputLayout mTilRegisterRealName;
    /** 身份证号输入控件 */
    @BindView(R2.id.til_input_register_id_card) TextInputLayout mTilRegisterIdCard;
    /** 学号输入控件 */
    @BindView(R2.id.til_input_register_stu_no) TextInputLayout mTilRegisterStuNo;
    /** 手机号输入控件 */
    @BindView(R2.id.til_input_register_tel) TextInputLayout mTilRegisterTel;
    /** QQ邮箱输入控件 */
    @BindView(R2.id.til_input_register_email) TextInputLayout mTilRegisterEmail;
    /** 短信验证码输入控件 */
    @BindView(R2.id.til_input_phone_register_code) TextInputLayout mTilRegisterPhoneCode;

    /****************** 2.获取EditText子控件 *******************/
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

    /**
     * 监听事件执行操作
     */
    @Override
    protected void initListener() {
        //输入框动态判断格式合法性，动态提示信息
        ifInputFormatHintMessage();
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

    /**
     * 输入框动态判断格式合法性，动态提示信息
     * charSequence为在你按键之前显示的字符串
     * start：表示为新字符串与charSequence开始出现差异的下标  count：表示原字符串的count个字符  after：表示将会被after个字符替换
     */
    private void ifInputFormatHintMessage() {
        //用户名
        mRegisterUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (!RegexUtils.isUsername(charSequence.toString())) {
                    mTilRegisterUsername.setError("用户名格式不正确！");
                    mTilRegisterUsername.setErrorEnabled(true);
                } else {
                    mTilRegisterUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //密码
        mRegisterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!MyRegexUtils.checkMobile(String.valueOf(charSequence))) {
                    mTilRegisterPassword.setError("密码不正确！");
                    mTilRegisterPassword.setErrorEnabled(true);
                } else {
                    mTilRegisterPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        /* 1.非空判断输入的注册信息*/
        strUsername = mRegisterUsername.getText().toString();//用户名
        strPassword = mRegisterPassword.getText().toString();//密码
        strRealName = mRegisterRealName.getText().toString();//真实姓名
        strSex = mRegisterSex.getText().toString();//性别
        strIdCard = mRegisterIdCard.getText().toString();//身份证号
        strStuNo = mRegisterStuNo.getText().toString();//学号
        strTel = mRegisterTel.getText().toString();//手机号
        strEmail = mRegisterEmail.getText().toString();//QQ邮箱
        strPhoneCode = mRegisterPhoneCode.getText().toString();//短信验证码
        /* 2.非空判断输入的注册信息*/

        // 用户名
        if(TextUtils.isEmpty(strUsername)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_user_name, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //范围为“a-z”，“a-z”，“0-9”，“_”，“中文字符”不能以“_”结尾长度在6 ~ 20之间
            if (!RegexUtils.isUsername(strUsername)){
                Snackbar snackbar = Snackbar.make(mRl_register_show, "请输入范围为“a-z”，“a-z”，“0-9”，“_”，“中文字符”不能以“_”结尾长度在6 ~ 20之间", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 密码
        if(TextUtils.isEmpty(strPassword)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_password, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //必须包含一个大写，一个小写字母，且长度为8到16位
            if (!MyRegexUtils.checkPassword(strPassword)){
                Snackbar snackbar = Snackbar.make(mRl_register_show, "必须包含一个大写，一个小写字母，且长度为8到16位", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 真实姓名
        if(TextUtils.isEmpty(strRealName)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_RealUsername, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //不能有特殊字符和数字,可以输入英文，可以有空格，可以输入英文名字中的点,可以输入汉字，中文英文不能同时出现，长度在1-10
            if (!MyRegexUtils.checkRealName(strRealName)){
                Snackbar snackbar = Snackbar.make(mRl_register_show, "真实姓名格式不正确", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 身份证号
        if(TextUtils.isEmpty(strIdCard)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_id_card, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //匹配长度为15位或者18位
            if (!MyRegexUtils.checkIdCard(strIdCard))  {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "身份证号格式不正确", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 学号
        if(TextUtils.isEmpty(strStuNo)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_stu_no, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //只能输入由数字或字母组成的20位字符串,不区分大小写
            if (!MyRegexUtils.checkEnglishAndNumber(strStuNo))  {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "学号格式不正确", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 手机号
        if(TextUtils.isEmpty(strTel)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_tel, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //验证手机号码
            if (!MyRegexUtils.checkMobile(strTel))  {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "手机号格式不正确", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // QQ邮箱
        if(TextUtils.isEmpty(strEmail)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_qq_email, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //验证QQ邮箱
            if (!MyRegexUtils.checkEmail(strEmail))  {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "QQ邮箱格式不正确", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 短信验证码
        if(TextUtils.isEmpty(strPhoneCode)){
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_phone_code, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }else {
            //验证QQ邮箱
            if (!MyRegexUtils.checkAllNumber(strPhoneCode))  {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "短信验证码格式不正确", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        //3.正则表达式校验格式合法性
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
    }
}
