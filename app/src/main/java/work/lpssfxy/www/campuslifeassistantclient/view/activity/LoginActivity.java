package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import static com.tencent.connect.common.Constants.KEY_QRCODE;
import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.Constants;
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
import work.lpssfxy.www.campuslifeassistantclient.base.StringDialogCallback;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.login.ProgressButton;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.RegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.coding.FileCodeUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.LoadingDialog;
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
    private String strUserName,strPassword;

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
        /** 默认不勾选✓授权同意 */
        mCheck_if_authorizer.setChecked(false);
        /** 默认不勾选✓扫码登录 */
        mCheckForceQr.setChecked(false);
        /** 初始化动画登录按钮属性 */
        initCheckAuthorizeState();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Log.i(TAG, "点击QQ登录图标前Session是否有效: "+Constant.mTencent.isSessionValid());
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
     * 业务操作
     */
    @Override
    protected void doBusiness() {

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

    @OnClick({R2.id.login_ptn_anim,R2.id.login_tv_forget_pwd,R2.id.login_tv_go_register,R2.id.login_iv_wx,R2.id.login_iv_qq,R2.id.tv_login_tel})
    public void onLoginViewClick(View view){
        switch (view.getId()){
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
                if (mCheck_if_authorizer.isChecked()){
                    QQLogin(false);//传入false，即拉起手Q登录，无须扫码
                }else {
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
//        Observable<String> call = OkGo.post(Constant.LOGIN_USERNAME_PASSWORD)//
//                .headers("aaa", "111")//
//                .params("bbb", "222")//
//                .getCall(StringConvert.create(), RxAdapter.<String>create());
        OkGo.<String>post(Constant.LOGIN_USERNAME_PASSWORD)
                .tag(this)
                .params("ulUsername","ZSAndroid").params("ulPassword","ZSAndroid1998")
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
        ToastUtil.showToast("微信登录");

//        //取消此次请求
//        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(),this);
    }
    /**
     * QQ登录
     */
    private void QQLogin(boolean isChooseQRLogin) {
        Log.i(TAG, "点击QQ登录图标后会话是否有效"+Constant.mTencent.isSessionValid());
        if (!Constant.mTencent.isSessionValid()) { //会话无效false时
            /** 强制扫码登录 */
            this.getIntent().putExtra(AuthAgent.KEY_FORCE_QR_LOGIN, mCheckForceQr.isChecked());
            HashMap<String, Object> params = new HashMap<>();
            if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                params.put(KEY_RESTORE_LANDSCAPE, isChooseQRLogin);
            }
            params.put(KEY_SCOPE, "all");//授权全部权限
            params.put(KEY_QRCODE, mCheckForceQr.isChecked());//传入二维码单选框选中参数
            Log.i(TAG, "强制二维码登录是否勾选: "+mCheckForceQr.isChecked());
            Constant.mTencent.login(this, loginListener, params);//传入登录参数集合，拉起二维码登录扫码页，如果没有选中，则进行普通方式QQ登录
            Log.d(TAG, "返回系统启动到现在的毫秒数，包含休眠时间" + SystemClock.elapsedRealtime());
        } else { //会话有效true时
            /** 退出登录+清除旧会话Session */
            Constant.mTencent.logout(this);
            /** 重新授权登录，拉起新会话Session */
            Constant.mTencent.login(this, "all", loginListener);
            Log.i(TAG, "重新授权登录，拉起新会话Session是否有效"+Constant.mTencent.isSessionValid());
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            LoadingDialog.showSimpleLD(LoginActivity.this,getString(R.string.indexLoadLoginInfo));
            Log.i(TAG, "QQ回调成功，会话消息===" + values.toString());//{"ret":0,"openid":"FD405BF12F7388E0A243786326AF3BC8","access_token":"EBC9BC62ADE...
            Log.i(TAG, "QQ回调后设置会话前是否有效1"+Constant.mTencent.isSessionValid());//false
            String strOpenId = values.optString("openid");
            String strAccessToken = values.optString("access_token");
            //QQ会话回调同时，执行post请求——RESTFul风格，调用MySQL数据
            OkGo.<String>post(Constant.LOGIN_QQ_SESSION+"/"+strOpenId+"/"+strAccessToken)
                    .tag(this)
                    .execute(new StringDialogCallback(LoginActivity.this) {
                        @Override
                        public void onSuccess(Response<String> response) {
                           //Json字符串解析转为实体类对象
                           ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                           Log.i(TAG, "网络请求响应成功数据onSuccess=== "+responseBean);
                           //当前QQ账号无授权登录APP应用——未绑定过登录账户
                           if (400 == responseBean.getCode() && "error".equals(responseBean.getMsg())){
                               DialogPrompt dialogPrompt = new DialogPrompt(LoginActivity.this, responseBean.getData()+getString(R.string.please_qq_login));
                               dialogPrompt.show();
                               Snackbar snackbar = Snackbar.make(mLogin_rl_show,"没有绑定QQ账户？",Snackbar.LENGTH_INDEFINITE)
                                       .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                                       .setDuration(10000)
                                       .setAction("点击绑定", new View.OnClickListener() {  //设置点击按钮
                                           @Override
                                           public void onClick(View v) {
                                               //设置Intent意图参数，跳转账户信息绑定QQ会话的Session数据
                                               Intent thisIntentToBindUser = new Intent();
                                               thisIntentToBindUser.setClass(LoginActivity.this,LoginBindActivity.class);
                                               //Gson解析Json，通过Activity传递数据
                                               thisIntentToBindUser.putExtra("QQJsonData", GsonUtil.gsonToJson(values));
                                               startActivityAnimLeftToRight(thisIntentToBindUser);//执行动画跳转
                                           }
                                       });
                               //设置Snackbar上提示的字体颜色
                               setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                               snackbar.show();
                               return;
                           }
                            //当前QQ账号已授权登录APP应用——已绑定过登录账户
                           if (200 == responseBean.getCode() && "此QQ账号已授权".equals(responseBean.getMsg())){
                               //开始查询MySQL用户表+QQ授权登录并集信息
                               Toast.makeText(LoginActivity.this, responseBean.getData(), Toast.LENGTH_SHORT).show();

                               return;
                           }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            //未绑定温馨提示
                            Snackbar snackbar= Snackbar.make(mLogin_rl_show,"登录失败，未知错误："+response.body(),Snackbar.LENGTH_SHORT)
                                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            super.onError(response);
                        }
                    });
            //Activity传递数据到Activity
//            Intent intent = new Intent();
//            intent.setClass(LoginActivity.this,LoginBindActivity.class);
//            intent.putExtra("QQJsonDate", values.toString());
//            startActivityAnimLeftToRight(intent);
//            /** 初始化传入OPENID+TOKEN值,使得Session有效，最终解析后得到登录用户信息 */
//            initOpenidAndTokenAndGsonGetParseQQUserInfo(values);
//            /** 保存Session信息存入SharePreference本地数据 */
//            initSaveSessionDataToLocalFile(values);
//            /** 回调成功会话信息，保存到Constant.mTencent中，不做持久化操作时，仅当前APP启动--有效时间--结束
//             * 调用Constant.mTencent.logout(上下文) 可以使得当前会话在APP结束之前失效——即注销当前授权登录QQ的Session信息*/
//            Constant.mTencent.saveSession(values);
//
//            /**登录成功跳转之前构思：
//             * 从IndexActivity跳转至LoginActivity时考虑用户不登录返回首页的情况，因此没有finish，为避免成功登录后新跳转IndexActivity与之前的IndexActivity重叠多个页面
//             * 解决方式：先finish掉之前的IndexActivity，然后登录成功后再跳转IndexActivity，并finish掉LoginActivity
//             */
//            App.appActivity.finish();//通过Application全局单例模式，在IndexActivity中赋值待销毁的Activity界面
//            startActivityAnimRightToLeft(new Intent(LoginActivity.this,IndexActivity.class));//登录成功后跳转主页
//            finish();//并销毁登录界面
            LoadingDialog.closeSimpleLD();

        }
    };

    /**
     * 登录回调成功后保存Session信息本地XML数据文件中
     * @param jsonObject
     */
    private void initSaveSessionDataToLocalFile(JSONObject jsonObject) {
        Log.i(TAG, "回调成功Gson解析Json前会话Session数据: "+jsonObject.toString());
        //Gson解析并序列号至Java对象中
        Constant.qqUserSessionBean = GsonUtil.gsonToBean(jsonObject.toString(), QQUserSessionBean.class);
        Log.i(TAG, "回调成功Gson解析Json后会话Session数据(持久化存入此解析数据到xml): "+Constant.qqUserSessionBean);
        /** Gson解析后Java对象持久化数据保存本地，IndexActivity首页调用JSONObject的put()方法重组顺序，提供给Constant.mTencent.initSessionCache(JSONObject实例) */
        SharePreferenceUtil.putObject(LoginActivity.this,Constant.qqUserSessionBean);
    }


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
            Toast.makeText(LoginActivity.this,"QQ授权登录出错！" + e.errorDetail, Toast.LENGTH_SHORT).show();
            //FileCodeUtil.toastMessage(LoginActivity.this, "QQ授权出错:" + e.errorDetail);
            //FileCodeUtil.dismissDialog();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,"取消QQ授权登录！", Toast.LENGTH_SHORT).show();
            //FileCodeUtil.toastMessage(LoginActivity.this, "取消qq授权");
            //FileCodeUtil.dismissDialog();
        }
    }

    /**
     * 初始化OPENID和TOKEN值（为了得到用户的信息）
     * @param jsonObject
     */
    public void initOpenidAndTokenAndGsonGetParseQQUserInfo(JSONObject jsonObject) {
        try {
            /**获取Constant.mTencent.login(监听器loginListener) 回调成功有效Session中的 openid值、access_token值、expires_in值*/
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);//用户授权令牌
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);//用户应用唯一标识
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);//有效时间
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                Log.i(TAG, "回调后设置会话前是否有效:"+Constant.mTencent.isSessionValid());//false
                /** 下面两行set设置方法非常关键重要，若没有设置，导致Constant.mTencent的Session无效，将不能对QQ授权用户的信息列表执行请求与回调 */
                Constant.mTencent.setAccessToken(token, expires);//授权令牌设置至Tencent实例
                Constant.mTencent.setOpenId(openId);//应用唯一标识设置至Tencent实例
                Log.i(TAG, "回调后设置会话后是否有效:"+Constant.mTencent.isSessionValid());//true

                /** 会话Session有效时进行QQ授权用户的信息列表请求与回调 */
                GsonParseJsonDataToLocalAndToBroadcast();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Constant.mTencent的Session有效为true时进行QQ授权用户的信息列表请求与回调
     * Gson解析Json数据存入SharePreference+通过广播消息发送解析后Json数据
     */
    private void GsonParseJsonDataToLocalAndToBroadcast() {
        if (Constant.mTencent != null && Constant.mTencent.isSessionValid()) { //
            IUiListener listener = new DefaultUiListener() {
                /** 以下进行对获取授权用户信息使用业务，这里存入本地，以及发送广播传刀IndexActivity并更新首页UI */
                @Override
                public void onComplete(final Object response) {
                    Log.d(TAG, "请求回调用户信息列表= " + response.toString());
                    /** 调用Gson工具类，回掉的JSON数据，转化为Java对象*/
                    Constant.qqUser = GsonUtil.gsonToBean(response.toString(), QQUserBean.class);
                    /** 调用SharePreference工具类把Gson转化后的Java对象实现数据持久化，文件名为“ZSAndroid”的本地数据*/
                    SharePreferenceUtil.putObject(LoginActivity.this,Constant.qqUser);
                    Log.i(TAG, "qqUser全部数据: "+Constant.qqUser);
                    /** 通过Intent发送广播消息，*/
                    Intent intent = new Intent(action);
                    /**创建捆绑实例，Intent传递Java对象*/
                    Bundle bundle=new Bundle();
                    /** Java对象序列化存入Intent */
                    bundle.putSerializable("QQUserBean", Constant.qqUser);
                    /** 发送Intent序列化数据至Bundle捆绑对象*/
                    intent.putExtras(bundle);
                    Log.i(TAG, "bundle: "+bundle.toString());
                    /** 发送广播，接受者通过“QQUserBean”接收广播消息内容 */
                    sendBroadcast(intent);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(LoginActivity.this, "取消获取授权用户信息", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError e) {
                    Toast.makeText(LoginActivity.this, "获取授权用户信息出错："+e.errorDetail, Toast.LENGTH_SHORT).show();
                }
            };
            /** 根据Constant.mTencent会话中TOKEN值，请求回调授权用户信息列表*/
            UserInfo info = new UserInfo(this, Constant.mTencent.getQQToken());
            Log.i(TAG, "QQToken====: "+Constant.mTencent.getQQToken().toString());
            /** 开始监听请求回调操作*/
            info.getUserInfo(listener);

        } else {
            Toast.makeText(this, "QQ无效Session", Toast.LENGTH_SHORT).show();
        }

    }

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

    /**
     * 初始化Check状态+登录按钮状态动画
     */
    public void initCheckAuthorizeState(){
        if (mCheck_if_authorizer.isChecked()){
            mLogin_ptn_anim.setEnabled(true);
            mLogin_ptn_anim.setBgColor(Color.RED);
            mLogin_ptn_anim.setTextColor(Color.WHITE);
            mLogin_ptn_anim.setProColor(Color.WHITE);
            mLogin_ptn_anim.setButtonText("登  录");
        }else {
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
     * @param compoundButton Check控件id
     * @param state 开关状态
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean state) {

        switch (compoundButton.getId()){
            case R.id.check_if_authorize: //勾选同意开关事件
                if (state){
                    mLogin_ptn_anim.setEnabled(true);
                    mLogin_ptn_anim.setBgColor(Color.RED);
                    mLogin_ptn_anim.setTextColor(Color.WHITE);
                    mLogin_ptn_anim.setProColor(Color.WHITE);
                    mLogin_ptn_anim.setButtonText("登  录");
                }else {
                    mLogin_ptn_anim.setEnabled(false);
                    mLogin_ptn_anim.setBgColor(Color.GRAY);
                    mLogin_ptn_anim.setTextColor(Color.WHITE);
                    mLogin_ptn_anim.setProColor(Color.WHITE);
                    mLogin_ptn_anim.setButtonText("请先勾选授权同意");
                    ToastUtil.showToast("请先勾选授权同意");
                }
                break;
            case R.id.check_force_qr: //QQ扫码登录开关事件
                if (state){
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
        LoginActivity.this.finish();
    }
}
