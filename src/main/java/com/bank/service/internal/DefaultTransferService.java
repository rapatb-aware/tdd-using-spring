package com.bank.service.internal;

import static java.lang.String.format;

import org.springframework.transaction.annotation.Transactional;

import com.bank.domain.Account;
import com.bank.domain.InsufficientFundsException;
import com.bank.domain.TransactionTimeNotAllowedPeriodException;
import com.bank.domain.TransferReceipt;
import com.bank.repository.AccountRepository;
import com.bank.service.FeePolicy;
import com.bank.service.TransactionTimeAllowedPeriodPolicy;
import com.bank.service.TransferService;

public class DefaultTransferService implements TransferService {

	private final AccountRepository accountRepository;
	private final FeePolicy feePolicy;
	private double minimumTransferAmount = 1.00;
	private TransactionTimeAllowedPeriodPolicy transactionTimeAllowedPeriodPolicy;

	public DefaultTransferService(AccountRepository accountRepository, FeePolicy feePolicy) {
		this.accountRepository = accountRepository;
		this.feePolicy = feePolicy;
		this.transactionTimeAllowedPeriodPolicy = defaultTransactionTimeAllowedPeriodPolicy();
	}

	public DefaultTransferService(AccountRepository accountRepository, FeePolicy feePolicy,
			TransactionTimeAllowedPeriodPolicy transactionTimeAllowedPeriodPolicy) {
		this.accountRepository = accountRepository;
		this.feePolicy = feePolicy;
		this.transactionTimeAllowedPeriodPolicy = transactionTimeAllowedPeriodPolicy;
	}

	@Override
	public void setMinimumTransferAmount(double minimumTransferAmount) {
		this.minimumTransferAmount = minimumTransferAmount;
	}

	@Override
	public void setTransactionTimeAllowedPeriodPolicy(
			TransactionTimeAllowedPeriodPolicy transactionTimeAllowedPeriodPolicy) {
		this.transactionTimeAllowedPeriodPolicy = transactionTimeAllowedPeriodPolicy;
	}

	@Override
	@Transactional
	public TransferReceipt transfer(double amount, String srcAcctId, String dstAcctId)
			throws InsufficientFundsException, TransactionTimeNotAllowedPeriodException {

		validateTransactionTimeAllowedPeriod();

		validateTransferAmount(amount);

		return transferProcess(amount, srcAcctId, dstAcctId);
	}

	protected TransactionTimeAllowedPeriodPolicy defaultTransactionTimeAllowedPeriodPolicy() {
		return new SingleTransactionTimeAllowedPeriodPolicy("06:00", "22:00");
	}

	protected void validateTransactionTimeAllowedPeriod() throws TransactionTimeNotAllowedPeriodException {
		if (transactionTimeAllowedPeriodPolicy != null
				&& !transactionTimeAllowedPeriodPolicy.isTransactionAllowedNow()) {
			throw new TransactionTimeNotAllowedPeriodException("transfer is not allowed for now due to company policy");
		}
	}

	protected void validateTransferAmount(double amount) {
		if (amount < minimumTransferAmount) {
			throw new IllegalArgumentException(format("transfer amount must be at least $%.2f", minimumTransferAmount));
		}
	}

	protected TransferReceipt transferProcess(double amount, String srcAcctId, String dstAcctId)
			throws InsufficientFundsException {

		TransferReceipt receipt = new TransferReceipt();

		Account srcAcct = accountRepository.findById(srcAcctId);
		Account dstAcct = accountRepository.findById(dstAcctId);

		receipt.setInitialSourceAccount(srcAcct);
		receipt.setInitialDestinationAccount(dstAcct);

		double fee = feePolicy.calculateFee(amount);
		if (fee > 0) {
			srcAcct.debit(fee);
		}

		receipt.setTransferAmount(amount);
		receipt.setFeeAmount(fee);

		srcAcct.debit(amount);
		dstAcct.credit(amount);

		accountRepository.updateBalance(srcAcct);
		accountRepository.updateBalance(dstAcct);

		receipt.setFinalSourceAccount(srcAcct);
		receipt.setFinalDestinationAccount(dstAcct);

		return receipt;
	}
}
