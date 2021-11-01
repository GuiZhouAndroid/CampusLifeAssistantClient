package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.IndexActivity;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;


/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:01
 */
public class BottomMineFragment extends BaseFragment {
    private IndexActivity indexActivity;
    private static final String TAG = "BottomMineFragment";

    TextView tv_username,tv_sex,tv_realname,tv_idcard,tv_stuno,tv_tel,tv_email,tv_class,tv_dept,tv_create_time,tv_update_time,tv_qq_info;

    @Override
    protected int bindLayout() {
        return R.layout.index_fragment_bottom_mine;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        tv_username =rootView.findViewById(R.id.tv_username);
        tv_sex =rootView.findViewById(R.id.tv_sex);
        tv_realname =rootView.findViewById(R.id.tv_realname);
        tv_idcard =rootView.findViewById(R.id.tv_idcard);
        tv_stuno =rootView.findViewById(R.id.tv_stuno);
        tv_tel =rootView.findViewById(R.id.tv_tel);
        tv_email =rootView.findViewById(R.id.tv_email);
        tv_class =rootView.findViewById(R.id.tv_class);
        tv_dept =rootView.findViewById(R.id.tv_dept);
        tv_create_time =rootView.findViewById(R.id.tv_create_time);
        tv_update_time =rootView.findViewById(R.id.tv_update_time);
        tv_qq_info =rootView.findViewById(R.id.tv_qq_info);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void doBusiness(Context context) {

    }

    @Override
    public void onAttach(@NonNull Context ctx) {
        super.onAttach(ctx);
    }

    @Override
    public void onClick(View view) {

    }
}
