package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import butterknife.BindBitmap;
import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.custominterface.ActivityInteraction;
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.AlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.scrollview.GoTopNestedScrollView;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.OnlyQQUserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoSessionAndUserBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.CustomAlertDialogUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
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
    //父布局
    @BindView(R2.id.scrollview_mine_info) GoTopNestedScrollView mScrollviewMineInfo;//绑定资源文件中mipmap中的ic_launcher图片
    //绑定资源文件中mipmap中的ic_launcher图片
    @BindBitmap(R.mipmap.index_not_login) Bitmap mIndex_not_login;
    /** 原生View布局 */
    @BindView(R2.id.qq_head) ImageView mQQHead;//QQ头像
    @BindView(R2.id.qq_back) ImageView mQQBack;//QQ头像高斯模糊背景
    @BindView(R2.id.qq_province) TextView mQQProvince;//QQ省份
    @BindView(R2.id.qq_city) TextView mQQCity;//QQ城市

    /** item控件--->用户信息 */
    @BindView(R2.id.stv_my_qq_nickname) SuperTextView mStvQQNickname;//QQ昵称
    @BindView(R2.id.stv_my_info) SuperTextView mStvMyInfo;//我的资料
    @BindView(R2.id.stv_my_cer) SuperTextView mStvMyCer;//实名认证
    @BindView(R2.id.stv_my_account_safe) SuperTextView mStvAccountSafe;//账户与安全
    @BindView(R2.id.stv_my_order) SuperTextView mStvMyOrder;//我的订单
    @BindView(R2.id.stv_my_feed_back) SuperTextView mStvFeedBack;//意见反馈
    @BindView(R2.id.stv_my_report) SuperTextView mStvReport;//违规举报
    @BindView(R2.id.stv_my_contact_us) SuperTextView mStvContactUs;//联系我们

    /** 自定义对话框 */
    private AlertDialog mDialog;
    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    OkGoSessionAndUserBean.Data.UserInfo userInfo = (OkGoSessionAndUserBean.Data.UserInfo) msg.obj;
                    if (userInfo !=null){
                        initUserNowCerInfo();//初始化已登录实名信息
                    }else {
                        mStvMyCer.setRightString("未登录");
                        mStvMyCer.setLeftTopString("未登录");
                        mStvMyCer.setLeftBottomString("未登录");
                    }
                    Log.i(TAG, "Fragment用户信息: "+ userInfo);
                    break;
                case 2://匹配成功，获取IndexActivity登录QQ用户信息
                    Constant.onlyQQUserInfo = (OnlyQQUserInfoBean) msg.obj;
                    mQQProvince.setText(Constant.onlyQQUserInfo.getProvince() + "省");//设置QQ信息的省份
                    mQQCity.setText(Constant.onlyQQUserInfo.getCity());//设置QQ信息的城市
                    mStvQQNickname.setRightString(Constant.onlyQQUserInfo.getNickname());//设置QQ信息的城市
                    //设置圆形图像
                    RequestOptions options = new RequestOptions();
                    options.circleCrop();
                    Glide.with(getActivity())
                            .load(Constant.onlyQQUserInfo.getFigureurl_qq_2())
                            .apply(options)
                            .into(mQQHead);
                    //设置背景高斯模糊效果
                    Glide.with(getActivity()).load(Constant.onlyQQUserInfo.getFigureurl_qq_2())
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
                    mQQProvince.setText("请先");//设置QQ省份
                    mQQCity.setText("登录");//设置QQ城市
                    mStvQQNickname.setRightString("未登录");//设置QQ昵称
                    initUserNowCerInfo();//初始化未登录实名信息
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
                public void userAllInfoPutMineFragment(OkGoSessionAndUserBean.Data.UserInfo userInfo) {
                    onAttach(activity);
                    if (userInfo != null) {
                        Message msg = new Message();
                        //5.携带数据为输入框文本数据
                        msg.obj = userInfo;
                        //5.消息标记为1
                        msg.what = 1;
                        //5.开始发送消息
                        mHandler.sendMessage(msg);
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

    /**
     * 为我的信息item设置监听事件
     */
    @Override
    protected void initEvent() {
        //QQ昵称
        mStvQQNickname.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView QQNickname) {
                String strQQName = QQNickname.getRightTextView().getText().toString();
                ToastUtils.show(strQQName);
            }
        });

        //点击我的资料跳转详情页，提供完善信息功能
        mStvMyInfo.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
//                //方式一：获取本地持久化文件，判断有无数据
//                if (Constant.userInfo !=null){ //登录有数据
//                    int userId = Constant.userInfo.getUlId();
//                    // 实现 Android 自带的Parcelable接口，封装对象数据，通过Bundle键值对形式，传递参数到MineInfoActivity页面
//                    OkGo.<String>post(Constant.SELECT_USER_ALL_INFO_BY_USERID + "/" + userId)
//                            .tag("用户ID查询个人信息")
//                            .execute(new StringCallback() {
//                                @Override
//                                public void onSuccess(Response<String> response) {
//                                    MyXPopupUtils.getInstance().setShowDialog(getActivity(),"请求信息中...");
//                                    //Json字符串解析转为实体类对象
//                                    OkGoUserBean okGoUserBeanData = GsonUtil.gsonToBean(response.body(), OkGoUserBean.class);
//                                    Log.i(TAG, "okGoUserBeanData=== " + okGoUserBeanData);
//                                    Intent thisIntentToMineActivity= new Intent();;
//                                    thisIntentToMineActivity.setClass(getActivity(),MineInfoActivity.class);
//                                    Bundle bundle=new Bundle();
//                                    //bundle.putString("userInfo",Constant.userInfo.toString());
//                                    bundle.putParcelable("userInfo", new ParcelableUserInfoData(
//                                            okGoUserBeanData.getData().getCreateTime(), okGoUserBeanData.getData().getUlClass(), okGoUserBeanData.getData().getUlDept(),
//                                            okGoUserBeanData.getData().getUlEmail(), okGoUserBeanData.getData().getUlId(), okGoUserBeanData.getData().getUlIdcard(),
//                                            okGoUserBeanData.getData().getUlRealname(), okGoUserBeanData.getData().getUlSex(), okGoUserBeanData.getData().getUlStuno(),
//                                            okGoUserBeanData.getData().getUlTel(), okGoUserBeanData.getData().getUlUsername(), okGoUserBeanData.getData().getUpdateTime()));
//                                    thisIntentToMineActivity.putExtras(bundle);
//                                    Handler handler = new Handler();
//                                    handler.postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            IntentUtil.startActivityAnimLeftToRight(getActivity(),thisIntentToMineActivity);
//                                            OkGo.getInstance().cancelTag("用户ID查询个人信息");
//                                        }
//                                    }, 200);
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    MyXPopupUtils.getInstance().setSmartDisDialog();
//                                }
//                            });
//                }else {
//                    CustomAlertDialogUtil.notification1(getActivity(),"温馨提示","您还没有登录呀~","朕知道了");
//                }
                //方式二：调用sa-Token后端检查登录API接口
                OkGo.<String>post(Constant.SA_TOKEN_CHECK_LOGIN)
                        .tag("检查登录")
                        .execute(new StringCallback() {
                            @Override
                            public void onStart(Request<String, ? extends Request> request) {
                                MyXPopupUtils.getInstance().setShowDialog(getActivity(), "请求信息中...");
                            }

                            @Override
                            public void onSuccess(Response<String> response) {
                                OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "当前账户已登录".equals(OkGoResponseBean.getMsg())) {
                                    IntentUtil.startActivityAnimLeftToRight(getActivity(),new Intent(getActivity(), MineInfoActivity.class));
                                    OkGo.getInstance().cancelTag("检查登录");
                                } else {
                                    CustomAlertDialogUtil.notification1(getActivity(),"温馨提示","您还没有登录呀~","朕知道了");
                                }
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                MyXPopupUtils.getInstance().setSmartDisDialog();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                OkGoErrorUtil.CustomFragmentOkGoError(response,getActivity(), mScrollviewMineInfo, "请求错误，服务器连接失败！");
                            }
                        });
            }
        });

        //实名认证
        mStvMyCer.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView myCer) {
                String strMyCer = myCer.getRightTextView().getText().toString();
                ToastUtils.show(strMyCer);
            }
        });

        //账户与安全
        mStvAccountSafe.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView accountSafe) {
                String strAccountSafe = accountSafe.getRightTextView().getText().toString();
                ToastUtils.show(strAccountSafe);
            }
        });

        //我的订单
        mStvMyOrder.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView MyOrder) {
                String strMyOrder = MyOrder.getLeftTextView().getText().toString();
                ToastUtils.show(strMyOrder);
            }
        });

        //意见反馈
        mStvFeedBack.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView superTextView) {
                MyXPopupUtils.getInstance().setShowDialog(getActivity(), "请求跳转中...");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                        IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), UserApplyUntieActivity.class));
                    }
                }, 500);
            }
        });

        //违规举报
        mStvReport.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView report) {
                String strReport = report.getLeftTextView().getText().toString();
                ToastUtils.show(strReport);
            }
        });

        //联系我们
        mStvContactUs.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView contactUs) {
                String strContactUs = contactUs.getLeftTextView().getText().toString();
                ToastUtils.show(strContactUs);
            }
        });
    }

    @Override
    protected void doBusiness(Context context) {
        //initUserNowCerInfo(); //初始化用户当前实名信息
    }


    /**
     * 初始化用户当前实名信息
     */
    private void initUserNowCerInfo(){
        OkGo.<String>post(Constant.SELECT_NOW_CER_STATE_BY_SA_TOKEN_LOGIN_REAL_NAME)
                .tag("查询当前用户实名状态")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        Log.i(TAG, "查询当前用户实名状态: " + response.body());
                        if (401 == OkGoResponseBean.getCode() && "未提供Token".equals(OkGoResponseBean.getData()) && "验证失败，禁止访问".equals(OkGoResponseBean.getMsg())) {
                            //设置未登录状态
                            mStvMyCer.setRightString("未登录");
                            mStvMyCer.setLeftTopString("未登录");
                            mStvMyCer.setLeftBottomString("未登录");
                            return;
                        }
                        if (200 == OkGoResponseBean.getCode() && "已实名认证".equals(OkGoResponseBean.getData()) && "success".equals(OkGoResponseBean.getMsg())) {
                            mStvMyCer.setRightString(OkGoResponseBean.getData());//设置实名状态
                            return;
                        }
                        if (200 == OkGoResponseBean.getCode() && "未实名认证".equals(OkGoResponseBean.getData()) && "error".equals(OkGoResponseBean.getMsg())) {
                            mStvMyCer.setRightString(OkGoResponseBean.getData());//设置实名状态
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response,getActivity(), mScrollviewMineInfo, "请求错误，服务器连接失败！");
                    }
                });
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
