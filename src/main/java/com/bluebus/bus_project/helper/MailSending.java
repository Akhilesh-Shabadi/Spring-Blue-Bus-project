package com.bluebus.bus_project.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bluebus.bus_project.dto.Agency;
import com.bluebus.bus_project.dto.Customer;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailSending {
	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	TemplateEngine engine;

	public boolean sendEmail(Agency agency) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom("akhileshshabadi@gmail.com", "BlueBus.com");
			helper.setTo(agency.getEmail());
			helper.setSubject("Account creation OTP");

			Context context = new Context();
			context.setVariable("agency", agency);

			String template = null;
			try {
				template = engine.process("agency-email-template", context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			helper.setText(template, true);

			javaMailSender.send(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean sendEmail(Customer customer) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		try {
			helper.setFrom("akhileshshabadi@gmail.com", "BlueBus.com");
			helper.setTo(customer.getEmail());
			helper.setSubject("Account creation OTP");

			Context context = new Context();
			context.setVariable("customer", customer);

			String template = null;
			try {
				template = engine.process("customer-email-template", context);
			} catch (Exception e) {
				e.printStackTrace();
			}
			helper.setText(template, true);

			javaMailSender.send(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
