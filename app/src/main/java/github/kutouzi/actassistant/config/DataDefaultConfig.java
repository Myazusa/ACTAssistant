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




}
