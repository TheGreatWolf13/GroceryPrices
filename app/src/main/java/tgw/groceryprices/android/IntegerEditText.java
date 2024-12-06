package tgw.groceryprices.android;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import tgw.groceryprices.utils.Utils;

public class IntegerEditText extends AppCompatEditText {

    private String current = "";

    public IntegerEditText(Context context) {
        super(context);
        this.init();
    }

    public IntegerEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public IntegerEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public int getCleanIntValue() throws NumberFormatException {
        String cleanString = this.getCleanString();
        if (cleanString.isEmpty()) {
            return 0;
        }
        return Math.min(Integer.parseUnsignedInt(cleanString), (1 << 8) - 1);
    }

    protected String getCleanString() {
        String text = Utils.getText(this);
        return Utils.NOT_DIGIT.matcher(text).replaceAll("");
    }

    private void init() {
        this.current = "0";
        this.setText(this.current);
        this.addTextChangedListener(new Watcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(IntegerEditText.this.current)) {
                    IntegerEditText.this.removeTextChangedListener(this);
                    try {
                        String str = Utils.FORMAT_INTEGER.format(IntegerEditText.this.getCleanIntValue());
                        IntegerEditText.this.current = str;
                        IntegerEditText.this.setText(str);
                        IntegerEditText.this.setSelection(str.length());
                    }
                    catch (NumberFormatException ignored) {
                        String current = IntegerEditText.this.current;
                        IntegerEditText.this.setText(current);
                        IntegerEditText.this.setSelection(0);
                    }
                    IntegerEditText.this.addTextChangedListener(this);
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