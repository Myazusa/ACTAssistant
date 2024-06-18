package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.adapter.ClientViewAdapter;
import github.kutouzi.actassistant.entity.ClientViewData;

public class CilentListviewFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private final int _recyclerViewSpanCount = 2;
    private RecyclerView _clientRecyclerView = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        if(_clientRecyclerView == null){
            // 寻找clientRecyclerView的xml资源
            _clientRecyclerView = _layout.findViewById(R.id.listView);

            // 设置布局管理器
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), _recyclerViewSpanCount, GridLayoutManager.VERTICAL, false);
            _clientRecyclerView.setLayoutManager(layoutManager);

            // 创建数据集
            List<ClientViewData> clientViewData = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                clientViewData.add(new ClientViewData("192.168.1." + i, R.drawable.connection_lost));
            }

            // 创建adapter，准备为视图提供数据
            ClientViewAdapter adapter = new ClientViewAdapter(clientViewData);

            // 通过adapter把数据集绑定到clientRecyclerView以显示
            _clientRecyclerView.setAdapter(adapter);
        }
        return inflater.inflate(_layoutResId, container, false);
    }
}
