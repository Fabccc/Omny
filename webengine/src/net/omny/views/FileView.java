package net.omny.views;

import java.io.File;
import java.nio.file.Files;

import lombok.Getter;
import net.omny.route.Response;
import net.omny.utils.Ex;

@Getter
public class FileView implements View {

	private File file;

	public FileView(String filePath) {
		this.file = new File(filePath);
	}
	
	public FileView(File file) {
		this.file = file;
	}
	
	@Override
	public void write(Response res) {
		Ex.grab(() -> {
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			res.addBody(fileBytes);
			
		});
	}


	@Override
	public String toString() {
		return "{" +
			" file='" + getFile() + "'" +
			"}";
	}


}
