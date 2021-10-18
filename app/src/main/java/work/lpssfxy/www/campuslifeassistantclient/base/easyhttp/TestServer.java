package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp;

import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;

/**
 * created by on 2021/10/17
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:20
 */
public class TestServer extends ReleaseServer {

    @Override
    public String getHost() {
        return Constant.BASE_URL;
    }
}
