package work.lpssfxy.www.campuslifeassistantclient.base.custompopup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.impl.FullScreenPopupView;

import work.lpssfxy.www.campuslifeassistantclient.R;

/**
 * created by on 2021/11/29
 * 描述：全屏底部弹窗--->查看用户详情
 *
 * @author ZSAndroid
 * @create 2021-11-29-14:24
 */

public class UserInfoFullPopup extends FullScreenPopupView {

    public UserInfoFullPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.activity_apply_commit_car_number_fullscreen_popup;
    }

}
