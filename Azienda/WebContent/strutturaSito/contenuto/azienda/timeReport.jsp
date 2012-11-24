<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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
%>
<div class="subtitle ">
	<h2>Visualizzazzione Consuntuvi</h2>
</div>
	<form action="./GestioneReport" method="post" name="report">
			<input type="hidden" name="azione" value="visualizzaConsuntivi">
			<table>
				<tr>
					<td>Da:</td>
					<td>
						<input type="text" name="da" class="data"/>
					</td>
					<td>a:</td>
					<td>
						<input type="text" name="a" class="data"/>
					</td>
				</tr>
				<tr>
					<td>Clienti:</td>
					<td>
						<select name="cliente">
							<option value="all">-- Tutti i Clienti --</option>
<%
	for(ClienteDTO cliente:clienti){
%><!-- AGGIUNGERE SELECTED -->
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
						<select name="commessa">
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
					<td colspan="2">
						<select name="risorsa">
							<option value="all">
								-- Tutte le Risorse --
							</option>
<%
	for(RisorsaDTO risorsa:risorse){
%><!-- AGGIUNGERE SELECTED -->
							<option value="<%=risorsa.getIdRisorsa()%>">
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


<!-- Ripristinare Controllo Sessione attiva -->