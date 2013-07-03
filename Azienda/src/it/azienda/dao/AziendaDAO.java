package it.azienda.dao;

import it.azienda.dto.AziendaDTO;
import it.util.log.MyLogger;
import it.util.password.MD5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class AziendaDAO extends BaseDao {
	private Logger log;

	public AziendaDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(AziendaDAO.class);
	}

	public String inserimentoAzienda(AziendaDTO azienda){
		
		log.info("metodo: inserimentoAzienda");
		
		StringBuilder sql = new StringBuilder("INSERT INTO tbl_azienda");
		sql	.append("(ragioneSociale,indirizzo,citta,provincia,cap,nazione,telefono,fax,mail,")
				.append("codiceFiscale,pIva,indirizzoLegale,cittaLegale,provinciaLegale,")
				.append("capLegale,nazioneLegale,referente,telefonoReferente,sito,trattamentoDati)")
				.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		log.info("sql: INSERT INTO tbl_azienda (ragioneSociale,indirizzo,citta,provincia,cap,nazione,telefono,fax,mail,codiceFiscale,pIva,indirizzoLegale,cittaLegale,provinciaLegale," +
				"capLegale,nazioneLegale,referente,telefonoReferente,sito,trattamentoDati) VALUES("+azienda.getRagioneSociale()+","+azienda.getIndirizzo()+","+azienda.getCitta()+","+azienda.getProvincia()+"," +
				""+azienda.getCap()+","+azienda.getNazione()+","+azienda.getTelefono()+","+azienda.getFax()+","+azienda.getMail()+","+azienda.getCodiceFiscale()+","+azienda.getPIva()+"," +
				""+azienda.getIndirizzoLegale()+","+azienda.getCittaLegale()+","+azienda.getProvinciaLegale()+","+azienda.getCapLegale()+","+azienda.getNazioneLegale()+","+azienda.getReferente()+"," +
				""+azienda.getTelefonoReferente()+","+azienda.getSito()+","+azienda.isTrattamentoDati()+")");
		
		int esitoInserimentoAzienda = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
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
			log.error("errore sql: " + e);
			return "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		return (esitoInserimentoAzienda == 1)?
			"Registrazione avvenuta con successo":
			"";
	}

	public String modificaAzienda(AziendaDTO azienda){
		
		log.info("metodo: modificaAzienda");
		StringBuilder sql = new StringBuilder("UPDATE tbl_aziende ");
		sql	.append("SET ragione_sociale=?,indirizzo=?,citta=?,provincia=?,cap=?,nazione=?,")
				.append("telefono=?,fax=?,mail=?,codice_fiscale=?,p_iva=?,indirizzo_legale=?,")
				.append("citta_legale=?,provincia_legale=?,cap_legale=?,nazione_legale=?,referente=?,")
				.append("telefono_referente=?,sito=?,trattamento_dati=? ")
				.append("WHERE id_azienda=?");
		
		log.info("sql: UPDATE tbl_aziende SET ragione_sociale="+azienda.getRagioneSociale()+",indirizzo="+azienda.getIndirizzo()+",citta="+azienda.getCitta()+",provincia="+azienda.getProvincia()+",cap="+azienda.getCap()+",nazione="+azienda.getNazione()+",telefono="+azienda.getTelefono()+",fax="+azienda.getFax()+",mail="+azienda.getMail()+",codice_fiscale="+azienda.getCodiceFiscale()+"," +
				"p_iva="+azienda.getPIva()+",indirizzo_legale="+azienda.getIndirizzoLegale()+",citta_legale="+azienda.getCittaLegale()+",provincia_legale="+azienda.getProvinciaLegale()+"," +
						"cap_legale="+azienda.getCapLegale()+",nazione_legale="+azienda.getNazioneLegale()+",referente="+azienda.getReferente()+",telefono_referente="+azienda.getTelefonoReferente()+",sito="+azienda.getSito()+",trattamento_dati="+azienda.isTrattamentoDati()+" WHERE id_azienda="+azienda.getIdAzienda());
		
		int esitoAggiornametoAzienda = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
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
			esitoAggiornametoAzienda = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
			return "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
		return (esitoAggiornametoAzienda == 1)?
			"ok":
			"";
	}

	/**
	 * effettuo il caricamento dell'idAzienda
	 * @return
	 */
	public int caricamentoIdAzienda(){
		
		log.info("metodo: caricamentoIdAzienda");
		int idAzienda = 0;
		String sql = "SELECT MAX(id_azienda)max_id_azienda FROM tbl_azienda";
		
		log.info("sql: "+sql);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				idAzienda = rs.getInt("max_id_azienda");
			}
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		return idAzienda;
	}

	/**
	 * Con questo metodo effettuo il caricamento del profilo dell'azienda che al momento è loggata
	 * @param idAzienda
	 * @return
	 */
	public AziendaDTO visualizzaProfiloAzienda(int idAzienda){
		
		log.info("metodo: visualizzaProfiloAzienda");
		
		StringBuilder sql = new StringBuilder("SELECT id_azienda,ragione_sociale,indirizzo,citta,");
		sql	.append("provincia,cap,nazione,telefono,fax,mail,codice_fiscale,p_iva,indirizzo_legale,")
				.append("citta_legale,provincia_legale,cap_legale,nazione_legale,referente,")
				.append("telefono_referente,sito,trattamento_dati,visible,attivo ")
				.append("FROM tbl_aziende ")
				.append("WHERE id_azienda=?");
		
		log.info("sql: SELECT id_azienda,ragione_sociale,indirizzo,citta,provincia,cap,nazione,telefono,fax,mail,codice_fiscale,p_iva,indirizzo_legale," +
				" citta_legale,provincia_legale,cap_legale,nazione_legale,referente," +
				" telefono_referente,sito,trattamento_dati,visible,attivo " +
				" FROM tbl_aziende WHERE id_azienda="+idAzienda);
		
		AziendaDTO azienda = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql.toString());
			ps.setInt(1, idAzienda);
			rs = ps.executeQuery();
			if(rs.next()){
				azienda = new AziendaDTO();
				azienda.setIdAzienda(rs.getInt("id_azienda"));
				azienda.setRagioneSociale(rs.getString("ragione_sociale"));
				azienda.setIndirizzo(rs.getString("indirizzo"));
				azienda.setCitta(rs.getString("citta"));
				azienda.setProvincia(rs.getString("provincia"));
				azienda.setCap(rs.getString("cap"));
				azienda.setNazione(rs.getString("nazione"));
				azienda.setTelefono(rs.getString("telefono"));
				azienda.setFax(rs.getString("fax"));
				azienda.setMail(rs.getString("mail"));
				azienda.setCodiceFiscale(rs.getString("codice_fiscale"));
				azienda.setPIva(rs.getString("p_iva"));
				azienda.setIndirizzoLegale(rs.getString("indirizzo_legale"));
				azienda.setCittaLegale(rs.getString("citta_legale"));
				azienda.setProvinciaLegale(rs.getString("provincia_legale"));
				azienda.setCapLegale(rs.getString("cap_legale"));
				azienda.setNazioneLegale(rs.getString("nazione_legale"));
				azienda.setReferente(rs.getString("referente"));
				azienda.setTelefonoReferente(rs.getString("telefono_referente"));
				azienda.setSito(rs.getString("sito"));
				azienda.setTrattamentoDati(rs.getBoolean("trattamento_dati"));
				azienda.setVisible(rs.getBoolean("visible"));
				azienda.setAttivo(rs.getBoolean("attivo"));
			}
		} catch (SQLException e) {
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return azienda;
	}

	/**
	 * con questo meotodo effettuo l'eliminazione logica dell'Azienda
	 * @param idAzienda
	 */
	public void eliminaProfiloAzienda(int idAzienda){
		
		log.info("metodo: eliminaProfiloAzienda");
		
		String sql = "UPDATE tbl_utente SET utenteVisible=false WHERE id_azienda=?";
		
		log.info("sql: UPDATE tbl_utente SET utenteVisible=false WHERE id_azienda="+idAzienda);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
	}

	public int loginEditor(String username, String password){
		
		final String metodo="loginEditor";
		log.info("metodo: loginEditor");
		String sql = "select id_azienda from tbl_utenti where username = ? and password = ?";
		
		log.info("sql: select id_azienda from tbl_utenti where username = "+username+" and password = "+MD5.encript(password));
		PreparedStatement ps=null;
		
		int id_azienda = 0;
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, MD5.encript(password));
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id_azienda = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}
		
		return id_azienda;
	}
	/**
	 * tramite questo metodo effettuo il cambiamento della password
	 * @param password
	 * @param idAzienda
	 * @return
	 */
	public String cambioPassword(String password,int id_utente){
		
		log.info("metodo: cambioPassword");
		String sql = "UPDATE tbl_utente SET password=? WHERE id_utente = ?";
		
		log.info("sql: UPDATE tbl_utente SET password=? WHERE id_utente = "+sql);
		int aggiornamentoPassword = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, MD5.encript(password));
			ps.setInt(2, id_utente);
			aggiornamentoPassword = ps.executeUpdate();
		} catch (SQLException e) {
			log.error("errore sql: " + e);
			return "Siamo spiacenti, il cambio password non è avvenuto con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		return (aggiornamentoPassword == 1)?
			"ok":
			"Siamo spiacenti, il cambio password non è avvenuto con successo. Contattare l'amministrazione.";
	}
}