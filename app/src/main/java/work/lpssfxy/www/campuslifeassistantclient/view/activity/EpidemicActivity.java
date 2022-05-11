package work.lpssfxy.www.campuslifeassistantclient.view.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.entity.EpidemicBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;
import work.lpssfxy.www.campuslifeassistantclient.utils.statusbarutils.StatusBarUtils;
import work.lpssfxy.www.campuslifeassistantclient.view.BaseActivity;

/**
 * created by on 2022/5/11
 * 描述：全国新冠疫情
 *
 * @author ZSAndroid
 * @create 2022-05-11-10:01
 */
@SuppressLint("NonConstantResourceId")
public class EpidemicActivity extends BaseActivity {

    @BindView(R2.id.tv_xl_api) TextView mTvXlApi;
    @BindView(R2.id.tv_xl_times) TextView mTvXlTimes;//数据更新截止时间
    @BindView(R2.id.tv_xl_gntotal) TextView mTvXlGntotal;//累积确诊
    @BindView(R2.id.tv_xl_deathtotal) TextView mTvXlDeathtotal;//累积死亡
    @BindView(R2.id.tv_xl_curetotal) TextView mTvXlCuretotal;//累积治愈

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
        return R.layout.activity_epidemic;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        StatusBarUtils.setStatusBarSingleColor(this, ContextCompat.getColor(this, R.color.Blue),1f);
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
        OkGo.<String>get("https://interface.sina.cn/news/wap/fymap2020_data.d.json")
                .tag("新浪疫情")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(EpidemicActivity.this, "请求信息中...");
                    }
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Response<String> response) {
                        EpidemicBean epidemicBean = GsonUtil.gsonToBean(response.body(), EpidemicBean.class);


                        EpidemicBean.Data data = epidemicBean.getData();
                        mTvXlTimes.setText(data.getTimes()+"数据统计");
                        mTvXlGntotal.setText(data.getGntotal()+"人");
                        mTvXlDeathtotal.setText(data.getDeathtotal()+"人");
                        mTvXlCuretotal.setText(data.getCuretotal()+"人");
                        mTvXlApi.setText(data.toString());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }
                });
    }
}
