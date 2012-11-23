package it.azienda.connessione;

import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

public class Connessione {
	private MyLogger log;
	private Connection connection;
	private String url;
	private String userName;
	private String password;

	public Connessione(ServletContext servletContext) {
		log =new MyLogger(this.getClass());
		final String metodo="costruttore";
		log.start(metodo);
		try {
			Class.forName(servletContext.getInitParameter("DB.driver"));
		} catch (ClassNotFoundException e) {
			log.fatal(metodo, "driver non caricati", e);
		}
		url=servletContext.getInitParameter("DB.url");
		userName=servletContext.getInitParameter("DB.userName");
		password=servletContext.getInitParameter("DB.password");
		try {
			connection = DriverManager.getConnection(url,userName,password);
		} catch (SQLException e) {
			log.fatal(metodo, "connessione fallita", e);
		}
		log.end(metodo);
	}

	/**
	 * distruttore
	 */
	protected void finalize(){
		closeConnection();
	}

	/**
	 *  close the Connection
	 */
	@Deprecated
	public void closeConnection(){
		final String metodo="closeConnection";
		log.start(metodo);
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warn(metodo, "tentativo chiusura connessione db", e);
			}
		}
		log.end(metodo);
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		final String metodo="getConnection";
		log.start(metodo);
		if(connection!=null){
			try {
				if(connection.isClosed()){
					log.info(metodo, "la connessione è chiusa");
					try {
						connection = DriverManager.getConnection(url,userName,password);
					} catch (SQLException e) {
						log.fatal(metodo, "connessione fallita", e);
					}
				}
				if(!connection.isValid(60)){
					log.info(metodo, "la connessione non è valida");
					try {
						connection = DriverManager.getConnection(url,userName,password);
					} catch (SQLException e) {
						log.fatal(metodo, "connessione fallita", e);
					}
				}
			} catch (SQLException e) {
				log.error(metodo, "verifica consistenza connessione", e);
			}
		}else{
			log.warn(metodo, "connessione null");
			try {
				connection = DriverManager.getConnection(url,userName,password);
			} catch (SQLException e) {
				log.fatal(metodo, "connessione fallita", e);
			}
		}
		log.end(metodo);
		return connection;
	}
}