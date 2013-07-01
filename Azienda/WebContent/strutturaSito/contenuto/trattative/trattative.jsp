<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">Area Trattative</div>

<div class="spazioUltra">
	<a href="./GestioneTrattattive?azione=aggiungiTrattative&dispositiva=trattamenti">
		<div class="contenuti">
			<div class="titoloContenuti">
				Aggiungi Trattativa
			</div>
			<div class="corpoContenuti">
				<img src="images/add_documenti.gif" class="immagini"><p>In questa sezione si ha la possibilità di aggiungere tutte "Trattative" che l'azienda può avere nell'arco dell'anno solare.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=tutte&dispositiva=trattative">
		<div class="contenuti">
			<div class="titoloContenuti">
				Visualizza Trattative
			</div>
			<div class="corpoContenuti">
				<img src="images/cerca.jpg" class="immagini"><p>In questa sezione puoi visualizzare tutte le "Trattative" che l'azienda possiede.</p>
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