package github.kutouzi.actassistant.entity;

import github.kutouzi.actassistant.config.DataDefaultConfig;
import github.kutouzi.actassistant.entity.inf.IData;

public class SwipeUpData implements IData {
    private int randomMaxSwipeupValue;
    private int randomMinSwipeupValue;
    private int randomMaxDelayValue;
    private int randomMinDelayValue;

    public SwipeUpData(int randomMaxSwipeupValue, int randomMinSwipeupValue, int randomMaxDelayValue, int randomMinDelayValue) {
        this.randomMaxSwipeupValue = randomMaxSwipeupValue;
        this.randomMinSwipeupValue = randomMinSwipeupValue;
        this.randomMaxDelayValue = randomMaxDelayValue;
        this.randomMinDelayValue = randomMinDelayValue;
    }

    public int getRandomMaxSwipeupValue() {
        return randomMaxSwipeupValue;
    }

    public void setRandomMaxSwipeupValue(int randomMaxSwipeupValue) {
        this.randomMaxSwipeupValue = randomMaxSwipeupValue;
    }

    public int getRandomMinSwipeupValue() {
        return randomMinSwipeupValue;
    }

    public void setRandomMinSwipeupValue(int randomMinSwipeupValue) {
        this.randomMinSwipeupValue = randomMinSwipeupValue;
    }

    public int getRandomMaxDelayValue() {
        return randomMaxDelayValue;
    }

    public void setRandomMaxDelayValue(int randomMaxDelayValue) {
        this.randomMaxDelayValue = randomMaxDelayValue;
    }

    public int getRandomMinDelayValue() {
        return randomMinDelayValue;
    }

    public void setRandomMinDelayValue(int randomMinDelayValue) {
        this.randomMinDelayValue = randomMinDelayValue;
    }
}
