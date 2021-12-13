package work.lpssfxy.www.campuslifeassistantclient.entity.dto;

import lombok.Data;

/**
 * created by on 2021/12/13
 * 描述：商家商品信息单个实体信息
 *
 * @author ZSAndroid
 * @create 2021-12-13-20:20
 */

@Data
public class StoreGoodsInfoBean {
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

    public StoreGoodsInfoBean() {
    }

    public StoreGoodsInfoBean(int categoryId, String createTime, String goodsDesc, int goodsFlag, int goodsId, String goodsName, String goodsPic, int goodsPrice, int goodsRepertory, int ssId, String updateTime) {
        this.categoryId = categoryId;
        this.createTime = createTime;
        this.goodsDesc = goodsDesc;
        this.goodsFlag = goodsFlag;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.goodsPic = goodsPic;
        this.goodsPrice = goodsPrice;
        this.goodsRepertory = goodsRepertory;
        this.ssId = ssId;
        this.updateTime = updateTime;
    }
}
