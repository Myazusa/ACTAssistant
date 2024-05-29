package github.kutouzi.actassistant.util;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import github.kutouzi.actassistant.enums.ApplicationIndexDefinition;

public class MeituanUtil {
    private static final String _TAG = MeituanUtil.class.getName();
    public static int scanMeituanApplication(CharSequence packageName){
        if(packageName.toString().contains("meituan")){
            Log.i(_TAG,"美团正在运行于前台");
            return ApplicationIndexDefinition.MEITUAN;
        }
        return ApplicationIndexDefinition.NULLAPP;
    }

    public static void switchToVideo(AccessibilityNodeInfo nodeInfo){
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
