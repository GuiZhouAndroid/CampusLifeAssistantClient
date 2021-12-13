package work.lpssfxy.www.campuslifeassistantclient.entity.okgo;

import java.util.List;

import lombok.Data;

/**
 * created by on 2021/12/13
 * 描述：全部商品列表实体
 *
 * @author ZSAndroid
 * @create 2021-12-13-19:22
 */
@Data
public class OkGoAllGoodsInfoBean {

    private int code;
    private List<Data> data;
    private String msg;
    private String nowTime;

    @lombok.Data
    public static class Data {
        private int categoryId;
        private String createTime;
        private String goodsDesc;
        private int goodsFlag;
        private int goodsId;
        private String goodsName;
        private String goodsPic;
        private int goodsPrice;
        private int goodsRepertory;
        private int ssId;
        private String updateTime;
    }
}
