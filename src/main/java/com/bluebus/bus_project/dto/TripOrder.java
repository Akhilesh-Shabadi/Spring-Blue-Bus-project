package com.bluebus.bus_project.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class TripOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String from;
	private String to;
	private double amount;
	private LocalTime departureTime;
	private LocalTime arrivalTime;
	private LocalDate bookingDate;
	private String orderId;
	private String paymentId;
}
