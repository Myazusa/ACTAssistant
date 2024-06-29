package github.kutouzi.actassistant.entity;

import github.kutouzi.actassistant.entity.inf.IData;

public class TimedTaskData implements IData {
    public int getTimedTaskDelayValue() {
        return timedTaskDelayValue;
    }

    public void setTimedTaskDelayValue(int timedTaskDelayValue) {
        this.timedTaskDelayValue = timedTaskDelayValue;
    }

    public TimedTaskData(int timedTaskDelayValue) {
        this.timedTaskDelayValue = timedTaskDelayValue;
    }

    private int timedTaskDelayValue;

}
