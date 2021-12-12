package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xutil.data.DateUtils;
import com.yoma.roundbutton.RoundButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridHealCodeImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridNucleicImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridRunCodeImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.apply.GridStuCardImageAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.pogress.CircleProgress;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyRunBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.UUIDUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.FullyGridLayoutManager;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.GlideEngine;
import work.lpssfxy.www.campuslifeassistantclient.utils.pictrueselect.OssManager;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/5
 * 描述：申请认证跑腿信息提交--->BUG未解决，外部样式删除按钮，没实现对应删除，而是全部已选图片同时删除清空
 *
 * @author ZSAndroid
 * @create 2021-12-05-14:35
 */
@SuppressLint("NonConstantResourceId")
public class ApplyRunCommitActivity extends BaseActivity {

    private static final String TAG = "ApplyRunCommitActivity";

    /** 获取View控件 */
    @BindView(R2.id.rl_commit_run_info_show) RelativeLayout mrlCommitRunInfoShow; //标题栏
    @BindView(R2.id.toolbar_apply_commit) Toolbar mToolbarApplyCommit; //标题栏
    @BindView(R2.id.iv_apply_commit) ImageView mIvApplyCommit;  //标题栏返回
    @BindView(R2.id.recycler_commit_stu_card) RecyclerView mRecycleCommitStuCard; //学生证列表控件
    @BindView(R2.id.recycler_commit_nucleic_pic) RecyclerView mRecycleCommitNucleicPic; //核酸证明列表控件
    @BindView(R2.id.recycler_commit_heal_code) RecyclerView mRecycleCommitHealCode; //健康码列表控件
    @BindView(R2.id.sample_heal_code_scale) ImageView mSampleHealCodeScale; //健康码示例放大
    @BindView(R2.id.recycler_commit_run_code) RecyclerView mRecycleCommitRunCode; //健康码示例放大
    @BindView(R2.id.sample_run_code_scale) ImageView mSampleRunCodeScale; //行程码示例放大
    @BindView(R2.id.rl_commit_graduation_data) RelativeLayout mRlCommitGraduationData; //选择毕业时间父布局
    @BindView(R2.id.tv_commit_data) TextView mTvCommitData; //设置毕业时间
    @BindView(R2.id.rl_commit_car_info) RelativeLayout mRlCommitCarInfo; //选择OCR车牌识别
    @BindView(R.id.spinner_car_type) MaterialSpinner mSpinnerCarType;//车辆类型
    @BindView(R2.id.tv_commit_car) TextView mTvCommitCar; //设置车牌信息
    @BindView(R2.id.rbn_commit_info) RoundButton mRbnCommitInfo; //提交信息
    @BindView(R2.id.circle_progress_commit) CircleProgress mCircleProgressCommit; //进度条
    private final static int[] COLORS = new int[]{Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
    /** 毕业日期 */
    private TimePickerView mDatePicker;//日期选择器
    /** 图片选择器基本参数 */
    private PictureParameterStyle mPictureParameterStyle;//全局主题
    private PictureCropParameterStyle mCropParameterStyle;//裁剪主题
    private final int maxSelectNum = 1;//最大选择数

    /** 接收相册中选择图片的回调到Activity中 */
    private ActivityResultLauncher<Intent> launcherStuCardResult;//回调显示学生证
    private ActivityResultLauncher<Intent> launcherNucleicPicResult;//回调显示核酸证明
    private ActivityResultLauncher<Intent> launcherHealCodeResult;//回调显示健康码
    private ActivityResultLauncher<Intent> launcherRunCodeResult;//回调显示行程码

    /** 自定义网格布局相册图片适配器，提供给列表控件RecyclerView加载显示相册图片或视频 */
    private GridStuCardImageAdapter mAdapterStuCard;//加载学生证
    private GridNucleicImageAdapter mAdapterNucleicPic;//加载核酸证明
    private GridHealCodeImageAdapter mAdapterHealCode;//加载健康码
    private GridRunCodeImageAdapter mAdapterRunCode;//加载行程码

    /** 每个列表控件获取图片的目录路径，用于判断内容长度，满足条件，调用阿里云OSS上传同时调用SpringBoot后端接口存储申请认证跑腿表 */
    public static String imgPathStuCard;//学生证图片路径
    public static String imgPathNucleicPic;//核酸证明图片路径
    public static String imgPathHealCode;//健康码图片路径
    public static String imgPathRunCode;//行程码图片路径
    public static List<String> imgApplyCommitPathList;//学生证图片路径 + 核酸证明图片路径 + 健康码图片路径 + 行程码图片路径 = 总集合图片路径
    //车辆类型默认值
    private String strCarType = "无车辆";
    //Android振动器
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
        return R.layout.activity_apply_run_commit;
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
        setSupportActionBar(mToolbarApplyCommit);
        /** 初始化List集合长度，用于装图片路径 ，不初始化4个值，将会出现跳过Add 时崩溃，初始化使用set就可以完美避免错误 */
        imgApplyCommitPathList = new ArrayList<>();
        imgApplyCommitPathList.add(0, "");
        imgApplyCommitPathList.add(1, "");
        imgApplyCommitPathList.add(2, "");
        imgApplyCommitPathList.add(3, "");
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        mPictureParameterStyle = getWeChatStyle();//设置全局主题为微信风格
        initStuCardPicSelect(savedInstanceState);//初始化学生证图片选择适配器配置参数
        initNucleicPicSelect(savedInstanceState);//初始化核酸证明图片选择适配器配置参数
        initHealCodeSelect(savedInstanceState);//初始化健康码图片选择适配器配置参数
        initRunCodeSelect(savedInstanceState);//初始化行程码图片选择适配器配置参数
        initAllRecyclerView();//初始化全部图片view列表
    }

    private void initAllRecyclerView() {
        /**自定义全局统一网格垂直布局 */
        FullyGridLayoutManager manager1 = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        /* 1.学生证 */
        mRecycleCommitStuCard.setLayoutManager(manager1);//设置RecyclerView的布局样式
        mRecycleCommitStuCard.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCommitStuCard.setAdapter(mAdapterStuCard);//设置RecyclerView的适配器
        /* 2.核酸证明 */
        FullyGridLayoutManager manager2 = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mRecycleCommitNucleicPic.setLayoutManager(manager2);//设置RecyclerView的布局样式
        mRecycleCommitNucleicPic.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCommitNucleicPic.setAdapter(mAdapterNucleicPic);//设置RecyclerView的适配器
        /* 3.健康码 */
        FullyGridLayoutManager manager3 = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mRecycleCommitHealCode.setLayoutManager(manager3);//设置RecyclerView的布局样式
        mRecycleCommitHealCode.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCommitHealCode.setAdapter(mAdapterHealCode);//设置RecyclerView的适配器
        /* 4.健康码 */
        FullyGridLayoutManager manager4 = new FullyGridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        mRecycleCommitRunCode.setLayoutManager(manager4);//设置RecyclerView的布局样式
        mRecycleCommitRunCode.addItemDecoration(new GridSpacingItemDecoration(1, ScreenUtils.dip2px(this, 8), false));
        mRecycleCommitRunCode.setAdapter(mAdapterRunCode);//设置RecyclerView的适配器
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {
        initNowData();//默认设置当前日期为毕业时间
        initSpinnerCarType();//初始化车辆类型下拉选择框
    }

    /**
     * 初始化车辆类型下拉选择框
     */
    private void initSpinnerCarType() {
        //设置下拉选项内容
        mSpinnerCarType.setItems(ResUtils.getStringArray(R.array.apply_run_car_type));
        //下拉框item监听事件
        mSpinnerCarType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ToastUtils.show("点击了" + item.toString());
                strCarType = item.toString();
            }
        });
        //未选择item监听事件
        mSpinnerCarType.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                ToastUtils.show("请选择车辆类型");
                strCarType = "无车辆";
            }
        });
    }

    /**
     * 默认设置当前日期为毕业时间
     */
    @SuppressLint("SimpleDateFormat")
    private void initNowData() {
        //当前选中日期
        String nowData = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mTvCommitData.setText(nowData);//设置日期
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
    @OnClick({R2.id.iv_apply_commit, R2.id.sample_heal_code_scale, R2.id.sample_run_code_scale, R2.id.rl_commit_graduation_data, R2.id.rl_commit_car_info, R2.id.rbn_commit_info})
    public void onApplyCommitViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_apply_commit://点击返回
                ApplyRunCommitActivity.this.finish();
                break;
            case R.id.sample_heal_code_scale://点击显示示例健康码大图
                bigImageLoader(((BitmapDrawable) mSampleHealCodeScale.getDrawable()).getBitmap());
                break;
            case R.id.sample_run_code_scale://点击显示示例行程码大图
                bigImageLoader(((BitmapDrawable) mSampleRunCodeScale.getDrawable()).getBitmap());
                break;
            case R.id.rl_commit_graduation_data://点击选择毕业时间
                chooseData();//选择毕业时间
                break;
            case R.id.rl_commit_car_info://点击调用OCR车牌识别
                IntentUtil.startActivityForResultAnimBottomToTop1(this, new Intent(this, ApplyCarNumberOCRActivity.class), Constant.REQUEST_CODE_VALUE);
                break;
            case R.id.rbn_commit_info://提交认证信息
                startCommitApplyInfo();
                break;
        }
    }

    /**
     * 查看大图，方法里直接实例化一个imageView不用xml文件，传入bitmap设置图片
     *
     * @param bitmap 大图资源
     */
    private void bigImageLoader(Bitmap bitmap) {
        //对话框 显示大图
        final Dialog dialog = new Dialog(ApplyRunCommitActivity.this);
        ImageView image = new ImageView(getContext());
        image.setImageBitmap(bitmap);
        dialog.setContentView(image);
        //将dialog周围的白块设置为透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        //显示
        dialog.show();
        //点击图片取消
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    /**
     * 选择毕业时间
     */
    private void chooseData() {
        if (mDatePicker == null) {
            mDatePicker = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    mTvCommitData.setText(DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                    ToastUtils.show("您选择毕业日期是：" + DateUtils.date2String(date, DateUtils.yyyyMMdd.get()));
                }
            })
                    .setTitleText("选择毕业日期")
                    .build();
        }
        mDatePicker.show();
    }

    /**
     * 提交认证信息
     */
    private void startCommitApplyInfo() {
        Log.i(TAG, "长度: " + imgApplyCommitPathList.size());
        if (imgPathStuCard == null) {
            ToastUtils.show("请选择学生证 ");
            return;
        }
        if (imgPathNucleicPic == null) {
            ToastUtils.show("请选择核酸证明");
            return;
        }
        if (imgPathHealCode == null) {
            ToastUtils.show("请选择健康码");
            return;
        }
        if (imgPathRunCode == null) {
            ToastUtils.show("请选择行程码");
            return;
        }
        if (imgApplyCommitPathList != null && imgApplyCommitPathList.size() == 4) {
            startOSSUploadFile(imgApplyCommitPathList);
        }
    }

    /**
     * 上传集合中的图片到阿里云OSS对象存储
     *
     * @param imgApplyCommitPathList 未处理的图片路径集合
     */
    private void startOSSUploadFile(List<String> imgApplyCommitPathList) {

        //MyXPopupUtils.getInstance().setShowDialog(ApplyRunCommitActivity.this,"正在上传...");

        List<String> list = new ArrayList<>();
        for (String imgPath : imgApplyCommitPathList) {
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
                                Toast.makeText(ApplyRunCommitActivity.this, "本机请求异常(无网络等情况)：" + clientException.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            if (serviceException != null) {
                                Toast.makeText(ApplyRunCommitActivity.this, "阿里服务异常：" + serviceException.getMessage(), Toast.LENGTH_SHORT).show();
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
                            mCircleProgressCommit.setVisibility(View.VISIBLE);
                            //在代码中动态改变渐变色，可能会导致颜色跳跃
                            mCircleProgressCommit.setGradientColors(COLORS);
                            //OSS当前值 ==  OSS最大值时，证明文件推送上传成功
                            if (currentSize == totalSize) {
                                mCircleProgressCommit.setValue(currentSize);
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
     * 初始化学生证图片选择适配器配置参数
     */
    private void initStuCardPicSelect(Bundle savedInstanceState) {
        mAdapterStuCard = new GridStuCardImageAdapter(getContext(), onAddStuCardPicClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterStuCard.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterStuCard.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterStuCard.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterStuCard.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(ApplyRunCommitActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        // 预览图片 可自定长按保存路径,测试过无效果
                        //PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
                        //animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
                        //animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                        PictureSelector.create(ApplyRunCommitActivity.this)
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
        // 注册广播，接收选择的学生证图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(stuCardBroadcastReceiver, Constant.ACTION_DELETE_STU_CARD_PREVIEW_POSITION);
        // 注册需要写在onCreate或Fragment onAttach里，否则会报java.lang.IllegalStateException异常
        launcherStuCardResult = createActivityStuCardResultLauncher();
    }

    /**
     * 初始化核酸证明图片选择适配器配置参数
     */
    private void initNucleicPicSelect(Bundle savedInstanceState) {
        mAdapterNucleicPic = new GridNucleicImageAdapter(getContext(), onAddNucleicPicClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterNucleicPic.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterNucleicPic.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterNucleicPic.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterNucleicPic.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(ApplyRunCommitActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        // 预览图片 可自定长按保存路径,测试过无效果
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        // 注册广播，接收选择的核酸图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(nucleicPicBroadcastReceiver, Constant.ACTION_DELETE_NUCLEIC_PIC_PREVIEW_POSITION);
        launcherNucleicPicResult = createActivityNucleicPicResultLauncher();
    }

    /**
     * 初始化健康码图片选择适配器配置参数
     */
    private void initHealCodeSelect(Bundle savedInstanceState) {
        mAdapterHealCode = new GridHealCodeImageAdapter(getContext(), onAddHealCodeClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterHealCode.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterHealCode.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterHealCode.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterHealCode.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(ApplyRunCommitActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        // 预览图片 可自定长按保存路径,测试过无效果
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        // 注册广播，接收选择的核酸图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(healCodeBroadcastReceiver, Constant.ACTION_DELETE_HEAL_CODE_PREVIEW_POSITION);
        launcherHealCodeResult = createActivityHealCodeResultLauncher();
    }

    /**
     * 初始化行程码图片选择适配器配置参数
     */
    private void initRunCodeSelect(Bundle savedInstanceState) {
        mAdapterRunCode = new GridRunCodeImageAdapter(getContext(), onAddRunCodeClickListener);//自定义网格布局相册图片适配器参数
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
            mAdapterRunCode.setList(savedInstanceState.getParcelableArrayList("selectorList"));
        }
        mAdapterRunCode.setSelectMax(maxSelectNum);//设置网格布局适配最大文件数
        //自定义网格布局相册图片item监听
        mAdapterRunCode.setOnItemClickListener((v, position) -> {
            List<LocalMedia> selectList = mAdapterRunCode.getData();
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:// 预览视频
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .externalPictureVideo(TextUtils.isEmpty(media.getAndroidQToPath()) ? media.getPath() : media.getAndroidQToPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:// 预览音频
                        PictureSelector.create(ApplyRunCommitActivity.this).externalPictureAudio(PictureMimeType.isContent(media.getPath()) ? media.getAndroidQToPath() : media.getPath());
                        break;

                    default:
                        // 预览图片 可自定长按保存路径,测试过无效果
                        PictureSelector.create(ApplyRunCommitActivity.this)
                                .themeStyle(R.style.picture_default_style) // xml设置主题
                                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });
        // 注册广播，接收选择的核酸图片 + 刷新适配器
        BroadcastManager.getInstance(getContext()).registerReceiver(runCodeBroadcastReceiver, Constant.ACTION_DELETE_RUN_CODE_PREVIEW_POSITION);
        launcherRunCodeResult = createActivityRunCodeResultLauncher();
    }

    /**
     * 学生证启动相册相机
     */
    private final GridStuCardImageAdapter.onAddPicClickListener onAddStuCardPicClickListener = new GridStuCardImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            //此处清空非常重要，不设置，将会把之前选择的路径存放在集合中，当需要再次启动相册增添其它照片
            //将导致第二次选择的图片路径集合 与 之前选择的路径集合 产生并集，那么OSS上传时，就会出现多次上传同张图片负效果
            imgPathStuCard = null;
            // 进入相册 以下是例子：不需要的api可以不写
            PictureSelector.create(ApplyRunCommitActivity.this)
                    //设置只选择图片
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    //.theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                    .setPictureUIStyle(PictureSelectorUIStyle.ofDefaultStyle())
                    .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                    .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                    .setPictureWindowAnimationStyle(PictureWindowAnimationStyle.ofCustomWindowAnimationStyle(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out))//自定义相册启动退出动画 上滑动画打开 下滑动画退出
                    .isWeChatStyle(true)// 是否开启微信图片选择风格
                    .isUseCustomCamera(false)// 是否使用自定义相机
                    .setLanguage(LanguageConfig.CHINESE)// 设置语言，默认中文
                    .isPageStrategy(true)// 是否开启分页策略 & 每页多少条；默认开启
                    .setRecyclerAnimationMode(AnimationType.SLIDE_IN_BOTTOM_ANIMATION)// 列表动画效果:滑动底部动画
                    //.setOfAllCameraType(PictureMimeType.ofVideo())
                    .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                    //.isSyncCover(true)// 是否强制从MediaStore里同步相册封面，如果相册封面没显示异常则没必要设置
                    //.isCameraAroundState(false) // 是否开启前置摄像头，默认false，如果使用系统拍照 可能部分机型会有兼容性问题
                    //.isCameraRotateImage(false) // 拍照图片旋转是否自动纠正
                    //.isAutoRotating(false)// 压缩时自动纠正有旋转的图片
                    .isMaxSelectEnabledMask(true)// 选择数到了最大阀值列表是否启用蒙层效果
                    //.isAutomaticTitleRecyclerTop(false)// 连续点击标题栏RecyclerView是否自动回到顶部,默认true
                    //.setOutputCameraPath(createCustomCameraOutPath())// 自定义相机输出目录
                    .setQuerySandboxDirectory(createCustomCameraOutPath())// 查询自定义相机输出目录
                    .isGetOnlySandboxDirectory(false) // 是否只显示某个目录下的资源；需与setQuerySandboxDirectory相对应
                    .setCustomCameraFeatures(CustomCameraType.BUTTON_STATE_BOTH)// 设置自定义相机按钮状态
                    .setCaptureLoadingColor(ContextCompat.getColor(getContext(), R.color.app_color_blue))
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .maxVideoSelectNum(1) // 视频最大选择数量
                    //.minVideoSelectNum(1)// 视频最小选择数量
                    //.closeAndroidQChangeVideoWH(!SdkVersionUtils.checkedAndroid_Q())// 关闭在AndroidQ下获取图片或视频宽高相反自动转换
                    .imageSpanCount(4)// 每行显示个数
                    //.queryFileSize() // 过滤最大资源,已废弃
                    //.filterMinFileSize(5)// 过滤最小资源，单位kb
                    //.filterMaxFileSize()// 过滤最大资源，单位kb
                    .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                    .isAndroidQTransform(true)// 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对compress(false); && .isEnableCrop(false);有效,默认处理
                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                    .isOriginalImageControl(true)// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，裁剪功能将会失效
                    .isDisplayOriginalSize(true)// 是否显示原文件大小，isOriginalImageControl true有效
                    .isEditorImage(true)//是否编辑图片
                    //.isAutoScalePreviewImage(true)// 如果图片宽度不能充满屏幕则自动处理成充满模式
                    //.bindCustomPlayVideoCallback(new MyVideoSelectedPlayCallback(getContext()))// 自定义视频播放回调控制，用户可以使用自己的视频播放界面
                    //.bindCustomPreviewCallback(new MyCustomPreviewInterfaceListener())// 自定义图片预览回调接口
                    //.bindCustomCameraInterfaceListener(new MyCustomCameraInterfaceListener())// 提供给用户的一些额外的自定义操作回调
                    .bindCustomPermissionsObtainListener(new MyPermissionsObtainCallback())// 自定义权限拦截
                    //.bindCustomChooseLimitListener(new MyChooseLimitCallback()) // 自定义选择限制条件Dialog
                    //.cameraFileName(System.currentTimeMillis() +".jpg")    // 重命名拍照文件名、如果是相册拍照则内部会自动拼上当前时间戳防止重复，注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                    //.renameCompressFile(System.currentTimeMillis() +".jpg")// 重命名压缩文件名、 如果是多张压缩则内部会自动拼上当前时间戳防止重复
                    //.renameCropFileName(System.currentTimeMillis() + ".jpg")// 重命名裁剪文件名、 如果是多张裁剪则内部会自动拼上当前时间戳防止重复
                    .selectionMode(PictureConfig.MULTIPLE)// MULTIPLE多选 or SINGLE单选
                    .isPreviewImage(true)// 是否可预览图片
                    .isPreviewVideo(true)// 是否可预览视频
                    //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
                    //.queryMimeTypeConditions(PictureMimeType.ofWEBP())
                    .isEnablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)// 是否显示拍照按钮
                    //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
                    //.isMultipleRecyclerAnimation(false)// 多图裁剪底部列表显示动画效果
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.imageFormat(PictureMimeType.PNG) // 拍照保存图片格式后缀,默认jpeg,Android Q使用PictureMimeType.PNG_Q
                    .setCameraImageFormat(PictureMimeType.JPEG) // 相机图片格式后缀,默认.jpeg
                    .setCameraVideoFormat(PictureMimeType.MP4)// 相机视频格式后缀,默认.mp4
                    .setCameraAudioFormat(PictureMimeType.AMR)// 录音音频格式后缀,默认.amr
                    .isEnableCrop(false)// 是否裁剪
                    //.basicUCropConfig()//对外提供所有UCropOptions参数配制，但如果PictureSelector原本支持设置的还是会使用原有的设置
                    .isCompress(true)// 是否压缩
                    //.compressFocusAlpha(true)// 压缩时是否开启透明通道
                    //.compressEngine(ImageCompressEngine.createCompressEngine()) // 自定义压缩引擎
                    //.compressQuality(80)// 图片压缩后输出质量 0~ 100
                    .synOrAsy(false)//同步true或异步false 压缩 默认同步
                    //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                    //.compressSavePath(getPath())//压缩图片保存地址
                    .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义 默认0 ， 0
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    //.isWebp(false)// 是否显示webp图片,默认显示
                    //.isBmp(false)//是否显示bmp图片,默认显示
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    //.freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE)// 裁剪框拖动模式
                    .isCropDragSmoothToCenter(true)// 裁剪框拖动时图片自动跟随居中
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    //.setCropDimmedColor(ContextCompat.getColor(getContext(), R.color.app_color_white))// 设置裁剪背景色值
                    //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
                    //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
                    .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .isOpenClickSound(true)// 是否开启点击声音
                    .selectionData(mAdapterStuCard.getData())// 是否传入已选图片
                    //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                    //.videoMinSecond(10)// 查询多少秒以内的视频
                    //.videoMaxSecond(15)// 查询多少秒以内的视频
                    //.recordVideoSecond(10)//录制视频秒数 默认60s
                    //.isPreviewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    //.cutCompressFormat(Bitmap.CompressFormat.PNG.name())//裁剪图片输出Format格式，默认JPEG
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.cropImageWideHigh()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled(false) // 裁剪是否可旋转图片
                    //.scaleEnabled(false)// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                    //.forResult(new MyResultCallback(mAdapter));
                    .forResult(launcherStuCardResult);
        }
    };

    /**
     * 核酸证明启动相册相机
     */
    private final GridNucleicImageAdapter.onAddPicClickListener onAddNucleicPicClickListener = new GridNucleicImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            imgPathNucleicPic = null;
            PictureSelector.create(ApplyRunCommitActivity.this)
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
                    .selectionData(mAdapterNucleicPic.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    .forResult(launcherNucleicPicResult);
        }
    };

    /**
     * 健康码证明启动相册相机
     */
    private final GridHealCodeImageAdapter.onAddPicClickListener onAddHealCodeClickListener = new GridHealCodeImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            imgPathHealCode = null;
            PictureSelector.create(ApplyRunCommitActivity.this)
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
                    .selectionData(mAdapterHealCode.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    .forResult(launcherHealCodeResult);
        }
    };

    /**
     * 行程码证明启动相册相机
     */
    private final GridRunCodeImageAdapter.onAddPicClickListener onAddRunCodeClickListener = new GridRunCodeImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            imgPathRunCode = null;
            PictureSelector.create(ApplyRunCommitActivity.this)
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
                    .selectionData(mAdapterRunCode.getData())// 是否传入已选图片
                    .cutOutQuality(90)// 裁剪输出质量 默认100
                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                    .forResult(launcherRunCodeResult);
        }
    };

    /**
     * 创建ActivityResultLauncher 回调监听学生证图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityStuCardResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(result.getData());
                            // 例如 LocalMedia 里面返回五种path
                            // 1.media.getPath(); 原图path
                            // 2.media.getCutPath();裁剪后path，需判断media.isCut();切勿直接使用
                            // 3.media.getCompressPath();压缩后path，需判断media.isCompressed();切勿直接使用
                            // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                            // 5.media.getAndroidQToPath();Android Q版本特有返回的字段，但如果开启了压缩或裁剪还是取裁剪或压缩路径；注意：.isAndroidQTransform 为false 此字段将返回空
                            // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
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
                                //OSS上传成功后，把imgPathList对象内存地址设置为null了，对象是唯一的，只是内存地址清空了，不是重复创建对象
                                //主要为避免选择多次相册图片，把上次选择的目录路径全往集合中，导致OSS批量遍历路径集合上传时，连旧的路径也重复上传
                                //既然设置为null，那么就进行判空处理，为空就新创集合，重组目录路径集合
                                if (imgPathStuCard != null) {
                                    imgPathStuCard = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathStuCard = media.getAndroidQToPath();
                                imgApplyCommitPathList.set(0, imgPathStuCard);
                                Log.i(TAG, "AndroidQ学生证Path" + media.getAndroidQToPath());
                            }
                            mAdapterStuCard.setList(selectList);
                            mAdapterStuCard.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 创建ActivityResultLauncher 回调监听核酸证明图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityNucleicPicResultLauncher() {
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
                                //OSS上传成功后，把imgPathList对象内存地址设置为null了，对象是唯一的，只是内存地址清空了，不是重复创建对象
                                //主要为避免选择多次相册图片，把上次选择的目录路径全往集合中，导致OSS批量遍历路径集合上传时，连旧的路径也重复上传
                                //既然设置为null，那么就进行判空处理，为空就新创集合，重组目录路径集合
                                if (imgPathNucleicPic != null) {
                                    imgPathNucleicPic = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathNucleicPic = media.getAndroidQToPath();
                                imgApplyCommitPathList.set(1, imgPathNucleicPic);
                                Log.i(TAG, "AndroidQ核酸证明Path:" + media.getAndroidQToPath());
                            }
                            mAdapterNucleicPic.setList(selectList);
                            mAdapterNucleicPic.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 创建ActivityResultLauncher 回调监听健康码图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityHealCodeResultLauncher() {
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
                                //OSS上传成功后，把imgPathList对象内存地址设置为null了，对象是唯一的，只是内存地址清空了，不是重复创建对象
                                //主要为避免选择多次相册图片，把上次选择的目录路径全往集合中，导致OSS批量遍历路径集合上传时，连旧的路径也重复上传
                                //既然设置为null，那么就进行判空处理，为空就新创集合，重组目录路径集合
                                if (imgPathHealCode != null) {
                                    imgPathHealCode = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathHealCode = media.getAndroidQToPath();
                                imgApplyCommitPathList.set(2, imgPathHealCode);
                                Log.i(TAG, "AndroidQ健康码Path:" + media.getAndroidQToPath());
                            }
                            mAdapterHealCode.setList(selectList);
                            mAdapterHealCode.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 创建ActivityResultLauncher 回调监听行程码图片路径地址
     *
     * @return 回调结果
     */
    private ActivityResultLauncher<Intent> createActivityRunCodeResultLauncher() {
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
                                //OSS上传成功后，把imgPathList对象内存地址设置为null了，对象是唯一的，只是内存地址清空了，不是重复创建对象
                                //主要为避免选择多次相册图片，把上次选择的目录路径全往集合中，导致OSS批量遍历路径集合上传时，连旧的路径也重复上传
                                //既然设置为null，那么就进行判空处理，为空就新创集合，重组目录路径集合
                                if (imgPathRunCode != null) {
                                    imgPathRunCode = null;
                                }
                                //Android Q 特有Path 赋值给目录路径的List集合
                                imgPathRunCode = media.getAndroidQToPath();
                                imgApplyCommitPathList.set(3, imgPathRunCode);
                                Log.i(TAG, "AndroidQ行程码Path:" + media.getAndroidQToPath());
                            }
                            mAdapterRunCode.setList(selectList);
                            mAdapterRunCode.notifyDataSetChanged();
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
        /* 1.学生证 */
        if (mAdapterStuCard != null && mAdapterStuCard.getData() != null && mAdapterStuCard.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterStuCard.getData());
        }
        /* 2.核酸证明 */
        if (mAdapterNucleicPic != null && mAdapterNucleicPic.getData() != null && mAdapterNucleicPic.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterNucleicPic.getData());
        }
        /* 3.健康码 */
        if (mAdapterHealCode != null && mAdapterHealCode.getData() != null && mAdapterHealCode.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterHealCode.getData());
        }
        /* 4.行程码 */
        if (mAdapterRunCode != null && mAdapterRunCode.getData() != null && mAdapterRunCode.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    (ArrayList<? extends Parcelable>) mAdapterRunCode.getData());
        }
    }

    /**
     * 广播接受回调的学生证图片
     */
    private final BroadcastReceiver stuCardBroadcastReceiver = new BroadcastReceiver() {
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
                    ToastUtils.show("校园帮APP提示：您已删除学生证图片！");
                    mAdapterStuCard.remove(position);
                    mAdapterStuCard.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathStuCard != null) {
                        imgPathStuCard = null;
                    }
                }
            }
        }
    };

    /**
     * 广播接受回调的核酸证明图片
     */
    private final BroadcastReceiver nucleicPicBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (Constant.ACTION_DELETE_NUCLEIC_PIC_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.show("校园帮APP提示：您已删除核酸证明图片！");
                    mAdapterNucleicPic.remove(position);
                    mAdapterNucleicPic.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathNucleicPic != null) {
                        imgPathNucleicPic = null;
                    }
                }
            }
        }
    };

    /**
     * 广播接受回调的健康码图片
     */
    private final BroadcastReceiver healCodeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (Constant.ACTION_DELETE_HEAL_CODE_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.show("校园帮APP提示：您已删除健康码图片！");
                    mAdapterHealCode.remove(position);
                    mAdapterHealCode.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathHealCode != null) {
                        imgPathHealCode = null;
                    }
                }
            }
        }
    };

    /**
     * 广播接受回调的行程码图片
     */
    private final BroadcastReceiver runCodeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            if (Constant.ACTION_DELETE_RUN_CODE_PREVIEW_POSITION.equals(action)) {
                // 外部预览删除按钮回调
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    int position = extras.getInt(PictureConfig.EXTRA_PREVIEW_DELETE_POSITION);
                    ToastUtils.show("校园帮APP提示：您已删除行程码图片！");
                    mAdapterRunCode.remove(position);
                    mAdapterRunCode.notifyItemRemoved(position);
                    //删除从相册回调的图片目录路径集合对应索引的图片，不设置将导致外部预览右上角删除图标点击后，OSS依旧可以读取之前的路径进行推送上传
                    if (imgPathRunCode != null) {
                        imgPathRunCode = null;
                    }
                }
            }
        }
    };

    /**
     * 回调数据
     *
     * @param requestCode 请求码，即调用startActivityForResult()传递过去的值
     * @param resultCode  结果码，结果码用于标识返回数据来自指定的Activity
     * @param data        返回数据，存放了返回数据的Intent，使用第三个输入参数可以取出新Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //实名认证完成回调真实姓名显示
        if (requestCode == Constant.REQUEST_CODE_VALUE && resultCode == Constant.RESULT_CODE_OCR_CAR_NUMBER_OCR_SUCCESS) {
            mTvCommitCar.setText(data.getStringExtra("CarNumberOCRResult"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

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
                    Toast.makeText(ApplyRunCommitActivity.this, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
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
        /* 1.清除学生证*/
        if (launcherStuCardResult != null) {
            launcherStuCardResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(stuCardBroadcastReceiver, Constant.ACTION_DELETE_STU_CARD_PREVIEW_POSITION);
        /* 2.清除核酸证明*/
        if (launcherNucleicPicResult != null) {
            launcherNucleicPicResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(nucleicPicBroadcastReceiver, Constant.ACTION_DELETE_NUCLEIC_PIC_PREVIEW_POSITION);
        /* 3.清除健康码*/
        if (launcherHealCodeResult != null) {
            launcherHealCodeResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(healCodeBroadcastReceiver, Constant.ACTION_DELETE_HEAL_CODE_PREVIEW_POSITION);
        /* 4.清除行程码*/
        if (launcherRunCodeResult != null) {
            launcherRunCodeResult.unregister();
        }
        BroadcastManager.getInstance(getContext()).unregisterReceiver(runCodeBroadcastReceiver, Constant.ACTION_DELETE_RUN_CODE_PREVIEW_POSITION);
        /* 5.清除学生证图片路径 */
        if (imgPathStuCard != null) {
            imgPathStuCard = null;
        }
        /* 6.清除核酸检测图片路径 */
        if (imgPathNucleicPic != null) {
            imgPathNucleicPic = null;
        }
        /* 7.清除健康码图片路径 */
        if (imgPathHealCode != null) {
            imgPathHealCode = null;
        }
        /* 8.清除健康码图片路径 */
        if (imgPathRunCode != null) {
            imgPathRunCode = null;
        }
        /* 9.清除子线程 */
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        /* 10.清除图片路径集合 */
        if (imgApplyCommitPathList != null) {
            imgPathRunCode = null;
            imgApplyCommitPathList.clear();
        }

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://此处理URL + OCR + 个人信息 上传后端数据库
                    //显示上传图片
                    //OSS上传成功重组的URL地址集合，OKGo上传后端数据库
                    List<String> ossUrlFileNameLists = (List<String>) msg.obj;
                    Log.i(TAG, "重组处理后的OSS地址集合: " + ossUrlFileNameLists);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String carNumberInfo = mTvCommitCar.getText().toString().trim();
                            if (carNumberInfo.equals("无车辆")) {
                                carNumberInfo = "";
                            } else {
                                carNumberInfo = "[" + carNumberInfo + "]";
                            }
                            OkGo.<String>post(Constant.USER_DO_APPLY_RUN_BY_MY_INFO)
                                    .tag("提交跑腿申请")
                                    .params("applyRoleType", 3) //跑腿 申请类型
                                    .params("applyCar", strCarType + carNumberInfo) //车辆信息
                                    .params("applyStuCard", ossUrlFileNameLists.get(0)) //学生证
                                    .params("applyNucleicPic", ossUrlFileNameLists.get(1)) //核算检测
                                    .params("applyHealthCode", ossUrlFileNameLists.get(2)) //健康码
                                    .params("applyRunCode", ossUrlFileNameLists.get(3)) //行程码
                                    .params("applyGraduationData", mTvCommitData.getText().toString().trim()) //毕业时间
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            startVibrator();//成功振动
                                            OkGoApplyRunBean okGoApplyRunBean = GsonUtil.gsonToBean(response.body(), OkGoApplyRunBean.class);
                                            if (200 == okGoApplyRunBean.getCode() && null == okGoApplyRunBean.getData() && "跑腿信息提交成功".equals(okGoApplyRunBean.getMsg())) {
                                                DialogPrompt dialogPrompt = new DialogPrompt(ApplyRunCommitActivity.this, "跑腿信息已提交成功，耐心等待超管审核哟~", 10);
                                                dialogPrompt.showAndFinish(ApplyRunCommitActivity.this);
                                                return;
                                            }
                                            if (200 == okGoApplyRunBean.getCode() && null == okGoApplyRunBean.getData() && "跑腿信息提交失败".equals(okGoApplyRunBean.getMsg())) {
                                                DialogPrompt dialogPrompt = new DialogPrompt(ApplyRunCommitActivity.this, "跑腿信息提交失败，如有疑问请联系我们~", 10);
                                                dialogPrompt.showAndFinish(ApplyRunCommitActivity.this);
                                            }
                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyRunCommitActivity.this, mrlCommitRunInfoShow, "请求错误，服务器连接失败！");
                                        }
                                    });
                        }
                    }, 1000);
                    break;
                case 2:
                    mCircleProgressCommit.reset();
                    mCircleProgressCommit.setVisibility(View.GONE);//进度条加载完成后，隐藏进度条
                    break;
            }
        }
    };
}
