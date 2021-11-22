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
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;


/**
 * created by on 2021/8/20
 * 描述：开发者通过真实姓名踢人下线
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperKickOffLineRealName extends BaseFragment {
    private static final String TAG = "DeveloperKickOffLineRealName";
    @BindView(R2.id.rl_dev_kickoff_realname) RelativeLayout mRlDevKicOffRealName;
    //被下线真实姓名输入框
    @BindView(R2.id.edit_kick_offline_realname)
    PowerfulEditText mEditKickOffLineRealName;
    //确定执行下线
    @BindView(R2.id.btn_kickoffLine)
    Button mBtnKickoffLine;

    @Override
    protected int bindLayout() {
        return R.layout.developer_kickoff_line_realname;
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
    @OnClick({R2.id.edit_kick_offline_realname, R2.id.btn_kickoffLine})
    public void onKickOffLineUsernameViewClick(View view) {
        switch (view.getId()) {
            case R.id.edit_kick_offline_realname://动画登录
                break;
            case R.id.btn_kickoffLine://确定执行
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
                        ResponseBean responseBean = GsonUtil.gsonToBean(response.body(), ResponseBean.class);
                        //失败
                        if (401 == responseBean.getCode() && "未提供Token".equals(responseBean.getData()) && "验证失败，禁止访问".equals(responseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, "未登录："+responseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }

                        if (401 == responseBean.getCode() && "验证失败，禁止访问".equals(responseBean.getMsg()) && "已被系统强制下线".equals(responseBean.getData())){
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
                                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, "超过3次提醒，将被永久封号！", Snackbar.LENGTH_INDEFINITE)
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
                        if (200 == responseBean.getCode() && "踢人下线失败，此真实姓名不存在".equals(responseBean.getMsg())){
                            mEditKickOffLineRealName.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, responseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功
                        if (200 == responseBean.getCode() && "踢人下线成功".equals(responseBean.getMsg())){
                            Snackbar snackbar = Snackbar.make(mRlDevKicOffRealName, "踢下线成功："+responseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response,getActivity(), mRlDevKicOffRealName, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public void onClick(View view) {

    }
}
