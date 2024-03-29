package it.azienda.servlet;

import it.azienda.dao.ClienteDAO;
import it.azienda.dao.ReportDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dao.RisorseDAO;
import it.azienda.dto.Associaz_Risor_Comm;
import it.azienda.dto.ClienteDTO;
import it.azienda.dto.CommessaDTO;
import it.azienda.dto.PlanningDTO;
import it.azienda.dto.RisorsaDTO;
import it.bo.azienda.TimeReport;
import it.util.log.MyLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Servlet implementation class GestioneReport
 */
public class GestioneReport extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private Logger log;

	public GestioneReport() {
		super();
		log = Logger.getLogger(GestioneReport.class);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("doGet");
		processRequest(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: doPost");
		processRequest(request, response);
		
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.info("metodo: processRequest");
		// mi carico la sessione
		HttpSession sessione = request.getSession();

		if(sessione.getAttribute("utenteLoggato") != null){
			
			String azione = request.getParameter("azione");
			
			if("visualizzaReport".equals(azione)){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				ReportDAO rDAO = new ReportDAO(conn.getConnection());
				RisorsaDAO risDAO = new RisorsaDAO(conn.getConnection());
				ClienteDAO cDAO = new ClienteDAO(conn.getConnection());
				
				List<CommessaDTO> listaCommesse = rDAO.caricamentoCommessa();
				ArrayList listaRisorse = risDAO.elencoRisorse();
				List<ClienteDTO> listaCliente = cDAO.caricamentoClienti();
				
				request.setAttribute("listaCommesse", listaCommesse);
				request.setAttribute("listaRisorse", listaRisorse);
				request.setAttribute("listaCliente", listaCliente);
				
				log.info("url: /index.jsp?azione=visualizzaReport&dispositiva=report");
				
				getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&dispositiva=report").forward(request, response);
				
			}else if("ricercaReport".equals(azione)){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
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
					log.info("errore di conversione: " + e1);
				}
				
				Date dataFine = null;
				try {
					dataFine = formatDate.parse(request.getParameter("a"));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					log.info("errore di conversione: " + e);
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
				
				log.info("url: /index.jsp?azione=visualizzaReport&dispositiva=report");
				
				getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaReport&dispositiva=report").forward(request, response);
			
			}else if("visualizzaConsuntivi".equals(azione)){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				ReportDAO reportDAO=new ReportDAO(conn.getConnection());
				request.setAttribute("commesse",reportDAO.caricamentoCommessa());
				request.setAttribute("risorse",new RisorseDAO(conn.getConnection()).getRisorse());
				request.setAttribute("clienti",new ClienteDAO(conn.getConnection()).caricamentoClienti());
				request.setAttribute("tipologiaReport", request.getParameter("tipologiaReport"));
				
				String tipologiaReport = request.getParameter("tipologiaReport");
				
				SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
				
				SimpleDateFormat formattazioneSql=new SimpleDateFormat("yyyy-MM-dd");
				
				Calendar dtDa=Calendar.getInstance();
				String dataDa=request.getParameter("dtDa");
				if(dataDa!=null){
					try {
						dtDa.setTime(sdf.parse(dataDa));
					} catch (ParseException e) {
						log.warn("errore di conversione: "+ e);
					}
				}else{
					while(dtDa.get(Calendar.DAY_OF_MONTH)!=1){
						dtDa.add(Calendar.DAY_OF_MONTH,-1);
					}
				}
				request.setAttribute("dtDa",sdf.format(dtDa.getTime()));

				Calendar dtA=Calendar.getInstance();
				String dataA=request.getParameter("dtA");
				if(dataA!=null){
					try {
						dtA.setTime(sdf.parse(dataA));
					} catch (ParseException e) {
						log.warn("errore di conversione: "+ e);
					}
				}else{
					int actualMaximum=dtA.getActualMaximum(Calendar.DAY_OF_MONTH);
					while(dtA.get(Calendar.DAY_OF_MONTH)!=actualMaximum){
						dtA.add(Calendar.DAY_OF_MONTH,1);
					}
				}
				request.setAttribute("dtA",sdf.format(dtA.getTime()));
				
				if(tipologiaReport.equals("1")){
					
					//creo il formato per recuperare il mese
					SimpleDateFormat formatoMese = new SimpleDateFormat("MM");
					
					//creo il formato per recuperare l'anno
					SimpleDateFormat formatoAnno = new SimpleDateFormat("yyyy");
					
					//creo il formato per recuperare il mese
					SimpleDateFormat formatoWeb = new SimpleDateFormat("dd-MM-yyyy");
					
					ArrayList<String> mesi = new ArrayList<String>();
					
					ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();

					//recupero la differenza dei mesi
					int diffMesi = reportDAO.differenzaMesi(dtDa, dtA);
					
					//creo un clone della differenza dei mesi
					int mese = diffMesi;
					
					//recupero il mese e anno dalla data iniziale
					int meseIniziale = Integer.parseInt(formatoMese.format(dtDa.getTime()));
					int annoIniziale = Integer.parseInt(formatoAnno.format(dtDa.getTime()));
					
					/*
					 * tramite questo switch effettuo la stampa dei mesi di differenza
					 * che ci sono tra la data iniziale da quella finale.
					 */
					do{
						switch(meseIniziale){
							case 1:
								mesi.add("Gennaio "+annoIniziale);
								break;
							case 2:
								mesi.add("Febbraio "+annoIniziale);
								break;
							case 3:
								mesi.add("Marzo "+annoIniziale);
								break;
							case 4:
								mesi.add("Aprile "+annoIniziale);
								break;
							case 5:
								mesi.add("Maggio "+annoIniziale);
								break;
							case 6:
								mesi.add("Giugno "+annoIniziale);
								break;
							case 7:
								mesi.add("Luglio "+annoIniziale);
								break;
							case 8:
								mesi.add("Agosto "+annoIniziale);
								break;
							case 9:
								mesi.add("Settembre "+annoIniziale);
								break;
							case 10:
								mesi.add("Ottobre "+annoIniziale);
								break;
							case 11:
								mesi.add("Novembre "+annoIniziale);
								break;
							case 12:
								mesi.add("Dicembre "+annoIniziale);
								break;
						}
						dtDa.add(Calendar.MONTH, 1);
						meseIniziale = Integer.parseInt(formatoMese.format(dtDa.getTime()));
						annoIniziale = Integer.parseInt(formatoAnno.format(dtDa.getTime()));
						diffMesi--;
						
					}while (diffMesi >= 0);
					
					//ricarico la variabile dtDa in quanto modificata in precedenza
					try {
						dtDa.setTime(sdf.parse(request.getParameter("dtDa")));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.info("eccezione: "+e);
					}
					
					/*
					 * effettuo il caricamento della lista di tutti i clienti in maniera ordinata 
					 * di quel determinato periodo per avere un indice di partenza da cui partire
					 */
					ArrayList<ClienteDTO> listaClienti = 
						new ReportDAO(
								conn.getConnection()).caricamentoClienti(
										formattazioneSql.format(dtDa.getTime()),
												formattazioneSql.format(dtA.getTime()));
					
					/*
					 * effettuo il caricamento di tutte le associazioni di un determinato periodo,
					 * a seconda di come viene effettua la ricerca.
					 */
					ArrayList<Associaz_Risor_Comm> listaAssociazioni = 
						new ReportDAO(conn.getConnection()).caricamentoAssociazioni(
												formattazioneSql.format(dtDa.getTime()),
												formattazioneSql.format(dtA.getTime()),
												request.getParameter("cliente"), 
												request.getParameter("risorsa"), 
												request.getParameter("commessa"));
					
					/*
					 * effettuo il ciclo sui clienti caricati
					 */
					for(int x = 0; x < listaClienti.size(); x++){
						ClienteDTO cliente = (ClienteDTO) listaClienti.get(x);
						
						/*
						 * effettuo il ciclo sulle associazioni caricati
						 */
						for(int y = 0; y < listaAssociazioni.size(); y++){
							Associaz_Risor_Comm asscomm = (Associaz_Risor_Comm)listaAssociazioni.get(y);
							if(asscomm.getDescrizioneCliente().equals(cliente.getRagioneSociale())){
								
								//mi carico tutte le giornate di quel determinato periodo
								try {
									dtDa.setTime(formatoWeb.parse(request.getParameter("dtDa")));
									dtA.setTime(formatoWeb.parse(request.getParameter("dtA")));
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									log.error("errore di conversione: "+e);
								}
								
								PlanningDTO planning = new PlanningDTO();
								planning.setId_associazione(asscomm.getId_associazione());
								planning.setRagione_sociale(asscomm.getDescrizioneCliente());
								planning.setDescrizione_commessa(asscomm.getDescrizioneCommessa());
								planning.setListaGiornate(new ReportDAO(conn.getConnection()).caricamentoOrePerCliente(
										dtDa,
										dtA,
										mese,
										asscomm.getId_associazione()
								));
								listaGiornate.add(planning);
							}
						}
					}
					
					request.setAttribute("listaGiornate", listaGiornate);
					request.setAttribute("listaAssCommessa", listaAssociazioni);
					request.setAttribute("listaClienti", listaClienti);
					request.setAttribute("calendario", mesi);
					
					log.info("url: /main.jsp?azione=visualizzaConsuntivi&tipologia=1&cliente="+request.getParameter("cliente")+"&risorsa="+request.getParameter("risorsa")+"&commessa="+request.getParameter("commessa"));
					
					getServletContext().getRequestDispatcher("/main.jsp?azione=visualizzaConsuntivi&tipologia=1&cliente="+request.getParameter("cliente")+"&risorsa="+request.getParameter("risorsa")+"&commessa="+request.getParameter("commessa")).forward(request, response);
										
				}else if(tipologiaReport.equals("2")){
					
					ArrayList<ClienteDTO> listaClienti = 
						new ReportDAO(
								conn.getConnection()).caricamentoClienti(
										formattazioneSql.format(
												dtDa.getTime()),
												formattazioneSql.format(dtA.getTime()));
					
					ArrayList<Associaz_Risor_Comm> listaAssociazioni = 
						new ReportDAO(conn.getConnection()).caricamentoAssociazioni(
												formattazioneSql.format(dtDa.getTime()),
												formattazioneSql.format(dtA.getTime()),
												request.getParameter("cliente"), 
												request.getParameter("risorsa"), 
												request.getParameter("commessa"));
					
					request.setAttribute("listaClienti", listaClienti);
					request.setAttribute("listaAssCommessa", listaAssociazioni);
					
					ArrayList<PlanningDTO> listaGiornate = new ArrayList<PlanningDTO>();
					
					//
					for(int x = 0; x < listaClienti.size(); x++){
						ClienteDTO cliente = (ClienteDTO) listaClienti.get(x);
						
						for(int y = 0; y < listaAssociazioni.size(); y++){
							Associaz_Risor_Comm asscomm = (Associaz_Risor_Comm)listaAssociazioni.get(y);
							if(asscomm.getDescrizioneCliente().equals(cliente.getRagioneSociale())){
								PlanningDTO planning = new PlanningDTO();
								planning.setDescrizione_commessa(asscomm.getDescrizioneCommessa());
								planning.setDescrizioneRisorsa(asscomm.getDescrizioneRisorsa());
								planning.setRagione_sociale(asscomm.getDescrizioneCliente());
								planning.setListaGiornate(new ReportDAO(conn.getConnection()).getTimeReportCommessaPerCliente(dtDa, dtA, asscomm.getId_risorsa(),asscomm.getId_commessa()));
								listaGiornate.add(planning);
							}
						}
					}
					
					TimeReport timeReport = new TimeReport();
					timeReport.setDays(dtDa, dtA);
					
					request.setAttribute("timeReport", timeReport);
					request.setAttribute("listaGiornate", listaGiornate);
					
					log.info("url: /main.jsp?azione=visualizzaConsuntivi&tipologia=2&cliente="+request.getParameter("cliente")+"&risorsa="+request.getParameter("risorsa")+"&commessa="+request.getParameter("commessa"));
					
					getServletContext().getRequestDispatcher("/main.jsp?azione=visualizzaConsuntivi&tipologia=2&cliente="+request.getParameter("cliente")+"&risorsa="+request.getParameter("risorsa")+"&commessa="+request.getParameter("commessa")).forward(request, response);

					
				}else if(tipologiaReport.equals("3")){
					
					request.setAttribute(
						"timeReport",
						reportDAO.getTimeReport(
							dtDa,
							dtA,
							request.getParameter("cliente"),
							request.getParameter("risorsa"),
							request.getParameter("commessa")));
					
					log.info("url: /main.jsp?azione=visualizzaConsuntivi&tipologia=3&cliente="+request.getParameter("cliente")+"&risorsa="+request.getParameter("risorsa")+"&commessa="+request.getParameter("commessa"));
					
					getServletContext().getRequestDispatcher("/main.jsp?azione=visualizzaConsuntivi&tipologia=3&cliente="+request.getParameter("cliente")+"&risorsa="+request.getParameter("risorsa")+"&commessa="+request.getParameter("commessa")).forward(request, response);
				}
				
			}else if("caricamentoReport".equals(azione)){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
				Calendar dtDa=Calendar.getInstance();
				String dataDa=request.getParameter("dtDa");
				if(dataDa!=null){
					try {
						dtDa.setTime(sdf.parse(dataDa));
					} catch (ParseException e) {
						log.warn("errore di conversione: "+ e);
					}
				}else{
					while(dtDa.get(Calendar.DAY_OF_MONTH)!=1){
						dtDa.add(Calendar.DAY_OF_MONTH,-1);
					}
				}
				request.setAttribute("dtDa",sdf.format(dtDa.getTime()));

				Calendar dtA=Calendar.getInstance();
				String dataA=request.getParameter("dtA");
				if(dataA!=null){
					try {
						dtA.setTime(sdf.parse(dataA));
					} catch (ParseException e) {
						log.warn("errore di conversione: "+e);
					}
				}else{
					int actualMaximum=dtA.getActualMaximum(Calendar.DAY_OF_MONTH);
					while(dtA.get(Calendar.DAY_OF_MONTH)!=actualMaximum){
						dtA.add(Calendar.DAY_OF_MONTH,1);
					}
				}
				request.setAttribute("dtA",sdf.format(dtA.getTime()));
				
				ReportDAO reportDAO=new ReportDAO(conn.getConnection());
				request.setAttribute("commesse",reportDAO.caricamentoCommessa());
				request.setAttribute("risorse",new RisorseDAO(conn.getConnection()).getRisorse());
				request.setAttribute("clienti",new ClienteDAO(conn.getConnection()).caricamentoClienti());
				
				log.info("url: /main.jsp?azione=visualizzaConsuntivi");
				
				getServletContext().getRequestDispatcher("/main.jsp?azione=visualizzaConsuntivi").forward(request, response);
			
			}else if(azione.equals("scaricaReportExcel")){
				
				log.info("-------------------------------------------------------------------------------");
				log.info("azione: "+ azione);
				
				ReportDAO rDAO = new ReportDAO(conn.getConnection());
				
				String dataDa = request.getParameter("dataInizio");
				String dataA = request.getParameter("dataFine");
				String risorsa = request.getParameter("risorsa");
				String cliente = request.getParameter("cliente");
				String commessa = request.getParameter("commessa");
				
				ArrayList<RisorsaDTO> listaRisorse = new RisorsaDAO(conn.getConnection()).elencoRisorse();
				ArrayList<PlanningDTO> giornate = rDAO.caricaGiornatePerExcel(dataDa, dataA,risorsa,cliente,commessa); 
				
				
				File file = rDAO.scaricaReportInExcel(dataDa,dataA,listaRisorse,giornate,getServletContext().getRealPath("/")+"xls");
				
				response.setContentType("application/octet-stream; name=\"" + file.getName() + "\"");
				response.setCharacterEncoding("UTF-8");
				response.addHeader("content-disposition", "attachment; filename=\"" + file.getName() + "\"");
				
				FileInputStream fileInputStream = new FileInputStream(file);
				ServletOutputStream out = response.getOutputStream();
				int i;
				while ((i=fileInputStream.read()) != -1)
					out.write(i);
				fileInputStream.close();
				out.close();
				boolean fileCancellato= new File(getServletContext().getRealPath("/")+"xls/"+file.getName()).delete();
				if(fileCancellato){
					log.info("scaricaReportInFormatoExcel File cancellato");
				}else{
					log.info("scaricaReportInFormatoExcel File non Cancellato");
				}
			}
		}else{
			sessioneScaduta(response);
		}
	}
}