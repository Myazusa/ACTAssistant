package github.kutouzi.actassistant.event.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Objects;

import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.enums.JsonFileDefinition;
import github.kutouzi.actassistant.enums.ToggleStateEnum;
import github.kutouzi.actassistant.io.JsonFileIO;
import github.kutouzi.actassistant.service.MeituanService;
import github.kutouzi.actassistant.service.PinduoduoService;
import github.kutouzi.actassistant.util.DialogUtil;
import github.kutouzi.actassistant.view.androidservice.ACTFloatingWindowService;
import github.kutouzi.actassistant.view.button.ToggleButton;

public class ListeningDialogAccessibilityEvent {
    public static void listeningDialogAccessibilityEvent(AccessibilityEvent event, AccessibilityService service, ToggleButton button){
        int changeType = event.getEventType();
        if(changeType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
            if((changeType & AccessibilityEvent.CONTENT_CHANGE_TYPE_SUBTREE) != 0){
                if (button.isButtonState() == ToggleStateEnum.Triggered) {
                    if(event.getSource() != null){
                        switchFunctionToDialog(event.getSource(),service);
                    }
                }
            }
        }
    }
    private static void switchFunctionToDialog(AccessibilityNodeInfo info, AccessibilityService service){
        switch (ACTFloatingWindowService.scanApplicationFlag) {
            case PinduoduoService.APPLICATION_INDEX:
                DialogUtil.cancelDialog(info, service,
                        Objects.requireNonNull((KeyWordData) JsonFileIO.readJson(service, JsonFileDefinition.KEYWORD_JSON_NAME, KeyWordData.class)).getPingduoduoCancelableKeyWordList());
                break;
            case MeituanService.APPLICATION_INDEX:
                DialogUtil.cancelDialog(info, service,
                        Objects.requireNonNull((KeyWordData)JsonFileIO.readJson(service,JsonFileDefinition.KEYWORD_JSON_NAME,KeyWordData.class)).getMeituanCancelableKeyWordList());
                break;
            default:
                break;
        }
    }
}
