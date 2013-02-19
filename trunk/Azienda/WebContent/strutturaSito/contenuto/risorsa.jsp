<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%

	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>


<div class="subtitle">Area Risorsa</div>

<div class="spazioUltra">
	<a href="index.jsp?azione=aggiungiRisorsa&dispositiva=risorsa">
		<div class="contenuti">
			<div class="titoloContenuti">
				Aggiungi Risorsa
			</div>
			<div class="corpoContenuti">
				<img src="images/add.png" class="immagini"><p>In questa sezione si ha la possibilità di aggiungere tutte le risorse appartenti all'azienda.</p>
			</div>
		</div>
	</a>

	<a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">
		<div class="contenuti">
			<div class="titoloContenuti">
				Ricerca Risorsa
			</div>
			<div class="corpoContenuti">
				<img src="images/cerca.jpg" class="immagini"><p>In questa sezione puoi ricercare tutte le risorse che l'azienda possiede.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneRisorse?azione=listaRisorseDaAbilitare">
		<div class="contenuti">
			<div class="titoloContenuti">
				Riabilitazione Risorsa
			</div>
			<div class="corpoContenuti">
				<img src="images/riabilitazione.gif" class="immagini_riabilitazione"><p>In questa sezione puoi riabilitare tutti le risorse con cui l'azienda ha avuto dei rapporti lavorativi.</p>
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