package view;

/**
 * Created by Administrator on 2017/2/27.
 */
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class TouchDelegate {
    public static final int ABOVE = 1;
    public static final int BELOW = 2;
    public static final int TO_LEFT = 4;
    public static final int TO_RIGHT = 8;

    public TouchDelegate(Rect bounds, View delegateView) {
        throw new RuntimeException("Stub!");
    }

    public boolean onTouchEvent(MotionEvent event) {
        throw new RuntimeException("Stub!");
    }
}
