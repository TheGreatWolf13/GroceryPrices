package tgw.groceryprices.obj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.utils.SaveUtils;

public class Observation {

    public final int amount;
    public final int date;
    public final int price;
    public final double pricePerUnit;
    public final int quantity;

    public Observation(int date, int amount, int quantity, int price) {
        this.amount = amount;
        this.date = date;
        this.price = price;
        this.quantity = quantity;
        double pricePerAmount = (double) price / amount;
        this.pricePerUnit = pricePerAmount / quantity;
    }

    public static Observation load(FileInputStream stream) throws IOException {
        int date = SaveUtils.readInt(stream);
        int amount = SaveUtils.read1Byte(stream);
        int quantity = SaveUtils.read3Bytes(stream);
        int price = SaveUtils.read3Bytes(stream);
        return new Observation(date, amount, quantity, price);
    }

    public void save(FileOutputStream stream) throws IOException {
        SaveUtils.write(stream, this.date);
        SaveUtils.write1Byte(stream, this.amount);
        SaveUtils.write3Bytes(stream, this.quantity);
        SaveUtils.write3Bytes(stream, this.price);
    }
}
