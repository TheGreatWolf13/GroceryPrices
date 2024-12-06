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

import tgw.groceryprices.android.Watcher;
import tgw.groceryprices.utils.Unit;
import tgw.groceryprices.utils.Utils;
import tgw.groceryprices.utils.Validator;

public class ActivityAddProductType extends GenericActivity {

    private static Validator checkValidProductName(String s) {
        if (s.isEmpty()) {
            return Validator.INVALID_NAME;
        }
        if (MainActivity.PRODUCT_LIST.contains(s)) {
            return Validator.DUPLICATE_PRODUCT;
        }
        return Validator.OK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_add_product_type);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner spnUnit = this.findViewById(R.id.spnUnit);
        spnUnit.setAdapter(new ArrayAdapter<>(this, R.layout.option_item, Unit.VALUE_NAMES));
        Button btnAdd = this.findViewById(R.id.btnAdd);
        EditText txtName = this.findViewById(R.id.txtName);
        TextView tvError = this.findViewById(R.id.tvBest);
        txtName.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.subSequence(0, s.length()).toString();
                Validator check = checkValidProductName(str.trim());
                btnAdd.setEnabled(check.valid);
                tvError.setText(check.error);
            }
        });
        btnAdd.setOnClickListener(v -> {
            MainActivity.PRODUCT_LIST.add(Utils.getText(txtName), Unit.find(spnUnit.getItemAtPosition(spnUnit.getSelectedItemPosition()).toString()));
            MainActivity.PRODUCT_LIST.save(this);
            this.finish();
        });
    }
}