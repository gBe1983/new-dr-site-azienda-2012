<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	if(request.getParameter("azione").equals("aggiungiRisorsa")){
%>
<div class="subtitle ">Aggiungi Risorsa</div>

<p>
	In questa sezione potrete gestire l'inserimento di tutte le risorse.
</p>
<p>* i campi segnati in asterisco sono obbligatori</p>
<form action="./GestioneRisorse" method="post" name="risorsa">
	<input type="hidden" name="azione" value="aggiungiRisorsa">
	<fieldset>
		<legend align="center">Dati Anagrafici</legend>
		<table>
			<tr>
				<td><label>* Cognome</label></td>
				<td><input type="text" name="cognome" /></td>
			</tr>
			<tr>
				<td><label>* Nome</label></td>
				<td><input type="text" name="nome" /></td>
			</tr>
			<tr>
				<td><label>* Data Nascita</label></td>
				<td><input type="text" name="dataNascita" size="10" /></td>
			</tr>
			<tr>
				<td><label>* Luogo Nascita</label></td>
				<td><input type="text" name="luogoNascita" /></td>
			</tr>
			<tr>
				<td><label>* Sesso</label></td>
				<td>
					<input type="radio" name="sesso" value="m"/><label>Maschio</label>
					<input type="radio" name="sesso" value="f"/><label>Femmina</label>
				</td>
			</tr>
			<tr>
				<td><label>Codice Fiscale</label></td>
				<td><input type="text" name="codiceFiscale" maxlength="16" size="16" /></td>
			</tr>
			<tr>
				<td><label>* Mail</label></td>
				<td><input type="text" name="mail" /></td>
			</tr>
			<tr>
				<td><label>Telefono</label></td>
				<td><input type="text" name="telefono" /></td>
			</tr>
			<tr>
				<td><label>* Cellulare</label></td>
				<td><input type="text" name="cellulare" /></td>
			</tr>
			<tr>
				<td><label>Fax</label></td>
				<td><input type="text" name="fax" /></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend align="center">Residenza</legend>
		<table>
			<tr>
				<td><label>Indirizzo</label></td>
				<td><input type="text" name="indirizzo" /></td>
			</tr>
			<tr>
				<td><label>Città</label></td>
				<td><input type="text" name="citta" /></td>
			</tr>
			<tr>
				<td><label>Provincia</label></td>
				<td><input type="text" name="provincia" maxlength="2" size="2"/></td>
			</tr>
			<tr>
				<td><label>Cap</label></td>
				<td><input type="text" name="cap" maxlength="5" size="5"/></td>
			</tr>
			<tr>
				<td><label>Nazione</label></td>
				<td><input type="text" name="nazione" /></td>
			</tr>
			<tr>
				<td><label>Servizio Militare </label></td>
				<td>
					<select name="militare">
						<option value="">-- Seleziona Servizio Militare --</option>
						<option value="essente">Esente</option>
						<option value="esonerato">Esonerato</option>
						<option value="assolto">Assolto</option>
						<option value="obbligatorio">Obbligatorio</option>
						<option value="obiettore">Obiettore di Coscienza</option>
						<option value="altro">altro</option>
					</select>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend align="center">Altri Dati</legend>
		<table>
			<tr>
				<td><label>Patente </label></td>
				<td>
					<select   name="patente">
							<option value=" "> Seleziona</option>
							<option value="Nessuna">Nessuna</option>
							<option value="A">A</option>
							<option value="A1">A1</option>
							<option value="B">B</option>
							<option value="C">C</option>
							<option value="D">D</option>														
							<option value="BE">BE</option>
							<option value="CE">CE</option>														
							<option value="DE">DE</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label>Costo al Giorno</label></td>
				<td>
					<input type="text" name="costo">
				</td>
			</tr>
			<tr>
				<td><label>Occupato</label></td>
				<td>
					<input type="radio" name="occupato" value="si"><label>Si</label>
					<input type="radio" name="occupato" value="no"><label>No</label>
				</td>
			</tr>
			<tr>
				<td><label>Tipo Contratto</label></td>
				<td>
					<input type="text" name="tipoContratto">
				</td>
			</tr>
			<tr>
				<td><label>Figura Professionale</label></td>
				<td>
					<input type="text" name="figuraProfessionale">
				</td>
			</tr>
			<tr>
				<td><label>Seniority</label></td>
				<td>
					<select name="seniority">
							<option value=" "> Seleziona</option>
							<option value="Junior">Junior</option>
							<option value="Medium">Medium</option>
							<option value="Senior">Senior</option>
					</select>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend align="center">Credenziali</legend>
		<table>
			<tr>
				<td><label>* Username</label></td>
				<td>
					<input type="text" name="username">
				</td>
			</tr>
			<tr>
				<td><label>* Password</label></td>
				<td>
					<input type="password" name="password">
				</td>
			</tr>
		</table>
	</fieldset>
	<table>
		<tr>
			<td><button type="submit" value="aggiungi Risorsa" onclick="return controlloInserisciModificaRisorsa()">Aggiungi Risorsa</button></td>
			<td><button type="reset" value="svuota campi">Svuota Campi</button></td>
		</tr>
	</table>
</form>
<%
	}else if(request.getParameter("azione").equals("modificaRisorsa")){
		RisorsaDTO risorsa = (RisorsaDTO) request.getAttribute("risorsa");
%>

<div class="subtitle ">Modifica Risorsa</div>

<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
		</tr>
	</table>
</div>


<div id="bluemenu" class="bluetabs" class="spazio">
	<ul>
		<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=dettaglio&risorsa=<%=risorsa.getIdRisorsa() %>">Dettaglio Risorsa</a></li>
		<li><a href="GestioneRisorse?azione=caricamentoCredenziali&risorsa=<%=risorsa.getIdRisorsa() %>">Modifica Credenziali</a></li>
		<li><a href="GestioneRisorse?azione=eliminaRisorsa&risorsa=<%=risorsa.getIdRisorsa() %>" onclick="return confirm('Vuoi disabilitare questa risorsa?');">Disabilita Risorsa</a></li>
<% 
	if(risorsa.isFlaCreazioneCurriculum()){
%>
		<li><a href="GestioneCurriculum?azione=caricamentoCv&parametro0=<%=risorsa.getIdRisorsa() %>&dispositiva=risorsa">Curriculum Vitae</a></li>				
<%
	}else{
%>
		<li><a href="GestioneCurriculum?azione=caricamentoCv&parametro0=<%=risorsa.getIdRisorsa() %>&creazioneCv=1&dispositiva=risorsa">Crea Curriculum Vitae</a></li>
<%
	}
%>
	</ul>
</div>

<p>* i campi segnati in asterisco sono obbligatori</p>

<form action="./GestioneRisorse" method="post" name="risorse">
	<input type="hidden" name="azione" value="modificaRisorsa">
	<input type="hidden" name="idRisorsa" value="<%=risorsa.getIdRisorsa() %>">
	<fieldset>
		<legend align="center">Dati Anagrafici</legend>
		<table>
			<tr>
				<td><label>* Cognome</label></td>
				<td><input type="text" name="cognome" value="<%=risorsa.getCognome() %>"/></td>
			</tr>
			<tr>
				<td><label>* Nome</label></td>
				<td><input type="text" name="nome" value="<%=risorsa.getNome() %>"/></td>
			</tr>
			<tr>
				<td><label>* Data Nascita</label></td>
				<td><input type="text" name="dataNascita" value="<%=risorsa.getDataNascita() %>" maxlength="10" size="10"/></td>
			</tr>
			<tr>
				<td><label>* Luogo Nascita</label></td>
				<td><input type="text" name="luogoNascita" value="<%=risorsa.getLuogoNascita() %>"/></td>
			</tr>
			<tr>
				<td><label>* Sesso</label></td>
				<td>
					<%
						if(risorsa.getSesso().equals("m")){
					%>
						<input type="radio" name="sesso" value="m" checked="checked"/><label>Maschio</label>
						<input type="radio" name="sesso" value="f"/><label>Femmina</label>
					<%
						}else{
					%>
							<input type="radio" name="sesso" value="m"/><label>Maschio</label>
							<input type="radio" name="sesso" value="f" checked="checked"><label>Femmina</label>
					<%
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>* Codice Fiscale</label></td>
				<td><input type="text" name="codiceFiscale" value="<%=risorsa.getCodiceFiscale() %>"/></td>
			</tr>
			<tr>
				<td><label>* Mail</label></td>
				<td><input type="text" name="mail" value="<%=risorsa.getEmail() %>"/></td>
			</tr>
			<tr>
				<td><label>Telefono</label></td>
				<td><input type="text" name="telefono" value="<%=risorsa.getTelefono() %>"/></td>
			</tr>
			<tr>
				<td><label>* Cellulare</label></td>
				<td><input type="text" name="cellulare" value="<%=risorsa.getCellulare() %>" /></td>
			</tr>
			<tr>
				<td><label>Fax</label></td>
				<td><input type="text" name="fax" value="<%=risorsa.getFax() %>"/></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend align="center">Residenza</legend>
		<table>
			<tr>
				<td><label>Indirizzo</label></td>
				<td><input type="text" name="indirizzo" value="<%=risorsa.getIndirizzo() %>"/></td>
			</tr>
			<tr>
				<td><label>Città</label></td>
				<td><input type="text" name="citta" value="<%=risorsa.getCitta() %>"/></td>
			</tr>
			<tr>
				<td><label>Provincia</label></td>
				<td><input type="text" name="provincia" value="<%=risorsa.getProvincia() %>" maxlength="2" size="2"/></td>
			</tr>
			<tr>
				<td><label>Cap</label></td>
				<td><input type="text" name="cap" value="<%=risorsa.getCap() %>" maxlength="5" size="5"/></td>
			</tr>
			<tr>
				<td><label>Nazione</label></td>
				<td><input type="text" name="nazione" value="<%=risorsa.getNazione() %>"/></td>
			</tr>
			<tr>
				<td><label>Servizio Militare </label></td>
				<td>
					<select name="militare">
						<option value="">-- Seleziona Servizio Militare --</option>
						<option value="essente">Esente</option>
						<option value="esonerato">Esonerato</option>
						<option value="assolto">Assolto</option>
						<option value="obbligatorio">Obbligatorio</option>
						<option value="obiettore">Obiettore di Coscienza</option>
						<option value="altro">altro</option>
					</select>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend align="center">Altri Dati</legend>
		<table>
			<tr>
				<td><label>Patente </label></td>
				<td>
					<select   name="patente">
							<option value=" "> Seleziona</option>
							<option value="Nessuna">Nessuna</option>
							<option value="A">A</option>
							<option value="A1">A1</option>
							<option value="B">B</option>
							<option value="C">C</option>
							<option value="D">D</option>														
							<option value="BE">BE</option>
							<option value="CE">CE</option>														
							<option value="DE">DE</option>
					</select>
				</td>
			</tr>
			<tr>
				<td><label>Costo al Giorno</label></td>
				<td>
					<input type="text" name="costo" value="<%=risorsa.getCosto() %>">
				</td>
			</tr>
			<tr>
				<td><label>Occupato</label></td>
				<td>
				<%
					if(risorsa.isOccupato()){ 
				%>
						<input type="radio" name="occupato" value="si" checked="checked"><label>Si</label>
						<input type="radio" name="occupato" value="no"><label>No</label>
				<%
					}else{
				%>
						<input type="radio" name="occupato" value="si"><label>Si</label>
						<input type="radio" name="occupato" value="no" checked="checked"><label>No</label>
				<%
					}
				%>		
				</td>
			</tr>
			<tr>
				<td><label>Tipo Contratto</label></td>
				<td>
					<input type="text" name="tipoContratto" value="<%=risorsa.getTipoContratto() %>">
				</td>
			</tr>
			<tr>
				<td><label>Figura Professionale</label></td>
				<td>
					<input type="text" name="figuraProfessionale" value="<%=risorsa.getFiguraProfessionale() %>">
				</td>
			</tr>
			<tr>
				<td><label>Seniority</label></td>
				<td>
					<select name="seniority">
				<%
					if(risorsa.getSeniority().equals("Junior")){
				%>
						<option value=" "> Seleziona</option>
						<option value="Junior" selected="selected">Junior</option>
						<option value="Medium">Medium</option>
						<option value="Senior">Senior</option>
				<%
					}else if(risorsa.getSeniority().equals("Medium")){
				%>
						<option value=" "> Seleziona</option>
						<option value="Junior" >Junior</option>
						<option value="Medium" selected="selected">Medium</option>
						<option value="Senior" >Senior</option>
				<%
					}else{
				%>
						<option value=" "> Seleziona</option>
						<option value="Junior" >Junior</option>
						<option value="Medium" >Medium</option>
						<option value="Senior" selected="selected">Senior</option>
				<%
					}
				%>
					</select>
				</td>
			</tr>
		</table>
	</fieldset>
	<table>
		<tr>
			<td><button type="submit" value="Modifica Risorsa" onclick="return controlloModificaRisorsa()">Modifica Risorsa</button></td>
		</tr>
	</table>
</form>
<%
	}else if(request.getParameter("azione").equals("dettaglioRisorsa")){
		RisorsaDTO risorsa = (RisorsaDTO) request.getAttribute("risorsa");
%>

<div class="subtitle ">Dettaglio Risorsa</div>

<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
		</tr>
	</table>
</div>


<div id="bluemenu" class="bluetabs">
	<ul>
		<li><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=modifica&risorsa=<%=risorsa.getIdRisorsa() %>&dispositiva=risorsa">Modifica  Risorsa</a></li>
		<li><a href="GestioneRisorse?azione=caricamentoCredenziali&risorsa=<%=risorsa.getIdRisorsa() %>">Modifica Credenziali</a></li>
		<li><a href="GestioneRisorse?azione=eliminaRisorsa&risorsa=<%=risorsa.getIdRisorsa() %>" onclick="return confirm('Vuoi disabilitare questa risorsa?');">Disabilita Risorsa</a></li>
<% 
	if(risorsa.isFlaCreazioneCurriculum()){
%>
		<li><a href="GestioneCurriculum?azione=caricamentoCv&parametro0=<%=risorsa.getIdRisorsa() %>&dispositiva=risorsa">Curriculum Vitae</a></li>				
<%
	}else{
%>
		<li><a href="GestioneCurriculum?azione=caricamentoCv&parametro0=<%=risorsa.getIdRisorsa() %>&creazioneCv=1&dispositiva=risorsa">Crea Curriculum Vitae</a></li>
<%
	}
%>
	</ul>
</div>

	<fieldset>
		<legend align="center">Dati Anagrafici</legend>
		<table>
			<tr>
				<td><label>Cognome</label></td>
				<td><label><%=risorsa.getCognome() %></label></td>
			</tr>
			<tr>
				<td><label>Nome</label></td>
				<td><label><%=risorsa.getNome() %></label></td>
			</tr>
			<tr>
				<td><label>Data Nascita</label></td>
				<td><label><%=risorsa.getDataNascita() %></label></td>
			</tr>
			<tr>
				<td><label>Luogo Nascita</label></td>
				<td><label><%=risorsa.getLuogoNascita() %></label></td>
			</tr>
			<tr>
				<td><label>Sesso</label></td>
				<td>
					<%
						if(risorsa.getSesso().equals("m")){
					%>
							<label><%="Maschio" %></label>		
					<%
						}else{
					%>
							<label><%="Femmina" %></label>
					<%
						}
					%>
					
				</td>
			</tr>
			<tr>
				<td><label>Codice Fiscale</label></td>
				<td><label><%=risorsa.getCodiceFiscale() %></label></td>
			</tr>
			<tr>
				<td><label>Mail</label></td>
				<td><label><%=risorsa.getEmail() %></label></td>
			</tr>
			<tr>
				<td><label>Telefono</label></td>
				<td><label><%=risorsa.getTelefono() %></label></td>
			</tr>
			<tr>
				<td><label>Cellulare</label></td>
				<td>
					<%
						if(risorsa.getCellulare() != null){
							out.print("<label>"+risorsa.getCellulare()+"</label>");
						}else{
							out.print("");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Fax</label></td>
				<td><label><%=risorsa.getFax() %></label></td>
			</tr>
		</table>
	</fieldset>	
	<fieldset>
		<legend align="center">Residenza</legend>
		<table>
			<tr>
				<td><label>Indirizzo</label></td>
				<td><label><%=risorsa.getIndirizzo() %></label></td>
			</tr>
			<tr>
				<td><label>Città</label></td>
				<td><label><%=risorsa.getCitta() %></label></td>
			</tr>
			<tr>
				<td><label>Provincia</label></td>
				<td><label><%=risorsa.getProvincia() %></label></td>
			</tr>
			<tr>
				<td><label>Cap</label></td>
				<td><label><%=risorsa.getCap() %></label></td>
			</tr>
			<tr>
				<td><label>Nazione</label></td>
				<td><label><%=risorsa.getNazione() %></label></td>
			</tr>
			<tr>
				<td><label>Servizio Militare </label></td>
				<td>
					<label><%=risorsa.getServizioMilitare() %></label>
				</td>
			</tr>
		</table>
	</fieldset>
	<fieldset>
		<legend align="center">Altri Dati</legend>
		<table>
			<tr>
				<td><label>Patente </label></td>
				<td>
					<label><%=risorsa.getPatente() %></label>
				</td>
			</tr>
			<tr>
				<td><label>Costo al Giorno</label></td>
				<td>
					<label><%=risorsa.getCosto() %></label>
				</td>
			</tr>
			<tr>
				<td><label>Occupato</label></td>
				<td>
				<%
					if(risorsa.isOccupato()){ 
				%>
						<label>Si</label>
				<%
					}else{
				%>
						<label>No</label>
				<%
					}
				%>		
				</td>
			</tr>
			<tr>
				<td><label>Tipo Contratto</label></td>
				<td>
					<label><%=risorsa.getTipoContratto() %></label>
				</td>
			</tr>
			<tr>
				<td><label>Figura Professionale</label></td>
				<td>
					<label><%=risorsa.getFiguraProfessionale() %></label>
				</td>
			</tr>
			<tr>
				<td><label>Seniority</label></td>
				<td>
					<label><%=risorsa.getSeniority() %></label>
				</td>
			</tr>
		</table>
	</fieldset>
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