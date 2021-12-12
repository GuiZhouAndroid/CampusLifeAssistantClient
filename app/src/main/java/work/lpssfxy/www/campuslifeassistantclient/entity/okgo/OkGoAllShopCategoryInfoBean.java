package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/12
 * 描述：商铺分类全部信息
 *
 * @author ZSAndroid
 * @create 2021-12-12-19:14
 */
@Data
public class OkGoAllShopCategoryInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private String scDesc;
        private int scId;
        private String scName;
    }
}
