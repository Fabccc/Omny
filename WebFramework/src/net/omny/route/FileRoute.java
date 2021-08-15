package net.omny.route;



import lombok.Getter;
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
		return this.fileView;
	}

}
