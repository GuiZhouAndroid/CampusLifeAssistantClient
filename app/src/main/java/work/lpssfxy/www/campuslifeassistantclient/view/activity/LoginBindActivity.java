package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.StringDialogCallback;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/10/27
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-27-10:22
 */
@SuppressLint("NonConstantResourceId")
public class LoginBindActivity extends BaseActivity {
    @BindView(R2.id.tv_qq) TextView textView;
    /** 绑定用户名 */
    @BindView(R2.id.edit_bind_username) EditText mEdit_bind_username;
    /** 绑定密码 */
    @BindView(R2.id.edit_bind_password) EditText mEdit_bind_password;
    /** 立即绑定QQ */
    @BindView(R2.id.btn_start_bind) Button mBtn_start_bind;

    private static final String TAG = "LoginBindActivity";
    private String strEditUsername, strEditPassword;//QQ会话数据、用户名数据、密码数据
    private QQUserSessionBean userSessionData;

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

    /**
     * 加载布局XML
     * @return 布局文件
     */
    @Override
    public int bindLayout() {
        return R.layout.login_activity_bind;
    }

    /**
     * 准备数据
     */
    @Override
    protected void prepareData() {
        //登录页拉起QQ授权传递QQ会话的Json数据，转为实体类对象，提供QQ信息绑定用户信息使用
        userSessionData = GsonUtil.gsonToBean(getIntent().getStringExtra("QQJsonData"),QQUserSessionBean.class);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        textView.setText(userSessionData.toString());
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
     *
     * @param view
     */
    @OnClick({R2.id.btn_start_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_bind: //点击立即绑定
                strEditUsername = mEdit_bind_username.getText().toString().trim();//获取输入的用户名值
                strEditPassword = mEdit_bind_password.getText().toString().trim();//获取输入的密码值
                hideKeyboard();//隐藏软键盘
                if (strEditUsername.isEmpty() && strEditPassword.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(mBtn_start_bind, R.string.please_input_user_name_password, Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                if (strEditUsername.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(mBtn_start_bind, R.string.please_input_bind_user_name, Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                if (strEditPassword.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(mBtn_start_bind, R.string.please_input_bind_password, Snackbar.LENGTH_LONG);
                    //设置Snackbar上提示的字体颜色
                    setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                    return;
                }
                doStartQQBindUser(strEditUsername, strEditPassword);//立即绑定
                break;
        }
    }

    /**
     * 开始立即绑定业务逻辑--->首先通过用户名查询是否处于封禁状态，
     * 已封禁：就跳出doStartQQBindUser()并打印提示
     * 非封禁：执行登录，获取用户的全部信息，获取LoginActivity传递过来的QQ会话Json数据，调用SpringBoot授权信息关联接口，以用户自增ID为授权依据，完成账户+QQ号的绑定功能
     * @param strEditUsername 用户名
     * @param strEditPassword 密码
     */
    private void doStartQQBindUser(String strEditUsername, String strEditPassword) {
        /** 判断是否封禁 */
        OkGo.<String>post(Constant.USER_QUERY_BANNED_STATE+"/"+ strEditUsername )
                .tag("判断封禁")
                .execute(new StringDialogCallback(this) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(),ResponseBean.class);
                        Log.i(TAG, "onSuccess==: " + responseBean);
                        if (200 == responseBean.getCode() && "此账户处于封禁状态".equals(responseBean.getMsg())){
                            DialogPrompt dialogPrompt = new DialogPrompt(LoginBindActivity.this, "【"+strEditUsername+"】"+"账户异常，禁止授权"+"，"+responseBean.getData(), 10);
                            dialogPrompt.showAndFinish(LoginBindActivity.this);
                            Intent intent = new Intent();
                            intent.putExtra("BindBackName",strEditUsername);
                            LoginBindActivity.this.setResult(Constant.RESULT_CODE_BIND_ACCOUNT_BANNED,intent);
                            return;
                        }
                        /** 执行至此，证明账户名没有被封禁，开始拉起用户信息 */
                        OkGo.<String>post(Constant.LOGIN_USERNAME_PASSWORD)
                                .tag("获取授权")
                                .params("ulUsername", strEditUsername).params("ulPassword", strEditPassword)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        //Json字符串解析转为实体类对象
                                        UserBean userBeanData = GsonUtil.gsonToBean(response.body(), UserBean.class);
                                        Log.i(TAG, "userBeanData=== " + userBeanData);
                                        /** 用户名密码效验失败 */
                                        if (200 == userBeanData.getCode() && null == userBeanData.getData() && "登录失败，用户名和密码不匹配".equals(userBeanData.getMsg())) {
                                            //QQ授权绑定用户失败
                                            Snackbar snackbar = Snackbar.make(mBtn_start_bind, userBeanData.getMsg(), Snackbar.LENGTH_SHORT)
                                                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                            snackbar.show();
                                            return;
                                        }
                                        /** 用户名密码效验成功 */
                                        if (200 == userBeanData.getCode() && null != userBeanData.getData() && "登录成功".equals(userBeanData.getMsg())) {
                                            int userBindUserId = userBeanData.getData().getUlId();
                                            Log.i(TAG, "QQ授权绑定的用户ID=== " + userBindUserId);
                                            Log.i(TAG, "QQ授权绑定的QQ会话=== " + userSessionData);
                                            //以上2份数据准备就绪，调用绑定接口，执行授权业务
                                            /** 开始调用SpringBoot授权信息关联接口 */
                                            OkGo.<String>post(Constant.LOGIN_ADD_QQ_SESSION)
                                                    .tag("绑定QQ")
                                                    .params("ret", userSessionData.getRet())
                                                    .params("openid", userSessionData.getOpenid())
                                                    .params("accessToken", userSessionData.getAccess_token())
                                                    .params("payToken", userSessionData.getPay_token())
                                                    .params("expiresIn", userSessionData.getExpires_in())
                                                    .params("pf", userSessionData.getPf())
                                                    .params("pfkey", userSessionData.getPfkey())
                                                    .params("msg", userSessionData.getMsg())
                                                    .params("loginCost", userSessionData.getLogin_cost())
                                                    .params("queryAuthorityCost", userSessionData.getQuery_authority_cost())
                                                    .params("authorityCost", userSessionData.getAuthority_cost())
                                                    .params("expiresTime", userSessionData.getExpires_time())
                                                    .params("ulId", userBindUserId) //此条是授权的用户自增ID，以上是拉起授权QQ会话数据
                                                    .execute(new StringCallback() {
                                                        @Override
                                                        public void onSuccess(Response<String> response) {
                                                            ResponseBean responseBean = GsonUtil.gsonToBean(response.body(),ResponseBean.class);
                                                            Log.i(TAG, "onSuccess==: " + responseBean);
                                                            //QQ授权绑定成功
                                                            if (200 == responseBean.getCode() && "success".equals(responseBean.getMsg())){
                                                                DialogPrompt dialogPrompt = new DialogPrompt(LoginBindActivity.this, R.string.qq_bing_success, 3);
                                                                dialogPrompt.showAndFinish(LoginBindActivity.this);
                                                                Intent intent = new Intent();
                                                                intent.putExtra("BindBackName",userBeanData.getData().getUlRealname());
                                                                LoginBindActivity.this.setResult(Constant.RESULT_CODE_BIND_ACCOUNT_SUCCESS,intent);
                                                                return;
                                                            }
                                                            //QQ授权绑定失败
                                                            if (400 == responseBean.getCode() && "error".equals(responseBean.getMsg())){
                                                                DialogPrompt dialogPrompt = new DialogPrompt(LoginBindActivity.this, R.string.qq_bing_error, 3);
                                                                dialogPrompt.showAndFinish(LoginBindActivity.this);
                                                                Intent intent = new Intent();
                                                                intent.putExtra("BindBackName",userBeanData.getData().getUlRealname());
                                                                LoginBindActivity.this.setResult(Constant.RESULT_CODE_BIND_ACCOUNT_ERROR);
                                                            }
                                                        }

                                                        @Override
                                                        public void onError(Response<String> response) {
                                                            //未绑定温馨提示
                                                            Snackbar snackbar = Snackbar.make(mBtn_start_bind, "请求错误，服务器连接失败：" + response.getException(), Snackbar.LENGTH_SHORT)
                                                                    .setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                            snackbar.show();
                                                        }
                                                    });
                                        }
                                    }
                                    @Override
                                    public void onError(Response<String> response) {
                                        //未绑定温馨提示
                                        Snackbar snackbar = Snackbar.make(mBtn_start_bind, "请求错误，服务器连接失败：" + response.getException(), Snackbar.LENGTH_SHORT)
                                                .setActionTextColor(getResources().getColor(R.color.colorAccent));
                                        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                        snackbar.show();
                                    }
                                });
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //未绑定温馨提示
                        Snackbar snackbar = Snackbar.make(mBtn_start_bind, "请求错误，服务器连接失败：" + response.getException(), Snackbar.LENGTH_SHORT)
                                .setActionTextColor(getResources().getColor(R.color.colorAccent));
                        setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                        snackbar.show();
                    }
                });
    }
}
