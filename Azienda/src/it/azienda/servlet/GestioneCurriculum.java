package it.azienda.servlet;

import it.azienda.dao.CurriculumDAO;
import it.azienda.dto.CurriculumDTO;
import it.azienda.dto.Dettaglio_Cv_DTO;
import it.azienda.dto.EsperienzeDTO;
import it.azienda.dto.RisorsaDTO;
import it.mail.Email;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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

		CurriculumDAO cDAO = new CurriculumDAO(conn.getConnection());
		
		if(sessione.getAttribute("utenteLoggato") != null){
			
			//recupero il parametro azione
			String azione = request.getParameter("azione");
			
			if(azione.equals("caricamentoAllCurriculum")){
				
				/**
				 * mi carico tutti i curriculum che sono stati creati verificando
				 * che ci siano sia esperinze che dettagli
				 */
				ArrayList<CurriculumDTO> curriculumVitae = cDAO.caricamentoAllCurriculum();
				
				request.setAttribute("listaCurriculum", curriculumVitae);
				
				/*
				 * effettuo questa distinzione tramite la dispositiva per via dei vari percorsi
				 * che l'utente può raggiungere questa sezione.
				 */
				
				if(request.getParameter("dispositiva").equals("gestione")){
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaCurriculum").forward(request, response);
				
				}else if(request.getParameter("dispositiva").equals("esportaPDF")){
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaElencoCurriculum&dispositiva=esportaPdf").forward(request, response);
					
				}else if(request.getParameter("dispositiva").equals("anteprimaCv")){
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaElencoCurriculum&dispositiva=anteprimaGlobale").forward(request, response);
					
				}
				
			}else if(azione.equals("caricamentoCv")){
				
				/**
				 * in questa sezione carico il curriculum selezionato 
				 * della singola risorsa
				 */
				int id_risorsa = Integer.parseInt(request.getParameter("parametro0"));
				
				if(request.getParameter("creazioneCv") != null){
					if(request.getParameter("creazioneCv").equals("1")){
						cDAO.creazioneFlagCreazioneCurriculum(id_risorsa);
					}
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum").forward(request, response);
				
			}else if(azione.equals("modificaIntestazione") || azione.equals("modificaEsperienza") || azione.equals("modificaDettaglio")){
				
				/**
				 * in questa sezione gestisco le modifiche delle varie sezioni
				 */
		
				if(azione.equals("modificaIntestazione")){
					
					int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
					//mi carico l'intestazione della singola risorsa
					RisorsaDTO risorsa = cDAO.caricamentoIntestazioneRisorsa(id_risorsa);
					
					request.setAttribute("intestazioneRisorsa", risorsa);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneSingoleSezioniCurriculum&sezione=intestazione").forward(request, response);
					
				}else if(azione.equals("modificaEsperienza")){
					
					int id_esperienza = Integer.parseInt(request.getParameter("parametro0"));
					
					EsperienzeDTO exp = cDAO.caricamentoEsperienza(id_esperienza);
					
					request.setAttribute("esperienza", exp);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneSingoleSezioniCurriculum&sezione=esperienza").forward(request, response);

				}else if(azione.equals("modificaDettaglio")){
					
					int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
					
					Dettaglio_Cv_DTO dettaglio = cDAO.caricamentoDettaglio(id_risorsa);
					
					request.setAttribute("dettaglio", dettaglio);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneSingoleSezioniCurriculum&sezione=dettaglio").forward(request, response);

				}
				
				
			}else if(azione.equals("salvaIntestazione") || azione.equals("salvaEsperienza") || azione.equals("salvaDettaglio")){
				
				/**
				 * in questa sezione salvo tutte le modifiche avvenute alle varie sezioni
				 */
				int id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				
				int esitoModifica = 0;
				
				if(azione.equals("salvaIntestazione")){
					
					//recupero i valori
					RisorsaDTO intestazioneRisorsa = new RisorsaDTO();
					
					intestazioneRisorsa.setIdRisorsa(id_risorsa);
					intestazioneRisorsa.setCognome(request.getParameter("cognome"));
					intestazioneRisorsa.setNome(request.getParameter("nome"));
					intestazioneRisorsa.setIndirizzo(request.getParameter("indirizzo"));
					intestazioneRisorsa.setTelefono(request.getParameter("telefono"));
					intestazioneRisorsa.setCellulare(request.getParameter("cellulare"));
					intestazioneRisorsa.setFax(request.getParameter("fax"));
					intestazioneRisorsa.setEmail(request.getParameter("email"));
					intestazioneRisorsa.setDataNascita(request.getParameter("dataNascita"));
					
					esitoModifica = cDAO.updateIntestazione(intestazioneRisorsa);
			
				}else if(azione.equals("salvaEsperienza")){
					
					EsperienzeDTO esperienza = new EsperienzeDTO();
					
					esperienza.setIdEsperienze(Integer.parseInt(request.getParameter("parametroId")));
					esperienza.setPeriodo(request.getParameter("annoInizio") + request.getParameter("meseInizio") 
						+ "_" +  request.getParameter("annoFine") + request.getParameter("meseFine"));
					esperienza.setAzienda(request.getParameter("azienda"));
					esperienza.setLuogo(request.getParameter("luogo"));
					esperienza.setDescrizione(request.getParameter("descrizione"));
					esperienza.setId_risorsa(id_risorsa);
					
					esitoModifica = cDAO.aggiornamentoEsperienza(esperienza);
					
				}else if(azione.equals("salvaDettaglio")){
					
					//recupero i valori della modifica del dettaglio
					Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
					dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
					dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
					dettaglio.setLingue_Straniere(request.getParameter("lingue"));
					dettaglio.setIstruzione(request.getParameter("istruzione"));
					dettaglio.setFormazione(request.getParameter("formazione"));
					dettaglio.setInteressi(request.getParameter("interessi"));
					dettaglio.setId_risorsa(id_risorsa);
					dettaglio.setId_dettaglio(Integer.parseInt(request.getParameter("parametroId")));
					
					esitoModifica = cDAO.aggiornamentoDettaglio(dettaglio);
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(esitoModifica == 1){
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=1").forward(request, response);
				}else{
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=0").forward(request, response);
				}
			
			}else if(azione.equals("anteprimaIntestazione") || azione.equals("anteprimaEsperienza") || azione.equals("anteprimaDettaglio") || azione.equals("anteprimaGlobale")){
				
				/**
				 * in questa sezione effettuo l'anteprima delle singole sezioni
				 */
				
				int id_risorsa = 0;

				if(request.getParameter("parametro") != null){
					id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				}else{
					id_risorsa = Integer.parseInt(request.getParameter("parametro0"));
				}
				
				if(azione.equals("anteprimaIntestazione")){
					
					//mi carico l'intestazione per poi effettuare l'anteprima
					RisorsaDTO risorsa = cDAO.caricamentoIntestazioneRisorsa(id_risorsa);
					
					request.setAttribute("anteprimaIntestazione", risorsa);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=intestazione").forward(request, response);
				
				}else if(azione.equals("anteprimaEsperienza")){
					
					int indice = Integer.parseInt(request.getParameter("indice"));
					
					ArrayList<EsperienzeDTO> listaEsperienze = new ArrayList<EsperienzeDTO>();
					
					CurriculumDTO curriculum = new CurriculumDTO();
					
					for(int x = 0; x < indice; x++){
						
						int id_esperienza = Integer.parseInt(request.getParameter("parametro"+x));
						
						EsperienzeDTO esperienza = cDAO.caricamentoEsperienza(id_esperienza);
						//effettuo questa operazione per formattare la data in MM/YYYY
						esperienza.setPeriodo(cDAO.formattazionePeriodo(esperienza.getPeriodo().split("_")));
						
						listaEsperienze.add(esperienza);
					}
					
					curriculum.setListaEsperienze(listaEsperienze);
					curriculum.setId_risorsa(id_risorsa);
					
					request.setAttribute("listaEsperienze", curriculum);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=esperienze").forward(request, response);
				
				}else if(azione.equals("anteprimaDettaglio")){
										
					Dettaglio_Cv_DTO dettaglio = cDAO.caricamentoDettaglio(id_risorsa);
					
					request.setAttribute("dettaglio", dettaglio);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=gestioneAnteprimeSezioniCurriculum&sezione=dettaglio").forward(request, response);
					
				}else if(azione.equals("anteprimaGlobale")){
										
					CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
					curriculumVitae.setId_risorsa(id_risorsa);
					
					request.setAttribute("curriculumVitae", curriculumVitae);
					
					if(request.getParameter("area") != null){
						if(request.getParameter("area").equals("all")){
							/*
							 * l'utente arriva dalla visualizzazione di tutti i curriculum
							 */
							getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&area=all").forward(request, response);
						}else{
							/*
							 * l'utente arriva dal dettaglio curriculum
							 */
							getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&area=notAll").forward(request, response);
						}
					}else{
						/*
						* questa anteprima arriva dalla schermata Curriculum Vitae/Curriculum -> Anteprima C.V. -> scelta risorsa -> anteprima
						*/
						getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum").forward(request, response);
					}
					
					
				}
				
			}else if (azione.equals("aggiungiEsperienza") || azione.equals("aggiungiDettaglio")) {
				
				int id_risorsa = Integer.parseInt(request.getParameter("parametroId"));
				
				int esitoAggiungi = 0;
				
				if(azione.equals("aggiungiEsperienza")){
					EsperienzeDTO esperienza = new EsperienzeDTO();
					esperienza.setPeriodo(request.getParameter("annoInizio") + request.getParameter("meseInizio") 
							+ "_" +  request.getParameter("annoFine") + request.getParameter("meseFine"));
					esperienza.setAzienda(request.getParameter("azienda"));
					esperienza.setLuogo(request.getParameter("luogo"));
					esperienza.setDescrizione(request.getParameter("descrizione"));
					esperienza.setId_risorsa(id_risorsa);
					
					esitoAggiungi = cDAO.inserimentoEsperienze(esperienza);
					
				}else{
					
					Dettaglio_Cv_DTO dettaglio = new Dettaglio_Cv_DTO();
					dettaglio.setCapacita_professionali(request.getParameter("capacitaProfessionali"));
					dettaglio.setCompetenze_tecniche(request.getParameter("competenzeTecniche"));
					dettaglio.setLingue_Straniere(request.getParameter("lingue"));
					dettaglio.setIstruzione(request.getParameter("istruzione"));
					dettaglio.setFormazione(request.getParameter("formazione"));
					dettaglio.setInteressi(request.getParameter("interessi"));
					dettaglio.setId_risorsa(id_risorsa);
					
					esitoAggiungi = cDAO.inserimentoDettaglio(dettaglio);
					
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(esitoAggiungi == 1){
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=1").forward(request, response);
				}else{
					getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=0").forward(request, response);
				}
				
				
				//esperienza.setPeriodo(periodo)
				
			}else if (azione.equals("eliminaEsperienza") || azione.equals("eliminaDettaglio") || azione.equals("eliminazioneGlobale")){
				/**
				 *  in questa sezione effettuo l'eliminazione delle singole 
				 *  parti del curriculum
				 */
				int id_risorsa = 0;
				
				if(request.getParameter("parametro") != null){
					id_risorsa = Integer.parseInt(request.getParameter("parametro"));
				}
				
				int esitoEliminazione = 0;
				
				if(azione.equals("eliminaEsperienza")){
					
					int indice = Integer.parseInt(request.getParameter("indice"));
					
					for(int x = 0; x < indice; x++){
						
						int id_esperienza = Integer.parseInt(request.getParameter("parametro"+x));
						
						esitoEliminazione = cDAO.eliminazioneEsperienza(id_esperienza);
						
					}
				}else if(azione.equals("eliminaDettaglio")){
						
					int id_dettaglio = Integer.parseInt(request.getParameter("parametroId"));
						
					esitoEliminazione = cDAO.eliminazioneDettaglio(id_dettaglio);
					
				}else if(azione.equals("eliminazioneGlobale")){
					
 					int indice = Integer.parseInt(request.getParameter("indice"));
					
					for(int x = 0; x < indice; x++){
						
						int idRisorsa = Integer.parseInt(request.getParameter("parametro"+x));
						
						cDAO.eliminaEsperienzaGlobale(idRisorsa);
						cDAO.eliminazioneDettaglioGlobale(idRisorsa);
						cDAO.disabilitazioneFlagCreazioneCurriculum(idRisorsa);
					}
					
					ArrayList<CurriculumDTO> curriculumVitae = cDAO.caricamentoAllCurriculum();
					
					request.setAttribute("listaCurriculum", curriculumVitae);
					
					getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaCurriculum").forward(request, response);

					
				}
				
				CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
				curriculumVitae.setId_risorsa(id_risorsa);
				
				request.setAttribute("curriculumVitae", curriculumVitae);
				
				if(!azione.equals("eliminazioneGlobale")){
					if(esitoEliminazione == 1){
						getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=1").forward(request, response);
					}else{
						getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum&esito=2").forward(request, response);
					}
				}
				
				
			}else if(azione.equals("esportaPdf")){
				
				//recupero l'id della risorsa
				int id_risorsa = Integer.parseInt(request.getParameter("parametro")); 
				
				CurriculumDTO curriculum =  cDAO.caricamentoCurriculum(id_risorsa);
				
				String sceltaTipoCurriculum = request.getParameter("sceltaCurriculum");
				
				if(sceltaTipoCurriculum.equals("aziendale")){
					
					File filePdf = null;
					
					String tipoVisualizzazione = request.getParameter("tipoVisualizzazione");
					
					if(tipoVisualizzazione.equals("completo")){
						/* recupero il file pdf */
						filePdf = cDAO.creazioneCurriculumVitaeFormatoAziendale(getServletContext().getRealPath("/"), curriculum, new File(getServletContext().getRealPath("/")+"CurriculumVitae_"+ curriculum.getRisorsa().getCognome() + "_" + curriculum.getRisorsa().getNome() +".pdf"),true);
					}else{
						/* recupero il file pdf */
						filePdf = cDAO.creazioneCurriculumVitaeFormatoAziendale(getServletContext().getRealPath("/"), curriculum, new File(getServletContext().getRealPath("/")+"CurriculumVitae_"+ curriculum.getRisorsa().getCognome() + "_" + curriculum.getRisorsa().getNome() +".pdf"),false);
					}
					
					String lastMovimentazione = request.getParameter("lastMovimento");
					
					String sceltaInvioEmail = request.getParameter("email");
					
					if(sceltaInvioEmail.equals("si")){
						
						String destinatario = request.getParameter("destinatario");
						String oggetto = request.getParameter("oggetto");
						String corpo = request.getParameter("corpo");
						
						Email email = new Email(getServletContext());
						boolean esitoInvioEmail = email.sendMailConAllegato(destinatario, oggetto, corpo, filePdf);
						
						if(lastMovimentazione.equals("dettaglioCurriculum")){
							
							CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
							curriculumVitae.setId_risorsa(id_risorsa);
							
							request.setAttribute("curriculumVitae", curriculumVitae);
							request.setAttribute("esitoInvioEmail", esitoInvioEmail);
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum").forward(request, response);
						
						}else if(lastMovimentazione.equals("anteprimaCurriculum")){
							
							CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
							curriculumVitae.setId_risorsa(id_risorsa);
							
							request.setAttribute("curriculumVitae", curriculumVitae);
							request.setAttribute("esitoInvioEmail", esitoInvioEmail);
							
							if(request.getParameter("area") != null){
								if(request.getParameter("area").equals("all")){
									/*
									 * l'utente arriva dalla visualizzazione di tutti i curriculum
									 */
									getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&area=all").forward(request, response);
								}else{
									/*
									 * l'utente arriva dal dettaglio curriculum
									 */
									getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&area=notAll").forward(request, response);
								}
							}
							
						}else if(lastMovimentazione.equals("visualizzaElencoCurriculum")){
							
							ArrayList<CurriculumDTO> curriculumVitae = cDAO.caricamentoAllCurriculum();
							
							request.setAttribute("listaCurriculum", curriculumVitae);
							request.setAttribute("esitoInvioEmail", esitoInvioEmail);
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaElencoCurriculum&dispositiva=esportaPdf").forward(request, response);
						}
						
						
					}else{
						response.setContentType("application/octet-stream; name=\"" + filePdf.getName() + "\"");
						response.setCharacterEncoding("UTF-8");
						response.addHeader("content-disposition", "attachment; filename=\"" + filePdf.getName() + "\"");
						
						FileInputStream fileInputStream = new FileInputStream(filePdf);
						ServletOutputStream out = response.getOutputStream();
						int i;
						while ((i=fileInputStream.read()) != -1)
							out.write(i);
						fileInputStream.close();
						out.close();
						boolean fileCancellato= new File(getServletContext().getRealPath("/")+"CurriculumVitae"+ curriculum.getRisorsa().getCognome() + curriculum.getRisorsa().getNome() +".pdf").delete();
						System.out.println("esito della cancellazione del file: " + fileCancellato);
						
					}
					
				}else if(sceltaTipoCurriculum.equals("europeo")){
					
					File filePdf = cDAO.creazioneCurriculumVitaeFormatoEuropeo(getServletContext().getRealPath("/"), curriculum, new File(getServletContext().getRealPath("/")+"CurriculumVitae_"+ curriculum.getRisorsa().getCognome() + "_" + curriculum.getRisorsa().getNome() +".pdf"));
					
					String lastMovimentazione = request.getParameter("lastMovimento");
					
					String sceltaInvioEmail = request.getParameter("email");
					
					if(sceltaInvioEmail.equals("si")){
						
						String destinatario = request.getParameter("destinatario");
						String oggetto = request.getParameter("oggetto");
						String corpo = request.getParameter("corpo");
						
						Email email = new Email(getServletContext());
						boolean esitoInvioEmail = email.sendMailConAllegato(destinatario, oggetto, corpo, filePdf);
						
						if(lastMovimentazione.equals("dettaglioCurriculum")){
							
							CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
							curriculumVitae.setId_risorsa(id_risorsa);
							
							request.setAttribute("curriculumVitae", curriculumVitae);
							request.setAttribute("esitoInvioEmail", esitoInvioEmail);
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=dettaglioCurriculum").forward(request, response);
						
						}else if(lastMovimentazione.equals("anteprimaCurriculum")){
							
							CurriculumDTO curriculumVitae = cDAO.caricamentoCurriculum(id_risorsa);
							curriculumVitae.setId_risorsa(id_risorsa);
							
							request.setAttribute("curriculumVitae", curriculumVitae);
							request.setAttribute("esitoInvioEmail", esitoInvioEmail);
							
							if(request.getParameter("area") != null){
								if(request.getParameter("area").equals("all")){
									/*
									 * l'utente arriva dalla visualizzazione di tutti i curriculum
									 */
									getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&area=all").forward(request, response);
								}else{
									/*
									 * l'utente arriva dal dettaglio curriculum
									 */
									getServletContext().getRequestDispatcher("/index.jsp?azione=anteprimaCurriculum&area=notAll").forward(request, response);
								}
							}
							
						}else if(lastMovimentazione.equals("visualizzaElencoCurriculum")){
							
							ArrayList<CurriculumDTO> curriculumVitae = cDAO.caricamentoAllCurriculum();
							
							request.setAttribute("listaCurriculum", curriculumVitae);
							request.setAttribute("esitoInvioEmail", esitoInvioEmail);
							
							getServletContext().getRequestDispatcher("/index.jsp?azione=visualizzaElencoCurriculum&dispositiva=esportaPdf").forward(request, response);
						}
						
						
					}else{
						response.setContentType("application/octet-stream; name=\"" + filePdf.getName() + "\"");
						response.setCharacterEncoding("UTF-8");
						response.addHeader("content-disposition", "attachment; filename=\"" + filePdf.getName() + "\"");
						
						FileInputStream fileInputStream = new FileInputStream(filePdf);
						ServletOutputStream out = response.getOutputStream();
						int i;
						while ((i=fileInputStream.read()) != -1)
							out.write(i);
						fileInputStream.close();
						out.close();
						boolean fileCancellato= new File(getServletContext().getRealPath("/")+"CurriculumVitae"+ curriculum.getRisorsa().getCognome() + curriculum.getRisorsa().getNome() +".pdf").delete();
						System.out.println("esito della cancellazione del file: " + fileCancellato);
						
					}
				}
				
			 }else if(azione.equals("selezionaRisorsa")){
				 
				 ArrayList<RisorsaDTO> listaRisorsa = cDAO.caricamentoRisorseSenzaCurriculum();
				 
				 request.setAttribute("listaRisorsa", listaRisorsa);
				 
				 getServletContext().getRequestDispatcher("/index.jsp?azione=selezionaRisorsa").forward(request, response);
				 
			 }
		}else{
			sessioneScaduta(response);
		}
	}
}
