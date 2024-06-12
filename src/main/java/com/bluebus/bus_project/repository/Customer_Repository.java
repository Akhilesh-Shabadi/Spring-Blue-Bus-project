package com.bluebus.bus_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluebus.bus_project.dto.Customer;

public interface Customer_Repository extends JpaRepository<Customer, Integer> {

	boolean existsByEmailAndStatusTrue(String email);

	boolean existsByMobileAndStatusTrue(long mobile);

	Customer findByEmail(String email);

	Customer findByMobile(long mobile);
}
