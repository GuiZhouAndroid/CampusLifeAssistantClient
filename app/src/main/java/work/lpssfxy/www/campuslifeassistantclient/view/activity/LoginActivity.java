package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import static com.tencent.connect.common.Constants.KEY_QRCODE;
import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.Constants;
import com.tencent.open.log.SLog;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUser;
import work.lpssfxy.www.campuslifeassistantclient.utils.RegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.coding.FileCodeUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.permission.PermissionMgr;

/**
 * created by on 2021/9/2
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-09-02-18:21
 */
@SuppressLint("NonConstantResourceId")
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    /** 父布局 */
    @BindView(R2.id.login_rl_show) RelativeLayout mLogin_rl_show;
    /** 用户名输入控件 */
    @BindView(R2.id.til_login_username) TextInputLayout mTil_login_username;
    /** 密码输入控件 */
    @BindView(R2.id.til_login_password) TextInputLayout mTil_login_password;
    /** 用户名 */
    @BindView(R2.id.login_edit_username) EditText mLogin_edit_username;
    /** 密码 */
    @BindView(R2.id.login_edit_password) EditText mLogin_edit_password;
    /** 忘记密码 */
    @BindView(R2.id.login_tv_forget_pwd) TextView mLogin_tv_forget_pwd;
    /** 登录动画 */
    @BindView(R2.id.login_ptn_anim) ProgressButton mLogin_ptn_anim;
    /** 去注册 */
    @BindView(R2.id.login_tv_go_register) TextView mLogin_tv_go_register;
    /** 微信登录 */
    @BindView(R2.id.login_iv_wx) ImageView mLogin_iv_wx;
    /** QQ登录 */
    @BindView(R2.id.login_iv_qq) ImageView mLogin_iv_qq;
    /** 强制扫码登录 */
    @BindView(R2.id.check_force_qr) CheckBox mCheckForceQr;
    /** 输入的用户名密码 */
    private String strUserName,strPassword;
    /** QQ互联SDK实例 */
    public static Tencent mTencent;
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
        return R.layout.login_activity;
    }

    @Override
    protected void prepareData() {
        Log.i(TAG, "-->onCreate结束+腾讯实例创建成功 ");
        mTencent = Tencent.createInstance("101965703", LoginActivity.this, "work.lpssfxy.www.campuslifeassistantclient.fileprovider");
        if (mTencent == null) {
            SLog.e(TAG, "腾讯实例创建失败");
            finish();
        }
        /** QQ登录权限申请*/
        PermissionMgr.getInstance().requestPermissions(this);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        /** 初始化动画登录按钮属性 */
        mLogin_ptn_anim.setBgColor(Color.RED);
        mLogin_ptn_anim.setTextColor(Color.WHITE);
        mLogin_ptn_anim.setProColor(Color.WHITE);
        mLogin_ptn_anim.setButtonText("登  录");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {
        mLogin_edit_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!RegexUtils.checkMobile(String.valueOf(charSequence))){
                    mTil_login_username.setError("手机号码格式不正确！");
                    mTil_login_username.setErrorEnabled(true);
                }else {
                    mTil_login_username.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mLogin_edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!RegexUtils.checkMobile(String.valueOf(charSequence))){
                    mTil_login_password.setError("手机号码格式不正确！");
                    mTil_login_password.setErrorEnabled(true);
                }else {
                    mTil_login_password.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 请求权限结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionMgr.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    @OnClick({R2.id.login_ptn_anim,R2.id.login_tv_forget_pwd,R2.id.login_tv_go_register,R2.id.login_iv_wx,R2.id.login_iv_qq})
    public void onLoginViewClick(View view){
        switch (view.getId()){
            case R.id.login_ptn_anim://动画登录
                strUserName = mLogin_edit_username.getText().toString();
                strPassword = mLogin_edit_password.getText().toString();
                closeKeyboard();//隐藏软键盘
                userLogin(strUserName, strPassword);//用户名密码登录
                break;
            case R.id.login_tv_forget_pwd://忘记密码
                userFindPassword();//找回密码
                break;
            case R.id.login_tv_go_register://去注册
                userGoRegisterAccount();//跳转注册
                break;
            case R.id.login_iv_wx://微信登录
                weChatLogin();//调用微信的SDK
                break;
            case R.id.login_iv_qq://QQ登录
                QQLogin();//调用QQ的SDK
                break;
        }
    }

    /**
     * 用户名密码判断登录
     */
    private void userLogin(String strUserName, String strPassword) {
        if(strUserName.isEmpty()&&strPassword.isEmpty()){
            Snackbar snackbar = Snackbar.make(mLogin_rl_show,  R.string.please_input_user_name_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (strUserName.isEmpty()) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show,  R.string.please_input_user_name, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (strPassword.isEmpty()) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show,   R.string.please_input_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        mLogin_ptn_anim.startAnim();
        Message m=mHandler.obtainMessage();
        mHandler.sendMessageDelayed(m,1500);
    }

    /**
     * 找回密码
     */
    private void userFindPassword() {
        ToastUtil.showToast("找回密码");
    }

    /**
     * 跳转注册
     */
    private void userGoRegisterAccount() {
        ToastUtil.showToast("跳转注册");
    }
    /**
     * 微信登录
     */
    private void weChatLogin() {
        ToastUtil.showToast("微信登录");
    }
    /**
     * QQ登录
     */
    private void QQLogin() {
        if (!mTencent.isSessionValid()) {
            // 强制扫码登录
            this.getIntent().putExtra(AuthAgent.KEY_FORCE_QR_LOGIN, mCheckForceQr.isChecked());
            HashMap<String, Object> params = new HashMap<>();
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                params.put(KEY_RESTORE_LANDSCAPE, true);
            }
            params.put(KEY_SCOPE, "all");
            params.put(KEY_QRCODE, mCheckForceQr.isChecked());
            mTencent.login(this, loginListener, params);

        } else {
            mTencent.logout(this);
            mTencent.login(this, "all", loginListener);

            // 第三方也可以选择注销的时候不去清除第三方的targetUin/targetMiniAppId
            //saveTargetUin("");
            //saveTargetMiniAppId("");
//            updateUserInfo();
//            updateLoginButton();
        }
        Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            Log.d(TAG, "doComplete: " + values.toString());
            initOpenidAndToken(values);
            updateUserInfo();
//            updateLoginButton();
        }
    };

    private class BaseUiListener extends DefaultUiListener {

        @Override
        public void onComplete(Object response) {
//            if(arg0!=null){
//                JSONObject jsonObject = (JSONObject) arg0;
//                try {
//                    String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
//                    String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
//                    String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
//
//                } catch (JSONException e) {
//                    Log.d(TAG, "onComplete: 失败");
//                }
//            }
            Log.d(TAG, "onComplete:成功");
            if (null == response) {
                FileCodeUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                FileCodeUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            FileCodeUtil.showResultDialog(LoginActivity.this, response.toString(), "登录成功");

            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            //toast("QQ授权出错："+arg0.errorCode+"--"+arg0.errorDetail);
            Toast.makeText(LoginActivity.this, e.errorDetail, Toast.LENGTH_SHORT).show();
            FileCodeUtil.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            FileCodeUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            //toast("取消qq授权");
            Toast.makeText(getApplicationContext(), "取消qq授权", Toast.LENGTH_SHORT).show();
            FileCodeUtil.toastMessage(LoginActivity.this, "onCancel: ");
            FileCodeUtil.dismissDialog();
        }
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch (Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new DefaultUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(final Object response) {
                    Log.d(TAG, "onComplete: " + response.toString());
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    QQHandler.sendMessage(msg);
                    new Thread() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            QQUser qqUser =gson.fromJson(response.toString(), QQUser.class);
                            Log.i(TAG, "Figureurl_qq: "+qqUser.getFigureurl_qq());
                            Log.i(TAG, "City: "+qqUser.getCity());
                            Log.i(TAG, "Level: "+qqUser.getLevel());
//                            JSONObject json = (JSONObject) response;
//                            if (json.has("figureurl")) {
//                                Bitmap bitmap = null;
//                                try {
//                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
//                                } catch (JSONException e) {
//                                    SLog.e(TAG, "Util.getBitmap() jsonException : " + e.getMessage());
//                                }
//                                Message msg = new Message();
//                                msg.obj = bitmap;
//                                msg.what = 1;
//                                QQHandler.sendMessage(msg);
//                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            UserInfo info = new UserInfo(this, mTencent.getQQToken());
            info.getUserInfo(listener);

        } else {
//            mUserInfo.setText("");
//            mUserInfo.setVisibility(android.view.View.GONE);
//            mUserLogo.setVisibility(android.view.View.GONE);
        }
    }

    /**
     * 线程刷新UI数据
     */
    Handler QQHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
//                    try {
//                        mUserInfo.setVisibility(android.view.View.VISIBLE);
//                        mUserInfo.setText(response.getString("nickname"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
//                mUserLogo.setImageBitmap(bitmap);
//                mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
        }
    };

    /**
     * 回调数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 软键盘的自动弹出
     * @param editText
     */
    private void showKeyboard(EditText editText){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }

    /**
     * 软键盘的关闭
     */
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 登录转场
     */
    private Handler mHandler=new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLogin_ptn_anim.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
//                    UiTools.showSimpleLD(loginAc.this,getString(R.string.loading_login));
//                    userNameOrPhone = edit_login_usernameOrPhone.getText().toString();
//                    password = edit_login_password.getText().toString();
//                    //  用户名/手机号和密码登录
//                    StartUsernameOrPhonePassword(userNameOrPhone ,password);
                }
            });

        }
    };

}
