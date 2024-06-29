package github.kutouzi.actassistant.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.gsls.gt.GT;

import java.util.List;
import java.util.Optional;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.RemoteIpaddressData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.io.JsonFileIO;

public class ServerAddIpFragment extends Fragment {
    private int _layoutResId;
    private View _layout;
    private EditText _ip1EditText;
    private EditText _ip2EditText;
    private EditText _ip3EditText;
    private EditText _ip4EditText;
    private EditText _portEditText;
    private MaterialButton _setServerButton;
    private MaterialButton _unsetServerButton;
    private RemoteIpaddressData _remoteIpaddressData = null;
    private String _ipaddr = "";
    private String _port = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            _layoutResId = getArguments().getInt("layoutResId");
        }
        _layout = inflater.inflate(_layoutResId, container, false);
        _ip1EditText = _layout.findViewById(R.id.ip1EditView);
        _ip2EditText = _layout.findViewById(R.id.ip2EditView);
        _ip3EditText = _layout.findViewById(R.id.ip3EditView);
        _ip4EditText = _layout.findViewById(R.id.ip4EditView);
        _portEditText = _layout.findViewById(R.id.portEditText);

        _setServerButton = _layout.findViewById(R.id.setServerButton);
        _unsetServerButton = _layout.findViewById(R.id.unsetServerButton);

        _setServerButton.setOnClickListener(v->{
            // 判断空输入
            if(!TextUtils.isEmpty(_ip1EditText.getText().toString().trim())
                    || !TextUtils.isEmpty(_ip2EditText.getText().toString().trim())
                    || !TextUtils.isEmpty(_ip3EditText.getText().toString().trim())
                    || !TextUtils.isEmpty(_ip4EditText.getText().toString().trim())
                    || !TextUtils.isEmpty(_portEditText.getText().toString().trim())){
                // 判断是否符合ipv4
                if(Integer.parseInt(_ip1EditText.getText().toString()) <= 255
                        && Integer.parseInt(_ip2EditText.getText().toString()) <= 255
                        && Integer.parseInt(_ip3EditText.getText().toString()) <= 255
                        && Integer.parseInt(_ip4EditText.getText().toString()) <= 255){
                    _ipaddr = _ip1EditText.getText() + "." + _ip2EditText.getText() + "." + _ip3EditText.getText() + "." + _ip4EditText.getText();
                }else {
                    GT.toast_time("非IPv4地址",3000);
                    return;
                }
                // 判断是否是端口
                if(Integer.parseInt(_portEditText.getText().toString()) <=65535){
                    _port = _portEditText.getText().toString();
                }else {
                    GT.toast_time("非法端口号",3000);
                    return;
                }
                _remoteIpaddressData = new RemoteIpaddressData(_ipaddr, _port);
                Optional.of(_remoteIpaddressData).ifPresent(s->{
                    JsonFileIO.writeJson(getContext(),JsonFileDefinition.IPADDRESS_JSON_NAME,s);
                });
                getParentFragmentManager().popBackStack("ServerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }else{
                GT.toast_time("请输入地址和端口",3000);
            }
        });
        _unsetServerButton.setOnClickListener(v->{
            getParentFragmentManager().popBackStack("ServerFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

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
        String ipaddr = remoteIpaddressData.getIpaddress();
        String[] ipaddrs = ipaddr.split("\\.");
        _ip1EditText.setText(ipaddrs[0]);
        _ip2EditText.setText(ipaddrs[1]);
        _ip3EditText.setText(ipaddrs[2]);
        _ip4EditText.setText(ipaddrs[3]);
        _portEditText.setText(remoteIpaddressData.getPort());
    }
}
