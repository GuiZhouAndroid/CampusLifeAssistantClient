package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elmargomez.typer.Font;
import com.elmargomez.typer.Typer;
import com.google.android.material.snackbar.Snackbar;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;

public class waimai extends BaseActivity {

    private static final String TAG = "waimai";
    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return false;
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
        return true;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_waimai;
    }

    @Override
    protected void prepareData() {
        showImg();
        UserQQSessionBean userQQSessionBean = SharePreferenceUtil.getObject(waimai.this, UserQQSessionBean.class);
//        Constant.qqUser = SharePreferenceUtil.getObject(IndexActivity.this, QQUserBean.class);
        Log.i(TAG, "首页: " + userQQSessionBean);
//        Log.i(TAG, "首页Constant.qqUser: " + Constant.qqUser);

        JSONObject jsonObject = new JSONObject();
        if (userQQSessionBean != null) {//本地持久化xml文件有数据时才满足重组条件
            try {
                jsonObject.put("ret", userQQSessionBean.getData().getRet());
                jsonObject.put("openid", userQQSessionBean.getData().getOpenid());
                jsonObject.put("access_token", userQQSessionBean.getData().getAccessToken());
                jsonObject.put("pay_token",userQQSessionBean.getData().getPayToken());
                jsonObject.put("expires_in", userQQSessionBean.getData().getExpiresIn());
                jsonObject.put("pf", userQQSessionBean.getData().getPf());
                jsonObject.put("pfkey", userQQSessionBean.getData().getPfkey());
                jsonObject.put("msg", userQQSessionBean.getData().getMsg());
                jsonObject.put("login_cost",userQQSessionBean.getData().getLoginCost());
                jsonObject.put("query_authority_cost", userQQSessionBean.getData().getQueryAuthorityCost());
                jsonObject.put("authority_cost", userQQSessionBean.getData().getAuthorityCost());
                jsonObject.put("pfkey", userQQSessionBean.getData().getPfkey());
                jsonObject.put("expires_time", userQQSessionBean.getData().getExpiresTime());
                /** 初始化设置上次授权登录的Session信息——来自持久化重组JSon数据顺序*/
                Constant.mTencent.initSessionCache(jsonObject);
                Log.i(TAG, "mTencent初始化后会话Session是否有效: " + Constant.mTencent.isSessionValid());//true
            } catch (JSONException e) {//异常捕捉
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void initView() {
        /** 注册广播接收者——更新UI*/
        IntentFilter filter = new IntentFilter(LoginActivity.action);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {
        if (Constant.mTencent != null && Constant.mTencent.isSessionValid()) { //
            IUiListener listener = new DefaultUiListener() {
                /** 以下进行对获取授权用户信息使用业务，这里存入本地，以及发送广播传刀IndexActivity并更新首页UI */
                @Override
                public void onComplete(final Object response) {
                    Log.d(TAG, "请求回调用户信息列表= " + response.toString());
//                    /** 调用Gson工具类，回掉的JSON数据，转化为Java对象*/
//                    Constant.qqUser = GsonUtil.gsonToBean(response.toString(), QQUserBean.class);
//                    /** 调用SharePreference工具类把Gson转化后的Java对象实现数据持久化，文件名为“ZSAndroid”的本地数据*/
//                    SharePreferenceUtil.putObject(LoginActivity.this, Constant.qqUser);
//                    Log.i(TAG, "qqUser全部数据: " + Constant.qqUser);
//                    /** 通过Intent发送广播消息，*/
//                    Intent intent = new Intent(action);
//                    /**创建捆绑实例，Intent传递Java对象*/
//                    Bundle bundle = new Bundle();
//                    /** Java对象序列化存入Intent */
//                    bundle.putSerializable("QQUserBean", Constant.qqUser);
//                    /** 发送Intent序列化数据至Bundle捆绑对象*/
//                    intent.putExtras(bundle);
//                    Log.i(TAG, "bundle: " + bundle.toString());
//                    /** 发送广播，接受者通过“QQUserBean”接收广播消息内容 */
//                    sendBroadcast(intent);
                }

                @Override
                public void onCancel() {
                    Toast.makeText(waimai.this, "取消获取授权用户信息", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(UiError e) {
                    Toast.makeText(waimai.this, "获取授权用户信息出错：" + e.errorDetail, Toast.LENGTH_SHORT).show();
                }
            };
            /** 根据Constant.mTencent会话中TOKEN值，请求回调授权用户信息列表*/
            UserInfo info = new UserInfo(this, Constant.mTencent.getQQToken());
            Log.i(TAG, "外卖QQToken====: " + Constant.mTencent.getQQToken().toString());
            /** 开始监听请求回调操作*/
            info.getUserInfo(listener);

        } else {
            Toast.makeText(this, "QQ无效Session", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 业务操作
     */
    @Override
    protected void doBusiness() {

    }

    /**
     * 使用Glide加载显示网络图片 记得加网络权限和http地址url访问许可
     */
    private void showImg() {
        Glide.with(this)
                .load("http://gank.io/images/2c924db2a1b84c5d8fdb9f8c5f6d1b71")
                .into((ImageView) findViewById(R.id.iv_one));
        Glide.with(this)
                .load("http://gank.io/images/92989b6a707b44dfb1c734e8d53d39a2")
                .into((ImageView) findViewById(R.id.iv_two));
        Glide.with(this)
                .load("http://gank.io/images/4817628a6762410895f814079a6690a1")
                .into((ImageView) findViewById(R.id.iv_three));
        Glide.with(this)
                .load("http://gank.io/images/f9523ebe24a34edfaedf2dd0df8e2b99")
                .into((ImageView) findViewById(R.id.iv_four));
        Glide.with(this)
                .load("http://gank.io/images/4002b1fd18544802b80193fad27eaa62")
                .into((ImageView) findViewById(R.id.iv_five));
    }

    /**
     * 接受QQ登录广播，数据来源于广播，不是本地持久化数据文件，因此需要严格区分
     */
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("CheckResult")
        @Override
        public void onReceive(Context context, Intent intent) {
            /** 根据Gson转化后的Java对象 Intent序列化的键获取广播消息内容*/
            UserQQSessionBean userQQSessionBean = (UserQQSessionBean) intent.getSerializableExtra("UserQQSessionBean");
            Log.i(TAG, "BroadcastReceiver的QQUserBean: " + userQQSessionBean);
        }
    };
}