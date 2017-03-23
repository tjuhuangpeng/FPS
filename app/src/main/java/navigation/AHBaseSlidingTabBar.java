package navigation;

/**
 * Created by Administrator on 2016/12/29.
 */

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.learning.R;

import java.util.Locale;

/**
 * <p>导航条控件带滚动条和下分隔线，支持子View之间左右滑动的，支持设置显示下划引导线的功能。导航菜单可为文本或图片，可以只有一个导航菜单。</p>
 */
public class AHBaseSlidingTabBar extends HorizontalScrollView implements View.OnClickListener {

    /**
     * tab带有margin边距
     */
    protected LinearLayout.LayoutParams defaultTabLayoutParams;
    /**
     * tab不带有margin边距
     */
    protected LinearLayout.LayoutParams expandedTabLayoutParams;

    /**
     * {@link HorizontalScrollView}下的tab容器。
     */
    protected LinearLayout tabsContainer;
    /**
     * 当前选中的菜单位置
     */
    protected int currentPosition = 0;
    /**
     * 导航菜单滑动偏移量
     */
    protected float currentPositionOffset = 0f;

    protected Paint rectPaint;
    /**
     * tab导航文字下划线蓝色
     */
    protected int indicatorColor;
    /**
     * tab导航下边线颜色
     */
    protected int underlineColor;
    /**
     * 为true tab不带margin，tab要间隔小，主要决定addTab时layoutParam 用哪个（{@link #defaultTabLayoutParams}和{@link #expandedTabLayoutParams}）
     */
    protected boolean shouldExpand = false;
    /**
     * 滚动条滚动偏移量
     */
    protected int scrollOffset = 0;
    /**
     * 下划线高度
     */
    private int indicatorHeight = 0;
    /**
     * 导航底部灰色底边
     */
    private int underlineHeight = 0;
    /**
     * 是否显示下划线
     */
    private boolean indicatorVisible = true;
    /**
     * 是否显示导航底部灰色底边
     */
    private boolean underlineVisible = true;
    /**
     * 导航栏分隔线上下边距
     */
    private int dividerPadding = 0;
    /**
     * tab导航View的Padding
     */
    private int tabPadding = 0;
    /**
     * tab导航View的间隔
     */
    private int tabMargins = 0;
    /**
     * 下划线水平方向padding
     */
    private int indicatorPadding = 0;
    private int lastScrollX = 0;
    protected int clickPosition = -1;
    protected boolean needScrollToClick = false;
    protected Locale locale;

    private AHBaseSlidingBarAdapter mAdapter;
    private DataSetObserver mDataSetObserver;
    private OnItemClickListener mOnItemClickListener;
    private boolean mIsCenterMode = false;
    protected boolean mIsBindViewPager = false;

    /**
     * 构造函数
     *
     * @param context
     */
    public AHBaseSlidingTabBar(Context context) {
        this(context, null);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public AHBaseSlidingTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AHBaseSlidingTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        setFillViewport(true);
        setWillNotDraw(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setBackgroundColor(getResources().getColor(R.color.ahlib_common_color09));

        tabsContainer = new LinearLayout(getContext());
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        if (mIsCenterMode) {
            tabsContainer.setGravity(Gravity.CENTER);
        }

        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        tabMargins = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, dm);
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, dm);

        changedSkin();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.FILL);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        defaultTabLayoutParams.setMargins(tabMargins, 0, tabMargins, 0);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void changedSkin() {
        indicatorColor = getContext().getResources().getColor(R.color.ahlib_common_color02);
        //ResUtil.getColor(getContext(), ResUtil.TEXT_COLOR_02);
        underlineColor = getContext().getResources().getColor(R.color.ahlib_common_color02);
        //ResUtil.getColor(getContext(), ResUtil.BG_COLOR_03);
    }

    protected void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        int tabCount = mAdapter.getCount();

        for (int i = 0; i < tabCount; i++) {
            View tabView = mAdapter.getView(i, null, this);
            tabView.setTag(i);
            tabView.setOnClickListener(this);
            tabView.setPadding(tabPadding, 0, tabPadding, 0);
            tabsContainer.addView(tabView, i, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
        }
        tabsContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (tabsContainer.getViewTreeObserver().isAlive()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        tabsContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        tabsContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
                int itemAvailableWidth = AHBaseSlidingTabBar.this.getWidth() - getPaddingLeft() - getPaddingRight();
                int maxItemWidth = 0;
                int childCount = tabsContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabsContainer.getChildAt(i).getLayoutParams();
                    int width = tabsContainer.getChildAt(i).getWidth();
                    if (params != null) {
                        width = width + params.leftMargin + params.rightMargin;
                    }
                    maxItemWidth = Math.max(width, maxItemWidth);
                }
                if (shouldExpand && childCount > 0 && maxItemWidth > itemAvailableWidth / childCount) {
                    for (int i = 0; i < childCount; i++) {
                        int itemWidth = itemAvailableWidth / childCount;
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabsContainer.getChildAt(i).getLayoutParams();
                        if (params != null) {
                            itemWidth = itemWidth - params.leftMargin - params.rightMargin;
                        } else {
                            params = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                        }
                        params.width = itemWidth;
                        tabsContainer.getChildAt(i).setLayoutParams(params);
                    }
                }
                if (!mIsBindViewPager) {
                    scrollToChild(currentPosition, 0f);
                }
            }
        });
    }

    private void registerObserver() {
        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (mAdapter == null) {
                        return;
                    }
                    notifyDataSetChanged();
                }
            };
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    private void unregisterObserver() {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterObserver();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        final int height = getHeight();

        // draw indicator line
        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        if (currentTab instanceof TextView && shouldExpand) {
            float gap = calculateGap((TextView) currentTab);
            lineLeft = lineLeft + gap;
            lineRight = lineRight - gap;
        }

        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < mAdapter.getCount() - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            float nextTabLeft = nextTab.getLeft();
            float nextTabRight = nextTab.getRight();
            if (nextTab instanceof TextView && shouldExpand) {
                float gap = calculateGap((TextView) nextTab);
                nextTabLeft = nextTabLeft + gap;
                nextTabRight = nextTabRight - gap;
            }

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);
        }
        if (indicatorVisible) {
            canvas.drawRect(lineLeft + indicatorPadding, height - indicatorHeight - underlineHeight,
                    lineRight - indicatorPadding, height - underlineHeight, rectPaint);
        }

        if (underlineVisible) {
            // draw underline
            rectPaint.setColor(underlineColor);
            canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
        }
    }

    private float calculateGap(TextView badgeTextView) {
        Paint paint = badgeTextView.getPaint();
        float textWidth = paint.measureText(badgeTextView.getText().toString());
        return (badgeTextView.getWidth() - textWidth) / 2;
    }

    /**
     * 设置底部的导航条的引导线的颜色值
     *
     * @param indicatorColor 颜色值
     */
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    /**
     * 设置底部的导航条的引导线的颜色资源ID
     *
     * @param resId 颜色的资源ID
     */
    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    /**
     * 获得底部引导线的颜色值
     *
     * @return
     */
    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    /**
     * 设置底部的导航条的引导线的高度
     *
     * @param indicatorLineHeightPx 引导线高度
     */
    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    /**
     * 获得底部引导线的高度
     *
     * @return
     */
    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    /**
     * 设置底部划线的颜色
     *
     * @param underlineColor
     */
    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    /**
     * 设置底部划线的颜色
     *
     * @param colorResId
     */
    public void setUnderlineColorResource(int colorResId) {
        this.underlineColor = getResources().getColor(colorResId);
        invalidate();
    }

    /**
     * 得到底部划线的颜色
     */
    public int getUnderlineColor() {
        return underlineColor;
    }

    /**
     * 设置底部划线的高度
     */
    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    /**
     * 得到底部划线的高度
     */
    public int getUnderlineHeight() {
        return underlineHeight;
    }

    /**
     * 设置Divider的padding
     *
     * @param dividerPaddingPx
     */
    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    /**
     * 得到Divider的padding
     */
    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    /**
     * 设置导航栏每个tab左右边距
     */
    public void setTabHorizontalPadding(int paddingPx) {
        this.tabPadding = paddingPx;
    }

    /**
     * 设置导航栏每个tab左右margin边距
     */
    public void setTabHorizontalMargin(int marginPx) {
        this.tabMargins = marginPx;
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        defaultTabLayoutParams.setMargins(tabMargins, 0, tabMargins, 0);
        notifyDataSetChanged();
        requestLayout();
    }

    /**
     * 获取导航栏每个tab左右边距
     */
    public int getTabHorizontalPadding() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setAdapter(AHBaseSlidingBarAdapter adapter) {
        unregisterObserver();
        this.mAdapter = adapter;
        registerObserver();
        boolean isSingle = mAdapter != null && mAdapter.getCount() == 1;
        indicatorVisible = !isSingle;
        setIsCenterMode(isSingle);
        notifyDataSetChanged();
    }

    public AHBaseSlidingBarAdapter getAdapter() {
        return mAdapter;
    }


    public boolean isCenterMode() {
        return mIsCenterMode;
    }

    public void setIsCenterMode(boolean mIsCenterMode) {
        this.mIsCenterMode = mIsCenterMode;
        if (mIsCenterMode) {
            tabsContainer.setGravity(Gravity.CENTER);
        } else {
            tabsContainer.setGravity(Gravity.START | Gravity.TOP);
        }
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        if (mIsBindViewPager) {
            clickPosition = position;
            int[] viewPosition = new int[2];
            view.getLocationOnScreen(viewPosition);
            int[] containerPosition = new int[2];
            this.getLocationOnScreen(containerPosition);
            needScrollToClick = viewPosition[0] + view.getWidth() - (containerPosition[0] + getWidth()) > 0
                    || viewPosition[0] <= 0;
        } else {
            scrollToChild(position, 0f);
            invalidate();
        }
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(position, view, this);
        }
    }

    /**
     * 设置Item点击的监听器
     *
     * @param listener 监听器
     */
    public void setOnItemClickListener(AHBaseSlidingTabBar.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 滑动到特定Child的某位置
     *
     * @param position
     * @param relativeOffset 相对偏移量，从0到1
     */
    public void scrollToChild(int position, float relativeOffset) {
        currentPosition = position;
        currentPositionOffset = relativeOffset;
        if (relativeOffset > 0.998) {
            currentPosition = position + 1;
            currentPositionOffset = 0;
        }
        if (tabsContainer == null || tabsContainer.getChildAt(position) == null) {
            return;
        }
        int absoluteOffset = (int) (relativeOffset * tabsContainer.getChildAt(position).getWidth());
        scrollToChild(position, absoluteOffset);
    }

    /**
     * 滑动到特定Child的某位置
     *
     * @param position
     * @param absoluteOffset 绝对偏移量，单位：px
     */
    public void scrollToChild(int position, int absoluteOffset) {
        // 计算绝对偏移量
        if (mAdapter.getCount() == 0) {
            return;
        }
        if (mIsBindViewPager && clickPosition > 0 && !needScrollToClick) {
            return;
        }
        clickPosition = -1;
        needScrollToClick = false;
        boolean needScroll = false;
        View view = tabsContainer.getChildAt(position);
        if (view != null) {
            int[] viewPosition = new int[2];
            view.getLocationOnScreen(viewPosition);
            int[] containerPosition = new int[2];
            this.getLocationOnScreen(containerPosition);
            int gap = viewPosition[0] + view.getWidth() - (containerPosition[0] + getWidth());
            needScroll = gap > 0 || viewPosition[0] <= 0;
        }
        if (needScroll) {
            int newScrollX = tabsContainer.getChildAt(position).getLeft() + absoluteOffset;
            // TextView添加Margins后需要修改滑动起始点
//            newScrollX = newScrollX - ((position * 2) + 1) * tabMargins;
            newScrollX = newScrollX - tabMargins;
            if (position > 0 || absoluteOffset > 0) {
                newScrollX -= scrollOffset;
            }
            if (newScrollX != lastScrollX) {
                lastScrollX = newScrollX;
                scrollTo(newScrollX, 0);
            }
        }
    }

    /**
     * 选中导航中的某一项
     *
     * @param position
     */
    public void setSelection(int position) {
        // check adapter
        if (mAdapter == null) {
            return;
        }
        // check position
        if (position < 0 || position >= mAdapter.getCount()) {
            return;
        }
        // start scroll
        scrollToChild(position, 0f);
    }

    public boolean isIndicatorVisible() {
        return indicatorVisible;
    }

    /**
     * 是否显示导航器
     * @param indicatorVisible
     */
    public void setIndicatorVisible(boolean indicatorVisible) {
        this.indicatorVisible = indicatorVisible;
        invalidate();
    }

    public boolean isUnderlineVisible() {
        return underlineVisible;
    }

    public void setUnderlineVisible(boolean underlineVisible) {
        this.underlineVisible = underlineVisible;
        invalidate();
    }

    public int getIndicatorPadding() {
        return indicatorPadding;
    }

    public void setIndicatorPadding(int indicatorPadding) {
        this.indicatorPadding = indicatorPadding;
        invalidate();
    }

    /**
     * Item点击的监听器
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view, ViewGroup parentView);
    }

}

