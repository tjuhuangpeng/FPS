package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.learning.R;


/**
 * 可折叠控件：左侧文本内容，右侧折叠状态图标
 */
public abstract class AHAbstractExpandView extends RelativeLayout {

    private OnFoldStateChangedListener onFoldStateChangedListener;
    protected ExpandMenuItem menuItem = new ExpandMenuItem();
    protected TextView contentTextView;
    protected ImageView statusImageView;
    protected Drawable collapseIcon;
    protected Drawable expandIcon;

    protected int drawableWidth;
    protected int centerMargin;
    protected int leftMargin;
    protected int rightMargin;

    public AHAbstractExpandView(Context context) {
        this(context, null);
    }

    public AHAbstractExpandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AHAbstractExpandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drawableWidth = ScreenUtils.dpToPxInt(context, 12);
        expandIcon = getResources().getDrawable(R.drawable.ahlib_common_arrow_up);
        collapseIcon = getResources().getDrawable(R.drawable.ahlib_common_arrow_down);
        centerMargin = ScreenUtils.dpToPxInt(getContext(), 10);
        leftMargin = ScreenUtils.dpToPxInt(getContext(), 15);
        rightMargin = ScreenUtils.dpToPxInt(getContext(), 15);
        fillContent();
    }

    /**
     * 更新当前显示数据
     * @param menuItem
     */
    public void update(ExpandMenuItem menuItem) {
        if (menuItem == null) {
            return;
        }
        this.menuItem = menuItem;
        updateUI(menuItem);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AHAbstractExpandView.this.menuItem.isExpanded = !isExpanded();
                updateUI(AHAbstractExpandView.this.menuItem);
                if (onFoldStateChangedListener != null) {
                    onFoldStateChangedListener.onStateChange(AHAbstractExpandView.this.menuItem);
                }
            }
        });
    }

    public void setCenterMargin(int marginPx) {
        this.centerMargin = marginPx;
    }

    public void setLeftMargin(int marginPx) {
        this.leftMargin = marginPx;
    }

    public void setRightMargin(int marginPx) {
        this.rightMargin = marginPx;
    }

    /**
     * 设置折叠状态图片资源id
     * @param collapseId
     * @param expandId
     */
    public void setStatusImageResource(@DrawableRes int collapseId, @DrawableRes int expandId) {
        this.collapseIcon = getDrawable(getResources(), collapseId);
        this.expandIcon = getDrawable(getResources(), expandId);
    }

    /**
     * 设置折叠状态图片资源Drawable
     * @param collapseDrawable
     * @param expandDrawable
     */
    public void setStatusImageDrawable(Drawable collapseDrawable, Drawable expandDrawable) {
        this.collapseIcon = collapseDrawable;
        this.expandIcon = expandDrawable;
    }

    /**
     * 更新当前折叠状态
     * @param isExpanded
     */
    public void changeState(boolean isExpanded) {
        this.menuItem.isExpanded = isExpanded;
        updateUI(menuItem);
    }

    public ExpandMenuItem getCurrentMenuItem() {
        return menuItem;
    }

    public TextView getContentView() {
        return contentTextView;
    }

    public ImageView getStatusView() {
        return statusImageView;
    }

    public boolean isExpanded() {
        if (menuItem != null) {
            return menuItem.isExpanded;
        }
        return false;
    }

    public void setOnFoldStateChangedListener(OnFoldStateChangedListener listener) {
        this.onFoldStateChangedListener = listener;
    }

    /**
     * 填充布局，为content和state对应View赋值
     */
    protected abstract void fillContent();

    /**
     * 刷新UI
     * @param menuItem
     */
    protected abstract void updateUI(ExpandMenuItem menuItem);

    public static class ExpandMenuItem {
        public int menuId;
        public String menuContent;
        public boolean isExpanded;

    }

    /**
     * 折叠状态监听
     */
    public interface OnFoldStateChangedListener {
        void onStateChange(ExpandMenuItem menuItem);
    }

    public Drawable getDrawable(Resources resources, int resId) {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(resId, null);
        } else {
            return resources.getDrawable(resId);
        }
    }
}

