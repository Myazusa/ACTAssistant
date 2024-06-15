package github.kutouzi.actassistant.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

public class XiaohongshuService extends ApplicationService {
    public static final int APPLICATION_INDEX = 6;
    public static final String PACKAGE_NAME = "com.xingin.xhs";
    public static final String NAME = "小红书";
    private static final String _TAG = XiaohongshuService.class.getName();
    private static final XiaohongshuService INSTANCE = new XiaohongshuService();
    public static XiaohongshuService getInsatance(){
        return INSTANCE;
    }
    private XiaohongshuService() {
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
