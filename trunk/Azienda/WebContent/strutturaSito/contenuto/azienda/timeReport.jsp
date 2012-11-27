<%@page import="it.bo.Day"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="it.bo.azienda.TimeReport"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.CommessaDTO"%>
<%@page import="it.azienda.dto.PlanningDTO"%>

<script type="text/javascript"> 
	$(function() {
		var pickerOpts = {
			dateFormat: "dd-mm-yy"
		};
		$(".data:input").datepicker(pickerOpts);
	});
</script>

<%
//if(request.getSession().getAttribute("utenteLoggato") != null){
	List<CommessaDTO>commesse=(List<CommessaDTO>)request.getAttribute("commesse");
	List<RisorsaDTO>risorse=(List<RisorsaDTO>)request.getAttribute("risorse");
	List<ClienteDTO>clienti=(List<ClienteDTO>)request.getAttribute("clienti");
	TimeReport tr=(TimeReport)request.getAttribute("timeReport");
	String dtDa=(String)request.getAttribute("dtDa");
	String dtA=(String)request.getAttribute("dtA");
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
			<td colspan="2">
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
			<td>
				<input type="submit" value="Cerca Consuntivi" onclick="return controlloDateReport()">
			</td>
		</tr>
	</table>
</form>
<br>
<%
if(tr.getRisorseKey().isEmpty()){
%>
<p align="center">Nessun Risultato per i criteri di filtro impostati</p>
<%
}else{
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
	double totaleOreOrdinarie=0;
	double totaleOreSraordinarie=0;
	for(Integer risorseKey:tr.getRisorseKey()){
		totaleOreOrdinarie=0;
		totaleOreSraordinarie=0;
		tr.resetHours();
%>
<tr>
<td class="Risorsa" colspan="<%=tr.getDays().size()+1%>">
<%=tr.getRisorse().get(risorseKey).getRisorsaDTO().getNome()%>&nbsp;<%=tr.getRisorse().get(risorseKey).getRisorsaDTO().getCognome()%>
</td>
</tr>
<%
		for(String commessaKey:tr.getRisorse().get(risorseKey).getCommesse().keySet()){
%>
<tr class="Commessa">
<td class="Commessa"><%=commessaKey%></td>
<%
			for(Day d:tr.getDays()){
%>
<td class="<%=d.getCssStyle()%>">
<%
				for(PlanningDTO p:tr.getRisorse().get(risorseKey).getCommesse().get(commessaKey)){
					if(	p.getData().get(Calendar.DAY_OF_YEAR)==d.getDay().get(Calendar.DAY_OF_YEAR)&&
							p.getData().get(Calendar.YEAR)==d.getDay().get(Calendar.YEAR)){
						totaleOreOrdinarie+=p.getNumeroOre();
						d.addOreOrdinarie(p.getNumeroOre());
						totaleOreSraordinarie+=p.getStraordinari();
						d.addOreSraordinarie(p.getStraordinari());
%>
<div class="OreOrdinarie"><%=p.getNumeroOre()%></div>
<br>
<div class="OreStraordinarie"><%=p.getStraordinari()%></div>
<%
						break;
					}
				}
%>
</td>
<%
			}
%>
</tr>
<%
		}
%>
<tr class="Totali">
<td class="Totali">
<div class="OreOrdinarie"><%=totaleOreOrdinarie%></div>
<br>
<div class="OreStraordinarie"><%=totaleOreSraordinarie%></div>
</td>
<%
		for(Day d:tr.getDays()){
%>
<td class="<%=d.getCssStyle()%>">
<div class="OreOrdinarie"><%=d.getOreOrdinarie()%></div>
<br>
<div class="OreStraordinarie"><%=d.getOreSraordinarie()%></div>
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
}
%>
<!-- Ripristinare Controllo Sessione attiva -->