package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.chad.library.adapter.base.module.BaseDraggableModule;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.popupwindow.status.StatusView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseUserAddressInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.CustomLoadMoreView;
import work.lpssfxy.www.campuslifeassistantclient.base.custominterface.UpdateActivityUIFromFragment;
import work.lpssfxy.www.campuslifeassistantclient.base.custompopup.AddAddressInfoFullPopup;
import work.lpssfxy.www.campuslifeassistantclient.base.custompopup.UpdateAddressInfoFullPopup;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.DynamicTimeFormat;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/9
 * 描述：用户收货地址界面
 *
 * @author ZSAndroid
 * @create 2021-12-09-22:28
 */

@SuppressLint("NonConstantResourceId")
public class UserAddressActivity extends BaseActivity implements UpdateActivityUIFromFragment {

    private static final String TAG = "UserAddressActivity";
    /** 状态布局 */
    @BindView(R.id.status_top_show) StatusView mStatusTopShow;
    /** 返回 */
    @BindView(R2.id.iv_address_back) ImageView mIvAddressBack;
    /** 下拉刷新 */
    @BindView(R2.id.refreshLayout_address) RefreshLayout mRefreshLayoutAddress;
    /** 收货地址内容适配器 */
    @BindView(R2.id.ryc_address) RecyclerView mRycAddress;
    private BaseUserAddressInfoAdapter userAddressInfoAdapter;


    @Override
    protected Boolean isSetSwipeBackLayout() {
        return true;
    }

    @Override
    protected Boolean isSetStatusBarState() {
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationState() {
        return false;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return false;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_user_address;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        initSelectUserAddressAllInfoRecyclerView();//初始化收货地址内容适配器
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {
        //初始化下拉刷新收货地址信息
        initSetRefreshListener();
    }

    /**
     * 使用 ButterKnife注解式监听单击事件
     *
     * @param view 控件Id
     */
    @OnClick({R2.id.iv_address_back})
    public void onUserAddressViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_address_back://点击返回
                UserAddressActivity.this.finish();
                break;
        }
    }

    /**
     * 初始化RecyclerView列表控件
     */
    private void initSelectUserAddressAllInfoRecyclerView() {
        // 1.创建布局管理实例对象
        LinearLayoutManager layoutManager = new LinearLayoutManager(UserAddressActivity.this);
        // 2.设置RecyclerView布局方式为垂直方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // 3.RecyclerView绑定携带垂直方向参数的布局管理实例对象
        mRycAddress.setLayoutManager(layoutManager);
    }

    /**
     * 初始化下拉刷新收货地址信息
     */
    private void initSetRefreshListener() {
        // 设置下拉刷新参数和主题颜色
        ClassicsHeader mClassicsHeader = (ClassicsHeader) mRefreshLayoutAddress.getRefreshHeader();
        if (mClassicsHeader != null) {
            mClassicsHeader.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        }
        setThemeColor(mRefreshLayoutAddress, R.color.welcome_colorMain);
        mRefreshLayoutAddress.autoRefresh();
        // 执行下拉刷新业务
        mRefreshLayoutAddress.setOnRefreshListener(new OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (userAddressInfoAdapter == null) {
                    startSelectUserAddressAllInfo();//查询当前登录用户全部的收货地址
                }
                if (userAddressInfoAdapter != null && mRefreshLayoutAddress.getState().isOpening) {
                    mRefreshLayoutAddress.finishRefresh();
                    userAddressInfoAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 查询当前登录用户全部的收货地址
     */
    private void startSelectUserAddressAllInfo() {
        OkGo.<String>post(Constant.USER_SELECT_ADDRESS_ALL_INFO_BY_USER_ID)
                .tag("查询用户全部收货地址")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(UserAddressActivity.this, "正在加载信息...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        doResponseJsonData(response);
                    }

                    @Override
                    public void onFinish() {
                        if (mRefreshLayoutAddress.getState().isOpening) {
                            mRefreshLayoutAddress.finishRefresh();
                        }
                        //mStatusTopShow.dismiss();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        mStatusTopShow.getErrorView();
                    }
                });
    }

    /**
     * 处理收货地址数据
     *
     * @param response JSON字符串
     */
    private void doResponseJsonData(Response<String> response) {
        OkGoAllAddressInfoBean okGoAllAddressInfoBean = GsonUtil.gsonToBean(response.body(), OkGoAllAddressInfoBean.class);
        if (200 == okGoAllAddressInfoBean.getCode() && "success".equals(okGoAllAddressInfoBean.getMsg())) {
            List<UserAddressInfoBean> userAddressInfoBeanList = new ArrayList<>();
            for (OkGoAllAddressInfoBean.Data data : okGoAllAddressInfoBean.getData()) {
                userAddressInfoBeanList.add(new UserAddressInfoBean(
                        data.getAddressId(), data.getAddressName(), data.getCreateTime(), data.getDistrict(), data.getFloor(),
                        data.getGender(), data.getMobile(), data.getPlace(), data.getStreet(), data.getUpdateTime(), data.getUserId()
                ));
            }
            /* 收货地址信息列表适配器 */
            userAddressInfoAdapter = new BaseUserAddressInfoAdapter(R.layout.activity_user_address_recycler_view_item, userAddressInfoBeanList);
            //为列表控件设置适配器，并为执行适配操作
            mRycAddress.setAdapter(userAddressInfoAdapter);
            //初始化适配器动画
            initAdapterAnimation(userAddressInfoAdapter);
            //初始化适配器上拉加载更多
            initAdapterLoadMore(userAddressInfoAdapter, userAddressInfoAdapter.getLoadMoreModule());
            //初始化适配器侧滑删除
            initAdapterDragDelete(userAddressInfoAdapter, userAddressInfoAdapter.getDraggableModule());
            //初始化适配器长按拖拽，有时会与侧滑有滑动冲突，这里不使用
            //initAdapterDragTouch(userAddressInfoAdapter.getDraggableModule());
            //初始化适配器空布局 + 监听事件--->新增收货地址（在setAdapter后使用，否则报错）
            initSetAdapterEmptyView(userAddressInfoAdapter);
            //初始化适配器头部页眉 + 监听事件--->新增收货地址
            initSetAdapterHeaderView(userAddressInfoAdapter);
            //初始化适配器item全部单击事件--->默认编辑收货地址
            initSetAdapterOnItemClickListener(userAddressInfoAdapter);
            //初始化适配器适配器item中的子view单击事件
            initSetAdapterOnItemChildClickListener(userAddressInfoAdapter);
        }
    }

    /**
     * 初始化适配器动画
     *
     * @param userAddressInfoAdapter 收货地址适配器
     */
    private void initAdapterAnimation(BaseUserAddressInfoAdapter userAddressInfoAdapter) {
        //创建适配器.动画
        userAddressInfoAdapter.setAnimationEnable(true);//打开动画
        userAddressInfoAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn);//设置动画类型
        userAddressInfoAdapter.setAnimationFirstOnly(false);//设置始终显示动画
        //设置自定义动画
//        userAddressInfoAdapter.setAdapterAnimation(new BaseAnimation() {
//            @NonNull
//            @Override
//            public Animator[] animators(@NonNull View view) {
//                return new Animator[]{
//                        ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
//                        ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
//                };
//            }
//        });
    }

    /**
     * 初始化适配器上拉加载更多
     *
     * @param userAddressInfoAdapter 收货地址适配器
     * @param loadMoreModule         收货地址适配器下拉加载基类
     */
    private void initAdapterLoadMore(BaseUserAddressInfoAdapter userAddressInfoAdapter, BaseLoadMoreModule loadMoreModule) {
        loadMoreModule.setEnableLoadMore(true);//打开上拉加载
        loadMoreModule.setAutoLoadMore(true);//自动加载
        loadMoreModule.setPreLoadNumber(1);//设置滑动到倒数第几个条目时自动加载，默认为1
        loadMoreModule.setEnableLoadMoreIfNotFullPage(true);//当数据不满一页时继续自动加载
        loadMoreModule.setLoadMoreView(new CustomLoadMoreView());//设置自定义加载布局
        loadMoreModule.setOnLoadMoreListener(new OnLoadMoreListener() {//设置加载更多监听事件
            @Override
            public void onLoadMore() {
                mRycAddress.postDelayed(new Runnable() {//延迟以提升用户体验
                    @Override
                    public void run() {
                        //userAddressInfoAdapter.addData(userAddressInfoBeanList);
                        userAddressInfoAdapter.getLoadMoreModule().loadMoreEnd();//加载完毕
                        //userAddressInfoAdapter.getLoadMoreModule().loadMoreComplete();//加载完毕
                        //userAddressInfoAdapter.getLoadMoreModule().loadMoreFail()//加载失败
                    }
                }, 1000);
            }
        });
    }

    /**
     * 初始化适配器侧滑删除
     *
     * @param userAddressInfoAdapter
     * @param draggableModule        收货地址适配器拖拽基类
     */
    private void initAdapterDragDelete(BaseUserAddressInfoAdapter userAddressInfoAdapter, BaseDraggableModule draggableModule) {
        draggableModule.setSwipeEnabled(true);//启动侧滑删除
        draggableModule.getItemTouchHelperCallback().setSwipeMoveFlags(ItemTouchHelper.START);//侧滑删除方向
        draggableModule.setOnItemSwipeListener(new OnItemSwipeListener() {
            private int addressId;

            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int position) { //当滑动动作开始时调用，提取收货地址ID
                addressId = userAddressInfoAdapter.getData().get(position).getAddressId();
                Log.i(TAG, "onItemSwipeStart: " + userAddressInfoAdapter.getData().get(position).toString());
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int position) {//当item滑动删除之前，松手之时调用

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int position) { //当item滑动时视图从适配器中删除之后调用
                deleteUserAddressInfo(addressId);//删除收货地址
                //移除list中的数据后，并没有紧接着告知adapter有数据已经移除，就会导致后面操作的报错
                //解决方法是，在list做完remove或者add操作后，紧跟着notifyItemInserted(notifyItemRangeInserted)或notifyDataSetChanged
                userAddressInfoAdapter.notifyDataSetChanged();//移出后，同步List收获地址数量，避免产生下标越界等错误--->必须在主线程
            }

            @Override //滑动时的背景，正在滑动时调用，一般用于绘制背景色
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(Color.parseColor("#fff75446"));
            }
        });
    }

    /**
     * 初始化适配器长按拖拽
     *
     * @param draggableModule 收货地址适配器拖拽基类
     */
    private void initAdapterDragTouch(BaseDraggableModule draggableModule) {
        draggableModule.setDragEnabled(true);//启动拖拽
        draggableModule.getItemTouchHelperCallback().setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);//拖拽方向
        draggableModule.setOnItemDragListener(new OnItemDragListener() {//拖拽监听
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Toast.makeText(UserAddressActivity.this, "onItemDragBefore" + viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Toast.makeText(UserAddressActivity.this, "onItemDragAfter" + viewHolder.getLayoutPosition(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化适配器空布局 + 监听事件--->新增收货地址
     *
     * @param userAddressInfoAdapter 收货地址适配器
     */
    private void initSetAdapterEmptyView(BaseUserAddressInfoAdapter userAddressInfoAdapter) {
        userAddressInfoAdapter.setEmptyView(R.layout.custom_address_empty_view);
        userAddressInfoAdapter.getEmptyLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserAddressInfo();//用户添加收货地址
            }
        });
    }

    /**
     * 初始化适配器头部页眉 + 监听事件--->新增收货地址
     *
     * @param userAddressInfoAdapter 收货地址适配器
     */
    private void initSetAdapterHeaderView(BaseUserAddressInfoAdapter userAddressInfoAdapter) {
        userAddressInfoAdapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.activity_address_rcy_header_view_layout, null));
        userAddressInfoAdapter.getHeaderLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserAddressInfo();//用户添加收货地址
            }
        });
    }

    /**
     * 初始化适配器item全部单击事件--->默认编辑收货地址
     *
     * @param userAddressInfoAdapter 收货地址适配器
     */
    private void initSetAdapterOnItemClickListener(BaseUserAddressInfoAdapter userAddressInfoAdapter) {
        userAddressInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                UserAddressInfoBean userAddressInfo = (UserAddressInfoBean) adapter.getItem(position);
                updateUserAddressInfo(userAddressInfo);
            }
        });
    }

    /**
     * 初始化适配器适配器item中的子view单击事件
     *
     * @param userAddressInfoAdapter 收货地址适配器
     */
    private void initSetAdapterOnItemChildClickListener(BaseUserAddressInfoAdapter userAddressInfoAdapter) {
        userAddressInfoAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                UserAddressInfoBean userAddressInfo = (UserAddressInfoBean) adapter.getItem(position);
                switch (view.getId()) {
                    case R.id.ll_address_update: //编辑收货地址
                        updateUserAddressInfo(userAddressInfo);
                        break;
                    case R.id.ll_address_delete://删除收货地址
                        deleteUserAddressInfo(userAddressInfo.getAddressId());
                        userAddressInfoAdapter.remove(position);
                        userAddressInfoAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    /**
     * 用户添加收货地址
     */
    private void addUserAddressInfo() {
        new XPopup.Builder(UserAddressActivity.this)
                .isDestroyOnDismiss(true) //关闭弹窗，释放资源
                .hasBlurBg(true) //开启高斯模糊
                .hasStatusBar(false)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated(BasePopupView popupView) { //重点：弹出创建之前
                        /*避免多次快速点击底部全屏弹窗，导致出现重叠现象*/
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();//移出栈顶Fragment的弹窗页面
                        }
                    }
                })
                .asCustom(new AddAddressInfoFullPopup(UserAddressActivity.this)) //定制自定义布局
                .show();
    }

    /**
     * 收货地址添加成功后，实现此接口拉取数据，更新UserAddressActivity的UI
     * 只需动态添加适配数据，无须访问接口再次调用全部收货地址信息
     *
     * @param userAddressInfoBean 新增的收获地址对象数据，同步现有适配器的集合数据，避免数组下标越界异常
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void doSetAddressData(UserAddressInfoBean userAddressInfoBean) {
        //1.在适配器最后一条数据后面动态追加此新增收货地址
        userAddressInfoAdapter.addData(userAddressInfoAdapter.getData().size(), userAddressInfoBean);
        //刷新适配器
        userAddressInfoAdapter.notifyDataSetChanged();
    }

    /**
     * 通过旧收货地址信息更新用户收货地址
     *
     * @param userAddressInfo 旧收货地址信息
     */
    private void updateUserAddressInfo(UserAddressInfoBean userAddressInfo) {
        new XPopup.Builder(UserAddressActivity.this)
                .isDestroyOnDismiss(true) //关闭弹窗，释放资源
                .hasBlurBg(true) //开启高斯模糊
                .hasStatusBar(false)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated(BasePopupView popupView) { //重点：弹出创建之前
                        /*避免多次快速点击底部全屏弹窗，导致出现重叠现象*/
                        while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStackImmediate();//移出栈顶Fragment的弹窗页面
                        }
                    }
                })
                .asCustom(new UpdateAddressInfoFullPopup(UserAddressActivity.this, userAddressInfo)) //定制自定义布局
                .show();
    }

    /**
     * 用户删除收货地址
     *
     * @param addressId 收货地址ID
     */
    private void deleteUserAddressInfo(int addressId) {
        OkGo.<String>post(Constant.USER_DELETE_ADDRESS_INFO_BY_USER_ID_AND_ADDRESS_ID + "/" + addressId)
                .tag("删除收获地址")
                .execute(new StringCallback() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == OkGoResponseBean.getCode() && "收获地址信息删除成功".equals(OkGoResponseBean.getData())) {
                            XToastUtils.success("地址删除成功");
                        } else {
                            XToastUtils.error(OkGoResponseBean.getData());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        mStatusTopShow.getErrorView();
                    }
                });
    }

}
