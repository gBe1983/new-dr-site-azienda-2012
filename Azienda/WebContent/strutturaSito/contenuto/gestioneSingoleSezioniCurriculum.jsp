<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.azienda.dto.Dettaglio_Cv_DTO"%>


<%
	if(request.getParameter("sezione") != null){
		if(request.getParameter("sezione").equals("intestazione")){
			
			RisorsaDTO intestazioneRisorsa = (RisorsaDTO) request.getAttribute("intestazioneRisorsa");
%>
		
		<div class="subtitle ">Modifica Intestazione</div>

		<fieldset class="spazioUltra">
			<legend>Modifica Intestazione</legend>
			<form action="./GestioneCurriculum" method="post">
				<input type="hidden" name="azione" value="salvaIntestazione">
				<input type="hidden" name="parametro" value="<%=intestazioneRisorsa.getIdRisorsa() %>" >
				<table>
					<tr>
						<td><label>Cognome </label></td>
						<td><input type="text" name="cognome" value="<%=intestazioneRisorsa.getCognome() %>" maxlength="45"></td>
					</tr>
					<tr>
						<td><label>Nome </label></td>
						<td><input type="text" name="nome" value="<%=intestazioneRisorsa.getNome() %>" maxlength="45"></td>
					</tr>
					<tr>
						<td><label>Indirizzo</label></td>
						<td><input type="text" name="indirizzo" maxlength="45" value="<%if(intestazioneRisorsa.getIndirizzo() != null){out.print(intestazioneRisorsa.getIndirizzo());}else{out.print("");} %>"></td>
					</tr>
					<tr>
						<td><label>Telefono</label></td>
						<td><input type="text" name="telefono" maxlength="45" value="<%if(intestazioneRisorsa.getTelefono() != null){out.print(intestazioneRisorsa.getTelefono());}else{out.print("");}%>"></td>
					</tr>
					<tr>
						<td><label>Cellulare</label></td>
						<td><input type="text" name="cellulare" maxlength="45" value="<%if(intestazioneRisorsa.getCellulare() != null){out.print(intestazioneRisorsa.getCellulare());}else{out.print("");}%>"></td>
					</tr>
					<tr>
						<td><label>Fax</label></td>
						<td><input type="text" name="fax" maxlength="45" value="<%if(intestazioneRisorsa.getFax() != null){out.print(intestazioneRisorsa.getFax());}else{out.print("");}%>"></td>
					</tr>
					<tr>
						<td><label>Email</label></td>
						<td><input type="text" name="email" maxlength="45" value="<%if(intestazioneRisorsa.getEmail() != null){out.print(intestazioneRisorsa.getEmail());}else{out.print("");}%>" size="40"></td>
					</tr>
					<tr>
						<td><label>Data Nascita</label></td>
						<td><input type="text" name="dataNascita" maxlength="45" value="<%if(intestazioneRisorsa.getDataNascita() != null){out.print(intestazioneRisorsa.getDataNascita());}else{out.print("");}%>"></td>
					</tr>
					<tr>
						<td colspan="2">
							<input type="submit" value="salva modifiche">
							<input type="button" value="Indietro" onClick="javascript:history.back()" name="button">
						</td>
					</tr>
				</table>
			</form>
		</fieldset>		
<%		

//-------------------------------------- Sezione Esperienza -------------------------------------------------------*/

		}else if(request.getParameter("sezione").equals("esperienza")){
			
			EsperienzeDTO exp = (EsperienzeDTO) request.getAttribute("esperienza");
%>			
			<div class="subtitle">Modifica Esperienza</div>

			<fieldset class="spazioUltra">
				<legend>Modifica Esperienza</legend>
				<form action="./GestioneCurriculum" method="post" name="inserisciEsperienza" id="inserisciEsperienza">
					<input type="hidden" name="azione" value="salvaEsperienza">
					<input type="hidden" name="parametro" value="<%=exp.getId_risorsa() %>" >
					<input type="hidden" name="parametroId" value="<%=exp.getIdEsperienze() %>" >
					<table>
						<tr>
							<td><label>*Periodo </label></td>
							<td>
								<%
									String [] date = exp.getPeriodo().split("_");
									int annoInizio = Integer.parseInt(date[0].substring(0,4));
									int meseInizio = Integer.parseInt(date[0].substring(4,6));
									
									int annoFine = Integer.parseInt(date[1].substring(0,4));
									int meseFine = Integer.parseInt(date[1].substring(4,6));
								%>
								<label>Da:</label>
									<select name="annoInizio">
										<%
											Calendar calendario = Calendar.getInstance();
											for(int x = calendario.get(Calendar.YEAR); x >= 1970; x--){
												if(x == annoInizio){
										%>		
													<option value="<%=x %>" selected="selected"><%=x %></option>	
										<%	
												}else{
										%>
													<option value="<%=x %>"><%=x %></option>
										<%
												}
											}
										%>
									</select>
									<select name="meseInizio">
										<%
											for(int x = 1; x < 13; x++){
												if(x == meseInizio){
													if(x < 10){
											%>		
														<option value="<%="0"+x %>" selected="selected"><%="0"+x %></option>
											<%	
													}else{
											%>
														<option value="<%=x %>" selected="selected"><%=x %></option>
											<%				
													}
												}else{
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
											}
										%>
									</select>
								
								<label id="labelA">A:</label>
									<select name="annoFine">
										<%
											for(int x = calendario.get(Calendar.YEAR); x >= 1970; x--){
												if(x == annoFine){
										%>		
													<option value="<%=x %>" selected="selected"><%=x %></option>	
										<%	
												}else{
										%>
													<option value="<%=x %>"><%=x %></option>
										<%
												}
											}
										%>
									</select>
									<select name="meseFine">
										<%
											for(int x = 1; x < 13; x++){
												if(x == meseFine){
													if(x < 10){
											%>		
														<option value="<%="0"+x %>" selected="selected"><%="0"+x %></option>
											<%	
													}else{
											%>
														<option value="<%=x %>" selected="selected"><%=x %></option>
											<%				
													}
												}else{
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
											}
										%>
									</select>
							</td>
						</tr>
						<tr>
							<td><label>*Azienda </label></td>
							<td><input type="text" name="azienda" maxlength="45" value="<%=exp.getAzienda() %>"></td>
						</tr>
						<tr>
							<td><label>*Luogo</label></td>
							<td><input type="text" name="luogo" maxlength="45" value="<%if(exp.getLuogo() != null){out.print(exp.getLuogo());}else{out.print("");} %>"></td>
						</tr>
						<tr>
							<td>
								<label>*Descrizione: <br> (10000 caratteri) </label>
							</td>
							<td>
								<textarea rows="15" cols="50" name="descrizione" maxlength="10000"><%=exp.getDescrizione() %></textarea>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="submit" value="salva modifiche" onclick="return controlloCampiInserimentoExp()">
								<input type="button" value="Indietro" onClick="javascript:history.back()" name="button">
							</td>
						</tr>
					</table>
				</form>
			</fieldset>
<%			
//------------------------------------------ Sezione Dettaglio -------------------------------------------------------*/

		}else if(request.getParameter("sezione").equals("dettaglio")){
			
			Dettaglio_Cv_DTO dettaglio = (Dettaglio_Cv_DTO) request.getAttribute("dettaglio");
%>
		<div class="subtitle ">Modifica Dettaglio</div>

		<div class="spazioUltra">
			<form action="./GestioneCurriculum" method="post" name="dettaglioCv" >
					<input type="hidden" name="azione" value="salvaDettaglio" />
					<input type="hidden" name="parametro" value="<%=dettaglio.getId_risorsa() %>"/>
					<input type="hidden" name="parametroId" value="<%=dettaglio.getId_dettaglio() %>"/>
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
									<input type="button" value="Indietro" onClick="javascript:history.back()" name="button">
								</td>
							</tr>
						</table>
					</form>
			</div>	
<%	
	}
}
%>
