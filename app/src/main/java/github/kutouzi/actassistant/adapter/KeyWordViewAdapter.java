package github.kutouzi.actassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import github.kutouzi.actassistant.R;

public class KeyWordViewAdapter extends RecyclerView.Adapter<KeyWordViewAdapter.ViewHolder> {
    private List<String> keyWordList;
    public KeyWordViewAdapter(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.key_word_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.keyWordEditText.setText(keyWordList.get(position));
        holder.keyWordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            // TODO:关键词写入文件
        });
        holder.removeButton.setOnClickListener(v -> {
            // TODO:移除当前项
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText keyWordEditText;
        ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            keyWordEditText = itemView.findViewById(R.id.keyWordEditText);
            removeButton = itemView.findViewById(R.id.removeKeyWordButton);
        }

    }
}
