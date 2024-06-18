package github.kutouzi.actassistant;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigationrail.NavigationRailView;

import java.util.List;

import github.kutouzi.actassistant.databinding.ActivityMainBinding;
import github.kutouzi.actassistant.service.ACTFloatingWindowService;
import github.kutouzi.actassistant.util.FragmentUtil;
import github.kutouzi.actassistant.view.fragment.CilentListviewFragment;
import github.kutouzi.actassistant.view.fragment.OptionFragment;
import github.kutouzi.actassistant.view.fragment.UploadIpaddressFragment;


//使用./gradlew assembleRelease来生成apk
public class MainActivity extends AppCompatActivity  {
    private static final String _TAG = MainActivity.class.getName();

    //////////////////////
    //全局变量相关
    private boolean _isStartACTFloatingWindowServiceButtonPressed = false;
    public static final String CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE = "github.kutouzi.actassistant.CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE";

    //////////////////////////////////


    //////////////////////
    //组件相关
    private ActivityMainBinding _binding;
    private FloatingActionButton _startACTFloatingWindowServiceButton;

    //////////////////////////////////


    //////////////////////
    //Fragment相关
    private OptionFragment _optionFragment = null;
    private CilentListviewFragment _cilentListviewFragment = null;
    private UploadIpaddressFragment _uploadIpaddressFragment = null;

    //////////////////////////////////


    //////////////////////
    //悬浮窗相关
    private static WindowManager windowManager;
    public static View windowView;
    private static boolean _isViewAdded = false;

    //////////////////////////////////
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
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentSlot, _cilentListviewFragment).hide(_cilentListviewFragment).commit();
                }
                FragmentUtil.switchFragment(getSupportFragmentManager(),_cilentListviewFragment);
            }else if(itemId == R.id.menuOption) {
                if (_optionFragment == null){
                    _optionFragment = new OptionFragment();
                    Bundle b = new Bundle();
                    b.putInt("layoutResId", R.layout.fragment_option);
                    _optionFragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentSlot, _optionFragment).hide(_optionFragment).commit();
                }
                FragmentUtil.switchFragment(getSupportFragmentManager(),_optionFragment);
            }else if (itemId == R.id.menuUpload) {
                if (_uploadIpaddressFragment == null){
                    _uploadIpaddressFragment = new UploadIpaddressFragment();
                    Bundle b = new Bundle();
                    b.putInt("layoutResId", R.layout.fragment_upload_ipaddress);
                    _uploadIpaddressFragment.setArguments(b);
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentSlot, _uploadIpaddressFragment).hide(_uploadIpaddressFragment).commit();
                }
                FragmentUtil.switchFragment(getSupportFragmentManager(),_uploadIpaddressFragment);
            }else if(itemId == R.id.menuHelp){

            }
            return true;
        });
        MenuItem item = navigationRailView.getMenu().findItem(R.id.menuList);
        navigationRailView.getMenu().performIdentifierAction(item.getItemId(), 0);
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
                        createFloatingWindow();
                        // 向服务请求开启悬浮窗
                        startService(new Intent(getApplicationContext(), ACTFloatingWindowService.class));
                        requestCreateACTFloatingWindow();
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
    private void createFloatingWindow(){
        //创建悬浮窗
        if(windowManager == null) {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }

        Display display = windowManager.getDefaultDisplay();
        display.getSize(new Point());

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        windowView = inflater.inflate(R.layout.window_view, null);

        if (windowView != null) {
            windowManager.addView(windowView, getLayoutParams());
            _isViewAdded = true;
        }
    }
    @NonNull
    private static WindowManager.LayoutParams getLayoutParams() {
        WindowManager.LayoutParams _layoutParams;
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
        _layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        return _layoutParams;
    }
    public static void removeFloatingWindow(){
        if (windowView != null) {
            windowManager.removeView(windowView);
            _isViewAdded = false;
            Log.i(_TAG, "悬浮窗被移除");
        }
    }
}