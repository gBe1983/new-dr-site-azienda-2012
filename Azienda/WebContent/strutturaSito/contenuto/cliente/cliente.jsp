<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle">Area Cliente</div>

<div class="spazioUltra">
	<a href="index.jsp?azione=aggiungiCliente&dispositiva=cliente">
		<div class="contenuti">
			<div class="titoloContenuti">
				Aggiungi Cliente
			</div>
			<div class="corpoContenuti">
				<img src="images/add.png" class="immagini"><p>In questa sezione si aggiungono tutti i clienti con cui l'azienda ha dei rapporti lavorativi.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneCliente?azione=caricamentoNominativiCliente">
		<div class="contenuti">
			<div class="titoloContenuti">
				Ricerca Cliente
			</div>
			<div class="corpoContenuti">
				<img src="images/cerca.jpg" class="immagini"><p>In questa sezione puoi ricercare tutti i clienti con cui l'azienda ha dei rapporti lavorativi.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneCliente?azione=caricamentoNominativiClienteDisabilitati">
		<div class="contenuti">
			<div class="titoloContenuti">
				Riabilitazione Cliente
			</div>
			<div class="corpoContenuti">
				<img src="images/riabilitazione.gif" class="immagini_riabilitazione"><p>In questa sezione puoi riabilitare tutti i clienti con cui l'azienda non ha avuto più dei rapporti lavorativi.</p>
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