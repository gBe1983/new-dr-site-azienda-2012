package it.azienda.servlet;

import it.azienda.dao.CurriculumDAO;
import it.azienda.dao.RisorsaDAO;
import it.azienda.dto.Dettaglio_Cv_DTO;
import it.azienda.dto.EsperienzeDTO;
import it.azienda.dto.RisorsaDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.StyleSheet;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Servlet implementation class GestioneCurriculum
 */
public class GestioneCurriculum extends BaseServlet {
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
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//recupero la sessione
		HttpSession sessione = request.getSession();
		
		RequestDispatcher rd = null;

	if(sessione.getAttribute("utenteLoggato") != null){	
		//recupero il valore azione 
		String azione = request.getParameter("azione");
		
		CurriculumDAO curriculum = new CurriculumDAO(conn.getConnection());
		
		if(azione.equals("creazioneCurriculum") || azione.equals("aggiornaCurriculum")){
			
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			
			curriculum.creazioneFlagCreazioneCurriculum(idRisorsa);
			
			boolean flag_dettaglio_cv = false;
			if(request.getParameter("dettaglio") != null){
				flag_dettaglio_cv = Boolean.parseBoolean(request.getParameter("dettaglio"));
			}
			
			//recupero il valore dal parametro "TipoCreazione" e verifico che valore assume
			String tipoCreazione = request.getParameter("tipoCreazione");
			
			if(tipoCreazione.equals("esperienze")){
				
				//carico le esperienze 
				EsperienzeDTO esperienze = new EsperienzeDTO();
				esperienze.setPeriodo(request.getParameter("periodo"));
				esperienze.setAzienda(request.getParameter("azienda"));
				esperienze.setLuogo(request.getParameter("luogo"));
				esperienze.setDescrizione(request.getParameter("descrizione"));
				esperienze.setId_risorsa(idRisorsa);
				
				//effettuo l'inserimento dell'Esperienza
				curriculum.inserimentoEsperienze(esperienze);
				
				//valorizzo la variabile Esperienze a "TRUE" per l'avvenuto inserimento
				request.setAttribute("esperienze", "true");
				
			}else if(tipoCreazione.equals("dettaglioCv")){
				
				/*
				 * recupero i valori dal form creazione Cv le voci Dettaglio_Cv
				 */
				
				Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
				dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
				dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
				dettaglio.setLingue_Straniere(request.getParameter("lingue"));
				dettaglio.setIstruzione(request.getParameter("istruzione"));
				dettaglio.setFormazione(request.getParameter("formazione"));
				dettaglio.setInteressi(request.getParameter("interessi"));
				dettaglio.setId_risorsa(idRisorsa);
				
				curriculum.inserimentoDettaglio(dettaglio);
				
				if(azione.equals("creazioneCurriculum")){
					request.setAttribute("dettaglioCv", "true");
					flag_dettaglio_cv = true;
				}else{
					request.setAttribute("dettaglioModificato", "true");
				}
			}
			
			/*
			 * mi serve caricare in sessione il curriculum della risorsa 
			 * perchè quando effettuo l'aggiornamento del curriculum aggiorno
			 * il curriculum
			 */
			
			if(azione.equals("aggiornaCurriculum")){
				ArrayList curriculumVitae = curriculum.caricamentoCurriculum(idRisorsa);
			
				//inserisco il curriculum caricato in sessione 
				sessione.setAttribute("curriculumVitae", curriculumVitae);
			}
			
			/*
			 * effettuo questi tipi di controllo per verificare con quale url
			 * utilizza. 
			 */
			
			if(azione.equals("creazioneCurriculum")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=creazioneCv&tipoCreazione="+ tipoCreazione +"&risorsa="+ idRisorsa +"&dettaglio="+ flag_dettaglio_cv +"&dispositiva=risorsa");
				rd.forward(request, response);
			
			}else if(azione.equals("aggiornaCurriculum")){
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione="+tipoCreazione+"&risorsa="+idRisorsa+"&dispositiva=risorsa");
				rd.forward(request, response);
				
			}
			
		}else if(azione.equals("caricamentoCv")){
			//effettuo il caricamento del curriculum della singola risorsa
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			
			ArrayList curriculumVitae = curriculum.caricamentoCurriculum(idRisorsa);
			
			/*
			 * recupero questa tipo di parametro per differenziare la visualizzazione curriculum
			 * da il modifica curriculum
			 */
			
			String page = request.getParameter("page");
			
			sessione.setAttribute("curriculumVitae", curriculumVitae);
			if(request.getParameter("tipoCreazione") != null){
				if(page == null){
					if(request.getParameter("tipoCreazione").equals("esperienze")){
						response.sendRedirect("index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&risorsa="+idRisorsa+"&dispositiva=risorsa");
					}else{
						response.sendRedirect("index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=dettaglioCv&risorsa="+idRisorsa+"&dispositiva=risorsa");
					}
				}else{
					response.sendRedirect("./strutturaSito/contenuto/visualizzaCurriculumRisorsa.jsp?azione=''&tipoCreazione=esperienze&risorsa="+idRisorsa+"&dispositiva=risorsa");
				}
			}else{
				response.sendRedirect("index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&risorsa="+idRisorsa+"&dispositiva=risorsa");
			}
			
			
		}else if(azione.equals("modificaCurriculum")){
			
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
			String tipoModifica = request.getParameter("tipoModifica");
			
			if(tipoModifica.equals("esperienze")){
				
				
				//carico le esperienze 
				EsperienzeDTO esperienze = new EsperienzeDTO();
				esperienze.setPeriodo(request.getParameter("periodo"));
				esperienze.setAzienda(request.getParameter("azienda"));
				esperienze.setLuogo(request.getParameter("luogo"));
				esperienze.setDescrizione(request.getParameter("descrizione"));
				esperienze.setId_risorsa(idRisorsa);
				esperienze.setIdEsperienze(Integer.parseInt(request.getParameter("idEsperienze")));
				
				//effettuo la modifica concreta dell'Esperienza
				String messaggio = curriculum.aggiornamentoEsperienza(esperienze);
				
				if(messaggio.equals("ok")){
					ArrayList curriculumVitae = curriculum.caricamentoCurriculum(idRisorsa);
					//inserisco il curriculum caricato in sessione
					sessione.setAttribute("curriculumVitae", curriculumVitae);
					request.setAttribute("esperienzeModificata", "true");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&risorsa="+idRisorsa+"&dispositiva=risorsa");
					rd.forward(request, response);
				}else{
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}
			}else if(tipoModifica.equals("dettaglioCv")){
				
				//recupero i valori della modifica del dettaglio
				Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
				dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
				dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
				dettaglio.setLingue_Straniere(request.getParameter("lingue"));
				dettaglio.setIstruzione(request.getParameter("istruzione"));
				dettaglio.setFormazione(request.getParameter("formazione"));
				dettaglio.setInteressi(request.getParameter("interessi"));
				dettaglio.setId_risorsa(idRisorsa);
				dettaglio.setId_dettaglio(Integer.parseInt(request.getParameter("id_dettaglio")));
				
				String messaggio = curriculum.aggiornamentoDettaglio(dettaglio);
				if(messaggio.equals("ok")){
					ArrayList curriculumVitae = curriculum.caricamentoCurriculum(idRisorsa);
					//inserisco il curriculum caricato in sessione
					sessione.setAttribute("curriculumVitae", curriculumVitae);
					request.setAttribute("dettaglioModificato", "true");
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=dettaglioCv&risorsa="+idRisorsa+"&dispositiva=risorsa");
					rd.forward(request, response);
				}else{
					request.setAttribute("messaggio", messaggio);
					rd = getServletContext().getRequestDispatcher("/index.jsp?azione=messaggio");
					rd.forward(request, response);
				}
				
			}
		}else if(azione.equals("eliminaCampiCurriculum")){
			
			if(request.getParameter("tipologia").equals("esperienze")){
				
				int idEsperienze = Integer.parseInt(request.getParameter("idEsperienze"));
				
				curriculum.eliminazioneEsperienza(idEsperienze);
				
				int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
				
				ArrayList curriculumVitae = curriculum.caricamentoCurriculum(idRisorsa);
				//inserisco il curriculum caricato in sessione
				sessione.setAttribute("curriculumVitae", curriculumVitae);
				
				request.setAttribute("esperienzeEliminata", "true");
				
				rd = getServletContext().getRequestDispatcher("/index.jsp?azione=modificaCurriculumRisorsa&tipoCreazione=esperienze&risorsa="+idRisorsa);
				rd.forward(request, response);
				
				
			}
		}else if(azione.equals("esportaPdf")){
			
			//recupero l'id della risorsa
			int idRisorsa = Integer.parseInt(request.getParameter("risorsa")); 
			
			ArrayList curriculumVitae =new ArrayList();
			
			CurriculumDAO cDAO = new CurriculumDAO(conn.getConnection());
			RisorsaDAO rDAO = new RisorsaDAO(conn.getConnection());
			
			curriculumVitae = cDAO.caricamentoCurriculum(idRisorsa);
			RisorsaDTO risorsa = rDAO.caricamentoProfiloRisorsa(idRisorsa);
			
			
			File file = new File(getServletContext().getRealPath("/")+"CurriculumVitae"+ risorsa.getCognome() + risorsa.getNome() +".pdf");
			
			response.setContentType("application/octet-stream; name=\"" + file.getName() + "\"");
			response.setCharacterEncoding("UTF-8");
			response.addHeader("content-disposition", "attachment; filename=\"" + file.getName() + "\"");
			
			String str = "";
			
			Document doc = new Document(PageSize.A4, 10, 10, 10, 40);
			
			boolean titoloEsperienze = false;
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(file));

			HTMLWorker htmlWorker = new HTMLWorker(doc);
			doc.open();
			for(int x = 0; x < curriculumVitae.size(); x++){
					
					if(curriculumVitae.get(x) instanceof RisorsaDTO){
						RisorsaDTO stampaRisorsa = (RisorsaDTO) curriculumVitae.get(x);
						
						// creazione pagina html
							str +=  "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">" +
									"<html>" +
									"<head>" +
									"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/tabella.css\">" +
									"</head>" +
									"<body>" +
									"<table width=\"575\" >" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<font size=\"2\"><h1 align=\"right\">Formato europeo<br>per il curriculum<br>vitae</h1><br><img src=\""+getServletContext().getRealPath("/")+"/images/logo.gif\" align=\"right\"></font>" +
									"	</td>" +
									"	<td>" +
									"		<br>"  +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<br>"  +
									"	</td>" +
									"	<td>" +
									"		<br>"  +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<h3 align=\"right\">Informazioni personali</h3>" +
									"	</td>" +
									"	<td>" +
									"		<br>"  +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<br>"  +
									"	</td>" +
									"	<td>" +
									"		<br>"  +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">Cognome</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>"+risorsa.getCognome()+ "</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">Nome</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>" + risorsa.getNome() + "</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">Indirizzo</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>" + risorsa.getTelefono() + "</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">Telefono</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>"+ risorsa.getTelefono() + "</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">Fax</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>"+ risorsa.getFax() + "</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">E-mail</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>" + risorsa.getEmail() + "</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<p align=\"right\">Data di nascita</p>" +
									"	</td>" +
									"	<td>" +
									"		<span>"+risorsa.getDataNascita()+"</span>" +
									"	</td>" +
									"</tr>" +
									"<tr>" +
									"	<td width=\"50%\">" +
									"		<br>"  +
									"	</td>" +
									"	<td>" +
									"		<br>"  +
									"	</td>" +
									"</tr>";
						}else if(curriculumVitae.get(x) instanceof EsperienzeDTO){
							EsperienzeDTO esperienze = (EsperienzeDTO) curriculumVitae.get(x);
				
							if(!titoloEsperienze){
								titoloEsperienze = true;
								str +=  "<tr>" +
										"	<td width=\"50%\">" +
										"		<h3 align=\"right\">Esperienze Lavorative</h3>" +
										"	<td>" +
										"	<td>" +
										"		<br>" +
										"	<td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>";
							}
								str += 	"<tr>" +
										"	<td width=\"50%\">" +
										"		<p align=\"right\">Periodo:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + esperienze.getPeriodo() + "</span>" +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<p align=\"right\">Azienda:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + esperienze.getAzienda() + "</span>" +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<p align=\"right\">Luogo:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + esperienze.getLuogo() + "</span>" +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Descrizione:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + esperienze.getDescrizione() + "</span>"+
										"	</td>" +
										"</tr>"+
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>";
								}else if(curriculumVitae.get(x) instanceof Dettaglio_Cv_DTO){
									Dettaglio_Cv_DTO dettaglio = (Dettaglio_Cv_DTO) curriculumVitae.get(x);
									
								str +=  "<tr>" +
										"	<td width=\"50%\">" +
										"		<h3 align=\"right\">Dettaglio Curriculum</h3>" +
										"	<td>" +
										"	<td>" +
										"		<br>" +
										"	<td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Capacita Professionali:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + dettaglio.getCapacita_professionali() + "</span>" +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>"+
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Competenze Tecniche:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + dettaglio.getCompetenze_tecniche() + "</span>" +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>"+
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Lingue:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + dettaglio.getLingue_Straniere() + "</span>" +
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>"+
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Istruzione:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + dettaglio.getIstruzione() + "</span>"+
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>"+
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Formazione:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + dettaglio.getFormazione() + "</span>"+
										"	</td>" +
										"</tr>" +
										"<tr>" +
										"	<td width=\"50%\">" +
										"		<br>"  +
										"	</td>" +
										"	<td>" +
										"		<br>"  +
										"	</td>" +
										"</tr>"+
										"<tr>" +
										"	<td width=\"50%\" valign=\"top\">" +
										"		<p align=\"right\">Interessi:</p>"  +
										"	</td>" +
										"	<td>" +
										"		<span>" + dettaglio.getInteressi() + "</span>"+
										"	</td>" +
										"</tr>";
								}
							}
				str += "</table>";
				System.out.println(str);
				StyleSheet styleSheet = new StyleSheet();
//				styleSheet.loadTagStyle("table", "border", "1");
				ArrayList lista = (ArrayList) htmlWorker.parseToList(new StringReader(str), styleSheet);
				
				//mi creo la tabella che servira a contenere le varie celle
				PdfPTable disegnaTabella = new PdfPTable(2);
				disegnaTabella.setWidthPercentage(100);
				float[] columnWidths = {60, 120};
				disegnaTabella.setWidths(columnWidths);
				
				//mi faccio restituire il risultato della conversione da Html a PDF
				PdfPTable tabella = (PdfPTable) lista.get(0);
				
				//mi faccio restituire le righe presenti nella Tabella
				ArrayList listaRows =  (ArrayList)tabella.getRows();
				
				//ciclo le righe
				for(int y = 0; y < listaRows.size(); y++){
					//recupero la singola riga
					PdfPRow row = (PdfPRow)listaRows.get(y);
					
					//recupero le varie celle prensenti nella riga
					PdfPCell[] celle  = (PdfPCell[]) row.getCells();
					
					
					PdfPCell cellaSinistra = new PdfPCell(celle[0]);
					cellaSinistra.setPaddingRight(5);
					disegnaTabella.addCell(cellaSinistra);
					
					PdfPCell cellaDestra = new PdfPCell(celle[1]);
					cellaDestra.setPaddingLeft(5);
					cellaDestra.setBorderWidthLeft(1);
					disegnaTabella.addCell(cellaDestra);
					
				}
				doc.add(disegnaTabella);
				doc.close();
				
				FileInputStream fileInputStream = new FileInputStream(file);
				ServletOutputStream out = response.getOutputStream();
				int i;
				while ((i=fileInputStream.read()) != -1)
					out.write(i);
				fileInputStream.close();
				out.close();
				boolean fileCancellato= new File(getServletContext().getRealPath("/")+"CurriculumVitae"+ risorsa.getCognome() + risorsa.getNome() +".pdf").delete();
				System.out.println("esito della cancellazione del file: " + fileCancellato);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
