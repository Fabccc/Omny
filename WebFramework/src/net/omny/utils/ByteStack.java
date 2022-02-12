package net.omny.utils;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * Optimized byte stack datastructure backed by a primitive byte array stack
 * and not an array of Byte object
 * 
 * @author Fabien CAYRE
 */
public class ByteStack {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int DEFAULT_GROW_CAPACITY = 16;
    private static final byte[] EMPTY_ELEMENTDATA = {};

    private byte[] array;
    private int size;
    private int modificationCount;

    public ByteStack(int defaultCapacity) {
        this.array = new byte[defaultCapacity];
        this.size = defaultCapacity;
    }

    public ByteStack() {
        this(DEFAULT_CAPACITY);
    }

    public int size() {
        return this.array.length;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void ensureCapacity(int minCapacity) {
        if (minCapacity > array.length
                && !(array == EMPTY_ELEMENTDATA
                        && minCapacity <= DEFAULT_CAPACITY)) {
            modificationCount++;
            grow(minCapacity);
        }
    }

    private byte[] grow(int minCapacity) {
        int oldCapacity = array.length;
        if (oldCapacity > 0 || array != EMPTY_ELEMENTDATA) {
            int newCapacity = ArraySupportOmny.newLength(oldCapacity,
                    minCapacity - oldCapacity, /* minimum growth */
                    oldCapacity >> 1 /* preferred growth */);
            return array = Arrays.copyOf(array, newCapacity);
        } else {
            return array = new byte[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    private byte[] grow() {
        return grow(size + 16);
    }

    public void trimToSize() {
        modificationCount++;
        if (size < this.array.length) {
            this.array = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(this.array, size);
        }
    }

    /**
     * This helper method split out from add(E) to keep method
     * bytecode size under 35 (the -XX:MaxInlineSize default value),
     * which helps when add(E) is called in a C1-compiled loop.
     */
    private void add(byte e, byte[] elementData, int s) {
        if (s == elementData.length)
            elementData = grow();
        elementData[s] = e;
        size = s + 1;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
     */
    public boolean add(byte e) {
        modificationCount++;
        add(e, array, size);
        return true;
    }

    public Iterator<Byte> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 
     * @return A copy of the backed byte array
     */
    public byte[] toArray() {
        return Arrays.copyOf(this.array, size);
    }

    public boolean push(byte e) {
        return add(e);
    }

    public boolean containsAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean addAll(Collection<? extends Byte> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean addAll(int index, Collection<? extends Byte> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        // TODO Auto-generated method stub
        return false;
    }

    public void clear() {
        modificationCount++;
        this.array = new byte[DEFAULT_CAPACITY];
    }

    public byte get(int index) {
        return this.array[index];
    }

    public void set(int index, byte element) {
        this.array[index] = element;
    }

    public void add(int index, Byte element) {
        // TODO Auto-generated method stub

    }

    public Byte remove(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public int indexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public int lastIndexOf(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

    public ListIterator<Byte> listIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public ListIterator<Byte> listIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Byte> subList(int fromIndex, int toIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    private class ByteStackIterator implements ListIterator<Byte> {

        int cursor;
        int expectedModCount = ByteStack.this.modificationCount;
        int lastRet = -1;

        public ByteStackIterator(int index) {
            this.cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != ByteStack.this.size;
        }

        @Override
        public Byte next() {
            checkForComodification();
            int localCursor = cursor;
            if (localCursor >= size) {
                throw new NoSuchElementException();
            }
            byte[] elementData = ByteStack.this.array;
            if (localCursor >= elementData.length)
                throw new ConcurrentModificationException();
            this.cursor = localCursor + 1;
            return elementData[lastRet = localCursor];
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public Byte previous() {
            checkForComodification();
            int localCursor = cursor - 1;
            if (localCursor < 0)
                throw new NoSuchElementException();
            byte[] elementData = ByteStack.this.array;
            if (localCursor >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = localCursor;
            return elementData[lastRet = localCursor];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ByteStack.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = ByteStack.this.modificationCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void set(Byte e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ByteStack.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(Byte e) {
            checkForComodification();

            try {
                int i = cursor;
                ByteStack.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = ByteStack.this.modificationCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (ByteStack.this.modificationCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

    }

}
