package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.learning.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 组合可折叠控件，各子View横向等分
 */
public class AHCompositeExpandView extends LinearLayout {

    private LinearLayout tabsContainer;
    private ImageView underlineView;
    private List<AHAbstractExpandView.ExpandMenuItem> compositeList;
    private List<AHAbstractExpandView> compositeViewList;
    private OnItemStateChangedClickListener onItemStateChangedClickListener;
    private int itemLeftMargin;
    private int itemRightMargin;
    private int itemCenterMargin;
    private int itemDividerColor;
    private Drawable collapseIcon;
    private Drawable expandIcon;
    private boolean isCentered = true;
    private boolean prevIsCentered = isCentered;
    private boolean showUnderline = true;

    public AHCompositeExpandView(Context context) {
        this(context, null);
    }

    public AHCompositeExpandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AHCompositeExpandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        tabsContainer = new LinearLayout(getContext());
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        tabsContainer.setLayoutParams(layoutParams);
        addView(tabsContainer);
        underlineView = new ImageView(getContext());
        underlineView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
        underlineView.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.ahlib_common_color07)));
        addView(underlineView);
        setShowUnderline(true);
        itemCenterMargin = ScreenUtils.dpToPxInt(getContext(), 10);
        itemLeftMargin = ScreenUtils.dpToPxInt(getContext(), 15);
        itemRightMargin = ScreenUtils.dpToPxInt(getContext(), 15);
        itemDividerColor = getResources().getColor(R.color.ahlib_common_color07);
    }

    /**
     * 设置数据
     * @param compositeList
     * @param isCentered 子item view是否是居中类型
     */
    public void setMenuList(List<AHAbstractExpandView.ExpandMenuItem> compositeList, boolean isCentered) {
        this.compositeList = compositeList;
        this.prevIsCentered = this.isCentered;
        this.isCentered = isCentered;
    }

    public void setItemLeftMargin(int px) {
        this.itemLeftMargin = px;
    }

    public void setItemRightMargin(int px) {
        this.itemRightMargin = px;
    }

    public void setItemCenterMargin(int px) {
        this.itemCenterMargin = px;
    }

    /**
     * item之间分割线色值
     * @param color
     */
    public void setItemDividerColor(int color) {
        this.itemDividerColor = color;
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

    public void setOnItemStateChangedClickListener(OnItemStateChangedClickListener listener) {
        this.onItemStateChangedClickListener = listener;
    }

    /**
     * 更新指定item状态
     * @param position
     * @param expand
     */
    public void changeItemState(int position, boolean expand) {
        if (position < 0 || position >= compositeList.size()) {
            return;
        }
        compositeList.get(position).isExpanded = expand;
        notifyDataSetChanged();
    }

    /**
     * 更新指定item状态
     * @param position
     * @param item
     */
    public void updateItem(int position, AHAbstractExpandView.ExpandMenuItem item) {
        if (position < 0 || position >= compositeList.size()) {
            return;
        }
        compositeList.set(position, item);
        notifyDataSetChanged();
    }

    /**
     * 折叠所有子View
     */
    public void collapseAll() {
        if (compositeList == null || compositeList.isEmpty()) {
            return;
        }
        for (AHAbstractExpandView.ExpandMenuItem item : compositeList) {
            item.isExpanded = false;
        }
        notifyDataSetChanged();
    }

    /**
     * 更新数据，刷新控件
     */
    public void notifyDataSetChanged() {
        if (compositeList == null || compositeList.isEmpty() || prevIsCentered != isCentered) {
            tabsContainer.removeAllViews();
            if (compositeViewList != null) {
                compositeViewList.clear();
            }
        }
        if (compositeViewList == null) {
            compositeViewList = new ArrayList<>(compositeList.size());
        } else if (compositeViewList.size() > compositeList.size()) {
            compositeViewList = compositeViewList.subList(0, compositeList.size());
            tabsContainer.removeViews(2 * compositeList.size(), tabsContainer.getChildCount() - 2 * compositeList.size());
        }
        for (int i = 0; i < compositeList.size(); i++) {
            if (i < compositeViewList.size()) {
                compositeViewList.get(i).update(compositeList.get(i));
            } else {
                AHAbstractExpandView itemView;
                if (isCentered) {
                    itemView = new AHCenteredExpandView(getContext());
                } else {
                    itemView = new AHEdgedExpandView(getContext());
                }
                itemView.setCenterMargin(itemCenterMargin);
                itemView.setLeftMargin(itemLeftMargin);
                itemView.setRightMargin(itemRightMargin);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                if (collapseIcon != null && expandIcon != null) {
                    itemView.setStatusImageDrawable(collapseIcon, expandIcon);
                }
                itemView.update(compositeList.get(i));
                tabsContainer.addView(itemView, params);
                compositeViewList.add(itemView);
                if (i != compositeList.size() - 1) {
                    // 分隔线
                    ImageView divider = new ImageView(getContext());
                    divider.setImageDrawable(new ColorDrawable(itemDividerColor));
                    LayoutParams dividerParams = new LayoutParams(1, ScreenUtils.dpToPxInt(getContext(), 16));
                    dividerParams.gravity = Gravity.CENTER_VERTICAL;
                    tabsContainer.addView(divider, dividerParams);
                }
            }
            final int index = i;
            compositeViewList.get(i).setOnFoldStateChangedListener(new AHAbstractExpandView.OnFoldStateChangedListener() {
                @Override
                public void onStateChange(AHAbstractExpandView.ExpandMenuItem menuItem) {
                    for (int i = 0; i < compositeList.size(); i++) {
                        if (i != index) {
                            compositeList.get(i).isExpanded = false;
                            compositeViewList.get(i).update(compositeList.get(i));
                        }
                    }
                    if (onItemStateChangedClickListener != null) {
                        onItemStateChangedClickListener.onItemStateChange(index, menuItem);
                    }
                }
            });
        }
    }

    public Drawable getDrawable(Resources resources, int resId) {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(resId, null);
        } else {
            return resources.getDrawable(resId);
        }
    }

    public boolean isShowUnderline() {
        return showUnderline;
    }

    /**
     * 是否显示下划线
     * @param showUnderline
     */
    public void setShowUnderline(boolean showUnderline) {
        this.showUnderline = showUnderline;
        if (showUnderline) {
            underlineView.setVisibility(View.VISIBLE);
        } else {
            underlineView.setVisibility(View.GONE);
        }
    }

    /**
     * Item状态切换监听
     */
    public interface OnItemStateChangedClickListener {
        void onItemStateChange(int position, AHAbstractExpandView.ExpandMenuItem menuItem);
    }
}

