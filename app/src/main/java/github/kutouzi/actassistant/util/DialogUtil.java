package github.kutouzi.actassistant.util;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import github.kutouzi.actassistant.service.ACTFloatingWindowService;

public class DialogUtil {
    private static final String _TAG = DialogUtil.class.getName();
    public static void clickDialog(AccessibilityNodeInfo nodeInfo, ACTFloatingWindowService actFloatingWindowService, List<String> keyWordList) {
        for (String s: keyWordList) {
            if(nodeInfo.findAccessibilityNodeInfosByText(s) != null){
                List<AccessibilityNodeInfo> jiangliList = nodeInfo.findAccessibilityNodeInfosByText(s);
                for (AccessibilityNodeInfo info:
                        jiangliList) {
                    Log.i(_TAG,"发现'"+ s + "'节点");
                    actFloatingWindowService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    AccessibilityNodeInfo i = TraverseNodeUtil.traverseParent(info);
                    if(i != null){
                        i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
        }
    }

    public static void cancelDialog(AccessibilityNodeInfo nodeInfo,ACTFloatingWindowService actFloatingWindowService,List<String> keyWordList){
        for (String s:keyWordList) {
            if(!nodeInfo.findAccessibilityNodeInfosByText(s).isEmpty()){
                actFloatingWindowService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Log.i(_TAG,"找到'" + s + "'关闭了弹窗");
            }
        }
    }
}
