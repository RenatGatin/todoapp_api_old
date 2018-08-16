package ca.gatin.api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ca.gatin.model.signup.PreSignupUser;

@Service
public class EmailService {
	
	private JavaMailSender javaMailSender;
	
	@Autowired
	private Environment env;
	
	@Autowired
	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	
	public void send(PreSignupUser user) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("renat.gatin@gmail.com");
		mail.setFrom("emc2.software.lab@gmail.com");
		mail.setSubject("Test subject");
		mail.setText("Test mail body text. Timestamp: " + new Date());
		
		javaMailSender.send(mail);
	}

}
