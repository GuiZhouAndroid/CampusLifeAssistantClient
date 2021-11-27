package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoRoleOrPermissionListBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/25
 * 描述：开发者通过用户名查询该用户的对应的角色
 *
 * @author ZSAndroid
 * @create 2021-11-25-22:22
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSelectUserRoleByUserNameFragment extends BaseFragment {

    private static final String TAG = "DeveloperBannedAccountRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_role_user_username) RelativeLayout mRlDevRoleUserUsername;
    //查询用户名
    @BindView(R2.id.edit_dev_role_user_username) PowerfulEditText mEditDevRoleUserUsername;
    //开始查询
    @BindView(R2.id.btn_dev_role_user_username) ButtonView mBtnDevRoleUserUsername;
    //显示角色集合
    @BindView(R2.id.tv_role_user_list_show) TextView mTvRoleUserListShow;


    /**
     * @return 单例对象
     */
    public static DeveloperSelectUserRoleByUserNameFragment newInstance() {
        return new DeveloperSelectUserRoleByUserNameFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_role_user_by_username ;
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
    @OnClick({R2.id.btn_dev_role_user_username})
    public void onSelectUserRoleByUserNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dev_role_user_username://开始查询
                //超管输入的用户名
                String strRoleUserUsername = mEditDevRoleUserUsername.getText().toString().trim();
                doSelectRoleUserByUsername(strRoleUserUsername);
                break;
        }
    }

    /**
     * 开始查询该用户的对应的角色
     *
     * @param strRoleUserUsername 用户名
     */
    private void doSelectRoleUserByUsername(String strRoleUserUsername) {
        //判空处理
        if (TextUtils.isEmpty(strRoleUserUsername)) {
            mEditDevRoleUserUsername.startShakeAnimation();//抖动输入框
            Snackbar snackbar = Snackbar.make(mRlDevRoleUserUsername, "请填入用户名", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行封禁账户操作
        OkGo.<String>post(Constant.SELECT_USER_ROLE_INFO_BY_USERNAME)
                .tag("用户持有的角色")
                .params("userName", strRoleUserUsername)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoRoleOrPermissionListBean okGoRoleOrPermissionListBean = GsonUtil.gsonToBean(response.body(), OkGoRoleOrPermissionListBean.class);
                        //成功(真实姓名不存在)
                        if (200 == okGoRoleOrPermissionListBean.getCode() && "success".equals(okGoRoleOrPermissionListBean.getMsg())) {
                            List<String> roleList = okGoRoleOrPermissionListBean.getData();
                            if (roleList.size() > 0){
                                StringBuilder stringBuffer = new StringBuilder();
                                for (String onlyRole : roleList) {
                                    stringBuffer.append(onlyRole + "、");
                                }
                                mTvRoleUserListShow.setText("【"+strRoleUserUsername+"】"+"持有"+roleList.size()+"条角色信息"+"\n"+stringBuffer.substring(0,stringBuffer.length()-1));
                                Snackbar snackbar = Snackbar.make(mRlDevRoleUserUsername, "查询成功", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                snackbar.show();
                            }else {
                                mTvRoleUserListShow.setText("【"+strRoleUserUsername+"】"+"无角色信息");
                                Snackbar snackbar = Snackbar.make(mRlDevRoleUserUsername, "查询成功", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                snackbar.show();
                            }
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevRoleUserUsername, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
