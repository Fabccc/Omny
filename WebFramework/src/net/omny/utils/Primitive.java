package net.omny.utils;

import java.util.List;

public class Primitive {

	public static final byte[] toArray(List<Byte> bytes) {
		byte[] byteArray = new byte[bytes.size()];
		for(int i = 0; i < byteArray.length; i++)
			byteArray[i] = bytes.get(i);
		return byteArray;
	}
	
}
