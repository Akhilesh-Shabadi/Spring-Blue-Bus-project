package com.bluebus.bus_project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.bluebus.bus_project.dao.Customer_Dao;
import com.bluebus.bus_project.dto.Customer;
import com.bluebus.bus_project.dto.TripOrder;
import com.bluebus.bus_project.helper.AES;
import com.bluebus.bus_project.helper.MailSending;

import jakarta.servlet.http.HttpSession;

@Service
public class Customer_Service {

	@Autowired
	Customer_Dao customer_Dao;

	@Autowired
	MailSending mailSending;

	public String signup(Customer customer, BindingResult bindingResult, HttpSession session) {
		if (customer_Dao.checkEmail(customer.getEmail()))
			bindingResult.rejectValue("email", "error.email", "* Email should be unique");
		if (customer_Dao.checkMobile(customer.getMobile()))
			bindingResult.rejectValue("mobile", "error.mobile", "* repected mobile number");
		if (!customer.getPassword().equals(customer.getCpassword())
				&& (customer.getPassword() != null || customer.getCpassword() != null))
			bindingResult.rejectValue("cpassword", "error.cpassword",
					"* Password and Confirm Password Should be Matching");
		if (!customer.getEmail().endsWith("@gmail.com"))
			bindingResult.rejectValue("email", "error.email", "* Email should with @gmail.com");
		if (!customer.getDob().isBefore(LocalDate.now()))
			bindingResult.rejectValue("dob", "eorror.dob", "* Incorrect Date of Birth");
		if (bindingResult.hasErrors())
			return "customer-signup";
		else {
			customer_Dao.deleteIfExists(customer);
			customer.setPassword(AES.encrypt(customer.getPassword(), "password"));
			customer.setOtp(new Random().nextInt(100000, 1000000));
			if (mailSending.sendEmail(customer)) {
				customer_Dao.save(customer);
				session.setAttribute("successMessage", "Otp Sent Success");
				return "redirect:/customer/send-otp/" + customer.getId() + "";
			} else {
				session.setAttribute("failMessage", "Sorry Not able to send OTP");
				return "redirect:/customer/signup";
			}
		}
	}

	public String verifyOtp(int id, int otp, HttpSession session) {
		Customer customer = customer_Dao.findById(id);
		if (customer.getOtp() == otp) {
			customer.setStatus(true);
			customer_Dao.save(customer);
			session.setAttribute("successMessage", "Otp Verified Success, You can Lgin Now");
			return "redirect:/login";
		} else {
			session.setAttribute("failMessage", "Invalid Otp, Try Again");
			return "redirect:/customer/send-otp/" + customer.getId() + "";
		}
	}

	public String resendOtp(int id, HttpSession session) {
		Customer customer = customer_Dao.findById(id);
		customer.setOtp(new Random().nextInt(100000, 1000000));
		if (mailSending.sendEmail(customer)) {
			customer_Dao.save(customer);
			session.setAttribute("successMessage", "Otp Re-Sent Success");
			return "redirect:/customer/send-otp/" + customer.getId() + "";
		} else {
			session.setAttribute("failMessage", "Failed to Send Otp");
			return "redirect:/customer/send-otp/" + customer.getId() + "";
		}
	}

	public String viewbookings(HttpSession session, ModelMap map) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer == null) {
			session.setAttribute("failMessage", "First Login to Book");
			return "redirect:/login";
		} else {
			List<TripOrder> orders = customer.getTripOrders();
			if (orders.isEmpty()) {
				session.setAttribute("failMessage", "No Bookings Yet");
				return "redirect:/";
			} else {
				map.put("orders", orders);
				return "view-bookings";
			}
		}
	}
}
