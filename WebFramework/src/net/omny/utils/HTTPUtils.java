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
		private String tag;

		private Version(String tag) {
			this.tag = tag;
		}
		
	}
	
	private static final Map<String, String> MIMES_TYPES = new HashMap<>();
	
	static {
		MIMES_TYPES.put(".js", "application/javascript");
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
