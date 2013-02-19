<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.CommessaDTO"%>
<%@page import="it.azienda.dto.PlanningDTO"%>

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
	ArrayList listaCommessa = (ArrayList) request.getAttribute("listaCommesse");
	ArrayList listaRisorse = (ArrayList) request.getAttribute("listaRisorse");
	ArrayList listaClienti = (ArrayList) request.getAttribute("listaCliente");
	if(request.getAttribute("report") == null){
%>

<div class="subtitle">Ricerca Report</div>

<fieldset class="modificaTrattative">
	<legend>Ricerca Report</legend>
	<form action="./GestioneReport" method="post" name="report">
			<input type="hidden" name="azione" value="ricercaReport">
			<table>
				<tr>
					<td>Cliente: </td>
					<td>
						<select name="cliente" selected="selected">
							<option value="">-- Seleziona Cliente --</option>
					 		<%
					 			for(int x = 0; x < listaClienti.size(); x++){
					 				ClienteDTO cliente = (ClienteDTO) listaClienti.get(x);
					 		%>
					 				<option value="<%=cliente.getId_cliente() %>"><%=cliente.getRagioneSociale() %></option>
					 		<%
					 			}
					 		%>
					 	</select>
					 </td>
				</tr>
				<tr>
					<td>Risorsa: </td>
					<td>
						<select name="risorsa">
					 		<option value="" selected="selected">-- Seleziona Risorsa --</option>
					 		<%
					 			for(int x = 0; x < listaRisorse.size(); x++){
					 				RisorsaDTO risorsa = (RisorsaDTO) listaRisorse.get(x);
					 		%>
					 				<option value="<%=risorsa.getIdRisorsa() %>"><%=risorsa.getCognome() + " " + risorsa.getNome() %></option>
					 		<%
					 			}
					 		%>
					 	</select>
					 </td>
				</tr>
				<tr>
					<td>Commessa: </td>
					<td>
						<select name="commessa">
					 		<option value="" selected="selected">-- Seleziona Commessa --</option>
					 		<%
					 			for(int x = 0; x < listaCommessa.size(); x++){
					 				CommessaDTO commessa = (CommessaDTO) listaCommessa.get(x);
					 		%>
					 				<option value="<%=commessa.getId_commessa() %>"><%=commessa.getCodiceCommessa() + " - " + commessa.getDescrizione() %></option>
					 		<%
					 			}
					 		%>
					 	</select>
					 </td>
				</tr>
				<tr>
					<td>Da:</td>
					<td>
						<input type="text" name="da" class="data"/>
					</td>
				</tr>
				<tr>
					<td>a:</td>
					<td>
						<input type="text" name="a" class="data"/>
					</td>
				</tr>
				<tr>
					<td><input type="submit" value="ricerca report" onclick="return controlloDateReport()"></td>
				<tr>
			</table>		
		</form>
</fieldset>
<%
	}else{
		ArrayList listaReport = (ArrayList)request.getAttribute("report");
		if(listaReport.size() > 0){
%>

<div class="subtitle ">
	<h2>Visualizza Report</h2>
</div>

<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="./GestioneReport?azione=visualizzaReport">Cerca</a></td>
		</tr>
	</table>
</div>	

		<table id="visualizzaReport">
			<th>Totale Ore </th>
			<th>Risorsa</th>
			<th>Cliente</th>
			<th>Commessa</th>
			<tr>
				<td colspan="4" /><hr size="1"></td>
			</tr>
		<%
			for(int x = 0; x < listaReport.size(); x++){
				PlanningDTO planning = (PlanningDTO) listaReport.get(x);
		%>
				<tr>
					<td>
						<%=planning.getNumero_ore() %>
					</td>
					<td>
						<%=planning.getCognome() + " " + planning.getNome() %>
					</td>
					<td>
						<%=planning.getRagione_sociale() %>
					</td>
					<td>
						<%=planning.getCodice_commessa() + " - " + planning.getDescrizione_commessa()%>
					</td>
				</tr>		
		<%		
			}
		%>
		</table>	
<%
		}else{
%>
		<div id="flusso">
			<table>
				<tr>
					<td><a href="index.jsp?azione=homePage">Home</a></td>
					<td><a href="./GestioneReport?azione=visualizzaReport">Cerca</a></td>
				</tr>
			</table>
		</div>	
		<p class="modificaTrattative" align="center">Non sono presenti report per il tipo di ricerca effettuata. <br><br><a href="./GestioneReport?azione=visualizzaReport">Home</a> </p>
			
<%	
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