<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
<%@page import="it.azienda.dto.Dettaglio_Cv_DTO"%>

<%
HttpSession sessioneCurriculum = request.getSession();
if(sessioneCurriculum.getAttribute("utenteLoggato") != null){	
	ArrayList curriculum = (ArrayList) sessioneCurriculum.getAttribute("curriculumVitae");
%>

<div class="subtitle ">
	<h2>Modifica Curriculum</h2>
</div>

<div id="flusso">
	<table>
		<tr>
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
		</tr>
	</table>
</div>



<div class="creaCurriculum">
	<%
		int idRisorsa = Integer.parseInt(request.getParameter("risorsa"));
		if(request.getParameter("tipoCreazione") != null){
			if(request.getParameter("tipoCreazione").equals("esperienze")){
	%>
				<%
				if(request.getAttribute("esperienze") != null){
					if(Boolean.parseBoolean(request.getAttribute("esperienze").toString())){
				%>
							<p align="center">Inserimento Esperienze Avvenuta con successo</p>
				<%
						}
					}
					if(request.getAttribute("esperienzeModificata") != null){
						if(Boolean.parseBoolean(request.getAttribute("esperienzeModificata").toString())){
				%>
							<p align="center">Modifica Esperienza Avvenuta con successo</p>
				<%
						}
					}if(request.getAttribute("esperienzeEliminata") != null){
						if(Boolean.parseBoolean(request.getAttribute("esperienzeEliminata").toString())){
				%>
							<p align="center">L'Esperienza è stata eliminata con successo</p>
				<%
						}
					}
				%>
					<div id="bluemenu" class="bluetabs" class="spazio">
					<ul>
							<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=modifica&risorsa=<%=idRisorsa %>&dispositiva=risorsa">Modifica Profilo</a></li>
							<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=dettaglio&risorsa=<%=idRisorsa %>">Dettaglio Profilo</a></li>
							<li><a href="GestioneRisorse?azione=eliminaRisorsa&risorsa=<%=idRisorsa %>" onclick="return confirm('Vuoi eliminare questa risorsa?');">Elimina Profilo</a></li>
							<li><a href="GestioneCurriculum?azione=caricamentoCv&tipoCreazione=visualizzaCurriculum&risorsa=<%=idRisorsa %>&page=visualizzaCurriculum">Anteprima C.V.</a></li>
							<li><a href="GestioneCurriculum?azione=caricamentoCv&risorsa=<%=idRisorsa %>">Modifica C.V.</a></li>				
					</ul>
					</div>
				

					<form action="./GestioneCurriculum" method="post" name="esperienze" id="esperienze" > 
					<input type="hidden" name="azione" value="aggiornaCurriculum" />
					<input type="hidden" name="tipoCreazione" value="esperienze"/>
					<input type="hidden" name="risorsa" value="<%=idRisorsa %>"/>			
					<fieldset>
							<legend>Esperienza Lavorative</legend>
							<table>
									<tr>
										<td>
											<label>Periodo: </label>
										</td>
										<td>
											<input type="text" name="periodo" />
										</td>
									</tr>
									<tr>
										<td>
											<label>Azienda: </label>
										</td>
										<td>
											<input type="text" name="azienda" />
										</td>
									</tr>
									<tr>
										<td>
											<label>Luogo: </label>
										</td>
										<td>
											<input type="text" name="luogo" />
										</td>
									</tr>
									<tr>
										<td>
											<label>Descrizione: <br> (10000 caratteri) </label>
										</td>
										<td>
											<textarea rows="15" cols="60" name="descrizione">Inserisci testo</textarea>
										</td>
									</tr>
								</table>
							
						</fieldset>
						<table>
							<tr>
								<td>
									<input type="submit" value="inserisci esperienza" />
								</td>
								<td>
									<input type="reset" value="svuota campi" />
								</td>
							</tr>
						</table>
					</form>
		<%
			for(int y = 0; y < curriculum.size(); y++){
				if(curriculum.get(y) instanceof EsperienzeDTO){
					EsperienzeDTO esperienze = (EsperienzeDTO)curriculum.get(y);
		%>
					<div id="bluemenu" class="bluetabs">
						<ul>
							<li><a href="index.jsp?azione=dettaglioSingoliCampiCurriculum&tipoModifica=esperienze&idEsperienze=<%=esperienze.getIdEsperienze() %>&risorsa=<%=esperienze.getId_risorsa() %>&dispositiva=risorsa">Dettaglio Esperienza</a></li>
							<li><a href="GestioneCurriculum?azione=eliminaCampiCurriculum&tipologia=esperienze&idEsperienze=<%=esperienze.getIdEsperienze() %>&risorsa=<%=esperienze.getId_risorsa() %>" onclick="return confirm('Vuoi eliminare questa esperienza?');">Elimina Esperienza</a></li>
						</ul>
					</div>
					<fieldset>
					<legend>Esperienza Lavorative</legend>
					<table>
							<tr>
								<td>
									<label>Periodo: </label>
								</td>
								<td>
									<input type="text" name="periodo" value="<%=esperienze.getPeriodo() %>" disabled="disabled"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Azienda: </label>
								</td>
								<td>
									<input type="text" name="azienda" value="<%=esperienze.getAzienda() %>" disabled="disabled"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Luogo: </label>
								</td>
								<td>
									<input type="text" name="luogo" value="<%=esperienze.getLuogo()%>" disabled="disabled"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Descrizione: <br> (10000 caratteri) </label>
								</td>
								<td>
									<textarea rows="2" cols="40" name="descrizione" disabled="disabled"><% if(esperienze.getDescrizione().length() < 100){ out.print(esperienze.getDescrizione()); }else{ out.print(esperienze.getDescrizione().substring(0,100)+"..."); }%></textarea>
								</td>
							</tr>
						</table>
					
				</fieldset>	
<%
		
				}						
			}
			
		}else if(request.getParameter("tipoCreazione").equals("dettaglioCv")){
				/*
				* questa variabile serve per verificare che sia caricata la sezione Tbl_Dettaglio_Cv
				* in caso in cui la variabile rimane a false carica un form del dettaglio vuoto.
				*/
				
				boolean esistenzaDettaglio = false;
				
				if(request.getAttribute("dettaglioModificato") != null){
						if(Boolean.parseBoolean(request.getAttribute("dettaglioModificato").toString())){
				%>
							<p align="center">Modifica Dettaglio Curriculum Avvenuta con successo</p>
				<%
						}
					}
				for(int y = 0; y < curriculum.size(); y++){
					if(curriculum.get(y) instanceof Dettaglio_Cv_DTO){
						
						esistenzaDettaglio = true;
						Dettaglio_Cv_DTO dettaglio = (Dettaglio_Cv_DTO) curriculum.get(y);
				%>
				<div id="bluemenu" class="bluetabs" class="spazio">
					<ul>
						<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=modifica&risorsa=<%=idRisorsa %>&dispositiva=risorsa">Modifica Profilo</a></li>
						<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=dettaglio&risorsa=<%=idRisorsa %>">Dettaglio Profilo</a></li>
						<li><a href="GestioneRisorse?azione=eliminaRisorsa&risorsa=<%=idRisorsa %>" onclick="return confirm('Vuoi eliminare questa risorsa?');">Elimina Profilo</a></li>
						<li><a href="GestioneCurriculum?azione=caricamentoCv&tipoCreazione=visualizzaCurriculum&risorsa=<%=idRisorsa %>&page=visualizzaCurriculum">Anteprima C.V.</a></li>
						<li><a href="GestioneCurriculum?azione=caricamentoCv&risorsa=<%=idRisorsa %>">Modifica C.V.</a></li>				
					</ul>
				</div>
				<form action="./GestioneCurriculum" method="post" name="dettaglioCv" >
					<input type="hidden" name="azione" value="modificaCurriculum" />
					<input type="hidden" name="tipoModifica" value="dettaglioCv"/>
					<input type="hidden" name="risorsa" value="<%=idRisorsa %>"/>
					<input type="hidden" name="id_dettaglio" value="<%=dettaglio.getId_dettaglio()%>"/>
					<fieldset>
							<legend>Dettaglio Cv</legend>
							<table>
									<tr>
										<td>
											<label>Capacita Professionali: <br> (10000 caratteri) </label>
										</td>
										<td>
											<textarea rows="15" cols="50" name="capacitaProfessionali"><%=dettaglio.getCapacita_professionali() %></textarea>
										</td>
									</tr>
									<tr>
										<td>
											<label>Competenze Tecniche: <br> (10000 caratteri) </label>
										</td>
										<td>
											<textarea rows="15" cols="50" name="competenzeTecniche"><%=dettaglio.getCompetenze_tecniche() %></textarea>
										</td>
									</tr>
									<tr>
										<td>
											<label>Lingue: <br> (1000 caratteri) </label>
										</td>
										<td>
											<textarea rows="15" cols="50" name="lingue"><%=dettaglio.getLingue_Straniere() %></textarea>
										</td>
									</tr>
									<tr>
										<td>
											<label>Istruzione: <br> (10000 caratteri) </label>
										</td>
										<td>
											<textarea rows="10" cols="50" name="istruzione" ><%=dettaglio.getIstruzione() %></textarea>
										</td>
									</tr>
									<tr>
										<td>
											<label>Formazione: <br> (10000 caratteri) </label>
										</td>
										<td>
											<textarea rows="15" cols="50" name="formazione" ><%=dettaglio.getFormazione() %></textarea>
										</td>
									</tr>
									<tr>
										<td>
											<label>Interessi: <br> (1000 caratteri) </label>
										</td>
										<td>
											<textarea rows="10" cols="50" name="interessi" ><%=dettaglio.getInteressi() %></textarea>
										</td>
									</tr>
								</table>
							
						</fieldset>
						<table>
							<tr>
								<td>
									<input type="submit" value="modifica dettaglio" />
								</td>
								<td>
									<input type="reset" value="svuota campi" />
								</td>
							</tr>
						</table>
					</form>
<%			
				}
			}
			if(!esistenzaDettaglio){
%>
				<div id="bluemenu" class="bluetabs" class="spazio">
					<ul>
						<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=modifica&risorsa=<%=idRisorsa %>&dispositiva=risorsa">Modifica Profilo</a></li>
						<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=dettaglio&risorsa=<%=idRisorsa %>">Dettaglio Profilo</a></li>
						<li><a href="GestioneRisorse?azione=eliminaRisorsa&risorsa=<%=idRisorsa %>" onclick="return confirm('Vuoi eliminare questa risorsa?');">Elimina Profilo</a></li>
						<li><a href="GestioneCurriculum?azione=caricamentoCv&tipoCreazione=visualizzaCurriculum&risorsa=<%=idRisorsa %>&page=visualizzaCurriculum">Anteprima C.V.</a></li>
						<li><a href="GestioneCurriculum?azione=caricamentoCv&risorsa=<%=idRisorsa %>">Modifica C.V.</a></li>				
					</ul>
				</div>
				<form action="./GestioneCurriculum" method="post" name="dettaglioCv">
					<input type="hidden" name="azione" value="aggiornaCurriculum" />
					<input type="hidden" name="tipoCreazione" value="dettaglioCv"/>
					<input type="hidden" name="risorsa" value="<%=idRisorsa %>"/>	
					<fieldset>
						<legend>Dettaglio Cv</legend>
						<table>
								<tr>
									<td>
										<label>Capacita Professionali: <br> (10000 caratteri) </label>
									</td>
									<td>
										<textarea rows="15" cols="50" name="capacitaProfessionali">Inserisci testo</textarea>
									</td>
								</tr>
								<tr>
									<td>
										<label>Competenze Tecniche: <br> (10000 caratteri) </label>
									</td>
									<td>
										<textarea rows="15" cols="50" name="competenzeTecniche">Inserisci testo</textarea>
									</td>
								</tr>
								<tr>
									<td>
										<label>Lingue: <br> (1000 caratteri) </label>
									</td>
									<td>
										<textarea rows="15" cols="50" name="lingue">Inserisci testo</textarea>
									</td>
								</tr>
								<tr>
									<td>
										<label>Istruzione: <br> (10000 caratteri) </label>
									</td>
									<td>
										<textarea rows="10" cols="50" name="istruzione" >Inserisci testo</textarea>
									</td>
								</tr>
								<tr>
									<td>
										<label>Formazione: <br> (10000 caratteri) </label>
									</td>
									<td>
										<textarea rows="15" cols="50" name="formazione" >Inserisci testo</textarea>
									</td>
								</tr>
								<tr>
									<td>
										<label>Interessi: <br> (1000 caratteri) </label>
									</td>
									<td>
										<textarea rows="10" cols="50" name="interessi" >Inserisci testo</textarea>
									</td>
								</tr>
							</table>
						
					</fieldset>
					<table>
						<tr>
							<td>
								<input type="submit" value="modifica dettaglio" />  
							</td>
							<td>
								<input type="reset" value="svuota campi" />
							</td>
						</tr>
					</table>
				</form>
<%				
			}
		}
	}
}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
}
%>
</div>

