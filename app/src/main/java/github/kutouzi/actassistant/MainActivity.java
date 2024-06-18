package github.kutouzi.actassistant;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigationrail.NavigationRailView;

import java.util.List;

import github.kutouzi.actassistant.databinding.ActivityMainBinding;
import github.kutouzi.actassistant.service.ACTFloatingWindowService;
import github.kutouzi.actassistant.view.fragment.CilentListviewFragment;
import github.kutouzi.actassistant.view.fragment.OptionFragment;
import github.kutouzi.actassistant.view.fragment.UploadIpaddressFragment;

public class MainActivity extends AppCompatActivity  {
    public static final String CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE = "github.kutouzi.actassistant.CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE";

    private static final String _TAG = MainActivity.class.getName();
    private ActivityMainBinding _binding;
    private FloatingActionButton _startACTFloatingWindowServiceButton;
    private boolean _isStartACTFloatingWindowServiceButtonPressed = false;
    private OptionFragment _optionFragment = null;
    private CilentListviewFragment _cilentListviewFragment = null;
    private UploadIpaddressFragment _uploadIpaddressFragment = null;
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        createNavigationRailView();
        createStartFloatingServiceWindowSwitch();
    }
    private void createNavigationRailView(){
        NavigationRailView navigationRailView = findViewById(R.id.navigationRail);
        navigationRailView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menuList) {
                if (_cilentListviewFragment == null){
                    _cilentListviewFragment = new CilentListviewFragment();
                    Bundle b = new Bundle();
                    b.putInt("layoutResId", R.layout.fragment_cilent_listview);
                    _cilentListviewFragment.setArguments(b);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentSlot, _cilentListviewFragment)
                        .commit();
            }else if(itemId == R.id.menuOption) {
                if (_optionFragment == null){
                    _optionFragment = new OptionFragment();
                    Bundle b = new Bundle();
                    b.putInt("layoutResId", R.layout.fragment_option);
                    _optionFragment.setArguments(b);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentSlot, _optionFragment)
                        .commit();
            }else if (itemId == R.id.menuUpload) {
                if (_uploadIpaddressFragment == null){
                    _uploadIpaddressFragment = new UploadIpaddressFragment();
                    Bundle b = new Bundle();
                    b.putInt("layoutResId", R.layout.fragment_upload_ipaddress);
                    _uploadIpaddressFragment.setArguments(b);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentSlot, _uploadIpaddressFragment)
                        .commit();
            }else if(itemId == R.id.menuHelp){

            }
            return true;
        });
    }
    private void createStartFloatingServiceWindowSwitch(){
        // 创建开启ACT悬浮窗的开关按钮
        _startACTFloatingWindowServiceButton = findViewById(R.id.serviceWindowButton);

        // 根据按钮状态打开或移除悬浮窗
        _startACTFloatingWindowServiceButton.setOnClickListener(v -> {
            // 判断用户有无悬浮窗权限
            if (!Settings.canDrawOverlays(v.getContext())) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                Log.i(_TAG,"用户没有悬浮窗权限");
            }else {
                Log.i(_TAG, "用户已获取悬浮窗权限");
                // 判断此应用有没有在设置里获得无障碍模式权限
                AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
                List<AccessibilityServiceInfo> enabledAccessibilityServiceList = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_SPOKEN);
                boolean isAccessibilityEnabled = isAccessibilityServiceEnabled(getPackageName(), enabledAccessibilityServiceList);
                if (!isAccessibilityEnabled) {
                    // 权限未开启，引导用户去开启
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                    Log.i(_TAG, "用户没有无障碍权限");
                } else {
                    Log.i(_TAG, "用户已获取无障碍权限");
                    if (_isStartACTFloatingWindowServiceButtonPressed) {
                        // 向服务请求销毁悬浮窗
                        _isStartACTFloatingWindowServiceButtonPressed = false;
                    } else {
                        // 向服务请求开启悬浮窗
                        startService(new Intent(this, ACTFloatingWindowService.class));
                        requestCreateACTFloatingWindow();
                        // 隐藏此activity
                        moveTaskToBack(true);
                        _isStartACTFloatingWindowServiceButtonPressed = true;
                    }
                }
            }
        });
    }
    private void requestCreateACTFloatingWindow() {
        sendBroadcast(new Intent(CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE).putExtra("key","Create"));
    }

    private boolean isAccessibilityServiceEnabled(String serviceName, List<AccessibilityServiceInfo> enabledAccessibilityServiceList) {
        for (AccessibilityServiceInfo info : enabledAccessibilityServiceList) {
            if (info.getResolveInfo().serviceInfo.packageName.equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}