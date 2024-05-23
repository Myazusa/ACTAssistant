package github.kutouzi.actassistant.service;

import static github.kutouzi.actassistant.MainActivity.ACTION_ACT_SWITCH;
import static github.kutouzi.actassistant.MainActivity.ACTION_INTERRUPT_ACCESSIBILITY_SERVICE;
import static github.kutouzi.actassistant.entity.ApplicationDefinition.MEITUAN;
import static github.kutouzi.actassistant.entity.ApplicationDefinition.PINGDUODUO;
import static github.kutouzi.actassistant.entity.ApplicationDefinition.PINGDUODUO_PAKAGENAME;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.kutouzi.actassistant.exception.PakageNotFoundException;


public class ACTService extends AccessibilityService {



    private final String _TAG = getClass().getName();
    private static final Random _randomValue = new Random();

    // 控制每一个上划间隔的参数
    private static final int _RANDOM_MAX_SWIPEUP_VALUE = 14000;
    private static final int _RANDOM_MIN_SWIPEUP_VALUE = 10000;

    // 控制上划操作所耗时间的参数
    private static final int _RANDOM_MAX_DELAY_VALUE = 600;

    private static final int _RANDOM_MIN_DELAY_VALUE = 400;

    private Runnable pendingAction = null;

    private final Handler handler = new Handler();

    private static boolean isServiceInterrupted = true;
    private static boolean isProcessSwipe = false;
    private static int scanDialogFlag = 0;
    //TODO: 待解耦
    private static final List<String> meituanKeyWordList = (ArrayList<String>) Stream.of("现金秒到账", "開")
            .collect(Collectors.toList());

    private static final List<String> pingduoduoKeyWordList = (ArrayList<String>) Stream.of("领取今日奖励","专属现金红包","立即提现","领取今日现金","明日继续来领","点击观看下一集","打款金额")
            .collect(Collectors.toList());

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

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
        handler.removeCallbacks(pendingAction);
        processSwipe();
        isServiceInterrupted = false;
        isProcessSwipe = true;
        Log.i(_TAG,"上划继续");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(ACTION_INTERRUPT_ACCESSIBILITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(broadcastReceiver, filter, Context.RECEIVER_EXPORTED);
        }else {
            registerReceiver(broadcastReceiver, filter);
        }
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN) {
                onInterrupt();
                sendBroadcast(new Intent(ACTION_ACT_SWITCH));
                return true;
            }
            return false;
        }
        return super.onKeyEvent(event);
    }

    private int pingduoduoFunction(CharSequence packageName){
        if(packageName.toString().contains("pinduoduo")){
            Log.i(_TAG,"拼多多正在运行于前台");
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            if (nodeInfo != null){
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("多多视频");
                Log.i(_TAG,"发现"+ list.size()+"个符合条件的节点");
                for (AccessibilityNodeInfo info:
                        list) {
                    if(info.isClickable()){
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.i(_TAG,"已找到按钮并点击");
                    }
                }
            }
            return 1;
        }
        return 0;
    }
    private int meituanFunction(CharSequence packageName){
        if(packageName.toString().contains("meituan")){
            Log.i(_TAG,"美团正在运行于前台");
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            if (nodeInfo != null){
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("视频");
                Log.i(_TAG,"发现"+ list.size()+"个符合条件的节点");
                for (AccessibilityNodeInfo info:
                        list) {
                    if(info.isClickable()){
                        info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        Log.i(_TAG,"已找到按钮并点击");
                    }
                }
            }
            return 2;
        }
        return 0;
    }

    private void cancelDialog(List<String> list){
        for (String s: list) {
            if(getRootInActiveWindow().findAccessibilityNodeInfosByText(s) != null){
                List<AccessibilityNodeInfo> jiangliList = getRootInActiveWindow().findAccessibilityNodeInfosByText(s);
                for (AccessibilityNodeInfo info:
                        jiangliList) {
                    Log.i(_TAG,"发现'"+ s + "'节点");
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    AccessibilityNodeInfo i = traverseParent(info);
                    if(i != null){
                        i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }

    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if(event.getPackageName() != null){
                    //执行检查拼多多、美团等软件是否启动的逻辑
                    scanDialogFlag = pingduoduoFunction(event.getPackageName()) + meituanFunction(event.getPackageName());
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
                            cancelDialog(pingduoduoKeyWordList);
                            break;
                        case MEITUAN:
                            cancelDialog(meituanKeyWordList);
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
        handler.removeCallbacks(pendingAction);
        isServiceInterrupted = true;
        isProcessSwipe = false;
        Log.i(_TAG,"上划中断");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        Log.i(_TAG,"服务被释放");
    }

    private void performSwipeUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            float startX = getScreenWidth() / 2f;
            float startY = getScreenHeight() * 3 / 4f;
            float endX = startX;
            float endY = getScreenHeight() / 4f;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);

            GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(
                    path, 0, getRandomDelayTillis(_RANDOM_MIN_DELAY_VALUE,_RANDOM_MAX_DELAY_VALUE));

            GestureDescription gesture = new GestureDescription.Builder()
                    .addStroke(stroke)
                    .build();

            dispatchGesture(gesture, null, null);
            Log.i(_TAG,"上划被调用");
        }
    }
    // 单位毫秒
    private static int getRandomDelayTillis(int _RANDOM_MIN_VALUE,int _RANDOM_MAX_VALUE){
        return _RANDOM_MIN_VALUE + _randomValue.nextInt(_RANDOM_MAX_VALUE - _RANDOM_MIN_VALUE +1);
    }

    private void processSwipe() {
        if (!isServiceInterrupted) {
            return;
        }
        if (pendingAction != null) {
            handler.removeCallbacks(pendingAction);
        }
        pendingAction = () -> {
            performSwipeUp();
            if (!isServiceInterrupted) {
                handler.postDelayed(pendingAction, getRandomDelayTillis(_RANDOM_MIN_SWIPEUP_VALUE,_RANDOM_MAX_SWIPEUP_VALUE));
            }
        };
        handler.post(pendingAction);
    }

    private void traverseNodes(AccessibilityNodeInfo node, int depth) {
        if (node == null) {
            return;
        }

        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            prefix.append("--");
        }

        Log.i(_TAG, prefix + "Class: " + node.getClassName());
        Log.i(_TAG, prefix + "PackageName: " + node.getPackageName());
        Log.i(_TAG, prefix + "Text: " + node.getText());
        Log.i(_TAG, prefix + "ContentDescription: " + node.getContentDescription());
        Log.i(_TAG, prefix + "IsClickable: " + node.isClickable());

        // 递归遍历子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            traverseNodes(child, depth + 1);
        }

        // 回收节点资源，避免内存泄漏
        if (node.isAccessibilityFocused() && !node.isImportantForAccessibility()) {
            node.recycle();
        }
    }
    private AccessibilityNodeInfo traverseParent(AccessibilityNodeInfo node){
        AccessibilityNodeInfo parent = node.getParent();
        if(parent != null){
            if(parent.getClassName().toString().contains("ViewGroup") && parent.isClickable()){
                Log.i(_TAG,"找到符合条件的ViewGroup");
                return parent;
            }
            if(parent.getParent() == null){
                return null;
            }else {
                return traverseParent(parent.getParent());
            }
        }
        else {
            return null;
        }
    }
}
