package it.azienda.dao;

import it.azienda.dto.UtenteDTO;
import it.util.log.MyLogger;
import it.util.password.MD5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class UtenteDAO extends BaseDao {
	private Logger log;

	public UtenteDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(UtenteDAO.class);
	}

	public String inserimentoUtente(UtenteDTO utente){
		
		log.info("metodo: inserimentoUtente");
		
		String sql = "INSERT INTO tbl_utenti(username,password,id_azienda,data_registrazione,data_login,id_risorsa)VALUES(?,?,?,?,?,?)";
		
		log.info("sql: INSERT INTO tbl_utenti(username,password,id_azienda,data_registrazione,data_login,id_risorsa)VALUES("+utente.getUsername()+","+MD5.encript(utente.getPassword())+","+utente.getId_azienda()+","+utente.getData_registrazione()+","+utente.getData_login()+","+utente.getId_risorsa()+")");
		
		int esito = 0; 
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, utente.getUsername());
			ps.setString(2, MD5.encript(utente.getPassword()));
			ps.setInt(3, utente.getId_azienda());
			ps.setString(4, utente.getData_registrazione());
			ps.setString(5, utente.getData_login());
			ps.setInt(6, utente.getId_risorsa());
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+e);
			return "Siamo spiacenti l'inserimento della risorsa non è avvenuto correttamente. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		return (esito == 1)?
			"ok":
			"Siamo spiacenti l'inserimento della risorsa non è avvenuto correttamente. Contattare l'amministrazione";
	}

	public int caricamentoIdRisorsa(){
		
		log.info("metodo: caricamentoIdRisorsa");
		
		String sql = "select max(id_risorsa) from tbl_risorse";
		
		log.info("sql: " + sql);
		
		int idRisorsa = 0;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			if(rs.next()){
				idRisorsa = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return idRisorsa;
	}

	public UtenteDTO caricamentoAzienda(int idAzienda){
		
		log.info("metodo: caricamentoAzienda");
		
		UtenteDTO utente = null;
		
		String sql = "select * from tbl_utenti where id_azienda = ?";//TODO *
		
		log.info("sql: select * from tbl_utenti where id_azienda = " + idAzienda);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			rs = ps.executeQuery();
			while(rs.next()){
				utente = new UtenteDTO();
				utente.setId_utente(rs.getInt(1));
				utente.setUsername(rs.getString(2));
				utente.setPassword(rs.getString(3));
				utente.setId_azienda(rs.getInt(4));
				utente.setData_registrazione(rs.getString(5));
				utente.setData_login(rs.getString(6));
				utente.setUtente_visible(rs.getBoolean(7));
				utente.setId_risorsa(rs.getInt(8));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return utente;
	}

	public UtenteDTO login(String username, String password){
		
		log.info("metodo: login");
		
		UtenteDTO utente = null;//TODO *
		String sql = "select login.* from tbl_utenti as login, tbl_azienda as azienda where login.username = ? and login.password = ? and login.id_azienda = azienda.id_azienda and azienda.attivo = true and login.utente_visible = true";
		
		log.info("sql: select login.* from tbl_utenti as login, tbl_azienda as azienda where login.username = "+username+" and login.password = "+password+" and login.id_azienda = azienda.id_azienda and azienda.attivo = true and login.utente_visible = true");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, MD5.encript(password));
			rs = ps.executeQuery();
			while(rs.next()){
				utente = new UtenteDTO();
				utente.setId_utente(rs.getInt(1));
				utente.setUsername(rs.getString(2));
				utente.setPassword(rs.getString(3));
				utente.setId_azienda(rs.getInt(4));
				utente.setData_registrazione(rs.getString(5));
				utente.setData_login(rs.getString(6));
				utente.setUtente_visible(rs.getBoolean(7));
				utente.setId_risorsa(rs.getInt(8));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return utente;
	}
}