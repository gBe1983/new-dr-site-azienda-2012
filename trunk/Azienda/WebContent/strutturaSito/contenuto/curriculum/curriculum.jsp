<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%

	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>


<div class="subtitle">Area Curriculum</div>

<div class="spazioUltra">
	<a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=gestione">
		<div class="contenuti">
			<div class="titoloContenuti">Gestione Curriculum Vitae</div>
			<div class="corpoContenuti">
				<img src="images/gestione.gif" class="immagini"><p>In questa sezione si ha la possibilità di gestire tutti i "Curriculum Vitae" di tutte le risorse appartenti all'azienda.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=esportaPDF">
		<div class="contenuti">
			<div class="titoloContenuti">Esporta Pdf</div>
			<div class="corpoContenuti">
				<img src="images/icon_pdf.gif" class="immagini"><p>In questa sezione esportare in formato Pdf il "Curriculum Vitae" della singola risorsa.</p>
			</div>
		</div>
	</a>

	<a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=anteprimaCv">
		<div class="contenuti">
			<div class="titoloContenuti">
				Anteprima C.V.
			</div>
			<div class="corpoContenuti">
				<img src="images/gestione_curriculum.gif" class="immagini"><p>In questa sezione puoi visualizzare l'anteprima del "Curriculum Vitae" del singolo dipendente.</p>
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