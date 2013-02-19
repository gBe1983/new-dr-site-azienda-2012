<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.CommessaDTO"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){

	CommessaDTO commessa = null;
	
	if(request.getAttribute("commessa") != null){
		commessa = (CommessaDTO) request.getAttribute("commessa");
	}
%>

	<div class="subtitle ">Risorse Da Associare</div>


	<div id="flussoCommessa">
		<table>
			<tr>
				<td><a href="index.jsp?azione=homePage">Home</a></td>
				<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
				<td><a href="<%=controlloUtenteLoggato.getAttribute("url").toString() %>">Indietro</a></td>
			</tr>
		</table>
	</div>
	
	<div id="bluemenu" class="bluetabs" class="spazio">
		<%
			if(commessa.getStato() != null){
				if(commessa.getStato().equals("aperta") && !commessa.getTipologia().equals("1")){
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>">Modifica Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=risorseDaAssociare&codice=<%=commessa.getId_cliente() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>

		<%
			}else if(commessa.getStato().equals("aperta") && commessa.getTipologia().equals("1")){
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>">Modifica Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>

		<%
			}else{
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>">Dettaglio Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&stato=<%=commessa.getStato() %>&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>
		<%
			}
		}else{
		%>	
			<ul>
				<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>">Dettaglio Commessa</a></li>
				<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
				<li><a href="./GestioneCommessa?azione=risorseDaAssociare&tipologia=<%=commessa.getTipologia() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
			</ul>
		<%	
		}
		%>
	</div>

<%
	if(request.getAttribute("listaRisorseDaAssociare") != null){
		ArrayList listaRisorseDaAssociare = (ArrayList) request.getAttribute("listaRisorseDaAssociare");
		if(listaRisorseDaAssociare.size() > 0){
%>
		
		
		<table id="channel">
			<tr>
				<th>Cognome</th>
				<th>Nome</th>
				<th>Costo</th>
				<th>Figura Professionale</th>
				<th>Seniority</th>
				<th>Scelta</th>
			</tr>
			
<%
		
			for(int x = 0; x < listaRisorseDaAssociare.size(); x++){
				RisorsaDTO risorsa = (RisorsaDTO) listaRisorseDaAssociare.get(x);
%>			
				<tr>
					<td><%=risorsa.getCognome() %></td>
					<td><%=risorsa.getNome() %></td>
					<td><%=risorsa.getCosto() %></td>
					<td><%=risorsa.getFiguraProfessionale() %></td>
					<td><%=risorsa.getSeniority() %></td>
					<%
						if(commessa.getTipologia().equals("4")){ 
					%>
							<td><a href="./GestioneCommessa?azione=caricaAssociazione&tipologia=<%=request.getParameter("tipologia")%>&commessa=<%=request.getParameter("parametro")%>&parametro2=<%=risorsa.getIdRisorsa() %>"><img src="images/add.png" alt="aggingi risorsa" id="aggiungiRisorsa"></a></td>
					<%
						}else{
					%>
							<td><a href="./index.jsp?azione=caricaAssociazione&parametro=<%=request.getParameter("parametro")%>&parametro2=<%=risorsa.getIdRisorsa()%>"><img src="images/add.png" alt="aggingi risorsa" id="aggiungiRisorsa"></a></td>
					<%
						}
					%>
				</tr>

<%
			}
		
%>
		</table>
<%
		}else{
%>
			<p class="spazio" align="center">Non ci sono risorse da associare per questa commessa. </p>
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