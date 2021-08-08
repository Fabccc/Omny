package net.omny.route;


import lombok.Getter;
import net.omny.views.View;

public class FileRoute implements Route{

	@Getter
	private String filePath;

	private FileRoute(String filePath) {
		this.filePath = filePath;
	}
	
	@Override
	public View handle(Request req, Response res) {
		return null;
	}

}
