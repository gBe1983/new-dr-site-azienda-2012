<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
    
    
    
<%
HttpSession gestioneCurriculum = request.getSession();
if(gestioneCurriculum.getAttribute("utenteLoggato") != null){
	ArrayList curriculumVitae = (ArrayList) gestioneCurriculum.getAttribute("curriculumVitae");
	int risorsa = Integer.parseInt(request.getParameter("risorsa"));
	
%>
<div class="subtitle ">
	<h2>Dettaglio Esperienze</h2>
</div>

<div id="flusso">
	<table>
		<tr>
			<td><img src="images/home.gif"><a href="index.jsp?azione=homePage">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Cerca</a></td>
		</tr>
	</table>
</div>

<div class="creaCurriculum">
	<%
	if(request.getParameter("tipoModifica") != null){
		if(request.getParameter("tipoModifica").equals("esperienze")){
		
		int idEsperienze = Integer.parseInt(request.getParameter("idEsperienze"));
			
		for(int y = 0; y < curriculumVitae.size(); y++){
			
			if(curriculumVitae.get(y) instanceof EsperienzeDTO){
				if(((EsperienzeDTO)curriculumVitae.get(y)).getIdEsperienze() == idEsperienze ){
					EsperienzeDTO esperienze = (EsperienzeDTO) curriculumVitae.get(y);
	%>
					<div id="bluemenu" class="bluetabs">
						<ul>
							<li><a href="index.jsp?azione=modificaSingoliCampiCurriculum&tipoModifica=esperienze&idEsperienze=<%=esperienze.getIdEsperienze() %>&risorsa=<%=esperienze.getId_risorsa() %>">Modifica Esperienza</a></li>
							<li><a href="GestioneCurriculum?azione=eliminaCampiCurriculum&tipologia=esperienze&idEsperienze=<%=esperienze.getIdEsperienze() %>&risorsa=<%=esperienze.getId_risorsa() %>" onclick="return confirm('Vuoi eliminare questa esperienza?');">Elimina Esperienza</a></li>
						</ul>
					</div>		
					<fieldset>
							<legend>Esperienza Lavorative</legend>
							<table>
									<tr>
										<td>
											<label>Periodo: </label>
										</td>
										<td>
											<input type="text" name="periodo" value="<%=esperienze.getPeriodo() %>" disabled="disabled"/>
										</td>
									</tr>
									<tr>
										<td>
											<label>Azienda: </label>
										</td>
										<td>
											<input type="text" name="azienda" value="<%=esperienze.getAzienda() %>" disabled="disabled"/>
										</td>
									</tr>
									<tr>
										<td>
											<label>Luogo: </label>
										</td>
										<td>
											<input type="text" name="luogo" value="<%=esperienze.getLuogo() %>" disabled="disabled"/>
										</td>
									</tr>
									<tr>
										<td>
											<label>Descrizione: <br> (10000 caratteri) </label>
										</td>
										<td>
											<textarea rows="25" cols="70" name="descrizione" disabled="disabled"><%=esperienze.getDescrizione() %></textarea>
										</td>
									</tr>
								</table>
							
						</fieldset>
	
<%
					}
				}
			}
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

</div>