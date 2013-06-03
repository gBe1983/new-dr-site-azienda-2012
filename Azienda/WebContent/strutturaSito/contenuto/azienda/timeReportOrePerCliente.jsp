<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="it.bo.Day"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.bo.azienda.TimeReport"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.CommessaDTO"%>
<%@page import="it.azienda.dto.PlanningDTO"%>
<%@page import="java.util.HashMap"%>
<%@page import="it.azienda.dto.Associaz_Risor_Comm"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>




<%@page import="java.util.Map"%><script type="text/javascript"> 
	$(function() {
		var pickerOpts = {
			dateFormat: "dd-mm-yy"
		};
		$(".data:input").datepicker(pickerOpts);
	});
</script>

<%
if(request.getSession().getAttribute("utenteLoggato") != null){
	List<CommessaDTO>commesse=(List<CommessaDTO>)request.getAttribute("commesse");
	List<RisorsaDTO>risorse=(List<RisorsaDTO>)request.getAttribute("risorse");
	List<ClienteDTO>clienti=(List<ClienteDTO>)request.getAttribute("clienti");
	String dtDa=(String)request.getAttribute("dtDa");
	String dtA=(String)request.getAttribute("dtA");
	ArrayList<String> mesi = (ArrayList<String>) request.getAttribute("calendario");
	ArrayList<ClienteDTO> listaClienti = (ArrayList<ClienteDTO>) request.getAttribute("listaClienti");
	ArrayList<Associaz_Risor_Comm> listaAssociazioni = (ArrayList<Associaz_Risor_Comm>) request.getAttribute("listaAssCommessa");
	ArrayList<PlanningDTO> listaGiornate = (ArrayList<PlanningDTO>) request.getAttribute("listaGiornate");

%>

<div class="subtitle">Visualizzazzione Consuntuvi</div>

<form action="./GestioneReport" method="post" name="report">
	<input type="hidden" name="azione" value="visualizzaConsuntivi">
	<table class="filter">
		<tr>
			<td>Da:</td>
			<td>
				<input type="text" name="dtDa" class="data" value="<%=dtDa%>"/>
			</td>
			<td>a:</td>
			<td>
				<input type="text" name="dtA" class="data" value="<%=dtA%>"/>
			</td>
		</tr>
		
		<tr>
			<td>Clienti:</td>
			<td>
				<select name="cliente" class="filter">
					<option value="all">-- Tutti i Clienti --</option>
<%
	for(ClienteDTO cliente:clienti){
%>
					<option value="<%=cliente.getId_cliente()%>">
						<%=cliente.getRagioneSociale()%>
					</option>
<%
	}
%>
				</select>
			</td>
			<td>Commesse:</td>
			<td>
				<select name="commessa" class="filter">
					<option value="all">
						-- Tutte le Commesse --
					</option>
<%
	for(CommessaDTO commessa:commesse){
%>
					<option value="<%=commessa.getId_commessa() %>">
						<%=commessa.getCodiceCommessa() + " - " + commessa.getDescrizione() %>
					</option>
<%
	}
%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Risorse:</td>
			<td colspan="1">
				<select name="risorsa" class="filter">
					<option value="all">
						-- Tutte le Risorse --
					</option>
<%
	for(RisorsaDTO risorsa:risorse){
%>
					<option value="<%=risorsa.getIdRisorsa()%>">
						<%=risorsa.getCognome()%> <%=risorsa.getNome()%>
					</option>
<%
	}
%>
				</select>
			</td>
			<td>Tipologia: </td>
			<td>
				<select name="tipologiaReport">
					<option value="" selected="selected">-- Seleziona la Tipologia -- </option>
					<option value="1">Ore per Cliente</option>
					<option value="2">Ore per Commessa</option>
					<option value="3">Ore per Dipendente</option>
				</select>
			</td>
			<td>
				<input type="submit" value="Cerca Consuntivi" onclick="return controlloDateReport()">
			</td>
		</tr>
	</table>
</form>
<br>

<%
	if(listaGiornate.size() > 0){
	
	%>
		<div class="timeReport">
		<table class="timeReport" border="0">
			<tr>
				<td class="intestazioni">
					<span>Cliente/Commessa</span>
				</td>
				<%
					for(String mese:mesi){
				%>		
						<td  class="intestazioni">	
							<span><%=mese %></span>
						</td>
				<%		
					}
				%>
				<td  class="intestazioni">
					<span>Totale Ore</span>
				</td>
			</tr>
			
			<%
			for(int y = 0; y < listaClienti.size(); y++){
				ClienteDTO client = (ClienteDTO) listaClienti.get(y);
				
				boolean cliente = false;
				
				for(int x = 0; x < listaAssociazioni.size(); x++){
					Associaz_Risor_Comm asscomm = (Associaz_Risor_Comm)listaAssociazioni.get(x);
					if(asscomm.getDescrizioneCliente().equals(client.getRagioneSociale())){
			%>		
				<tr>
					<td colspan="<%=mesi.size()+2 %>">
						<br>
					</td>
				<tr>
				<tr>
					<%
						if(!cliente){
							cliente = true;
					%>
							<td class="cliente" ><%=client.getRagioneSociale() %></td>
					<%
						}	
					%>
				</tr>
				<tr>	
					  <td class="Commessa"><p><%=asscomm.getDescrizioneCommessa() %></p></td>
	<%
						double totaliOre = 0;
						for(int k = 0; k < listaGiornate.size(); k++){
							PlanningDTO planning = (PlanningDTO) listaGiornate.get(k);
							if(asscomm.getId_associazione() == planning.getId_associazione()){
								
								ArrayList<PlanningDTO> listaGiorni = planning.getListaGiornate();
								
								for(int i = 0; i < listaGiorni.size(); i++){
									PlanningDTO plan = (PlanningDTO)listaGiorni.get(i);
										totaliOre += plan.getNumeroOre() + plan.getStraordinari() + plan.getFerie() + plan.getMutua() + plan.getPermessi();				
								}
							}
						}
	%>				
							<td>
								<div class="totale"><%=totaliOre %></div>
							</td>
							<td>
								<div class="totaliOre"><%=totaliOre %></div>
							</td>
					</tr>
										
	<%	
				}
			}
		}
	%>
			
		</table>
		</div>	
	<%
	}else{
	%>	
		<p align="center">Non ci sono report per il tipo di ricerca effettuata</p>
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