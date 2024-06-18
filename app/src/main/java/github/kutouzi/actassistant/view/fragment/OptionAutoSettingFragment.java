package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gsls.gt.GT;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.view.button.ToggleButton;

public class OptionAutoSettingFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    public static ToggleButton _autoScanApplicationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _autoScanApplicationButton = _layout.findViewById(R.id.autoScanApplicationButton);
        _autoScanApplicationButton.setOnClickListener(v -> {
            if(!_autoScanApplicationButton.isToggled()){
                _autoScanApplicationButton.toggle();
                GT.toast_time("自动切换应用被开启",3000);
            }else {
                _autoScanApplicationButton.toggle();
                GT.toast_time("自动切换应用被关闭",3000);
            }
        });
        _autoScanApplicationButton.callOnClick();
        return _layout;
    }
}
