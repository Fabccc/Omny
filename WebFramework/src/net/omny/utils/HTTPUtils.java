package net.omny.utils;

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
			return null;
		}
		
		@Getter
		private String tag;

		private Version(String tag) {
			this.tag = tag;
		}
		
	}
	
	
}
