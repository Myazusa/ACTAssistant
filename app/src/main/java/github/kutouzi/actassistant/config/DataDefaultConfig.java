package github.kutouzi.actassistant.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataDefaultConfig {
    public static final List<String> defaultPingduoduoClickableKeyWordList = Stream.of("专属现金红包")
            .collect(Collectors.toList());
    public static final List<String> defaultMeituanClickableKeyWordList = Stream.of("開")
            .collect(Collectors.toList());

    public static final List<String> defaultPingduoduoCancelableKeyWordList = Stream.of("领取今日奖励","立即提现","领取今日现金","明日继续来领","打款金额","立即收下，去拼单")
            .collect(Collectors.toList());
    public static final List<String> defaultMeituanCancelableKeyWordList = Stream.of("现金秒到账","领取今日奖励")
            .collect(Collectors.toList());
    // 控制每个操作的间隔
    public static final int defaultRandomMaxSwipeupValue = 14000;
    public static final int defaultRandomMinSwipeupValue = 10000;

    // 控制上划所耗的时间
    public static final int defaultRandomMaxDelayValue = 400;
    public static final int defaultRandomMinDelayValue = 200;

    public static final String defaultLocalServerIPAddress = "192.168.1.100";
    public static final String defaultLocalServerPort = "48802";

    // 控制切换应用的时间间隔
    public static final int defaultSwitchApplicationTime = 600000;
}
