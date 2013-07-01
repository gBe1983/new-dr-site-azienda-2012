<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">Area Report</div>

<div class="spazioUltra">
	<a href="./GestioneReport?azione=caricamentoReport">
		<div class="contenuti">
			<div class="titoloContenuti">Ricerca Report</div>
			<div class="corpoContenuti">
				<img src="images/cerca.jpg" class="immagini"><p>In questa sezione puoi ricercare tutti i report di cui l'azienda dispone.</p>
			</div>
		</div>
	</a>
</div>
<%
	}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>	
<%
	}
%>