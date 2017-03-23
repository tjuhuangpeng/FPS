package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;


/**
 * 自定义的PopupWindow，解决Android 7.0版本以上showAsDropdown时窗体没有在anchor下问题
 * Created by liyabing on 2017/02/20.
 */
public class AHCompatPopupWindow extends android.widget.PopupWindow {

    private boolean mShowAsDropdown = false;

    public AHCompatPopupWindow(Context context) {
        super(context);
    }

    public AHCompatPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AHCompatPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AHCompatPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AHCompatPopupWindow() {
        super();
    }

    public AHCompatPopupWindow(View contentView) {
        super(contentView);
    }

    public AHCompatPopupWindow(int width, int height) {
        super(width, height);
    }

    public AHCompatPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public AHCompatPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void setShowAsDropdown(boolean showAsDropdown) {
        this.mShowAsDropdown = showAsDropdown;
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        mShowAsDropdown = true;
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            int[] anchorLocation = new int[2];
            anchor.getLocationInWindow(anchorLocation);
            if (getContentView() != null) {
                if (android.os.Build.VERSION.SDK_INT == 24) {
                    gravity = Gravity.NO_GRAVITY;
                }
                showAtLocation(((Activity) getContentView().getContext()).getWindow().getDecorView(), gravity,
                        anchorLocation[0] + xoff, anchorLocation[1] + anchor.getHeight() + yoff);
            }
        } else {
            super.showAsDropDown(anchor, xoff, yoff, gravity);
        }
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (android.os.Build.VERSION.SDK_INT == 25 && mShowAsDropdown) {
            if (isShowing() || getContentView() == null) {
                return;
            }
            try {
                Object decorView = ReflectionUtils.getFieldAll(this, "mDecorView");
                TransitionManager.class.getMethod("endTransitions", ViewGroup.class).invoke(null, (ViewGroup) decorView);
                ReflectionUtils.invokeMethodAll(this, "detachFromAnchor");
                ReflectionUtils.setFieldAll(this, "mIsShowing", true);
                // 是下拉展示时，修改mIsDropdown属性为true，强制下拉生效
                ReflectionUtils.setFieldAll(this, "mIsDropdown", true);
                ReflectionUtils.setFieldAll(this, "mGravity", gravity);
                final WindowManager.LayoutParams p = (WindowManager.LayoutParams) ReflectionUtils.findMethodStepup(this, "createPopupLayoutParams", IBinder.class)
                        .invoke(this, parent.getWindowToken());
                ReflectionUtils.findMethodStepup(this, "preparePopup", WindowManager.LayoutParams.class).invoke(this, p);
                p.x = x;
                p.y = y;
                ReflectionUtils.findMethodStepup(this, "invokePopup", WindowManager.LayoutParams.class).invoke(this, p);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            super.showAtLocation(parent, gravity, x, y);
        }
    }
}
