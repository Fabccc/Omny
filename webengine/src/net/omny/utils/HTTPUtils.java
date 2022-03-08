package net.omny.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import net.omny.route.Code;
import net.omny.route.Response;
import net.omny.views.View;

public class HTTPUtils {

	public enum Version {

		V1_1("HTTP/1.1"),
		V2("HTTP/2"),
		V3("HTTP/3");

		public static Version byTag(String tag) {
			for (Version v : Version.values())
				if (v.tag.equals(tag))
					return v;
			return V1_1;
		}

		@Getter
		private final String tag;
		@Getter
		private final byte[] tagAsByte;

		private Version(String tag) {
			this.tag = tag;
			this.tagAsByte = this.tag.getBytes();
		}

	}

	public static class ErrorView implements View {

		private Code code;
		private String content;
		private String mimeType;

		public ErrorView(Code code, String content) {
			this(code, content, MimeType.JSON);
		}

		public ErrorView(Code code, String content, String mimeType) {
			this.code = code;
			this.content = content;
			this.mimeType = mimeType;
		}

		@Override
		public void write(Response res) {
			res.setResponseCode(this.code);
			res.setHeader(Headers.CONTENT_TYPE, this.mimeType);
			res.addBody(content);
		}

	}

	public static class MimeType{
		public static final String JAVASCRIPT = "application/javascript";
		public static final String MICROSOFT_EXECUTABLE = "application/vnd.microsoft.portable-executable";
		public static final String JSON = "application/json";
		public static final String HTML = "text/html";
	}

	public static class Headers {
		public static final String ACCEPT_DATETIME = "Accept-Datetime";
		public static final String ACCEPT_CHARSET = "Accept-Charset";
		public static final String ACCEPT_ENCODING = "Accept-Encoding";
		public static final String ACCEPT_LANGUAGE = "Accept-Language";
		public static final String CONTENT_TYPE = "Content-Type";
		public static final String CACHE_CONTROL = "Cache-Control";
		public static final String AUTHORIZATION = "Authorization";
	}

	public static final String CRLF = "\r\n";
	public static final byte[] CRLF_AS_BYTES = CRLF.getBytes();
	public static final String DOUBLE_CRLF = "\r\n\r\n";
	public static final byte[] DOUBLE_CRLF_AS_BYTES = DOUBLE_CRLF.getBytes();
	public static final byte SPACE_AS_BYTE = ' ';
	public static final String DEFAULT_NAMESPACE = "__none__";

	private static final Map<String, String> MIMES_TYPES = new HashMap<>();

	static {
		MIMES_TYPES.put(".js", MimeType.JAVASCRIPT);
		MIMES_TYPES.put(".exe", MimeType.MICROSOFT_EXECUTABLE);
	}

	/**
	 * Find MIME types of files
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param filePath Path to the file
	 * @return MIME type of the file or text/plain if not found
	 * @date 22/08/2021
	 */
	public static String findMime(String filePath) {
		return MIMES_TYPES.entrySet()
				.stream()
				.filter(entry -> filePath.endsWith(entry.getKey()))
				.map(Map.Entry::getValue)
				.findFirst()
				.orElse("text/plain");
	}

	public static String urlEncode(String base) throws UnsupportedEncodingException {
		return urlEncode(base, "UTF-8");
	}

	public static String urlDecode(String base) throws UnsupportedEncodingException {
		return urlDecode(base, "UTF-8");
	}

	public static String urlEncode(String base, String charset) throws UnsupportedEncodingException {
		return URLEncoder.encode(base, charset);
	}

	public static String urlDecode(String base, String charset) throws UnsupportedEncodingException {
		return URLDecoder.decode(base, charset);
	}

}
