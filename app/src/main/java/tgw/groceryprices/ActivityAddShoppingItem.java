package tgw.groceryprices;

import android.os.Bundle;
import android.text.Editable;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import tgw.groceryprices.android.CurrencyEditText;
import tgw.groceryprices.android.DecimalEditText;
import tgw.groceryprices.android.IntegerEditText;
import tgw.groceryprices.android.Watcher;
import tgw.groceryprices.utils.Unit;
import tgw.groceryprices.utils.Utils;
import tgw.groceryprices.utils.Validator;

public class ActivityAddShoppingItem extends GenericActivity {

    private static Validator checkValidItem(String name, IntegerEditText amount, DecimalEditText quantity, CurrencyEditText price) {
        if (name.isEmpty()) {
            return Validator.INVALID_NAME;
        }
        try {
            if (amount.getCleanIntValue() == 0) {
                return Validator.INVALID_AMOUNT;
            }
        }
        catch (NumberFormatException e) {
            return Validator.INVALID_AMOUNT;
        }
        try {
            if (quantity.getCleanIntValue() == 0) {
                return Validator.INVALID_QUANTITY;
            }
        }
        catch (NumberFormatException e) {
            return Validator.INVALID_QUANTITY;
        }
        try {
            if (price.getCleanIntValue() == 0) {
                return Validator.INVALID_PRICE;
            }
        }
        catch (NumberFormatException e) {
            return Validator.INVALID_PRICE;
        }
        return Validator.OK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_add_shopping_item);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner spnUnit = this.findViewById(R.id.spnUnit);
        spnUnit.setAdapter(new ArrayAdapter<>(this, R.layout.option_item, Unit.VALUE_NAMES));
        EditText txtName = this.findViewById(R.id.txtName);
        Button btnAdd = this.findViewById(R.id.btnAdd);
        TextView tvError = this.findViewById(R.id.tvBest);
        IntegerEditText txtAmount = this.findViewById(R.id.txtAmount);
        DecimalEditText txtQuantity = this.findViewById(R.id.txtQuantity);
        CurrencyEditText txtPrice = this.findViewById(R.id.txtPrice);
        EditText txtObs = this.findViewById(R.id.txtObs);
        txtName.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = getText(s);
                Validator check = checkValidItem(str, txtAmount, txtQuantity, txtPrice);
                btnAdd.setEnabled(check.valid);
                tvError.setText(check.error);
            }
        });
        txtAmount.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Validator check = checkValidItem(Utils.getText(txtName), txtAmount, txtQuantity, txtPrice);
                btnAdd.setEnabled(check.valid);
                tvError.setText(check.error);
            }
        });
        txtQuantity.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Validator check = checkValidItem(Utils.getText(txtName), txtAmount, txtQuantity, txtPrice);
                btnAdd.setEnabled(check.valid);
                tvError.setText(check.error);
            }
        });
        txtPrice.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Validator check = checkValidItem(Utils.getText(txtName), txtAmount, txtQuantity, txtPrice);
                btnAdd.setEnabled(check.valid);
                tvError.setText(check.error);
            }
        });
        btnAdd.setOnClickListener(v -> {
            MainActivity.SHOPPING_LIST.add(Utils.getText(txtName), txtAmount.getCleanIntValue(), txtQuantity.getCleanIntValue(), Unit.find(spnUnit.getItemAtPosition(spnUnit.getSelectedItemPosition()).toString()), txtPrice.getCleanIntValue(), Utils.getText(txtObs));
            MainActivity.SHOPPING_LIST.save(ActivityAddShoppingItem.this);
            this.finish();
        });
    }
}