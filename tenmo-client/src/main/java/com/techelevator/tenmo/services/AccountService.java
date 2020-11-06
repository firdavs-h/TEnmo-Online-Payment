package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import com.techelevator.tenmo.models.Account;
import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.view.ConsoleService;

public class AccountService {
	private String BASE_URL;
	private ConsoleService console;
	private AuthenticatedUser currentUser;
	private RestTemplate restTemplate;

	public AccountService(String url) {
		this.BASE_URL = url;
		this.console = new ConsoleService(System.in, System.out);
		this.restTemplate = new RestTemplate();

	}

	public void setCurrentUser(AuthenticatedUser currentUser) {
		this.currentUser = currentUser;
	}

	public BigDecimal viewCurrentBalance() {
		BigDecimal balance = null;
		Integer id = currentUser.getUser().getId();
		try {
			balance = restTemplate
					.exchange(BASE_URL + "balance/" + id, HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}

		return balance;
	}

	public void viewTransferHistory() {
		Transfer[] transfers = null;
		Integer id = currentUser.getUser().getId();
		try {
			transfers = restTemplate
					.exchange(BASE_URL + "transfers/" + id, HttpMethod.GET, makeAuthEntity(), Transfer[].class)
					.getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		printTransfers(transfers);
		Integer transferId = console.getUserInputInteger("Please enter transfer ID to view details (0 to cancel)");
		if (transferId == 0)
			return;
		for (Transfer transfer : transfers) {
			if (transfer.getTransferId().equals(transferId))
				printTransferDetails(transfer);
		}

	}

	public void viewPendingRequests() {
		Transfer[] transfers = null;
		Integer id = currentUser.getUser().getId();
		try {
			transfers = restTemplate.exchange(BASE_URL + "transfers/" + id + "?status=1", HttpMethod.GET,
					makeAuthEntity(), Transfer[].class).getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		printTransfers(transfers);
		Integer transferId = console.getUserInputInteger("Please enter transfer ID to approve/reject (0 to cancel)");
		if (transferId == 0)
			return;
		for (Transfer transfer : transfers) {
			if (transfer.getTransferId().equals(transferId))
				update(transfer);
		}

	}

	public void sendBucks() {
		Transfer transferRet = null;
		Integer currentUserId = currentUser.getUser().getId();
		printAccount();
		Integer receiverUserId;
		Integer accountTo = null;

		while (accountTo == null) {
			receiverUserId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel)");
			if (receiverUserId == 0)
				return;
			accountTo = accountFromId(receiverUserId);
			if (accountTo == null)
				System.out.println("Wrong user ID, try again");
		}

		BigDecimal amount;
		BigDecimal currentBal = viewCurrentBalance();
		do {
			amount = console.getUserInputBigDecimal("Enter amount (0 to cancel)");
			if (amount.compareTo(BigDecimal.valueOf(0)) == 0)
				return;

			if (amount.compareTo(currentBal) > 0) {
				System.out.println("Amount exceeds your current balance: $" + currentBal + ", try different amount");
			}
		} while (amount.compareTo(currentBal) > 0);

		Integer accountFrom = accountFromId(currentUserId);

		Transfer transfer = new Transfer();
		transfer.setTransferType(2);
		transfer.setTransferStatus(2);
		transfer.setAccountFrom(accountFrom);
		transfer.setAccountTo(accountTo);
		transfer.setAmount(amount);
		try {
			transferRet = restTemplate
					.exchange(BASE_URL + "create", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class)
					.getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		printTransfers(transferRet);
		System.out.println("Your current account balance is: $" + viewCurrentBalance());
		System.out.println("---Transaction successful---");

	}

	public void requestBucks() {
		Transfer transferRet = null;
		Integer currentUserId = currentUser.getUser().getId();
		printAccount();
		Integer requestFromUserId;
		Integer accountFrom = null;

		while (accountFrom == null) {
			requestFromUserId = console.getUserInputInteger("Enter ID of user you are requesting from (0 to cancel)");
			if (requestFromUserId == 0)
				return;
			accountFrom = accountFromId(requestFromUserId);
			if (accountFrom == null)
				System.out.println("Wrong user ID, try again");
		}

		BigDecimal amount = console.getUserInputBigDecimal("Enter amount (0 to cancel)");
		if (amount.compareTo(BigDecimal.valueOf(0)) == 0)
			return;
		Integer accountTo = accountFromId(currentUserId);

		Transfer transfer = new Transfer();
		transfer.setTransferType(1);
		transfer.setTransferStatus(1);
		transfer.setAccountFrom(accountFrom);
		transfer.setAccountTo(accountTo);
		transfer.setAmount(amount);
		try {
			transferRet = restTemplate
					.exchange(BASE_URL + "create", HttpMethod.POST, makeTransferEntity(transfer), Transfer.class)
					.getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		printTransfers(transferRet);
		System.out.println("---Transfer request created---");

	}

	// helpers

	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private void printTransfers(Transfer... transfers) {
		if (transfers != null) {
			System.out.println("-------------------------------------------\n" + "Transfers\n"
					+ "ID\t\t From/To \t\t Amount\n" + "-------------------------------------------");

			for (Transfer t : transfers) {
				Integer currentAcc = accountFromId(currentUser.getUser().getId());
				String name = "";
				if (t.getAccountFrom().equals(currentAcc)) {
					name = userNameFromAccount(t.getAccountTo());
					System.out.println(t.getTransferId() + "\t\t To: " + name + "\t\t\t $ " + t.getAmount());
				} else {
					name = userNameFromAccount(t.getAccountFrom());
					System.out.println(t.getTransferId() + "\t\t From: " + name + "\t\t $ " + t.getAmount());
				}
			}
		}
	}

	private void printTransferDetails(Transfer tr) {
		if (tr != null) {

			String type = "";
			if (tr.getTransferType().equals(1))
				type = "Request";
			if (tr.getTransferType().equals(2))
				type = "Send";

			String status = "";
			if (tr.getTransferStatus().equals(1))
				status = "Pending";
			if (tr.getTransferStatus().equals(2))
				status = "Approved";
			if (tr.getTransferStatus().equals(3))
				status = "Rejected";

			System.out.println("--------------------------------------------\n" + "Transfer Details\n"
					+ "--------------------------------------------\n" + "Id: " + tr.getTransferId() + "\n" + "From: "
					+ userNameFromAccount(tr.getAccountFrom()) + "\n" + "To: " + userNameFromAccount(tr.getAccountTo())
					+ "\n" + "Type: " + type + "\n" + "Status: " + status + "\n" + "Amount: " + tr.getAmount());
		}
	}

	private void printAccount() {
		Account[] accounts = getAllAccounts();
		if (accounts != null) {
			System.out.println("-------------------------------------------\n" + "Users\n" + "ID\t Name\n"
					+ "-------------------------------------------");
			for (Account a : accounts) {
				if (a.getUserId() == currentUser.getUser().getId())
					continue;
				System.out.println(a.getUserId() + "\t " + a.getUsername());
			}
			System.out.println("-----------");
		}
	}

	private Account[] getAllAccounts() {
		Account[] accounts = null;

		try {
			accounts = restTemplate.exchange(BASE_URL + "accounts", HttpMethod.GET, makeAuthEntity(), Account[].class)
					.getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		return accounts;

	}

	private Integer accountFromId(Integer userId) {
		Account[] accounts = getAllAccounts();
		Integer accountNum = null;
		if (userId != null && accounts != null) {
			for (Account acc : accounts) {
				if (userId.equals(acc.getUserId()))
					accountNum = acc.getAccountId();
			}
		}
		return accountNum;
	}

	private String userNameFromAccount(Integer accountNum) {
		Account[] accounts = getAllAccounts();
		String name = "";
		if (accountNum != null && accounts != null) {
			for (Account acc : accounts) {
				if (accountNum.equals(acc.getAccountId()) && currentUser.getUser().getId().equals(acc.getUserId())) {
					name = "(Me) " + acc.getUsername();
				} else if (accountNum.equals(acc.getAccountId())) {
					name = acc.getUsername();
				}
			}
		}
		return name;
	}

	public void update(Transfer transfer) {
		Transfer t = transfer;
		System.out.println("1: Approve\n" + "2: Reject\n" + "0: Don't approve or reject\n" + "---------\nPlease choose 1, 2 or 0");
		Integer status = null;
		boolean validEntry = false;
		while (!validEntry) {
			status = console.getUserInputInteger("Please choose an option");
			if ((status.equals(1) || status.equals(2)) || status.equals(0))
				validEntry = true;
		}

		if (status.equals(0))
			return;
		if (t.getAccountTo().equals(accountFromId(currentUser.getUser().getId()))) {
			System.out.println("Not authorized to approve you own request");
			return;

		}
		if (status.equals(1) || status.equals(2)) {
			Integer inserStatus = 0;

			if (status.equals(1)) {
				inserStatus = 2;
				BigDecimal currentBal = viewCurrentBalance();
				if (t.getAmount().compareTo(currentBal) > 0) {
					System.out.println(
							"Request amount exceeds your current balance: $" + currentBal + " , Approval canceled");
					return;
				}
			}
			if (status.equals(2))
				inserStatus = 3;

			t.setTransferStatus(inserStatus);
			try {
				restTemplate.exchange(BASE_URL + "update", HttpMethod.PUT, makeTransferEntity(transfer), Transfer.class)
						.getBody();

			} catch (RestClientResponseException ex) {
				System.out.println("Request - Responce error: " + ex.getRawStatusCode());
			} catch (ResourceAccessException e) {
				System.out.println("Server not accessible. Check your connection or try again.");
			}

		}
		System.out.println("---Transfer Status updated---\n"+viewCurrentBalance());
		
	}

}
