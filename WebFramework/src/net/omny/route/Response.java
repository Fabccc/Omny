package net.omny.route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;
import net.omny.utils.Primitive;
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
	private List<Byte> body = new ArrayList<>();

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
		this.httpVersion = req.getHttpVersion();
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
	
	
	public void addBody(String value) {
		for(byte b : value.getBytes())
			this.body.add(b);
	}
	
	public void addBody(byte value) {
		this.body.add(value);
	}
	
	public void addBody(char value) {
		byte first = (byte) (value >> 2);
		byte sec = (byte) value;
		this.body.add(first);
		this.body.add(sec);
	}
	
	@Override
	public String toString() {
		String firstLine = this.httpVersion.getTag()+" "+this.responseCode.getCode()+" "+this.getResponseCode().getResponseText()+"\r\n";
		StringBuilder fullText = new StringBuilder(firstLine);
		// Add server attributes in the response text if doesn't exists
		if(!this.headers.containsKey("server")) {
			this.headers.put("server", "Omny");
		}
		
		if(!this.headers.containsKey("Content-Length")) {
			this.headers.put("content-length", String.valueOf(this.body.size()));
		}
		
		for(String header : this.headers.keySet()) {
			String value = this.headers.get(header);
			
			String capitalized = Arrays.stream(header.split("\\-"))
				.map(StringUtils::capitalize)
				.collect(Collectors.joining("-"));
			fullText.append(capitalized+": "+value+"\r\n");
		}
		
		fullText.append("\r\n");
		if(this.body.size() == 0) {
			return fullText.toString();
		}
		fullText.append(new String(Primitive.toArray(this.body)));
		fullText.append("\r\n");
		
		return fullText.toString();
	}
	

}
