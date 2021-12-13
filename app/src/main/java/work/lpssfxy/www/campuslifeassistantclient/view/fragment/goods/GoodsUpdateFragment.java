package work.lpssfxy.www.campuslifeassistantclient.view.fragment.goods;

import android.util.Log;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.fragment.LazyLoadFragment;

/**
 * created by on 2021/12/13
 * 描述：商家更新商品
 *
 * @author ZSAndroid
 * @create 2021-12-13-16:20
 */
public class GoodsUpdateFragment extends LazyLoadFragment {


    /**
     * @return 单例对象
     */
    public static GoodsUpdateFragment newInstance() {
        return new GoodsUpdateFragment();
    }
    @Override
    protected int setContentView() {
        return R.layout.fragment_goods_update;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void stopLoad() {
        Log.d(TAG, "Fragment4" + "已经对用户不可见，可以停止加载数据");
    }
}
