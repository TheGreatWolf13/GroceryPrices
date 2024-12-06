package tgw.groceryprices.collections;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.util.Locale;

import tgw.groceryprices.GenericActivity;
import tgw.groceryprices.utils.SaveUtils;

public abstract class NameAndIdList<T extends NameAndIdList.Item> {

    @Nullable
    private RecyclerView.Adapter adapter;
    protected int id;
    private String lastSearch = "";
    protected final OArrayList<T> listById = new OArrayList<>();
    private final OArrayList<T> listByName = new OArrayList<>();
    private final OArrayList<T> visibleList = new OArrayList<>();

    private static char baseChar(char ch) {
        ch = Character.toLowerCase(ch);
        switch (ch) {
            case 'ä':
            case 'â':
            case 'ã':
            case 'à':
            case 'á': {
                return 'a';
            }
            case 'ë':
            case 'ê':
            case 'è':
            case 'é': {
                return 'e';
            }
            case 'ï':
            case 'î':
            case 'ì':
            case 'í': {
                return 'i';
            }
            case 'ö':
            case 'ô':
            case 'õ':
            case 'ò':
            case 'ó': {
                return 'o';
            }
            case 'ü':
            case 'û':
            case 'ù':
            case 'ú': {
                return 'u';
            }
            case 'ç': {
                return 'c';
            }
            case 'ñ': {
                return 'n';
            }
            case 'ý': {
                return 'y';
            }
        }
        return ch;
    }

    private static int compare(String a, String b, int strength) {
        Collator collator = Collator.getInstance(Locale.ROOT);
        collator.setStrength(strength);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
        return collator.compare(a, b);
    }

    private static int indexOf(CharSequence source, CharSequence target) {
        final int sourceLength = source.length();
        final int targetLength = target.length();
        if (0 == sourceLength) {
            return targetLength == 0 ? sourceLength : -1;
        }
        if (targetLength == 0) {
            return 0;
        }
        char first = baseChar(target.charAt(0));
        int max = sourceLength - targetLength;
        for (int i = 0; i <= max; i++) {
            /* Look for first character. */
            if (baseChar(source.charAt(i)) != first) {
                while (++i <= max) {
                    if (baseChar(source.charAt(i)) == first) {
                        break;
                    }
                }
            }
            /* Found first character, now look at the rest of v2 */
            if (i <= max) {
                int j = i + 1;
                int end = j + targetLength - 1;
                int k = 1;
                while (j < end && baseChar(source.charAt(j)) == baseChar(target.charAt(k))) {
                    ++j;
                    ++k;
                }
                if (j == end) {
                    /* Found whole string. */
                    return i;
                }
            }
        }
        return -1;
    }

    protected void add(T t) {
        String name = t.name();
        OArrayList<T> list = this.listByName;
        int insertAt = list.size();
        for (int i = 0, len = list.size(); i < len; i++) {
            String s = list.get(i).name();
            if (compare(name, s, Collator.IDENTICAL) < 0) {
                insertAt = i;
                break;
            }
        }
        list.add(insertAt, t);
        this.listById.add(t);
    }

    public boolean contains(String name) {
        return this.searchByName(0, this.listByName.size() - 1, name) != -1;
    }

    protected abstract String filename();

    public T get(int index) {
        return this.visibleList.get(index);
    }

    @Nullable
    public T getById(int id) {
        int index = this.searchById(0, this.listById.size - 1, id);
        if (index == -1) {
            return null;
        }
        return this.get(index);
    }

    @Nullable
    public T getByName(String name) {
        int index = this.searchByName(0, this.listByName.size - 1, name);
        if (index == -1) {
            return null;
        }
        return this.get(index);
    }

    public void load(GenericActivity activity) {
        File directory = activity.getApplicationContext().getFilesDir();
        File markets = new File(directory, this.filename());
        if (markets.exists()) {
            try (FileInputStream stream = new FileInputStream(markets)) {
                this.load(stream);
            }
            catch (IOException e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    protected void load(FileInputStream stream) throws IOException {
        this.listById.clear();
        this.listByName.clear();
        this.visibleList.clear();
        this.id = 0;
        int size = SaveUtils.readInt(stream);
        T t = null;
        for (int i = 0; i < size; i++) {
            t = this.loadItem(stream);
            this.listById.add(t);
            int index = this.searchByName(0, this.listByName.size() - 1, t.name());
            if (index == -1) {
                index = this.listByName.size();
            }
            this.listByName.add(index, t);
        }
        if (t != null) {
            this.id = t.id();
        }
    }

    protected abstract T loadItem(FileInputStream stream) throws IOException;

    public void remove(int id) {
        T t = this.listById.remove(this.searchById(0, this.listById.size(), id));
        this.listByName.remove(this.searchByName(0, this.listByName.size(), t.name()));
        this.resetDisplay();
        if (this.listByName.isEmpty()) {
            this.id = 0;
        }
        else if (this.id == t.id()) {
            --this.id;
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetDisplay() {
        this.visibleList.clear();
        this.visibleList.addAll(this.listByName);
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }
    }

    public void save(GenericActivity activity) {
        try {
            File directory = activity.getApplicationContext().getFilesDir();
            File file = new File(directory, this.filename());
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            }
            try (FileOutputStream stream = new FileOutputStream(file)) {
                this.save(stream);
            }
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    protected void save(FileOutputStream stream) throws IOException {
        OArrayList<T> list = this.listById;
        int size = list.size();
        SaveUtils.write(stream, size);
        for (int i = 0; i < size; i++) {
            list.get(i).save(stream);
        }
    }

    private int searchById(int from, int end, int id) {
        OArrayList<T> list = this.listById;
        int delta = end - from + 1;
        if (delta <= 10) {
            for (int i = from; i <= end; i++) {
                if (list.get(i).id() == id) {
                    return i;
                }
            }
            return -1;
        }
        int middle = from + delta / 2;
        int middleId = list.get(middle).id();
        if (middleId == id) {
            //Lucky
            return middle;
        }
        if (middleId < id) {
            return this.searchById(from, middle - 1, id);
        }
        return this.searchById(middle + 1, end, id);
    }

    private int searchByName(int from, int end, String value) {
        OArrayList<T> list = this.listByName;
        int delta = end - from + 1;
        if (delta <= 10) {
            for (int i = from; i <= end; i++) {
                if (list.get(i).name().equals(value)) {
                    return i;
                }
            }
            return -1;
        }
        int middle = from + delta / 2;
        String s = list.get(middle).name();
        if (s.equals(value)) {
            //Lucky
            return middle;
        }
        int compare = compare(s, value, Collator.IDENTICAL);
        if (compare < 0) {
            return this.searchByName(from, middle - 1, value);
        }
        if (compare > 0) {
            return this.searchByName(middle + 1, end, value);
        }
        throw new IllegalStateException("Duplicate string?");
    }

    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public int size() {
        return this.visibleList.size();
    }

    public String[] toArray() {
        int len = this.listByName.size;
        String[] arr = new String[len];
        for (int i = 0; i < len; ++i) {
            arr[i] = this.listByName.get(i).name();
        }
        return arr;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateSearch(String name) {
        if (!this.lastSearch.equals(name)) {
            OArrayList<T> visibleList = this.visibleList;
            if (name.startsWith(this.lastSearch)) {
                for (int i = 0; i < visibleList.size(); i++) {
                    if (indexOf(visibleList.get(i).name(), name) == -1) {
                        visibleList.remove(i--);
                    }
                }
            }
            else {
                visibleList.clear();
                visibleList.addAll(this.listByName);
                if (!name.isEmpty()) {
                    for (int i = 0; i < visibleList.size(); i++) {
                        if (indexOf(visibleList.get(i).name(), name) == -1) {
                            visibleList.remove(i--);
                        }
                    }
                }
            }
            this.lastSearch = name;
            if (this.adapter != null) {
                this.adapter.notifyDataSetChanged();
            }
        }
    }

    public interface Item {

        int id();

        String name();

        void save(FileOutputStream stream) throws IOException;
    }
}
