package tgw.groceryprices.utils;

import android.text.Editable;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.regex.Pattern;

public final class Utils {

    public static final Pattern NOT_DIGIT = Pattern.compile("\\D");

    public static final DecimalFormat FORMAT_CURRENCY;
    public static final DecimalFormat FORMAT_CURRENCY_PRECISE;

    public static final DecimalFormat FORMAT_DECIMAL;
    public static final DecimalFormat FORMAT_INTEGER;
    public static final DecimalFormat FORMAT_INTEGER_2;
    public static final DecimalFormat FORMAT_INTEGER_4;


    static {
        DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance();
        instance.setDecimalSeparator(',');
        instance.setGroupingSeparator('.');
        FORMAT_CURRENCY = new DecimalFormat("R$ ,##0.00", instance);
        FORMAT_CURRENCY_PRECISE = new DecimalFormat("R$ ,##0.0000", instance);
        FORMAT_DECIMAL = new DecimalFormat(",##0.00", instance);
        FORMAT_INTEGER = new DecimalFormat(",##0", instance);
        FORMAT_INTEGER_2 = new DecimalFormat("00", instance);
        FORMAT_INTEGER_4 = new DecimalFormat("0000", instance);
    }

    private Utils() {

    }

    public static int getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + month * 100 + year * 10_000;
    }

    public static String getDate(int date) {
        int year = date / 10_000;
        date -= year * 10_000;
        int month = date / 100;
        date -= month * 100;
        int day = date;
        return FORMAT_INTEGER_2.format(day) + "/" + FORMAT_INTEGER_2.format(month) + "/" + FORMAT_INTEGER_4.format(year);
    }

    public static String getText(EditText txt) {
        Editable text = txt.getText();
        return text.subSequence(0, text.length()).toString().trim();
    }
}
