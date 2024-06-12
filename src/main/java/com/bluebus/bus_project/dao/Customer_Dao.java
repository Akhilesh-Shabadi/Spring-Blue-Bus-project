package com.bluebus.bus_project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluebus.bus_project.dto.Customer;
import com.bluebus.bus_project.repository.Customer_Repository;

@Repository
public class Customer_Dao {
	@Autowired
	Customer_Repository customer_Repository;

	public boolean checkEmail(String email) {
		return customer_Repository.existsByEmailAndStatusTrue(email);
	}

	public boolean checkMobile(long mobile) {
		return customer_Repository.existsByMobileAndStatusTrue(mobile);
	}

	public void save(Customer customer) {
		customer_Repository.save(customer);
	}

	public Customer findById(int id) {
		return customer_Repository.findById(id).orElseThrow();
	}

	public Customer findByEmail(String email) {
		return customer_Repository.findByEmail(email);
	}

	public Customer findByMobile(long mobile) {
		return customer_Repository.findByMobile(mobile);
	}

	public void deleteIfExists(Customer customer) {
		if (findByMobile(customer.getMobile()) != null) {
			delete(findByMobile(customer.getMobile()));
		}
		if (findByEmail(customer.getEmail()) != null) {
			delete(findByEmail(customer.getEmail()));
		}
	}

	public void delete(Customer customer) {
		customer_Repository.delete(customer);
	}
}
