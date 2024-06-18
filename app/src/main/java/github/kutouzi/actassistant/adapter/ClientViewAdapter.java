package github.kutouzi.actassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.ClientViewData;

public class ClientViewAdapter extends RecyclerView.Adapter<ClientViewAdapter.ViewHolder> {
    private final List<ClientViewData> constraintLayoutList; //提供视图列表

    public ClientViewAdapter(List<ClientViewData> constraintLayoutList) {
        this.constraintLayoutList = constraintLayoutList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_view, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClientViewData clientViewData = constraintLayoutList.get(position);
        holder.clientInfo.setText(clientViewData.getClientInfo());
        holder.clientPreview.setImageResource(clientViewData.getClientPreview());

    }

    @Override
    public int getItemCount() {
        return constraintLayoutList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView clientInfo;
        ShapeableImageView clientPreview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clientInfo = itemView.findViewById(R.id.clientInfo);
            clientPreview = itemView.findViewById(R.id.clientPreview);
        }

    }

}
