package navigation;

/**
 * Created by Administrator on 2016/12/29.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.administrator.learning.R;

public class AHSimpleBadgeTextView extends TextView {
    /**
     * 居中
     */
    public static final int MODE_CENTER = 1;
    /**
     * 上左
     */
    public static final int MODE_TOP_LEFT = 2;
    /**
     * 下左
     */
    public static final int MODE_BOTTOM_LEFT = 3;
    /**
     * 上右
     */
    public static final int MODE_TOP_RIGHT = 4;
    /**
     * 下右
     */
    public static final int MODE_BOTTOM_RIGHT = 5;

    private int radius = 5;

    private int paddingHorizontal = 0;

    private int paddingVertical = 0;

    private Paint paint = new Paint();

    private boolean isShowBadge = false;

    private int mode = MODE_CENTER;
    /**
     * Simple constructor to use when creating a view from code.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public AHSimpleBadgeTextView(Context context) {
        super(context);
        initBadge();
    }
    /**
     * Constructor that is called when inflating a view from XML. This is called
     * when a view is being constructed from an XML file, supplying attributes
     * that were specified in the XML file. This version uses a default style of
     * 0, so the only attribute values applied are those in the Context's Theme
     * and the given AttributeSet.
     *
     * <p>
     * The method onFinishInflate() will be called after all children have been
     * added.
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public AHSimpleBadgeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBadge();
    }
    /**
     * Perform inflation from XML and apply a class-specific base style from a
     * theme attribute or style resource. This constructor of View allows
     * subclasses to use their own base style when they are inflating.
     * <p>
     * When determining the final value of a particular attribute, there are
     * four inputs that come into play:
     * <ol>
     * <li>Any attribute values in the given AttributeSet.
     * <li>The style resource specified in the AttributeSet (named "style").
     * <li>The default style specified by <var>defStyleAttr</var>.
     * <li>The default style specified by <var>defStyleRes</var>.
     * <li>The base values in this theme.
     * </ol>
     * <p>
     * Each of these inputs is considered in-order, with the first listed taking
     * precedence over the following ones. In other words, if in the
     * AttributeSet you have supplied <code>&lt;Button * textColor="#ff000000"&gt;</code>
     * , then the button's text will <em>always</em> be black, regardless of
     * what is specified in any of the styles.
     */
    public AHSimpleBadgeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBadge();
    }

    private void initBadge() {
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setLines(1);

        paint.setColor(getResources().getColor(R.color.ahlib_common_textcolor07));
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (isShowBadge) {
            int x = 0;
            int y = 0;
            switch (mode) {
                case MODE_CENTER:
                    x = getMeasuredWidth() / 2 + paddingHorizontal;
                    // 绘制红点时，需要检测一下，是否已经超出了控件真实的区域。
                    if (x + radius > getMeasuredWidth()) {
                        x = getMeasuredWidth() - radius;
                    }
                    y = getMeasuredHeight() / 2 + paddingVertical;
                    break;
                case MODE_TOP_LEFT:
                    x = 0 + paddingHorizontal;
                    y = 0 + paddingVertical;
                    break;
                case MODE_BOTTOM_LEFT:
                    x = 0 + paddingHorizontal;
                    y = getMeasuredHeight() + paddingVertical;
                    break;
                case MODE_TOP_RIGHT:
                    x = getMeasuredWidth() + paddingHorizontal;
                    y = 0 + paddingVertical;
                    break;
                case MODE_BOTTOM_RIGHT:
                    x = getMeasuredWidth() + paddingHorizontal;
                    y = getMeasuredHeight() + paddingVertical;
                    break;
            }
            canvas.drawCircle(x, y, radius, paint);
        }
    }
    /**
     * 设置红点展示模式
     * <ol>
     * <li> 居中模式 {@link #MODE_CENTER}
     * <li> 左上模式 {@link #MODE_TOP_LEFT}
     * <li> 右上模式 {@link #MODE_TOP_RIGHT}
     * <li> 左下模式 {@link #MODE_BOTTOM_LEFT}
     * <li> 右下模式 {@link #MODE_BOTTOM_RIGHT}
     * </ol>
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * 设置提示点色值
     * @param color 色值
     */
    public void setBadgeColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    /**
     * 设置提示点的半径
     * @param radius 半径
     */
    public void setBadgeRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    /**
     * 设置提示点的水平padding和垂直padding
     * @param paddingHorizontal 水平padding
     * @param paddingVertical 垂直padding
     */
    public void setBadgePadding(int paddingHorizontal, int paddingVertical) {
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;
        invalidate();
    }

    /**
     * 显示红色标记
     */
    public void showBadge() {
        isShowBadge = true;
        invalidate();
    }

    /**
     * 隐藏红色标记
     */
    public void hideBadge() {
        isShowBadge = false;
        invalidate();
    }

}

