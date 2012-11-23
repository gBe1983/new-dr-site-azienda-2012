package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dao.ReportDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.PlanningDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneReport
 */
public class GestioneReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GestioneReport() {
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
		
		// mi carico la sessione
		HttpSession sessione = request.getSession();

		RequestDispatcher rd = null;

		//recupeto la Connection
		Connection conn = (Connection)sessione.getAttribute("connessione");
		
		ReportDAO rDAO = new ReportDAO();
		
		if(sessione.getAttribute("utenteLoggato") != null){
			
			String azione = request.getParameter("azione");
			
			if(azione.equals("visualizzaReport")){
				
				RisorsaDAO risDAO = new RisorsaDAO();
				ClienteDAO cDAO = new ClienteDAO();
				
				ArrayList listaCommesse = rDAO.caricamentoCommessa(conn);
				ArrayList listaRisorse = risDAO.elencoRisorse(conn);
				ArrayList listaCliente = cDAO.caricamentoClienti(conn);
				
				request.setAttribute("listaCommesse", listaCommesse);
				request.setAttribute("listaRisorse", listaRisorse);
				request.setAttribute("listaCliente", listaCliente);
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&dispositiva=report");			
				rd.forward(request, response);
				
			}else if(azione.equals("ricercaReport")){
				
				SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
				SimpleDateFormat formattazione = new SimpleDateFormat("yyyy-MM-dd");
				
				
				//recupero i valori dal form presente in "VisualizzaReport"
				
				String cliente = request.getParameter("cliente");
				String risorsa = request.getParameter("risorsa");
				String commessa = request.getParameter("commessa");
				
				//casto le date che recupero in tipo "Date"
				
				Date dataInizio = null;
				try {
					dataInizio = formatDate.parse(request.getParameter("da"));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Date dataFine = null;
				try {
					dataFine = formatDate.parse(request.getParameter("a"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ArrayList report = null;
				
				/*
				 * verifico come sono stati valorizzati i campi per la ricerca dei report
				 */
				
				/*
				 * cliente != null && risorsa == null && commessa == ""
				 */
				if(!cliente.equals("") && risorsa.equals("") && commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente,conn);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), 0,formattazione.format(dataInizio),formattazione.format(dataFine), conn);
					}
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
					
				/*
				 * cliente == null && risorsa != null && commessa == ""
				 */
					
				}else if(cliente.equals("") && !risorsa.equals("") && commessa.equals("")){
					
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,0, Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine), conn);
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente == null && risorsa == null && commessa != ""
				 */
				
				}else if(cliente.equals("") && risorsa.equals("") && !commessa.equals("")){
					
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,Integer.parseInt(commessa), 0,formattazione.format(dataInizio),formattazione.format(dataFine), conn);
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente != null && risorsa != null && commessa == ""
				 */
				
				}else if(!cliente.equals("") && !risorsa.equals("") && commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente,conn);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine), conn);
					}
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente != null && risorsa == null && commessa != ""
				 */
				
				}else if(!cliente.equals("") && risorsa.equals("") && !commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente,conn);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						if(commessaCliente.getId_commessa() == Integer.parseInt(commessa)){
							listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), 0,formattazione.format(dataInizio),formattazione.format(dataFine), conn);
						}
					}
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente == null && risorsa != null && commessa != ""
				 */
					
				}else if(cliente.equals("") && !risorsa.equals("") && !commessa.equals("")){
					
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,Integer.parseInt(commessa), Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine), conn);
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente != "" && risorsa != "" && commessa != ""
				 */
				
				}else if(!cliente.equals("") && !risorsa.equals("") && !commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente,conn);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						if(commessaCliente.getId_commessa() == Integer.parseInt(commessa)){
							listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine), conn);
						}
					}
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
					
					/*
					 * cliente == null && risorsa == "" && commessa == ""
					 */
				}else if(cliente.equals("") && risorsa.equals("") && commessa.equals("")){
					
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,0, 0,request.getParameter("da"),request.getParameter("a"), conn);
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine), conn);
						if(planning != null){
							report.add(planning);
						}
					}
					
					
				}
				
				request.setAttribute("report",report);
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&dispositiva=report");			
				rd.forward(request, response);
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
