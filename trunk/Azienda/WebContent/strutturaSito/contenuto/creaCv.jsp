<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>
<%
	if(request.getParameter("sezione") != null){
		if(request.getParameter("sezione").equals("esperienza")){
%>

	<div class="subtitle ">Inserisci Esperienza Lavorativa</div>

	<p>* i campi segnati in asterisco sono obbligatori
	<div class="spazioUltra">
		<fieldset>
			<legend>Esperienza Lavorativa</legend>
			<form action="./GestioneCurriculum" method="post" id="inserisciEsperienza" name="inserisciEsperienza">		
				<input type="hidden" name="azione" value="aggiungiEsperienza">
				<input type="hidden" name="parametroId" value="<%=Integer.parseInt(request.getParameter("parametroId")) %>" />
				<table>
					<tr>
						<td>
							<label>*Periodo:</label>
						</td>
						<td>
							<label>Da:</label>
							<select name="annoInizio">
								<%
									Calendar calendario = Calendar.getInstance();
									for(int x = calendario.get(Calendar.YEAR); x >= 1970; x--){
								%>		
										<option value="<%=x %>"><%=x %></option>	
								<%	
									}
								%>
							</select>
							<select name="meseInizio">
								<%
									for(int x = 1; x < 13; x++){
										if(x < 10){
								%>		
											<option value="<%="0"+x %>"><%="0"+x %></option>
								<%	
										}else{
								%>
											<option value="<%=x %>"><%=x %></option>
								<%				
										}
									}
								%>
							</select>
						
							<label id="labelA">A:</label>
							<select name="annoFine">
								<%
									for(int x = calendario.get(Calendar.YEAR); x >= 1970; x--){
								%>		
										<option value="<%=x %>"><%=x %></option>	
								<%	
									}
								%>
							</select>
							<select name="meseFine">
								<%
									for(int x = 1; x < 13; x++){
										if(x < 10){
								%>		
											<option value="<%="0"+x %>"><%="0"+x %></option>
								<%	
										}else{
								%>
											<option value="<%=x %>"><%=x %></option>
								<%				
										}
									}
								%>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<label>*Azienda:</label>
						</td>
						<td>
							<input type="text" name="azienda" maxlength="45"/>
						</td>
					</tr>
					<tr>
						<td>
							<label>*Luogo:</label>
						</td>
						<td>
							<input type="text" name="luogo" maxlength="45"/>
						</td>
					</tr>
					<tr>
						<td>
							<label>*Descrizione: <br> (10000 caratteri) </label>
						</td>
						<td>
							<textarea rows="15" cols="50" name="descrizione" maxlength="10000">Inserisci testo</textarea>
						</td>
					</tr>
					<tr>
						<td colspan="2"> 
							<input type="submit" value="inserisci esperienza" onclick="return controlloCampiInserimentoExp()">
							<input type="button" value="indietro" onClick="javascript:history.back()" name="button">
						</td>
					</tr>
				</table>
			</form>
			</fieldset>
		</div>
<%			
		}else{
%>			
			<div class="subtitle">Inserisci Dettaglio Lavorativo</div>

			<div class="spazioUltra">
					<form action="./GestioneCurriculum" method="post" name="dettaglioCv">
						<input type="hidden" name="azione" value="aggiungiDettaglio" />
						<input type="hidden" name="parametroId" value="<%=Integer.parseInt(request.getParameter("parametroId")) %>"/>		
						<fieldset>
								<legend>Dettaglio Cv</legend>
								<table>
										<tr>
											<td>
												<label>Capacita Professionali: <br> (10000 caratteri) </label>
											</td>
											<td>
												<textarea rows="15" cols="50" name="capacitaProfessionali" maxlength="10000">Inserisci testo</textarea>
											</td>
										</tr>
										<tr>
											<td>
												<label>Competenze Tecniche: <br> (10000 caratteri) </label>
											</td>
											<td>
												<textarea rows="15" cols="50" name="competenzeTecniche" maxlength="10000">Inserisci testo</textarea>
											</td>
										</tr>
										<tr>
											<td>
												<label>Lingue: <br> (1000 caratteri) </label>
											</td>
											<td>
												<textarea rows="15" cols="50" name="lingue" maxlength="1000">Inserisci testo</textarea>
											</td>
										</tr>
										<tr>
											<td>
												<label>Istruzione: <br> (10000 caratteri) </label>
											</td>
											<td>
												<textarea rows="10" cols="50" name="istruzione" maxlength="10000">Inserisci testo</textarea>
											</td>
										</tr>
										<tr>
											<td>
												<label>Formazione: <br> (10000 caratteri) </label>
											</td>
											<td>
												<textarea rows="15" cols="50" name="formazione" maxlength="10000">Inserisci testo</textarea>
											</td>
										</tr>
										<tr>
											<td>
												<label>Interessi: <br> (1000 caratteri) </label>
											</td>
											<td>
												<textarea rows="10" cols="50" name="interessi" maxlength="1000">Inserisci testo</textarea>
											</td>
										</tr>
									</table>
								
							</fieldset>
							<table>
								<tr>
									<td>
										<input type="submit" value="inserisci dettaglio" onclick="return controlloInserimentoDettaglio()"/>
									</td>
									<td>
										<input type="button" value="indietro" onClick="javascript:history.back()" name="button">
									</td>
								</tr>
							</table>
						</form>
					</div>		
<%			
		}
	}
%>