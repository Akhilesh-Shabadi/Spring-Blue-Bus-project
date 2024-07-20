package com.bluebus.bus_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bluebus.bus_project.dto.Customer;
import com.bluebus.bus_project.service.Customer_Service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/customer")
public class Customer_Controller {
	@Autowired
	Customer customer;

	@Autowired
	Customer_Service customer_Service;

	@GetMapping("/signup")
	public String loadSignup(ModelMap map) {
		map.put("customer", customer);
		return "customer-signup.html";
	}

	@PostMapping("/signup")
	public String signup(@Valid Customer customer, BindingResult result, HttpSession session) {
		return customer_Service.signup(customer, result, session);
	}

	@GetMapping("/send-otp/{id}")
	public String loadOtpPage(@PathVariable int id, ModelMap map) {
		map.put("id", id);
		return "customer-otp";
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam int id, @RequestParam int otp, HttpSession session) {
		return customer_Service.verifyOtp(id, otp, session);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, HttpSession session) {
		return customer_Service.resendOtp(id, session);
	}

	@GetMapping("/view-bookings")
	public String viewPastBookings(HttpSession session, ModelMap map) {
		return customer_Service.viewbookings(session, map);
	}
}
