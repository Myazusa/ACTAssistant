package github.kutouzi.actassistant.service;

import static github.kutouzi.actassistant.MainActivity.CREATE_OR_DESTROY_ACT_FLOATING_WINGDOW_SERVICE;
import static github.kutouzi.actassistant.MainActivity.windowView;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gsls.gt.GT;

import java.util.List;
import java.util.Objects;

import github.kutouzi.actassistant.MainActivity;
import github.kutouzi.actassistant.R;
import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.exception.FailedTaskException;
import github.kutouzi.actassistant.exception.PakageNotFoundException;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.util.ActionUtil;
import github.kutouzi.actassistant.util.DialogUtil;
import github.kutouzi.actassistant.util.DrawableUtil;
import github.kutouzi.actassistant.util.PackageCheckUtil;
import github.kutouzi.actassistant.util.RandomUtil;
import github.kutouzi.actassistant.view.button.ToggleButton;


public class ACTFloatingWindowService extends AccessibilityService {
    private static final String _TAG = ACTFloatingWindowService.class.getName();

    //////////////////////
    //悬浮窗窗体相关
    private FloatingActionButton _windowFloatButton;
    LinearLayout _functionButtonLayout;
    //////////////////////////////////


    //////////////////////
    //悬浮窗内按钮相关
    private ToggleButton _returnMainActivityButton;
    private ToggleButton _scanApplicationButton;
    private ToggleButton _listeningDialogButton;
    private ToggleButton _startApplicationButton;
    private ToggleButton _swipeUpButton;

    //////////////////////////////////


    //////////////////////
    //全局标记相关
    private static int _scanApplicationFlag = 0;
    private List<String> installedPackageList;
    private CountDownTimer countDownTimer = null;
    private static boolean _runTask = false;

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
                    // 创建悬浮开关
                    createFloatingButton();
                    // 再创建悬浮窗里的开关
                    createListeningDialogSwitch();
                    createStartApplicationSwitch();
                    createReturnMainActivitySwitch();
                    createSwipeUpSwitch();

                    createScanApplicationSwitch();
                    Log.i(_TAG,"悬浮窗已创建");

                }
            }
        }
    };

    private void createFloatingButton() {
        _windowFloatButton = windowView.findViewById(R.id.windowFloatButton);
        _functionButtonLayout = windowView.findViewById(R.id.functionButtonLayout);
        _functionButtonLayout.setVisibility(View.GONE);
        _windowFloatButton.setOnClickListener(v -> {
            if(_functionButtonLayout.getVisibility() == View.GONE){
                _functionButtonLayout.setVisibility(View.VISIBLE);
            }else {
                _functionButtonLayout.setVisibility(View.GONE);
            }
        });

    }
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
    private void createListeningDialogSwitch(){
        // 创建监听弹窗开关
        _listeningDialogButton = windowView.findViewById(R.id.listeningDialogButton);
        _listeningDialogButton.setEnabled(true);
        // 根据按钮状态开启和禁用
        _listeningDialogButton.setOnClickListener(v->{
            if (_listeningDialogButton.isToggled()){
                // 如果按钮已经被按过
                switchButtonColor(_listeningDialogButton);
                _listeningDialogButton.toggle();
                switchOtherButtonStates();
                cancelTimer();
                Log.i(_TAG,"监听弹窗开关已被禁用");
            }else {
                //switchFunctionToDialog();
                // 如果按钮没按过
                switchButtonColor(_listeningDialogButton);
                _listeningDialogButton.toggle();
                switchOtherButtonStates();
                Log.i(_TAG,"监听弹窗开关已被启用");
            }
        });
    }
    private void createStartApplicationSwitch(){
        // 创建开启应用开关
        _startApplicationButton = windowView.findViewById(R.id.startApplicationButton);
        installedPackageList = PackageCheckUtil.getInstalledPackageList(this);
        _startApplicationButton.setOnClickListener(v->{
            try {
                if(!_startApplicationButton.isToggled()){
                    String startPackageName = RandomUtil.getRandomPackage(installedPackageList);
                    Log.i(_TAG, "将打开：" + startPackageName);
                    requestStartApplication(startPackageName);
                    //OptionAutoSettingFragment.autoScanApplicationSwitch.isChecked()
                    if(true) {
                        startSwitchApplicationTimer();
                        Log.i(_TAG,"计时器已启用");
                    }
                    _startApplicationButton.toggle();
                    Thread.sleep(5000);
                    _scanApplicationButton.callOnClick();
                }else {
                    _startApplicationButton.toggle();
                }
            } catch (PakageNotFoundException e) {
                Log.i(_TAG, e.getMessage());
                GT.toast_time("未找到应用，切换下一个", 8000);
                _startApplicationButton.callOnClick();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void startSwitchApplicationTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
        countDownTimer = new CountDownTimer(Objects.requireNonNull(JsonFileIO.readSwitchApplicationDataJson(this, JsonFileDefinition.SWITCHAPP_JSON_NAME)).getSwitchApplicationTime(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                if(_listeningDialogButton.isToggled()){
                    _listeningDialogButton.callOnClick();
                }
                if(_swipeUpButton.isToggled()){
                    _swipeUpButton.callOnClick();
                }
                stopForegroundApplication();
                Log.i(_TAG, "计时器正常结束");
                _startApplicationButton.toggle();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                _startApplicationButton.callOnClick();
            }
        }.start();
    }

    private void cancelTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
            Log.i(_TAG, "计时器被取消");
            GT.toast_time("已暂停自动切换应用",2000);
        }
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
    private void createScanApplicationSwitch(){
        _scanApplicationButton = windowView.findViewById(R.id.scanApplicationButton);
        _scanApplicationButton.setOnClickListener(v -> {
            _scanApplicationFlag = PinduoduoService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName())
                    + MeituanService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName())
                    + DouyinjisuService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName())
                    + KuaishoujisuService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName())
                    + XiaohongshuService.getInsatance().scanApplication(getRootInActiveWindow().getPackageName());
            applicationAnnounce();
            findApplicationAction();
        });
    }
    private void findApplicationAction(){
        if(_scanApplicationFlag != 0){
            switch (_scanApplicationFlag){
                case PinduoduoService.APPLICATION_INDEX:
                    PinduoduoService.getInsatance().switchToVideo(getRootInActiveWindow());
                    break;
                case MeituanService.APPLICATION_INDEX:
                    MeituanService.getInsatance().switchToVideo(getRootInActiveWindow());
                    break;
                case DouyinjisuService.APPLICATION_INDEX:
                    DouyinjisuService.getInsatance().switchToVideo(getRootInActiveWindow());
                    break;
                case KuaishoujisuService.APPLICATION_INDEX:
                    KuaishoujisuService.getInsatance().switchToVideo(getRootInActiveWindow());
                    break;
                case BaidujisuService.APPLICATION_INDEX:
                    //BaidujisuService.getInsatance().switchToVideo(getRootInActiveWindow());
                    break;
                case XiaohongshuService.APPLICATION_INDEX:
                    XiaohongshuService.getInsatance().switchToVideo(getRootInActiveWindow());
                    break;
                default:
                    break;
            }
            if(!_swipeUpButton.isToggled()){
                // 如果没有被按下过
                _swipeUpButton.callOnClick();
            }
            if(!_listeningDialogButton.isToggled()){
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
                    //applicationAnnounceToast(BaidujisuService.NAME);
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
        _returnMainActivityButton = windowView.findViewById(R.id.returnMainActivityButton);
        _returnMainActivityButton.setOnClickListener(v->{
            onInterrupt();
        });
    }

    private void createSwipeUpSwitch() {
        //创建上划开关
        _swipeUpButton = windowView.findViewById(R.id.swipeUpButton);
        _swipeUpButton.setOnClickListener(v->{
            if(_swipeUpButton.isToggled()){
                // 如果上划按钮被按过
                // 停止上划
                ActionUtil.removeSwipeAction();
                switchButtonColor(_swipeUpButton);
                _swipeUpButton.toggle();
                switchOtherButtonStates();
                cancelTimer();
                GT.toast_time("上划结束",1000);
            }else {
                SwipeUpData swipeUpData = JsonFileIO.readSwipeUpDataJson(this, JsonFileDefinition.SWIPEUP_JSON_NAME);
                // 如果上划按钮没被按过
                // 直接开始上划
                ActionUtil.processSwipe(getResources(),swipeUpData,this);
                switchButtonColor(_swipeUpButton);
                _swipeUpButton.toggle();
                switchOtherButtonStates();
                GT.toast_time("上划开始",1000);
            }
        });
    }

    private void switchOtherButtonStates(){
        if(_swipeUpButton.isToggled() || _listeningDialogButton.isToggled()){
            if(_returnMainActivityButton.isClickable() && _startApplicationButton.isClickable() && _scanApplicationButton.isClickable()) {
                // 如果这两个按钮任意一个被按下过
                _returnMainActivityButton.setClickable(false);
                _startApplicationButton.setClickable(false);
                _scanApplicationButton.setClickable(false);
                DrawableUtil.setDrawableBackground(this, _returnMainActivityButton, 1, R.color.disable_button_rounded_color);
                DrawableUtil.setDrawableBackground(this, _startApplicationButton, 1, R.color.disable_button_rounded_color);
                DrawableUtil.setDrawableBackground(this, _scanApplicationButton, 1, R.color.disable_button_rounded_color);
                Log.i(_TAG, "悬浮窗开启应用按钮和返回按钮已禁用");
            }
        }else{
            if(!_returnMainActivityButton.isClickable() && !_startApplicationButton.isClickable() && !_scanApplicationButton.isClickable()) {
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
    }

    private void switchButtonColor(ToggleButton toggleButton){
        if (toggleButton.isToggled()){
            // 如果已经被按下，就变按下去的颜色
            DrawableUtil.setDrawableBackground(this, toggleButton,1,R.color.button_color);
        }else {
            // 如果没被按下过，就变没按下去的颜色
            DrawableUtil.setDrawableBackground(this, toggleButton,1,R.color.pressed_button_color);
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
        if(windowView != null){
            if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
                listeningDialogAccessibilityEvent(event);
            }
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                //checkPackageNameAccessibilityEvent(event);
            }
            if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
                if (_runTask){
                    if(getRootInActiveWindow()!=null){
                        switch (_scanApplicationFlag){
                            case PinduoduoService.APPLICATION_INDEX:
                                try{
                                    PinduoduoService.getInsatance().autoHeshuiTask(getRootInActiveWindow(),this);
                                }catch (FailedTaskException e){
                                    Log.w(_TAG,e.getMessage());
                                }
                                break;
                            case MeituanService.APPLICATION_INDEX:
                                try{
                                    MeituanService.getInsatance().autoCheckInTask(getRootInActiveWindow(),this);
                                }catch (FailedTaskException e){
                                    Log.w(_TAG,e.getMessage());
                                }
                                break;
                            case DouyinjisuService.APPLICATION_INDEX:
                                try{
                                    DouyinjisuService.getInsatance().autoCheckInTask(getRootInActiveWindow(),this);
                                }catch (FailedTaskException e){
                                    Log.w(_TAG,e.getMessage());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void listeningDialogAccessibilityEvent(AccessibilityEvent event){
        if (_listeningDialogButton.isToggled()) {
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
        MainActivity.removeFloatingWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        Log.i(_TAG, "服务被释放");
    }
}
