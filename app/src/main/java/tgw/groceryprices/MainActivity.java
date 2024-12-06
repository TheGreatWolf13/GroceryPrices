package tgw.groceryprices;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import tgw.groceryprices.collections.MarketList;
import tgw.groceryprices.collections.ProductList;
import tgw.groceryprices.collections.ShoppingList;

public class MainActivity extends GenericActivity {

    public static final MarketList MARKET_LIST = new MarketList();
    public static final ProductList PRODUCT_LIST = new ProductList();
    public static final ShoppingList SHOPPING_LIST = new ShoppingList();
    public static int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MARKET_LIST.load(this);
        PRODUCT_LIST.load(this);
        SHOPPING_LIST.load(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        this.setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(this.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.findViewById(R.id.btnMarkets).setOnClickListener(v -> this.setScreen(ActivityMarkets.class));
        this.findViewById(R.id.btnProductTypes).setOnClickListener(v -> this.setScreen(ActivityProductTypes.class));
        this.findViewById(R.id.btnShoppingList).setOnClickListener(v -> this.setScreen(ActivityShoppingList.class));
        this.findViewById(R.id.btnRecords).setOnClickListener(v -> this.setScreen(ActivityRecords.class));
    }
}