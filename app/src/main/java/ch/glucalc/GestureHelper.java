package ch.glucalc;

import android.view.MotionEvent;

/**
 * Created by jmottet on 19/01/2016.
 */
public class GestureHelper {

    final public static int SWIPE_MIN_DISTANCE = 120;
    final public static int SWIPE_MAX_OFF_PATH = 250;
    final public static int SWIPE_THRESHOLD_VELOCITY = 200;

    public static boolean isGestureOffPath(MotionEvent e1, MotionEvent e2) {
        return Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH;
    }

    public static boolean isGestureRightToLeft(MotionEvent e1, MotionEvent e2, float velocityX) {
        return e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
    }

    public static boolean isGestureLeftToRight(MotionEvent e1, MotionEvent e2, float velocityX) {
        return e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY;
    }
}
