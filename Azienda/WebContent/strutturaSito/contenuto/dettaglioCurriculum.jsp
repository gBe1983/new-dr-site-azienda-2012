<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.CurriculumDTO"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
<%@page import="it.azienda.dao.CurriculumDAO"%>
<%
	CurriculumDTO curriculum = (CurriculumDTO ) request.getAttribute("curriculumVitae");
%>
<div class="subtitle ">Dettaglio Curriculum Vitae</div>

<div id="toolbar" class="spazioUltra"><a href="./GestioneCurriculum?azione=anteprimaGlobale&parametro=<%=curriculum.getId_risorsa() %>&area=notAll" >Anteprima</a><a href="#" onclick="return openFinestra('<%=curriculum.getId_risorsa() %>','<%=request.getParameter("azione") %>','notAll')">Esporta in Pdf</a><a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=gestione">Indietro</a></div>

<div id="finestra" title="Esporta Pdf">
	<%@include file="esportaPdf.jsp" %>
</div>

<div class="spazioUltra">
	<table width="100%" border="0">
		<tr>
			<td width="30%"><p></p></td>
			<td width="30%"><h4 align="center"> Intestazione Cv</h4></td>
			<td width="30%">
				<form action="./GestioneCurriculum" method="post" class="sceltaCurriculum" name="intestazione">
					<input type="hidden" name="azione" value="">
					<input type="hidden" name="parametro" value="<%=curriculum.getId_risorsa() %>">
					<select name="scelta">
						<option value="">-- Scegli Azione --</option>
						<option value="modificaIntestazione">Modifica</option>
						<option value="anteprimaIntestazione">Anteprima</option>
					</select>
					<input type="submit" value="esegui" onclick="return controlloSceltaIntestazione()">
				</form>
			</td>
		</tr>
	</table>
	<form action="" method="post" name="curriculum">
		<table id="channel">
			<tr><th>Nominativo</th><th>Indirizzo</th><th>Telefono</th><th>Cellulare</th><th>Fax</th><th>Email</th><th>Data Nascita</th></tr>	
	<%
		if(curriculum.getRisorsa() != null){
	%>
			<tr>
				<td>
					<%=curriculum.getRisorsa().getCognome() + " " + curriculum.getRisorsa().getNome()%>
				</td>
				<td>
					<%
						if(curriculum.getRisorsa().getIndirizzo() != null){
							out.print(curriculum.getRisorsa().getIndirizzo());
						}else{
							out.print("");
						}
					%>
				</td>
				<td>
					<%
						if(curriculum.getRisorsa().getTelefono() != null){
							out.print(curriculum.getRisorsa().getTelefono());
						}else{
							out.print("");
						}
					%>
				</td>
				<td>
					<%
						if(curriculum.getRisorsa().getCellulare() != null){
							out.print(curriculum.getRisorsa().getCellulare());
						}else{
							out.print("");
						}
					%>
				</td>
				<td>
					<%
						if(curriculum.getRisorsa().getFax() != null){
							out.print(curriculum.getRisorsa().getFax());
						}else{
							out.print("");
						}
					%>
				</td>
				<td>
					<%
						if(curriculum.getRisorsa().getEmail() != null){
							out.print(curriculum.getRisorsa().getEmail());
						}else{
							out.print("");
						}
					%>
				</td>
				<td>
					<%
						if(curriculum.getRisorsa().getDataNascita() != null){
							out.print(curriculum.getRisorsa().getDataNascita());
						}else{
							out.print("");
						}
					%>
				</td>
			</tr>
	<%		
		}
	%>
		</table>
	</form>
</div>

<div class="spazioUltra">
	<!--   -------------------------- Sezione Esperienza --------------------------- -->	
	<table width="100%" border="0">
		<tr>
			<td width="30%"><p></p></td>
			<td width="30%"><h4 align="center"> Esperienze Cv</h4></td>
			<td width="30%">
				<form action="./GestioneCurriculum" method="post" class="sceltaCurriculum" name="sceltaEsperienza" id="sceltaEsperienza">
					<input type="hidden" name="azione" value="">
					<input type="hidden" name="parametro" value="<%=curriculum.getId_risorsa() %>">
					<select name="scelta">
						<option value="">-- Scegli Azione --</option>
						<option value="aggiungiEsperienza">Aggiungi</option>
						<option value="modificaEsperienza">Modifica</option>
						<option value="anteprimaEsperienza">Anteprima</option>
						<option value="eliminaEsperienza">Elimina</option>
					</select>
					<input type="submit" value="esegui" onclick="return controlloSceltaEsperienze()">
				</form>
			</td>
		</tr>
	</table>
	<form action="" method="post" name="esperienze" id="esperienze">
		<table id="channel">
			<tr><th>Periodo</th><th>Azienda</th><th>Luogo</th><th>Descrizione</th><th>Scelta</th></tr>	
	<%
		if(curriculum.getListaEsperienze().size() > 0){
			for(int x = 0; x < curriculum.getListaEsperienze().size(); x++){
				EsperienzeDTO esperienza = curriculum.getListaEsperienze().get(x);
		%>
				<tr>
					<td>
						<%
							if(esperienza.getPeriodo() != null){
								out.print(esperienza.getPeriodo());
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(esperienza.getAzienda() != null){
								out.print(esperienza.getAzienda());
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(esperienza.getLuogo() != null){
								out.print(esperienza.getLuogo());
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(esperienza.getDescrizione() != null){
								if(esperienza.getDescrizione().length() < 25){
									out.print(esperienza.getDescrizione());
								}else{
									out.print(esperienza.getDescrizione().substring(0,25)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<input type="checkbox" name="exp" value="<%=esperienza.getIdEsperienze() %>">
					</td>
				</tr>
	<%		
			}
		}else{
	%>
			<tr>
				<td colspan="5"><label>Non sono prensenti esperienze per questa risorsa</label></td>
			</tr>
	<%
		}
	%>
		</table>
	</form>
</div>

<div class="spazioUltraPlus">
	<!-- -------------------------- Sezione Dettaglio --------------------------- -->
	<table width="100%" border="0">
		<tr>
			<td width="30%"><p></p></td>
			<td width="30%"><h4 align="center"> Dettaglio Cv</h4></td>
			<td width="30%">
				<form action="./GestioneCurriculum" method="post" class="sceltaCurriculum" name="sceltaDettaglio" id="sceltaDettaglio">
					<input type="hidden" name="azione" value="">
					<input type="hidden" name="parametro" value="<%=curriculum.getId_risorsa() %>">
					<input type="hidden" name="parametroId" value="<% if(curriculum.getListaDettaglio() != null){ if(curriculum.getListaDettaglio().getId_dettaglio() != 0){ out.print(curriculum.getListaDettaglio().getId_dettaglio()); } else{ out.print(0); }}%>">
					<select name="scelta">
						<option value="">-- Scegli Azione --</option>
					<%
						if(!curriculum.getRisorsa().isFlagCreazioneDettaglio()){
					%>
							<option value="aggiungiDettaglio">Aggiungi</option>
					<%
						}
					%>
						<option value="modificaDettaglio">Modifica</option>
						<option value="anteprimaDettaglio">Anteprima</option>
						<option value="eliminaDettaglio">Elimina</option>
					</select>
					<input type="submit" value="esegui" onclick="return controlloSceltaDettaglio()">
				</form>
			</td>
		</tr>
	</table>
	<form action="" method="post" name="dettaglio" id="dettaglioCurriculum">
		<table id="channel">
			<tr><th>Capacità Professionali</th><th>Competenze Tecniche</th><th>Lingue</th><th>Istruzione</th><th>Formazione</th><th>Interessi</th></tr>	
	<%
		if(curriculum.getListaDettaglio() != null){
	%>
				<tr>
					<td>
						<%
							if(curriculum.getListaDettaglio().getCapacita_professionali() != null){
								if(curriculum.getListaDettaglio().getCapacita_professionali().length() < 20){
									out.print(curriculum.getListaDettaglio().getCapacita_professionali());
								}else{
									out.print(curriculum.getListaDettaglio().getCapacita_professionali().substring(0,20)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(curriculum.getListaDettaglio().getCompetenze_tecniche() != null){
								if(curriculum.getListaDettaglio().getCompetenze_tecniche().length() < 20){
									out.print(curriculum.getListaDettaglio().getCompetenze_tecniche());
								}else{
									out.print(curriculum.getListaDettaglio().getCompetenze_tecniche().substring(0,20)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(curriculum.getListaDettaglio().getLingue_Straniere() != null){
								if(curriculum.getListaDettaglio().getLingue_Straniere().length() < 20){
									out.print(curriculum.getListaDettaglio().getLingue_Straniere());
								}else{
									out.print(curriculum.getListaDettaglio().getLingue_Straniere().substring(0,20)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(curriculum.getListaDettaglio().getIstruzione() != null){
								if(curriculum.getListaDettaglio().getIstruzione().length() < 20){
									out.print(curriculum.getListaDettaglio().getIstruzione());
								}else{
									out.print(curriculum.getListaDettaglio().getLingue_Straniere().substring(0,20)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(curriculum.getListaDettaglio().getFormazione() != null){
								if(curriculum.getListaDettaglio().getFormazione().length() < 20){
									out.print(curriculum.getListaDettaglio().getFormazione());
								}else{
									out.print(curriculum.getListaDettaglio().getLingue_Straniere().substring(0,20)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(curriculum.getListaDettaglio().getInteressi() != null){
								if(curriculum.getListaDettaglio().getInteressi().length() < 20){
									out.print(curriculum.getListaDettaglio().getInteressi());
								}else{
									out.print(curriculum.getListaDettaglio().getLingue_Straniere().substring(0,20)+"...");
								}
							}else{
								out.print("");
							}
						%>
					</td>
				</tr>
	<%		
		}else{
	%>
			<tr>
				<td colspan="6"><label>Non è prensente nessun tipo di dettaglio per questa risorsa</label></td>
			</tr>
	<%
		}
	%>
		</table>
	</form>
</div>
<%
	if(request.getParameter("esito") != null){
		if(Integer.parseInt(request.getParameter("esito")) == 1){
%>
			<script type="text/javascript">alert("Operazione avvenuta con successo");</script>
<%				
		}else{
%>			
			<script type="text/javascript">alert("Siamo spiacenti ma l'operazione effettuata non è andata a buon fine. Contattare l'amministrazione.");</script>		
<%		
		}
	}

	if(request.getAttribute("esitoInvioEmail") != null){
		if(Boolean.parseBoolean(request.getAttribute("esitoInvioEmail").toString())){
%>	
			<script type="text/javascript">alert("L'invio dell'email con l'allegato il Curriculum Vitae è avvenuta correttamente"); </script>
<%		
		}else{
%>
			<script type="text/javascript">alert("Impossibile inviare l'email con l'allegato il Curriculum Vitae. Contattare l'amministratore.");</script>
<%			
		}
	}
%>