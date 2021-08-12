package work.lpssfxy.www.campuslifeassistantclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.LoadingDialog;

/**
 * created by on 2021/8/12
 * 描述：登录
 *
 * @author ZSAndroid
 * @create 2021-08-12-2:07
 */
public class LoginActivity extends BaseActivity{
    private TextView tv_login;
    private TextView tv_go_register;
    //微信
    private ImageView iv_login_wx;
    //QQ
    private ImageView iv_login_qq;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //设置状态栏颜色
//        StatusBarUtils.setStatusColor(getWindow(), ContextCompat.getColor(this, R.color.main_status_bar_blue),1f);

        //点击登录
        tv_login=findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        //跳转注册页
        tv_go_register=findViewById(R.id.tv_go_register);
        tv_go_register.setOnClickListener(this);
        //微信登录
        iv_login_wx=findViewById(R.id.iv_login_wx);
        iv_login_wx.setOnClickListener(this);
        //QQ登录
        iv_login_qq=findViewById(R.id.iv_login_qq);
        iv_login_qq.setOnClickListener(this);

    }

    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //点击登录
            case R.id.tv_login:
                LoadingDialog.showDialog(this);
//                startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                finish();
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                break;
            //跳转注册页
            case R.id.tv_go_register:
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                finish();
//                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                break;
            //微信登录
            case R.id.iv_login_wx:
//                Platform wechat= ShareSDK.getPlatform(Wechat.NAME);
//                wechat.setPlatformActionListener(new PlatformActionListener() {
//                    @Override
//                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                        if(i==Platform.ACTION_USER_INFOR){
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(Platform platform, int i, Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onCancel(Platform platform, int i) {
//
//                    }
//                });
//
//                wechat.authorize();

                break;
            //QQ登录
            case R.id.iv_login_qq:
                //getQQ();
                break;
        }
    }

    /**
     * 第三方登录QQ
     */
//    public void getQQ(){
//
//        Platform plat = ShareSDK.getPlatform(QQ.NAME);
//        plat.removeAccount(false); //移除授权状态和本地缓存，下次授权会重新授权
//        plat.SSOSetting(false); //SSO授权，传false默认是客户端授权，没有客户端授权或者不支持客户端授权会跳web授权
//        plat.setPlatformActionListener(new PlatformActionListener() {//授权回调监听，监听oncomplete，onerror，oncancel三种状态
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                Iterator iterator = hashMap.entrySet().iterator();
//                while (iterator.hasNext()){
//                    Map.Entry next = (Map.Entry) iterator.next();
//                    Object key = next.getKey();
//                    Object value = next.getValue();
//                    Log.d("哈哈", "onComplete: "+key+"  "+value);
//
//                }
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                Log.d("哈哈", "onError: "+platform.getName()+" "+platform.getDb()+"  "+"  "+platform.getId()+"  "+platform.getPlatformActionListener()+" "+platform.getVersion());
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                Log.d("哈哈", "onCancel: "+platform.getName());
//            }
//        });
//
//
//        if (plat.isClientValid()) {
//            //todo 判断是否存在授权凭条的客户端，true是有客户端，false是无
//        }
//        if (plat.isAuthValid()) {
//            //todo 判断是否已经存在授权状态，可以根据自己的登录逻辑设置
//            Toast.makeText(this, "已经授权过了", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //要功能，不要数据
//        // plat.authorize();
//        //要数据不要功能，主要体现在不会重复出现授权界面
//        plat.showUser(null);
//    }
}