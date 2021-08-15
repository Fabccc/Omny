package net.omny.views;

import java.nio.ByteBuffer;

//TODO find a way to put the content of the "view"
// into the HTTP response
public interface View {

	void write(ByteBuffer buffer);
	
}
