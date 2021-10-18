package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp;

import com.hjq.http.config.IRequestPath;

/**
 * created by on 2021/10/17
 * 描述：实现这个接口之后可以重新指定这个请求的接口路径
 *
 * @author ZSAndroid
 * @create 2021-10-17-22:49
 */
public class RequestPath implements IRequestPath {

    @Override
    public String getPath() {
        return "api/";
    }
}
