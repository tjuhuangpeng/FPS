package selsectwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.administrator.learning.R;

/**
 * 自定义弹出窗口，内容最高为屏幕高度0.6倍，下滑动画展示，上滑动画收起
 */
public class AHSelectWindow extends AHCompatPopupWindow{
    // 窗口在此View之下展示
    protected View anchorView;
    // 根布局
    protected View rootContentLayout;
    // 内容布局，最高为屏幕高度0.6倍
    protected LinearLayout contentLayout;
    protected Activity parentActivity;
    private OnDismissListener dismissListener;
    private Drawable background;
    protected Context context;

    // 底部预留高度
    private int bottomHeight = -1;
    // 控件主体高度
    private int hostFrameHeight = -1;
    // 默认底部预留高度
    private int defaultBottomHeight = 0;

    public AHSelectWindow(Context context) {
        this(context, null);
    }

    public AHSelectWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AHSelectWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public AHSelectWindow(final Activity activity, View contentView, View anchorView) {
        this(activity);
        this.anchorView = anchorView;
        this.parentActivity = activity;
        LayoutInflater inflater = LayoutInflater.from(context);
        rootContentLayout = inflater.inflate(R.layout.ahlib_common_select_wondow, null);

        contentLayout = (LinearLayout) rootContentLayout.findViewById(R.id.ah_common_select_window_content);
        contentLayout.addView(contentView, 0);
        setContentView(rootContentLayout);
        // 点击空白区域关闭窗口
        rootContentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        contentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        init();
    }

    protected void init() {
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(getMaxAvailableHeight(anchorView));
        setWindowBackgroundDrawable("#00000000");
        setAnimationStyle(R.style.PopupAnimation_cp);

        update();
        setOutsideTouchable(true);
        setTouchable(true);
        setFocusable(true);

        super.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
//                LayoutParams lp = parentActivity.getWindow().getAttributes();
//                lp.alpha = 1f;
//                lp.dimAmount = 1f;
//                parentActivity.getWindow().setAttributes(lp);

                if (dismissListener != null) {
                    dismissListener.onDismiss();
                }
            }
        });
    }

    public int getBottomHeight() {
        return bottomHeight;
    }

    public void setBottomHeight(int bottomHeight) {
        this.bottomHeight = bottomHeight;
    }

    public int getHostFrameHeight() {
        return hostFrameHeight;
    }

    public void setHostFrameHeight(int hostFrameHeight) {
        this.hostFrameHeight = hostFrameHeight;
    }

    public void setWindowBackgroundDrawable(@DrawableRes int drawableId) {
        background = parentActivity.getResources().getDrawable(drawableId);
    }

    public void setWindowBackgroundDrawable(@NonNull String backColor) {
        if (!TextUtils.isEmpty(backColor)) {
            background = new ColorDrawable(Color.parseColor(backColor));
        }
    }

    public void setWindowBackgroundColor(@ColorInt int color) {
        background = new ColorDrawable(color);
    }

    @Override
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
    }

    public void show() {
        show(0, 0);
    }

    /**
     * 带偏移量显示Window内容，计算高度，设置动画
     * @param xoff
     * @param yoff
     */
    public void show(int xoff, int yoff) {
        if (contentLayout != null) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentLayout.getLayoutParams();
            params.height = countHeight();
            contentLayout.setLayoutParams(params);
        }
        setBackgroundDrawable(background);
        try {
            // catch exception, in case of Adding window failed because TransactionTooLargeException
/*            if (android.os.Build.VERSION.SDK_INT >= 24) {
                int[] anchorLocation = new int[2];
                anchorView.getLocationInWindow(anchorLocation);
                showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.NO_GRAVITY,
                        anchorLocation[0] + xoff, anchorLocation[1] + anchorView.getHeight() + yoff);
            } else{*/
            showAsDropDown(anchorView, xoff, yoff);
            // }

            rootContentLayout.clearAnimation();
            contentLayout.clearAnimation();

            rootContentLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.ahlib_common_popup_alpha_in));
            contentLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.ahlib_common_popup_list_in));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        LayoutParams lp = parentActivity.getWindow().getAttributes();
//        lp.alpha = 1.0f;
//        lp.dimAmount = 0.6f;
//        parentActivity.getWindow().setAttributes(lp);
        //contentLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.ahlib_common_popup_list_in));
    }

    /**
     * 计算内容区域高度，强制设置为屏幕高度的60%
     * @return
     */
    private int countHeight() {
        return  (int) (ScreenUtils.getScreenHeight(context) * 0.6f);
    }

    public View getAnchorView() {
        return anchorView;
    }

    public void setAnchorView(View anchorView) {
        this.anchorView = anchorView;
    }

    @Override
    public void dismiss() {
        if (rootContentLayout == null || contentLayout == null) {
            super.dismiss();
            return;
        }

        if (isDismiss) {
            isDismiss = false;
            super.dismiss();
        } else {
            rootContentLayout.clearAnimation();
            contentLayout.clearAnimation();
            animDismiss();
        }
    }

    private boolean isDismiss = false;

    /**
     * 窗口消失动画
     */
    public void animDismiss() {
        rootContentLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.ahlib_common_popup_alpha_out));
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.ahlib_common_popup_list_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isDismiss = false;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isDismiss = true;
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        contentLayout.setAnimation(anim);
    }

}
