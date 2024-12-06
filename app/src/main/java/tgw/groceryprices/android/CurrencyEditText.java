package tgw.groceryprices.android;

import android.content.Context;
import android.util.AttributeSet;

import tgw.groceryprices.utils.Utils;

public class CurrencyEditText extends FormattedEditText {

    public CurrencyEditText(Context context) {
        super("R$ 0,00", Utils.FORMAT_CURRENCY, context);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super("R$ 0,00", Utils.FORMAT_CURRENCY, context, attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super("R$ 0,00", Utils.FORMAT_CURRENCY, context, attrs, defStyleAttr);
    }
}
