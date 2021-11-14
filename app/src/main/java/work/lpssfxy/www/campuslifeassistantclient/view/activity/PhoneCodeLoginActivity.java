package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import static com.tencent.connect.common.Constants.KEY_QRCODE;
import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.xuexiang.xui.utils.CountDownButtonHelper;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import work.lpssfxy.www.campuslifeassistantclient.App;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.SessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.SessionUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyRegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.coding.FileCodeUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/11/14
 * 描述：手机短信验证码登录
 *
 * @author ZSAndroid
 * @create 2021-11-14-18:29
 */
@SuppressLint("NonConstantResourceId")
public class PhoneCodeLoginActivity extends BaseActivity {
    private static final String TAG = "PhoneCodeLoginActivity";
    @BindView(R2.id.rl_login_show) RelativeLayout mRlLoginShow;
    /** Toolbar */
    @BindView(R2.id.toolbar_login_phone) Toolbar mLoginToolbar;
    /****************** 1.获取TextInputLayout父控件 *******************/
    /** 手机号输入控件 */
    @BindView(R2.id.til_input_login_tel) TextInputLayout mTilLoginTel;
    /** 短信验证码输入控件 */
    @BindView(R2.id.til_input_login_phone_code) TextInputLayout mTilLoginPhoneCode;
    /****************** 2.获取EditText子控件 *******************/
    /** 手机号 */
    @BindView(R2.id.edit_login_tel) EditText mLoginTel;
    /** 输入的短信验证码 */
    @BindView(R2.id.edit_login_phone_code) EditText mLoginPhoneCode;
    /** 点击获取验证码 */
    @BindView(R2.id.btn_login_phone_code) Button mBtnLoginPhoneCode;
    /** 手机快捷登录动画 */
    @BindView(R2.id.ptn_login_phone) ProgressButton mPtnTelLogin;
    /** 用户输入的手机号 + 短信验证码 */
    private String strPhoneNumber,strPhoneCode;
    /** XUI倒计时Button帮助类 */
    private CountDownButtonHelper buttonHelper;
    /** 自定义对话框 */
    private AlertDialog mDialog;

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
        return R.layout.login_activity_phone_code;
    }

    @Override
    protected void prepareData() {
        initPtnTelLoginState();
    }

    @Override
    protected void initView() {
        /**判断Toolbar，开启主图标并显示title*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        /** 设置Toolbar */
        setSupportActionBar(mLoginToolbar);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {
        //手机号
        mLoginTel.addTextChangedListener(new PhoneCodeLoginActivity.MyTextWatcher(mLoginTel, mTilLoginTel));
        //短信验证码
        mLoginPhoneCode.addTextChangedListener(new PhoneCodeLoginActivity.MyTextWatcher(mLoginPhoneCode, mTilLoginPhoneCode));
    }

    @Override
    protected void doBusiness() {

    }

    /**
     * 初始化手机快捷登录按钮状态动画
     */
    public void initPtnTelLoginState() {
        mPtnTelLogin.setEnabled(true);
        mPtnTelLogin.setBgColor(Color.RED);
        mPtnTelLogin.setTextColor(Color.WHITE);
        mPtnTelLogin.setProColor(Color.WHITE);
        mPtnTelLogin.setButtonText("快 捷 登 录");
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.btn_login_phone_code, R2.id.ptn_login_phone})
    public void onLoginViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_phone_code://点击获取验证码
                strPhoneNumber = mLoginTel.getText().toString().trim();
                strPhoneCode = mLoginPhoneCode.getText().toString().trim();
                RegexRequestSMSPhoneNumber(strPhoneNumber);//正则表达式判断手机号 + 60秒倒计时 + 请求短信验证码
                closeKeyboard();//隐藏软键盘
                break;
            case R.id.ptn_login_phone://点击开始手机号登录
                RegexVerifySMSPhoneCodeToLogin(strPhoneNumber, strPhoneCode);
                break;
        }
    }

    /**
     * 点击获取验证码
     *
     * @param strPhoneNumber 手机号
     */
    private void RegexRequestSMSPhoneNumber(String strPhoneNumber) {
        /* 1.验证信息格式*/
        // 手机号
        if (TextUtils.isEmpty(strPhoneNumber)) {
            Snackbar snackbar = Snackbar.make(mRlLoginShow, R.string.please_input_tel, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证手机号码
            if (!MyRegexUtils.checkMobile(strPhoneNumber)) {
                Snackbar snackbar = Snackbar.make(mRlLoginShow, "请输入11位数的手机号", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        /* 2. 60秒倒计时Button */
        buttonHelper = new CountDownButtonHelper(mBtnLoginPhoneCode, 60, 1);
        buttonHelper.setOnCountDownListener(new CountDownButtonHelper.OnCountDownListener() {
            /* 1.倒计时中 */
            @Override
            public void onCountDown(int time) {
                mBtnLoginPhoneCode.setText("重新获取(" + time + "秒)");
            }

            /* 2.结束后 */
            @Override
            public void onFinished() {
                mBtnLoginPhoneCode.setText("获取验证码");
            }
        });
        //启动60秒倒计时
        buttonHelper.start();
        /* 3. 请求获取手机验证码。第一个参数：手机号　第二个参数：Bmob官方控制台短信模板 */
        BmobSMS.requestSMSCode(strPhoneNumber, "", new QueryListener<Integer>() {
            @Override
            public void done(Integer smsId, BmobException e) {
                if (e == null) {
                    Snackbar snackbar = Snackbar.make(mBtnLoginPhoneCode, "短信验证码发送成功到" + strPhoneNumber + "，请您注意查收噢~", Snackbar.LENGTH_LONG);
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    Toast.makeText(PhoneCodeLoginActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar snackbar = Snackbar.make(mBtnLoginPhoneCode, "短信验证码发送失败！系统延迟或频繁获取验证码导致，请稍候再试！" + e.toString(), Snackbar.LENGTH_LONG);
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                }
            }
        });
    }

    private void RegexVerifySMSPhoneCodeToLogin(String strPhoneNumber, String strPhoneCode) {
        /* 1.验证信息格式*/
        // 手机号
        if (TextUtils.isEmpty(strPhoneNumber)) {
            Snackbar snackbar = Snackbar.make(mRlLoginShow, R.string.please_input_tel, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证手机号码
            if (!MyRegexUtils.checkMobile(strPhoneNumber)) {
                Snackbar snackbar = Snackbar.make(mRlLoginShow, "请输入11位数的手机号", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        // 短信验证码
        if (TextUtils.isEmpty(strPhoneCode)) {
            Snackbar snackbar = Snackbar.make(mRlLoginShow, R.string.please_input_phone_code, Snackbar.LENGTH_LONG);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        } else {
            //验证短信验证码
            if (!MyRegexUtils.checkAllNumber(strPhoneCode)) {
                Snackbar snackbar = Snackbar.make(mRlLoginShow, "请输入6位数的短信验证码", Snackbar.LENGTH_LONG);
                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
                return;
            }
        }
        /* 2.调用登录动画UI，倒计时1.5秒开始执行手机一键登录API接口*/
        mPtnTelLogin.startAnim();
        Message obtainMessage = mHandler.obtainMessage();
        mHandler.sendMessageDelayed(obtainMessage, 1500);
    }

    /**
     * 注册转场
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPtnTelLogin.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    /* 3. 验证短信验证码 */
                    BmobSMS.verifySmsCode(strPhoneNumber, strPhoneCode, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                /* 4. 验证成功，传递注册参数调用注册API接口 */
                                starPhoneFastLoginUserAccount(strPhoneNumber);
                            } else {
                                Snackbar snackbar = Snackbar.make(mRlLoginShow, "短信验证码不正确或已过期！", Snackbar.LENGTH_LONG);
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
     * 开始执行手机快捷登录，调用SpringBoot登录API接口
     *
     * @param strPhoneNumber SpringBoot后端待验证的手机号
     */
    private void starPhoneFastLoginUserAccount(String strPhoneNumber) {
        OkGo.<String>post(Constant.LOGIN_FAST_TEL)
                .tag("手机号快捷登录")
                .params("ulTel", strPhoneNumber)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        XPopupUtils.setShowDialog(PhoneCodeLoginActivity.this, "正在验证账户...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        UserQQSessionBean userQQSessionBean = GsonUtil.gsonToBean(response.body(), UserQQSessionBean.class);
                        Log.i(TAG, "onSuccessQQ一键登录==: " + userQQSessionBean);
                        if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "此账户处于封禁状态".equals(userQQSessionBean.getMsg())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneCodeLoginActivity.this);
                            builder.setTitle("登录提示")//这里设置标题
                                    .setMessage("当前手机号关联账户处于封禁状态，去主页查询封禁详情")
                                    .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                                    .setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OkGo.getInstance().cancelTag("手机号快捷登录");//跳转前取消本次请求
                                            mDialog.dismiss();
                                        }
                                    });
                            mDialog = builder.create();
                            mDialog.show();
                            return;
                        }
                        if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "登录失败，此手机号未绑定账户".equals(userQQSessionBean.getMsg())) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(PhoneCodeLoginActivity.this);
                            builder1.setTitle("登录提示")//这里设置标题
                                    .setMessage("当前手机号还未绑定账户~" + getString(R.string.please_bind_back_User_login))
                                    .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                                    .setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OkGo.getInstance().cancelTag("手机号快捷登录");//跳转前取消本次请求
                                            mDialog.dismiss();
                                        }
                                    });
                            mDialog = builder1.create();
                            mDialog.show();
                            return;
                        }
                        if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "登录失败，此手机号未绑定QQ".equals(userQQSessionBean.getMsg())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhoneCodeLoginActivity.this);
                            builder.setTitle("登录提示")//这里设置标题
                                    .setMessage("当前手机号还未绑定QQ~" + getString(R.string.please_bind_back_qq_login))
                                    .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                                    .setPositiveButton("朕知道了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OkGo.getInstance().cancelTag("手机号快捷登录");//跳转前取消本次请求
                                            mDialog.dismiss();
                                        }
                                    });
                            mDialog = builder.create();
                            mDialog.show();
                            return;
                        }
                        if (200 == userQQSessionBean.getCode() && null != userQQSessionBean.getData() && "登录成功".equals(userQQSessionBean.getMsg())) {
                            //拉去并联登录信息持久化
                            if (App.appActivity != null) {
                                App.appActivity.finish();//销毁主页
                            }
                            if (LoginActivity.getInstance() != null) {
                                LoginActivity.getInstance().finish();//登录主页单例模式上下文，登录成功，销毁登录页
                            }
                            SharePreferenceUtil.putObject(PhoneCodeLoginActivity.this, userQQSessionBean);
                            startActivityAnimRightToLeft(new Intent(PhoneCodeLoginActivity.this, IndexActivity.class));
                            PhoneCodeLoginActivity.this.finish();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //服务器连接失败
                        DialogPrompt dialogPrompt = new DialogPrompt(PhoneCodeLoginActivity.this, "服务器连接失败：" + response.getException());
                        dialogPrompt.show();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        XPopupUtils.setSmartDisDialog();
                    }
                });
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

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String text = s.toString().trim();
            switch (editText.getId()) {
                case R.id.edit_login_tel:
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
                case R.id.edit_login_phone_code:
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
                case R.id.edit_login_tel:
                    strPhoneNumber = text;
                    break;
                case R.id.edit_login_phone_code:
                    strPhoneCode = text;
                    break;
            }
        }
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
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}
