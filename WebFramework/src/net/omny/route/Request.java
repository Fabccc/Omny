package net.omny.route;

import lombok.Getter;

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
	private String httpVersion;
	@Getter
	private String path;
	
	private Request(String[] lines) {
		// Split by space
		String[] firstLine = lines[0].split("\\s+");
		// <METHOD> <PATH> <HTTP_VERSION>
		this.method = Method.valueOf(firstLine[0]);
		this.path = firstLine[1];
		this.httpVersion = firstLine[2];
		//TODO fully parse HTTP request
		
	}
	
}
