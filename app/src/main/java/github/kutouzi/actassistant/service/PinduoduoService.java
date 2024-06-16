package github.kutouzi.actassistant.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import github.kutouzi.actassistant.exception.FailedTaskException;
import github.kutouzi.actassistant.util.ActionUtil;

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
        if(!ActionUtil.clickAction(nodeInfo.getWindow().getRoot(),"去提现")){
            ActionUtil.returnAction(accessibilityService,layers);
            throw new FailedTaskException("未能打开任务页");
        }
        layers++;
        if(!ActionUtil.findClickAction(nodeInfo.getWindow().getRoot(),"每日8次喝水赚钱","去领取")){
            ActionUtil.returnAction(accessibilityService,layers);
            throw new FailedTaskException("未能喝水打卡");
        }
        layers++;
        if(!ActionUtil.clickAction(nodeInfo.getWindow().getRoot(),"喝水赚现金")){
            ActionUtil.returnAction(accessibilityService,layers);
            throw new FailedTaskException("未能领取金币");
        }
        ActionUtil.returnAction(accessibilityService,layers);
    }
}
