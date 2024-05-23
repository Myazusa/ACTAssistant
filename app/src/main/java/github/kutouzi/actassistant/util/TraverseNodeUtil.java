package github.kutouzi.actassistant.util;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class TraverseNodeUtil {
    public static AccessibilityNodeInfo traverseParent(String TAG,AccessibilityNodeInfo node){
        AccessibilityNodeInfo parent = node.getParent();
        if(parent != null){
            if(parent.getClassName().toString().contains("ViewGroup") && parent.isClickable()){
                Log.i(TAG,"找到符合条件的ViewGroup");
                return parent;
            }
            if(parent.getParent() == null){
                return null;
            }else {
                return traverseParent(TAG,parent.getParent());
            }
        }
        else {
            return null;
        }
    }

    public void traverseNodes(String TAG,AccessibilityNodeInfo node, int depth) {
        if (node == null) {
            return;
        }

        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            prefix.append("--");
        }

        Log.i(TAG, prefix + "Class: " + node.getClassName());
        Log.i(TAG, prefix + "PackageName: " + node.getPackageName());
        Log.i(TAG, prefix + "Text: " + node.getText());
        Log.i(TAG, prefix + "ContentDescription: " + node.getContentDescription());
        Log.i(TAG, prefix + "IsClickable: " + node.isClickable());

        // 递归遍历子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            AccessibilityNodeInfo child = node.getChild(i);
            traverseNodes(TAG,child, depth + 1);
        }

        // 回收节点资源，避免内存泄漏
        if (node.isAccessibilityFocused() && !node.isImportantForAccessibility()) {
            node.recycle();
        }
    }
}
