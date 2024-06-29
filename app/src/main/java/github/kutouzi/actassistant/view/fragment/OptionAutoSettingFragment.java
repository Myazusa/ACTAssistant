package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.gsls.gt.GT;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.AutoSettingData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class OptionAutoSettingFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    public static MaterialSwitch autoScanApplicationSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        autoScanApplicationSwitch = _layout.findViewById(R.id.autoScanApplicationSwitch);
        AutoSettingData autoSettingData = (AutoSettingData) JsonFileIO.readJson(getContext(), JsonFileDefinition.AUTOSETTING_JSON_NAME, AutoSettingData.class);
        autoScanApplicationSwitch.setChecked(autoSettingData.getAutoScanApplicationButtonState());
        autoScanApplicationSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if(isChecked){
                //autoScanApplicationSwitch.setText("开启中");
                autoScanApplicationSwitch.setTextColor( getResources().getColor(R.color.my_app_accent));
                JsonFileIO.writeJson(getContext(), JsonFileDefinition.AUTOSETTING_JSON_NAME, new AutoSettingData(true));
                GT.toast_time("自动切换应用被开启",3000);
            }else {
                //autoScanApplicationSwitch.setText("关闭中");
                autoScanApplicationSwitch.setTextColor(getResources().getColor(R.color.my_app_on_primary));
                JsonFileIO.writeJson(getContext(), JsonFileDefinition.AUTOSETTING_JSON_NAME, new AutoSettingData(false));
                GT.toast_time("自动切换应用被关闭",3000);
            }
        });
        return _layout;
    }
}
