package net.omny.utils;

import java.util.HashMap;
import java.util.Map;

public class Debug {

	public static final boolean ENABLE = true;
	
	private static Map<String, Long> chronos = new HashMap<>();
	
	public static void debug(String string) {
		if(ENABLE)
			System.out.println("[Debug] "+string);
	}
	
	public static void reset(String tag) {
		if(ENABLE) {
			chronos.put(tag, System.currentTimeMillis());
		}
	}
	
	public static void warn(String string) {
		if(ENABLE) {
			System.err.println("[Warn Debug] "+string);
		}
	}
	
	public static void time(String tag) {
		if(ENABLE) {
			if(chronos.containsKey(tag)) {
				System.out.println("[Timer Debug] Elapsed "+(System.currentTimeMillis()-chronos.get(tag))+" ms from the last time set {"+tag+"}");				
			}
		}
	}
	
}
