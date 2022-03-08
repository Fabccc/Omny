package net.omny.route;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.omny.exceptions.MalformedRequestException;
import net.omny.utils.HTTPUtils;
import net.omny.utils.HTTPUtils.Headers;

public final class Request {

	// Get Request Example
	// GET / HTTP/1.1
	// Host: localhost:8080
	// User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101
	// Firefox/90.0
	// Accept:
	// text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
	// Accept-Language: fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3
	// Accept-Encoding: gzip, deflate
	// Connection: keep-alive
	// Cookie: email=fabiencayre81%40gmail.com
	// Upgrade-Insecure-Requests: 1
	// Sec-Fetch-Dest: document
	// Sec-Fetch-Mode: navigate
	// Sec-Fetch-Site: none
	// Sec-Fetch-User: ?1

	public static Request lightWeight(String headerLine)
			throws MalformedRequestException {
		return new Request(headerLine);
	}

	/**
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param req
	 * @return
	 * @date 08/08/2021
	 */
	public static Request parse(String req) throws MalformedRequestException {
		String[] lines = req.split("\r\n");
		return new Request(lines);
	}

	@Getter
	private Method method;
	@Getter
	private HTTPUtils.Version httpVersion;
	@Getter
	private String path;
	@Getter
	private String charset;

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
	@Setter @Getter
	private Map<String, String> params = new HashMap<>();

	private Request(String header) throws MalformedRequestException {
		String[] firstLine = header.split("\\s+");
		// <METHOD> <PATH> <HTTP_VERSION>
		try {
			this.method = Method.valueOf(firstLine[0]);
		} catch (IllegalArgumentException e) {
			// Method.valueOf not found
			throw new MalformedRequestException(header);
		}
		this.path = firstLine[1];
		this.httpVersion = HTTPUtils.Version.byTag(firstLine[2]);
	}

	/**
	 * Private constructor
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param lines
	 * @throws MalformedRequestException If the request is malformed
	 * @date 16/08/2021
	 */
	private Request(String[] lines) throws MalformedRequestException {
		// Split by space
		String[] firstLine = lines[0].split("\\s+");
		// <METHOD> <PATH> <HTTP_VERSION>
		try {
			this.method = Method.valueOf(firstLine[0]);
		} catch (IllegalArgumentException e) {
			// Method.valueOf not found
			throw new MalformedRequestException(Arrays.toString(lines));
		}
		this.path = firstLine[1];
		this.httpVersion = HTTPUtils.Version.byTag(firstLine[2]);

		for (int i = 1; i < lines.length; i++) {
			String[] headerLines = lines[i].split(":\\s+");
			this.headers.put(headerLines[0].toLowerCase(), headerLines[1]);
		}
		setCharset();
	}

	public void readFurther(String nextLines) {
		String[] lines = nextLines.split("\r\n");
		for (int i = 0; i < lines.length; i++) {
			String[] headerLines = lines[i].split(":\\s+");
			this.headers.put(headerLines[0].toLowerCase(), headerLines[1]);
		}
		setCharset();
	}

	private void setCharset(){
		if(containsHeader(Headers.ACCEPT_CHARSET)){
			//https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Accept-Charset
			String charset = getHeader(Headers.ACCEPT_CHARSET);
			if(charset.equals("*")){
				charset = "UTF-8";
			}else{
				String[] charsetList = charset.split(",");
				charset = charsetList[0];
			}
		}
	}

	public boolean equalsPath(String path, boolean params) {
		if (!params) {
			return this.path.equals(path);
		}
		String[] urlDivions = path.split("\\/");
		String[] currentUrlDivision = this.path.split("\\/");

		// The number of division is different from the current request division
		if (urlDivions.length != currentUrlDivision.length) {
			return false;
		}
		for (int i = 0; i < currentUrlDivision.length; i++) {
			if (!urlDivions[i].equals(currentUrlDivision[i])) {
				if(!urlDivions[i].startsWith(":"))
					return false;
			}
		}
		return true;
	}

	public Map<String, String> extractParams(String path){
		String[] urlDivions = path.split("\\/");
		String[] currentUrlDivision = this.path.split("\\/");

		// The number of division is different from the current request division
		if (urlDivions.length != currentUrlDivision.length) {
			return null;
		}
		Map<String, String> params = new HashMap<>();
		for (int i = 0; i < currentUrlDivision.length; i++) {
			if (!urlDivions[i].equals(currentUrlDivision[i])) {
				if(urlDivions[i].startsWith(":")){
					try {
						params.put(urlDivions[i].substring(1), HTTPUtils.urlDecode(currentUrlDivision[i]));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
					
			}
		}
		return params;
	}

	/**
	 * Get a URL parameter from the HTTP URL request
	 * This params Map might be empty before this request
	 * is handled in the function "handleRoute" from Router class
	 * 
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

	/**
	 * Return whatever a specified header is contained in the parsed HTTP request
	 * Example:
	 * 
	 * getHeader("host") => true
	 * getHeader("non-existing") => false
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param header
	 * @return True if it contains, False otherwise
	 */
	public boolean containsHeader(String header) {
		return this.headers.containsKey(header.toLowerCase());
	}

}
