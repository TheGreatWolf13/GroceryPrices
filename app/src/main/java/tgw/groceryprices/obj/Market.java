package tgw.groceryprices.obj;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.collections.NameAndIdList;
import tgw.groceryprices.utils.SaveUtils;

public class Market implements NameAndIdList.Item {

    public final int id;
    public final String name;

    public Market(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public static Market load(FileInputStream stream) throws IOException {
        int id = SaveUtils.readInt(stream);
        String name = SaveUtils.readString(stream);
        return new Market(id, name);
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
    }
}
