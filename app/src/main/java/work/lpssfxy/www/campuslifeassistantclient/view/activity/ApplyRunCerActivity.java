package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.xuexiang.xui.widget.statelayout.StatefulLayout;

import java.util.List;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoApplyRunBean;
import work.lpssfxy.www.campuslifeassistantclient.utils.MyXPopupUtils;
import work.lpssfxy.www.campuslifeassistantclient.utils.gson.GsonUtil;

/**
 * created by on 2021/12/4
 * 描述：申请跑腿资格
 *
 * @author ZSAndroid
 * @create 2021-12-04-20:38
 */
@SuppressLint("NonConstantResourceId")
public class ApplyRunCerActivity extends BaseActivity {


    @BindView(R.id.ll_stateful) StatefulLayout mStatefulLayout;
    private static final String TAG = "ApplyRunCerActivity";

    //多图片选择路径List集合
    public static List<String> imgPathList;
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
        return R.layout.activity_apply_run_cer;
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
        initNowUserApplyRunInfo();
    }

    /**
     * 初始化当前用户跑腿认证信息
     */
    private void initNowUserApplyRunInfo() {
//        OkGo.<String>post(Constant.USER_DO_APPLY_RUN_BY_MY_INFO)
//                .tag("申请跑腿认证")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onStart(Request<String, ? extends Request> request) {
//                        MyXPopupUtils.getInstance().setShowDialog(ApplyRunCerActivity.this, "正在请求认证信息...");
//                    }
//
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        OkGoApplyRunBean okGoApplyRunBean = GsonUtil.gsonToBean(response.body(), OkGoApplyRunBean.class);
//                        Log.i("okGoApplyRunBean", okGoApplyRunBean.toString());
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "跑腿认证审核中".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "跑腿认证已审核".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "跑腿认证审核失败".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "查询失败，用户不存在".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "跑腿信息提交失败".equals(okGoApplyRunBean.getMsg())) {
//                            return;
//                        }
//                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "跑腿信息提交成功".equals(okGoApplyRunBean.getMsg())) {
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        MyXPopupUtils.getInstance().setSmartDisDialog();
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        //OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyRunCerActivity.this, goTopNestedScrollView, "请求错误，服务器连接失败！");
//                    }
//                });

        OkGo.<String>post(Constant.USER_SELECT_APPLY_RUN_BY_SA_TOKEN_SESSION)
                .tag("当前用户申请信息")
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        MyXPopupUtils.getInstance().setShowDialog(ApplyRunCerActivity.this, "正在请求认证信息...");
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        OkGoApplyRunBean okGoApplyRunBean = GsonUtil.gsonToBean(response.body(), OkGoApplyRunBean.class);
                        Log.i("okGoApplyRunBean", okGoApplyRunBean.toString());
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "查询失败，用户不存在".equals(okGoApplyRunBean.getMsg())) {
                            return;
                        }
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() == null && "无申请信息".equals(okGoApplyRunBean.getMsg())) {
                            mStatefulLayout.showEmpty();
                            return;
                        }
                        if (200 == okGoApplyRunBean.getCode() && okGoApplyRunBean.getData() != null && "有历史申请信息".equals(okGoApplyRunBean.getMsg())) {
                            Log.i("有历史申请信息", okGoApplyRunBean.getData().toString());
                            mStatefulLayout.showContent();
                        }
                    }

                    @Override
                    public void onFinish() {
                        MyXPopupUtils.getInstance().setSmartDisDialog();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        //OkGoErrorUtil.CustomFragmentOkGoError(response, ApplyRunCerActivity.this, goTopNestedScrollView, "请求错误，服务器连接失败！");
                    }
                });
    }
}
