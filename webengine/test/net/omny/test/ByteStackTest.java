package net.omny.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.omny.utils.ByteStack;

public class ByteStackTest {

    @Test
    public void testPushByte() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);

        assertEquals((byte) 1, byteStack.getNative(0));
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testContains(){
        ByteStack byteStack = new ByteStack();
        byteStack.contains(null);
    }

    @Test
    public void testSetByte() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);

        assertEquals((byte) 1, byteStack.getNative(0));

        byteStack.set(0, (byte) 2);

        assertEquals((byte) 2, byteStack.getNative(0));
    }

    @Test
    public void testClear() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);

        assertEquals((byte) 1, byteStack.getNative(0));
        byteStack.clear();
        assertEquals(0, byteStack.size());
    }

    @Test
    public void testIterable() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);
        byteStack.push((byte) 2);
        byteStack.push((byte) 3);

        int index = 0;
        for(byte b : byteStack){
            assertEquals(index + 1, b);
            index++;
        }
    }

    @Test
    public void testListIterator() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);
        byteStack.push((byte) 2);
        byteStack.push((byte) 3);
        
        var listIterator = byteStack.listIterator();
        int index = 0;
        while (listIterator.hasNext()) {
            byte b = listIterator.next();
            assertEquals(index + 1, b);
            listIterator.remove();
        }
    }

    @Test
    public void testListIteratorIndex() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);
        byteStack.push((byte) 2);
        byteStack.push((byte) 3);
        
        var listIterator = byteStack.listIterator(1);
        int index = 1;
        while (listIterator.hasNext()) {
            byte b = listIterator.next();
            assertEquals(index + 1, b);
            listIterator.remove();
        }
    }

    @Test
    public void testPushBytesArraySlow() {
        ByteStack byteStack = new ByteStack();
        byte[] arr = new byte[] { 48, 52, 67, (byte) 120 };
        byteStack.push((byte) 1);
        byteStack.addAllBytesSlow(arr);

        assertEquals((byte) 1, byteStack.getNative(0));
        assertEquals((byte) 48, byteStack.getNative(1));
        assertEquals((byte) 52, byteStack.getNative(2));
        assertEquals((byte) 67, byteStack.getNative(3));
        assertEquals((byte) 120, byteStack.getNative(4));
    }

    @Test
    public void testPushBytesArray() {
        ByteStack byteStack = new ByteStack();
        byte[] arr = new byte[] { 48, 52, 67, 120 };
        byteStack.push((byte) 1);
        byteStack.push(arr);

        assertEquals((byte) 1, byteStack.getNative(0));
        assertEquals((byte) 48, byteStack.getNative(1));
        assertEquals((byte) 52, byteStack.getNative(2));
        assertEquals((byte) 67, byteStack.getNative(3));
        assertEquals((byte) 120, byteStack.getNative(4));
    }

}
