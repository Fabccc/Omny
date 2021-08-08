package net.omny.route;

/**
 * RFC = https://datatracker.ietf.org/doc/html/rfc2616#page-51
 * 
 * Those are the method that the server can handle
 * @author Fabien CAYRE (Computer)
 *
 * @date 08/08/2021
 */
public enum Method {

	OPTIONS,
	HEAD,
	GET,
	POST,
	PUT,
	DELETE,
	CONNECT,
	TRACE,
	PATCH;
	
}
