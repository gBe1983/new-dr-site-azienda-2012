<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.ClienteDTO"%>
<%@page import="it.azienda.dao.ClienteDAO"%>
<%@page import="java.sql.Connection"%>


<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){

	ClienteDAO clienteDAO = new ClienteDAO((Connection) controlloUtenteLoggato.getAttribute("connessione"));
	if(request.getAttribute("cliente") == null){
%>
<div class="subtitle ">Aggiungi Cliente</div>

<div id="aggiungiCliente">
<p>
	In questa sezione potrete gestire l'inserimento di tutti i clienti con cui avete un rappporto lavorativo.
</p>	
		
<p>* i campi segnati in asterisco sono obbligatori</p>

<form action="./GestioneCliente" method="post" name="cliente">
			<input type="hidden" name="azione" value="inserimentoCliente">
			<fieldset>
				<legend>Dati Anagrafici</legend>
				<table>
					<tr>
						<td><span>Codice</span></td>
						<td><input type="text" name="codiceCliente" id="codiceCliente" onblur="controlloCodiceCliente(this.value)" value="<%=clienteDAO.creazioneCodiceCliente() %>" readonly="readonly" /></td>
					</tr>
					<tr>
						<td><span>* Ragione Sociale</span></td>
						<td><input type="text" name="ragioneSociale" /></td>
					</tr>
					<tr>
						<td><span>Indirizzo</span></td>
						<td><input type="text" name="indirizzo" /></td>
					</tr>
					<tr>
						<td><span>Cap</span></td>
						<td><input type="text" name="cap" maxlength="5" size="5"/></td>
					</tr>
					<tr>
						<td><span>Citta</span></td>
						<td><input type="text" name="citta" /></td>
					</tr>
					<tr>
						<td><span>Provincia</span></td>
						<td><input type="text" name="provincia" maxlength="2" size="2"/></td>
					</tr>
					<tr>
						<td><span>* Partita Iva</span></td>
						<td><input type="text" name="pIva" maxlength="11" size="11" /></td>
					</tr>
				</table>	
			</fieldset>
			<fieldset>
				<legend>Altri Dati</legend>
				<table>
					<tr>
						<td><span>Referente</span></td>
						<td><input type="text" name="referente" /></td>
					</tr>
					<tr>
						<td><span>Telefono</span></td>
						<td><input type="text" name="telefono" /></td>
					</tr>
					<tr>
						<td><span>Cellulare</span></td>
						<td><input type="text" name="cellulare" /></td>
					</tr>
					<tr>
						<td><span>Fax</span></td>
						<td><input type="text" name="fax" /></td>
					</tr>
					<tr>
						<td><span>* Email</span></td>
						<td><input type="text" name="email" /></td>
					</tr>
					<tr>
						<td><span>Sito</span></td>
						<td><input type="text" name="sito" /></td>
					</tr>
					<tr>
						<td><span>Codice Fiscale</span></td>
						<td><input type="text" name="codFiscale" maxlength="16" size="16"/></td>
					</tr>
				</table>
			</fieldset>
			<table>
				<tr>
					<td><button type="submit" value="inserisci Cliente" id="inviaCliente" onclick="return controlloInserisciModificaCliente()">Inserisci Cliente</button></td>
					<td><button type="reset" value="svuota campi" />Svuota Campi</button></td>
				</tr>
			</table>
		</form>
</div>
<%
	}else if(request.getAttribute("cliente") != null && request.getParameter("azione").equals("visualizzaCliente")){
		ClienteDTO cliente = (ClienteDTO)request.getAttribute("cliente");
%>
<div class="subtitle">Visualizza Cliente</div>

<div id="flusso">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
			<%
				if(cliente.isAttivo()){
			%>
					<td><a href="./GestioneCliente?azione=caricamentoNominativiCliente">Cerca</a></td>
			<%
				}else{
			%>		
					<td><a href="./GestioneCliente?azione=caricamentoNominativiClienteDisabilitati">Cerca</a></td>
			<%	
				}
			%>
		</tr>
	</table>
</div>

<%
	if(cliente.isAttivo()){
%>
	<div id="bluemenu" class="bluetabs" >
		<ul>
			<li><a href="./GestioneCliente?azione=ricercaCliente&codice=<%=cliente.getId_cliente() %>">Modifica Cliente</a></li>
			<li><a href="./GestioneCliente?azione=disabilitaCliente&codice=<%=cliente.getId_cliente() %>" onclick="return confirm('Vuoi disabilitare questo Cliente?');">Disabilita Cliente</a></li>
			<li><a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&codice=<%=cliente.getId_cliente() %>&tipo=azienda&dispositiva=cliente">Trattative</a></li>
		</ul>
	</div>
<%
	}else{
%>
	<div id="bluemenu" class="bluetabs">
		<ul>
			<li><a href="./GestioneCliente?azione=abilitazioneCliente&codice=<%=cliente.getId_cliente() %>" onclick="return confirm('Vuoi abilitare questo Cliente?');">Abilita Cliente</a></li>
		</ul>
	</div>

<%
	}
%>
	<div class="spazioMin">
		<form action="./GestioneCliente" method="post" >
			<input type="hidden" name="azione" value="inserimentoCliente">
			<fieldset>
				<legend align="center">Dati Anagrafici</legend>
				<table>
					<tr>
						<td><label>Codice Cliente</label></td>
						<td><label><%=cliente.getId_cliente() %></label></td>
					</tr>
					<tr>
						<td><label>Ragione Sociale</label></td>
						<td><label><%=cliente.getRagioneSociale() %></label></td>
					</tr>
					<tr>
						<td><label>Indirizzo</label></td>
						<td><label><%=cliente.getIndirizzo() %></label></td>
					</tr>
					<tr>
						<td><label>Cap</label></td>
						<td><label><%=cliente.getCap() %></label></td>
					</tr>
					<tr>
						<td><label>Citta</label></td>
						<td><label><%=cliente.getCitta() %></label></td>
					</tr>
					<tr>
						<td><label>Provincia</label></td>
						<td><label><%=cliente.getProvincia() %></label></td>
					</tr>
					<tr>
						<td><label>Partita Iva</label></td>
						<td><label><%=cliente.getPIva() %></label></td>
					</tr>
				</table>	
			</fieldset>
			<fieldset>
				<legend align="center">Altri Dati</legend>
				<table>
					<tr>
						<td><label>Referente</label></td>
						<td><label><%=cliente.getReferente() %></label></td>
					</tr>
					<tr>
						<td><label>Telefono</label></td>
						<td><label><%=cliente.getTelefono() %></label></td>
					</tr>
					<tr>
						<td><label>Cellulare</label></td>
						<td><label><%=cliente.getCellulare() %></label></td>
					</tr>
					<tr>
						<td><label>Fax</label></td>
						<td><label><%=cliente.getFax() %></label></td>
					</tr>
					<tr>
						<td><label>Email</label></td>
						<td><label><%=cliente.getEmail() %></label></td>
					</tr>
					<tr>
						<td><label>Sito</label></td>
						<td><label><%=cliente.getSito() %></label></td>
					</tr>
					<tr>
						<td><label>Codice Fiscale</label></td>
						<td><label><%=cliente.getCodFiscale() %></label></td>
					</tr>
				</table>
			</fieldset>
		</form>
	</div>
<%
	}else{
		ClienteDTO cliente = (ClienteDTO)request.getAttribute("cliente");
%>
		<div class="subtitle ">Modifica Cliente</div>
		
		<div id="flusso">
			<table>
				<tr>
					<td><a href="index.jsp?azione=homePage">Home</a></td>
					<td><a href="./GestioneCliente?azione=caricamentoNominativiCliente">Cerca</a></td>
				</tr>
			</table>
		</div>
		
		<div id="bluemenu" class="bluetabs" class="spazio">
			<ul>
				<li><a href="./GestioneCliente?azione=ricercaCliente&nominativo=<%=cliente.getRagioneSociale() %>">Dettaglio Cliente</a></li>
				<li><a href="./GestioneCliente?azione=eliminaCliente&codice=<%=cliente.getId_cliente() %>" onclick="return confirm('Vuoi eliminare questo Cliente?');">Disabilita Cliente</a></li>
				<li><a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&codice=<%=cliente.getId_cliente() %>&tipo=azienda&dispositiva=cliente">Trattative</a></li>
			</ul>
		</div>
		
		<p>* i campi segnati in asterisco sono obbligatori</p>

		<form action="./GestioneCliente" method="post" name="cliente">
			<input type="hidden" name="azione" value="modificaCliente">
			<input type="hidden" name="codiceCliente" value="<%=cliente.getId_cliente() %>">
			<fieldset>
				<legend align="center">Dati Anagrafici</legend>
				<table>
					<tr>
						<td><label>Codice Cliente</label></td>
						<td><input type="text" name="codiceCliente" value="<%=cliente.getId_cliente() %>" readonly="readonly"/></td>
					</tr>
					<tr>
						<td><label>* Ragione Sociale</label></td>
						<td><input type="text" name="ragioneSociale" value="<%=cliente.getRagioneSociale() %>"/></td>
					</tr>
					<tr>
						<td><label>Indirizzo</label></td>
						<td><input type="text" name="indirizzo" value="<%=cliente.getIndirizzo() %>"/></td>
					</tr>
					<tr>
						<td><label>Cap</label></td>
						<td><input type="text" name="cap" value="<%=cliente.getCap() %>" maxlength="5" size="5"/></td>
					</tr>
					<tr>
						<td><label>Citta</label></td>
						<td><input type="text" name="citta" value="<%=cliente.getCitta() %>"/></td>
					</tr>
					<tr>
						<td><label>Provincia</label></td>
						<td><input type="text" name="provincia" value="<%=cliente.getProvincia() %>" maxlength="2" size="2"/></td>
					</tr>
					<tr>
						<td><label>* Partita Iva</label></td>
						<td><input type="text" name="pIva" value="<%=cliente.getPIva() %>" maxlength="11" size="11"/></td>
					</tr>
				</table>	
			</fieldset>
			<fieldset>
				<legend>Altri Dati</legend>
				<table>
					<tr>
						<td><label>Referente</label></td>
						<td><input type="text" name="referente" value="<%=cliente.getReferente() %>"/></td>
					</tr>
					<tr>
						<td><label>Telefono</label></td>
						<td><input type="text" name="telefono" value="<%=cliente.getTelefono() %>"/></td>
					</tr>
					<tr>
						<td><label>Cellulare</label></td>
						<td><input type="text" name="cellulare" value="<%=cliente.getCellulare() %>"/></td>
					</tr>
					<tr>
						<td><label>Fax</label></td>
						<td><input type="text" name="fax" value="<%=cliente.getFax() %>"/></td>
					</tr>
					<tr>
						<td><label>* Email</label></td>
						<td><input type="text" name="email" value="<%=cliente.getEmail() %>"/></td>
					</tr>
					<tr>
						<td><label>Sito</label></td>
						<td><input type="text" name="sito" value="<%=cliente.getSito() %>"/></td>
					</tr>
					<tr>
						<td><label>Codice Fiscale</label></td>
						<td><input type="text" name="codFiscale" value="<%=cliente.getCodFiscale() %>" maxlength="16" size="16"/></td>
					</tr>
				</table>
			</fieldset>
			<table>
				<tr>
					<td><button type="submit" value="modifica Cliente" onclick="return controlloInserisciModificaCliente()">Modifica Cliente</button></td>
				</tr>
			</table>
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