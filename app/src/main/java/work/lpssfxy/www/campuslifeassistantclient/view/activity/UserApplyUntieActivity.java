package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/10/29
 * 描述：用户申请解封账户--->联系本人开发者
 *
 * @author ZSAndroid
 * @create 2021-10-29-12:05
 */

public class UserApplyUntieActivity extends BaseActivity {

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
        return R.layout.activity_user_apply_untie;
    }

    @Override
    protected void prepareData() {

    }

    @Override
    protected void initView() {
        Toast.makeText(this, getIntent().getStringExtra("UntieBannedName"), Toast.LENGTH_LONG).show();
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
