package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.rmondjone.locktableview.DisplayUtil;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.textview.MarqueeTextView;

import butterknife.BindView;
import butterknife.OnClick;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/12/1
 * 描述：超管查询实名认证全部信息
 *
 * @author ZSAndroid
 * @create 2021-12-01-22:57
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperSelectAllCertificationInfoFragment extends BaseFragment {

    private static final String TAG = "DeveloperSelectAllCertificationInfoFragment";

    /* 父布局 */
    @BindView(R2.id.ll_dev_select_all_certification_info) LinearLayout mLlDevSelectAllCertificationInfo;
    /* XUI按钮搜索用户 */
    @BindView(R2.id.btn_search_certification_info) ButtonView mBtnSearchCertificationInfo;
    /* 填充表格视图 */
    @BindView(R2.id.ll_certification_info_table_content_view) LinearLayout mLlCertificationInfoTableContentView;
    //跑马灯滚动显示用户总条数
    @BindView(R2.id.mtv_count_certification_number) MarqueeTextView mMtvCountCertificationNumber;
    //顶部标题数组
    private String[] topTitleArrays;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectAllCertificationInfoFragment newInstance() {
        return new DeveloperSelectAllCertificationInfoFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_all_certification_info;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {
        //准备表的顶部标题数据
        topTitleArrays = new String[]{"实名ID", "用户ID", "身份证号", "实名状态"};
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
        //开始查询实名认证信息
        startSelectAllCertificationInfo(context);
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
    @OnClick({R2.id.btn_search_certification_info})
    public void onSelectAllCertificationInfoViewClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_certification_info://确定搜索
                startSearchCertificationInfo();
                break;
        }
    }

    /**
     * 弹出用户信息搜索方式选择框
     */
    private void startSearchCertificationInfo() {
        new XPopup.Builder(getContext())
                .maxHeight(800)
                .isDarkTheme(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCenterList("选择搜索信息方式", new String[]{"用户ID", "用户名", "真实姓名"}, new OnSelectListener() {
                    @Override
                    public void onSelect(int position, String searchMode) {
                        switch (position) {
                            case 0:
                                //chooseUserId(); //选择用户ID
                                break;
                            case 1:
                                //chooseUserName();//选择用户名
                                break;
                            case 2:
                                //chooseRealName();//选择真实姓名
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * OkGo网络请求开始调用API接口开始查询全部实名认证信息
     *
     * @param context 上下文
     */
    private void startSelectAllCertificationInfo(Context context) {
        //开始网络请求，访问后端服务器，执行查询角色操作
        OkGo.<String>post(Constant.ADMIN_SELECT_ALL_USER_CER_INFO)
                .tag("查询全部实名信息")
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n") // I18代表国际化,带有占位符的资源字符串
                    @Override
                    public void onSuccess(Response<String> response) {
                        starSetTabData(response);//Json字符串Gson解析使用，绘制表格
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        OkGoErrorUtil.CustomFragmentOkGoError(response, getActivity(), mLlDevSelectAllCertificationInfo, "请求错误，服务器连接失败！");
                    }
                });
    }

    /**
     * 开始设置表数据
     *
     * @param response okGo响应返回的Json字符串
     */
    private void starSetTabData(Response<String> response) {

    }


}
