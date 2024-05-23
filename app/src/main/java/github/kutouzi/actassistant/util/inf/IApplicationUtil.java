package github.kutouzi.actassistant.util.inf;

import android.view.accessibility.AccessibilityNodeInfo;

import github.kutouzi.actassistant.service.ACTFloatingWindowService;

public interface IApplicationUtil {

    //请传入根节点getRootInActiveWindow()以扩大范围方便查询
    void cancelDialog(String TAG, AccessibilityNodeInfo nodeInfo, ACTFloatingWindowService actFloatingWindowService);

}
