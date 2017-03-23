package navigation;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.administrator.learning.R;

public class AHDownToast extends Toast {
    private Context context;
    private String warnText;
    private TextView view;
    private LinearLayout ll;

    public AHDownToast(Context context) {
        super(context);
        this.context = context;
        this.init();
    }

    public AHDownToast(Context context, String warText, View visableView) {
        super(context);
        this.context = context;
        this.warnText = warText;
        this.setPosition(visableView);
        this.init();
    }

    public AHDownToast(Context context, String warText) {
        super(context);
        this.context = context;
        this.warnText = warText;
        this.init();
    }

    private void init() {
        this.ll = new LinearLayout(this.context);
        this.ll.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        this.view = new TextView(this.context);
        this.view.setLayoutParams(new LinearLayout.LayoutParams(-1, dpToPxInt(this.context, 40.0F)));
        this.view.setGravity(17);
        this.view.setText(TextUtils.isEmpty(this.warnText)?"当前网络不可用，请检查网络设置":this.warnText);
        this.view.setBackgroundColor(this.context.getResources().getColor(R.color.ahlib_common_ahwebview_bgcolor08));
        this.view.setTextColor(this.context.getResources().getColor(R.color.ahlib_common_ahwebview_textcolor09));
        this.view.setTextSize(2, 15.0F);
        this.ll.addView(this.view);
        this.setView(this.ll);
        this.setGravity(87, 0, dpToPxInt(this.context, 45.0F));
    }

    public void setText(String text) {
        this.warnText = text;
        this.view.setText(TextUtils.isEmpty(this.warnText)?"当前网络不可用，请检查网络设置":this.warnText);
    }

    public void setBlue(boolean isBlue) {
        if(Build.VERSION.SDK_INT >= 11) {
            this.view.setAlpha(0.96F);
        }

        if(isBlue) {
            this.view.setBackgroundColor(this.context.getResources().getColor(R.color.ahlib_common_ahwebview_textcolor02));
            this.view.setTextColor(this.context.getResources().getColor(R.color.ahlib_common_ahwebview_textcolor09));
        } else {
            this.view.setBackgroundColor(this.context.getResources().getColor(R.color.ahlib_common_bgcolor08));
            //this.view.setTextColor(this.context.getResources().getColor(R.color.ahlib_common_bgcolor03);
        }

    }

    public void setPosition(View visableView) {
        if(visableView == null) {
            this.setGravity(87, 0, 0);
        } else {
            this.setGravity(87, 0, visableView.getMeasuredHeight());
        }

    }

    public static int dpToPxInt(Context context, float dp) {
        return (int)(dpToPx(context, dp) + 0.5F);
    }

    public static float dpToPx(Context context, float dp) {
        return context == null?-1.0F:dp * context.getResources().getDisplayMetrics().density;
    }
}
