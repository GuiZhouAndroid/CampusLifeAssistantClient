package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.statelayout.MultipleStatusView;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoShopStoreBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.IntentUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/12
 * 描述：我的店铺
 *
 * @author ZSAndroid
 * @create 2021-12-12-12:02
 */
@SuppressLint("NonConstantResourceId")
public class MyShopActivity extends BaseActivity {

    private static final String TAG = "MyShopActivity";
    /** 父布局*/
    @BindView(R2.id.rl_my_shop_info) RelativeLayout mRlMyShopInfo;
    /** 状态控件 */
    @BindView(R.id.my_shop_status_view) MultipleStatusView mMyShopStatusView;
    /** Android振动器 */
    private Vibrator vibrator;

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
        return R.layout.activity_my_shop;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {

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
        initNowUserShopStoreInfo();//初始化当前用户店铺信息
    }

    /**
     * Android振动器
     */
    private void startVibrator() {
        if (vibrator == null)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    /**
     * 初始化当前用户店铺信息
     */
    private void initNowUserShopStoreInfo() {
        OkGo.<String>post(Constant.SHOP_SELECT_STORE_INFO_BY_SA_TOKEN)
                .tag("当前用户店铺信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        mMyShopStatusView.showLoading();
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoShopStoreBean okGoShopStoreBean = GsonUtil.gsonToBean(response.body(), OkGoShopStoreBean.class);
                        if (200 == okGoShopStoreBean.getCode() && okGoShopStoreBean.getData() == null && "无商铺信息".equals(okGoShopStoreBean.getMsg())) {
                            notShopStoreInfo();//无商铺信息
                            return;
                        }
                        if (200 == okGoShopStoreBean.getCode() && okGoShopStoreBean.getData() != null && "有商铺信息".equals(okGoShopStoreBean.getMsg())) {
                            haveShopStoreInfo(okGoShopStoreBean.getData());//有商铺信息
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        OkGoErrorUtil.CustomFragmentOkGoError(response, MyShopActivity.this, mRlMyShopInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 无商铺信息
     */
    private void notShopStoreInfo() {
        //加载状态当前View自定义空布局
        mMyShopStatusView.showEmpty(R.layout.custom_my_shop_empty_layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //子View的自定义空布局
        MultipleStatusView mMultipleEmptyMyShopStatusView = mMyShopStatusView.getEmptyView().findViewById(R.id.multiple_empty_my_shop_status_view);
        mMultipleEmptyMyShopStatusView.showEmpty();

        //子View中父布局view + 父布局点击事件
        RelativeLayout relativeLayout = mMultipleEmptyMyShopStatusView.getEmptyView().findViewById(R.id.rl_empty_my_shop_info);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivityAnimLeftToRight(MyShopActivity.this, new Intent(MyShopActivity.this, CreateStoreActivity.class));//执行动画跳转
                finish();
            }
        });
    }

    /**
     * 有商铺信息
     *
     * @param data 商铺实体信息
     */
    private void haveShopStoreInfo(OkGoShopStoreBean.Data data) {
        ToastUtils.show("有信息呀" + data.toString());
    }
}
