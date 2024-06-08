package com.bluebus.bus_project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.bluebus.bus_project.dao.Agency_Dao;
import com.bluebus.bus_project.dao.Customer_Dao;
import com.bluebus.bus_project.dto.Agency;

@Service
public class Agency_Service {
	@Autowired
	Customer_Dao customerDao;

	@Autowired
	Agency_Dao agencyDao;

	public String signup(Agency agency, BindingResult result) {
		if (!agency.getPassword().equals(agency.getCpassword()))
			result.rejectValue("cpassword", "error.cpassword", "* Password and Confirm Password Should be Matching");
		if (agencyDao.checkEmail(agency.getEmail()) || customerDao.checkEmail(agency.getEmail()))
			result.rejectValue("email", "error.email", "* Email Should be Unique");
		if (agencyDao.checkMobile(agency.getMobile()) || customerDao.checkMobile(agency.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number Should be Unique");
		if (result.hasErrors())
			return "agency-signup";
		else
			return "home";

	}
}
