package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.AccountDAO;

@RestController
public class AccountController {
	private AccountDAO accountDao;
	
	public AccountController(AccountDAO accountDAO) {
		this.accountDao =accountDAO;
	
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping (path="/balance/{id}", method = RequestMethod.GET)
	public BigDecimal getBal(@PathVariable int id) {
		return accountDao.getBalance(id);
		
		
	}

}
