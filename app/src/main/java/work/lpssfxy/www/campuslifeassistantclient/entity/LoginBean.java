package work.lpssfxy.www.campuslifeassistantclient.entity;

/**
 * created by on 2021/10/17
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:38
 */
public class LoginBean {

    private int code;
    private String data;
    private String msg;
    private String nowTime;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }
}
