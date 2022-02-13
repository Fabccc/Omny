package net.omny.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * 
 * Optimized byte stack datastructure backed by a primitive byte array stack
 * and not an array of Byte object
 * 
 * @author Fabien CAYRE
 */
public class ByteStack implements List<Byte> {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int DEFAULT_GROW_CAPACITY = 16;
    private static final byte[] EMPTY_ELEMENTDATA = {};

    private byte[] array;
    private int size;
    private int modificationCount;

    public ByteStack() {
        this(DEFAULT_CAPACITY);
    }

    public ByteStack(int defaultCapacity) {
        this.array = new byte[defaultCapacity];
        this.size = defaultCapacity;
    }

    public ByteStack(byte[] backedArray) {
        this.array = backedArray;
        this.size = array.length;
    }

    @Override
    public int size() {
        return this.array.length;
    }

    @Override
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
            int newCapacity = newLength(oldCapacity,
                    minCapacity - oldCapacity, /* minimum growth */
                    oldCapacity >> 1 /* preferred growth */);
            return array = Arrays.copyOf(array, newCapacity);
        } else {
            return array = new byte[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    private byte[] grow() {
        return grow(size + DEFAULT_GROW_CAPACITY);
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
     * A version of rangeCheck used by add and addAll.
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Constructs an IndexOutOfBoundsException detail message.
     * Of the many possible refactorings of the error handling code,
     * this "outlining" performs best with both server and client VMs.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
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
        return new ByteStackIterator();
    }

    /**
     * 
     * @return A copy of the backed byte array
     */
    public byte[] toArrayNative() {
        return Arrays.copyOf(this.array, size);
    }

    public byte[] getBackedArray() {
        return this.array;
    }

    public boolean push(byte e) {
        return add(e);
    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        nonSense();
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        c.forEach(this::add);
        return true;
    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        nonSense();
        return false;
    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        nonSense();
        return false;
    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        nonSense();
        return false;
    }

    @Override
    public void clear() {
        modificationCount++;
        this.array = new byte[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public byte getNative(int index) {
        return this.array[index];
    }

    @SuppressWarnings("unchecked")
    byte at(int index) {
        return array[index];
    }

    public byte set(int index, byte element) {
        Objects.checkIndex(index, size);
        byte old = at(index);
        this.array[index] = element;
        return old;
    }

    public void add(int index, byte element) {
        rangeCheckForAdd(index);
        modificationCount++;
        final int s;
        byte[] elementData;
        if ((s = size) == (elementData = this.array).length)
            elementData = grow();
        System.arraycopy(elementData, index,
                elementData, index + 1,
                s - index);
        elementData[index] = element;
        size = s + 1;

    }

    /**
     * Private remove method that skips bounds checking and does not
     * return the value removed.
     */
    private void fastRemove(byte[] es, int i) {
        modificationCount++;
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = 0;
    }

    public byte pop() {
        return removeNative(size - 1);
    }

    public byte removeNative(int index) {
        Objects.checkIndex(index, size);
        final byte[] es = array;

        byte oldValue = (byte) es[index];
        fastRemove(es, index);

        return oldValue;
    }

    public ListIterator<Byte> listIterator() {
        return listIterator(0);
    }

    public ListIterator<Byte> listIterator(int index) {
        return new ByteStackListIterator(index);
    }

    private class ByteStackIterator implements Iterator<Byte> {

        int cursor;
        int expectedModCount = ByteStack.this.modificationCount;
        int lastRet = -1;

        ByteStackIterator() {
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

        final void checkForComodification() {
            if (ByteStack.this.modificationCount != expectedModCount)
                throw new ConcurrentModificationException();
        }

    }

    private class ByteStackListIterator extends ByteStackIterator implements ListIterator<Byte> {

        public ByteStackListIterator(int index) {
            super();
            this.cursor = index;
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

    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public boolean contains(Object o) {
        nonSense();
        return false;
    }

    @Override
    public Object[] toArray() {
        Byte[] bytes = new Byte[this.size];
        for (int i = 0; i < size; i++) {
            bytes[i] = this.array[i];
        }
        return bytes;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Byte[] elementData = (Byte[]) toArray();
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(Byte e) {
        this.add((byte) e);
        return true;
    }

    /**
     * Pop a byte from the stack
     * 
     * @deprecated Use {@link ByteStack#pop()} instead
     * @return false
     */
    @Override
    public boolean remove(Object o) {
        this.pop();
        return false;
    }

    @Override
    public Byte get(int index) {
        return this.getNative(index);
    }

    @Override
    public Byte set(int index, Byte element) {
        Objects.checkIndex(index, size);
        return set(index, (byte) element);
    }

    @Override
    public void add(int index, Byte element) {
        this.add(index, (byte) element);
    }

    /**
     * Pop byte from the stack and return it
     * 
     * @deprecated use {@link ByteStack#pop()} instead
     * @return The byte at the top of the stack
     */
    @Override
    public Byte remove(int index) {
        return this.pop();
    }

    void nonSense() {
        throw new UnsupportedOperationException("Non-sense in a byte stack data structure");
    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public int indexOf(Object o) {
        nonSense();
        return -1;
    }

    /**
     * @deprecated Non-sense in a byte stack data structure
     */
    @Override
    public int lastIndexOf(Object o) {
        nonSense();
        return -1;
    }

    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static final int SOFT_MAX_ARRAY_LENGTH = Integer.MAX_VALUE - 8;

    private static int newLength(int oldLength, int minGrowth, int prefGrowth) {
        // preconditions not checked because of inlining
        // assert oldLength >= 0
        // assert minGrowth > 0

        int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
        if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
            return prefLength;
        } else {
            // put code cold in a separate method
            return hugeLength(oldLength, minGrowth);
        }
    }

    private static int hugeLength(int oldLength, int minGrowth) {
        int minLength = oldLength + minGrowth;
        if (minLength < 0) { // overflow
            throw new OutOfMemoryError(
                    "Required array length " + oldLength + " + " + minGrowth + " is too large");
        } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
            return SOFT_MAX_ARRAY_LENGTH;
        } else {
            return minLength;
        }
    }

}
