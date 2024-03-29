<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.CommessaDTO"%>
<%@page import="it.azienda.dto.TipologiaCommessa"%>
<%@page import="java.text.SimpleDateFormat"%>

<script type="text/javascript"> 
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
	String newCodCommessaInterna = (String) request.getAttribute("newCodCommessaInterna");
	CommessaDTO commessa = null;
	
	if(request.getAttribute("commessa") != null){
		commessa = (CommessaDTO) request.getAttribute("commessa");
	}
	
	ArrayList listaClienti = (ArrayList) request.getAttribute("listaClienti");
	ArrayList listaRisorse = (ArrayList) request.getAttribute("listaRisorse");
	ArrayList tipologie = (ArrayList) request.getAttribute("tipologiaCommessa");
	CommessaDTO altreCommesse = (CommessaDTO) request.getAttribute("listaCommesseTipologiaAltro");
	
	if(commessa == null){
		if(listaClienti.size() != 0 && listaRisorse.size() != 0){
%>
<div class="subtitle">Aggiungi Commessa</div>

<p>In questa sezione si pu� effettuate l'inserimento delle commesse che principalmente si suddividono in quattro categorie "Commessa Esterna Singola", "Commessa Esterna Multipla", "Commessa Interna" e "Altro". <br>
   Nelle "Commesse Esterne Singole" abbiamo un associazione uno a uno cio� dato un Cliente abbiamo una risorsa associata, nelle "Commesse Esterna Multipla" troviamo un associazione uno a molti nel senso che dato un cliente ci posso essere pi� risorse legate a quel cliente,
   poi abbiamo "Commessa Interna" dove non troviamo alcuna associazione a un cliente ma possiamo avere piu risorse associate a quella commessa. Infine abbiamo la tipologia "Altro" che serve per la gestione burocratica dell'azienda. Esempio "Ferie" "Permessi" ecc... e possiamo avere pi� risorse legate ad essa.
</p>

<form action="./GestioneCommessa" method="post" name="commessa">
<input type="hidden" name="azione" value="inserisciCommessa" /> 

<table>
	<tr>
		<td><label>Tipologia Commessa:</label></td>
		<td>
			<select name="tipologiaCommessa" id="sceltaTipologia" onchange="tipologia(this.value)">
				<option value="0" selected="selected">-- Scegliere il tipo di
				commessa --</option>
				<%
					for(int x = 0; x < tipologie.size(); x++){
						TipologiaCommessa tipologia = (TipologiaCommessa) tipologie.get(x);
				%>
						<option value="<%=tipologia.getId_tipologia()%>"><%=tipologia.getDescrizione() %></option>
				<%
					}
				%>
			</select>
		</td>
	</tr>
</table>

<!-- questa tipologia serve per le commesse singolo dove abbiamo un cliente e una risorsa associata ad essa -->



<div id="commessaEsternaSingola">

<p>* i campi segnati in asterisco sono obbligatori</p>

<fieldset><legend align="center">Aggiungi Commessa Esterna Singola</legend>
<table>
	<tr>
		<td><label>* Cliente: </label></td>
		<td><select name="commessaEsternaSingola_codice">
			<option value="" selected="selected">-- Seleziona il Cliente
			</option>
			<%
				
						for(int x = 0; x < listaClienti.size(); x++){
							ClienteDTO cliente = (ClienteDTO)listaClienti.get(x);
					%>
			<option value="<%=cliente.getId_cliente() %>"><%=cliente.getRagioneSociale() %></option>
			<%			
						}
					%>
		</select></td>
	</tr>
	<tr>
		<td><label>* Risorsa: </label></td>
		<td><select name="commessaEsternaSingola_idRisorsa">
			<option value="" selected="selected">-- Seleziona la risorsa
			--</option>
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
		<td><label>Codice</label></td>
		<td><input type="text" name="commessaEsternaSingola_codiceCommessa" id="codiceCommessa" value="<%=newCodCommessaEsterna%>" readonly="readonly" /></td>
	</tr>
	<tr>
		<td><label>* Data</label></td>
		<td><input type="text" name="commessaEsternaSingola_dataOfferta"
			class="data" /></td>
	</tr>
	<tr>
		<td><label>* Oggetto</label></td>
		<td><input type="text" name="commessaEsternaSingola_oggettoOfferta" /></td>

		<td><label>* Descrizione</label></td>
		<td><input type="text" name="commessaEsternaSingola_descrizione" />
		</td>
	</tr>
	<tr>
		<td><label>Sede Lavoro</label></td>
		<td><input type="text" name="commessaEsternaSingola_sedeLavoro" />
		</td>
	</tr>
	<tr>
		<td><label>* Data Inizio</label></td>
		<td><input type="text" name="commessaEsternaSingola_dataInizio"
			class="data" /></td>

		<td><label>* Data Fine</label></td>
		<td><input type="text" name="commessaEsternaSingola_dataFine"
			class="data" /></td>
	</tr>
	<tr>
		<td><label>* Importo</label></td>
		<td><input type="text" name="commessaEsternaSingola_importo" /></td>
	</tr>
	<tr>
		<td><label>Importo Lettere</label></td>
		<td><input type="text" name="commessaEsternaSingola_importoLettere" /></td>


		<td><label>Pagamento</label></td>
		<td><select name="commessaEsternaSingola_pagamento">
			<option value=""></option>
			<option value="30">30</option>
			<option value="60">60</option>
			<option value="90">90</option>
			<option value="120">120</option>
			<option value="ricevimento fattura">Ricevimento Fattura</option>
		</select></td>
	</tr>
	<tr>
		<td><label>Ore</label></td>
		<td><input type="text" name="commessaEsternaSingola_ore" /></td>
		
		<td><label>Al</label></td>
		<td>
			<select name="commessaEsternaSingola_al">
				<option value=""></option>
				<option value="ore">ore</option>
				<option value="giorno">giorno</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><label>Note<br>
		(1000 caratteri)</label></td>
		<td><textarea name="commessaEsternaSingola_note" cols="30"
			rows="10"
			onclick="cancellaCampoTextArea('inserisciCommessa','singola')"
			onfocus="cancellaCampoTextArea('inserisciCommessa','singola')">Inserisci la descrizione</textarea></td>
	</tr>
	<tr>
		<td><label>Stato</label></td>
		<td><select name="commessaEsternaSingola_stato">
			<option value="aperta">Aperta</option>
		</select></td>
	</tr>
</table>
</fieldset>
<table>
	<tr>
		<td>
			<button type="submit" value="inserisci commessa" id="inserisciTrattativa" onclick="return controlloInserisciCommessa('1')" >Inserisci Commessa</button>
		</td>
		<td>
			<button type="reset" value="svuota campi" >Svuota Campi</button>
		</td>
	</tr>
</table>
</div>

<!-- questa tipologia serve per le commesse che hanno pi� di una risorsa associata -->

<div id="commessaEsternaMultipla">

<p>* i campi segnati in asterisco sono obbligatori</p>

<fieldset><legend>Aggiungi Commessa Esterna Multipla</legend>
<table>
	<tr>
		<td><label>* Cliente: </label></td>
		<td><select name="codice">
			<option value="" selected="selected">-- Seleziona il Cliente
			</option>
			<%
				for(int x = 0; x < listaClienti.size(); x++){
					ClienteDTO cliente = (ClienteDTO)listaClienti.get(x);
			%>
			<option value="<%=cliente.getId_cliente() %>"><%=cliente.getRagioneSociale() %></option>
			<%			
				}
			%>
		</select></td>
	</tr>
	<tr>
		<td><label>Codice</label></td>
		<td><input type="text" name="codiceCommessa" id="codiceCommessa" value="<%=newCodCommessaEsterna%>" readonly="readonly" /></td>
	</tr>
	<tr>
		<td><label>* Data</label></td>
		<td><input type="text" name="dataOfferta" class="data" /></td>
	</tr>
	<tr>
		<td><label>* Oggetto</label></td>
		<td><input type="text" name="oggettoOfferta" /></td>

		<td><label>* Descrizione</label></td>
		<td><input type="text" name="descrizione" /></td>
	</tr>
	<tr>
		<td><label>Sede Lavoro</label></td>
		<td><input type="text" name="sedeLavoro" /></td>
	</tr>
	<tr>
		<td><label>* Data Inizio</label></td>
		<td><input type="text" name="dataInizio" class="data" /></td>

		<td><label>* Data Fine</label></td>
		<td><input type="text" name="dataFine" class="data" /></td>
	</tr>
	<tr>
		<td><label>* Importo</label></td>
		<td><input type="text" name="importo" /></td>
	</tr>
	<tr>
		<td><label>Importo Lettere</label></td>
		<td><input type="text" name="importoLettere" /></td>

		<td><label>Pagamento</label></td>
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
		<td><label>Ore</label></td>
		<td><input type="text" name="ore" /></td>
		
		<td><label>Al</label></td>
		<td>
			<select name="al">
				<option value=""></option>
				<option value="ore">ore</option>
				<option value="giorno">giorno</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><label>Note<br>
		(1000 caratteri)</label></td>
		<td><textarea name="note" cols="30" rows="10"
			onclick="cancellaCampoTextArea('inserisciCommessa','multiple')"
			onfocus="cancellaCampoTextArea('inserisciCommessa','multiple')">Inserisci la descrizione</textarea></td>
	</tr>
	<tr>
		<td><label>Stato</label></td>
		<td><select name="stato">
			<option value="aperta">Aperta</option>
		</select></td>
	</tr>
</table>
</fieldset>
<table>
	<tr>
		<td><button type="submit" value="inserisci commessa" id="inserisciTrattativa" onclick="return controlloInserisciCommessa('2')" >Inserisci commessa</button></td>
		<td><button type="reset" value="svuota campi" >Svuota Campi</button></td>
	</tr>
</table>
</div>


<!-- questa tipologia serve per le commesse interne dove vengono associate le risorsa ad essa -->

<div id="commessaInterna">

<p>* i campi segnati in asterisco sono obbligatori</p>

<fieldset>
<legend>Aggiungi Commessa Interna</legend>
<table>
	<tr>
		<td><label>Codice</label></td>
		<td><input type="text" name="commessaInterna_codiceCommessa" id="codiceCommessa" value="<%=newCodCommessaInterna%>" readonly="readonly" /></td>
	</tr>
	<tr>
		<td><label>* Data</label></td>
		<td><input type="text" name="commessaInterna_dataOfferta"
			class="data" /></td>
	</tr>
	<tr>
		<td><label>* Oggetto</label></td>
		<td><input type="text"name="commessaInterna_oggettoOfferta" /></td>

		<td><label>* Descrizione</label></td>
			<td><input type="text" name="commessaInterna_descrizione" />
		</td>
	</tr>
	<tr>
		<td><label>Sede Lavoro</label></td>
		<td><input type="text" name="commessaInterna_sedeLavoro" />
		</td>
	</tr>
	<tr>
		<td><label>* Data Inizio</label></td>
		<td><input type="text" name="commessaInterna_dataInizio"
			class="data" /></td>

		<td><label>* Data Fine</label></td>
		<td><input type="text" name="commessaInterna_dataFine"
			class="data" /></td>
	</tr>
	<tr>
		<td><label>* Importo</label></td>
		<td><input type="text" name="commessaInterna_importo" /></td>
	</tr>
	<tr>
		<td><label>Importo Lettere</label></td>
		<td><input type="text" name="commessaInterna_importoLettere" /></td>

		<td><label>Pagamento</label></td>
		<td><select name="commessaInterna_pagamento">
			<option value=""></option>
			<option value="30">30</option>
			<option value="60">60</option>
			<option value="90">90</option>
			<option value="120">120</option>
			<option value="ricevimento fattura">Ricevimento Fattura</option>
		</select></td>
	</tr>
	<tr>
		<td><label>Ore</label></td>
		<td><input type="text" name="commessaInterna_ore" /></td>
		
		<td><label>Al</label></td>
		<td>
			<select name="commessaInterna_al">
				<option value=""></option>
				<option value="ore">ore</option>
				<option value="giorno">giorno</option>
			</select>
		</td>
	</tr>
	<tr>
		<td><label>Note<br>
		(1000 caratteri)</label></td>
		<td><textarea name="commessaInterna_note" cols="30"
			rows="10"
			onclick="cancellaCampoTextArea('inserisciCommessa','interna')"
			onfocus="cancellaCampoTextArea('inserisciCommessa','interna')">Inserisci la descrizione</textarea></td>
	</tr>
	<tr>
		<td><label>Stato</label></td>
		<td><select name="commessaInterna_stato">
			<option value="aperta">Aperta</option>
		</select></td>
	</tr>
</table>
</fieldset>
<table>
	<tr>
		<td><button type="submit" value="inserisci commessa" id="inserisciTrattativa" onclick="return controlloInserisciCommessa('3')" >Inserisci Commessa</button></td>
		<td><button type="reset" value="svuota campi" />Svuota Campi</button></td>
	</tr>
</table>
</div>

<!-- questa tipologia serve per le commesse interne che non hanno ne inizio e ne scadenza es. (ferie,permessi malattia)-->

<div id="altro">
	<%
	if(altreCommesse != null){
%>		
		<p align="center">Commessa che gestisce le Assenze � gi� presente. Selezionare "Modifica Commessa" per visualizzare le voci selezionate. </p>
<%
	}else{
%>
	<p>* i campi segnati in asterisco sono obbligatori</p>
	
	<fieldset>
		<legend>Aggiungi Commessa Generica</legend>
		<table>
			<tr>
				<td><label>Codice</label></td>
				<td><input type="text" name="altro_codiceCommessa" id="codiceCommessa" value="<%=newCodCommessaInterna%>" readonly="readonly"/></td>
			</tr>
			<tr>
			<td><label>* Descrizione</label></td>
				<td><input type="text" name="altro_descrizione" value="Assenze"/>
			</td>
			</tr>
			<tr>
				<td><label>* Tipologia Commessa:</label></td>
				<td>
					<input type="checkbox" name="ferie" value="true" />Ferie
					<input type="checkbox" name="permessi" value="true" />Permessi
					<input type="checkbox" name="mutua" value="true" />Mutua<br><br>			
					<input type="checkbox" name="ferieNonRetribuite" value="true" />Ferie Non Retribuite
					<input type="checkbox" name="permessiNonRetribuiti" value="true" />Permessi Non Retribuiti
					<input type="checkbox" name="mutuaNonRetribuiti" value="true" />Mutua Non Retribuiti
				</td>
			</tr>
			<tr>
				<td><label>Note<br>(1000 caratteri)</label></td>
				<td><textarea name="altro_note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','altro')" onfocus="cancellaCampoTextArea('inserisciCommessa','altro')">Inserisci la descrizione</textarea></td>
			</tr>
		</table>
	</fieldset>
	<table>
		<tr>
			<td>
				<button type="submit" value="inserisci commessa" id="inserisciTrattativa" onclick="return controlloInserisciCommessa('4')" >Inserisci Commessa</button>
			</td>
			<td>
				<button type="reset" value="svuota campi" />Svuota Campi</button>
			</td>
		</tr>
	</table>

<%
}
%>
	</div>
</form>
<%
		
	}else{
%>
			<p align="center" class="spazio">Non ci sono cliente e risorse caricate. Effettuare l'inserimento di queste sezioni.<br><br>
			<a href="./index.jsp?azione=homePage">Home</a></p>
<%
		}
	/*
	*	qua inizia la sezione Dettaglio Commessa
	*/
	}if(request.getParameter("azione").equals("dettaglioCommessa")){
%>
		<div class="subtitle ">Dettaglio Commessa</div>


		<div id="flusso">
			<table>
				<tr>
					<td><a href="index.jsp?azione=homePage">Home</a></td>
					<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
				</tr>
			</table>
		</div>
		
		<div id="bluemenu" class="bluetabs">
			<%
			/*
			* effettuo un controllo sullo stato perch� tra le commesse esiste la modalit� "Altro"
			* che non ha stato ma pu� essere modificata e/o effettuare l'associazione delle risorse.
			*/
			
			
			if(commessa.getStato() != null){
				if(commessa.getStato().equals("aperta") && !commessa.getTipologia().equals("1")){
			%>
					<ul>
						<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Modifica Commessa</a></li>
						<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
						<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
						<li><a href="./GestioneCommessa?azione=risorseDaAssociare&codice=<%=commessa.getId_cliente() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
						<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
					</ul>
	
			<%
				}else if(commessa.getStato().equals("aperta") && commessa.getTipologia().equals("1")){
			%>
					<ul>
						<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Modifica Commessa</a></li>
						<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
						<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
						<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
					</ul>
	
			<%
				}else{
			%>
					<ul>
						<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Dettaglio Commessa</a></li>
						<li><a href="./GestioneCommessa?azione=risorseAssociate&stato=<%=commessa.getStato() %>&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
						<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
					</ul>
			<%
				}
			}else{
			%>	
				<ul>
					<li><a href="./GestioneCommessa?azione=aggiornaCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Modifica Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=risorseDaAssociare&tipologia=<%=commessa.getTipologia() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
				</ul>
			<%	
			}
			%>
		</div>
<%
	if(!commessa.getTipologia().equals("4")){
%>
	<fieldset>
		<legend align="center">Dettaglio Commessa Esterna Singola</legend>
		<table class="dettaglioCommessa">
			<tr>
				<td><label>Cliente: </label></td>
				<td>
				<%
					if(commessa.getDescrizioneCliente() != null){
						out.print("<label>" + commessa.getDescrizioneCliente() + "</label>");
					}
				%>
				</td>
			</tr>
			<%
				if(commessa.getDescrizioneRisorsa() != null){
			%>
			<tr>
				<td><label>Risorsa: </label></td>
				<td>
				<%out.print("<label>" + commessa.getDescrizioneRisorsa() + "</label>");%>
				</td>
			</tr>
			<%
				}
			%>
			<tr>
				<td><label>Codice</label></td>
				<td>
					<%
						if(commessa.getCodiceCommessa() != null){
							out.print("<label>" + commessa.getCodiceCommessa() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Data Offerta</label></td>
				<td>
					<%
						if(commessa.getData_offerta() != null){
							out.print("<label>" + commessa.getData_offerta() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Oggetto</label></td>
				<td>
					<%
						if(commessa.getOggetto_offerta() != null){
							out.print("<label>" + commessa.getOggetto_offerta() + "</label>");
						}
					%>
				</td>
		
				<td><label>Descrizione</label></td>
				<td>
					<%
						if(commessa.getDescrizione() != null){
							out.print("<label>" + commessa.getDescrizione() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Sede Lavoro</label></td>
				<td>
					<%
						if(commessa.getSede_lavoro() != null){
							out.print("<label>" + commessa.getSede_lavoro() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Data Inizio</label></td>
				<td>
					<%
						if(commessa.getData_inizio() != null){
							out.print("<label>" + commessa.getData_inizio() + "</label>");
						}
					%>
				</td>
		
				<td><label>Data Fine</label></td>
				<td>
					<%
						if(commessa.getData_fine() != null){
							out.print("<label>" + commessa.getData_fine() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Importo</label></td>
				<td>
					<%
						if(commessa.getImporto() != 0.0){
							out.print("<label>" + commessa.getImporto() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Importo Lettere</label></td>
				<td>
					<%
						if(commessa.getImporto_lettere() != null){
							out.print("<label>" + commessa.getImporto_lettere() + "</label>");
						}
					%>
				</td>
				
				<td><label>Pagamento</label></td>
				<td>
					<%
						if(commessa.getPagamento() != null){
							if(commessa.getPagamento().equals("rf")){
								out.print("<label> Ricevimento Fattura </label>");
							}else{
								out.print("<label>" + commessa.getPagamento() + "</label>");
							}
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Ore</label></td>
				<td>
					<%
						if(commessa.getTotaleOre() != 0){
							out.print("<label>" + commessa.getTotaleOre() + "</label>");
						}
					%>
				</td>
				
				<td><label>Al</label></td>
				<td>
					<%
						if(commessa.getAl() != null){
							out.print("<label>" + commessa.getAl() + "</label>");
						}
					%>
				</td>
			</tr>
			<tr>
				<td><label>Note</label></td>
				<td>
					<label>
						<%
							if(commessa.getNote() != null){
								out.print(commessa.getNote());
							}
						%>
					</label>
				</td>
			</tr>
			<tr>
				<td><label>Stato</label></td>
				<td>
					<%
						if(commessa.getStato() != null){
							out.print("<label>" + commessa.getStato() + "</label>");
						}
					%>
				</td>
			</tr>
		</table>
		</fieldset>
<%
		}else{
%>
			<fieldset>
				<legend>Dettaglio Commessa</legend>
				<table class="dettaglioCommessa">
					<tr>
						<td>Codice</td>
						<td><label><%=commessa.getCodiceCommessa() %></label></td>
					</tr>
					<tr>
						<td>Descrizione</td>
						<td><label><%=commessa.getDescrizione() %></label></td>
					</tr>
					<tr>
						<td>Data Inizio</td>
						<td>
							<%
								if(commessa.getData_inizio() != null){
									out.print("<label>" + commessa.getData_inizio() + "</label>");
								}
							%>
						</td>
						<td>Data Fine</td>
						<td>
							<%
								if(commessa.getData_fine() != null){
									out.print("<label>" + commessa.getData_fine() + "</label>");
								}
							%>
							<br><br>
						</td>
					</tr>
					<tr>
						<td>Tipologia Commessa:</td>
						<td colspan="3">
							<% 
								if(commessa.isFlag_ferie()){
							%>
									<input type="checkbox" name="ferie" value="ferie" checked="checked" disabled="disabled">Ferie
							<%
								}else{
							%>
									<input type="checkbox" name="ferie" value="ferie" disabled="disabled">Ferie
							<%		
								}
								if(commessa.isFlag_mutua()){
							%>
									<input type="checkbox" name="ferie" value="ferie" checked="checked" disabled="disabled">Mutua
							<%
								}else{
							%>
									<input type="checkbox" name="ferie" value="ferie" disabled="disabled">Mutua
							<%		
								}
								if(commessa.isFlag_permessi()){
							%>
									<input type="checkbox" name="ferie" value="ferie" checked="checked" disabled="disabled">Permessi
							<%
								}else{
							%>
									<input type="checkbox" name="ferie" value="ferie" disabled="disabled">Permessi
							<%		
								}
								if(commessa.isFlag_permessiNonRetribuite()){
							%>
									<input type="checkbox" name="ferie" value="ferie" checked="checked" disabled="disabled">Permessi Non Retribuiti<br><br>
							<%
								}else{
							%>
									<input type="checkbox" name="ferie" value="ferie" disabled="disabled">Permessi Non Retribuiti<br><br>
							<%		
								}
							%>
						</td>
					</tr>
					<tr>
						<td>Note</td>
						<td>
							<label>
							<%
								if(commessa.getNote() != null){
									out.print(commessa.getNote().trim());
								}
							%>
							</label>
						</td>
					</tr>
				</table>
			</fieldset>
<%
		}

		/*
		*	qua incomincia la sezione Aggiorna Commessa
		*/

	}else if(request.getParameter("azione").equals("aggiornaCommessa")){
%>		

	<div class="subtitle ">Modifica Commessa</div>

	<div id="flusso">
		<table>
			<tr>
				<td><a href="index.jsp?azione=homePage">Home</a></td>
				<td><a href="./GestioneTrattattive?azione=ricercaCommessa">Cerca</a></td>
			</tr>
		</table>
	</div>

	<div id="bluemenu" class="bluetabs" class="spazio">
		<%
		if(commessa.getStato() != null){
			if(commessa.getStato().equals("aperta") && !commessa.getTipologia().equals("1")){
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Dettaglio Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="return confirm('Vuoi chiudere questa commessa?')">Chiudi Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=risorseDaAssociare&codice=<%=commessa.getId_cliente() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>

		<%
			}else if(commessa.getStato().equals("aperta") && commessa.getTipologia().equals("1")){
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Dettaglio Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=chiudiCommessa&parametro=<%=commessa.getId_commessa() %>" onclick="return confirm('Vuoi chiudere questa commessa?');">Chiudi Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>

		<%
			}else{
		%>
				<ul>
					<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Dettaglio Commessa</a></li>
					<li><a href="./GestioneCommessa?azione=risorseAssociate&stato=<%=commessa.getStato() %>&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
					<li><a href="./GestioneCommessa?azione=esportaCommessaPDF&parametro=<%=commessa.getId_commessa() %>">Esporta in PDF</a></li>
				</ul>
		<%
			}
		}else{
		%>	
			<ul>
				<li><a href="./GestioneCommessa?azione=dettaglioCommessa&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Dettaglio Commessa</a></li>
				<li><a href="./GestioneCommessa?azione=risorseAssociate&parametro=<%=commessa.getId_commessa() %>&tipologia=<%=commessa.getTipologia() %>">Risorse Associate</a></li>
				<li><a href="./GestioneCommessa?azione=risorseDaAssociare&tipologia=<%=commessa.getTipologia() %>&parametro=<%=commessa.getId_commessa() %>">Associare Risorsa</a></li>
			</ul>
		<%	
		}
		%>
	</div>
	
	<form action="./GestioneCommessa" method="post" name="inserisciCommessaMultipla">
		<input type="hidden" name="azione" value="modificaCommessa" />
		<input type="hidden" name="parametro" value="<%=commessa.getId_commessa() %>" />
		<input type="hidden" name="tipologiaCommessa" value="<%=commessa.getTipologia() %>" />
		
	
<%
	if(commessa.getTipologia().equals("1")){
%>
		<!-- questa tipologia serve per le commesse singolo dove abbiamo un cliente e una risorsa associata ad essa -->

<div>

	<p>* i campi segnati in asterisco sono obbligatori</p>

	<fieldset>
	<legend>Modifica Commessa Esterna Singola</legend>
	<table>
		<tr>
			<td><label>* Cliente: </label></td>
			<td>
				<select name="commessaEsternaSingola_codice">
					<option value="<%=commessa.getId_cliente() %>"><%=commessa.getDescrizioneCliente() %></option>
				</select>
			</td>
		</tr>
		<tr>
			<td><label>* Risorsa: </label></td>
			<td>
				<select name="commessaEsternaSingola_idRisorsa">
					<option value="<% //commessa.getId_risorsa() %>" ><%=commessa.getDescrizioneRisorsa() %></option>			
				</select>
			</td>
		</tr>
		<tr>
			<td>Codice</td>
			<td><input type="text" name="commessaEsternaSingola_codiceCommessa" id="codiceCommessa" onblur="controlloCodiceCommessa(this.value)" value="<%=commessa.getCodiceCommessa() %>" readonly="readonly" /></td>
		</tr>
		<tr>
			<td>* Data</td>
			<td><input type="text" name="commessaEsternaSingola_dataOfferta" class="data" value="<%=commessa.getData_offerta() %>"/></td>
		</tr>
		<tr>
			<td>* Oggetto</td>
			<td><input type="text" name="commessaEsternaSingola_oggettoOfferta" value="<%=commessa.getOggetto_offerta() %>"/></td>
	
			<td>* Descrizione</td>
			<td><input type="text" name="commessaEsternaSingola_descrizione" value="<%=commessa.getDescrizione() %>"/>
			</td>
		</tr>
		<tr>
			<td>Sede Lavoro</td>
			<td><input type="text" name="commessaEsternaSingola_sedeLavoro" value="<%=commessa.getSede_lavoro() %>"/>
			</td>
		</tr>
		<tr>
			<td>* Data Inizio</td>
			<td><input type="text" name="commessaEsternaSingola_dataInizio" value="<%=commessa.getData_inizio() %>" readonly="readonly"/></td>
	
			<td>* Data Fine</td>
			<td><input type="text" name="commessaEsternaSingola_dataFine" class="data" value="<%=commessa.getData_fine() %>"/></td>
		</tr>
		<tr>
			<td>* Importo</td>
			<td><input type="text" name="commessaEsternaSingola_importo" value="<%=commessa.getImporto() %>"/></td>
		</tr>
		<tr>
			<td>Importo Lettere</td>
			<td><input type="text" name="commessaEsternaSingola_importoLettere" value="<%=commessa.getImporto_lettere() %>"/></td>
	
			<td>Pagamento</td>
			<td><select name="commessaEsternaSingola_pagamento">
				<%
					if(commessa.getPagamento().equals("30")){
				%>
						<option value=""></option>
						<option value="30" selected="selected">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
				<%
					}else if(commessa.getPagamento().equals("60")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60" selected="selected">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("90")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90" selected="selected">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("120")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120" selected="selected">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("rf")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura" selected="selected">Ricevimento Fattura</option>
						
				<%		
					}else{
				%>
						<option value="" selected="selected"></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>				
				<%
					}
				%>			</select></td>
		</tr>
		<tr>
			<td>Ore</td>
			<td><input type="text" name="commessaEsternaSingola_ore" value="<%=commessa.getTotaleOre() %>"/></td>
			
			<td>Al</td>
			<td>
				<select name="commessaEsternaSingola_al">
					<%
						if(commessa.getAl().equals("ore")){
					%>
							<option value=""></option>
							<option value="ore" selected="selected">ore</option>
							<option value="giorno">giorno</option>
					<%
						}else{
					%>
							<option value=""></option>
							<option value="ore">ore</option>
							<option value="giorno" selected="selected">giorno</option>
					<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Note<br>
			(1000 caratteri)</td>
			<td>
				<textarea name="commessaEsternaSingola_note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','singola')" onfocus="cancellaCampoTextArea('inserisciCommessa','singola')"><%=commessa.getNote() %></textarea>
			</td>
		</tr>
		<tr>
			<td>Stato</td>
			<td>
				<select name="commessaEsternaSingola_stato">
					<option value="aperta" selected="selected">Aperta</option>
					<option value="chiusa">Chiusa</option>
				</select></td>
		</tr>
	</table>
	</fieldset>
	<table>
		<tr>
			<td>
				<input type="submit" value="modifica commessa" onclick="return controlloModificaCommessa('1')"/>
			</td>
			<td>
				<input type="reset" value="svuota campi" />
			</td>
		</tr>
	</table>
</div>

<%
	}else if(commessa.getTipologia().equals("2")){
%>
	<!-- questa tipologia serve per le commesse che hanno pi� di una risorsa associata -->

	<div>
	
	<p>* i campi segnati in asterisco sono obbligatori</p>
	
	<fieldset>
	<legend>Modifica Commessa Esterna Multipla</legend>
	<table>
		<tr>
			<td><label>* Cliente: </label></td>
			<td><select name="codice">
				<option value="<%=commessa.getId_cliente() %>" ><%=commessa.getDescrizioneCliente() %></option>
			</select></td>
		</tr>
		<tr>
			<td>* Codice</td>
			<td><input type="text" name="codiceCommessa" id="codiceCommessa" onblur="controlloCodiceCommessa(this.value)" value="<%=commessa.getCodiceCommessa() %>" readonly="readonly" /></td>
		</tr>
		<tr>
			<td>* Data</td>
			<td><input type="text" name="dataOfferta" class="data" value="<%=commessa.getData_offerta() %>"/></td>
		</tr>
		<tr>
			<td>* Oggetto</td>
			<td><input type="text" name="oggettoOfferta" value="<%=commessa.getOggetto_offerta() %>"/></td>
	
			<td>* Descrizione</td>
			<td><input type="text" name="descrizione" value="<%=commessa.getDescrizione() %>"/>
			</td>
		</tr>
		<tr>
			<td>Sede Lavoro</td>
			<td><input type="text" name="sedeLavoro" value="<%=commessa.getSede_lavoro() %>"/>
			</td>
		</tr>
		<tr>
			<td>* Data Inizio</td>
			<td><input type="text" name="dataInizio" value="<%=commessa.getData_inizio() %>" readonly="readonly"/></td>
	
			<td>* Data Fine</td>
			<td><input type="text" name="dataFine" class="data" value="<%=commessa.getData_fine() %>"/></td>
		</tr>
		<tr>
			<td>* Importo</td>
			<td><input type="text" name="importo" value="<%=commessa.getImporto() %>"/></td>
		</tr>
		<tr>
			<td>Importo Lettere</td>
			<td><input type="text" name="importoLettere" value="<%=commessa.getImporto_lettere() %>"/></td>
	
			<td>Pagamento</td>
			<td><select name="pagamento">
				<%
					if(commessa.getPagamento().equals("30")){
				%>
						<option value=""></option>
						<option value="30" selected="selected">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
				<%
					}else if(commessa.getPagamento().equals("60")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60" selected="selected">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("90")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90" selected="selected">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("120")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120" selected="selected">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("rf")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura" selected="selected">Ricevimento Fattura</option>
				<%		
					}else{
				%>
						<option value="" selected="selected"></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>				
				<%
					}
				%>
			</select></td>
		</tr>
		<tr>
			<td>Ore</td>
			<td><input type="text" name="ore" value="<%=commessa.getTotaleOre() %>"/></td>
			
			<td>Al</td>
			<td>
				<select name="al">
					<%
						if(commessa.getAl().equals("ore")){
					%>
							<option value=""></option>
							<option value="ore" selected="selected">ore</option>
							<option value="giorno">giorno</option>
					<%
						}else{
					%>
							<option value=""></option>
							<option value="ore">ore</option>
							<option value="giorno" selected="selected">giorno</option>
					<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Note<br>
			(1000 caratteri)</td>
			<td>
				<textarea name="note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','singola')" onfocus="cancellaCampoTextArea('inserisciCommessa','singola')">
					<%=commessa.getNote() %>
				</textarea>
			</td>
		</tr>
		<tr>
			<td>Stato</td>
			<td>
				<select name="stato">
					<option value="aperta" selected="selected">Aperta</option>
					<option value="chiusa">Chiusa</option>
				</select></td>
		</tr>
	</table>
	</fieldset>
	<table>
		<tr>
			<td><input type="submit" value="modifica commessa" id="inserisciTrattativa" onclick="return controlloModificaCommessa('2')" /></td>
			<td><input type="reset" value="svuota campi" /></td>
		</tr>
	</table>
	</div>

<%
	}else if(commessa.getTipologia().equals("3")){
%>

		<p>* i campi segnati in asterisco sono obbligatori</p>

		<fieldset>
		<legend>Modifica Commessa Interna</legend>
		<table>
			<tr>
				<td>* Codice</td>
				<td><input type="text" name="commessaInterna_codiceCommessa" id="codiceCommessa" onblur="controlloCodiceCommessa(this.value)" value="<%=commessa.getCodiceCommessa() %>" readonly="readonly" /></td>
			</tr>
			<tr>
				<td>* Data</td>
				<td><input type="text" name="commessaInterna_dataOfferta" class="data" value="<%=commessa.getData_offerta() %>"/></td>
			</tr>
			<tr>
				<td>* Oggetto</td>
				<td><input type="text" name="commessaInterna_oggettoOfferta" value="<%=commessa.getOggetto_offerta() %>"/></td>
			
				<td>* Descrizione</td>
				<td><input type="text" name="commessaInterna_descrizione" value="<%=commessa.getDescrizione() %>"/>
				</td>
			</tr>
			<tr>
				<td>Sede Lavoro</td>
				<td><input type="text" name="commessaInterna_sedeLavoro" value="<%=commessa.getSede_lavoro() %>"/>
				</td>
			</tr>
			<tr>
				<td>* Data Inizio</td>
				<td><input type="text" name="commessaInterna_dataInizio" value="<%=commessa.getData_inizio() %>" readonly="readonly"/></td>
			
				<td>* Data Fine</td>
				<td><input type="text" name="commessaInterna_dataFine" class="data" value="<%=commessa.getData_fine() %>"/></td>
			</tr>
			<tr>
				<td>* Importo</td>
				<td><input type="text" name="commessaInterna_importo" value="<%=commessa.getImporto() %>"/></td>
			</tr>
			<tr>
				<td>Importo Lettere</td>
				<td><input type="text" name="commessaInterna_importoLettere" value="<%=commessa.getImporto_lettere() %>"/></td>
		
				<td>Pagamento</td>
			<td><select name="commessaInterna_pagamento">
				<%
					if(commessa.getPagamento().equals("30")){
				%>
						<option value=""></option>
						<option value="30" selected="selected">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
				<%
					}else if(commessa.getPagamento().equals("60")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60" selected="selected">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("90")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90" selected="selected">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("120")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120" selected="selected">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>
						
				<%		
					}else if(commessa.getPagamento().equals("rf")){
				%>		
						<option value=""></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura" selected="selected">Ricevimento Fattura</option>
				<%		
					}else{
				%>
						<option value="" selected="selected"></option>
						<option value="30">30</option>
						<option value="60">60</option>
						<option value="90">90</option>
						<option value="120">120</option>
						<option value="ricevimento fattura">Ricevimento Fattura</option>				
				<%
					}
				%>
			</select></td>
		</tr>
		<tr>
			<td>Ore</td>
			<td><input type="text" name="commessaInterna_ore" value="<%=commessa.getTotaleOre() %>"/></td>
			
			<td>Al</td>
			<td>
				<select name="commessaInterna_al">
					<%
						if(commessa.getAl().equals("ore")){
					%>
							<option value=""></option>
							<option value="ore" selected="selected">ore</option>
							<option value="giorno">giorno</option>
					<%
						}else{
					%>
							<option value=""></option>
							<option value="ore">ore</option>
							<option value="giorno" selected="selected">giorno</option>
					<%
						}
					%>
				</select>
			</td>
		</tr>
			<tr>
				<td>Note<br>
				(1000 caratteri)</td>
				<td><textarea name="commessaInterna_note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','interna')" onfocus="cancellaCampoTextArea('inserisciCommessa','interna')"><%=commessa.getNote() %></textarea></td>
			</tr>
			<tr>
				<td>Stato</td>
				<td><select name="commessaInterna_stato">
					<option value="aperta">Aperta</option>
					<option value="chiusa">Chiusa</option>
				</select></td>
			</tr>
		</table>
		</fieldset>
		<table>
			<tr>
				<td><input type="submit" value="modifica commessa" onclick="return controlloModificaCommessa('3')"/></td>
				<td><input type="reset" value="svuota campi" /></td>
			</tr>
		</table>
<%
	}else if(commessa.getTipologia().equals("4")){
%>
		<fieldset>
			<legend>Aggiungi Commessa Generica</legend>
			<table>
				<tr>
					<td>Codice</td>
					<td><input type="text" name="altro_codiceCommessa" id="codiceCommessa" onblur="controlloCodiceCommessa(this.value)" value="<%=commessa.getCodiceCommessa() %>" readonly="readonly"/></td>
				</tr>
				<tr>
					<td>Descrizione</td>
					<td><input type="text" name="altro_descrizione" value="<%=commessa.getDescrizione() %>" /></td>
				</tr>
				<tr>
					<td>Data Inizio</td>
					<td>
						<%
							if(commessa.getData_inizio() != null){
								out.print("<input type=\"text\" name=\"dataInizio\" readonly=\"readonly\" value=\""+ commessa.getData_inizio() + "\" >");
							}
						%>
					</td>
					<td>Data Fine</td>
					<td>
						<%
							if(commessa.getData_fine() != null){
								out.print("<input type=\"text\" name=\"dataInizio\" readonly=\"readonly\" value=\""+ commessa.getData_fine() + "\" >");
							}
						%>
					</td>
				</tr>
				<tr>
					<td><br></td>
				</tr>
				<tr>
					<td>Tipologia Commessa:</td>
					<td colspan="3">
						<% 
							if(commessa.isFlag_ferie()){
						%>
								<input type="checkbox" name="ferie" value="ferie" checked="checked">Ferie
						<%
							}else{
						%>
								<input type="checkbox" name="ferie" value="ferie" >Ferie
						<%		
							}
							if(commessa.isFlag_mutua()){
						%>
								<input type="checkbox" name="mutua" value="mutua" checked="checked">Mutua
						<%
							}else{
						%>
								<input type="checkbox" name="mutua" value="mutua" >Mutua
						<%		
							}
							if(commessa.isFlag_permessi()){
						%>
								<input type="checkbox" name="permessi" value="permessi" checked="checked" >Permessi
						<%
							}else{
						%>
								<input type="checkbox" name="permessi" value="permessi" >Permessi
						<%		
							}
							if(commessa.isFlag_permessiNonRetribuite()){
						%>
								<input type="checkbox" name="permessiNonRetribuiti" value="permessiNonRetribuiti" checked="checked">Permessi Non Retribuiti<br><br>
						<%
							}else{
						%>
								<input type="checkbox" name="permessiNonRetribuiti" value="permessiNonRetribuiti">Permessi Non Retribuiti<br><br>
						<%		
							}
						%>
					</td>
				</tr>
				<tr>
					<td>Note<br>(1000 caratteri)</td>
					<td><textarea name="altro_note" cols="30" rows="10" onclick="cancellaCampoTextArea('inserisciCommessa','altro')" onfocus="cancellaCampoTextArea('inserisciCommessa','altro')"><%=commessa.getNote() %></textarea></td>
				</tr>
			</table>
		</fieldset>
		<table>
			<tr>
				<td>
					<input type="submit" value="modifica commessa"/>
				</td>
				<td>
					<input type="reset" value="svuota campi" />
				</td>
			</tr>
		</table>
<%
		}
%>

</form>
<%
	}
}else{
%>	
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%	
}
%>