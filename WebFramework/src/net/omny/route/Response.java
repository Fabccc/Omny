package net.omny.route;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.omny.utils.HTTP.Version;

public class Response {

	// Response example
	// HTTP VERSION (common to both request & response)
//	HTTP/1.1 404 Not Found
//	Date: Sun, 18 Oct 2012 10:36:20 GMT
//	Server: Apache/2.2.14 (Win32)
//	Content-Length: 230
//	Connection: Closed
//	Content-Type: text/html; charset=iso-8859-1

	@Getter @Setter
	private Code responseCode;
	@Getter @Setter
	private Version httpVersion;
	private Map<String, String> headers = new HashMap<>();

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
		this.headers = new HashMap<>(req.headers);
	}
	
	public Response() {
		
	}

	public String getHeader(String header) {
		return this.headers.get(header.toLowerCase());
	}

	public String setHeader(String header, String value) {
		return this.headers.put(header.toLowerCase(), value);
	}
	
	public String toRawText() {
		return toString();
	}
	
	/**
	 * Returns the bytes of the response
	 * @author Fabien CAYRE (Computer)
	 *
	 * @return
	 * @date 17/08/2021
	 */
	public char[] toChars() {
		return toString().toCharArray();
	}
	
	@Override
	public String toString() {
		String firstLine = this.httpVersion.getTag()+" "+this.responseCode.getCode()+" "+this.getResponseCode().getResponseText()+"\r\n";
		StringBuilder fullText = new StringBuilder(firstLine);
		// Add server attributes in the response text
		if(this.headers.containsKey("server")) {
			fullText.append("Server: "+this.headers.get("server")+"\r\n");
		}else fullText.append("Server: Omny"+"\r\n");
		
		fullText.append("\r\n");
		
		return fullText.toString();
	}
	

}
