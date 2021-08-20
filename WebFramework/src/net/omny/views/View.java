package net.omny.views;

import net.omny.route.Response;

public interface View {

	/**
	 * 
	 * Write to the passed buffer
	 * 
	 * You must flip the buffer before ending the function
	 * @author Fabien CAYRE (Computer)
	 *
	 * @param buffer
	 * @date 17/08/2021
	 */
	void write(Response res);
	
}
