package com.bluebus.bus_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluebus.bus_project.dto.Customer;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/customer")
public class Customer_Controller {
	@Autowired
	Customer customer;

	@GetMapping("/signup")
	public String loadSignup(ModelMap map) {
		map.put("customer", customer);
		return "customer-signup.html";
	}

	@PostMapping("/signup")
	public String signup(@Valid Customer customer, BindingResult result) {
		if (result.hasErrors())
			return "customer-signup.html";
		else
			return "home.html";
	}
}