package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.android.material.tabs.TabLayout;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.xuexiang.xutil.common.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.ViewPagerAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyStringUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XXPermissionsAction;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen.CanteenChaoYangFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.canteen.CanteenLongSHanFragment;

/**
 * created by on 2021/12/14
 * 描述：食堂跑腿代购
 *
 * @author ZSAndroid
 * @create 2021-12-14-20:22
 */
@SuppressLint("NonConstantResourceId")
public class RepastPracticeActivity extends BaseActivity {

    @BindView(R2.id.iv_repast_back) ImageView mIvRepastBack;//返回
    @BindView(R2.id.ll_repast_location) LinearLayout mLlRepastLocation;//当前位置
    @BindView(R2.id.iv_repast_location) ImageView mIvRepastLocation;//当前位置
    @BindView(R2.id.tv_now_location) TextView mTvNowLocation;//当前位置
    @BindView(R2.id.tv_repast_location) TextView mTvRepastLocation;//当前位置
    @BindView(R.id.tab_layout_repast) TabLayout mTabLayoutRepast;//顶部导航栏
    @BindView(R.id.view_pager_repast) ViewPager mViewPagerRepast;//左右滑动

    public LocationClient mLocationClient = null;

    @Override
    protected Boolean isSetSwipeBackLayout() {
        return false;
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
        return R.layout.activity_practice_repast;
    }

    @Override
    protected void prepareData() {
    }

    @Override
    protected void initView() {
        XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            initTabLayoutAndViewPager();
                            initLocationClient();
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            XToastUtils.error("被永久拒绝授权，请手动定位权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(RepastPracticeActivity.this, permissions);
                        } else {
                            XToastUtils.error("获取定位权限失败");
                        }
                    }
                });
        //StatusBarUtil.setPaddingSmart(RepastPracticeActivity.this, toolbar);

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

    private void initLocationClient() {
        mLocationClient = new LocationClient(getApplicationContext());//声明LocationClient类
        mLocationClient.registerLocationListener(new MyLocationListener());//注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedLocationDescribe(true);//获取位置描述信息
        option.setIsNeedAddress(true);//默认false不需要，获得当前点的地址信息，此处必须为true
        option.setNeedNewVersionRgc(true);//默认true需要最新版本的地址信息
        mLocationClient.setLocOption(option);//定位参数设置完成，传递LocationClient对象使用，根据已绑定参数，实现接口回调地址信息
        mLocationClient.start();//开启定位
    }

    @OnClick({R2.id.iv_repast_back,R2.id.ll_repast_location,R2.id.iv_repast_location,R2.id.tv_now_location,R2.id.tv_repast_location})
    public void onRepastViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_repast_back://点击返回
                RepastPracticeActivity.this.finish();
                break;
            case R.id.ll_repast_location://打开地图
            case R.id.iv_repast_location:
            case R.id.tv_now_location:
            case R.id.tv_repast_location:
                IntentUtil.startActivityAnimBottomToTop1(this, new Intent(this, MapViewActivity.class));
                break;
        }
    }

    private void initTabLayoutAndViewPager() {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> tabTitleList = new ArrayList<>();
        Collections.addAll(tabTitleList, "朝阳食堂", "龙山食堂");
        Collections.addAll(fragmentArrayList, CanteenChaoYangFragment.newInstance(), CanteenLongSHanFragment.newInstance());
        mTabLayoutRepast.setTabMode(TabLayout.MODE_FIXED);//设置TabLayout的模式
        //为TabLayout添加tab名称
        for (int i = 0; i < tabTitleList.size(); i++) {
            mTabLayoutRepast.addTab(mTabLayoutRepast.newTab().setText(tabTitleList.get(i)));
        }
        mViewPagerRepast.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentArrayList, tabTitleList));//设置适配器
        mTabLayoutRepast.setupWithViewPager(mViewPagerRepast); //TabLayout绑定ViewPager
    }

    /**
     * 注册定位监听器
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            String adcode = location.getAdCode();    //获取adcode
            String town = location.getTown();    //获取乡镇信息
            String locationDescribe = location.getLocationDescribe();    //获取位置描述信息

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                XToastUtils.error("请先开启GPS位置服务");
            }
            else {
                //在此处进行你的后续联网操作
                mTvRepastLocation.setText(province+city+district+"-"+street+ town+ "-" + locationDescribe.substring(locationDescribe.indexOf("在")+1));
                //Log.i("定位: " ,addr+"-"+country+"-"+province+"-"+city+"-"+district+"-"+street+"-"+adcode+"-"+town + "===" +locationDescribe);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION) && XXPermissions.isGranted(this, Permission.ACCESS_COARSE_LOCATION)) {
                initLocationClient();
                initTabLayoutAndViewPager();
                XToastUtils.warning("GPS定位服务授权完成");
            } else {
                XToastUtils.error("请先开启GPS定位服务",5000);
            }
        }
    }
}
