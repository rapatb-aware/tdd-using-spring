package com.bank.service.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;

import org.junit.Test;

public class SingleTransactionTimeAllowedPeriodPolicyTest {

	private SingleTransactionTimeAllowedPeriodPolicy startTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy(
			String mockCurrentTime) {
		return new SingleTransactionTimeAllowedPeriodPolicy("06:00", "22:00") {
			protected LocalTime getCurrentTime() {
				return LocalTime.parse(mockCurrentTime);
			}
		};
	}

	private SingleTransactionTimeAllowedPeriodPolicy startTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy(
			String mockCurrentTime) {
		return new SingleTransactionTimeAllowedPeriodPolicy("22:00", "06:00") {
			protected LocalTime getCurrentTime() {
				return LocalTime.parse(mockCurrentTime);
			}
		};
	}

	@Test
	public void forStartTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy_shouldReturnFalse_IfCurrentTimeIsBeforeStartAllowedTime() {
		assertFalse(
				startTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy("05:59:59").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy_shouldReturnTrue_IfCurrentTimeIsAfterStartAllowedTime() {
		assertTrue(
				startTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy("06:00:01").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy_shouldReturnTrue_IfCurrentTimeIsBeforeEndAllowedTime() {
		assertTrue(
				startTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy("21:59:59").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy_shouldReturnFalse_IfCurrentTimeIsAfterEndAllowedTime() {
		assertFalse(
				startTimeBeforeEndTimeSingleTransactionTimeAllowedPeriodPolicy("22:00:01").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy_shouldReturnFalse_IfCurrentTimeIsBeforeStartAllowedTime() {
		assertFalse(
				startTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy("21:59:59").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolic_shouldReturnTrue_IfCurrentTimeIsAfterStartAllowedTime() {
		assertTrue(startTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy("22:00:01").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolic_shouldReturnTrue_IfCurrentTimeIsBeforeEndAllowedTime() {
		assertTrue(startTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy("05:59:59").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolic_shouldReturnFalse_IfCurrentTimeIsAfterEndAllowedTime() {
		assertFalse(
				startTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy("06:00:01").isTransactionAllowedNow());
	}

	@Test
	public void forStartTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolic_shouldReturnTrue_IfCurrentTimeIsMidnight() {
		assertTrue(startTimeAfterEndTimeSingleTransactionTimeAllowedPeriodPolicy("00:00").isTransactionAllowedNow());
	}
}
