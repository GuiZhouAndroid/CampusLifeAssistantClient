package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
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
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListAdapter;
import com.xuexiang.xui.widget.dialog.materialdialog.simplelist.MaterialSimpleListItem;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BasePermissionInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.PermissionInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllPermissionInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/27
 * 描述：超管查询权限全部信息
 *
 * @author ZSAndroid
 * @create 2021-11-27-13:59
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperSelectAllPermissionInfoFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectAllPermissionInfoFragment";

    /* 父布局 */
    @BindView(R2.id.ll_dev_select_all_permission_info) LinearLayout mLlDevSelectAllPermissionInfo;
    /* XUI按钮添加权限 */
    @BindView(R2.id.btn_add_permission) ButtonView mBtnAddPermission;
    /* 权限拥有数 */
    @BindView(R2.id.tv_all_permission_info_show) TextView mTvAllPermissionInfoShow;
    /* RecyclerView列表 */
    @BindView(R2.id.recyclerView_all_permission_info) RecyclerView mRecyclerViewAllPermissionInfo;

    /* 权限信息列表适配器 */
    private BasePermissionInfoAdapter permissionInfoAdapter;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectAllPermissionInfoFragment newInstance() {
        return new DeveloperSelectAllPermissionInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_all_permission_info;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        //初始化RecyclerView列表控件
        initSelectAllPermissionInfoRecyclerView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {
        //开始查询全部权限信息
        startSelectAllPermissionInfo(context);
    }

    /**
     * @param view 视图View
     */
    @OnClick({R2.id.btn_add_permission})
    public void onSelectAllPermissionInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_permission://确定添加
                startAddPermissionInfo();
                break;
        }
    }

    /**
     * 初始化RecyclerView列表控件
     */
    private void initSelectAllPermissionInfoRecyclerView() {
        // 1.创建布局管理实例对象
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 2.设置RecyclerView布局方式为垂直方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 3.RecyclerView绑定携带垂直方向参数的布局管理实例对象
        mRecyclerViewAllPermissionInfo.setLayoutManager(layoutManager);
    }

    /**
     * OkGo网络请求开始调用API接口开始查询全部权限信息
     *
     * @param context 上下文
     */
    private void startSelectAllPermissionInfo(Context context) {
        //开始网络请求，访问后端服务器，执行查询权限操作
        OkGo.<String>post(Constant.ADMIN_SELECT_ALL_PERMISSION_INFO)
                .tag("查询全部权限信息")
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoAllPermissionInfoBean okGoAllPermissionInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllPermissionInfoBean.class);
                        //查询成功，取出getData，遍历集合并创建权限实体添加适配
                        if (200 == okGoAllPermissionInfoBean.getCode() && "success".equals(okGoAllPermissionInfoBean.getMsg())) {
                            // 1.提示信息
                            ToastUtils.show("全部权限信息获取成功");
                            // 2.取出权限对象集合对象数据
                            List<OkGoAllPermissionInfoBean.Data> okGoPermissionInfoBeanDataList = okGoAllPermissionInfoBean.getData();
                            // 3.创建权限实体集合，为适配列表准备数据
                            List<PermissionInfoBean> permissionInfoBeanList = new ArrayList<>();
                            // 4.遍历getData集合，分离集合获取单个权限对象数据，同时遍历添加到适配列表权限实体
                            for (OkGoAllPermissionInfoBean.Data data : okGoPermissionInfoBeanDataList) {
                                // 4.1 循环遍历创建对象，动态添加权限数据
                                PermissionInfoBean permissionInfoBean = new PermissionInfoBean(data.getTpId(), data.getTpName(), data.getTpDescription(), data.getCreateTime(), data.getUpdateTime());
                                // 4.2 携带权限对象数据，每次循环遍历都依次加载到RecyclerView列表适配需要集合中
                                permissionInfoBeanList.add(permissionInfoBean);
                            }
                            // 5.创建权限适配器实例对象，参数一：适配显示样式的item布局文件id，参数二：循环遍历准备好的携带权限对象集合数据
                            permissionInfoAdapter = new BasePermissionInfoAdapter(R.layout.developer_fragment_select_all_permission_info_recycler_view_item, permissionInfoBeanList);
                            mRecyclerViewAllPermissionInfo.setAdapter(permissionInfoAdapter);
                            // 7.设置权限拥有数
                            mTvAllPermissionInfoShow.setText("校园帮APP已有：" + permissionInfoBeanList.size() + "条权限信息");
                            // 8.执行子View单击事件业务逻辑--->更新权限信息
                            permissionInfoAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                                    // 8.1 通过位置索引，获取对应适配的权限实体对象数据
                                    PermissionInfoBean permissionInfoBean = (PermissionInfoBean) adapter.getData().get(position);
                                    // 8.2 更新权限名称
                                    if (view.getId() == R.id.select_all_permission_name) {
                                        new MaterialDialog.Builder(getContext())
                                                .customView(R.layout.developer_fragment_select_all_permission_info_update_permission_info_dialog_item, true)
                                                .titleGravity(GravityEnum.CENTER)
                                                .title("更新" + permissionInfoBean.getTpName())
                                                .titleColor(getResources().getColor(R.color.colorAccent))
                                                .positiveText("更新")
                                                .positiveColor(getResources().getColor(R.color.colorAccent))
                                                .negativeText("取消")
                                                .cancelable(false)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        //获取自定义布局中的控件id
                                                        MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check_update_permission_info);
                                                        //新角色名称
                                                        String newPermissionName = materialEditText.getText().toString().trim();
                                                        OkGo.<String>post(Constant.ADMIN_UPDATE_PERMISSION_NAME_BY_PERMISSION_ID_AND_OLD_PERMISSION_NAME + "/" + permissionInfoBean.getTpId() + "/" + permissionInfoBean.getTpName() + "/" + newPermissionName)
                                                                .tag("更新权限名称")
                                                                .execute(new StringCallback() {
                                                                    @Override
                                                                    public void onStart(Request<String, ? extends Request> request) {
                                                                        super.onStart(request);
                                                                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在更新...");
                                                                    }

                                                                    @SuppressLint("NotifyDataSetChanged")
                                                                    @Override
                                                                    public void onSuccess(Response<String> response) {
                                                                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                                        //失败(超管未登录)
                                                                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show("未登录：" + okGoResponseBean.getMsg());
                                                                            return;
                                                                        }
                                                                        if (200 == okGoResponseBean.getCode() && "权限名更新失败，此权限名已存在".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show("【" + permissionInfoBean.getTpName() + "】" + okGoResponseBean.getMsg());
                                                                            return;
                                                                        }
                                                                        //更新失败
                                                                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show(okGoResponseBean.getData());
                                                                            return;
                                                                        }
                                                                        //更新成功
                                                                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                                            //再次调用查询全部角色接口，实现动态更新显示
                                                                            startSelectAllPermissionInfo(context);
                                                                            ToastUtils.show(okGoResponseBean.getData());
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFinish() {
                                                                        super.onFinish();
                                                                        XPopupUtils.getInstance().setSmartDisDialog();
                                                                    }
                                                                });
                                                    }
                                                })
                                                .show();
                                    }
                                    // 8.2 更新权限描述
                                    if (view.getId() == R.id.select_all_permission_info) {
                                        new MaterialDialog.Builder(getContext())
                                                .customView(R.layout.developer_fragment_select_all_permission_info_update_permission_info_dialog_item, true)
                                                .titleGravity(GravityEnum.CENTER)
                                                .title("更新" + permissionInfoBean.getTpDescription())
                                                .titleColor(getResources().getColor(R.color.colorAccent))
                                                .positiveText("更新")
                                                .positiveColor(getResources().getColor(R.color.colorAccent))
                                                .negativeText("取消")
                                                .cancelable(false)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        //获取自定义布局中的控件id
                                                        MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check_update_permission_info);
                                                        //新权限描述
                                                        String newPermissionDescription = materialEditText.getText().toString().trim();
                                                        OkGo.<String>post(Constant.ADMIN_UPDATE_PERMISSION_DESCRIPTION_BY_PERMISSION_ID_AND_OLD_PERMISSION_DESCRIPTION + "/" + permissionInfoBean.getTpId() + "/" + permissionInfoBean.getTpDescription() + "/" + newPermissionDescription)
                                                                .tag("更新权限描述")
                                                                .execute(new StringCallback() {
                                                                    @Override
                                                                    public void onStart(Request<String, ? extends Request> request) {
                                                                        super.onStart(request);
                                                                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在更新...");
                                                                    }

                                                                    @Override
                                                                    public void onSuccess(Response<String> response) {
                                                                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                                        //失败(超管未登录)
                                                                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show("未登录：" + okGoResponseBean.getMsg());
                                                                            return;
                                                                        }
                                                                        //更新失败
                                                                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show(okGoResponseBean.getData());
                                                                            return;
                                                                        }
                                                                        //更新成功
                                                                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                                            //再次调用查询全部角色接口，实现动态更新显示
                                                                            startSelectAllPermissionInfo(context);
                                                                            ToastUtils.show(okGoResponseBean.getData());
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
                                                                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                                                                    }
                                                                });
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });

                            // 9.为item设置单击监听事件--->删除一条权限信息
                            permissionInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                                    PermissionInfoBean permissionInfoBean = (PermissionInfoBean) adapter.getData().get(position);
                                    List<MaterialSimpleListItem> list = new ArrayList<>();
                                    list.add(new MaterialSimpleListItem.Builder(context)
                                            .content("删除" + permissionInfoBean.getTpName() + "权限信息")
                                            .icon(R.drawable.logo)
                                            .iconPaddingDp(8)
                                            .build());
                                    final MaterialSimpleListAdapter simpleListAdapter = new MaterialSimpleListAdapter(list)
                                            .setOnItemClickListener(new MaterialSimpleListAdapter.OnItemClickListener() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                                                    //logo列表item内容匹配成功true时执行删除业务
                                                    if (item.getContent().toString().equals("删除" + permissionInfoBean.getTpName() + "权限信息")) {
                                                        OkGo.<String>post(Constant.ADMIN_DELETE_ONCE_PERMISSION_INFO_BY_PERMISSION_ENTITY + "/" + permissionInfoBean.getTpId() + "/" + permissionInfoBean.getTpName())
                                                                .tag("删除权限信息")
                                                                .execute(new StringCallback() {
                                                                    @SuppressLint("NotifyDataSetChanged")
                                                                    @Override
                                                                    public void onSuccess(Response<String> response) {
                                                                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                                        //失败(超管未登录)
                                                                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show("未登录：" + okGoResponseBean.getMsg());
                                                                            return;
                                                                        }
                                                                        //删除失败
                                                                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                                            ToastUtils.show(okGoResponseBean.getData());
                                                                            return;
                                                                        }
                                                                        //删除成功
                                                                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                                            //再次调用查询全部权限接口，实现动态更新显示
                                                                            startSelectAllPermissionInfo(context);
                                                                            ToastUtils.show(okGoResponseBean.getData());
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(Response<String> response) {
                                                                        super.onError(response);
                                                                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                    new MaterialDialog.Builder(context).adapter(simpleListAdapter, null).show();

                                }
                            });
                        }
                    }


                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                    }
                });

    }

    /**
     * 添加一条权限信息
     */
    private void startAddPermissionInfo() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.developer_fragment_select_all_permission_info_add_permission_info_dialog_item, true)
                .titleGravity(GravityEnum.CENTER)
                .title("新增权限")
                .titleColor(getResources().getColor(R.color.colorAccent))
                .positiveText("添加")
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .negativeText("取消")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //点击添加按钮同时获取dialog中的控件
                        PowerfulEditText mEditAddPermissionName = dialog.findViewById(R.id.edit_add_permission_name);
                        PowerfulEditText mEditAddPermissionInfo = dialog.findViewById(R.id.edit_add_permission_info);
                        //点击添加按钮同时获取dialog中的控件输入的文本内容
                        String strEditAddPermissionName = mEditAddPermissionName.getText().toString().trim();
                        String strAddPermissionInfo = mEditAddPermissionInfo.getText().toString().trim();
                        //权限名称 + 权限描述 文本内容传递doAddPermissionInfo()并开始添加
                        doAddPermissionInfo(strEditAddPermissionName, strAddPermissionInfo);
                    }
                })
                .show();
    }

    /**
     * 开始添加权限信息
     *
     * @param strEditAddPermissionName 权限名称
     * @param strAddPermissionInfo     权限描述
     */
    private void doAddPermissionInfo(String strEditAddPermissionName, String strAddPermissionInfo) {
        //判空处理
        if (TextUtils.isEmpty(strEditAddPermissionName)) {
            Snackbar snackbar = Snackbar.make(mLlDevSelectAllPermissionInfo, "请填入权限名称", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (TextUtils.isEmpty(strAddPermissionInfo)) {
            Snackbar snackbar = Snackbar.make(mLlDevSelectAllPermissionInfo, "请填入权限描述", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        //开始网络请求，访问后端服务器，执行封禁账户操作
        OkGo.<String>post(Constant.ADMIN_ADD_ONCE_PERMISSION_INFO + "/" + strEditAddPermissionName + "/" + strAddPermissionInfo)
                .tag("超管添加用户角色")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show("未登录：" + okGoResponseBean.getMsg());
                            return;
                        }
                        //成功(权限存在)
                        if (200 == okGoResponseBean.getCode() && "权限添加失败，此权限已被注册".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show("添加失败，此权限已被注册：" + okGoResponseBean.getData());
                            return;
                        }
                        //成功(未添加)
                        if (200 == okGoResponseBean.getCode() && "权限添加失败".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show("添加失败：" + okGoResponseBean.getData());
                            return;
                        }
                        //成功(已添加)
                        if (200 == okGoResponseBean.getCode() && "权限添加成功".equals(okGoResponseBean.getMsg())) {
                            //再次调用查询全部权限接口，实现动态更新显示
                            startSelectAllPermissionInfo(context);
                            ToastUtils.show("权限添加成功：" + okGoResponseBean.getData());
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                    }
                });
    }
}
