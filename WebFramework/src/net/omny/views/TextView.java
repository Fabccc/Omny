package net.omny.views;

import java.nio.ByteBuffer;
import java.util.Objects;

import net.omny.utils.Ex;

public class TextView implements View{

	private byte[] textBytes;
	
	public TextView(String text) {
		Objects.requireNonNull(text);
		this.textBytes = text.getBytes();
	}
	
	@Override
	public void write(ByteBuffer buffer) {
		Ex.grab(() -> {
			buffer.put(textBytes);
			buffer.flip();
		});
	}

}
