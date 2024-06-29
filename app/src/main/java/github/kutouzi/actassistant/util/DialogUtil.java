package github.kutouzi.actassistant.util;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import github.kutouzi.actassistant.view.androidservice.ACTFloatingWindowService;

public class DialogUtil {
    private static final String _TAG = DialogUtil.class.getName();
    private static Set<String> previousChildNodes = new HashSet<>();
//    public static void clickDialog(AccessibilityNodeInfo nodeInfo, List<String> keyWordList) {
//        for (String s: keyWordList) {
//            if(nodeInfo.findAccessibilityNodeInfosByText(s) != null){
//                List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(s);
//                for (AccessibilityNodeInfo info:
//                        nodeInfos) {
//                    Log.i(_TAG,"发现'"+ s + "'节点");
//                    AccessibilityNodeInfo i = TraverseNodeUtil.traverseParent(info);
//                    if(i != null){
//                        i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    }
//                }
//            }
//        }
//    }


    public static void cancelDialog(AccessibilityNodeInfo nodeInfo,AccessibilityService service,List<String> keyWordList){
        for (String s:keyWordList) {
            if(!nodeInfo.findAccessibilityNodeInfosByText(s).isEmpty()){
                service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                Log.i(_TAG,"找到'" + s + "'关闭了弹窗");
            }
        }
    }
}
