package com.bank.service.internal;

import java.time.LocalTime;

import com.bank.service.TransactionTimeAllowedPeriodPolicy;

public class SingleTransactionTimeAllowedPeriodPolicy implements TransactionTimeAllowedPeriodPolicy {

	private final String startAllowedTime;
	private final String endAllowedTime;

	public SingleTransactionTimeAllowedPeriodPolicy(String startAllowedTime, String endAllowedTime) {
		super();
		this.startAllowedTime = startAllowedTime;
		this.endAllowedTime = endAllowedTime;
	}

	@Override
	public boolean isTransactionAllowedNow() {
		LocalTime startAllowedLocalTime = LocalTime.parse(startAllowedTime);
		LocalTime endAllowedLocalTime = LocalTime.parse(endAllowedTime);
		LocalTime currentLocalTime = getCurrentTime();

		if (startAllowedLocalTime.isBefore(endAllowedLocalTime)) {
			return currentLocalTime.isAfter(startAllowedLocalTime) && currentLocalTime.isBefore(endAllowedLocalTime);
		} else {
			return currentLocalTime.isAfter(startAllowedLocalTime) || currentLocalTime.isBefore(endAllowedLocalTime);
		}
	}

	protected LocalTime getCurrentTime() {
		return LocalTime.now();
	}

	@Override
	public String toString() {
		return "SingleTransactionTimePeriodPolicy [startAllowedTime=" + startAllowedTime + ", endAllowedTime="
				+ endAllowedTime + "]";
	}

}
