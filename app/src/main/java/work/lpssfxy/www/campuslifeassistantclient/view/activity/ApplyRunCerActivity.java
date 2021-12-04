package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/12/4
 * 描述：申请跑腿资格
 *
 * @author ZSAndroid
 * @create 2021-12-04-20:38
 */
public class ApplyRunCerActivity extends BaseActivity{
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

    }
}
