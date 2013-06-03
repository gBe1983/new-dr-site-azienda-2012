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
		
	<%
	for(int y = 0; y < listaClienti.size(); y++){
			ClienteDTO client = (ClienteDTO) listaClienti.get(y);
			
			boolean cliente = false;
			boolean stampaRiga = false;
			for(int x = 0; x < listaAssociazioni.size(); x++){
				Associaz_Risor_Comm asscomm = (Associaz_Risor_Comm)listaAssociazioni.get(x);
				if(asscomm.getDescrizioneCliente().equals(client.getRagioneSociale())){
	%>		
				<tr>
					<td colspan="<%=tr.getDays().size()+1 %>">
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
						}else{
					%>
							<td>
								<br>
							</td>		
					<%	
						}
						boolean creazioneCalendario = false;
						if(!creazioneCalendario){
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
							creazioneCalendario = true;
						}
					%>		
						
					</tr>
					<tr>	
					  <td class="Risorsa"><%=asscomm.getDescrizioneRisorsa() %></td>
	<%
						double totaleOrdinarie = 0;
						double totaleStraordinario = 0;
						double totaleAssenze = 0;
						for(int k = 0; k < listaGiornate.size(); k++){
							PlanningDTO planning = (PlanningDTO) listaGiornate.get(k);
							if(planning.getDescrizioneRisorsa().equals(asscomm.getDescrizioneRisorsa()) &&
								planning.getRagione_sociale().equals(client.getRagioneSociale()) &&
								planning.getDescrizione_commessa().equals(asscomm.getDescrizioneCommessa())){
								
								ArrayList<PlanningDTO> listaGiorni = planning.getListaGiornate();
								
								boolean spazioVuoto = false;
								
								for(int i = 0; i < listaGiorni.size(); i++){
									PlanningDTO plan = (PlanningDTO)listaGiorni.get(i);
									Day day = new Day(plan.getData());
									totaleOrdinarie += plan.getNumeroOre();
									totaleStraordinario += plan.getStraordinari();
									if(plan.getFerie() > 0.0){
										totaleAssenze += plan.getFerie();
									}else if(plan.getPermessi() > 0.0){
										totaleAssenze += plan.getPermessi();
									}else if(plan.getMutua() > 0.0){
										totaleAssenze += plan.getMutua();
									}
									for(Day data:tr.getDays()){
										
										if(data.getDay().getTime().equals(plan.getData().getTime())){
											spazioVuoto = true;
	%>
											<td class="<%=day.getCssStyle("")%>">
												<table>
													<tr>
														<td><div class="OreOrdinarie"><%=plan.getNumeroOre()%></div></td>
													</tr>
													<tr>
														<td><div class="OreStraordinarie"><%=plan.getStraordinari()%></div></td>
													</tr>
									<%			
												if(plan.getFerie() > 0.0){
									%>
													<tr>
														<td><div class="OreAssenze"><span title="Ferie"><%=plan.getFerie() %></span></div></td>
													</tr>
																	
									<%
												}else if(plan.getPermessi() > 0.0){
									%>	
													<tr>
														<td><div class="OreAssenze"><span title="Permessi"><%=plan.getPermessi() %></span></div></td>		
													</tr>			
									<%
												}else if(plan.getMutua() > 0.0){
									%>
													<tr>
														<td><div class="OreAssenze"><span title="Mutua"><%=plan.getMutua() %></span></div></td>
													</tr>			
									<%
												}else if(plan.getPermessiNonRetribuiti() > 0.0){
									%>
													<tr>	
														<td><div class="OreAssenze" ><span title="Permessi Non Retribuiti"><%=plan.getPermessiNonRetribuiti() %></span></div></td>
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
												</table>
											</td>
	<%						
											break;
										}else if(!spazioVuoto){
	%>
											<td class="<%=day.getCssStyle("riepilogo")%>">
												<table>
													<tr>
														<td><div class="OreOrdinarie"></div></td>
													</tr>
													<tr>
														<td><div class="OreStraordinarie"></div></td>
													</tr>
													<tr>
														<td><div class="OreAssenze"></div></td>
													</tr>
												</table>
											</td>
	<%
										}
									}
								}
							}
						}
	%>				
						<tr>
							<td>
								<table>
									<tr>
										<td><div class="OreOrdinarie">Ore Lavorative: </div></td>
										<td><div class="OreOrdinarie" id="totali"><%=totaleOrdinarie %></div></td>
									</tr>
									<tr>
										<td><div class="OreStraordinarie">Straordinario: </div></td>
										<td><div class="OreStraordinarie" id="totali"><%=totaleStraordinario %></div></td>
									</tr>
									<tr>
										<td><div class="OreAssenze" >Assenze: </div></td>
										<td><div class="OreAssenze" id="totali"><%=totaleAssenze %></div></td>
									</tr>
									<tr>
										<td><div class="OreOrdinarie">Totale Ore: : </div></td>
										<td><div class="OreOrdinarie"><%=totaleOrdinarie + totaleStraordinario + totaleAssenze%></div></td>
									</tr>
									
								</table>
							</td>
						</tr>
					</tr>
	<%
				}
			}
			if(!stampaRiga){
				stampaRiga = true;
	%>
				<tr>
					<td colspan="<%=tr.getDays().size()+1 %>">
						<br>
						<hr size="1"> 
					</td>
				<tr>
	<%		
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