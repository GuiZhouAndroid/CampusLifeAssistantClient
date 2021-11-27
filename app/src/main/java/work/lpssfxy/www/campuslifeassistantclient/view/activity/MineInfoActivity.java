package work.lpssfxy.www.campuslifeassistantclient.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.helloworld.library.MiddleDialogConfig;
import com.helloworld.library.utils.DialogEnum;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.StringDialogCallback;
import work.lpssfxy.www.campuslifeassistantclient.base.index.ItemView;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyRegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;

/**
 * created by on 2021/11/6
 * 描述：个人信息
 *
 * @author ZSAndroid
 * @create 2021-11-06-17:12
 */
@SuppressLint("NonConstantResourceId")
public class MineInfoActivity extends BaseActivity {
    private static final String TAG = "MineInfoActivity";

    //父布局控件
    @BindView(R2.id.ll_mine_info_show) LinearLayout mLlMineInfoShow;
    /** 返回按钮 */
    @BindView(R2.id.iv_my_info_back) ImageView mIvMyInfoBack;
    /** Toolbar */
    @BindView(R2.id.toolbar_my_info) Toolbar mToolbarMyInfo;
    /** RefreshLayout盒子下拉刷新 */
    @BindView(R2.id.refreshLayout_my_info) RefreshLayout mRefreshLayoutMyInfo;

    /**
     * item控件--->用户信息
     */
    @BindView(R2.id.ll_user_number) ItemView mUserNumber;//用户编号
    @BindView(R2.id.ll_username) ItemView mUserName;//用户名
    @BindView(R2.id.ll_sex) ItemView mSex;//性别
    @BindView(R2.id.ll_realname) ItemView mRealName;//真实姓名
    @BindView(R2.id.ll_idcard) ItemView mIdCard;//身份证号
    @BindView(R2.id.ll_stuno) ItemView mStuNo;//学号
    @BindView(R2.id.ll_tel) ItemView mTel;//手机号
    @BindView(R2.id.ll_email) ItemView mEmail;//QQ邮箱
    @BindView(R2.id.ll_dept) ItemView mDept;//所属院系
    @BindView(R2.id.ll_class) ItemView mClass;//专业班级
    @BindView(R2.id.ll_create_time) ItemView mCreateTime;//账户注册时间
    @BindView(R2.id.ll_update_time) ItemView mUpdateTime;//账户更新时间

    //当前查询的用户名，利于此值，进行所有用户信息作为网络请求后端API的参数传递
    private String nowUserName;

    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1://匹配成功，获取IndexActivity个人用户信息
                    OkGoUserBean.Data UserInfoData = (OkGoUserBean.Data) msg.obj;
                    if ( UserInfoData!= null) {
                        mUserNumber.setRightDesc(String.valueOf(UserInfoData.getUlId()));
                        mUserName.setRightDesc(UserInfoData.getUlUsername());
                        mSex.setRightDesc(UserInfoData.getUlSex());
                        mRealName.setRightDesc(UserInfoData.getUlRealname());
                        mIdCard.setRightDesc(UserInfoData.getUlIdcard());
                        mStuNo.setRightDesc(UserInfoData.getUlStuno());
                        mTel.setRightDesc(UserInfoData.getUlTel());
                        mEmail.setRightDesc(UserInfoData.getUlEmail());
                        mDept.setRightDesc(UserInfoData.getUlDept());
                        mClass.setRightDesc(UserInfoData.getUlClass());
                        mCreateTime.setRightDesc(UserInfoData.getCreateTime());
                        mUpdateTime.setRightDesc(UserInfoData.getUpdateTime());
                    }
                    break;
            }
        }
    };

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
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return true;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }


    @Override
    public int bindLayout() {
        return R.layout.mine_info_activity;
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
        setSupportActionBar(mToolbarMyInfo);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        Bundle bundle = getIntent().getExtras();
//        //String testBundleString = bundle.getString("userInfo");
//        if (bundle != null) {
//            ParcelableUserInfoData parcelableUserInfoData = bundle.getParcelable("userInfo");
//            Log.i("用户信息=", parcelableUserInfoData.toString());
//            //全局当前用户名，用于调用更新信息接口
//            nowUserName = parcelableUserInfoData.getUlUsername();
//            Message message = new Message();
//            message.obj = parcelableUserInfoData;
//            message.what = 1;
//            mHandler.sendMessage(message);
//        }
    }

    @Override
    protected void initEvent() {
        managerMyUserInfo();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {
        //设置默认主题
        setThemeColor(this,mToolbarMyInfo,mRefreshLayoutMyInfo,R.color.boxBg, R.color.boxBg);
        //进入页面自动刷新调用查询API接口，拉取用户数据
        doRefresh();
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_my_info_back})
    public void onMyInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_my_info_back://点击返回
                MineInfoActivity.this.finish();
                break;
        }
    }

    /**
     * 进入页面自动刷新调用查询API接口，拉取用户数据
     */
    private void doRefresh() {
        XPopupUtils.getInstance().setShowDialog(MineInfoActivity.this,"拉取信息中...");
        //进入触发自动刷新
        mRefreshLayoutMyInfo.autoRefresh();
        //下拉刷新
        mRefreshLayoutMyInfo.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startSelectLoginUserInfoByUserId();
            }
        });
    }

    /**
     * 通过用户ID开始查询全部信息
     */
    public void startSelectLoginUserInfoByUserId(){
        //查询当前已登录账号ID的Session信息，从Session中获取用户ID，提供此ID查询用户全部信息
        OkGo.<String>post(Constant.SA_TOKEN_GET_SESSION_INFO)
                .tag("用户登录Session信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        //Json字符串解析转为实体类对象
                        OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
                        if (200 == okGoUserBeanData.getCode() & null != okGoUserBeanData.getData() && "success".equals(okGoUserBeanData.getMsg())){
                            // 通过Session中的ID查询用户全部信息
                            OkGo.<String>post(Constant.SELECT_USER_ALL_INFO_BY_USERID + "/" + okGoUserBeanData.getData().getUlId())
                                    .tag("用户ID查询个人信息")
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            //Json字符串解析转为实体类对象
                                            OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
                                            //全局当前用户名，用于调用更新信息接口
                                            nowUserName = okGoUserBeanData.getData().getUlUsername();
                                            Message message = new Message();
                                            message.obj = okGoUserBeanData.getData();
                                            message.what = 1;
                                            mHandler.sendMessage(message);
                                        }

                                        @Override
                                        public void onFinish() {
                                            //延时2.5秒完成刷新
                                            mRefreshLayoutMyInfo.finishRefresh(1000);
                                            XPopupUtils.getInstance().setSmartDisDialog();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }
    public void managerMyUserInfo() {
        mUserNumber.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, "【" + text + "】" + "唯一编号，无法修改~", Toast.LENGTH_SHORT).show();
            }
        });
        mUserName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldUserName) {
                new MiddleDialogConfig().builder(MineInfoActivity.this)
                        .setEditHint("输入新的用户名")
                        .setEditHintColor("#FF4081")
                        .setTitleColor("#FF4081")
                        .setEditTextColor("#00bfff")
                        .setTitle("更新用户名")
                        .setDialogStyle(DialogEnum.EDIT)
                        .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                            @Override
                            public void rightBtn(String newUserName) {
                                if (MyRegexUtils.checkUsername(newUserName, 5, 10)) {
                                    OkGo.<String>post(Constant.UPDATE_USERNAME + "/" + nowUserName + "/" + oldUserName + "/" + newUserName)
                                            .tag("更新用户名")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "用户名更新失败！", 3);
                                                        dialogPrompt.show();
                                                    }

                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "更新请求被系统拒绝！此用户名已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "用户名格式错误！必须是5-10位且不包含特殊符号，可以是汉字且不能以'_'结尾！");
                                dialogPrompt.show();
                            }
                        })
                        .show();
            }
        });
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldSex) {
                Toast.makeText(MineInfoActivity.this, "【" + oldSex + "】" + "性别无法修改~", Toast.LENGTH_SHORT).show();
            }
        });
        mRealName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldRealName) {
                Toast.makeText(MineInfoActivity.this, "真实姓名无法修改~", Toast.LENGTH_SHORT).show();
            }
        });
        mIdCard.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldIdCard) {
                Toast.makeText(MineInfoActivity.this, "【" + oldIdCard + "】" + "身份证号无法修改~", Toast.LENGTH_SHORT).show();
            }
        });
        mStuNo.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldStuNo) {
                new MiddleDialogConfig().builder(MineInfoActivity.this)
                        .setEditHint("输入新的学号")
                        .setEditHintColor("#FF4081")
                        .setTitleColor("#FF4081")
                        .setEditTextColor("#00bfff")
                        .setTitle("更新学号")
                        .setDialogStyle(DialogEnum.EDIT)
                        .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                            @Override
                            public void rightBtn(String newStuNo) {
                                if (MyRegexUtils.checkEnglishAndNumber(newStuNo)) {//正则表达式，判断身份证号
                                    OkGo.<String>post(Constant.UPDATE_STU_NO + "/" + nowUserName + "/" + oldStuNo + "/" + newStuNo)
                                            .tag("更新学号")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "学号更新失败！", 3);
                                                        dialogPrompt.show();
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "更新请求被系统拒绝！此学号已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "【" + newStuNo + "】" + "学号格式错误！", 3);
                                dialogPrompt.show();
                            }
                        })
                        .show();
            }
        });
        mTel.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldTel) {
                new MiddleDialogConfig().builder(MineInfoActivity.this)
                        .setEditHint("输入新的手机号")
                        .setEditHintColor("#FF4081")
                        .setTitleColor("#FF4081")
                        .setEditTextColor("#00bfff")
                        .setTitle("更新手机号")
                        .setDialogStyle(DialogEnum.EDIT)
                        .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                            @Override
                            public void rightBtn(String newTel) {
                                if (MyRegexUtils.checkMobile(newTel)) {//正则表达式，判断手机号
                                    OkGo.<String>post(Constant.UPDATE_TEL + "/" + nowUserName + "/" + oldTel + "/" + newTel)
                                            .tag("更新手机号")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "手机号更新失败！", 3);
                                                        dialogPrompt.show();
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "更新请求被系统拒绝！此手机号已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "【" + newTel + "】" + "手机号格式错误！", 3);
                                dialogPrompt.show();
                            }
                        })
                        .show();
            }
        });
        mEmail.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldEmail) {
                new MiddleDialogConfig().builder(MineInfoActivity.this)
                        .setEditHint("输入新的QQ邮箱")
                        .setEditHintColor("#FF4081")
                        .setTitleColor("#FF4081")
                        .setEditTextColor("#00bfff")
                        .setTitle("更新QQ邮箱")
                        .setDialogStyle(DialogEnum.EDIT)
                        .setRightCallBack(new MiddleDialogConfig.RightCallBack() {
                            @Override
                            public void rightBtn(String newEmail) {
                                if (MyRegexUtils.checkEmail(newEmail)) {//正则表达式，判断QQ邮箱
                                    OkGo.<String>post(Constant.UPDATE_QQ_EMAIL + "/" + nowUserName + "/" + oldEmail + "/" + newEmail)
                                            .tag("更新QQ邮箱")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "QQ邮箱更新失败！", 3);
                                                        dialogPrompt.show();
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "更新请求被系统拒绝！此QQ邮箱已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "【" + newEmail + "】" + "QQ邮箱格式错误！", 3);
                                dialogPrompt.show();
                            }
                        })
                        .show();
            }
        });
        mDept.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldDept) {
                CommonPickerPopup popup = new CommonPickerPopup(MineInfoActivity.this);
                ArrayList<String> list = new ArrayList<String>();
                list.add("数学与计算机科学学院");
                list.add("马克思主义学院");
                list.add("旅游与历史文化学院");
                list.add("文学与新闻学院");
                list.add("外国语学院");
                list.add("教育科学学院");
                list.add("物理与电气工程学院");
                list.add("化学与材料工程学院");
                list.add("生物科学与技术学院");
                list.add("矿业与土木工程学院");
                list.add("美术与设计学院");
                list.add("体育学院");
                list.add("经济与管理学院");
                list.add("音乐学院");
                list.add("继续教育学院");
                popup.setPickerData(list).setCurrentItem(0);//默认选中《数学与计算机科学学院》
                popup.setCommonPickerListener(new CommonPickerListener() {
                    @Override
                    public void onItemSelected(int index, String newDept) {
                        OkGo.<String>post(Constant.UPDATE_DEPT + "/" + nowUserName + "/" + oldDept + "/" + newDept)
                                .tag("更新所属院系")
                                .execute(new StringDialogCallback(MineInfoActivity.this) {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                        if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                            doRefresh();
                                            return;
                                        }
                                        if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                            DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "所属院系更新失败！", 3);
                                            dialogPrompt.show();
                                        }
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "请求错误，服务器连接失败！");
                                    }
                                });
                    }
                });
                new XPopup.Builder(MineInfoActivity.this)
                        .asCustom(popup)
                        .show();
            }
        });
        mClass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldClass) {
                CommonPickerPopup popup = new CommonPickerPopup(MineInfoActivity.this);
                ArrayList<String> list = new ArrayList<String>();
                list.add("计算机科学与技术");
                list.add("城乡规划");
                list.add("机械电子工程");
                list.add("冶金工程");
                list.add("植物科学与技术");
                list.add("教育科学学院");
                popup.setPickerData(list).setCurrentItem(0);//默认选中《数学与计算机科学学院》
                popup.setCommonPickerListener(new CommonPickerListener() {
                    @Override
                    public void onItemSelected(int index, String newClass) {
                        OkGo.<String>post(Constant.UPDATE_CLASS + "/" + nowUserName + "/" + oldClass + "/" + newClass)
                                .tag("更新专业班级")
                                .execute(new StringDialogCallback(MineInfoActivity.this) {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                        if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                            doRefresh();
                                            return;
                                        }
                                        if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                            DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this, "专业班级更新失败！", 3);
                                            dialogPrompt.show();
                                        }
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "请求错误，服务器连接失败！");
                                    }
                                });
                    }
                });
                new XPopup.Builder(MineInfoActivity.this)
                        .asCustom(popup)
                        .show();
            }
        });
        mCreateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mUpdateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
