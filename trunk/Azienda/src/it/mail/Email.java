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

import org.apache.log4j.Logger;

public class Email{
	private MyLogger log;

	public Email() {
		log = new MyLogger(this.getClass());
	}

	private final static String host = "smtp.gmail.com";
	//private final static String ccn = "roberto@dierreconsulting.com";
	private final static String user = "info.drconsulting@gmail.com";
	private final static String pass = "R0bert01";

	static Logger logger = Logger.getLogger(Email.class);

	public void sendMail(String dest, String mitt, String oggetto, String testoEmail){
		final String metodo = "inserimentoEsperienze";
		log.start(metodo);
		try {
			Properties props = System.getProperties();
			// Setup mail servera
			props.put("mail.smtp.host", host);
			props.put("mail.debug", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.port", "465");
			// Session
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);
			session.setPasswordAuthentication(new URLName("smtp", host, 465, "INBOX", user, pass), new PasswordAuthentication(user, pass));

			// Inizializzo il messaggio
			MimeMessage msg = new MimeMessage(session);
			// Setto l'indirizzo del mittente
			msg.setFrom(new InternetAddress(mitt));
			// Setto gli indirizzi (destinatario e ccn)
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress( mitt));
			// Setto l'oggetto
			msg.setSubject(oggetto);
			// Setto il corpo del testo
			msg.setContent(testoEmail, "text/html");
			// Invio messaggio
			Transport tr = session.getTransport("smtp");
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