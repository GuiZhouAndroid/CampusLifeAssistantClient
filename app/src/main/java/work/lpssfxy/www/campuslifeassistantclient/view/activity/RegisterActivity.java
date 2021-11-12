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
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xuexiang.xutil.common.RegexUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.StringDialogCallback;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyRegexUtils;
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
            String text = s.toString();
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
                            textInputLayout.setError("身份证号格式不正确");
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
                            textInputLayout.setError("手机号格式不正确");
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
                            textInputLayout.setError("短信验证码格式不正确");
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
            String text = s.toString();
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
                closeKeyboard();//隐藏软键盘
                break;
            case R.id.edit_register_sex://点击选择性别
                chooseSex();
                break;
            case R.id.ptn_register://点击注册账户
                doUserRegisterAccount();
                break;
        }
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
            }
        });
        new XPopup.Builder(RegisterActivity.this)
                .asCustom(popup)
                .show();
    }

    /**
     * 注册账户：非空判断输入的注册信息
     */
    private void doUserRegisterAccount() {
        /* 1. 判断信息格式 */
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
        // 身份证号
        if (TextUtils.isEmpty(strIdCard)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_id_card, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证真实姓名
            if (!MyRegexUtils.checkIdCard(strIdCard)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "身份证号格式不正确！", Snackbar.LENGTH_LONG);
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
        if (TextUtils.isEmpty(strTel)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_tel, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证手机号码
            if (!MyRegexUtils.checkMobile(strTel)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "手机号格式不正确！", Snackbar.LENGTH_LONG);
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
        if (TextUtils.isEmpty(strPhoneCode)) {
            Snackbar snackbar = Snackbar.make(mRl_register_show, R.string.please_input_phone_code, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证短信验证码
            if (!MyRegexUtils.checkAllNumber(strPhoneCode)) {
                Snackbar snackbar = Snackbar.make(mRl_register_show, "短信验证码格式不正确！", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        //2. 获取手机验证码
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
    }
}
