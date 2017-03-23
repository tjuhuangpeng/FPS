package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class PopupWindow {
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;

    public PopupWindow(Context context) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(Context context, AttributeSet attrs) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow() {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(View contentView) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(View contentView, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public Drawable getBackground() {
        throw new RuntimeException("Stub!");
    }

    public void setBackgroundDrawable(Drawable background) {
        throw new RuntimeException("Stub!");
    }

    public float getElevation() {
        throw new RuntimeException("Stub!");
    }

    public void setElevation(float elevation) {
        throw new RuntimeException("Stub!");
    }

    public int getAnimationStyle() {
        throw new RuntimeException("Stub!");
    }

    public void setIgnoreCheekPress() {
        throw new RuntimeException("Stub!");
    }

    public void setAnimationStyle(int animationStyle) {
        throw new RuntimeException("Stub!");
    }

    public View getContentView() {
        throw new RuntimeException("Stub!");
    }

    public void setContentView(View contentView) {
        throw new RuntimeException("Stub!");
    }

    public void setTouchInterceptor(View.OnTouchListener l) {
        throw new RuntimeException("Stub!");
    }

    public boolean isFocusable() {
        throw new RuntimeException("Stub!");
    }

    public void setFocusable(boolean focusable) {
        throw new RuntimeException("Stub!");
    }

    public int getInputMethodMode() {
        throw new RuntimeException("Stub!");
    }

    public void setInputMethodMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public void setSoftInputMode(int mode) {
        throw new RuntimeException("Stub!");
    }

    public int getSoftInputMode() {
        throw new RuntimeException("Stub!");
    }

    public boolean isTouchable() {
        throw new RuntimeException("Stub!");
    }

    public void setTouchable(boolean touchable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isOutsideTouchable() {
        throw new RuntimeException("Stub!");
    }

    public void setOutsideTouchable(boolean touchable) {
        throw new RuntimeException("Stub!");
    }

    public boolean isClippingEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setClippingEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isSplitTouchEnabled() {
        throw new RuntimeException("Stub!");
    }

    public void setSplitTouchEnabled(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAttachedInDecor() {
        throw new RuntimeException("Stub!");
    }

    public void setAttachedInDecor(boolean enabled) {
        throw new RuntimeException("Stub!");
    }

    public void setWindowLayoutMode(int widthSpec, int heightSpec) {
        throw new RuntimeException("Stub!");
    }

    public int getHeight() {
        throw new RuntimeException("Stub!");
    }

    public void setHeight(int height) {
        throw new RuntimeException("Stub!");
    }

    public int getWidth() {
        throw new RuntimeException("Stub!");
    }

    public void setWidth(int width) {
        throw new RuntimeException("Stub!");
    }

    public boolean isShowing() {
        throw new RuntimeException("Stub!");
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        throw new RuntimeException("Stub!");
    }

    public void showAsDropDown(View anchor) {
        throw new RuntimeException("Stub!");
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        throw new RuntimeException("Stub!");
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        throw new RuntimeException("Stub!");
    }

    public boolean isAboveAnchor() {
        throw new RuntimeException("Stub!");
    }

    public int getMaxAvailableHeight(View anchor) {
        throw new RuntimeException("Stub!");
    }

    public int getMaxAvailableHeight(View anchor, int yOffset) {
        throw new RuntimeException("Stub!");
    }

    public void dismiss() {
        throw new RuntimeException("Stub!");
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        throw new RuntimeException("Stub!");
    }

    public void update() {
        throw new RuntimeException("Stub!");
    }

    public void update(int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void update(int x, int y, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void update(int x, int y, int width, int height, boolean force) {
        throw new RuntimeException("Stub!");
    }

    public void update(View anchor, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        throw new RuntimeException("Stub!");
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}

