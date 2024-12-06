package tgw.groceryprices.android;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.text.DecimalFormat;

import tgw.groceryprices.utils.Utils;

public abstract class FormattedEditText extends AppCompatEditText {

    private String current = "";
    private final DecimalFormat format;

    public FormattedEditText(String startText, DecimalFormat format, Context context) {
        super(context);
        this.format = format;
        this.init(startText);
    }

    public FormattedEditText(String startText, DecimalFormat format, Context context, AttributeSet attrs) {
        super(context, attrs);
        this.format = format;
        this.init(startText);
    }

    public FormattedEditText(String startText, DecimalFormat format, Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.format = format;
        this.init(startText);
    }

    public int getCleanIntValue() throws NumberFormatException {
        return Math.min(Integer.parseUnsignedInt(this.getCleanString()), (1 << 24) - 1);
    }

    protected String getCleanString() {
        String text = Utils.getText(this);
        return Utils.NOT_DIGIT.matcher(text).replaceAll("");
    }

    private void init(String startText) {
        this.current = startText;
        this.setText(this.current);
        this.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(FormattedEditText.this.current)) {
                    FormattedEditText.this.removeTextChangedListener(this);
                    try {
                        String str = FormattedEditText.this.format.format(FormattedEditText.this.getCleanIntValue() / 100.0);
                        FormattedEditText.this.current = str;
                        FormattedEditText.this.setText(str);
                        FormattedEditText.this.setSelection(str.length());
                    }
                    catch (NumberFormatException ignored) {
                        String current = FormattedEditText.this.current;
                        FormattedEditText.this.setText(current);
                        FormattedEditText.this.setSelection(0);
                    }
                    FormattedEditText.this.addTextChangedListener(this);
                }
            }
        });
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        super.setSelection(Utils.getText(this).length());
    }

    @Override
    public void setSelection(int index) {
        super.setSelection(Utils.getText(this).length());
    }
}
