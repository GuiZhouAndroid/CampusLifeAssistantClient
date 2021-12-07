package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.rmondjone.locktableview.DisplayUtil;
import com.rmondjone.locktableview.LockTableView;
import com.rmondjone.xrecyclerview.ProgressStyle;
import com.rmondjone.xrecyclerview.XRecyclerView;
import com.xuexiang.xui.widget.button.ButtonView;
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
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllPermissionInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;

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
    /* XUI按钮搜索权限 */
    @BindView(R2.id.btn_search_permission) ButtonView mBtnSearchPermission;
    /* XUI按钮添加权限色 */
    @BindView(R2.id.btn_add_permission) ButtonView mBtnAddPermission;
    /* 填充表格视图 */
    @BindView(R2.id.ll_permission_info_table_content_view) LinearLayout mLlPermissionInfoTableContentView;
    //跑马灯滚动显示权限总条数
    @BindView(R2.id.mtv_count_permission_number) MarqueeTextView mMtvCountPermissionNumber;

    //顶部标题数组
    private String[] topTitleArrays;
//    /* RecyclerView列表 */
//    @BindView(R2.id.recyclerView_all_permission_info) RecyclerView mRecyclerViewAllPermissionInfo;
//    /* 权限信息列表适配器 */
//    private BasePermissionInfoAdapter permissionInfoAdapter;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectAllPermissionInfoFragment newInstance() {
        return new DeveloperSelectAllPermissionInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_select_all_permission_info;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        //准备表的顶部标题数据
        topTitleArrays = new String[]{"权限ID", "权限名称", "权限描述", "创建时间", "修改时间"};
    }

    @Override
    protected void initView(View rootView) {
//        //初始化RecyclerView列表控件
//        initSelectAllPermissionInfoRecyclerView();
        initDisplayOpinion();
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

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getContext(), dm.heightPixels);
    }

    /**
     * @param view 视图View
     */
    @OnClick({R2.id.btn_search_permission, R2.id.btn_add_permission})
    public void onSelectAllPermissionInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_permission://搜索权限
                startSearchPermissionInfo();
                break;
            case R.id.btn_add_permission://添加权限
                startAddPermissionInfo();
                break;
        }
    }

//    /**
//     * 初始化RecyclerView列表控件
//     */
//    private void initSelectAllPermissionInfoRecyclerView() {
//        // 1.创建布局管理实例对象
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        // 2.设置RecyclerView布局方式为垂直方向
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        // 3.RecyclerView绑定携带垂直方向参数的布局管理实例对象
//        mRecyclerViewAllPermissionInfo.setLayoutManager(layoutManager);
//    }

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
                        starSetTabData(response);//Json字符串Gson解析使用，绘制表格

                        // 5.创建权限适配器实例对象，参数一：适配显示样式的item布局文件id，参数二：循环遍历准备好的携带权限对象集合数据
//                            permissionInfoAdapter = new BasePermissionInfoAdapter(R.layout.developer_fragment_select_all_permission_info_recycler_view_item, permissionInfoBeanList);
//                            mRecyclerViewAllPermissionInfo.setAdapter(permissionInfoAdapter);
//                            // 7.设置权限拥有数
//                            mTvAllPermissionInfoShow.setText("校园帮APP已有：" + permissionInfoBeanList.size() + "条权限信息");
                        // 8.执行子View单击事件业务逻辑--->更新权限信息

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                    }
                });

    }

    /**
     * 开始设置表数据
     *
     * @param response okGo响应返回的Json字符串
     */
    private void starSetTabData(Response<String> response) {
        OkGoAllPermissionInfoBean okGoAllPermissionInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllPermissionInfoBean.class);
        //查询成功，取出getData，遍历集合并创建权限实体添加适配
        if (200 == okGoAllPermissionInfoBean.getCode() && okGoAllPermissionInfoBean.getData().size() > 0 && "success".equals(okGoAllPermissionInfoBean.getMsg())) {
            //1.创建集合，用于显示表格
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //2.顶部标题数组，绑定表第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //3.遍历权限集合，绑定表格内容
            for (OkGoAllPermissionInfoBean.Data data : okGoAllPermissionInfoBean.getData()) {
                //3.1 创建集合，装载表格内容
                ArrayList<String> rowData = new ArrayList<>();
                rowData.add(String.valueOf(data.getTpId()));
                rowData.add(data.getTpName());
                rowData.add(data.getTpDescription());
                rowData.add(data.getCreateTime());
                rowData.add(data.getUpdateTime());
                //3.2 单个权限信息遍历后，设置进表格总集合
                tableData.add(rowData);
            }
            // 4.设置文本滚动显示权限条数
            if (!tableData.isEmpty()) {
                //4.1设置滚动文本
                mMtvCountPermissionNumber.startSimpleRoll(Collections.singletonList("        校园帮APP当前已有" + (tableData.size() - 1) + "条权限信息"));
                //4.2监听文本是否匹配--->匹配相同，执行循环滚动
                mMtvCountPermissionNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                    @Override
                    public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                        //4.3滚动开始
                        if (displayMsg.toString().equals("        校园帮APP当前已有" + (tableData.size() - 1) + "条权限信息")) {
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
            //5.携带全部权限信息的表格总集合，开始适配绘制表格
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlPermissionInfoTableContentView, tableData);
            //6.表格UI设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(150) //列最大宽度
                    .setMinColumnWidth(50) //列最小宽度
                    .setColumnWidth(2, 150) //权限描述宽度指定
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
                            //下拉刷新，再次获取用户全部数据
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSelectAllPermissionInfo(context);
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
                            //获取索引对应的权限信息
                            String strPermissionId = tableData.get(position).get(0); //当前item权限ID
                            String strOldPermissionName = tableData.get(position).get(1); //当前item权限名称
                            String strOldPermissionDescription = tableData.get(position).get(2); //当前item权限描述
                            //弹出更新权限对话框
                            new XPopup.Builder(getContext())
                                    .maxHeight(800)
                                    .isDarkTheme(true)
                                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                    .asCenterList("选择操作方式", new String[]{"修改[" + strOldPermissionName + "]的权限名", "修改[" + strOldPermissionDescription + "]的描述信息"}, new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String searchMode) {
                                            switch (position) {
                                                case 0:
                                                    chooseUpdatePermissionName(strPermissionId, strOldPermissionName); //修改权限名称
                                                    break;
                                                case 1:
                                                    chooseUpdatePermissionDescription(strPermissionId, strOldPermissionDescription);//修改权限描述
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        }
                    })
                    //长按删除权限信息
                    .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                        @Override
                        public void onItemLongClick(View item, int position) {
                            String strPermissionId = tableData.get(position).get(0); //当前item权限ID
                            String strOldPermissionName = tableData.get(position).get(1); //当前item权限名称
                            new MaterialDialog.Builder(getContext())
                                    .title("是否删除[" + strOldPermissionName + "]权限")
                                    .titleGravity(GravityEnum.CENTER)
                                    .titleColor(getResources().getColor(R.color.colorAccent))
                                    .content("须知：删除此权限可能会造成部分用户无权访问相关功能，并无法正常使用校园帮APP！如已核实清楚，请忽略此提醒~")
                                    .cancelable(false)
                                    .positiveText(R.string.yes)
                                    .negativeText(R.string.no)
                                    .onPositive((dialog, which) -> startDeletePermissionInfo(strPermissionId, strOldPermissionName)) //是-->删除权限
                                    .show();
                        }
                    })
                    .setOnItemSeletor(R.color.Grey300)//设置Item被选中颜色
                    .show(); //显示表格
            mLockTableView.getTableScrollView().setPullRefreshEnabled(true);//开启下拉刷新
            mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);//开启上拉加载
            mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.BallBeat);//设置下拉刷样式风格
        }else {
            mMtvCountPermissionNumber.startSimpleRoll(Collections.singletonList("无此权限相关信息"));
            //4.2监听文本是否匹配--->匹配相同，执行循环滚动
            mMtvCountPermissionNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                @Override
                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                    //4.3滚动开始
                    if (displayMsg.toString().equals("无此权限相关信息")) {
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
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlPermissionInfoTableContentView, tableData);
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
                    .setNullableString("无此权限") //空值替换值
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
                            //下拉刷新，再次获取权限全部数据
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSelectAllPermissionInfo(context);
                                    ToastUtils.show("全部权限信息重新加载完毕");
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
     * 弹出权限信息搜索方式选择框
     */
    private void startSearchPermissionInfo() {
        new XPopup.Builder(getContext())
                .maxHeight(800)
                .isDarkTheme(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList("选择搜索信息方式", new String[]{"权限ID", "权限名称"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String searchMode) {
                        switch (position) {
                            case 0:
                                choosePermissionId(); //选择权限ID
                                break;
                            case 1:
                                choosePermissionName();//选择权限名称
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 通过权限ID查询对应信息
     */
    private void choosePermissionId() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索权限信息", null, null, "请输入权限ID",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strPermissionId) {
                                //开始网络请求，访问后端服务器，执行查询权限操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_PERMISSION_INFO_BY_PERMISSION_ID + "/" + strPermissionId)
                                        .tag("权限ID查询信息")
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 通过权限名称查询对应信息
     */
    private void choosePermissionName() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索权限信息", null, null, "请输入权限名称",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strPermissionName) {
                                //开始网络请求，访问后端服务器，执行查询用户操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_PERMISSION_INFO_BY_PERMISSION_NAME + "/" + strPermissionName)
                                        .tag("权限名称查询信息")
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 修改权限名称
     *
     * @param strPermissionId      唯一权限ID
     * @param strOldPermissionName 旧权限名称
     */
    private void chooseUpdatePermissionName(String strPermissionId, String strOldPermissionName) {
        //1.弹出输入对话框
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true)
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("修改权限名称", null, null, "请输入新的权限名称",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strNewPermissionName) {
                                //准备好更新参数，执行更新
                                OkGo.<String>post(Constant.ADMIN_UPDATE_PERMISSION_NAME_BY_PERMISSION_ID_AND_OLD_PERMISSION_NAME + "/" + strPermissionId + "/" + strOldPermissionName + "/" + strNewPermissionName)
                                        .tag("更新权限名称")
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
                                                if (200 == okGoResponseBean.getCode() && "权限名更新失败，此权限名已存在".equals(okGoResponseBean.getMsg())) {
                                                    ToastUtils.show("[" + strOldPermissionName + "]" + okGoResponseBean.getMsg());
                                                    return;
                                                }
                                                //更新失败
                                                if (200 == okGoResponseBean.getCode() && "error".equals(okGoResponseBean.getMsg())) {
                                                    ToastUtils.show(okGoResponseBean.getData());
                                                    return;
                                                }
                                                //更新成功
                                                if (200 == okGoResponseBean.getCode() && "success".equals(okGoResponseBean.getMsg())) {
                                                    //再次调用查询全部权限接口，实现动态更新显示
                                                    startSelectAllPermissionInfo(context);
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 修改权限描述
     *
     * @param strPermissionId             唯一权限ID
     * @param strOldPermissionDescription 旧权限描述
     */
    private void chooseUpdatePermissionDescription(String strPermissionId, String strOldPermissionDescription) {
        //1.弹出输入对话框
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true)
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("修改权限描述", null, null, "请输入新的权限描述信息",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strNewPermissionDescription) {
                                //准备好更新参数，执行更新
                                OkGo.<String>post(Constant.ADMIN_UPDATE_PERMISSION_DESCRIPTION_BY_PERMISSION_ID_AND_OLD_PERMISSION_DESCRIPTION + "/" + strPermissionId + "/" + strOldPermissionDescription + "/" + strNewPermissionDescription)
                                        .tag("更新权限描述")
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
                                                    //再次调用查询全部权限接口，实现动态更新显示
                                                    startSelectAllPermissionInfo(context);
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                                            }
                                        });

                            }
                        })
                .show();
    }

    /**
     * 添加一条权限信息
     */
    private void startAddPermissionInfo() {
        new MaterialDialog.Builder(getContext())
                .customView(R.layout.fragment_developer_select_all_permission_info_add_permission_info_dialog_item, true)
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
                .tag("超管添加权限权限")
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


    private void startDeletePermissionInfo(String strPermissionId, String strOldPermissionName) {
        OkGo.<String>post(Constant.ADMIN_DELETE_ONCE_PERMISSION_INFO_BY_PERMISSION_ENTITY + "/" + strPermissionId + "/" + strOldPermissionName)
                .tag("删除权限信息")
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
                            //再次调用查询全部权限接口，实现动态更新显示
                            startSelectAllPermissionInfo(context);
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
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllPermissionInfo, "请求错误，服务器连接失败！");
                    }
                });
    }
}
