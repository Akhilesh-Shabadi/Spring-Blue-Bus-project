package com.bluebus.bus_project.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/book-bus")
	public String bookbus() {
		return "bookbus";
	}

	@PostMapping("/bookbus")
	public String bookbus(@RequestParam String from, @RequestParam String to, @RequestParam LocalDate date,
			HttpSession httpSession, ModelMap map) {
		return common_Service.searchBus(from, to, date, httpSession, map);
	}

	@PostMapping("/book-ticket")
	public String bookTicket(@RequestParam String from, @RequestParam String to, @RequestParam int routeId,
			HttpSession session, ModelMap map, @RequestParam int seat) {
		return common_Service.bookTicket(from, to, routeId, session, map, seat);
	}

	@PostMapping("/confirm-order/{id}")
	public String confirmOrder(@PathVariable int id, @RequestParam String razorpay_payment_id, HttpSession session) {
		return common_Service.confirmOrder(id, razorpay_payment_id, session);
	}
}
