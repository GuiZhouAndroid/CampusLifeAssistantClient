package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
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
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoRoleOrPermissionListBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
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
    //跑马灯滚动显示权限集合总条数
    @BindView(R2.id.mtv_count_user_permission_list_number) MarqueeTextView mMtvCountUserPermissionListNumber;
    /* 填充表格视图 */
    @BindView(R2.id.ll_user_permission_list_table_content_view) LinearLayout mLlUserPermissionListTableContentView;
    //顶部标题数组
    private String[] topTitleArrays;

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
        topTitleArrays = new String[]{"权限名"};
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
                doSelectUserPermissionByUserName(strPermissionUserName);// 开始查询该用户的对应的权限集合
                break;
        }
    }

    /**
     * 开始查询该用户的对应的权限集合
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
                        XToastUtils.success("搜索成功");
                        starSetTabData(response, strPermissionUserName);//Json字符串Gson解析使用，绘制表格
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

    /**
     * 开始设置表数据
     *
     * @param response              okGo响应返回的Json字符串
     * @param strPermissionUserName
     */
    private void starSetTabData(Response<String> response, String strPermissionUserName) {
        OkGoRoleOrPermissionListBean okGoRoleOrPermissionListBean = GsonUtil.gsonToBean(response.body(), OkGoRoleOrPermissionListBean.class);
        //成功(真实姓名不存在)
        if (200 == okGoRoleOrPermissionListBean.getCode() && okGoRoleOrPermissionListBean.getData().size() > 0 && "success".equals(okGoRoleOrPermissionListBean.getMsg())) {
            //1.创建集合，用于显示表格
            ArrayList<ArrayList<String>> tableData = new ArrayList<>();
            //2.顶部标题数组，绑定表第一列标题
            tableData.add(new ArrayList<>(Arrays.asList(topTitleArrays)));
            //3.遍历用户角色权限集合，绑定表格内容
            for (String strPermission : okGoRoleOrPermissionListBean.getData()) {
                //3.1 创建集合，装载表格内容
                ArrayList<String> rowData = new ArrayList<>();
                //用户
                rowData.add(strPermission);
                //3.2 单个用户角色权限信息遍历后，设置进表格总集合
                tableData.add(rowData);
            }
            // 4.设置文本滚动显示用户条数
            if (!tableData.isEmpty()) {
                //4.1设置滚动文本
                mMtvCountUserPermissionListNumber.startSimpleRoll(Collections.singletonList("        " + strPermissionUserName + "拥有" + (tableData.size() - 1) + "条权限        "));
                //4.2监听文本是否匹配--->匹配相同，执行循环滚动
                mMtvCountUserPermissionListNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                    @Override
                    public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                        //4.3滚动开始
                        if (displayMsg.toString().equals("        " + strPermissionUserName + "拥有" + (tableData.size() - 1) + "条权限        ")) {
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
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlUserPermissionListTableContentView, tableData);
            //6.表格UI设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(260) //列最大宽度
                    .setMinColumnWidth(260) //列最小宽度
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
                                    doSelectUserPermissionByUserName(strPermissionUserName);
                                    XToastUtils.success("权限信息重新加载完成");
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
                                    XToastUtils.info("分页功能待实现");
                                }
                            }, 1000);
                        }
                    })
                    .setOnItemSeletor(R.color.Grey300)//设置Item被选中颜色
                    .show(); //显示表格
            mLockTableView.getTableScrollView().setPullRefreshEnabled(true);//开启下拉刷新
            mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);//开启上拉加载
            mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.BallBeat);//设置下拉刷样式风格
        } else {
            mMtvCountUserPermissionListNumber.startSimpleRoll(Collections.singletonList("       无此权限相关信息       "));
            //4.2监听文本是否匹配--->匹配相同，执行循环滚动
            mMtvCountUserPermissionListNumber.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
                @Override
                public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                    //4.3滚动开始
                    if (displayMsg.toString().equals("       无此权限相关信息       ")) {
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
            final LockTableView mLockTableView = new LockTableView(getActivity(), mLlUserPermissionListTableContentView, tableData);
            //表格UI参数设置
            mLockTableView.setLockFristColumn(true) //是否锁定第一列
                    .setLockFristRow(true) //是否锁定第一行
                    .setMaxColumnWidth(260) //列最大宽度
                    .setMinColumnWidth(260) //列最小宽度
                    .setMinRowHeight(30)//行最小高度
                    .setMaxRowHeight(30)//行最大高度
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
                            //下拉刷新，再次获取用户全部数据
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    doSelectUserPermissionByUserName(strPermissionUserName);
                                    XToastUtils.success("权限信息重新加载完成");
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
                                    XToastUtils.info("分页功能待实现");
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
