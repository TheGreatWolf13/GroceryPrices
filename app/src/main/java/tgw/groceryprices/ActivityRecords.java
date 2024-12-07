package tgw.groceryprices;

import android.os.Bundle;
import android.text.Editable;
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

public class ActivityRecords extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_records);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView list = this.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        Adapter adapter = new Adapter();
        list.setAdapter(adapter);
        MainActivity.PRODUCT_LIST.setAdapter(adapter);
        this.<EditText>findViewById(R.id.txtSearch).addTextChangedListener(new Watcher() {
            @Override
            public void afterTextChanged(Editable s) {
                MainActivity.PRODUCT_LIST.updateSearch(s.subSequence(0, s.length()).toString().trim());
            }
        });
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
            holder.btn.setOnClickListener(v -> {
                MainActivity.selectedProductId = product.id;
                ActivityRecords.this.setScreen(ActivitySeeRecord.class);
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