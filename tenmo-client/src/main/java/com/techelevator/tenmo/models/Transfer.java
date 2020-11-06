package com.techelevator.tenmo.models;

import javax.validation.Valid; 

import javax.validation.constraints.Positive;

import java.math.BigDecimal;
@Valid
public class Transfer {
	
	private Integer transferId;
	private Integer transferType;
	private Integer transferStatus;
	private Integer accountFrom;
	private Integer accountTo;
	@Positive
	private BigDecimal amount;
	
	
	public Integer getTransferId() {
		return transferId;
	}
	public void setTransferId(Integer transferId) {
		this.transferId = transferId;
	}
	public Integer getTransferType() {
		return transferType;
	}
	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}
	public Integer getTransferStatus() {
		return transferStatus;
	}
	public void setTransferStatus(Integer transferStatus) {
		this.transferStatus = transferStatus;
	}
	public Integer getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(Integer accountFrom) {
		this.accountFrom = accountFrom;
	}
	public Integer getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(Integer accountTo) {
		this.accountTo = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}