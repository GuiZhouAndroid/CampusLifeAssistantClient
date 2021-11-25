package work.lpssfxy.www.campuslifeassistantclient.base.custominterface;

import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoSessionAndUserBean;

/**
 * created by on 2021/11/3
 * 描述：定义 Activity 传递数据到 Fragment时 所有 activity 必须实现公共接口代方法
 *
 * @author ZSAndroid
 * @create 2021-11-03-17:24
 */
public interface FragmentInteraction {
    void userInfoInfoPutMineMineActivity(OkGoSessionAndUserBean.Data.UserInfo userInfo);
}
