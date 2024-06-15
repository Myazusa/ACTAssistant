package github.kutouzi.actassistant.service;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class PinduoduoService extends ApplicationService{
    public static final int APPLICATION_INDEX = 1 ;
    public static final String PACKAGE_NAME = "com.xunmeng.pinduoduo";
    public static final String NAME = "拼多多";
    public static String CLICKABLE_KEYWORD_LIST = "拼多多点击列表";
    public static String CANCELABLE_KEYWORD_LIST = "拼多多取消列表";
    private static final String _TAG = PinduoduoService.class.getName();
    private static final PinduoduoService INSTANCE = new PinduoduoService();
    public static PinduoduoService getInsatance(){
        return INSTANCE;
    }
    private PinduoduoService() {
        super();
    }

    @Override
    public int scanApplication(CharSequence packageName){
        if(packageName.toString().equals(PACKAGE_NAME)){
            Log.i(_TAG,NAME + "正在运行于前台");
            return APPLICATION_INDEX;
        }
        return NullService.APPLICATION_INDEX;
    }
    @Override
    public void switchToVideo(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo != null){
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("多多视频");
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
