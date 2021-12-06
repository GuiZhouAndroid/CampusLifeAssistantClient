package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.os.Bundle;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/12/5
 * 描述：申请认证跑腿信息提交
 *
 * @author ZSAndroid
 * @create 2021-12-05-14:35
 */
public class ApplyRunCommitActivity extends BaseActivity {

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
        return R.layout.activity_apply_commit;
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
