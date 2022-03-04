package net.omny.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class HTTPUtils {

	public enum Version {

		V1_1("HTTP/1.1"),
		V2("HTTP/2"),
		V3("HTTP/3");
		
		public static Version byTag(String tag) {
			for(Version v : Version.values())
				if(v.tag.equals(tag))
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
	
	public static final String CRLF = "\r\n";
	public static final byte[] CRLF_AS_BYTES = CRLF.getBytes();
	public static final String DOUBLE_CRLF = "\r\n\r\n";
	public static final byte[] DOUBLE_CRLF_AS_BYTES = DOUBLE_CRLF.getBytes();
	public static final byte SPACE_AS_BYTE = ' ';
	public static final String DEFAULT_NAMESPACE = "__none__";

	private static final Map<String, String> MIMES_TYPES = new HashMap<>();
	
	static {
		MIMES_TYPES.put(".js", "application/javascript");
		MIMES_TYPES.put(".exe", "application/vnd.microsoft.portable-executable");
	}
	
	/**
	 * Find MIME types of files
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
	
	
}
