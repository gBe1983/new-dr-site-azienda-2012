package it.azienda.connessione;

import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

public class Connessione {
	
	Logger log = Logger.getLogger(Connessione.class);
	
	private Connection connection;
	private String url;
	private String userName;
	private String password;

	public Connessione(ServletContext servletContext) {
		
		log.info("metodo: costruttore Connessione");
		
		try {
			Class.forName(servletContext.getInitParameter("DB.driver"));
		} catch (ClassNotFoundException e) {
			log.error("errore sql: driver non caricati" + e);
		}
		url=servletContext.getInitParameter("DB.url");
		userName=servletContext.getInitParameter("DB.userName");
		password=servletContext.getInitParameter("DB.password");
		try {
			connection = DriverManager.getConnection(url,userName,password);
		} catch (SQLException e) {
			log.fatal("errore sql: connessione fallita" + e);
		}
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
		
		log.info("metodo: closeConnection");
		
		if (connection!=null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warn("errore sql: tentativo chiusura connessione db" + e);
			}
		}
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		
		log.info("metodo: getConnection");
		
		if(connection!=null){
			try {
				if(connection.isClosed()){
					log.info("la connessione è chiusa");
					try {
						connection = DriverManager.getConnection(url,userName,password);
					} catch (SQLException e) {
						log.error("errore sql: connessione fallita" + e);
					}
				}
				if(!connection.isValid(60)){
					log.info("la connessione non è valida");
					try {
						connection = DriverManager.getConnection(url,userName,password);
					} catch (SQLException e) {
						log.error("errore sql: connessione fallita"+e);
					}
				}
			} catch (SQLException e) {
				log.error("errore sql: verifica consistenza connessione"+ e);
			}
		}else{
			log.warn("errore sql: connessione null");
			try {
				connection = DriverManager.getConnection(url,userName,password);
			} catch (SQLException e) {
				log.error("errore sql: connessione fallita"+e);
			}
		}
		return connection;
	}
}