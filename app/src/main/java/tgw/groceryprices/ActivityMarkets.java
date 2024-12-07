package tgw.groceryprices;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import tgw.groceryprices.android.ViewHolder;
import tgw.groceryprices.android.Watcher;
import tgw.groceryprices.obj.Market;

public class ActivityMarkets extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_markets);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.findViewById(R.id.btnAddMarket).setOnClickListener(v -> this.setScreen(ActivityAddMarket.class));
        RecyclerView listMarkets = this.findViewById(R.id.listMarkets);
        listMarkets.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Adapter adapter = new Adapter();
        listMarkets.setAdapter(adapter);
        MainActivity.MARKET_LIST.setAdapter(adapter);
        this.<EditText>findViewById(R.id.txtSearch).addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.MARKET_LIST.updateSearch(s.subSequence(0, s.length()).toString().trim());
            }
        });
    }

    @Override
    protected void onDestroy() {
        MainActivity.MARKET_LIST.setAdapter(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        EditText txtSearch = this.findViewById(R.id.txtSearch);
        txtSearch.setText("");
        txtSearch.clearFocus();
        MainActivity.MARKET_LIST.resetDisplay();
        super.onResume();
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public int getItemCount() {
            return MainActivity.MARKET_LIST.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Market market = MainActivity.MARKET_LIST.get(position);
            holder.tvName.setText(market.name());
            holder.btn.setOnClickListener(v -> new AlertDialog.Builder(new ContextThemeWrapper(ActivityMarkets.this, R.style.dlg_default))
                    .setMessage("Quer realmente apagar o mercado \"" + market.name() + "\" e todos os registros associados?")
                    .setPositiveButton("Apagar", (di, w) -> {
                        MainActivity.PRODUCT_LIST.removeRecords(market);
                        MainActivity.PRODUCT_LIST.save(ActivityMarkets.this);
                        MainActivity.MARKET_LIST.remove(market.id());
                        MainActivity.MARKET_LIST.save(ActivityMarkets.this);
                    })
                    .setNegativeButton("Cancelar", (di, w) -> {})
                    .create()
                    .show()
            );
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
            return new ViewHolder(view);
        }
    }
}