package github.kutouzi.actassistant.service;

import android.view.accessibility.AccessibilityNodeInfo;

import github.kutouzi.actassistant.entity.KeyWordData;
import github.kutouzi.actassistant.service.PinduoduoService;

public abstract class ApplicationService {

    public KeyWordData clickableKeyWordList = null;
    public KeyWordData cancelableKeyWordList = null;


    public KeyWordData getClickableKeyWordList() {
        return clickableKeyWordList;
    }

    public void setClickableKeyWordList(KeyWordData clickableKeyWordList) {
        this.clickableKeyWordList = clickableKeyWordList;
    }

    public KeyWordData getCancelableKeyWordList() {
        return cancelableKeyWordList;
    }

    public void setCancelableKeyWordList(KeyWordData cancelableKeyWordList) {
        this.cancelableKeyWordList = cancelableKeyWordList;
    }

    public abstract int scanApplication(CharSequence packageName);

    public abstract void switchToVideo(AccessibilityNodeInfo nodeInfo);

}
