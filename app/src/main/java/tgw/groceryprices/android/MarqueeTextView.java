package tgw.groceryprices.android;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class MarqueeTextView extends AppCompatTextView {

    public MarqueeTextView(Context context) {
        super(context);
        this.setAlwaysMarquee();
    }

    public MarqueeTextView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
        this.setAlwaysMarquee();
    }

    public MarqueeTextView(Context context, AttributeSet attributeset, int i) {
        super(context, attributeset, i);
        this.setAlwaysMarquee();
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    public void marquee() {
        this.setSelected(false);
        this.setSelected(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(true, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        if (focused) {
            super.onWindowFocusChanged(true);
        }
    }

    public void setAlwaysMarquee() {
        this.setSelected(true);
        this.setSingleLine(true);
        this.setEllipsize(TextUtils.TruncateAt.MARQUEE);
    }
}
