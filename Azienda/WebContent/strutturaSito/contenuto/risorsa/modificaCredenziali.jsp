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

<div class="subtitle">Modifica Credenzialità</div>

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




<form action="./GestioneRisorse" method="post">
	<input type="hidden" name="azione" value="modificaCredenziali" />
	<input type="hidden" name="utente" value="<%=utente.getId_utente() %>" />
	<input type="hidden" name="email" value="<%=utente.getEmail() %>" />
	<fieldset>
		<legend align="center">Modifica Credenziali</legend>
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
		</table>
	</fieldset>
	<table>
		<tr>
			<td colspan="2">
				<button type="submit" value="modifica credenziali" >Modifica Credenziali</button>
			</td>
		</tr>
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