package tgw.groceryprices.collections;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import tgw.groceryprices.android.CurrencyEditText;
import tgw.groceryprices.android.DecimalEditText;
import tgw.groceryprices.android.IntegerEditText;
import tgw.groceryprices.obj.Market;
import tgw.groceryprices.obj.Observation;
import tgw.groceryprices.obj.Record_;
import tgw.groceryprices.utils.Utils;

public class RecordList extends NameAndIdList<Record_> {

    public void addObservation(Market market, IntegerEditText txtAmount, DecimalEditText txtQuantity, CurrencyEditText txtPrice) {
        Record_ record = this.getById(market.id);
        int currentDate = Utils.getCurrentDate();
        Observation observation = new Observation(currentDate, txtAmount.getCleanIntValue(), txtQuantity.getCleanIntValue(), txtPrice.getCleanIntValue());
        if (record == null) {
            this.add(new Record_(market, observation));
        }
        else {
            if (observation.pricePerUnit < record.bestObservation.pricePerUnit || observation.pricePerUnit == record.bestObservation.pricePerUnit && observation.date > record.bestObservation.date) {
                record.bestObservation = observation;
                record.latestObservation = null;
            }
            else {
                if (observation.date > record.bestObservation.date && (record.latestObservation == null || observation.date > record.latestObservation.date)) {
                    record.latestObservation = observation;
                }
            }
        }
    }

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
