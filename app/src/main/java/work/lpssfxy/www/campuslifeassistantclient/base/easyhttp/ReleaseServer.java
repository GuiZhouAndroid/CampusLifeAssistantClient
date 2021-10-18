package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp;

import com.hjq.http.config.IRequestServer;

import work.lpssfxy.www.campuslifeassistantclient.base.constant.Constant;

/**
 * created by on 2021/10/17
 * 描述：服务器正式环境，实现这个接口之后可以重新指定这个请求的主机地址
 *
 * @author ZSAndroid
 * @create 2021-10-17-22:42
 */

public class ReleaseServer implements IRequestServer {

    @Override
    public String getHost() {
        return Constant.BASE_URL;
    }

    @Override
    public String getPath() {
        return "";
    }
}
