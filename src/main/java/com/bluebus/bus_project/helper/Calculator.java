package com.bluebus.bus_project.helper;

import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.bluebus.bus_project.dto.Route;
import com.bluebus.bus_project.dto.Station;

@Service
public class Calculator {
	public double calculatePrice(String from, String to, Route route) {
		double fromPrice = 0;
		double toPrice = 0;
		for (Station station : route.getStations()) {
			if (station.getName().equalsIgnoreCase(from)) {
				fromPrice = station.getPrice();
			}
			if (station.getName().equalsIgnoreCase(to)) {
				toPrice = station.getPrice();
			}
		}
		return toPrice - fromPrice;
	}

	public LocalTime timeCalculator(String location, Route route) {
		for (Station station : route.getStations()) {
			if (station.getName().equals(location)) {
				return station.getTime();
			}
		}
		return null;
	}
}
