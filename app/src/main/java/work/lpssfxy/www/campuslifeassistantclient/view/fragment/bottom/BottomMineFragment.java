package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;
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

    TextView tv_username, tv_sex, tv_realname, tv_idcard, tv_stuno, tv_tel, tv_email, tv_class, tv_dept, tv_create_time, tv_update_time, tv_qq_info;

    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1://匹配成功，获取IndexActivity个人用户信息
                    Constant.userInfo = (UserQQSessionBean.Data.UserInfo)msg.obj;
                    tv_username.setText(Constant.userInfo.toString());
                    break;
                case 2://匹配成功，获取IndexActivity登录QQ用户信息
                    Constant.qqUser = (QQUserBean) msg.obj;
                    tv_sex.setText(Constant.qqUser.toString());
                    break;
                case 3://匹配成功，接收IndexActivity发来的空消息
                    tv_username.setText("无数据");
                    tv_sex.setText("无数据");
                    break;
            }
        };
    };


    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        IndexActivity mIndexActivity = (IndexActivity) activity;
        //通过强转成宿主activity，就可以获取到传递过来的数据
//        titles = mIndexActivity.getTitles();
        mIndexActivity.setHandler(mHandler);
    }

    @Override
    protected int bindLayout() {
        return R.layout.index_fragment_bottom_mine;
    }

    @Override
    protected void prepareData(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(View rootView) {
        tv_username = rootView.findViewById(R.id.tv_username);
        tv_sex = rootView.findViewById(R.id.tv_sex);
        tv_realname = rootView.findViewById(R.id.tv_realname);
        tv_idcard = rootView.findViewById(R.id.tv_idcard);
        tv_stuno = rootView.findViewById(R.id.tv_stuno);
        tv_tel = rootView.findViewById(R.id.tv_tel);
        tv_email = rootView.findViewById(R.id.tv_email);
        tv_class = rootView.findViewById(R.id.tv_class);
        tv_dept = rootView.findViewById(R.id.tv_dept);
        tv_create_time = rootView.findViewById(R.id.tv_create_time);
        tv_update_time = rootView.findViewById(R.id.tv_update_time);
        tv_qq_info = rootView.findViewById(R.id.tv_qq_info);
        tv_qq_info.setText("asdasd");
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
    public void onClick(View view) {

    }
}
