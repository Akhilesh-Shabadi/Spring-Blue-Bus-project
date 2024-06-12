package com.bluebus.bus_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluebus.bus_project.dto.Agency;

public interface Agency_Repository extends JpaRepository<Agency, Integer> {

	boolean existsByEmailAndStatusTrue(String email);

	boolean existsByMobileAndStatusTrue(long mobile);

	Agency findByEmail(String email);

	Agency findByMobile(long mobile);
}
