package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.util.FragmentUtil;

public class OptionFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private MaterialButton _swipeUpSettingButton;
    private MaterialButton _switchApplicationSettingButton;
    private MaterialButton _autoSettingButton;
    private MaterialButton _keyWordButton;

    private OptionSwipeupFragment _optionSwipeupFragment = null;
    private OptionKeywordFragment _optionKeywordFragment = null;
    private OptionAutoSettingFragment _optionAutoSettingFragment = null;
    private OptionSwitchAppFragment _optionSwitchAppFragment = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _swipeUpSettingButton = _layout.findViewById(R.id.swipeUpSettingButton);
        _swipeUpSettingButton.setOnClickListener(v -> {
            if(_optionSwipeupFragment == null){
                _optionSwipeupFragment = new OptionSwipeupFragment();
                Bundle b = new Bundle();
                b.putInt("layoutResId", R.layout.fragment_option_swipeup);
                _optionSwipeupFragment.setArguments(b);

                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragmentSlot, _optionSwipeupFragment)
                        .hide(_optionSwipeupFragment)
                        .commit();
            }
            FragmentUtil.switchFragment(getParentFragmentManager(),_optionSwipeupFragment);
        });
        _switchApplicationSettingButton = _layout.findViewById(R.id.switchApplicationSettingButton);
        _switchApplicationSettingButton.setOnClickListener(v -> {
            if(_optionSwitchAppFragment == null){
                _optionSwitchAppFragment = new OptionSwitchAppFragment();
                Bundle b = new Bundle();
                b.putInt("layoutResId", R.layout.fragment_option_switchapp);
                _optionSwitchAppFragment.setArguments(b);

                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragmentSlot, _optionSwitchAppFragment)
                        .hide(_optionSwitchAppFragment)
                        .commit();
            }
            FragmentUtil.switchFragment(getParentFragmentManager(),_optionSwitchAppFragment);
        });
        _autoSettingButton = _layout.findViewById(R.id.autoSettingButton);
        _autoSettingButton.setOnClickListener(v -> {
            if(_optionAutoSettingFragment == null){
                _optionAutoSettingFragment = new OptionAutoSettingFragment();
                Bundle b = new Bundle();
                b.putInt("layoutResId", R.layout.fragment_option_autosetting);
                _optionAutoSettingFragment.setArguments(b);
                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragmentSlot, _optionAutoSettingFragment)
                        .hide(_optionAutoSettingFragment)
                        .commit();
            }
            FragmentUtil.switchFragment(getParentFragmentManager(),_optionAutoSettingFragment);
        });
        _keyWordButton = _layout.findViewById(R.id.keyWordButton);
        _keyWordButton.setOnClickListener(v -> {
            if(_optionKeywordFragment == null){
                _optionKeywordFragment = new OptionKeywordFragment();
                Bundle b = new Bundle();
                b.putInt("layoutResId", R.layout.fragment_option_keyword);
                _optionKeywordFragment.setArguments(b);
                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragmentSlot, _optionKeywordFragment)
                        .hide(_optionKeywordFragment)
                        .commit();
            }
            FragmentUtil.switchFragment(getParentFragmentManager(),_optionKeywordFragment);
        });
        return _layout;
    }
}
