package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.network.JsonFileNIO;

public class UploadIpaddressFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private EditText ip1EditText;
    private EditText ip2EditText;
    private EditText ip3EditText;
    private EditText ip4EditText;
    private EditText portEditView;
    private MaterialButton sendToServerButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        ip1EditText = _layout.findViewById(R.id.ip1EditView);
        ip2EditText = _layout.findViewById(R.id.ip2EditView);
        ip3EditText = _layout.findViewById(R.id.ip3EditView);
        ip4EditText = _layout.findViewById(R.id.ip4EditView);
        portEditView = _layout.findViewById(R.id.portEditView);
        // TODO:获取ip地址，转化对象存储
        MaterialButton sendToServerButton = _layout.findViewById(R.id.sendToServerButton);
        sendToServerButton.setOnClickListener(v->{
            SwipeUpData swipeUpData = (SwipeUpData) JsonFileIO.readJson(getContext(), JsonFileDefinition.SWIPEUP_JSON_NAME,SwipeUpData.class);
            JsonFileNIO.sendSwipeUpJsonToQt(JsonFileIO._gson.toJson(swipeUpData));
            KeyWordData keyWordData = (KeyWordData) JsonFileIO.readJson(getContext(),JsonFileDefinition.KEYWORD_JSON_NAME,KeyWordData.class);
            JsonFileNIO.sendSwipeUpJsonToQt(JsonFileIO._gson.toJson(keyWordData));
        });

        return _layout;
    }
}
