package work.lpssfxy.www.campuslifeassistantclient.base.openmap;

/**
 * created by on 2021/9/1
 * 描述：设置导航经纬度
 *
 * @author ZSAndroid
 * @create 2021-09-01-17:42
 */
public class AddressInfo {

    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public AddressInfo setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLng() {
        return lng;
    }

    public AddressInfo setLng(double lng) {
        this.lng = lng;
        return this;
    }
}
