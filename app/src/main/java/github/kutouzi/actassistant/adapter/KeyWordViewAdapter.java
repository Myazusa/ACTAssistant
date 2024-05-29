package github.kutouzi.actassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gsls.gt.GT;

import java.util.List;
import java.util.Optional;

import github.kutouzi.actassistant.MainActivity;
import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.enums.KeyWordListDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class KeyWordViewAdapter extends RecyclerView.Adapter<KeyWordViewAdapter.ViewHolder> {
    public List<String> getKeyWordList() {
        return keyWordList;
    }

    public void setKeyWordList(List<String> keyWordList) {
        this.keyWordList = keyWordList;
    }

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
            if(!hasFocus){
                keyWordList.set(position,holder.keyWordEditText.getText().toString());
                updateJsonFile(v);
            }
        });
        holder.removeButton.setOnClickListener(v -> {
            keyWordList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return keyWordList.size();
    }

    public void addItem(String s,View v){
        keyWordList.add(s);
        updateJsonFile(v);
        // 添加到最尾部
        notifyItemInserted(getItemCount());
    }

    private void updateJsonFile(View v){
        KeyWordData keyWordData = JsonFileIO.readKeyWordDataJson(v.getContext(),JsonFileDefinition.KEYWORD_JSON_NAME);
        Optional.ofNullable(keyWordData).ifPresent(s->{
            if(MainActivity.listName.equals(KeyWordListDefinition.PINGDUODUO_CANCELABLE_KEYWORD_LIST)){
                keyWordData.setPingduoduoCancelableKeyWordList(keyWordList);
                JsonFileIO.writeKeyWordDataJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            } else if (MainActivity.listName.equals(KeyWordListDefinition.PINGDUODUO_CLICKABLE_KEYWORD_LIST)) {
                keyWordData.setPingduoduoClickableKeyWordList(keyWordList);
                JsonFileIO.writeKeyWordDataJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            } else if (MainActivity.listName.equals(KeyWordListDefinition.MEITUAN_CANCELABLE_KEYWORD_LIST)) {
                keyWordData.setMeituanCancelableKeyWordList(keyWordList);
                JsonFileIO.writeKeyWordDataJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            } else if (MainActivity.listName.equals(KeyWordListDefinition.MEITUAN_CANCELABLE_KEYWORD_LIST)) {
                keyWordData.setMeituanClickableKeyWordList(keyWordList);
                JsonFileIO.writeKeyWordDataJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            }
        });
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
