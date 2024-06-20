package com.bluebus.bus_project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluebus.bus_project.dto.Route;

public interface Route_Repository extends JpaRepository<Route, Integer> {

	List<Route> findByBus_idIn(List<Integer> list);

}
