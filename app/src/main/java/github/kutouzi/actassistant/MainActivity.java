package github.kutouzi.actassistant;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import github.kutouzi.actassistant.adapter.ClientViewAdapter;
import github.kutouzi.actassistant.databinding.ActivityMainBinding;
import github.kutouzi.actassistant.entity.ClientViewData;

public class MainActivity extends AppCompatActivity  {

    static {
        System.loadLibrary("actassistant");
    }

    public static final String ACTION_INTERRUPT_ACCESSIBILITY_SERVICE = "github.kutouzi.actassistant.ACTION_INTERRUPT_ACCESSIBILITY_SERVICE";


    private final String _TAG = getClass().getName();
    private ActivityMainBinding _binding;
    private WindowManager windowManager;

    // 除服务开关外，其他所有按钮的状态，false表示全部禁用
    private boolean _allButtonInWindowStartedState = true;
    private boolean _allButtonInServiceStartedState = true;



    private ImageButton _serviceWindowButton;
    private ImageButton _settingButton;
    private ImageButton _addClientButton;
    private RecyclerView _clientRecyclerView;
    private View _windowView;

    private ImageButton _returnMainActivityButton;

    private WindowManager.LayoutParams _layoutParams;

    private View _actServiceButton;

    private final int _recyclerViewSpanCount = 3;
    private boolean _isStartServiceWindowButtonPressed = false;

    private boolean _isACTServiceButtonPressed = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());


        CreateClientView();
        CreateSettingSwitch();
        CreateAddClientSwitch();
        CreateServiceWindowSwitch();
    }

    private void CreateClientView(){
        // 寻找clientRecyclerView的xml资源
        _clientRecyclerView = findViewById(R.id.clientView);

        // 设置布局管理器
        GridLayoutManager layoutManager = new GridLayoutManager(this, _recyclerViewSpanCount, GridLayoutManager.VERTICAL, false);
        _clientRecyclerView.setLayoutManager(layoutManager);

        // 创建数据集
        List<ClientViewData> clientViewData = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            clientViewData.add(new ClientViewData("192.168.1." + i, R.drawable.connect_lost));
        }

        // 创建adapter，准备为视图提供数据
        ClientViewAdapter adapter = new ClientViewAdapter(clientViewData);

        // 通过adapter把数据集绑定到clientRecyclerView以显示
        _clientRecyclerView.setAdapter(adapter);
    }

    private void CreateServiceWindowSwitch(){
        // 创建悬浮窗开关按钮
        _serviceWindowButton = findViewById(R.id.serviceWindowButton);

        // 初始化悬浮窗管理对象
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // 设置悬浮窗参数
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }else {
            _layoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
        }

        // 根据按钮状态打开或移除悬浮窗
        _serviceWindowButton.setOnClickListener(v -> {
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
                    if (_isStartServiceWindowButtonPressed) {
                        _isStartServiceWindowButtonPressed = false;
                        SwitchAllButtonInWindowStarted();

                    } else {
                        Display display = windowManager.getDefaultDisplay();
                        display.getSize(new Point());
                        _layoutParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        _windowView = inflater.inflate(R.layout.window_view, null);

                        if (_windowView != null) {
                            windowManager.addView(_windowView, _layoutParams);
                        }

                        // 创建悬浮窗里的开关
                        CreateWindowACTServiceSwitch();
                        CreateReturnMainActivitySwitch();

                        moveTaskToBack(true);

                        _isStartServiceWindowButtonPressed = true;
                        SwitchAllButtonInWindowStarted();

                    }
                }
            }
        });
    }

    private void CreateWindowACTServiceSwitch(){
        // 创建ACT服务开关
        _actServiceButton = _windowView.findViewById(R.id.actServiceButton);
        // 根据按钮状态开关ACT服务
        _actServiceButton.setOnClickListener(v->{
            if (_isACTServiceButtonPressed){
                InterruptACTService();
                _isACTServiceButtonPressed = false;
                _actServiceButton.setBackgroundColor(Color.parseColor("#FFAAFDF4"));
                SwitchAllButtonInServiceStarted();
            }else {
                StartACTService();
                _isACTServiceButtonPressed = true;
                _actServiceButton.setBackgroundColor(Color.parseColor("#FFFDAAAA"));
                SwitchAllButtonInServiceStarted();
            }
        });
    }

    private void CreateReturnMainActivitySwitch(){
        _returnMainActivityButton = _windowView.findViewById(R.id.returnMainActivityButton);
        _returnMainActivityButton.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            windowManager.removeView(_windowView);
        });
    }
    private void SwitchAllButtonInServiceStarted(){
        if(!_allButtonInServiceStartedState) {
            _returnMainActivityButton.setEnabled(true);
            _returnMainActivityButton.setBackgroundColor(Color.parseColor("#FFAAFDF4"));
            _allButtonInServiceStartedState = true;
            Log.i(_TAG,"悬浮窗其他按钮已启用");
        }else {
            _returnMainActivityButton.setEnabled(false);
            _returnMainActivityButton.setBackgroundColor(Color.parseColor("#FF979797"));
            _allButtonInServiceStartedState = false;
            Log.i(_TAG,"悬浮窗其他按钮已禁用");
        }
    }

    private void SwitchAllButtonInWindowStarted(){
        if(!_allButtonInWindowStartedState){
            _settingButton.setEnabled(true);
            _addClientButton.setEnabled(true);
            _allButtonInWindowStartedState = true;
            Log.i(_TAG,"主菜单其他按钮已启用");
        }else {
            _settingButton.setEnabled(false);
            _addClientButton.setEnabled(false);
            _allButtonInWindowStartedState = false;
            Log.i(_TAG,"主菜单其他按钮已禁用");
        }

    }
    private void CreateSettingSwitch(){
        _settingButton = findViewById(R.id.settingButton);
    }

    private void CreateAddClientSwitch(){
        _addClientButton = findViewById(R.id.addClientButton);
    }
    private void StartACTService() {
        sendBroadcast(new Intent(ACTION_INTERRUPT_ACCESSIBILITY_SERVICE).putExtra("key","Start"));
    }

    private void InterruptACTService() {
        sendBroadcast(new Intent(ACTION_INTERRUPT_ACCESSIBILITY_SERVICE).putExtra("key","Interrupt"));
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