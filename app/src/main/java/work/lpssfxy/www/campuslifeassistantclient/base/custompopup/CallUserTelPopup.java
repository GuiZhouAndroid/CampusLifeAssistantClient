package work.lpssfxy.www.campuslifeassistantclient.base.custompopup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BubbleAttachPopupView;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.utils.XXPermissionsAction;

/**
 * created by on 2021/11/30
 * 描述：自定义气泡Attach弹窗--->打电话
 *
 * @author ZSAndroid
 * @create 2021-11-30-11:07
 */

public class CallUserTelPopup extends BubbleAttachPopupView {

    private String strRealName; //姓名
    private String strPhoneNumber; //手机号

    public CallUserTelPopup(@NonNull Context context) {
        super(context);
    }

    public CallUserTelPopup(@NonNull Context context, String phoneNumber, String realName) {
        super(context);
        this.strRealName = realName;
        this.strPhoneNumber = phoneNumber;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_bubble_attach_call_phone_popup;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate() {
        super.onCreate();
        final TextView mTvCallPhoneToUser = findViewById(R.id.tv_call_phone_to_user);
        //显示联系信息
        mTvCallPhoneToUser.setText("点我联系" + strRealName + "同学，手机号：" + strPhoneNumber);
        //点击拨打电话联系
        mTvCallPhoneToUser.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                XXPermissionsAction.getInstance().callPhone(getContext(), strPhoneNumber);
                dismiss();
            }
        });

    }
}
