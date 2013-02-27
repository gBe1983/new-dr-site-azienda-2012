<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<form action="./GestioneCurriculum" method="post" name="pdf">
	<input type="hidden" name="azione" value="esportaPdf" >
	<input type="hidden" name="parametro" value="" >
	<input type="hidden" name="lastMovimento" value="" >
	<input type="hidden" name="area" value="" >
	<label>In che modalità vuoi esportare il curriculum?</label><br>
	<input type="radio" name="sceltaCurriculum" value="europeo" checked="checked" onClick="visualizzazione(this.value);"><label>Europeo</label><br>
	<input type="radio" name="sceltaCurriculum" value="aziendale" onClick="visualizzazione(this.value);"><label>Aziendale</label><br><br>
	<div id="formato">
		<label>In che formato?</label><br>
		<input type="radio" name="tipoVisualizzazione" value="completo"><label>Completo</label><br>
		<input type="radio" name="tipoVisualizzazione" value="anonimo"><label>Anonimo</label><br><br>
	</div>
	<label>Vuoi inviarlo come allegato?</label><br>
	<input type="radio" name="email" value="si" onClick="invioEmail(this.value);"><label>Si</label><br>
	<input type="radio" name="email" value="no" checked="checked" onClick="invioEmail(this.value);"><label>No</label><br><br>
	<div id="modulo">
		<table>
			<tr>
				<td><label>Destinatario: </label></td>
				<td><input type="text" name="destinatario"></td>
			</tr>
			<tr>
				<td><label>Oggetto: </label></td>
				<td><input type="text" name="oggetto"></td>
			</tr>
			<tr>
				<td><label>Corpo<br>(Max 10000 caratteri) </label></td>
				<td><textarea rows="10" cols="35" name="corpo">Inserisci Testo</textarea></td>
			</tr>
		</table>
	</div>
	<input type="submit" value="esporta" onclick="return closeWindows()">
	<button value="chiudi" onclick="return closeFinestra()">chiudi</button>
</form>