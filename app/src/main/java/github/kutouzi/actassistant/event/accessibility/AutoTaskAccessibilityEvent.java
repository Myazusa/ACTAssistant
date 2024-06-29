package github.kutouzi.actassistant.event.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import github.kutouzi.actassistant.exception.FailedTaskException;
import github.kutouzi.actassistant.service.DouyinjisuService;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.PinduoduoService;
import github.kutouzi.actassistant.view.androidservice.ACTFloatingWindowService;

public class AutoTaskAccessibilityEvent {
    private static final String _TAG = AutoTaskAccessibilityEvent.class.getName();
    public static void autoTaskAccessibilityEvent(AccessibilityEvent event, AccessibilityService service){
        int changeType = event.getEventType();
        if(changeType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            //判断一定要是节点添加事件，因为文本变换事件也可以触发WINDOW_CONTENT_CHANGED
            if((changeType & AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) != 0){
                if (ACTFloatingWindowService.runTask){
                    if(service.getRootInActiveWindow()!=null){
                        switch (ACTFloatingWindowService.scanApplicationFlag){
                            case PinduoduoService.APPLICATION_INDEX:
                                try{
                                    PinduoduoService.getInsatance().autoHeshuiTask(service.getRootInActiveWindow(),service);
                                }catch (FailedTaskException e){
                                    Log.w(_TAG,e.getMessage());
                                }
                                break;
                            case MeituanService.APPLICATION_INDEX:
                                try{
                                    MeituanService.getInsatance().autoCheckInTask(service.getRootInActiveWindow(),service);
                                }catch (FailedTaskException e){
                                    Log.w(_TAG,e.getMessage());
                                }
                                break;
                            case DouyinjisuService.APPLICATION_INDEX:
                                try{
                                    DouyinjisuService.getInsatance().autoCheckInTask(service.getRootInActiveWindow(),service);
                                }catch (FailedTaskException e){
                                    Log.w(_TAG,e.getMessage());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    ACTFloatingWindowService.runTask = false;
                }
            }
        }
    }
}
