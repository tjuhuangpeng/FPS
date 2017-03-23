package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.learning.R;


/**
 * 居中显示的可折叠控件
 */
public class AHCenteredExpandView extends AHAbstractExpandView {

    public AHCenteredExpandView(Context context) {
        this(context, null);
    }

    public AHCenteredExpandView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AHCenteredExpandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void fillContent() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ahlib_common_center_expand_view, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setGravity(Gravity.CENTER);
        contentTextView = (TextView) findViewById(R.id.ah_common_center_expand_text);
        statusImageView = (ImageView) findViewById(R.id.ah_common_center_expand_status_image);
    }

    @Override
    protected void updateUI(ExpandMenuItem menuItem) {
        contentTextView.setText(menuItem.menuContent);
        statusImageView.setImageDrawable(menuItem.isExpanded ? expandIcon : collapseIcon);
        LinearLayout.LayoutParams statusLayoutParams = (LinearLayout.LayoutParams) statusImageView.getLayoutParams();
        if (statusLayoutParams == null) {
            statusLayoutParams = new LinearLayout.LayoutParams(drawableWidth, drawableWidth);
        }
        statusLayoutParams.leftMargin = centerMargin;
        statusLayoutParams.rightMargin = rightMargin;
        statusImageView.setLayoutParams(statusLayoutParams);
        LinearLayout.LayoutParams leftParams = (LinearLayout.LayoutParams) contentTextView.getLayoutParams();
        leftParams.leftMargin = leftMargin;
        contentTextView.setLayoutParams(leftParams);
    }

}
