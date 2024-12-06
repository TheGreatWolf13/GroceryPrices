package tgw.groceryprices.obj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.collections.NameAndIdList;
import tgw.groceryprices.collections.RecordList;
import tgw.groceryprices.utils.SaveUtils;
import tgw.groceryprices.utils.Unit;

public class ProductType implements NameAndIdList.Item {

    public final int id;
    public final String name;
    public final RecordList recordList = new RecordList();
    public final Unit unit;

    public ProductType(int id, String name, Unit unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public static ProductType load(FileInputStream stream) throws IOException {
        int id = SaveUtils.readInt(stream);
        String name = SaveUtils.readString(stream);
        Unit unit = SaveUtils.readEnum(stream, Unit.VALUES);
        ProductType produt = new ProductType(id, name, unit);
        produt.recordList.load(stream);
        return produt;
    }

    @Override
    public int id() {
        return this.id;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void save(FileOutputStream stream) throws IOException {
        SaveUtils.write(stream, this.id);
        SaveUtils.write(stream, this.name);
        SaveUtils.write(stream, this.unit);
        this.recordList.save(stream);
    }
}
