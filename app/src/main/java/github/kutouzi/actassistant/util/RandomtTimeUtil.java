package github.kutouzi.actassistant.util;

import java.util.Random;

public class RandomtTimeUtil {
    private static final Random _randomValue = new Random();

    // 单位毫秒
    public static int getRandomDelayTillis(int _RANDOM_MIN_VALUE,int _RANDOM_MAX_VALUE){
        return _RANDOM_MIN_VALUE + _randomValue.nextInt(_RANDOM_MAX_VALUE - _RANDOM_MIN_VALUE +1);
    }
}
