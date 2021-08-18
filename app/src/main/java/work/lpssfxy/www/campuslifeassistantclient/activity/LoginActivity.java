package work.lpssfxy.www.campuslifeassistantclient.activity;

import static com.tencent.connect.common.Constants.KEY_ENABLE_SHOW_DOWNLOAD_URL;
import static com.tencent.connect.common.Constants.KEY_QRCODE;
import static com.tencent.connect.common.Constants.KEY_RESTORE_LANDSCAPE;
import static com.tencent.connect.common.Constants.KEY_SCOPE;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.AuthAgent;
import com.tencent.connect.common.Constants;
import com.tencent.open.log.SLog;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.Util;
import work.lpssfxy.www.campuslifeassistantclient.utils.permission.PermissionMgr;

/**
 * created by on 2021/8/12
 * 描述：登录
 *
 * @author ZSAndroid
 * @create 2021-08-12-2:07
 */
public class LoginActivity extends BaseActivity{
    private static final String TAG = LoginActivity.class.getName();
    private TextView tv_login;
    private TextView tv_go_register;
    //微信
    private ImageView iv_login_wx;
    //QQ
    private ImageView iv_login_qq;
    public static Tencent mTencent;
    private static boolean isServerSideLogin = false;
    private static Intent mPrizeIntent = null;
    private Button mNewLoginButton;
    private Button mServerSideLoginBtn;
    private CheckBox mCheckForceQr;

    private CheckBox mQrCk;
    private CheckBox mOEMLogin;

    private CheckBox mShowWebDownloadUi;

    private TextView mUserInfo;
    private ImageView mUserLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "-->onCreate开始");
        setContentView(R.layout.login_activity);
        initViews();
        mTencent = Tencent.createInstance("101965703", LoginActivity.this, "work.lpssfxy.www.campuslifeassistantclient.fileprovider");
        if (mTencent == null) {
            SLog.e(TAG, "腾讯实例创建失败");
            finish();
        }
        Log.i(TAG, "-->onCreate结束+腾讯实例创建成功 ");

        // 获取有奖分享的intent信息
        if (null != getIntent()) {
            mPrizeIntent = getIntent();
        }
        // 有奖分享处理
        handlePrizeShare();

        Map<String, String> params = Tencent.parseMiniParameters(getIntent());
        if (!params.isEmpty()) {
            Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();

            StringBuffer sBuf = new StringBuffer();
            while(iter.hasNext()) {
                Map.Entry<String, String> entry= iter.next();
                sBuf.append(entry.getKey() + "=" + entry.getValue()).append(" ");
            }

            Toast.makeText(this, sBuf.toString(), Toast.LENGTH_LONG).show();
        }

        PermissionMgr.getInstance().requestPermissions(this);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionMgr.getInstance().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 有奖分享处理，未接入有奖分享可以不考虑
     */
    private void handlePrizeShare() {

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "-->onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "-->onResume");
        // 有奖分享处理
        handlePrizeShare();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "-->onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "-->onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "-->onDestroy");
        super.onDestroy();
    }

    private void initViews() {
        mNewLoginButton = (Button) findViewById(R.id.new_login_btn);
        mNewLoginButton.setOnClickListener(this);

        mServerSideLoginBtn = (Button) findViewById(R.id.server_side_login_btn);
        mServerSideLoginBtn.setOnClickListener(this);

        mQrCk = (CheckBox) findViewById(R.id.ck_qr);
        mQrCk.setOnClickListener(this);

        mCheckForceQr = (CheckBox) findViewById(R.id.check_force_qr);
        mCheckForceQr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.getId() == R.id.check_force_qr) {
                    mQrCk.setChecked(b);
                }
            }
        });
        mOEMLogin = (CheckBox)findViewById(R.id.check_oem_login);
        mShowWebDownloadUi = (CheckBox)findViewById(R.id.show_web_download_ui);
        mUserInfo = (TextView) findViewById(R.id.user_nickname);
        mUserLogo = (ImageView) findViewById(R.id.user_logo);
        updateLoginButton();
    }

    private void updateLoginButton() {
        if (mTencent != null && mTencent.isSessionValid()) {
            if (isServerSideLogin) {
                mNewLoginButton.setTextColor(Color.BLUE);
                mNewLoginButton.setText("登录");
                mServerSideLoginBtn.setTextColor(Color.RED);
                mServerSideLoginBtn.setText("退出Server-Side账号");
            } else {
                mNewLoginButton.setTextColor(Color.RED);
                mNewLoginButton.setText("退出帐号");
                mServerSideLoginBtn.setTextColor(Color.BLUE);
                mServerSideLoginBtn.setText("Server-Side登录");
            }
        } else {
            mNewLoginButton.setTextColor(Color.BLUE);
            mNewLoginButton.setText("登录");
            mServerSideLoginBtn.setTextColor(Color.BLUE);
            mServerSideLoginBtn.setText("Server-Side登录");
        }
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        Class<?> cls = null;
        boolean isAppbar = false;
        switch (view.getId()){
            case R.id.new_login_btn:
            onClickLogin();
            view.startAnimation(shake);
            break;

        }
        view.startAnimation(shake);
        if (cls != null) {
            Intent intent = new Intent(context, cls);
            if (isAppbar) { //APP内应用吧登录需接收登录结果
                startActivityForResult(intent, Constants.REQUEST_APPBAR);
            } else {
                context.startActivity(intent);
            }
        }
    }
    private void onClickLogin() {
        if (!mTencent.isSessionValid()) {
            // 强制扫码登录
            this.getIntent().putExtra(AuthAgent.KEY_FORCE_QR_LOGIN, mCheckForceQr.isChecked());

            if (mOEMLogin.isChecked()) {
                mTencent.loginWithOEM(this, "all", loginListener, mQrCk.isChecked(), "10000144","10000144","xxxx");
            } else {
                HashMap<String, Object> params = new HashMap<String, Object>();
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                    params.put(KEY_RESTORE_LANDSCAPE, true);
                }


                params.put(KEY_SCOPE, "all");
                params.put(KEY_QRCODE, mQrCk.isChecked());
                params.put(KEY_ENABLE_SHOW_DOWNLOAD_URL, mShowWebDownloadUi.isChecked());
                mTencent.login(this, loginListener, params);
            }
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登录, 先退出，再进行SSO登录
                mTencent.logout(this);
                mTencent.login(this, "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(this);
            // 第三方也可以选择注销的时候不去清除第三方的targetUin/targetMiniAppId
//            saveTargetUin("");
//            saveTargetMiniAppId("");
            updateUserInfo();
            updateLoginButton();
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
            updateUserInfo();
            updateLoginButton();
        }
    };

    private class BaseUiListener extends DefaultUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (jsonResponse.length() == 0) {
                Util.showResultDialog(LoginActivity.this, "返回为空", "登录失败");
                return;
            }
            Util.showResultDialog(LoginActivity.this, response.toString(), "登录成功");
            // 有奖分享处理
            handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(LoginActivity.this, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(LoginActivity.this, "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
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
        } catch(Exception e) {
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
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread(){

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            if(json.has("figureurl")){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {
                                    SLog.e(TAG, "Util.getBitmap() jsonException : " + e.getMessage());
                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
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
            mUserInfo.setText("");
            mUserInfo.setVisibility(android.view.View.GONE);
            mUserLogo.setVisibility(android.view.View.GONE);
        }
    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        mUserInfo.setVisibility(android.view.View.VISIBLE);
                        mUserInfo.setText(response.getString("nickname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                mUserLogo.setImageBitmap(bitmap);
                mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "-->onActivityResult " + requestCode  + " resultCode=" + resultCode);
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}