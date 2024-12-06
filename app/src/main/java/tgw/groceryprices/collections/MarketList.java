package tgw.groceryprices.collections;

import java.io.FileInputStream;
import java.io.IOException;

import tgw.groceryprices.obj.Market;

public class MarketList extends NameAndIdList<Market> {

    public void add(String name) {
        this.add(new Market(++this.id, name));
    }

    @Override
    protected String filename() {
        return "markets";
    }

    @Override
    protected Market loadItem(FileInputStream stream) throws IOException {
        return Market.load(stream);
    }
}
