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
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class OptionSwipeupFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private EditText _maxSwipeupTimeEditText;
    private EditText _minSwipeupTimeEditText;
    private EditText _maxDelayTimeEditText;
    private EditText _minDelayTimeEditText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _maxSwipeupTimeEditText = _layout.findViewById(R.id.maxSwipeupTimeEditText);
        _minSwipeupTimeEditText = _layout.findViewById(R.id.minSwipeupTimeEditText);
        _maxDelayTimeEditText = _layout.findViewById(R.id.maxDelayTimeEditText);
        _minDelayTimeEditText = _layout.findViewById(R.id.minDelayTimeEditText);
        SwipeUpData swipeUpData = JsonFileIO.readSwipeUpDataJson(getContext(), JsonFileDefinition.SWIPEUP_JSON_NAME);

        Optional.ofNullable(swipeUpData).ifPresent(s -> {
            _maxSwipeupTimeEditText.setText(String.valueOf(s.getRandomMaxSwipeupValue()));
            _minSwipeupTimeEditText.setText(String.valueOf(s.getRandomMinSwipeupValue()));
            _maxDelayTimeEditText.setText(String.valueOf(s.getRandomMaxDelayValue()));
            _minDelayTimeEditText.setText(String.valueOf(s.getRandomMinDelayValue()));
        });
        _maxSwipeupTimeEditText.setOnFocusChangeListener((v, hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(_maxSwipeupTimeEditText.getText().toString()) >= s.getRandomMinSwipeupValue()){
                        // 检测是否大于等于当前最小值
                        s.setRandomMaxSwipeupValue(Integer.parseInt(_maxSwipeupTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(getContext(),JsonFileDefinition.SWIPEUP_JSON_NAME,s);
                    }else {
                        // 小于的话就设置回去
                        s.setRandomMaxSwipeupValue(s.getRandomMaxSwipeupValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        _minSwipeupTimeEditText.setOnFocusChangeListener((v, hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(_minSwipeupTimeEditText.getText().toString()) <= s.getRandomMaxSwipeupValue()){
                        s.setRandomMinSwipeupValue(Integer.parseInt(_minSwipeupTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(getContext(),JsonFileDefinition.SWIPEUP_JSON_NAME,s);
                    }else {
                        s.setRandomMinSwipeupValue(s.getRandomMinSwipeupValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        _maxDelayTimeEditText.setOnFocusChangeListener((v, hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(_maxDelayTimeEditText.getText().toString()) >= s.getRandomMinDelayValue()) {
                        s.setRandomMaxDelayValue(Integer.parseInt(_maxDelayTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(getContext(), JsonFileDefinition.SWIPEUP_JSON_NAME, s);
                    }else {
                        s.setRandomMaxDelayValue(s.getRandomMaxDelayValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        _minDelayTimeEditText.setOnFocusChangeListener((v, hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(swipeUpData).ifPresent(s->{
                    if(Integer.parseInt(_minDelayTimeEditText.getText().toString()) <= s.getRandomMaxDelayValue()) {
                        s.setRandomMinDelayValue(Integer.parseInt(_minDelayTimeEditText.getText().toString()));
                        JsonFileIO.writeSwipeUpDataJson(getContext(), JsonFileDefinition.SWIPEUP_JSON_NAME, s);
                    }else {
                        s.setRandomMinDelayValue(s.getRandomMinDelayValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        return _layout;
    }
}
