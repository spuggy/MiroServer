package uk.co.bluetrail.mobriz.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * Class for sending e-mail messages based on Velocity templates or with
 * attachments.
 * 
 * <p>
 * <a href="MailEngine.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author Matt Raible
 */
public class MailEngine {
	protected static final Log log = LogFactory.getLog(MailEngine.class);

	private MailSender mailSender;

	private VelocityEngine velocityEngine;

	private String smsTo;

	private String smsPassword;

	private String smsAccount;

	private String smsAPI;

	private String smsBluetrailEmail;

	public String getSmsBluetrailEmail() {
		return smsBluetrailEmail;
	}

	public void setSmsBluetrailEmail(String smsBluetrailEmail) {
		this.smsBluetrailEmail = smsBluetrailEmail;
	}

	public String getSmsTo() {
		return smsTo;
	}

	public void setSmsTo(String smsTo) {
		this.smsTo = smsTo;
	}

	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}

	public String getSmsAccount() {
		return smsAccount;
	}

	public void setSmsAccount(String smsAccount) {
		this.smsAccount = smsAccount;
	}

	public String getSmsAPI() {
		return smsAPI;
	}

	public void setSmsAPI(String smsAPI) {
		this.smsAPI = smsAPI;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	/**
	 * Send a simple message based on a Velocity template.
	 * 
	 * @param msg
	 * @param templateName
	 * @param model
	 */
	public void sendMessage(SimpleMailMessage msg, String templateName,
			Map model) {
		String result = null;

		try {
			result = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, templateName, model);
		} catch (VelocityException e) {
			e.printStackTrace();
		}

		msg.setText(result);
		send(msg);
	}
	
	public void sendMessageWithExceptions(SimpleMailMessage msg, String templateName,
			Map model) throws Exception {
		String result = null;

		
			result = VelocityEngineUtils.mergeTemplateIntoString(
					velocityEngine, templateName, model);
		

		msg.setText(result);
		mailSender.send(msg);
		
	}
	

	/**
	 * Send a simple message with pre-populated values.
	 * 
	 * @param msg
	 */
	public void send(SimpleMailMessage msg) {
		try {
			mailSender.send(msg);
		} catch (MailException ex) {
			// log it and go on
			log.error(ex.getMessage());
		}
	}

	public void sendSMSMessage(String number, SimpleMailMessage message,
			String mess) {

		message.setTo(smsTo);
		message.setCc(this.getSmsBluetrailEmail());

		StringBuffer msg = new StringBuffer();
		msg.append("api_id:" + smsAPI);
		msg.append("\n");
		msg.append("user:" + smsAccount);
		msg.append("\n");
		msg.append("Password:" + smsPassword);
		msg.append("\n");
		msg.append("to:" + fullTrim(number));
		msg.append("\n");
		msg.append("text:" + mess);
		msg.append("\n");
		msg.append("reply:" + this.getSmsBluetrailEmail());
		msg.append("\n");

		System.out.println(msg.toString());

		message.setText(msg.toString());
		message.setSubject("SMSInstaller");

		log.info(msg);

		send(message);
	}

	private String fullTrim(String str) {

		StringBuffer newStr = new StringBuffer();
		char[] s = str.toCharArray();
		for (int i = 0; i < s.length; i++) {
			if (s[i] == ' ') {
				// do nowt it s a space
			} else {
				newStr.append(s[i]);
			}

		}

		return newStr.toString();
	}

	/**
	 * Convenience method for sending messages with attachments.
	 * 
	 * @param emailAddresses
	 * @param resource
	 * @param bodyText
	 * @param subject
	 * @param attachmentName
	 * @throws MessagingException
	 * @author Ben Gill
	 */
	public void sendMessage(String[] emailAddresses,
			ClassPathResource resource, String bodyText, String subject,
			String attachmentName) throws MessagingException {
		MimeMessage message = ((JavaMailSenderImpl) mailSender)
				.createMimeMessage();

		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(emailAddresses);
		helper.setText(bodyText);
		helper.setSubject(subject);

		helper.addAttachment(attachmentName, resource);

		((JavaMailSenderImpl) mailSender).send(message);
	}

	public void sendMessage(String fromEmail,String emailAddresses, String bodyText,
			String subject, ArrayList attachments) throws MessagingException {
		MimeMessage message = ((JavaMailSenderImpl) mailSender)
				.createMimeMessage();

		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(emailAddresses);
		helper.setText(bodyText);
		helper.setSubject(subject);
		helper.setFrom(fromEmail);

		File attachment = null;
		for (int i = 0; i < attachments.size(); i++) {
			attachment = (File) attachments.get(i);
			helper.addAttachment(attachment.getName(), attachment);
		}

		((JavaMailSenderImpl) mailSender).send(message);
	}
}
