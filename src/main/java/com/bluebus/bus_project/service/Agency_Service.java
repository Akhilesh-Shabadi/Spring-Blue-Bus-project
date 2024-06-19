package com.bluebus.bus_project.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.bluebus.bus_project.dao.Agency_Dao;
import com.bluebus.bus_project.dao.Customer_Dao;
import com.bluebus.bus_project.dto.Agency;
import com.bluebus.bus_project.dto.Bus;
import com.bluebus.bus_project.helper.AES;
import com.bluebus.bus_project.helper.MailSending;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import jakarta.servlet.http.HttpSession;

@Service
public class Agency_Service {
	@Autowired
	Customer_Dao customerDao;

	@Autowired
	Agency_Dao agencyDao;

	@Autowired
	MailSending mailSending;

	public String signup(Agency agency, BindingResult result, HttpSession session) {
		if (!agency.getPassword().equals(agency.getCpassword()))
			result.rejectValue("cpassword", "error.cpassword", "* Password and Confirm Password Should be Matching");
		if (agencyDao.checkEmail(agency.getEmail()) || customerDao.checkEmail(agency.getEmail()))
			result.rejectValue("email", "error.email", "* Email Should be Unique");
		if (agencyDao.checkMobile(agency.getMobile()) || customerDao.checkMobile(agency.getMobile()))
			result.rejectValue("mobile", "error.mobile", "* Mobile Number Should be Unique");
		if (result.hasErrors())
			return "agency-signup";
		else {
			agencyDao.deleteIfExists(agency);
//			agency.setCpassword(AES.encrypt(agency.getCpassword(), "password"));
			agency.setPassword(AES.encrypt(agency.getPassword(), "password"));
			agency.setOtp(new Random().nextInt(100000, 1000000));
			if (mailSending.sendEmail(agency)) {
				agencyDao.save(agency);
				session.setAttribute("successMessage", "Otp Sent Success");
				return "redirect:/agency/send-otp/" + agency.getId() + "";
			} else {
				session.setAttribute("failMessage", "Sorry Not able to send OTP");
				return "redirect:/agency/signup";
			}
		}
	}

	public String verifyOtp(int id, int otp, HttpSession session) {
		Agency agency = agencyDao.findById(id);
		if (agency.getOtp() == otp) {
			agency.setStatus(true);
			agencyDao.save(agency);
			session.setAttribute("successMessage", "Otp Verified Success, You can Lgin Now");
			return "redirect:/login";
		} else {
			session.setAttribute("failMessage", "Invalid Otp, Try Again");
			return "redirect:/agency/send-otp/" + agency.getId() + "";
		}
	}

	public String resendOtp(int id, HttpSession session) {
		Agency agency = agencyDao.findById(id);
		agency.setOtp(new Random().nextInt(100000, 1000000));
		if (mailSending.sendEmail(agency)) {
			agencyDao.save(agency);
			session.setAttribute("successMessage", "Otp Re-Sent Success");
			return "redirect:/agency/send-otp/" + agency.getId() + "";
		} else {
			session.setAttribute("failMessage", "Failed to Send Otp");
			return "redirect:/agency/send-otp/" + agency.getId() + "";
		}
	}

	public String addBus(Bus bus, MultipartFile image, HttpSession session) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			bus.setImageLink(addToCloudinary(image));
			agency.getBuses().add(bus);
			agencyDao.save(agency);
			session.setAttribute("agency", agencyDao.findById(agency.getId()));
			session.setAttribute("successMessage", "Bus Added Success");
			return "redirect:/";
		}
	}

	public String addToCloudinary(MultipartFile image) {
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dvx4er6bo", "api_key",
				"425547426345111", "api_secret", "hIGse_HSel3hGCCMlToxpBKD_98", "secure", true));
		Map resume = null;
		try {
			Map<String, Object> uploadOptions = new HashMap<String, Object>();
			uploadOptions.put("folder", "Bus");
			resume = cloudinary.uploader().upload(image.getBytes(), uploadOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) resume.get("url");
	}
}
