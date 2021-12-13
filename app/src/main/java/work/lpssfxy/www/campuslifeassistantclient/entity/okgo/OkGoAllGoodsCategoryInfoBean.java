package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/13
 * 描述：商品分类全部信息
 *
 * @author ZSAndroid
 * @create 2021-12-13-21:58
 */
@Data
public class OkGoAllGoodsCategoryInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String gcDesc;
        private int gcId;
        private String gcName;
    }
}
