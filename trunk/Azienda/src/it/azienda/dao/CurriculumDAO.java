package it.azienda.dao;

import it.azienda.dto.CurriculumDTO;
import it.azienda.dto.Dettaglio_Cv_DTO;
import it.azienda.dto.EsperienzeDTO;
import it.azienda.dto.RisorsaDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;



public class CurriculumDAO extends BaseDao {
	private MyLogger log;

	public CurriculumDAO(Connection connessione) {
		super(connessione);
		log=new MyLogger(this.getClass());
	}

	/**
	 * tramite questo metodo effettuo il caricamento di tutti
	 * i curriculum creati.
	 */
	public ArrayList<CurriculumDTO> caricamentoAllCurriculum(){
		final String metodo = "caricamento All Curriculum";
		log.start(metodo);
				
		ArrayList<CurriculumDTO> listaCurriculum = new ArrayList<CurriculumDTO>();
		
		String sql = "select risorsa.id_risorsa, risorsa.nome, risorsa.cognome, risorsa.flag_creazione_cv, if((select count(*) from tbl_esperienze_professionali_cv as esperienze where esperienze.id_risorsa = risorsa.id_risorsa and esperienze.visibile = true) > 0 , 1, 0) as esperienza, if((select count(*) from tbl_dettaglio_cv as dettaglio where dettaglio.id_risorsa = risorsa.id_risorsa and dettaglio.visible = true) > 0 , 1, 0) as dettaglio " +
				" from tbl_risorse as risorsa, tbl_esperienze_professionali_cv as esperienza, tbl_dettaglio_cv as dettaglio " +
				" where risorsa.flag_creazione_cv = true " +
				" group by risorsa.nome;";
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
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
			e.printStackTrace();
		}finally{
			close(ps, rs);
			log.end("caricamento All Curriculum");
		}
		
		return listaCurriculum;
	}
	
	/**
	 * effettuo il caricamento del singolo curriculum selezionato
	 */
	public CurriculumDTO caricamentoCurriculum(int idRisorsa) throws IOException{
		final String metodo = "caricamento Curriculum";
		log.start(metodo);
		
		CurriculumDTO curriculum = new CurriculumDTO();
		
		curriculum.setRisorsa(caricamentoIntestazioneRisorsa(idRisorsa));
		curriculum.setListaEsperienze(caricamentoEsperienze(idRisorsa));
		curriculum.setListaDettaglio(caricamentoDettaglio(idRisorsa));
		
		log.end("caricamento Curriculum");
		
		return curriculum;
		
	}
	
	/**
	 * effettuo il caricamento della parte anagrafica
	 */
	
	public RisorsaDTO caricamentoIntestazioneRisorsa(int idRisorsa){
		
		final String metodo = "caricamento Intestazione Risorsa";
		log.start(metodo);
		
		RisorsaDTO risorsa = null;
		
		String sql = "select id_risorsa, cognome, nome,data_nascita, mail, telefono, cellulare, fax, indirizzo from tbl_risorse where id_risorsa = ?";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setInt(1, idRisorsa);
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
			}	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			close(ps,rs);
			log.end("caricamento Intestazione Risorsa");
		}
		return risorsa;
	}
	
	/**
	 * effettuo il caricamento delle esperienze prensenti nel singolo curriculum
	 */
	
	public ArrayList caricamentoEsperienze(int idRisorsa) throws IOException{
		
		final String metodo = "caricamento Esperienze";
		log.start(metodo);
		
		ArrayList<EsperienzeDTO> listaEsperienze = new ArrayList<EsperienzeDTO>();
		
		String sql = "select * from tbl_esperienze_professionali_cv where id_risorsa = ? and visibile = true order by SUBSTRING(periodo,0,6),SUBSTRING(periodo,7,6) DESC";
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
			e.printStackTrace();
		}finally{
			close(ps,rs);
			log.end("caricamento Esperienze");
		}
		
		return listaEsperienze;
	}

	/**
	 * effettuo il caricamento del dettaglio del singolo curriculum
	 */
	
	public Dettaglio_Cv_DTO caricamentoDettaglio(int idRisorsa) {
		
		final String metodo = "caricamento Dettaglio";
		log.start(metodo);
		
		Dettaglio_Cv_DTO dettaglio = null;
		
		String sql = "select * from tbl_dettaglio_cv where id_risorsa = ? and visible = true";
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
			e.printStackTrace();
		}finally{
			close(ps,rs);
			log.end("caricamento Dettaglio");
		}
		
		return dettaglio;
	}
	
	public int updateIntestazione(RisorsaDTO risorsa) {
		
		final String metodo = "caricamento Dettaglio";
		log.start(metodo);
		
		int esito = 0;
		String sql = "update tbl_risorse set cognome = ?, nome = ?, data_nascita = ?, mail = ?, telefono = ?, cellulare = ?, fax = ?, indirizzo = ? where id_risorsa = ?";
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
			log.error("caricamento Dettaglio", "Errore nell'update dell'intestazione");
		}finally{
			close(ps);
			log.end("caricamento Dettaglio");
		}
		
		return esito;
	}
	
	/**
	 * effettuiamo l'inserimento delle esperienza nella tabella "Tbl_Esperienze_Professionali_Cv"
	 * @param esperienze
	 */
	public int inserimentoEsperienze(EsperienzeDTO esperienze){
		
		final String metodo = "inserimento Esperienze";
		log.start(metodo);
		
		int esito = 0;
		
		String sql = "insert into tbl_esperienze_professionali_cv(periodo,azienda,luogo,descrizione,id_risorsa) values (?,?,?,?,?)";
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
			log.error("inserimento Esperienze", e.getMessage());
		}finally{
			close(ps);
			log.end("inserimento Esperienze");
		}
		
		return esito;
	}
	
	/**
	 * tramite questo metodo effettuo il caricamento della singola esperienza
	 */
	public EsperienzeDTO caricamentoEsperienza(int id_esperienza){
		
		final String metodo = "caricamento esperienza";
		log.start(metodo);
		
		EsperienzeDTO exp = null;
		
		String sql = "select * from tbl_esperienze_professionali_cv where id_esperienza_professionale = ?";
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
			log.error("caricamento esperienza", e.getMessage());
		}finally{
			close(ps,rs);
			log.end("caricamento esperienza");
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
			
			final String metodo = "aggiornamento esperienza";
			log.start(metodo);
		
			int esito = 0;
			
			String sql = "update tbl_esperienze_professionali_cv set periodo = ?, azienda = ?, luogo = ?, descrizione = ?, id_risorsa = ? where id_esperienza_professionale = ?";
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
				log.error("aggiornamento esperienza", e.getMessage());
			}finally{
				close(ps);
				log.end("aggiornamento esperienza");
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
		
		final String metodo = "eliminazione esperienza";
		log.start(metodo);
		
		int esito = 0;
		
		String sql = "update tbl_esperienze_professionali_cv set visibile = ? where id_esperienza_professionale = ?";
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idEsperienza);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("eliminazione esperienza", e.getMessage());
		}finally{
			close(ps);
			log.end("eliminazione esperienza");
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
		
		final String metodo = "eliminazione esperienza globale";
		log.start(metodo);
		
		String sql = "update tbl_esperienze_professionali_cv set visibile = ? where id_risorsa = ?";
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, id_risorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("eliminazione esperienza globale", e.getMessage());
		}finally{
			close(ps);
			log.end("eliminazione esperienza globale");
		}
		
	}
	
	/**
	 * tramite questo metodo effettuo l'inserimento del Dettaglio
	 * @param dettaglio
	 * @return esito
	 * @throws IOException
	 */
	
	public int inserimentoDettaglio(Dettaglio_Cv_DTO dettaglio){
		
		final String metodo = "inserimento Dettaglio";
		log.start(metodo);
		
		String sql = "insert into tbl_dettaglio_cv(capacita_professionali,competenze_tecniche,lingue_straniere,istruzione,formazione,interessi,id_risorsa) values (?,?,?,?,?,?,?)";
		
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
			// TODO Auto-generated catch block
			log.error("inserimento Dettaglio", e.getMessage());
		}finally{
			close(ps);
			log.end("inserimento Dettaglio");
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
			
			final String metodo = "aggiornamento Dettaglio";
			log.start(metodo);
			
			String sql = "update tbl_dettaglio_cv set capacita_professionali = ?, competenze_tecniche = ?, lingue_straniere = ?, istruzione = ?, formazione = ?, interessi = ? where id_risorsa = ? and id_dettaglio = ?";
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
				log.error("", e.getMessage());
			}finally{
				close(ps);
				log.end("aggiornamento Dettaglio");
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
	
	public String esportaCurriculumVitae(String url, CurriculumDTO curriculum){
		
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
	
	/**
	 * tramite questo metodo effettuo l'eliminazione del Dettaglio
	 * @param idEsperienza
	 * @return esito
	 * @throws IOException
	 */
	public int eliminazioneDettaglio(int id_dettaglio) throws IOException{
		
		final String metodo = "eliminazione dettaglio";
		log.start(metodo);
		
		int esito = 0;
		
		String sql = "update tbl_dettaglio_cv set visible = ? where id_dettaglio = ?";
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, id_dettaglio);
			esito = ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("eliminazione dettaglio", e.getMessage());
		}finally{
			close(ps);
			log.end("eliminazione dettaglio");
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
		
		final String metodo = "eliminazione dettaglio globale";
		log.start(metodo);
		
		String sql = "update tbl_dettaglio_cv set visible = ? where id_risorsa = ?";
		
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, id_risorsa);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("eliminazione dettaglio globale", e.getMessage());
		}finally{
			close(ps);
			log.end("eliminazione dettaglio globale");
		}
		
	}
	
	/**
	 * tramite questo metodo effettuo il caricamento di tutte le risorse senza Curriculum
	 * @param 
	 * @return ArrayList
	 * @throws IOException
	 */
	public ArrayList caricamentoRisorseSenzaCurriculum() throws IOException{
		
		final String metodo = "caricamento risorse senza curriculum";
		log.start(metodo);
		
		ArrayList<RisorsaDTO> listaRisorse = new ArrayList<RisorsaDTO>();
		
		String sql = "select * from tbl_risorse where flag_creazione_cv = false and visible = true";
		
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
			log.error("caricamento risorse senza curriculum", e.getMessage());
		}finally{
			close(ps);
			log.end("caricamento risorse senza curriculum");
		}
		
		 return listaRisorse;
	}
	
	/**
	 * tramite questo metodo effettuo l'abilitazione del flag_creazione_cv sulla tabella
	 * TBL_RISORSE
	 * @param idRisorsa
	 */
	public void creazioneFlagCreazioneCurriculum(int idRisorsa){
		
		String sql = "update tbl_risorse set flag_creazione_cv = ? where id_risorsa = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, true);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		String sql = "update tbl_risorse set flag_creazione_cv = ? where id_risorsa = ?";
		PreparedStatement ps=null;
		try {
			ps = connessione.prepareStatement(sql);
			ps.setBoolean(1, false);
			ps.setInt(2, idRisorsa);
			ps.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

}