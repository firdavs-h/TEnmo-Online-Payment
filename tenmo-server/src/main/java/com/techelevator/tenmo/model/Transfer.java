package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class Transfer {
	
	
	private Integer transferId;
	
	@Min(value = 1, message = "The field `transfer type` is more than 0")
	private Integer transferType;
	
	@Min(value = 1, message = "The field `transfer type` min is 1")
	@Max( value = 3, message = "The field 'transfer type` max is 3")
	private Integer transferStatus;
	
	@Min(value = 1, message = "The field `account_from` min is 1")
	private Integer accountFrom;
	
	@Min(value = 1, message = "The field `account_to` min is 1")
	private Integer accountTo;
	
	@DecimalMin(value = "0.01", message = "The field `amount` should be greater than 0.")
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