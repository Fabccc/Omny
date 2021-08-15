package net.omny.route;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class Response {

	// Response example
	// HTTP VERSION (common to both request & response)
//	HTTP/1.1 404 Not Found
//	Date: Sun, 18 Oct 2012 10:36:20 GMT
//	Server: Apache/2.2.14 (Win32)
//	Content-Length: 230
//	Connection: Closed
//	Content-Type: text/html; charset=iso-8859-1

	@Getter
	private Code responseCode;
	@Getter
	private String httpVersion;
	@Getter
	private String path;
	private Map<String, String> headers;

	/**
	 * Creating response based on the request (taking the same
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param req
	 * @date 15/08/2021
	 */
	public Response(Request req) {
		this.responseCode = Code.S200_OK;
		this.path = req.getPath();
		this.headers = new HashMap<>(req.headers);
	}

	public String getHeader(String header) {
		return this.headers.get(header.toLowerCase());
	}

	public String setHeader(String header, String value) {
		return this.headers.put(header.toLowerCase(), value);
	}

}
