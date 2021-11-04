package work.lpssfxy.www.campuslifeassistantclient.view.fragment.bottom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;
import work.lpssfxy.www.campuslifeassistantclient.base.index.ItemView;
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

@SuppressLint("NonConstantResourceId")
public class BottomMineFragment extends BaseFragment {
    private IndexActivity indexActivity;
    private static final String TAG = "BottomMineFragment";


    /** 原生View布局 */
    @BindView(R2.id.qq_head) ImageView mQQHead;
    @BindView(R2.id.qq_back) ImageView mQQBack;
    @BindView(R2.id.qq_province) TextView mQQProvince;
    @BindView(R2.id.qq_city) TextView mQQCity;

    /** item控件--->用户信息 */
    @BindView(R2.id.qq_nickname) ItemView mQQNickName;//用户名
    @BindView(R2.id.ll_username) ItemView mUserName;//用户名
    @BindView(R2.id.ll_sex) ItemView mSex;//性别
    @BindView(R2.id.ll_realname) ItemView mRealName;//真实姓名
    @BindView(R2.id.ll_idcard) ItemView mIdCard;//身份证号
    @BindView(R2.id.ll_stuno) ItemView mStuNo;//学号
    @BindView(R2.id.ll_tel) ItemView mTel;//手机号
    @BindView(R2.id.ll_email) ItemView mEmail;//QQ邮箱
    @BindView(R2.id.ll_dept) ItemView mDept;//所属院系
    @BindView(R2.id.ll_class) ItemView mClass;//专业班级
    @BindView(R2.id.ll_create_time) ItemView mCreateTime;//账户注册时间
    @BindView(R2.id.ll_update_time) ItemView mUpdateTime;//账户更新时间

    // 接收MainActivity发送消息，匹配消息what值(消息标记)
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1://匹配成功，获取IndexActivity个人用户信息
                    Constant.userInfo = (UserQQSessionBean.Data.UserInfo) msg.obj;
                    mUserName.setRightDesc(Constant.userInfo.getUlUsername());
                    mSex.setRightDesc(Constant.userInfo.getUlSex());
                    mRealName.setRightDesc(Constant.userInfo.getUlRealname());
                    mIdCard.setRightDesc(Constant.userInfo.getUlIdcard());
                    mStuNo.setRightDesc(Constant.userInfo.getUlStuno());
                    mTel.setRightDesc(Constant.userInfo.getUlTel());
                    mEmail.setRightDesc(Constant.userInfo.getUlEmail());
                    mDept.setRightDesc(Constant.userInfo.getUlDept());
                    mClass.setRightDesc(Constant.userInfo.getUlClass());
                    mCreateTime.setRightDesc(Constant.userInfo.getCreateTime());
                    mUpdateTime.setRightDesc(Constant.userInfo.getUpdateTime());
                    break;
                case 2://匹配成功，获取IndexActivity登录QQ用户信息
                    Constant.qqUser = (QQUserBean) msg.obj;
                    mQQProvince.setText(Constant.qqUser.getProvince() + "省");//设置QQ信息的省份
                    mQQCity.setText(Constant.qqUser.getCity());//设置QQ信息的城市
                    mQQNickName.setRightDesc(Constant.qqUser.getNickname());//设置QQ信息的城市
                    //设置圆形图像
                    RequestOptions options = new RequestOptions();
                    options.circleCrop();
                    Glide.with(getActivity())
                            .load(Constant.qqUser.getFigureurl_qq_2())
                            .apply(options)
                            .into(mQQHead);
                    //设置背景高斯模糊效果
                    Glide.with(getActivity()).load(Constant.qqUser.getFigureurl_qq_2())
                            .transform(new BlurTransformation(20, 3))
                            .into(mQQBack);
                    break;
                case 3://匹配成功，接收IndexActivity发来的空消息
//                    tv_username.setText("无数据");
//                    tv_sex.setText("无数据");
                    break;
            }
        }

        ;
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
//        mNickName.setItemClickListener(new ItemView.itemClickListener() {
//            @Override
//            public void itemClick(String text) {
//                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
//        mSex.setItemClickListener(new ItemView.itemClickListener() {
//            @Override
//            public void itemClick(String text) {
//                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        managerMyUserInfo();
    }

    @Override
    protected void doBusiness(Context context) {

    }


    @Override
    public void onClick(View view) {

    }

    public void managerMyUserInfo() {
        mQQNickName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mUserName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mRealName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mIdCard.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mStuNo.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mTel.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mEmail.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mDept.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mClass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mCreateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
        mUpdateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.get(getActivity()).clearMemory();
        Glide.get(getActivity()).clearDiskCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(getActivity()).clearMemory();
        Glide.get(getActivity()).clearDiskCache();
    }

}
