<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.Associaz_Risor_Comm"%>
<%@page import="it.azienda.dto.CommessaDTO"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle">
	<h2>Risorse Associate</h2>
</div>

<div id="flussoCommessa">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
			<td><a href="<%=controlloUtenteLoggato.getAttribute("url").toString() %>">Indietro</a></td>
		</tr>
	</table>
</div>

<%
	CommessaDTO commessa = null;
	
	if(request.getAttribute("commessa") != null){
		commessa = (CommessaDTO) request.getAttribute("commessa");
	}
	%>
	
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
	if(request.getAttribute("listaRisorseAssociate") != null){
		ArrayList listaAssociazioniCommessaRisorsa = (ArrayList) request.getAttribute("listaRisorseAssociate");
		if(listaAssociazioniCommessaRisorsa.size() > 0){
			Associaz_Risor_Comm asscommessa = new Associaz_Risor_Comm();
%>

			<table class="flusso" id="channel">
				<tr>
					<th>Descrizione Cliente</th>
					<th>Descrizione Risorsa</th>
					<th>Data Inizio </th>
					<th>Data Fine </th>
					<th>Importo </th>
					<th>Scelta </th>
				</tr>
<%
			for(int x = 0; x < listaAssociazioniCommessaRisorsa.size(); x++){
				asscommessa = (Associaz_Risor_Comm) listaAssociazioniCommessaRisorsa.get(x);
%>			
			
				<tr>
					<td>
						<%
							if(asscommessa.getDescrizioneCliente() != null){
								out.print(asscommessa.getDescrizioneCliente());
							}else{
								out.print("");
							}
						%>
					</td>
					<td><%=asscommessa.getDescrizioneRisorsa() %></td>
					<td>
						<%
							if(asscommessa.getDataInizio() != null){
								out.print(asscommessa.getDataInizio());
							}else{
								out.print("");
							}
						%>
					</td>
					<td>
						<%
							if(asscommessa.getDataFine() != null){
								out.print(asscommessa.getDataFine());
							}else{
								out.print("");
							}
						%>
					</td>
					<td><%
							if(asscommessa.getTotaleImporto() != 0.0){
								out.print(asscommessa.getTotaleImporto());
							}else{
								out.print("");
							}		
						%>
					</td>
					<td>
						<%
							if(asscommessa.isAttiva()){
						%>
								<a href="./GestioneCommessa?azione=dissociazioneRisorsaCommessa&parametro=<%=asscommessa.getId_associazione() %>&tipologia=<%=commessa.getTipologia() %>" ><img src="images/dissociazioneRisorsaCommessa.png" alt="dissociazione risorsa" id="dissociazioneRisorsa"></a>
						<%
							}
						%>
					</td>
				</tr>
<%		
			}
%>
			</table>
<%
		}else{
%>
			<p class="spazio" align="center">Non ci sono risorse associate a questa commessa</p>
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