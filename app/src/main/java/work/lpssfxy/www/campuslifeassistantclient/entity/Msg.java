package work.lpssfxy.www.campuslifeassistantclient.entity;

import lombok.Data;

/**
 * created by on 2021/8/23
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:19
 */
@Data
public class Msg {
    /**
     * errorCode : 0
     * errorMsg : null
     * data : jj
     */

    private int errorCode;
    private Object errorMsg;
    private Object data;
    private Object data2;

    public Msg() {
    }

    public Msg(int errorCode, Object errorMsg, Object data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

}

