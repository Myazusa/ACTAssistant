package github.kutouzi.actassistant.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class NullService extends ApplicationService{
    public static final int APPLICATION_INDEX = 0 ;
    public static final String NAME = "无应用";
    private static final String _TAG = NullService.class.getName();

    @Override
    public int scanApplication(CharSequence packageName) {
        Log.i(_TAG,NAME + "正在运行于前台");
        return NullService.APPLICATION_INDEX;
    }

    @Override
    public void switchToVideo(AccessibilityNodeInfo nodeInfo) {

    }
}
