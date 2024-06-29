package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Optional;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.RemoteIpaddressData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class ServerFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private TextInputEditText _textInputEditText;
    private ServerAddIpFragment _serverAddIpFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _textInputEditText = _layout.findViewById(R.id.serverIpaddressEditText);
        RemoteIpaddressData remoteIpaddressData = (RemoteIpaddressData) JsonFileIO.readJson(getContext(), JsonFileDefinition.IPADDRESS_JSON_NAME,RemoteIpaddressData.class);
        Optional.ofNullable(remoteIpaddressData).ifPresent(s->{
            _textInputEditText.setText(remoteIpaddressData.getIpaddress()+":"+remoteIpaddressData.getPort());
        });
        _textInputEditText.setFocusable(false);
        _textInputEditText.setFocusableInTouchMode(false);
        _textInputEditText.setInputType(InputType.TYPE_NULL);
        _textInputEditText.setOnClickListener(v -> {
            // TODO: 设置弹出addip的fragment
            if (_serverAddIpFragment == null){
                _serverAddIpFragment = new ServerAddIpFragment();
                Bundle b = new Bundle();
                b.putInt("layoutResId", R.layout.fragment_server_addip);
                _serverAddIpFragment.setArguments(b);
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentSlot, _serverAddIpFragment).addToBackStack("ServerFragment").commit();
            }else {
                getParentFragmentManager().beginTransaction().replace(R.id.fragmentSlot, _serverAddIpFragment).addToBackStack("ServerFragment").commit();
            }
        });
        return _layout;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateTextInputEditText();
    }

    private void updateTextInputEditText(){
        RemoteIpaddressData remoteIpaddressData = (RemoteIpaddressData) JsonFileIO.readJson(getContext(), JsonFileDefinition.IPADDRESS_JSON_NAME,RemoteIpaddressData.class);
        Optional.ofNullable(remoteIpaddressData).ifPresent(s->{
            _textInputEditText.setText(remoteIpaddressData.getIpaddress()+":"+remoteIpaddressData.getPort());
        });
    }
}
