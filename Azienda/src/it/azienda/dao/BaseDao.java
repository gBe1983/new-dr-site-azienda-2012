package it.azienda.dao;

import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class BaseDao {
	protected Connection connessione=null;
	private Logger log;

	public BaseDao(Connection connessione) {
		log= Logger.getLogger(BaseDao.class);
		this.connessione = connessione;
	}

	public void close(PreparedStatement ps,ResultSet rs){
		
		log.info("metodo: close");
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("errore sql: " + e);
			}
		}
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				log.error("errore sql: " + e);
			}
		}
	}

	public void close(PreparedStatement ps){
		close(ps,null);
	}
}
