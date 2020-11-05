package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private int transferId;
	private String TransferType;
	private String TransferStatus;
	private int accountFrom;
	private int accountTo;
	private BigDecimal amount;
	
	
	public int getTransferId() {
		return transferId;
	}
	public void setTransferId(int transferId) {
		this.transferId = transferId;
	}
	public String getTransferType() {
		return TransferType;
	}
	public void setTransferType(String transferType) {
		TransferType = transferType;
	}
	public String getTransferStatus() {
		return TransferStatus;
	}
	public void setTransferStatus(String transferStatus) {
		TransferStatus = transferStatus;
	}
	public int getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(int accountFrom) {
		this.accountFrom = accountFrom;
	}
	public int getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(int accountTo) {
		this.accountTo = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
}
