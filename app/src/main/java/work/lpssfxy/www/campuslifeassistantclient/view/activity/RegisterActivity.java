package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.utils.CountDownButtonHelper;
import com.xuexiang.xutil.common.RegexUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.CustomAlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.KeyboardUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyRegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/11/11
 * 描述：用户注册--->性别使用选择器，因此不需要正则表达式 + 不需要动态判断提示信息
 *
 * @author ZSAndroid
 * @create 2021-11-11-8:47
 */

@SuppressLint("NonConstantResourceId")
public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    /****************** 1.获取TextInputLayout父控件 *******************/
    /** 父布局 */
    @BindView(R2.id.rl_register_show) RelativeLayout mRl_register_show;
    /** 用户名输入控件 */
    @BindView(R2.id.til_input_register_username) TextInputLayout mTilRegisterUsername;
    /** 密码输入控件 */
    @BindView(R2.id.til_input_register_password) TextInputLayout mTilRegisterPassword;
    /** 真实姓名输入控件 */
    @BindView(R2.id.til_input_real_name) TextInputLayout mTilRegisterRealName;
    /** 性别输入控件 */
    @BindView(R2.id.til_input_register_sex) TextInputLayout mTilRegisterSex;
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
    /** XUI倒计时Button帮助类 */
    private CountDownButtonHelper buttonHelper;
    /** 注册成功 3S倒计时 */
    private DialogPrompt dialogPrompt;

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
     * afterTextChanged ： 获取输入框的信息值
     * onTextChanged　：　判断输入后的信息值，执行正则表达式判断合法性
     */
    private void ifInputFormatHintMessage() {
        //用户名
        mRegisterUsername.addTextChangedListener(new MyTextWatcher(mRegisterUsername, mTilRegisterUsername));
        //密码
        mRegisterPassword.addTextChangedListener(new MyTextWatcher(mRegisterPassword, mTilRegisterPassword));
        //真实姓名
        mRegisterRealName.addTextChangedListener(new MyTextWatcher(mRegisterRealName, mTilRegisterRealName));
        //身份证号
        mRegisterIdCard.addTextChangedListener(new MyTextWatcher(mRegisterIdCard, mTilRegisterIdCard));
        //学号
        mRegisterStuNo.addTextChangedListener(new MyTextWatcher(mRegisterStuNo, mTilRegisterStuNo));
        //手机号
        mRegisterTel.addTextChangedListener(new MyTextWatcher(mRegisterTel, mTilRegisterTel));
        //QQ邮箱
        mRegisterEmail.addTextChangedListener(new MyTextWatcher(mRegisterEmail, mTilRegisterEmail));
        //短信验证码
        mRegisterPhoneCode.addTextChangedListener(new MyTextWatcher(mRegisterPhoneCode, mTilRegisterPhoneCode));
    }

    /**
     * 内部类实现TextWatcher
     */
    private class MyTextWatcher implements TextWatcher {

        private final EditText editText;
        private final TextInputLayout textInputLayout;

        private MyTextWatcher(EditText editText, TextInputLayout textInputLayout) {
            this.editText = editText;
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //s  输入框中改变前的字符串信息
            //start 输入框中改变前的字符串的起始位置
            //count 输入框中改变前后的字符串改变数量一般为0
            //after 输入框中改变后的字符串与起始位置的偏移量
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //s  输入框中改变后的字符串信息
            //start 输入框中改变后的字符串的起始位置
            //before 输入框中改变前的字符串的位置 默认为0
            //count 输入框中改变后的一共输入字符串的数量
            String text = s.toString().trim();
            switch (editText.getId()) {
                case R.id.edit_register_username:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("用户名必须填写喔");
                    } else {
                        if (!RegexUtils.isUsername(text)) {
                            //开启提示
                            textInputLayout.setErrorEnabled(true);
                            //错误消息
                            textInputLayout.setError("用户名格式不正确！请输入6~20位任意数字、字母或下划线'_'，且不以下划线'_'结尾");
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_password:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("密码必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkPassword(text)) {
                            textInputLayout.setError("密码格式不正确！请输入8~16位且必须包含1个大写和1个小写字母");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_real_name:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("真实姓名必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkRealName(text)) {
                            textInputLayout.setError("真实姓名格式不正确");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_id_card:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("身份证号必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkIdCard(text)) {
                            textInputLayout.setError("请输入15位或18位身份证号码");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_stu_no:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("学号必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkEnglishAndNumber(text)) {
                            textInputLayout.setError("学号格式不正确！请输入10-20位由数字或字母，不区分大小写");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_tel:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("手机号必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkMobile(text)) {
                            textInputLayout.setError("请输入11位数的手机号");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_email:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("QQ邮箱必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkEmail(text)) {
                            textInputLayout.setError("QQ邮箱格式不正确");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
                case R.id.edit_register_phone_code:
                    if (text.isEmpty()) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("短信验证码必须填写喔");
                    } else {
                        if (!MyRegexUtils.checkAllNumber(text)) {
                            textInputLayout.setError("请输入6位数的短信验证码");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            textInputLayout.setErrorEnabled(false);
                        }
                    }
                    break;
            }
        }

        /**
         * 监听输入框最后的信息赋值给全局变量，提供注册信息值
         *
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {
            //s 输入结束呈现在输入框中的信息
            String text = s.toString().trim();
            switch (editText.getId()) {
                case R.id.edit_register_username:
                    strUsername = text;
                    break;
                case R.id.edit_register_password:
                    strPassword = text;
                    break;
                case R.id.edit_register_real_name:
                    strRealName = text;
                    break;
                case R.id.edit_register_id_card:
                    strIdCard = text;
                    break;
                case R.id.edit_register_stu_no:
                    strStuNo = text;
                    break;
                case R.id.edit_register_tel:
                    strTel = text;
                    break;
                case R.id.edit_register_email:
                    strEmail = text;
                    break;
                case R.id.edit_register_phone_code:
                    strPhoneCode = text;
                    break;
            }
        }
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.btn_register_phone_code, R2.id.ptn_register,R2.id.edit_register_sex})
    public void onRegisterViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_phone_code://点击获取验证码
                RegexRequestSMSPhoneCode(strTel);//正则表达式判断 + 60秒倒计时 + 请求短信验证码
                KeyboardUtil.closeKeyboard(RegisterActivity.this);//关闭软键盘
                break;
            case R.id.edit_register_sex://点击选择性别
                chooseSex();
                break;
            case R.id.ptn_register://点击注册账户
                RegexVerifySMSPhoneCodeRegisterInfo(strTel,strPhoneCode);//正在判断信息合法性
                break;
        }
    }

    /**
     * 正则表达式判断 + 60秒倒计时 + 请求短信验证码
     * @param phoneNumber 手机号
     */
    private void RegexRequestSMSPhoneCode(String phoneNumber) {
        /* 1. 正则判断信息格式 */
        // 用户名
        if (TextUtils.isEmpty(strUsername)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_user_name, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证用户名格式
            if (!RegexUtils.isUsername(strUsername)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "用户名格式不正确！请输入6~20位任意数字、字母或下划线'_'，且不以下划线'_'结尾", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 密码
        if (TextUtils.isEmpty(strPassword)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_password, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证密码格式
            if (!MyRegexUtils.checkPassword(strPassword)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "密码格式错误！请输入8~16位且必须包含1个大写和1个小写字母", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 真实姓名
        if (TextUtils.isEmpty(strRealName)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_RealUsername, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证真实姓名
            if (!MyRegexUtils.checkRealName(strRealName)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "真实姓名格式不正确！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        //性别
        if (TextUtils.isEmpty(strSex)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_Sex, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        // 身份证号
        if (TextUtils.isEmpty(strIdCard)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_id_card, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证真实姓名
            if (!MyRegexUtils.checkIdCard(strIdCard)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "请输入15位或18位身份证号码！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 学号
        if (TextUtils.isEmpty(strStuNo)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_stu_no, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证学号
            if (!MyRegexUtils.checkEnglishAndNumber(strStuNo)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "学号格式错误！请输入10-20位由数字或字母，不区分大小写！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 手机号
        if (TextUtils.isEmpty(phoneNumber)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_tel, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证手机号码
            if (!MyRegexUtils.checkMobile(phoneNumber)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "请输入11位数的手机号！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // QQ邮箱
        if (TextUtils.isEmpty(strEmail)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_qq_email, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证QQ邮箱
            if (!MyRegexUtils.checkEmail(strEmail)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "QQ邮箱格式不正确！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        /* 2. 60秒倒计时Button */
        buttonHelper = new CountDownButtonHelper(mBtnRegisterPhoneCode,60,1);
        buttonHelper.setOnCountDownListener(new CountDownButtonHelper.OnCountDownListener() {
            /* 1.倒计时中 */
            @Override
            public void onCountDown(int time) {
                mBtnRegisterPhoneCode.setText("重新获取("+time+"秒)");
            }

            /* 2.结束后 */
            @Override
            public void onFinished() {
                mBtnRegisterPhoneCode.setText("获取验证码");
            }
        });
        //启动60秒倒计时
        buttonHelper.start();
        /* 3. 请求获取手机验证码。第一个参数：手机号　第二个参数：Bmob官方控制台短信模板 */
        BmobSMS.requestSMSCode(phoneNumber, "注册", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Snackbar snackbar = Snackbar.make(mRl_register_show, "验证码发送成功！"+strRealName+"同学，请您注意查收噢~", Snackbar.LENGTH_LONG);
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar snackbar = Snackbar.make(mRl_register_show, "验证码发送失败！系统延迟或频繁获取验证码导致，请稍候再试！"+e.toString(), Snackbar.LENGTH_LONG);
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                }
            }
        });
    }

    /**
     * 自定义选择器：性别
     */
    private void chooseSex() {
        CommonPickerPopup popup = new CommonPickerPopup(RegisterActivity.this);
        ArrayList<String> list = new ArrayList<String>();
        list.add("男");
        list.add("女");
        //默认选择男
        popup.setPickerData(list).setCurrentItem(0);
        popup.setCommonPickerListener(new CommonPickerListener() {
            @Override
            public void onItemSelected(int index, String RegisterSex) {
                mRegisterSex.setText(RegisterSex);//设置文本为新性别
                strSex = RegisterSex;
            }
        });
        new XPopup.Builder(RegisterActivity.this)
                .asCustom(popup)
                .show();
    }

    /**
     * 开始注册：正在表达式验证 + 验证输入短信验证码 + 网络请求调用注册接口
     * @param phoneNumber 待验证的手机号
     * @param phoneCode 短信验证码
     */
    private void RegexVerifySMSPhoneCodeRegisterInfo(String phoneNumber, String phoneCode) {
        /* 1. 正则判断信息格式 */
        // 用户名
        if (TextUtils.isEmpty(strUsername)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_user_name, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证用户名格式
            if (!RegexUtils.isUsername(strUsername)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "用户名格式不正确！请输入6~20位任意数字、字母或下划线'_'，且不以下划线'_'结尾", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 密码
        if (TextUtils.isEmpty(strPassword)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_password, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证密码格式
            if (!MyRegexUtils.checkPassword(strPassword)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "密码格式错误！请输入8~16位且必须包含1个大写和1个小写字母", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 真实姓名
        if (TextUtils.isEmpty(strRealName)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_RealUsername, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证真实姓名
            if (!MyRegexUtils.checkRealName(strRealName)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "真实姓名格式不正确！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        //性别
        if (TextUtils.isEmpty(strSex)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_Sex, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        // 身份证号
        if (TextUtils.isEmpty(strIdCard)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_id_card, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证真实姓名
            if (!MyRegexUtils.checkIdCard(strIdCard)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "请输入15位或18位身份证号码！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 学号
        if (TextUtils.isEmpty(strStuNo)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_stu_no, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证学号
            if (!MyRegexUtils.checkEnglishAndNumber(strStuNo)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "学号格式错误！请输入10-20位由数字或字母，不区分大小写！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 手机号
        if (TextUtils.isEmpty(phoneNumber)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_tel, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证手机号码
            if (!MyRegexUtils.checkMobile(phoneNumber)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "请输入11位数的手机号！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // QQ邮箱
        if (TextUtils.isEmpty(strEmail)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_qq_email, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证QQ邮箱
            if (!MyRegexUtils.checkEmail(strEmail)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "QQ邮箱格式不正确！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 短信验证码
        if (TextUtils.isEmpty(phoneCode)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_phone_code, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证短信验证码
            if (!MyRegexUtils.checkAllNumber(phoneCode)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "请输入6位数的短信验证码！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        /* 2. 注册前弹窗提示 */
        new CustomAlertDialog(this)
                .builderBottom()
                .setCancelable(true)
                .setTitle("注册须知")
                .setTitleTextBold(true)
                .setMsg("为了您正常使用本APP，请务必核实本人真实信息，真实姓名+性别+身份证号+学号，一旦注册将无法修改，请知悉，继续注册视为同意本条例。\t个人账户问题，请联系开发者！联系方式：QQ(1210762604)，微信(ZS_Android)\t联系请注明来意，感谢支持~")
                //.setMsgTextBold(true)
                .setOkButton("同意注册", 0, "", "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* 3. 动画UI按钮调用短信验证逻辑 */
                        mPtnRegister.startAnim();
                        Message obtainMessage = mHandler.obtainMessage();
                        mHandler.sendMessageDelayed(obtainMessage, 1500);
                    }
                })
                .setCancleButton("溜了溜了", 0, "", "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar = Snackbar.make(mRl_register_show, "记得常来看我喔~祝您生活愉快！", Snackbar.LENGTH_LONG);
                        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                        RegisterActivity.this.finish();
                    }
                })
                .show();
    }

    /**
     * 注册转场
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPtnRegister.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    /* 6. 验证短信验证码 */
                    BmobSMS.verifySmsCode(strTel, strPhoneCode, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                /* 7. 验证成功，传递注册参数调用注册API接口 */
                                starRegisterUserAccount(strUsername,strPassword,strRealName,strSex,strIdCard,strStuNo,strTel,strEmail);
                            } else {
                                Snackbar snackbar = Snackbar.make(mRl_register_show, "短信验证码不正确或已过期！", Snackbar.LENGTH_LONG);
                                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                snackbar.show();
                            }
                        }
                    });
                }
            });

        }
    };

    /**
     * 调用SpringBoot UserController中注册API接口，传递参数，进行账户注册
     * @param strUsername 用户名
     * @param strPassword 密码
     * @param strRealName 真实姓名
     * @param strSex 性别
     * @param strIdCard 身份证号
     * @param strStuNo 学号
     * @param strTel 手机号
     * @param strEmail QQ邮箱
     */
    private void starRegisterUserAccount(String strUsername, String strPassword, String strRealName, String strSex, String strIdCard, String strStuNo, String strTel, String strEmail) {
        OkGo.<String>post(Constant.REGISTER_USER_INFO + "/" + strUsername + "/" + strPassword + "/" + strRealName + "/" + strSex + "/" + strIdCard + "/" + strStuNo + "/" + strTel + "/" + strEmail)
                .tag("注册账户")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        XPopupUtils.getInstance().setShowDialog(RegisterActivity.this,"正在注册账户...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        Log.i(TAG, "注册账户: " + OkGoResponseBean);
                        /* 1. 注册失败时*/
                        if (200 == OkGoResponseBean.getCode() && strUsername.equals(OkGoResponseBean.getData()) && "注册失败，此用户名已被注册".equals(OkGoResponseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this,"此用户名已被注册，那就重新取个好听吧~");
                            dialogPrompt.show();
                            return;
                        }
                        if (200 == OkGoResponseBean.getCode() && strIdCard.equals(OkGoResponseBean.getData()) && "注册失败，此身份证已被注册".equals(OkGoResponseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this,"此身份证已被注册，请使用本人身份证号进行注册！");
                            dialogPrompt.show();
                            return;
                        }
                        if (200 == OkGoResponseBean.getCode() && strStuNo.equals(OkGoResponseBean.getData()) && "注册失败，此学号已被注册".equals(OkGoResponseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this,"此学号已被注册，请使用本人的学号！");
                            dialogPrompt.show();
                            return;
                        }
                        if (200 == OkGoResponseBean.getCode() && strTel.equals(OkGoResponseBean.getData()) && "注册失败，此手机号已被注册".equals(OkGoResponseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this,"此手机号已被注册，请使用本人手机号进行注册！");
                            dialogPrompt.show();
                            return;
                        }

                        if (200 == OkGoResponseBean.getCode() && strEmail.equals(OkGoResponseBean.getData()) && "注册失败，此邮箱已被注册".equals(OkGoResponseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this,"此QQ邮箱已被注册，请使用本人QQ邮箱进行注册！");
                            dialogPrompt.show();
                            return;
                        }

                        if (200 == OkGoResponseBean.getCode() && strUsername.equals(OkGoResponseBean.getData()) && "注册失败".equals(OkGoResponseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(RegisterActivity.this,"注册失败，系统错误！请联系开发者解决~");
                            dialogPrompt.show();
                            return;
                        }
                        /* 2. 注册成功时*/
                        if (200 == OkGoResponseBean.getCode() && strUsername.equals(OkGoResponseBean.getData()) && "注册成功".equals(OkGoResponseBean.getMsg())){
                            dialogPrompt = new DialogPrompt(RegisterActivity.this, R.string.register_success, 3);
                            dialogPrompt.showAndFinish(RegisterActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("RegisterBackName", strRealName);
                            RegisterActivity.this.setResult(Constant.RESULT_CODE_REGISTER_ACCOUNT_SUCCESS, intent);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response,RegisterActivity.this, mRl_register_show, "请求错误，服务器连接失败！");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        XPopupUtils.getInstance().setSmartDisDialog();
                    }
                });
    }


    /**
     * 释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //倒计时验证码必须回收资源，避免造成线程UI问题，使APP崩溃
        if (buttonHelper != null) {
            buttonHelper.recycle();
        }
        if (dialogPrompt != null ){
            dialogPrompt.cancel();
        }
        if (mHandler !=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
