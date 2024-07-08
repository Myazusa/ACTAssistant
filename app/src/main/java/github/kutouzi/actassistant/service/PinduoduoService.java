package github.kutouzi.actassistant.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.List;

import github.kutouzi.actassistant.exception.FailedTaskException;
import github.kutouzi.actassistant.util.ActionUtil;
import github.kutouzi.actassistant.util.TraverseNodeUtil;

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
    public void autoHeshuiTask(AccessibilityNodeInfo nodeInfo, AccessibilityService accessibilityService) throws FailedTaskException{
        int layers = 0;
        if(!ActionUtil.findSiblingsChildClickableNodesAction(nodeInfo,"金币",1)){
            ActionUtil.returnAction(accessibilityService,layers);
            TraverseNodeUtil.traverseNodes(nodeInfo,nodeInfo.getChildCount());
            throw new FailedTaskException("未能打开任务页");
        }
        layers++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!ActionUtil.findSiblingsClickableNodesAction(nodeInfo,"喝水赚钱","去领取")){
            ActionUtil.returnAction(accessibilityService,layers);
            TraverseNodeUtil.traverseNodes(nodeInfo,nodeInfo.getChildCount());
            throw new FailedTaskException("未能喝水打卡");
        }
        layers++;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(!ActionUtil.findParentsClickableNodesAction(nodeInfo,"喝水打卡领")){
            ActionUtil.returnAction(accessibilityService,layers);
            TraverseNodeUtil.traverseNodes(nodeInfo,nodeInfo.getChildCount());
            throw new FailedTaskException("未能领取金币");
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ActionUtil.returnAction(accessibilityService,layers);
    }
    public void autoCheckInTask(AccessibilityNodeInfo nodeInfo, AccessibilityService accessibilityService) throws FailedTaskException{
        int layers = 0;
        if(!ActionUtil.findParentsClickableNodesAction(nodeInfo,"领取今日现金")){
            layers++;
            ActionUtil.returnAction(accessibilityService,layers);
            throw new FailedTaskException("未能领取今日现金");
        }
        layers++;
        ActionUtil.returnAction(accessibilityService,layers);
    }
}
