<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	int risorsa = Integer.parseInt(request.getParameter("risorsa"));
%>

<div class="subtitle ">
	<h2>Creazione Currilum</h2>
</div>
<p>
	In questa sezione potrete gestire la creazione del curriculum associato alla risorsa.
</p>

<div class="creaCurriculum">
	<%
		if(request.getParameter("tipoCreazione") != null){
			if(request.getParameter("tipoCreazione").equals("esperienze")){
	%>
			
			<%
				if(request.getAttribute("esperienze") != null){
					if(Boolean.parseBoolean(request.getAttribute("esperienze").toString())){
			%>
						<p align="center">Inserimento Esperienza Avvenuta con successo</p>
			<%
					}
				}
			%>
				<form action="./GestioneCurriculum" method="post" name="esperienze">
				<input type="hidden" name="azione" value="creazioneCurriculum" />
				<input type="hidden" name="tipoCreazione" value="esperienze"/>
				<input type="hidden" name="risorsa" value="<%=risorsa %>"/>
				<input type="hidden" name="dettaglio" value="<%=request.getParameter("dettaglio") %>" />			
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
										<textarea rows="20" cols="40" name="descrizione">Inserisci testo</textarea>
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
			}if(request.getParameter("tipoCreazione").equals("dettaglioCv")){
				
					if(request.getAttribute("dettaglioCv") != null){
						if(Boolean.parseBoolean(request.getAttribute("dettaglioCv").toString())){
				%>
							<p align="center" class="spazio">Inserimento Dettaglio avvenuta con successo<br>
								<a href="index.jsp?azione=creazioneCv&tipoCreazione=esperienze&risorsa=<%=risorsa%>&dettaglio=<%=request.getParameter("dettaglio") %>" >Torna al Menu </a>							
							</p>
				<%
						}
					}
					if(request.getParameter("dettaglio") != null){
						if(!Boolean.parseBoolean(request.getParameter("dettaglio").toString())){
				%>
						<form action="./GestioneCurriculum" method="post" name="dettaglioCv">
						<input type="hidden" name="azione" value="creazioneCurriculum" />
						<input type="hidden" name="tipoCreazione" value="dettaglioCv"/>
						<input type="hidden" name="risorsa" value="<%=risorsa %>"/>	
						<input type="hidden" name="dettaglio" value="<%=request.getParameter("dettaglio") %>" />		
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
										<input type="submit" value="inserisci dettaglio" />
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
		<form action="./GestioneCurriculum" method="post" name="istruzioni">
		<input type="hidden" name="azione" value="creazioneCurriculum" />
		<input type="hidden" name="tipoCreazione" value="esperienze"/>
		<input type="hidden" name="risorsa" value="<%=risorsa %>"/>
		<input type="hidden" name="dettaglio" value="<%=request.getParameter("dettaglio") %>" />		
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
								<textarea rows="25" cols="70" name="descrizione">Inserisci testo</textarea>
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