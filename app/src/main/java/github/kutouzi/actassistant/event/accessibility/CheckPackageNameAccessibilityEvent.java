package github.kutouzi.actassistant.event.accessibility;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class CheckPackageNameAccessibilityEvent {
    static String _TAG = CheckPackageNameAccessibilityEvent.class.getName();
    public static void checkPackageNameAccessibilityEvent(AccessibilityEvent event){
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            CharSequence packageName = event.getPackageName();
            if (packageName != null) {
                Log.d(_TAG, "前台应用包名为: " + packageName);
            }
        }
    }
}
