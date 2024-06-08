package com.bluebus.bus_project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bluebus.bus_project.dto.Agency;
import com.bluebus.bus_project.repository.Agency_Repository;

@Repository
public class Agency_Dao {
	@Autowired
	Agency_Repository agency_Repository;

	public boolean checkEmail(String email) {
		return agency_Repository.existsByEmail(email);
	}

	public boolean checkMobile(long mobile) {
		return agency_Repository.existsByMobile(mobile);
	}

	public void save(Agency agency) {
		agency_Repository.save(agency);
	}
}
