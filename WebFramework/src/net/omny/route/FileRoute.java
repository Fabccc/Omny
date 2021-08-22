package net.omny.route;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.Getter;
import net.omny.utils.Ex;
import net.omny.utils.HTTPUtils;
import net.omny.views.FileView;
import net.omny.views.View;

/**
 * Represent a route to a file
 * @author Fabien CAYRE (Computer)
 *
 * @date 08/08/2021
 */
public class FileRoute implements Route{

	/**
	 * The path to the file (relative)
	 */
	@Getter
	private String filePath;
	
	private FileView fileView;

	/**
	 * Constructor of the file
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param filePath The path to the file
	 * @date 08/08/2021
	 */
	public FileRoute(String filePath) {
		this.filePath = filePath;
		this.fileView = new FileView(this.filePath);
	}
	
	/**
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param file
	 * @date 22/08/2021
	 */
	public FileRoute(File file) {
		this.filePath = file.getPath();
		this.fileView = new FileView(file);
	}
	
	
	/**
	 * The route handler process
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param req HTTP Request
	 * @param res HTTP Response
	 * @return
	 * @date 08/08/2021
	 */
	@Override
	public View handle(Request req, Response res) {
		Ex.grab(() -> {
			String mimeType = Files.probeContentType(Path.of(this.filePath));
			if(mimeType == null) {
				mimeType = HTTPUtils.findMime(this.filePath);
			}
			if(mimeType.equals("application/pdf")) {
				res.setBinary(true);
			}
			if(mimeType.equals("application/x-msdownload")) {
				res.setBinary(true);
			}
			res.setHeader("Content-Type", mimeType);
		});
		return this.fileView;
	}

}
