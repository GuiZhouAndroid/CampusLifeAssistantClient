package work.lpssfxy.www.campuslifeassistantclient.base.custompopup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.impl.FullScreenPopupView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllUserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;

/**
 * created by on 2021/11/29
 * 描述：全屏底部弹窗--->跑腿的户通过用ID查询对应详情信息
 *
 * @author ZSAndroid
 * @create 2021-11-29-14:24
 */


public class UserInfoFullPopup extends FullScreenPopupView {

    private String userId;//用户ID

    private Context context;

    public UserInfoFullPopup(@NonNull Context context) {
        super(context);
    }

    public UserInfoFullPopup(@NonNull Context context, String userId) {
        super(context);
        this.context = context;
        this.userId = userId;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.developer_select_apply_cer_user_info_fullscreen_popup;
    }

    @Override
    protected void onShow() {
        super.onShow();

        RelativeLayout relativeLayout = findViewById(R.id.fl_user_info_show);//相对布局

        relativeLayout.setOnClickListener(new OnClickListener() { //触摸屏幕退出
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        LinearLayout LlUserShow = findViewById(R.id.ll_user_show);//线性布局
        LlUserShow.setOnClickListener(new OnClickListener() { //触摸屏幕退出
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ImageView mIvUserBack = findViewById(R.id.iv_user_back);//返回
        mIvUserBack.setOnClickListener(new OnClickListener() { //触摸返回图标退出
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        TextView mTvUserId = findViewById(R.id.tv_user_id);//用户ID
        TextView mTvUserName = findViewById(R.id.tv_user_name);//用户名
        TextView mTvUserRealName = findViewById(R.id.tv_user_real_name);//真实姓名
        TextView mTvUserSex = findViewById(R.id.tv_user_sex);//性别
        TextView mTvUserIdCard = findViewById(R.id.tv_user_id_card);//身份证号
        TextView mTvUserStuNo = findViewById(R.id.tv_user_stu_no);//学号
        TextView mTvUserTel = findViewById(R.id.tv_user_tel);//手机号
        TextView mTvUserEmail = findViewById(R.id.tv_user_email);//邮箱
        TextView mTvUserDept = findViewById(R.id.tv_user_dept);//所属院系
        TextView mTvUserClass = findViewById(R.id.tv_user_class);//班级专业
        TextView mTvUserRegisterTime = findViewById(R.id.tv_user_register_time);//注册时间
        TextView mTvUserUpdateTime = findViewById(R.id.tv_user_update_time);//更新时间
        //开始网络请求，访问后端服务器，执行查询角色操作
        OkGo.<String>post(Constant.ADMIN_SELECT_USER_INFO_BY_USER_ID + "/" + Integer.parseInt(userId))
                .tag("跑腿用户ID查询信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog((Activity) context, "正在查询...");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoAllUserInfoBean okGoAllUserInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllUserInfoBean.class);
                        if (200 == okGoAllUserInfoBean.getCode() && okGoAllUserInfoBean.getData().size() > 0 && "success".equals(okGoAllUserInfoBean.getMsg())) {
                            List<OkGoAllUserInfoBean.Data> userInfoList = okGoAllUserInfoBean.getData();
                            for (OkGoAllUserInfoBean.Data userInfo : userInfoList) {
                                mTvUserId.setText(String.valueOf(userInfo.getUlId()));
                                mTvUserName.setText(userInfo.getUlUsername());
                                mTvUserRealName.setText(userInfo.getUlRealname());
                                mTvUserSex.setText(userInfo.getUlSex());
                                mTvUserIdCard.setText(userInfo.getUlIdcard());
                                mTvUserStuNo.setText(userInfo.getUlStuno());
                                mTvUserTel.setText(userInfo.getUlTel());
                                mTvUserEmail.setText(userInfo.getUlEmail());
                                mTvUserDept.setText(userInfo.getUlDept());
                                mTvUserClass.setText(userInfo.getUlClass());
                                mTvUserRegisterTime.setText(userInfo.getCreateTime());
                                mTvUserUpdateTime.setText(userInfo.getUpdateTime());
                            }
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, (Activity) context, relativeLayout, "请求错误，服务器连接失败！");
                    }
                });

    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        OkGo.getInstance().cancelTag("跑腿用户ID查询信息");
    }
}
