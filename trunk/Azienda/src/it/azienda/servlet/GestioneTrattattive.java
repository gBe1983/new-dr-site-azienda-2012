package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dao.CommesseDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dao.TrattativeDAO;
import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.TrattativeDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class GestioneTrattattive
 */
public class GestioneTrattattive extends BaseServlet {
	private static final long serialVersionUID = -1773569816950815813L;
	private Logger log = Logger.getLogger(GestioneCommessa.class);
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: doGet");
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: doPost");
		processRequest(request, response);
		
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: processRequest");

		// recupero sessione
		HttpSession sessione = request.getSession();

		TrattativeDAO tDAO = new TrattativeDAO(conn.getConnection());
		RisorsaDAO rDAO = new RisorsaDAO(conn.getConnection());
		ClienteDAO cDAO = new ClienteDAO(conn.getConnection());
		CommesseDAO commesseDAO = new CommesseDAO(conn.getConnection());


		if(sessione.getAttribute("utenteLoggato") != null){
			// recupero il parametro azione e verifico il suo valore
			String azione = request.getParameter("azione");
			/*
			 * in questa sezione effettuo la ricerca di tutte le trattative sia del
			 * singolo cliente o di tutte le trattative dell'azienda
			 */
			if (azione.equals("ricercaTrattativaCliente")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				ArrayList listaRisorse = rDAO.elencoRisorse();
				ArrayList listaTrattattive = null;
	
				/*
				 * in questa sezione gestito le varie possibilita di ricerca delle
				 * trattative che una azienda può avere, che sono: 
				 * - Codice <> "" e idRisorsa <> "" e esito <> "" 
				 * - Codice <> "" e idRisorsa <> "" e esito == ""
				 * - Codice <> "" e idRisorsa == "" e esito <> "" 
				 * - Codice == "" e idRisorsa <> "" e esito <> "" 
				 * - Codice <> "" e idRisorsa == "" e esito == "" 
				 * - Codice == "" e idRisorsa <> "" e esito == ""
				 * - Codice == "" e idRisorsa == "" e esito <> "" 
				 * - Codice == "" e idRisorsa == "" e esito == "" 
				 * - idTrattativa <> ""
				 */
				
				/*
				 * parametri di ricerca:
				 * - idRisorsa corrisponde all'id della risorsa
				 * - codice corrisponse al codice del cliente
				 * - esito corrisponde allo stato della trattativa
				 * - anno corrisponde all'anno con cui si ricerca le trattative
				 */
				if (request.getParameter("idRisorsa") != null ||
						request.getParameter("codice") != null ||
						request.getParameter("esito") != null || 
						request.getParameter("anno") != null) {
	
					listaTrattattive = tDAO.ricercaTrattative(request.getParameter("codice"), request.getParameter("idRisorsa"), 0,request.getParameter("esito"),request.getParameter("anno"));
					
				} else if (request.getParameter("dettaglioTrattativa") != null) {
	
					listaTrattattive = tDAO.ricercaTrattative("", "", Integer.parseInt(request.getParameter("dettaglioTrattativa")),"","");
					
				} else {
					
					listaTrattattive = tDAO.ricercaTrattative("", "", 0,"","");
					
				}
	
				/*
				 * effettuo questo ciclo sulle trattative per caricare il codice
				 * della commessa con cui è stata chiusa la trattativa.
				 
				for (int x = 0; x < listaTrattattive.size(); x++) {
					TrattativeDTO trattativa = (TrattativeDTO) listaTrattattive.get(x);
					if (trattativa.getEsito().equals("nuova Commessa")) {
						trattativa.setCodiceCommessa(tDAO.ricercaCodiceCommessa(trattativa.getIdTrattative()));
						listaTrattattive.set(x, trattativa);
					}
				}
				
				
				 * effettuo questo ciclo sulle trattative per caricare la descrizione
				 * della risorsa a cui è stata associata.
				 
				for (int x = 0; x < listaTrattattive.size(); x++) {
					TrattativeDTO trattativa = (TrattativeDTO) listaTrattattive.get(x);
					if (trattativa.getId_risorsa() != 0) {
						trattativa.setDescrizioneRisorsa(tDAO.ricercaDescrizioneRisorsa(trattativa.getId_risorsa()));
						listaTrattattive.set(x, trattativa);
					}
				}*/
				
				/*
				 * effettuo tipo di controllo a fronte di una ricerca delle
				 * trattattive relazionate unicamente con valorizzato al cliente
				 */
				if (request.getParameter("tipo") != null) {
					if (request.getParameter("tipo").equals("azienda")) {
						request.setAttribute("codice", request.getParameter("codice"));
					}
				}
	
				request.setAttribute("listaTrattattive", listaTrattattive);
				request.setAttribute("listaClienti",cDAO.caricamentoClienti());
				request.setAttribute("listaRisorse", listaRisorse);
	
				/*
				 * differenzio le due url perchè una è la gestione di tutte le
				 * trattative e l'altra invece è la gestione delle singole
				 * trattative collegate a un cliente
				 */
				if (request.getParameter("dispositiva") != null) {
					if (request.getParameter("dispositiva").equals("trattative")) {
						if(request.getParameter("dettaglioTrattativa") != null){
							
							log.info("url: /index.jsp?azione=visualizzaTrattative&tipo=tutte&dispositiva=trattative&dettaglioTrattativa="+request.getParameter("dettaglioTrattativa"));
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaTrattative&tipo=tutte&dispositiva=trattative&dettaglioTrattativa="+request.getParameter("dettaglioTrattativa")).forward(request, response);
						}else{
							
							log.info("url: /index.jsp?azione=visualizzaTrattative&tipo=tutte&dispositiva=trattative");
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaTrattative&tipo=tutte&dispositiva=trattative").forward(request, response);
						}
					} else {
						if(request.getParameter("dettaglioTrattativa") != null){
							
							log.info("url: /index.jsp?azione=visualizzaTrattative&dettaglioTrattativa="+request.getParameter("dettaglioTrattativa")+"&tipo=azienda&dispositiva=cliente");
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaTrattative&dettaglioTrattativa="+request.getParameter("dettaglioTrattativa")+"&tipo=azienda&dispositiva=cliente").forward(request, response);
						}else{
							
							log.info("url: /index.jsp?azione=visualizzaTrattative&tipo=azienda&dispositiva=cliente");
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaTrattative&tipo=azienda&dispositiva=cliente").forward(request, response);
						}
						
					}
				}
	
			} else if (azione.equals("aggiungiTrattative") || azione.equals("aggiungiCommessa") || azione.equals("ricercaCommessa")) {
				request.setAttribute("newCodCommessaEsterna",commesseDAO.creazioneCodiceCommessaEsterna());
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				/*
				 * in questa sezione entro quando l'utente clicca su
				 * "Aggiungi Trattative" o clicca su "Aggingi Commessa" o clicca "Ricerca Commessa" 
				 * per caricare i Cliente e le Risorse legati all'Azienda.
				 */
				if (azione.equals("aggiungiTrattative")) {
					request.setAttribute("listaClienti",cDAO.caricamentoClienti());
					request.setAttribute("listaRisorse", rDAO.elencoRisorse());
					request.setAttribute("listaTrattattive", tDAO.caricamentoTipologie());
				} else if (azione.equals("aggiungiCommessa")) {
					request.setAttribute("newCodCommessaInterna",commesseDAO.creazioneCodiceCommessaInterna());
					request.setAttribute("listaClienti",cDAO.caricamentoClienti());
					request.setAttribute("listaRisorse",  rDAO.elencoRisorse());
					request.setAttribute("tipologiaCommessa", commesseDAO.caricamentoTipologiaCommessa());
					request.setAttribute("listaCommesseTipologiaAltro", commesseDAO.caricamentoCommesseTipologiaAltro());
				} else if (azione.equals("ricercaCommessa")) {
					request.setAttribute("listaClienti",cDAO.caricamentoClienti());
					request.setAttribute("tipologiaCommessa", commesseDAO.caricamentoTipologiaCommessa());
				}
				/*
				 * ArrayList listaCommesse = commesseDAO.caricamentoCommessa();
				 * request.setAttribute("listaCommesse", listaCommesse);
				 */
	
				if (azione.equals("aggiungiTrattative")) {
					
					log.info("url: /index.jsp?azione=aggiungiTrattative&tipo=tutte&dispositiva=trattative");
					
					getServletContext()
							.getRequestDispatcher(
									"/index.jsp?azione=aggiungiTrattative&tipo=tutte&dispositiva=trattative").forward(request, response);;
				
				} else if (azione.equals("ricercaCommessa")) {
					
					log.info("url: /index.jsp?azione=ricercaCommessa&dispositiva=commessa");
					
					getServletContext()
							.getRequestDispatcher(
									"/index.jsp?azione=ricercaCommessa&dispositiva=commessa").forward(request, response);;
				} else {
					
					log.info("url: /index.jsp?azione=aggiungiCommessa&dispositiva=commessa");
					
					getServletContext()
							.getRequestDispatcher(
									"/index.jsp?azione=aggiungiCommessa&dispositiva=commessa").forward(request, response);
				}
				
	
			} else if (azione.equals("inserisciTrattative") || azione.equals("modificaTrattativa")) {
	
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				/*
				 * in questa sezione effettuo l'inserimento di tutte le trattative
				 * che l'utente
				 */
				
				//mi serve per castare le varie date_inizio e date_fine delle varie commesse
				SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
				
				//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
				SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");
				
				
				TrattativeDTO trattative = new TrattativeDTO();
				
				if(request.getParameter("sceltaTrattativa").equals("1")){
				
					trattative.setId_cliente(request.getParameter("trattattivaSingola_codice"));
					trattative.setId_risorsa(Integer.parseInt(request.getParameter("trattattivaSingola_risorsa")));
					trattative.setContatto(request.getParameter("trattattivaSingola_contatto"));
					trattative.setData(request.getParameter("trattattivaSingola_data"));
					trattative.setOggetto(request.getParameter("trattattivaSingola_oggetto"));
					trattative.setEsito(request.getParameter("trattattivaSingola_esito"));
					trattative.setId_tipologiaTrattative(request.getParameter("sceltaTrattativa"));
					trattative.setNote(request.getParameter("trattattivaSingola_note"));
				}else{
					trattative.setId_cliente(request.getParameter("codice"));
					trattative.setContatto(request.getParameter("contatto"));
					trattative.setData(request.getParameter("data"));
					trattative.setOggetto(request.getParameter("oggetto"));
					trattative.setEsito(request.getParameter("esito"));
					trattative.setId_tipologiaTrattative(request.getParameter("sceltaTrattativa"));
					trattative.setNote(request.getParameter("note"));
				}
	
				// verifico che valore ha assunto azione
				if (azione.equals("inserisciTrattative")) {
	
					// effettuo l'inserimento della trattattiva
					String messaggio = tDAO.inserimentoTrattative(trattative);
					if (messaggio.equals("ok")) {
	
						/*
						 * verifico il valore che assume esito al momento
						 * dell'inserimento della trattativa
						 */
	
						// in questa sezione il parametro esito ha assunto valore
						// "NuovaCommessa"
						if (request.getParameter("esito").equals("nuova commessa") || request.getParameter("trattattivaSingola_esito").equals("nuova commessa")) {
	
							CommessaDTO commessa = new CommessaDTO();
							
							if(request.getParameter("trattattivaSingola_codice") != null && !request.getParameter("trattattivaSingola_codice").equals("")){
								commessa.setId_cliente(request.getParameter("trattattivaSingola_codice"));
							}else{
								commessa.setId_cliente(request.getParameter("codice"));
							}
							
							commessa.setCodiceCommessa(request.getParameter("codiceCommessa"));
							commessa.setData_offerta(request.getParameter("dataOfferta"));
							commessa.setOggetto_offerta(request.getParameter("oggettoOfferta"));
							commessa.setDescrizione(request.getParameter("descrizione"));
							commessa.setSede_lavoro(request.getParameter("sedeLavoro"));
							try {
								commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
							} catch (ParseException e) {
								log.error("errore di conversione data: " + e);
							}
							try {
								commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
							} catch (ParseException e) {
								log.error("errore di conversione data: " + e);
							}
	
							commessa.setImporto(Double.parseDouble(request.getParameter("importo")));
							commessa.setImporto_lettere(request.getParameter("importoLettere"));
							commessa.setPagamento(request.getParameter("pagamento"));
							if(request.getParameter("ore") != null && !request.getParameter("ore").equals("")){
								commessa.setTotaleOre(Integer.parseInt(request.getParameter("ore")));
							}else{
								commessa.setTotaleOre(0);
							}
							commessa.setAl(request.getParameter("al"));
							commessa.setNote(request.getParameter("note"));
							commessa.setStato(request.getParameter("stato"));
							/*
							 * effettuo questo tipo di controllo perchè a seconda di che tipo di trattattiva
							 * viene inserito valorizzo la tipologia.
							 */
							if(request.getParameter("sceltaTrattativa").equals("1")){
								commessa.setTipologia("1");
							}else{
								commessa.setTipologia("2");
							}
							commessa.setId_trattative(tDAO.caricamentoIdTrattativa());
	
							String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa,commessa.getTipologia());
							if (messaggioCommessa.equals("ok")) {
								String verificaInserimentoAssCommessa = "";
								
								
								if(request.getParameter("sceltaTrattativa").equals("1")){
									/*
									 * effettuo il caricamento dei dati che poi verranno inseriti nella tabella Tbl_associaz_risor_comm
									 */
									Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
									asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("trattattivaSingola_risorsa")));
									asscommessa.setId_commessa(commesseDAO.selectIdCommessa());
									try {
										asscommessa.setDataInizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
									} catch (ParseException e) {
										log.error("errore di conversione data: " + e);
									}
									try {
										asscommessa.setDataFine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
									} catch (ParseException e) {
										log.error("errore di conversione data: " + e);
									}
									asscommessa.setTotaleImporto(Double.parseDouble(request.getParameter("importo")));
									
									SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
									
									Calendar calendar = Calendar.getInstance();
									try {
										calendar.setTime(format.parse(request.getParameter("dataInizio")));
									} catch (ParseException e) {
										log.error("errore di conversione data: " + e);
									}
									
									Calendar calendar2 = Calendar.getInstance();
									try {
										calendar2.setTime(format.parse(request.getParameter("dataFine")));
									} catch (ParseException e) {
										log.error("errore di conversione data: " + e);
									}
									
									double giorni = calendar.getTimeInMillis() - calendar2.getTimeInMillis();
									
									long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
									giornieffettivi = Math.abs(giornieffettivi);
									
									System.out.println("I giorni di differenza sono: " + giornieffettivi);
									
									if(request.getParameter("stato").equals("aperta")){
										asscommessa.setAttiva(true);
										verificaInserimentoAssCommessa = commesseDAO.inserimentoAssCommessa(asscommessa);
									}
									
									if (verificaInserimentoAssCommessa.equals("ok")) {
										
										//carico le giornate nella tabella planning
										commesseDAO.caricamentoCalendario(giornieffettivi,calendar,commesseDAO.caricamentoIdAssociazione());
										
										request.setAttribute("messaggio","La modifica della trattattiva e l'associazione con una nuova commessa è avvenuta con successo");
										
										log.info("url: /index.jsp?azione=messaggio");
										
										getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
									}else{
										request.setAttribute("messaggio","Spiacenti la modifica della trattativa con l'associazione a una nuova commessa non è avvenuta con successo.");
										
										log.info("url: /index.jsp?azione=messaggio");
										getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
									}
								} else{
									request.setAttribute("messaggio","La modifica della trattattiva e l'associazione con una nuova commessa è avvenuta con successo");
									
									log.info("url: /index.jsp?azione=messaggio");
									getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
								}
							} else {
								request.setAttribute("messaggio", messaggioCommessa);
								
								log.info("url: /index.jsp?azione=messaggio");
								
								getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
							}
	
						} else {
							request.setAttribute("messaggio","Inserimento trattattiva avvenuta con successo");
							getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
						}
					} else {
						request.setAttribute("messaggio", messaggio);
						getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
					}
				} else {
					
					//modifica trattattiva
					
					trattative.setIdTrattative(Integer.parseInt(request.getParameter("trattativa")));
					String messaggio = tDAO.modificaTrattativa(trattative);
					if (messaggio.equals("ok")) {
	
						/*
						 * verifico il valore che assume esito al momento
						 * dell'inserimento della trattativa
						 */
	
						// in questa sezione il parametro esito ha assunto valore
						// "NuovaCommessa"
						
						String esito = "";
						
						if(request.getParameter("esito") != null){
							esito = request.getParameter("esito");
						}else{
							esito = request.getParameter("trattattivaSingola_esito");
						}
						
						if (esito.equals("nuova commessa")) {
	
							// creo l'istanza della classe Commessa CommessaDTO
							CommessaDTO commessa = new CommessaDTO();
							
							if(request.getParameter("trattattivaSingola_codice") != null && !request.getParameter("trattattivaSingola_codice").equals("")){
								commessa.setId_cliente(request.getParameter("trattattivaSingola_codice"));
							}else{
								commessa.setId_cliente(request.getParameter("codice"));
							}
							
							commessa.setCodiceCommessa(request.getParameter("codiceCommessa"));
							commessa.setData_offerta(request.getParameter("dataOfferta"));
							commessa.setOggetto_offerta(request.getParameter("oggettoOfferta"));
							commessa.setDescrizione(request.getParameter("descrizione"));
							commessa.setSede_lavoro(request.getParameter("sedeLavoro"));
							try {
								commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
							} catch (ParseException e) {
								log.error("errore conversione della data: " + e);
							}
							try {
								commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
							} catch (ParseException e) {
								log.error("errore conversione della data: "+ e);
							}
	
							commessa.setImporto(Double.parseDouble(request.getParameter("importo")));
							commessa.setImporto_lettere(request.getParameter("importoLettere"));
							commessa.setPagamento(request.getParameter("pagamento"));
							if(request.getParameter("ore") != ""){
								commessa.setTotaleOre(Integer.parseInt(request.getParameter("ore")));
							}else{
								commessa.setTotaleOre(0);
							}
							commessa.setAl(request.getParameter("al"));
							commessa.setNote(request.getParameter("note"));
							commessa.setStato(request.getParameter("stato"));
							commessa.setTipologia(request.getParameter("sceltaTrattativa"));
							commessa.setId_trattative(Integer.parseInt(request.getParameter("trattativa")));
	
							String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa,commessa.getTipologia());
	
							if (messaggioCommessa.equals("ok")) {
								
								String verificaInserimentoAssCommessa = "";
								if(request.getParameter("sceltaTrattativa").equals("1")){
									/*
									 * effettuo il caricamento dei dati che poi verranno inseriti nella tabella Tbl_associaz_risor_comm
									 */
									Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
									asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("trattattivaSingola_risorsa")));
									asscommessa.setId_commessa(commesseDAO.selectIdCommessa());
									try {
										asscommessa.setDataInizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
									} catch (ParseException e) {
										log.error("errore conversione della data: "+ e);
									}
									try {
										asscommessa.setDataFine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
									} catch (ParseException e) {
										log.error("errore conversione della data: "+ e);
									}
									asscommessa.setTotaleImporto(commessa.getImporto());
									
									SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
									
									Calendar calendar = Calendar.getInstance();
									try {
										calendar.setTime(format.parse(request.getParameter("dataInizio")));
									} catch (ParseException e) {
										log.error("errore conversione della data: "+ e);
									}
									
									Calendar calendar2 = Calendar.getInstance();
									try {
										calendar2.setTime(format.parse(request.getParameter("dataFine")));
									} catch (ParseException e) {
										log.error("errore conversione della data: "+ e);
									}
									
									double giorni = calendar.getTimeInMillis() - calendar2.getTimeInMillis();
									
									long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
									giornieffettivi = Math.abs(giornieffettivi);
									
									log.info("I giorni di differenza sono: " + giornieffettivi);
									
	
									if(request.getParameter("stato").equals("aperta")){
										asscommessa.setAttiva(true);
										verificaInserimentoAssCommessa = commesseDAO.inserimentoAssCommessa(asscommessa);
									}
									
									if (verificaInserimentoAssCommessa.equals("ok")) {
										/*
										 * carico il calendario nella tabella Tbl_Planning
										 */
										commesseDAO.caricamentoCalendario(giornieffettivi,calendar,commesseDAO.caricamentoIdAssociazione());
										
										request.setAttribute("messaggio","La modifica della trattattiva e l'associazione con una nuova commessa è avvenuta con successo");
										
										log.info("url: /index.jsp?azione=messaggio");
										
										getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
									}else{
										request.setAttribute("messaggio","Spiacenti la modifica della trattativa con l'associazione a una nuova commessa non è avvenuta con successo.");
										
										log.info("url: /index.jsp?azione=messaggio");
										
										getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
									}
								}else{
									request.setAttribute("messaggio","La modifica della trattattiva e l'associazione con una nuova commessa è avvenuta con successo");
									
									log.info("url: /index.jsp?azione=messaggio");
									
									getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
								}
							} else {
								request.setAttribute("messaggio",messaggioCommessa);
								
								log.info("url: /index.jsp?azione=messaggio");
								
								getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
							}
	
							// in questa sezione gestisco il valore "CommessaEsistente" del campo "Esito"
						} else {
							request.setAttribute("messaggio","Modifica trattattiva avvenuta con successo");
							
							log.info("url: /index.jsp?azione=messaggio");
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
						}
					} else {
						request.setAttribute("messaggio", messaggio);
						
						log.info("url: /index.jsp?azione=messaggio");
						
						getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
					}
				}
			}else if (azione.equals("aggiornaTrattativa")) {
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				request.setAttribute("newCodCommessaEsterna",commesseDAO.creazioneCodiceCommessaEsterna());

				TrattativeDTO trattativa = tDAO.aggiornaSingolaTrattativa(Integer.parseInt(request.getParameter("trattativa")));
				/*
				 * effettuo questo ciclo sulle trattative per caricare la descrizione
				 * della risorsa a cui è stata associata.
				 */
				if (trattativa.getId_risorsa() != 0) {
					trattativa.setDescrizioneRisorsa(tDAO.ricercaDescrizioneRisorsa(trattativa.getId_risorsa()));
				}

				request.setAttribute("trattativa", trattativa);
				request.setAttribute("listaClienti",cDAO.caricamentoClienti());
				request.setAttribute("listaRisorse", rDAO.elencoRisorse());

				/*
				 * verifico che tipo di dispositiva per via del fatto che possiamo avere
				 * due tipi di dispositiva una cliente e una trattativa.
				 */

				if (request.getParameter("dispositiva") != null) { 
					if(request.getParameter("dispositiva").equals("cliente")) {
						
						log.info("url: /index.jsp?azione=aggiornaTrattativa&dispositiva=cliente");
						
						getServletContext() .getRequestDispatcher("/index.jsp?azione=aggiornaTrattativa&dispositiva=cliente").forward(request, response);
					} else {
						
						log.info("url: /index.jsp?azione=aggiornaTrattativa&tipo=tutte&dispositiva=trattative");
						
						getServletContext() .getRequestDispatcher("/index.jsp?azione=aggiornaTrattativa&tipo=tutte&dispositiva=trattative").forward(request, response); 
					}
				}
			}
		}else{
			sessioneScaduta(response);
		}
	}
}