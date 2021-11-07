package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import static com.tencent.connect.common.Constants.KEY_QRCODE;
import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;
import static com.xuexiang.xutil.tip.ToastUtils.toast;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CityPickerListener;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.listener.TimePickerListener;
import com.lxj.xpopupext.popup.CityPickerPopup;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lxj.xpopupext.popup.TimePickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.App;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.SessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.SessionUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.RegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.coding.FileCodeUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.permission.PermissionMgr;

/**
 * created by on 2021/9/2
 * 描述：登录界面
 *
 * @author ZSAndroid
 * @create 2021-09-02-18:21
 */
@SuppressLint("NonConstantResourceId")
public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "LoginActivity";
    /**登录界面中Gson数据解析发送广播消息，首页通过对象序列号设置的键“QQUserBean”接受广播消息内容 */
    public static final String action = "QQUserLogin.BroadCast.Action";
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
    /** 手机号登录 */
    @BindView(R2.id.tv_login_tel) TextView mTv_login_tel;
    /** 去注册 */
    @BindView(R2.id.login_tv_go_register) TextView mLogin_tv_go_register;
    /** 微信登录 */
    @BindView(R2.id.login_iv_wx) ImageView mLogin_iv_wx;
    /** QQ登录 */
    @BindView(R2.id.login_iv_qq) ImageView mLogin_iv_qq;
    /** 强制扫码登录 */
    @BindView(R2.id.check_force_qr) CheckBox mCheckForceQr;
    /** 勾选授权同意 */
    @BindView(R2.id.check_if_authorize) CheckBox mCheck_if_authorizer;
    /** 输入的用户名密码 */
    private String strUserName, strPassword;
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
        return R.layout.login_activity;
    }


    @Override
    protected void prepareData() {
        /** QQ登录权限申请*/
        PermissionMgr.getInstance().requestPermissions(this);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        Log.i(TAG, "进入登录" + Constant.mTencent.isSessionValid());//true
        /** 默认不勾选✓授权同意 */
        mCheck_if_authorizer.setChecked(false);
        /** 默认不勾选✓扫码登录 */
        mCheckForceQr.setChecked(false);
        /** 初始化动画登录按钮属性 */
        initCheckAuthorizeState();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Log.i(TAG, "点击QQ登录图标前Session是否有效: " + Constant.mTencent.isSessionValid());
    }

    @Override
    protected void initEvent() {
        //授权同意开关监听
        mCheck_if_authorizer.setOnCheckedChangeListener(this);
        //扫码登录开关监听
        mCheckForceQr.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initListener() {
        mLogin_edit_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!RegexUtils.checkMobile(String.valueOf(charSequence))) {
                    mTil_login_username.setError("手机号码格式不正确！");
                    mTil_login_username.setErrorEnabled(true);
                } else {
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
                if (!RegexUtils.checkMobile(String.valueOf(charSequence))) {
                    mTil_login_password.setError("手机号码格式不正确！");
                    mTil_login_password.setErrorEnabled(true);
                } else {
                    mTil_login_password.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 业务操作
     */
    @Override
    protected void doBusiness() {

    }

    /**
     * 请求权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionMgr.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnClick({R2.id.login_ptn_anim, R2.id.login_tv_forget_pwd, R2.id.login_tv_go_register, R2.id.login_iv_wx, R2.id.login_iv_qq, R2.id.tv_login_tel})
    public void onLoginViewClick(View view) {
        switch (view.getId()) {
            case R.id.login_ptn_anim://动画登录
                strUserName = mLogin_edit_username.getText().toString();//获取用户输入信息
                strPassword = mLogin_edit_password.getText().toString();//获取用户输入密码
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
                if (mCheck_if_authorizer.isChecked()) {
                    QQLogin(false);//传入false，即拉起手Q登录，无须扫码
                } else {
                    ToastUtil.showToast("请先勾选授权同意");
                }
                break;
            case R.id.tv_login_tel://手机号快捷登录
                userGoTelNumberLogin();
                break;
        }
    }

    /**
     * 用户名密码判断登录
     */
    private void userLogin(String strUserName, String strPassword) {
        if (TextUtils.isEmpty(strUserName) && TextUtils.isEmpty(strPassword)) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, R.string.please_input_user_name_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (TextUtils.isEmpty(strUserName)) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, R.string.please_input_user_name, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (TextUtils.isEmpty(strPassword)) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, R.string.please_input_password, Snackbar.LENGTH_LONG);
            //设置Snackbar上提示的字体颜色
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        mLogin_ptn_anim.startAnim();
        Message m = mHandler.obtainMessage();
        mHandler.sendMessageDelayed(m, 1500);
    }

    /**
     * 手机号快捷登录
     */
    private void userGoTelNumberLogin() {
        ToastUtil.showToast("手机号快捷登录");
    }

    /**
     * 找回密码
     */
    private void userFindPassword() {
        ToastUtil.showToast("找回密码");
        CityPickerPopup popup = new CityPickerPopup(LoginActivity.this);
        popup.setCityPickerListener(new CityPickerListener() {
            @Override
            public void onCityConfirm(String province, String city, String area, View v) {
                Log.e("tag", province +" - " +city+" - " +area);
                Toast.makeText(LoginActivity.this, province +" - " +city+" - " +area, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCityChange(String province, String city, String area) {
                Log.e("tag", province +" - " +city+" - " +area);
                Toast.makeText(LoginActivity.this, province +" - " +city+" - " +area, Toast.LENGTH_SHORT).show();
            }
        });
        new XPopup.Builder(LoginActivity.this)
                .asCustom(popup)
                .show();

    }

    /**
     * 跳转注册
     */
    private void userGoRegisterAccount() {
        ToastUtil.showToast("跳转注册");
        Calendar date = Calendar.getInstance();
        date.set(2000, 5,1);
        Calendar date2 = Calendar.getInstance();
        date2.set(2020, 5,1);
        TimePickerPopup popup = new TimePickerPopup(LoginActivity.this)
//                        .setDefaultDate(date)  //设置默认选中日期
//                        .setYearRange(1990, 1999) //设置年份范围
//                        .setDateRange(date, date2) //设置日期范围
                .setTimePickerListener(new TimePickerListener() {
                    @Override
                    public void onTimeChanged(Date date) {
                        //时间改变
                    }
                    @Override
                    public void onTimeConfirm(Date date, View view) {
                        //点击确认时间
                        Toast.makeText(LoginActivity.this, "选择的时间："+date.toLocaleString(), Toast.LENGTH_SHORT).show();
                    }
                });

        new XPopup.Builder(LoginActivity.this)
                .asCustom(popup)
                .show();

    }

    /**
     * 微信登录
     */
    private void weChatLogin() {
        notification4();
//        OkGo.<String>post(Constant.LOGIN_USERNAME_PASSWORD)
//                .tag(this)
//                .params("ulUsername", "ZSAndroid").params("ulPassword", "ZSAndroid1998")
//                .execute(new StringDialogCallback(this) {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        //注意这里已经是在主线程了
//                        String data = response.body();//这个就是返回来的结果
//                        Log.i(TAG, "onSuccess: " + data);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                    }
//                });
    }

    /**
     * QQ登录
     */
    private void QQLogin(boolean isChooseQRLogin) {
        Log.i(TAG, "点击QQ登录图标后会话是否有效" + Constant.mTencent.isSessionValid());
        if (!Constant.mTencent.isSessionValid()) { //会话无效false时
            /** 强制扫码登录 */
            this.getIntent().putExtra(AuthAgent.KEY_FORCE_QR_LOGIN, mCheckForceQr.isChecked());
            HashMap<String, Object> params = new HashMap<>();
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                params.put(KEY_RESTORE_LANDSCAPE, isChooseQRLogin);
            }
            params.put(KEY_SCOPE, "all");//授权全部权限
            params.put(KEY_QRCODE, mCheckForceQr.isChecked());//传入二维码单选框选中参数
            Log.i(TAG, "强制二维码登录是否勾选: " + mCheckForceQr.isChecked());
            Constant.mTencent.login(this, loginListener, params);//传入登录参数集合，拉起二维码登录扫码页，如果没有选中，则进行普通方式QQ登录
            Log.d(TAG, "返回系统启动到现在的毫秒数，包含休眠时间" + SystemClock.elapsedRealtime());
        } else { //会话有效true时
            /** 退出登录+清除旧会话Session */
            Constant.mTencent.logout(this);
            /** 重新授权登录，拉起新会话Session */
            Constant.mTencent.login(this, "all", loginListener);
            Log.i(TAG, "重新授权登录，拉起新会话Session是否有效" + Constant.mTencent.isSessionValid());
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.i(TAG, "QQ回调成功，会话消息===" + values.toString());//{"ret":0,"openid":"FD405BF12F7388E0A243786326AF3BC8","access_token":"EBC9BC62ADE...
            Log.i(TAG, "QQ回调后设置会话前是否有效" + Constant.mTencent.isSessionValid());//false
            //解析回调的QQ会话JSON数据
            SessionBean sessionBean = GsonUtil.gsonToBean(values.toString(), SessionBean.class);
            //QQ会话回调同时，执行post请求——RESTFul风格，调用MySQL数据
            OkGo.<String>post(Constant.LOGIN_QQ_SESSION + "/" + sessionBean.getOpenid() + "/" + sessionBean.getAccess_token())
                    .tag("QQ授权")
                    .execute(new StringCallback() {
                        @Override
                        public void onStart(Request<String, ? extends Request> request) {
                            XPopupUtils.setShowDialog(LoginActivity.this,"正在验证账户");
                        }
                        @Override
                        public void onSuccess(Response<String> response) {
                            //Json字符串解析转为实体类对象
                            SessionUserBean sessionUserBean = GsonUtil.gsonToBean(response.body(), SessionUserBean.class);
                            Log.i(TAG, "qqSessionBean=== " + sessionUserBean);
                            //当前QQ账号无授权登录APP应用——未绑定过登录账户
                            if (200 == sessionUserBean.getCode() && null == sessionUserBean.getData() && "此QQ号码暂未授权".equals(sessionUserBean.getMsg())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setTitle("授权提示")//这里设置标题
                                        .setMessage("系统检测此QQ号未关联账户")//这里设置提示信息
                                        .setTopImage(R.drawable.icon_tanchuang_tanhao)//这里设置顶部图标
                                        .setPositiveButton("点击关联", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                mDialog.dismiss();
                                                OkGo.getInstance().cancelTag("QQ授权");//跳转前取消本次请求
                                                //设置Intent意图参数，跳转账户信息绑定QQ会话的Session数据
                                                Intent thisIntentToBindUser = new Intent();
                                                thisIntentToBindUser.setClass(LoginActivity.this, LoginBindActivity.class);
                                                //Gson解析Json，通过Activity传递QQ会话解析Json数据到LoginBindActivity页面
                                                thisIntentToBindUser.putExtra("QQJsonData", values.toString());
                                                startActivityForResultAnimLeftToRight(thisIntentToBindUser, Constant.REQUEST_CODE_VALUE);//执行动画跳转
                                            }
                                        });
                                mDialog = builder.create();
                                mDialog.show();
                                return;
                            }
                            //当前QQ账号已授权登录APP应用——已绑定过登录账户
                            if (200 == sessionUserBean.getCode() && null != sessionUserBean.getData() && "此QQ账号已授权".equals(sessionUserBean.getMsg())) {
                                //开始查询MySQL用户表+QQ授权登录并集信息
                                //准备用户ID
                                int userIdToSelectBannedState = sessionUserBean.getData().getUlId();
                                /** QQ一键登录 */
                                OkGo.<String>post(Constant.QUERY_BANNED_STATE_BY_USERID_AND_ONE_KEY_QQ_LOGIN)
                                        .tag("QQ一键登录+更新QQ会话")
                                        .params("ret", sessionBean.getRet())
                                        .params("openid", sessionBean.getOpenid())
                                        .params("accessToken", sessionBean.getAccess_token())
                                        .params("payToken", sessionBean.getPay_token())
                                        .params("expiresIn", sessionBean.getExpires_in())
                                        .params("pf", sessionBean.getPf())
                                        .params("pfkey", sessionBean.getPfkey())
                                        .params("msg", sessionBean.getMsg())
                                        .params("loginCost", sessionBean.getLogin_cost())
                                        .params("queryAuthorityCost", sessionBean.getQuery_authority_cost())
                                        .params("authorityCost", sessionBean.getAuthority_cost())
                                        .params("expiresTime", sessionBean.getExpires_time())
                                        .params("ulId", userIdToSelectBannedState) //此条是授权的用户自增ID，以上是拉起授权QQ会话数据
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                //等待对话框
                                                UserQQSessionBean userQQSessionBean = GsonUtil.gsonToBean(response.body(), UserQQSessionBean.class);
                                                Log.i(TAG, "onSuccessQQ一键登录==: " + userQQSessionBean);

                                                if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "此账户处于封禁状态".equals(userQQSessionBean.getMsg())) {
                                                    DialogPrompt dialogPrompt = new DialogPrompt(LoginActivity.this, userQQSessionBean.toString());
                                                    dialogPrompt.show();
                                                    return;
                                                }
                                                if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "QQ一键登录失败".equals(userQQSessionBean.getMsg())) {
                                                    DialogPrompt dialogPrompt = new DialogPrompt(LoginActivity.this, "登录失败，未知错误，请联系开发者解决！");
                                                    dialogPrompt.show();
                                                    return;
                                                }
                                                if (200 == userQQSessionBean.getCode() && null != userQQSessionBean.getData() && "登录成功".equals(userQQSessionBean.getMsg())) {
                                                    //一键登录(并联信息持久化手机内存，生命周期：存储——清空(卸载App或注销时clear))
                                                    App.appActivity.finish();//销毁的Activity界面
                                                    SharePreferenceUtil.putObject(LoginActivity.this, userQQSessionBean);
                                                    /** 初始化传入OPENID+TOKEN值,使得Session有效，最终解析后得到登录用户信息 */
                                                    initOpenidAndTokenAndGsonGetParseQQUserInfo(values);
                                                    startActivityAnimRightToLeft(new Intent(LoginActivity.this, IndexActivity.class));
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onError(Response<String> response) {
                                                //未绑定温馨提示
                                                Snackbar snackbar = Snackbar.make(mLogin_rl_show, "请求错误，服务器连接失败：" + response.getException(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                snackbar.show();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onFinish() {
                            super.onFinish();
                            XPopupUtils.setSmartDisDialog();
                        }

                        @Override
                        public void onError(Response<String> response) {
                            //未绑定温馨提示
                            Snackbar snackbar = Snackbar.make(mLogin_rl_show, "请求错误，服务器连接失败：" + response.getException(), Snackbar.LENGTH_SHORT)
                                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                        }
                    });
        }
    };

    /**
     * 拉起选择QQ授权界面后监听回调结果
     */
    private class BaseUiListener extends DefaultUiListener {
        @Override
        public void onComplete(Object response) {
            Log.d(TAG, "onComplete:QQ登录回调成功");
            if (null == response) {
                FileCodeUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                FileCodeUtil.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            //FileCodeUtil.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
            doComplete((JSONObject) response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, "本次QQ授权登录出错啦~" + e.errorDetail, Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }

        @Override
        public void onCancel() {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, "您取消了QQ授权登录哟~", Snackbar.LENGTH_SHORT)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
    }

    /**
     * 初始化OPENID和TOKEN值（为了得到用户的信息）
     *
     * @param jsonObject
     */
    public void initOpenidAndTokenAndGsonGetParseQQUserInfo(JSONObject jsonObject) {
        try {
            /**获取Constant.mTencent.login(监听器loginListener) 回调成功有效Session中的 openid值、access_token值、expires_in值*/
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);//用户应用唯一标识
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);//用户授权令牌
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);//有效时间
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
                Log.i(TAG, "回调后设置会话前是否有效:" + Constant.mTencent.isSessionValid());//false
                /** 下面两行set设置方法非常关键重要，若没有设置，导致Constant.mTencent的Session无效，将不能对QQ授权用户的信息列表执行请求与回调 */
                Constant.mTencent.setAccessToken(token, expires);//授权令牌设置至Tencent实例
                Constant.mTencent.setOpenId(openId);//应用唯一标识设置至Tencent实例
                Log.i(TAG, "回调后设置会话后是否有效:" + Constant.mTencent.isSessionValid());//true
                /** 会话Session有效时进行QQ授权用户的信息列表请求与回调 */
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 回调数据
     *
     * @param requestCode 请求码，即调用startActivityForResult()传递过去的值
     * @param resultCode  结果码，结果码用于标识返回数据来自指定的Activity
     * @param data        返回数据，存放了返回数据的Intent，使用第三个输入参数可以取出新Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode + " resultCode=" + resultCode);
        //QQ授权选择QQ登录界面回调
        if (requestCode == Constants.REQUEST_LOGIN || requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        //账户绑定QQ成功
        if (requestCode == Constant.REQUEST_CODE_VALUE && resultCode == Constant.RESULT_CODE_BIND_ACCOUNT_SUCCESS) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, data.getStringExtra("BindBackName") + "同学，您已成功绑定QQ账号，快去登录吧~", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .setDuration(5000);
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
        //账户绑定QQ失败
        if (requestCode == Constant.REQUEST_CODE_VALUE && resultCode == Constant.RESULT_CODE_BIND_ACCOUNT_ERROR) {
            DialogPrompt dialogPrompt = new DialogPrompt(LoginActivity.this, data.getStringExtra("BindBackName") + "同学，您的账户无法绑定QQ号，请联系开发者~");
            dialogPrompt.showAndFinish(LoginActivity.this);
        }
        //账户被封禁
        if (requestCode == Constant.REQUEST_CODE_VALUE && resultCode == Constant.RESULT_CODE_BIND_ACCOUNT_BANNED) {
            Snackbar snackbar = Snackbar.make(mLogin_rl_show, data.getStringExtra("BindBackName") + "同学，系统检测出您的账户处于封禁状态！", Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.colorAccent))
                    .setDuration(5000)
                    .setAction("点击申请解封", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //设置Intent意图参数，跳转账户信息绑定QQ会话的Session数据
                            Intent thisIntentToBindUser = new Intent();
                            thisIntentToBindUser.setClass(LoginActivity.this, UserApplyUntieActivity.class);
                            //Gson解析Json，通过Activity传递QQ会话解析Json数据到LoginBindActivity页面
                            thisIntentToBindUser.putExtra("UntieBannedName", data.getStringExtra("BindBackName"));
                            startActivityForResultAnimLeftToRight(thisIntentToBindUser, Constant.REQUEST_CODE_VALUE);//执行动画跳转
                        }
                    });
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 登录转场
     */
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mLogin_ptn_anim.stopAnim(new ProgressButton.OnStopAnim() {
                @Override
                public void Stop() {
                    //UiTools.showSimpleLD(LoginActivity.this,"getString(R.string.loading_login)");
                    strUserName = mLogin_edit_username.getText().toString();
                    strPassword = mLogin_edit_password.getText().toString();
                    //用户名和密码登录
                    StartUserNameAndPassWordByLogin(strUserName ,strPassword);
                }
            });

        }
    };

    /**
     *
     * @param strUserName 用户名
     * @param strPassword 密码
     */
    private void StartUserNameAndPassWordByLogin(String strUserName, String strPassword) {

        OkGo.<String>post(Constant.LOGIN_USERNAME_PASSWORD_AND_QQ_SESSION)
                .tag("用户名密码登录")
                .params("ulUsername", strUserName).params("ulPassword", strPassword)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.setShowDialog(LoginActivity.this,"正在验证账户");
                        //popupView.dismiss();  //立即消失
                        //popupView.delayDismiss(300);//延时消失，有时候消失过快体验可能不好，可以延时一下
                        //popupView.smartDismiss(); //会等待弹窗的开始动画执行完毕再进行消失，可以防止接口调用过快导致的动画不完整。
                    }
                    @Override
                    public void onSuccess(Response<String> response) {
                        //等待对话框
                        //LoadingDialog.showSimpleLD(LoginActivity.this, getString(R.string.indexLoadLoginInfo));
                        UserQQSessionBean userQQSessionBean = GsonUtil.gsonToBean(response.body(), UserQQSessionBean.class);
                        Log.i(TAG, "onSuccessQQ一键登录==: " + userQQSessionBean);
                        if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "登录失败，用户名和密码不匹配".equals(userQQSessionBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mLogin_rl_show, userQQSessionBean.getMsg(), Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent)).setDuration(5000);
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "此账户处于封禁状态".equals(userQQSessionBean.getMsg())) {
                            DialogPrompt dialogPrompt = new DialogPrompt(LoginActivity.this, userQQSessionBean.getMsg());
                            dialogPrompt.show();
                            return;
                        }
                        if (200 == userQQSessionBean.getCode() && null == userQQSessionBean.getData() && "登录失败，此账户未绑定QQ".equals(userQQSessionBean.getMsg())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("登录提示")
                                    .setMessage("当前账户还未绑定QQ~" + getString(R.string.please_bind_qq_login))
                                    .setTopImage(R.drawable.icon_tanchuang_wenhao)
                                    .setNegativeButton("溜了溜了", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mDialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("立即授权", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            OkGo.getInstance().cancelTag("用户名密码登录");//跳转前取消本次请求
                                            QQLogin(false);//传入false，即拉起手Q登录，无须扫码
                                            mDialog.dismiss();
                                        }
                                    });
                            mDialog = builder.create();
                            mDialog.show();
                            return;
                        }
                        if (200 == userQQSessionBean.getCode() && null != userQQSessionBean.getData() && "登录成功".equals(userQQSessionBean.getMsg())) {
                            //拉去并联登录信息持久化
                            App.appActivity.finish();//销毁的Activity界面
                            SharePreferenceUtil.putObject(LoginActivity.this, userQQSessionBean);
                            startActivityAnimRightToLeft(new Intent(LoginActivity.this, IndexActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        XPopupUtils.setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //未绑定温馨提示
                        Snackbar snackbar = Snackbar.make(mLogin_rl_show, "请求错误，服务器连接失败：" + response.getException(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }
                });
    }

    /**
     * 初始化Check状态+登录按钮状态动画
     */
    public void initCheckAuthorizeState() {
        if (mCheck_if_authorizer.isChecked()) {
            mLogin_ptn_anim.setEnabled(true);
            mLogin_ptn_anim.setBgColor(Color.RED);
            mLogin_ptn_anim.setTextColor(Color.WHITE);
            mLogin_ptn_anim.setProColor(Color.WHITE);
            mLogin_ptn_anim.setButtonText("登  录");
        } else {
            mLogin_ptn_anim.setEnabled(false);
            mLogin_ptn_anim.setBgColor(Color.GRAY);
            mLogin_ptn_anim.setTextColor(Color.WHITE);
            mLogin_ptn_anim.setProColor(Color.WHITE);
            mLogin_ptn_anim.setButtonText("请先勾选授权同意");
            ToastUtil.showToast("请先勾选授权同意");
        }
    }

    /**
     * Check开关监听状态触发
     *
     * @param compoundButton Check控件id
     * @param state          开关状态
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean state) {

        switch (compoundButton.getId()) {
            case R.id.check_if_authorize: //勾选同意开关事件
                if (state) {
                    mLogin_ptn_anim.setEnabled(true);
                    mLogin_ptn_anim.setBgColor(Color.RED);
                    mLogin_ptn_anim.setTextColor(Color.WHITE);
                    mLogin_ptn_anim.setProColor(Color.WHITE);
                    mLogin_ptn_anim.setButtonText("登  录");
                } else {
                    mLogin_ptn_anim.setEnabled(false);
                    mLogin_ptn_anim.setBgColor(Color.GRAY);
                    mLogin_ptn_anim.setTextColor(Color.WHITE);
                    mLogin_ptn_anim.setProColor(Color.WHITE);
                    mLogin_ptn_anim.setButtonText("请先勾选授权同意");
                    ToastUtil.showToast("请先勾选授权同意");
                }
                break;
            case R.id.check_force_qr: //QQ扫码登录开关事件
                if (state) {
                    QQLogin(true);//调用扫码登录
                    mCheckForceQr.setChecked(false);//点击一次，恢复未选状态，人性化操作
                }
                break;
        }
    }

    /**
     * 按下返回键，销毁当前页面
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_right_out);
        LoginActivity.this.finish();
    }
}
