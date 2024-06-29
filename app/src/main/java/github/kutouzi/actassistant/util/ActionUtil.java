package github.kutouzi.actassistant.util;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.view.androidservice.ACTFloatingWindowService;

public class ActionUtil {
    private static final String _TAG = ActionUtil.class.getName();

    // 可执行动作
    private static Runnable pendingAction = null;

    private static final Handler handler = new Handler();

    private static void performSwipeUp(Resources resources,SwipeUpData swipeUpData,ACTFloatingWindowService actFloatingWindowService) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            float startX = getScreenWidth(resources) / 2f;
            float startY = getScreenHeight(resources) / 2f;
            float endX = startX;
            float endY = getScreenHeight(resources) / 16f;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);

            GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(
                    path, 0, RandomUtil.getRandomDelayTillis(swipeUpData.getRandomMinDelayValue(),swipeUpData.getRandomMaxDelayValue()));

            GestureDescription gesture = new GestureDescription.Builder()
                    .addStroke(stroke)
                    .build();
            actFloatingWindowService.dispatchGesture(gesture, null, null);
            Log.i(_TAG,"上划被调用");
        }
    }

    private static int getScreenWidth(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight(Resources resources) {
        return resources.getDisplayMetrics().heightPixels;
    }

    public static void processSwipe(Resources resources,SwipeUpData swipeUpData,ACTFloatingWindowService actFloatingWindowService) {
        if (pendingAction != null) {
            handler.removeCallbacks(pendingAction);
        }
        pendingAction = () -> {
            ActionUtil.performSwipeUp(resources,swipeUpData,actFloatingWindowService);
            handler.postDelayed(pendingAction, RandomUtil.getRandomDelayTillis(swipeUpData.getRandomMinSwipeupValue(),swipeUpData.getRandomMaxSwipeupValue()));
        };
        handler.post(pendingAction);
    }
    public static void removeSwipeAction(){
        handler.removeCallbacks(pendingAction);
    }

    public static boolean clickAction(AccessibilityNodeInfo nodeInfo, String text) {
        if(nodeInfo.findAccessibilityNodeInfosByText(text) != null){
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(text);
            for (AccessibilityNodeInfo info:
                    nodeInfos) {
                Log.i(_TAG,"找到'"+ text + "'节点");
                AccessibilityNodeInfo i = TraverseNodeUtil.traverseParent(info);
                if(i != null){
                    i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    public static boolean findClickAction(AccessibilityNodeInfo nodeInfo, String infoText,String targetText){
        if(nodeInfo.findAccessibilityNodeInfosByText(infoText) != null){
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(infoText);
            for (AccessibilityNodeInfo info:
                    nodeInfos) {
                AccessibilityNodeInfo unClickableParent = TraverseNodeUtil.traverseUnClickableParent(info);
                if(unClickableParent !=null){
                    if(unClickableParent.findAccessibilityNodeInfosByText(targetText) != null){
                        List<AccessibilityNodeInfo> infos = unClickableParent.findAccessibilityNodeInfosByText(targetText);
                        for (AccessibilityNodeInfo i:
                                infos) {
                            AccessibilityNodeInfo clickableParent = TraverseNodeUtil.traverseParent(i);
                            if(clickableParent !=null){
                                clickableParent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                return true;
                            }
                        }
                    };

                }
            }
            return false;
        }
        return false;
    }
    public static void returnAction(AccessibilityService accessibilityService,int layers){
        while (layers>0){
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            layers--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
