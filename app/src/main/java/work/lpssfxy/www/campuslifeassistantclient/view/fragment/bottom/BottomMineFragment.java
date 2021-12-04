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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.List;

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
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoRoleOrPermissionListBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoSessionAndUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserCerBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.SharePreferenceUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.CustomAlertDialogUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.IndexActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.MineInfoActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.UserApplyUntieActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.UserCerBindOCRIdCardActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.UserCerSelectIdCardActivity;
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
    /** QQ个人资料 */
    private OnlyQQUserInfoBean onlyQQUserInfo;
    /** 自定义对话框 */
    private AlertDialog mDialog;
    private String strNowRole;

    public static BottomMineFragment newInstance() {
        //GlobalBus.getBus().register(fragment);
        return new BottomMineFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IndexActivity activity = (IndexActivity) getActivity();
        if (activity != null) {
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
                        //mHandler.sendEmptyMessage(1); //实名认证动态设置登录状态信息
                        mHandler.sendEmptyMessage(3); //其它未登录信息 (QQ未登录+用户未登录等头像文本的信息设置)
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
        return R.layout.fragment_index_bottom_mine;
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
                superTextView.setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
                    @Override
                    public void onClick(TextView textView) {
                        ToastUtils.show(strNowRole);
                    }
                });
//                //方式一：获取本地持久化文件，判断有无数据
//                if (userInfo !=null){ //登录有数据
//                    int userId = userInfo.getUlId();
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
//                                    //bundle.putString("userInfo",userInfo.toString());
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
                                    IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), MineInfoActivity.class));
                                    OkGo.getInstance().cancelTag("检查登录");
                                } else {
                                    CustomAlertDialogUtil.notification1(getActivity(), "温馨提示", "您还没有登录呀~", "朕知道了");
                                }
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                MyXPopupUtils.getInstance().setSmartDisDialog();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mScrollviewMineInfo, "请求错误，服务器连接失败！");
                            }
                        });
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

    }


    /**
     * 初始化用户当前实名信息
     * @param userInfo
     */
    private void initUserNowCerInfo(OkGoSessionAndUserBean.Data.UserInfo userInfo) {
        OkGo.<String>post(Constant.SELECT_NOW_CER_ALL_INFO_BY_SA_TOKEN_LOGIN_REAL_NAME)
                .tag("查询当前用户实名状态")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoUserCerBean okGoUserCerBean = GsonUtil.gsonToBean(response.body(), OkGoUserCerBean.class);
                        Log.i(TAG, "查询当前用户实名状态: " + okGoUserCerBean);
                        if (200 == okGoUserCerBean.getCode() && null == okGoUserCerBean.getData() && "未登录".equals(okGoUserCerBean.getMsg())) {
                            //设置未登录状态
                            mStvMyCer.setRightString("未登录");
                            mStvMyCer.setLeftTopString("未登录");
                            mStvMyCer.setLeftBottomString("未登录");
                            mStvMyCer.setRightTvDrawableLeft(null);
                            return;
                        }
                        if (200 == okGoUserCerBean.getCode() && null == okGoUserCerBean.getData() && "success".equals(okGoUserCerBean.getMsg())) {
                            mStvMyCer.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.notcer));
                            //组装未实名状态假数据，用于判断true或false进入绑定或查看详情页面
                            OkGoUserCerBean.Data customFalseCerState = new OkGoUserCerBean.Data(new OkGoUserCerBean.Data.UserCertificationBean(false));
                            mStvMyCer.setLeftTopString("您暂未认证");//认证真实姓名
                            mStvMyCer.setLeftBottomString("请绑定身份证");//认证身份证号
                            userDoToBindOrSelectCerIdCard(customFalseCerState, userInfo);
                            return;
                        }
                        if (200 == okGoUserCerBean.getCode() && null != okGoUserCerBean.getData() && "success".equals(okGoUserCerBean.getMsg())) {
                            mStvMyCer.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.alreadycer));
                            mStvMyCer.setLeftTopString(okGoUserCerBean.getData().getUlRealname());//认证真实姓名
                            //认证安全显示处理过的身份证号
                            String strCerIdCard = okGoUserCerBean.getData().getUserCertificationBean().getTucIdcard().replaceAll("(\\d{4})\\d{8}(\\w{6})", "$1*****$2");
                            mStvMyCer.setLeftBottomString(strCerIdCard);
                            userDoToBindOrSelectCerIdCard(okGoUserCerBean.getData(),userInfo);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mScrollviewMineInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 初始化账户封禁信息
     * @param userInfo QQSession+用户全部信息(并集信息中的用户全部信息)
     */
    private void initUserAccountBannedInfo(OkGoSessionAndUserBean.Data.UserInfo userInfo) {
        OkGo.<String>post(Constant.QUERY_BANNED_STATE_BY_USERID + "/" + userInfo.getUlId())
                .tag("当前账户封禁状态")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == okGoResponseBean.getCode() && "查询失败，此用户ID不存在".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show(okGoResponseBean.getMsg());
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "此账户未被封禁".equals(okGoResponseBean.getMsg())) {
                            mStvAccountSafe.setRightString("正在保护");//设置实名状态
                            mStvAccountSafe.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.safeaccount));//账户与安全图标
                            return;
                        }
                        if (200 == okGoResponseBean.getCode() && "此账户处于封禁状态".equals(okGoResponseBean.getMsg())) {
                            SharePreferenceUtil.removeObject(getActivity(), OkGoSessionAndUserBean.class);//清空持久化xml文件
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().finish();
                                }
                            }, 300000);
                            CustomAlertDialogUtil.notification1(getActivity(), "超管提示", "您已被系统超管强制下线，5分钟后将自动退出APP！如有疑问，请联系开发者！" + okGoResponseBean.getData(), "我知道了");
                            mStvAccountSafe.setRightString("已被封禁");//设置实名状态
                            mStvAccountSafe.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.alreayban));//账户与安全图标
                        }
                    }
                });
    }

    /**
     * 初始化我的信息
     *
     * @param userInfo QQSession+用户全部信息(并集信息中的用户全部信息)
     */
    private void initUserMyInfo(OkGoSessionAndUserBean.Data.UserInfo userInfo) {
        OkGo.<String>post(Constant.SA_TOKEN_REDIS_USER_SESSION_SELECT_ROLE_LIST_BY_REAL_NAME_TO_USERNAME)
                .tag("当前登录会话角色集合")
                .execute(new StringCallback() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoRoleOrPermissionListBean okGoRoleOrPermissionListBean = GsonUtil.gsonToBean(response.body(), OkGoRoleOrPermissionListBean.class);
                        Log.i(TAG, "OkGoRoleOrPermissionListBean: " + okGoRoleOrPermissionListBean.getData());
                        if (200 == okGoRoleOrPermissionListBean.getCode() && "未登录".equals(okGoRoleOrPermissionListBean.getMsg())){
                            mStvMyInfo.setRightString("未登录");//我的资料
                            mStvMyInfo.setRightTvDrawableLeft(null);
                            return;
                        }
                        if (200 == okGoRoleOrPermissionListBean.getCode() && "success".equals(okGoRoleOrPermissionListBean.getMsg())){
                            List<String> roleStringList =  okGoRoleOrPermissionListBean.getData();
                            if (roleStringList.contains("超管") && roleStringList.contains("学生") && roleStringList.contains("跑腿")) {
                                mStvMyInfo.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.supermanager));//设置管理员角色图片
                                strNowRole = "您目前身份是尊贵的超管开发者";
                            } else if(roleStringList.contains("学生") && roleStringList.contains("跑腿")){
                                strNowRole = "您已成为跑腿认证学生，欢迎您的加入";
                                mStvMyInfo.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.run));//设置跑腿角色图片
                            } else if(roleStringList.contains("学生")){
                                strNowRole = "您目前是普通用户，无资格兼职接单，快去申请加入跑腿大家庭吧";
                                mStvMyInfo.setRightTvDrawableLeft(getResources().getDrawable(R.mipmap.ordinary));//设置普通用户角色图片
                            }
                            return;
                        }
                        if (200 == okGoRoleOrPermissionListBean.getCode() && "error".equals(okGoRoleOrPermissionListBean.getMsg())){
                            mStvMyInfo.setRightString("无资格");
                            mStvMyInfo.setRightTvDrawableLeft(null);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mScrollviewMineInfo, "请求错误，服务器连接失败！");
                    }
                });
    }
    /**
     * 设置监听事件：根据实名认证状态布尔值执行相应的：查看已实名认证相关信息 + 跳转绑定身份证号
     *
     * @param data 当前用户实名认证信息
     * @param userInfo QQSession+用户全部信息(并集信息中的用户全部信息)
     */
    private void userDoToBindOrSelectCerIdCard(OkGoUserCerBean.Data data, OkGoSessionAndUserBean.Data.UserInfo userInfo) {
        //实名认证
        mStvMyCer.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClick(SuperTextView myCer) {
                if (data.getUserCertificationBean().isTucState()) { //已实名，跳转实名详情页
                    IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), UserCerSelectIdCardActivity.class));
                } else { //未实名，跳转OCR绑定界面
                    Intent thisToCerBindOCRIdCard = new Intent(getActivity(), UserCerBindOCRIdCardActivity.class);
                    thisToCerBindOCRIdCard.putExtra("NowDoCerUserId", String.valueOf(userInfo.getUlId()));
                    thisToCerBindOCRIdCard.putExtra("NowDoCerUserRealName", userInfo.getUlRealname());
                    IntentUtil.startActivityForResultAnimLeftToRight(getActivity(), new Intent(thisToCerBindOCRIdCard), Constant.REQUEST_CODE_VALUE);
                }
            }
        });
    }

    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    OkGoSessionAndUserBean.Data.UserInfo userInfo = (OkGoSessionAndUserBean.Data.UserInfo) msg.obj;
                    if (userInfo != null) {
                        initUserNowCerInfo(userInfo);//初始化已登录实名信息
                        initUserAccountBannedInfo(userInfo);//初始化账户安全
                        initUserMyInfo(userInfo);//初始化我的信息
                    } else {
//                        mStvMyCer.setLeftTopString("未登录");
//                        mStvMyCer.setLeftBottomString("未登录");
//                        mStvMyCer.setRightString("未登录");
//                        mStvMyCer.setRightTvDrawableLeft(null);
//                        mStvMyInfo.setRightTvDrawableLeft(null);
//                        mStvAccountSafe.setRightString("未登录");//账户与安全文本
//                        mStvAccountSafe.setRightTvDrawableLeft(null);//账户与安全图标
                    }
                    Log.i(TAG, "Fragment用户信息: " + userInfo);
                    break;
                case 2://匹配成功，获取IndexActivity登录QQ用户信息
                    onlyQQUserInfo = (OnlyQQUserInfoBean) msg.obj;
                    if (onlyQQUserInfo != null) {
                        mQQProvince.setText(onlyQQUserInfo.getProvince() + "省");//设置QQ信息的省份
                        mQQCity.setText(onlyQQUserInfo.getCity());//设置QQ信息的城市
                        mStvQQNickname.setRightString(onlyQQUserInfo.getNickname());//设置QQ信息的城市
                        //设置圆形图像
                        RequestOptions options = new RequestOptions();
                        options.circleCrop();
                        Glide.with(getActivity())
                                .load(onlyQQUserInfo.getFigureurl_qq_2())
                                .apply(options)
                                .into(mQQHead);
                        //设置背景高斯模糊效果
                        Glide.with(getActivity()).load(onlyQQUserInfo.getFigureurl_qq_2())
                                .transform(new BlurTransformation(20, 3))
                                .into(mQQBack);
                    }
                    break;
                case 3://匹配成功，接收IndexActivity发来的空消息--->未登录设置默认值
                    //设置圆形图像
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

                    mStvMyInfo.setRightString("未登录");//我的资料
                    mStvMyInfo.setRightTvDrawableLeft(null);

                    mStvAccountSafe.setRightString("未登录");//账户与安全文本
                    mStvAccountSafe.setRightTvDrawableLeft(null);//账户与安全图标
                    initUserNowCerInfo(null);//初始化未登录实名信息
                    break;
            }
        }
    };

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
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        //GlobalBus.getBus().unregister(this);
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

}
