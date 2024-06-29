package github.kutouzi.actassistant.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import github.kutouzi.actassistant.exception.FailedTaskException;
import github.kutouzi.actassistant.util.ActionUtil;

public class MeituanService extends ApplicationService{
    public static final int APPLICATION_INDEX = 2 ;
    public static final String PACKAGE_NAME = "com.sankuai.meituan";
    public static final String NAME = "美团";
    public static String CLICKABLE_KEYWORD_LIST = "美团点击列表";
    public static String CANCELABLE_KEYWORD_LIST = "美团取消列表";
    private static final String _TAG = MeituanService.class.getName();
    private static final MeituanService INSTANCE = new MeituanService();
    public static MeituanService getInsatance(){
        return INSTANCE;
    }
    private MeituanService() {
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
    public void autoCheckInTask(AccessibilityNodeInfo nodeInfo, AccessibilityService accessibilityService) throws FailedTaskException {
        int layers = 0;
        if(!ActionUtil.clickAction(nodeInfo,"去提现")){
            ActionUtil.returnAction(accessibilityService,layers);
            throw new FailedTaskException("未能打开任务页");
        }
        layers++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!ActionUtil.clickAction(nodeInfo,"去打卡")){
            ActionUtil.returnAction(accessibilityService,layers);
            throw new FailedTaskException("未能点击到打卡");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ActionUtil.returnAction(accessibilityService,layers);
    }
}
