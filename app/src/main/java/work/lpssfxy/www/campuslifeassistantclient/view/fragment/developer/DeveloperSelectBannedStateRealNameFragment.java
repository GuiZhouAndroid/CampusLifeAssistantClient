package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/16
 * 描述：开发者通过用户名查询封禁状态信息
 *
 * @author ZSAndroid
 * @create 2021-11-16-13:04
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSelectBannedStateRealNameFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectBannedStateRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_ban_account_state_realname) RelativeLayout mRlDevBanAccountStateRealName;
    //待查讯真实姓名
    @BindView(R2.id.edit_ban_account_state_realname) PowerfulEditText mEditBanAccountStateRealName;
    //确定执行下线
    @BindView(R2.id.btn_ban_account_state_realname) Button mBtnBanAccountStateRealName;

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_banned_state_real_name;
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
    @OnClick({R2.id.btn_ban_account_state_realname})
    public void onBannedAccountStateRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ban_account_state_realname://确定执行
                //超管输入的用户真实姓名
                String strBanAccountStateRealName = mEditBanAccountStateRealName.getText().toString().trim();
                doSelectBanAccountState(strBanAccountStateRealName);
                break;
        }
    }

    /**
     * 开始查询真实姓名对应账户的封禁状态
     *
     * @param strBanAccountStateRealName 真实姓名
     */
    private void doSelectBanAccountState(String strBanAccountStateRealName) {
        //判空处理
        if (TextUtils.isEmpty(strBanAccountStateRealName)) {
            mEditBanAccountStateRealName.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevBanAccountStateRealName, "请填入真实姓名", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行查封状态操作
        OkGo.<String>post(Constant.ADMIN_SELECT_BANNED_ACCOUNT_STATE_BY_REAL_NAME)
                .tag("查封状态真实姓名")
                .params("accountBannedValues", strBanAccountStateRealName)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                        //失败(超管未登录)
                        if (401 == responseBean.getCode() && "未提供Token".equals(responseBean.getData()) && "验证失败，禁止访问".equals(responseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevBanAccountStateRealName, "未登录：" + responseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功,未封禁
                        if (200 == responseBean.getCode() && "此账户未封禁".equals(responseBean.getMsg())) {
                            mEditBanAccountStateRealName.startShakeAnimation();//抖动输入框
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), "此账户未封禁！" + responseBean.getData());
                            dialogPrompt.show();
                            return;
                        }
                        //成功,已封禁
                        if (200 == responseBean.getCode() && "此账户已封禁".equals(responseBean.getMsg())) {
                            mEditBanAccountStateRealName.startShakeAnimation();//抖动输入框
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), "此账户已封禁！" + responseBean.getData());
                            dialogPrompt.show();

                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        XPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevBanAccountStateRealName, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

