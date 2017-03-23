package navigation;

/**
 * Created by Administrator on 2016/12/29.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.learning.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 可滑动TabBar的实现类
 */
public class AHSlidingTabBar extends AHBaseSlidingTabBar {

    /**
     * 使TabBar布局文件支持android:textSize和android:textColor配置。
     */
    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};

    private OnScrollStopListener onScrollStopListener;
    /**
     * 标题字母大写
     */
    private boolean textAllCaps = false;
    /**
     * tab导航文字大小
     */
    private int tabTextSize = 16;
    /**
     * tab导航文字默认颜色
     */
    private ColorStateList tabTextColor = null;
    /**
     * tab导航文字选中颜色
     */
    private int tabTextColorFocus;
    /**
     * tab导航文字选中颜色(单个居中)
     */
    private int tabTextColorFocusSingleCenter;
    /**
     * 文本字体等
     */
    private Typeface tabTypeface = null;
    /**
     * 字体加粗,斜体等
     */
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int hotPointX = (int) getResources().getDimension(
            R.dimen.ahlib_common_sliding_scroll_view_horizontal);// 降价活动画红点x轴
    private int hotPointY = (int) getResources().getDimension(
            R.dimen.ahlib_common_sliding_scroll_view_vertical);// 降价活动画红点y轴
    private int hotRadius = (int) getResources().getDimension(
            R.dimen.ahlib_common_sliding_scroll_view_radius);// 画红点圆半径

    /**
     * 文本TabBar适配器
     */
    private AHBaseSlidingBarAdapter textAdapter;
    /**
     * 图片TabBar适配器
     */
    private AHBaseSlidingBarAdapter imageAdapter;

    /**
     * 构造函数
     *
     * @param context
     */
    public AHSlidingTabBar(Context context) {
        this(context, null);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public AHSlidingTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AHSlidingTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        init();
        changedSkin();
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        a.recycle();
    }

    @Override
    protected void init() {
        super.init();
        changedSkin();
    }

    @Override
    protected void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateTabStyles();
        updateTabTextColor(currentPosition);
    }

    protected ImageView createImageTab(Drawable icon, LinearLayout.LayoutParams params) {
        ImageView tab = new ImageView(getContext());
        tab.setImageDrawable(icon);
        if (params != null) {
            tab.setLayoutParams(params);
        }
        return tab;
    }

    protected AHSimpleBadgeTextView createTextTab(String title) {
        AHSimpleBadgeTextView tab = new AHSimpleBadgeTextView(getContext());
        tab.setBadgePadding(hotPointX, hotPointY);
        tab.setBadgeRadius(hotRadius);
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        return tab;
    }

    /**
     * 在特定的Item上显示红点
     *
     * @param position
     */
    public void showBadge(int position) {
        View v = tabsContainer.getChildAt(position);
        if (v instanceof AHSimpleBadgeTextView) {
            AHSimpleBadgeTextView tab = (AHSimpleBadgeTextView) v;
            tab.showBadge();
        }
    }

    /**
     * 在特定的Item上隐藏红点
     *
     * @param position
     */
    public void hideBadge(int position) {
        View v = tabsContainer.getChildAt(position);
        if (v instanceof AHSimpleBadgeTextView) {
            AHSimpleBadgeTextView tab = (AHSimpleBadgeTextView) v;
            tab.hideBadge();
        }
    }

    /**
     * 获得特定位置的View
     *
     * @param position
     * @return
     */
    public View getTextTabView(int position) {
        return tabsContainer.getChildCount() > position ? tabsContainer.getChildAt(position) : null;
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            View v = tabsContainer.getChildAt(i);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case
                // is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString().toUpperCase(locale));
                    }
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (onScrollStopListener != null) {
            onScrollStopListener.onScrollStopped();
        }
    }

    /**
     * 修改选中tab文字颜色
     */
    public void updateTabTextColor(int position) {
        for (int i = 0; i < tabsContainer.getChildCount(); i++) {
            View v = tabsContainer.getChildAt(i);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                if (i == position) {
                    tab.setTextColor(tabTextColorFocus);
                } else {
                    tab.setTextColor(tabTextColor);
                }
                if (tabsContainer.getChildCount() == 1 && isCenterMode()) {
                    tab.setTextColor(tabTextColorFocusSingleCenter);
                }
            }
        }
    }

    /**
     * 判断导航栏文字是否都是大写
     */

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    /**
     * 设置导航栏文字是否都是大写
     */
    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    /**
     * 设置导航栏文字大小
     */
    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    /**
     * 获取导航栏文字大小
     */
    public int getTextSize() {
        return tabTextSize;
    }

    /**
     * 设置导航栏文字字体
     */
    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    /**
     * 设置导航栏每个tab左右边距
     */
    public void setTabPaddingLeftRight(int paddingPx) {
        setTabHorizontalPadding(paddingPx);
        updateTabStyles();
    }

    /**
     * 获取导航栏每个tab左右边距
     */
    public int getTabPaddingLeftRight() {
        return getTabHorizontalPadding();
    }

    /**
     * 这一块代码在AHSlidingTabBar(Context context, AttributeSet attrs, int defStyle)里也有
     */
    @Override
    public void changedSkin() {
        super.changedSkin();
        tabTextColor = getContext().getResources().getColorStateList(R.color.ahlib_common_color05);//.getColorStateList(getContext(), "dir_primarybar_txt");
        tabTextColorFocus = getContext().getResources().getColor(R.color.ahlib_common_color02);//ResUtil.getColor(getContext(), ResUtil.TEXT_COLOR_02);
        tabTextColorFocusSingleCenter = getContext().getResources().getColor(R.color.ahlib_common_color02);//ResUtil.getColor(getContext(), ResUtil.TEXT_COLOR_03);
    }

    /**
     * 设置导航栏tab文字颜色
     */
    public void setTabTextColor(ColorStateList colorStateList) {
        tabTextColor = colorStateList;
    }

    public void setTabTextColorFocus(int tabTextColorFocus) {
        this.tabTextColorFocus = tabTextColorFocus;
    }

    public void setTabTextColorFocusSingleCenter(int tabTextColorFocusSingleCenter) {
        this.tabTextColorFocusSingleCenter = tabTextColorFocusSingleCenter;
    }

    /**
     * 创建文本可滑动TabBar
     * @param textTabs
     */
    public void setTextTabs(final List<String> textTabs) {
        if (textAdapter == null) {
            textAdapter = new AHBaseSlidingBarAdapter() {
                @Override
                public int getCount() {
                    return textTabs == null ? 0 : textTabs.size();
                }

                @Override
                public Object getItem(int position) {
                    return textTabs.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return createTextTab(textTabs.get(position));
                }
            };
        }
        setAdapter(textAdapter);
    }

    /**
     * 创建图片可滑动TabBar
     * @param imageTabs
     */
    public void setImageTabs(final List<Drawable> imageTabs) {
        setImageTabs(imageTabs, 0, 0);
    }

    /**
     * 创建支持自定义宽度高度的图片可滑动TabBar
     * @param imageTabs
     * @param width
     * @param height
     */
    public void setImageTabs(final List<Drawable> imageTabs, int width, int height) {
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (width > 0) {
            layoutParams.width = width;
        }
        if (height > 0) {
            layoutParams.height = height;
        }
        if (imageAdapter == null) {
            imageAdapter =  new AHBaseSlidingBarAdapter() {
                @Override
                public int getCount() {
                    return imageTabs == null ? 0 : imageTabs.size();
                }

                @Override
                public Object getItem(int position) {
                    return imageTabs.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    return createImageTab(imageTabs.get(position), layoutParams);
                }
            };
        }
        setAdapter(imageAdapter);
    }

    /**
     * 创建图片可滑动TabBar
     * @param imageTabs
     */
    public void setImageTabs(final int[] imageTabs) {
        setImageTabs(imageTabs, 0, 0);
    }

    /**
     * 创建支持自定义宽度高度的图片可滑动TabBar
     * @param imageTabs
     * @param width
     * @param height
     */
    public void setImageTabs(final int[] imageTabs, int width, int height) {
        if (imageTabs == null || imageTabs.length == 0) {
            return;
        }
        List<Drawable> drawableList = new ArrayList<>(imageTabs.length);
        for (int i : imageTabs) {
            drawableList.add(getResources().getDrawable(i));
        }
        setImageTabs(drawableList, width, height);
    }

    /**
     * TODO 滑动到特定Position的某位置，使用Scroller控制动画效果
     *
     * @param position
     * @param offset
     */
    public void scrollToChildWithAnim(int position, int offset) {
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        updateTabTextColor(position);
        super.onClick(view);
    }

    public void setOnScrollStopListener(OnScrollStopListener listener) {
        this.onScrollStopListener = listener;
    }

    /**
     * 滑动监听(控制遮罩显示)
     */
    public interface OnScrollStopListener {
        void onScrollStopped();
    }

    /**
     * 滑动监听(控制遮罩显示)
     */
    @Deprecated
    public interface OnScrollStopListner {
        void onScrollStoped();
    }
}
