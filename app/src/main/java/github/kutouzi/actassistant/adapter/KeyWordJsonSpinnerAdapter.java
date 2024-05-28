package github.kutouzi.actassistant.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.enums.KeyWordListDefinition;

public class KeyWordJsonSpinnerAdapter extends ArrayAdapter<String>{
    private final List<String> constraintLayoutList = Stream.of(KeyWordListDefinition.PINGDUODUO_CLICKABLE_KEYWORD_LIST,
            KeyWordListDefinition.PINGDUODUO_CANCELABLE_KEYWORD_LIST,KeyWordListDefinition.MEITUAN_CLICKABLE_KEYWORD_LIST,KeyWordListDefinition.MEITUAN_CANCELABLE_KEYWORD_LIST).collect(Collectors.toList());

    public KeyWordJsonSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = parent.findViewById(R.id.keyWordJsonSpinner);
        }
        TextView textView = convertView.findViewById(R.id.keyWordListTextView);
        textView.setText(constraintLayoutList.get(position));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = parent.findViewById(R.id.keyWordJsonSpinner);
        }
        TextView textView = convertView.findViewById(R.id.keyWordListTextView);
        textView.setText(constraintLayoutList.get(position));
        return convertView;
    }
}
