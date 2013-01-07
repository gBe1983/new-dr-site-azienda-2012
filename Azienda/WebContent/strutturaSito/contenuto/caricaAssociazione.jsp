<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.Associaz_Risor_Comm"%>
<%@page import="java.text.SimpleDateFormat"%>

<script type="text/javascript">
	
	$(function() {
		var pickerOpts = {
			dateFormat: "dd-mm-yy"
		};
		$(".data:input").datepicker(pickerOpts);
	});
</script>

<%

HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	if(request.getParameter("azione").equals("caricaAssociazione")){
%>
<div class="subtitle ">
	<h2>Associazione Risorsa</h2>
</div>


<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
		</tr>
	</table>
</div>

<p class="spazio">* i campi segnati in asterisco sono obbligatori</p>

<fieldset>
	<legend>Caricamento Dati Commessa</legend>
	<form action="./GestioneCommessa" method="post" name="caricaAssociazione">
	<input type="hidden" name="azione" value="caricaAssociazione" />
	<input type="hidden" name="parametro" value="<%=request.getParameter("parametro") %>" />
	<input type="hidden" name="parametro2" value="<%=request.getParameter("parametro2") %>" />
	<table>
		<tr>
			<td><label>* Data Inizio</label></td>
			<td>
				<input type="text" name="dataInizio" id="dataInizio" onblur="controlloDataInizio_Associazione('<%=request.getParameter("parametro") %>',this.value)" /><label> (formato "dd-mm-yyyy")</label>
			</td>
		</tr>
		<tr>
			<td><label>* Data Fine</label></td>
			<td>
				<input type="text" name="dataFine" id="dataFine" onblur="controlloDataCommessa('<%=request.getParameter("parametro") %>',this.value)"/><label> (formato "dd-mm-yyyy")</label>
			</td>
		</tr>
		<tr>
			<td><label>* Importo</label></td>
			<td>
				<input type="text" name="importo"/>
			</td>
		</tr>
		<tr>
			<td>Al</td>
			<td>
				<select name="commessaEsternaSingola_al">
					<option value=""></option>
					<option value="ore">ore</option>
					<option value="giorno">giorno</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="inserisci associazione" id="inserisciAssociazione" disabled="disabled" onclick="return controlloDateAssociazioni()"/>
			</td>
		</tr>
	</table>
	</form>
</fieldset>
<%
	}else if(request.getParameter("azione").equals("dettaglioAssociazione")){
		Associaz_Risor_Comm associazione = (Associaz_Risor_Comm)request.getAttribute("associazione_commessa");
%>
<div class="subtitle ">
	<h2>Modifica Associzione Risorsa</h2>
</div>


<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
		</tr>
	</table>
</div>

<fieldset class="spazio">
	<legend>Caricamento Dati Commessa</legend>
	<form action="./GestioneCommessa" method="post">
		<input type="hidden" name="azione" value="modificaDissociazioneRisorsaCommessa" />
		<input type="hidden" name="parametro" value="<%=associazione.getId_associazione() %>" />
		<table>
			<tr>
				<td><label>Data Inizio</label></td>
				<td>
					<input type="text" name="dataInizio" value="<%=associazione.getDataInizio() %>" readonly="readonly"/>
				</td>
			</tr>
			<tr>
				<td><label>Data Fine</label></td>
				<td>
					<input type="text" name="dataFine" id="dataFine" value="<%=associazione.getDataFine() %>" onblur="controlloDataFine_ModificaAssociazioneCommessa('<%=associazione.getId_commessa() %>',this.value)"/><label> (formato "dd-mm-yyyy")</label>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="modifica associazione" id="modificaAssociazione" disabled="disabled" onclick="return controlloDataCommessa()"/>
				</td>
			</tr>
		</table>
	</form>
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

