package work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.button.ButtonView;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.edit.PowerfulEditText;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.BaseFragment;

/**
 * created by on 2021/11/25
 * 描述：开发者通过角色编号查询拥有该角色全部用户的信息
 *
 * @author ZSAndroid
 * @create 2021-11-25-23:40
 */
@SuppressLint("NonConstantResourceId")
public class DeveloperSelectHaveRoleUserInfoByRoleIdFragment extends BaseFragment {

    private static final String TAG = "DeveloperBannedAccountRealNameFragment";
    //父布局
    @BindView(R2.id.rl_dev_role_user_info_role_id) RelativeLayout mRlDevRoleUserInfoRoleId;
    //查询角色编号
    @BindView(R2.id.edit_dev_role_user_info_role_id) PowerfulEditText mEditDevRoleUserInfoRoleId;
    //开始查询
    @BindView(R2.id.btn_dev_role_user_info_role_id) ButtonView mBtnDevRoleUserInfoRoleId;
    /* RecyclerView列表 */
    @BindView(R2.id.recyclerView_role_user_info_role_id) RecyclerView mRecyclerViewRoleUserInfoRoleId;

    /**
     * @return 单例对象
     */
    public static DeveloperSelectHaveRoleUserInfoByRoleIdFragment newInstance() {
        return new DeveloperSelectHaveRoleUserInfoByRoleIdFragment();
    }

    @Override
    protected int bindLayout() {
        return R.layout.developer_fragment_select_have_role_user_info_by_role_id;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {

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
    public boolean onBackPressed() {
        return false;
    }
}
