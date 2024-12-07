package tgw.groceryprices;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import tgw.groceryprices.obj.Market;
import tgw.groceryprices.obj.Observation;
import tgw.groceryprices.obj.ProductType;
import tgw.groceryprices.obj.Record_;
import tgw.groceryprices.utils.Utils;

public class ActivitySeeObservation extends GenericActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_see_observation);
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
        Market market = MainActivity.MARKET_LIST.getById(MainActivity.selectedMarketId);
        if (market == null) {
            this.finish();
            return;
        }
        Record_ record = product.recordList.getById(MainActivity.selectedMarketId);
        if (record == null) {
            this.finish();
            return;
        }
        this.<TextView>findViewById(R.id.tvTitle).setText(product.name);
        this.<TextView>findViewById(R.id.tvMarket).setText(market.name);
        Observation obsBest = record.bestObservation;
        this.<TextView>findViewById(R.id.tvAmountBest).setText(Utils.FORMAT_INTEGER.format(obsBest.amount));
        this.<TextView>findViewById(R.id.tvQtdBest).setText(Utils.FORMAT_DECIMAL.format(obsBest.quantity / 100.0));
        this.<TextView>findViewById(R.id.tvUnitBest).setText(product.unit.name);
        this.<TextView>findViewById(R.id.tvPriceBest).setText(Utils.FORMAT_CURRENCY.format(obsBest.price / 100.0));
        this.<TextView>findViewById(R.id.tvDateBest).setText(Utils.getDate(obsBest.date));
        this.<TextView>findViewById(R.id.tvPricePUBest).setText(Utils.FORMAT_CURRENCY_PRECISE.format(obsBest.pricePerUnit) + " / " + product.unit.name);
        this.<ImageButton>findViewById(R.id.btnDelBest).setOnClickListener(v -> new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dlg_default))
                .setMessage("Deseja realmente apagar a melhor observação?")
                .setPositiveButton("Sim", (di, w) -> {
                    if (record.latestObservation == null) {
                        product.recordList.remove(record.id());
                    }
                    else {
                        record.bestObservation = record.latestObservation;
                    }
                    MainActivity.PRODUCT_LIST.save(this);
                    this.finish();
                })
                .setNegativeButton("Não", (di, w) -> {})
                .create()
                .show()
        );
        Observation obsLast = record.latestObservation;
        if (obsLast == null) {
            this.<TextView>findViewById(R.id.tvObsBest).setText("Melhor e Última");
            this.findViewById(R.id.tvObsLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.btnDelLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvAmountLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvXLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvQtdLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvUnitLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvPriceLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvDateLast).setVisibility(View.INVISIBLE);
            this.findViewById(R.id.tvPricePULast).setVisibility(View.INVISIBLE);
        }
        else {
            this.<TextView>findViewById(R.id.tvObsBest).setText("Melhor");
            this.findViewById(R.id.tvObsLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.btnDelLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvAmountLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvXLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvQtdLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvUnitLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvPriceLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvDateLast).setVisibility(View.VISIBLE);
            this.findViewById(R.id.tvPricePULast).setVisibility(View.VISIBLE);
            this.<TextView>findViewById(R.id.tvAmountLast).setText(Utils.FORMAT_INTEGER.format(obsLast.amount));
            this.<TextView>findViewById(R.id.tvQtdLast).setText(Utils.FORMAT_DECIMAL.format(obsLast.quantity / 100.0));
            this.<TextView>findViewById(R.id.tvUnitLast).setText(product.unit.name);
            this.<TextView>findViewById(R.id.tvPriceLast).setText(Utils.FORMAT_CURRENCY.format(obsLast.price / 100.0));
            this.<TextView>findViewById(R.id.tvDateLast).setText(Utils.getDate(obsLast.date));
            this.<TextView>findViewById(R.id.tvPricePULast).setText(Utils.FORMAT_CURRENCY_PRECISE.format(obsLast.pricePerUnit) + " / " + product.unit.name);
            this.<ImageButton>findViewById(R.id.btnDelLast).setOnClickListener(v -> new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.dlg_default))
                    .setMessage("Deseja realmente apagar a última observação?")
                    .setPositiveButton("Sim", (di, w) -> {
                        record.latestObservation = null;
                        MainActivity.PRODUCT_LIST.save(this);
                        this.finish();
                    })
                    .setNegativeButton("Não", (di, w) -> {})
                    .create()
                    .show()
            );
        }
    }
}