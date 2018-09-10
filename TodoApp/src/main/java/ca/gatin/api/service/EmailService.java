package ca.gatin.api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ca.gatin.model.security.PseudoUser;
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
	
	public void test() {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo("renat.gatin@gmail.com");
		mail.setFrom("emc2.software.lab@gmail.com");
		mail.setSubject("Test subject");
		mail.setText("Test mail body text. Timestamp: " + new Date());
		
		javaMailSender.send(mail);
	}
	
	public void sendActivationKey(PseudoUser user) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom("emc2.software.lab@gmail.com");
		mail.setSubject("Email Activation required");
		mail.setText("Hey, " + user.getFirstname() + "!" +
					 "\nPlease use this Key:" + 
				     "\n" + user.getActivationKey() + 
					 "\nto activate your account." + 
				     "\nOr click this link: " + "http://localhost:8080/todoapp/customer/#/sign-up?username=" + user.getEmail() + "&key=" + user.getActivationKey() +
				     "\nand follow the instuctions." + 
				     "\n\nTimestamp: " + new Date());
		
		javaMailSender.send(mail);
	}

}
