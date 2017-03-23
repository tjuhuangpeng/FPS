package navigation;

/**
 * Created by Administrator on 2016/12/29.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.learning.R;

/**
 * 可左右扩展滑动TabBar
 */
public class AHExtendedSlidingTabBar extends RelativeLayout {

    private AHBaseSlidingTabBar slidingTabBar;
    private View leftExtensionView;
    private View rightExtensionView;
    private LinearLayout leftLayout;
    private LinearLayout middleLayout;
    private LinearLayout rightLayout;
    private ImageView coverImage;

    private int leftMargin;
    private int rightMargin;
    private boolean showScrollCover = false;

    public AHExtendedSlidingTabBar(Context context) {
        this(context, null);
    }

    public AHExtendedSlidingTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AHExtendedSlidingTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ah_extend_sliding_tabar, this);
        setBackgroundColor(getResources().getColor(R.color.ahlib_common_color09));
        leftLayout = (LinearLayout) findViewById(R.id.ah_common_tabbar_layout_left);
        rightLayout = (LinearLayout) findViewById(R.id.ah_common_tabbar_layout_right);
        middleLayout = (LinearLayout) findViewById(R.id.ah_common_tabbar_layout_middle);
        coverImage = (ImageView) findViewById(R.id.ah_common_tabbar_cover);
        leftMargin = ScreenUtils.dpToPxInt(getContext(), 0);
        rightMargin = ScreenUtils.dpToPxInt(getContext(), 15);
    }

    public void setLeftMargin(int px) {
        this.leftMargin = px;
        LayoutParams layoutParams = (LayoutParams) leftLayout.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        layoutParams.leftMargin = leftMargin;
        leftLayout.setLayoutParams(layoutParams);
        requestLayout();
    }

    public void setRightMargin(int px) {
        this.rightMargin = px;
        LayoutParams layoutParams = (LayoutParams) rightLayout.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
        layoutParams.rightMargin = rightMargin;
        rightLayout.setLayoutParams(layoutParams);
        requestLayout();
    }

    public AHBaseSlidingTabBar getSlidingTabBar() {
        return slidingTabBar;
    }

    /**
     * 填充TabBar内容
     * @param slidingTabBar
     */
    public void setSlidingTabBar(AHBaseSlidingTabBar slidingTabBar) {
        if (slidingTabBar != null) {
            middleLayout.removeAllViews();
            this.slidingTabBar = slidingTabBar;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            middleLayout.addView(this.slidingTabBar, layoutParams);
        }
    }

    public View getLeftExtensionView() {
        return leftExtensionView;
    }

    /**
     * 设置左侧扩展View
     * @param leftExtensionView
     */
    public void setLeftExtensionView(View leftExtensionView) {
        if (leftExtensionView != null) {
            leftLayout.removeAllViews();
            this.leftExtensionView = leftExtensionView;
            leftLayout.addView(this.leftExtensionView);
        }
    }

    public View getRightExtensionView() {
        return rightExtensionView;
    }

    /**
     * 设置右侧扩展View
     * @param rightExtensionView
     */
    public void setRightExtensionView(View rightExtensionView) {
        LayoutParams layoutParams = (LayoutParams) rightLayout.getLayoutParams();
        if (rightExtensionView != null) {
            rightLayout.removeAllViews();
            this.rightExtensionView = rightExtensionView;
            rightLayout.addView(this.rightExtensionView);
            layoutParams.rightMargin = rightMargin;
        } else {
            layoutParams.rightMargin = 0;
        }
        rightLayout.setLayoutParams(layoutParams);
    }

    public void changeSkin() {
        if (slidingTabBar != null) {
            slidingTabBar.changedSkin();
        }
    }

    public boolean isShowScrollCover() {
        return showScrollCover;
    }

    /**
     * 设置是否显示白色渐变遮罩
     * @param showScrollCover
     */
    public void setShowScrollCover(boolean showScrollCover) {
        this.showScrollCover = showScrollCover;
        if (showScrollCover) {
            coverImage.setVisibility(View.VISIBLE);
        } else {
            coverImage.setVisibility(View.GONE);
        }
    }
}
