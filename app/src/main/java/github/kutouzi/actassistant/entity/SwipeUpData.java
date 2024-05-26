package github.kutouzi.actassistant.entity;

public class SwipeUpData {
    private int randomMaxSwipeupValue = 14000;
    private int randomMinSwipeupValue = 10000;

    // 控制上划操作所耗时间的参数
    private int randomMaxDelayValue = 400;
    private int randomMinDelayValue = 200;

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
