package github.kutouzi.actassistant.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private List<String> pingduoduoClickableKeyWordList = Stream.of("专属现金红包").collect(Collectors.toList());
    private List<String> meituanClickableKeyWordList = new ArrayList<>();

    private List<String> pingduoduoCancelableKeyWordList = Stream.of("领取今日奖励","立即提现","领取今日现金","明日继续来领","打款金额","立即收下，去拼单")
            .collect(Collectors.toList());
    private List<String> meituanCancelableKeyWordList = Stream.of("现金秒到账","领取今日奖励")
            .collect(Collectors.toList());

    public KeyWordData(List<String> pingduoduoClickableKeyWordList, List<String> meituanClickableKeyWordList, List<String> pingduoduoCancelableKeyWordList, List<String> meituanCancelableKeyWordList) {
        this.pingduoduoClickableKeyWordList = pingduoduoClickableKeyWordList;
        this.meituanClickableKeyWordList = meituanClickableKeyWordList;
        this.pingduoduoCancelableKeyWordList = pingduoduoCancelableKeyWordList;
        this.meituanCancelableKeyWordList = meituanCancelableKeyWordList;
    }
}
