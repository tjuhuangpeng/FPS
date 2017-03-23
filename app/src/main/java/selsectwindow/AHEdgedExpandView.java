package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.learning.R;

/**
 * 两边对齐显示的可折叠控件
 */
public class AHEdgedExpandView extends AHAbstractExpandView {
    public AHEdgedExpandView(Context context) {
        super(context);
    }

    public AHEdgedExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AHEdgedExpandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void fillContent() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.ahlib_common_edge_expand_view, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        contentTextView = (TextView) findViewById(R.id.ah_common_edge_expand_text);
        statusImageView = (ImageView) findViewById(R.id.ah_common_edge_expand_status_image);
    }

    @Override
    protected void updateUI(ExpandMenuItem menuItem) {
        contentTextView.setText(menuItem.menuContent);
        statusImageView.setImageDrawable(menuItem.isExpanded ? expandIcon : collapseIcon);
        LayoutParams contentParams = (LayoutParams) contentTextView.getLayoutParams();
        contentParams.leftMargin = leftMargin;
        contentTextView.setLayoutParams(contentParams);
        LayoutParams statusParams = (LayoutParams) statusImageView.getLayoutParams();
        statusParams.rightMargin = rightMargin;
        statusImageView.setLayoutParams(statusParams);
    }

}

