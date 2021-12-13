package work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods;

import android.util.Log;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.LazyLoadFragment;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.developer.DeveloperKickOffLineTokenFragment;

/**
 * created by on 2021/12/13
 * 描述：商家添加商品
 *
 * @author ZSAndroid
 * @create 2021-12-13-16:20
 */
public class GoodsAddFragment extends LazyLoadFragment {

    /**
     * @return 单例对象
     */
    public static GoodsAddFragment newInstance() {
        return new GoodsAddFragment();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_add;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void stopLoad() {
        Log.d(TAG, "Fragment2" + "已经对用户不可见，可以停止加载数据");
    }
}
