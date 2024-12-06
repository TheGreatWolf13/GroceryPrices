package tgw.groceryprices.collections;

import java.io.FileInputStream;
import java.io.IOException;

import tgw.groceryprices.obj.ShoppingItem;
import tgw.groceryprices.utils.Unit;

public class ShoppingList extends NameAndIdList<ShoppingItem> {

    public void add(String name, int amount, int quantity, Unit unit, int price, String obs) {
        this.add(new ShoppingItem(++this.id, name, amount, quantity, unit, price, obs));
    }

    @Override
    protected String filename() {
        return "shopping";
    }

    @Override
    protected ShoppingItem loadItem(FileInputStream stream) throws IOException {
        return ShoppingItem.load(stream);
    }
}
