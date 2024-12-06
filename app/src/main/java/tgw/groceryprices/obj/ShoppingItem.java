package tgw.groceryprices.obj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.collections.NameAndIdList;
import tgw.groceryprices.utils.SaveUtils;
import tgw.groceryprices.utils.Unit;

public class ShoppingItem implements NameAndIdList.Item {

    public final int amount;
    private boolean checked;
    public final int id;
    public final String name;
    public final String observation;
    public final int price;
    public final int quantity;
    public final Unit unit;

    public ShoppingItem(int id, String name, int amount, int quantity, Unit unit, int price, String observation) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.observation = observation;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
    }

    public static ShoppingItem load(FileInputStream stream) throws IOException {
        int id = SaveUtils.readInt(stream);
        String name = SaveUtils.readString(stream);
        boolean checked = SaveUtils.readBoolean(stream);
        int amount = SaveUtils.read1Byte(stream);
        int quantity = SaveUtils.read3Bytes(stream);
        Unit unit = SaveUtils.readEnum(stream, Unit.VALUES);
        int price = SaveUtils.read3Bytes(stream);
        String observation = SaveUtils.readString(stream);
        ShoppingItem item = new ShoppingItem(id, name, amount, quantity, unit, price, observation);
        item.checked = checked;
        return item;
    }

    @Override
    public int id() {
        return this.id;
    }

    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void save(FileOutputStream stream) throws IOException {
        SaveUtils.write(stream, this.id);
        SaveUtils.write(stream, this.name);
        SaveUtils.write(stream, this.checked);
        SaveUtils.write1Byte(stream, this.amount);
        SaveUtils.write3Bytes(stream, this.quantity);
        SaveUtils.write(stream, this.unit);
        SaveUtils.write3Bytes(stream, this.price);
        SaveUtils.write(stream, this.observation);
    }

    public void toggleChecked() {
        this.checked = !this.checked;
    }
}
