package it.azienda.servlet;

import it.azienda.connessione.Connessione;
import it.azienda.dao.AziendaDAO;
import it.azienda.dao.Email;
import it.azienda.dao.UtenteDAO;
import it.azienda.dto.AziendaDTO;
import it.azienda.dto.UtenteDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneAzienda
 */
public class GestioneAzienda extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneAzienda() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		HttpSession sessione = request.getSession();
		
		String messaggio = "";
		
		//questa classe mi serve per la gestione dei passaggi dati tra jsp e servlet
		RequestDispatcher rd = null;
		
		//creo l'istanza della classe UtenteDAO
		UtenteDAO uDAO = new UtenteDAO();
		
		//creo l'istanza della classe AziendaDAO
		AziendaDAO aDAO = new AziendaDAO();
		
		//creo l'istanza della classe Connessione
		Connessione connessione = new Connessione();
		
		Connection conn = null;
		
		//recupero la Connection
		if(request.getParameter("connessione") != null){
			conn = connessione.connessione(request.getParameter("connessione"));
			sessione.setAttribute("modalitaDiConnessione", request.getParameter("connessione"));
		}else{
			conn = (Connection) sessione.getAttribute("connessione");
		}
		
		//setto in sessione la connessione
		sessione.setAttribute("connessione", conn);
		
		//recupero il valore che può assumere l'attributo "azione"
		String azione = request.getParameter("azione");
		
		if(azione.equals("registrazioneAzienda") || azione.equals("modificaAzienda")){
			UtenteDTO utente = new UtenteDTO();
			//recupero tutti i dati relativi all'inserimento dell'azienda
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
				messaggio = aDAO.inserimentoAzienda(azienda, conn);
				
				if(messaggio.equals("")){
					messaggio = "Siamo spiacenti, per via dei problemi tecnici la registrazione non avvenuta con successo. Contattare l'amministrazione";
				}else if(messaggio.equals("Registrazione avvenuta con successo")){
					
					utente.setUsername(request.getParameter("username"));
					utente.setPassword(request.getParameter("password"));
					utente.setId_azienda(aDAO.caricamentoIdAzienda(conn));
					utente.setData_registrazione((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
					utente.setData_login((new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date())));
					
					uDAO.inserimentoUtente(utente, conn);
				}
				Email email = new Email();
				String testoEmail="<html>" +  "<head>" +
			       " <title> Registrazione Account </title>" +
			       "</head>" +  "<body>" +
			       "Gentile cliente " + azienda.getRagioneSociale()+ ",<br>" +
			       "<p> grazie di esserti registrato presso di noi." + "<br><br> I dati d'accesso sono:" + "<br>" + 
			       "username: " + utente.getUsername() + "<br>"+ 
			       "password: " + utente.getPassword() + "<br>"+
			       "<br><br> Presto sarai abilitato all'accesso sul nostro portale. </p><br/> Cordiali Saluti <br><br><br> Lo Staff</p>" +
			       "</body>" +
			       "</html>";
				try {
					email.sendMail(azienda.getMail(), "info.drconsulting@gmail.com", "Iscrizione Nuovo Utente Aziendale", testoEmail);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				
				azienda.setIdAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda());
				
				messaggio = aDAO.modificaAzienda(azienda, conn);
				if(messaggio.equals("ok")){
					messaggio = "Modifica Profilo Azienda avvenuta con successo.";
				}
			}
			request.setAttribute("messaggio", messaggio);
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
			rd.forward(request, response);
		
		}else if(azione.equals("login")){
			
			int idAzienda = Integer.parseInt(request.getParameter("utente"));
			
			UtenteDTO utenteLoggato = uDAO.caricamentoAzienda(idAzienda, conn);
			
			sessione.setAttribute("utenteLoggato", utenteLoggato);
			response.sendRedirect("./index.jsp?azione=homePage");
			
		}
		
	if(sessione.getAttribute("utenteLoggato") != null){
		if(azione.equals("visualizzaAzienda") || azione.equals("aggiornaAzienda")){
			
			/*
			 * in questa sezione effettuo il caricamento del profilo dell'Azienda
			 */
			AziendaDTO azienda = aDAO.visualizzaProfiloAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda(), conn);
			
			request.setAttribute("profiloAzienda", azienda);
			
			if(azione.equals("visualizzaAzienda")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaAzienda");
				rd.forward(request, response);
			}else{
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=aggiornaAzienda");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("logout")){
			
			Enumeration attrNames = sessione.getAttributeNames();
			while (attrNames.hasMoreElements())	{		  
				String valoriSessione = (String) attrNames.nextElement();// Stampa i nomi di tutti gli attributi di sessione
				if(valoriSessione.equals("modalitaDiConnessione")){
					continue;
				}else{
					sessione.removeAttribute(valoriSessione);
					System.out.println(valoriSessione);
				}
			}
			
			if(sessione.getAttribute("modalitaDiConnessione").equals("cvonline")){;
				response.sendRedirect("http://cvonline.tv");
			}else{
				response.sendRedirect("http://drconsulting.tv");
			}
		
		}else if(azione.equals("eliminaProfilo")){
			/*
			 * in questa sezione effettuo l'eliminazione logica dell'azienda
			 */
			
			aDAO.eliminaProfiloAzienda(((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda(), conn);
			
			Enumeration enumerati = sessione.getAttributeNames();
			while (enumerati.hasMoreElements())	{
				String nomeOggetto = (String) enumerati.nextElement();
				System.out.println(nomeOggetto);// Stampa i nomi di tutti gli attributi di sessione
				sessione.removeAttribute(nomeOggetto);
			}
			
			response.sendRedirect("./index.jsp?azione=homePage");
			
			
		}else if(azione.equals("cambioPassword")){
			/*
			 * in questa sezione effettuo l'eliminazione logica dell'azienda
			 */
			
			String messaggioCambioPass = aDAO.cambioPassword(request.getParameter("nuovaPassword"),((UtenteDTO)sessione.getAttribute("utenteLoggato")).getId_azienda(), conn);
			if(messaggioCambioPass.equals("ok")){
				request.setAttribute("messaggio", "Cambio della password avvenuta con successo");
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
