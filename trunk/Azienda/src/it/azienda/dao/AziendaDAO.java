package it.azienda.dao;

import it.azienda.dto.AziendaDTO;
import it.util.log.MyLogger;
import it.util.password.MD5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AziendaDAO extends BaseDao {
	private MyLogger log;

	public AziendaDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	public String inserimentoAzienda(AziendaDTO azienda){
		final String metodo="inserimentoAzienda";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("INSERT INTO tbl_azienda");
		sql	.append("(ragioneSociale,indirizzo,citta,provincia,cap,nazione,telefono,fax,mail,")
				.append("codiceFiscale,pIva,indirizzoLegale,cittaLegale,provinciaLegale,")
				.append("capLegale,nazioneLegale,referente,telefonoReferente,sito,trattamentoDati)")
				.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		log.debug(metodo, sql.toString());
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
			log.error(metodo, "INSERT INTO tbl_azienda", e);
			return "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (esitoInserimentoAzienda == 1)?
			"Registrazione avvenuta con successo":
			"";
	}

	public String modificaAzienda(AziendaDTO azienda){
		final String metodo="modificaAzienda";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("UPDATE tbl_aziende ");
		sql	.append("SET ragione_sociale=?,indirizzo=?,citta=?,provincia=?,cap=?,nazione=?,")
				.append("telefono=?,fax=?,mail=?,codice_fiscale=?,p_iva=?,indirizzo_legale=?,")
				.append("citta_legale=?,provincia_legale=?,cap_legale=?,nazione_legale=?,referente=?,")
				.append("telefono_referente=?,sito=?,trattamento_dati=? ")
				.append("WHERE id_azienda=?");
		log.debug(metodo, sql.toString());
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
			log.error(metodo, "UPDATE tbl_aziende", e);
			return "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
			log.end(metodo);
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
		final String metodo="caricamentoIdAzienda";
		log.start(metodo);
		int idAzienda = 0;
		String sql = "SELECT MAX(id_azienda)max_id_azienda FROM tbl_azienda";
		log.debug(metodo, sql);
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				idAzienda = rs.getInt("max_id_azienda");
			}
		} catch (SQLException e) {
			log.error(metodo, "SELECT MAX(id_azienda) tbl_azienda", e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return idAzienda;
	}

	/**
	 * Con questo metodo effettuo il caricamento del profilo dell'azienda che al momento è loggata
	 * @param idAzienda
	 * @return
	 */
	public AziendaDTO visualizzaProfiloAzienda(int idAzienda){
		final String metodo="visualizzaProfiloAzienda";
		log.start(metodo);
		StringBuilder sql = new StringBuilder("SELECT id_azienda,ragione_sociale,indirizzo,citta,");
		sql	.append("provincia,cap,nazione,telefono,fax,mail,codice_fiscale,p_iva,indirizzo_legale,")
				.append("citta_legale,provincia_legale,cap_legale,nazione_legale,referente,")
				.append("telefono_referente,sito,trattamento_dati,visible,attivo ")
				.append("FROM tbl_aziende ")
				.append("WHERE id_azienda=?");
		log.debug(metodo, sql.toString());
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
			log.error(metodo, "SELECT tbl_aziende for idAzienda:"+idAzienda, e);
		}finally{
			close(ps,rs);
			log.end(metodo);
		}
		return azienda;
	}

	/**
	 * con questo meotodo effettuo l'eliminazione logica dell'Azienda
	 * @param idAzienda
	 */
	public void eliminaProfiloAzienda(int idAzienda){
		final String metodo="eliminaProfiloAzienda";
		log.start(metodo);
		String sql = "UPDATE tbl_utente SET utenteVisible=false WHERE id_azienda=?";
		log.debug(metodo, sql);
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idAzienda);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_utente for idAzienda:"+idAzienda, e);
		}finally{
			close(ps);
			log.end(metodo);
		}
	}

	/**
	 * tramite questo metodo effettuo il cambiamento della password
	 * @param password
	 * @param idAzienda
	 * @return
	 */
	public String cambioPassword(String password,int id_utente){
		final String metodo="cambioPassword";
		log.start(metodo);
		String sql = "UPDATE tbl_utente SET password=? WHERE id_utente = ?";
		log.debug(metodo, sql);
		int aggiornamentoPassword = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, MD5.encript(password));
			ps.setInt(2, id_utente);
			aggiornamentoPassword = ps.executeUpdate();
		} catch (SQLException e) {
			log.error(metodo, "UPDATE tbl_utente for id_utente :"+id_utente, e);
			return "Siamo spiacenti, il cambio password non è avvenuto con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
			log.end(metodo);
		}
		return (aggiornamentoPassword == 1)?
			"ok":
			"Siamo spiacenti, il cambio password non è avvenuto con successo. Contattare l'amministrazione.";
	}
}