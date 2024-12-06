package tgw.groceryprices.collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class OArrayList<K> extends AbstractCollection<K> implements List<K> {

    private static final Object[] DEFAULT_EMPTY_ARRAY = new Object[0];
    protected K[] a;
    protected int size;

    public OArrayList() {
        this.a = (K[]) DEFAULT_EMPTY_ARRAY;
    }

    public static void ensureIndex(int index, int size) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        }
        if (index > size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + size + ")");
        }
    }

    public static void ensureOffsetLength(int arrayLength, int offset, int length) {
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length (" + length + ") is negative");
        }
        if (offset + length > arrayLength) {
            throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than array length (" + arrayLength + ")");
        }
    }

    @Override
    public boolean add(K k) {
        this.add(this.size(), k);
        return true;
    }

    @Override
    public void add(int index, K k) {
        ensureIndex(index, this.size);
        this.grow(this.size + 1);
        if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
        }
        this.a[index] = k;
        ++this.size;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends K> c) {
        if (c instanceof OArrayList) {
            return this.addAll(index, (OArrayList) c);
        }
        ensureIndex(index, this.size);
        int n = c.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        Iterator<? extends K> i = c.iterator();
        this.size += n;
        while (n-- != 0) {
            this.a[index++] = i.next();
        }
        return true;
    }

    public boolean addAll(int index, OArrayList<? extends K> l) {
        ensureIndex(index, this.size);
        int n = l.size();
        if (n == 0) {
            return false;
        }
        this.grow(this.size + n);
        System.arraycopy(this.a, index, this.a, index + n, this.size - index);
        l.getElements(0, this.a, index, n);
        this.size += n;
        return true;
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends K> c) {
        return this.addAll(this.size(), c);
    }

    @Override
    public void clear() {
        Arrays.fill(this.a, 0, this.size, null);
        this.size = 0;
    }

    @Override
    public K get(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return this.a[index];
    }

    public void getElements(int from, Object[] a, int offset, int length) {
        ensureOffsetLength(a.length, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    private void grow(int capacity) {
        if (capacity > this.a.length) {
            //noinspection ArrayEquality
            if (this.a != DEFAULT_EMPTY_ARRAY) {
                capacity = (int) Math.max(Math.min((long) this.a.length + (this.a.length >> 1), Integer.MAX_VALUE - 8L), capacity);
            }
            else if (capacity < 10) {
                capacity = 10;
            }
            Object[] t = new Object[capacity];
            System.arraycopy(this.a, 0, t, 0, this.size);
            this.a = (K[]) t;
        }
    }

    @Override
    public int indexOf(@Nullable Object k) {
        for (int i = 0; i < this.size; ++i) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(@Nullable Object k) {
        for (int i = this.size - 1; i >= 0; --i) {
            if (Objects.equals(k, this.a[i])) {
                return i;
            }
        }
        return -1;
    }

    @NonNull
    @Override
    public ListIterator<K> listIterator() {
        throw new UnsupportedOperationException();
    }

    @NonNull
    @Override
    public ListIterator<K> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public K remove(int index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        K old = this.a[index];
        --this.size;
        if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
        }
        this.a[this.size] = null;
        return old;
    }

    @Override
    public K set(int index, K k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        K old = this.a[index];
        this.a[index] = k;
        return old;
    }

    @Override
    public int size() {
        return this.size;
    }

    @NonNull
    @Override
    public OArrayList<K> subList(int from, int to) {
        throw new UnsupportedOperationException();
    }
}
