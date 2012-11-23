<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.CommessaDTO"%>


<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>
<div class="subtitle ">
	<h2>Visualizza Commesse</h2>
</div>

<div id="flusso">
	<table>
		<tr>
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
		</tr>
	</table>
</div>

<%
	if(request.getAttribute("chiusuraCommessa") != null){
		out.print("<p align=\"center\" class=\"spazio\">"+request.getAttribute("chiusuraCommessa").toString()+"</p>");
	}

	if(request.getAttribute("listaCommesse") != null){
		ArrayList listaCommesse = (ArrayList) request.getAttribute("listaCommesse");
		if(listaCommesse.size() > 0){		
%>
		<table id="visualizzaCommesse">
			<th>Cliente</th>
			<th>Codice Commessa</th>
			<th>Data Inizio</th>
			<th>Data Fine</th>
			<th>Importo Commessa</th>
			<th>Stato</th>
			<th>Tipologia</th>
			<tr>
				<td colspan="9"><hr size="1"></td>
			</tr>
<%
		for(int x = 0; x < listaCommesse.size(); x++){
			CommessaDTO visualizzaCommessa = (CommessaDTO)listaCommesse.get(x);
%>			
					<tr>
						<td>
							<label>
								<%
									if(visualizzaCommessa.getDescrizioneCliente() != null){
										out.print(visualizzaCommessa.getDescrizioneCliente());
									}
								%>
							</label>
						</td>
						<td><label><%=visualizzaCommessa.getCodiceCommessa() %></label></td>
						<td>
							<label>
									<%
										if(visualizzaCommessa.getData_inizio() != null){
											out.print(visualizzaCommessa.getData_inizio());
										}
									%>
							</label>
						</td>
						<td>
							<label>
									<%
										if(visualizzaCommessa.getData_fine() != null){
											out.print(visualizzaCommessa.getData_fine());
										}
									%>
							</label>
						</td>
						<td>
							<label>
									<%
										if(visualizzaCommessa.getImporto() != 0){
											out.print(visualizzaCommessa.getImporto());
										}
									%>
							</label>
						</td>
						<td>
							<label>
									<%
										if(visualizzaCommessa.getStato() != null){
											out.print(visualizzaCommessa.getStato());
										}
									%>
							</label>
						</td>
						<td>
							<label>
									<%
										if(visualizzaCommessa.getTipologia().equals("1")){
											out.print("Singola");
										}else if(visualizzaCommessa.getTipologia().equals("2")){
											out.print("Multipla");
										}else if(visualizzaCommessa.getTipologia().equals("3")){
											out.print("Interna");
										}else if(visualizzaCommessa.getTipologia().equals("4")){
											out.print("Altro");	
										}
									%>
							</label>
						</td>
						<td>
							<a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=visualizzaCommessa.getId_commessa() %>"><img src="images/dettaglio.gif" alt="dettaglio commessa" id="dettaglio"/></a>
						</td>
							<%
								if(!visualizzaCommessa.getTipologia().equals("4")){
									if(visualizzaCommessa.isAttiva()){
							%>
										<td>
											<a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=visualizzaCommessa.getId_commessa() %>" onclick="return confirm('Vuoi chiudere questa commessa?');"><img src="images/elimina.jpg" alt="dettaglio risorsa" id="disabilita"/></a>
										</td>
							<%
									}
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
			<p align="center" class="modificaTrattative">Non ci sono Commessa trovate! Riprovare a effettuare una nuova ricerca.<br><br> 
			<a href="index.jsp?azione=homePage">Home</a></p>
			
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