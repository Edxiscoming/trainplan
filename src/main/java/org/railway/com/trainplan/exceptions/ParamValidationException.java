package org.railway.com.trainplan.exceptions;

import org.railway.com.trainplan.common.constants.TPResponseCode;


public class ParamValidationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorCode;
	private String errorMsg;


	public ParamValidationException(String errorCode) {
		super();
		this.errorCode = errorCode;
		if (TPResponseCode.parseCode(errorCode) != null) {
			this.errorMsg = TPResponseCode.parseCode(errorCode).getMsg();
		}
	}
	
	
	public ParamValidationException(String errorCode, String requestParamKey) {
		super();
		this.errorCode = errorCode;
		if (TPResponseCode.parseCode(errorCode) != null) {
			this.errorMsg = TPResponseCode.parseCode(errorCode).getMsg()+" ("+requestParamKey+")";
		}
	}
	

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
