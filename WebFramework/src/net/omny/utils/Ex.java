package net.omny.utils;

public class Ex {

	/**
	 * Catch exception in a fancy way UwU
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param <T>
	 * @param supp
	 * @return
	 * @date 08/08/2021
	 */
	public static <T> T grab(ExcSupplier<T> supp) {
		T data = null;
		try {
			data = supp.get();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 
	 * Catch exception in a fancy way UwU
	 * 
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param supp
	 * @date 08/08/2021
	 */
	public static void grab(ExcRunnable supp) {
		try {
			supp.run();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static interface ExcRunnable{
		
		void run() throws Exception;
		
	}
	
	public static interface ExcSupplier<T>{
		
		T get() throws Exception;
		
	}
	
}
