package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.MarqueeTextView;
import com.xuexiang.xui.widget.textview.marqueen.DisplayEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllRoleInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
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
    /* XUI按钮搜索角色 */
    @BindView(R2.id.btn_search_role) ButtonView mBtnSearchRole;
    /* XUI按钮添加角色 */
    @BindView(R2.id.btn_add_role) ButtonView mBtnAddRole;
    /* 填充表格视图 */
    @BindView(R2.id.ll_role_info_table_content_view) LinearLayout mLlRoleInfoTableContentView;
    //跑马灯滚动显示角色总条数
    @BindView(R2.id.mtv_count_role_number) MarqueeTextView mMtvCountRoleNumber;

    //顶部标题数组
    private String[] topTitleArrays;

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
        //准备表的顶部标题数据
        topTitleArrays = new String[]{"角色ID", "角色名称", "角色描述", "创建时间", "修改时间"};
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
        //开始查询全部角色信息
        startSelectAllRoleInfo(context);
    }

    /**
     * @param view 视图View
     */
    @OnClick({R2.id.btn_add_role, R2.id.btn_search_role})
    public void onSelectAllRoleInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_role://搜索角色
                startSearchRoleInfo();
                break;
            case R.id.btn_add_role://添加角色
                startAddRoleInfo();
                break;
        }
    }

    /**
     * OkGo网络请求开始调用API接口开始查询全部角色信息
     *
     * @param context 上下文
     */
    private void startSelectAllRoleInfo(Context context) {
        //开始网络请求，访问后端服务器，执行查询角色操作
        OkGo.<String>post(Constant.ADMIN_SELECT_ALL_ROLE_INFO)
                .tag("查询全部角色信息")
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n") // I18代表国际化,带有占位符的资源字符串
                    @Override
                    public void onSuccess(Response<String> response) {
                        starSetTabData(response);//Json字符串Gson解析使用，绘制表格
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
        OkGo.<String>post(Constant.ADMIN_ADD_ONCE_ROLE_INFO + "/" + strEditAddRoleName + "/" + strAddRoleInfo)
                .tag("超管添加角色信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在添加...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        //失败(超管未登录)
                        if (401 == okGoResponseBean.getCode() && "未提供Token".equals(okGoResponseBean.getData()) && "验证失败，禁止访问".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show("未登录：" + okGoResponseBean.getMsg());
                            return;
                        }
                        //成功(角色存在)
                        if (200 == okGoResponseBean.getCode() && "角色添加失败，此角色已被注册".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show("添加失败，此角色已被注册：" + okGoResponseBean.getData());
                            return;
                        }
                        //成功(未添加)
                        if (200 == okGoResponseBean.getCode() && "角色添加失败".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show("添加失败：" + okGoResponseBean.getData());
                            return;
                        }
                        //成功(已添加)
                        if (200 == okGoResponseBean.getCode() && "角色添加成功".equals(okGoResponseBean.getMsg())) {
                            //再次调用查询全部角色接口，实现动态更新显示
                            startSelectAllRoleInfo(context);
                            ToastUtils.show("角色添加成功：" + okGoResponseBean.getData());
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 开始设置表数据
     *
     * @param response okGo响应返回的Json字符串
     */
    private void starSetTabData(Response<String> response) {
        OkGoAllRoleInfoBean okGoAllRoleInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllRoleInfoBean.class);
        //查询成功，取出getData，遍历集合并创建角色实体添加适配
        if (200 == okGoAllRoleInfoBean.getCode() && okGoAllRoleInfoBean.getData().size() > 0 && "success".equals(okGoAllRoleInfoBean.getMsg())) {
            //1.创建集合，用于显示表格
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //2.顶部标题数组，绑定表第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //3.遍历角色集合，绑定表格内容
            for (OkGoAllRoleInfoBean.Data data : okGoAllRoleInfoBean.getData()) {
                //3.1 创建集合，装载表格内容
                ArrayList<String> rowData = new ArrayList<>();
                rowData.add(String.valueOf(data.getTrId()));
                rowData.add(data.getTrName());
                rowData.add(data.getTrDescription());
                rowData.add(data.getCreateTime());
                rowData.add(data.getUpdateTime());
                //3.2 单个角色信息遍历后，设置进表格总集合
                tableData.add(rowData);
            }
            // 4.设置文本滚动显示角色条数
            if (!tableData.isEmpty()) {
                //4.1设置滚动文本
                mMtvCountRoleNumber.startSimpleRoll(Collections.singletonList("        校园帮APP当前已有" + (tableData.size() - 1) + "条角色信息"));
                //4.2监听文本是否匹配--->匹配相同，执行循环滚动
                mMtvCountRoleNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                    @Override
                    public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                        //4.3滚动开始
                        if (displayMsg.toString().equals("        校园帮APP当前已有" + (tableData.size() - 1) + "条角色信息")) {
                            return displayMsg;//匹配相同，继续滚动
                        }
                        return null;
                    }

                    @Override
                    public List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas) {
                        //4.4滚动结束
                        return displayDatas;
                    }
                });
            }
            //5.携带全部角色信息的表格总集合，开始适配绘制表格
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlRoleInfoTableContentView, tableData);
            //6.表格UI设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(150) //列最大宽度
                    .setMinColumnWidth(50) //列最小宽度
                    .setColumnWidth(2, 120) //角色描述宽度指定
                    .setMinRowHeight(20)//行最小高度
                    .setMaxRowHeight(20)//行最大高度
                    .setTextViewSize(12) //单元格字体大小
                    .setFristRowBackGroudColor(R.color.xui_btn_blue_normal_color)//表头背景色
                    .setTableHeadTextColor(R.color.White)//表头字体颜色
                    .setTableContentTextColor(R.color.colorAccent)//单元格字体颜色
                    .setCellPadding(15)//设置单元格内边距(dp)
                    .setNullableString("暂无") //空值替换值
                    .setTableViewListener(new LockTableView.OnTableViewListener() {
                        @Override
                        public void onTableViewScrollChange(int x, int y) {
                            //手指在表格中滑动的X轴、Y轴的实时数据值
                        }
                    })
                    //设置横向滚动回调监听
                    .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                        @Override
                        public void onLeft(HorizontalScrollView view) {
                            ToastUtils.show("哦豁~已经滑到最左边了");
                        }

                        @Override
                        public void onRight(HorizontalScrollView view) {
                            ToastUtils.show("哦豁~已经滑到最右边了");
                        }
                    })
                    //设置竖向滚动边界监听
                    .setOnLoadingListener(new LockTableView.OnLoadingListener() {
                        @Override
                        public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                            //下拉刷新，再次获取角色全部数据
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSelectAllRoleInfo(context);
                                    ToastUtils.show("信息重新加载完成");
                                }
                            }, 1000);
                        }

                        @Override
                        public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                            //上拉加载刷新，分页功能待开发
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mXRecyclerView.setNoMore(true);
                                }
                            }, 1000);
                        }
                    })
                    .setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
                        @Override
                        public void onItemClick(View item, int position) {
                            //获取索引对应的角色信息
                            String strRoleId = tableData.get(position).get(0); //当前item角色ID
                            String strOldRoleName = tableData.get(position).get(1); //当前item角色名称
                            String strOldRoleDescription = tableData.get(position).get(2); //当前item角色描述
                            //弹出更新角色对话框
                            new XPopup.Builder(getContext())
                                    .maxHeight(800)
                                    .isDarkTheme(true)
                                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                    .asCenterList("选择操作方式", new String[]{"修改[" + strOldRoleName + "]的角色名", "修改[" + strOldRoleName + "]的描述信息"}, new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String searchMode) {
                                            switch (position) {
                                                case 0:
                                                    chooseUpdateRoleName(strRoleId, strOldRoleName); //修改角色名称
                                                    break;
                                                case 1:
                                                    chooseUpdateRoleDescription(strRoleId, strOldRoleDescription);//修改描述信息
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        }
                    })
                    //长按删除角色信息
                    .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                        @Override
                        public void onItemLongClick(View item, int position) {
                            String strRoleId = tableData.get(position).get(0); //当前item角色ID
                            String strOldRoleName = tableData.get(position).get(1); //当前item角色名称
                            new MaterialDialog.Builder(getContext())
                                    .title("是否删除[" + strOldRoleName + "]角色")
                                    .titleGravity(GravityEnum.CENTER)
                                    .titleColor(getResources().getColor(R.color.colorAccent))
                                    .content("须知：删除此角色可能会造成部分用户无权访问相关功能，并无法正常使用校园帮APP！如已核实清楚，请忽略此提醒~")
                                    .cancelable(false)
                                    .positiveText(R.string.yes)
                                    .negativeText(R.string.no)
                                    .onPositive((dialog, which) -> startDeleteRoleInfo(strRoleId, strOldRoleName)) //是-->删除角色
                                    .show();
                        }
                    })
                    .setOnItemSeletor(R.color.Grey300)//设置Item被选中颜色
                    .show(); //显示表格
            mLockTableView.getTableScrollView().setPullRefreshEnabled(true);//开启下拉刷新
            mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);//开启上拉加载
            mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.BallBeat);//设置下拉刷样式风格
        } else {
            mMtvCountRoleNumber.startSimpleRoll(Collections.singletonList("无此角色相关信息"));
            //4.2监听文本是否匹配--->匹配相同，执行循环滚动
            mMtvCountRoleNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                @Override
                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                    //4.3滚动开始
                    if (displayMsg.toString().equals("无此角色相关信息")) {
                        return displayMsg;//匹配相同，继续滚动
                    }
                    return null;
                }

                @Override
                public List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas) {
                    //4.4滚动结束
                    return displayDatas;
                }
            });
            //空集合表数据
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //设置顶部第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //空数据其它列字段
            tableData.add(new ArrayList<>());
            //表格绑定空数据
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlRoleInfoTableContentView, tableData);
            //表格UI参数设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(150) //列最大宽度
                    .setMinColumnWidth(50) //列最小宽度
                    .setColumnWidth(4, 120) //设置指定列文本宽度
                    .setColumnWidth(9, 120) //设置指定列文本宽度
                    .setMinRowHeight(20)//行最小高度
                    .setMaxRowHeight(20)//行最大高度
                    .setTextViewSize(12) //单元格字体大小
                    .setFristRowBackGroudColor(R.color.xui_btn_blue_normal_color)//表头背景色
                    .setTableHeadTextColor(R.color.White)//表头字体颜色
                    .setTableContentTextColor(R.color.colorAccent)//单元格字体颜色
                    .setCellPadding(15)//设置单元格内边距(dp)
                    .setNullableString("无此角色") //空值替换值
                    .setTableViewListener(new LockTableView.OnTableViewListener() {
                        @Override
                        public void onTableViewScrollChange(int x, int y) {
                            //手指在表格中滑动的X轴、Y轴的实时数据值
                        }
                    })//设置横向滚动回调监听
                    .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                        @Override
                        public void onLeft(HorizontalScrollView view) {
                            ToastUtils.show("哦豁~已经滑到最左边了");
                        }

                        @Override
                        public void onRight(HorizontalScrollView view) {
                            ToastUtils.show("哦豁~已经滑到最右边了");
                        }
                    })
                    //设置竖向滚动边界监听
                    .setOnLoadingListener(new LockTableView.OnLoadingListener() {
                        @Override
                        public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                            //下拉刷新，再次获取角色全部数据
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSelectAllRoleInfo(context);
                                    ToastUtils.show("全部角色信息重新加载完毕");
                                }
                            }, 1000);
                        }

                        @Override
                        public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                            //上拉加载刷新，分页功能待开发
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mXRecyclerView.setNoMore(true);
                                    ToastUtils.show("分页功能待实现");
                                }
                            }, 1000);
                        }
                    })
                    .setOnItemSeletor(R.color.Grey300)//设置Item被选中颜色
                    .show(); //显示表格
            mLockTableView.getTableScrollView().setPullRefreshEnabled(true);//开启下拉刷新
            mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);//开启上拉加载
            mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.BallBeat);//设置下拉刷样式风格
        }
    }

    /**
     * 弹出角色信息搜索方式选择框
     */
    private void startSearchRoleInfo() {
        new XPopup.Builder(getContext())
                .maxHeight(800)
                .isDarkTheme(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList("选择搜索信息方式", new String[]{"角色ID", "角色名称"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String searchMode) {
                        switch (position) {
                            case 0:
                                chooseRoleId(); //选择角色ID
                                break;
                            case 1:
                                chooseRoleName();//选择角色名称
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 通过角色ID查询对应信息
     */
    private void chooseRoleId() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索角色信息", null, null, "请输入角色ID",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strRoleId) {
                                //开始网络请求，访问后端服务器，执行查询角色操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_ROLE_INFO_BY_ROLE_ID + "/" + strRoleId)
                                        .tag("角色ID查询信息")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在搜索...");
                                            }

                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                                            }

                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                                MyXPopupUtils.getInstance().setSmartDisDialog();
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

    /**
     * 通过角色名称查询对应信息
     */
    private void chooseRoleName() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索角色信息", null, null, "请输入角色名称",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strRoleName) {
                                //开始网络请求，访问后端服务器，执行查询角色操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_ROLE_INFO_BY_ROLE_NAME + "/" + strRoleName)
                                        .tag("角色名称查询信息")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在搜索...");
                                            }

                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                                            }

                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                                MyXPopupUtils.getInstance().setSmartDisDialog();
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

    /**
     * 修改角色名称
     *
     * @param strRoleId      唯一角色ID
     * @param strOldRoleName 旧角色名称
     */
    private void chooseUpdateRoleName(String strRoleId, String strOldRoleName) {
        //1.弹出输入对话框
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true)
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("修改角色名称", null, null, "请输入新的角色名称",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strNewRoleName) {
                                //准备好更新参数，执行更新
                                OkGo.<String>post(Constant.ADMIN_UPDATE_ROLE_NAME_BY_ROLE_ID_AND_OLD_ROLE_NAME + "/" + strRoleId + "/" + strOldRoleName + "/" + strNewRoleName)
                                        .tag("更新角色名称")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在更新...");
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
                                                //更新失败
                                                if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                    ToastUtils.show(okGoResponseBean.getData());
                                                    return;
                                                }
                                                //更新成功
                                                if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                    //再次调用查询全部角色接口，实现动态更新显示
                                                    startSelectAllRoleInfo(context);
                                                    ToastUtils.show(okGoResponseBean.getData());
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 修改角色描述
     *
     * @param strRoleId             唯一角色ID
     * @param strOldRoleDescription 旧角色描述
     */
    private void chooseUpdateRoleDescription(String strRoleId, String strOldRoleDescription) {
        //1.弹出输入对话框
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true)
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("修改角色描述", null, null, "请输入新的角色描述信息",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strNewRoleDescription) {
                                //准备好更新参数，执行更新
                                OkGo.<String>post(Constant.ADMIN_UPDATE_ROLE_DESCRIPTION_BY_ROLE_ID_AND_OLD_ROLE_DESCRIPTION + "/" + strRoleId + "/" + strOldRoleDescription + "/" + strNewRoleDescription)
                                        .tag("更新角色描述")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在更新...");
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
                                                    startSelectAllRoleInfo(context);
                                                    ToastUtils.show(okGoResponseBean.getData());
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                                            }
                                        });

                            }
                        })
                .show();
    }

    /**
     * 删除角色名称
     *
     * @param strRoleId      唯一角色ID
     * @param strOldRoleName 旧角色名称
     */
    private void startDeleteRoleInfo(String strRoleId, String strOldRoleName) {
        OkGo.<String>post(Constant.ADMIN_DELETE_ONCE_ROLE_INFO_BY_ROLE_ENTITY + "/" + strRoleId + "/" + strOldRoleName)
                .tag("删除角色信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "正在删除...");
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
                        //删除失败
                        if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                            ToastUtils.show(okGoResponseBean.getData());
                            return;
                        }
                        //删除成功
                        if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                            //再次调用查询全部角色接口，实现动态更新显示
                            startSelectAllRoleInfo(context);
                            ToastUtils.show(okGoResponseBean.getData());
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllRoleInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
