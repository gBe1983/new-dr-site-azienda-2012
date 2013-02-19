<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.CurriculumDTO"%>    

<%
	ArrayList<CurriculumDTO> listaCurriculum = (ArrayList<CurriculumDTO>) request.getAttribute("listaCurriculum");
%>
<div class="subtitle">Visualizza Curriculum Vitae</div>

<div class="spazioUltra">
	
	<form action="./GestioneCurriculum" method="post" class="sceltaCurriculum" name="sceltaCurriculum" id="sceltaCurriculum">
		<input type="hidden" name="azione" value="">
		<input type="hidden" name="area" value="all" >
		<select name="scelta">
			<option value="">-- Scegli Azione --</option>
			<option value="aggiungi">Aggiungi Cv</option>
			<option value="modifica">Modifica Cv</option>
			<option value="anteprima">Anteprima Cv</option>
			<option value="elimina">Elimina Cv</option>
		</select>
		<input type="submit" value="esegui" onclick="return controlloSceltaVisualizzaCurriculum()">
	</form>

	<form action="" method="" id="curriculum">
		<table id="channel">
			<tr>	
				<th>Risorsa</th>
				<th>Informazioni Personali</th>
				<th>Esperienze Lavorative</th>
				<th>Dettaglio Lavorativo</th>
				<th>Scelta</th>
			</tr>	
	<%
	if(listaCurriculum.size() > 0){
		for(CurriculumDTO curriculum: listaCurriculum){
	%>
			<tr>
				<td>
					<%=curriculum.getNominativo() %>
				</td>
				<td>
					<img src="images/spunta.gif" id="dettaglio" />
				</td>
				<td>
					<%
						if(curriculum.isEsperienze()){
					%>
							<img src="images/spunta.gif" id="dettaglio"/>
					<%
						}else{
					%>
							<img src="images/elimina.gif" id="dettaglio" />
					<%	
						}
					%>
				</td>
				<td>
					<%
						if(curriculum.isDettaglio()){
					%>
							<img src="images/spunta.gif" id="dettaglio"/>
					<%
						}else{
					%>
							<img src="images/elimina.gif" id="dettaglio"/>
					<%	
						}
					%>
				</td>
				<td>
					<input type="checkbox" name="curriculum" value="<%=curriculum.getId_risorsa() %>">
				</td>
			</tr>
	<%		
		}
	}else{
	%>
			<tr>	
				<td colspan="5">
					<p>Non sono presenti nessun tipo di Curriculum Vitae</p>
				</td>
			</tr>
		
	<%
	}
	%>
	
		</table>
	</form>
</div>