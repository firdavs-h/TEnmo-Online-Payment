package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	private RestTemplate restTemplate = new RestTemplate();

	public AccountService(String url) {
		this.BASE_URL = url;
		this.console = new ConsoleService(System.in, System.out);

	}

	public void viewCurrentBalance(AuthenticatedUser currentUser) {
		BigDecimal balance = null;
		Integer id = currentUser.getUser().getId();
		try {
			ResponseEntity<BigDecimal> response = restTemplate.exchange(BASE_URL + "balance/" + id, HttpMethod.GET,
					makeAuthEntity(currentUser), BigDecimal.class);
			balance = response.getBody();
		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		System.out.println("Your current account balance is: " + balance);
	}

	public Transfer[] viewTransferHistory(AuthenticatedUser currentUser) {
		Transfer[] transfers = null;
		Integer id = currentUser.getUser().getId();
		try {
			transfers = restTemplate.exchange(BASE_URL + "transfers/" + id, HttpMethod.GET, makeAuthEntity(currentUser),
					Transfer[].class).getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		printTransfer(transfers);
		return transfers;
	} 

	public void viewPendingRequests(AuthenticatedUser currentUser) {
		Transfer[] transfers = null;
		Integer id = currentUser.getUser().getId();
		try {
			transfers = restTemplate.exchange(BASE_URL + "transfers/" + id + "/pending", HttpMethod.GET,
					makeAuthEntity(currentUser), Transfer[].class).getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		printTransfer(transfers);
	}

	public Transfer sendBucks(AuthenticatedUser currentUser) {
		Transfer transferRet =null;
		Account[] accounts = getAllAccounts(currentUser);
		Integer currentUserId = currentUser.getUser().getId();
		printAccount(currentUserId, accounts);
		Integer receiverUserId = console.getUserInputInteger("Enter ID of user you are sending to (0 to cancel): ");
		

		BigDecimal amount = console.getUserInputBigDecimal("Enter amount: ");
		Integer accountFrom = accountFromId(currentUserId, accounts);
		Integer accountTo = accountFromId(receiverUserId, accounts);

		Transfer transfer = new Transfer();
		transfer.setTransferType(2);
		transfer.setTransferStatus(2);
		transfer.setAccountFrom(accountFrom);
		transfer.setAccountTo(accountTo);
		transfer.setAmount(amount);
		try {
			transferRet=restTemplate.exchange(BASE_URL + "send", HttpMethod.POST, makeTransferEntity(transfer, currentUser),
					Transfer.class).getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		return transferRet;
		
	}

	// helpers
	private HttpEntity makeAuthEntity(AuthenticatedUser currentUser) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, AuthenticatedUser currentUser) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(currentUser.getToken());
		HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

	private void printTransfer(Transfer... transfer) {
		if (transfer != null) {

			for (Transfer t : transfer) {
				System.out.println(t.getTransferId() + "\t" + t.getAccountFrom() + "\t" + t.getAmount());
			}
		}
	}
	
	private void printAccount(int userId, Account... accounts ) {
		if(accounts==null) System.out.println("no account");
		if (accounts != null) {
			System.out.println("-------------------------------------------\n" + 
					"Users\n" + 
					"ID          Name\n" + 
					"-------------------------------------------");

			for (Account a : accounts){
				if(a.getUserId()==userId) continue;
				System.out.println(a.getUserId() + "\t" + a.getUsername()+"\n" );
			}
			System.out.println("-----------");
		}
	}

	private Account[] getAllAccounts(AuthenticatedUser currentUser) {
		Account[] accounts = null;

		try {
			accounts = restTemplate
					.exchange(BASE_URL + "accounts", HttpMethod.GET, makeAuthEntity(currentUser), Account[].class)
					.getBody();

		} catch (RestClientResponseException ex) {
			System.out.println("Request - Responce error: " + ex.getRawStatusCode());
		} catch (ResourceAccessException e) {
			System.out.println("Server not accessible. Check your connection or try again.");
		}
		return accounts;

	}

	private Integer accountFromId(Integer userId, Account[] accounts) {
		Integer accountNum = null;
		if (userId != null && accounts != null) {
			for (Account account : accounts) {
				if (account.getUserId() == userId)
					accountNum = account.getAccountId();
			}
		}
		return accountNum;
	}

}