package tgw.groceryprices.collections;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.obj.Record_;

public class RecordList extends NameAndIdList<Record_> {

    @Override
    protected String filename() {
        return "records";
    }

    @Nullable
    public Record_ getBest() {
        OArrayList<Record_> list = this.listById;
        Record_ best = null;
        for (int i = 0, len = list.size(); i < len; i++) {
            Record_ record = list.get(i);
            if (record.isBetterThan(best)) {
                best = record;
            }
        }
        return best;
    }

    @Override
    public void load(FileInputStream stream) throws IOException {
        super.load(stream);
    }

    @Override
    protected Record_ loadItem(FileInputStream stream) throws IOException {
        return Record_.load(stream);
    }

    @Override
    public void save(FileOutputStream stream) throws IOException {
        super.save(stream);
    }
}
