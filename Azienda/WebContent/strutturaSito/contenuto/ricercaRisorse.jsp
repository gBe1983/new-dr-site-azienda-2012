<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>
<div class="subtitle ">Ricerca Risorsa</div>

<form action="./GestioneRisorse" method="post" class="spazio">
	<input type="hidden" name="azione" value="ricercaRisorse">
	<fieldset>
		<legend>Ricerca Risorse</legend>
	<table>
		<tr>
			<td><label>Cognome</label></td>
			<td><input type="text" name="cognome"></td>
		</tr>
		<tr>
			<td><label>Nome</label></td>
			<td><input type="text" name="nome"></td>
		</tr>
		<tr>
			<td><label>Costo Giornaliero</label></td>
			<td><input type="text" name="costo"></td>
		</tr>
		<tr>
			<td><label>Figura Professionale</label></td>
			<td><input type="text" name="figuraProfessionale"></td>
		</tr>
		<tr>
			<td><label>Seniority</label></td>
			<td>
				<select name="seniority">
						<option value=""> Seleziona</option>
						<option value="Junior">Junior</option>
						<option value="Medium" >Medium</option>
						<option value="Senior">Senior</option>
				</select>
			</td>
		</tr>
	</table>
	</fieldset>
	<table>
		<tr>
			<td><input type="submit" value="ricerca Risorsa"/></td>
			<td><input type="reset" value="cancella Dati"/></td>
		<tr>
	</table>
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