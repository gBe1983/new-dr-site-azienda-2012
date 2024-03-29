<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>



<%
	HttpSession sessioneCambioPassword = request.getSession();
	if(sessioneCambioPassword.getAttribute("utenteLoggato") != null){
		ArrayList listaRisorse = (ArrayList)request.getAttribute("listaRisorseDaAbilitare");
		if(listaRisorse.size() > 0){
%>
			
			<div class="subtitle ">Abilita Risorsa</div>
			
			<div class="spazioUltra">
			
			
			<form action="./GestioneRisorse" method="post" >
				<input type="hidden" name="azione" value="abilitaRisorsa">
				<fieldset>
				<legend align="center">Abilita Risorsa</legend>
				<br>
				<table align="center">
					<tr>
						<td><label>Risorsa:</label></td>
						<td><select name="risorsa">
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
							<br>
							<button type="submit" value="abilita risorsa" />Abilita Risorsa</button>
						</td>
					</tr>
				</table>
				</fieldset>
			</form>
			</div>
<%
		}else{
%>
			<div class="subtitle ">
				<h2>Abilita Risorsa</h2>
			</div>
			
			<p align="center" class="spazio">Non ci sono risorse disabilitate</p>
<%
		}
	}
%>