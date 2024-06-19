package com.bluebus.bus_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluebus.bus_project.dao.Agency_Dao;
import com.bluebus.bus_project.dao.Customer_Dao;
import com.bluebus.bus_project.dto.Agency;
import com.bluebus.bus_project.dto.Customer;
import com.bluebus.bus_project.helper.AES;

import jakarta.servlet.http.HttpSession;

@Service
public class Common_Service {
	@Autowired
	Customer_Dao customer_Dao;

	@Autowired
	Agency_Dao agency_Dao;

	public String signup(String role) {
		if (role.equals("customer"))
			return "redirect:/customer/signup";
		else
			return "redirect:/agency/signup";
	}

	public String login(String emph, String password, HttpSession session) {
		Agency agency = null;
		Customer customer = null;
		try {
			long mobile = Long.parseLong(emph);
			customer = customer_Dao.findByMobile(mobile);
			agency = agency_Dao.findByMobile(mobile);
		} catch (NumberFormatException e) {
			String email = emph;
			customer = customer_Dao.findByEmail(email);
			agency = agency_Dao.findByEmail(email);
		}
		if (customer == null && agency == null) {
			session.setAttribute("failMessage", "Invalid Email or Phone");
			return "redirect:/login";
		} else {
			if (customer == null) {
				if (AES.decrypt(agency.getPassword(), "password").equals(password)) {
					session.setAttribute("agency", agency);
					session.setAttribute("successMessage", "Login Success");
					return "redirect:/";
				} else {
					session.setAttribute("failMessage", "Invalid Password");
					return "redirect:/login";
				}
			} else {
				if (AES.decrypt(customer.getPassword(), "password").equals(password)) {
					session.setAttribute("customer", customer);
					session.setAttribute("successMessage", "Login Success");
					return "redirect:/";
				} else {
					session.setAttribute("failMessage", "Invalid Password");
					return "redirect:/login";
				}
			}
		}

	}
}
