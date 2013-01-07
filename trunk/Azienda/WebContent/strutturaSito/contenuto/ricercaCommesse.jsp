<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dto.TipologiaCommessa"%>

<script type="text/javascript"> 
	$(function() {
		var pickerOpts = {
			dateFormat: "dd-mm-yy"
		};
		$(".data:input").datepicker(pickerOpts);
	});
</script>

<%
	Calendar calendario = new GregorianCalendar();
	int anno = calendario.get(Calendar.YEAR);
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Ricerca Commesse</h2>
</div>

<form action="./GestioneCommessa" method="post" class="modificaTrattative">
	<input type="hidden" name="azione" value="visualizzaCommessa">
	
	<fieldset>
		<legend>Ricerca Commessa</legend>
		<table>	
			<tr>
				<td><label>Codice Commessa</label></td>
				<td><input type="text" name="codiceCommessa"></td>
			</tr>
			<tr>
				<td>Cliente</td>
				<td><select name="codice">
					<option value="" selected="selected">-- Seleziona il Cliente --</option>
					<%
						ArrayList listaClienti = (ArrayList) request.getAttribute("listaClienti");
						for(int x = 0; x < listaClienti.size(); x++){
							ClienteDTO cliente = (ClienteDTO) listaClienti.get(x);
							if(request.getAttribute("codice") != null){
								if(request.getAttribute("codice").equals(cliente.getId_cliente())){
					%>
											<option value="<%=cliente.getId_cliente() %>" selected="selected"><%=cliente.getRagioneSociale() %></option> 
					<%			
										break;
									}
								}else{
					%>
									<option value="<%=cliente.getId_cliente() %>" ><%=cliente.getRagioneSociale() %></option>
					<%			
								}
							}
					%>
					</select>
				</td>
			</tr>
			<tr>
				<td>Stato</td>
				<td>
					<select name="stato">
						<option value="" selected="selected">-- Seleziona lo stato --</option>
						<option value="aperta">Aperta</option>
						<option value="chiusa">Chiusa</option>
					</select>	
				</td>
			</tr>
			<tr>
				<td>Tipologia</td>
				<td>
					<select name="tipologiaCommessa" id="sceltaTipologia" onchange="tipologia(this.value)">
						<option value="" selected="selected">-- Scegliere il tipo di commessa --</option>
						<%
							ArrayList tipologie = (ArrayList) request.getAttribute("tipologiaCommessa");
							for(int x = 0; x < tipologie.size(); x++){
								TipologiaCommessa tipologia = (TipologiaCommessa) tipologie.get(x);
						%>	
								<option value="<%=tipologia.getId_tipologia()%>"><%=tipologia.getDescrizione() %></option>
						<%
							}
						%>
					</select>
				</td>
			</tr>
			<tr>
				<td>Anno</td>
				<td>
					<select name="anno">
						<%
							for(int x = 12; x < (Calendar.getInstance().get(Calendar.YEAR) - 1999); x++){
						%>
								<option value="<%=x %>"><%=(x+2000) %></option>	
						<%	
							}
						%>
					</select>
				</td>
			</tr>
			<tr>
				<td><input type="submit" value="ricerca Commessa"/></td>
				<td><input type="reset" value="cancella Dati"/></td>
			</tr>
		</table>
	</fieldset>
</form>

<%
	}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%	
	}
%>