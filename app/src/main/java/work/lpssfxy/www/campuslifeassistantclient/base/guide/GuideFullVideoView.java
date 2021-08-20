package work.lpssfxy.www.campuslifeassistantclient.base.guide;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * created by on 2021/8/21
 * 描述：视频引导页
 *
 * @author ZSAndroid
 * @create 2021-08-21-0:22
 */
public class GuideFullVideoView extends VideoView {
    public GuideFullVideoView(Context context) {
        super(context);
    }

    public GuideFullVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GuideFullVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GuideFullVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * 重新计算高度和宽度,实现全屏
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
