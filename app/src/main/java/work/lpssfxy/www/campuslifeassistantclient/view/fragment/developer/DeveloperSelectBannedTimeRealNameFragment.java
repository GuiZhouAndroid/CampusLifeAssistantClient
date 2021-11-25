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
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.DialogPrompt;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/16
 * 描述：开发者通过用户名查询封禁剩余时间
 *
 * @author ZSAndroid
 * @create 2021-11-16-13:04
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSelectBannedTimeRealNameFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectBannedTimeRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_ban_account_time_realname) RelativeLayout mRlDevBanAccountTimeRealName;
    //待查讯真实姓名
    @BindView(R2.id.edit_ban_account_time_realname) PowerfulEditText mEditBanAccountTimeRealName;
    //确定执行下线
    @BindView(R2.id.btn_ban_account_time_realname) Button mBtnBanAccountTimeRealName;

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_banned_time_real_name;
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
    @OnClick({R2.id.btn_ban_account_time_realname})
    public void onBannedAccountTimeRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ban_account_time_realname://确定执行
                //超管输入的用户真实姓名
                String strBanAccountTimeRealName = mEditBanAccountTimeRealName.getText().toString().trim();
                doSelectBanAccountTime(strBanAccountTimeRealName);
                break;
        }
    }

    /**
     * 开始查询账户剩余的封禁时间
     *
     * @param strBanAccountTimeRealName 真实姓名
     */
    private void doSelectBanAccountTime(String strBanAccountTimeRealName) {
        //判空处理
        if (TextUtils.isEmpty(strBanAccountTimeRealName)) {
            mEditBanAccountTimeRealName.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevBanAccountTimeRealName, "请填入真实姓名", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行查封剩余时间操作
        OkGo.<String>post(Constant.ADMIN_SELECT_BANNED_ACCOUNT_RESIDUE_TIME_BY_REAL_NAME)
                .tag("查封剩余时间真实姓名")
                .params("accountBannedValues", strBanAccountTimeRealName)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == OkGoResponseBean.getCode() && "未提供Token".equals(OkGoResponseBean.getData()) && "验证失败，禁止访问".equals(OkGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevBanAccountTimeRealName, "未登录：" + OkGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(未封禁)
                        if (200 == OkGoResponseBean.getCode() && "此账户未封禁".equals(OkGoResponseBean.getMsg())) {
                            mEditBanAccountTimeRealName.startShakeAnimation();//抖动输入框
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), "此账户未封禁！" + OkGoResponseBean.getData());
                            dialogPrompt.show();
                            return;
                        }
                        //成功(已封禁)，显示状态+剩余时间
                        if (200 == OkGoResponseBean.getCode() && "此账户处于封禁状态".equals(OkGoResponseBean.getMsg())) {
                            mEditBanAccountTimeRealName.startShakeAnimation();//抖动输入框
                            DialogPrompt dialogPrompt = new DialogPrompt(getActivity(), "此账户已封禁！" + OkGoResponseBean.getData());
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevBanAccountTimeRealName, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

