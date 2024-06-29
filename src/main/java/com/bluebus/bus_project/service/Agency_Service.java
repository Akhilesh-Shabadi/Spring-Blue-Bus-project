package com.bluebus.bus_project.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.bluebus.bus_project.dao.Agency_Dao;
import com.bluebus.bus_project.dao.Customer_Dao;
import com.bluebus.bus_project.dto.Agency;
import com.bluebus.bus_project.dto.Bus;
import com.bluebus.bus_project.dto.Route;
import com.bluebus.bus_project.dto.Station;
import com.bluebus.bus_project.helper.AES;
import com.bluebus.bus_project.helper.MailSending;
import com.bluebus.bus_project.repository.Bus_Repository;
import com.bluebus.bus_project.repository.Route_Repository;
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

	@Autowired
	Bus_Repository busRepository;

	@Autowired
	Route_Repository routeRepository;

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
			agency.setCpassword(AES.encrypt(agency.getCpassword(), "password"));
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
		Map<?, ?> resume = null;
		try {
			Map<String, Object> uploadOptions = new HashMap<String, Object>();
			uploadOptions.put("folder", "Bus");
			resume = cloudinary.uploader().upload(image.getBytes(), uploadOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (String) resume.get("url");
	}

	public String addRoute(HttpSession session, ModelMap map) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			List<Bus> buses = agency.getBuses();
			if (buses.isEmpty()) {
				session.setAttribute("failMessage", "First Add a Bus");
				return "redirect:/";
			} else {
				map.put("buses", buses);
				return "add-route";
			}
		}
	}

	public String addRoute(Route route, HttpSession session) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			Bus bus = busRepository.findById(route.getBus().getId()).orElse(null);
			for (Station station : route.getStations()) {
				station.setRoute(route);
			}
			route.setBus(bus);
			route.setAgency(agency);
			routeRepository.save(route);
			bus.getRoutes().add(route);
			busRepository.save(bus);
			session.setAttribute("agency", agencyDao.findById(agency.getId()));
			session.setAttribute("successMessage", "Route Added Success");
			return "redirect:/";
		}
	}

	public String fetchRoutes(HttpSession session, ModelMap map) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			List<Bus> buses = agency.getBuses();
			if (buses.isEmpty()) {
				session.setAttribute("failMessage", "No Routes Added Yet");
				return "redirect:/";
			} else {
				List<Integer> list = buses.stream().mapToInt(x -> x.getId()).boxed().collect(Collectors.toList());
				List<Route> routes = routeRepository.findByBus_idIn(list);
				if (routes.isEmpty()) {
					session.setAttribute("failMessage", "No Routes Added Yet");
					return "redirect:/";
				} else {
					map.put("routes", routes);
					return "fetch-route";
				}
			}
		}
	}

	public String deleteRoute(int id, HttpSession session) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			Bus bus = routeRepository.findById(id).orElseThrow().getBus();
			Route route = null;
			for (Route route1 : bus.getRoutes()) {
				if (route1.getId() == id) {
					route = route1;
					break;
				}
			}
			bus.getRoutes().remove(route);
			busRepository.save(bus);
			routeRepository.delete(route);
			session.setAttribute("agency", agencyDao.findById(agency.getId()));
			session.setAttribute("successMessage", "Route Removed Success");
			return "redirect:/agency/manage-route";
		}
	}

	public String editRoute(int id, HttpSession session, ModelMap map) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			Route route = routeRepository.findById(id).orElseThrow();
			map.put("route", route);
			return "edit-route";
		}
	}

	public String fetchBus(HttpSession session, ModelMap map) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			List<Bus> buses = agency.getBuses();
			if (buses.isEmpty()) {
				session.setAttribute("failMessage", "No Bus Added Yet");
				return "redirect:/";
			} else {
				map.put("buses", buses);
				return "fetch-bus";
			}
		}
	}

	public String deleteBus(int id, HttpSession session) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			Bus bus = null;
			for (Bus bus1 : agency.getBuses()) {
				if (bus1.getId() == id) {
					bus = bus1;
					break;
				}
			}
			agency.getBuses().remove(bus);
			agencyDao.save(agency);
			busRepository.delete(bus);
			session.setAttribute("agency", agencyDao.findById(agency.getId()));
			session.setAttribute("successMessage", "Bus Removed Success");
			return "redirect:/agency/manage-bus";
		}
	}

	public String editBus(int id, HttpSession session, ModelMap map) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			Bus bus = busRepository.findById(id).orElseThrow();
			map.put("bus", bus);
			return "edit-bus";
		}
	}

	public String editBus(Bus bus, MultipartFile image, HttpSession session) {
		Agency agency = (Agency) session.getAttribute("agency");
		if (agency == null) {
			session.setAttribute("failMessage", "Invalid Session");
			return "redirect:/";
		} else {
			try {
				if (image.getInputStream().available() != 0) {
					bus.setImageLink(addToCloudinary(image));
				} else {
					bus.setImageLink(busRepository.findById(bus.getId()).orElseThrow().getImageLink());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			busRepository.save(bus);
			session.setAttribute("agency", agencyDao.findById(agency.getId()));
			session.setAttribute("successMessage", "Bus Updated Success");
			return "redirect:/agency/manage-bus";
		}
	}
}
