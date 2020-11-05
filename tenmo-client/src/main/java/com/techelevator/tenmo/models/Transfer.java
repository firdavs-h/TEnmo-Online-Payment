package com.techelevator.tenmo.models;

import java.math.BigDecimal;

public class Transfer {
	
	private Integer transfer_id;
	private Integer transfer_type_id;
	private Integer transfer_status_id;
	private Integer account_from;
	private Integer account_to;
	private BigDecimal amount;
	
	
	
	public Transfer(Integer transfer_id, Integer transfer_type_id, Integer transfer_status_id, Integer account_from,
			Integer account_to, BigDecimal amount) {
		
		this.transfer_id = transfer_id;
		this.transfer_type_id = transfer_type_id;
		this.transfer_status_id = transfer_status_id;
		this.account_from = account_from;
		this.account_to = account_to;
		this.amount = amount;
	}
	
	public int getTransferId() {
		return transfer_id;
	}
	public void setTransferId(Integer transferId) {
		this.transfer_id = transferId;
	}
	public Integer getTransferType() {
		return transfer_type_id;
	}
	public void setTransferType(Integer transferType) {
		transfer_type_id = transferType;
	}
	public Integer getTransferStatus() {
		return transfer_status_id;
	}
	public void setTransferStatus(Integer transferStatus) {
		transfer_status_id = transferStatus;
	}
	public int getAccountFrom() {
		return account_from;
	}
	public void setAccountFrom(Integer accountFrom) {
		this.account_from = accountFrom;
	}
	public int getAccountTo() {
		return account_to;
	}
	public void setAccountTo(Integer accountTo) {
		this.account_to = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	
}
