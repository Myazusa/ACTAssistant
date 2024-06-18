package github.kutouzi.actassistant.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class BaidujisuService extends ApplicationService{
    public static final int APPLICATION_INDEX = 5;
    public static final String PACKAGE_NAME = "com.baidu.searchbox.lite";
    public static final String NAME = "百度极速版";
    private static final String _TAG = BaidujisuService.class.getName();
    private static final BaidujisuService INSTANCE = new BaidujisuService();
    public static BaidujisuService getInsatance(){
        return INSTANCE;
    }
    private BaidujisuService() {
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
