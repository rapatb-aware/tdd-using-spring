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
			// LocalTime.MAX is 23:59:59.999999999
			return currentLocalTime.isAfter(startAllowedLocalTime) && currentLocalTime.isBefore(LocalTime.MAX)
					|| currentLocalTime.isAfter(LocalTime.MIDNIGHT) && currentLocalTime.isBefore(endAllowedLocalTime)
					|| currentLocalTime.equals(LocalTime.MIDNIGHT);
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
