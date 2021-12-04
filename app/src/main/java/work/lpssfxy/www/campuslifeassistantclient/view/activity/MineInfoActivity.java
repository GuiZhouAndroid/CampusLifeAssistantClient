package work.lpssfxy.www.campuslifeassistantclient.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CommonPickerListener;
import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;


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
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
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
                case 1:
                    //子线程获取用户数据成功
                    OkGoUserBean.Data UserInfoData = (OkGoUserBean.Data) msg.obj;
                    if (UserInfoData != null) {
                        //更新UI，设置用户数据到信息列表控件上
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
        return R.layout.activity_mine_info;
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
        setThemeColor(this, mToolbarMyInfo, mRefreshLayoutMyInfo, R.color.boxBg, R.color.boxBg);
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
        //开启加载进度条
        MyXPopupUtils.getInstance().setShowDialog(MineInfoActivity.this, "拉取信息中...");
        //自动触发下拉刷新
        mRefreshLayoutMyInfo.autoRefresh();
        //开始下拉刷新，执行调用API接口业务
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
    public void startSelectLoginUserInfoByUserId() {
        //查询当前已登录账号ID的Session信息，从Session中获取用户ID，提供此ID查询用户全部信息
        OkGo.<String>post(Constant.SA_TOKEN_GET_SESSION_INFO)
                .tag("用户登录Session信息")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //Json字符串解析转为实体类对象
                        OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
                        if (200 == okGoUserBeanData.getCode() & null != okGoUserBeanData.getData() && "success".equals(okGoUserBeanData.getMsg())) {
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
                                            //发送用户信息到子线程
                                            Message message = new Message();
                                            message.obj = okGoUserBeanData.getData();
                                            message.what = 1;
                                            mHandler.sendMessage(message);
                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            super.onError(response);
                                            OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "请求错误，服务器连接失败！");
                                        }

                                        @Override
                                        public void onFinish() {
                                            mRefreshLayoutMyInfo.finishRefresh();//结束下拉刷新
                                            MyXPopupUtils.getInstance().setNowDisDialog();//关闭加载进度条
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
                ToastUtils.show("【" + text + "】" + "唯一编号，无法修改");
            }
        });
        mUserName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldUserName) {
                new MaterialDialog.Builder(MineInfoActivity.this)
                        .customView(R.layout.activity_mine_info_update_user_username_dialog_item, true)
                        .titleGravity(GravityEnum.CENTER)
                        .title("换绑用户名" + oldUserName)
                        .titleColor(getResources().getColor(R.color.colorAccent))
                        .positiveText("换绑")
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .negativeText("取消")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //获取自定义布局中的控件id
                                MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check_user_username);
                                //新角色名称
                                String newUserName = materialEditText.getText().toString().trim();
                                if (MyRegexUtils.checkUsername(newUserName, 5, 10)) {
                                    OkGo.<String>post(Constant.UPDATE_USERNAME + "/" + nowUserName + "/" + oldUserName + "/" + newUserName)
                                            .tag("换绑用户名")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        ToastUtils.show("用户名换绑成功");
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        ToastUtils.show("【" + oldUserName + "】" + "用户名换绑失败");
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "换绑请求被系统拒绝！此用户名已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                ToastUtils.show("【" + newUserName + "】" + "用户名格式错误！必须是5-10位且不包含特殊符号，可以是汉字且不能以'_'结尾");
                            }
                        })
                        .show();
            }
        });
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldSex) {
                ToastUtils.show("【" + oldSex + "】" + "性别无法修改");
            }
        });
        mRealName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldRealName) {
                ToastUtils.show("【" + oldRealName + "】" + "真实姓名无法修改");
            }
        });
        mIdCard.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldIdCard) {
                ToastUtils.show("【" + oldIdCard + "】" + "身份证号无法修改");
            }
        });
        mStuNo.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldStuNo) {
                new MaterialDialog.Builder(MineInfoActivity.this)
                        .customView(R.layout.activity_mine_info_update_user_stu_no_dialog_item, true)
                        .titleGravity(GravityEnum.CENTER)
                        .title("换绑学号" + oldStuNo)
                        .titleColor(getResources().getColor(R.color.colorAccent))
                        .positiveText("换绑")
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .negativeText("取消")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //获取自定义布局中的控件id
                                MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check_user_stu_no);
                                //新角色名称
                                String newStuNo = materialEditText.getText().toString().trim();
                                if (MyRegexUtils.checkEnglishAndNumber(newStuNo)) {//正则表达式，判断学号格式
                                    OkGo.<String>post(Constant.UPDATE_STU_NO + "/" + nowUserName + "/" + oldStuNo + "/" + newStuNo)
                                            .tag("换绑学号")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        ToastUtils.show("学号换绑成功");
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        ToastUtils.show("【" + oldStuNo + "】" + "学号换绑失败");
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "换绑请求被系统拒绝！此学号已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                ToastUtils.show("【" + newStuNo + "】" + "换绑失败，学号格式错误");
                            }
                        })
                        .show();
            }
        });
        mTel.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldTel) {
                new MaterialDialog.Builder(MineInfoActivity.this)
                        .customView(R.layout.activity_mine_info_update_user_tel_dialog_item, true)
                        .titleGravity(GravityEnum.CENTER)
                        .title("换绑手机号" + oldTel)
                        .titleColor(getResources().getColor(R.color.colorAccent))
                        .positiveText("换绑")
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .negativeText("取消")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //获取自定义布局中的控件id
                                MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check_user_tel);
                                //新角色名称
                                String newTel = materialEditText.getText().toString().trim();
                                if (MyRegexUtils.checkMobile(newTel)) {//正则表达式，判断手机号
                                    OkGo.<String>post(Constant.UPDATE_TEL + "/" + nowUserName + "/" + oldTel + "/" + newTel)
                                            .tag("换绑学号")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        ToastUtils.show("手机号换绑成功");
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        ToastUtils.show("【" + oldTel + "】" + "手机号换绑失败");
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "换绑请求被系统拒绝！此手机号已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                ToastUtils.show("【" + newTel + "】" + "换绑失败，手机号格式错误");
                            }
                        })
                        .show();
            }
        });
        mEmail.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldEmail) {
                new MaterialDialog.Builder(MineInfoActivity.this)
                        .customView(R.layout.activity_mine_info_update_user_qq_email_dialog_item, true)
                        .titleGravity(GravityEnum.CENTER)
                        .title("换绑QQ邮箱" + oldEmail)
                        .titleColor(getResources().getColor(R.color.colorAccent))
                        .positiveText("换绑")
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .negativeText("取消")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //获取自定义布局中的控件id
                                MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check_user_qq_email);
                                //新角色名称
                                String newEmail = materialEditText.getText().toString().trim();
                                if (MyRegexUtils.checkEmail(newEmail)) {//正则表达式，判断QQ邮箱
                                    OkGo.<String>post(Constant.UPDATE_QQ_EMAIL + "/" + nowUserName + "/" + oldEmail + "/" + newEmail)
                                            .tag("换绑QQ邮箱")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                                                        doRefresh();
                                                        ToastUtils.show("QQ邮箱换绑成功");
                                                        return;
                                                    }
                                                    if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                                        ToastUtils.show("【" + oldEmail + "】" + "QQ邮箱换绑失败");
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, MineInfoActivity.this, mLlMineInfoShow, "换绑请求被系统拒绝！此学号已被别人使用");
                                                }
                                            });
                                    return;
                                }
                                ToastUtils.show("【" + newEmail + "】" + "换绑失败，QQ邮箱格式错误");
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
                                            ToastUtils.show("所属院系更新成功");
                                            return;
                                        }
                                        if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                            ToastUtils.show("【" + oldDept + "】" + "所属院系更新失败");
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
                                            ToastUtils.show("专业班级更新成功");
                                            return;
                                        }
                                        if (200 == OkGoResponseBean.getCode() && "false".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                                            ToastUtils.show("【" + oldClass + "】" + "专业班级更新失败");
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
            public void itemClick(String registerTime) {
                ToastUtils.show("您账号注册日期是：" + registerTime);
            }
        });
        mUpdateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String updateTime) {
                ToastUtils.show("您上次修改信息时间是：" + updateTime);
            }
        });
    }
}
