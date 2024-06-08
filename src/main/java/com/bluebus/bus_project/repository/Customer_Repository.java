package com.bluebus.bus_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluebus.bus_project.dto.Customer;

public interface Customer_Repository extends JpaRepository<Customer, Integer> {

	boolean existsByEmail(String email);

	boolean existsByMobile(long mobile);

}
