package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

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
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
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
import work.lpssfxy.www.campuslifeassistantclient.base.custompopup.UserInfoFullPopup;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllApplyShopInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.ApplyImgLookFragment;

/**
 * created by on 2021/12/11
 * 描述：超管查询商家认证全部信息
 *
 * @author ZSAndroid
 * @create 2021-12-11-23:35
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperSelectAllCerShopInfoFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectAllCerShopInfoFragment";

    /* 父布局 */
    @BindView(R2.id.fl_dev_select_all_cer_shop_info) FrameLayout mFlDevSelectAllCerShopInfo;
    /* XUI按钮搜索 */
    @BindView(R2.id.btn_search_cer_shop_info) ButtonView mBtnSearchCerShopInfo;
    /* 填充表格视图 */
    @BindView(R2.id.ll_cer_shop_table_content_view) LinearLayout mLlCerShopInfoTableContentView;
    //跑马灯滚动显示认证总条数
    @BindView(R2.id.mtv_count_cer_shop_number) MarqueeTextView mMtvCountCerShopNumber;
    //顶部标题数组
    private String[] topTitleArrays;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectAllCerShopInfoFragment newInstance() {
        return new DeveloperSelectAllCerShopInfoFragment();
    }


    @Override
    protected int bindLayout() {
        return R.layout.fragment_developer_select_all_cer_shop_info;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        //准备表的顶部标题数据
        topTitleArrays = new String[]{"用户ID", "申请ID", "申请类型", "审核状态", "餐饮许可证", "申请时间", "审核时间"};
    }

    @Override
    protected void initView(View rootView) {
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
        //开始查询全部商家认证信息
        startSelectAllCerShopInfo(context);
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
    @OnClick({R2.id.btn_search_cer_shop_info})
    public void onSelectAllCerShopInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_cer_shop_info://确定搜索
                startSearchCerShopInfo();
                break;
        }
    }

    /**
     * 开始查询全部商家认证信息
     *
     * @param context 上下文
     */
    private void startSelectAllCerShopInfo(Context context) {
        //开始网络请求，访问后端服务器
        OkGo.<String>post(Constant.ADMIN_SELECT_ALL_APPLY_SHOP_INFO)
                .tag("查询全部申请商家信息")
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n") // I18代表国际化,带有占位符的资源字符串
                    @Override
                    public void onSuccess(Response<String> response) {
                        starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mFlDevSelectAllCerShopInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 开始设置表数据
     *
     * @param response okGo响应返回的Json字符串
     */
    private void starSetTabData(Response<String> response) {
        OkGoAllApplyShopInfoBean okGoAllApplyShopInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllApplyShopInfoBean.class);
        if (200 == okGoAllApplyShopInfoBean.getCode() && okGoAllApplyShopInfoBean.getData().size() > 0 && "success".equals(okGoAllApplyShopInfoBean.getMsg())) {
            //1.创建集合，用于显示表格
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //2.顶部标题数组，绑定表第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //3.遍历用户集合，绑定表格内容
            for (OkGoAllApplyShopInfoBean.Data data : okGoAllApplyShopInfoBean.getData()) {
                //3.1 创建集合，装载表格内容
                ArrayList<String> rowData = new ArrayList<>();
                rowData.add(String.valueOf(data.getAsUserId()));
                rowData.add(String.valueOf(data.getAsId()));
                if (data.getAsType() == 4) {
                    rowData.add("商家");
                }
                if (data.getAsState() == 0) {
                    rowData.add("待审核");
                } else if (data.getAsState() == 1) {
                    rowData.add("已审核");
                } else if (data.getAsState() == -1) {
                    rowData.add("审核失败");
                }
                rowData.add(data.getAsLicence());
                rowData.add(data.getCreateTime());
                rowData.add(data.getUpdateTime());
                //3.2 单个用户信息遍历后，设置进表格总集合
                tableData.add(rowData);
            }
            // 4.设置文本滚动显示用户条数
            if (!tableData.isEmpty()) {
                //4.1设置滚动文本
                mMtvCountCerShopNumber.startSimpleRoll(Collections.singletonList("        校园帮APP当前拥有" + (tableData.size() - 1) + "个申请商家认证用户        "));
                //4.2监听文本是否匹配--->匹配相同，执行循环滚动
                mMtvCountCerShopNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                    @Override
                    public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                        //4.3滚动开始
                        if (displayMsg.toString().equals("        校园帮APP当前拥有\" + (tableData.size() - 1) + \"个申请商家认证用户        ")) {
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
            //5.携带全部用户信息的表格总集合，开始适配绘制表格
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlCerShopInfoTableContentView, tableData);
            //6.表格UI设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(200) //列最大宽度
                    .setMinColumnWidth(50) //列最小宽度
                    .setColumnWidth(4, 200) //设置指定列文本宽度
                    .setMinRowHeight(30)//行最小高度
                    .setMaxRowHeight(30)//行最大高度
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
                                    startSelectAllCerShopInfo(context);
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
                            //获取索引对应的用户信息
                            String strUserId = tableData.get(position).get(0); //当前item用户ID
                            String strCerType = tableData.get(position).get(2); //当前item申请类型
                            String strCerState = tableData.get(position).get(3); //当前item审核状态
                            String strLicencePicURL = tableData.get(position).get(4); //当前item餐饮许可证URL
                            //弹出操作对话框
                            new XPopup.Builder(getContext())
                                    .maxHeight(800)
                                    .isDarkTheme(true)
                                    .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                    .asCenterList("选择用户ID[" + strUserId + "]申请的操作方式", new String[]{"处理商家申请", "查看用户信息", "查看餐饮许可证"}, new OnSelectListener() {
                                        @Override
                                        public void onSelect(int position, String searchMode) {
                                            switch (position) {
                                                case 0:
                                                    doCerShopApplyInfo(strUserId, strCerType, strCerState);//处理商家申请，分配用户角色 + 审核状态
                                                    break;
                                                case 1:
                                                    doSelectShopUserInfo(strUserId);//查看商家信息,通过用户ID
                                                    break;
                                                case 2:
                                                    checkLicenceImg(strLicencePicURL);//查看学生证图片
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        }
                    })
                    .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                        @Override
                        public void onItemLongClick(View item, int position) {
                            ToastUtils.show("您长按了第" + position + "行商家信息");
                        }
                    })
                    .setOnItemSeletor(R.color.Grey300)//设置Item被选中颜色
                    .show(); //显示表格
            mLockTableView.getTableScrollView().setPullRefreshEnabled(true);//开启下拉刷新
            mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);//开启上拉加载
            mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.BallBeat);//设置下拉刷样式风格
        } else {
            mMtvCountCerShopNumber.startSimpleRoll(Collections.singletonList("无此商家认证信息"));
            //4.2监听文本是否匹配--->匹配相同，执行循环滚动
            mMtvCountCerShopNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                @Override
                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                    //4.3滚动开始
                    if (displayMsg.toString().equals("无此商家认证信息")) {
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
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlCerShopInfoTableContentView, tableData);
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
                    .setNullableString("无此跑腿学生") //空值替换值
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
                            //下拉刷新，再次获取用户全部数据
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startSelectAllCerShopInfo(context);
                                    ToastUtils.show("全部跑腿认证信息重新加载完毕");
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
     * 搜索已申请商家认证信息
     */
    private void startSearchCerShopInfo() {
        new XPopup.Builder(getContext())
                .maxHeight(800)
                .isDarkTheme(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList("选择搜索信息方式", new String[]{"用户ID", "其它"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String searchMode) {
                        switch (position) {
                            case 0:
                                chooseUserId(); //选择用户ID
                                break;
                            case 1:
                                ToastUtils.show("正在努力开发中...");
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 通过用户ID查询已申请商家认证信息
     */
    private void chooseUserId() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索跑腿信息", null, null, "请输入用户ID",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strCerRunId) {
                                //开始网络请求，访问后端服务器，执行查询跑腿认证操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_APPLY_RUN_INFO_BY_USERID + "/" + strCerRunId)
                                        .tag("跑腿用户ID查询信息")
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
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mFlDevSelectAllCerShopInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 处理商家申请，分配用户角色 + 审核状态
     *
     * @param strUserId  用户ID
     * @param strCerType 申请类型
     * @param cerState   审核状态
     */
    private void doCerShopApplyInfo(String strUserId, String strCerType, String cerState) {
        new BottomSheet.BottomListSheetBuilder(getActivity())
                .setTitle("商家审核处理")
                .addItem("暂不处理")
                .addItem("同意申请")
                .addItem("拒绝申请")
                .setIsCenter(true)
                .setOnSheetItemClickListener(new BottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(BottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();//关闭底部弹窗
                        switch (position) {
                            case 0: //暂不处理
                                XToastUtils.warning("别忘记处理申请哟");
                                break;
                            case 1: //同意申请
                                agreeApplyCerToInfo(strUserId, strCerType, cerState);// 角色分配 + 审核状态
                                break;
                            case 2: //拒绝申请
                                notAgreeUserApply(strUserId, cerState);//不同意申请 + 审核状态
                                break;
                        }
                    }
                })
                .build()
                .show();
    }

    /**
     * 角色分配 + 审核状态 + 审核回复
     *
     * @param userId        用户ID
     * @param strCerType    申请类型
     * @param cerOldState   旧审核状态
     */
    private void agreeApplyCerToInfo(String userId, String strCerType, String cerOldState) {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("同意申请原因", null, null, "请输入审核评语内容",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strNewPostscript) {
                                if (!TextUtils.isEmpty(strNewPostscript)) {
                                    /*1.先处理表格抽取的数据 */
                                    int intRoleId = 0;

                                    if (strCerType.equals("商家")) {
                                        intRoleId = 4;
                                    }

                                    /* 2.分配角色 */
                                    OkGo.<String>post(Constant.ADMIN_ADD_ONCE_USER_ROLE_INFO_BY_USERID_AND_ROLE_ID + "/" + Integer.parseInt(userId) + "/" + intRoleId)
                                            .tag("同意跑腿审核处理")
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                    if (200 == okGoResponseBean.getCode() && "用户角色添加失败".equals(okGoResponseBean.getMsg())) {
                                                        XToastUtils.success(R.string.apply_cer_no);
                                                        return;
                                                    }
                                                    if (200 == okGoResponseBean.getCode() && "用户角色添加成功".equals(okGoResponseBean.getMsg())) {
                                                        XToastUtils.success(R.string.apply_cer_ok);
                                                        /* 3.审核状态和审核回复业务操作 */
                                                        int intCerOldState = 0, intNewState = 1;//此0为初始值无意义————此1代表审核通过
                                                        if (cerOldState.equals("待审核")) {
                                                            intCerOldState = 0; //此0代表旧审核状态-->待审核
                                                        }
                                                        OkGo.<String>post(Constant.ADMIN_UPDATE_APPLY_SHOP_INFO_STATE_OLD_INFO + "/" + Integer.parseInt(userId) + "/" + intCerOldState + "/" + intNewState)
                                                                .tag("同意跑腿申请")
                                                                .execute(new StringCallback() {
                                                                    @Override
                                                                    public void onSuccess(Response<String> response) {
                                                                        OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                                        if (200 == okGoResponseBean.getCode() && "跑腿认证审核通过".equals(okGoResponseBean.getMsg())) {
                                                                            startSelectAllCerShopInfo(context);
                                                                            XToastUtils.success(R.string.apply_cer_ok);
                                                                            return;
                                                                        }
                                                                        if (200 == okGoResponseBean.getCode() && "跑腿认证审核失败".equals(okGoResponseBean.getMsg())) {
                                                                            XToastUtils.success(R.string.apply_cer_no);

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(Response<String> response) {
                                                                        super.onError(response);
                                                                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mFlDevSelectAllCerShopInfo, "请求错误，服务器连接失败！");
                                                                    }
                                                                });
                                                    }
                                                }

                                                @Override
                                                public void onError(Response<String> response) {
                                                    super.onError(response);
                                                    OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mFlDevSelectAllCerShopInfo, "请求错误，服务器连接失败！");
                                                }
                                            });
                                } else {
                                    XToastUtils.info(R.string.apply_cer_info);
                                }
                            }
                        })
                .show();
    }

    /**
     * 不同意申请 + 审核状态 + 审核回复
     *
     * @param strUserId     用户ID
     * @param cerOldState   旧审核状态
     */
    private void notAgreeUserApply(String strUserId, String cerOldState) {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("拒绝申请原因", null, null, "请输入审核评语内容",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strNewPostscript) {
                                /* 3.审核状态和审核回复业务操作 */
                                int intCerOldState = 0, intNewState = -1;//此0为初始值无意义————此-1代表审核失败
                                if (cerOldState.equals("待审核")) {
                                    intCerOldState = 0; //此0代表旧审核状态-->待审核
                                }
                                OkGo.<String>post(Constant.ADMIN_UPDATE_APPLY_RUN_INFO_STATE_AND_POSTSCRIPT_BY_OLD_INFO + "/" + Integer.parseInt(strUserId) + "/" + intCerOldState + "/" + intNewState)
                                        .tag("拒绝申请原因")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                OkGoResponseBean okGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                                                if (200 == okGoResponseBean.getCode() && "跑腿认证审核通过".equals(okGoResponseBean.getMsg())) {
                                                    startSelectAllCerShopInfo(context);
                                                    XToastUtils.success(R.string.apply_cer_not_ok);
                                                    return;
                                                }
                                                if (200 == okGoResponseBean.getCode() && "跑腿认证审核失败".equals(okGoResponseBean.getMsg())) {
                                                    XToastUtils.success(R.string.apply_cer_not_no);
                                                }
                                            }

                                            @Override
                                            public void onError(Response<String> response) {
                                                super.onError(response);
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mFlDevSelectAllCerShopInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 通过用户ID，查看学生信息信息
     *
     * @param strUserId 用户ID
     */
    private void doSelectShopUserInfo(String strUserId) {
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true) //关闭弹窗，释放资源
                .hasBlurBg(true) //开启高斯模糊
                .hasStatusBar(true)
                .asCustom(new UserInfoFullPopup(getContext(), strUserId)) //定制自定义布局
                .show();
    }

    /**
     * 查看餐饮许可证图片
     *
     * @param strLicenceURL 阿里云OSS餐饮许可证图片URL地址
     */
    private void checkLicenceImg(String strLicenceURL) {
        //单例模式将学生证URL传到Fragment中
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fl_dev_select_all_cer_run_info, ApplyImgLookFragment.getInstance("urlData", strLicenceURL)).commit();
    }
}
