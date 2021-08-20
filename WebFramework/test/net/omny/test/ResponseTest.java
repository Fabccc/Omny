package net.omny.test;



import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.StringWriter;

import org.junit.Test;

import net.omny.route.Code;
import net.omny.route.Response;
import net.omny.utils.Ex;
import net.omny.utils.HTTP.Version;
import net.omny.views.TextView;

public class ResponseTest{

	@Test
	public void test404ResponseAndHeader() {
		Response response = new Response();
		response.setHttpVersion(Version.V1_1);
		response.setResponseCode(Code.E404_NOT_FOUND);
		response.setHeader("Server", "Nginx");
		
		assertEquals("HTTP/1.1 404 Not Found\r\nServer: Nginx\r\nContent-Length: 0\r\n\r\n", response.toString());
	}
	
	@Test
	public void testFullResponseProcess() {
		TextView txtView = new TextView("This is a text !!!\r\n");

		StringWriter fakeClientSocket = new StringWriter();
		
		BufferedWriter writer = new BufferedWriter(fakeClientSocket);
		
		Response response = new Response();
		response.setHttpVersion(Version.V1_1);
		response.setResponseCode(Code.S200_OK);
		
		txtView.write(response);
		
		Ex.grab(() -> {
			// Response contains  header, body and CRLF
			writer.write(response.toChars());
			writer.flush();
		});
		StringBuffer fullHTTPResponse = fakeClientSocket.getBuffer();
		String resultString = fullHTTPResponse.toString().trim()+"\r\n\r\n";
		assertEquals("HTTP/1.1 200 OK\r\nServer: Omny\r\nContent-Length: 20\r\n\r\nThis is a text !!!\r\n\r\n", resultString);
	}
	
}
