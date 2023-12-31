package net.omny.route;

import lombok.Getter;

/**
 * {@link} https://developer.mozilla.org/fr/docs/Web/HTTP/Status
 * @author Fabien CAYRE (Computer)
 *
 * @date 15/08/2021
 */
public enum Code {

	// INFO RESPONSE CODE
	I100_CONTINUE(100),
	I101_SWITCH_PROTOCOL(101),
	I103_PROCESSING(103),
	// SUCESS RESPONSE CODE
	S200_OK(200, "OK"),
	S201_CREATED(201),
	S202_ACCEPTED(202),
	S203_NON_AUTH_INFO(203),
	S204_NO_CONTENT(204),
	S205_RESET_CONTENT(205),
	S206_PARTIAL_CONTENT(206),
	// REDIRECTING RESPONSE CODE
	R300_MULTIPLE_CHOICE(300),
	R301_MOVED_PERM(301),
	R302_FOUND(302),
	R303_SEE_OTHER(303),
	R304_NOT_MODIFIER(304),
	R307_TEMP_REDIRECT(307),
	R308_PERM_REDIRECT(308),
	// ERROR CLIENT SIDE RESPONSE CODE
	E400_BAD_REQUEST(400),
	E401_UNAUTHORIZED(401, "Not authentified"),
	E402_PAYMENT_REQUIRED(402),
	E403_FORBIDDEN(403),
	E404_NOT_FOUND(404, "Not Found"),
	E405_METHOD_NOT_ALLOWED(405),
	E406_NOT_ACCEPT(406),
	E407_PROXY_AUTH_REQUIRED(407),
	E408_REQUEST_TIMEOUT(408),
	E409_CONFLICT(409),
	E410_GONE(410),
	E411_LENGTH_REQUIRED(411),
	E412_PRECONDITION_FAILED(412),
	E413_PAYLOAD_TOO_LARGE(413),
	// ERROR SERVER SIDE RESPONSE CODE
	E500_INTERNAL_ERROR(500);

	@Getter
	private int code;
	@Getter
	private final byte[] codeStringAsByte;
	@Getter
	private String responseText;
	@Getter
	private final byte[] responseAsByte;
	
	private Code(int code, String responseText) {
		this.code = code;
		this.codeStringAsByte = Integer.toString(code).getBytes();
		this.responseText = responseText;
		this.responseAsByte = this.responseText.getBytes();
	}

	private Code(int code) {
		this(code, "Not defined");
	}
	
	
	
	
	
}
