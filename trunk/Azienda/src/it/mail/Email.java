package it.mail;

import it.util.log.MyLogger;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletContext;

public class Email{
	private MyLogger log;
	private String protocol = null;
	private String host = null;
	private String port = null;
	private String folder = null;
	private String user = null;
	private String pass = null;
	private String debug = null;
	private String smtpAuth = null;
	private String startTls = null;
	private String socketFactoryClass = null;

	public Email(ServletContext servletContext) {
		log = new MyLogger(this.getClass());
		final String metodo = "costruttore";
		log.start(metodo);
		protocol = servletContext.getInitParameter("mail.protocol");
		host = servletContext.getInitParameter("mail.host");
		port = servletContext.getInitParameter("mail.port");
		user = servletContext.getInitParameter("mail.userName");
		pass = servletContext.getInitParameter("mail.password");
		folder = servletContext.getInitParameter("mail.folder");
		debug = servletContext.getInitParameter("mail.debug");
		smtpAuth = servletContext.getInitParameter("mail.smtp.auth");
		startTls = servletContext.getInitParameter("mail.smtp.starttls.enable");
		socketFactoryClass = servletContext.getInitParameter("mail.smtp.socketFactory.class");
		log.end(metodo);
	}

	public void sendMail(String dest, String oggetto, String testoEmail){
		final String metodo = "sendMail";
		log.start(metodo);
		try {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.debug", debug);
			props.put("mail.smtp.auth", smtpAuth);
			props.put("mail.smtp.starttls.enable", startTls);
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.socketFactory.class", socketFactoryClass);
			props.put("mail.smtp.port", port);
			Session session = Session.getDefaultInstance(props, null);
			session.setPasswordAuthentication(
				new URLName(protocol, host, Integer.parseInt(port), folder, user, pass),
				new PasswordAuthentication(user, pass));
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(user));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress( user));
			msg.setSubject(oggetto);
			msg.setContent(testoEmail, "text/html");
			Transport tr = session.getTransport(protocol);
			tr.connect(host, user, pass);
			msg.saveChanges();
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();
			log.debug(metodo, "mail inviata");
		} catch (MessagingException e) {
			log.error(metodo, "invio mail fallito", e);
		}finally{
			log.end(metodo);
		}
	}
}