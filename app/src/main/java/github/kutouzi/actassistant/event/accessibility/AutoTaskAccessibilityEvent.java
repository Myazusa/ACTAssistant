package github.kutouzi.actassistant.event.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import github.kutouzi.actassistant.exception.FailedTaskException;
import github.kutouzi.actassistant.service.DouyinjisuService;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.PinduoduoService;
import github.kutouzi.actassistant.androidservice.ACTFloatingWindowService;

public class AutoTaskAccessibilityEvent {
    private static final String _TAG = AutoTaskAccessibilityEvent.class.getName();
    public static void autoTaskAccessibilityEvent(AccessibilityEvent event, AccessibilityNodeInfo nodeInfo, AccessibilityService service){
        defaultAutoTask(event,nodeInfo,service);
        nodeAddedAutoTask(event,service);
    }
    private static void nodeAddedAutoTask(AccessibilityEvent event,AccessibilityService service){
        int changeType = event.getEventType();
        if(changeType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            //判断一定要是节点添加事件才执行的任务，因为文本变换事件也可以触发WINDOW_CONTENT_CHANGED
            if((changeType & AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) != 0) {
                switch (ACTFloatingWindowService.scanApplicationFlag){
                    case PinduoduoService.APPLICATION_INDEX:
                        try{
                            PinduoduoService.getInsatance().autoCheckInTask(service.getRootInActiveWindow(),service);
                        }catch (FailedTaskException e){
                            Log.w(_TAG,e.getMessage());
                        }
                        break;
                    default:
                        break;
                }

            }
        }
    }

    // 无需条件，触发即可执行的任务
    private static void defaultAutoTask( AccessibilityEvent event, AccessibilityNodeInfo nodeInfo,AccessibilityService service){
        switch (ACTFloatingWindowService.scanApplicationFlag){
            case PinduoduoService.APPLICATION_INDEX:
                try{
                    PinduoduoService.getInsatance().autoHeshuiTask(nodeInfo,service);
                }catch (FailedTaskException e){
                    Log.w(_TAG,e.getMessage());
                }
                break;
            case MeituanService.APPLICATION_INDEX:
                try{
                    MeituanService.getInsatance().autoCheckInTask(event.getSource(),service);
                }catch (FailedTaskException e){
                    Log.w(_TAG,e.getMessage());
                }
                break;
            case DouyinjisuService.APPLICATION_INDEX:
                try{
                    DouyinjisuService.getInsatance().autoCheckInTask(event.getSource(),service);
                }catch (FailedTaskException e){
                    Log.w(_TAG,e.getMessage());
                }
                break;
            default:
                break;
        }
    }
}
