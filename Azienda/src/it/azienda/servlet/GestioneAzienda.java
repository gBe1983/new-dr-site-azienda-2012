package it.azienda.servlet;

import it.azienda.dao.AziendaDAO;
import it.azienda.dao.UtenteDAO;
import it.azienda.dto.AziendaDTO;
import it.azienda.dto.UtenteDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class GestioneAzienda
 */
public class GestioneAzienda extends BaseServlet{
	private static final long serialVersionUID = -7318371187106812683L;
	private Logger log = Logger.getLogger(GestioneAzienda.class);

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("-------------------------------------------------------------------------------");
		log.info("metodo: doGet");
		
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.info("-------------------------------------------------------------------------------");
		log.info("metodo: doPost");
		
		processRequest(request,response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("-------------------------------------------------------------------------------");
		log.info("metodo: processRequest");
		
		HttpSession sessione = request.getSession();
		String messaggio = "";

		UtenteDAO uDAO = new UtenteDAO(conn.getConnection());
		AziendaDAO aDAO = new AziendaDAO(conn.getConnection());

		sessione.setAttribute("connessione", conn.getConnection());//TODO VERIFICARE COME TOGLIERE IL PASSAGGIO DELLA CONNESSIONE ALLE 3 JSP CHE LA SFRUTTANO

		String azione = request.getParameter("azione");
		
		if(azione.equals("registrazioneAzienda") || azione.equals("modificaAzienda")){
			AziendaDTO azienda = new AziendaDTO();
			azienda.setRagioneSociale(request.getParameter("ragioneSociale"));
			azienda.setIndirizzo(request.getParameter("indirizzo"));
			azienda.setCitta(request.getParameter("citta"));
			azienda.setProvincia(request.getParameter("provincia"));
			azienda.setCap(request.getParameter("cap"));
			azienda.setNazione(request.getParameter("nazione"));
			azienda.setTelefono(request.getParameter("telefono"));
			azienda.setFax(request.getParameter("fax"));
			azienda.setMail(request.getParameter("email"));
			azienda.setCodiceFiscale(request.getParameter("codiceFiscale"));
			azienda.setPIva(request.getParameter("pIva"));
			azienda.setIndirizzoLegale(request.getParameter("indirizzoLegale"));
			azienda.setCittaLegale(request.getParameter("cittaLegale"));
			azienda.setProvinciaLegale(request.getParameter("provinciaLegale"));
			azienda.setCapLegale(request.getParameter("capLegale"));
			azienda.setNazioneLegale(request.getParameter("nazioneLegale"));
			azienda.setReferente(request.getParameter("referente"));
			azienda.setTelefonoReferente(request.getParameter("telefonoReferente"));
			azienda.setSito(request.getParameter("sito"));
			azienda.setTrattamentoDati(true);

			if(azione.equals("registrazioneAzienda")){
				messaggio = aDAO.inserimentoAzienda(azienda);
				UtenteDTO utente = new UtenteDTO();
				if(messaggio.equals("")){
					messaggio = "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
				}else if(messaggio.equals("Registrazione avvenuta con successo")){
					
					utente.setUsername(request.getParameter("username"));
					utente.setPassword(request.getParameter("password"));
					utente.setId_azienda(aDAO.caricamentoIdAzienda());
					String now = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
					utente.setData_registrazione(now);
					utente.setData_login(now);
					uDAO.inserimentoUtente(utente);
				}
				StringBuilder testoEmail= new StringBuilder("<html><head><title> Registrazione Account </title></head><body>Gentile cliente ");
				testoEmail	.append(azienda.getRagioneSociale())
							.append(",<br><p> grazie di esserti registrato presso di noi.<br><br> I dati d'accesso sono:<br>username: ")
							.append(utente.getUsername())
							.append("<br>password: ")
							.append(utente.getPassword())
							.append("<br><br><br> Presto sarai abilitato all'accesso sul nostro portale. </p><br/> Cordiali Saluti <br><br><br> Lo Staff</p></body></html>");
				try {
					mail.sendMail(azienda.getMail(), "Iscrizione Nuovo Utente Aziendale", testoEmail.toString());
				} catch (Exception e) {
					log.error("invio mail creazione utente aziendale fallita"+ e);
				}
			}else{
				
				azienda.setIdAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda());
				
				messaggio = aDAO.modificaAzienda(azienda);
				if(messaggio.equals("ok")){
					messaggio = "Modifica Profilo Azienda avvenuta con successo.";
				}
			}
			request.setAttribute("messaggio", messaggio);
			getServletContext()
				.getRequestDispatcher("/index.jsp?azione=messaggio")
					.forward(request, response);
			
		}else if(azione.equals("login")){
			
			log.info("-------------------------------------------------------------------------------");
			log.info("azione: login");
			
			int idAzienda = Integer.parseInt(request.getParameter("utente"));
			UtenteDTO utenteLoggato = uDAO.caricamentoAzienda(idAzienda);
			sessione.setAttribute("utenteLoggato", utenteLoggato);
			
			log.info("url: ./index.jsp?azione=homePage");
			
			response.sendRedirect("./index.jsp?azione=homePage");
		}
		
		if(sessione.getAttribute("utenteLoggato") != null){
			
			if(azione.equals("visualizzaAzienda") || azione.equals("aggiornaAzienda")){//in questa sezione effettuo il caricamento del profilo dell'Azienda
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: " + azione);
				
				AziendaDTO azienda = aDAO.visualizzaProfiloAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda());
				
				request.setAttribute("profiloAzienda", azienda);
				
				log.info("url: /index.jsp?azione="+azione);
				
				getServletContext().getRequestDispatcher("/index.jsp?azione="+azione).forward(request, response);
				
			}else if(azione.equals("logout")){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: " + azione);
				
				clearSession(sessione);
				
				log.info("url: " + prop.getProperty("siteUrl"));
				
				response.sendRedirect(prop.getProperty("siteUrl"));
				
			}else if(azione.equals("eliminaProfilo")){//in questa sezione effettuo l'eliminazione logica dell'azienda
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: " + azione);
				
				aDAO.eliminaProfiloAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda());
				clearSession(sessione);
				
				log.info("url: ./index.jsp?azione=homePage");
				
				response.sendRedirect("./index.jsp?azione=homePage");
			}else if(azione.equals("cambioPassword")){
				//in questa sezione effettuo l'eliminazione logica dell'azienda
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: " + azione);
				
				
				String messaggioCambioPass = aDAO.cambioPassword(request.getParameter("nuovaPassword"),((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_utente());
				if(messaggioCambioPass.equals("ok")){
					
					request.setAttribute("messaggio", "Cambio della password avvenuta con successo");
					
					log.info("url: /index.jsp?azione=messaggio");
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio").forward(request, response);
				}
			}else if(azione.equals("loginEditor")){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: " + azione);
				
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				
				String url = request.getParameter("url");
				
				if(!username.equals("editor")){
					request.setAttribute("url", url);
					
					log.info("url: /editorLogin.jsp?errore=loginErrato");
					
					getServletContext().getRequestDispatcher("/editorLogin.jsp?errore=loginErrato").forward(request, response);
				}else{
				
					int id_azienda = aDAO.loginEditor(username, password);
					
					if(id_azienda == 0){
						request.setAttribute("url", url);
						
						log.info("url: /editorLogin.jsp?errore=loginErrato");
						
						getServletContext().getRequestDispatcher("/editorLogin.jsp?errore=loginErrato").forward(request, response);
					}else{
						//response.sendRedirect("http://localhost:8080/DrConsultingEditor/GestioneLogin?azione=login&utente="+id_azienda);
						
						log.info("url: http://drconsulting.tv/DrConsultingEditor/GestioneLogin?azione=login&utente="+id_azienda);
						
						response.sendRedirect("http://drconsulting.tv/DrConsultingEditor/GestioneLogin?azione=login&utente="+id_azienda);
					}
				}
			}
		}else{
			sessioneScaduta(response);
		}
	}
}