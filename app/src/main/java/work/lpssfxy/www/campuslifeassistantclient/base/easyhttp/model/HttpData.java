package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.model;

/**
 * created by on 2021/10/17
 * 描述：统一接口数据结构
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:02
 */
public class HttpData<T> {

    /** 返回码 */
    private int errorCode;
    /** 提示语 */
    private String errorMsg;
    /** 数据 */
    private T data;

    public int getCode() {
        return errorCode;
    }

    public String getMessage() {
        return errorMsg;
    }

    public T getData() {
        return data;
    }

    /**
     * 是否请求成功
     */
    public boolean isRequestSucceed() {
        return errorCode == 0;
    }

    /**
     * 是否 Token 失效
     */
    public boolean isTokenFailure() {
        return errorCode == 1001;
    }
}
