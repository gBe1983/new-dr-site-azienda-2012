package it.azienda.schedulatore;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Schedulatore implements Job{

	public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
		sendMail("emmanuel.devita@gmail.com", "test", "questa è una prova");
	}
	
	public void sendMail(String dest, String oggetto, String testoEmail){
		
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.debug", false);
			props.put("mail.smtp.auth", true);
			props.put("mail.smtp.starttls.enable", true);
			props.put("mail.smtp.socketFactory.port", 465);
			props.put("mail.smtp.socketFactory.class", javax.net.ssl.SSLSocketFactory.class);
			props.put("mail.smtp.port", 465);
			Session session = Session.getDefaultInstance(props, null);
			session.setPasswordAuthentication(
				new URLName("smtp", "smtp.gmail.com", 465, "INBOX", "info.drconsulting@gmail.com", "R0bert01"),
				new PasswordAuthentication("info.drconsulting@gmail.com", "R0bert01"));
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("info.drconsulting@gmail.com"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress( "info.drconsulting@gmail.com"));
			msg.setSubject(oggetto);
			msg.setContent(testoEmail, "text/html");
			Transport tr = session.getTransport("smtp");
			tr.connect("smtp.gmail.com", "info.drconsulting@gmail.com", "R0bert01");
			msg.saveChanges();
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
			System.out.println("mail inviata");
		} catch (MessagingException e) {
			System.out.println("invio mail fallito " + e);
		}
	}
}
