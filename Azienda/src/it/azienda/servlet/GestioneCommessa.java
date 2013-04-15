package it.azienda.servlet;

import it.azienda.dao.AziendaDAO;
import it.azienda.dao.ClienteDAO;
import it.azienda.dao.CommesseDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.AziendaDTO;
import it.azienda.dto.ClienteDTO;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.RisorsaDTO;
import it.azienda.dto.UtenteDTO;
import it.util.log.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Servlet implementation class GestioneCommessa
 */
public class GestioneCommessa extends BaseServlet {
	private static final long serialVersionUID = -7191888491109702174L;
	private MyLogger log = new MyLogger(GestioneCommessa.class);

	// mi serve per castare le varie date_inizio e date_fine delle varie
	// commesse
	SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");

	// mi serve per formattare le varie date_inizio e date_fine nel formato del
	// DB
	SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		final String metodo = "doGet";
		log.start(metodo);
		processRequest(request, response);
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		final String metodo = "doPost";
		log.start(metodo);
		processRequest(request, response);
		log.end(metodo);
	}

	private void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		final String metodo = "processRequest";
		log.start(metodo);

		// mi carico la sessione
		HttpSession sessione = request.getSession();

		RequestDispatcher rd = null;

		// creo l'istanza della classe CommesseDAO
		CommesseDAO commesseDAO = new CommesseDAO(conn.getConnection());

		if (sessione.getAttribute("utenteLoggato") != null) {
			String azione = request.getParameter("azione");

			if (azione.equals("inserisciCommessa") || azione.equals("modificaCommessa")) {

				String tipologia = request.getParameter("tipologiaCommessa");

				/*
				 * verifico che tipoligia è stata passata
				 */
				if (tipologia.equals("1")) {
					/*
					 * - tipologia 1: corrisponde alla commessa esterna singola
					 * dove abbiamo un cliente associato a una risorsa
					 */

					/*
					 * Mi creo un oggetto commessa per recuperare i valori
					 * passati dal form Aggungi Commessa
					 */
					CommessaDTO commessa = new CommessaDTO();

					commessa.setId_cliente(request.getParameter("commessaEsternaSingola_codice"));
					commessa.setCodiceCommessa(request.getParameter("commessaEsternaSingola_codiceCommessa"));

					commessa.setData_offerta(request.getParameter("commessaEsternaSingola_dataOfferta"));
					commessa.setOggetto_offerta(request.getParameter("commessaEsternaSingola_oggettoOfferta"));
					commessa.setDescrizione(request.getParameter("commessaEsternaSingola_descrizione"));
					commessa.setSede_lavoro(request.getParameter("commessaEsternaSingola_sedeLavoro"));

					commessa.setImporto(Double.parseDouble(request.getParameter("commessaEsternaSingola_importo")));
					commessa.setImporto_lettere(request.getParameter("commessaEsternaSingola_importoLettere"));
					commessa.setPagamento(request.getParameter("commessaEsternaSingola_pagamento"));
					if (request.getParameter("commessaEsternaSingola_ore") != null
							&& !request.getParameter("commessaEsternaSingola_ore").equals("")) {
						commessa.setTotaleOre(Integer.parseInt(request.getParameter("commessaEsternaSingola_ore")));
					} else {
						commessa.setTotaleOre(0);
					}
					commessa.setAl(request.getParameter("commessaEsternaSingola_al"));
					commessa.setNote(request.getParameter("commessaEsternaSingola_note"));
					commessa.setStato(request.getParameter("commessaEsternaSingola_stato"));
					commessa.setTipologia(request.getParameter("tipologiaCommessa"));

					if (azione.equals("inserisciCommessa")) {

/*
 * recupero le date e le casto nel formato del server
 * cioè "yyyy-MM-dd"
 */
						try {
							commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataInizio"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}
						try {
							commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataFine"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}

						/*
						 * effettuo l'inserimento della commessa nella tabella
						 * Tbl_Commessa e recupero il risultato valorizzandolo
						 * nella variabile messaggioCommessa.
						 */
						String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

						if (messaggioCommessa.equals("ok")) {

							/*
							 * Mi creo l'oggetto assCommessa che mi serve per
							 * caricare i valori nella tabella
							 * Tbl_Associaz_risor_comm
							 */
							Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
							asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("commessaEsternaSingola_idRisorsa")));
							asscommessa.setId_commessa(commesseDAO.selectIdCommessa());

							/*
							 * in questa parte di codice formatto la data nel
							 * formato del DB cioè "yyyy-MM-dd"
							 */
							try {
								asscommessa.setDataInizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataInizio"))));
							} catch (ParseException e) {
								log.error(metodo, "ParseException", e);
							}
							try {
								asscommessa.setDataFine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataFine"))));
							} catch (ParseException e) {
								log.error(metodo, "ParseException", e);
							}
							asscommessa.setTotaleImporto(Double.parseDouble(request.getParameter("commessaEsternaSingola_importo")));
							asscommessa.setAttiva(true);
							/*
							 * effettuo l'inserimento dei valori recuperati
							 * nella tabella Tbl_Associaz_risor_comm e recupero
							 * il risultato dell'inserimento
							 */

							String verificaInserimentoAssCommessa = commesseDAO
									.inserimentoAssCommessa(asscommessa);
							if (verificaInserimentoAssCommessa.equals("ok")) {
							/*
							 * mi dichiaro questo oggetto perchè mi serve
							 * per convertire le date da stringhe in oggetti
							 * java.util.Date
							 */
								SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

								Calendar calendar = Calendar.getInstance();
								try {
									calendar.setTime(format.parse(request.getParameter("commessaEsternaSingola_dataInizio")));
								} catch (ParseException e) {
									log.error(metodo, "ParseException", e);
								}

								Calendar calendar2 = Calendar.getInstance();
								try {
									calendar2.setTime(format.parse(request.getParameter("commessaEsternaSingola_dataFine")));
								} catch (ParseException e) {
									log.error(metodo, "ParseException", e);
								}

								/*
								 * recupero i giorni di differenza che ci sono
								 * tra la data inizio e la data fine
								 */
								double giorni = calendar.getTimeInMillis() - calendar2.getTimeInMillis();

								long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
								giornieffettivi = Math.abs(giornieffettivi);

								log.debug(metodo, "I giorni di differenza sono: "+ giornieffettivi);

								/*
								 * con questo metodo effettuo l'inserimento
								 * delle giornate nella tabellla Tbl_Planning
								 * 
								 * giorniEffettivi equivale hai giorni di
								 * differenza tra un giorno all'altro. calendar
								 * corrisponde alla data iniziale iDAssociazione
								 * corrisponde all'id appena inserito conn
								 * corrisponde alla connessione aperta
								 */
								commesseDAO.caricamentoCalendario(
									giornieffettivi,
									calendar,
									commesseDAO.caricamentoIdAssociazione());

								request.setAttribute("messaggio","L'inserimento della commessa è avvenuta con successo");
							} else {
								request.setAttribute("messaggio","L'inserimento della commessa non è avvenuta con successo. Contattare l'amministratore");
							}
							
						} else {
							request.setAttribute("messaggio","L'inserimento della commessa non è avvenuta con successo. Contattare l'amministratore");
						}
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					} else {

						/*
						 * questa è la sezione di modifica per la commessa
						 * avente come tipologia "1" cioè
						 * "Commessa Esterna Singola"
						 */

						try {
							commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataInizio"))));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						/*
						 * casto solo la data fine perchè la data inizio è già
						 * nel formato corretto
						 */

						try {
							commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataFine"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}

						commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro")));

						/*
						 * tramite il metodo ModificaCommessa effettuo la
						 * modifica della commessa nella tabella Tbl_Commessa
						 */

						String messaggio = commesseDAO.modificaCommessa(commessa);

						if (messaggio.equals("ok")) {
							request.setAttribute("messaggio","La modifica della commessa è avvenuta con successo.");
						} else {
							request.setAttribute("messaggio", messaggio);
						}
						getServletContext()
							.getRequestDispatcher("/index.jsp?azione=messaggio")
								.forward(request, response);
					}
				} else if (tipologia.equals("2")) {
					/*
					 * - tipologia 2: corrisponde alla commessa esterna multipla
					 * dove abbiamo un cliente associato a tante risorse.
					 */

					CommessaDTO commessa = new CommessaDTO();

					commessa.setId_cliente(request.getParameter("codice"));
					commessa.setCodiceCommessa(request.getParameter("codiceCommessa"));
					commessa.setData_offerta(request.getParameter("dataOfferta"));
					commessa.setOggetto_offerta(request.getParameter("oggettoOfferta"));
					commessa.setDescrizione(request.getParameter("descrizione"));
					commessa.setSede_lavoro(request.getParameter("sedeLavoro"));
					commessa.setImporto(Double.parseDouble(request.getParameter("importo")));
					commessa.setImporto_lettere(request.getParameter("importoLettere"));
					commessa.setPagamento(request.getParameter("pagamento"));
					if (request.getParameter("ore") != null
							&& !request.getParameter("ore").equals("")) {
						commessa.setTotaleOre(Integer.parseInt(request.getParameter("ore")));
					} else {
						commessa.setTotaleOre(0);
					}
					commessa.setAl(request.getParameter("al"));
					commessa.setNote(request.getParameter("note"));
					commessa.setStato(request.getParameter("stato"));
					commessa.setTipologia(request.getParameter("tipologiaCommessa"));

					if (azione.equals("inserisciCommessa")) {

						try {
							commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}
						try {
							commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}

						String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

						if (messaggioCommessa.equals("ok")) {
							request.setAttribute("messaggio","L'inserimento della commessa è avvenuta con successo");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio","L'inserimento della commessa non è avvenuta con successo. Contattare l'amministratore");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					} else {
						
						/*
						 * questa è la sezione di modifica per la commessa
						 * avente come tipologia "2" cioè
						 * "Commessa Esterna Multipla"
						 */
						
						try {
							commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						try {
							commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}

						commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro")));
						String messaggio = commesseDAO.modificaCommessa(commessa);
						if (messaggio.equals("ok")) {
							request.setAttribute("messaggio","La modifica della commessa è avvenuta con successo.");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio", messaggio);
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					}
				} else if (tipologia.equals("3")) {

					/*
					 * - tipologia 3: corrisponde alla commessa interna dove
					 * abbiamo un associazione di molte risorsa legate al
					 * progetto per cui lavora
					 */

					CommessaDTO commessa = new CommessaDTO();

					commessa.setCodiceCommessa(request.getParameter("commessaInterna_codiceCommessa"));
					commessa.setData_offerta(request.getParameter("commessaInterna_dataOfferta"));
					commessa.setOggetto_offerta(request.getParameter("commessaInterna_oggettoOfferta"));
					commessa.setDescrizione(request.getParameter("commessaInterna_descrizione"));
					commessa.setSede_lavoro(request.getParameter("commessaInterna_sedeLavoro"));
					commessa.setImporto(Double.parseDouble(request.getParameter("commessaInterna_importo")));
					commessa.setImporto_lettere(request.getParameter("commessaInterna_importoLettere"));
					commessa.setPagamento(request.getParameter("commessaInterna_pagamento"));
					if (request.getParameter("commessaInterna_ore") != null && !request.getParameter("commessaInterna_ore").equals("")) {
						commessa.setTotaleOre(Integer.parseInt(request.getParameter("commessaInterna_ore")));
					} else {
						commessa.setTotaleOre(0);
					}
					commessa.setAl(request.getParameter("commessaInterna_al"));
					commessa.setNote(request.getParameter("commessaInterna_note"));
					commessa.setStato(request.getParameter("commessaInterna_stato"));
					commessa.setTipologia(request.getParameter("tipologiaCommessa"));

					if (azione.equals("inserisciCommessa")) {
						try {
							commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaInterna_dataInizio"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}
						try {
							commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaInterna_dataFine"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}

						String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

						if (messaggioCommessa.equals("ok")) {
							request.setAttribute("messaggio","L'inserimento della commessa è avvenuta con successo");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio","L'inserimento della commessa non è avvenuta con successo. Contattare l'amministratore");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					} else {
						/*
						 * questa è la sezione di modifica per la commessa
						 * avente come tipologia "3" cioè
						 * "Commessa Interna Multipla"
						 */
						
						try {
							commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaInterna_dataInizio"))));
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						try {
							commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaInterna_dataFine"))));
						} catch (ParseException e) {
							log.error(metodo, "ParseException", e);
						}

						commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro")));
						String messaggio = commesseDAO.modificaCommessa(commessa);

						if (messaggio.equals("ok")) {
							request.setAttribute("messaggio","La modifica della commessa è avvenuta con successo.");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio", messaggio);
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					}
				} else if (tipologia.equals("4")) {

					/*
					 * - tipologia 4: corrisponde alla commesse che non hanno un
					 * periodo di scadenza. Esempio: ferie malattia ecc...
					 */

					CommessaDTO commessa = new CommessaDTO();
					commessa.setCodiceCommessa(request.getParameter("altro_codiceCommessa"));
					commessa.setDescrizione(request.getParameter("altro_descrizione"));
					commessa.setTipologia(tipologia);
					commessa.setNote(request.getParameter("altro_note"));

					if (azione.equals("inserisciCommessa")) {
						String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

						if (messaggioCommessa.equals("ok")) {
							request.setAttribute("messaggio","L'inserimento della commessa è avvenuta con successo");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio","L'inserimento della commessa non è avvenuta con successo. Contattare l'amministratore");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					} else {

						commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro")));
						String messaggio = commesseDAO.modificaCommessa(commessa);
						if (messaggio.equals("ok")) {
							request.setAttribute("messaggio","La modifica della commessa è avvenuta con successo.");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio", messaggio);
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					}
				}
			} else if (azione.equals("visualizzaCommessa")) {

				CommessaDTO commessa = new CommessaDTO();

				if (request.getParameter("codiceCommessa") != null) {
					if(request.getParameter("codiceCommessa").equals("")){
						commessa.setCodiceCommessa("");
					}else {
						commessa.setCodiceCommessa(request.getParameter("codiceCommessa"));
					}
				} 

				if (request.getParameter("codice") != null) {
					if(request.getParameter("codice").equals("")){
						commessa.setId_cliente("");
					}else {
						commessa.setId_cliente(request.getParameter("codice"));
					}
				} 

				if (request.getParameter("stato") != null) {
					if(request.getParameter("stato").equals("")){
						commessa.setStato("");
					}else {
						commessa.setStato(request.getParameter("stato"));
					}
				}

				if (request.getParameter("tipologiaCommessa") != null) {
					if(request.getParameter("tipologiaCommessa").equals("")){
						commessa.setTipologia("");
					}else {
						commessa.setTipologia(request.getParameter("tipologiaCommessa"));
					}
				} 

				int anno = 0;
				if(request.getParameter("anno") != null){
					anno = Integer.parseInt(request.getParameter("anno"));
				}
				
				ArrayList listaCommesse = commesseDAO.caricamentoCommesse(commessa,anno);

				request.setAttribute("listaCommesse", listaCommesse);
				getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaCommesse&dispositiva=commessa").forward(request, response);

			} else if (azione.equals("aggiornaCommessa")
					|| azione.equals("dettaglioCommessa")) {
				/*
				 * in questa sezione effettuo il carimento della singola
				 * commessa per poi visualizzare i dati
				 */
				CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));

				/*
				 * verifico la tipologia perchè le commesse con tipologia 1
				 * (Esterna Singola) recupero la descrizione della risorsa
				 */

				if (commessa.getTipologia().equals("1")) {
					commessa.setDescrizioneRisorsa(commesseDAO.descrizioneRisorsa(commessa.getId_commessa()));
				}

				request.setAttribute("commessa", commessa);

				if (azione.equals("aggiornaCommessa")) {
					getServletContext().getRequestDispatcher("/index.jsp?azione=aggiornaCommessa&dispositiva=commessa").forward(request, response);
				} else {
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCommessa&dispositiva=commessa").forward(request, response);
				}

			} else if (azione.equals("risorseAssociate")) {

				ArrayList listaRisorseAssociate = commesseDAO.caricamentoRisorseCommessa(Integer.parseInt(request.getParameter("parametro")), request.getParameter("tipologia"));

				/*
				 * effettuo il caricamento delle commessa in carico presa in
				 * gestione in modo da poter caricare la barra di navizione
				 * presente dopo il dettaglio della commessa
				 */
				CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));

				request.setAttribute("commessa", commessa);
				request.setAttribute("listaRisorseAssociate",listaRisorseAssociate);

				
				getServletContext().getRequestDispatcher("/index.jsp?azione=risorseAssociate&dispositiva=commessa").forward(request, response);

			} else if (azione.equals("controlloCodiceCommessa")) {
				/*
				 * in questa sezione effettuo il controllo del codiceCommessa
				 * che non venga inserito due volte
				 */
				boolean controlloCommessa = commesseDAO.controlloCodiceCommessa(request.getParameter("codiceCommessa"));
				PrintWriter out;
				try {
					out = response.getWriter();
					out.print(controlloCommessa);
					out.flush();
				} catch (IOException e) {
					log.error(metodo, "IOException", e);
				}
			} else if (azione.equals("risorseDaAssociare")) {

				RisorsaDAO risorsa = new RisorsaDAO(conn.getConnection());

				/*
				 * listaRisorsaAssociate: corrisponde alla lista delle risorse
				 * associate alla commessa listaRisorse: corrisponde alla lista
				 * di tutte le risorse che sono visibili
				 * 
				 * Ricavati questi due ArrayList rimuovo dalla listaRisorse le
				 * risorse già associate alla commessa
				 */
				ArrayList listaRisorsaAssociate = commesseDAO.risorseAssociate(Integer.parseInt(request.getParameter("parametro")));
				ArrayList listaRisorse = risorsa.elencoRisorse();

				for (int x = 0; x < listaRisorsaAssociate.size(); x++) {
					for (int y = 0; y < listaRisorse.size(); y++) {
						if (Integer.parseInt(listaRisorsaAssociate.get(x).toString()) == ((RisorsaDTO) listaRisorse.get(y)).getIdRisorsa()) {
							listaRisorse.remove(y);
						}
					}
				}

				/*
				 * effettuo il caricamento delle commessa in carico presa in
				 * gestione in modo da poter caricare la barra di navizione
				 * presente dopo il dettaglio della commessa
				 */
				CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));

				if (request.getParameter("tipoligia") != null
						&& request.getParameter("tipoligia").equals("4")) {
					request.setAttribute("commessa", commessa);
					request.setAttribute("listaRisorseDaAssociare",listaRisorse);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseDaAssociate&tipologia="+ request.getParameter("tipologia")+ "&parametro="+ request.getParameter("parametro")+ "&dispositiva=commessa");
					rd.forward(request, response);
				} else {
					request.setAttribute("commessa", commessa);
					request.setAttribute("listaRisorseDaAssociare",listaRisorse);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseDaAssociate&codice="+ request.getParameter("codice")+ "&parametro="+ request.getParameter("parametro")+ "&dispositiva=commessa");
					rd.forward(request, response);
				}

			} else if (azione.equals("caricaAssociazione")) {

				/*
				 * in questa sezione effettuo l'associazione tra la risorsa e la
				 * commessa
				 */
				String messaggio = "";
				if (request.getParameter("tipologia") != null
						&& request.getParameter("tipologia").equals("4")) {
					Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
					asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("parametro2")));
					asscommessa.setId_commessa(Integer.parseInt(request.getParameter("commessa")));
					asscommessa.setAttiva(true);
					messaggio = commesseDAO.inserimentoAssCommessa(asscommessa);
				} else {
					Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
					asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("parametro2"))); //Id_Risorsa
					asscommessa.setId_commessa(Integer.parseInt(request.getParameter("parametro"))); //Id_Commessa
					try {
						asscommessa.setDataInizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
					} catch (ParseException e) {
						log.error(metodo, "ParseException", e);
					}
					try {
						asscommessa.setDataFine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
					} catch (ParseException e) {
						log.error(metodo, "ParseException", e);
					}
					asscommessa.setTotaleImporto(Double.parseDouble(request.getParameter("importo")));
					asscommessa.setAl(request.getParameter("al"));
					asscommessa.setAttiva(true);

					messaggio = commesseDAO.inserimentoAssCommessa(asscommessa);

					Calendar calendar = Calendar.getInstance();
					try {
						calendar.setTime(formattaDataWeb.parse(request.getParameter("dataInizio")));
					} catch (ParseException e) {
						log.error(metodo, "ParseException", e);
					}

					Calendar calendar2 = Calendar.getInstance();
					try {
						calendar2.setTime(formattaDataWeb.parse(request.getParameter("dataFine")));
					} catch (ParseException e) {
						log.error(metodo, "ParseException", e);
					}

					double giorni = calendar.getTimeInMillis()- calendar2.getTimeInMillis();

					long giornieffettivi = Math.round(Math.round(giorni / 1000/ 60 / 60 / 24));
					giornieffettivi = Math.abs(giornieffettivi);

					log.debug(metodo, "I giorni di differenza sono: " + giornieffettivi);

					commesseDAO.caricamentoCalendario(giornieffettivi,calendar, commesseDAO.caricamentoIdAssociazione());
				}

				if (messaggio.equals("ok")) {
					RisorsaDAO risorsa = new RisorsaDAO(conn.getConnection());
					/*
					 * listaRisorsaAssociate: corrisponde alla lista delle
					 * risorse associate alla commessa listaRisorse: corrisponde
					 * alla lista di tutte le risorse che sono visibili
					 * 
					 * Ricavati questi due ArrayList rimuovo dalla listaRisorse
					 * le risorse già associate alla commessa
					 */
					ArrayList listaRisorsaAssociate = commesseDAO.risorseAssociate(Integer.parseInt(request.getParameter("parametro")));
					ArrayList listaRisorse = risorsa.elencoRisorse();

					for (int x = 0; x < listaRisorsaAssociate.size(); x++) {
						for (int y = 0; y < listaRisorse.size(); y++) {
							if (Integer.parseInt(listaRisorsaAssociate.get(x).toString()) == ((RisorsaDTO) listaRisorse.get(y)).getIdRisorsa()) {
								listaRisorse.remove(y);
							}
						}
					}

					/*
					 * effettuo il caricamento delle commessa in carico presa in
					 * gestione in modo da poter caricare la barra di navizione
					 * presente dopo il dettaglio della commessa
					 */
					CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));

					request.setAttribute("commessa", commessa);
					request.setAttribute("listaRisorseDaAssociare",listaRisorse);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseDaAssociate&codice="+ request.getParameter("cliente")+ "&parametro="+ Integer.parseInt(request.getParameter("parametro"))+ "&dispositiva=commessa");
					rd.forward(request, response);
				} else {
					request.setAttribute("messaggio","L'associazione della risorsa con la commessa non è avvenuta con successo. Contattare l'amministratore");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}

			} else if (azione.equals("dissociazioneRisorsaCommessa")) {

				/*
				 * recupero i parametri che mi vengono passati
				 */
				int id_associazione = Integer.parseInt(request.getParameter("parametro"));
				String tipologia = request.getParameter("tipologia");

				/*
				 * effettuo il controllo sulla tipologia della commessa
				 * controllando che se fosse una commessa con tipologia 4 cioè
				 * ("Altro") effettuo la dissociazione diretta alla commessa,
				 * metre per le altre tipologie visualizzo il dettaglio
				 * dell'associazione
				 */
				
				/*
				 * in questo procedimento effettuo il caricamento della
				 * singola associazione tra commessa e risorsa
				 */
				Associaz_Risor_Comm associazione_Risorsa_Commessa = commesseDAO.caricamento_Singole_Associazione_Risorsa_Commessa(id_associazione);
				
				if (!tipologia.equals("4")) {
					
					request.setAttribute("associazione_commessa",associazione_Risorsa_Commessa);
				} else {
					/*
					 * effettuo l'eliminazione concreta dell'associazione tra
					 * commessa e risorsa.
					 */

					commesseDAO.elimina_Associazione_Risorsa_con_Commessa_Altro(id_associazione);

					ArrayList listaRisorseAssociate = commesseDAO.caricamentoRisorseCommessa(associazione_Risorsa_Commessa.getId_commessa(),request.getParameter("tipologia"));

					/*
					 * effettuo il caricamento delle commessa in carico presa in
					 * gestione in modo da poter caricare la barra di navizione
					 * presente dopo il dettaglio della commessa
					 */

					CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("commessa")));

					request.setAttribute("commessa", commessa);
					request.setAttribute("listaRisorseAssociate",listaRisorseAssociate);

				}

				if (!tipologia.equals("4")) {
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioAssociazione&dispositiva=commessa");
					rd.forward(request, response);
				} else {
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseAssociate&dispositiva=commessa");
					rd.forward(request, response);
				}

			} else if (azione.equals("modificaDissociazioneRisorsaCommessa")) {

				/*
				 * recupero i valori dal form caricaAssociazione
				 */
				int id_associazione = Integer.parseInt(request.getParameter("parametro"));
				String dataFine = request.getParameter("dataFine");

				/*
				 * mi carico la singola associazione
				 */
				Associaz_Risor_Comm associazione_Risorsa_Commessa = commesseDAO.caricamento_Singole_Associazione_Risorsa_Commessa(id_associazione);

				Date dataFineForm = null;
				Date dataFineCommessa = null;
				try {
					dataFineForm = formattaDataWeb.parse(dataFine);

					// casto la data in formato Web
					dataFineCommessa = formattaDataWeb.parse(associazione_Risorsa_Commessa.getDataFine());
				} catch (ParseException e) {
					log.error(metodo, "ParseException", e);
				}

				/*
				 * verifico che la data inserita chiusura inserita sia
				 * antieccedente alla data fine caricata inizialmente
				 */
				if (dataFineForm.before(dataFineCommessa)) {

					/*
					 * mi creo due oggetti Calendari per caricare le due date
					 * Fine
					 */
					Calendar giorniFine = Calendar.getInstance();
					giorniFine.setTime(dataFineForm);
					giorniFine.add(Calendar.DATE, 1);

					Calendar giornoCommessa = Calendar.getInstance();
					giornoCommessa.setTime(dataFineCommessa);

					double giorni = giorniFine.getTimeInMillis()- giornoCommessa.getTimeInMillis();

					long giornieffettivi = Math.round(Math.round(giorni / 1000/ 60 / 60 / 24));

					/*
					 * recupero i giorni di differenza che ci sono tra la data
					 * digitata con quella data Fine dell'associazione
					 */
					giornieffettivi = Math.abs(giornieffettivi);

					// cato la data fine nel formato del DB
					String date = formattaDataServer.format(giorniFine.getTime());
					
					//effettuo la chiusura della commessa solo in caso di tipologia == 1
					if(commesseDAO.estrazione_tipologia_commessa(associazione_Risorsa_Commessa.getId_commessa()) == 1)
						commesseDAO.chiudiCommessa_Con_Data(formattaDataServer.format(dataFineForm),associazione_Risorsa_Commessa.getId_commessa());
					
					// effettuo la chiusura dell'associazione
					commesseDAO.chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente(formattaDataServer.format(dataFineForm),associazione_Risorsa_Commessa.getId_associazione());

					for (int x = 0; x <= giornieffettivi; x++) {
						String esito = commesseDAO.aggiornaCalendarioChiusuraSingolo(date,associazione_Risorsa_Commessa.getId_associazione());
						if (esito.equals("corretto")) {
							giorniFine.add(Calendar.DATE, 1);
							date = formattaDataServer.format(giorniFine.getTime());
							log.debug(metodo, "chiusura commessa + associazione e giorni avvenuta con successo");
						} else {
							giorniFine.add(Calendar.DATE, 1);
							log.debug(metodo, "chiusura commessa + associazione e giorni non è avvenuta con successo");
						}
					}
				} else if (dataFineForm.after(dataFineCommessa)) {

					Calendar giorniFine = Calendar.getInstance();
					giorniFine.setTime(dataFineForm);

					Calendar giornoCommessa = Calendar.getInstance();
					giornoCommessa.setTime(dataFineCommessa);

					double giorni = giorniFine.getTimeInMillis()- giornoCommessa.getTimeInMillis();

					long giornieffettivi = Math.round(Math.round(giorni / 1000/ 60 / 60 / 24));

					/*
					 * calcolo i giorni di differenza che cè tra la data Fine
					 * iniziale e quella appena inserita.
					 */
					giornieffettivi = Math.abs(giornieffettivi);

					/*
					 * effettuo il prolungamento della data fine relativa
					 * all'associazione.
					 */
					commesseDAO.associaz_Risors_Comm_Data_Fine_Posticipata(formattaDataServer.format(giorniFine.getTime()),associazione_Risorsa_Commessa.getId_associazione());
					
					/*
					 * faccio partire la data di un giorno in avanti rispetto a quella 
					 * che è gia caricata sulla tabella associazione
					 */
					giornoCommessa.add(Calendar.DATE, 1);
					
					commesseDAO.caricamentoCalendario(giornieffettivi-1,giornoCommessa,associazione_Risorsa_Commessa.getId_associazione());

				}

				request.setAttribute("messaggio","Modifica associazione avvenuta correttamente");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);

			} else if (azione.equals("chiudiCommessa")) {

				/*
				 * in questa parte di codice effettuo la chiusa di una commessa
				 * cambiato la data e lo stato da aperto a chiuso e la voce
				 * attivo.
				 */

				CommessaDTO ricercaCommessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));

				/*
				 * con questo metodo effettuo la chiusura della commessa
				 * presente in Tbl_Commesse
				 */
				Date dataChiusuraCommessa = new Date();
				String messaggio = commesseDAO.chiudiCommessa_Con_Data(formattaDataServer.format(dataChiusuraCommessa),ricercaCommessa.getId_commessa());
				if (messaggio.equals("ok")) {
					/*
					 * in questa parte recupero tutte le associazioni che ci
					 * sono tra la commessa e la singola risorsa
					 */
					ArrayList listaAssociazioni = commesseDAO.caricamento_Tutte_Associazione_Risorsa_Commessa(ricercaCommessa.getId_commessa());

					if (listaAssociazioni.size() > 0) {
						for (int y = 0; y < listaAssociazioni.size(); y++) {
							Associaz_Risor_Comm asscommessa = (Associaz_Risor_Comm) listaAssociazioni.get(y);

							/*
							 * effettuata la chiusura della commessa e della sua
							 * associazione mi occupo di chiudere le giornate
							 * relazionate a quella associazione
							 */

							commesseDAO.chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente(formattaDataServer.format(new Date()),asscommessa.getId_associazione());

							Date data = new Date();
							Date dataOdierna = null;
							try {
								dataOdierna = formattaDataWeb.parse(formattaDataWeb.format(data.getTime()));
							} catch (ParseException e) {
								log.error(metodo, "ParseException", e);
							}
							try {
								if (dataOdierna.before(formattaDataWeb.parse(formattaDataWeb.format(formattaDataServer.parse(asscommessa.getDataFine()))))) {
									
									Calendar dateOdierna = Calendar.getInstance();
									dateOdierna.setTime(dataOdierna);
									dateOdierna.add(Calendar.DATE, 1);

									Calendar giornoCommessa = Calendar.getInstance();
									try {
										giornoCommessa.setTime(formattaDataWeb.parse(formattaDataWeb.format(formattaDataServer.parse(asscommessa.getDataFine()))));
									} catch (ParseException e) {
										log.error(metodo, "ParseException", e);
									}

									double giorni = dateOdierna.getTimeInMillis()- giornoCommessa.getTimeInMillis();

									long giornieffettivi = Math.round(Math.round(giorni / 1000/ 60 / 60 / 24));

									/*
									 * recupero i giorni di differenza che ci
									 * sono tra la data digitata con quella data
									 * Fine dell'associazione
									 */
									giornieffettivi = Math.abs(giornieffettivi);

									// cato la data fine nel formato del DB
									String date = formattaDataServer.format(dateOdierna.getTime());

									for (int x = 0; x <= giornieffettivi; x++) {
										String esito = commesseDAO.aggiornaCalendarioChiusuraSingolo(date,asscommessa.getId_associazione());
										if (esito.equals("corretto")) {
											dateOdierna.add(Calendar.DATE, 1);
											date = formattaDataServer.format(dateOdierna.getTime());
											log.debug(metodo, "chiusura commessa + associazione e giorni avvenuta con successo");
										} else {
											dateOdierna.add(Calendar.DATE, 1);
											log.debug(metodo, "chiusura commessa + associazione e giorni non è avvenuta con successo");
										}
									}
								}
							} catch (ParseException e) {
								log.error(metodo, "ParseException", e);
							}

						}
					}
				} else {
					log.debug(metodo, "chiusura commessa non avvenuta con successo");
				}

				/*
				 * in questo parte di codice effettuo il caricamento della
				 * commessa
				 */
				CommessaDTO commessa = commesseDAO.aggiornoCommessa(ricercaCommessa.getId_commessa());

				request.setAttribute("commessa", commessa);

				if (messaggio.equals("ok")) {
					request.setAttribute("chiusuraCommessa","Chiusura commessa avvenuta con successo");
					rd = getServletContext()
							.getRequestDispatcher("/GestioneCommessa?azione=visualizzaCommessa&codiceCommessa=&codice=&stato=&tipologiaCommessa=0");
					rd.forward(request, response);
				} else {
					request.setAttribute("messaggio","Siamo spiacenti abbiamo riscontrato un problema sulla chiusura della commessa. Contattare l'amministratore.");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}

			} else if (azione.equals("chiudiMensilita")) {
				/*
				 * questa sezione mi permette di chiudere, se presenti tutte le
				 * commesse del mese e anno scelto.
				 */

				int mese = Integer.parseInt(request.getParameter("mese"));
				int anno = Integer.parseInt(request.getParameter("anno"));

				int giorni = commesseDAO.calcoloGiorni(mese);

				ArrayList listaCommesseDaChiudere = null;

				for (int x = 1; x < giorni + 1; x++) {

					if (mese < 10) {
						if (x < 10) {
							/*
							 * tramite il metodo chiusura Mensilita vado a
							 * effettuare la chiusura delle giornate del mese
							 * scelto sulla tabella Tbl_Planning
							 */
							commesseDAO.chiusuraMensilita(anno + "-0" + mese+ "-0" + x);

							/*
							 * tramite il metodo "Controllo Chiusura Mensilità
							 * Commessa vado a effettuare la chiusura delle
							 * commesse che ricadono in quel mese scelto sulla
							 * tabella Tbl_Commessa e Tbl_Associaz_Risor_Comm
							 */

							listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilitàCommessa("0" + x+ "-0" + mese + "-" + anno);
							for (int y = 0; y < listaCommesseDaChiudere.size(); y++) {
								String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								if (messaggio.equals("ok")) {
									String messaggioCommessa = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									if (messaggioCommessa.equals("ok")) {
										log.debug(metodo, "Update corretta sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									} else {
										log.warn(metodo, "Update fallita sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									}
								} else {
									log.warn(metodo, "Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								}
							}
						} else {
							commesseDAO.chiusuraMensilita(anno + "-0" + mese+ "-" + x);
							listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilitàCommessa(x+ "-0" + mese + "-" + anno);

							/*
							 * tramite il metodo "Controllo Chiusura Mensilità
							 * Commessa vado a effettuare la chiusura delle
							 * commesse che ricadono in quel mese scelto sulla
							 * tabella Tbl_Commessa e Tbl_Associaz_Risor_Comm
							 */

							for (int y = 0; y < listaCommesseDaChiudere.size(); y++) {
								String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								if (messaggio.equals("ok")) {
									String messaggioCommessa = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									if (messaggioCommessa.equals("ok")) {
										log.debug(metodo, "Update corretta sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									} else {
										log.warn(metodo, "Update fallita sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									}
								} else {
									log.warn(metodo, "Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								}
							}
						}
					} else {
						if (x < 10) {
							commesseDAO.chiusuraMensilita(anno + "-" + mese+ "-0" + x);
							listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilitàCommessa("0" + x+ "-" + mese + "-" + anno);

							/*
							 * tramite il metodo "Controllo Chiusura Mensilità
							 * Commessa vado a effettuare la chiusura delle
							 * commesse che ricadono in quel mese scelto sulla
							 * tabella Tbl_Commessa e Tbl_Associaz_Risor_Comm
							 */

							for (int y = 0; y < listaCommesseDaChiudere.size(); y++) {
								String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								if (messaggio.equals("ok")) {
									String messaggioCommessa = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									if (messaggioCommessa.equals("ok")) {
										log.debug(metodo, "Update corretta sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									} else {
										log.warn(metodo, "Update fallita sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									}
								} else {
									log.warn(metodo, "Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								}
							}
						} else {
							commesseDAO.chiusuraMensilita(anno + "-" + mese+ "-" + x);
							listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilitàCommessa(x + "-"+ mese + "-" + anno);

							/*
							 * tramite il metodo "Controllo Chiusura Mensilità
							 * Commessa vado a effettuare la chiusura delle
							 * commesse che ricadono in quel mese scelto sulla
							 * tabella Tbl_Commessa e Tbl_Associaz_Risor_Comm
							 */

							for (int y = 0; y < listaCommesseDaChiudere.size(); y++) {
								String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								if (messaggio.equals("ok")) {
									String messaggioCommessa = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									if (messaggioCommessa.equals("ok")) {
										log.debug(metodo, "Update corretta sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									} else {
										log.warn(metodo, "Update fallita sulla tabella Tbl_Commesse con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_commessa());
									}
								} else {
									log.warn(metodo, "Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id "+ ((Associaz_Risor_Comm) listaCommesseDaChiudere.get(y)).getId_associazione());
								}
							}
						}
					}
				}

				String messaggio = "";

				switch (mese) {
				case 1:
					messaggio = "Le commesse del mese di Gennaio sono state chiuse correttamente.";
					break;
				case 2:
					messaggio = "Le commesse del mese di Febbraio sono state chiuse correttamente.";
					break;
				case 3:
					messaggio = "Le commesse del mese di Marzo sono state chiuse correttamente.";
					break;
				case 4:
					messaggio = "Le commesse del mese di Aprile sono state chiuse correttamente.";
					break;
				case 5:
					messaggio = "Le commesse del mese di Maggio sono state chiuse correttamente.";
					break;
				case 6:
					messaggio = "Le commesse del mese di Giugno sono state chiuse correttamente.";
					break;
				case 7:
					messaggio = "Le commesse del mese di Luglio sono state chiuse correttamente.";
					break;
				case 8:
					messaggio = "Le commesse del mese di Agosto sono state chiuse correttamente.";
					break;
				case 9:
					messaggio = "Le commesse del mese di Settembre sono state chiuse correttamente.";
					break;
				case 10:
					messaggio = "Le commesse del mese di Ottobre sono state chiuse correttamente.";
					break;
				case 11:
					messaggio = "Le commesse del mese di Novembre sono state chiuse correttamente.";
					break;
				case 12:
					messaggio = "Le commesse del mese di Dicembre sono state chiuse correttamente.";
					break;
				}

				request.setAttribute("messaggio", messaggio);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);

			} else if (azione.equals("controlloDataCommessa")) {

				int idCommessa = Integer.parseInt(request.getParameter("parametro"));
				String data = request.getParameter("data");

				String esitoControlloDataCommessa = commesseDAO.controlloDataCommessa(idCommessa, data);
				PrintWriter out;
				try {
					out = response.getWriter();
					out.print(esitoControlloDataCommessa);
					out.flush();
				} catch (IOException e) {
					log.error(metodo, "IOException", e);
				}

			} else if (azione.equals("controlloDataInizioAssociazione")) {

				int idCommessa = Integer.parseInt(request.getParameter("parametro"));
				String data = request.getParameter("data");

				String esitoControlloDataCommessa = commesseDAO.controlloDataInizio_Associazione(idCommessa, data);
				PrintWriter out;
				try {
					out = response.getWriter();
					out.print(esitoControlloDataCommessa);
					out.flush();
				} catch (IOException e) {
					log.error(metodo, "IOException", e);
				}

			}else if(azione.equals("esportaCommessaPDF")){
				
				//recupero l'id della risorsa
				int id_commessa = Integer.parseInt(request.getParameter("parametro")); 
				
				//mi carico la commessa
				CommessaDTO commessa =  commesseDAO.aggiornoCommessa(id_commessa);
				
				commessa.setListaRisorse(commesseDAO.descrizioneRisorse(commessa.getId_commessa()));
				
				File file = new File(getServletContext().getRealPath("/")+"Commessa_"+ commessa.getCodiceCommessa() +".pdf");
				
				response.setContentType("application/octet-stream; name=\"" + file.getName() + "\"");
				response.setCharacterEncoding("UTF-8");
				response.addHeader("content-disposition", "attachment; filename=\"" + file.getName() + "\"");
				
				
				Document doc = new Document(PageSize.A4, 10, 10, 10, 40);
				doc.addTitle("Commessa_"+ commessa.getCodiceCommessa());
				
				try {
					PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(file));
					
					doc.open();
					
					Font fontIntestazione = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,new BaseColor(122,78,43));
					Font fontIntestazioneCliente = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD,new BaseColor(0,0,0));
					Font fontCorpo = new Font(Font.FontFamily.TIMES_ROMAN, 10, 0,new BaseColor(0,0,0));
					Font fontTitoli = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.UNDERLINE,new BaseColor(0,0,128));
					SimpleDateFormat formattazionePDF = new SimpleDateFormat("dd/MM/yyyy");
					formattazionePDF.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
					SimpleDateFormat formattazionePDFDurata = new SimpleDateFormat("EEEEEE dd MMMMMM yyyy");
					formattazionePDFDurata.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
					Paragraph spazio = new Paragraph("\n");
					
					//carico il logo
					PdfPTable disegnaTabella = new PdfPTable(1);
					disegnaTabella.setWidthPercentage(100);
					
					Image immagine = Image.getInstance(getServletContext().getRealPath("/") + "images/logo_DierreConsulting.gif");
					immagine.setWidthPercentage(100);
					PdfPCell cella = new PdfPCell(immagine);
					cella.setPaddingLeft(30);
					cella.setBorder(0);
					disegnaTabella.addCell(cella);
					doc.add(disegnaTabella);
					
					PdfPTable intestazioni = new PdfPTable(2);
					intestazioni.setWidthPercentage(100);
					
					AziendaDAO aDAO = new AziendaDAO(conn.getConnection());
					AziendaDTO azienda = aDAO.visualizzaProfiloAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda()); 
					
					PdfPCell intestazioneSinistra = new PdfPCell();
					intestazioneSinistra.setPaddingTop(15);
					intestazioneSinistra.setPaddingLeft(30);
					intestazioneSinistra.setBorder(0);
					
					//ragione sociale azienda
					Paragraph intestazioneAzienda = new Paragraph(azienda.getRagioneSociale() + "\n" 
							+ azienda.getIndirizzo() + "\n" 
							+ azienda.getCap() + " " + azienda.getCitta() + " " + azienda.getProvincia() + "\n" 
							+ "P.IVA e Cod. Fisc " + azienda.getPIva(),fontIntestazione);
					intestazioneSinistra.addElement(intestazioneAzienda);
					intestazioni.addCell(intestazioneSinistra);
					
					PdfPCell intestazioneDestra = new PdfPCell();
					intestazioneDestra.setBorder(0);
					intestazioneDestra.addElement(spazio);
					intestazioni.addCell(intestazioneDestra);
					
					ClienteDAO cDAO = new ClienteDAO(conn.getConnection());
					ClienteDTO cliente = cDAO.caricamentoCliente(commessa.getId_cliente(), null);
					
					PdfPCell intestazioneClienteSinistra = new PdfPCell();
					intestazioneClienteSinistra.setBorder(0);
					intestazioneClienteSinistra.addElement(spazio);
					intestazioni.addCell(intestazioneClienteSinistra);
					
					//ragione sociale cliente
					PdfPCell intestazioneClienteDestra = new PdfPCell();
					intestazioneClienteDestra.setBorder(0);
					intestazioneClienteDestra.setPaddingLeft(85);
					Paragraph intestazioneCliente = new Paragraph("Spett.le \n" 
							+  ((cliente.getRagioneSociale() != null)?cliente.getRagioneSociale() + "\n":"") 
							+  ((cliente.getIndirizzo() != null && !cliente.getIndirizzo().equals(""))?cliente.getIndirizzo() + "\n":"")
							+  ((cliente.getCap() != null && !cliente.getCap().equals("") || cliente.getCitta() != null && !cliente.getCitta().equals("") || cliente.getProvincia() != null && !cliente.getProvincia().equals(""))?cliente.getCap() + " " + cliente.getCitta() + " " + cliente.getProvincia() + "\n" :"")
							+  ((cliente.getPIva() != null && !cliente.getPIva().equals(""))?"P.IVA e Cod. Fisc " + cliente.getPIva():""),fontIntestazioneCliente);
					intestazioneClienteDestra.addElement(intestazioneCliente);
					intestazioni.addCell(intestazioneClienteDestra);
					
					doc.add(intestazioni);
					
					PdfPTable corpo = new PdfPTable(1);
					corpo.setWidthPercentage(100);
					
					//data Commessa
					PdfPCell dataCommessa = new PdfPCell();
					dataCommessa.setBorder(0);
					dataCommessa.setPaddingLeft(30);
					Paragraph dateCommessa = new Paragraph("Torino, il " + formattazionePDF.format(formattaDataWeb.parse(commessa.getData_offerta())),fontCorpo);
					dataCommessa.addElement(dateCommessa);
					corpo.addCell(dataCommessa);
					
					//numero Offerta
					PdfPCell offerta = new PdfPCell();
					offerta.setBorder(0);
					offerta.setPaddingLeft(30);
					Paragraph numeroOfferta = new Paragraph("Offerta n. " + commessa.getCodiceCommessa(),fontIntestazioneCliente);
					offerta.addElement(numeroOfferta);
					corpo.addCell(offerta);
					
					//Oggetto Commessa
					PdfPCell oggettoCommessa = new PdfPCell();
					oggettoCommessa.setBorder(0);
					oggettoCommessa.setPaddingLeft(30);
					Chunk objectCommessa = new Chunk("Oggetto: " + commessa.getOggetto_offerta(),fontIntestazioneCliente);
					oggettoCommessa.addElement(objectCommessa);
					corpo.addCell(oggettoCommessa);
					
					PdfPCell scritta = new PdfPCell();
					scritta.setBorder(0);
					scritta.setPaddingLeft(30);
					Paragraph write = new Paragraph("Come da accordi con la presente formalizziamo un offerta per l’attività espressa in oggetto: ",fontCorpo);
					scritta.addElement(write);
					corpo.addCell(scritta);
					
					//Contenuto Consulenza
					
					PdfPCell titoloConsulenza = new PdfPCell();
					titoloConsulenza.setBorder(0);
					titoloConsulenza.setPaddingLeft(30);
					Paragraph titleConsulenza = new Paragraph("- Contenuto della consulenza: ",fontTitoli);
					titoloConsulenza.addElement(titleConsulenza);
					corpo.addCell(titoloConsulenza);
					
					PdfPCell contenutoTitoloConsulenza = new PdfPCell();
					contenutoTitoloConsulenza.setBorder(0);
					contenutoTitoloConsulenza.setPaddingLeft(50);
					Paragraph contenutoTitleConsulenza = new Paragraph(commessa.getNote(),fontCorpo);
					contenutoTitoloConsulenza.addElement(contenutoTitleConsulenza);
					corpo.addCell(contenutoTitoloConsulenza);
					
					//Nominativo Risorsa
					PdfPCell nominativoRisorsa = new PdfPCell();
					nominativoRisorsa.setBorder(0);
					nominativoRisorsa.setPaddingLeft(30);
					Paragraph titoloNominativo = new Paragraph("- Risorsa: ",fontTitoli);
					nominativoRisorsa.addElement(titoloNominativo);
					corpo.addCell(nominativoRisorsa);
					
					String listaRisorse = "";
					
					for(int y = 0; y < commessa.getListaRisorse().size(); y++){
						if(commessa.getListaRisorse().size() == 1){
							listaRisorse = commessa.getListaRisorse().get(y);
						}else if(y == commessa.getListaRisorse().size()-1){
							listaRisorse += commessa.getListaRisorse().get(y);
						}else{
							listaRisorse += commessa.getListaRisorse().get(y) + ",";
						}
					}
					
					PdfPCell contenutoNominativoRisorsa = new PdfPCell();
					contenutoNominativoRisorsa.setBorder(0);
					contenutoNominativoRisorsa.setPaddingLeft(50);
					Paragraph contenutoNominativo = new Paragraph(listaRisorse,fontCorpo);
					contenutoNominativoRisorsa.addElement(contenutoNominativo);
					corpo.addCell(contenutoNominativoRisorsa);
					
					//sede lavoro
					PdfPCell sedeLavoro = new PdfPCell();
					sedeLavoro.setBorder(0);
					sedeLavoro.setPaddingLeft(30);
					Paragraph titoloSedeLavoro = new Paragraph("- Sede Lavoro: ",fontTitoli);
					sedeLavoro.addElement(titoloSedeLavoro);
					corpo.addCell(sedeLavoro);
					
					PdfPCell contenutoSedeLavoro = new PdfPCell();
					contenutoSedeLavoro.setBorder(0);
					contenutoSedeLavoro.setPaddingLeft(50);
					Paragraph contentSedeLavoro = new Paragraph(commessa.getSede_lavoro(),fontCorpo);
					contenutoSedeLavoro.addElement(contentSedeLavoro);
					corpo.addCell(contenutoSedeLavoro);
					
					//durata
					PdfPCell durata = new PdfPCell();
					durata.setBorder(0);
					durata.setPaddingLeft(30);
					Paragraph titoloDurata = new Paragraph("- Durata: ",fontTitoli);
					durata.addElement(titoloDurata);
					corpo.addCell(durata);
					
					formattaDataWeb.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
					
					Calendar calendarioPartenza = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome")); 
					calendarioPartenza.setTime(formattaDataWeb.parse(commessa.getData_inizio()));
					
					Calendar calendarioFine = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome")); 
					calendarioFine.setTime(formattaDataWeb.parse(commessa.getData_fine()));
					
					PdfPCell contenutoDurata = new PdfPCell();
					contenutoDurata.setBorder(0);
					contenutoDurata.setPaddingLeft(50);
					Paragraph contentDurata = new Paragraph("a partire da " + formattazionePDFDurata.format(calendarioPartenza.getTime()) + " fino a " + formattazionePDFDurata.format(calendarioFine.getTime()),fontCorpo);
					contenutoDurata.addElement(contentDurata);
					corpo.addCell(contenutoDurata);
					
					//valutazione
					PdfPCell valutazione = new PdfPCell();
					valutazione.setBorder(0);
					valutazione.setPaddingLeft(30);
					Paragraph titoloValutazione = new Paragraph("- Valutazione Economica: ",fontTitoli);
					valutazione.addElement(titoloValutazione);
					corpo.addCell(valutazione);
					
					PdfPCell contenutoValutazione = new PdfPCell();
					contenutoValutazione.setBorder(0);
					contenutoValutazione.setPaddingLeft(50);
					Paragraph contentValutazione = new Paragraph("Tale consulenza è quotata " + commessa.getImporto() + ((!commessa.getImporto_lettere().equals(""))?" (" + commessa.getImporto_lettere() + ")":"") + " IVA " + ((!commessa.getAl().equals(""))?" al " + commessa.getAl() + ".\n":".\n") 
							+ "Si considera una giornata di lavoro composta da 8 ore. Le ore giornaliere eccedenti saranno fatturate come da accordi già in essere.",fontCorpo);
					contenutoValutazione.addElement(contentValutazione);
					corpo.addCell(contenutoValutazione);
					
					//fatturazione
					PdfPCell fatturazione = new PdfPCell();
					fatturazione.setBorder(0);
					fatturazione.setPaddingLeft(30);
					Paragraph titoloFatturazione = new Paragraph("- Fatturazione: ",fontTitoli);
					fatturazione.addElement(titoloFatturazione);
					corpo.addCell(fatturazione);
					
					PdfPCell contenutoFatturazione = new PdfPCell();
					contenutoFatturazione.setBorder(0);
					contenutoFatturazione.setPaddingLeft(50);
					Paragraph contentFatturazione = new Paragraph("Ogni fine mese con allegato report delle giornate lavorate che dovrà essere da voi confermato",fontCorpo);
					contenutoFatturazione.addElement(contentFatturazione);
					corpo.addCell(contenutoFatturazione);
					
					//pagamento
					PdfPCell pagamento = new PdfPCell();
					pagamento.setBorder(0);
					pagamento.setPaddingLeft(30);
					Paragraph titoloPagamento = new Paragraph("- Pagamento: ",fontTitoli);
					pagamento.addElement(titoloPagamento);
					corpo.addCell(pagamento);
					
					PdfPCell contenutoPagamento = new PdfPCell();
					contenutoPagamento.setBorder(0);
					contenutoPagamento.setPaddingLeft(50);
					Paragraph contentPagamento = new Paragraph(((!commessa.getPagamento().equals(""))?commessa.getPagamento() + " gg Fine Mese Data Fattura":""),fontCorpo);
					contenutoPagamento.addElement(contentPagamento);
					corpo.addCell(contenutoPagamento);
					
					
					for(int x = 0; x < 1; x++){
						PdfPCell vuoto = new PdfPCell();
						vuoto.setBorder(0);
						vuoto.setPaddingLeft(30);
						vuoto.addElement(spazio);
						corpo.addCell(vuoto);
					}
					
					PdfPCell conclusione = new PdfPCell();
					conclusione.setBorder(0);
					conclusione.setPaddingLeft(30);
					Paragraph contentConclusione = new Paragraph("Attendiamo vostre considerazioni in merito e cogliamo l’occasione per porgere i nostri più cordiali saluti.",fontCorpo);
					conclusione.addElement(contentConclusione);
					corpo.addCell(conclusione);
					
					doc.add(corpo);
					
					PdfPTable firma = new PdfPTable(2);
					firma.setWidthPercentage(100);
					
					PdfPCell vuoto = new PdfPCell();
					vuoto.setBorder(0);
					vuoto.setPaddingLeft(30);
					vuoto.addElement(spazio);
					firma.addCell(vuoto);
					
					firma.addCell(vuoto);
					
					firma.addCell(vuoto);
					
					PdfPCell intestazionePieDiPagina = new PdfPCell();
					intestazionePieDiPagina.setBorder(0);
					intestazionePieDiPagina.setPaddingLeft(85);
					Paragraph contenuto = new Paragraph("L’amministratore Unico",fontCorpo);
					intestazionePieDiPagina.addElement(contenuto);
					firma.addCell(intestazionePieDiPagina);
					
					PdfPCell accetazione = new PdfPCell();
					accetazione.setBorder(0);
					accetazione.setPaddingLeft(95);
					Paragraph titoloAccetazione = new Paragraph("Per accettazione",fontCorpo);
					accetazione.addElement(titoloAccetazione);
					firma.addCell(accetazione);
					
					
					PdfPCell proprietario = new PdfPCell();
					proprietario.setBorder(0);
					proprietario.setPaddingLeft(100);
					Paragraph contenutoProprietario = new Paragraph("Camarca Roberto",fontCorpo);
					proprietario.addElement(contenutoProprietario);
					firma.addCell(proprietario);
					
					PdfPCell puntini = new PdfPCell();
					puntini.setBorder(0);
					puntini.setPaddingLeft(50);
					Paragraph titoloPuntini = new Paragraph(".........................................................",fontCorpo);
					puntini.addElement(titoloPuntini);
					firma.addCell(puntini);
					
					Image firmaDigitale = Image.getInstance(getServletContext().getRealPath("/") + "images/FirmaDigitale.gif");
					firmaDigitale.setWidthPercentage(50);
					PdfPCell vuoto2 = new PdfPCell();
					vuoto2.setBorder(0);
					vuoto2.setPaddingLeft(90);
					vuoto2.addElement(firmaDigitale);
					firma.addCell(vuoto2);
					
					doc.add(firma);
					
					doc.close();
					
					FileInputStream fileInputStream = new FileInputStream(file);
					ServletOutputStream out = response.getOutputStream();
					int i;
					while ((i=fileInputStream.read()) != -1)
						out.write(i);
					fileInputStream.close();
					out.close();
					
					new File(getServletContext().getRealPath("/")+"Commessa_"+ commessa.getCodiceCommessa() +".pdf").delete();
					
				} catch (BadElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}// qui terminano gli if();
		} else {
			sessioneScaduta(response);
		}
		log.end(metodo);
	}
}