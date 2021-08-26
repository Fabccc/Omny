package net.omny.route;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.omny.utils.Ex;
import net.omny.utils.HTTPUtils;

public final class Request {

	// Get Request Example
//	GET / HTTP/1.1
//	Host: localhost:8080
//	User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0
//	Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
//	Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3
//	Accept-Encoding: gzip, deflate
//	Connection: keep-alive
//	Cookie: email=fabiencayre81%40gmail.com
//	Upgrade-Insecure-Requests: 1
//	Sec-Fetch-Dest: document
//	Sec-Fetch-Mode: navigate
//	Sec-Fetch-Site: none
//	Sec-Fetch-User: ?1

	/**
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param req
	 * @return
	 * @date 08/08/2021
	 */
	public static Request parse(String req) {
		String[] lines = req.split("\r\n");
		return new Request(lines);
	}
	
	@Getter
	private Method method;
	@Getter
	private HTTPUtils.Version httpVersion;
	@Getter
	private String path;
	
	/**
	 * Headers content
	 */
	protected Map<String, String> headers = new HashMap<>();
	
	/**
	 * Represent URL parameters
	 * Example:
	 * "/foo/:bar"
	 * =>
	 * "/foo/TestTest"
	 * TestTest is treated as parameter
	 * and is accessible from params hashmap
	 */
	@Setter
	private Map<String, String> params = new HashMap<>();
	
	/**
	 * Private constructor
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param lines
	 * @date 16/08/2021
	 */
	private Request(String[] lines) {
		// Split by space
		String[] firstLine = lines[0].split("\\s+");
		// <METHOD> <PATH> <HTTP_VERSION>
		this.method = Ex.grab(() -> Method.valueOf(firstLine[0]), Method.GET);
		this.path = firstLine[1];
		this.httpVersion = HTTPUtils.Version.byTag(firstLine[2]);

		for(int i = 1; i < lines.length; i++){
			String[] headerLines = lines[i].split(":\\s+");
			this.headers.put(headerLines[0].toLowerCase(), headerLines[1]);
		}
	}
	
	/**
	 * Get a URL parameter from the HTTP URL request
	 * This params Map might be empty before this request 
	 * is handled in the function "handleRoute" from Router class
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param param
	 * @return
	 * @date 16/08/2021
	 */
	public String getParams(String param) {
		return this.params.get(param);
	}
	
	/**
	 * Get a header from the parsed HTTP request
	 * Example:
	 * 
	 * getHeader("host") => "example.com"
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param header
	 * @return
	 * @date 16/08/2021
	 */
	public String getHeader(String header) {
			return this.headers.get(header.toLowerCase());
	}
	
}
