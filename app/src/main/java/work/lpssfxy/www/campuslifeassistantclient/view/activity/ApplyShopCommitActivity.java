package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yoma.roundbutton.RoundButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridLicenceImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.pogress.CircleProgress;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyRunBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyShopBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.UUIDUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.FullyGridLayoutManager;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.GlideEngine;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.OssManager;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/11
 * 描述：申请商家入驻信息提交--->BUG未解决，外部样式删除按钮，没实现对应删除，而是全部已选图片同时删除清空
 *
 * @author ZSAndroid
 * @create 2021-12-11-19:25
 */
@SuppressLint("NonConstantResourceId")
public class ApplyShopCommitActivity extends BaseActivity {

    private static final String TAG = "ApplyShopCommitActivity";

    /** 获取View控件 */
    @BindView(R2.id.rl_commit_shop_info_show) RelativeLayout mrlCommitShopInfoShow; //标题栏
    @BindView(R2.id.toolbar_apply_shop_commit) Toolbar mToolbarApplyShopCommit; //标题栏
    @BindView(R2.id.iv_apply_shop_commit) ImageView mIvApplyShopCommit;  //标题栏返回
    @BindView(R2.id.recycler_commit_licence_pic) RecyclerView mRecycleCommitLicencePic; //餐饮许可证列表控件
    @BindView(R2.id.rbn_commit_shop_info) RoundButton mRbnCommitShopInfo; //提交信息
    @BindView(R2.id.circle_progress_shop_commit) CircleProgress mCircleProgressShopCommit; //进度条
    private final static int[] COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
    /** 图片选择器基本参数 */
    private PictureParameterStyle mPictureParameterStyle;//全局主题
    private PictureCropParameterStyle mCropParameterStyle;//裁剪主题
    private final int maxSelectNum = 1;//最大选择数
    /** 接收相册中选择图片的回调到Activity中 */
    private ActivityResultLauncher<Intent> launcherLicenceResult;//回调显示核酸证明
    /** 网格布局相册餐饮许可证适配器 */
    private GridLicenceImageAdapter mAdapterLicence;
    /** 餐饮许可证图片路径 */
    public static String imgPathLicence;
    /** Android振动器 */
    private Vibrator vibrator;

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
        return R.layout.activity_apply_shop_commit;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        /**判断Toolbar，开启主图标并显示title*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        } else {
            Log.i(TAG, "onCreate: actionBar is null");
        }
        /** 设置Toolbar */
        setSupportActionBar(mToolbarApplyShopCommit);
        /** 初始化List集合长度，用于装图片路径 ，不初始化4个值，将会出现跳过Add 时崩溃，初始化使用set就可以完美避免错误 */

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPictureParameterStyle = getWeChatStyle();//设置全局主题为微信风格
        initLicencePicSelect(savedInstanceState);//初始化餐饮许可证图片选择适配器配置参数
        initRecyclerView();//初始化全部图片view列表
    }

    private void initRecyclerView() {
        /**自定义全局统一网格垂直布局 */
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mRecycleCommitLicencePic.setLayoutManager(manager);//设置RecyclerView的布局样式
        mRecycleCommitLicencePic.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCommitLicencePic.setAdapter(mAdapterLicence);//设置RecyclerView的适配器
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
    @OnClick({R2.id.iv_apply_shop_commit, R2.id.rbn_commit_shop_info})
    public void onApplyShopCommitViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_apply_shop_commit://点击返回
                ApplyShopCommitActivity.this.finish();
                break;
            case R.id.rbn_commit_shop_info://提交认证信息
                startCommitShopApplyInfo();
                break;
        }
    }

    private void startCommitShopApplyInfo() {
        if (imgPathLicence == null) {
            ToastUtils.show("请选择餐饮许可证");
            return;
        }
        startOSSUploadFile();
    }

    /**
     * 上传集合中的图片到阿里云OSS对象存储
     */
    private void startOSSUploadFile() {
        //时间戳使用原因 + UUID：避免OSS云资源重名覆盖文件，导致用户数据丢失，加入时间戳来拼接在OSS文件名中，那么必然不会出现重名问题
        String fileName = System.currentTimeMillis() + UUIDUtil.UUID32() + imgPathLicence.substring(imgPathLicence.lastIndexOf("."));
        String imgPath = Constant.BASE_OSS_URL + Constant.OSS_IMG_PATH + fileName;

        OssManager builder = new OssManager.Builder(this)
                .bucketName("zs-android")//OSS桶名
                .accessKeyId("LTAI5tB2LygPxntS2B56AH75")
                .accessKeySecret("LYG9rSEDENh7kZuJhZKRJMfbaRgf4B")
                .endPoint(Constant.BASE_OSS_URL)//OSS外网域名(阿里云分配的或自定义域名)
                .objectKey(Constant.OSS_IMG_PATH + fileName)//对应OSS文件夹+文件名(时间戳+UUID+图片后缀)
                .localFilePath(imgPathLicence)//本机的文件AndroidQ目录路径
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
                            Toast.makeText(ApplyShopCommitActivity.this, "本机请求异常(无网络等情况)：" + clientException.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (serviceException != null) {
                            Toast.makeText(ApplyShopCommitActivity.this, "阿里服务异常：" + serviceException.getMessage(), Toast.LENGTH_SHORT).show();
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
                        mCircleProgressShopCommit.setVisibility(View.VISIBLE);
                        //在代码中动态改变渐变色，可能会导致颜色跳跃
                        mCircleProgressShopCommit.setGradientColors(COLORS);
                        //OSS当前值 ==  OSS最大值时，证明文件推送上传成功
                        if (currentSize == totalSize) {
                            mCircleProgressShopCommit.setValue(currentSize);
                        }
                    }
                });
            }
        });
        //构建完成，开始调用OSS推送上传图片服务
        builder.push();
        Message msg1 = new Message();
        msg1.obj = imgPath;
        msg1.what = 1;
        mHandler.postDelayed(new Runnable() {   //延迟发送，让进度条的1秒钟绘制执行完，才调用 msg.what = 2中的业务逻辑
            @Override
            public void run() {
                mHandler.sendMessage(msg1);
            }
        }, 500);
    }

    /**
     * 初始化餐饮许可证图片选择适配器配置参数
     */
    private void initLicencePicSelect(Bundle savedInstanceState) {
        mAdapterLicence = new GridLicenceImageAdapter(getContext(), onAddLicencePicClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterLicence.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterLicence.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterLicence.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterLicence.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(ApplyShopCommitActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(ApplyShopCommitActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        PictureSelector.create(ApplyShopCommitActivity.this)
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
        // 注册广播，接收选择的餐饮许可证图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(licenceBroadcastReceiver, Constant.ACTION_DELETE_STU_CARD_PREVIEW_POSITION);
        launcherLicenceResult = createActivityLicenceResultLauncher();
    }

    /**
     * 核酸证明启动相册相机
     */
    private final GridLicenceImageAdapter.onAddPicClickListener onAddLicencePicClickListener = new GridLicenceImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            imgPathLicence = null;
            PictureSelector.create(ApplyShopCommitActivity.this)
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
                    .selectionData(mAdapterLicence.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    .forResult(launcherLicenceResult);
        }
    };

    /**
     * 创建ActivityResultLauncher 回调监听餐饮许可证图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityLicenceResultLauncher() {
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
                                if (imgPathLicence != null) {
                                    imgPathLicence = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathLicence = media.getAndroidQToPath();
                                Log.i(TAG, "AndroidQ核酸证明Path:" + media.getAndroidQToPath());
                            }
                            mAdapterLicence.setList(selectList);
                            mAdapterLicence.notifyDataSetChanged();
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
        /* 1.餐饮许可证 */
        if (mAdapterLicence != null && mAdapterLicence.getData() != null && mAdapterLicence.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterLicence.getData());
        }
    }

    /**
     * 广播接受回调的餐饮许可证图片
     */
    private final BroadcastReceiver licenceBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (Constant.ACTION_DELETE_STU_CARD_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.show("校园帮APP提示：您已删除餐饮许可证图片！");
                    mAdapterLicence.remove(position);
                    mAdapterLicence.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathLicence != null) {
                        imgPathLicence = null;
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
                            Log.i(TAG, "刷新图库:" + absolutePath);
                        }
                    });
                } else {
                    Toast.makeText(ApplyShopCommitActivity.this, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
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
        /* 2.清除餐饮许可证 */
        if (launcherLicenceResult != null) {
            launcherLicenceResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(licenceBroadcastReceiver, Constant.ACTION_DELETE_NUCLEIC_PIC_PREVIEW_POSITION);
        /* 9.清除子线程 */
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
                    String ossUrlFileName = (String) msg.obj;
                    Log.i(TAG, "，OKGo上传后端数据库: " + ossUrlFileName);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            OkGo.<String>post(Constant.USER_DO_APPLY_SHOP_BY_MY_INFO)
                                    .tag("提交商家认证申请")
                                    .params("applyRoleType",4)
                                    .params("applyLicencePic",ossUrlFileName)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            startVibrator();//成功振动
                                            OkGoApplyShopBean okGoApplyShopBean = GsonUtil.gsonToBean(response.body(), OkGoApplyShopBean.class);
                                            if (200 == okGoApplyShopBean.getCode() && null == okGoApplyShopBean.getData() && "商家入驻信息提交成功".equals(okGoApplyShopBean.getMsg())) {
                                                DialogPrompt dialogPrompt = new DialogPrompt(ApplyShopCommitActivity.this, "商家入驻信息已提交成功，耐心等待超管审核哟~", 10);
                                                dialogPrompt.showAndFinish(ApplyShopCommitActivity.this);
                                                return;
                                            }
                                            if (200 == okGoApplyShopBean.getCode() && null == okGoApplyShopBean.getData() && "商家入驻信息提交失败".equals(okGoApplyShopBean.getMsg())) {
                                                DialogPrompt dialogPrompt = new DialogPrompt(ApplyShopCommitActivity.this, "商家入驻信息提交失败，如有疑问请联系我们~", 10);
                                                dialogPrompt.showAndFinish(ApplyShopCommitActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyShopCommitActivity.this, mrlCommitShopInfoShow, "请求错误，服务器连接失败！");
                                        }
                                    });
                        }
                    }, 1000);
                    break;
                case 2:
                    mCircleProgressShopCommit.reset();
                    mCircleProgressShopCommit.setVisibility(View.GONE);//进度条加载完成后，隐藏进度条
                    break;
            }
        }
    };
}
