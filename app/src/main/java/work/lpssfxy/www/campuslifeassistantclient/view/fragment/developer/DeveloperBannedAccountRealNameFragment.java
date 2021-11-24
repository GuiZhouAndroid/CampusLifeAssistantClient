package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import work.lpssfxy.www.campuslifeassistantclient.base.dialog.CustomAlertDialog;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.ResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/16
 * 描述：开发者通过用户名封禁账户
 *
 * @author ZSAndroid
 * @create 2021-11-16-13:03
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperBannedAccountRealNameFragment extends BaseFragment {

    private static final String TAG = "DeveloperBannedAccountRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_ban_account_realname) RelativeLayout mRlDevBanAccountRealName;
    //被封禁真实姓名
    @BindView(R2.id.edit_ban_account_realname) PowerfulEditText mEditBanAccountRealName;
    //被封禁时长/天
    @BindView(R2.id.edit_ban_account_day) PowerfulEditText mEditBanAccountDay;
    //确定执行下线
    @BindView(R2.id.btn_ban_account_realname) Button mBtnBanAccountRealName;

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_banned_account_real_name;
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
    @OnClick({R2.id.btn_ban_account_realname})
    public void onBannedAccountRealNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ban_account_realname://确定执行
                //超管输入的用户真实姓名、封禁时长
                String strBanAccountRealName = mEditBanAccountRealName.getText().toString().trim();
                String strBanAccountDay = mEditBanAccountDay.getText().toString().trim();
                doBanAccount(strBanAccountRealName, strBanAccountDay);
                break;
        }
    }

    /**
     * 开始封禁指定账户
     *
     * @param strBanAccountRealName 真实姓名
     * @param strBanAccountDay      封禁时长
     */
    private void doBanAccount(String strBanAccountRealName, String strBanAccountDay) {
        //判空处理
        if (TextUtils.isEmpty(strBanAccountRealName)) {
            mEditBanAccountRealName.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevBanAccountRealName, "请填入真实姓名", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (TextUtils.isEmpty(strBanAccountDay)) {
            mEditBanAccountDay.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevBanAccountRealName, "请填入天数", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行封禁账户操作
        OkGo.<String>post(Constant.ADMIN_BANNED_ACCOUNT_BY_REAL_NAME)
                .tag("真实姓名封禁账户")
                .params("accountBannedValues", strBanAccountRealName)
                .params("accountBannedDay", Long.parseLong(strBanAccountDay))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在执行...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                        //失败(超管未登录)
                        if (401 == responseBean.getCode() && "未提供Token".equals(responseBean.getData()) && "验证失败，禁止访问".equals(responseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevBanAccountRealName, "未登录：" + responseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //失败(超管封禁了自己或强制下线了自己)
                        if (401 == responseBean.getCode() && "验证失败，禁止访问".equals(responseBean.getMsg()) && "已被系统强制下线".equals(responseBean.getData())) {
                            new CustomAlertDialog(getActivity())
                                    .builder()
                                    .setCancelable(false)
                                    .setTitle("超管提示")
                                    .setTitleTextColor("#FF0000")
                                    .setTitleTextSizeSp(18)
                                    .setTitleTextBold(true)
                                    .setMsg("已被系统超管强制下线！")
                                    .setMsgTextBold(true)
                                    .setOkButton("我知道了", 0, "#FF0000", "", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Snackbar snackbar = Snackbar.make(mRlDevBanAccountRealName, "超过3次提醒，将被永久封号！", Snackbar.LENGTH_INDEFINITE)
                                                    .setActionTextColor(getResources().getColor(R.color.colorAccent))//设置点击按钮的字体颜色
                                                    .setAction("我知道了", new View.OnClickListener() {  //设置点击按钮
                                                        @Override
                                                        public void onClick(View v) {
                                                            Toast.makeText(getActivity(), "别撒谎喔~", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                            //设置Snackbar上提示的字体颜色
                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                            snackbar.show();
                                        }
                                    })
                                    .show();
                            return;
                        }
                        //成功(真实姓名不存在)
                        if (200 == responseBean.getCode() && "封禁账户失败，此真实姓名不存在".equals(responseBean.getMsg())) {
                            mEditBanAccountRealName.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevBanAccountRealName, responseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(已封禁)
                        if (200 == responseBean.getCode() && "账户封禁成功".equals(responseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevBanAccountRealName, "账户封禁成功：" + responseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevBanAccountRealName, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}

