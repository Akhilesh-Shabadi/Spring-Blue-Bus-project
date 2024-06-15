package com.bluebus.bus_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bluebus.bus_project.service.Common_Service;

import jakarta.servlet.http.HttpSession;

@Controller
public class Blue_Bus_Controller {

	@Autowired
	Common_Service common_Service;

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

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String login(@RequestParam("email-phone") String emph, String password, HttpSession session) {
		return common_Service.login(emph, password, session);
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("agency");
		session.removeAttribute("customer");
		session.setAttribute("successMessage", "Logout Success");
		return "redirect:/";
	}
}
