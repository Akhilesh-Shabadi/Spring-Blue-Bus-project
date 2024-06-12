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
		return agency_Repository.existsByEmailAndStatusTrue(email);
	}

	public boolean checkMobile(long mobile) {
		return agency_Repository.existsByMobileAndStatusTrue(mobile);
	}

	public void save(Agency agency) {
		agency_Repository.save(agency);
	}

	public Agency findById(int id) {
		return agency_Repository.findById(id).orElseThrow();
	}

	public Agency findByEmail(String email) {
		return agency_Repository.findByEmail(email);
	}

	public Agency findByMobile(long mobile) {
		return agency_Repository.findByMobile(mobile);
	}

	public void deleteIfExists(Agency agency) {
		if (findByMobile(agency.getMobile()) != null) {
			delete(findByMobile(agency.getMobile()));
		}
		if (findByEmail(agency.getEmail()) != null) {
			delete(findByEmail(agency.getEmail()));
		}
	}

	public void delete(Agency agency) {
		agency_Repository.delete(agency);
	}
}
