<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Calendar"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">Chiudi Mensilità</div>

<p>
	In questa sezione potrai gestire la chiusura dei mesi dell'anno corrette in modo da non dare più la possibilità di modificare le ore dei mesi già fatturati.
</p>

<form action="./GestioneCommessa" method="post" class="spazio">
	<input type="hidden" name="azione" value="chiudiMensilita" />
	<fieldset>
		<legend align="center">Chiusura Mensilità</legend>
		<table align="center">
			<tr>
				<td>
					<select name="mese">
						<option value="1">Gennaio</option>
						<option value="2">Febbraio</option>
						<option value="3">Marzo</option>
						<option value="4">Aprile</option>
						<option value="5">Maggio</option>
						<option value="6">Giugno</option>
						<option value="7">Luglio</option>
						<option value="8">Agosto</option>
						<option value="9">Settembre</option>
						<option value="10">Ottobre</option>
						<option value="11">Novembre</option>
						<option value="12">Dicembre</option>
					</select>
				</td>
				<td>
					<select name="anno">
					<%	
						Calendar calendario = Calendar.getInstance();
						int anno = calendario.get(Calendar.YEAR);
						for(int x = anno; x >= 1900; x--){
					%>		
							<option value="<%=x %>"><%=x %></option>
					<%	
						}
					%>
					</select>
				</td>
				<td>
					<button type="submit" value="chiudiMensilita" >Chiudi Mensilita</button>
				</td>
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