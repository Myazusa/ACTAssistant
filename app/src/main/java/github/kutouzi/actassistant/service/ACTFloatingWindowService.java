package github.kutouzi.actassistant.service;

import static github.kutouzi.actassistant.MainActivity.ACTION_ACT_SWITCH;
import static github.kutouzi.actassistant.MainActivity.ACTION_INTERRUPT_ACCESSIBILITY_SERVICE;
import static github.kutouzi.actassistant.enums.ApplicationDefinition.MEITUAN;
import static github.kutouzi.actassistant.enums.ApplicationDefinition.PINGDUODUO;
import static github.kutouzi.actassistant.enums.ApplicationDefinition.PINGDUODUO_PAKAGENAME;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

import java.util.Objects;

import github.kutouzi.actassistant.exception.PakageNotFoundException;
import github.kutouzi.actassistant.util.ActionUtil;
import github.kutouzi.actassistant.util.MeituanUtil;
import github.kutouzi.actassistant.util.PingduoduoUtil;


public class ACTFloatingWindowService extends AccessibilityService {
    private final String _TAG = getClass().getName();

    //悬浮窗相关
    private WindowManager _windowManager;

    private WindowManager.LayoutParams _layoutParams;

    private static boolean isServiceInterrupted = true;

    private static int scanDialogFlag = 0;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_INTERRUPT_ACCESSIBILITY_SERVICE.equals(intent.getAction())) {
                if(Objects.equals(intent.getStringExtra("key"), "Start")){
                    try {
                        startActivity(PINGDUODUO_PAKAGENAME);
                    }catch (PakageNotFoundException e){
                        Log.e(_TAG,e.getMessage());
                    }finally {
                        onInterrupt();
                        sendBroadcast(new Intent(ACTION_ACT_SWITCH));
                    }
                    Log.i(_TAG,"服务已被设置为Start");
                }else if (Objects.equals(intent.getStringExtra("key"), "Interrupt")){
                    onInterrupt();
                    Log.i(_TAG,"服务已被设置为Interrupt");
                }
            }
        }
    };

    private void startActivity(String pakageName) throws PakageNotFoundException {
        Intent intent = getPackageManager().getLaunchIntentForPackage(pakageName);
        if (intent != null){
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
        }else {
            throw new PakageNotFoundException(PINGDUODUO_PAKAGENAME);
        }

    }

    public void onContinue(){
        ActionUtil.removeSwipeAction();
        ActionUtil.processSwipe(_TAG,isServiceInterrupted,getResources(),this);
        isServiceInterrupted = false;
        Log.i(_TAG,"上划继续");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        _windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

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

        IntentFilter filter = new IntentFilter(ACTION_INTERRUPT_ACCESSIBILITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, filter, Context.RECEIVER_EXPORTED);
        }else {
            registerReceiver(broadcastReceiver, filter);
        }
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if(event.getPackageName() != null){
                    //执行检查拼多多、美团等软件是否启动的逻辑
                    scanDialogFlag = PingduoduoUtil.pingduoduoFunction(_TAG,event.getPackageName(),getRootInActiveWindow())
                            + MeituanUtil.meituanFunction(_TAG,event.getPackageName(),getRootInActiveWindow());
                    if(scanDialogFlag != 0 && isServiceInterrupted){
                        onContinue();
                        //它们在前台就自动启动开关
                        sendBroadcast(new Intent(ACTION_ACT_SWITCH));
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                //检查拼多多、美团等软件是否启动后才会进入搜索dialog逻辑
                if(event.getSource() != null){
                    switch (scanDialogFlag){
                        case PINGDUODUO:
                            new PingduoduoUtil().cancelDialog(_TAG,getRootInActiveWindow(),this);
                            break;
                        case MEITUAN:
                            new MeituanUtil().cancelDialog(_TAG,getRootInActiveWindow(),this);
                            break;
                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onInterrupt() {
        ActionUtil.removeSwipeAction();
        isServiceInterrupted = true;
        Log.i(_TAG,"上划中断");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        Log.i(_TAG,"服务被释放");
    }
}
