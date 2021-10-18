package work.lpssfxy.www.campuslifeassistantclient.base.easyhttp.model;

import java.util.List;

/**
 * created by on 2021/10/17
 * 描述：统一接口列表数据结构
 *
 * @author ZSAndroid
 * @create 2021-10-17-23:03
 */
public class HttpListData<T> extends HttpData<HttpListData.ListBean<T>> {

    public static class ListBean<T> {

        /** 当前页码 */
        private int pageIndex;
        /** 页大小 */
        private int pageSize;
        /** 总数量 */
        private int totalNumber;
        /** 数据 */
        private List<T> items;

        /**
         * 判断是否是最后一页
         */
        public boolean isLastPage() {
            return Math.ceil((float) totalNumber / pageSize) <= pageIndex;
        }

        public int getTotalNumber() {
            return totalNumber;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public int getPageSize() {
            return pageSize;
        }

        public List<T> getItems() {
            return items;
        }
    }
}
