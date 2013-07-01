<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<form action="./GestioneCurriculum" method="post" name="anteprima" id="anteprimaForm">
	<input type="hidden" name="azione" value="anteprimaGlobale" >
	<input type="hidden" name="parametro" value="" >
	<input type="hidden" name="lastMovimento" value="" >
	<label>In che modalità d'anteprima vuoi visualizzare il Curriculum Vitae?</label><br>
	<input type="radio" name="sceltaCurriculumAnteprima" value="europeo" checked="checked" onClick="visualizzazione(this.value);"><label>Europeo</label><br>
	<input type="radio" name="sceltaCurriculumAnteprima" value="aziendale" onClick="visualizzazione(this.value);"><label>Aziendale</label><br><br>
	<div class="formato">
		<label>In che formato?</label><br>
		<input type="radio" name="tipoVisualizzazioneAnteprima" value="completo"><label>Completo</label><br>
		<input type="radio" name="tipoVisualizzazioneAnteprima" value="anonimo"><label>Anonimo</label><br><br>
	</div>
	<button type="submit" value="anteprima" onclick="return closeWindowsAnteprima()">Anteprima</button>
	<button value="chiudi" onclick="return closeFinestraAnteprima()">Chiudi</button>
</form>