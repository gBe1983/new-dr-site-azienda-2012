<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<form action="./GestioneCurriculum" method="post" name="anteprima">
	<input type="hidden" name="azione" value="anteprimaGlobale" >
	<input type="hidden" name="parametro" value="" >
	<input type="hidden" name="lastMovimento" value="" >
	<input type="hidden" name="area" value="" >
	<label>In che modalità d'anteprima vuoi visualizzare il Curriculum Vitae?</label><br>
	<input type="radio" name="sceltaCurriculumAnteprima" value="europeo" checked="checked" onClick="visualizzazione(this.value);"><label>Europeo</label><br>
	<input type="radio" name="sceltaCurriculumAnteprima" value="aziendale" onClick="visualizzazione(this.value);"><label>Aziendale</label><br><br>
	<div id="formato">
		<label>In che formato?</label><br>
		<input type="radio" name="tipoVisualizzazioneAnteprima" value="completo"><label>Completo</label><br>
		<input type="radio" name="tipoVisualizzazioneAnteprima" value="anonimo"><label>Anonimo</label><br><br>
	</div>
	<input type="submit" value="anteprima" onclick="return closeWindowsAnteprima()">
	<button value="chiudi" onclick="return closeFinestraAnteprima()">chiudi</button>
</form>