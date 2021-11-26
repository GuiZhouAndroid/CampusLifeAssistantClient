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
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;


/**
 * created by on 2021/8/20
 * 描述：开发者通过真实姓名踢人下线
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperKickOffLineRealNameFragment extends BaseFragment {
    private static final String TAG = "DeveloperKickOffLineRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_kickoff_realname) RelativeLayout mRlDevKicOffRealName;
    //被下线真实姓名输入框
    @BindView(R2.id.edit_kick_offline_realname) PowerfulEditText mEditKickOffLineRealName;
    //确定执行下线
    @BindView(R2.id.btn_kickoffLine_realname) ButtonView mBtnKickoffLineRealName;

    /**
     * @return 单例对象
     */
    public static DeveloperKickOffLineRealNameFragment newInstance() {
        return new DeveloperKickOffLineRealNameFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_kickoff_line_realname;
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
    @OnClick({R2.id.btn_kickoffLine_realname})
    public void onKickOffLineRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_kickoffLine_realname://确定执行
                //超管输入的用户真实姓名
                String strEditRealName = mEditKickOffLineRealName.getText().toString().trim();
                doKickOffLine(strEditRealName);
                break;
        }
    }

    /**
     * 点击执行下线
     *
     * @param strEditRealName 真实姓名
     */
    private void doKickOffLine(String strEditRealName) {
        //判空处理
        if (TextUtils.isEmpty(strEditRealName)) {
            mEditKickOffLineRealName.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, "请填入真实姓名", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行强制下线操作
        OkGo.<String>post(Constant.ADMIN_KICK_BY_REAL_NAME)
                .tag("真实姓名踢人下线")
                .params("kickIdValues", strEditRealName)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在执行...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == OkGoResponseBean.getCode() && "未提供Token".equals(OkGoResponseBean.getData()) && "验证失败，禁止访问".equals(OkGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, "未登录：" + OkGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(下线失败)
                        if (200 == OkGoResponseBean.getCode() && "踢人下线失败，此真实姓名不存在".equals(OkGoResponseBean.getMsg())) {
                            mEditKickOffLineRealName.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, OkGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(下线成功)
                        if (200 == OkGoResponseBean.getCode() && "踢人下线成功".equals(OkGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, "踢下线成功：" + OkGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevKicOffRealName, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
