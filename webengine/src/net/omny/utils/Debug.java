package net.omny.utils;

import java.util.HashMap;
import java.util.Map;

public class Debug {

	public static boolean ENABLE = true;

	private static ThreadLocal<Map<String, Long>> chronos = new ThreadLocal<>() {
		@Override
		public java.util.Map<String, Long> initialValue() {
			return new HashMap<>();
		};
	};

	public static void debug(String string) {
		if (ENABLE)
			System.out.println("[Debug] " + string);
	}

	public static void reset(String tag) {
		if (ENABLE) {
			chronos.get().put(tag, System.currentTimeMillis());
		}
	}

	public static void warn(String string) {
		if (ENABLE) {
			System.err.println("[Warn Debug] " + string);
		}
	}

	public static void time(String tag, String message){
		if(ENABLE){
			if(chronos.get().containsKey(tag)){
				System.out.println("[Timer Debug] "+
				message.replace("{ms}", String.valueOf(System.currentTimeMillis() - chronos.get().get(tag))));
			}
		}
	}

	public static void time(String tag) {
		if (ENABLE) {
			if (chronos.get().containsKey(tag)) {
				System.out
					.println(
						"[Timer Debug] Elapsed " + (System.currentTimeMillis() - chronos.get().get(tag)) + " ms from the last time set {"
							+ tag + "}");
			}
		}
	}

}
