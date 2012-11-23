package it.azienda.dao;

import it.azienda.dto.AziendaDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AziendaDAO {

	PreparedStatement ps = null;
	
	public String inserimentoAzienda(AziendaDTO azienda, Connection conn){
		
		String sql = "insert into tbl_azienda (ragioneSociale,indirizzo,citta,provincia,cap,nazione,telefono,fax,mail,codiceFiscale,pIva,indirizzoLegale,cittaLegale,provinciaLegale,capLegale,nazioneLegale,referente,telefonoReferente,sito,trattamentoDati)" +
				"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		String messaggio = "";
		int esitoInserimentoAzienda = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, azienda.getRagioneSociale());
			ps.setString(2, azienda.getIndirizzo());
			ps.setString(3, azienda.getCitta());
			ps.setString(4, azienda.getProvincia());
			ps.setString(5, azienda.getCap());
			ps.setString(6, azienda.getNazione());
			ps.setString(7, azienda.getTelefono());
			ps.setString(8, azienda.getFax());
			ps.setString(9, azienda.getMail());
			ps.setString(10, azienda.getCodiceFiscale());
			ps.setString(11, azienda.getPIva());
			ps.setString(12, azienda.getIndirizzoLegale());
			ps.setString(13, azienda.getCittaLegale());
			ps.setString(14, azienda.getProvinciaLegale());
			ps.setString(15, azienda.getCapLegale());
			ps.setString(16, azienda.getNazioneLegale());
			ps.setString(17, azienda.getReferente());
			ps.setString(18, azienda.getTelefonoReferente());
			ps.setString(19, azienda.getSito());
			ps.setBoolean(20, azienda.isTrattamentoDati());
			esitoInserimentoAzienda = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
		}
		
		if(esitoInserimentoAzienda == 1){
			messaggio = "Registrazione avvenuta con successo";
		}
		
		return messaggio;
	}
	
	public String modificaAzienda(AziendaDTO azienda, Connection conn){
		
		String sql = "update tbl_aziende set ragione_sociale = ?,indirizzo = ?, citta = ?, provincia = ?, cap = ?, nazione = ?, telefono = ?, fax = ?, mail = ?, codice_fiscale = ?, p_iva = ?, indirizzo_legale = ?, citta_legale = ?, provincia_legale = ?, cap_legale = ?, nazione_legale = ?, referente = ?, telefono_referente = ?, sito = ?, trattamento_dati = ? where id_azienda = ?";
		
		String messaggio = "";
		int esitoInserimentoAzienda = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, azienda.getRagioneSociale());
			ps.setString(2, azienda.getIndirizzo());
			ps.setString(3, azienda.getCitta());
			ps.setString(4, azienda.getProvincia());
			ps.setString(5, azienda.getCap());
			ps.setString(6, azienda.getNazione());
			ps.setString(7, azienda.getTelefono());
			ps.setString(8, azienda.getFax());
			ps.setString(9, azienda.getMail());
			ps.setString(10, azienda.getCodiceFiscale());
			ps.setString(11, azienda.getPIva());
			ps.setString(12, azienda.getIndirizzoLegale());
			ps.setString(13, azienda.getCittaLegale());
			ps.setString(14, azienda.getProvinciaLegale());
			ps.setString(15, azienda.getCapLegale());
			ps.setString(16, azienda.getNazioneLegale());
			ps.setString(17, azienda.getReferente());
			ps.setString(18, azienda.getTelefonoReferente());
			ps.setString(19, azienda.getSito());
			ps.setBoolean(20, azienda.isTrattamentoDati());
			ps.setInt(21, azienda.getIdAzienda());
			esitoInserimentoAzienda = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
		}
		
		if(esitoInserimentoAzienda == 1){
			messaggio = "ok";
		}
		
		return messaggio;
	}
	
	/*
	 * effettuo il caricamento dell'idAzienda
	 */
	
	public int caricamentoIdAzienda(Connection conn){
		
		int idAzienda = 0;
		String sql = "select max(id_azienda) from tbl_azienda";
		
		try {
			ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				idAzienda = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idAzienda;
	}
	
	
	/*
	 * Con questo metodo effettuo il caricamento del profilo dell'azienda che al momento 
	 * è loggata
	 * 
	 */
	public AziendaDTO visualizzaProfiloAzienda(int idAzienda, Connection conn){
		
		String sql = "select * from tbl_aziende where id_azienda = ?";
		
		AziendaDTO azienda = null;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				azienda = new AziendaDTO();
				azienda.setIdAzienda(rs.getInt(1));
				azienda.setRagioneSociale(rs.getString(2));
				azienda.setIndirizzo(rs.getString(3));
				azienda.setCitta(rs.getString(4));
				azienda.setProvincia(rs.getString(5));
				azienda.setCap(rs.getString(6));
				azienda.setNazione(rs.getString(7));
				azienda.setTelefono(rs.getString(8));
				azienda.setFax(rs.getString(9));
				azienda.setMail(rs.getString(10));
				azienda.setCodiceFiscale(rs.getString(11));
				azienda.setPIva(rs.getString(12));
				azienda.setIndirizzoLegale(rs.getString(13));
				azienda.setCittaLegale(rs.getString(14));
				azienda.setProvinciaLegale(rs.getString(15));
				azienda.setCapLegale(rs.getString(16));
				azienda.setNazioneLegale(rs.getString(17));
				azienda.setReferente(rs.getString(18));
				azienda.setTelefonoReferente(rs.getString(19));
				azienda.setSito(rs.getString(20));
				azienda.setTrattamentoDati(rs.getBoolean(21));
				azienda.setVisible(rs.getBoolean(22));
				azienda.setAttivo(rs.getBoolean(23));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return azienda;
	}
	
	
	/*
	 * con questo meotodo effettuo l'eliminazione logica dell'Azienda
	 */
	public void eliminaProfiloAzienda(int idAzienda,Connection conn){
		
		String sql = "update tbl_login set utenteVisible = false where id_azienda = ?";
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * tramite questo metodo effettuo il cambiamento della password
	 */
	
	public String cambioPassword(String password,int idAzienda,Connection conn){
		
		String sql = "update tbl_login set password = ? where id_azienda = ?";
		
		int aggiornamentoPassword = 0;
		
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, MD5(password));
			ps.setInt(2, idAzienda);
			aggiornamentoPassword = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Siamo spiacenti, il cambio password non è avvenuto con successo. Contattare l'amministrazione.";
		}
		
		if(aggiornamentoPassword == 1){
			return "ok";
		}else{
			return "Siamo spiacenti, il cambio password non è avvenuto con successo. Contattare l'amministrazione.";
		}
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
