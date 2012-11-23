<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.UtenteDTO"%>

<%
HttpSession sessioneNominativi = request.getSession();
if(sessioneNominativi.getAttribute("utenteLoggato") != null){
	if(request.getAttribute("nominativi") != null){
		ArrayList listaNominativi = (ArrayList)request.getAttribute("nominativi");
		if(listaNominativi.size() > 0){
			if(request.getParameter("tipo") != null){
%>	
				<div class="subtitle ">
					<h2>Riabilitazione Cliente</h2>
				</div>
				<p>
					In questa sezione potrete riabilitare tutti i clienti che avete disabilitato.
				</p>
<%
			}else{
%>				
				<div class="subtitle ">
					<h2>Ricerca Cliente</h2>
				</div>
				<p>
					In questa sezione potrete ricercare tutti i clienti con cui avete un rapporto lavorativo.
				</p>		
				
<%				
			}
%>
	<form action="./GestioneCliente" method="post" name="ricercaCliente" class="spazio">
			<input type="hidden" name="azione" value="ricercaCliente">
			<fieldset>
			<legend>Elenco Nominativi Clienti</legend>
			<table>
				<tr>
					<td>
						<select name="nominativo">
							<option value="" selected="selected">Seleziona Cliente</option>
						<%
							for(int x = 0; x < listaNominativi.size(); x++){
						%>
								<option value="<%=listaNominativi.get(x).toString() %>"><%=listaNominativi.get(x).toString() %></option>
						<%
							}
						%>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<br><input type="submit" value="ricerca Cliente" onclick="return controlloRicercaCliente()" />
					</td>
				</tr>
			</table>
			</fieldset>
		</form>	
<%	
		}else{
			if(request.getParameter("tipo") != null){
%>			
			<div class="subtitle ">
				<h2>Disabilita Cliente</h2>
			</div>
			<p align="center" class="spazio"> Al momento non sono i clienti disabilitati.</p>
<%	
			}else{
%>
			<div class="subtitle ">
				<h2>Ricerca Cliente</h2>
			</div>
			<p align="center" class="spazio"> Al momento non sono caricati i clienti. Aggiungere dei clienti.</p>
<%
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