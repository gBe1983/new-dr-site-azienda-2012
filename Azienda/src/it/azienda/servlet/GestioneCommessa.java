package it.azienda.servlet;

import it.azienda.dao.CommesseDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.RisorsaDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneCommessa
 */
public class GestioneCommessa extends BaseServlet {
	private static final long serialVersionUID = 1L;
	
	//mi serve per castare le varie date_inizio e date_fine delle varie commesse
	SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");
	
	//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
	SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// mi carico la sessione
		HttpSession sessione = request.getSession();

		RequestDispatcher rd = null;

		// creo l'istanza della classe CommesseDAO
		CommesseDAO commesseDAO = new CommesseDAO(conn.getConnection());
		
	if(sessione.getAttribute("utenteLoggato") != null){
		String azione = request.getParameter("azione");

		if (azione.equals("inserisciCommessa") || azione.equals("modificaCommessa")) {

			String tipologia = request.getParameter("tipologiaCommessa");
			
			/*
			 * verifico che tipoligia � stata passata
			 */
			if (tipologia.equals("1")) {
				/*
				 * - tipologia 1: corrisponde alla commessa esterna singola dove
				 * abbiamo un cliente associato a una risorsa
				 */
				
				/*
				 * Mi creo un oggetto commessa per recuperare i valori passati dal
				 * form Aggungi Commessa
				 */
				CommessaDTO commessa = new CommessaDTO();

				commessa.setId_cliente(request.getParameter("commessaEsternaSingola_codice"));
				commessa.setCodiceCommessa(request.getParameter("commessaEsternaSingola_codiceCommessa"));
			
				commessa.setData_offerta(request.getParameter("commessaEsternaSingola_dataOfferta"));
				commessa.setOggetto_offerta(request.getParameter("commessaEsternaSingola_oggettoOfferta"));
				commessa.setDescrizione(request.getParameter("commessaEsternaSingola_descrizione"));
				commessa.setSede_lavoro(request.getParameter("commessaEsternaSingola_sedeLavoro"));

				commessa.setImporto(Integer.parseInt(request.getParameter("commessaEsternaSingola_importo")));
				commessa.setImporto_lettere(request.getParameter("commessaEsternaSingola_importoLettere"));
				commessa.setPagamento(request.getParameter("commessaEsternaSingola_pagamento"));
				if(request.getParameter("commessaEsternaSingola_ore") != null && !request.getParameter("commessaEsternaSingola_ore").equals("")){
					commessa.setTotaleOre(Integer.parseInt(request.getParameter("commessaEsternaSingola_ore")));
				}else{
					commessa.setTotaleOre(0);
				}
				commessa.setAl(request.getParameter("commessaEsternaSingola_al"));
				commessa.setNote(request.getParameter("commessaEsternaSingola_note"));
				commessa.setStato(request.getParameter("commessaEsternaSingola_stato"));
				commessa.setTipologia(request.getParameter("tipologiaCommessa"));

				if (azione.equals("inserisciCommessa")) {
					
					/*
					 * recupero le date e le casto nel formato del server cio� "yyyy-MM-dd"
					 */
					try {
						commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataInizio"))));
					} catch (ParseException e4) {
						// TODO Auto-generated catch block
						e4.printStackTrace();
					}
					try {
						commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataFine"))));
					} catch (ParseException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
					
					/*
					 * effettuo l'inserimento della commessa nella tabella Tbl_Commessa e recupero 
					 * il risultato valorizzandolo nella variabile messaggioCommessa. 
					 */
					String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

					if (messaggioCommessa.equals("ok")) {
						
						/*
						 * Mi creo l'oggetto assCommessa che mi serve per caricare i valori
						 * nella tabella Tbl_Associaz_risor_comm
						 */
						Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
						asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("commessaEsternaSingola_idRisorsa")));
						asscommessa.setId_commessa(commesseDAO.selectIdCommessa());
						
						/*
						 * in questa parte di codice formatto la data nel formato del DB cio� "yyyy-MM-dd"
						 */
						try {
							asscommessa.setDataInizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataInizio"))));
						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						try {
							asscommessa.setDataFine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataFine"))));
						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						asscommessa.setTotaleImporto(Integer.parseInt(request.getParameter("commessaEsternaSingola_importo")));
						asscommessa.setAttiva(true);
						/*
						 * effettuo l'inserimento dei valori recuperati nella tabella Tbl_Associaz_risor_comm
						 * e recupero il risultato dell'inserimento
						 */
						
						String verificaInserimentoAssCommessa = commesseDAO.inserimentoAssCommessa(asscommessa);
						if (verificaInserimentoAssCommessa.equals("ok")) {
							
							/*
							 * mi dichiaro questo oggetto perch� mi serve per convertire le 
							 * date da stringhe in oggetti java.util.Date
							 */
							SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
							
							Calendar calendar = Calendar.getInstance();
							try {
								calendar.setTime(format.parse(request.getParameter("commessaEsternaSingola_dataInizio")));
							} catch (ParseException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							Calendar calendar2 = Calendar.getInstance();
							try {
								calendar2.setTime(format.parse(request.getParameter("commessaEsternaSingola_dataFine")));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							/*
							 * recupero i giorni di differenza che ci sono tra
							 * la data inizio e la data fine
							 */
							double giorni = calendar.getTimeInMillis() - calendar2.getTimeInMillis();
							
							long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
							giornieffettivi = Math.abs(giornieffettivi);
							
							System.out.println("I giorni di differenza sono: " + giornieffettivi);
							
							
							
							/*
							 * con questo metodo effettuo l'inserimento delle giornate nella tabellla
							 * Tbl_Planning
							 * 
							 * giorniEffettivi equivale hai giorni di differenza tra un giorno all'altro.
							 * calendar corrisponde alla data iniziale
							 * iDAssociazione corrisponde all'id appena inserito
							 * conn corrisponde alla connessione aperta
							 */
							commesseDAO.caricamentoCalendario(giornieffettivi,calendar,commesseDAO.caricamentoIdAssociazione());
						
						
							request.setAttribute("messaggio","L'inserimento della commessa � avvenuta con successo");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						} else {
							request.setAttribute("messaggio","L'inserimento della commessa non � avvenuta con successo. Contattare l'amministratore");
							rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
							rd.forward(request, response);
						}
					} else {
						request.setAttribute("messaggio","L'inserimento della commessa non � avvenuta con successo. Contattare l'amministratore");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
				} else {
					
					/*
					 * questa � la sezione di modifica per la commessa avente come tipologia "1"
					 * cio� "Commessa Esterna Singola"
					 */
					
					commessa.setData_inizio(request.getParameter("commessaEsternaSingola_dataInizio"));
					
					/*
					 * casto solo la data fine perch� la data inizio � gi� nel formato corretto
					 */
					
					try {
						commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaEsternaSingola_dataFine"))));
					} catch (ParseException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
					commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro"))); 
					
					/*
					 * tramite il metodo ModificaCommessa effettuo la modifica della commessa 
					 * nella tabella Tbl_Commessa
					 */
					
					String messaggio = commesseDAO.modificaCommessa(commessa);
					
					if(messaggio.equals("ok")){
						request.setAttribute("messaggio","La modifica della commessa � avvenuta con successo.");
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}else{
						request.setAttribute("messaggio",messaggio);
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}
				}
			} else if (tipologia.equals("2")) {
				/*
				 * - tipologia 1: corrisponde alla commessa esterna multipla
				 * dove abbiamo un cliente associato a tante risorse.
				 */

				CommessaDTO commessa = new CommessaDTO();

				commessa.setId_cliente(request.getParameter("codice"));
				commessa.setCodiceCommessa(request.getParameter("codiceCommessa"));
				commessa.setData_offerta(request.getParameter("dataOfferta"));
				commessa.setOggetto_offerta(request.getParameter("oggettoOfferta"));
				commessa.setDescrizione(request.getParameter("descrizione"));
				commessa.setSede_lavoro(request.getParameter("sedeLavoro"));
				commessa.setImporto(Integer.parseInt(request.getParameter("importo")));
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
				commessa.setTipologia(request.getParameter("tipologiaCommessa"));

				if (azione.equals("inserisciCommessa")) {
					
					try {
						commessa.setData_inizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

					if (messaggioCommessa.equals("ok")) {
						request.setAttribute("messaggio","L'inserimento della commessa � avvenuta con successo");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					} else {
						request.setAttribute("messaggio","L'inserimento della commessa non � avvenuta con successo. Contattare l'amministratore");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
				} else {
					commessa.setData_inizio(request.getParameter("dataInizio"));
					
					try {
						commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					
					
					commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro"))); 
					String messaggio = commesseDAO.modificaCommessa(commessa); 
					if(messaggio.equals("ok")){
						request.setAttribute("messaggio","La modifica della commessa � avvenuta con successo.");
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}else{
						request.setAttribute("messaggio",messaggio);
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}
				}
			} else if (tipologia.equals("3")) {

				/*
				 * - tipologia 3: corrisponde alla commessa interna dove abbiamo
				 * un associazione di molte risorsa legate al progetto per cui
				 * lavora
				 */

				CommessaDTO commessa = new CommessaDTO();

				commessa.setCodiceCommessa(request.getParameter("commessaInterna_codiceCommessa"));
				commessa.setData_offerta(request.getParameter("commessaInterna_dataOfferta"));
				commessa.setOggetto_offerta(request.getParameter("commessaInterna_oggettoOfferta"));
				commessa.setDescrizione(request.getParameter("commessaInterna_descrizione"));
				commessa.setSede_lavoro(request.getParameter("commessaInterna_sedeLavoro"));
				commessa.setImporto(Integer.parseInt(request.getParameter("commessaInterna_importo")));
				commessa.setImporto_lettere(request.getParameter("commessaInterna_importoLettere"));
				commessa.setPagamento(request.getParameter("commessaInterna_pagamento"));
				if(request.getParameter("commessaInterna_ore") != null && !request.getParameter("commessaInterna_ore").equals("")){
					commessa.setTotaleOre(Integer.parseInt(request.getParameter("commessaInterna_ore")));
				}else{
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaInterna_dataFine"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					String messaggioCommessa = commesseDAO.inserimentoCommessa(commessa, tipologia);

					if (messaggioCommessa.equals("ok")) {
						request.setAttribute("messaggio","L'inserimento della commessa � avvenuta con successo");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					} else {
						request.setAttribute("messaggio","L'inserimento della commessa non � avvenuta con successo. Contattare l'amministratore");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
				} else {
					commessa.setData_inizio(request.getParameter("commessaInterna_dataInizio"));
					
					try {
						commessa.setData_fine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("commessaInterna_dataFine"))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro"))); 
					String messaggio = commesseDAO.modificaCommessa(commessa); 
					
					if(messaggio.equals("ok")){
						request.setAttribute("messaggio","La modifica della commessa � avvenuta con successo.");
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}else{
						request.setAttribute("messaggio",messaggio);
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
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
						request.setAttribute("messaggio","L'inserimento della commessa � avvenuta con successo");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					} else {
						request.setAttribute("messaggio","L'inserimento della commessa non � avvenuta con successo. Contattare l'amministratore");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
				} else {
					
					commessa.setId_commessa(Integer.parseInt(request.getParameter("parametro"))); 
					String messaggio = commesseDAO.modificaCommessa(commessa); 
					if(messaggio.equals("ok")){
						request.setAttribute("messaggio","La modifica della commessa � avvenuta con successo.");
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}else{
						request.setAttribute("messaggio",messaggio);
						rd =getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio"); 
						rd.forward(request,response);
					}
				}
			}
		}else if(azione.equals("visualizzaCommessa")){
			
			CommessaDTO commessa = new CommessaDTO();
			
			if(request.getParameter("codiceCommessa").equals("") || request.getParameter("codiceCommessa") == null){
				commessa.setCodiceCommessa("");
			}else{
				commessa.setCodiceCommessa(request.getParameter("codiceCommessa"));
			}
			
			if(request.getParameter("codice").equals("") || request.getParameter("codice") == null){
				commessa.setId_cliente("");
			}else{
				commessa.setId_cliente(request.getParameter("codice"));
			}
			
			if(request.getParameter("stato").equals("") || request.getParameter("stato") == null){
				commessa.setStato("");
			}else{
				commessa.setStato(request.getParameter("stato"));
			}
			
			if(request.getParameter("tipologiaCommessa").equals("") || request.getParameter("tipologiaCommessa") == null){
				commessa.setTipologia("");
			}else{
				commessa.setTipologia(request.getParameter("tipologiaCommessa"));
			}
			
			ArrayList listaCommesse = commesseDAO.caricamentoCommesse(commessa);
			
			String codiceCommessa = "";
			String codice = "";
			String stato = "";
			String tipologia = "";
			
			if(!request.getParameter("codiceCommessa").equals("") && request.getParameter("codiceCommessa") != null){
				codiceCommessa = request.getParameter("codiceCommessa");
			}
			if(!request.getParameter("codice").equals("") && request.getParameter("codice") != null){
				codice = request.getParameter("codice");
			}
			if(!request.getParameter("stato").equals("") && request.getParameter("stato") != null){
				stato = request.getParameter("stato");
			}
			if(!request.getParameter("tipologiaCommessa").equals("") && request.getParameter("tipologiaCommessa") != null){
				tipologia = request.getParameter("tipologiaCommessa");
			}
			
			
			String url = request.getRequestURL().append("?azione="+ azione + "&codiceCommessa=" + codiceCommessa + "&codice=" + codice + "&stato=" + stato + "&tipologiaCommessa="+ tipologia).toString(); 
			sessione.setAttribute("url", url);
			
			request.setAttribute("listaCommesse", listaCommesse);
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaCommesse&dispositiva=commessa");			
			rd.forward(request, response);
			
		}else if(azione.equals("aggiornaCommessa") || azione.equals("dettaglioCommessa")){
			/*
			 * in questa sezione effettuo il carimento della singola commessa
			 * per poi visualizzare i dati
			 */
			CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));
			
			/*
			 * verifico la tipologia perch� le commesse con tipologia 1 (Esterna Singola) recupero la 
			 * descrizione della risorsa
			 */
			
			if(commessa.getTipologia().equals("1")){
				commessa.setDescrizioneRisorsa(commesseDAO.descrizioneRisorsa(commessa.getId_commessa()));
			}
			
			request.setAttribute("commessa", commessa);
			
			if(azione.equals("aggiornaCommessa")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=aggiornaCommessa&dispositiva=commessa");
			}else{
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCommessa&dispositiva=commessa");
			}
			rd.forward(request, response);
			
		}else if(azione.equals("risorseAssociate")){
			
			ArrayList listaRisorseAssociate = commesseDAO.caricamentoRisorseCommessa(Integer.parseInt(request.getParameter("parametro")),request.getParameter("tipologia"));
			
			/*
			 * effettuo il caricamento delle commessa in carico presa in gestione
			 * in modo da poter caricare la barra di navizione presente dopo il 
			 * dettaglio della commessa
			 */
			CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));
			
			request.setAttribute("commessa", commessa);
			request.setAttribute("listaRisorseAssociate", listaRisorseAssociate);
			
			/*
			 * stato corrisponde allo stato della commessa invece
			 * parametro corrisponde all'id della commessa
			 */
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseAssociate&dispositiva=commessa");
			rd.forward(request, response);

		}else if(azione.equals("controlloCodiceCommessa")){
			/*
			 * in questa sezione effettuo il controllo del codiceCommessa che non
			 * venga inserito due volte
			 */
			boolean controlloCommessa = commesseDAO.controlloCodiceCommessa(request.getParameter("codiceCommessa"));
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(controlloCommessa);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(azione.equals("risorseDaAssociare")){
			
			RisorsaDAO risorsa = new RisorsaDAO(conn.getConnection());
			
			/*
			 * listaRisorsaAssociate: corrisponde alla lista delle risorse associate alla commessa
			 * listaRisorse: corrisponde alla lista di tutte le risorse che sono visibili
			 * 
			 * Ricavati questi due ArrayList rimuovo dalla listaRisorse le risorse gi�
			 * associate alla commessa
			 */
			ArrayList listaRisorsaAssociate = commesseDAO.risorseAssociate(Integer.parseInt(request.getParameter("parametro")));
			ArrayList listaRisorse = risorsa.elencoRisorse();
			
			for(int x = 0; x < listaRisorsaAssociate.size(); x++){
				for(int y = 0; y < listaRisorse.size(); y++){
					if(Integer.parseInt(listaRisorsaAssociate.get(x).toString()) == ((RisorsaDTO)listaRisorse.get(y)).getIdRisorsa()){
						listaRisorse.remove(y);
					}
				}
			}
			
			/*
			 * effettuo il caricamento delle commessa in carico presa in gestione
			 * in modo da poter caricare la barra di navizione presente dopo il 
			 * dettaglio della commessa
			 */
			CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));
			
			if(request.getParameter("tipoligia") != null && request.getParameter("tipoligia").equals("4")){
				request.setAttribute("commessa", commessa);
				request.setAttribute("listaRisorseDaAssociare", listaRisorse);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseDaAssociate&tipologia="+request.getParameter("tipologia")+"&parametro="+request.getParameter("parametro")+"&dispositiva=commessa");
				rd.forward(request, response);
			}else{
				request.setAttribute("commessa", commessa);
				request.setAttribute("listaRisorseDaAssociare", listaRisorse);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseDaAssociate&codice="+request.getParameter("codice")+"&parametro="+request.getParameter("parametro")+"&dispositiva=commessa");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("caricaAssociazione")){
	
			/*
			 * in questa sezione effettuo l'associazione tra la risorsa e la commessa
			 * 
			 */
			String messaggio = "";
			if(request.getParameter("tipologia") != null && request.getParameter("tipologia").equals("4")){
				Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("parametro2")));
				asscommessa.setId_commessa(Integer.parseInt(request.getParameter("commessa")));
				asscommessa.setAttiva(true);
				messaggio = commesseDAO.inserimentoAssCommessa(asscommessa);
			}else{
				Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
				asscommessa.setId_risorsa(Integer.parseInt(request.getParameter("risorsa")));
				asscommessa.setId_commessa(Integer.parseInt(request.getParameter("commessa")));
				try {
					asscommessa.setDataInizio(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataInizio"))));
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					asscommessa.setDataFine(formattaDataServer.format(formattaDataWeb.parse(request.getParameter("dataFine"))));
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				asscommessa.setTotaleImporto(Integer.parseInt(request.getParameter("importo")));
				asscommessa.setAl(request.getParameter("al"));
				asscommessa.setAttiva(true);
				
				messaggio = commesseDAO.inserimentoAssCommessa(asscommessa);
				
				
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(formattaDataWeb.parse(request.getParameter("dataInizio")));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Calendar calendar2 = Calendar.getInstance();
				try {
					calendar2.setTime(formattaDataWeb.parse(request.getParameter("dataFine")));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				double giorni = calendar.getTimeInMillis() - calendar2.getTimeInMillis();
				
				long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
				giornieffettivi = Math.abs(giornieffettivi);
				
				System.out.println("I giorni di differenza sono: " + giornieffettivi);
				
				commesseDAO.caricamentoCalendario(giornieffettivi,calendar,commesseDAO.caricamentoIdAssociazione());
			}
			
			if(messaggio.equals("ok")){
				RisorsaDAO risorsa = new RisorsaDAO(conn.getConnection());
				/*
				 * listaRisorsaAssociate: corrisponde alla lista delle risorse associate alla commessa
				 * listaRisorse: corrisponde alla lista di tutte le risorse che sono visibili
				 * 
				 * Ricavati questi due ArrayList rimuovo dalla listaRisorse le risorse gi�
				 * associate alla commessa
				 */
				ArrayList listaRisorsaAssociate = commesseDAO.risorseAssociate(Integer.parseInt(request.getParameter("commessa")));
				ArrayList listaRisorse = risorsa.elencoRisorse();
				
				for(int x = 0; x < listaRisorsaAssociate.size(); x++){
					for(int y = 0; y < listaRisorse.size(); y++){
						if(Integer.parseInt(listaRisorsaAssociate.get(x).toString()) == ((RisorsaDTO)listaRisorse.get(y)).getIdRisorsa()){
							listaRisorse.remove(y);
						}
					}
				}
				
				/*
				 * effettuo il caricamento delle commessa in carico presa in gestione
				 * in modo da poter caricare la barra di navizione presente dopo il 
				 * dettaglio della commessa
				 */
				CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("commessa")));
				
				request.setAttribute("commessa", commessa);
				request.setAttribute("listaRisorseDaAssociare", listaRisorse);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseDaAssociate&codice="+request.getParameter("cliente")+"&parametro="+Integer.parseInt(request.getParameter("commessa"))+"&dispositiva=commessa");
				rd.forward(request, response);
			}else{
				request.setAttribute("messaggio", "L'associazione della risorsa con la commessa non � avvenuta con successo. Contattare l'amministratore");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("dissociazioneRisorsaCommessa")){
			
			/*
			 * recupero i parametri che mi vengono passati
			 */
			int idCommessa = Integer.parseInt(request.getParameter("commessa"));
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			String tipologia = request.getParameter("tipologia");
			
			/*
			 * effettuo il controllo sulla tipologia della commessa controllando che
			 * se fosse una commessa con tipologia 4 cio� ("Altro") effettuo l'associazione
			 * diretta alla commessa, metre per le altre tipologie visualizzo il 
			 * dettaglio dell'associazione
			 */
			
			if(!tipologia.equals("4")){
				/*
				 * in questo procedimento effettuo il caricamento della singola associazione tra commessa e risorsa
				 */
				Associaz_Risor_Comm associazione_Risorsa_Commessa = commesseDAO.caricamento_Singole_Associazione_Risorsa_Commessa(idCommessa,idRisorsa);
				
				request.setAttribute("associazione_commessa", associazione_Risorsa_Commessa);
			}else{
				/*
				 * effettuo l'eliminazione concreta dell'associazione tra commessa e risorsa.
				 */
				
				commesseDAO.elimina_Associazione_Risorsa_con_Commessa_Altro(idRisorsa,idCommessa);
				
				ArrayList listaRisorseAssociate = commesseDAO.caricamentoRisorseCommessa(idCommessa,request.getParameter("tipologia"));
				
				/*
				 * effettuo il caricamento delle commessa in carico presa in gestione
				 * in modo da poter caricare la barra di navizione presente dopo il 
				 * dettaglio della commessa
				 */
				
				CommessaDTO commessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("commessa")));
				
				request.setAttribute("commessa", commessa);
				request.setAttribute("listaRisorseAssociate", listaRisorseAssociate);
					
			}
			
			if(!tipologia.equals("4")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioAssociazione&dispositiva=commessa");
				rd.forward(request, response);
			}else{
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=risorseAssociate&dispositiva=commessa");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("modificaDissociazioneRisorsaCommessa")){
			
			/*
			 * recupero i valori dal form caricaAssociazione 
			 */
			int idCommessa = Integer.parseInt(request.getParameter("commessa"));
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			String dataFine = request.getParameter("dataFine");
			
			
			/*
			 * mi carico la singola associazione
			 */
			Associaz_Risor_Comm associazione_Risorsa_Commessa = commesseDAO.caricamento_Singole_Associazione_Risorsa_Commessa(idCommessa,idRisorsa);
			
			Date dataFineForm = null; 
			Date dataFineCommessa = null;
			try {
				dataFineForm = formattaDataWeb.parse(dataFine);
				
				//casto la data in formato Web
				dataFineCommessa = formattaDataWeb.parse(formattaDataWeb.format(formattaDataServer.parse(associazione_Risorsa_Commessa.getDataFine())));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			 * verifico che la data inserita chiusura inserita sia antieccedente alla
			 * data fine caricata inizialmente
			 */
			if(dataFineForm.before(dataFineCommessa)){
				
				
				/*
				 * mi creo due oggetti Calendari per caricare
				 * le due date Fine
				 */
				Calendar giorniFine = Calendar.getInstance();
				giorniFine.setTime(dataFineForm);
				
				Calendar giornoCommessa = Calendar.getInstance();
				giornoCommessa.setTime(dataFineCommessa);
				
				double giorni = giorniFine.getTimeInMillis() - giornoCommessa.getTimeInMillis();
				
				long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
				
				/*
				 * recupero i giorni di differenza che ci sono tra la data digitata 
				 * con quella data Fine dell'associazione
				 */
				giornieffettivi = Math.abs(giornieffettivi);
				
				//cato la data fine nel formato del DB
				String date = formattaDataServer.format(giorniFine.getTime());
				
				//effettuo la chiusura dell'associazione
				commesseDAO.chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente(formattaDataServer.format(dataFineForm),associazione_Risorsa_Commessa.getId_associazione());
				
				for(int x = 0; x <= giornieffettivi; x++){
					String esito = commesseDAO.aggiornaCalendarioChiusuraSingolo(date,associazione_Risorsa_Commessa.getId_associazione());
					if(esito.equals("corretto")){
						giorniFine.add(Calendar.DATE, 1);
						date = formattaDataServer.format(giorniFine.getTime());
						System.out.println("chiusura commessa + associazione e giorni avvenuta con successo");
					}else{
						giorniFine.add(Calendar.DATE, 1);
						System.out.println("chiusura commessa + associazione e giorni non � avvenuta con successo");
					}
				}
			}else if(dataFineForm.after(dataFineCommessa)){
				
				Calendar giorniFine = Calendar.getInstance();
				giorniFine.setTime(dataFineForm);
				
				Calendar giornoCommessa = Calendar.getInstance();
				giornoCommessa.setTime(dataFineCommessa);
				
				double giorni = giorniFine.getTimeInMillis() - giornoCommessa.getTimeInMillis();
				
				long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
				
				/*
				 * calcolo i giorni di differenza che c� tra la data Fine iniziale e quella appena inserita.
				 */
				giornieffettivi = Math.abs(giornieffettivi);
				
				/*
				 * effettuo il prolungamento della data fine relativa all'associazione.
				 */
				commesseDAO.chiudi_Associaz_Risors_Comm_Data_Fine_Posticipata(formattaDataServer.format(giorniFine.getTime()),associazione_Risorsa_Commessa.getId_associazione());
				
				/*
				 * carico tutti i giorni di differenza che esisto tra la vecchia data associazione con quella nuova.
				 */
				giornoCommessa.add(Calendar.DATE, 1);
				
				commesseDAO.caricamentoCalendario(giornieffettivi, giornoCommessa, associazione_Risorsa_Commessa.getId_associazione());
				
			}
		
			request.setAttribute("messaggio", "Modifica associazione avvenuta correttamente");
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
			rd.forward(request, response);
			
		}else if(azione.equals("chiudiCommessa")){
			
			/*
			 * in questa parte di codice effettuo la chiusa di una commessa cambiato la data e lo stato
			 * da aperto a chiuso e la voce attivo.
			 */
			
			CommessaDTO ricercaCommessa = commesseDAO.aggiornoCommessa(Integer.parseInt(request.getParameter("parametro")));
			
			/*
			 * con questo metodo effettuo la chiusura della commessa presente in Tbl_Commesse
			 */
			String messaggio = commesseDAO.chiudiCommessa_Con_Data(ricercaCommessa.getId_commessa());
			if(messaggio.equals("ok")){
				/*
				 * in questa parte recupero tutte le associazioni che ci sono tra la commessa e la singola risorsa
				 */
				ArrayList listaAssociazioni = commesseDAO.caricamento_Tutte_Associazione_Risorsa_Commessa(ricercaCommessa.getId_commessa());
				
				if(listaAssociazioni.size() > 0){
					for(int y = 0; y < listaAssociazioni.size(); y++){
						Associaz_Risor_Comm asscommessa = (Associaz_Risor_Comm)listaAssociazioni.get(y);
						
						/*
						 * effettuata la chiusura della commessa e della sua associazione mi occupo 
						 * di chiudere le giornate relazionate a quella associazione
						 */
						
						//effettuo la chiusura dell'associazione
						try {
							commesseDAO.chiudi_Associaz_Risors_Comm_Data_Fine_Antecedente(formattaDataServer.format(formattaDataServer.parse(asscommessa.getDataFine())),asscommessa.getId_associazione());
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						Date data = new Date();
						Date dataOdierna = null;
						try {
							dataOdierna = formattaDataWeb.parse(formattaDataWeb.format(data.getTime()));
						} catch (ParseException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
						try {
							if(dataOdierna.before(formattaDataWeb.parse(formattaDataWeb.format(formattaDataServer.parse(asscommessa.getDataFine()))))){

								/*
								 * mi creo due oggetti Calendari per caricare
								 * le due date Fine
								 */
								Calendar giorniFine = Calendar.getInstance();
								giorniFine.setTime(dataOdierna);
								
								Calendar giornoCommessa = Calendar.getInstance();
								try {
									giornoCommessa.setTime(formattaDataWeb.parse(formattaDataWeb.format(formattaDataServer.parse(asscommessa.getDataFine()))));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								double giorni = giorniFine.getTimeInMillis() - giornoCommessa.getTimeInMillis();
								
								long giornieffettivi = Math.round(Math.round(giorni / 1000 / 60 / 60 / 24));
								
								/*
								 * recupero i giorni di differenza che ci sono tra la data digitata 
								 * con quella data Fine dell'associazione
								 */
								giornieffettivi = Math.abs(giornieffettivi);
								
								//cato la data fine nel formato del DB
								String date = formattaDataServer.format(giorniFine.getTime());
								
																
								for(int x = 0; x <= giornieffettivi; x++){
									String esito = commesseDAO.aggiornaCalendarioChiusuraSingolo(date,asscommessa.getId_associazione());
									if(esito.equals("corretto")){
										giorniFine.add(Calendar.DATE, 1);
										date = formattaDataServer.format(giorniFine.getTime());
										System.out.println("chiusura commessa + associazione e giorni avvenuta con successo");
									}else{
										giorniFine.add(Calendar.DATE, 1);
										System.out.println("chiusura commessa + associazione e giorni non � avvenuta con successo");
									}
								}
							}
						} catch (ParseException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						
					}
				}
			}else{
				System.out.println("chiusura commessa non avvenuta con successo");
			}
			
			/*
			 * in questo parte di codice effettuo il caricamento della commessa
			 */
			CommessaDTO commessa = commesseDAO.aggiornoCommessa(ricercaCommessa.getId_commessa());
			
			request.setAttribute("commessa", commessa);
			
			if(messaggio.equals("ok")){
				request.setAttribute("chiusuraCommessa", "Chiusura commessa avvenuta con successo");
				rd = getServletContext().getRequestDispatcher("/GestioneCommessa?azione=visualizzaCommessa&codiceCommessa=&codice=&stato=&tipologiaCommessa=0");
				rd.forward(request, response);
			}else{
				request.setAttribute("messaggio", "Siamo spiacenti abbiamo riscontrato un problema sulla chiusura della commessa. Contattare l'amministratore.");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("chiudiMensilita")){
			/*
			 * questa sezione mi permette di chiudere, se presenti tutte le commesse 
			 * del mese e anno scelto.
			 */
			
			int mese = Integer.parseInt(request.getParameter("mese"));
			int anno = Integer.parseInt(request.getParameter("anno"));
			
			int giorni = commesseDAO.calcoloGiorni(mese);
			
			ArrayList listaCommesseDaChiudere = null;
			
			for(int x = 1; x < giorni+1; x++){
				
				if(mese < 10){
					if(x < 10){
						/*
						 * tramite il metodo chiusura Mensilita vado a effettuare 
						 * la chiusura delle giornate del mese scelto sulla tabella Tbl_Planning 
						 */
						commesseDAO.chiusuraMensilita(anno+"-0"+mese+"-0"+x);
						
						/*
						 * tramite il metodo "Controllo Chiusura Mensilit� Commessa vado a effettuare
						 * la chiusura delle commesse che ricadono in quel mese scelto sulla tabella
						 * Tbl_Commessa e Tbl_Associaz_Risor_Comm 
						 */
						
						listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilit�Commessa("0"+x+"-0"+mese+"-"+anno);
						for(int y = 0; y < listaCommesseDaChiudere.size(); y++){
							String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							if(messaggio.equals("ok")){
								String messaggioCommessa  = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								if(messaggioCommessa.equals("ok")){
									System.out.println("Update corretta sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}else{
									System.out.println("Update fallita sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}
							}else{
								System.out.println("Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							}
						}
					}else{
						commesseDAO.chiusuraMensilita(anno+"-0"+mese+"-"+x);
						listaCommesseDaChiudere =  commesseDAO.controlloChiusuraMensilit�Commessa(x+"-0"+mese+"-"+anno);
						
						/*
						 * tramite il metodo "Controllo Chiusura Mensilit� Commessa vado a effettuare
						 * la chiusura delle commesse che ricadono in quel mese scelto sulla tabella
						 * Tbl_Commessa e Tbl_Associaz_Risor_Comm 
						 */
						
						for(int y = 0; y < listaCommesseDaChiudere.size(); y++){
							String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							if(messaggio.equals("ok")){
								String messaggioCommessa  = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								if(messaggioCommessa.equals("ok")){
									System.out.println("Update corretta sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}else{
									System.out.println("Update fallita sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}
							}else{
								System.out.println("Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							}
						}
					}
				}else{
					if(x < 10){
						commesseDAO.chiusuraMensilita(anno+"-"+mese+"-0"+x);
						listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilit�Commessa("0"+x+"-"+mese+"-"+anno);
						
						/*
						 * tramite il metodo "Controllo Chiusura Mensilit� Commessa vado a effettuare
						 * la chiusura delle commesse che ricadono in quel mese scelto sulla tabella
						 * Tbl_Commessa e Tbl_Associaz_Risor_Comm 
						 */
						
						for(int y = 0; y < listaCommesseDaChiudere.size(); y++){
							String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							if(messaggio.equals("ok")){
								String messaggioCommessa  = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								if(messaggioCommessa.equals("ok")){
									System.out.println("Update corretta sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}else{
									System.out.println("Update fallita sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}
							}else{
								System.out.println("Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							}
						}
					}else{
						commesseDAO.chiusuraMensilita(anno+"-"+mese+"-"+x);
						listaCommesseDaChiudere = commesseDAO.controlloChiusuraMensilit�Commessa(x+"-"+mese+"-"+anno);
						
						/*
						 * tramite il metodo "Controllo Chiusura Mensilit� Commessa vado a effettuare
						 * la chiusura delle commesse che ricadono in quel mese scelto sulla tabella
						 * Tbl_Commessa e Tbl_Associaz_Risor_Comm 
						 */
						
						for(int y = 0; y < listaCommesseDaChiudere.size(); y++){
							String messaggio = commesseDAO.chiudi_Associaz_Risors_Comm_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							if(messaggio.equals("ok")){
								String messaggioCommessa  = commesseDAO.chiudiCommessa_Senza_Data(((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								if(messaggioCommessa.equals("ok")){
									System.out.println("Update corretta sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}else{
									System.out.println("Update fallita sulla tabella Tbl_Commesse con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_commessa());
								}
							}else{
								System.out.println("Update fallita sulla tabella Tbl_Associaz_Risor_Comm con id " + ((Associaz_Risor_Comm)listaCommesseDaChiudere.get(y)).getId_associazione());
							}
						}
					}
				}
			}
			
			String messaggio = "";
			
			switch(mese){
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
		
		}else if(azione.equals("controlloDataCommessa")){
			
			int idCommessa = Integer.parseInt(request.getParameter("parametro"));
			String data = request.getParameter("data");
			
			String esitoControlloDataCommessa = commesseDAO.controlloDataCommessa(idCommessa, data);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(esitoControlloDataCommessa);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else if(azione.equals("controlloDataInizioAssociazione")){
			
			int idCommessa = Integer.parseInt(request.getParameter("parametro"));
			String data = request.getParameter("data");
			
			String esitoControlloDataCommessa = commesseDAO.controlloDataInizio_Associazione(idCommessa, data);
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(esitoControlloDataCommessa);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}//qui terminano gli if();
	}else{
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
		try {
			out = response.getWriter();
			out.print("<html>" +
					"<head>" +
					"</head>" +
					"<body>" +
					"<script type=\"text/javascript\">" +
					"alert(\"La sessione � scaduta. Rieffettuare la login\");" +
					"url = window.location.href;" +
					"var variabiliUrl = url.split(\"/\");" +
					"for(a=0; a < variabiliUrl.length; a++){" +
					"		if(a == 2){" +
					"			var localVariabili = variabiliUrl[a].split(\":\");" +
					"			for(x=0; x < localVariabili.length; x++){" +
					"				if(localVariabili[x] == \"localhost\"){" +
					"					window.location = \"http://localhost/dr\";" +
					"				}if(localVariabili[x] == \"cvonline\"){" +
					"					window.location.href = \"http://cvonline.tv\";" +
					"				}if(localVariabili[x] == \"drconsulting\"){" +
					"					window.location.href= \"http://drconsulting.tv\";" +
					"				}" +
					"			}" +
					"		}else{" +
					"			continue;" +
					"		}" +
					"}" +
					"</script>" +
					"</body>" +
					"</html>");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
  }
}
