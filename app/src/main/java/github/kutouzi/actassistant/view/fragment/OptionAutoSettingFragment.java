package github.kutouzi.actassistant.view.fragment;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
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
        autoScanApplicationSwitch.setOnCheckedChangeListener((v, isChecked) -> {
            if(isChecked){
                autoScanApplicationSwitch.setText("开启中");
                autoScanApplicationSwitch.setTextColor( getResources().getColor(R.color.my_app_accent));
                GT.toast_time("自动切换应用被开启",3000);
            }else {
                autoScanApplicationSwitch.setText("关闭中");
                autoScanApplicationSwitch.setTextColor(getResources().getColor(R.color.my_app_on_primary));
                GT.toast_time("自动切换应用被关闭",3000);
            }
        });
        return _layout;
    }
}
