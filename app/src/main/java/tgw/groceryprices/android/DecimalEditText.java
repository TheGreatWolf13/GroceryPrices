package tgw.groceryprices.android;

import android.content.Context;
import android.util.AttributeSet;

import tgw.groceryprices.utils.Utils;

public class DecimalEditText extends FormattedEditText {

    public DecimalEditText(Context context) {
        super("0,00", Utils.FORMAT_DECIMAL, context);
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super("0,00", Utils.FORMAT_DECIMAL, context, attrs);
    }

    public DecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super("0,00", Utils.FORMAT_DECIMAL, context, attrs, defStyleAttr);
    }
}
