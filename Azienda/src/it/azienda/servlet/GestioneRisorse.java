package it.azienda.servlet;

import it.azienda.dao.RisorsaDAO;
import it.azienda.dao.UtenteDAO;
import it.azienda.dto.RisorsaDTO;
import it.azienda.dto.UtenteDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneRisorse
 */
public class GestioneRisorse extends BaseServlet {
	private static final long serialVersionUID = 4268516633020206244L;
	private MyLogger log;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doGet";
		log.start(metodo);
		processRequest(request,response);
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doPost";
		log.start(metodo);
		processRequest(request,response);
		log.end(metodo);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="processRequest";
		log.start(metodo);
		HttpSession sessione = request.getSession();
		RisorsaDAO rDAO = new RisorsaDAO(conn.getConnection());
		if(sessione.getAttribute("utenteLoggato") != null){
			//recupero il valore dell'attributo azione
			String azione = request.getParameter("azione");
			
			if(azione.equals("aggiungiRisorsa") || azione.equals("modificaRisorsa")){
				RisorsaDTO risorsa = new RisorsaDTO();
				
				//parte anagrafica
				risorsa.setCognome(request.getParameter("cognome"));
				risorsa.setNome(request.getParameter("nome"));
				risorsa.setDataNascita(request.getParameter("dataNascita"));
				risorsa.setLuogoNascita(request.getParameter("luogoNascita"));
				risorsa.setSesso(request.getParameter("sesso"));
				risorsa.setCodiceFiscale(request.getParameter("codiceFiscale"));
				risorsa.setEmail(request.getParameter("mail"));
				risorsa.setTelefono(request.getParameter("telefono"));
				risorsa.setCellulare(request.getParameter("cellulare"));
				risorsa.setFax(request.getParameter("fax"));
				
				//parte residenza
				risorsa.setIndirizzo(request.getParameter("indirizzo"));
				risorsa.setCitta(request.getParameter("citta"));
				risorsa.setProvincia(request.getParameter("provincia"));
				risorsa.setCap(request.getParameter("cap"));
				risorsa.setNazione(request.getParameter("nazione"));
				risorsa.setServizioMilitare(request.getParameter("militare"));
				
				//parte altri dati
				risorsa.setPatente(request.getParameter("patente"));
				if(request.getParameter("costo") != null && request.getParameter("costo") != ""){
					risorsa.setCosto(request.getParameter("costo"));
				}else{
					risorsa.setCosto("0");
				}
				if(request.getParameter("occupato") != null){
					if(request.getParameter("occupato").equals("si")){
						risorsa.setOccupato(true);
					}else{
						risorsa.setOccupato(false);
					}
				}else{
					risorsa.setOccupato(false);
				}
				risorsa.setTipoContratto(request.getParameter("tipoContratto"));
				risorsa.setFiguraProfessionale(request.getParameter("figuraProfessionale"));
				risorsa.setSeniority(request.getParameter("seniority"));
				
				if(azione.equals("aggiungiRisorsa")){
					/*
					 * in questa sezione effettuo l'inserimento della risorsa
					 * effettuando un controllo sull'esito dell'inserimento della risorsa
					 */
					String messaggio = rDAO.inserimentoRisorsa(risorsa);
					
					if(messaggio.equals("ok")){//creo l'stanza dell'oggetto UtenteDTO per accogliere i valori che andranno poi a essere inseriti
						UtenteDAO uDAO = new UtenteDAO(conn.getConnection());
						UtenteDTO utente = new UtenteDTO();
						utente.setUsername(request.getParameter("username"));
						utente.setPassword(request.getParameter("password"));
						utente.setData_registrazione((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
						utente.setData_login((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
						utente.setId_risorsa(uDAO.caricamentoIdRisorsa());
						
						String messaggioUtenteRisorsa = uDAO.inserimentoUtente(utente);//TODO E QUESTO MESSAGGIO?
					
						if(messaggio.equals("ok")){
							StringBuilder testoEmail = new StringBuilder("<html><head><title>Registrazione Account Azienda</title></head><body>Gentile ");
							testoEmail	.append(risorsa.getCognome())
										.append(" ")
										.append(risorsa.getNome())
										.append(",<br><p> sei stato registrato nel portale dell'azienda.<br>")
										.append("Per effettuare la login cliccare su questo <a href=\"")
										.append(prop.getProperty("siteUrl"))
										.append("/index.php?azione=login\"> Login </a>.<br><br> I dati d'accesso al portale sono:<br>username: ")
										.append(utente.getUsername())
										.append("<br>password: ")
										.append(utente.getPassword())
										.append("<br><p>Cordiali Saluti <br>Roberto Camarca</p></body></html>");
							try {
								mail.sendMail(risorsa.getEmail(), "Iscrizione Account Aziendale", testoEmail.toString());
							} catch (Exception e) {
								log.error(metodo, "Invio Mail Iscrizione Account Aziendale Fallita", e);
							}
							request.setAttribute("messaggio", "Inserimento della risorsa avvenuta correttamente");
						}else{
							request.setAttribute("messaggio", messaggio);
						}
					}else{
						request.setAttribute("messaggio", messaggio);
					}
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}else if(azione.equals("modificaRisorsa")){//in questa sezione effettuo la modifica della risorsa effettuando un controllo sull'esito dell'inserimento della risorsa
					risorsa.setIdRisorsa(Integer.parseInt(request.getParameter("idRisorsa")));
					request.setAttribute("messaggio", rDAO.modificaRisorsa(risorsa));
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}
			}else if(azione.equals("ricercaRisorse")){
				
				/*
				 * in questa sezione effettuo la ricerca parziale e/o dettagliata
				 * della risorsa
				 */
				
				RisorsaDTO risorsa = new RisorsaDTO();
				risorsa.setCognome(request.getParameter("cognome"));
				risorsa.setNome(request.getParameter("nome"));
				risorsa.setCosto(request.getParameter("costo"));
				risorsa.setFiguraProfessionale(request.getParameter("figuraProfessionale"));
				risorsa.setSeniority(request.getParameter("seniority"));
				
				ArrayList listaRisorse = rDAO.ricercaRisorse(risorsa);
				if(listaRisorse.size() != 0){
					sessione.setAttribute("listaRisorse", listaRisorse);
					response.sendRedirect("./index.jsp?azione=visualizzaRisorse&numeroPagina=0&dispositiva=risorsa");
				}else{
					request.setAttribute("messaggio", "La risorsa ricerca non è presente! Inserire i dati correttamente");
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=messaggio")
							.forward(request, response);
				}
			}else if(azione.equals("caricamentoProfiloRisorsa")){
				
				/*
				 * in questa sezione effettuo il caricamento del profilo
				 * della risorsa per visualizzarla in seguito nella
				 * pagina AggiungiRisorsa
				 */
				
				int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
				RisorsaDTO risorsa = rDAO.caricamentoProfiloRisorsa(idRisorsa);
				request.setAttribute("risorsa", risorsa);
				if(request.getParameter("page").equals("modifica")){
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=modificaRisorsa&dispositiva=risorsa")
							.forward(request, response);
				}else if(request.getParameter("page").equals("dettaglio")){
					getServletContext()
						.getRequestDispatcher("/index.jsp?azione=dettaglioRisorsa&dispositiva=risorsa")
							.forward(request, response);
				}
			}else if(azione.equals("eliminaRisorsa")){
				request.setAttribute("messaggio", rDAO.eliminaRisorsa(Integer.parseInt(request.getParameter("risorsa"))));
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio")
						.forward(request, response);
			}else if(azione.equals("valorizzazioneRisorsa")){
				/*
				 * questo metodo viene richiamato, tramite funzione javascript, per valorizzare dinamicamente
				 * le risorse associate a un cliente al momento della ricerca delle trattattive.
				 */
				PrintWriter out;
				try {
					out = response.getWriter();
					out.print(rDAO.elencoTrattativeRisorse(request.getParameter("parametro")));
					out.flush();
				} catch (IOException e) {
					log.error(metodo, "valorizzazioneRisorsa Fallita", e);
				}
			}else if(azione.equals("caricamentoCredenziali")){
				request.setAttribute("risorsa", rDAO.caricamentoProfiloRisorsa(Integer.parseInt(request.getParameter("risorsa"))));
				sessione.setAttribute("credenziali", rDAO.caricamentoCredenziali(Integer.parseInt(request.getParameter("risorsa"))));
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=credenziali&dispositiva=risorsa")
						.forward(request, response);
			}else if(azione.equals("modificaCredenziali")){

				String messaggio = rDAO.modificaCredenziali(request.getParameter("username"),
															request.getParameter("password"),
															Integer.parseInt(request.getParameter("utente")));

				if(messaggio.equals("ok")){
					StringBuilder testoEmail = new StringBuilder("<html><head><title> Modifica Credenziali</title></head><body>Gentile ");
					testoEmail	.append(((UtenteDTO)sessione.getAttribute("credenziali")).getDescrizioneRisorsa())
								.append(",<br><p> le nuove credenziali per accedere al portale dell'azienda sono: <br>username: ")
								.append(request.getParameter("username"))
								.append("<br>password: ")
								.append(request.getParameter("password"))
								.append("<br>Per effettuare la login cliccare su questo <a href=\"")
								.append(prop.getProperty("siteUrl"))
								.append("/index.php?azione=login\"> Login </a>.<br><br><br><p>Cordiali Saluti <br>Roberto Camarca</p></body></html>");
					try {
						mail.sendMail(((UtenteDTO)sessione.getAttribute("credenziali")).getEmail(),"Modifica Credenziali", testoEmail.toString());
					} catch (Exception e) {
						log.error(metodo, "Invio Mail Modifica Credenziali Fallita", e);
					}
					request.setAttribute("messaggio", "La modifica delle credenziali della risorsa è avvenuta correttamente");
				}else{
					request.setAttribute("messaggio", "La modifica delle credenziali della risorsa non avvenuta correttamente");
				}
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio")
						.forward(request, response);
			}else if(azione.equals("listaRisorseDaAbilitare")){
				request.setAttribute("listaRisorseDaAbilitare", rDAO.listaRisorseDaAbilitare());
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=listaRisorseDaAbilitare&dispositiva=risorsa")
						.forward(request, response);
			}else if(azione.equals("abilitaRisorsa")){
				int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
				String messaggio = rDAO.abilitaRisorsa(idRisorsa);
				request.setAttribute("messaggio",messaggio.equals("ok")?"L'abilitazione della Risorsa è avvenuta con successo":messaggio);
				getServletContext()
					.getRequestDispatcher("/index.jsp?azione=messaggio")
						.forward(request, response);
			}
		}else{
			sessioneScaduta(response);
		}
		log.end(metodo);
	}
}
