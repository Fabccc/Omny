package net.omny.views;

import java.nio.CharBuffer;
import java.util.Objects;

import net.omny.utils.Ex;

public class TextView implements View{

	private char[] textBytes;
	
	public TextView(String text) {
		Objects.requireNonNull(text);
		this.textBytes = text.toCharArray();
	}
	
	@Override
	public void write(CharBuffer buffer) {
		Ex.grab(() -> {
			buffer.put(textBytes);
			buffer.flip();
		});
	}

}
