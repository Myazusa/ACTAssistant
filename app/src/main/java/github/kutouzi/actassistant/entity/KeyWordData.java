package github.kutouzi.actassistant.entity;

import java.util.List;

public class KeyWordData {
    public List<String> getPingduoduoClickableKeyWordList() {
        return pingduoduoClickableKeyWordList;
    }

    public void setPingduoduoClickableKeyWordList(List<String> pingduoduoClickableKeyWordList) {
        this.pingduoduoClickableKeyWordList = pingduoduoClickableKeyWordList;
    }

    public List<String> getMeituanClickableKeyWordList() {
        return meituanClickableKeyWordList;
    }

    public void setMeituanClickableKeyWordList(List<String> meituanClickableKeyWordList) {
        this.meituanClickableKeyWordList = meituanClickableKeyWordList;
    }

    public List<String> getPingduoduoCancelableKeyWordList() {
        return pingduoduoCancelableKeyWordList;
    }

    public void setPingduoduoCancelableKeyWordList(List<String> pingduoduoCancelableKeyWordList) {
        this.pingduoduoCancelableKeyWordList = pingduoduoCancelableKeyWordList;
    }

    public List<String> getMeituanCancelableKeyWordList() {
        return meituanCancelableKeyWordList;
    }

    public void setMeituanCancelableKeyWordList(List<String> meituanCancelableKeyWordList) {
        this.meituanCancelableKeyWordList = meituanCancelableKeyWordList;
    }

    private List<String> pingduoduoClickableKeyWordList;
    private List<String> meituanClickableKeyWordList;

    private List<String> pingduoduoCancelableKeyWordList;
    private List<String> meituanCancelableKeyWordList;

    public KeyWordData(List<String> pingduoduoClickableKeyWordList, List<String> meituanClickableKeyWordList, List<String> pingduoduoCancelableKeyWordList, List<String> meituanCancelableKeyWordList) {
        this.pingduoduoClickableKeyWordList = pingduoduoClickableKeyWordList;
        this.meituanClickableKeyWordList = meituanClickableKeyWordList;
        this.pingduoduoCancelableKeyWordList = pingduoduoCancelableKeyWordList;
        this.meituanCancelableKeyWordList = meituanCancelableKeyWordList;
    }
}
