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
	TimeReport tr=(TimeReport)request.getAttribute("timeReport");
	String dtDa=(String)request.getAttribute("dtDa");
	String dtA=(String)request.getAttribute("dtA");
	ArrayList<ClienteDTO> listaClienti = (ArrayList<ClienteDTO>) request.getAttribute("listaClienti");
	ArrayList<PlanningDTO> listaGiornate = (ArrayList<PlanningDTO>) request.getAttribute("listaGiornate");
%>
<div class="subtitle">
	<h2>Visualizzazzione Consuntuvi</h2>
</div>
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
					<option value="<%=cliente.getId_cliente()%>"<%if(cliente.getId_cliente().equals(tr.getIdCliente())){%> selected="selected"<%}%>>
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
					<option value="<%=commessa.getId_commessa() %>"<%if(tr.getIdCommessa()!=null&&tr.getIdCommessa().equals(commessa.getId_commessa()+"")){%> selected="selected"<%}%>>
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
					<option value="<%=risorsa.getIdRisorsa()%>"<%if(tr.getIdRisorsa()!=null&&tr.getIdRisorsa().equals(risorsa.getIdRisorsa()+"")){%> selected="selected"<%}%>>
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
if(tr != null && listaGiornate.size() > 0){
	
	%>
	<div class="timeReport">
	<table class="timeReport">
		<tr>
			<td>
			</td>
		<%
			SimpleDateFormat sdfnd = new SimpleDateFormat("d");
			SimpleDateFormat sdfld = new SimpleDateFormat("E",Locale.ITALIAN);
			for(Day d:tr.getDays()){
		%>
				<td class="<%=d.getCssStyle()%>">
					<%=sdfnd.format(d.getDay().getTime())%>
					<br>
					<%=sdfld.format(d.getDay().getTime())%>
				</td>
		<%
			}
		%>
		</tr>
		
	<%
				
				for(int k = 0; k < listaGiornate.size(); k++){
					double totaleOrdinarie = 0;
					double totaleStraordinario = 0;
					PlanningDTO planning = (PlanningDTO) listaGiornate.get(k);
	%>
					<tr>
						<td colspan="<%=tr.getDays().size() %>">
							<br>
						</td>
					<tr>
					<tr>
						<td class="cliente" ><%=planning.getRagione_sociale() %></td>			
<%				
						ArrayList<PlanningDTO> listaGiorni = planning.getListaGiornate();
						
						for(int i = 0; i < listaGiorni.size(); i++){
							PlanningDTO plan = (PlanningDTO)listaGiorni.get(i);
							Day day = new Day(plan.getData());
							totaleOrdinarie += plan.getNumeroOre();
							totaleStraordinario += plan.getStraordinari();
%>
								<td class="<%=day.getCssStyle()%>">
									<div class="OreOrdinarie"><%=plan.getNumeroOre()%></div>
									<br>
									<div class="OreStraordinarie"><%=plan.getStraordinari()%></div>
								</td>
<%
						}
%>		
						<tr>
							<td colspan="<%=tr.getDays().size() %>" class="risorsa">
								Totale Ore: <%=totaleOrdinarie + totaleStraordinario %>
							</td>
						</tr>
					</tr>			
<%			
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