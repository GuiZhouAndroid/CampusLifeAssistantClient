package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.helloworld.library.MiddleDialogConfig;
import com.helloworld.library.utils.DialogEnum;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopupext.listener.CommonPickerListener;

import com.lxj.xpopupext.popup.CommonPickerPopup;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.entity.ParcelableData;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.StringDialogCallback;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.index.ItemView;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.RegexUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/11/6
 * 描述：个人信息
 *
 * @author ZSAndroid
 * @create 2021-11-06-17:12
 */
@SuppressLint("NonConstantResourceId")
public class MineInfoActivity extends BaseActivity  {
    private static final String TAG = "MineInfoActivity";

    /** item控件--->用户信息 */
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

    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1://匹配成功，获取IndexActivity个人用户信息
                    ParcelableData parcelableData = (ParcelableData) msg.obj;
                    if (parcelableData !=null){
                        mUserNumber.setRightDesc(String.valueOf(parcelableData.getUlId()));
                        mUserName.setRightDesc(parcelableData.getUlUsername());
                        mSex.setRightDesc(parcelableData.getUlSex());
                        mRealName.setRightDesc(parcelableData.getUlRealname());
                        mIdCard.setRightDesc(parcelableData.getUlIdcard());
                        mStuNo.setRightDesc(parcelableData.getUlStuno());
                        mTel.setRightDesc(parcelableData.getUlTel());
                        mEmail.setRightDesc(parcelableData.getUlEmail());
                        mDept.setRightDesc(parcelableData.getUlDept());
                        mClass.setRightDesc(parcelableData.getUlClass());
                        mCreateTime.setRightDesc(parcelableData.getCreateTime());
                        mUpdateTime.setRightDesc(parcelableData.getUpdateTime());
                    }
                    break;
            }
        }
    };
    private String nowUserName;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GlobalBus.getBus().register(this);
        MineInfoActivity activity=(MineInfoActivity)this;
    }

    @Override
    public int bindLayout() {
        return R.layout.mine_info_activity;
    }

    @Override
    protected void prepareData() {
        //GlobalBus.getBus().register(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();

        //String testBundleString = bundle.getString("userInfo");
        if (bundle!=null){
            ParcelableData parcelableData = bundle.getParcelable("userInfo");
            Log.i("用户信息=", parcelableData.toString());
            //全局当前用户名，用于调用更新信息接口
            nowUserName = parcelableData.getUlUsername();
            Message message = new Message();
            message.obj = parcelableData;
            message.what = 1;
            mHandler.sendMessage(message);
        }
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

    }

    public void managerMyUserInfo() {
        mUserNumber.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, "【"+text+"】"+"唯一编号，无法修改~", Toast.LENGTH_SHORT).show();
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
                                if (RegexUtils.checkUsername(newUserName,5,10)){
                                    OkGo.<String>post(Constant.UPDATE_USERNAME + "/" + nowUserName + "/" + oldUserName + "/" + newUserName)
                                            .tag("更新用户名")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                                    if(200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())){
                                                        mUserName.setRightDesc(newUserName);//设置文本为新用户名
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"用户名更新成功！",3);
                                                        dialogPrompt.showAndFinish(MineInfoActivity.this);
                                                        return;
                                                    }
                                                    if(200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())){
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"用户名更新失败！",3);
                                                        dialogPrompt.show();
                                                    }
                                                }
                                                @Override
                                                public void onError(Response<String> response) {
                                                    DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"请求错误，服务器连接失败",3);
                                                    dialogPrompt.showAndFinish(MineInfoActivity.this);
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"用户名格式错误！必须是5-10位且不包含特殊符号，可以是汉字且不能以'_'结尾！");
                                dialogPrompt.show();
                            }
                        })
                       .show();
            }
        });
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String oldSex) {
                Toast.makeText(MineInfoActivity.this, "【"+oldSex+"】"+"性别无法修改~", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MineInfoActivity.this, "【"+oldIdCard+"】"+"身份证号无法修改~", Toast.LENGTH_SHORT).show();
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
                                if (RegexUtils.checkEnglishAndNumber(newStuNo)){//正则表达式，判断身份证号
                                    OkGo.<String>post(Constant.UPDATE_STU_NO + "/" + nowUserName + "/" + oldStuNo + "/" + newStuNo)
                                            .tag("更新学号")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                                    if(200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())){
                                                        mStuNo.setRightDesc(newStuNo);//设置文本为新用户名
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"学号更新成功！",3);
                                                        dialogPrompt.show();
                                                        return;
                                                    }
                                                    if(200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())){
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"学号更新失败！",3);
                                                        dialogPrompt.show();
                                                    }
                                                }
                                                @Override
                                                public void onError(Response<String> response) {
                                                    DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"更新请求被系统拒绝！请输入本人学号！" ,10);
                                                    dialogPrompt.showAndFinish(MineInfoActivity.this);
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"【"+newStuNo+"】"+"学号格式错误！",3);
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
                                if (RegexUtils.checkMobile(newTel)){//正则表达式，判断手机号
                                    OkGo.<String>post(Constant.UPDATE_TEL + "/" + nowUserName + "/" + oldTel + "/" + newTel)
                                            .tag("更新手机号")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                                    if(200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())){
                                                        mTel.setRightDesc(newTel);//设置文本为新用户名
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"手机号更新成功！",3);
                                                        dialogPrompt.show();
                                                        return;
                                                    }
                                                    if(200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())){
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"手机号更新失败！",3);
                                                        dialogPrompt.show();
                                                    }
                                                }
                                                @Override
                                                public void onError(Response<String> response) {
                                                    DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"更新请求被系统拒绝！请输入本人手机号！" ,10);
                                                    dialogPrompt.showAndFinish(MineInfoActivity.this);
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"【"+newTel+"】"+"手机号格式错误！",3);
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
                                if (RegexUtils.checkEmail(newEmail)){//正则表达式，判断QQ邮箱
                                    OkGo.<String>post(Constant.UPDATE_QQ_EMAIL + "/" + nowUserName + "/" + oldEmail + "/" + newEmail)
                                            .tag("更新QQ邮箱")
                                            .execute(new StringDialogCallback(MineInfoActivity.this) {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                                    if(200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())){
                                                        mEmail.setRightDesc(newEmail);//设置文本为新QQ邮箱
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"QQ邮箱更新成功！",3);
                                                        dialogPrompt.show();
                                                        return;
                                                    }
                                                    if(200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())){
                                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"QQ邮箱更新失败！",3);
                                                        dialogPrompt.show();
                                                    }
                                                }
                                                @Override
                                                public void onError(Response<String> response) {
                                                    DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"更新请求被系统拒绝！请输入本人QQ邮箱！" ,10);
                                                    dialogPrompt.showAndFinish(MineInfoActivity.this);
                                                }
                                            });
                                    return;
                                }
                                DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"【"+newEmail+"】"+"QQ邮箱格式错误！",3);
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
                        OkGo.<String>post(Constant.UPDATE_DEPT+ "/" + nowUserName  + "/" + oldDept + "/" + newDept)
                                .tag("更新所属院系")
                                .execute(new StringDialogCallback(MineInfoActivity.this) {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                        if(200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())){
                                            mDept.setRightDesc(newDept);//设置文本为新所属院系
                                            DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"所属院系更新成功！",3);
                                            dialogPrompt.show();
                                            return;
                                        }
                                        if(200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())){
                                            DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"所属院系更新失败！",3);
                                            dialogPrompt.show();
                                        }
                                    }
                                    @Override
                                    public void onError(Response<String> response) {
                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"请求错误，服务器连接失败",3);
                                        dialogPrompt.showAndFinish(MineInfoActivity.this);
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
                        OkGo.<String>post(Constant.UPDATE_CLASS+ "/" + nowUserName  + "/" + oldClass + "/" + newClass)
                                .tag("更新专业班级")
                                .execute(new StringDialogCallback(MineInfoActivity.this) {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                                        if(200 == responseBean.getCode() && "true".equals(responseBean.getData()) && "success".equals(responseBean.getMsg())){
                                            mClass.setRightDesc(newClass);//设置文本为新所属专业班级
                                            DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"专业班级更新成功！",3);
                                            dialogPrompt.show();
                                            return;
                                        }
                                        if(200 == responseBean.getCode() && "false".equals(responseBean.getData()) && "error".equals(responseBean.getMsg())){
                                            DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"专业班级更新失败！",3);
                                            dialogPrompt.show();
                                        }
                                    }
                                    @Override
                                    public void onError(Response<String> response) {
                                        DialogPrompt dialogPrompt = new DialogPrompt(MineInfoActivity.this,"请求错误，服务器连接失败",3);
                                        dialogPrompt.showAndFinish(MineInfoActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MineInfoActivity.this.finish();
    }


    /**
     * 销毁时：清楚全部网络请求
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
        //GlobalBus.getBus().unregister(this);
    }

}
