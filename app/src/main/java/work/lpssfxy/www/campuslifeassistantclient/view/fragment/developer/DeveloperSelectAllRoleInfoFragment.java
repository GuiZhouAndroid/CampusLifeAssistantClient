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
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseRoleInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.RoleInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoRoleInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/24
 * 描述：超管查询角色全部信息
 *
 * @author ZSAndroid
 * @create 2021-11-24-22:13
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSelectAllRoleInfoFragment extends BaseFragment {
    private static final String TAG = "DeveloperSelectAllRoleInfoFragment";

    /* 父布局 */
    @BindView(R2.id.ll_dev_select_all_role_info) LinearLayout mLlDevSelectAllRoleInfo;
    /* XUI按钮添加角色 */
    @BindView(R2.id.btn_add_role) ButtonView mBtnAddRole;
    /* 角色拥有数 */
    @BindView(R2.id.tv_all_role_info_show) TextView mTvAllRoleInfoShow;
    /* RecyclerView列表 */
    @BindView(R2.id.recyclerView_all_role_info) RecyclerView mRecyclerViewAllRoleInfo;
    /* 角色信息列表适配器 */
    private BaseRoleInfoAdapter roleInfoAdapter;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectAllRoleInfoFragment newInstance() {
        return new DeveloperSelectAllRoleInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_all_role_info;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        //初始化RecyclerView列表控件
        initSelectAllRoleInfoRecyclerView();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {
        //开始查询全部角色信息
        startSelectAllRoleInfo(context);
    }

    /**
     * @param view 视图View
     */
    @OnClick({R2.id.btn_add_role})
    public void onSelectAllRoleInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_role://确定添加
                startAddRoleInfo();
                break;
        }
    }

    /**
     * 初始化RecyclerView列表控件
     */
    private void initSelectAllRoleInfoRecyclerView() {
        // 1.创建布局管理实例对象
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        // 2.设置RecyclerView布局方式为垂直方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 3.RecyclerView绑定携带垂直方向参数的布局管理实例对象
        mRecyclerViewAllRoleInfo.setLayoutManager(layoutManager);
    }

    /**
     * OkGo网络请求开始调用API接口开始查询全部角色信息
     *
     * @param context 上下文
     */
    private void startSelectAllRoleInfo(Context context) {
        //开始网络请求，访问后端服务器，执行封禁账户操作
        OkGo.<String>post(Constant.ADMIN_SELECT_ALL_ROLE_INFO)
                .tag("查询全部角色信息")
                // .upString("这是要上传的长文本数据！", MediaType.parse("application/xml"))
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在查询...");
                    }

                    @SuppressLint("SetTextI18n") // I18代表国际化,带有占位符的资源字符串
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoRoleInfoBean okGoRoleInfoBean = GsonUtil.gsonToBean(response.body(), OkGoRoleInfoBean.class);
                        //查询成功，取出getData，遍历集合并创建角色实体添加适配
                        if (200 == okGoRoleInfoBean.getCode() && "success".equals(okGoRoleInfoBean.getMsg())) {
                            // 1.提示信息
                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "全部角色信息获取成功", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            // 2.取出角色对象集合对象数据
                            List<OkGoRoleInfoBean.Data> okGoRoleInfoBeanDataList = okGoRoleInfoBean.getData();
                            // 3.创建角色实体集合，为适配列表准备数据
                            List<RoleInfoBean> roleInfoBeanList = new ArrayList<>();
                            // 4.遍历getData集合，分离集合获取单个角色对象数据，同时遍历添加到适配列表角色实体
                            for (OkGoRoleInfoBean.Data data : okGoRoleInfoBeanDataList) {
                                // 4.1 循环遍历创建对象，动态添加角色数据
                                RoleInfoBean roleInfoBean = new RoleInfoBean(data.getTrId(), data.getTrName(), data.getTrDescription(), data.getCreateTime(), data.getUpdateTime());
                                // 4.2 携带角色对象数据，每次循环遍历都依次加载到RecyclerView列表适配需要集合中
                                roleInfoBeanList.add(roleInfoBean);
                            }
                            // 5.创建角色适配器实例对象，参数一：适配显示样式的item布局文件id，参数二：循环遍历准备好的携带角色对象集合数据
                            roleInfoAdapter = new BaseRoleInfoAdapter(R.layout.developer_fragment_select_all_info_recycler_view_item, roleInfoBeanList);
                            // 6.为RecyclerView列表控件设置适配器，并为执行适配操作
                            mRecyclerViewAllRoleInfo.setAdapter(roleInfoAdapter);
                            // 7.设置角色拥有数
                            mTvAllRoleInfoShow.setText("校园帮APP已有：" + roleInfoBeanList.size() + "条角色信息");

                            // 8.执行子View单击事件业务逻辑--->更新角色信息
                            roleInfoAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                                    // 8.1 通过位置索引，获取对应适配的角色实体对象数据
                                    RoleInfoBean roleInfoBean = (RoleInfoBean) adapter.getData().get(position);
                                    // 8.2 更新角色名称
                                    if (view.getId() == R.id.select_all_role_name) {
                                        new MaterialDialog.Builder(getContext())
                                                .customView(R.layout.developer_fragment_select_all_role_info_update_role_info_dialog_item, true)
                                                .titleGravity(GravityEnum.CENTER)
                                                .title("更新" + roleInfoBean.getTrName())
                                                .titleColor(getResources().getColor(R.color.colorAccent))
                                                .positiveText("更新")
                                                .positiveColor(getResources().getColor(R.color.colorAccent))
                                                .negativeText("取消")
                                                .cancelable(false)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        //获取自定义布局中的控件id
                                                        MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check);
                                                        //新角色名称
                                                        String newRoleName = materialEditText.getText().toString().trim();
                                                        OkGo.<String>post(Constant.ADMIN_UPDATE_ROLE_NAME_BY_ROLE_ID_AND_OLD_ROLE_NAME + "/" + roleInfoBean.getTrId() + "/" + roleInfoBean.getTrName() + "/" + newRoleName)
                                                                .tag("更新角色名称")
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
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "未登录：" + okGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
                                                                            return;
                                                                        }
                                                                        //更新失败
                                                                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
                                                                            return;
                                                                        }
                                                                        //更新成功
                                                                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                                            //再次调用查询全部角色接口，实现动态更新显示
                                                                            startSelectAllRoleInfo(context);
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
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
                                    // 8.2 更新角色描述
                                    if (view.getId() == R.id.select_all_role_info) {
                                        new MaterialDialog.Builder(getContext())
                                                .customView(R.layout.developer_fragment_select_all_role_info_update_role_info_dialog_item, true)
                                                .titleGravity(GravityEnum.CENTER)
                                                .title("更新" + roleInfoBean.getTrDescription())
                                                .titleColor(getResources().getColor(R.color.colorAccent))
                                                .positiveText("更新")
                                                .positiveColor(getResources().getColor(R.color.colorAccent))
                                                .negativeText("取消")
                                                .cancelable(false)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        //获取自定义布局中的控件id
                                                        MaterialEditText materialEditText = dialog.findViewById(R.id.et_auto_check);
                                                        //新角色描述
                                                        String newRoleDescription = materialEditText.getText().toString().trim();

                                                        OkGo.<String>post(Constant.ADMIN_UPDATE_ROLE_DESCRIPTION_BY_ROLE_ID_AND_OLD_ROLE_DESCRIPTION + "/" + roleInfoBean.getTrId() + "/" + roleInfoBean.getTrDescription() + "/" + newRoleDescription)
                                                                .tag("更新角色描述")
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
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "未登录：" + okGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
                                                                            return;
                                                                        }
                                                                        //更新失败
                                                                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
                                                                            return;
                                                                        }
                                                                        //更新成功
                                                                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                                            //再次调用查询全部角色接口，实现动态更新显示
                                                                            startSelectAllRoleInfo(context);
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                                                                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                                                                    }
                                                                });
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });

                            // 9.为item设置单击监听事件--->删除一条角色信息
                            roleInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                                    RoleInfoBean roleInfoBean = (RoleInfoBean) adapter.getData().get(position);
                                    List<MaterialSimpleListItem> list = new ArrayList<>();
                                    list.add(new MaterialSimpleListItem.Builder(context)
                                            .content("删除" + roleInfoBean.getTrName() + "角色信息")
                                            .icon(R.drawable.logo)
                                            .iconPaddingDp(8)
                                            .build());
                                    final MaterialSimpleListAdapter simpleListAdapter = new MaterialSimpleListAdapter(list)
                                            .setOnItemClickListener(new MaterialSimpleListAdapter.OnItemClickListener() {
                                                @SuppressLint("NotifyDataSetChanged")
                                                @Override
                                                public void onMaterialListItemSelected(MaterialDialog dialog, int index, MaterialSimpleListItem item) {
                                                    //logo列表item内容匹配成功true时执行删除业务
                                                    if (item.getContent().toString().equals("删除" + roleInfoBean.getTrName() + "角色信息")) {
                                                        OkGo.<String>post(Constant.ADMIN_DELETE_ONCE_ROLE_INFO_BY_ROLE_ENTITY + "/" + roleInfoBean.getTrId() + "/" + roleInfoBean.getTrName())
                                                                .tag("删除角色信息")
                                                                .execute(new StringCallback() {
                                                                    @Override
                                                                    public void onStart(Request<String, ? extends Request> request) {
                                                                        super.onStart(request);
                                                                        XPopupUtils.getInstance().setShowDialog(getActivity(), "正在删除...");
                                                                    }

                                                                    @SuppressLint("NotifyDataSetChanged")
                                                                    @Override
                                                                    public void onSuccess(Response<String> response) {
                                                                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                                        //失败(超管未登录)
                                                                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "未登录：" + okGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
                                                                            return;
                                                                        }
                                                                        //删除失败
                                                                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                                                                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                                                                            snackbar.show();
                                                                            return;
                                                                        }
                                                                        //删除成功
                                                                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                                            //删除成功移出当前对应的item对象数据
                                                                            adapter.removeAt(position);
                                                                            //移出成功同时刷新适配器
                                                                            roleInfoAdapter.notifyDataSetChanged();
                                                                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                                                                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
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
                    public void onFinish() {
                        super.onFinish();
                        XPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 添加一条角色信息
     */
    private void startAddRoleInfo() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.developer_fragment_select_all_role_info_add_role_info_dialog_item, true)
                .titleGravity(GravityEnum.CENTER)
                .title("新增角色")
                .titleColor(getResources().getColor(R.color.colorAccent))
                .positiveText("添加")
                .positiveColor(getResources().getColor(R.color.colorAccent))
                .negativeText("取消")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //点击添加按钮同时获取dialog中的控件
                        PowerfulEditText mEditAddRoleName = dialog.findViewById(R.id.edit_add_role_name);
                        PowerfulEditText mEditAddRoleInfo = dialog.findViewById(R.id.edit_add_role_info);
                        //点击添加按钮同时获取dialog中的控件输入的文本内容
                        String strEditAddRoleName = mEditAddRoleName.getText().toString().trim();
                        String strAddRoleInfo = mEditAddRoleInfo.getText().toString().trim();
                        //角色名称 + 角色描述 文本内容传递doAddRoleInfo()并开始添加
                        doAddRoleInfo(strEditAddRoleName, strAddRoleInfo);
                    }
                })
                .show();
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
            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "请填入角色名称", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
            return;
        }
        if (TextUtils.isEmpty(strAddRoleInfo)) {
            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "请填入角色描述", Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "未登录：" + okGoResponseBean.getMsg(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(角色存在)
                        if (200 == okGoResponseBean.getCode() && "角色添加失败，此角色已被注册".equals(okGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "角色添加失败，此角色已被注册：" + okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(未添加)
                        if (200 == okGoResponseBean.getCode() && "角色添加失败".equals(okGoResponseBean.getMsg())) {
                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "角色添加失败：" + okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
                            setSnackBarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                            snackbar.show();
                            return;
                        }
                        //成功(已添加)
                        if (200 == okGoResponseBean.getCode() && "角色添加成功".equals(okGoResponseBean.getMsg())) {
                            startSelectAllRoleInfo(context);
                            Snackbar snackbar = Snackbar.make(mLlDevSelectAllRoleInfo, "角色添加成功：" + okGoResponseBean.getData(), Snackbar.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.colorAccent));
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                    }
                });
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
