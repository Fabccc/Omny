package net.omny.utils;

import java.util.ArrayList;
import java.util.List;

public class Primitive {

	public static final List<Byte> toList(byte[] bytes){
		List<Byte> byteList = new ArrayList<>(bytes.length);
		for(int i = 0; i < bytes.length; i++)
			byteList.add(bytes[i]);
		return byteList;
	}

	public static final byte[] toArray(List<Byte> bytes) {
		byte[] byteArray = new byte[bytes.size()];
		for(int i = 0; i < byteArray.length; i++)
			byteArray[i] = bytes.get(i);
		return byteArray;
	}
	
}
