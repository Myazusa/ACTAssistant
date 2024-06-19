package github.kutouzi.actassistant.entity;

import github.kutouzi.actassistant.entity.inf.IData;

public class SwitchApplicationData  implements IData {
    public int getSwitchApplicationTime() {
        return switchApplicationTime;
    }

    public void setSwitchApplicationTime(int switchApplicationTime) {
        this.switchApplicationTime = switchApplicationTime;
    }

    private int switchApplicationTime;

    public SwitchApplicationData(int switchApplicationTime) {
        this.switchApplicationTime = switchApplicationTime;
    }
}
