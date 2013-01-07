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
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
		</tr>
	</table>
</div>
<div class="spazio">
	<div class="visualizzaChannel">
<% 
HttpSession sessioneRisorse = request.getSession();
if(sessioneRisorse.getAttribute("utenteLoggato") != null){
	ArrayList listaRisorse = new ArrayList();
	if(sessioneRisorse.getAttribute("listaRisorse") != null){
		listaRisorse = (ArrayList) sessioneRisorse.getAttribute("listaRisorse");
		if(listaRisorse.size() > 0){
%>
		<table id="channel">
			<th>Cognome</th>
			<th>Nome</th>
			<th>Costo</th>
			<th>Figura Professionale</th>
			<th>Seniority</th>
			<th>Città</th>
			<th colspan="2">Scelta</th>
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
	</div>
</div>