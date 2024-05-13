package com.bpl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;



@Component
@Slf4j
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;
	
	public boolean sendEmail(String to,String subject,String body) {
		boolean isMailSent=false;
		try {
			MimeMessage mimemessage =mailSender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(mimemessage);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true);
			mailSender.send( mimemessage );
			isMailSent=true;
		} catch (Exception e) {
			log.error("Exception Occured", e);
			//e.printStackTrace();
		}
		return isMailSent;
	}
}
