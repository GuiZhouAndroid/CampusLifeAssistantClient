package work.lpssfxy.www.campuslifeassistantclient.base.listener;

/**
 * created by on 2021/11/15
 * 描述：拖拽图片监听事件
 *
 * @author ZSAndroid
 * @create 2021-11-15-23:17
 */
public interface DragListener {
    /**
     * 是否将 item拖动到删除处，根据状态改变颜色
     *
     * @param isDelete
     */
    void deleteState(boolean isDelete);

    /**
     * 是否于拖拽状态
     *
     * @param isStart
     */
    void dragState(boolean isStart);
}
