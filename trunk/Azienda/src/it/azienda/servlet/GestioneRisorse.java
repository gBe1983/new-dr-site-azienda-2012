package it.azienda.servlet;

import it.azienda.dao.Email;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dao.UtenteDAO;
import it.azienda.dto.RisorsaDTO;
import it.azienda.dto.UtenteDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneRisorse
 */
public class GestioneRisorse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneRisorse() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession sessione = request.getSession();
		
		RequestDispatcher rd = null;
		
		//recupeto la Connection
		Connection conn = (Connection)sessione.getAttribute("connessione");
		
		//creo l'istanza della classe RisorsaDAO
		RisorsaDAO rDAO = new RisorsaDAO();
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
				String messaggio = rDAO.inserimentoRisorsa(risorsa, conn);
				
				if(messaggio.equals("ok")){
					//creo l'stanza dell'oggetto UtenteDTO per accogliere i valori 
					//che andranno poi a essere inseriti
					UtenteDTO utente = new UtenteDTO();
					
					/*
					 * creo l'stanza dell'oggetto UtenteDAO per richiamare il
					 * metodo che andrà a inserire l'utenza della risorsa 
					 */
					UtenteDAO uDAO = new UtenteDAO();
					
					utente.setUsername(request.getParameter("username"));
					utente.setPassword(request.getParameter("password"));
					utente.setData_registrazione((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
					utente.setData_login((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
					utente.setId_risorsa(uDAO.caricamentoIdRisorsa(conn));
					
					String messaggioUtenteRisorsa = uDAO.inserimentoUtente(utente, conn);
				
					if(messaggio.equals("ok")){
						Email email = new Email();

						if(sessione.getAttribute("modalitaDiConnessione").equals("cvonline")){
								String testoEmail = "<html>"
									+ "<head>"
									+ " <title> Registrazione Account Azienda</title>"
									+ "</head>"
									+ "<body>"
									+ "Gentile " + risorsa.getCognome() + " " + risorsa.getNome() +",<br>"
									+ "<p> sei stato registrato nel portale dell'azienda.<br>"
									+ "Per effettuare la login cliccare su questo <a href=\"http://cvonline.tv/index.php?azione=login\"> Login </a>." 
									+ "<br><br> I dati d'accesso al portale sono:"
									+ "<br>"
									+ "username: "
									+ utente.getUsername()
									+ "<br>"
									+ "password: "
									+ utente.getPassword()
									+ "<br>"
									+ "<br><br/> <p>Cordiali Saluti</p>"
									+ "</body>" + "</html>";
								try {
									email.sendMail(risorsa.getEmail(),
											"info.drconsulting@gmail.com",
											"Iscrizione Account Aziendale", testoEmail);
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								String testoEmail = "<html>"
									+ "<head>"
									+ " <title> Registrazione Account Azienda</title>"
									+ "</head>"
									+ "<body>"
									+ "Gentile " + risorsa.getCognome() + " " + risorsa.getNome() +",<br>"
									+ "<p> sei stato registrato nel portale dell'azienda.<br>"
									+ "Per effettuare la login cliccare su questo <a href=\"http://drconsulting.tv/index.php?azione=login\"> Login </a>." 
									+ "<br><br> I dati d'accesso al portale sono:"
									+ "<br>"
									+ "username: "
									+ utente.getUsername()
									+ "<br>"
									+ "password: "
									+ utente.getPassword()
									+ "<br>"
									+ "<br><br/> <p>Cordiali Saluti <br>Roberto Camarca</p>"
									+ "</body>" + "</html>";
								try {
									email.sendMail(risorsa.getEmail(),
											"info.drconsulting@gmail.com",
											"Iscrizione Account Aziendale", testoEmail);
								} catch (MessagingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						/*
						 * qua effettuo il caricamento dell'intero calendario
						 * personalizzato alla risorsa
						 */
						
						
						request.setAttribute("messaggio", "Inserimento della risorsa avvenuta correttamente");
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}else{
						request.setAttribute("messaggio", messaggio);
						rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
						rd.forward(request, response);
					}
					
				}else{
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}
				
				
			}else if(azione.equals("modificaRisorsa")){
				
				/*
				 * in questa sezione effettuo la modifica della risorsa
				 * effettuando un controllo sull'esito dell'inserimento della risorsa
				 */
				risorsa.setIdRisorsa(Integer.parseInt(request.getParameter("idRisorsa")));
				
				String messaggio = rDAO.modificaRisorsa(risorsa, conn);
				
				request.setAttribute("messaggio", messaggio);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
				
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
			
			ArrayList listaRisorse = rDAO.ricercaRisorse(risorsa, conn);
			if(listaRisorse.size() != 0){
				sessione.setAttribute("listaRisorse", listaRisorse);
				response.sendRedirect("./index.jsp?azione=visualizzaRisorse&numeroPagina=0&dispositiva=risorsa");
			}else{
				request.setAttribute("messaggio", "La risorsa ricerca non è presente! Inserire i dati correttamente");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}
		}else if(azione.equals("caricamentoProfiloRisorsa")){
			
			/*
			 * in questa sezione effettuo il caricamento del profilo
			 * della risorsa per visualizzarla in seguito nella
			 * pagina AggiungiRisorsa
			 */
			
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			RisorsaDTO risorsa = rDAO.caricamentoProfiloRisorsa(idRisorsa, conn);
			request.setAttribute("risorsa", risorsa);
			
			if(request.getParameter("page").equals("modifica")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaRisorsa&dispositiva=risorsa");
				rd.forward(request, response);
			}else if(request.getParameter("page").equals("dettaglio")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioRisorsa&dispositiva=risorsa");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("eliminaRisorsa")){
			
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			String messaggio = rDAO.eliminaRisorsa(idRisorsa, conn);
			
			request.setAttribute("messaggio", messaggio);
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
			rd.forward(request, response);
		
		}else if(azione.equals("valorizzazioneRisorsa")){
			
			/*
			 * questo metodo viene richiamato, tramite funzione javascript, per valorizzare dinamicamente
			 * le risorse associate a un cliente al momento della ricerca delle trattattive.
			 */
			
			String codiceCliente =request.getParameter("parametro");
			
			String valoriRisorse = rDAO.elencoTrattativeRisorse(codiceCliente, conn);
			
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(valoriRisorse);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else if(azione.equals("caricamentoCredenziali")){
			
			UtenteDTO utente = rDAO.caricamentoCredenziali(Integer.parseInt(request.getParameter("risorsa")), conn);
			RisorsaDTO risorsa = rDAO.caricamentoProfiloRisorsa(Integer.parseInt(request.getParameter("risorsa")), conn);
			
			
			request.setAttribute("risorsa", risorsa);
			sessione.setAttribute("credenziali", utente);
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=credenziali&dispositiva=risorsa");
			rd.forward(request, response);
			
		}else if(azione.equals("modificaCredenziali")){
			
			String messaggio = rDAO.modificaCredenziali(request.getParameter("username"), request.getParameter("password"), Integer.parseInt(request.getParameter("utente")), conn);
			
			if(messaggio.equals("ok")){
				Email email = new Email();
				String testoEmail = "<html>"
						+ "<head>"
						+ " <title> Modifica Credenziali</title>"
						+ "</head>"
						+ "<body>"
						+ "Gentile " + ((UtenteDTO)sessione.getAttribute("credenziali")).getDescrizioneRisorsa() +",<br>"
						+ "<p> le nuove credenziali per accedere al portale dell'azienda sono: <br>"
						+ "username: "
						+ request.getParameter("username")
						+ "<br>"
						+ "password: "
						+ request.getParameter("password")
						+ "<br>"
						+ "Per effettuare la login cliccare su questo <a href=\"http://drconsulting.tv/index.php?azione=login\"> Login </a>." 
						+ "<br><br>"
						+ "<br><p>Cordiali Saluti <br>Roberto Camarca</p>"
						+ "</body>" + "</html>";
				try {
					email.sendMail(((UtenteDTO)sessione.getAttribute("credenziali")).getEmail(),
							"info.drconsulting@gmail.com",
							"Modifica Credenziali", testoEmail);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*
				 * qua effettuo il caricamento dell'intero calendario
				 * personalizzato alla risorsa
				 */
				
				
				request.setAttribute("messaggio", "La modifica delle credenziali della risorsa è avvenuta correttamente");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}else{
				request.setAttribute("messaggio", "La modifica delle credenziali della risorsa non avvenuta correttamente");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}
			
		}
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
						"alert(\"La sessione è scaduta. Rieffettuare la login\");" +
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
