package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dao.ReportDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dao.RisorseDAO;
import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.ClienteDTO;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.PlanningDTO;
import it.util.log.MyLogger;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GestioneReport
 */
public class GestioneReport extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private MyLogger log;

	public GestioneReport() {
		super();
		log =new MyLogger(this.getClass());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doGet";
		log.start(metodo);
		processRequest(request, response);
		log.end(metodo);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="doPost";
		log.start(metodo);
		processRequest(request, response);
		log.end(metodo);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String metodo="processRequest";
		log.start(metodo);
		// mi carico la sessione
		HttpSession sessione = request.getSession();

		RequestDispatcher rd = null;

		
		
		//if(sessione.getAttribute("utenteLoggato") != null){//TODO DA RIPRISTINARE
			
			String azione = request.getParameter("azione");
			
			if("visualizzaReport".equals(azione)){
				
				ReportDAO rDAO = new ReportDAO(conn.getConnection());
				RisorsaDAO risDAO = new RisorsaDAO(conn.getConnection());
				ClienteDAO cDAO = new ClienteDAO(conn.getConnection());
				
				List<CommessaDTO> listaCommesse = rDAO.caricamentoCommessa();
				ArrayList listaRisorse = risDAO.elencoRisorse();
				List<ClienteDTO> listaCliente = cDAO.caricamentoClienti();
				
				request.setAttribute("listaCommesse", listaCommesse);
				request.setAttribute("listaRisorse", listaRisorse);
				request.setAttribute("listaCliente", listaCliente);
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&dispositiva=report");			
				rd.forward(request, response);
				
			}else if("ricercaReport".equals(azione)){
				
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
				ReportDAO rDAO = new ReportDAO(conn.getConnection());
				if(!cliente.equals("") && risorsa.equals("") && commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), 0,formattazione.format(dataInizio),formattazione.format(dataFine));
					}
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
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
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,0, Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine));
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
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
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,Integer.parseInt(commessa), 0,formattazione.format(dataInizio),formattazione.format(dataFine));
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente != null && risorsa != null && commessa == ""
				 */
				
				}else if(!cliente.equals("") && !risorsa.equals("") && commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine));
					}
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente != null && risorsa == null && commessa != ""
				 */
				
				}else if(!cliente.equals("") && risorsa.equals("") && !commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						if(commessaCliente.getId_commessa() == Integer.parseInt(commessa)){
							listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), 0,formattazione.format(dataInizio),formattazione.format(dataFine));
						}
					}
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
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
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,Integer.parseInt(commessa), Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine));
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
						if(planning != null){
							report.add(planning);
						}
					}
				
				/*
				 * cliente != "" && risorsa != "" && commessa != ""
				 */
				
				}else if(!cliente.equals("") && !risorsa.equals("") && !commessa.equals("")){
					
					ArrayList listaCommesseCliente = rDAO.caricamentoCommesseCliente(cliente);
					ArrayList listaAssociazioniCommesse = new ArrayList();
					report = new ArrayList();
					
					for(int x = 0; x < listaCommesseCliente.size(); x++){
						CommessaDTO commessaCliente = (CommessaDTO) listaCommesseCliente.get(x);
						if(commessaCliente.getId_commessa() == Integer.parseInt(commessa)){
							listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,commessaCliente.getId_commessa(), Integer.parseInt(risorsa),formattazione.format(dataInizio),formattazione.format(dataFine));
						}
					}
						
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
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
					
					listaAssociazioniCommesse = rDAO.caricamentoAssociazioniCommessaRisorsa(listaAssociazioniCommesse,0, 0,request.getParameter("da"),request.getParameter("a"));
					
					for(int y = 0; y < listaAssociazioniCommesse.size(); y++){
						Associaz_Risor_Comm associazioneCommesse = (Associaz_Risor_Comm) listaAssociazioniCommesse.get(y);
						PlanningDTO planning = rDAO.caricamentoPlanning(associazioneCommesse.getId_associazione(), formattazione.format(dataInizio), formattazione.format(dataFine));
						if(planning != null){
							report.add(planning);
						}
					}
					
					
				}
				
				request.setAttribute("report",report);
				getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&dispositiva=report").forward(request, response);
			}else if("visualizzaConsuntivi".equals(azione)){
				request.setAttribute("commesse",new ReportDAO(conn.getConnection()).caricamentoCommessa());
				request.setAttribute("risorse",new RisorseDAO(conn.getConnection()).getRisorse());
				request.setAttribute("clienti",new ClienteDAO(conn.getConnection()).caricamentoClienti());
				
				getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaConsuntivi").forward(request, response);
			}
//		}else{//TODO DA RIPRISTINARE
//			sessioneScaduta(response);
//		}
	}
}