<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.CurriculumDTO"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
<%@page import="it.azienda.dto.Dettaglio_Cv_DTO"%>

<%
	if(request.getParameter("sezione") != null){
		if(request.getParameter("sezione").equals("intestazione")){
			
			RisorsaDTO risorsa = (RisorsaDTO) request.getAttribute("anteprimaIntestazione");
%>

			<div class="subtitle ">Anteprima Intestazione C.V.</div>
			
			<div id="toolbar" class="spazioUltra">
				<a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=risorsa.getIdRisorsa() %>" >Indietro</a>
			</div>

			<div class="spazioUltra">
				<table id="intestazioneCurriculum" border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td class="titoli"><b><h3>Formato Europeo <br> per il curriculum <br>vitae </h3></b></td>
						<td class="valore"><p></p></td>
					</tr>
					<tr>
						<td class="titoli"><img src="images/logo.gif" id="logo" align="right"></td>
						<td class="valore"><p></p></td>
					</tr>
					<tr>
						<td class="titoli"><h4>Informazioni Personali</h4></td>
						<td class="valore"><p></p></td>
					</tr>
					<tr>
						<td class="descrizioni">Cognome</td>
						<td class="valore"><%=risorsa.getCognome() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Nome</td>
						<td class="valore"><%=risorsa.getNome() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Indirizzo</td>
						<td class="valore"><%=risorsa.getIndirizzo() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Telefono</td>
						<td class="valore"><%=risorsa.getTelefono() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Cellulare</td>
						<td class="valore"><%=risorsa.getCellulare() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Fax</td>
						<td class="valore"><%=risorsa.getFax() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Email</td>
						<td class="valore"><%=risorsa.getEmail() %></td>
					</tr>
					<tr>
						<td class="descrizioni">Data di Nascita</td>
						<td class="valore"><%=risorsa.getDataNascita() %></td>
					</tr>
				</table>
			</div>
<%
		}else if(request.getParameter("sezione").equals("esperienze")){
			CurriculumDTO curriculum = (CurriculumDTO) request.getAttribute("listaEsperienze");
			%>
						<div class="subtitle">Anteprima Esperienze Lavorative</div>
						
						<div id="toolbar" class="spazioUltra">
							<a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=curriculum.getId_risorsa() %>" >Indietro</a>
						</div>

						<div class="spazioUltra">
							<table id="intestazioneCurriculum" border="0" cellpadding="0" cellspacing="0">
								<%
									boolean titoloEsperienze = false;
									for(EsperienzeDTO esperienza:curriculum.getListaEsperienze()){
										if(!titoloEsperienze){
											titoloEsperienze = true;
								%>
											<tr>
												<td class="titoli"><h4>Esperienze Lavorative</td>
												<td class="valore"><p></p></td>
											</tr>
								<%
										}
								%>
										<tr>
											<td class="descrizioni">Periodo</td>
											<td class="valore"><%=esperienza.getPeriodo() %></td>
										</tr>
										<tr>
											<td class="descrizioni">Azienda</td>
											<td class="valore"><%=esperienza.getAzienda() %></td>
										</tr>
										<tr>
											<td class="descrizioni">Luogo</td>
											<td class="valore"><%=esperienza.getLuogo() %></td>
										</tr>
										<tr>
											<td class="descrizioni" valign="top">Descrizione</td>
											<td class="valore">
												<textarea rows="15" cols="50" name="descrizione" readonly="readonly"><%=esperienza.getDescrizione() %></textarea>
											</td>
										</tr>
										<tr>
											<td class="descrizioni"><p></p></td>
											<td class="valore"><p></p></td>
										</tr>
								<%
									}
								%>
							</table>
						</div>
<%		
	/*------------------------------- Anteprima Dettaglio ----------------------------------------*/
	
		}else if(request.getParameter("sezione").equals("dettaglio")){
			Dettaglio_Cv_DTO dettaglio = (Dettaglio_Cv_DTO) request.getAttribute("dettaglio");
			%>
					<div class="subtitle">Anteprima Dettaglio Lavorativo</div>
					
					<div id="toolbar" class="spazioUltra">
						<a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=dettaglio.getId_risorsa() %>" >Indietro</a>
					</div>

					<div class="spazioUltra">
						<table id="intestazioneCurriculum" border="0" cellpadding="0" cellspacing="0">
									<tr>
										<td class="titoli"><h4>Dettaglio Lavorativo</td>
										<td class="valore"><p></p></td>
									</tr>
									<tr>
										<td class="titoli"><p></p></td>
										<td class="valore"><p></p></td>
									</tr>
									<tr>
										<td class="titoli"><p></p></td>
										<td class="valore"><p></p></td>
									</tr>
									<tr>
										<td class="descrizioni" valign="top">Capacità Professionali</td>
										<td class="valore"><textarea rows="15" cols="50" name="capacitaProfessionali" readonly="readonly"><%=dettaglio.getCapacita_professionali() %></textarea></td>
									</tr>
									<tr>
										<td class="descrizioni" valign="top">Competenze Tecniche</td>
										<td class="valore"><textarea rows="15" cols="50" name="competenzeTecniche" readonly="readonly"><%=dettaglio.getCompetenze_tecniche() %></textarea></td>
									</tr>
									<tr>
										<td class="descrizioni" valign="top">Lingue</td>
										<td class="valore"><textarea rows="15" cols="50" name="lingue" readonly="readonly"><%=dettaglio.getLingue_Straniere() %></textarea></td>
									</tr>
									<tr>
										<td class="descrizioni" valign="top">Istruzione</td>
										<td class="valore"><textarea rows="15" cols="50" name="istruzione" readonly="readonly"><%=dettaglio.getIstruzione() %></textarea></td>
									</tr>
									<tr>
										<td class="descrizioni" valign="top">Formazione</td>
										<td class="valore"><textarea rows="15" cols="50" name="formazione" readonly="readonly"><%=dettaglio.getFormazione() %></textarea></td>
									</tr>
									<tr>
										<td class="descrizioni" valign="top">Interessi</td>
										<td class="valore"><textarea rows="15" cols="50" name="interessi" readonly="readonly"><%=dettaglio.getInteressi() %></textarea></td>
									</tr>
							</table>
						</div>
						
<%		
		}
	}
%>
