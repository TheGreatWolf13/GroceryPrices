package tgw.groceryprices;

import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import tgw.groceryprices.android.Watcher;
import tgw.groceryprices.utils.Validator;

public class ActivityAddMarket extends GenericActivity {

    private static Validator checkValidMarketName(String s) {
        if (s.isEmpty()) {
            return Validator.INVALID_NAME;
        }
        if (MainActivity.MARKET_LIST.contains(s)) {
            return Validator.DUPLICATE_MARKET;
        }
        return Validator.OK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_add_market);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnAdd = this.findViewById(R.id.btnAdd);
        EditText txtName = this.findViewById(R.id.txtName);
        TextView tvError = this.findViewById(R.id.tvBest);
        txtName.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String str = s.subSequence(0, s.length()).toString();
                Validator check = checkValidMarketName(str.trim());
                btnAdd.setEnabled(check.valid);
                tvError.setText(check.error);
            }
        });
        btnAdd.setOnClickListener(v -> {
            Editable text = txtName.getText();
            MainActivity.MARKET_LIST.add(text.subSequence(0, text.length()).toString().trim());
            MainActivity.MARKET_LIST.save(this);
            this.finish();
        });
    }
}