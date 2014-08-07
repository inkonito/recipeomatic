package fr.ropiteaux.rom.core.security;

public class RomSecurityException extends Exception {
	private static final long serialVersionUID = -9070987487331991695L;
	public static final int UNKNWON_USER = 01;
	public static final int INVALID_USER_TOKEN = 02;
	public static final int USER_TOKEN_EXPIRED = 03;

	private int errorCode;

	public RomSecurityException(int errorCode) {
		this.setErrorCode(errorCode);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}