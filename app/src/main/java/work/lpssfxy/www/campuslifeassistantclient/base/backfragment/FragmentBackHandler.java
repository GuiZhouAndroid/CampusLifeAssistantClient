package work.lpssfxy.www.campuslifeassistantclient.base.backfragment;

/**
 * created by on 2021/11/23
 * 描述：Fragment并没有onBackPressed()方法，假如需要在Fragment监听返回操作需要自己实现返回的回调监听
 *
 * @author ZSAndroid
 * @create 2021-11-23-18:27
 */

public interface FragmentBackHandler {
    //定义Fragment返回监听接口
    boolean onBackPressed();
}
