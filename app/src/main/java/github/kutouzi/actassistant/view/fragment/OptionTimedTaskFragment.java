package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.gsls.gt.GT;

import java.util.Optional;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.TimedTaskData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class OptionTimedTaskFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private TextInputEditText _taskDelayTimeEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _taskDelayTimeEditText = _layout.findViewById(R.id.taskDelayTimeEditText);
        TimedTaskData timedTaskData = (TimedTaskData) JsonFileIO.readJson(getContext(), JsonFileDefinition.TIMEDTASK_JSON_NAME, TimedTaskData.class);
        Optional.ofNullable(timedTaskData).ifPresent(s -> {
            _taskDelayTimeEditText.setText(timedTaskData.getTimedTaskDelayValue());
        });

        _taskDelayTimeEditText.setOnFocusChangeListener((v, hasFocus)->{
            if(!hasFocus){
                Optional.ofNullable(timedTaskData).ifPresent(s->{
                    if(Integer.parseInt(_taskDelayTimeEditText.getText().toString()) >= s.getTimedTaskDelayValue()){
                        // 检测是否大于等于当前最小值
                        s.setTimedTaskDelayValue(Integer.parseInt(_taskDelayTimeEditText.getText().toString()));
                        JsonFileIO.writeJson(getContext(),JsonFileDefinition.TIMEDTASK_JSON_NAME,s);
                    }else {
                        // 小于的话就设置回去
                        s.setTimedTaskDelayValue(s.getTimedTaskDelayValue());
                        GT.toast_time("写入失败：错误的值",3000);
                    }
                });
            }
        });
        return _layout;
    }
}
