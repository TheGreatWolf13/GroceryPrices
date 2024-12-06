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
import tgw.groceryprices.obj.ProductType;

public class ActivityProductTypes extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_product_types);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.findViewById(R.id.btnAddProductType).setOnClickListener(v -> this.setScreen(ActivityAddProductType.class));
        RecyclerView listProductTypes = this.findViewById(R.id.listProductTypes);
        listProductTypes.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Adapter adapter = new Adapter();
        listProductTypes.setAdapter(adapter);
        MainActivity.PRODUCT_LIST.setAdapter(adapter);
        this.<EditText>findViewById(R.id.txtSearch).addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.PRODUCT_LIST.updateSearch(s.subSequence(0, s.length()).toString().trim());
            }
        });
    }

    @Override
    protected void onDestroy() {
        MainActivity.PRODUCT_LIST.setAdapter(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        EditText txtSearch = this.findViewById(R.id.txtSearch);
        txtSearch.setText("");
        txtSearch.clearFocus();
        MainActivity.PRODUCT_LIST.resetDisplay();
        super.onResume();
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public int getItemCount() {
            return MainActivity.PRODUCT_LIST.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductType product = MainActivity.PRODUCT_LIST.get(position);
            holder.tvName.setText(product.name());
            holder.btn.setOnClickListener(v -> new AlertDialog.Builder(new ContextThemeWrapper(ActivityProductTypes.this, R.style.dlg_default))
                    .setMessage("Quer realmente apagar o tipo de produto \"" + product.name() + "\" e todos os registros associados?")
                    .setPositiveButton("Apagar", (di, w) -> {
                        MainActivity.PRODUCT_LIST.remove(product.id());
                        MainActivity.PRODUCT_LIST.save(ActivityProductTypes.this);
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