<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>

<%
	HttpSession sessioneSelezioneRisorsa = request.getSession();
	if(sessioneSelezioneRisorsa.getAttribute("utenteLoggato") != null){
		ArrayList listaRisorse = (ArrayList)request.getAttribute("listaRisorsa");
		if(listaRisorse.size() > 0){
%>
			<div class="subtitle ">Seleziona Risorsa</div>
			
			<div>
				<p>In questa sezione si seleziona la risorsa per la quale si vuole creare il Curriculum Vitae</p>
			</div>
		
			<div class="spazioUltra" align="center">
				<fieldset>
					<legend>Seleziona Risorsa</legend>
					<form action="./GestioneCurriculum" method="post" >
						<input type="hidden" name="azione" value="caricamentoCv">
						<input type="hidden" name="creazioneCv" value="1">
						<table>
							<tr>
								<td><label>Risorsa:</label></td>
								<td><select name="parametro0">
							<%
								for(int x = 0; x < listaRisorse.size(); x++){
									RisorsaDTO risorsa = (RisorsaDTO) listaRisorse.get(x);
							%>		
									<option value="<%=risorsa.getIdRisorsa()%>"><%=risorsa.getCognome() + " " + risorsa.getNome() %></option>
							<%	
								}
							%>
								</select></td>
							</tr>
							<tr>
								<td colspan="2">
									<input type="submit" value="seleziona risorsa" />
								</td>
							</tr>
						</table>
					</form>
				</fieldset>
			</div>
<%
		}else{
%>
			<div class="subtitle ">
				<h2>Seleziona Risorsa</h2>
			</div>
			
			<p align="center" class="spazio">Tutte le risorse hanno associato un Curriculum Vitae</p>
<%
		}
	}
%>