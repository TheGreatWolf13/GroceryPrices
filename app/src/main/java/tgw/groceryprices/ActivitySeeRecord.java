package tgw.groceryprices;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tgw.groceryprices.android.ViewHolder;
import tgw.groceryprices.android.Watcher;
import tgw.groceryprices.collections.RecordList;
import tgw.groceryprices.obj.ProductType;
import tgw.groceryprices.obj.Record_;
import tgw.groceryprices.utils.Utils;

public class ActivitySeeRecord extends GenericActivity {

    private ProductType product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_see_record);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int id = MainActivity.selectedProductId;
        if (id == 0) {
            this.finish();
            return;
        }
        ProductType product = MainActivity.PRODUCT_LIST.getById(id);
        if (product == null) {
            this.finish();
            return;
        }
        this.<TextView>findViewById(R.id.tvTitle).setText(product.name);
        this.findViewById(R.id.btnAdd).setOnClickListener(v -> {
            MainActivity.MARKET_LIST.resetDisplay();
            if (MainActivity.MARKET_LIST.size() == 0) {
                new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dlg_default))
                        .setMessage("Não há mercados registrados. Adicione um mercado primeiro.")
                        .setNeutralButton("Ok", (di, w) -> {})
                        .create()
                        .show();
            }
            else {
                this.setScreen(ActivityAddRecord.class);
            }
        });
        RecyclerView list = this.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Adapter adapter = new Adapter(product.recordList);
        list.setAdapter(adapter);
        product.recordList.setAdapter(adapter);
        this.<EditText>findViewById(R.id.txtSearch).addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                product.recordList.updateSearch(s.subSequence(0, s.length()).toString().trim());
            }
        });
        this.product = product;
    }

    @Override
    protected void onResume() {
        EditText txtSearch = this.findViewById(R.id.txtSearch);
        txtSearch.setText("");
        txtSearch.clearFocus();
        this.product.recordList.resetDisplay();
        Record_ bestRecord = this.product.recordList.getBest();
        if (bestRecord == null) {
            this.<TextView>findViewById(R.id.tvBestPrice).setText("");
            this.<TextView>findViewById(R.id.tvDate).setText("");
            this.<TextView>findViewById(R.id.tvMarket).setText("");
            this.<TextView>findViewById(R.id.tvPrice).setText("Sem registros");
        }
        else {
            this.<TextView>findViewById(R.id.tvBestPrice).setText("Preço");
            this.<TextView>findViewById(R.id.tvDate).setText(Utils.getDate(bestRecord.bestObservation.date));
            this.<TextView>findViewById(R.id.tvMarket).setText(bestRecord.market.name);
            this.<TextView>findViewById(R.id.tvPrice).setText(Utils.FORMAT_CURRENCY_PRECISE.format(bestRecord.bestObservation.pricePerUnit) + " / " + this.product.unit.name);
        }
        super.onResume();
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final RecordList list;

        public Adapter(RecordList list) {
            this.list = list;
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Record_ record_ = this.list.get(position);
            holder.tvName.setText(record_.name());
            holder.btn.setOnClickListener(v -> {
                MainActivity.selectedMarketId = record_.id();
                ActivitySeeRecord.this.setScreen(ActivitySeeObservation.class);
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_record, parent, false);
            return new ViewHolder(view);
        }
    }
}