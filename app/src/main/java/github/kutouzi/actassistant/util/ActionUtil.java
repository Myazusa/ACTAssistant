package github.kutouzi.actassistant.util;

import android.accessibilityservice.GestureDescription;
import android.content.res.Resources;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import github.kutouzi.actassistant.entity.SwipeUpData;
import github.kutouzi.actassistant.service.ACTFloatingWindowService;

public class ActionUtil {
    private static final String _TAG = ActionUtil.class.getName();

    // 可执行动作
    private static Runnable pendingAction = null;

    private static final Handler handler = new Handler();

    private static void performSwipeUp(Resources resources,SwipeUpData swipeUpData,ACTFloatingWindowService actFloatingWindowService) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            float startX = getScreenWidth(resources) / 2f;
            float startY = getScreenHeight(resources) / 2f;
            float endX = startX;
            float endY = getScreenHeight(resources) / 4f;

            Path path = new Path();
            path.moveTo(startX, startY);
            path.lineTo(endX, endY);

            GestureDescription.StrokeDescription stroke = new GestureDescription.StrokeDescription(
                    path, 0, RandomUtil.getRandomDelayTillis(swipeUpData.getRandomMinDelayValue(),swipeUpData.getRandomMaxDelayValue()));

            GestureDescription gesture = new GestureDescription.Builder()
                    .addStroke(stroke)
                    .build();
            actFloatingWindowService.dispatchGesture(gesture, null, null);
            Log.i(_TAG,"上划被调用");
        }
    }

    private static int getScreenWidth(Resources resources) {
        return resources.getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight(Resources resources) {
        return resources.getDisplayMetrics().heightPixels;
    }

    public static void processSwipe(Resources resources,SwipeUpData swipeUpData,ACTFloatingWindowService actFloatingWindowService) {
        if (pendingAction != null) {
            handler.removeCallbacks(pendingAction);
        }
        pendingAction = () -> {
            ActionUtil.performSwipeUp(resources,swipeUpData,actFloatingWindowService);
            handler.postDelayed(pendingAction, RandomUtil.getRandomDelayTillis(swipeUpData.getRandomMinSwipeupValue(),swipeUpData.getRandomMaxSwipeupValue()));
        };
        handler.post(pendingAction);
    }
    public static void removeSwipeAction(){
        handler.removeCallbacks(pendingAction);
    }
}
