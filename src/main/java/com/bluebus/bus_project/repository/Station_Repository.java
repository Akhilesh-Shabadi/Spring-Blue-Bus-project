package com.bluebus.bus_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluebus.bus_project.dto.Station;

public interface Station_Repository extends JpaRepository<Station, Integer> {
	List<Station> findByName(String from);
}
