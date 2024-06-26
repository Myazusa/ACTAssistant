package github.kutouzi.actassistant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Optional;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.entity.inf.IData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.PinduoduoService;
import github.kutouzi.actassistant.view.fragment.OptionKeywordFragment;

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
            if(!hasFocus){
                keyWordList.set(position,holder.keyWordEditText.getText().toString());
                updateJsonFile(v);
            }
        });
        holder.removeButton.setOnClickListener(v -> {
            keyWordList.remove(position);
            updateJsonFile(v);
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
        KeyWordData keyWordData = (KeyWordData) JsonFileIO.readJson(v.getContext(),JsonFileDefinition.KEYWORD_JSON_NAME, KeyWordData.class);
        Optional.ofNullable(keyWordData).ifPresent(s->{
            if(OptionKeywordFragment.listName.equals(PinduoduoService.CANCELABLE_KEYWORD_LIST)){
                keyWordData.setPingduoduoCancelableKeyWordList(keyWordList);
                JsonFileIO.writeJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            } else if (OptionKeywordFragment.listName.equals(PinduoduoService.CLICKABLE_KEYWORD_LIST)) {
                keyWordData.setPingduoduoClickableKeyWordList(keyWordList);
                JsonFileIO.writeJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            } else if (OptionKeywordFragment.listName.equals(MeituanService.CANCELABLE_KEYWORD_LIST)) {
                keyWordData.setMeituanCancelableKeyWordList(keyWordList);
                JsonFileIO.writeJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
            } else if (OptionKeywordFragment.listName.equals(MeituanService.CLICKABLE_KEYWORD_LIST)) {
                keyWordData.setMeituanClickableKeyWordList(keyWordList);
                JsonFileIO.writeJson(v.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME,keyWordData);
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
