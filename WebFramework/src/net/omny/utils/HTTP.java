package net.omny.utils;

import lombok.Getter;

public class HTTP {

	public enum Version {

		V1_1("HTTP/1.1"),
		V2("HTTP/2"),
		V3("HTTP/3");
		
		@Getter
		private String tag;

		private Version(String tag) {
			this.tag = tag;
		}
		
	}
	
	
}
