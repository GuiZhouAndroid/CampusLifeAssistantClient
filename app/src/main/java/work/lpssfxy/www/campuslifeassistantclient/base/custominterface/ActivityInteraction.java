package work.lpssfxy.www.campuslifeassistantclient.base.custominterface;

import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoSessionAndUserBean;

/**
 * created by on 2021/11/6
 * 描述：Activity中携带的MySQL用户数据,接口回调方式传递到宿主Activity的子Fragment中
 *
 * @author ZSAndroid
 * @create 2021-11-06-13:33
 */
public interface ActivityInteraction {
    // 登录时网络请求拉取的用户数据(从持久化文件取出)，传递到ViewPager中的MineFragment
    // 因为是接口回调，必须在MinFragment的onCreate()方法中获取，而ViewPager使用自定义适配器时，只填充适配一次
    // 并且Fragment 生命周期未replace之前，生命周期只执行一次，如果replace，那么不满足ViewPager适配需求
    // 目前未解决情况：ViewPager适配的Fragment刷新不了，导致接口回调数据只生效一次。
    void userAllInfoPutMineFragment(OkGoSessionAndUserBean.Data.UserInfo userInfo);
}
