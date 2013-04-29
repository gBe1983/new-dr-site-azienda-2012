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


	<div id="flusso">
		<table>
			<tr>
				<td><a href="index.jsp?azione=homePage">Home</a></td>
				<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
			</tr>
		</table>
	</div>
	
	<div id="bluemenu" class="bluetabs" class="spazio">
		<%
		if(commessa.getStato() != null){
			if(commessa.getStato().equals("aperta") && !commessa.getTipologia().equals("1")){
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Modifica Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=risorseDaAssociare&codice=<%=commessa.getId_cliente() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>

		<%
			}else if(commessa.getStato().equals("aperta") && commessa.getTipologia().equals("1")){
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Modifica Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>

		<%
			}else{
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Dettaglio Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&stato=<%=commessa.getStato() %>&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>
		<%
			}
		}else{
		%>	
			<ul>
				<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Modifica Commessa</a></li>
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
			if(!commessa.getTipologia().equals("4")){
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
					<td><a href="./index.jsp?azione=caricaAssociazione&parametro=<%=request.getParameter("parametro")%>&parametro2=<%=risorsa.getIdRisorsa()%>"><img src="images/add.png" alt="aggingi risorsa" id="aggiungiRisorsa"></a></td>
				</tr>

<%
			}
		
%>
		</table>
<%
			}else{
%>
			<form action="./GestioneCommessa" method="post" name="associazioni">
				<input type="hidden" name="azione" value="caricaAssociazione">
				<input type="hidden" name="tipologia" value="<%=request.getParameter("tipologia") %>">
				<input type="hidden" name="commessa" value="<%=request.getParameter("parametro") %>">
				<input type="hidden" name="risorseSelezionate" value="">
				<table id="channel">
				<tr id="salva">
					<td colspan="6"><input type="submit" value="salva" onclick="return salvaAssociazioni('parametro')"></td>
				</tr>
				<tr>
					<th>Cognome</th>
					<th>Nome</th>
					<th>Costo</th>
					<th>Figura Professionale</th>
					<th>Seniority</th>
					<th><input type="checkbox" name="checkAll" id="checkboxAll" onclick="checkedAll(this,'parametro')"></th>
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
						<td><input type="checkbox" name="parametro<%=x %>" value="<%=risorsa.getIdRisorsa() %>" class="case" onclick="checkedNotAll(this)"></td>
					</tr>

	<%
				}
			
	%>
				</table>
			</form>
	<%
				}
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