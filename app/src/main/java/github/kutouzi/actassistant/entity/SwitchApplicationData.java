package github.kutouzi.actassistant.entity;

public class SwitchApplicationData {
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
