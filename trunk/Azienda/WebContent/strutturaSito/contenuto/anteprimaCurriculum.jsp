<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.CurriculumDTO"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
<%@page import="it.azienda.dto.Dettaglio_Cv_DTO"%>
<%
	CurriculumDTO curriculum = (CurriculumDTO) request.getAttribute("curriculumVitae");
%>

<div class="subtitle ">Anteprima Curriculum</div>

<div id="finestra" title="Esporta Pdf">
	<%@include file="esportaPdf.jsp" %>
</div>

<%  
	if(request.getParameter("area") != null){
		if(request.getParameter("area").equals("all")){ 
%>
			<div id="toolbar" class="spazioUltra"><a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=curriculum.getId_risorsa() %>">Modifica Cv</a><a href="#" onclick="return openFinestra('<%=curriculum.getId_risorsa() %>','<%=request.getParameter("azione") %>','all','esporta')">Esporta in Pdf</a><a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=gestione&area=all" >Indietro</a></div>
<%
		}else{
%>
			<div id="toolbar" class="spazioUltra"><a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=curriculum.getId_risorsa() %>">Modifica Cv</a><a href="#" onclick="return openFinestra('<%=curriculum.getId_risorsa() %>','<%=request.getParameter("azione") %>','notAll','esporta')">Esporta in Pdf</a><a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=curriculum.getId_risorsa() %>" >Indietro</a></div>
<%
		}
	}else{
%>
			<div id="toolbar" class="spazioUltra"><a href="./GestioneCurriculum?azione=caricamentoCv&parametro0=<%=curriculum.getId_risorsa() %>">Modifica Cv</a><a href="#" onclick="return openFinestra('<%=curriculum.getId_risorsa() %>','<%=request.getParameter("azione") %>','notAll','esporta')">Esporta in Pdf</a><a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=anteprimaCv" >Indietro</a></div>
<%	
	}
%>
<div class="spazioUltra">

<%
	if(request.getParameter("tipoAnteprima").equals("europeo")){
%>

	<table id="intestazioneCurriculum" border="0" cellpadding="0" cellspacing="0">

<%	
	if(curriculum.getRisorsa() != null){
			RisorsaDTO risorsa = curriculum.getRisorsa();
%>
				<tr><td class="titoli"><b><h3>Formato Europeo <br> per il curriculum <br>vitae </h3></b></td><td class="valore"><p></p></td></tr>
				<tr><td class="titoli"><img src="images/logo.gif" id="logo" align="right"></td><td class="valore"><p></p></td></tr>
				<tr><td class="titoli"><h4>Informazioni Personali</h4></td><td class="valore"><p></p></td></tr>
				<tr><td class="descrizioni">Cognome</td><td class="valore"><%=risorsa.getCognome() %></td></tr>
				<tr><td class="descrizioni">Nome</td><td class="valore"><%=risorsa.getNome() %></td></tr>
				<tr><td class="descrizioni">Indirizzo</td><td class="valore"><%=risorsa.getIndirizzo() %></td></tr>
				<tr><td class="descrizioni">Telefono</td><td class="valore"><%=risorsa.getTelefono() %></td></tr>
				<tr><td class="descrizioni">Cellulare</td><td class="valore"><%=risorsa.getCellulare() %></td></tr>
				<tr><td class="descrizioni">Fax</td><td class="valore"><%=risorsa.getFax() %></td></tr>
				<tr><td class="descrizioni">Email</td><td class="valore"><%=risorsa.getEmail() %></td></tr>
				<tr><td class="descrizioni">Data di Nascita</td><td class="valore"><%=risorsa.getDataNascita() %></td></tr>
<%	
/*------------------------------- Anteprima Esperienze ----------------------------------------*/
	}
	if(curriculum.getListaEsperienze().size() > 0){

			boolean titoloEsperienze = false;
			for(EsperienzeDTO esperienza:curriculum.getListaEsperienze()){
				if(!titoloEsperienze){
					titoloEsperienze = true;
		%>
					<tr><td class="titoli"><h4>Esperienze Lavorative</td><td class="valore"><p></p></td></tr>
		<%
				}
		%>
					<tr><td class="descrizioni">Periodo</td><td class="valore"><%=esperienza.getPeriodo() %></td></tr>
					<tr><td class="descrizioni">Azienda</td><td class="valore"><%=esperienza.getAzienda() %></td></tr>
					<tr><td class="descrizioni">Luogo</td><td class="valore"><%=esperienza.getLuogo() %></td></tr>
					<tr><td class="descrizioni" valign="top">Descrizione</td><td class="valore"><textarea rows="15" cols="50" name="descrizione" readonly="readonly"><%=esperienza.getDescrizione() %></textarea></td></tr>
					<tr><td class="descrizioni"><p></p></td><td class="valore"><p></p></td></tr>
		<%
			}
	/*------------------------------- Anteprima Dettaglio ----------------------------------------*/
	}
	if(curriculum.getListaDettaglio() != null){
		Dettaglio_Cv_DTO dettaglio = curriculum.getListaDettaglio();
%>
			<tr><td class="titoli"><h4>Dettaglio Lavorativo</td><td class="valore"><p></p></td></tr>
			<tr><td class="titoli"><p></p></td><td class="valore"><p></p></td></tr>
			<tr><td class="titoli"><p></p></td><td class="valore"><p></p></td></tr>
			<tr><td class="descrizioni" valign="top">Capacità Professionali</td><td class="valore"><textarea rows="15" cols="50" name="capacitaProfessionali" readonly="readonly"><%=dettaglio.getCapacita_professionali() %></textarea></td></tr>
			<tr><td class="descrizioni" valign="top">Competenze Tecniche</td><td class="valore"><textarea rows="15" cols="50" name="competenzeTecniche" readonly="readonly"><%=dettaglio.getCompetenze_tecniche() %></textarea></td></tr>
			<tr><td class="descrizioni" valign="top">Lingue</td><td class="valore"><textarea rows="15" cols="50" name="lingue" readonly="readonly"><%=dettaglio.getLingue_Straniere() %></textarea></td></tr>
			<tr><td class="descrizioni" valign="top">Istruzione</td><td class="valore"><textarea rows="15" cols="50" name="istruzione" readonly="readonly"><%=dettaglio.getIstruzione() %></textarea></td></tr>
			<tr><td class="descrizioni" valign="top">Formazione</td><td class="valore"><textarea rows="15" cols="50" name="formazione" readonly="readonly"><%=dettaglio.getFormazione() %></textarea></td></tr>						
			<tr><td class="descrizioni" valign="top">Interessi</td><td class="valore"><textarea rows="15" cols="50" name="interessi" readonly="readonly"><%=dettaglio.getInteressi() %></textarea></td></tr>
<%
	}
%>
		</table>
<%
	}else{
%>
		<table width="750" align="center" border="0">
			<tr><td colspan="3"><img src="images/logo_DierreConsulting_Intestazione.gif" /></td></tr>
			<tr><td colspan="3" class="contenitoreIntestazione"><p align="center" class="intestazioneCurriculum"><b>CURRICULUM VITAE</b></p></td></tr>
		<%	
			if(request.getParameter("dispositiva").equals("completo")){
		%>				
				<tr><td class="spazioSinistro"><br></td><td class="spazioCentro"><p class="contenutiCurriculum"><b>Cognome Nome: </b><br>Figura Professionale: </p></td><td class="spazioDestro"><p class="contenutiCurriculum"><b> <%=curriculum.getRisorsa().getCognome() + " " +  curriculum.getRisorsa().getNome() %></b><br>
				<% 
				if(curriculum.getRisorsa().getFiguraProfessionale() != null){ 
					out.print(curriculum.getRisorsa().getFiguraProfessionale()); 
				}else{ 
					out.print("<br>"); 
				} 
			%>
			</p></td></tr>
			<tr><td colspan="3" class="contenitoreIntestazione"><br><span class="intestazioniCurriculum"><b>INFORMAZIONI PERSONALI<b></span></td></tr>
			<tr><td class="spazioSinistro"><br></td><td class="spazioCentro"><p class="contenutiCurriculum">Nazionalità: <br>Data Nascita: <br>Luogo Nascita: <br>Residenza: </p></td><td class="spazioDestro"><p class="contenutiCurriculum">
			<%
				if(curriculum.getRisorsa().getNazione() != null){
					out.print(curriculum.getRisorsa().getNazione() + "<br>");	
				}else{
					out.print("<br>");
				}
			
				if(curriculum.getRisorsa().getDataNascita() != null){
					out.print(curriculum.getRisorsa().getDataNascita() + "<br>");		
				}else{
					out.print("<br>");
				}
			
				if(curriculum.getRisorsa().getLuogoNascita() != null){
					out.print(curriculum.getRisorsa().getLuogoNascita() + "<br>");
				}else{
					out.print("<br>");
				}
			
				if(curriculum.getRisorsa().getIndirizzo() != null){
					out.print(curriculum.getRisorsa().getIndirizzo() + "<br>");
				}else{
					out.print("<br>");
				}
			%>
			</p></td></tr>	
		<%
			}else{
		%>
				<tr><td class="spazioSinistro"><br></td><td class="spazioCentro"><p class="contenutiCurriculum"><br>Figura Professionale: </p></td><td class="spazioDestro"><p class="contenutiCurriculum"><br><%=curriculum.getRisorsa().getFiguraProfessionale()%></p></td></tr>
		<%	
			}
			
			if(curriculum.getListaDettaglio() != null){
		
				if(curriculum.getListaDettaglio().getIstruzione() != null){
					if(curriculum.getListaDettaglio().getIstruzione().length() > 0){
		%>
						<tr><td colspan="3" class="contenitoreIntestazione"><br><p class="intestazioniCurriculum"><b>ISTRUZIONE<b></p></td></tr>
						<tr><td class="spazioSinistro"><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="7" cols="58"><%=curriculum.getListaDettaglio().getIstruzione() %></textarea></td></tr>
		<%
					}
				}
				if(curriculum.getListaDettaglio().getLingue_Straniere() != null){
					if(curriculum.getListaDettaglio().getLingue_Straniere().length() > 0){
		%>					
						<tr><td colspan="3" class="contenitoreIntestazione"><b><p class="intestazioniCurriculum">LINGUE STRANIERE</p><b></td></tr>
						<tr><td class="spazioSinistro"><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="7" cols="58"><%=curriculum.getListaDettaglio().getLingue_Straniere() %></textarea></td></tr>
		<%
						}
					}
				}
				
				if(curriculum.getListaEsperienze().size() > 0){
		%>
				<tr><td colspan="3" class="contenitoreIntestazione"><p class="intestazioniCurriculum"><b>ESPERIENZE</b></p></td></tr>
		<%
					for(int x = 0; x < curriculum.getListaEsperienze().size(); x++){
						EsperienzeDTO exp = (EsperienzeDTO) curriculum.getListaEsperienze().get(x);
		%>					
							<tr><td colspan="3"><br></td></tr>
							<tr>
								<td class="spazioSinistro"><br></td>
								<td class="context" colspan="2">
									<table width="480">
										<tr><td><p><b><%=exp.getPeriodo() %><b></p></td><td><p><b><%=exp.getAzienda()%><b></p></td><td><p align="right"><%=exp.getLuogo() %></p></td></tr>
									</table>
								</td>
							</tr>
							<tr><td colspan="3"><br></td></tr>
							<tr><td><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="30" cols="58"><%=exp.getDescrizione() %></textarea></td></tr>
		<%
						}
					}
				
				if(curriculum.getListaDettaglio() != null){
						
						if(curriculum.getListaDettaglio().getFormazione() != null){
							if(curriculum.getListaDettaglio().getFormazione().length() > 0){
		%>							
								<tr><td colspan="3" class="contenitoreIntestazione"><p class="intestazioniCurriculum"><b>FORMAZIONE</b></p></td></tr>
								<tr><td class="spazioSinistro"><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="13" cols="58"><%=curriculum.getListaDettaglio().getFormazione() %> </textarea></td></tr>
		<%
							}
						}
						
						if(curriculum.getListaDettaglio().getCompetenze_tecniche() != null){
							if(curriculum.getListaDettaglio().getCompetenze_tecniche().length() > 0){
		%>					
								<tr><td colspan="3" class="contenitoreIntestazione"><p class="intestazioniCurriculum"><b>CAPACITA E COMPETENZE TECNICHE</b></p></td></tr>
								<tr><td class="spazioSinistro"><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="13" cols="58"><%=curriculum.getListaDettaglio().getCompetenze_tecniche() %> </textarea></td></tr>
		<%					
							}
						}
						
						if(curriculum.getListaDettaglio().getCapacita_professionali() != null){
							if(curriculum.getListaDettaglio().getCapacita_professionali().length() > 0){
		%>						
								<tr><td colspan="3" class="contenitoreIntestazione"><p class="intestazioniCurriculum"><b>CAPACITA' PROFESSIONALI</b></p></td></tr>
								<tr><td class="spazioSinistro"><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="13" cols="58"><%=curriculum.getListaDettaglio().getCapacita_professionali() %></textarea></td></tr>			
		<%
							}
						}
						
						if(curriculum.getListaDettaglio().getInteressi() != null){
							if(curriculum.getListaDettaglio().getInteressi().length() > 0){
		%>					
								<tr><td colspan="3" class="contenitoreIntestazione"><p class="intestazioniCurriculum"><b>INTERESSI</b></p></td></tr>
								<tr><td class="spazioSinistro"><br></td><td class="context" colspan="2"><textarea readonly="readonly" class="contenutiCurriculum" rows="13" cols="58"><%=curriculum.getListaDettaglio().getInteressi() %></textarea></td></tr>
		<%
							}
						}
						
						if(curriculum.getRisorsa().getServizioMilitare() != null){
							if(curriculum.getRisorsa().getServizioMilitare().length() > 0){
		%>	
								<tr>
									<td colspan="3" class="contenitoreIntestazione"><p class="intestazioniCurriculum"><b>SERVIZIO MILITARE</b></p></td>
								</tr>
								<tr>
									<td><br></td>
									<td class="context" colspan="2"><br><span> <%=curriculum.getRisorsa().getServizioMilitare() %></span></td>
								</tr>
		<%
							}
						}
					}
		%>
		</table>
<%
	}
%>
	</div>
<%
	if(request.getAttribute("esitoInvioEmail") != null){
		if(Boolean.parseBoolean(request.getAttribute("esitoInvioEmail").toString())){
%>	
			<script type="text/javascript">alert("L'invio dell'email con l'allegato il Curriculum Vitae è avvenuta correttamente"); </script>
<%		
		}else{
%>
			<script type="text/javascript">alert("Impossibile inviare l'email con l'allegato il Curriculum Vitae. Contattare l'amministratore.");</script>
<%			
		}
	}
%>   