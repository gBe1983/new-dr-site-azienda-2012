package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dto.ClienteDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneCliente
 */
public class GestioneCliente extends BaseServlet {
	private static final long serialVersionUID = 1L;

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
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// TODO Auto-generated method stub
		
		HttpSession sessione = request.getSession();
		
		//creo l'istanza della classe ClienteDAO
		ClienteDAO cDAO = new ClienteDAO(conn.getConnection());
		
		//recupero il parametro "azione" 
	if(sessione.getAttribute("utenteLoggato") != null){
		String azione = request.getParameter("azione");
		
		RequestDispatcher rd = null;
		
		//verifico che valore ha assunto il parametro "Azione"
		if(azione.equals("inserimentoCliente") || azione.equals("modificaCliente")){
			
			//creo l'istanza dell'oggetto ClienteDTO
			ClienteDTO cliente = new ClienteDTO();
			
			//recupero i valori che vengono passati dal form
			cliente.setId_cliente(request.getParameter("codiceCliente"));
			cliente.setRagioneSociale(request.getParameter("ragioneSociale"));
			cliente.setIndirizzo(request.getParameter("indirizzo"));
			cliente.setCap(request.getParameter("cap"));
			cliente.setCitta(request.getParameter("citta"));
			cliente.setProvincia(request.getParameter("provincia"));
			cliente.setPIva(request.getParameter("pIva"));
			cliente.setReferente(request.getParameter("referente"));
			cliente.setTelefono(request.getParameter("telefono"));
			cliente.setCellulare(request.getParameter("cellulare"));
			cliente.setFax(request.getParameter("fax"));
			cliente.setEmail(request.getParameter("email"));
			cliente.setSito(request.getParameter("sito"));
			cliente.setCodFiscale(request.getParameter("codFiscale"));
			
			/*
			 * verifico che tipo di azione sta compiendo l'utente in modo che a 
			 * seconda di come è valorizzata la variabile "azione" compio 
			 * una query piuttosto che un altra.
			 */
			if(azione.equals("inserimentoCliente")){
				String messaggio = cDAO.inserimentoCliente(cliente);
				if(messaggio.equals("ok")){
					request.setAttribute("messaggio", "Inserimento del Cliente avvenuta con successo");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}else {
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}
			}else{
				String messaggio = cDAO.modificaCliente(cliente);
				if(messaggio.equals("ok")){
					request.setAttribute("messaggio", "La modifica del Cliente avvenuta con successo");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}else {
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}
			}
			
		}else if(azione.equals("caricamentoNominativiCliente")){
			/*
			 * in questa sezione effettuo il caricamento di tutti i clienti che sono stati caricati 
			 * nella tabella "Tbl_Cliente"
			 */
			
			ArrayList listaNominativi = cDAO.caricamentoNominativiCliente();
			
			request.setAttribute("nominativi", listaNominativi);	
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaNominativi&dispositiva=cliente");
			rd.forward(request, response);
			
		}else if(azione.equals("ricercaCliente")){
			
			ClienteDTO cliente = null;
			
			/*
			 * il parametro nominativo lo ritroviamo nella pagina Ricerca Cliente
			 */
			if(request.getParameter("nominativo") != null){
				cliente = cDAO.caricamentoCliente(null,request.getParameter("nominativo"));
			}else{
				cliente = cDAO.caricamentoCliente(request.getParameter("codice"),null);
			}
			
			request.setAttribute("cliente", cliente);
			if(request.getParameter("nominativo") != null){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaCliente&dispositiva=cliente");
			}else{
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=aggiornaCliente&dispositiva=cliente");
			}
				rd.forward(request, response);
			
		}else if(azione.equals("disabilitaCliente")){
			
			/*
			 * in questa sezione effettuo l'eliminazione logica del singolo
			 * Cliente che l'Utente ha deciso di eliminare
			 */
			
			String messaggio = cDAO.disabilitaCliente(request.getParameter("codice"));
			
			if(messaggio.equals("ok")){
				request.setAttribute("messaggio", "L'eliminazione del Cliente avvenuta con successo");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}else {
				request.setAttribute("messaggio", messaggio);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}
			
		}else if(azione.equals("controlloCodiceCliente")){
			
			boolean controlloCodiceCliente = cDAO.controlloCodiceCliente(request.getParameter("codiceCliente"));
			PrintWriter out;
			try {
				out = response.getWriter();
				out.print(controlloCodiceCliente);
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(azione.equals("caricamentoNominativiClienteDisabilitati")){
			/*
			 * in questa sezione effettuo il caricamento di tutti i clienti che sono stati caricati 
			 * nella tabella "Tbl_Cliente"
			 */
			
			ArrayList listaNominativi = cDAO.caricamentoNominativiClienteDisabilitati();
			
			request.setAttribute("nominativi", listaNominativi);	
			rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaNominativi&dispositiva=cliente&tipo=disabilitato");
			rd.forward(request, response);
			
		}else if(azione.equals("abilitazioneCliente")){
			
			String  messaggio = cDAO.abilitaCliente(request.getParameter("codice"));
			
			if(messaggio.equals("ok")){
				request.setAttribute("messaggio", "L'abilitazione del Cliente avvenuta con successo");
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
				rd.forward(request, response);
			}else {
				request.setAttribute("messaggio", messaggio);
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
