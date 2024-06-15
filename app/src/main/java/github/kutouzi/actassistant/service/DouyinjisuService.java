package github.kutouzi.actassistant.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class DouyinjisuService extends ApplicationService{
    public static final int APPLICATION_INDEX = 3;
    public static final String PACKAGE_NAME = "com.ss.android.ugc.aweme.lite";
    public static final String NAME = "抖音极速版";
    private static final String _TAG = DouyinjisuService.class.getName();
    private static final DouyinjisuService INSTANCE = new DouyinjisuService();
    public static DouyinjisuService getInsatance(){
        return INSTANCE;
    }
    private DouyinjisuService() {
        super();
    }
    @Override
    public int scanApplication(CharSequence packageName) {
        if(packageName.toString().equals(PACKAGE_NAME)){
            Log.i(_TAG,NAME + "正在运行于前台");
            return APPLICATION_INDEX;
        }
        return NullService.APPLICATION_INDEX;
    }

    @Override
    public void switchToVideo(AccessibilityNodeInfo nodeInfo) {

    }
}
