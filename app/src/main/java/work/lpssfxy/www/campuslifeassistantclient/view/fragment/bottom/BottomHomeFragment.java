package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tencent.connect.UnionInfo;
import com.tencent.tauth.DefaultUiListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.wikikii.bannerlib.banner.IndicatorLocation;
import com.wikikii.bannerlib.banner.LoopLayout;
import com.wikikii.bannerlib.banner.LoopStyle;
import com.wikikii.bannerlib.banner.OnDefaultImageViewLoader;
import com.wikikii.bannerlib.banner.bean.BannerInfo;
import com.wikikii.bannerlib.banner.listener.OnBannerItemClickListener;
import com.wikikii.bannerlib.banner.view.BannerBgContainer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.MyRecyclerViewAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.scrollview.GoTopNestedScrollView;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoResponseBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.other.CampusInformationBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.QQUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.ToastUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.dialog.CustomAlertDialogUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.ApplyRunCerActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.ApplyShopCerActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.CanteenRunBuyActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.NaviCationActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.RepastPracticeActivity;


/**
 * created by on 2021/8/20
 * 描述：底部导航-首页Fragment
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
@SuppressLint("NonConstantResourceId")
public class BottomHomeFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener, AutoFlowLayout.OnItemClickListener, OnBannerItemClickListener{
    private static final String TAG = "BottomHomeFragment";
    /** 图片轮播 */
//    @BindView(R2.id.banner) BGABanner mBanner;
    @BindView(R2.id.go_top_scrollview) GoTopNestedScrollView mGoTopScrollview;
    @BindView(R2.id.banner_bg_container) BannerBgContainer mBanner_bg_container;
    @BindView(R2.id.loop_layout) LoopLayout mLoop_layout;
    /** 网格布局 */
    @BindView(R2.id.auto_grid_layout) AutoFlowLayout mAuto_grid_layout;
    /** 列表-校园资讯 */
    @BindView(R2.id.recyclerView) RecyclerView mRecyclerView;
    /** 标题栏Toolbar */
    private Toolbar toolbar;
    /** 应用栏布局 */
    private AppBarLayout appBarLayout;
    /** 折叠布局 */
    private CollapsingToolbarLayout collapsing_toolbar_layout;
    /** 浮动按钮 */
    private FloatingActionButton floating_action_btn;
    /** 自定义滑动控件NestedScrollView */
    private GoTopNestedScrollView goTopNestedScrollView;
    /** 网格布局适配数据源 */
    //private static final String[] Grid_Tv_Data = new String[]{"食堂预定","食堂代取", "快递代取", "二手交易","失物招领","星座运势","笑话百科", "天气预报", "位置服务","进入官网"};
    //private static final int[] Grid_Iv_Data =new int[]{R.mipmap.index_own_food,R.mipmap.index_other_people_server,
    //R.mipmap.index_express, R.mipmap.index_second_hand,R.mipmap.index_lost_and_found,R.mipmap.index_horoscope,
    //R.mipmap.index_joke_encyclopedia, R.mipmap.index_weather,R.mipmap.index_map,R.mipmap.index_into_school};
    private static final String[] Grid_Tv_Data = new String[]{"六师官网","食堂代取","快递代取","跑腿认证","商家入驻","接单兼职","告白墙","笑话百科"};
    private static final int[] Grid_Iv_Data =new int[]{R.mipmap.index_into_school,R.mipmap.index_other_people_server, R.mipmap.index_express,R.mipmap.cerrun,R.mipmap.shopapply,R.mipmap.partjob,R.mipmap.confession, R.mipmap.index_joke_encyclopedia};
    /** 校园资讯适配标题源 */
    private static final String[] campusInformationTitles = new String[]{"庆祝中国共产党建党 100 周年“守初","六盘水师范学院2021年度普法责任清单", "六盘水师范学院2022年高层次人才招聘","六盘水师范学院微格（录播）教室","庆祝中国共产党建党 100 周年“守初","六盘水师范学院2021年度普法责任清单", "六盘水师范学院2022年高层次人才招聘","六盘水师范学院微格（录播）教室",
            "庆祝中国共产党建党 100 周年“守初","六盘水师范学院2021年度普法责任清单", "六盘水师范学院2022年高层次人才招聘","六盘水师范学院微格（录播）教室","庆祝中国共产党建党 100 周年“守初","六盘水师范学院2021年度普法责任清单", "六盘水师范学院2022年高层次人才招聘","六盘水师范学院微格（录播）教室"};
    /** 校园资讯适配来源 */
    private static final String[] campusInformationSources = new String[]{"来源：六盘水师范学院","来源：六盘水师范学院", "来源：六盘水师范学院","来源：六盘水师范学院","来源：六盘水师范学院","来源：六盘水师范学院", "来源：六盘水师范学院","来源：六盘水师范学院","来源：六盘水师范学院","来源：六盘水师范学院", "来源：六盘水师范学院","来源：六盘水师范学院","来源：六盘水师范学院","来源：六盘水师范学院", "来源：六盘水师范学院","来源：六盘水师范学院"};
    /** 校园资讯适配发布时间源 */
    private static final String[] campusInformationIssueTimes = new String[]{"时间：2021-07-02","时间：2021-08-12","时间：2021-08-27","时间：2021-09-02","时间：2021-07-02","时间：2021-08-12","时间：2021-08-27","时间：2021-09-02","时间：2021-07-02","时间：2021-08-12","时间：2021-08-27","时间：2021-09-02","时间：2021-07-02","时间：2021-08-12","时间：2021-08-27","时间：2021-09-02"};
    /** 校园资讯适配内容源 */
    private static final String[] campusInformationIssueContents = new String[]{"对学校所征集到的151篇征文，进行评审，评出一等奖2名","各学院、部门：根据市普法办关于《进一步推进六盘水市普法责","六盘水师范学院地处有“中国凉都”之称的贵州省六盘水市","采用竞争性谈判方式进行采购，欢迎国内合格供应商前来递交响应文件","对学校所征集到的151篇征文，进行评审，评出一等奖2名","各学院、部门：根据市普法办关于《进一步推进六盘水市普法责","六盘水师范学院地处有“中国凉都”之称的贵州省六盘水市","采用竞争性谈判方式进行采购，欢迎国内合格供应商前来递交响应文件",
            "对学校所征集到的151篇征文，进行评审，评出一等奖2名","各学院、部门：根据市普法办关于《进一步推进六盘水市普法责","六盘水师范学院地处有“中国凉都”之称的贵州省六盘水市","采用竞争性谈判方式进行采购，欢迎国内合格供应商前来递交响应文件","对学校所征集到的151篇征文，进行评审，评出一等奖2名","各学院、部门：根据市普法办关于《进一步推进六盘水市普法责","六盘水师范学院地处有“中国凉都”之称的贵州省六盘水市","采用竞争性谈判方式进行采购，欢迎国内合格供应商前来递交响应文件"};
    /** 创建校园资讯实体类数据集合，循环遍历添加到ArrayList集合，为RecyclerView提供适配数据 */
    private ArrayList<CampusInformationBean> campusInformationBeanArrayList = new ArrayList<>();
    /** 自定义recyclerveiw的适配器 */
    private MyRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int bindLayout() {
        return R.layout.fragment_index_bottom_home;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        toolbar = getActivity().findViewById(R.id.appbar_constant_toolbar);//id获取Activity父控件Toolbar
        appBarLayout = getActivity().findViewById(R.id.appbar);//id获取Activity父控件AppBarLayout
        collapsing_toolbar_layout = getActivity().findViewById(R.id.collapsing_toolbar_layout);//id获取Activity父控件CollapsingToolbarLayout
        floating_action_btn = getActivity().findViewById(R.id.floating_action_btn);//id获取Activity父控件FloatingActionButton
        goTopNestedScrollView = rootView.findViewById(R.id.go_top_scrollview);//id获取当前Fragment自定义滑动控件
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        appBarLayout.addOnOffsetChangedListener(this);//监听Toolbar滑动渐变
        mAuto_grid_layout.setOnItemClickListener(this);//监听网格布局item
    }

    /**
     * 业务操作
     *
     * @param context
     */
    @Override
    protected void doBusiness(Context context) {
        imagePlay();//图片轮播
        gridLayout();//网格布局
        stickTop();//置顶
        setRecyclerView();//校园资讯列表
    }


    /**
     * 标题栏渐变
     * 滑动监听透明度及颜色设置Toolbar
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int toolbarHeight = appBarLayout.getTotalScrollRange();
        int dy = Math.abs(verticalOffset);
        if (dy <= toolbarHeight) {
            float scale = (float) dy / toolbarHeight;
            float alpha = scale * 255;
            toolbar.setBackgroundColor(Color.argb((int) alpha, 246, 105, 174));
            collapsing_toolbar_layout.setBackgroundColor(Color.argb((int) alpha, 246, 105, 174));
        }
    }


    /**
     * 网格布局设置Item监听
     *
     * @param position 当前点击网格坐标
     * @param view
     */
    @Override
    public void onItemClick(int position, View view) {
        switch (position) {
            case 0://六师官网
                Toast.makeText(getActivity(), "六师官网", Toast.LENGTH_SHORT).show();
                break;
            case 1://食堂代取
                doCanteenService();
                break;
            case 2://快递代取
                checkQQLogin();
                Toast.makeText(getActivity(), "快递代取", Toast.LENGTH_SHORT).show();
                //getUnionId();
                //IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), NaviCationActivity.class));//笑话百科
                break;
            case 3://跑腿认证
                startApplyRunCer();
                break;
            case 4://商家入驻
                startApplyShopCer();
                break;
            case 5://接单兼职
                Toast.makeText(getActivity(), "接单兼职", Toast.LENGTH_SHORT).show();
                break;
            case 6://告白墙
                Toast.makeText(getActivity(), "告白墙", Toast.LENGTH_SHORT).show();
                break;
            case 7://笑话百科
                Toast.makeText(getActivity(), "笑话百科", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 食堂代取
     */
    private void doCanteenService() {
        //Constant.mTencent.reportDAU();
        OkGo.<String>post(Constant.SA_TOKEN_CHECK_LOGIN)
                .tag("检查登录")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "请求信息中...");
                    }
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "当前账户已登录".equals(OkGoResponseBean.getMsg())) {
                            IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), RepastPracticeActivity.class));
                            OkGo.getInstance().cancelTag("检查登录");
                        } else {
                            CustomAlertDialogUtil.notification1(getActivity(), "温馨提示", "您还没有登录呀~", "朕知道了");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mGoTopScrollview, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 申请跑腿
     */
    private void startApplyRunCer() {
        OkGo.<String>post(Constant.SA_TOKEN_CHECK_LOGIN)
                .tag("检查登录")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "请求信息中...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "当前账户已登录".equals(OkGoResponseBean.getMsg())) {
                            IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), ApplyRunCerActivity.class));//执行动画跳转
                        } else {
                            CustomAlertDialogUtil.notification1(getActivity(), "温馨提示", "您还没有登录呀~", "朕知道了");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), goTopNestedScrollView, "请求错误，服务器连接失败！");
                    }
                });

    }

    /**
     * 商家入驻
     */
    private void startApplyShopCer() {
        OkGo.<String>post(Constant.SA_TOKEN_CHECK_LOGIN)
                .tag("检查登录")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(getActivity(), "请求信息中...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoResponseBean OkGoResponseBean = GsonUtil.gsonToBean(response.body(), OkGoResponseBean.class);
                        if (200 == OkGoResponseBean.getCode() && "true".equals(OkGoResponseBean.getData()) && "当前账户已登录".equals(OkGoResponseBean.getMsg())) {
                            IntentUtil.startActivityAnimLeftToRight(getActivity(), new Intent(getActivity(), ApplyShopCerActivity.class));//执行动画跳转
                        } else {
                            CustomAlertDialogUtil.notification1(getActivity(), "温馨提示", "您还没有登录呀~", "朕知道了");
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), goTopNestedScrollView, "请求错误，服务器连接失败！");
                    }
                });

    }

    /**
     * 首页图片轮播
     */
    private void imagePlay() {
//        BGALocalImageSize bgaLocalImageSize=new BGALocalImageSize(720,1280,320,640);
//        mBanner.setData(bgaLocalImageSize, ImageView.ScaleType.CENTER_CROP,
//                R.drawable.index_banner_img1,
//                R.drawable.index_banner_img2,
//                R.drawable.index_banner_img3);
        mLoop_layout.setLoop_ms(3000);//轮播的速度(毫秒)
        mLoop_layout.setLoop_duration(500);//滑动的速率(毫秒)
        mLoop_layout.setScaleAnimation(true);// 设置是否需要动画
        mLoop_layout.setLoop_style(LoopStyle.Empty);//轮播的样式-默认empty
        mLoop_layout.setIndicatorLocation(IndicatorLocation.Center);//指示器位置-中Center
        mLoop_layout.initializeData(getActivity());
        // 准备数据
        ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
        List<Object> bgList = new ArrayList<>();
        bannerInfos.add(new BannerInfo(R.drawable.index_banner_img2, "first"));
        bannerInfos.add(new BannerInfo(R.drawable.index_banner_img1, "second"));
        bgList.add(R.mipmap.banner_bg1);
        bgList.add(R.mipmap.banner_bg2);
        // 设置监听
        mLoop_layout.setOnLoadImageViewListener(new OnDefaultImageViewLoader() {
            @Override
            public void onLoadImageView(ImageView view, Object object) {
                Glide.with(view.getContext())
                        .load(object)
                        .into(view);
            }
        });
        mLoop_layout.setOnBannerItemClickListener(this);
        if (bannerInfos.size() == 0) {
            return;
        }
        mLoop_layout.setLoopData(bannerInfos);
        mBanner_bg_container.setBannerBackBg(getActivity(), bgList);
        mLoop_layout.setBannerBgContainer(mBanner_bg_container);
        mLoop_layout.startLoop();
    }

    @Override
    public void onBannerClick(int index, ArrayList<BannerInfo> banner) {
        switch (index) {
            case 0:
                Toast.makeText(getActivity(), "第一张图", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(getActivity(), "第二张图", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    /**
     * 首页网格布局
     */
    private void gridLayout() {
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
        mAuto_grid_layout.setAdapter(new FlowAdapter(Arrays.asList(Grid_Tv_Data)) {
            @Override
            public View getView(int position) {
                View item = mLayoutInflater.inflate(R.layout.fragment_index_homepage_grid_item, null);
                TextView tv_gird_tab = item.findViewById(R.id.tv_gird_tab);
                tv_gird_tab.setText(Grid_Tv_Data[position]);
                ImageView iv_grid_show = item.findViewById(R.id.iv_grid_show);
                iv_grid_show.setImageResource(Grid_Iv_Data[position]);
                return item;
            }
        });
    }

    /**
     * 首页点击浮动按钮置顶
     */
    private void stickTop() {
        //设置点击置顶的ImageView
        goTopNestedScrollView.setImageViewOnClickGoToFirst(floating_action_btn);
        //ScrollView滑动超过屏幕高度则显示置顶按钮,不设置的话就会使用自定义View中的默认高度
        DisplayMetrics metric = new DisplayMetrics();//获取屏幕高度
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        goTopNestedScrollView.setScreenHeight(metric.heightPixels);//设置高度
    }

    /**
     * 校园资讯列表适配数据源
     */
    private void setRecyclerView() {
        /** 循环遍历添加数组索引数据 */
        for (int i = 0; i < campusInformationTitles.length; i++) {
            CampusInformationBean informationBean = new CampusInformationBean();
            informationBean.setInfoTitle(campusInformationTitles[i]);//遍历添加资讯标题
            informationBean.setInfoSource(campusInformationSources[i]);//遍历添加资讯来源
            informationBean.setInfoIssueTime(campusInformationIssueTimes[i]);//遍历添加资讯发布时间
            informationBean.setInfoContent(campusInformationIssueContents[i]);//遍历添加资讯内容
            campusInformationBeanArrayList.add(informationBean);//遍历的数据全部添加到ArrayList集合中，提供RecyclerView适配器使用
        }
        //当我们确定Item的改变不会影响RecyclerView的宽高的时候可以设置setHasFixedSize(true)，
        // 并通过Adapter的增删改插方法去刷新RecyclerView，而不是通过notifyDataSetChanged()。
        // （当需要改变宽高的时候就用notifyDataSetChanged()去整体刷新一下）
        //获取LinearLayoutManager实例
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ////限制recyclerview自身滑动特性,滑动全部靠scrollview完成
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        //当平滑滚动条被禁用时，滚动条拇指的位置和大小是基于
        //只关注适配器中项目的数量以及内部可见项的位置
        //适配器。这提供了一个稳定的滚动条，用户可以通过一个项目列表进行导航
        //有不同的宽度/高度。
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        //创建adapter
        recyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), campusInformationBeanArrayList);
        //给RecyclerView设置adapter
        mRecyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
       mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.index_recyclerview_shape));
        mRecyclerView.addItemDecoration(divider);
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        recyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CampusInformationBean data) {
                switch (data.infoTitle){
                    case "庆祝中国共产党建党 100 周年“守初":
                        ToastUtil.showToast(campusInformationTitles[0]);
                        break;
                    case "六盘水师范学院2021年度普法责任清单":
                        ToastUtil.showToast(campusInformationTitles[1]);
                        break;
                    case "六盘水师范学院2022年高层次人才招聘":
                        ToastUtil.showToast(campusInformationTitles[2]);
                        break;
                    case "六盘水师范学院微格（录播）教室":
                        ToastUtil.showToast(campusInformationTitles[3]);
                        break;
                }
            }
        });
    }


    private void getUnionId() {
        if (Constant.mTencent != null && Constant.mTencent.isSessionValid()) {
            IUiListener listener = new DefaultUiListener() {
                @Override
                public void onError(UiError e) {
                    Toast.makeText(getActivity(), "onError", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onComplete(final Object response) {
                    if (response != null) {
                        JSONObject jsonObject = (JSONObject) response;
                        try {
                            String unionid = jsonObject.getString("unionid");
                            QQUtil.showResultDialog(getActivity(), "unionid:\n" + unionid, "onComplete");
                            QQUtil.dismissDialog();
                            Log.i(TAG, "getUnionId: ===" + unionid);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "no unionid", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "no unionid", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getActivity(), "onCancel", Toast.LENGTH_LONG).show();
                }
            };
            UnionInfo unionInfo = new UnionInfo(getActivity(), Constant.mTencent.getQQToken());
            unionInfo.getUnionId(listener);
        } else {
            Toast.makeText(getActivity(), "please login frist!", Toast.LENGTH_LONG).show();
        }
    }

    private void checkQQLogin() {
        Constant.mTencent.checkLogin(new DefaultUiListener() {
            @Override
            public void onComplete(Object response) {
                JSONObject jsonResp = (JSONObject) response;
                if (jsonResp.optInt("ret", -1) == 0) {
                    JSONObject jsonObject = Constant.mTencent.loadSession(Constant.APP_ID);
                    Constant.mTencent.initSessionCache(jsonObject);
                    if (jsonObject == null) {
                        QQUtil.showResultDialog(getActivity(), "jsonObject is null", "登录失败");
                    } else {
                        QQUtil.showResultDialog(getActivity(), jsonObject.toString(), "登录成功");
                    }

                } else {
                    QQUtil.showResultDialog(getActivity(), "token过期，请调用登录接口拉起手Q授权登录", "登录失败");
                }
            }

            @Override
            public void onError(UiError e) {
                QQUtil.showResultDialog(getActivity(), "token过期，请调用登录接口拉起手Q授权登录", "登录失败");
            }

            @Override
            public void onCancel() {
                QQUtil.toastMessage(getActivity(), "onCancel");
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }
}
