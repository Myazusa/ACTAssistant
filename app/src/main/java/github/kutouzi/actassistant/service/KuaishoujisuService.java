package github.kutouzi.actassistant.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class KuaishoujisuService extends ApplicationService{
    public static final int APPLICATION_INDEX = 4;
    public static final String PACKAGE_NAME = "com.kuaishou.nebula";
    public static final String NAME = "快手极速版";
    private static final String _TAG = KuaishoujisuService.class.getName();
    private static final KuaishoujisuService INSTANCE = new KuaishoujisuService();
    public static KuaishoujisuService getInsatance(){
        return INSTANCE;
    }
    private KuaishoujisuService() {
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
