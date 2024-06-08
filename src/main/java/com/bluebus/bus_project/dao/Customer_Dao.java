package com.bluebus.bus_project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluebus.bus_project.repository.Customer_Repository;

@Repository
public class Customer_Dao {
	@Autowired
	Customer_Repository customer_Repository;

	public boolean checkEmail(String email) {
		return customer_Repository.existsByEmail(email);
	}

	public boolean checkMobile(long mobile) {
		return customer_Repository.existsByMobile(mobile);
	}
}
