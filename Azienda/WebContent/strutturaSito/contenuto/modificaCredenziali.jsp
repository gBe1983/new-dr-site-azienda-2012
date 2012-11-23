<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.UtenteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>

  
<%
HttpSession sessioneModificaCredenziali = request.getSession();
if(sessioneModificaCredenziali.getAttribute("utenteLoggato") != null){
	UtenteDTO utente = (UtenteDTO) sessioneModificaCredenziali.getAttribute("credenziali");
	RisorsaDTO risorsa = (RisorsaDTO) request.getAttribute("risorsa");
 %>    
<div class="subtitle ">
	<h2>Modifica Credenzialità</h2>
</div>

<div id="flusso">
	<table>
		<tr>
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
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
		<li><a href="GestioneCurriculum?azione=caricamentoCv&tipoCreazione=visualizzaCurriculum&risorsa=<%=risorsa.getIdRisorsa() %>&page=visualizzaCurriculum">Anteprima C.V.</a></li>
		<li><a href="GestioneCurriculum?azione=caricamentoCv&risorsa=<%=risorsa.getIdRisorsa() %>">Modifica C.V.</a></li>				
<%
	}else{
%>
		<li><a href="index.jsp?azione=creazioneCv&risorsa=<%=risorsa.getIdRisorsa() %>&dispositiva=risorsa">Crea C.V.</a></li>
<%
	}
%>
	</ul>
</div>




<form action="./GestioneRisorse" method="post">
	<input type="hidden" name="azione" value="modificaCredenziali" />
	<input type="hidden" name="utente" value="<%=utente.getId_utente() %>" />
	<input type="hidden" name="email" value="<%=utente.getEmail() %>" />
	<fieldset>
		<legend>Modifica Credenziali</legend>
		<table>
			<tr>
				<td>
					<label>Username:</label>
				</td>
				<td>
					<input type="text" name="username" value="<%=utente.getUsername() %>" />
				</td>
			</tr>
			<tr>
				<td>
					<label>Password</label>
				</td>
				<td>
					<input type="password" name="password" value=""/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="submit" value="modifica credenziali" />
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