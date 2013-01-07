<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.TrattativeDTO"%>
<%@page import="java.lang.reflect.Array"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.GregorianCalendar"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Visualizza Trattative</h2>
</div>
	
<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=tutte&dispositiva=trattative"">Cerca</a></td>
		</tr>
	</table>
</div>

<div class="spazio" id="ricercaTrattattive">
	<form action="./GestioneTrattattive?azione=ricercaTrattativaCliente" method="post" name="visualizzaTrattative">
	<%
		if(request.getParameter("tipo").equals("azienda")){
	%>		
			<input type="hidden" name="tipo" value="azienda" >
	<%		
		}else{
	%>
			<input type="hidden" name="tipo" value="tutte" >
	<%
		}
		if(request.getParameter("dispositiva").equals("trattative")){
	%>		
			<input type="hidden" name="dispositiva" value="trattative" >
	<%		
		}else{
	%>
			<input type="hidden" name="dispositiva" value="cliente" >
	<%
		}
	%>

	
	<table id="ricercaTrattative">
		<tr>
			<td>
				<select name="codice" onchange="valorizzazioneTrattativeRisorsa()">
				<%
					if(request.getParameter("tipo") != null){
						if(request.getParameter("tipo").equals("tutte")){
				%>
							<option value="" selected="selected">-- Seleziona il Cliente --</option>
				<%
						}
					}
					ArrayList listaClienti = (ArrayList) request.getAttribute("listaClienti");
					for(int x = 0; x < listaClienti.size(); x++){
							ClienteDTO cliente = (ClienteDTO) listaClienti.get(x);
							if(request.getAttribute("codice") != null){
								if(request.getAttribute("codice").equals(cliente.getId_cliente())){
				%>
										<option value="<%=cliente.getId_cliente() %>" selected="selected"><%=cliente.getRagioneSociale() %></option> 
				<%			
									break;
								}
							}else{
				%>
								<option value="<%=cliente.getId_cliente() %>" ><%=cliente.getRagioneSociale() %></option>
				<%			
							}
						}
				%>
				</select>
				</td>
				<td>
					<%@include file="valorizzazioneRisorsaTrattative.jsp" %>
				</td>
				<td>
					<select name="esito">
						<option value="">-- Seleziona lo Stato --</option>
						<option value="aperta">aperta</option>
						<option value="persa">persa</option>
						<option value="commessaPresa">commessa presa</option>
					</select>
				</td>
				<td>
					<select name="anno">
						<option value="0">-- Seleziona Anno --</option>
						<%
							for(int x = 12; x < (Calendar.getInstance().get(Calendar.YEAR) - 1999); x++){
						%>
								<option value="<%=x %>"><%=(x+2000) %></option>	
						<%	
							}
						%>
					</select>
				</td>
				<td>
					<input type="submit" value="ricerca">
				</td>
			</tr>
		</table>
		</form>
</div>
	<%
	
	if(request.getAttribute("listaTrattattive") != null){
		if(((ArrayList) request.getAttribute("listaTrattattive")).size() > 0 && request.getParameter("dettaglioTrattativa") == null){
			ArrayList listaTrattative = (ArrayList) request.getAttribute("listaTrattattive");

	%>
	<div class="spazioUltra">
			<table id="channel">
				<th>Cliente</th>
				<th>Risorsa</th>
				<th>Data</th>
				<th>Oggetto</th>
				<th>Esito/CodiceCommessa</th>
				<th colspan="2"> Scelta </th>
					
				
	<%
			for(int y = 0; y < listaTrattative.size(); y++){
				TrattativeDTO trattative = (TrattativeDTO) listaTrattative.get(y);
	%>
					
					<tr>
						<td>
							<%out.print(trattative.getDescrizioneCliente());  %>
						</td>
						<td>
							<%
								if(trattative.getDescrizioneRisorsa() != null){
									out.print(trattative.getDescrizioneRisorsa());
								} %>
						</td>
		
						<td>
							<%=trattative.getData() %>
						</td>
						<td>
							<%
								if(trattative.getOggetto().length() < 10){
									out.print(trattative.getOggetto().substring(0, trattative.getOggetto().length()));	
								}else{
									out.print(trattative.getOggetto().substring(0, 10));
								}
							%>
						</td>
						<td>
							<%
							  if(trattative.getEsito() != null){
								out.print(trattative.getEsito());
							  }
							%>
						</td>
							<%
								if(request.getParameter("dispositiva") != null){
									if(request.getParameter("dispositiva").equals("cliente")){
							%>
										<td>
											<a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=azienda&dispositiva=cliente&dettaglioTrattativa=<%=trattative.getIdTrattative() %>"><img src="images/dettaglio.gif" alt="dettaglio trattativa" id="dettaglio"/></a>
										</td>
							<%
									}else{
							%>
										<td>
											<a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=tutte&dispositiva=trattative&dettaglioTrattativa=<%=trattative.getIdTrattative() %>"><img src="images/dettaglio.gif" alt="dettaglio trattativa" id="dettaglio"/></a>
										</td>
							<%		
									}
								}
							
							if(trattative.getEsito() != null){
								if(trattative.getEsito().equals("aperta")){
									if(request.getParameter("dispositiva") != null){
										if(request.getParameter("dispositiva").equals("cliente")){
								%>
											<td>
												<a href="./GestioneTrattattive?azione=aggiornaTrattativa&trattativa=<%=trattative.getIdTrattative() %>&tipo=azienda&dispositiva=cliente&codice=<%=trattative.getId_cliente() %>"><img src="images/modifica.png" alt="modifica trattativa" id="modifica"/></a>
											</td>
								<%
										}else{
								%>
											<td>
												<a href="./GestioneTrattattive?azione=aggiornaTrattativa&trattativa=<%=trattative.getIdTrattative() %>&dispositiva=trattative"><img src="images/modifica.png" alt="modifica trattativa" id="modifica"/></a>
											</td>
								<%		
										}
									}
								}else{
								%>	
									<td>
										<br>
									</td>
								<%
								}
							}else{
								%>	
									<td>
										<br>
									</td>
								<%
							}
							%>
						</tr>		
	<%
				}
	%>
		</table>
	</div>
	<%
			}else if(((ArrayList) request.getAttribute("listaTrattattive")).size() == 1 && request.getParameter("dettaglioTrattativa") != null){
				TrattativeDTO trattative = (TrattativeDTO) ((ArrayList) request.getAttribute("listaTrattattive")).get(0);
	%>			
	
			<%
				if(trattative.getEsito().equals("aperta")){
					if(request.getParameter("dispositiva") != null){
						if(request.getParameter("dispositiva").equals("cliente")){
			%>
							<div id="bluemenu" class="bluetabs" >
								<ul>
									<li><a href="./GestioneTrattattive?azione=aggiornaTrattativa&trattativa=<%=trattative.getIdTrattative() %>&tipo=azienda&dispositiva=cliente&codice=<%=trattative.getId_cliente() %>">Modifica Trattativa</a></li>
								</ul>
							</div>
			<%
						}else{
			%>	
							<div id="bluemenu" class="bluetabs">
								<ul>
									<li><a href="./GestioneTrattattive?azione=aggiornaTrattativa&trattativa=<%=trattative.getIdTrattative() %>&dispositiva=trattative">Modifica Trattativa</a></li>
								</ul>
							</div>
			<%
						}
					}
				}else if(!trattative.getEsito().equals("aperta") && !trattative.getEsito().equals("persa")){
			%>	
					<div id="bluemenu" class="bluetabs">
						<ul>
							<li><a href="./GestioneCommessa?azione=visualizzaCommessa&codiceCommessa=<%=trattative.getEsito() %>&dispositiva=commessa">Modifica Trattativa</a></li>
						</ul>
					</div>
			<%
				}
			%>
			<fieldset>
				<legend>Visualizzazione Trattativa Cliente</legend>
				<table>
					<tr>
						<td><label>Cliente</label></td>
						<td><%=trattative.getDescrizioneCliente() %></td>
					</tr>
					<tr>
						<td><label>Risorsa</label></td>
						<td>
							<%
							if(trattative.getDescrizioneRisorsa() != null){
								out.print(trattative.getDescrizioneRisorsa());
							}
							%>
						</td>
					</tr>
					<tr>
						<td><label>Data</label></td>
						<td><%=trattative.getData() %></td>
					</tr>
					<tr>
						<td><label>Oggetto</label></td>
						<td><%=trattative.getOggetto() %></td>
					</tr>
					<tr>
						<td><label>Esito</label></td>
						<td><%=trattative.getEsito() %></td>
					</tr>
				</table>
			</fieldset>
	<%		
			}else{
	%>
				<p align="center">Nessun tipo di Trattative per questo cliente e risorsa</p>	
	<%
			}
		}else{
	%>
			<p align="center">Nessun tipo di Trattative per questo cliente e risorsa</p>
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
