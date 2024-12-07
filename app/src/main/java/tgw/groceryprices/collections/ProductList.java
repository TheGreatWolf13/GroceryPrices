package tgw.groceryprices.collections;

import java.io.FileInputStream;
import java.io.IOException;

import tgw.groceryprices.obj.Market;
import tgw.groceryprices.obj.ProductType;
import tgw.groceryprices.utils.Unit;

public class ProductList extends NameAndIdList<ProductType> {


    public void add(String name, Unit unit) {
        this.add(new ProductType(++this.id, name, unit));
    }

    @Override
    protected String filename() {
        return "products";
    }

    @Override
    protected ProductType loadItem(FileInputStream stream) throws IOException {
        return ProductType.load(stream);
    }

    public void removeRecords(Market market) {
        OArrayList<ProductType> list = this.listById;
        for (int i = 0, len = list.size; i < len; i++) {
            list.get(i).recordList.remove(market.id);
        }
    }
}
