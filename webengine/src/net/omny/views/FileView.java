package net.omny.views;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

import net.omny.route.Response;
import net.omny.utils.Debug;
import net.omny.utils.Ex;

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
			Debug.debug("A");
			byte[] fileBytes = Files.readAllBytes(file.toPath());
			Debug.debug("B");
			res.addBody(fileBytes);
			Debug.debug("C");
			Debug.debug(new String(res.toStringAsByte()));
			
		});
	}

}
