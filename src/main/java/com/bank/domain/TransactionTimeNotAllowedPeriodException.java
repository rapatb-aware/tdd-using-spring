package com.bank.domain;

@SuppressWarnings("serial")
public class TransactionTimeNotAllowedPeriodException extends Exception {

	public TransactionTimeNotAllowedPeriodException() {
	}

	public TransactionTimeNotAllowedPeriodException(String arg0) {
		super(arg0);
	}

	public TransactionTimeNotAllowedPeriodException(Throwable arg0) {
		super(arg0);
	}

	public TransactionTimeNotAllowedPeriodException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public TransactionTimeNotAllowedPeriodException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
