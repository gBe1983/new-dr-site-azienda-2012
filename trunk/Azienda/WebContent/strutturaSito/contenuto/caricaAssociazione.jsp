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
//mi serve per castare le varie date_inizio e date_fine delle varie commesse
SimpleDateFormat formattaDataWeb = new SimpleDateFormat("dd-MM-yyyy");

//mi serve per formattare le varie date_inizio e date_fine nel formato del DB
SimpleDateFormat formattaDataServer = new SimpleDateFormat("yyyy-MM-dd");

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
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
		</tr>
	</table>
</div>

<p class="spazio">* i campi segnati in asterisco sono obbligatori</p>

<fieldset>
	<legend>Caricamento Dati Commessa</legend>
	<form action="./GestioneCommessa" method="post" name="caricaAssociazione">
	<input type="hidden" name="azione" value="caricaAssociazione" />
	<input type="hidden" name="commessa" value="<%=request.getParameter("parametro") %>" />
	<input type="hidden" name="risorsa" value="<%=request.getParameter("parametro2") %>" />
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
				<input type="reset" value="svuota campi"/>
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
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
		</tr>
	</table>
</div>

<fieldset class="modificaTrattative">
	<legend>Caricamento Dati Commessa</legend>
	<form action="./GestioneCommessa" method="post">
		<input type="hidden" name="azione" value="modificaDissociazioneRisorsaCommessa" />
		<input type="hidden" name="commessa" value="<%=associazione.getId_commessa() %>" />
		<input type="hidden" name="risorsa" value="<%=associazione.getId_risorsa() %>" />
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
					<input type="text" name="dataFine" id="dataFine" value="<%=formattaDataWeb.format(formattaDataServer.parse(associazione.getDataFine())) %>" onblur="controlloDataFine_ModificaAssociazioneCommessa('<%=associazione.getId_commessa() %>',this.value)"/><label> (formato "dd-mm-yyyy")</label>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="modifica associazione" id="modificaAssociazione" disabled="disabled" onclick="return controlloDataCommessa()"/>
					<input type="reset" value="svuota campi"/>
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

