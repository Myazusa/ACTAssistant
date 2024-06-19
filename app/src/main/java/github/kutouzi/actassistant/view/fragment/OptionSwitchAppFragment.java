package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gsls.gt.GT;

import java.util.Optional;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.SwitchApplicationData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class OptionSwitchAppFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private EditText _switchApplicationTimeEditText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _switchApplicationTimeEditText = _layout.findViewById(R.id.switchApplicationTimeEditText);
        SwitchApplicationData switchApplicationData = (SwitchApplicationData) JsonFileIO.readJson(getContext(), JsonFileDefinition.SWITCHAPP_JSON_NAME,SwitchApplicationData.class);
        Optional.ofNullable(switchApplicationData).ifPresent(s -> {
            // 因为显示的是分钟，所以要除以60000得到分钟
            _switchApplicationTimeEditText.setText(String.valueOf(s.getSwitchApplicationTime() /60000));
        });
        _switchApplicationTimeEditText.setOnFocusChangeListener((v, hasFocus)->{
            if(!hasFocus){
                SwitchApplicationData data = (SwitchApplicationData)JsonFileIO.readJson(getContext(), JsonFileDefinition.SWITCHAPP_JSON_NAME,SwitchApplicationData.class);
                Optional.ofNullable(data).ifPresent(s->{
                    // 获取文本得到的是分钟，需要乘以60000得到毫秒
                    int i = Integer.parseInt(_switchApplicationTimeEditText.getText().toString()) * 60000;
                    if(i != 0 && i <= 3600000) {
                        s.setSwitchApplicationTime(i);
                        JsonFileIO.writeJson(getContext(), JsonFileDefinition.SWITCHAPP_JSON_NAME, s);
                    }else {
                        s.setSwitchApplicationTime(data.getSwitchApplicationTime()/60000);
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        return _layout;
    }
}
