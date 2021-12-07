package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.textview.MarqueeTextView;
import com.xuexiang.xui.widget.textview.marqueen.DisplayEntity;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/11/26
 * 描述：开发者通过用户ID查询封禁状态信息
 *
 * @author ZSAndroid
 * @create 2021-11-26-23:00
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperSelectBannedStateUserIdFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectBannedStateRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_ban_account_state_user_id) RelativeLayout mRlDevSelectBannedStateUserId;
    //待查询用户编号
    @BindView(R2.id.edit_ban_account_state_user_id) PowerfulEditText mEditSelectBannedStateUserId;
    //开始查询
    @BindView(R2.id.btn_ban_account_state_user_id) ButtonView mBtnSelectBannedStateUserId;
    //XUI跑马灯
    @BindView(R2.id.tv_role_user_list_show) MarqueeTextView mMtvRoleUserListShow;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectBannedStateUserIdFragment newInstance() {
        return new DeveloperSelectBannedStateUserIdFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_select_banned_state_user_id;
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

    }

    @Override
    protected void doBusiness(Context context) {

    }

    /**
     * @param view 视图View
     */
    @OnClick({R2.id.btn_ban_account_state_user_id})
    public void onSelectBannedStateUserIdViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ban_account_state_user_id://确定执行
                //超管输入的用户编号
                String strSelectBannedStateUserId = mEditSelectBannedStateUserId.getText().toString().trim();
                doSelectBannedStateUserId(strSelectBannedStateUserId);
                break;
        }
    }

    /**
     * 开始查询封禁状态
     *
     * @param strSelectBannedStateUserId 用户编号
     */
    private void doSelectBannedStateUserId(String strSelectBannedStateUserId) {
        //判空处理
        if (TextUtils.isEmpty(strSelectBannedStateUserId)) {
            mEditSelectBannedStateUserId.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevSelectBannedStateUserId, "请填入用户编号", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }

        //开始网络请求，访问后端服务器，执行查封状态操作
        OkGo.<String>post(Constant.QUERY_BANNED_STATE_BY_USERID + "/" + Integer.parseInt(strSelectBannedStateUserId))
                .tag("用户编号查询封禁状态")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //成功,用户ID不存在
                        if (200 == OkGoResponseBean.getCode() && "查询失败，此用户ID不存在".equals(OkGoResponseBean.getMsg())) {
                            //设置滚动集合数据
                            mMtvRoleUserListShow.startSimpleRoll(Collections.singletonList(OkGoResponseBean.getMsg()));
                            mMtvRoleUserListShow.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                                @Override
                                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                                    if (displayMsg.toString().equals(OkGoResponseBean.getMsg())) {
                                        return displayMsg;
                                    }
                                    return null;
                                }

                                @Override
                                public List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas) {
                                    return displayDatas;
                                }
                            });
                            mEditSelectBannedStateUserId.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevSelectBannedStateUserId, OkGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功,未封禁
                        if (200 == OkGoResponseBean.getCode() && "此账户未被封禁".equals(OkGoResponseBean.getMsg())) {
                            //设置滚动集合数据
                            mMtvRoleUserListShow.startSimpleRoll(Collections.singletonList(OkGoResponseBean.getData()));
                            mMtvRoleUserListShow.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                                @Override
                                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                                    if (displayMsg.toString().equals(OkGoResponseBean.getData())) {
                                        return displayMsg;
                                    }
                                    return null;
                                }

                                @Override
                                public List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas) {
                                    return displayDatas;
                                }
                            });
                            mEditSelectBannedStateUserId.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevSelectBannedStateUserId, OkGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }

                        //成功,已封禁
                        if (200 == OkGoResponseBean.getCode() && "此账户处于封禁状态".equals(OkGoResponseBean.getMsg())) {
                            //设置滚动集合数据
                            mMtvRoleUserListShow.startSimpleRoll(Collections.singletonList(OkGoResponseBean.getData()));
                            mMtvRoleUserListShow.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                                @Override
                                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                                    if (displayMsg.toString().equals(OkGoResponseBean.getData())) {
                                        return displayMsg;
                                    }
                                    return null;
                                }

                                @Override
                                public List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas) {
                                    return displayDatas;
                                }
                            });

                            mEditSelectBannedStateUserId.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevSelectBannedStateUserId, OkGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevSelectBannedStateUserId, "请求错误，服务器连接失败！");
                    }
                });
    }


    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMtvRoleUserListShow != null) {
            mMtvRoleUserListShow.clear();
        }
    }
}
