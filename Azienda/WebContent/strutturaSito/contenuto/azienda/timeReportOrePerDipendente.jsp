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

<script type="text/javascript"> 
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
	String tipologiaReport = request.getParameter("tipologiaReport");
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
		
<%
if(tr != null){
%>
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
					<option value="1">Commessa Per Cliente</option>
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

}else{
%>
		<tr>
			<td>Clienti:</td>
			<td>
				<select name="cliente" class="filter">
					<option value="all">-- Tutti i Clienti --</option>
<%
		for(ClienteDTO cliente:clienti){
%>
						<option value="<%=cliente.getId_cliente()%>" >
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
						<option value="<%=commessa.getId_commessa() %>" >
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
						<option value="<%=risorsa.getIdRisorsa()%>" >
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
						<option value=""  selected="selected">-- Seleziona la Tipologia -- </option>
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
	}
if(tr != null){
	
	%>
<div class="timeReport">
	<table class="timeReport">
	<%	
		if(tr.getRisorseKey().isEmpty()){
			%>
				<p align="center">Nessun Risultato per i criteri di filtro impostati</p>
			<%
		}else{
		
			double totaleOreOrdinarie=0;
			double totaleOreSraordinarie=0;
			double totaleOreAssenze = 0;
			for(Integer risorseKey:tr.getRisorseKey()){
				totaleOreOrdinarie=0;
				totaleOreSraordinarie=0;
				totaleOreAssenze=0;
				tr.resetHours();

				boolean creazioneCalendario = false;
				for(String commessaKey:tr.getRisorse().get(risorseKey).getCommesse().keySet()){
					
					if(!creazioneCalendario){
		%>
							<tr>
								<td class="Risorsa" >
									<br>
									<%=tr.getRisorse().get(risorseKey).getRisorsaDTO().getNome()%>&nbsp;<%=tr.getRisorse().get(risorseKey).getRisorsaDTO().getCognome()%>
								</td>
							<%
								SimpleDateFormat sdfnd = new SimpleDateFormat("d");
								SimpleDateFormat sdfld = new SimpleDateFormat("E",Locale.ITALIAN);
								for(Day d:tr.getDays()){
							%>
									<td class="<%=d.getCssStyle("")%>">
										<%=sdfnd.format(d.getDay().getTime())%>
										<br>
										<%=sdfld.format(d.getDay().getTime())%>
									</td>
							<%
								}
							%>
							</tr>
					<%
							creazioneCalendario = true;
						}
					%>
						<tr class="Commessa">
							<td class="Commessa"><b><u><%=commessaKey%></u></b></td>
		<%
			for(int z = 0; z < tr.getDays().size(); z++){
				Day d = tr.getDays().get(z);
		%>
						<td class="<%=d.getCssStyle("")%>">
							<table>
	<%
					for(int y = 0; y < tr.getRisorse().get(risorseKey).getCommesse().get(commessaKey).size(); y++){
						PlanningDTO p = tr.getRisorse().get(risorseKey).getCommesse().get(commessaKey).get(y);
						if((p.getData().get(Calendar.DAY_OF_YEAR))==(d.getDay().get(Calendar.DAY_OF_YEAR))&&
								(p.getData().get(Calendar.YEAR))==(d.getDay().get(Calendar.YEAR))){
							
							totaleOreOrdinarie+=p.getNumeroOre();
							d.addOreOrdinarie(p.getNumeroOre());
							totaleOreSraordinarie+=p.getStraordinari();
							d.addOreSraordinarie(p.getStraordinari());
							
							if(p.getFerie() > 0.0){
								totaleOreAssenze += p.getFerie();
								d.setAssenze(p.getFerie());
							}else if(p.getPermessi() > 0.0){
								totaleOreAssenze += p.getPermessi();
								d.setAssenze(p.getPermessi());
							}else if(p.getMutua() > 0.0){
								totaleOreAssenze += p.getMutua();
								d.setAssenze(p.getMutua());
							}else if(p.getPermessiNonRetribuiti() > 0.0){
								d.setAssenze(p.getPermessiNonRetribuiti());
							}
							
							%>
							<tr>
								<td><div class="OreOrdinarie"><%=p.getNumeroOre()%></div></td>
							</tr>
							<tr>
								<td><div class="OreStraordinarie"><%=p.getStraordinari()%></div></td>
							</tr>
							<%			
									if(p.getFerie() > 0.0){
							%>
										<tr>
											<td><div class="OreAssenze"><span title="Ferie"><%=p.getFerie() %></span></div></td>
										</tr>
														
							<%
									}else if(p.getPermessi() > 0.0){
							%>	
										<tr>
											<td><div class="OreAssenze"><span title="Permessi"><%=p.getPermessi() %></span></div></td>		
										</tr>			
							<%
									}else if(p.getMutua() > 0.0){
							%>
										<tr>
											<td><div class="OreAssenze"><span title="Mutua"><%=p.getMutua() %></span></div></td>
										</tr>			
							<%
									}else if(p.getPermessiNonRetribuiti() > 0.0){
							%>
										<tr>	
											<td><div class="OreAssenze" ><span title="Permessi Non Retribuiti"><%=p.getPermessiNonRetribuiti() %></span></div></td>
										</tr>			
							<%
									}else{
							%>
										<tr>
											<td><div class="OreAssenze"><%=0.0%></div></td>
										</tr>
						<%
									}
						%>
								
					<%	
							break;
						}
					}
					%>
							</table>
						</td>
		<%
					}
		%>
					</tr>
		<%
				}
		%>
				<tr>
					<td>
						<table>
							<tr>
								<td><div class="OreOrdinarie">Ordinarie: </div></td>
								<td><div class="OreOrdinarie" id="totali"><%=totaleOreOrdinarie%></div></td>
							</tr>
							<tr>
								<td><div class="OreStraordinarie">Straordinario: </div></td>
								<td><div class="OreStraordinarie" id="totali"><%=totaleOreSraordinarie%></div></td>
							</tr>
							<tr>
								<td><div class="OreAssenze" >Assenze: </div></td>
								<td><div class="OreAssenze" id="totali"><%=totaleOreAssenze %></div></td>
							</tr>
						</table>
					</td>
		<%
				for(int x = 0; x < tr.getDays().size(); x++){
					Day d = tr.getDays().get(x);
					if(d.getOreOrdinarie() == 0.0 && d.getOreSraordinarie() == 0.0 && d.getAssenze() == 0.0){
		%>
						<td class="<%=d.getCssStyle("riepilogo")%>">
							<br>
						</td>
		<%							
					}else{
		%>
						<td class="<%=d.getCssStyle("riepilogo")%>">
							<table>
								<tr>
									<td><div class="OreOrdinarie"><%=d.getOreOrdinarie()%></div></td>
								</tr>
								<tr>
									<td><div class="OreStraordinarie"><%=d.getOreSraordinarie()%></div></td>
								</tr>
								<tr>
									<td><div class="OreAssenze" ><%=d.getAssenze() %></div></td>
								</tr>
							</table>
						</td>
		<%
					}
				}
		%>
				</tr>
				<tr>
					<td colspan="<%=tr.getDays().size() %>" class="Totali">
						<table>
							<tr>
								<td>
									<div class="totale">Totale ore: </div>
								</td>
								<td>
									<div class="OreTotali"><%=totaleOreOrdinarie + totaleOreSraordinarie + totaleOreAssenze %></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="<%=tr.getDays().size()+1 %>">
						<hr size="1">
						<br>
					</td>
				</tr>
		<%
				}
		}
		%>
	</table>
	</div>
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