<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">Area Commessa</div>

<div class="spazioUltra">
	<a href="./GestioneTrattattive?azione=aggiungiCommessa">
		<div class="contenuti">
			<div class="titoloContenuti">
				Aggiungi Commessa
			</div>
			<div class="corpoContenuti">
				<img src="images/add_documenti.gif" class="immagini"><p>In questa sezione si ha la possibilità di aggiungere tutte "Commesse" che l'azienda può avere nell'arco dell'anno solare.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneTrattattive?azione=ricercaCommessa">
		<div class="contenuti">
			<div class="titoloContenuti">
				Ricerca Commessa
			</div>
			<div class="corpoContenuti">
				<img src="images/cerca.jpg" class="immagini"><p>In questa sezione puoi ricerca tutte le "Commesse" che l'azienda possiede.</p>
			</div>
		</div>
	</a>

	<a href="./index.jsp?azione=chiudiMensilita">
		<div class="contenuti">
			<div class="titoloContenuti">
				Chiudi Mensilità
			</div>
			<div class="corpoContenuti">
				<img src="images/chiudi_documenti.gif" class="immagini"><p>In questa sezione puoi chiudere periodicamente le mensilità.</p>
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