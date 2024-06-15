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

import com.bluebus.bus_project.dto.Agency;
import com.bluebus.bus_project.service.Agency_Service;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/agency")
public class Agency_Controller {

	@Autowired
	Agency agency;

	@Autowired
	Agency_Service agencyService;

	@GetMapping("/signup")
	public String signup(ModelMap map) {
		map.put("agency", agency);
		return "agency-signup";
	}

	@PostMapping("/signup")
	public String signup(@Valid Agency agency, BindingResult result, HttpSession session) {
		return agencyService.signup(agency, result, session);
	}

	@GetMapping("/send-otp/{id}")
	public String loadOtpPage(@PathVariable int id, ModelMap map) {
		map.put("id", id);
		return "agency-otp";
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam int id, @RequestParam int otp, HttpSession session) {
		return agencyService.verifyOtp(id, otp, session);
	}

	@GetMapping("/resend-otp/{id}")
	public String resendOtp(@PathVariable int id, HttpSession session) {
		return agencyService.resendOtp(id, session);
	}
}
