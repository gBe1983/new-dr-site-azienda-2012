<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.TrattativeDTO"%>

<%@page import="it.azienda.dto.TipologiaTrattative"%><script type="text/javascript"> 
	$(function() {
		var pickerOpts = {
			dateFormat: "dd-mm-yy"
		};
		$(".data:input").datepicker(pickerOpts);
	});
	
</script>

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){

	String newCodCommessaEsterna = (String) request.getAttribute("newCodCommessaEsterna");

	ArrayList listaTipologie = (ArrayList) request.getAttribute("listaTrattattive");
	ArrayList listaClienti = (ArrayList) request.getAttribute("listaClienti");
	ArrayList listaRisorse = (ArrayList) request.getAttribute("listaRisorse");
	if(request.getParameter("azione") != null){
		if(!request.getParameter("azione").equals("aggiornaTrattativa")){
			if(listaClienti.size() > 0 && listaRisorse.size() > 0){
%>
<div class="subtitle ">Aggiungi Trattative</div>	

<p>In questa sezione si può effettuate l'inserimento delle trattative che principalmente si suddividono in due categorie "Trattative Multiple" o "Trattative Singole". <br>
   Le "Trattative Multiple" corrispondono alle trattattive con appalto, invece le "Trattative Singole" corrispondono alle trattative della singola risorsa.
</p>

<form action="./GestioneTrattattive" method="post" name="inserisciTrattative">
		<input type="hidden" name="azione" value="inserisciTrattative" />
		<table>
			<tr>
				<td>Tipo Trattativa:</td>
				<td>
					<select name="sceltaTrattativa" onchange="sceltaTrattattiva(this.value)">
							<option value="" selected="selected">-- Seleziona la Trattativa --</option>
						<%
							
							for(int x = 0; x < listaTipologie.size(); x++){
								TipologiaTrattative tipologieTrattative = (TipologiaTrattative)listaTipologie.get(x);
						%>
										<option value="<%=tipologieTrattative.getIdTrattattive() %>" ><%=tipologieTrattative.getDescrizione() %></option>
						<%			
							}
						%>
					</select>
				</td>
			</tr>
		</table>

<!-- trattativa singola -->


<div id="trattattivaSingola" class="spazio">
	
	<p>* i campi segnati in asterisco sono obbligatori</p>
		
	<fieldset>
		<legend>Aggiungi Trattative</legend>
	<table>
		<tr>
			<td>
				<label>* Cliente: </label>
			</td>
			<td>
				<select name="trattattivaSingola_codice">
					<option value="" selected="selected">-- Seleziona il Cliente </option>
				<%
					
					for(int x = 0; x < listaClienti.size(); x++){
						ClienteDTO cliente = (ClienteDTO)listaClienti.get(x);
				%>
								<option value="<%=cliente.getId_cliente() %>" ><%=cliente.getRagioneSociale() %></option>
				<%			
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
		<td><label>* Risorsa: </label></td>
		<td><select name="trattattivaSingola_risorsa">
				<option value="" selected="selected">-- Seleziona la risorsa --</option>
				<%
							if(request.getAttribute("listaRisorse") != null){
								for(int x = 0; x < listaRisorse.size(); x++){
									RisorsaDTO risorsa = ((RisorsaDTO) listaRisorse.get(x));
				%>
									<option value="<%=risorsa.getIdRisorsa() %>"><%=risorsa.getCognome() + " " + risorsa.getNome() %></option>
				<%
								}
							}
				%>
			</select></td>
		</tr>
		<tr>
			<td>
				<label>Contatto: </label>
			</td>
			<td>
				<input type="text" name="trattattivaSingola_contatto" />
			</td>
		</tr>
		<tr>
			<td>
				<label>* Data: </label>
			</td>
			<td>
				<input type="text" name="trattattivaSingola_data" class="data"/>
			</td>
		</tr>
		<tr>
			<td>
				<label>* Oggetto: </label>
			</td>
			<td>
				<input type="text" name="trattattivaSingola_oggetto" />
			</td>
		</tr>
		<tr>
			<td>
				<label>Esito: </label>
			</td>
			<td>
				<select name="trattattivaSingola_esito" onchange="commessa('inserisci')">
					<option value="aperta" selected="selected">Aperta</option>
					<option value="persa">Persa</option>
					<option value="nuova commessa">Nuova Commessa</option>
				</select>
			</td>
		</tr>
	</table>
	</fieldset>
</div>

<!-- trattativa multipla-->

<div id="trattattivaMultipla" class="spazio">

	<p>* i campi segnati in asterisco sono obbligatori</p>

		<fieldset>
			<legend>Aggiungi Trattative</legend>
		<table>
		<tr>
			<td>
				<label>* Cliente: </label>
			</td>
			<td>
				<select name="codice">
					<option value="" selected="selected">-- Seleziona il Cliente </option>
				<%
					
					for(int x = 0; x < listaClienti.size(); x++){
						ClienteDTO cliente = (ClienteDTO)listaClienti.get(x);
				%>
						<option value="<%=cliente.getId_cliente() %>" ><%=cliente.getRagioneSociale() %></option>
				<%			
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<label>Contatto: </label>
			</td>
			<td>
				<input type="text" name="contatto" />
			</td>
		</tr>
		<tr>
			<td>
				<label>* Data: </label>
			</td>
			<td>
				<input type="text" name="data" class="data"/>
			</td>
		</tr>
		<tr>
			<td>
				<label>* Oggetto: </label>
			</td>
			<td>
				<input type="text" name="oggetto" />
			</td>
		</tr>
		<tr>
			<td>
				<label>Esito: </label>
			</td>
			<td>
				<select name="esito" onchange="commessa('inserisci')">
					<option value="aperta" selected="selected">Aperta</option>
					<option value="persa">Persa</option>
					<option value="nuova commessa">Nuova Commessa</option>
				</select>
			</td>
		</tr>
	</table>
	</fieldset>	
</div>

<!-- commessa -->

<div id="creazioneCommessa">

<fieldset><legend>Aggiungi Commessa</legend>
<table>
	<tr>
		<td>Codice Commessa</td>
		<td><input type="text" name="codiceCommessa" id="codiceCommessa" value="<%=newCodCommessaEsterna%>" readonly="readonly" /></td>
	</tr>
	<tr>
		<td>* Data</td>
		<td><input type="text" name="dataOfferta" class="data" /></td>
	</tr>
	<tr>
		<td>* Oggetto</td>
		<td><input type="text" name="oggettoOfferta" /></td>

		<td>* Descrizione</td>
		<td><input type="text" name="descrizione" />
		</td>
	</tr>
	<tr>
		<td>Sede Lavoro</td>
		<td><input type="text" name="sedeLavoro" />
		</td>
	</tr>
	<tr>
		<td>* Data Inizio</td>
		<td><input type="text" name="dataInizio" class="data" /></td>

		<td>* Data Fine</td>
		<td><input type="text" name="dataFine" class="data" /></td>
	</tr>
	<tr>
		<td>* Importo</td>
		<td><input type="text" name="importo" /></td>
	</tr>
	<tr>
		<td>Importo Lettere</td>
		<td><input type="text" name="importoLettere" /></td>
		
		
		<td>Pagamento</td>
		<td><select name="pagamento">
			<option value=""></option>
			<option value="30">30</option>
			<option value="60">60</option>
			<option value="90">90</option>
			<option value="120">120</option>
			<option value="ricevimento fattura">Ricevimento Fattura</option>
		</select></td>
	</tr>
	<tr>
		<td>Ore</td>
		<td><input type="text" name="ore" /></td>
		
		<td>Al</td>
		<td>
			<select name="al">
				<option value=""></option>
				<option value="ore">ore</option>
				<option value="giorno">giorno</option>
			</select>
		</td>
	</tr>
	<tr>
		<td>Note<br>
		(1000 caratteri)</td>
		<td><textarea name="note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','singola')" onfocus="cancellaCampoTextArea('inserisciCommessa','singola')">Inserisci la descrizione</textarea></td>
	</tr>
	<tr>
		<td>Stato</td>
		<td><select name="stato">
			<option value="aperta">Aperta</option>
		</select></td>
	</tr>
</table>
</fieldset>

</div>
<table id="bottoni">
	<tr>
		<td>
			<input type="submit" value="inserisci trattativa" id="inserisciTrattativa" onclick="return controlloInserimentoTrattativa()"/>
		</td>
		<td>
			<input type="reset" value="svuota campi" />
		</td>
	</tr>
</table>


</form>
<%
			}else{
%>
				<p align="center" class="spazio"> Non ci sono clienti e risorse caricate <br><br>
					<a href="./index.jsp?azione=homePage">Home</a>
				</p>
<%
			}
		}else{
			TrattativeDTO trattativa = (TrattativeDTO) request.getAttribute("trattativa");
%>

<div class="subtitle ">Modifica Trattative</div>

<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<td><a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=tutte&dispositiva=trattative"">Cerca</a></td>
		</tr>
	</table>
</div>

<form action="./GestioneTrattattive" method="post" name="modificaTrattativa">
	<input type="hidden" name="azione" value="modificaTrattativa" />
	<input type="hidden" name="trattativa" value="<%=trattativa.getIdTrattative() %>" />
	<input type="hidden" name="sceltaTrattativa" value="<%=trattativa.getId_tipologiaTrattative() %>" />

<%
	if(trattativa.getId_tipologiaTrattative().equals("1")){
%>
	<div class="modificaTrattative">
	
	<p>* i campi segnati in asterisco sono obbligatori</p>
	
	<fieldset>
		<legend>Modifica Trattative</legend>
			<table>
				<tr>
					<td>
						<label>* Cliente: </label>
					</td>
					<td>
						<select name="trattattivaSingola_codice">
							<option value="<%=trattativa.getId_cliente() %>"><%=trattativa.getDescrizioneCliente() %></option>
						</select>
					</td>
				</tr>
				<tr>
					<td><label>* Risorsa: </label></td>
					<td>
						<select name="trattattivaSingola_risorsa">
							<option value="<%=trattativa.getId_risorsa() %>"><%=trattativa.getDescrizioneRisorsa() %></option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<label>Contatto: </label>
					</td>
					<td>
						<input type="text" name="trattattivaSingola_contatto" value="<%=trattativa.getContatto() %>" />
					</td>
				</tr>
				<tr>
					<td>
						<label>* Data: </label>
					</td>
					<td>
						<input type="text" name="trattattivaSingola_data" value="<%=trattativa.getData() %>" readonly="readonly"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>* Oggetto: </label>
					</td>
					<td>
						<input type="text" name="trattattivaSingola_oggetto" value="<%=trattativa.getOggetto() %>"/>
					</td>
				</tr>
				<tr>
					<td>
						<label>Esito: </label>
					</td>
					<td>
						<select name="trattattivaSingola_esito" onchange="commessa('modifica')">
							<option value="aperta" selected="selected">Aperta</option>
							<option value="persa">Persa</option>
							<option value="nuova commessa">Nuova Commessa</option>
						</select>
					</td>
				</tr>
			</table>
	</fieldset>
</div>
<%
	}else{
%>

<!-- trattativa multipla-->



<div class="modificaTrattative">
		<p>* i campi segnati in asterisco sono obbligatori</p>
		
		<fieldset>
			<legend>Modifica Trattative</legend>
		<table>
		<tr>
			<td>
				<label>* Cliente: </label>
			</td>
			<td>
				<select name="codice">
					<option value="<%=trattativa.getId_cliente() %>" ><%=trattativa.getDescrizioneCliente() %></option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<label>Contatto: </label>
			</td>
			<td>
				<input type="text" name="contatto" value="<%=trattativa.getContatto() %>" />
			</td>
		</tr>
		<tr>
			<td>
				<label>* Data: </label>
			</td>
			<td>
				<input type="text" name="data" value="<%=trattativa.getData() %>" readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td>
				<label>* Oggetto: </label>
			</td>
			<td>
				<input type="text" name="oggetto" value="<%=trattativa.getOggetto() %>" />
			</td>
		</tr>
		<tr>
			<td>
				<label>Esito: </label>
			</td>
			<td>
				<select name="esito" onchange="commessa('modifica')">
					<option value="aperta" selected="selected">Aperta</option>
					<option value="persa">Persa</option>
					<option value="nuova commessa">Nuova Commessa</option>
				</select>
			</td>
		</tr>
	</table>
	</fieldset>	
</div>
<!-- commessa -->

<%
}
%>

<div id="creazioneCommessa">

	<fieldset>
		<legend>Aggiungi Commessa</legend>
			<table>
				<tr>
					<td>* Codice Commessa</td>
					<td><input type="text" name="codiceCommessa" id="codiceCommessa" onblur="controlloCodiceCommessa(this.value)" value="<%=newCodCommessaEsterna%>" readonly="readonly" /></td>
				</tr>
				<tr>
					<td>* Data</td>
					<td><input type="text" name="dataOfferta" class="data" /></td>
				</tr>
				<tr>
					<td>* Oggetto</td>
					<td><input type="text" name="oggettoOfferta" /></td>
			
					<td>* Descrizione</td>
					<td><input type="text" name="descrizione" />
					</td>
				</tr>
				<tr>
					<td>Sede Lavoro</td>
					<td><input type="text" name="sedeLavoro" />
					</td>
				</tr>
				<tr>
					<td>* Data Inizio</td>
					<td><input type="text" name="dataInizio" class="data" /></td>
			
					<td>* Data Fine</td>
					<td><input type="text" name="dataFine" class="data" /></td>
				</tr>
				<tr>
					<td>* Importo</td>
					<td><input type="text" name="importo" /></td>
				</tr>
				<tr>
					<td>Importo Lettere</td>
					<td><input type="text" name="importoLettere" /></td>
			
					<td>Pagamento</td>
					<td><select name="pagamento">
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
					</select></td>
				</tr>
				<tr>
					<td>Ore</td>
					<td><input type="text" name="ore" /></td>
					
					<td>Al</td>
					<td>
						<select name="al">
							<option value=""></option>
							<option value="ore">ore</option>
							<option value="giorno">giorno</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Note<br>
					(1000 caratteri)</td>
					<td><textarea name="note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','singola')" onfocus="cancellaCampoTextArea('inserisciCommessa','singola')">Inserisci la descrizione</textarea></td>
				</tr>
				<tr>
					<td>Stato</td>
					<td><select name="stato">
						<option value="aperta">Aperta</option>
					</select></td>
				</tr>
		</table>
	</fieldset>
</div>
<table>
	<tr>
		<td>
			<input type="submit" value="modifica trattativa" id="inserisciTrattativa" onclick="return controlloModificaTrattativa('<%=trattativa.getId_tipologiaTrattative() %>')"/>
		</td>
		<td>
			<input type="reset" value="svuota campi" />
		</td>
	</tr>
</table>
</form>
<%		
		}
	}
}else{
%>	
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%	
}
%>