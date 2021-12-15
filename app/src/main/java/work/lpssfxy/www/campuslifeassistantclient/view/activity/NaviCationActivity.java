package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.baidu.navisdk.adapter.struct.BNTTsInitConfig;
import com.baidu.platform.comapi.location.CoordinateType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/15
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-15-23:38
 */
public class NaviCationActivity extends BaseActivity {

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
        return R.layout.activity_navication;
    }

    @Override
    protected void prepareData() {
//        //导航初始化
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
//        if(sdCardExist)
//        {
//            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//        }
//        BaiduNaviManagerFactory.getBaiduNaviManager().init(getApplicationContext(), sdDir.toString(), "lmap",
//                new IBaiduNaviManager.INaviInitListener() {
//                    @Override
//                    public void onAuthResult(int i, String s) {
//                        if(i==0)
//                        {
//                            Toast.makeText(NaviCationActivity.this, "key校验成功!", Toast.LENGTH_SHORT).show();
//                        }
//                        else if(i==1)
//                        {
//                            Toast.makeText(NaviCationActivity.this, "key校验失败, " + s, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void initStart() {
//
//                    }
//
//                    @Override
//                    public void initSuccess() {
//                        Toast.makeText(NaviCationActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
//                        // 初始化tts
//                        //initTTs();
//                    }
//
//                    @Override
//                    public void initFailed(int i) {
//                        Toast.makeText(NaviCationActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
    }
    private void initTTs() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        BNTTsInitConfig.Builder builder=new BNTTsInitConfig.Builder();
        builder.context(getApplicationContext()).sdcardRootPath(sdDir.toString()).appFolderName("lmap").appId(("填入1.3步骤申请的app id"));
        BaiduNaviManagerFactory.getTTSManager().initTTS( builder.build());

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        Log.e("lmap", "ttsCallback.onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        Log.e("lmap", "ttsCallback.onPlayEnd");
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        Log.e("lmap", "ttsCallback.onPlayError");
                    }
                }
        );
    }

    @Override
    protected void initView() {
        BNRoutePlanNode sNode = new BNRoutePlanNode.Builder()
                .latitude(40.05087)
                .longitude(116.30142)
                .name("百度大厦")
                .description("百度大厦")
                .build();
        BNRoutePlanNode eNode = new BNRoutePlanNode.Builder()
                .latitude(39.90882)
                .longitude(116.39750)
                .name("北京天安门")
                .description("北京天安门")
                .build();
        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);
        BaiduNaviManagerFactory.getRoutePlanManager().routePlanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(NaviCationActivity.this.getApplicationContext(),
                                        "算路开始", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(NaviCationActivity.this.getApplicationContext(),
                                        "算路成功", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(NaviCationActivity.this.getApplicationContext(),
                                        "算路失败", Toast.LENGTH_SHORT).show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(NaviCationActivity.this.getApplicationContext(),
                                        "算路成功准备进入导航", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(NaviCationActivity.this, DemoGuideActivity.class);
//                                startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

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
}
