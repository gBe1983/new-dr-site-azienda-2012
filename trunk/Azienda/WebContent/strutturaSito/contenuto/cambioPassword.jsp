<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.UtenteDTO"%>

<%
	HttpSession sessioneCambioPassword = request.getSession();
	if(sessioneCambioPassword.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Cambio Password</h2>
</div>

<fieldset class="spazio">
	<legend>Cambio Password</legend>
	<form action="./GestioneAzienda" method="post" name="vecchiaPassword">
		<input type="hidden" name="azione" value="cambioPassword">
		<table>
			<tr>
				<td>
					<label>Nuova Password</label>
				</td>
				<td>
					<input type="password" name="nuovaPassword" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Conferma Nuova Password</label>
				</td>
				<td>
					<input type="password" name="confermaNuovaPassword" />
				</td>
			</tr>
			<tr>
				<td>
					<input type="submit" value="cambia Password" onclick="return controlloPassword('<%=((UtenteDTO)sessioneCambioPassword.getAttribute("utenteLoggato")).getPassword() %>')" />
				</td>
				<td>
					<input type="reset" value="cancella" />
				</td>
			</tr>
		</table>
	</form>
</fieldset>
<%
	}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
	}
%>