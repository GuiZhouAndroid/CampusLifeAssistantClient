package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindBitmap;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.custominterface.ActivityInteraction;
import work.lpssfxy.www.campuslifeassistantclient.entity.ParcelableData;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.index.ItemView;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.CustomAlertDialogUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.IndexActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.MineInfoActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.UserApplyUntieActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;


/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */

@SuppressLint("NonConstantResourceId")
public class BottomMineFragment extends BaseFragment {
    private static final String TAG = "BottomMineFragment";
    @BindBitmap(R.mipmap.index_not_login) Bitmap mIndex_not_login;//绑定资源文件中mipmap中的ic_launcher图片
    /** 原生View布局 */
    @BindView(R2.id.qq_head) ImageView mQQHead;//QQ头像
    @BindView(R2.id.qq_back) ImageView mQQBack;//QQ头像高斯模糊背景
    @BindView(R2.id.qq_province) TextView mQQProvince;//QQ省份
    @BindView(R2.id.qq_city) TextView mQQCity;//QQ城市
    //@BindView(R2.id.btn_logout_login) Button mLogoutLogin;//注销登录

    /** item控件--->用户信息 */
    @BindView(R2.id.qq_nickname) ItemView mQQNickname;//QQ昵称
    @BindView(R2.id.ll_my_info) ItemView mMyInfo;//我的信息
    @BindView(R2.id.ll_my_goods) ItemView mMyGoods;//我的订单
    @BindView(R2.id.ll_feedback) ItemView mFeedback;//意见反馈
    @BindView(R2.id.ll_report_center) ItemView mReportCenter;//举报中心
    @BindView(R2.id.ll_account_safe) ItemView mAccountSafe;//账户安全
    @BindView(R2.id.ll_check_update) ItemView mCheckUpdate;//检查更新
    @BindView(R2.id.ll_system_set) ItemView mSystemSet;//系统设置

    /** 自定义对话框 */
    private AlertDialog mDialog;
    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    UserQQSessionBean.Data.UserInfo userInfo = (UserQQSessionBean.Data.UserInfo) msg.obj;
                    if (userInfo !=null){
                        mAccountSafe.setRightDesc(userInfo.getCreateTime());//设置QQ信息的城市
                    }else {
                        mAccountSafe.setRightDesc("未登录");//设置QQ信息的城市
                    }
                    Log.i(TAG, "Fragment用户信息: "+ userInfo);
                    break;
                case 2://匹配成功，获取IndexActivity登录QQ用户信息
                    Constant.qqUser = (QQUserBean) msg.obj;
                    mQQProvince.setText(Constant.qqUser.getProvince() + "省");//设置QQ信息的省份
                    mQQCity.setText(Constant.qqUser.getCity());//设置QQ信息的城市
                    mQQNickname.setRightDesc(Constant.qqUser.getNickname());//设置QQ信息的城市
                    //设置圆形图像
                    RequestOptions options = new RequestOptions();
                    options.circleCrop();
                    Glide.with(getActivity())
                            .load(Constant.qqUser.getFigureurl_qq_2())
                            .apply(options)
                            .into(mQQHead);
                    //设置背景高斯模糊效果
                    Glide.with(getActivity()).load(Constant.qqUser.getFigureurl_qq_2())
                            .transform(new BlurTransformation(20, 3))
                            .into(mQQBack);
                    break;
                case 3://匹配成功，接收IndexActivity发来的空消息--->未登录设置默认值
                    //设置圆形图像
                    Constant.userInfo = null;
                    RequestOptions options1 = new RequestOptions();
                    options1.circleCrop();
                    Glide.with(getActivity())
                            .load(mIndex_not_login)
                            .apply(options1)
                            .into(mQQHead);
                    //设置背景高斯模糊效果
                    Glide.with(getActivity())
                            .load(mIndex_not_login)
                            .transform(new BlurTransformation(20, 3))
                            .into(mQQBack);
                    mQQProvince.setText("请先");//设置QQ信息的省份
                    mQQCity.setText("登录");//设置QQ信息的城市
                    mQQNickname.setRightDesc("未登录");//设置QQ信息的城市
                    break;
            }
        }
    };

    public static BottomMineFragment newInstance(){
        //GlobalBus.getBus().register(fragment);
        return new BottomMineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IndexActivity activity=(IndexActivity) getActivity();
        if (activity!=null){
            activity.setStartPushUserInfoListener(new ActivityInteraction() {
                @Override
                public void userAllInfoPutMineFragment(UserQQSessionBean.Data.UserInfo userInfo) {
                    onAttach(activity);
                    if (userInfo != null) {
                        Message msg = new Message();
                        //5.携带数据为输入框文本数据
                        msg.obj = userInfo;
                        //5.消息标记为1
                        msg.what = 1;
                        //5.开始发送消息
                        mHandler.sendMessage(msg);
                        //Toast.makeText(getActivity(), userInfo.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        mHandler.sendEmptyMessage(3);
                    }
                }

            });
        }
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        IndexActivity mIndexActivity = (IndexActivity) activity;
        //通过强转成宿主activity，就可以获取到传递过来的数据
        //titles = mIndexActivity.getTitles();
        mIndexActivity.setHandler(mHandler);
    }

    @Override
    protected int bindLayout() {
        return R.layout.index_fragment_bottom_mine;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mQQNickname.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        //点击我的信息跳转详情页，提供完善信息功能
        mMyInfo.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                if (Constant.userInfo !=null){ //登录有数据
                    int userId = Constant.userInfo.getUlId();
                    // 实现 Android 自带的Parcelable接口，封装对象数据，通过Bundle键值对形式，传递参数到MineInfoActivity页面
                    OkGo.<String>post(Constant.SELECT_USER_ALL_INFO_BY_USERID + "/" + userId)
                            .tag("用户ID查询个人信息")
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    XPopupUtils.getInstance().setShowDialog(getActivity(),"请求信息中...");
                                    //Json字符串解析转为实体类对象
                                    UserBean userBeanData = GsonUtil.gsonToBean(response.body(), UserBean.class);
                                    Log.i(TAG, "userBeanData=== " + userBeanData);
                                    Intent thisIntentToMineActivity= new Intent();;
                                    thisIntentToMineActivity.setClass(getActivity(),MineInfoActivity.class);
                                    Bundle bundle=new Bundle();
                                    //bundle.putString("userInfo",Constant.userInfo.toString());
                                    bundle.putParcelable("userInfo", new ParcelableData(
                                            userBeanData.getData().getCreateTime(),userBeanData.getData().getUlClass(), userBeanData.getData().getUlDept(),
                                            userBeanData.getData().getUlEmail(),userBeanData.getData().getUlId(), userBeanData.getData().getUlIdcard(),
                                            userBeanData.getData().getUlRealname(),userBeanData.getData().getUlSex(),userBeanData.getData().getUlStuno(),
                                            userBeanData.getData().getUlTel(),userBeanData.getData().getUlUsername(),userBeanData.getData().getUpdateTime()));
                                    thisIntentToMineActivity.putExtras(bundle);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            IntentUtil.startActivityAnimLeftToRight(getActivity(),thisIntentToMineActivity);
                                            OkGo.getInstance().cancelTag("用户ID查询个人信息");
                                        }
                                    }, 200);
                                }

                                @Override
                                public void onFinish() {
                                    XPopupUtils.getInstance().setSmartDisDialog();
                                }
                            });
                }else {
                    //如果没有
                    CustomAlertDialogUtil.notification1(getActivity(),"温馨提示","您还没有登录呀~","朕知道了");
                }
            }
        });
        mMyGoods.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mFeedback.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mReportCenter.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mAccountSafe.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                XPopupUtils.getInstance().setShowDialog(getActivity(),"请求跳转中...");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        XPopupUtils.getInstance().setSmartDisDialog();
                        IntentUtil.startActivityAnimLeftToRight(getActivity(),new Intent(getActivity(), UserApplyUntieActivity.class));
                    }
                }, 500);
            }
        });
        mCheckUpdate.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mSystemSet.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void doBusiness(Context context) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.get(getActivity()).clearMemory();
        //Unregister the Registered Event.
        //GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getActivity()).clearMemory();
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
        }
        //GlobalBus.getBus().unregister(this);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
