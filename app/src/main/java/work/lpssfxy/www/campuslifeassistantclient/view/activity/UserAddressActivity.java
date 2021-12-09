package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseRoleUserInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.adapter.BaseUserAddressInfoAdapter;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoAllAddressInfoBean;
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
public class UserAddressActivity extends BaseActivity {

    private static final String TAG = "UserAddressActivity";
    /** 返回 */
    @BindView(R2.id.iv_address_back) ImageView mIvAddressBack;
    /** 收货地址内容适配器 */
    @BindView(R2.id.ryc_address) RecyclerView mRycAddress;

    /* 收货地址信息列表适配器 */
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
        startSelectUserAddressAllInfo();//查询当前登录用户全部的收货地址
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
            Log.i(TAG, "doResponseJsonData: " + userAddressInfoBeanList);
            //绑定item布局文件，携带收货地址对象集合数据，传递此数据开始适配
            userAddressInfoAdapter = new BaseUserAddressInfoAdapter(R.layout.activity_user_address_recycler_view_item, userAddressInfoBeanList);
            //为列表控件设置适配器，并为执行适配操作
            mRycAddress.setAdapter(userAddressInfoAdapter);
        }
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
                        MyXPopupUtils.getInstance().setShowDialog(UserAddressActivity.this,"正在加载收货地址...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        XToastUtils.success("加载完成");
                        doResponseJsonData(response);
                    }
                });
    }
}
