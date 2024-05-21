package github.kutouzi.actassistant.service;

import static github.kutouzi.actassistant.MainActivity.ACTION_INTERRUPT_ACCESSIBILITY_SERVICE;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

// 拼多多：com.xunmeng.pinduoduo

public class ACTService extends AccessibilityService {



    private final String _TAG = getClass().getName();
    private static final Random _randomValue = new Random();

    // 控制每一个上划间隔的参数
    private static final int _RANDOM_MAX_SWIPEUP_VALUE = 24000;
    private static final int _RANDOM_MIN_SWIPEUP_VALUE = 12000;

    // 控制上划操作所耗时间的参数
    private static final int _RANDOM_MAX_DELAY_VALUE = 600;

    private static final int _RANDOM_MIN_DELAY_VALUE = 400;

    private Runnable pendingAction = null;

    private final Handler handler = new Handler();

    private static boolean isServiceInterrupted = true;

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
                    processSwipe();
                    isServiceInterrupted = false;
                    Log.i(_TAG,"服务已被设置为Start");
                }else if (Objects.equals(intent.getStringExtra("key"), "Interrupt")){
                    onInterrupt();
                    Log.i(_TAG,"服务已被设置为Interrupt");
                }
            }
        }
    };

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
                return true;
            }
            return false;
        }
        return super.onKeyEvent(event);
    }

    private void pingduoduoFunction(CharSequence packageName){
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
        }
    }
    private void meituanFunction(CharSequence packageName){
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
        }
    }

    private void meituanCancelJinrijiangli(AccessibilityNodeInfo rootInfo){
        if(rootInfo.findAccessibilityNodeInfosByText("领取今日奖励") != null){
            List<AccessibilityNodeInfo> jiangliList = rootInfo.findAccessibilityNodeInfosByText("领取今日奖励");
            for (AccessibilityNodeInfo info:
                    jiangliList) {
                traverseParent(info).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if(event.getPackageName() != null){
                    //执行拼多多逻辑
                    pingduoduoFunction(event.getPackageName());
                    //执行美团逻辑
                    meituanFunction(event.getPackageName());
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if(!isServiceInterrupted){
                    if(event.getSource() != null){
                        meituanCancelJinrijiangli(event.getSource());
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
        Log.i(_TAG,"服务被中断");
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
        if(node.getParent() != null){
            if(node.getClassName().toString().contains("ViewGroup") && node.getParent().isClickable()){
                Log.i(_TAG,"找到符合条件的ViewGroup");
                return node.getParent();
            }
        }
        return traverseParent(node.getParent());
    }
}
