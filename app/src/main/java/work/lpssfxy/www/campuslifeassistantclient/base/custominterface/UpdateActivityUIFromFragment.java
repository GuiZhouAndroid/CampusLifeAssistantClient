package work.lpssfxy.www.campuslifeassistantclient.base.custominterface;

import work.lpssfxy.www.campuslifeassistantclient.entity.dto.UserAddressInfoBean;

/**
 * created by on 2021/12/11
 * 描述：Fragment更新Activity的UI或传输数据
 *
 * @author ZSAndroid
 * @create 2021-12-11-9:53
 */
public interface UpdateActivityUIFromFragment {

    void doSetAddressData(UserAddressInfoBean userAddressInfoBean);

    void doUpdateAddressData(UserAddressInfoBean userAddressInfoBean,int position);
}
