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
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;


/**
 * created by on 2021/8/20
 * 描述：开发者通过Token踢人下线
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperKickOffLineTokenFragment extends BaseFragment {

    private static final String TAG = "DeveloperKickOffLineTokenFragment";
    //父布局
    @BindView(R2.id.rl_dev_kickoff_token) RelativeLayout mRlDevKicOffToken;
    //被下线真实姓名输入框
    @BindView(R2.id.edit_kick_offline_token) PowerfulEditText mEditKickOffLineToken;
    //确定执行下线
    @BindView(R2.id.btn_kickoffLine_token) ButtonView mBtnKickoffLineToken;

    /**
     * @return 单例对象
     */
    public static DeveloperKickOffLineTokenFragment newInstance() {
        return new DeveloperKickOffLineTokenFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_kickoff_line_token;
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
    @OnClick({R2.id.btn_kickoffLine_token})
    public void onKickOffLineRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kickoffLine_token://确定执行
                //超管输入的用户登录Token值
                String strEditToken = mEditKickOffLineToken.getText().toString().trim();
                doKickOffLine(strEditToken);
                break;
        }
    }


    /**
     * 点击执行下线
     *
     * @param strEditToken 登录Token
     */
    private void doKickOffLine(String strEditToken) {
        //判空处理
        if (TextUtils.isEmpty(strEditToken)) {
            mEditKickOffLineToken.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevKicOffToken, "请填入登录Token", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行强制下线操作
        OkGo.<String>post(Constant.ADMIN_KICK_BY_TOKEN_VALUES)
                .tag("登录Token下线")
                .params("kickTokenValues", strEditToken)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在执行...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffToken, "未登录：" + okGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(下线成功)
                        if (200 == okGoResponseBean.getCode() && "踢人下线成功".equals(okGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffToken, "踢下线成功：" + okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevKicOffToken, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
