package tgw.groceryprices;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import tgw.groceryprices.collections.RecordList;
import tgw.groceryprices.obj.Market;
import tgw.groceryprices.obj.ProductType;
import tgw.groceryprices.obj.Record_;
import tgw.groceryprices.utils.Utils;
import tgw.groceryprices.utils.Validator;

public class ActivityAddRecord extends GenericActivity {

    private Button btnAdd;
    private Market market;
    private ProductType product;
    private TextView tvBest;
    private TextView tvBestPrice;
    private IntegerEditText txtAmount;
    private CurrencyEditText txtPrice;
    private DecimalEditText txtQuantity;

    private static Validator checkValidItem(IntegerEditText amount, DecimalEditText quantity, CurrencyEditText price) {
        if (amount.getCleanIntValue() == 0) {
            return Validator.INVALID_AMOUNT;
        }
        if (quantity.getCleanIntValue() == 0) {
            return Validator.INVALID_QUANTITY;
        }
        if (price.getCleanIntValue() == 0) {
            return Validator.INVALID_PRICE;
        }
        return Validator.OK;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_add_record);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ProductType product = MainActivity.PRODUCT_LIST.getById(MainActivity.selectedProductId);
        if (product == null) {
            this.finish();
            return;
        }
        this.product = product;
        Spinner spnUnit = this.findViewById(R.id.spnMarket);
        String[] array = MainActivity.MARKET_LIST.toArray();
        this.tvBest = this.findViewById(R.id.tvBest);
        this.tvBestPrice = this.findViewById(R.id.tvBestPrice);
        //noinspection DataFlowIssue
        this.market = MainActivity.MARKET_LIST.getByName(array[0]);
        spnUnit.setAdapter(new ArrayAdapter<>(this, R.layout.option_item, array));
        spnUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = array[position];
                //noinspection DataFlowIssue
                ActivityAddRecord.this.market = MainActivity.MARKET_LIST.getByName(str);
                ActivityAddRecord.this.updateEstimate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.txtAmount = this.findViewById(R.id.txtAmount);
        this.txtAmount.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ActivityAddRecord.this.updateEstimate();
            }
        });
        this.txtPrice = this.findViewById(R.id.txtPrice);
        this.txtPrice.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ActivityAddRecord.this.updateEstimate();
            }
        });
        this.txtQuantity = this.findViewById(R.id.txtQuantity);
        this.txtQuantity.addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                ActivityAddRecord.this.updateEstimate();
            }
        });
        this.btnAdd = this.findViewById(R.id.btnAdd);
        this.btnAdd.setOnClickListener(v -> {
            product.recordList.addObservation(this.market, this.txtAmount, this.txtQuantity, this.txtPrice);
            MainActivity.PRODUCT_LIST.save(this);
            this.finish();
        });
    }

    private void updateEstimate() {
        double pricePerUnit = (double) this.txtPrice.getCleanIntValue() / this.txtAmount.getCleanIntValue() / this.txtQuantity.getCleanIntValue();
        if (Double.isNaN(pricePerUnit)) {
            pricePerUnit = Double.POSITIVE_INFINITY;
        }
        this.tvBestPrice.setText(Utils.FORMAT_CURRENCY_PRECISE.format(pricePerUnit) + " / " + this.product.unit.name);
        RecordList list = this.product.recordList;
        Record_ best = list.getBest();
        if (best == null || pricePerUnit < best.bestObservation.pricePerUnit) {
            this.tvBest.setText("Novo melhor global!");
        }
        else {
            Record_ record = list.getById(this.market.id);
            if (record == null || pricePerUnit < record.bestObservation.pricePerUnit) {
                this.tvBest.setText("Novo melhor no mercado!");
            }
            else {
                this.tvBest.setText("");
            }
        }
        Validator validator = checkValidItem(this.txtAmount, this.txtQuantity, this.txtPrice);
        if (!validator.valid) {
            this.btnAdd.setEnabled(false);
            this.tvBest.setText(validator.error);
            this.tvBest.setTextColor(this.getResources().getColor(R.color.red));
        }
        else {
            this.btnAdd.setEnabled(true);
            this.tvBest.setTextColor(this.getResources().getColor(R.color.lightGreen));
        }
    }
}