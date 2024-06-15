package github.kutouzi.actassistant.service;

import static github.kutouzi.actassistant.MainActivity.CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gsls.gt.GT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.config.ButtonStateConfig;
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.exception.PakageNotFoundException;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.util.ActionUtil;
import github.kutouzi.actassistant.util.DialogUtil;
import github.kutouzi.actassistant.util.DrawableUtil;
import github.kutouzi.actassistant.util.PackageCheckUtil;
import github.kutouzi.actassistant.util.RandomUtil;
import github.kutouzi.actassistant.view.ToggleButtonLayout;


public class ACTFloatingWindowService extends AccessibilityService {
    private static final String _TAG = ACTFloatingWindowService.class.getName();

    //////////////////////
    //悬浮窗窗体相关
    private WindowManager _windowManager;
    private View _windowView;
    //////////////////////////////////


    //////////////////////
    //悬浮窗内按钮相关
    private ToggleButtonLayout _returnMainActivityButton;
    private ToggleButtonLayout _scanApplicationButton;
    private ToggleButtonLayout _listeningDialogButton;
    private ToggleButtonLayout _startApplicationButton;
    private ToggleButtonLayout _swipeUpButton;
    private ToggleButtonLayout _autoScanApplicationButton;
    private TextView _autoScanApplicationButtonText;

    //////////////////////////////////


    //////////////////////
    //全局标记相关
    private static int _scanApplicationFlag = 0;
    private boolean _isViewAdded = false;
    private List<String> installedPackageList;
    private CountDownTimer countDownTimer = null;

    //////////////////////////////////

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE.equals(intent.getAction())) {
                if(Objects.equals(intent.getStringExtra("key"), "Create")){
                    if(_windowManager != null){
                        //如果这个窗口还存在，就是重复创建悬浮窗
                        if(!_isViewAdded){
                            _windowManager.addView(_windowView,getLayoutParams());
                        }
                    }else {
                        // 先创建悬浮窗
                        createFloatingWindow();
                        // 再创建悬浮窗里的开关
                        createListeningDialogSwitch();
                        createStartApplicationSwitch();
                        createReturnMainActivitySwitch();
                        createSwipeUpSwitch();
                        createAutoScanApplicationSwitch();

                        createScanApplicationSwitch();

                        Log.i(_TAG,"悬浮窗已创建");
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, filter, Context.RECEIVER_EXPORTED);
        }else {
            registerReceiver(broadcastReceiver, filter);
        }
    }

    private void createFloatingWindow(){
        //创建悬浮窗
        if(_windowManager == null) {
            _windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        }

        Display display = _windowManager.getDefaultDisplay();
        display.getSize(new Point());

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        _windowView = inflater.inflate(R.layout.window_view, null);

        if (_windowView != null) {
            _windowManager.addView(_windowView, getLayoutParams());
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

    private void removeFloatingWindow(){
        if (_windowView != null) {
            _windowManager.removeView(_windowView);
            _isViewAdded = false;
            Log.i(_TAG, "悬浮窗被移除");
        }
    }

    private void createListeningDialogSwitch(){
        // 创建监听弹窗开关
        _listeningDialogButton = _windowView.findViewById(R.id.listeningDialogButton);
        _listeningDialogButton.setEnabled(true);
        // 根据按钮状态开启和禁用
        _listeningDialogButton.setOnClickListener(v->{
            if (_listeningDialogButton._isToggle){
                // 如果按钮已经被按过
                switchButtonColor(_listeningDialogButton);
                _listeningDialogButton._isToggle = false;
                switchOtherButtonStates();
                Log.i(_TAG,"服务开关已被禁用");
            }else {
                //switchFunctionToDialog();
                // 如果按钮没按过
                switchButtonColor(_listeningDialogButton);
                _listeningDialogButton._isToggle = true;
                switchOtherButtonStates();
                Log.i(_TAG,"服务开关已被禁用");
            }
        });
    }

    private void createStartApplicationSwitch(){
        // 创建开启应用开关
        _startApplicationButton = _windowView.findViewById(R.id.startApplicationButton);
        installedPackageList = PackageCheckUtil.getInstalledPackageList(this);
        _startApplicationButton.setOnClickListener(v->{
            try {
                if(!_startApplicationButton._isToggle){
                    String startPackageName = RandomUtil.getRandomPackage(installedPackageList);
                    Log.i(_TAG, "将打开：" + startPackageName);
                    requestStartApplication(startPackageName);
                    startSwitchApplicationTimer();
                    _startApplicationButton._isToggle = true;
                    if(_autoScanApplicationButton._isToggle){
                        _scanApplicationButton.callOnClick();
                    }
                }else {
                    if (countDownTimer != null){
                        countDownTimer.cancel();
                        Log.i(_TAG, "计时器被取消");
                    }
                    _startApplicationButton._isToggle = false;
                }
            } catch (PakageNotFoundException e) {
                Log.i(_TAG, e.getMessage());
                GT.toast_time("未找到应用，切换下一个", 8000);
                _startApplicationButton.callOnClick();
            }
        });
    }
    private void startSwitchApplicationTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(Objects.requireNonNull(JsonFileIO.readSwitchApplicationDataJson(this, JsonFileDefinition.SWITCHAPP_JSON_NAME)).getSwitchApplicationTime(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                if(_listeningDialogButton._isToggle){
                    _listeningDialogButton.callOnClick();
                }
                if(_swipeUpButton._isToggle){
                    _swipeUpButton.callOnClick();
                }
                stopForegroundApplication();
                Log.i(_TAG, "计时器正常结束");
                _startApplicationButton._isToggle = false;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                _startApplicationButton.callOnClick();
            }
        }.start();
    }

    private void requestStartApplication(String applicationPackageNameDefinition) throws PakageNotFoundException {
        Intent intent = getPackageManager().getLaunchIntentForPackage(applicationPackageNameDefinition);
        if (intent != null){
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
        }else {
            throw new PakageNotFoundException(applicationPackageNameDefinition);
        }
    }

    private void stopForegroundApplication(){
        for (int i = 0; i < 10; i++) {
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }

    private void createAutoScanApplicationSwitch(){
        _autoScanApplicationButton = _windowView.findViewById(R.id.autoScanApplicationButton);
        _autoScanApplicationButton._isToggle = ButtonStateConfig.autoScanApplicationButtonState;
        _autoScanApplicationButtonText = _windowView.findViewById(R.id.autoScanApplicationButtonText);
        _autoScanApplicationButton.setOnClickListener(v -> {
            if(_autoScanApplicationButton._isToggle){
                _autoScanApplicationButtonText.setText("关闭自动");
                _autoScanApplicationButton._isToggle = false;
            }else {
                _autoScanApplicationButton._isToggle = true;
                _autoScanApplicationButtonText.setText("开启自动");
            }
            ButtonStateConfig.autoScanApplicationButtonState = _autoScanApplicationButton._isToggle;
        });
    }


    private void createScanApplicationSwitch(){
        _scanApplicationButton = _windowView.findViewById(R.id.scanApplicationButton);
        _scanApplicationButton.setOnClickListener(v -> {
            _scanApplicationFlag = PinduoduoService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName())
                    + MeituanService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName());
            applicationAnnounce();
            findApplicationAction();
        });
    }
    private void findApplicationAction(){
        if(_scanApplicationFlag != 0){
            switch (_scanApplicationFlag){
                case PinduoduoService.APPLICATION_INDEX:
                    PinduoduoService.getInsatance().switchToVideo(getRootInActiveWindow());
                case MeituanService.APPLICATION_INDEX:
                    MeituanService.getInsatance().switchToVideo(getRootInActiveWindow());
                case DouyinjisuService.APPLICATION_INDEX:
                    DouyinjisuService.getInsatance().switchToVideo(getRootInActiveWindow());
                case KuaishoujisuService.APPLICATION_INDEX:
                    KuaishoujisuService.getInsatance().switchToVideo(getRootInActiveWindow());
                case BaidujisuService.APPLICATION_INDEX:
                    BaidujisuService.getInsatance().switchToVideo(getRootInActiveWindow());
                case XiaohongshuService.APPLICATION_INDEX:
                    XiaohongshuService.getInsatance().switchToVideo(getRootInActiveWindow());
            }
            if(!_swipeUpButton._isToggle){
                // 如果没有被按下过
                _swipeUpButton.callOnClick();
            }
            if(!_listeningDialogButton._isToggle){
                // 如果没有被按下过
                _listeningDialogButton.callOnClick();
            }
        }
    }
    private void applicationAnnounce(){
        if(_scanApplicationFlag != 0) {
            switch (_scanApplicationFlag) {
                case PinduoduoService.APPLICATION_INDEX:
                    applicationAnnounceToast(PinduoduoService.NAME);
                    break;
                case MeituanService.APPLICATION_INDEX:
                    applicationAnnounceToast(MeituanService.NAME);
                    break;
                case DouyinjisuService.APPLICATION_INDEX:
                    applicationAnnounceToast(DouyinjisuService.NAME);
                    break;
                case KuaishoujisuService.APPLICATION_INDEX:
                    applicationAnnounceToast(KuaishoujisuService.NAME);
                    break;
                case BaidujisuService.APPLICATION_INDEX:
                    applicationAnnounceToast(BaidujisuService.NAME);
                    break;
                case XiaohongshuService.APPLICATION_INDEX:
                    applicationAnnounceToast(XiaohongshuService.NAME);
                    break;
                default:
                    GT.toast_time("没找到受支持的应用", 1000);
                    break;
            }
        }
    }
    private void applicationAnnounceToast(String s){
        GT.toast_time("找到" + s + "应用", 1000);
    }

    private void createReturnMainActivitySwitch(){
        //创建返回开关
        _returnMainActivityButton = _windowView.findViewById(R.id.returnMainActivityButton);
        _returnMainActivityButton.setOnClickListener(v->{
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            onInterrupt();
        });
    }

    private void createSwipeUpSwitch() {
        //创建上划开关
        _swipeUpButton = _windowView.findViewById(R.id.swipeUpButton);
        _swipeUpButton.setOnClickListener(v->{
            if(_swipeUpButton._isToggle){
                // 如果上划按钮被按过
                // 停止上划
                ActionUtil.removeSwipeAction();
                switchButtonColor(_swipeUpButton);
                _swipeUpButton._isToggle = false;
                switchOtherButtonStates();
                GT.toast_time("上划结束",1000);
            }else {
                SwipeUpData swipeUpData = JsonFileIO.readSwipeUpDataJson(this, JsonFileDefinition.SWIPEUP_JSON_NAME);
                // 如果上划按钮没被按过
                // 直接开始上划
                ActionUtil.processSwipe(getResources(),swipeUpData,this);
                switchButtonColor(_swipeUpButton);
                _swipeUpButton._isToggle = true;
                switchOtherButtonStates();
                GT.toast_time("上划开始",1000);
            }
        });
    }

    private void switchOtherButtonStates(){
        if(_swipeUpButton._isToggle || _listeningDialogButton._isToggle){
            // 如果这两个按钮任意一个被按下过
            _returnMainActivityButton.setClickable(false);
            _startApplicationButton.setClickable(false);
            _scanApplicationButton.setClickable(false);
            DrawableUtil.setDrawableBackground(this, _returnMainActivityButton, 1, R.color.disable_button_rounded_color);
            DrawableUtil.setDrawableBackground(this, _startApplicationButton, 1, R.color.disable_button_rounded_color);
            DrawableUtil.setDrawableBackground(this, _scanApplicationButton, 1, R.color.disable_button_rounded_color);
            Log.i(_TAG, "悬浮窗开启应用按钮和返回按钮已禁用");
        }else{
            // 如果没有被按下过
            _returnMainActivityButton.setClickable(true);
            _startApplicationButton.setClickable(true);
            _scanApplicationButton.setClickable(true);
            DrawableUtil.setDrawableBackground(this, _returnMainActivityButton, 1, R.color.button_color);
            DrawableUtil.setDrawableBackground(this, _startApplicationButton, 1, R.color.button_color);
            DrawableUtil.setDrawableBackground(this, _scanApplicationButton, 1, R.color.button_color);
            Log.i(_TAG, "悬浮窗开启应用按钮和返回按钮已启用");

        }
    }

    private void switchButtonColor(ToggleButtonLayout toggleButtonLayout){
        if (toggleButtonLayout._isToggle){
            // 如果已经被按下，就变按下去的颜色
            DrawableUtil.setDrawableBackground(this,toggleButtonLayout,1,R.color.button_color);
        }else {
            // 如果没被按下过，就变没按下去的颜色
            DrawableUtil.setDrawableBackground(this,toggleButtonLayout,1,R.color.pressed_button_color);
        }

    }
    private void switchFunctionToDialog(AccessibilityNodeInfo info){
        switch (_scanApplicationFlag) {
            case PinduoduoService.APPLICATION_INDEX:
                DialogUtil.cancelDialog(info, this,
                        Objects.requireNonNull(JsonFileIO.readKeyWordDataJson(this,JsonFileDefinition.KEYWORD_JSON_NAME)).getPingduoduoCancelableKeyWordList());
                break;
            case MeituanService.APPLICATION_INDEX:
                DialogUtil.cancelDialog(info, this,
                        Objects.requireNonNull(JsonFileIO.readKeyWordDataJson(this,JsonFileDefinition.KEYWORD_JSON_NAME)).getMeituanCancelableKeyWordList());
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(_windowView != null){
            if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
                listeningDialogAccessibilityEvent(event);
            }
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                //checkPackageNameAccessibilityEvent(event);
            }
        }
    }

    private void listeningDialogAccessibilityEvent(AccessibilityEvent event){
        if (_listeningDialogButton._isToggle) {
            if(event.getSource() != null){
                switchFunctionToDialog(event.getSource());
            }
        }
    }

    private void checkPackageNameAccessibilityEvent(AccessibilityEvent event){
        CharSequence packageName = event.getPackageName();
        if (packageName != null) {
            Log.d(_TAG, "前台应用包名为: " + packageName);
        }
    }

    @Override
    public void onInterrupt() {
        removeFloatingWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        Log.i(_TAG, "服务被释放");
    }
}
