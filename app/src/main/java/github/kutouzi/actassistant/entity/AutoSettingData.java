package github.kutouzi.actassistant.entity;

import github.kutouzi.actassistant.entity.inf.IData;

public class AutoSettingData implements IData {
    public boolean getAutoScanApplicationButtonState() {
        return autoScanApplicationButtonState;
    }

    public void setAutoScanApplicationButtonState(boolean autoScanApplicationButtonState) {
        this.autoScanApplicationButtonState = autoScanApplicationButtonState;
    }

    private boolean autoScanApplicationButtonState;

    public AutoSettingData(boolean autoScanApplicationButtonState) {
        this.autoScanApplicationButtonState = autoScanApplicationButtonState;
    }
}
