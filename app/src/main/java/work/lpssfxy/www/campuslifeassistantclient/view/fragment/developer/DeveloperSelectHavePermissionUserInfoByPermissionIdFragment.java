package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseRoleUserInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.RoleUserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoRoleUserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/27
 * 描述：开发者通过权限编号查询拥有该权限全部用户的信息
 *
 * @author ZSAndroid
 * @create 2021-11-27-23:03
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperSelectHavePermissionUserInfoByPermissionIdFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectHavePermissionUserInfoByPermissionIdFragment";

    /* 父布局 */
    @BindView(R2.id.ll_dev_select_have_permission_user_info) LinearLayout mLlDevSelectHavePermissionUserInfoPermissionId;
    /* XUI按钮添加角色 */
    @BindView(R2.id.btn_select_have_permission_user_info) ButtonView mBtnSelectHavePermissionUserInfoPermissionId;
    /* 用户和角色总数 */
    @BindView(R2.id.tv_select_have_permission_user_info) TextView mTvSelectHavePermissionUserInfoPermissionIdShow;
    /* RecyclerView列表 */
    @BindView(R2.id.recyclerView_select_have_permission_user_info) RecyclerView mRecyclerViewSelectHavePermissionUserInfoPermissionId;
    /* 角色信息列表适配器 */
    private BaseRoleUserInfoAdapter roleUserInfoAdapter;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectHavePermissionUserInfoByPermissionIdFragment newInstance() {
        return new DeveloperSelectHavePermissionUserInfoByPermissionIdFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_have_permission_user_info_by_permission_id;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        //初始化RecyclerView列表控件
        initSelectHavePermissionUserInfoByPermissionIdRecyclerView();
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
    @OnClick({R2.id.btn_select_have_permission_user_info})
    public void onSelectHavePermissionUserInfoByPermissionIdViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select_have_permission_user_info://开始查询
                startSelectHavePermissionUserInfoByPermissionId();
                break;
        }
    }

    /**
     * 初始化RecyclerView列表控件
     */
    private void initSelectHavePermissionUserInfoByPermissionIdRecyclerView() {
        // 1.创建布局管理实例对象
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 2.设置RecyclerView布局方式为垂直方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 3.RecyclerView绑定携带垂直方向参数的布局管理实例对象
        mRecyclerViewSelectHavePermissionUserInfoPermissionId.setLayoutManager(layoutManager);
    }

    /**
     * 拉起对话框自定义布局
     */
    private void startSelectHavePermissionUserInfoByPermissionId() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.developer_fragment_select_have_permission_user_info_dialog_item, true)
                .titleGravity(GravityEnum.CENTER)
                .title("查询权限专属用户")
                .titleColor(getResources().getColor(R.color.colorAccent))
                .positiveText("查询")
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .negativeText("取消")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //点击添加按钮同时获取dialog中的控件
                        PowerfulEditText mEditRoleId = dialog.findViewById(R.id.edit_dev_permission_user_info_permission_id);
                        //点击添加按钮同时获取dialog中的控件输入的文本内容
                        String strPermissionId = mEditRoleId.getText().toString().trim();
                        //权限编号文本内容开始查询
                        doSelectHavePermissionUserInfoByPermissionId(strPermissionId);
                    }
                })
                .show();
    }

    /**
     * 开始查询
     *
     * @param strPermissionId 权限编号
     */
    private void doSelectHavePermissionUserInfoByPermissionId(String strPermissionId) {
        //判空处理
        if (TextUtils.isEmpty(strPermissionId)) {
            ToastUtils.show("请填入权限编号");
            return;
        }
        //开始网络请求，访问后端服务器，执行封禁账户操作
        OkGo.<String>post(Constant.ADMIN_SELECT_HAVE_PERMISSION_USER_INFO_BY_USERNAME)
                .tag("拥有该权限全部用户")
                .params("permissionId", Integer.parseInt(strPermissionId))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoRoleUserInfoBean okGoRoleUserInfoBean = GsonUtil.gsonToBean(response.body(), OkGoRoleUserInfoBean.class);
                        //成功(角色存在)
                        if (200 == okGoRoleUserInfoBean.getCode() && "success".equals(okGoRoleUserInfoBean.getMsg())) {
                            // 1.提示信息
                            ToastUtils.show("全部用户信息获取成功");
                            // 2.取出用户和角色集合数据
                            List<OkGoRoleUserInfoBean.Data> okGoRoleUserInfoBeanDataList = okGoRoleUserInfoBean.getData();
                            // 3.创建用户和角色实体集合，为适配列表准备用户和角色数据
                            List<RoleUserInfoBean> roleUserInfoBeans = new ArrayList<>();
                            // 4.遍历getData集合，分离集合获取单个用户和角色对象数据，同时遍历添加到适配列表用户和角色实体
                            for (OkGoRoleUserInfoBean.Data data : okGoRoleUserInfoBeanDataList) {
                                //4.1 循环遍历List角色对象数据
                                List<OkGoRoleUserInfoBean.Data.RoleInfo> roleInfoList = data.getRoleInfo();
                                //4.1 循环遍历取出单个角色对象
                                for (OkGoRoleUserInfoBean.Data.RoleInfo roleInfo : roleInfoList) {
                                    // 4.2 循环遍历创建对象，动态添加用户和角色数据
                                    RoleUserInfoBean roleUserInfoBean = new RoleUserInfoBean(data.getUlId(), data.getUlUsername(), data.getUlRealname(), roleInfo.getTrId(), roleInfo.getTrName());
                                    // 4.3 携带用户和角色对象数据，每次循环遍历都依次加载到RecyclerView列表适配需要集合中
                                    roleUserInfoBeans.add(roleUserInfoBean);
                                }
                            }
                            Log.i("拥有该权限全部用户", "拥有该权限全部用户: " + roleUserInfoBeans);
                            // 5.创建用户和角色适配器实例对象，参数一：适配显示样式的item布局文件id，参数二：循环遍历准备好的携带用户和角色对象集合数据
                            roleUserInfoAdapter = new BaseRoleUserInfoAdapter(R.layout.developer_fragment_select_have_role_user_info_role_id_recycler_view_item, roleUserInfoBeans);
                            // 6.为RecyclerView列表控件设置适配器，并为执行适配操作
                            mRecyclerViewSelectHavePermissionUserInfoPermissionId.setAdapter(roleUserInfoAdapter);
                            // 7.设置用户和角色拥有数
                            mTvSelectHavePermissionUserInfoPermissionIdShow.setText("权限编号【" + strPermissionId + "】+已有：" + roleUserInfoBeans.size() + "条用户信息");
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectHavePermissionUserInfoPermissionId, "请求错误，服务器连接失败！");
                    }
                });
    }
}
