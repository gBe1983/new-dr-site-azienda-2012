<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>

<div class="subtitle ">
	<h2>Visualizza Risorse</h2>
</div>

<div id="flusso">
	<table>
		<tr>
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
		</tr>
	</table>
</div>

<% 
HttpSession sessioneRisorse = request.getSession();
if(sessioneRisorse.getAttribute("utenteLoggato") != null){
	ArrayList listaRisorse = new ArrayList();
	if(sessioneRisorse.getAttribute("listaRisorse") != null){
		listaRisorse = (ArrayList) sessioneRisorse.getAttribute("listaRisorse");
		if(listaRisorse.size() > 0){
%>
		<table id="visualizzaRisorsa">
			<th>Cognome</th>
			<th>Nome</th>
			<th>Costo</th>
			<th>Figura Professionale</th>
			<th>Seniority</th>
			<th>Citt�</th>
			<tr>
				<td colspan="8" /><hr size="1"></td>
			</tr>
<%
		for(int x = 0; x < listaRisorse.size(); x++){
			
			RisorsaDTO risorsa = (RisorsaDTO) listaRisorse.get(x);
%>
				<tr>
					<td><%=risorsa.getCognome() %></td>
					<td><%=risorsa.getNome() %></td>
					<td><%=risorsa.getCosto() %></td>
					<td><%=risorsa.getFiguraProfessionale() %></td>
					<td><%=risorsa.getSeniority() %></td>
					<td><%=risorsa.getCitta() %></td>
					<td><a href="GestioneRisorse?azione=caricamentoProfiloRisorsa&page=dettaglio&risorsa=<%=risorsa.getIdRisorsa() %>"><img src="images/dettaglio.gif" alt="dettaglio risorsa" id="dettaglio"/></a></td>
					<td><a href="GestioneRisorse?azione=eliminaRisorsa&risorsa=<%=risorsa.getIdRisorsa() %>" onclick="return confirm('Vuoi disabilitare questa risorsa?');"><img src="images/elimina.jpg" alt="dettaglio risorsa" id="disabilita"/></a></td>
				</tr>
		
		
<%
			}
		}
	}else{
%>
		<p>Non sono presenti risorse con i criteri ricercati.</p> 
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
</table>