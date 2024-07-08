package github.kutouzi.actassistant.util;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.androidservice.ACTFloatingWindowService;

public class ActionUtil {
    private static final String _TAG = ActionUtil.class.getName();

    // 可执行动作
    private static Runnable pendingAction = null;

    private static final Handler handler = new Handler();

    /**
     * 点击指定坐标
     * @param service 传入无障碍服务对象
     * @param x 从左上开始的屏幕横向偏移量
     * @param y 从左上开始的屏幕纵向偏移量
     * @return 返回是否成功点击
     */
    public static boolean performClick(AccessibilityService service , int x, int y){
        Path clickPath = new Path();
        clickPath.moveTo(x, y);

        GestureDescription.StrokeDescription strokeDescription = new GestureDescription.StrokeDescription(clickPath, 0, 100);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(strokeDescription);

        boolean result = service.dispatchGesture(gestureBuilder.build(), new AccessibilityService.GestureResultCallback() {}, null);

        return result;
    }

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

    /**
     * 获取目标文本，直接检测它是否可以点击，如果可以就点击它
     * @param nodeInfo 传入节点
     * @param text 传入要搜索的文本
     * @return 返回是否找到
     */
    public static boolean findTargetClickableNodesAction(AccessibilityNodeInfo nodeInfo, String text){
        int flag = 0;
        if (nodeInfo.findAccessibilityNodeInfosByText(text) != null){
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(text);
            for (AccessibilityNodeInfo info:
                    nodeInfos) {
                if(info.isClickable()){
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    flag++;
                }
            }
        }
        return flag >0;
    }

    /**
     * 获取目标文本的节点后，遍历父节点找第一个为ViewGroup且可点击的节点，并点击符合要求的节点
     * @param nodeInfo 传入节点
     * @param text 传入要搜索的文本
     * @return 返回是否找到
     */
    public static boolean findParentsClickableNodesAction(AccessibilityNodeInfo nodeInfo, String text) {
        int flag = 0;
        if(nodeInfo.findAccessibilityNodeInfosByText(text) != null){
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(text);
            for (AccessibilityNodeInfo info:
                    nodeInfos) {
                Log.i(_TAG,"找到'"+ text + "'节点");
                AccessibilityNodeInfo i = TraverseNodeUtil.traverseParent(info);
                if(i != null){
                    i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    flag++;
                }
            }
        }
        return flag > 0;
    }

    /**
     * 搜索所有相邻兄弟节点下的子节点，并点击符合要求的节点
     * @param nodeInfo 传入节点
     * @param targetText 传入要搜索的文本
     * @param deep 传入要向上搜索多少层父节点
     * @return 返回是否找到
     */
    public static boolean findSiblingsChildClickableNodesAction(AccessibilityNodeInfo nodeInfo, String targetText,int deep){
        int flag =0;
        if(nodeInfo.findAccessibilityNodeInfosByText(targetText) != null){
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(targetText);
            for (AccessibilityNodeInfo info:
                    nodeInfos) {
                AccessibilityNodeInfo result = info;
                for (int i = 0; i < deep; i++) {
                    result = getDeepParent(result);
                }
                List<AccessibilityNodeInfo> nodeInfoList = TraverseNodeUtil.traverseChilds(result);
                for (AccessibilityNodeInfo node: nodeInfoList) {
                    if(node != null){
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        flag++;
                    }
                }
            }
        }
        return flag > 0;
    }

    private static AccessibilityNodeInfo getDeepParent(AccessibilityNodeInfo node){
        return node.getParent();
    }

    /**
     * 向父节点找第一个为ViewGroup的节点，并点击符合要求的节点
     * （双文本定位法）
     * @param nodeInfo 传入节点
     * @param infoText 传入辅助搜索的文本
     * @param targetText 传入要搜索的目标文本（如按钮上的文本）
     * @return 返回是否找到
     */
    public static boolean findSiblingsClickableNodesAction(AccessibilityNodeInfo nodeInfo, String infoText, String targetText){
        int flag = 0;
        if(nodeInfo.findAccessibilityNodeInfosByText(infoText) != null){
            // 得到包含这个文本的节点
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(infoText);
            // 遍历包含这个文本的节点
            for (AccessibilityNodeInfo info:
                    nodeInfos) {
                // 向父节点寻找含有ClassName为ViewGroup的节点，不关心是否可以点击
                AccessibilityNodeInfo unClickableParent = TraverseNodeUtil.traverseUnClickableParent(info);
                if(unClickableParent !=null){
                    // 找到父节点后，查找父节点中子节点是否有包含可点击目标文本的ViewGroup节点
                    if(unClickableParent.findAccessibilityNodeInfosByText(targetText) != null){
                        List<AccessibilityNodeInfo> infos = unClickableParent.findAccessibilityNodeInfosByText(targetText);
                        // 调用遍历父节点，寻找可以点击且ClassName为ViewGroup的节点
                        for (AccessibilityNodeInfo i:
                                infos) {
                            AccessibilityNodeInfo clickableParent = TraverseNodeUtil.traverseParent(i);
                            if(clickableParent !=null){
                                clickableParent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                flag++;
                            }
                        }
                    };

                }
            }
        }
        return flag > 0;
    }

    public static boolean findParentsClickableInWindowAction(List<AccessibilityWindowInfo> windowInfoList,String text){
        int flag = 0;
        for (AccessibilityWindowInfo window:windowInfoList) {
            AtomicReference<List<AccessibilityNodeInfo>> accessibilityNodeInfosByText = new AtomicReference<>(new ArrayList<>());
            Optional.of(window.getRoot()).ifPresent(s->{
                accessibilityNodeInfosByText.set(s.findAccessibilityNodeInfosByText(text));
            });
            if(accessibilityNodeInfosByText.get() != null){
                for (AccessibilityNodeInfo info: accessibilityNodeInfosByText.get()) {
                    AccessibilityNodeInfo i = TraverseNodeUtil.traverseParent(info);
                    if(i != null){
                        i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        flag++;
                    }
                }
            }
        }
        return flag > 0;
    }

    /**
     * 返回上一层级
     * @param accessibilityService 传入无障碍服务对象
     * @param layers 传入层级
     */
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
