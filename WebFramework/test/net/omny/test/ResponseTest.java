package net.omny.test;



import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.omny.route.Code;
import net.omny.route.Response;
import net.omny.utils.HTTP.Version;

public class ResponseTest{

	@Test
	public void textResponseTest() {
		Response response = new Response();
		response.setHttpVersion(Version.V1_1);
		response.setResponseCode(Code.E404_NOT_FOUND);
		response.setHeader("Server", "Nginx");
		
		
		assertEquals("HTTP/1.1 404 Not Found\r\nServer: Nginx\r\n\r\n", response.toString());
	}
	
}
