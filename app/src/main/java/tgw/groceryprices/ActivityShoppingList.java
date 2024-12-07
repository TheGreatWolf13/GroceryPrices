package tgw.groceryprices;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import tgw.groceryprices.android.MarqueeTextView;
import tgw.groceryprices.obj.ShoppingItem;
import tgw.groceryprices.utils.Utils;

public class ActivityShoppingList extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_shopping_list);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView list = this.findViewById(R.id.shoppingList);
        list.setLayoutManager(new StaggeredGridLayoutManager(1, RecyclerView.VERTICAL));
        Adapter adapter = new Adapter(this.getResources().getDisplayMetrics());
        list.setAdapter(adapter);
        MainActivity.SHOPPING_LIST.setAdapter(adapter);
        this.findViewById(R.id.btnAdd).setOnClickListener(v -> this.setScreen(ActivityAddShoppingItem.class));
    }

    @Override
    protected void onDestroy() {
        MainActivity.SHOPPING_LIST.setAdapter(null);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        MainActivity.SHOPPING_LIST.resetDisplay();
        super.onResume();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox compCheckbox;
        public final ImageButton compDelete;
        public final MarqueeTextView compName;
        public final TextView fullAmount;
        public final CheckBox fullCheckbox;
        public final ImageButton fullDelete;
        public final MarqueeTextView fullName;
        public final MarqueeTextView fullObs;
        public final TextView fullPrice;
        public final TextView fullQuantity;
        public final TextView fullUnit;
        public final ViewSwitcher switcher;

        public ViewHolder(View view, ViewGroup parent) {
            super(view);
            this.switcher = view.findViewById(R.id.view);
            View compactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_compact, parent, false);
            this.compName = compactView.findViewById(R.id.tvName);
            this.compName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            this.compDelete = compactView.findViewById(R.id.btnView);
            this.compCheckbox = compactView.findViewById(R.id.ckbDone);
            this.switcher.addView(compactView);
            //
            View fullView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopping_full, parent, false);
            this.switcher.addView(fullView);
            this.fullName = fullView.findViewById(R.id.tvName);
            this.fullCheckbox = fullView.findViewById(R.id.ckbDone);
            this.fullAmount = fullView.findViewById(R.id.tvAmountBest);
            this.fullQuantity = fullView.findViewById(R.id.tvQtd);
            this.fullUnit = fullView.findViewById(R.id.tvUnitBest);
            this.fullPrice = fullView.findViewById(R.id.tvPrice);
            this.fullDelete = fullView.findViewById(R.id.btnView);
            this.fullObs = fullView.findViewById(R.id.tvObs);
        }
    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final DisplayMetrics metrics;

        public Adapter(DisplayMetrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public int getItemCount() {
            return MainActivity.SHOPPING_LIST.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ShoppingItem item = MainActivity.SHOPPING_LIST.get(position);
            holder.compName.setText(item.name);
            holder.compDelete.setOnClickListener(v -> new AlertDialog.Builder(new ContextThemeWrapper(ActivityShoppingList.this, R.style.dlg_default))
                    .setMessage("Quer realmente apagar o item \"" + item.name() + "\"?")
                    .setPositiveButton("Apagar", (di, w) -> {
                        MainActivity.SHOPPING_LIST.remove(item.id());
                        MainActivity.SHOPPING_LIST.save(ActivityShoppingList.this);
                    })
                    .setNegativeButton("Cancelar", (di, w) -> {})
                    .create()
                    .show()
            );
            holder.compCheckbox.setChecked(true);
            holder.compCheckbox.setOnCheckedChangeListener((checkbox, checked) -> {
                if (!checked) {
                    this.setFull(holder);
                    item.toggleChecked();
                    holder.fullName.marquee();
                    holder.fullObs.marquee();
                    holder.compCheckbox.setChecked(true);
                }
            });
            //
            holder.fullName.setText(item.name);
            holder.fullCheckbox.setOnCheckedChangeListener((checkbox, checked) -> {
                if (checked) {
                    this.setCompact(holder);
                    item.toggleChecked();
                    holder.compName.marquee();
                    holder.fullCheckbox.setChecked(false);
                }
            });
            holder.fullAmount.setText(Utils.FORMAT_INTEGER.format(item.amount));
            holder.fullQuantity.setText(Utils.FORMAT_DECIMAL.format(item.quantity / 100.0));
            holder.fullUnit.setText(item.unit.name);
            holder.fullPrice.setText(Utils.FORMAT_CURRENCY.format(item.price / 100.0));
            holder.fullDelete.setOnClickListener(v -> new AlertDialog.Builder(new ContextThemeWrapper(ActivityShoppingList.this, R.style.dlg_default))
                    .setMessage("Quer realmente apagar o item \"" + item.name() + "\"?")
                    .setPositiveButton("Apagar", (di, w) -> {
                        MainActivity.SHOPPING_LIST.remove(item.id());
                        MainActivity.SHOPPING_LIST.save(ActivityShoppingList.this);
                    })
                    .setNegativeButton("Cancelar", (di, w) -> {})
                    .create()
                    .show()
            );
            holder.fullObs.setText(item.observation);
            if (item.isChecked()) {
                this.setCompact(holder);
            }
            else {
                this.setFull(holder);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_shopping, parent, false);
            return new ViewHolder(view, parent);
        }

        private void setCompact(ViewHolder holder) {
            holder.switcher.setDisplayedChild(0);
            holder.itemView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, this.metrics);
        }

        private void setFull(ViewHolder holder) {
            holder.switcher.setDisplayedChild(1);
            holder.itemView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, this.metrics);
        }
    }
}