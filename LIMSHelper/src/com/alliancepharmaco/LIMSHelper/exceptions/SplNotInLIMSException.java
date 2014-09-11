package com.alliancepharmaco.LIMSHelper.exceptions;

/*
 * Thrown when a sample in manifest can't be found in LIMS
 */
public class SplNotInLIMSException extends Exception {
	public SplNotInLIMSException() {
		super();
	}
	
	public SplNotInLIMSException(String msg) {
		super(msg);
	}
}
