package github.kutouzi.actassistant.entity;

import github.kutouzi.actassistant.entity.inf.IData;

public class HelpData implements IData {
    public boolean getAutoCheckUpdateSwitchState() {
        return autoCheckUpdateSwitchState;
    }

    public void setAutoCheckUpdateSwitchState(boolean autoCheckUpdateSwitchState) {
        this.autoCheckUpdateSwitchState = autoCheckUpdateSwitchState;
    }

    public HelpData(boolean autoCheckUpdateSwitchState) {
        this.autoCheckUpdateSwitchState = autoCheckUpdateSwitchState;
    }

    private boolean autoCheckUpdateSwitchState;

}
