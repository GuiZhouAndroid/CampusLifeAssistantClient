package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.pogress.DialProgress;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoSessionAndUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserCerBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.XToastUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.okhttp.OkGoErrorUtil;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2021/12/2
 * 描述：用户已实名认证，查看实名详情
 *
 * @author ZSAndroid
 * @create 2021-12-02-22:32
 */

@SuppressLint("NonConstantResourceId")
public class UserCerSelectIdCardActivity extends BaseActivity {

    @BindView(R2.id.dial_progress_bar) DialProgress mDialProgress;
    @BindView(R2.id.tv_cer_name) TextView mTvCerName;
    @BindView(R2.id.tv_cer_nation) TextView mTvCerNation;
    @BindView(R2.id.tv_cer_birth) TextView mTvCerBirth;
    @BindView(R2.id.tv_cer_card) TextView mTvCerCard;
    @BindView(R2.id.tv_cer_address) TextView mTvCerAddress;
    @BindView(R2.id.tv_cer_organization) TextView mTvCerOrganization;
    @BindView(R2.id.tv_cer_valid) TextView mTvCerValid;
    @BindView(R2.id.tv_cer_create) TextView mTvCerCreate;

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
        return R.layout.activity_user_cer_select_idcard;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        mDialProgress.setValue(100);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        mDialProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XToastUtils.success("欢迎成为校园帮APP认证用户");
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {
        initUserNowCerInfo();
    }

    /**
     * 初始化用户当前实名信息
     */
    private void initUserNowCerInfo() {
        OkGo.<String>post(Constant.SELECT_NOW_CER_ALL_INFO_BY_SA_TOKEN_LOGIN_REAL_NAME)
                .tag("查询当前用户实名状态")
                .execute(new StringCallback() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoUserCerBean okGoUserCerBean = GsonUtil.gsonToBean(response.body(), OkGoUserCerBean.class);
                        if (200 == okGoUserCerBean.getCode() && null != okGoUserCerBean.getData() && "success".equals(okGoUserCerBean.getMsg())) {
                            XToastUtils.success("实名信息加载完成");
                            mTvCerName.setText("姓名："+okGoUserCerBean.getData().getUlRealname());
                            mTvCerNation.setText("民族："+okGoUserCerBean.getData().getUserCertificationBean().getTucNation());
                            mTvCerBirth.setText("出生日期："+okGoUserCerBean.getData().getUserCertificationBean().getTucBirth());
                            mTvCerCard.setText("身份证号："+okGoUserCerBean.getData().getUserCertificationBean().getTucIdcard().replaceAll("(\\d{4})\\d{8}(\\w{6})", "$1*****$2"));
                            mTvCerAddress.setText("家庭住址："+okGoUserCerBean.getData().getUserCertificationBean().getTucAddress());
                            mTvCerOrganization.setText("签发机关："+okGoUserCerBean.getData().getUserCertificationBean().getTucOrganization());
                            mTvCerValid.setText("有效期限："+okGoUserCerBean.getData().getUserCertificationBean().getTucValidPeriod());
                            mTvCerCreate.setText("认证时间："+okGoUserCerBean.getData().getUserCertificationBean().getCreateTime());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        XToastUtils.error("请求错误，服务器连接失败");
                    }
                });
    }
}
