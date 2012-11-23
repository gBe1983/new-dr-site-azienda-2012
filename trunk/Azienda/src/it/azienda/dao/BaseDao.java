package it.azienda.dao;

import it.util.log.MyLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
	protected Connection connessione=null;
	private MyLogger log;

	public BaseDao(Connection connessione) {
		log=new MyLogger(this.getClass());
		this.connessione = connessione;
	}

	public void close(PreparedStatement ps,ResultSet rs){
		final String metodo="close";
		log.start(metodo);
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(metodo, "rs.close", e);
			}
		}
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				log.error(metodo, "ps.close", e);
			}
		}
		log.end(metodo);
	}

	public void close(PreparedStatement ps){
		close(ps,null);
	}
}
