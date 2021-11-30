package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserInfoPageBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.okGoAllUserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/29
 * 描述：超管查询用户全部信息
 *
 * @author ZSAndroid
 * @create 2021-11-29-13:23
 */

@SuppressLint("NonConstantResourceId")
public class DeveloperSelectAllUserInfoFragment extends BaseFragment {
    private static final String TAG = "DeveloperSelectAllUserInfoFragment";
    /* 父布局 */
    @BindView(R2.id.ll_dev_select_all_user_info) LinearLayout mLlDevSelectAllUserInfo;
    /* 列表控件 */
    @BindView(R2.id.contentView) LinearLayout mContentView;
    /* XUI按钮搜索用户 */
    @BindView(R2.id.btn_search_user_info) ButtonView mBtnSearchUserInfo;
    //XUI跑马灯
    @BindView(R2.id.mtv_count_user_number) MarqueeTextView mMtvCountUserNumber;
    //顶部标题数组
    private String[] topTitleArrays;



    /**
     * @return 单例对象
     */
    public static DeveloperSelectAllUserInfoFragment newInstance() {
        return new DeveloperSelectAllUserInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_all_user_info;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        //准备表的顶部标题数据
        topTitleArrays = new String[]{"用户ID","用户名","真实姓名","性别","身份证号","学号","手机号","QQ邮箱","班级","院系","注册时间","修改时间"};
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
        //开始查询全部用户信息
        startSelectAllUserInfo(context);
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
    @OnClick({R2.id.btn_search_user_info})
    public void onSelectAllUserInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_user_info://确定查询
                startSearchUserInfo();
                break;
        }
    }

    /**
     * 弹出搜索界面
     */
    private void startSearchUserInfo() {
        new XPopup.Builder(getContext())
                .maxHeight(800)
                .isDarkTheme(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList("选择搜索信息方式", new String[]{"编号", "姓名", "用户名"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String searchMode) {
                        switch (position) {
                            case 0:
                                chooseUserId(); //选择编号
                                break;
                            case 1:
                                chooseRealName();//选择用户姓名
                                break;
                            case 2:
                                chooseUserName();//选择用户名
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 通过ID查询对应信息
     */
    private void chooseUserId() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索用户信息", null, null, "请输入用户便编号",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strUserId) {
                                //开始网络请求，访问后端服务器，执行查询角色操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_USER_INFO_BY_USER_ID + "/" + strUserId)
                                        .tag("用户ID查询信息")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                XPopupUtils.getInstance().setShowDialog(getActivity(), "正在搜索...");
                                            }

                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                                            }

                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                                XPopupUtils.getInstance().setSmartDisDialog();
                                            }

                                            @Override
                                            public void onError(Response<String> response) {
                                                super.onError(response);
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllUserInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 通过姓名查询对应信息
     */
    private void chooseRealName() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索用户信息", null, null, "请输入用户姓名",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strRealName) {
                                //开始网络请求，访问后端服务器，执行查询用户操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_USER_INFO_BY_USER_REAL_NAME + "/" + strRealName)
                                        .tag("姓名查询信息")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                XPopupUtils.getInstance().setShowDialog(getActivity(), "正在搜索...");
                                            }

                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                                            }

                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                                XPopupUtils.getInstance().setSmartDisDialog();
                                            }

                                            @Override
                                            public void onError(Response<String> response) {
                                                super.onError(response);
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllUserInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * 通过用户名查询对应信息
     */
    private void chooseUserName() {
        new XPopup.Builder(getContext())
                .hasStatusBarShadow(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)
                .isDarkTheme(true)
                .isViewMode(true)
                .asInputConfirm("搜索用户信息", null, null, "请输入用户名",
                        new OnInputConfirmListener() {
                            @Override
                            public void onConfirm(String strUserName) {
                                //开始网络请求，访问后端服务器，执行查询用户操作
                                OkGo.<String>post(Constant.ADMIN_SELECT_USER_INFO_BY_USERNAME + "/" + strUserName)
                                        .tag("用户名查询信息")
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onStart(Request<String, ? extends Request> request) {
                                                super.onStart(request);
                                                XPopupUtils.getInstance().setShowDialog(getActivity(), "正在搜索...");
                                            }

                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                                            }

                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                                XPopupUtils.getInstance().setSmartDisDialog();
                                            }

                                            @Override
                                            public void onError(Response<String> response) {
                                                super.onError(response);
                                                OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllUserInfo, "请求错误，服务器连接失败！");
                                            }
                                        });
                            }
                        })
                .show();
    }

    /**
     * OkGo网络请求开始调用API接口开始查询全部用户信息
     *
     * @param context 上下文
     */
    private void startSelectAllUserInfo(Context context) {
        //开始网络请求，访问后端服务器，执行查询角色操作
        OkGo.<String>post(Constant.ADMIN_SELECT_ALL_USER_INFO)
                .tag("查询全部用户信息")
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllUserInfo, "请求错误，服务器连接失败！");
                    }
                });

    }

    /**
     * 开始设置表数据
     * @param response okGo响应返回的Json字符串
     */
    private void starSetTabData(Response<String> response) {
        okGoAllUserInfoBean okGoAllUserInfoBean = GsonUtil.gsonToBean(response.body(), okGoAllUserInfoBean.class);
        if (200 == okGoAllUserInfoBean.getCode() && okGoAllUserInfoBean.getData().size() > 0 && "success".equals(okGoAllUserInfoBean.getMsg())) {
            //1.创建集合，用于显示表格
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //2.顶部标题数组，绑定表第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //3.遍历用户集合，绑定表格内容
            for (okGoAllUserInfoBean.Data data : okGoAllUserInfoBean.getData()) {
                //3.1 创建集合，装载表格内容
                ArrayList<String> rowData = new ArrayList<>();
                rowData.add(String.valueOf(data.getUlId()));
                rowData.add(data.getUlUsername());
                rowData.add(data.getUlRealname());
                rowData.add(data.getUlSex());
                rowData.add(data.getUlIdcard());
                rowData.add(data.getUlStuno());
                rowData.add(data.getUlTel());
                rowData.add(data.getUlEmail());
                rowData.add(data.getUlClass());
                rowData.add(data.getUlDept());
                rowData.add(data.getCreateTime());
                rowData.add(data.getUpdateTime());
                //3.2 单个用户信息遍历后，设置进表格总集合
                tableData.add(rowData);
            }
            // 4.设置文本滚动显示用户条数
            if (!tableData.isEmpty()) {
                //4.1设置滚动文本
                mMtvCountUserNumber.startSimpleRoll(Collections.singletonList("        已成功查询出" + (tableData.size()-1) + "条用户数据"        ));
                //4.2监听文本是否匹配--->匹配相同，执行循环滚动
                mMtvCountUserNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                    @Override
                    public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                        //4.3滚动开始
                        if (displayMsg.toString().equals("        已成功查询出" + (tableData.size()-1) + "条用户数据"        )) {
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
            final LockTableView mLockTableView = new LockTableView(getActivity(), mContentView, tableData);
            //6.表格UI设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(150) //列最大宽度
                    .setMinColumnWidth(50) //列最小宽度
                    .setColumnWidth(4,120) //设置指定列文本宽度
                    .setColumnWidth(9,120) //设置指定列文本宽度
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
                                    startSelectAllUserInfo(context);
                                    ToastUtils.show("全部用户信息重新加载完毕");
                                }
                            }, 1000);
                        }
                        @Override
                        public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<String>> mTableDatas) {
                            //上拉加载刷新，分页功能待开发
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show("分页功能待实现");
                                    mXRecyclerView.setNoMore(true);
//                                                    Log.i("现有表格数据", mTableDatas.toString());
//                                                    OkGo.<String>post(Constant.ADMIN_SELECT_ALL_USER_INFO_BY_PAGE + "/" +1+"/" +4)
//                                                            .tag("查询全部用户信息")
//                                                            .execute(new StringCallback() {
//                                                                @Override
//                                                                public void onSuccess(Response<String> response) {
//                                                                    OkGoUserInfoPageBean okGoUserInfoPageBean = GsonUtil.gsonToBean(response.body(), OkGoUserInfoPageBean.class);
//                                                                    if (200 == okGoUserInfoPageBean.getCode() && okGoUserInfoPageBean.getData().getRecords().size() > 0 && "success".equals(okGoUserInfoPageBean.getMsg())) {
//                                                                        //5.遍历添加表数据
//                                                                        for (OkGoUserInfoPageBean.Data.Records records : okGoUserInfoPageBean.getData().getRecords()) {
//                                                                            ArrayList<String> rowData = new ArrayList<>();
//                                                                            rowData.add(String.valueOf(records.getUlId()));
//                                                                            rowData.add(records.getUlUsername());
//                                                                            rowData.add(records.getUlRealname());
//                                                                            rowData.add(records.getUlSex());
//                                                                            rowData.add(records.getUlIdcard());
//                                                                            rowData.add(records.getUlStuno());
//                                                                            rowData.add(records.getUlTel());
//                                                                            rowData.add(records.getUlEmail());
//                                                                            rowData.add(records.getUlClass());
//                                                                            rowData.add(records.getUlDept());
//                                                                            rowData.add(records.getCreateTime());
//                                                                            rowData.add(records.getUpdateTime());
//                                                                            mTableDatas.add(rowData);
//                                                                        } mLockTableView.setTableDatas(mTableDatas);
//                                                                    } else {
//                                                                        mXRecyclerView.setNoMore(true);
//                                                                    }
//                                                                    mXRecyclerView.loadMoreComplete();
//                                                                }
//                                                            });
                                }
                            }, 1000);
                        }
                    })
                    .setOnItemClickListenter(new LockTableView.OnItemClickListenter() {
                        @Override
                        public void onItemClick(View item, int position) {
                            Log.i("选择用户信息------",tableData.get(position).toString());
                            ToastUtils.show("您点击了第" + position + "行用户信息");

                        }
                    })
                    .setOnItemLongClickListenter(new LockTableView.OnItemLongClickListenter() {
                        @Override
                        public void onItemLongClick(View item, int position) {
                            ToastUtils.show("您长按了第" + position + "行用户信息");
                        }
                    })
                    .setOnItemSeletor(R.color.Grey300)//设置Item被选中颜色
                    .show(); //显示表格
            mLockTableView.getTableScrollView().setPullRefreshEnabled(true);//开启下拉刷新
            mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);//开启上拉加载
            mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.BallBeat);//设置下拉刷样式风格
        }else {
            //空集合表数据
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //设置顶部第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //空数据其它列字段
            tableData.add(new ArrayList<>());
            //表格绑定空数据
            final LockTableView mLockTableView = new LockTableView(getActivity(), mContentView, tableData);
            //表格UI参数设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(150) //列最大宽度
                    .setMinColumnWidth(50) //列最小宽度
                    .setColumnWidth(4,120) //设置指定列文本宽度
                    .setColumnWidth(9,120) //设置指定列文本宽度
                    .setMinRowHeight(20)//行最小高度
                    .setMaxRowHeight(20)//行最大高度
                    .setTextViewSize(12) //单元格字体大小
                    .setFristRowBackGroudColor(R.color.xui_btn_blue_normal_color)//表头背景色
                    .setTableHeadTextColor(R.color.White)//表头字体颜色
                    .setTableContentTextColor(R.color.colorAccent)//单元格字体颜色
                    .setCellPadding(15)//设置单元格内边距(dp)
                    .setNullableString("无此用户") //空值替换值
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
                                    startSelectAllUserInfo(context);
                                    ToastUtils.show("全部用户信息重新加载完毕");
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
}
//new XPopup.Builder(getContext())
//        .isDestroyOnDismiss(true) //关闭弹窗，释放资源
//        .isLightStatusBar(true) //高亮状态栏
//        .hasBlurBg(true) //开启高斯模糊
//        .setPopupCallback(new XPopupCallback() {
//@Override
//public void onCreated(BasePopupView popupView) {
//        TextView textView = popupView.findViewById(R.id.tv_user_tel);
//        Button button = popupView.findViewById(R.id.btn_back_all_user_info);
//        button.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//        popupView.dismiss();
//        }
//        });
//        textView.setText(userInfo.toString());
//        List<UserInfoBean> list = new ArrayList<>();
//        SmartTable smartTable =  (SmartTable<UserInfoBean>)popupView.findViewById(R.id.table_user_info);
//        smartTable.setData(list);
//
//        }
//
//@Override
//public void beforeShow(BasePopupView popupView) {
//
//        }
//
//@Override
//public void onShow(BasePopupView popupView) {
//
//        }
//
//@Override
//public void onDismiss(BasePopupView popupView) {
//
//        }
//
//@Override
//public void beforeDismiss(BasePopupView popupView) {
//
//        }
//
//@Override
//public boolean onBackPressed(BasePopupView popupView) {
//        return false;
//        }
//
//@Override
//public void onKeyBoardStateChanged(BasePopupView popupView, int height) {
//
//        }
//
//@Override
//public void onDrag(BasePopupView popupView, int value, float percent, boolean upOrLeft) {
//
//        }
//        })
//        .asCustom(new UserInfoFullPopup(getContext())) //定制自定义布局
//        .show();