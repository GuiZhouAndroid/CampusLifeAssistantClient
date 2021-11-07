package work.lpssfxy.www.campuslifeassistantclient.base.interfacedata;

import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;

/**
 * created by on 2021/11/3
 * 描述：定义 Activity 传递数据到 Fragment时 所有 activity 必须实现公共接口代方法
 *
 * @author ZSAndroid
 * @create 2021-11-03-17:24
 */
public interface FragmentInteraction {
    void userInfoInfoPutMineMineActivity(UserQQSessionBean.Data.UserInfo userInfo);
}
