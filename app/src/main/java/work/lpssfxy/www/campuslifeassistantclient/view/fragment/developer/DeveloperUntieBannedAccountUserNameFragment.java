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

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/16
 * 描述：开发者通过用户名查询解除封禁
 *
 * @author ZSAndroid
 * @create 2021-11-16-13:04
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperUntieBannedAccountUserNameFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectBannedTimeRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_un_ban_account_realname) RelativeLayout mRlDevUnBanAccountRealName;
    //待查讯真实姓名
    @BindView(R2.id.edit_un_ban_account_realname) PowerfulEditText mEditUnBanAccountRealName;
    //确定执行下线
    @BindView(R2.id.btn_un_ban_account_realname) ButtonView mBtnUnBanAccountRealName;

    /**
     * @return 单例对象
     */
    public static DeveloperUntieBannedAccountUserNameFragment newInstance() {
        return new DeveloperUntieBannedAccountUserNameFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_untie_banned_account_real_name;
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
    @OnClick({R2.id.btn_un_ban_account_realname})
    public void onUnBannedAccountTimeRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_un_ban_account_realname://确定执行
                //超管输入的用户真实姓名
                String strUnBanAccountRealName = mEditUnBanAccountRealName.getText().toString().trim();
                doUnBanAccount(strUnBanAccountRealName);
                break;
        }
    }

    /**
     * 开始解除账户封禁状态
     *
     * @param strUnBanAccountRealName 真实姓名
     */
    private void doUnBanAccount(String strUnBanAccountRealName) {
        //判空处理
        if (TextUtils.isEmpty(strUnBanAccountRealName)) {
            mEditUnBanAccountRealName.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevUnBanAccountRealName, "请填入真实姓名", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行解除封禁操作
        OkGo.<String>post(Constant.ADMIN_TO_UNTIE_BANNED_ACCOUNT_BY_REAL_NAME)
                .tag("解除封禁真实姓名")
                .params("accountBannedValues", strUnBanAccountRealName)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在解封...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == OkGoResponseBean.getCode() && "未提供Token".equals(OkGoResponseBean.getData()) && "验证失败，禁止访问".equals(OkGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevUnBanAccountRealName, "未登录：" + OkGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(未封禁)
                        if (200 == OkGoResponseBean.getCode() && "此账户未封禁".equals(OkGoResponseBean.getMsg())) {
                            mEditUnBanAccountRealName.startShakeAnimation();//抖动输入框
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), "此账户未封禁！" + OkGoResponseBean.getData());
                            dialogPrompt.show();
                            return;
                        }
                        //成功(已解封)
                        if (200 == OkGoResponseBean.getCode() && "此账户已解除封禁".equals(OkGoResponseBean.getMsg())) {
                            mEditUnBanAccountRealName.startShakeAnimation();//抖动输入框
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), "此账户已解除封禁！" + OkGoResponseBean.getData());
                            dialogPrompt.show();
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevUnBanAccountRealName, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

