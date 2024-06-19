package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.gsls.gt.GT;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.adapter.KeyWordJsonSpinnerAdapter;
import github.kutouzi.actassistant.adapter.KeyWordViewAdapter;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.entity.inf.IData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.PinduoduoService;

public class OptionKeywordFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private Spinner _keyWordJsonSpinner;
    public static String listName = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _keyWordJsonSpinner = _layout.findViewById(R.id.keyWordJsonSpinner);
        SpinnerAdapter spinnerAdapter = new KeyWordJsonSpinnerAdapter(getContext(), Stream.of(PinduoduoService.CLICKABLE_KEYWORD_LIST,
                PinduoduoService.CANCELABLE_KEYWORD_LIST, MeituanService.CLICKABLE_KEYWORD_LIST,MeituanService.CANCELABLE_KEYWORD_LIST).collect(Collectors.toList()));
        _keyWordJsonSpinner.setAdapter(spinnerAdapter);
        _keyWordJsonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listName = (String) parent.getItemAtPosition(position);
                KeyWordData keyWordData = (KeyWordData) JsonFileIO.readJson(view.getContext(), JsonFileDefinition.KEYWORD_JSON_NAME, KeyWordData.class);

                // TODO：bug Json可能有列表被清空的情况
                if (listName.equals(PinduoduoService.CLICKABLE_KEYWORD_LIST)) {
                    RecyclerView keyWordRecyclerView = _layout.findViewById(R.id.keyWordRecyclerView);
                    KeyWordViewAdapter keyWordViewAdapter = new KeyWordViewAdapter(Objects.requireNonNull(keyWordData).getPingduoduoClickableKeyWordList());
                    keyWordRecyclerView.setAdapter(keyWordViewAdapter);
                    keyWordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    createAddKeyWordItem(keyWordViewAdapter);
                }else if(listName.equals(PinduoduoService.CANCELABLE_KEYWORD_LIST)){
                    RecyclerView keyWordRecyclerView = _layout.findViewById(R.id.keyWordRecyclerView);
                    KeyWordViewAdapter keyWordViewAdapter = new KeyWordViewAdapter(Objects.requireNonNull(keyWordData).getPingduoduoCancelableKeyWordList());
                    keyWordRecyclerView.setAdapter(keyWordViewAdapter);
                    keyWordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    createAddKeyWordItem(keyWordViewAdapter);
                }else if(listName.equals(MeituanService.CLICKABLE_KEYWORD_LIST)){
                    RecyclerView keyWordRecyclerView = _layout.findViewById(R.id.keyWordRecyclerView);
                    KeyWordViewAdapter keyWordViewAdapter = new KeyWordViewAdapter(Objects.requireNonNull(keyWordData).getMeituanClickableKeyWordList());
                    keyWordRecyclerView.setAdapter(keyWordViewAdapter);
                    keyWordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    createAddKeyWordItem(keyWordViewAdapter);
                }else if(listName.equals(MeituanService.CANCELABLE_KEYWORD_LIST)){
                    RecyclerView keyWordRecyclerView = _layout.findViewById(R.id.keyWordRecyclerView);
                    KeyWordViewAdapter keyWordViewAdapter = new KeyWordViewAdapter(Objects.requireNonNull(keyWordData).getMeituanCancelableKeyWordList());
                    keyWordRecyclerView.setAdapter(keyWordViewAdapter);
                    keyWordRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    createAddKeyWordItem(keyWordViewAdapter);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return _layout;
    }
    private void createAddKeyWordItem(KeyWordViewAdapter keyWordViewAdapter){
        MaterialButton addKeyWordButton = _layout.findViewById(R.id.addKeyWordButton);
        EditText addKeyWordEditText = _layout.findViewById(R.id.addKeyWordEditText);
        addKeyWordButton.setOnClickListener(v -> {
            String s = addKeyWordEditText.getText().toString();
            if (!s.isEmpty()){
                keyWordViewAdapter.addItem(s,v);
            }else {
                GT.toast_time("未输入任何东西",3000);
            }
        });
    }
}
