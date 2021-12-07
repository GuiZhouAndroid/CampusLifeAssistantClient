package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
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
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

/**
 * created by on 2021/11/27
 * 描述：开发者通过用户名查询该用户的对应的权限
 *
 * @author ZSAndroid
 * @create 2021-11-27-19:50
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSelectUserPermissionByUserNameFragment extends BaseFragment {

    private static final String TAG = "DeveloperBannedAccountRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_select_user_permission_username) RelativeLayout mRlDevSelectUserPermissionUserName;
    //查询用户名
    @BindView(R2.id.edit_dev_select_user_permission_username) PowerfulEditText mEditDevSelectUserPermissionUserName;
    //开始查询
    @BindView(R2.id.btn_dev_select_user_permission_username) ButtonView mBtnDevSelectUserPermissionUserName;
    //显示角色集合
    @BindView(R2.id.tv_select_user_permission_username_list_show) TextView mTvSelectUserPermissionListShow;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectUserPermissionByUserNameFragment newInstance() {
        return new DeveloperSelectUserPermissionByUserNameFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_select_permission_user_by_username;
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
    @OnClick({R2.id.btn_dev_select_user_permission_username})
    public void onSelectUserPermissionByUserNameViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dev_select_user_permission_username://开始查询
                //超管输入的用户名
                String strPermissionUserName = mEditDevSelectUserPermissionUserName.getText().toString().trim();
                doSelectUserPermissionByUserName(strPermissionUserName);
                break;
        }
    }

    /**
     * 开始查询该用户的对应的权限
     *
     * @param strPermissionUserName 用户名
     */
    private void doSelectUserPermissionByUserName(String strPermissionUserName) {
        //判空处理
        if (TextUtils.isEmpty(strPermissionUserName)) {
            mEditDevSelectUserPermissionUserName.startShakeAnimation();//抖动输入框
            ToastUtils.show("请填入用户名");
            return;
        }
        //开始网络请求，访问后端服务器，执行查询该用户的对应的权限
        OkGo.<String>post(Constant.ADMIN_SELECT_USER_THE_PERMISSION_INFO_BY_USERNAME + "/" + strPermissionUserName)
                .tag("用户持有的权限")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoRoleOrPermissionListBean okGoRoleOrPermissionListBean = GsonUtil.gsonToBean(response.body(), OkGoRoleOrPermissionListBean.class);
                        //成功(真实姓名不存在)
                        if (200 == okGoRoleOrPermissionListBean.getCode() && "success".equals(okGoRoleOrPermissionListBean.getMsg())) {
                            List<String> permissionList = okGoRoleOrPermissionListBean.getData();
                            if (permissionList.size() > 0) {
                                StringBuilder stringBuffer = new StringBuilder();
                                for (String onlyPermission : permissionList) {
                                    stringBuffer.append(onlyPermission + "、");
                                }
                                mTvSelectUserPermissionListShow.setText("【" + strPermissionUserName + "】" + "持有" + permissionList.size() + "条权限信息" + "\n" + stringBuffer.substring(0, stringBuffer.length() - 1));
                            } else {
                                mTvSelectUserPermissionListShow.setText("【" + strPermissionUserName + "】" + "无权限信息");
                            }
                            ToastUtils.show("查询成功");
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mRlDevSelectUserPermissionUserName, "请求错误，服务器连接失败！");
                    }
                });
    }
}
