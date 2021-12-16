package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/15
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-12-15-18:40
 */
@SuppressLint("NonConstantResourceId")
public class MapViewActivity extends BaseActivity {

    @BindView(R2.id.map_view) MapView mMapView;//百度地图控件
    @BindView(R2.id.tv_now_poi) TextView mTvNowPoi;//poi
    @BindView(R2.id.btn_my_location) Button mBtmMyLocation;//我的位置
    @BindView(R2.id.btn_wxt) Button mBtnWxt;//卫星图
    @BindView(R2.id.btn_normal) Button mBtnNormal;//普通图

    public LocationClient mLocationClient = null;
    private boolean isFirstLoc = true; // 是否首次定位

    private BaiduMap mBaiduMap;//百度地图

    private LatLng latLng;
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
        return R.layout.activity_map_view;
    }

    @Override
    protected void prepareData() {
        XXPermissions.with(this)
                // 申请单个权限
                .permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            intBadDuMap();//判断定位权限后初始化百度地图
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            XToastUtils.error("被永久拒绝授权，请手动定位权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MapViewActivity.this, permissions);
                        } else {
                            XToastUtils.error("获取定位权限失败");
                        }
                    }
                });
    }

    @Override
    protected void initView() {
        intBadDuMap();//判断定位权限后初始化百度地图
    }
    /**
     * 初始化百度地图View
     */
    private void intBadDuMap() {
        /* 1 获取地图控件引用 .*/
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);//普通地图
        mBaiduMap.setTrafficEnabled(true);//开启交通图
        mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");//定制交通图模板
        mBaiduMap.setMyLocationEnabled(true);//开启地图的定位图层
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(13);//对地图状态做更新，否则可能不会触发渲染，造成样式定义无法立即生效
        //mBaiduMap.setBaiduHeatMapEnabled(true);//开启热力图
        mBaiduMap.animateMapStatus(u);

        // 2.自定义定制地图内容
        // 定位精度圈大小:是根据当前定位精度自动控制的，无法手动控制大小。精度圈越小，代表当前定位精度越高；反之圈越大，代表当前定位精度越低。
        // 定位指针方向:是通过获取手机系统陀螺仪数据，控制定位指针的方向，需要开发者自己实现，并不在地图实现范畴。
        MyLocationConfiguration.LocationMode locationMode =MyLocationConfiguration.LocationMode.FOLLOWING;//定位模式
        boolean enableDirection = true;//是否开启方向
        BitmapDescriptor DmCurrentMarker = BitmapDescriptorFactory.fromResource(R.mipmap.studentlogo);//自定义定位图标
        int accuracyCircleFillColor = 0xAAFFFF88;//自定义精度圈填充颜色
        int accuracyCircleStrokeColor = 0xAA00FF00;//自定义精度圈边框颜色
        MyLocationConfiguration myLocationConfiguration = new MyLocationConfiguration(locationMode,enableDirection,DmCurrentMarker,accuracyCircleFillColor,accuracyCircleStrokeColor);
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration);//定义了以上属性之后，通过如下方法来设置生效
        /* 3.声明LocationClient定位类 .*/
        mLocationClient = new LocationClient(getApplicationContext());
        /* 4.配置定位参数 */
        initLocation();
        /* 5.注册LocationListener监听器函数 */
        mLocationClient.registerLocationListener(new MyLocationListener());
        /* 6.开启定位 */
        mLocationClient.start();
        /* 7.地图图片点击事件，回到当前定位经纬度点 */
        mLocationClient.requestLocation();
    }

    /**
     * 配置定位参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedLocationPoiList(true);//是否需要周边POI信息，默认为不需要，即参数为false
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，设置坐标类型默认gcj02，设置返回的定位结果坐标系
        option.setScanSpan(1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        //.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);//绑定定位参数
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

    @OnClick({R2.id.btn_my_location,R2.id.btn_wxt,R2.id.btn_normal})
    public void onRepastViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_my_location://我的位置
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
            case R.id.btn_wxt://卫星图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.btn_normal://普通图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }
            latLng = new LatLng(location.getLatitude(), location.getLongitude());//纬度 + 经度
            // 构造定位数据，在当前经纬度处绘制当前位置蓝色小图标
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())// 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0)//当前logo的放向度数
                    .latitude(location.getLatitude())//纬度
                    .longitude(location.getLongitude()).build();//经度
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                //POI周边
                Poi poi = location.getPoiList().get(0);
                String poiName = poi.getName();    //获取POI名称
                String poiTags = poi.getTags();    //获取POI类型
                String poiAddr = poi.getAddr();    //获取POI地址 //获取周边POI信息
                mTvNowPoi.setText("当前实时位置："+ poiAddr+"-"+poiTags+"-"+poiName);

                //室内定位
                if (location.getFloor() != null) {
                    // 当前支持高精度室内定位
                    String buildingID = location.getBuildingID();// 百度内部建筑物ID
                    String buildingName = location.getBuildingName();// 百度内部建筑物缩写
                    String floor = location.getFloor();// 室内定位的楼层信息，如 f1,f2,b1,b2
                    mLocationClient.startIndoorMode();// 开启室内定位模式（重复调用也没问题），开启后，定位SDK会融合各种定位信息（GPS,WI-FI，蓝牙，传感器等）连续平滑的输出定位结果；
                    ToastUtils.show("当前处于室内定位状态："+buildingID+"-"+buildingName+"-"+floor);
                }
                //构造地图状态参数
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                //对地图状态做更新，否则可能不会触发渲染，造成样式定义无法立即生效
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(MapViewActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(MapViewActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(MapViewActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(MapViewActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(MapViewActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(MapViewActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();//停止定位服务
        mBaiduMap.setMyLocationEnabled(false);//关闭当前定位信息和状态参数
        mMapView.onDestroy();//释放地图资源
        mMapView = null;
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //从下往下结束动画
        IntentUtil.finishActivityAnimTopToBottom(MapViewActivity.this);
    }
}
