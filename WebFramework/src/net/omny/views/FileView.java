package net.omny.views;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.file.Files;

import net.omny.utils.Ex;

public class FileView implements View {

	private String filePath;

	public FileView(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public void write(ByteBuffer buffer) {
		Ex.grab(() -> {
			byte[] fileBytes = Files.readAllBytes(new File(this.filePath).toPath());
			buffer.put(fileBytes);
			buffer.flip();
		});
	}

}
