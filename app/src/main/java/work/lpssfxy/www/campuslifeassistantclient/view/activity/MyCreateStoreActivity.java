package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.luck.picture.lib.PictureMediaScannerConnection;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.broadcast.BroadcastManager;
import com.luck.picture.lib.camera.CustomCameraType;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.dialog.PictureCustomDialog;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.listener.OnCallbackListener;
import com.luck.picture.lib.listener.OnPermissionDialogOptionCallback;
import com.luck.picture.lib.listener.OnPermissionsObtainCallback;
import com.luck.picture.lib.manager.PictureCacheManager;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.luck.picture.lib.style.PictureSelectorUIStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.tools.MediaUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.edittext.ClearEditText;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.yoma.roundbutton.RoundButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridStoreImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridStoreLogoImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.button.NotFastButton;
import work.lpssfxy.www.campuslifeassistantclient.base.pogress.CircleProgress;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllShopCategoryInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyStringUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.UUIDUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.FullyGridLayoutManager;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.GlideEngine;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.OssManager;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/12
 * 描述：商家开店
 *
 * @author ZSAndroid
 * @create 2021-12-12-20:20
 */
@SuppressLint("NonConstantResourceId")
public class MyCreateStoreActivity extends BaseActivity implements SuperTextView.OnSuperTextViewClickListener{

    private static final String TAG = "MyCreateStoreActivity";

    /** 获取View控件 */
    @BindView(R2.id.fl_create_store_show) RelativeLayout mrlCreateStoreShow; //标题栏
    @BindView(R2.id.iv_create_store_back) ImageView mIvCreateStoreBack;  //标题栏返回
    @BindView(R2.id.recycler_create_store_pic) RecyclerView mRecycleCreateStorePic; //门店实图列表控件
    @BindView(R2.id.recycler_create_store_logo) RecyclerView mRecycleCreateStoreLogo; //店铺Logo图列表控件
    @BindView(R2.id.super_create_store_category) SuperTextView mStvCreateStoreCategory;//店铺分类
    @BindView(R2.id.edit_create_store_name) ClearEditText mEditCreateStoreName;//商铺名称
    @BindView(R2.id.edit_create_store_notice) ClearEditText mEditCreateStoreNotice;//商铺公告
    @BindView(R2.id.edit_create_store_address) ClearEditText mEditCreateStoreAddress;//商铺详细地址
    @BindView(R2.id.edit_create_store_mobile) ClearEditText mEditCreateStoreMobile;//联系电话
    @BindView(R2.id.edit_create_store_recommend) ClearEditText mEditCreateStoreRecommend;//推荐商品
    @BindView(R2.id.super_create_store_desc) SuperTextView mStvCreateStoreDesc;//商铺所属校区
    @BindView(R2.id.super_create_store_begin_time) SuperTextView mStvCreateStoreBeginTime;//营业开始时间
    @BindView(R2.id.super_create_store_end_time) SuperTextView mStvCreateStoreEndTime;//营业结束时间
    @BindView(R2.id.rbn_create_store_ok) RoundButton mRbnCreateStoreOk; //立即开店
    @BindView(R2.id.circle_progress_create_store) CircleProgress mCircleProgressCreateStore; //进度条
    private final static int[] COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
    /** 图片选择器基本参数 */
    private PictureParameterStyle mPictureParameterStyle;//全局主题
    private PictureCropParameterStyle mCropParameterStyle;//裁剪主题
    private final int maxSelectNum = 1;//最大选择数
    /** 接收相册中选择图片的回调到Activity中 */
    private ActivityResultLauncher<Intent> launcherStoreResult;//回调显示门店实图
    private ActivityResultLauncher<Intent> launcherStoreLogoResult;//回调店铺Logo图
    /** 网格布局相册门店实图 + 店铺Logo图适配器 */
    private GridStoreImageAdapter mAdapterStore;
    private GridStoreLogoImageAdapter mAdapterStoreLogo;
    /** 门店实图片路径 + 店铺Logo图路径 */
    public static String imgPathStore;
    public static String imgPathStoreLogo;
    public static List<String> imgApplyStorePathList;//门店实图片路径 + 店铺Logo图路径
    /** Android振动器 */
    private Vibrator vibrator;
    /** 商铺分类集合 */
    private List<String> stringCategoryNameList;
    /** 选择营业时间 */
    private String[][] mChooseBeginTime,mChooseEndTime;
    /** 输入框中的全局数据，提供网络请求提交信息时使用 */
    private String strGetCategoryName,strGetStoreName,strGetStoreDesc,strGetStoreNotice,strGetStoreAddress,strGetStoreMobile,strGetStoreRecommend,strGetBeginTime,strGetEndTime;
    /** 提取商铺分类ID */
    private int categoryId;
    /** 百度定位 */
    public LocationClient mLocationClient = null;

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
        return R.layout.activity_my_create_store;
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
                            initLocationClient();
                        }
                    }
                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            initLocationClient();
                            XToastUtils.error("被永久拒绝授权，请手动定位权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(MyCreateStoreActivity.this, permissions);
                        } else {
                            XToastUtils.error("获取定位权限失败");
                        }
                    }
                });
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
    @Override
    protected void initView() {
        imgApplyStorePathList = new ArrayList<>();
        imgApplyStorePathList.add(0, "");
        imgApplyStorePathList.add(1, "");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPictureParameterStyle = getWeChatStyle();//设置全局主题为微信风格
        initCreateStorePicSelect(savedInstanceState);//初始化门店实拍图片选择适配器配置参数
        initCreateStoreLogoSelect(savedInstanceState);//初始化店铺Logo图片选择适配器配置参数
        initAllRecyclerView();//初始化全部图片view列表
    }

    private void initAllRecyclerView() {
        /**自定义全局统一网格垂直布局 */
        /* 1.门店实拍图片 */
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mRecycleCreateStorePic.setLayoutManager(manager);//设置RecyclerView的布局样式
        mRecycleCreateStorePic.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCreateStorePic.setAdapter(mAdapterStore);//设置RecyclerView的适配器
        /* 2.店铺Logo图片 */
        FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mRecycleCreateStoreLogo.setLayoutManager(manager1);//设置RecyclerView的布局样式
        mRecycleCreateStoreLogo.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCreateStoreLogo.setAdapter(mAdapterStoreLogo);//设置RecyclerView的适配器
    }

    @Override
    protected void initEvent() {
        mStvCreateStoreCategory.setOnSuperTextViewClickListener(this);//店铺分类
    }

    @Override
    protected void initListener() {
        //避免快速点击，提交开店信息，导致服务器错误。
        mRbnCreateStoreOk.setOnClickListener(new NotFastButton.NotFastClickListener() {
            @Override
            public void onNotFastClick(View v) {
                startCreateStoreInfo();
            }
        });
    }

    @Override
    protected void doBusiness() {
        initStoreCategoryData();
    }

    /**
     * 初始化获取商铺分类信息
     */
    private void initStoreCategoryData() {
        OkGo.<String>post(Constant.SELECT_ALL_SHOP_CATEGORY_INFO)
                .tag("全部店铺分类信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(MyCreateStoreActivity.this, "正在加载信息...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoAllShopCategoryInfoBean okGoAllShopCategoryInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllShopCategoryInfoBean.class);
                        stringCategoryNameList = new ArrayList<>();
                        //提取商铺分类集合信息
                        if (200 == okGoAllShopCategoryInfoBean.getCode() && okGoAllShopCategoryInfoBean.getData().size() > 0 && "success".equals(okGoAllShopCategoryInfoBean.getMsg())) {
                            for (OkGoAllShopCategoryInfoBean.Data data : okGoAllShopCategoryInfoBean.getData()) {
                                stringCategoryNameList.add("[编号" + data.getScId() + "]" + data.getScName());
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, MyCreateStoreActivity.this, mrlCreateStoreShow, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 定义上下文
     *
     * @return 当前上下文
     */
    public Context getContext() {
        return this;
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_create_store_back, R2.id.super_create_store_desc, R2.id.super_create_store_begin_time, R2.id.super_create_store_end_time})
    public void onCreateStoreViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_create_store_back://点击返回
                MyCreateStoreActivity.this.finish();
                break;
            case R.id.super_create_store_desc://点击选择商铺所在校区
                chooseDesc();
                break;
            case R.id.super_create_store_begin_time://点击选择营业开始时间
                chooseBeginTime();
                break;
            case R.id.super_create_store_end_time://点击选择营业结束时间
                chooseEndTime();
                break;
        }
    }

    /**
     * 选择商铺所在校区
     */

    private void chooseDesc() {
        CommonPickerPopup popup = new CommonPickerPopup(MyCreateStoreActivity.this);
        ArrayList<String> list = new ArrayList<String>();
        list.add("龙山校区");
        list.add("朝阳校区");
        //默认选择男
        popup.setPickerData(list).setCurrentItem(0);
        popup.setCommonPickerListener(new CommonPickerListener() {
            @Override
            public void onItemSelected(int index, String descName) {
                mStvCreateStoreDesc.setRightString(descName);
            }
        });
        new XPopup.Builder(MyCreateStoreActivity.this)
                .asCustom(popup)
                .show();
    }

    /**
     * 获取时间段
     *
     * @param interval 时间间隔（分钟）
     * @return
     */
    public String[] getTimePeriod(int startHour, int totalHour, int interval) {
        String[] time = new String[totalHour * 60 / interval];
        int point, hour, min;
        for (int i = 0; i < time.length; i++) {
            point = i * interval + startHour * 60;
            hour = point / 60;
            min = point - hour * 60;
            time[i] = (hour < 9 ? "0" + hour : "" + hour) + ":" + (min < 9 ? "0" + min : "" + min);
        }
        return time;
    }

    /**
     * 选择营业开始时间
     */
    private void chooseBeginTime() {
        if (mChooseBeginTime == null) {
            mChooseBeginTime = new String[5][];
            mChooseBeginTime[0] = getTimePeriod(0, 6, 15);
            mChooseBeginTime[1] = getTimePeriod(6, 6, 15);
            mChooseBeginTime[2] = getTimePeriod(12, 1, 15);
            mChooseBeginTime[3] = getTimePeriod(1, 5, 15);
            mChooseBeginTime[4] = getTimePeriod(6, 6, 15);
        }
        String[] option = new String[]{"凌晨", "上午", "中午", "下午", "晚上"};
        //8点
        int defaultOption = 2 * 60 / 15;
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            mStvCreateStoreBeginTime.setRightString(option[options1] + mChooseBeginTime[options1][options2]);
            return false;
        })
                .setTitleText("选择营业开始时间")
                .isRestoreItem(true)
                .setSelectOptions(1, defaultOption)
                .build();
        pvOptions.setPicker(option, mChooseBeginTime);
        pvOptions.show();
    }

    /**
     * 选择营业结束时间
     */
    private void chooseEndTime() {
        if (mChooseEndTime == null) {
            mChooseEndTime = new String[5][];
            mChooseEndTime[0] = getTimePeriod(0, 6, 15);
            mChooseEndTime[1] = getTimePeriod(6, 6, 15);
            mChooseEndTime[2] = getTimePeriod(12, 1, 15);
            mChooseEndTime[3] = getTimePeriod(1, 5, 15);
            mChooseEndTime[4] = getTimePeriod(6, 6, 15);
        }
        String[] option = new String[]{"凌晨", "上午", "中午", "下午", "晚上"};
        //8点
        int defaultOption = 2 * 60 / 15;
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), (v, options1, options2, options3) -> {
            mStvCreateStoreEndTime.setRightString(option[options1] + mChooseEndTime[options1][options2]);
            return false;
        })
                .setTitleText("选择营业结束时间")
                .isRestoreItem(true)
                .setSelectOptions(1, defaultOption)
                .build();
        pvOptions.setPicker(option, mChooseEndTime);
        pvOptions.show();
    }


    @Override
    public void onClick(SuperTextView superTextView) {
        switch (superTextView.getLeftString()) {
            case "店铺分类":
                doStoreCategoryClick();
                break;
        }
    }

    /**
     * 选择店铺分类
     */
    private void doStoreCategoryClick() {
        if (stringCategoryNameList.size() > 0) {
            String[] strings = new String[stringCategoryNameList.size()];
            for (int i = 0; i < stringCategoryNameList.size(); i++) {
                strings[i] = stringCategoryNameList.get(i);
            }
            new XPopup.Builder(getContext())
                    .isDarkTheme(true)
                    .hasShadowBg(true)
                    .isViewMode(true)
                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                    .asBottomList("请选择店铺分类", strings,
                            new OnSelectListener() {
                                @Override
                                public void onSelect(int position, String categoryName) {
                                    //设置ID对应的分类名称
                                    mStvCreateStoreCategory.setRightString(categoryName.substring(categoryName.indexOf("]") + 1));
                                    //提取商铺分类ID
                                    categoryId = Integer.parseInt(MyStringUtils.subString(categoryName, "[编号", "]"));
                                }
                            }).show();
        } else {
            XToastUtils.error("请求错误，服务器连接失败");
        }
    }

    /**
     * 提交开店请求
     */
    private void startCreateStoreInfo() {
        /* 1.判空 */
        if (imgPathStore == null) {
            ToastUtils.show("请选择门店实拍图");
            return;
        }
        if (imgPathStoreLogo == null) {
            ToastUtils.show("请选择店铺Logo图");
            return;
        }
        strGetCategoryName = mStvCreateStoreCategory.getRightString();//商铺分类
        strGetStoreName = mEditCreateStoreName.getText().toString().trim();//商铺名称
        strGetStoreNotice = mEditCreateStoreNotice.getText().toString().trim();//商铺公告
        strGetStoreAddress = mEditCreateStoreAddress.getText().toString().trim();//商铺地址
        strGetStoreMobile = mEditCreateStoreMobile.getText().toString().trim();//联系电话
        strGetStoreRecommend = mEditCreateStoreRecommend.getText().toString().trim();//推荐商品
        strGetStoreDesc = mStvCreateStoreDesc.getRightString();//商铺所属校区
        strGetBeginTime = mStvCreateStoreBeginTime.getRightString();//营业开始时间
        strGetEndTime = mStvCreateStoreEndTime.getRightString();//营业结束时间

        if (strGetCategoryName.equals("请选择")) {
            strGetCategoryName = "";
        }
        if (strGetStoreDesc.equals("请选择")) {
            strGetStoreDesc = "";
        }
        if (strGetBeginTime.equals("请选择")) {
            strGetBeginTime = "";
        }
        if (strGetEndTime.equals("请选择")) {
            strGetEndTime = "";
        }
        if (strGetCategoryName.isEmpty()) {
            ToastUtils.show("请选择店铺分类");
            return;
        }
        if (TextUtils.isEmpty(strGetStoreName)) {
            ToastUtils.show("请完善商铺名称信息");
            return;
        }
        if (TextUtils.isEmpty(strGetStoreNotice)) {
            ToastUtils.show("请完善商铺公告信息");
            return;
        }
        if (TextUtils.isEmpty(strGetStoreAddress)) {
            ToastUtils.show("请完善商铺地址信息");
            return;
        }
        if (TextUtils.isEmpty(strGetStoreMobile)) {
            ToastUtils.show("请完善联系电话信息");
            return;
        }
        if (TextUtils.isEmpty(strGetStoreRecommend)) {
            ToastUtils.show("请完善推荐商品信息");
            return;
        }
        if (TextUtils.isEmpty(strGetStoreDesc)) {
            ToastUtils.show("请选择商铺所属校区");
            return;
        }
        if (TextUtils.isEmpty(strGetBeginTime)) {
            ToastUtils.show("请选择营业开始时间");
            return;
        }
        if (TextUtils.isEmpty(strGetEndTime)) {
            ToastUtils.show("请选择营业结束时间");
            return;
        }
        /* 2.上传图片到阿里云OSS */
        if (imgApplyStorePathList != null && imgApplyStorePathList.size() == 2) {
            startOSSUploadFile(imgApplyStorePathList);
        }
    }

//    /**
//     * 上传集合中的图片到阿里云OSS对象存储
//     * @param imgApplyStorePathList 路径集合
//     */
//    private void startOSSUploadFile(List<String> imgApplyStorePathList) {
//        //时间戳使用原因 + UUID：避免OSS云资源重名覆盖文件，导致用户数据丢失，加入时间戳来拼接在OSS文件名中，那么必然不会出现重名问题
//        String fileName = System.currentTimeMillis() + UUIDUtil.UUID32() + imgPathStore.substring(imgPathStore.lastIndexOf("."));
//        String imgPath = Constant.BASE_OSS_URL + Constant.OSS_IMG_PATH + fileName;
//
//        OssManager builder = new OssManager.Builder(this)
//                .bucketName("zs-android")//OSS桶名
//                .accessKeyId("LTAI5tB2LygPxntS2B56AH75")
//                .accessKeySecret("LYG9rSEDENh7kZuJhZKRJMfbaRgf4B")
//                .endPoint(Constant.BASE_OSS_URL)//OSS外网域名(阿里云分配的或自定义域名)
//                .objectKey(Constant.OSS_IMG_PATH + fileName)//对应OSS文件夹+文件名(时间戳+UUID+图片后缀)
//                .localFilePath(imgPathStore)//本机的文件AndroidQ目录路径
//                .build();
//
//        //OSS推送上传状态监听事件
//        builder.setPushStateListener(new OssManager.OnPushStateListener() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                //上传参数，主要获取objectKey(例如：pic_data/1637295593752.jpeg)
//                //时间戳数据，通过Handle子线程处理，调用后端API接口，进行存储。主要作为:拼接访问的URL地址，不进行记录将不清楚OSS对应的资源文件是什么
//
//                //OSS监听文件上传成功后，返回成功数据信息，发送消息到子线程中进行相关操作
//                Message msg = new Message();
//                msg.obj = result;
//                msg.what = 2;
//                mHandler.postDelayed(new Runnable() {   //延迟发送，让进度条的1秒钟绘制执行完，才调用 msg.what = 2中的业务逻辑
//                    @Override
//                    public void run() {
//                        mHandler.sendMessage(msg);
//                    }
//                }, 1000);
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 请求异常。
//                        if (clientException != null) {
//                            Toast.makeText(MyCreateStoreActivity.this, "本机请求异常(无网络等情况)：" + clientException.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                        if (serviceException != null) {
//                            Toast.makeText(MyCreateStoreActivity.this, "阿里服务异常：" + serviceException.getMessage(), Toast.LENGTH_SHORT).show();
//                            // 服务异常。
//                            Log.e("ErrorCode", serviceException.getErrorCode());
//                            Log.e("RequestId", serviceException.getRequestId());
//                            Log.e("HostId", serviceException.getHostId());
//                            Log.e("RawMessage", serviceException.getRawMessage());
//                        }
//                    }
//                });
//            }
//        });
//        //OSS推送上传进度监听事件
//        builder.setPushProgressListener(new OssManager.OnPushProgressListener() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                //不要Handler发送消息 ，文件 当前kb大小 和 总kb大小，必须实时传递进度参数设置到水波纹进度上
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //默认Gone失效，有进度值就设置可见VISIBLE
//                        mCircleProgressCreateStore.setVisibility(View.VISIBLE);
//                        //在代码中动态改变渐变色，可能会导致颜色跳跃
//                        mCircleProgressCreateStore.setGradientColors(COLORS);
//                        //OSS当前值 ==  OSS最大值时，证明文件推送上传成功
//                        if (currentSize == totalSize) {
//                            mCircleProgressCreateStore.setValue(currentSize);
//                        }
//                    }
//                });
//            }
//        });
//        //构建完成，开始调用OSS推送上传图片服务
//        builder.push();
//        Message msg1 = new Message();
//        msg1.obj = imgPath;
//        msg1.what = 1;
//        mHandler.postDelayed(new Runnable() {   //延迟发送，让进度条的1秒钟绘制执行完，才调用 msg.what = 2中的业务逻辑
//            @Override
//            public void run() {
//                mHandler.sendMessage(msg1);
//            }
//        }, 500);
//    }

    /**
     * 上传集合中的图片到阿里云OSS对象存储
     * @param imgApplyStorePathList 路径集合
     */
    private void startOSSUploadFile(List<String> imgApplyStorePathList) {

        //MyXPopupUtils.getInstance().setShowDialog(ApplyRunCommitActivity.this,"正在上传...");

        List<String> list = new ArrayList<>();
        for (String imgPath : imgApplyStorePathList) {
            //时间戳使用原因 + UUID：避免OSS云资源重名覆盖文件，导致用户数据丢失，加入时间戳来拼接在OSS文件名中，那么必然不会出现重名问题
            String fileName = System.currentTimeMillis() + UUIDUtil.UUID32() + imgPath.substring(imgPath.lastIndexOf("."));
            list.add(Constant.BASE_OSS_URL + Constant.OSS_IMG_PATH + fileName);
            OssManager builder = new OssManager.Builder(this)
                    .bucketName("zs-android")//OSS桶名
                    .accessKeyId("LTAI5tB2LygPxntS2B56AH75")
                    .accessKeySecret("LYG9rSEDENh7kZuJhZKRJMfbaRgf4B")
                    .endPoint(Constant.BASE_OSS_URL)//OSS外网域名(阿里云分配的或自定义域名)
                    .objectKey(Constant.OSS_IMG_PATH + fileName)//对应OSS文件夹+文件名(时间戳+UUID+图片后缀)
                    .localFilePath(imgPath)//本机的文件AndroidQ目录路径
                    .build();

            //OSS推送上传状态监听事件
            builder.setPushStateListener(new OssManager.OnPushStateListener() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    //上传参数，主要获取objectKey(例如：pic_data/1637295593752.jpeg)
                    //时间戳数据，通过Handle子线程处理，调用后端API接口，进行存储。主要作为:拼接访问的URL地址，不进行记录将不清楚OSS对应的资源文件是什么

                    //OSS监听文件上传成功后，返回成功数据信息，发送消息到子线程中进行相关操作
                    Message msg = new Message();
                    msg.obj = result;
                    msg.what = 2;
                    mHandler.postDelayed(new Runnable() {   //延迟发送，让进度条的1秒钟绘制执行完，才调用 msg.what = 2中的业务逻辑
                        @Override
                        public void run() {
                            mHandler.sendMessage(msg);
                        }
                    }, 1000);
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 请求异常。
                            if (clientException != null) {
                                Toast.makeText(MyCreateStoreActivity.this, "本机请求异常(无网络等情况)：" + clientException.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if (serviceException != null) {
                                Toast.makeText(MyCreateStoreActivity.this, "阿里服务异常：" + serviceException.getMessage(), Toast.LENGTH_SHORT).show();
                                // 服务异常。
                                Log.e("ErrorCode", serviceException.getErrorCode());
                                Log.e("RequestId", serviceException.getRequestId());
                                Log.e("HostId", serviceException.getHostId());
                                Log.e("RawMessage", serviceException.getRawMessage());
                            }
                        }
                    });
                }
            });
            //OSS推送上传进度监听事件
            builder.setPushProgressListener(new OssManager.OnPushProgressListener() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    //不要Handler发送消息 ，文件 当前kb大小 和 总kb大小，必须实时传递进度参数设置到水波纹进度上
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //默认Gone失效，有进度值就设置可见VISIBLE
                            mCircleProgressCreateStore.setVisibility(View.VISIBLE);
                            //在代码中动态改变渐变色，可能会导致颜色跳跃
                            mCircleProgressCreateStore.setGradientColors(COLORS);
                            //OSS当前值 ==  OSS最大值时，证明文件推送上传成功
                            if (currentSize == totalSize) {
                                mCircleProgressCreateStore.setValue(currentSize);
                            }
                        }
                    });
                }
            });
            //构建完成，开始调用OSS推送上传图片服务
            builder.push();
        }
        Message msg1 = new Message();
        msg1.obj = list;
        msg1.what = 1;
        mHandler.postDelayed(new Runnable() {   //延迟发送，让进度条的1秒钟绘制执行完，才调用 msg.what = 2中的业务逻辑
            @Override
            public void run() {
                mHandler.sendMessage(msg1);
            }
        }, 500);
    }

    /**
     * 初始化门店实拍图片选择适配器配置参数
     */
    private void initCreateStorePicSelect(Bundle savedInstanceState) {
        mAdapterStore = new GridStoreImageAdapter(getContext(), onAddStorePicClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterStore.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterStore.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterStore.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterStore.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(MyCreateStoreActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(MyCreateStoreActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        PictureSelector.create(MyCreateStoreActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        // 注册广播，接收选择的门店实拍图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(storeBroadcastReceiver, Constant.ACTION_DELETE_CREATE_STORE_PREVIEW_POSITION);
        launcherStoreResult = createActivityStoreResultLauncher();
    }

    /**
     * 初始化店铺Logo图片选择适配器配置参数
     */
    private void initCreateStoreLogoSelect(Bundle savedInstanceState) {
        mAdapterStoreLogo = new GridStoreLogoImageAdapter(getContext(), onAddStoreLogoClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterStoreLogo.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterStoreLogo.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterStoreLogo.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterStoreLogo.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(MyCreateStoreActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(MyCreateStoreActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        PictureSelector.create(MyCreateStoreActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义播放回调控制，用户可以使用自己的视频播放界面
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        // 注册广播，接收选择的店铺Logo图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(storeLogoBroadcastReceiver, Constant.ACTION_DELETE_CREATE_STORE_LOGO_PREVIEW_POSITION);
        launcherStoreLogoResult = createActivityStoreLogoResultLauncher();
    }

    /**
     * 门店实拍图启动相册相机
     */
    private final GridStoreImageAdapter.onAddPicClickListener onAddStorePicClickListener = new GridStoreImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            imgPathStore = null;
            PictureSelector.create(MyCreateStoreActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())
                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                    .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                    .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofCustomWindowAnimationStyle(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out))//自定义相册启动退出动画 上滑动画打开 下滑动画退出
                    .isWeChatStyle(true)// 是否开启微信图片选择风格
                    .isUseCustomCamera(false)// 是否使用自定义相机
                    .setLanguage(LanguageConfig.CHINESE)// 设置语言，默认中文
                    .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                    .setRecyclerAnimationMode(AnimationType.SLIDE_IN_BOTTOM_ANIMATION)// 列表动画效果:滑动底部动画
                    .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                    .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                    .setQuerySandboxDirectory(createCustomCameraOutPath())// 查询自定义相机输出目录
                    .isGetOnlySandboxDirectory(false) // 是否只显示某个目录下的资源；需与setQuerySandboxDirectory相对应
                    .setCustomCameraFeatures(CustomCameraType.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                    .setCaptureLoadingColor(ContextCompat.getColor(getContext(), R.color.app_color_blue))
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .maxVideoSelectNum(1) // 视频最大选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                    .isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                    .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，裁剪功能将会失效
                    .isDisplayOriginalSize(true)// 是否显示原文件大小，isOriginalImageControl true有效
                    .isEditorImage(true)//是否编辑图片
                    .bindCustomPermissionsObtainListener(new MyPermissionsObtainCallback())// 自定义权限拦截
                    .selectionMode(PictureConfig.MULTIPLE)// MULTIPLE多选 or SINGLE单选
                    .isPreviewImage(true)// 是否可预览图片
                    .isPreviewVideo(true)// 是否可预览视频
                    .isEnablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .setCameraImageFormat(PictureMimeType.JPEG) // 相机图片格式后缀,默认.jpeg
                    .setCameraVideoFormat(PictureMimeType.MP4)// 相机视频格式后缀,默认.mp4
                    .setCameraAudioFormat(PictureMimeType.AMR)// 录音音频格式后缀,默认.amr
                    .isEnableCrop(false)// 是否裁剪
                    .isCompress(true)// 是否压缩
                    .synOrAsy(false)//同步true或异步false 压缩 默认同步
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义 默认0 ， 0
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .isCropDragSmoothToCenter(true)// 裁剪框拖动时图片自动跟随居中
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(true)// 是否开启点击声音
                    .selectionData(mAdapterStore.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    .forResult(launcherStoreResult);
        }
    };

    /**
     * 核酸证明启动相册相机
     */
    private final GridStoreLogoImageAdapter.onAddPicClickListener onAddStoreLogoClickListener = new GridStoreLogoImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            imgPathStoreLogo = null;
            PictureSelector.create(MyCreateStoreActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())
                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                    .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                    .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofCustomWindowAnimationStyle(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out))//自定义相册启动退出动画 上滑动画打开 下滑动画退出
                    .isWeChatStyle(true)// 是否开启微信图片选择风格
                    .isUseCustomCamera(false)// 是否使用自定义相机
                    .setLanguage(LanguageConfig.CHINESE)// 设置语言，默认中文
                    .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                    .setRecyclerAnimationMode(AnimationType.SLIDE_IN_BOTTOM_ANIMATION)// 列表动画效果:滑动底部动画
                    .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                    .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                    .setQuerySandboxDirectory(createCustomCameraOutPath())// 查询自定义相机输出目录
                    .isGetOnlySandboxDirectory(false) // 是否只显示某个目录下的资源；需与setQuerySandboxDirectory相对应
                    .setCustomCameraFeatures(CustomCameraType.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                    .setCaptureLoadingColor(ContextCompat.getColor(getContext(), R.color.app_color_blue))
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .maxVideoSelectNum(1) // 视频最大选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                    .isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                    .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，裁剪功能将会失效
                    .isDisplayOriginalSize(true)// 是否显示原文件大小，isOriginalImageControl true有效
                    .isEditorImage(true)//是否编辑图片
                    .bindCustomPermissionsObtainListener(new MyPermissionsObtainCallback())// 自定义权限拦截
                    .selectionMode(PictureConfig.MULTIPLE)// MULTIPLE多选 or SINGLE单选
                    .isPreviewImage(true)// 是否可预览图片
                    .isPreviewVideo(true)// 是否可预览视频
                    .isEnablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .setCameraImageFormat(PictureMimeType.JPEG) // 相机图片格式后缀,默认.jpeg
                    .setCameraVideoFormat(PictureMimeType.MP4)// 相机视频格式后缀,默认.mp4
                    .setCameraAudioFormat(PictureMimeType.AMR)// 录音音频格式后缀,默认.amr
                    .isEnableCrop(false)// 是否裁剪
                    .isCompress(true)// 是否压缩
                    .synOrAsy(false)//同步true或异步false 压缩 默认同步
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义 默认0 ， 0
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .isCropDragSmoothToCenter(true)// 裁剪框拖动时图片自动跟随居中
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(true)// 是否开启点击声音
                    .selectionData(mAdapterStoreLogo.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    .forResult(launcherStoreLogoResult);
        }
    };

    /**
     * 创建ActivityResultLauncher 回调监听门店实拍图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityStoreResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(result.getData());
                            for (LocalMedia media : selectList) {
                                if (media.getWidth() == 0 || media.getHeight() == 0) {
                                    if (PictureMimeType.isHasImage(media.getMimeType())) {
                                        MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                                        media.setWidth(imageExtraInfo.getWidth());
                                        media.setHeight(imageExtraInfo.getHeight());
                                    } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                                        MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(getContext(), media.getPath());
                                        media.setWidth(videoExtraInfo.getWidth());
                                        media.setHeight(videoExtraInfo.getHeight());
                                    }
                                }
                                if (imgPathStore != null) {
                                    imgPathStore = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathStore = media.getAndroidQToPath();
                                imgApplyStorePathList.set(0, imgPathStore);
                                Log.i(TAG, "AndroidQ门店实拍图Path:" + media.getAndroidQToPath());
                            }
                            mAdapterStore.setList(selectList);
                            mAdapterStore.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 创建ActivityResultLauncher 回调监听店铺Logo图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityStoreLogoResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(result.getData());
                            for (LocalMedia media : selectList) {
                                if (media.getWidth() == 0 || media.getHeight() == 0) {
                                    if (PictureMimeType.isHasImage(media.getMimeType())) {
                                        MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                                        media.setWidth(imageExtraInfo.getWidth());
                                        media.setHeight(imageExtraInfo.getHeight());
                                    } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                                        MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(getContext(), media.getPath());
                                        media.setWidth(videoExtraInfo.getWidth());
                                        media.setHeight(videoExtraInfo.getHeight());
                                    }
                                }
                                if (imgPathStoreLogo != null) {
                                    imgPathStoreLogo = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathStoreLogo = media.getAndroidQToPath();
                                imgApplyStorePathList.set(1, imgPathStoreLogo);
                                Log.i(TAG, "AndroidQ店铺LogoPath:" + media.getAndroidQToPath());
                            }
                            mAdapterStoreLogo.setList(selectList);
                            mAdapterStoreLogo.notifyDataSetChanged();
                        }
                    }
                });
    }
    /**
     * Bundle数据临时保存状态
     *
     * @param outState Bundle数据
     */
    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        /* 1.门店实拍图片 */
        if (mAdapterStore != null && mAdapterStore.getData() != null && mAdapterStore.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterStore.getData());
        }
        /* 1.店铺Logo图 */
        if (mAdapterStoreLogo != null && mAdapterStoreLogo.getData() != null && mAdapterStoreLogo.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterStoreLogo.getData());
        }
    }

    /**
     * 广播接受回调的门店实拍图片
     */
    private final BroadcastReceiver storeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (Constant.ACTION_DELETE_CREATE_STORE_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    mAdapterStore.remove(position);
                    mAdapterStore.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathStore != null) {
                        imgPathStore = null;
                    }
                }
            }
        }
    };

    /**
     * 广播接受回调的店铺Logo图片
     */
    private final BroadcastReceiver storeLogoBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (Constant.ACTION_DELETE_CREATE_STORE_LOGO_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    mAdapterStoreLogo.remove(position);
                    mAdapterStoreLogo.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathStoreLogo != null) {
                        imgPathStoreLogo = null;
                    }
                }
            }
        }
    };

    /**
     * 微信主题
     *
     * @return 裁剪样式
     */
    private PictureParameterStyle getWeChatStyle() {
        // 相册主题
        PictureParameterStyle mPictureParameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        mPictureParameterStyle.isChangeStatusBarFontColor = false;
        // 是否开启右下角已完成(0/9)风格
        mPictureParameterStyle.isOpenCompletedNumStyle = false;
        // 是否开启类似QQ相册带数字选择风格
        mPictureParameterStyle.isOpenCheckNumStyle = true;
        // 状态栏背景色
        mPictureParameterStyle.pictureStatusBarColor = Color.parseColor("#393a3e");
        // 相册列表标题栏背景色
        mPictureParameterStyle.pictureTitleBarBackgroundColor = Color.parseColor("#393a3e");
        // 相册父容器背景色
        mPictureParameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(getContext(), R.color.app_color_black);
        // 相册列表标题栏右侧上拉箭头
        mPictureParameterStyle.pictureTitleUpResId = R.drawable.picture_icon_wechat_up;
        // 相册列表标题栏右侧下拉箭头
        mPictureParameterStyle.pictureTitleDownResId = R.drawable.picture_icon_wechat_down;
        // 相册文件夹列表选中圆点
        mPictureParameterStyle.pictureFolderCheckedDotStyle = R.drawable.picture_orange_oval;
        // 相册返回箭头
        mPictureParameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_close;
        // 标题栏字体颜色
        mPictureParameterStyle.pictureTitleTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册右侧按钮字体颜色  废弃 改用.pictureRightDefaultTextColor和.pictureRightDefaultTextColor
        mPictureParameterStyle.pictureCancelTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_53575e);
        // 相册右侧按钮字体默认颜色
        mPictureParameterStyle.pictureRightDefaultTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_53575e);
        // 相册右侧按可点击字体颜色,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureRightSelectedTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册右侧按钮背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureUnCompleteBackgroundStyle = R.drawable.picture_send_button_default_bg;
        // 相册右侧按钮可点击背景样式,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureCompleteBackgroundStyle = R.drawable.picture_send_button_bg;
        // 选择相册目录背景样式
        mPictureParameterStyle.pictureAlbumStyle = R.drawable.picture_new_item_select_bg;
        // 相册列表勾选图片样式
        mPictureParameterStyle.pictureCheckedStyle = R.drawable.picture_wechat_num_selector;
        // 相册标题背景样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatTitleBackgroundStyle = R.drawable.picture_album_bg;
        // 微信样式 预览右下角样式 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatChooseStyle = R.drawable.picture_wechat_select_cb;
        // 相册返回箭头 ,只针对isWeChatStyle 为true时有效果
        mPictureParameterStyle.pictureWeChatLeftBackStyle = R.drawable.picture_icon_back;
        // 相册列表底部背景色
        mPictureParameterStyle.pictureBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_grey);
        // 已选数量圆点背景样式
        mPictureParameterStyle.pictureCheckNumBgStyle = R.drawable.picture_num_oval;
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        mPictureParameterStyle.picturePreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        mPictureParameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_9b);
        // 相册列表已完成色值(已完成 可点击色值)
        mPictureParameterStyle.pictureCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_white);
        // 相册列表未完成色值(请选择 不可点击色值)
        mPictureParameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(getContext(), R.color.picture_color_53575e);
        // 预览界面底部背景色
        mPictureParameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(getContext(), R.color.picture_color_half_grey);
        // 外部预览界面删除按钮样式
        mPictureParameterStyle.pictureExternalPreviewDeleteStyle = R.drawable.picture_icon_delete;
        // 原图按钮勾选样式  需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalControlStyle = R.drawable.picture_original_wechat_checkbox;
        // 原图文字颜色 需设置.isOriginalImageControl(true); 才有效
        mPictureParameterStyle.pictureOriginalFontColor = ContextCompat.getColor(getContext(), R.color.app_color_white);
        // 外部预览界面是否显示删除按钮
        mPictureParameterStyle.pictureExternalPreviewGonePreviewDelete = true;
        // 设置NavBar Color SDK Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP有效
        mPictureParameterStyle.pictureNavBarColor = Color.parseColor("#393a3e");
        // 标题栏高度
        mPictureParameterStyle.pictureTitleBarHeight = ScreenUtils.dip2px(getContext(), 48);
        // 标题栏右侧按钮方向箭头left Padding
        mPictureParameterStyle.pictureTitleRightArrowLeftPadding = ScreenUtils.dip2px(getContext(), 3);
        // 裁剪主题
        mCropParameterStyle = new PictureCropParameterStyle(
                ContextCompat.getColor(getContext(), R.color.app_color_grey),
                ContextCompat.getColor(getContext(), R.color.app_color_grey),
                Color.parseColor("#393a3e"),
                ContextCompat.getColor(getContext(), R.color.app_color_white),
                mPictureParameterStyle.isChangeStatusBarFontColor);

        return mPictureParameterStyle;
    }

    /**
     * 创建自定义拍照输出目录
     *
     * @return
     */
    private String createCustomCameraOutPath() {
        File externalFilesDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 自定义权限管理回调 （自定义权限拦截）
     */
    private static class MyPermissionsObtainCallback implements OnPermissionsObtainCallback {

        @Override
        public void onPermissionsIntercept(Context context, boolean isCamera, String[] permissions, String tips, OnPermissionDialogOptionCallback dialogOptionCallback) {
            PictureCustomDialog dialog = new PictureCustomDialog(context, R.layout.picture_wind_base_dialog);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
            Button btn_commit = dialog.findViewById(R.id.btn_commit);
            btn_commit.setText(context.getString(R.string.picture_go_setting));
            TextView tvTitle = dialog.findViewById(R.id.tvTitle);
            TextView tv_content = dialog.findViewById(R.id.tv_content);
            tvTitle.setText(context.getString(R.string.picture_prompt));
            tv_content.setText(tips);
            btn_cancel.setOnClickListener(v -> {
                dialog.dismiss();
                dialogOptionCallback.onCancel();
            });
            btn_commit.setOnClickListener(v -> {
                dialog.dismiss();
                dialogOptionCallback.onSetting();
                PermissionChecker.launchAppDetailsSettings(context);
            });
            dialog.show();
        }
    }

    /**
     * 权限回调结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE) {// 存储权限
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    PictureCacheManager.deleteCacheDirFile(getContext(), PictureMimeType.ofImage(), new OnCallbackListener<String>() {
                        @Override
                        public void onCall(String absolutePath) {
                            new PictureMediaScannerConnection(getContext(), absolutePath);
                        }
                    });
                } else {
                    Toast.makeText(MyCreateStoreActivity.this, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Android振动器
     */
    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PictureCacheManager.deleteAllCacheDirRefreshFile(getContext());
        } else {
            PermissionChecker.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PictureConfig.APPLY_STORAGE_PERMISSIONS_CODE);
        }
    }

    /**
     * 注销广播注册 + 销毁广播资源 + 清除相关图片回调资源
     */
    @Override
    protected void onDestroy() {
        //释放振动资源
        if (vibrator != null) {
            vibrator.cancel();
            vibrator = null;
        }
        super.onDestroy();
        /** 清空图片选择参数的缓存图片 */
        clearCache();
        /* 2.清除门店实拍图 */
        if (launcherStoreResult != null) {
            launcherStoreResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(storeBroadcastReceiver, Constant.ACTION_DELETE_CREATE_STORE_PREVIEW_POSITION);
        /* 3.清除店铺Logo图 */
        if (launcherStoreLogoResult != null) {
            launcherStoreLogoResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(storeLogoBroadcastReceiver, Constant.ACTION_DELETE_CREATE_STORE_LOGO_PREVIEW_POSITION);
        /* 4.清除子线程 */
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://此处理URL + OCR + 商家信息 上传后端数据库
                    //OSS上传成功重组的URL地址集合，OKGo上传后端数据库
                    List<String> ossUrlFileNameLists = (List<String>) msg.obj;
                    Log.i(TAG, "重组处理后的OSS地址集合: " + ossUrlFileNameLists);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OkGo.<String>post(Constant.SHOP_ADD_STORE_INFO_BY_SA_TOKEN)
                                    .tag("商家创建店铺")
                                    .params("shopCategory", categoryId)
                                    .params("shopName", strGetStoreName)
                                    .params("shopPic", ossUrlFileNameLists.get(0))
                                    .params("shopLogo", ossUrlFileNameLists.get(1))
                                    .params("shopBeginTime", strGetBeginTime)
                                    .params("shopEndTime", strGetEndTime)
                                    .params("shopDesc", strGetStoreDesc)
                                    .params("shopAddress", strGetStoreAddress)
                                    .params("shopRecommend", strGetStoreRecommend)
                                    .params("shopMobile", strGetStoreMobile)
                                    .params("shopNotice", strGetStoreNotice)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                            if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                startVibrator();//成功振动
                                                DialogPrompt dialogPrompt = new DialogPrompt(MyCreateStoreActivity.this, "商铺已开通成功，快去管理商品信息吧", 10);
                                                dialogPrompt.showAndFinish(MyCreateStoreActivity.this);
                                                return;
                                            }
                                            if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                DialogPrompt dialogPrompt = new DialogPrompt(MyCreateStoreActivity.this, "商铺已开通失败，请联系校园帮开发者解决", 10);
                                                dialogPrompt.showAndFinish(MyCreateStoreActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            OkGoErrorUtil.CustomFragmentOkGoError(response, MyCreateStoreActivity.this, mrlCreateStoreShow, "请求错误，服务器连接失败！");
                                        }
                                    });
                        }
                    }, 1000);
                    break;
                case 2:
                    mCircleProgressCreateStore.reset();
                    mCircleProgressCreateStore.setVisibility(View.GONE);//进度条加载完成后，隐藏进度条
                    break;
            }
        }
    };

    /**
     * 注册定位监听器
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location){
            String street = location.getStreet();    //获取街道信息
            String town = location.getTown();    //获取乡镇信息
            String locationDescribe = location.getLocationDescribe();//获取位置描述信息
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                XToastUtils.error("请先开启GPS位置服务");
            }
            else {
                //在此处进行你的后续联网操作
                mEditCreateStoreAddress.setText(street+ town+ "-" + locationDescribe.substring(locationDescribe.indexOf("在")+1));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Permission.ACCESS_FINE_LOCATION) && XXPermissions.isGranted(this, Permission.ACCESS_COARSE_LOCATION)) {
                initLocationClient();
                XToastUtils.success("GPS定位服务授权完成");
            } else {
                XToastUtils.error("请先开启GPS定位服务，否则开通不了商铺哟",5000);
            }
        }
    }
}

