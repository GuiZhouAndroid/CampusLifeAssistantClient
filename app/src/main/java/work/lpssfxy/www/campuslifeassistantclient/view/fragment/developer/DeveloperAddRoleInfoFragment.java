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
 * created by on 2021/11/24
 * 描述：开发者添加校园帮APP角色信息
 *
 * @author ZSAndroid
 * @create 2021-11-24-20:49
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperAddRoleInfoFragment extends BaseFragment {
    private static final String TAG = "DeveloperAddRoleInfoFragment";
    //父布局
    @BindView(R2.id.rl_dev_add_role) RelativeLayout mRlDevAddRole;
    //角色名称
    @BindView(R2.id.edit_add_role_name) PowerfulEditText mEditAddRoleName;
    //角色描述
    @BindView(R2.id.edit_add_role_info) PowerfulEditText mEditAddRoleInfo;
    //确定添加
    @BindView(R2.id.btn_add_role) ButtonView mBtnAddRole;

    /**
     * @return 单例对象
     */
    public static DeveloperAddRoleInfoFragment newInstance() {
        return new DeveloperAddRoleInfoFragment();
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_add_role_info;
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
    @OnClick({R2.id.btn_add_role})
    public void onAddRoleInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_role://确定添加
                //超管输入的角色名称、角色描述
                String strEditAddRoleName = mEditAddRoleName.getText().toString().trim();
                String strAddRoleInfo = mEditAddRoleInfo.getText().toString().trim();
                doAddRoleInfo(strEditAddRoleName, strAddRoleInfo);
                break;
        }
    }

    /**
     * 开始添加角色信息
     *
     * @param strEditAddRoleName 角色名称
     * @param strAddRoleInfo     角色描述
     */
    private void doAddRoleInfo(String strEditAddRoleName, String strAddRoleInfo) {
        //判空处理
        if (TextUtils.isEmpty(strEditAddRoleName)) {
            mEditAddRoleName.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevAddRole, "请填入角色名称", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (TextUtils.isEmpty(strAddRoleInfo)) {
            mEditAddRoleInfo.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevAddRole, "请填入角色描述", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行封禁账户操作
        OkGo.<String>post(Constant.ADMIN_ADD_ONCE_ROLE_INFO)
                .tag("超管添加用户角色")
                .params("roleName", strEditAddRoleName)
                .params("roleDescription", strAddRoleInfo)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在添加...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == OkGoResponseBean.getCode() && "未提供Token".equals(OkGoResponseBean.getData()) && "验证失败，禁止访问".equals(OkGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevAddRole, "未登录：" + OkGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(角色存在)
                        if (200 == OkGoResponseBean.getCode() && "角色添加失败，此角色已被注册".equals(OkGoResponseBean.getMsg())) {
                            mEditAddRoleName.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevAddRole,"角色添加失败，此角色已被注册：" + OkGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(未添加)
                        if (200 == OkGoResponseBean.getCode() && "角色添加失败".equals(OkGoResponseBean.getMsg())) {
                            mEditAddRoleName.startShakeAnimation();//抖动输入框
                            mEditAddRoleInfo.startShakeAnimation();//抖动输入框
                            Snackbar snackbar = Snackbar.make(mRlDevAddRole,"角色添加失败：" + OkGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(已添加)
                        if (200 == OkGoResponseBean.getCode() && "角色添加成功".equals(OkGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mRlDevAddRole, "角色添加成功：" + OkGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevAddRole, "请求错误，服务器连接失败！");
                    }
                });
    }
}
