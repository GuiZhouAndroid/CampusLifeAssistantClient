package work.lpssfxy.www.campuslifeassistantclient.utils;

import static com.xuexiang.xutil.tip.ToastUtils.toast;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.interfaces.OnSrcViewUpdateListener;
import com.lxj.xpopup.util.SmartGlideImageLoader;

import java.util.List;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.view.activity.LoginActivity;

/**
 * created by on 2021/11/5
 * 描述： XPopup自定义工具类，请求进度条的延迟关闭，仿真网络请求
 *
 * @author ZSAndroid
 * @create 2021-11-05-23:25
 */
public class XPopupUtils {

    /**
     * 单例模式全局静态变量
     */
    private static XPopupUtils xPopupUtils;

    /**
     * XPopup全局变量
     */
    private static BasePopupView popupView;

    /**
     * @return XPopupUtils单例实例
     */
    public static XPopupUtils getInstance() {
        //实例为空时就创建对象，java垃圾回收机制，回收之前，一直生效
        if (xPopupUtils == null) {
            xPopupUtils = new XPopupUtils();
        }
        //不为空返回XPopupUtils对象实例
        return xPopupUtils;
    }

    /**
     * 设置显示进度条
     *
     * @param activity 上下文
     * @param msg      进度条显示文本
     */
    public void setShowDialog(Activity activity, String msg) {
        popupView = new XPopup.Builder(activity)
                .asLoading(msg)
                .show();
    }

    /**
     * 立即消失进度条，体验不好，不推荐
     */
    public void setNowDisDialog() {
        popupView.smartDismiss();
    }

    /**
     * 会等待弹窗的开始动画执行完毕再进行消失，可以防止接口调用过快导致的动画不完整
     */
    public void setSmartDisDialog() {
        popupView.smartDismiss();
    }

    /**
     * 延时消失，有时候消失过快体验可能不好，可以延时一下
     */
    public void setTimerDisDialog(long time) {
        popupView.delayDismiss(time);
    }

    /**
     * 显示从底部弹出的列表弹窗：这种弹窗从 1.0.0版本开始实现了优雅的手势交互和智能嵌套滚动
     * 如需另外执行，请Copy方法体，自定义onSelect事件
     *
     * @param activity 上下文
     */
    public void BottomListDialog(Activity activity) {
        new XPopup.Builder(activity)
                .asBottomList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4", "条目5"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                .show();
    }


    /**
     * 显示确认和取消对话框
     * 如需另外执行，请Copy方法体，自定义onConfirm事件
     *
     * @param activity 上下文
     */
    public void ConfirmAndCancelDialog(Activity activity) {
        new XPopup.Builder(activity).asConfirm("我是标题", "我是内容",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        toast("click confirm");
                    }
                })
                .show();
    }

    /**
     * 显示中间弹出的列表弹窗
     * 如需另外执行，请Copy方法体，自定义onSelect事件
     *
     * @param activity 上下文
     */
    public void CenterSelectDialog(Activity activity) {
        new XPopup.Builder(activity)
                //.maxWidth(600)
                .asCenterList("请选择一项", new String[]{"条目1", "条目2", "条目3", "条目4"},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
                                toast("click " + text);
                            }
                        })
                .show();

    }

    /**
     * 显示带输入框的确认和取消对话框
     * 如需另外执行，请Copy方法体，自定义onConfirm事件
     *
     * @param activity 上下文
     */
    public void InputConfirmDialog(Activity activity) {
        new XPopup.Builder(activity).asInputConfirm("我是标题", "请输入内容。",
                new OnInputConfirmListener() {
                    @Override
                    public void onConfirm(String text) {
                        toast("input text: " + text);
                    }
                })
                .show();
    }

    /**
     * 显示依附于某个View或者某个点的弹窗
     * 如需另外执行，请Copy方法体，自定义onConfirm事件
     * 大图浏览弹窗
     * 当你点击图片的时候执行以下代码：
     * 多图片场景（你有多张图片需要浏览）
     * srcView参数表示你点击的那个ImageView，动画从它开始，结束时回到它的位置。
     *
     * @param activity 上下文
     */
    public void AttachListDialog(Activity activity, ImageView imageView, int position,  List<Object> urls , RecyclerView recyclerView) {

        new XPopup.Builder(activity).asImageViewer(imageView, position, urls, new OnSrcViewUpdateListener() {
            @Override
            public void onSrcViewUpdate(ImageViewerPopupView popupView, int position) {
                // 注意这里：根据position找到你的itemView。根据你的itemView找到你的ImageView。
                // Demo中RecyclerView里面只有ImageView所以这样写，不要原样COPY。
                // 作用是当Pager切换了图片，需要更新源View，
                popupView.updateSrcView((ImageView) recyclerView.getChildAt(position));
            }
        }, new SmartGlideImageLoader())
                .show();
        // 单张图片场景
        new XPopup.Builder(activity)
                .asImageViewer(imageView, urls, new SmartGlideImageLoader())
                .show();
//        new XPopup.Builder(activity)
//                .atView(v)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
//                .asAttachList(new String[]{"分享", "编辑", "不带icon"},
//                        new int[]{
//                                R.mipmap.ic_launcher, R.mipmap.ic_launcher},
//                        new OnSelectListener() {
//                            @Override
//                            public void onSelect(int position, String text) {
//                                toast("click " + text);
//                            }
//                        })
//                .show();
//
//        // 必须在事件发生前，调用这个方法来监视View的触摸
//        final XPopup.Builder builder = new XPopup.Builder(activity)
//                .watchView(view.findViewById(R.id.btnShowAttachPoint));
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                builder.asAttachList(new String[]{"置顶", "复制", "删除"}, null,
//                        new OnSelectListener() {
//                            @Override
//                            public void onSelect(int position, String text) {
//                                toast("click " + text);
//                            }
//                        })
//                        .show();
//                return false;
//            }
//        });
    }
}
