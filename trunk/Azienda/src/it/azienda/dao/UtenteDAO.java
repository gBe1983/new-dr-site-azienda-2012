package it.azienda.dao;

import it.azienda.dto.UtenteDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteDAO {

	PreparedStatement ps = null;
	
	public String inserimentoUtente(UtenteDTO utente, Connection conn){
		
		String sql = "insert into tbl_utenti (username,password,id_azienda,data_registrazione,data_login,id_risorsa) values (?,?,?,?,?,?)";
		
		int esito = 0; 
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, utente.getUsername());
			ps.setString(2, MD5(utente.getPassword()));
			ps.setInt(3, utente.getId_azienda());
			ps.setString(4, utente.getData_registrazione());
			ps.setString(5, utente.getData_login());
			ps.setInt(6, utente.getId_risorsa());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti l'inserimento della risorsa non è avvenuto correttamente. Contattare l'amministrazione";
		}
		
		if(esito == 1){
			return "ok";
		}else{
			return "Siamo spiacenti l'inserimento della risorsa non è avvenuto correttamente. Contattare l'amministrazione";
		}
	}
	
	public int caricamentoIdRisorsa(Connection conn){
		
		String sql = "select max(id_risorsa) from tbl_risorse";
		
		int idRisorsa = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				idRisorsa = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idRisorsa;
	}
	
	public UtenteDTO caricamentoAzienda(int idAzienda, Connection conn){
		
		UtenteDTO utente = null;
		
		String sql = "select * from tbl_utenti where id_azienda = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			ResultSet rs = ps.executeQuery();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return utente;
	}
	
	public UtenteDTO login(String username, String password, Connection conn){
		
		UtenteDTO utente = null;
		
		String sql = "select login.* from tbl_utenti as login, tbl_azienda as azienda where login.username = ? and login.password = ? and login.id_azienda = azienda.id_azienda and azienda.attivo = true and login.utente_visible = true";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, MD5(password));
			ResultSet rs = ps.executeQuery();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return utente;
	}
	
	
	public String MD5(String md5) {
		
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	        }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    	
	    }
	    return null;
	}
}
