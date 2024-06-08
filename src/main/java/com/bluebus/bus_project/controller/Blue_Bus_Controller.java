package com.bluebus.bus_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Blue_Bus_Controller {

	@GetMapping("/")
	public String loadHome() {
		return "home";
	}

	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@RequestParam String role, ModelMap map) {
		if (role.equals("customer"))
			return "redirect:/customer/signup";
		else
			return "redirect:/agency/signup";
	}
}
