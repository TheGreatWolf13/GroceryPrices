package tgw.groceryprices.android;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class Watcher implements TextWatcher {

    public static String getText(CharSequence s) {
        return s.subSequence(0, s.length()).toString().trim();
    }

    @Override
    public void afterTextChanged(Editable s) {
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
