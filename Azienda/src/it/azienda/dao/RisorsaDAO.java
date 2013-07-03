package it.azienda.dao;

import it.azienda.dto.RisorsaDTO;
import it.azienda.dto.UtenteDTO;
import it.util.log.MyLogger;
import it.util.password.MD5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class RisorsaDAO extends BaseDao {
	private Logger log;

	public RisorsaDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(RisorsaDAO.class);
	}

	/*
	 * con questo metodo effettuo l'inserimento della risorsa 
	 * nella tabella tbl_risorse
	 */
	
	public String inserimentoRisorsa(RisorsaDTO risorsa){
		
		log.info("metodo: inserimentoRisorsa");
		
		String messaggio = "";
		
		int esitoInserimentoRisorsa = 0;
		
		String sql = "insert into tbl_risorse (cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono,cellulare,fax,indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato,tipo_contratto,figura_professionale,seniority) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		log.info("sql: insert into tbl_risorse (cognome,nome,data_nascita,luogo_nascita,sesso,cod_fiscale,mail,telefono,cellulare,fax, " +
				"indirizzo,citta,provincia,cap,nazione,servizio_militare,patente,costo,occupato,tipo_contratto,figura_professionale,seniority) values ("
				+risorsa.getCognome()+","+risorsa.getNome()+","+risorsa.getDataNascita()+","+risorsa.getLuogoNascita()+","+risorsa.getSesso()+","+risorsa.getCodiceFiscale()+","
				+risorsa.getEmail()+","+risorsa.getTelefono()+","+risorsa.getCellulare()+","+risorsa.getFax()+","+risorsa.getIndirizzo()+","+risorsa.getCitta()+","+risorsa.getProvincia()+","+risorsa.getCap()+","
				+risorsa.getNazione()+","+risorsa.getServizioMilitare()+","+risorsa.getPatente()+","+risorsa.getCosto()+","+risorsa.isOccupato()+","+risorsa.getTipoContratto()+","
				+risorsa.getFiguraProfessionale()+","+risorsa.getSeniority()+")");
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			
			//dati anagrafici
			ps.setString(1, risorsa.getCognome());
			ps.setString(2, risorsa.getNome());
			ps.setString(3, risorsa.getDataNascita());
			ps.setString(4, risorsa.getLuogoNascita());
			ps.setString(5, risorsa.getSesso());
			ps.setString(6, risorsa.getCodiceFiscale());
			ps.setString(7, risorsa.getEmail());
			ps.setString(8, risorsa.getTelefono());
			ps.setString(9, risorsa.getCellulare());
			ps.setString(10, risorsa.getFax());
			
			//residenza
			ps.setString(11, risorsa.getIndirizzo());
			ps.setString(12, risorsa.getCitta());
			ps.setString(13, risorsa.getProvincia());
			ps.setString(14, risorsa.getCap());
			ps.setString(15, risorsa.getNazione());
			ps.setString(16, risorsa.getServizioMilitare());
			
			//altri dati
			ps.setString(17, risorsa.getPatente());
			ps.setString(18, risorsa.getCosto());
			ps.setBoolean(19, risorsa.isOccupato());
			ps.setString(20, risorsa.getTipoContratto());
			ps.setString(21, risorsa.getFiguraProfessionale());
			ps.setString(22, risorsa.getSeniority());
			
			esitoInserimentoRisorsa = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
			return "Siamo spiacenti l'inserimento della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
			
		if(esitoInserimentoRisorsa == 1){
			messaggio = "ok";
		}else{
			messaggio = "Siamo spiacenti l'inserimento della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		return messaggio;
	}

	/*
	 * con questo metodo effettuo la ricerca a seconda dei paramatri
	 * inseriti dall'utente
	 */
	public ArrayList ricercaRisorse(RisorsaDTO risorsa){
		
		log.info("metodo: ricercaRisorse");
		
		ArrayList listaRisorse = new ArrayList();
		
		String sql = "select * from tbl_risorse where visible = true ";
		
		if(risorsa.getCognome() != null && !risorsa.getCognome().equals("")){
			sql += "and cognome like ? ";
		}
		
		if(risorsa.getNome() != null && !risorsa.getNome().equals("")){
			sql += " and nome like ? ";
		}
		
		if(risorsa.getCosto() != null && !risorsa.getCosto().equals("")){
			sql += " and costo = ? ";
		}
		
		if(risorsa.getFiguraProfessionale() != null && !risorsa.getFiguraProfessionale().equals("")){
			sql += " and figura_professionale like ?";
		}
		
		if(risorsa.getSeniority() != null && !risorsa.getSeniority().equals("")){
			sql += " and seniority = ?";
		}
		
		sql += "  order by cognome ASC";
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		log.info("sql: " + sql);
		
		int contatore = 1;
		try {
			ps = connessione.prepareStatement(sql);
			
			if(risorsa.getCognome() != null && !risorsa.getCognome().equals("")){
				ps.setString(contatore++, "%"+risorsa.getCognome().trim()+"%");
			}
			
			if(risorsa.getNome() != null && !risorsa.getNome().equals("")){
				ps.setString(contatore++,	"%"+risorsa.getNome().trim()+"%");
			}
			
			if(risorsa.getCosto() != null && !risorsa.getCosto().equals("")){
				ps.setString(contatore++, risorsa.getCosto().trim());
			}
			
			if(risorsa.getFiguraProfessionale() != null && !risorsa.getFiguraProfessionale().equals("")){
				ps.setString(contatore++, "%"+risorsa.getFiguraProfessionale().trim()+"%");
			}
			
			if(risorsa.getSeniority() != null && !risorsa.getSeniority().equals("")){
				ps.setString(contatore, risorsa.getSeniority());
			}
			
			rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO risorseTrovate = new RisorsaDTO();
				risorseTrovate.setIdRisorsa(rs.getInt(1));
				risorseTrovate.setCognome(rs.getString(2));
				risorseTrovate.setNome(rs.getString(3));
				risorseTrovate.setDataNascita(rs.getString(4));
				risorseTrovate.setLuogoNascita(rs.getString(5));
				risorseTrovate.setSesso(rs.getString(6));
				risorseTrovate.setCodiceFiscale(rs.getString(7));
				risorseTrovate.setEmail(rs.getString(8));
				risorseTrovate.setTelefono(rs.getString(9));
				risorseTrovate.setCellulare(rs.getString(10));
				risorseTrovate.setFax(rs.getString(11));
				risorseTrovate.setIndirizzo(rs.getString(12));
				risorseTrovate.setCitta(rs.getString(13));
				risorseTrovate.setProvincia(rs.getString(14));
				risorseTrovate.setCap(rs.getString(15));
				risorseTrovate.setNazione(rs.getString(16));
				risorseTrovate.setServizioMilitare(rs.getString(17));
				risorseTrovate.setPatente(rs.getString(18));
				risorseTrovate.setCosto(rs.getString(19));
				risorseTrovate.setOccupato(rs.getBoolean(20));
				risorseTrovate.setTipoContratto(rs.getString(21));
				risorseTrovate.setFiguraProfessionale(rs.getString(22));
				risorseTrovate.setSeniority(rs.getString(23));
				risorseTrovate.setVisible(rs.getBoolean(24));
				risorseTrovate.setFlaCreazioneCurriculum(rs.getBoolean(25));
				
				listaRisorse.add(risorseTrovate);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		
		return listaRisorse;
		
	}
	
	/*
	 * questo metodo serve per il profilo della risorsa
	 */
	
	public RisorsaDTO caricamentoProfiloRisorsa(int idRisorsa){
		
		log.info("metodo: caricamentoProfiloRisorsa");
		
		RisorsaDTO risorsa = null;
		
		String sql = "select * from tbl_risorse where id_risorsa = ?";
		
		log.info("sql: select * from tbl_risorse where id_risorsa = "+idRisorsa);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setCognome(rs.getString(2));
				risorsa.setNome(rs.getString(3));
				risorsa.setDataNascita(rs.getString(4));
				risorsa.setLuogoNascita(rs.getString(5));
				risorsa.setSesso(rs.getString(6));
				risorsa.setCodiceFiscale(rs.getString(7));
				risorsa.setEmail(rs.getString(8));
				risorsa.setTelefono(rs.getString(9));
				risorsa.setCellulare(rs.getString(10));
				risorsa.setFax(rs.getString(11));
				risorsa.setIndirizzo(rs.getString(12));
				risorsa.setCitta(rs.getString(13));
				risorsa.setProvincia(rs.getString(14));
				risorsa.setCap(rs.getString(15));
				risorsa.setNazione(rs.getString(16));
				risorsa.setServizioMilitare(rs.getString(17));
				risorsa.setPatente(rs.getString(18));
				risorsa.setCosto(rs.getString(19));
				risorsa.setOccupato(rs.getBoolean(20));
				risorsa.setTipoContratto(rs.getString(21));
				risorsa.setFiguraProfessionale(rs.getString(22));
				risorsa.setSeniority(rs.getString(23));
				risorsa.setVisible(rs.getBoolean(24));
				risorsa.setFlaCreazioneCurriculum(rs.getBoolean(25));
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return risorsa;
	}
	
	public String modificaRisorsa(RisorsaDTO risorsa){
	
		log.info("metodo: modificaRisorsa");
		
		String messaggio = "";
		
		int esitoModificaRisorsa = 0;
		
		String sql = "update tbl_risorse set cognome = ?, nome = ?, data_nascita = ?, luogo_nascita = ?, sesso = ?, cod_fiscale = ?, mail = ?, telefono = ?, cellulare = ?, fax = ?, indirizzo = ?, citta = ?, provincia = ?, cap = ?, nazione = ?, servizio_militare = ?, patente = ?, costo = ?, occupato = ?, tipo_contratto = ?, figura_professionale = ?, seniority = ? where id_risorsa = ?";
		
		log.info("sql: update tbl_risorse set cognome = "+risorsa.getCognome()+", nome = "+risorsa.getNome()+", data_nascita = "+risorsa.getDataNascita()+", luogo_nascita = "+risorsa.getLuogoNascita()+"," +
				"sesso = "+risorsa.getSesso()+", cod_fiscale = "+risorsa.getCodiceFiscale()+", mail = "+risorsa.getEmail()+", telefono = "+risorsa.getTelefono()+", cellulare = "+risorsa.getCellulare()+", " +
				"fax = "+risorsa.getFax()+", indirizzo = "+risorsa.getIndirizzo()+", citta = "+risorsa.getCitta()+", provincia = "+risorsa.getProvincia()+", cap = "+risorsa.getCap()+", " +
				"nazione = "+risorsa.getNazione()+", servizio_militare = "+risorsa.getServizioMilitare()+", patente = "+risorsa.getPatente()+", costo = "+risorsa.getCosto()+"," +
				"occupato = "+risorsa.isOccupato()+", tipo_contratto = "+risorsa.getTipoContratto()+", figura_professionale = "+risorsa.getFiguraProfessionale()+", seniority = "+risorsa.getSeniority()+" " +
				"where id_risorsa = "+risorsa.getIdRisorsa());
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			
			//dati anagrafici
			ps.setString(1, risorsa.getCognome());
			ps.setString(2, risorsa.getNome());
			ps.setString(3, risorsa.getDataNascita());
			ps.setString(4, risorsa.getLuogoNascita());
			ps.setString(5, risorsa.getSesso());
			ps.setString(6, risorsa.getCodiceFiscale());
			ps.setString(7, risorsa.getEmail());
			ps.setString(8, risorsa.getTelefono());
			ps.setString(9, risorsa.getCellulare());
			ps.setString(10, risorsa.getFax());
			
			//residenza
			ps.setString(11, risorsa.getIndirizzo());
			ps.setString(12, risorsa.getCitta());
			ps.setString(13, risorsa.getProvincia());
			ps.setString(14, risorsa.getCap());
			ps.setString(15, risorsa.getNazione());
			ps.setString(16, risorsa.getServizioMilitare());
			
			//altri dati
			ps.setString(17, risorsa.getPatente());
			ps.setString(18, risorsa.getCosto());
			ps.setBoolean(19, risorsa.isOccupato());
			ps.setString(20, risorsa.getTipoContratto());
			ps.setString(21, risorsa.getFiguraProfessionale());
			ps.setString(22, risorsa.getSeniority());
			
			ps.setInt(23, risorsa.getIdRisorsa());
			
			esitoModificaRisorsa = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
			return "Siamo spiacenti la modifica della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}
			
		if(esitoModificaRisorsa == 1){
			messaggio = "Modifica Risorsa avvenuta con successo";
		}else{
			messaggio = "Siamo spiacenti la modifica della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		return messaggio;
	}
	
	public String eliminaRisorsa(int idRisorsa){
		
		log.info("metodo: eliminaRisorsa");
		
		String messaggio = "";
		
		int esitoEliminaRisorsa = 0;
		
		String sql = "update tbl_risorse as risorsa,tbl_utenti as utente set risorsa.visible = 0,utente.utente_visible = 0 where risorsa.id_risorsa = ? and utente.id_risorsa = ?";
		
		log.info("sql: update tbl_risorse as risorsa,tbl_utenti as utente set risorsa.visible = 0,utente.utente_visible = 0 where risorsa.id_risorsa = "+idRisorsa+" and utente.id_risorsa = "+idRisorsa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ps.setInt(2, idRisorsa);
			esitoEliminaRisorsa = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
			return "Siamo spiacenti la risorsa non è stata disibilitata con successo. Contattare l'amministrazione";
		}finally{
			close(ps);
		}

		if(esitoEliminaRisorsa > 0){
			messaggio = "La Risorsa è stata disabilitata con successo";
		}else{
			messaggio = "Siamo spiacenti l'eliminazione della risorsa non è avvenuta con successo. Contattare l'amministrazione";
		}
		
		return messaggio;
	}
	
	public ArrayList<RisorsaDTO> elencoRisorse(){
		
		log.info("metodo: elencoRisorse");
		
		ArrayList<RisorsaDTO> listaRisorse = new ArrayList<RisorsaDTO>();
		
		String sql = "select id_risorsa, nome, cognome, costo, figura_professionale, seniority from tbl_risorse where visible = true order by cognome ASC";
		
		log.info("sql: select id_risorsa, nome, cognome, costo, figura_professionale, seniority from tbl_risorse where visible = true order by cognome ASC");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setNome(rs.getString(2));
				risorsa.setCognome(rs.getString(3));
				risorsa.setCosto(rs.getString(4));
				risorsa.setFiguraProfessionale(rs.getString(5));
				risorsa.setSeniority(rs.getString(6));
				listaRisorse.add(risorsa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
					
		return listaRisorse;
	}
	
	/*
	 * questo metodo viene richiamato, tramite funzione javascript, per valorizzare dinamicamente
	 * le risorse associate a un cliente al momento della ricerca delle trattattive.
	 */
	
	public String elencoTrattativeRisorse(String codiceCliente){
		
		log.info("metodo: elencoTrattativeRisorse");
		
		String valori = "";
		
		String sql = "select risorse.id_risorsa, risorse.cognome, risorse.nome from tbl_risorse as risorse, tbl_trattative as trattative where trattative.id_risorsa = risorse.id_risorsa and trattative.id_cliente = ? and risorse.visible = true group by id_risorsa order by cognome ASC";
		
		log.info("sql: select risorse.id_risorsa, risorse.cognome, risorse.nome from tbl_risorse as risorse, tbl_trattative as trattative where trattative.id_risorsa = risorse.id_risorsa and trattative.id_cliente = "+codiceCliente+" and risorse.visible = true group by id_risorsa order by cognome ASC");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, codiceCliente);
			rs = ps.executeQuery();
			while(rs.next()){
				valori = valori + rs.getInt(1) + "," + rs.getString(2) + "," + rs.getString(3) + ";";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
					
		return valori;
	}
	
	
	public UtenteDTO caricamentoCredenziali(int idRisorsa){
		
		log.info("metodo: caricamentoCredenziali");
		
		String sql = "select risorsa.cognome, risorsa.nome, risorsa.mail, utenti.id_utente, utenti.username, utenti.password from tbl_utenti as utenti, tbl_risorse as risorsa where utenti.id_risorsa = risorsa.id_risorsa and utenti.id_risorsa = ?";
		
		log.info("sql: select risorsa.cognome, risorsa.nome, risorsa.mail, utenti.id_utente, utenti.username, utenti.password from tbl_utenti as utenti, tbl_risorse as risorsa where utenti.id_risorsa = risorsa.id_risorsa and utenti.id_risorsa = "+idRisorsa);
		
		UtenteDTO utente = null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			if(rs.next()){
				utente = new UtenteDTO();
				utente.setDescrizioneRisorsa(rs.getString(1) + " " + rs.getString(2));
				utente.setEmail(rs.getString(3));
				utente.setId_utente(rs.getInt(4));
				utente.setUsername(rs.getString(5));
				utente.setPassword(rs.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		
		return utente;
	}
	
	public String modificaCredenziali(String username,String password,int idUtente){
		
		log.info("metodo: modificaCredenziali");
		
		String sql = "update tbl_utenti set username = ?, password = ? where id_utente = ?";
		
		log.info("sql: update tbl_utenti set username = "+username+", password = "+password+" where id_utente = "+idUtente);
		
		int esitoModifica = 0;
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, MD5.encript(password));
			ps.setInt(3, idUtente);
			esitoModifica = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+e);
			return "Siamo spiacenti la modifica delle credenziali della risorsa non sono avvenute con successo. Contattare l'amministrazione.";
		}finally{
			close(ps);
		}
		
		if(esitoModifica == 1){
			return "ok";
		}else{
			return "Siamo spiacenti la modifica delle credenziali della risorsa non sono avvenute con successo. Contattare l'amministrazione.";
		}
		
	}
	
	/*
	 * caricamento di tutte le risorse non più abilitate
	 */
	
	public ArrayList listaRisorseDaAbilitare(){
		
		log.info("metodo: listaRisorseDaAbilitare");
		
		String sql = "select id_risorsa,cognome,nome from tbl_risorse where visible = false";
		
		log.info("sql: select id_risorsa,cognome,nome from tbl_risorse where visible = false");
		
		ArrayList listaRisorse = new ArrayList();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt(1));
				risorsa.setCognome(rs.getString(2));
				risorsa.setNome(rs.getString(3));
				listaRisorse.add(risorsa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps, rs);
		}
		
		return listaRisorse;
	}
	
	public String abilitaRisorsa(int id_risorsa){
		
		log.info("metodo: abilitaRisorsa");
		
		String sql = "update tbl_risorse as risorsa,tbl_utenti as utente set risorsa.visible = 1,utente.utente_visible = 1 where risorsa.id_risorsa = ? and utente.id_risorsa = ?";
		
		log.info("sql: update tbl_risorse as risorsa,tbl_utenti as utente set risorsa.visible = 1,utente.utente_visible = 1 where risorsa.id_risorsa = "+id_risorsa+" and utente.id_risorsa = "+id_risorsa);
		
		PreparedStatement ps = null;
		
		int esito = 0;
		
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_risorsa);
			ps.setInt(2, id_risorsa);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("errore sql: " + e);
			return "errore comando sql "+e.getMessage();
		}
		
		if(esito > 0){
			return "ok";
		}else{
			return "Impossibile abilitare la risorsa. Contattare l'amministratore.";
		}
		
		
	}
	
	
	public ArrayList<RisorsaDTO>invioEmail(){
		
		log.info("metodo: caricamentoCommessa");
		
		String sql = "select * from utente";
		
		log.info("sql: "+sql);
		
		ArrayList<RisorsaDTO>listaUtenti = new ArrayList<RisorsaDTO>();
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				listaUtenti.add(new RisorsaDTO(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)));
			}
		} catch (SQLException e) {
			log.error("errore sql: "+  e);
		}finally{
			close(ps,rs);
		}
		return listaUtenti;
	}
}
