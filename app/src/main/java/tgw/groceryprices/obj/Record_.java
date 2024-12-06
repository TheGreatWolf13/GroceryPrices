package tgw.groceryprices.obj;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.MainActivity;
import tgw.groceryprices.collections.NameAndIdList;
import tgw.groceryprices.utils.SaveUtils;

public class Record_ implements NameAndIdList.Item {

    public Observation bestObservation;
    @Nullable
    public Observation latestObservation;
    public final Market market;

    public Record_(Market market, Observation obs) {
        this.market = market;
        this.bestObservation = obs;
    }

    public static Record_ load(FileInputStream stream) throws IOException {
        int id = SaveUtils.readInt(stream);
        Market market = MainActivity.MARKET_LIST.getById(id);
        if (market == null) {
            throw new IOException("Invalid market");
        }
        byte flag = SaveUtils.readByte(stream);
        Observation best = Observation.load(stream);
        Observation last = null;
        if ((flag & 1) != 0) {
            last = Observation.load(stream);
        }
        Record_ record = new Record_(market, best);
        record.latestObservation = last;
        return record;
    }

    @Override
    public int id() {
        return this.market.id;
    }

    public boolean isBetterThan(@Nullable Record_ other) {
        if (other == null) {
            return true;
        }
        if (this.bestObservation.pricePerUnit < other.bestObservation.pricePerUnit) {
            return true;
        }
        if (this.bestObservation.pricePerUnit == other.bestObservation.pricePerUnit) {
            return this.bestObservation.date > other.bestObservation.date;
        }
        return false;
    }

    @Override
    public String name() {
        return this.market.name;
    }

    /**
     * @noinspection VariableNotUsedInsideIf
     */
    @Override
    public void save(FileOutputStream stream) throws IOException {
        SaveUtils.write(stream, this.market.id);
        byte flag = 0;
        if (this.latestObservation != null) {
            flag = 1;
        }
        SaveUtils.write(stream, flag);
        this.bestObservation.save(stream);
        if (this.latestObservation != null) {
            this.latestObservation.save(stream);
        }
    }
}
