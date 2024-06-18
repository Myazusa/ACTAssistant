package github.kutouzi.actassistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import github.kutouzi.actassistant.R;

public class KeyWordJsonSpinnerAdapter extends ArrayAdapter<String>{
    private final List<String> constraintLayoutList;
    private Context context;

    public KeyWordJsonSpinnerAdapter(@NonNull Context context, @NonNull List<String> constraintLayoutList) {
        super(context, 0, constraintLayoutList);
        this.constraintLayoutList = constraintLayoutList;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, android.R.layout.simple_spinner_dropdown_item);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.key_word_list_view, parent, false);
        }

        TextView textView = view.findViewById(R.id.keyWordListTextView);
        String item = constraintLayoutList.get(position);
        textView.setText(item);

        return view;
    }
}
