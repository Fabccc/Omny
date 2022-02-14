package net.omny.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.omny.utils.ByteStack;

public class ByteStackTest {

    @Test
    public void testPushBytes() {
        ByteStack byteStack = new ByteStack();
        byteStack.push((byte) 1);

        assertEquals((byte) 1, byteStack.getNative(0));
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
        byte[] arr = new byte[] { 48, 52, 67, (byte) 120 };
        byteStack.push((byte) 1);
        byteStack.push(arr);

        assertEquals((byte) 1, byteStack.getNative(0));
        assertEquals((byte) 48, byteStack.getNative(1));
        assertEquals((byte) 52, byteStack.getNative(2));
        assertEquals((byte) 67, byteStack.getNative(3));
        assertEquals((byte) 120, byteStack.getNative(4));
    }

}
