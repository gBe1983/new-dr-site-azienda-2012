<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>


<div class="subtitle ">
<h2>Gestionale Azienda</h2></div>
<p>Questo prodotto è stato sviluppato per la gestione, archiviazione e consultazione dei dipendenti con i relativi curriculum.
	<br/><br/>L'applicativo consente di raccogliere in maniera rapida ed ordinata i dati dell'utente durante la sua iscrizione al portale.
	<br/><br/>Consente di risparmiare tempo e di ottimizzare le scelte dei colloquiati grazie al motore di ricerca dinamico con i campi multipli (nome, cognome, qualifica, residenza, patente, etc..)
</p>

<%
	}else{
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%		
	}
%>