package work.lpssfxy.www.campuslifeassistantclient.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import butterknife.BindView;
import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.R2;
import work.lpssfxy.www.campuslifeassistantclient.base.ParcelableData;
import work.lpssfxy.www.campuslifeassistantclient.base.index.ItemView;

/**
 * created by on 2021/11/6
 * 描述：个人信息
 *
 * @author ZSAndroid
 * @create 2021-11-06-17:12
 */
@SuppressLint("NonConstantResourceId")
public class MineInfoActivity extends BaseActivity  {


    /** item控件--->用户信息 */
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
                    ParcelableData parcelableData = (ParcelableData) msg.obj;
                    if (parcelableData !=null){
                        mUserName.setRightDesc(parcelableData.getUlUsername());
                        mSex.setRightDesc(parcelableData.getUlSex());
                        mRealName.setRightDesc(parcelableData.getUlRealname());
                        mIdCard.setRightDesc(parcelableData.getUlIdcard());
                        mStuNo.setRightDesc(parcelableData.getUlStuno());
                        mTel.setRightDesc(parcelableData.getUlTel());
                        mEmail.setRightDesc(parcelableData.getUlEmail());
                        mDept.setRightDesc(parcelableData.getUlDept());
                        mClass.setRightDesc(parcelableData.getUlClass());
                        mCreateTime.setRightDesc(parcelableData.getCreateTime());
                        mUpdateTime.setRightDesc(parcelableData.getUpdateTime());
                    }
                    break;
                case 2://匹配成功，接收IndexActivity发来的空消息--->未登录设置默认值
                    mUserName.setRightDesc(getString(R.string.notLogin));
                    mSex.setRightDesc(getString(R.string.notLogin));
                    mRealName.setRightDesc(getString(R.string.notLogin));
                    mIdCard.setRightDesc(getString(R.string.notLogin));
                    mStuNo.setRightDesc(getString(R.string.notLogin));
                    mTel.setRightDesc(getString(R.string.notLogin));
                    mEmail.setRightDesc(getString(R.string.notLogin));
                    mDept.setRightDesc(getString(R.string.notLogin));
                    mClass.setRightDesc(getString(R.string.notLogin));
                    mCreateTime.setRightDesc(getString(R.string.notLogin));
                    mUpdateTime.setRightDesc(getString(R.string.notLogin));
                    break;
            }
        }
    };

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
        return true;
    }

    @Override
    protected Boolean isSetBottomNaviCationColor() {
        return true;
    }

    @Override
    protected Boolean isSetImmersiveFullScreen() {
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //GlobalBus.getBus().register(this);
        MineInfoActivity activity=(MineInfoActivity)this;
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_mine_info;
    }

    @Override
    protected void prepareData() {
        //GlobalBus.getBus().register(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        //String testBundleString = bundle.getString("userInfo");
        if (bundle!=null){
            ParcelableData parcelableData = bundle.getParcelable("userInfo");
            Log.i("用户信息=", parcelableData.toString());
            Message message = new Message();
            message.obj = parcelableData;
            message.what = 1;
            mHandler.sendMessage(message);
        }else {
            mHandler.sendEmptyMessage(2);
        }
    }

    @Override
    protected void initEvent() {
        managerMyUserInfo();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void doBusiness() {

    }

    public void managerMyUserInfo() {

        mUserName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mRealName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mIdCard.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mStuNo.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mTel.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mEmail.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mDept.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mClass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mCreateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        mUpdateTime.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(MineInfoActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 暂停时：注销EventBus
     */
    @Override
    protected void onStop() {
        super.onStop();
        //GlobalBus.getBus().unregister(this);
    }
    /**
     * 销毁时：注销EventBus
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //GlobalBus.getBus().unregister(this);
    }

}
