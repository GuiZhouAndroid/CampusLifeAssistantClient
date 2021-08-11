package work.lpssfxy.www.campuslifeassistantclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class myFragment extends Fragment {
    public Unbinder unbinder;//Fragment使用ButterKnife注解初始化用到的
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment,container,false);
        //初始化ButterKnife注解
        unbinder= ButterKnife.bind(this,view);
        return view;
    }

    /**
     * 点击事件
     */
    @OnClick(R2.id.qwe)
    public void onViewClick(){
        Toast.makeText(getActivity(), "Fragment", Toast.LENGTH_SHORT).show();
    }

    /**
     * 解绑操作
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
