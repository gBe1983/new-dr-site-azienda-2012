package it.azienda.dao;

import it.azienda.dto.CurriculumDTO;
import it.azienda.dto.Dettaglio_Cv_DTO;
import it.azienda.dto.EsperienzeDTO;
import it.azienda.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class CurriculumDAO extends BaseDao {
	private Logger log;

	public CurriculumDAO(Connection connessione) {
		super(connessione);
		log= Logger.getLogger(CurriculumDAO.class);
	}

	/**
	 * tramite questo metodo effettuo il caricamento di tutti
	 * i curriculum creati.
	 */
	public ArrayList<CurriculumDTO> caricamentoAllCurriculum(){
		
		log.info("metodo: caricamento All Curriculum");
				
		ArrayList<CurriculumDTO> listaCurriculum = new ArrayList<CurriculumDTO>();
		
		String sql = "select risorsa.id_risorsa, risorsa.nome, risorsa.cognome, risorsa.flag_creazione_cv, " +
				" if((select count(*) from tbl_esperienze_professionali_cv as esperienze where esperienze.id_risorsa = risorsa.id_risorsa and esperienze.visibile = true) > 0 , 1, 0) as esperienza," +
				" if((select count(*) from tbl_dettaglio_cv as dettaglio where dettaglio.id_risorsa = risorsa.id_risorsa and dettaglio.visible = true) > 0 , 1, 0) as dettaglio " +
				" from tbl_risorse as risorsa " +
				" where risorsa.flag_creazione_cv = true and risorsa.visible = true";
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		log.info("sql: select risorsa.id_risorsa, risorsa.nome, risorsa.cognome, risorsa.flag_creazione_cv, " +
				" if((select count(*) from tbl_esperienze_professionali_cv as esperienze where esperienze.id_risorsa = risorsa.id_risorsa and esperienze.visibile = true) > 0 , 1, 0) as esperienza," +
				" if((select count(*) from tbl_dettaglio_cv as dettaglio where dettaglio.id_risorsa = risorsa.id_risorsa and dettaglio.visible = true) > 0 , 1, 0) as dettaglio " +
				" from tbl_risorse as risorsa " +
				" where risorsa.flag_creazione_cv = true and risorsa.visible = true");
		
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				CurriculumDTO curriculum = new CurriculumDTO();
				curriculum.setId_risorsa(rs.getInt("id_risorsa"));
				curriculum.setNominativo(rs.getString("nome") + " " + rs.getString("cognome"));
				curriculum.setInformazioniPersonali(rs.getBoolean("flag_creazione_cv"));
				curriculum.setEsperienze(rs.getBoolean("esperienza"));
				curriculum.setDettaglio(rs.getBoolean("dettaglio"));
				listaCurriculum.add(curriculum);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps, rs);
		}
		
		return listaCurriculum;
	}
	
	/**
	 * effettuo il caricamento del singolo curriculum selezionato
	 */
	public CurriculumDTO caricamentoCurriculum(int idRisorsa) throws IOException{
		
		log.info("metodo: caricamento Curriculum");
		
		CurriculumDTO curriculum = new CurriculumDTO();
		
		curriculum.setRisorsa(caricamentoIntestazioneRisorsa(idRisorsa));
		curriculum.setListaEsperienze(caricamentoEsperienze(idRisorsa));
		curriculum.setListaDettaglio(caricamentoDettaglio(idRisorsa));
		
		return curriculum;
		
	}
	
	/**
	 * effettuo il caricamento della parte anagrafica
	 */
	
	public RisorsaDTO caricamentoIntestazioneRisorsa(int idRisorsa){
		
		log.info("metodo: caricamento Intestazione Risorsa");
		
		RisorsaDTO risorsa = null;
		
		String sql = "select risorsa.id_risorsa, risorsa.cognome, risorsa.nome, risorsa.data_nascita, risorsa.mail, risorsa.telefono, risorsa.cellulare, risorsa.fax, risorsa.indirizzo, risorsa.luogo_nascita, risorsa.nazione, risorsa.figura_professionale, risorsa.servizio_militare, if((select count(*) from tbl_dettaglio_cv as dettaglio where dettaglio.id_risorsa = risorsa.id_risorsa and risorsa.id_risorsa = ? and dettaglio.visible = true) > 0 , 1, 0) as flagDettaglio from tbl_risorse as risorsa where risorsa.id_risorsa = ?";
		
		log.info("select risorsa.id_risorsa, risorsa.cognome, risorsa.nome, risorsa.data_nascita, risorsa.mail, risorsa.telefono, risorsa.cellulare, risorsa.fax, risorsa.indirizzo, risorsa.luogo_nascita, risorsa.nazione, risorsa.figura_professionale, risorsa.servizio_militare, if((select count(*) from tbl_dettaglio_cv as dettaglio where dettaglio.id_risorsa = risorsa.id_risorsa and risorsa.id_risorsa = ? and dettaglio.visible = true) > 0 , 1, 0) as flagDettaglio from tbl_risorse as risorsa where risorsa.id_risorsa = "+idRisorsa);
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			ps.setInt(2, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt("id_risorsa"));
				risorsa.setCognome(rs.getString("cognome"));
				risorsa.setNome(rs.getString("nome"));
				risorsa.setDataNascita(rs.getString("data_nascita"));
				risorsa.setEmail(rs.getString("mail"));
				risorsa.setTelefono(rs.getString("telefono"));
				risorsa.setCellulare(rs.getString("cellulare"));
				risorsa.setFax(rs.getString("fax"));
				risorsa.setIndirizzo(rs.getString("indirizzo"));
				risorsa.setLuogoNascita(rs.getString("luogo_nascita"));
				risorsa.setNazione(rs.getString("nazione"));
				risorsa.setFiguraProfessionale(rs.getString("figura_professionale"));
				risorsa.setServizioMilitare(rs.getString("servizio_militare"));
				risorsa.setFlagCreazioneDettaglio(rs.getBoolean("flagDettaglio"));
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.info("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		return risorsa;
	}
	
	/**
	 * effettuo il caricamento delle esperienze prensenti nel singolo curriculum
	 */
	
	public ArrayList caricamentoEsperienze(int idRisorsa) throws IOException{
		
		log.info("metodo: caricamento Esperienze");
		
		ArrayList<EsperienzeDTO> listaEsperienze = new ArrayList<EsperienzeDTO>();
		
		String sql = "select * from tbl_esperienze_professionali_cv where id_risorsa = ? and visibile = true order by SUBSTRING(periodo,0,6),SUBSTRING(periodo,7,6) DESC";
		
		log.info("sql: select * from tbl_esperienze_professionali_cv where id_risorsa = "+idRisorsa+" and visibile = true order by SUBSTRING(periodo,0,6),SUBSTRING(periodo,7,6) DESC");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				EsperienzeDTO esperienze = new EsperienzeDTO();
				esperienze.setIdEsperienze(rs.getInt(1));
				esperienze.setPeriodo(formattazionePeriodo(rs.getString(2).split("_")));
				esperienze.setAzienda(rs.getString(3));
				esperienze.setLuogo(rs.getString(4));
				esperienze.setDescrizione(rs.getString(5));
				esperienze.setId_risorsa(Integer.parseInt(rs.getString(6)));
				esperienze.setVisibile(rs.getBoolean(7));
				
				listaEsperienze.add(esperienze);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return listaEsperienze;
	}

	/**
	 * effettuo il caricamento del dettaglio del singolo curriculum
	 */
	
	public Dettaglio_Cv_DTO caricamentoDettaglio(int idRisorsa) {
		
		
		log.info("metodo: caricamento Dettaglio");
		
		Dettaglio_Cv_DTO dettaglio = null;
		
		String sql = "select * from tbl_dettaglio_cv where id_risorsa = ? and visible = true";
		
		log.info("select * from tbl_dettaglio_cv where id_risorsa = "+idRisorsa+" and visible = true");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
			rs = ps.executeQuery();
			while(rs.next()){
				dettaglio = new Dettaglio_Cv_DTO();
				dettaglio.setId_dettaglio(rs.getInt(1));
				dettaglio.setCapacita_professionali(rs.getString(2));
				dettaglio.setCompetenze_tecniche(rs.getString(3));
				dettaglio.setLingue_Straniere(rs.getString(4));
				dettaglio.setIstruzione(rs.getString(5));
				dettaglio.setFormazione(rs.getString(6));
				dettaglio.setInteressi(rs.getString(7));
				dettaglio.setId_risorsa(rs.getInt(8));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps,rs);
		}
		
		return dettaglio;
	}
	
	public int updateIntestazione(RisorsaDTO risorsa) {
		
		log.info("metodo: caricamento Dettaglio");
		
		int esito = 0;
		String sql = "update tbl_risorse set cognome = ?, nome = ?, data_nascita = ?, mail = ?, telefono = ?, cellulare = ?, fax = ?, indirizzo = ? where id_risorsa = ?";
		
		log.info("sql: update tbl_risorse set cognome = "+risorsa.getCognome()+", nome = "+risorsa.getNome()+", data_nascita = "+risorsa.getDataNascita()+", mail = "+risorsa.getEmail()+"," +
				"telefono = "+risorsa.getTelefono()+", cellulare = "+risorsa.getCellulare()+", fax = "+risorsa.getFax()+", indirizzo = "+risorsa.getIndirizzo()+" where id_risorsa = "+risorsa.getIdRisorsa());
		
		PreparedStatement ps=null;
		try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, risorsa.getCognome());
				ps.setString(2, risorsa.getNome());
				ps.setString(3, risorsa.getDataNascita());
				ps.setString(4, risorsa.getEmail());
				ps.setString(5, risorsa.getTelefono());
				ps.setString(6, risorsa.getCellulare());
				ps.setString(7, risorsa.getFax());
				ps.setString(8, risorsa.getIndirizzo());
				ps.setInt(9, risorsa.getIdRisorsa());
				esito = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps);
		}
		
		return esito;
	}
	
	/**
	 * effettuiamo l'inserimento delle esperienza nella tabella "Tbl_Esperienze_Professionali_Cv"
	 * @param esperienze
	 */
	public int inserimentoEsperienze(EsperienzeDTO esperienze){
		
		log.info("metodo: inserimento Esperienze");
		
		int esito = 0;
		
		String sql = "insert into tbl_esperienze_professionali_cv(periodo,azienda,luogo,descrizione,id_risorsa) values (?,?,?,?,?)";
		
		log.info("sql: insert into tbl_esperienze_professionali_cv(periodo,azienda,luogo,descrizione,id_risorsa) values ("+esperienze.getPeriodo()+","+esperienze.getAzienda()+","+esperienze.getLuogo()+","+esperienze.getDescrizione()+","+esperienze.getId_risorsa()+")");
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, esperienze.getPeriodo());
			ps.setString(2, esperienze.getAzienda());
			ps.setString(3, esperienze.getLuogo());
			ps.setString(4, esperienze.getDescrizione());
			ps.setInt(5, esperienze.getId_risorsa());
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		return esito;
	}
	
	/**
	 * tramite questo metodo effettuo il caricamento della singola esperienza
	 */
	public EsperienzeDTO caricamentoEsperienza(int id_esperienza){
		
		log.info("metodo: caricamento esperienza");
		
		EsperienzeDTO exp = null;
		
		String sql = "select * from tbl_esperienze_professionali_cv where id_esperienza_professionale = ?";
		
		log.info("select * from tbl_esperienze_professionali_cv where id_esperienza_professionale = "+id_esperienza);
		
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, id_esperienza);
			rs = ps.executeQuery();
			if(rs.next()){
				exp = new EsperienzeDTO();
				exp.setIdEsperienze(rs.getInt("id_esperienza_professionale"));
				exp.setPeriodo(rs.getString("periodo"));
				exp.setAzienda(rs.getString("azienda"));
				exp.setLuogo(rs.getString("luogo"));
				exp.setDescrizione(rs.getString("descrizione"));
				exp.setId_risorsa(rs.getInt("id_risorsa"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps,rs);
		}
		
		return exp;
	}
	
	/**
	 * tramite questo metodo effettuo l'aggiornamento della singola Esperinza
	 * @param esperienza
	 * @return void
	 * @throws IOException
	 */
	public int aggiornamentoEsperienza(EsperienzeDTO esperienza) throws IOException{
			
			log.info("aggiornamento esperienza");
		
			int esito = 0;
			
			String sql = "update tbl_esperienze_professionali_cv set periodo = ?, azienda = ?, luogo = ?, descrizione = ?, id_risorsa = ? where id_esperienza_professionale = ?";
			
			log.info("update tbl_esperienze_professionali_cv set periodo = "+esperienza.getPeriodo()+", azienda = "+esperienza.getAzienda()+", luogo = "+esperienza.getLuogo()+", descrizione = "+esperienza.getDescrizione()+", id_risorsa = "+esperienza.getId_risorsa()+" where id_esperienza_professionale = "+esperienza.getIdEsperienze());
			
			PreparedStatement ps=null;
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, esperienza.getPeriodo());
				ps.setString(2, esperienza.getAzienda());
				ps.setString(3, esperienza.getLuogo());
				ps.setString(4, esperienza.getDescrizione());
				ps.setInt(5, esperienza.getId_risorsa());
				ps.setInt(6, esperienza.getIdEsperienze());
				esito = ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("errore sql: "+ e);
			}finally{
				close(ps);
			}
	
			return esito;
	}
	
	/**
	 * tramite questo metodo effettuo l'eliminazione dell'Esperienze
	 * @param idEsperienza
	 * @return esito
	 * @throws IOException
	 */
	public int eliminazioneEsperienza(int idEsperienza) throws IOException{
		
		log.info("metodo: eliminazione esperienza");
		
		int esito = 0;
		
		String sql = "update tbl_esperienze_professionali_cv set visibile = ? where id_esperienza_professionale = ?";
		
		log.info("sql: update tbl_esperienze_professionali_cv set visibile = false where id_esperienza_professionale = "+idEsperienza);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idEsperienza);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		 return esito;
	}
	
	/**
	 * tramite questo metodo effettuo l'eliminazione di tutte le espeerienze 
	 * presenti per quella risorsa
	 * @param idEsperienza
	 * @return esito
	 * @throws IOException
	 */
	public void eliminaEsperienzaGlobale(int id_risorsa) throws IOException{
		
		log.info("metodo: eliminazione esperienza globale");
		
		String sql = "update tbl_esperienze_professionali_cv set visibile = ? where id_risorsa = ?";
		
		log.info("sql: update tbl_esperienze_professionali_cv set visibile = false where id_risorsa = "+id_risorsa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, id_risorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
	}
	
	/**
	 * tramite questo metodo effettuo l'inserimento del Dettaglio
	 * @param dettaglio
	 * @return esito
	 * @throws IOException
	 */
	
	public int inserimentoDettaglio(Dettaglio_Cv_DTO dettaglio){
		
		log.info("metodo: inserimento Dettaglio");
		
		String sql = "insert into tbl_dettaglio_cv(capacita_professionali,competenze_tecniche,lingue_straniere,istruzione,formazione,interessi,id_risorsa) values (?,?,?,?,?,?,?)";
		
		log.info("sql: insert into tbl_dettaglio_cv(capacita_professionali,competenze_tecniche,lingue_straniere,istruzione,formazione,interessi,id_risorsa) values ("+dettaglio.getCapacita_professionali()+","+dettaglio.getCompetenze_tecniche()+","+dettaglio.getLingue_Straniere()+","+dettaglio.getIstruzione()+","+dettaglio.getFormazione()+","+dettaglio.getInteressi()+","+dettaglio.getId_risorsa()+")");
		
		int esitoInserimentoDettaglio = 0; 
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setString(1, dettaglio.getCapacita_professionali());
			ps.setString(2, dettaglio.getCompetenze_tecniche());
			ps.setString(3, dettaglio.getLingue_Straniere());
			ps.setString(4, dettaglio.getIstruzione());
			ps.setString(5, dettaglio.getFormazione());
			ps.setString(6, dettaglio.getInteressi());
			ps.setInt(7, dettaglio.getId_risorsa());
			esitoInserimentoDettaglio = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch 
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		return esitoInserimentoDettaglio;
	}
	
	/**
	 * tramite questo messaggio effettuo l'aggiornamento della singola Esperinza
	 * @param dettaglio
	 * @return esitoModificaDettaglio
	 * @throws IOException
	 */
	public int aggiornamentoDettaglio(Dettaglio_Cv_DTO dettaglio) throws IOException{
			
			log.info("metodo: aggiornamento Dettaglio");
			
			String sql = "update tbl_dettaglio_cv set capacita_professionali = ?, competenze_tecniche = ?, lingue_straniere = ?, istruzione = ?, formazione = ?, interessi = ? where id_risorsa = ? and id_dettaglio = ?";
			
			log.info("sql: update tbl_dettaglio_cv set capacita_professionali = "+dettaglio.getCapacita_professionali()+", competenze_tecniche = "+dettaglio.getCompetenze_tecniche()+", lingue_straniere = "+dettaglio.getLingue_Straniere()+", istruzione = "+dettaglio.getIstruzione()+", formazione = "+dettaglio.getFormazione()+", interessi = "+dettaglio.getInteressi()+" where id_risorsa = "+dettaglio.getId_risorsa()+" and id_dettaglio = "+dettaglio.getId_dettaglio());
			
			int esitoModificaDettaglio = 0;
			PreparedStatement ps=null;
			try {
				ps = connessione.prepareStatement(sql);
				ps.setString(1, dettaglio.getCapacita_professionali());
				ps.setString(2, dettaglio.getCompetenze_tecniche());
				ps.setString(3, dettaglio.getLingue_Straniere());
				ps.setString(4, dettaglio.getIstruzione());
				ps.setString(5, dettaglio.getFormazione());
				ps.setString(6, dettaglio.getInteressi());
				ps.setInt(7, dettaglio.getId_risorsa());
				ps.setInt(8, dettaglio.getId_dettaglio());
				esitoModificaDettaglio = ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("errore sql: "+ e);
			}finally{
				close(ps);
			}
			
			return esitoModificaDettaglio;
			
	}
	
	/**
	 * tramite questo metodo creo la pagina html che poi verrà convertita in 
	 * pdf
	 * @param url
	 * @param curriculum
	 * @return
	 */
	
	public String esportaCurriculumVitaeFormatoEuropeo(String url, CurriculumDTO curriculum){
		
		log.info("metodo: esportaCurriculumVitaeFormatoEuropeo");
		
		String stampaCurriculum = "";
		
		boolean titoloEsperienze = false;
			
		if(curriculum.getRisorsa() != null){
			RisorsaDTO risorsa = curriculum.getRisorsa();
			
			// creazione pagina html
   stampaCurriculum +=  "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">" +
						"<html>" +
						"<head>" +
						"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tabella.css\">" +
						"</head>" +
						"<body>" +
						"<table width=\"575\" >" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<font size=\"2\"><h1 align=\"right\">Formato europeo<br>per il curriculum<br>vitae</h1>" +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<br>"  +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		img src=\""+url+"images/logo.gif\" align=\"right\"></font>" +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<br>"  +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<h3 align=\"right\">Informazioni personali</h3>" +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<br>"  +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">Cognome</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>"+risorsa.getCognome()+ "</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">Nome</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>" + risorsa.getNome() + "</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">Indirizzo</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>" + risorsa.getIndirizzo() + "</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">Telefono</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>"+ risorsa.getTelefono() + "</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">Fax</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>"+ risorsa.getFax() + "</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">E-mail</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>" + risorsa.getEmail() + "</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<p align=\"right\">Data di nascita</p>" +
						"	</td>" +
						"	<td>" +
						"		<span>"+risorsa.getDataNascita()+"</span>" +
						"	</td>" +
						"</tr>" +
						"<tr>" +
						"	<td width=\"50%\">" +
						"		<br>"  +
						"	</td>" +
						"	<td>" +
						"		<br>"  +
						"	</td>" +
						"</tr>";
			}
		for(int x = 0; x < curriculum.getListaEsperienze().size(); x++){
				EsperienzeDTO esperienze = (EsperienzeDTO) curriculum.getListaEsperienze().get(x);
	
				if(!titoloEsperienze){
					titoloEsperienze = true;
	   stampaCurriculum +=  "<tr>" +
							"	<td width=\"50%\">" +
							"		<h3 align=\"right\">Esperienze Lavorative</h3>" +
							"	<td>" +
							"	<td>" +
							"		<br>" +
							"	<td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>";
				}
	   stampaCurriculum +=  "<tr>" +
							"	<td width=\"50%\">" +
							"		<p align=\"right\">Periodo:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<span>" + esperienze.getPeriodo() + "</span>" +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<p align=\"right\">Azienda:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<span>" + esperienze.getAzienda() + "</span>" +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<p align=\"right\">Luogo:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<span>" + esperienze.getLuogo() + "</span>" +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Descrizione:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">" + esperienze.getDescrizione().replace("\n", "<br>") + "</textarea>" +
							"	</td>" +
							"</tr>"+
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>";
			}
		if(curriculum.getListaDettaglio() != null){
						Dettaglio_Cv_DTO dettaglio = (Dettaglio_Cv_DTO) curriculum.getListaDettaglio();
						
	   stampaCurriculum +=  "<tr>" +
							"	<td width=\"50%\">" +
							"		<h3 align=\"right\">Dettaglio Curriculum</h3>" +
							"	<td>" +
							"	<td>" +
							"		<br>" +
							"	<td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Capacita Professionali:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">"  + dettaglio.getCapacita_professionali().replace("\n", "<br>") + "</textarea>" +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>"+
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Competenze Tecniche:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">"  + dettaglio.getCompetenze_tecniche().replace("\n", "<br>") + "</textarea>"  +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>"+
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Lingue:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">"  + dettaglio.getLingue_Straniere().replace("\n", "<br>") + "</textarea>"  +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>"+
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Istruzione:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">"  + dettaglio.getIstruzione().replace("\n", "<br>") + "</textarea>" +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>"+
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Formazione:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">"  + dettaglio.getFormazione().replace("\n", "<br>") + "</textarea>" +
							"	</td>" +
							"</tr>" +
							"<tr>" +
							"	<td width=\"50%\">" +
							"		<br>"  +
							"	</td>" +
							"	<td>" +
							"		<br>"  +
							"	</td>" +
							"</tr>"+
							"<tr>" +
							"	<td width=\"50%\" valign=\"top\">" +
							"		<p align=\"right\">Interessi:</p>"  +
							"	</td>" +
							"	<td>" +
							"		<textarea rows=\"15\" cols=\"50\" name=\"formazione\" readonly=\"readonly\">"  + dettaglio.getInteressi().replace("\n", "<br>") + "</textarea>" +
							"	</td>" +
							"</tr>";
					}
		stampaCurriculum += "</table>";
		
		return stampaCurriculum;
	}
	
	public String esportaCurriculumVitaeFormatoAziendale(String url, CurriculumDTO cv,boolean completo){
		
		log.info("metodo: esportaCurriculumVitaeFormatoAziendale");
		
		String stampaCurriculum = "";
		
		stampaCurriculum += "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"> " +
				"<html> " +
				"<head> " +
				"	<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> " +
				"	<title>Insert title here</title> " +
				"	<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tabella.css\"> " +
				"</head> " +
				"<body> " +
				"	<table width=\"575\" align=\"center\" border=\"0\"> " +
						"<tr> " +
						"	<td colspan=\"3\">img src=\""+url+"images/logo_DierreConsulting.gif\" /></td>" +
						"</tr> " +
						"<tr> " +
						"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p align=\"center\" class=\"intestazione\"><b>CURRICULUM VITAE</b></p></td> " +
						"</tr>";
		if(completo){
			
			stampaCurriculum += "<tr> " +
							"	<td class=\"spazioSinistro\"><br></td>" +
							"	<td class=\"spazioCentro\"><p class=\"contenuti\"><b>Cognome Nome: </b><br>Figura Professionale: </p></td> " +
							"	<td class=\"spazioDestro\"><p class=\"contenuti\"><b>"+cv.getRisorsa().getCognome() + " " +  cv.getRisorsa().getNome()+ "</b><br>";
							if(cv.getRisorsa().getFiguraProfessionale() != null){ 
								stampaCurriculum += cv.getRisorsa().getFiguraProfessionale(); 
							}else{ 
								stampaCurriculum += ""; 
							}
			stampaCurriculum +=	"</p></td> " +
							"</tr> " +
							"<tr> " +
							"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">INFORMAZIONI PERSONALI</p></td> " +
							"</tr> " +
							"<tr> " +
							"	<td class=\"spazioSinistro\"><br></td> " +
							"	<td class=\"spazioCentro\"><p class=\"contenuti\">Nazionalità: <br>Data Nascita: <br>Luogo Nascita: <br>Residenza: </p></td> " +
							"	<td class=\"spazioDestro\"><p class=\"contenuti\"> ";
							if(cv.getRisorsa().getNazione() != null){
								stampaCurriculum += cv.getRisorsa().getNazione() + "<br>";
							}else{
								stampaCurriculum += "" + "<br>";
							}
							if(cv.getRisorsa().getDataNascita() != null){
								stampaCurriculum += cv.getRisorsa().getDataNascita() +  "<br>";
							}else{
								stampaCurriculum += "" + "<br>";
							}
							if(cv.getRisorsa().getLuogoNascita() != null){
								stampaCurriculum += cv.getRisorsa().getLuogoNascita() +  "<br>";
							}else{
								stampaCurriculum += "" + "<br>";
							}
							if(cv.getRisorsa().getIndirizzo() != null){
								stampaCurriculum +=  cv.getRisorsa().getIndirizzo();
							}else{
								stampaCurriculum += "";
							}
							stampaCurriculum += "</p></td></tr>";
				}else{
							stampaCurriculum += "<tr> " +
										    	"	<td class=\"spazioSinistro\"><br></td>" +
										    	"	<td class=\"spazioCentro\"><p class=\"contenuti\"><br>Figura Professionale: </p></td> " +
										    	"	<td class=\"spazioDestro\"><p class=\"contenuti\"><br>";
												if(cv.getRisorsa().getFiguraProfessionale() != null){ 
													stampaCurriculum += cv.getRisorsa().getFiguraProfessionale(); 
												}else{ 
													stampaCurriculum += ""; 
												}
							stampaCurriculum += "</p></td></tr>";
				}
				if(cv.getListaDettaglio() != null){
				
						if(cv.getListaDettaglio().getIstruzione() != null){
							if(cv.getListaDettaglio().getIstruzione().length() > 0){
								stampaCurriculum += "<tr> " +
									"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">ISTRUZIONE</p></td> " +
									"</tr>" +
									"<tr> " +
									"	<td><br></td>" +
									"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"7\" cols=\"58\">" + cv.getListaDettaglio().getIstruzione().replace("\n", "<br>") + "</textarea></td> " +
									"</tr>";
							}
						}
						if(cv.getListaDettaglio().getLingue_Straniere() != null){
							if(cv.getListaDettaglio().getLingue_Straniere().length() > 0){
							
				stampaCurriculum += "<tr> " +
									"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">LINGUE STRANIERE</p></td> " +
									"</tr> " +
									"<tr> " +
									"	<td><br></td>" +
									"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"7\" cols=\"58\">" + cv.getListaDettaglio().getLingue_Straniere().replace("\n", "<br>") + "</textarea></td> " +
									"</tr>";
							}
						}
					}
					if(cv.getListaEsperienze().size() > 0){
				stampaCurriculum += "<tr> " +
									"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">ESPERIENZE</p></td> " +
									"</tr>";
				
						for(int x = 0; x < cv.getListaEsperienze().size(); x++){
							EsperienzeDTO exp = (EsperienzeDTO) cv.getListaEsperienze().get(x);
							
				stampaCurriculum += "<tr> " +
									"	<td colspan=\"3\"><br></td> " +
									"</tr> " +
									"<tr>" +
									"	<td><br></td>" +
									"	<td class=\"context\" colspan=\"2\"> " +
									"		<table width=\"480\"> " +
									"			<tr> " +
									"				<td><p>" + exp.getPeriodo() + "</p></td> " +
									"				<td><p>" + exp.getAzienda() + "</p></td> " +
									"				<td><p align=\"right\">" + exp.getLuogo() + "</p></td> " +
									"			</tr>" +
									"		</table>" +
									"	</td>" +
									"</tr> " +
									"<tr> " +
									"	<td colspan=\"3\"><br></td>" +
									"</tr> " +
									"<tr>" +
									"	<td><br></td> " +
									"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"30\" cols=\"58\">" + exp.getDescrizione().replace("\n", "<br>") + "</textarea></td>" +
									"</tr>";
						}
					}
					
					if(cv.getListaDettaglio() != null){
						
						if(cv.getListaDettaglio().getFormazione() != null){
							if(cv.getListaDettaglio().getFormazione().length() > 0){
								
			stampaCurriculum += "<tr> " +
								"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">FORMAZIONE</p></td> " +
								"</tr> " +
								"<tr>" +
								"	<td><br></td> " +
								"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"20\" cols=\"58\">" + cv.getListaDettaglio().getFormazione().replace("\n", "<br>") + "</textarea></td> " +
								"</tr>";
							}
						}
						
						if(cv.getListaDettaglio().getCompetenze_tecniche() != null){
							if(cv.getListaDettaglio().getCompetenze_tecniche().length() > 0){
							
			stampaCurriculum +=	"<tr> " +
								"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">CAPACITA E COMPETENZE TECNICHE</p></td> " +
								"</tr> " +
								"<tr>" +
								"	<td><br></td> " +
								"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"20\" cols=\"58\">" + cv.getListaDettaglio().getCompetenze_tecniche().replace("\n", "<br>") + "</textarea></td> " +
								"</tr>";
							}
						}
						
						if(cv.getListaDettaglio().getCapacita_professionali() != null){
							if(cv.getListaDettaglio().getCapacita_professionali().length() > 0){
								
			stampaCurriculum +=	"<tr> " +
								"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">CAPACITA' PROFESSIONALI</p></td> " +
								"</tr> " +
								"<tr>" +
								"	<td><br></td>" +
								"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"20\" cols=\"58\">" + cv.getListaDettaglio().getCapacita_professionali().replace("\n", "<br>") + "</textarea></td> " +
								"</tr>";
			
							}
						}
						
						if(cv.getListaDettaglio().getInteressi() != null){
							if(cv.getListaDettaglio().getInteressi().length() > 0){
								
			stampaCurriculum +=	"<tr> " +
								"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">INTERESSI</p></td> " +
								"</tr> " +
								"<tr>" +
								"	<td><br></td>" +
								"	<td class=\"context\" colspan=\"2\"><textarea readonly=\"readonly\" class=\"contenuti\" rows=\"20\" cols=\"58\">" + cv.getListaDettaglio().getInteressi().replace("\n", "<br>") + "</textarea></td> " +
								"</tr>";
			
							}
						}
						
						if(cv.getRisorsa().getServizioMilitare() != null){
							if(cv.getRisorsa().getServizioMilitare().length() > 0){
							
			stampaCurriculum +=	"<tr> " +
								"	<td colspan=\"3\" class=\"contenitoreIntestazione\"><p class=\"intestazioni\">SERVIZIO MILITARE</p></td> " +
								"</tr> " +
								"<tr>" +
								"	<td><br></td>" +
								"	<td class=\"context\" colspan=\"2\"><br><span>" + cv.getRisorsa().getServizioMilitare() + "</span></td> " +
								"</tr>";
							}
						}
					}
			stampaCurriculum += "</table> </body> </html>";
		
			
			return stampaCurriculum;
		
	}
	
	/**
	 * tramite questo metodo effettuo l'eliminazione del Dettaglio
	 * @param idEsperienza
	 * @return esito
	 * @throws IOException
	 */
	public int eliminazioneDettaglio(int id_dettaglio) throws IOException{
		
		log.info("metodo: eliminazione dettaglio");
		
		int esito = 0;
		
		String sql = "update tbl_dettaglio_cv set visible = ? where id_dettaglio = ?";
		
		log.info("sql: update tbl_dettaglio_cv set visible = false where id_dettaglio = "+id_dettaglio);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, id_dettaglio);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql"+ e);
		}finally{
			close(ps);
		}
		
		 return esito;
	}
	
	/**
	 * tramite questo metodo effettuo l'eliminazione del Dettaglio attraverso l'id_risorsa
	 * @param idEsperienza
	 * @return esito
	 * @throws IOException
	 */
	public void eliminazioneDettaglioGlobale(int id_risorsa) throws IOException{
		
		log.info("metodo: eliminazione dettaglio globale");
		
		String sql = "update tbl_dettaglio_cv set visible = ? where id_risorsa = ?";
		
		log.info("sql: update tbl_dettaglio_cv set visible = false where id_risorsa = "+id_risorsa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, id_risorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
	}
	
	/**
	 * tramite questo metodo effettuo il caricamento di tutte le risorse senza Curriculum
	 * @param 
	 * @return ArrayList
	 * @throws IOException
	 */
	public ArrayList caricamentoRisorseSenzaCurriculum() throws IOException{
		
		log.info("metodo: caricamento risorse senza curriculum");
		
		ArrayList<RisorsaDTO> listaRisorse = new ArrayList<RisorsaDTO>();
		
		String sql = "select * from tbl_risorse where flag_creazione_cv = false and visible = true";
		
		log.info("sql: "+sql);
		
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = connessione.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				RisorsaDTO risorsa = new RisorsaDTO();
				risorsa.setIdRisorsa(rs.getInt("id_risorsa"));
				risorsa.setCognome(rs.getString("cognome"));
				risorsa.setNome(rs.getString("nome"));
				listaRisorse.add(risorsa);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: "+ e);
		}finally{
			close(ps);
		}
		
		 return listaRisorse;
	}
	
	/**
	 * tramite questo metodo effettuo l'abilitazione del flag_creazione_cv sulla tabella
	 * TBL_RISORSE
	 * @param idRisorsa
	 */
	public void creazioneFlagCreazioneCurriculum(int idRisorsa){
		
		log.info("metodo: creazioneFlagCreazioneCurriculum");
		
		String sql = "update tbl_risorse set flag_creazione_cv = ? where id_risorsa = ?";
		
		log.info("sql: update tbl_risorse set flag_creazione_cv = true where id_risorsa = "+idRisorsa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, true);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + e);
		}finally{
			close(ps);
		}

	}
	
	/**
	 * tramite questo metodo effettuo disabilitazione del flag_creazione_cv sulla tabella
	 * TBL_RISORSE
	 * @param idRisorsa
	 */
	public void disabilitazioneFlagCreazioneCurriculum(int idRisorsa){
		
		log.info("metodo: disabilitazioneFlagCreazioneCurriculum");
		
		String sql = "update tbl_risorse set flag_creazione_cv = ? where id_risorsa = ?";
		
		log.info("sql: update tbl_risorse set flag_creazione_cv = false where id_risorsa = "+idRisorsa);
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("errore sql: " + sql);
		}finally{
			close(ps);
		}

	}
	
	/**
	 * In questo metodo formatto la data nel Formato MM/YYYY
	 * @param date
	 * @return periodo
	 */
	
	public String formattazionePeriodo(String[] date){
	
		log.info("metodo: formattazionePeriodo");
		
		String periodo = "Da: ";
						
		for(int x = 0; x < date.length; x++){
			if(x == 0){
				String annoInizio = date[x].substring(0,4);
				int meseInizio = Integer.parseInt(date[x].substring(4,6));
				switch(meseInizio){
					case 1:
						periodo += " gennaio " + annoInizio;
						break;
					case 2:
						periodo += " febbraio " + annoInizio;
						break;
					case 3:
						periodo += " marzo " + annoInizio;
						break;
					case 4:
						periodo += " aprile " + annoInizio;
						break;
					case 5:
						periodo += " maggio " + annoInizio;
						break;
					case 6:
						periodo += " giugno " + annoInizio;
						break;
					case 7:
						periodo += " luglio " + annoInizio;
						break;
					case 8:
						periodo += " agosto " + annoInizio;
						break;
					case 9:
						periodo += " settembre " + annoInizio;
						break;
					case 10:
						periodo += " ottobre " + annoInizio;
						break;
					case 11:
						periodo += " novembre " + annoInizio;
						break;
					case 12:
						periodo += " dicembre " + annoInizio;
						break;
				}
			}else{
				String annoInizio = date[x].substring(0,4);
				int meseInizio = Integer.parseInt(date[x].substring(4,6));
				switch(meseInizio){
					case 1:
						periodo += " A: gennaio " + annoInizio;
						break;
					case 2:
						periodo += " A: febbraio " + annoInizio;
						break;
					case 3:
						periodo += " A: marzo " + annoInizio;
						break;
					case 4:
						periodo += " A: aprile " + annoInizio;
						break;
					case 5:
						periodo += " A: maggio " + annoInizio;
						break;
					case 6:
						periodo += " A: giugno " + annoInizio;
						break;
					case 7:
						periodo += " A: luglio " + annoInizio;
						break;
					case 8:
						periodo += " A: agosto " + annoInizio;
						break;
					case 9:
						periodo += " A: settembre " + annoInizio;
						break;
					case 10:
						periodo += " A: ottobre " + annoInizio;
						break;
					case 11:
						periodo += " A: novembre " + annoInizio;
						break;
					case 12:
						periodo += " A: dicembre " + annoInizio;
						break;
				}
			}
		}
		
		return periodo;
	}
	
	public File creazioneCurriculumVitaeFormatoEuropeo(String url,CurriculumDTO curriculum,File file){
		
		log.info("metodo: creazioneCurriculumVitaeFormatoEuropeo");
		
		Document doc = new Document(PageSize.A4, 10, 10, 10, 40);
		
		
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(file));
			

			HTMLWorker htmlWorker = new HTMLWorker(doc);
			doc.open();
			
				String curriculumVItaeHtml = esportaCurriculumVitaeFormatoEuropeo(url, curriculum);
			
				System.out.println(curriculumVItaeHtml);
				StyleSheet styleSheet = new StyleSheet();
//				styleSheet.loadTagStyle("table", "border", "1");
				ArrayList lista = (ArrayList) htmlWorker.parseToList(new StringReader(curriculumVItaeHtml), styleSheet);
				
				//mi creo la tabella che servira a contenere le varie celle
				PdfPTable disegnaTabella = new PdfPTable(2);
				
				disegnaTabella.setWidthPercentage(100);
				
				//carico le dimensione delle due celle
				float[] columnWidths = {40, 140};
				disegnaTabella.setWidths(columnWidths);
				
				//mi faccio restituire il risultato della conversione da Html a PDF
				PdfPTable tabella = (PdfPTable) lista.get(0);
				
				//mi faccio restituire le righe presenti nella Tabella
				ArrayList listaRows =  (ArrayList)tabella.getRows();
				
				//Gestione del font per la parte sinistra del curriculum
				Font fontIntestazione = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
				Font fontTitoli = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
				Font fontDescrizione = new Font(Font.FontFamily.TIMES_ROMAN, 8);
				
				//ciclo le righe
				for(int y = 0; y < listaRows.size(); y++){
					//recupero la singola riga
					PdfPRow row = (PdfPRow)listaRows.get(y);
					
					//recupero le varie celle prensenti nella riga
					PdfPCell[] celle  = (PdfPCell[]) row.getCells();
					
					
					if(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent().equals("Formato europeo")){
						
						
						PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent() + 
								  celle[0].getCompositeElements().get(0).getChunks().get(1).getContent() +
								  celle[0].getCompositeElements().get(0).getChunks().get(2).getContent() +
								  celle[0].getCompositeElements().get(0).getChunks().get(3).getContent() +
								  celle[0].getCompositeElements().get(0).getChunks().get(4).getContent() , fontIntestazione));
						cellaSinistra.setPaddingRight(5);
						cellaSinistra.setBorderWidth(0);
						cellaSinistra.setHorizontalAlignment(Element.ALIGN_RIGHT);
						disegnaTabella.addCell(cellaSinistra);
						
					}else if(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("img") == 0){
						
						Image immagine = Image.getInstance(url + "images/logo.gif");
						immagine.setWidthPercentage(25);
						PdfPCell cellaSinistra = new PdfPCell(immagine);
						cellaSinistra.setPaddingRight(5);
						cellaSinistra.setBorderWidth(0);
						cellaSinistra.setHorizontalAlignment(Element.ALIGN_RIGHT);
						disegnaTabella.addCell(cellaSinistra);
						
						
					}else if(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent().equals("Informazioni personali")){
						
						
						PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
						cellaSinistra.setPaddingRight(5);
						cellaSinistra.setBorderWidth(0);
						cellaSinistra.setHorizontalAlignment(Element.ALIGN_RIGHT);
						disegnaTabella.addCell(cellaSinistra);
						
					}else if(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent().equals("Esperienze Lavorative")){
						
						
						PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
						cellaSinistra.setPaddingRight(5);
						cellaSinistra.setBorderWidth(0);
						cellaSinistra.setHorizontalAlignment(Element.ALIGN_RIGHT);
						disegnaTabella.addCell(cellaSinistra);
						
					}else if(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent().equals("Dettaglio Curriculum")){
						
						
						PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
						cellaSinistra.setPaddingRight(5);
						cellaSinistra.setBorderWidth(0);
						cellaSinistra.setHorizontalAlignment(Element.ALIGN_RIGHT);
						disegnaTabella.addCell(cellaSinistra);
						
					}else{
						PdfPCell cellaSinistra = new PdfPCell(new Phrase(celle[0].getCompositeElements().get(0).getChunks().get(0).getContent(), fontDescrizione));
						cellaSinistra.setPaddingRight(5);
						cellaSinistra.setBorderWidth(0);
						cellaSinistra.setHorizontalAlignment(Element.ALIGN_RIGHT);
						disegnaTabella.addCell(cellaSinistra);
					}
					
					if(celle[1].getCompositeElements() == null){
						PdfPCell cellaDestra = new PdfPCell(celle[1]);
						cellaDestra.setPaddingLeft(5);
						cellaDestra.setBorderWidth(0);
						cellaDestra.setBorderWidthLeft(1);
						cellaDestra.setHorizontalAlignment(Element.ALIGN_LEFT);
						disegnaTabella.addCell(cellaDestra);
					}else{
						if(celle[1].getCompositeElements().get(0).getChunks().size() > 0){
							String testo = "";
							for(Chunk chuck:celle[1].getCompositeElements().get(0).getChunks()){
								testo += chuck.getContent();
							}
							PdfPCell cellaDestra = new PdfPCell(new Phrase(testo, fontDescrizione));
							cellaDestra.setPaddingLeft(5);
							cellaDestra.setBorderWidth(0);
							cellaDestra.setBorderWidthLeft(1);
							cellaDestra.setHorizontalAlignment(Element.ALIGN_LEFT);
							disegnaTabella.addCell(cellaDestra);
						}else{
							PdfPCell cellaDestra = new PdfPCell(new Phrase(celle[1].getCompositeElements().get(0).getChunks().get(0).getContent(), fontDescrizione));
							cellaDestra.setPaddingLeft(5);
							cellaDestra.setBorderWidth(0);
							cellaDestra.setBorderWidthLeft(1);
							cellaDestra.setHorizontalAlignment(Element.ALIGN_LEFT);
							disegnaTabella.addCell(cellaDestra);
						}
					}
				}
				doc.add(disegnaTabella);
				SimpleDateFormat formatoPdf = new SimpleDateFormat("dd/MM/yyyy");
				doc.add(new Paragraph());
				doc.add(new Paragraph());
				doc.add(new Paragraph("Autorizzo il trattamento dei miei dati personali ai sensi del D.Lgs. 196/2003",fontDescrizione));
				doc.add(new Paragraph());
				doc.add(new Paragraph("Torino, " + formatoPdf.format(Calendar.getInstance().getTime()),fontDescrizione));
				doc.add(new Paragraph("Firma ",fontDescrizione));
				doc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error("file non trovato: " + e);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.error("documentation error: " + e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			log.error("url formattata in maniera errata: " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("eccezione: " + e);
		}
		
		return file;
	}
	
	public File creazioneCurriculumVitaeFormatoAziendale(String url,CurriculumDTO curriculum,File file,boolean completo){
		
		log.info("metodo: creazioneCurriculumVitaeFormatoAziendale");
		
 		Document doc = new Document(PageSize.A4, 10, 10, 10, 40);
				
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(file));
			

			HTMLWorker htmlWorker = new HTMLWorker(doc);
			doc.open();
			
				String curriculumVItaeHtml = esportaCurriculumVitaeFormatoAziendale(url, curriculum,completo);
			
				System.out.println(curriculumVItaeHtml);
				StyleSheet styleSheet = new StyleSheet();
//				styleSheet.loadTagStyle("table", "border", "1");
				ArrayList lista = (ArrayList) htmlWorker.parseToList(new StringReader(curriculumVItaeHtml), styleSheet);
				
				//mi creo la tabella che servira a contenere le varie celle
				PdfPTable disegnaTabella = new PdfPTable(3);
				
				disegnaTabella.setWidthPercentage(87);
				
				//carico le dimensione delle due celle
				float[] columnWidths = {40, 30,40};
				disegnaTabella.setWidths(columnWidths);
				
				//mi faccio restituire il risultato della conversione da Html a PDF
				PdfPTable tabella = (PdfPTable) lista.get(1);
				
				//mi faccio restituire le righe presenti nella Tabella
				ArrayList listaRows =  (ArrayList)tabella.getRows();
				
				//Gestione del font per la parte sinistra del curriculum
				Font fontIntestazione = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
				Font fontTitoli = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
				Font fontDescrizione = new Font(Font.FontFamily.TIMES_ROMAN, 8);
				
				//ciclo le righe
				for(int y = 0; y < listaRows.size(); y++){
					//recupero la singola riga
					PdfPRow row = (PdfPRow)listaRows.get(y);
					
					//recupero le varie celle prensenti nella riga
					PdfPCell[] celle  = (PdfPCell[]) row.getCells();
					
					for(int x = 0; x < celle.length; x++){
						if(celle[x] != null){
							if(celle[x].getCompositeElements().get(0).getChunks().size() == 1 && (celle[x].getColspan() == 1 || celle[x].getColspan() == 3)){
								if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("img") == 0){
									Image immagine = Image.getInstance(url + "images/logo_DierreConsulting_Intestazione.gif");
									immagine.setWidthPercentage(95);
									PdfPCell cellaSinistra = new PdfPCell(immagine);
									cellaSinistra.setColspan(3);
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_CENTER);
									disegnaTabella.addCell(cellaSinistra);
									
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("CURRICULUM VITAE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontIntestazione));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_CENTER);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("INFORMAZIONI PERSONALI") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("ISTRUZIONE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("LINGUE STRANIERE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("ESPERIENZE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("CAPACITA E COMPETENZE TECNICHE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("FORMAZIONE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
								
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("CAPACITA' PROFESSIONALI") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
									
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("INTERESSI") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
									
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("SERVIZIO MILITARE") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontTitoli));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setBorderWidthBottom(1);
									cellaSinistra.setColspan(3);
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaSinistra);
									
								}else if(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent().indexOf("\n") == 0){
									
									PdfPCell cellaSinistra = new PdfPCell(new Paragraph(celle[x].getCompositeElements().get(0).getChunks().get(0).getContent(), fontIntestazione));
									cellaSinistra.setBorderWidth(0);
									cellaSinistra.setColspan(celle[x].getColspan());
									cellaSinistra.setHorizontalAlignment(Element.ALIGN_CENTER);
									disegnaTabella.addCell(cellaSinistra);
									
								}
								
							}else if(celle[x].getCompositeElements().get(0) instanceof PdfPTable){
								
								Font fontPeriodo = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
								
								PdfPTable disegnaExp = new PdfPTable(3);
								
								disegnaExp.setWidthPercentage(120);
								
								//carico le dimensione delle due celle
								float[] columnWidth = {55, 40, 25};
								disegnaExp.setWidths(columnWidth);
								
								PdfPTable periodoExp = (PdfPTable) celle[x].getCompositeElements().get(0);
								
								ArrayList righe = periodoExp.getRows();
								
								for(int z = 0; z < righe.size(); z++){
									PdfPRow rows = (PdfPRow)righe.get(z);
									
									//recupero le varie celle prensenti nella riga
									PdfPCell[] cells  = (PdfPCell[]) rows.getCells();
									
									for(int w = 0; w < cells.length; w++){
										if(cells[w].getCompositeElements().get(0).getChunks().size() == 1){
											PdfPCell cell = new PdfPCell(new Paragraph(cells[w].getCompositeElements().get(0).getChunks().get(0).getContent(), fontPeriodo));
											cell.setBorderWidth(0);
											cell.setHorizontalAlignment(Element.ALIGN_LEFT);
											disegnaExp.addCell(cell);
										}
									}
								}
								
								PdfPCell cella = new PdfPCell(disegnaExp);
								cella.setBorderWidth(0);
								cella.setColspan(celle[x].getColspan());
								cella.setHorizontalAlignment(Element.ALIGN_LEFT);
								disegnaTabella.addCell(cella);
								
							}else if(celle[x].getCompositeElements().get(0).getChunks().size() > 1 || celle[x].getColspan() == 2){
								String testo = "";
								for(Chunk chuck:celle[x].getCompositeElements().get(0).getChunks()){
									testo += chuck.getContent();
								}
								if(celle[x].getColspan() == 1){
									PdfPCell cellaDestra = new PdfPCell(new Phrase(testo, fontDescrizione));
									cellaDestra.setBorderWidth(0);
									cellaDestra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaDestra);
								}else{
									PdfPCell cellaDestra = new PdfPCell(new Phrase(testo, fontDescrizione));
									cellaDestra.setBorderWidth(0);
									cellaDestra.setColspan(2);
									cellaDestra.setHorizontalAlignment(Element.ALIGN_LEFT);
									disegnaTabella.addCell(cellaDestra);
								}
							}
						}
					}
				}
				doc.add(disegnaTabella);
				SimpleDateFormat formatoPdf = new SimpleDateFormat("dd/MM/yyyy");
				doc.add(new Paragraph());
				doc.add(new Paragraph());
				doc.add(new Paragraph("Autorizzo il trattamento dei miei dati personali ai sensi del D.Lgs. 196/2003",fontDescrizione));
				doc.add(new Paragraph());
				doc.add(new Paragraph("Torino, " + formatoPdf.format(Calendar.getInstance().getTime()),fontDescrizione));
				doc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			log.error("file non trovato: " + e);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			log.error("documentation error: " + e);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			log.error("url formattata in maniera errata: " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("eccezione: " + e);
		}
		
		return file;
	}
	

}