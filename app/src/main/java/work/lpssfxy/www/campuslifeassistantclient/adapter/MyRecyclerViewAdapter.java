package work.lpssfxy.www.campuslifeassistantclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import work.lpssfxy.www.campuslifeassistantclient.R;
import work.lpssfxy.www.campuslifeassistantclient.entity.CampusInformationBean;

/**
 * created by on 2021/9/2
 * 描述：校园资讯适配器
 *
 * @author ZSAndroid
 * @create 2021-09-02-17:20
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.myViewHodler> {
    private Context context;
    private ArrayList<CampusInformationBean> entityArrayList;

    //创建构造函数
    public MyRecyclerViewAdapter(Context context, ArrayList<CampusInformationBean> noticeEntityArrayList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.entityArrayList = noticeEntityArrayList;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.index_fragment_recyclerview_item, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHodler holder, int position) {
        //根据点击位置绑定数据
        CampusInformationBean bean = entityArrayList.get(position);
        holder.tv_notice_title.setText(bean.infoTitle);//获取实体类中的校园资讯标题字段并设置
        holder.tv_notice_source.setText(bean.infoSource);//获取实体类中的校园资讯信息来源字段并设置
        holder.tv_notice_time.setText(bean.infoIssueTime);//获取实体类中的校园资讯发布时间字段并设置
        holder.tv_notice_content.setText(bean.infoContent);//获取实体类中的校园资讯内容概况字段并设置
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return entityArrayList.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {
        private TextView tv_notice_title;
        private TextView tv_notice_content;
        private TextView tv_notice_time;
        private TextView tv_notice_source;

        public myViewHodler(View itemView) {
            super(itemView);
            tv_notice_title = itemView.findViewById(R.id.tv_notice_title);
            tv_notice_content = itemView.findViewById(R.id.tv_notice_content);
            tv_notice_time = itemView.findViewById(R.id.tv_notice_time);
            tv_notice_source = itemView.findViewById(R.id.tv_notice_source);
            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(v, entityArrayList.get(getLayoutPosition()));
                    }
                }
            });

        }
    }

    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        void OnItemClick(View view, CampusInformationBean data);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
