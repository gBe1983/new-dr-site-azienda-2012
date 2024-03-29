<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.UtenteDTO"%>

<%
HttpSession sessioneNominativi = request.getSession();
if(sessioneNominativi.getAttribute("utenteLoggato") != null){
	
	if(request.getAttribute("nominativi") != null){
		List<String>listaNominativi=(List<String>)request.getAttribute("nominativi");
		if(listaNominativi.size() > 0){
			
			/*
			* tramite questo parametro verifico che la chiamata a questa 
			* pagina non arrivi da Ricerca Cliente piuttosto che da 
			* Riabilita Cliente
			*/
			
			if(request.getParameter("tipo") != null){
%>
				<div class="subtitle">Riabilitazione Cliente</div>

				<p>
					In questa sezione potrete riabilitare tutti i clienti che avete disabilitato.
				</p>
<%
			}else{
%>
				<div class="subtitle ">Ricerca Cliente</div>
				<p>
					In questa sezione potrete ricercare tutti i clienti con cui avete un rapporto lavorativo.
				</p>
<%
			}
%>
	    <form action="./GestioneCliente" method="post" name="ricercaCliente" class="spazio">
			<input type="hidden" name="azione" value="ricercaCliente">
			<fieldset>
			<legend align="center"><label>Elenco Nominativi Clienti</label></legend>
			<table align="center" class="spazioMin">
				<tr>
					<td>
						<select name="nominativo">
							<option value="" selected="selected">Seleziona Cliente</option>
						<%
							for(String nominativo:listaNominativi){
						%>
							<option value="<%=nominativo%>"><%=nominativo%></option>
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
				<div class="subtitle">Disabilita Cliente</div>
				<p align="center" class="spazio"> Al momento non sono i clienti disabilitati.</p>
<%	
			}else{
%>
				<div class="subtitle ">Ricerca Cliente</div>
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