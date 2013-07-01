<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.AziendaDTO"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){

	if(request.getParameter("azione") != null){
		if(request.getParameter("azione").equals("registrazioneAzienda")){
%>

<div class="subtitle ">Registrazione Azienda</div>


<div id="flusso">
	<table>
		<tr>
			<td><img src="images/home.gif"><a href="#">Home</a></td>
			<td><img src="images/cerca.jpg"><a href="#">Cerca</a></td>
		</tr>
	</table>
</div>

		
<form action="./GestioneAzienda" method="post" name="azienda">
			<input type="hidden" name="azione" value="registrazioneAzienda" />
			<fieldset>
				<legend>Dati Anagrafici</legend>
				<table>
						<tr>
							<td>
								<label>Ragione Sociale </label>
							</td>
							<td>
								<input type="text" name="ragioneSociale" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Indirizzo </label>
							</td>
							<td>
								<input type="text" name="indirizzo" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Citta </label>
							</td>
							<td>
								<input type="text" name="citta" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Provincia </label>
							</td>
							<td>
								<input type="text" name="provincia" size="2" maxlength="2"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Cap</label>
							</td>
							<td>
								<input type="text" name="cap" size="5" maxlength="5"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Nazione </label>
							</td>
							<td>
								<input type="text" name="nazione" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Telefono </label>
							</td>
							<td>
								<input type="text" name="telefono" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Fax</label>
							</td>
							<td>
								<input type="text" name="fax" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Email</label>
							</td>
							<td>
								<input type="text" name="email" />
							</td>
						</tr>
						<tr>
							<td>
								<label>Codice Fiscale</label>
							</td>
							<td>
								<input type="text" name="codiceFiscale" size="16" maxlength="16"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Partita Iva</label>
							</td>
							<td>
								<input type="text" name="pIva" size="10" maxlength="10"/>
							</td>
						</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>Dati Legali</legend>
				<table>
					<tr>
						<td>
							<label>Indirizzo</label>
						</td>
						<td>
							<input type="text" name="indirizzoLegale" />
						</td>
					</tr>
					<tr>
						<td>
							<label>Citta</label>
						</td>
						<td>
							<input type="text" name="cittaLegale" />
						</td>
					</tr>
					<tr>
						<td>
							<label>Provincia</label>
						</td>
						<td>
							<input type="text" name="provinciaLegale" size="2" maxlength="2"/>
						</td>
					</tr>
					<tr>
						<td>
							<label>Cap</label>
						</td>
						<td>
							<input type="text" name="capLegale" size="5" maxlength="5"/>
						</td>
					</tr>
					<tr>
						<td>
							<label>Nazione</label>
						</td>
						<td>
							<input type="text" name="nazioneLegale" />
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>Altri Dati</legend>
				<table>
					<tr>
						<td>
							<label>Referente</label>
						</td>
						<td>
							<input type="text" name="referente" />
						</td>
					</tr>
					<tr>
						<td>
							<label>Telefono</label>
						</td>
						<td>
							<input type="text" name="telefonoReferente" />
						</td>
					</tr>
					<tr>
						<td>
							<label>Sito</label>
						</td>
						<td>
							<input type="text" name="sito" />
						</td>
					</tr>
					<tr>
						<td>
							<input class="checkbox" type="checkbox" name="trattamento" >
						</td>
						<td>
							<textarea rows="4" cols="30" name="trattamento_dati" disabled="disabled">Autorizzo il trattamento dei dati personali contenuti nel mio curriculum vitae in base art. 13 del D. Lgs. 196/2003. </textarea>
						</td>
					</tr>
				</table>
			</fieldset>
			<fieldset>
				<legend>Utente</legend>
				<table>
					<tr>
						<td>
							<label>Username</label>
						</td>
						<td>
							<input type="text" name="username" />
						</td>
					</tr>
					<tr>
						<td>
							<label>Password</label>
						</td>
						<td>
							<input type="password" name="password" />
						</td>
					</tr>
				</table>
			</fieldset>
				<table>
					<tr>
						<td><input type="submit" value="Registrazione Azienda" onclick="return controlloIscrizioneAzienda()"/></td>
						<td><input type="reset" value="Cancella Dati" /></td>
					</tr>
				</table>
		</form>
<%
		}else if(request.getParameter("azione").equals("visualizzaAzienda")){
			AziendaDTO azienda = (AziendaDTO) request.getAttribute("profiloAzienda");
%>

<div class="subtitle">Visualizza Profilo</div>


<div id="flussoAzienda">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
		</tr>
	</table>
</div>


			
				<fieldset class="spazio">
					<legend>Dati Anagrafici</legend>
					<table>
							<tr>
								<td>
									<label>Ragione Sociale </label>
								</td>
								<td>
									<label><%=azienda.getRagioneSociale() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Indirizzo </label>
								</td>
								<td>
									<label><%=azienda.getIndirizzo() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Citta </label>
								</td>
								<td>
									<label><%=azienda.getCitta() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Provincia </label>
								</td>
								<td>
									<label><%=azienda.getProvincia() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Cap</label>
								</td>
								<td>
									<label><%=azienda.getCap() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Nazione </label>
								</td>
								<td>
									<label><%=azienda.getNazione() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Telefono </label>
								</td>
								<td>
									<label><%=azienda.getTelefono() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Fax</label>
								</td>
								<td>
									<label><%=azienda.getFax() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Email</label>
								</td>
								<td>
									<label><%=azienda.getMail() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Codice Fiscale</label>
								</td>
								<td>
									<label><%=azienda.getCodiceFiscale() %> </label>
								</td>
							</tr>
							<tr>
								<td>
									<label>Partita Iva</label>
								</td>
								<td>
									<label><%=azienda.getPIva() %> </label>
								</td>
							</tr>
					</table>
				</fieldset>
				<fieldset>
					<legend>Dati Legali</legend>
					<table>
						<tr>
							<td>
								<label>Indirizzo</label>
							</td>
							<td>
								<label><%=azienda.getIndirizzoLegale() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<label>Citta</label>
							</td>
							<td>
								<label><%=azienda.getCittaLegale() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<label>Provincia</label>
							</td>
							<td>
								<label><%=azienda.getProvinciaLegale() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<label>Cap</label>
							</td>
							<td>
								<label><%=azienda.getCapLegale() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<label>Nazione</label>
							</td>
							<td>
								<label><%=azienda.getNazioneLegale() %> </label>
							</td>
						</tr>
					</table>
				</fieldset>
				<fieldset>
					<legend>Altri Dati</legend>
					<table>
						<tr>
							<td>
								<label>Referente</label>
							</td>
							<td>
								<label><%=azienda.getReferente() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<label>Telefono</label>
							</td>
							<td>
								<label><%=azienda.getTelefono() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<label>Sito</label>
							</td>
							<td>
								<label><%=azienda.getSito() %> </label>
							</td>
						</tr>
						<tr>
							<td>
								<input class="checkbox" type="checkbox" name="trattamento" checked="checked" disabled="disabled">
							</td>
							<td>
								<textarea rows="4" cols="30" name="trattamento_dati" disabled="disabled">Autorizzo il trattamento dei dati personali contenuti nel mio curriculum vitae in base art. 13 del D. Lgs. 196/2003. </textarea>
							</td>
						</tr>
					</table>
				</fieldset>	
<%		
		}else if(request.getParameter("azione").equals("aggiornaAzienda")){
			AziendaDTO azienda = (AziendaDTO) request.getAttribute("profiloAzienda");
%>

<div class="subtitle ">Modifica Profilo</div>


<div id="flussoAzienda">
	<table>
		<tr>
			<td><a href="index.jsp?azione=homePage">Home</a></td>
		</tr>
	</table>
</div>


			<p class="spazio">* i campi segnati con l'asterisco sono obbligatori</p>
			
			<form action="./GestioneAzienda" method="post" name="azienda">
			<input type="hidden" name="azione" value="modificaAzienda" />
				<fieldset>
					<legend>Dati Anagrafici</legend>
					<table>
							<tr>
								<td>
									<label>* Ragione Sociale </label>
								</td>
								<td>
									<input type="text" name="ragioneSociale" value="<%=azienda.getRagioneSociale() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Indirizzo </label>
								</td>
								<td>
									<input type="text" name="indirizzo" value="<%=azienda.getIndirizzo() %>" />
								</td>
							</tr>
							<tr>
								<td>
									<label>Citta </label>
								</td>
								<td>
									<input type="text" name="citta" value="<%=azienda.getCitta() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Provincia </label>
								</td>
								<td>
									<input type="text" name="provincia" size="2" maxlength="2" value="<%=azienda.getProvincia() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Cap</label>
								</td>
								<td>
									<input type="text" name="cap" size="5" maxlength="5" value="<%=azienda.getCap() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Nazione </label>
								</td>
								<td>
									<input type="text" name="nazione" value="<%=azienda.getNazione() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Telefono </label>
								</td>
								<td>
									<input type="text" name="telefono" value="<%=azienda.getTelefono() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Fax</label>
								</td>
								<td>
									<input type="text" name="fax" value="<%=azienda.getFax() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>* Email</label>
								</td>
								<td>
									<input type="text" name="email" value="<%=azienda.getMail() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>Codice Fiscale</label>
								</td>
								<td>
									<input type="text" name="codiceFiscale" size="16" maxlength="16" value="<%=azienda.getCodiceFiscale() %>"/>
								</td>
							</tr>
							<tr>
								<td>
									<label>* Partita Iva</label>
								</td>
								<td>
									<input type="text" name="pIva" size="11" maxlength="11" value="<%=azienda.getPIva() %>"/>
								</td>
							</tr>
					</table>
				</fieldset>
				<fieldset>
					<legend>Dati Legali</legend>
					<table>
						<tr>
							<td>
								<label>* Indirizzo</label>
							</td>
							<td>
								<input type="text" name="indirizzoLegale" value="<%=azienda.getIndirizzoLegale() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>* Citta</label>
							</td>
							<td>
								<input type="text" name="cittaLegale" value="<%=azienda.getCittaLegale() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>* Provincia</label>
							</td>
							<td>
								<input type="text" name="provinciaLegale" size="2" maxlength="2" value="<%=azienda.getProvinciaLegale() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>* Cap</label>
							</td>
							<td>
								<input type="text" name="capLegale" size="5" maxlength="5" value="<%=azienda.getCapLegale() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>* Nazione</label>
							</td>
							<td>
								<input type="text" name="nazioneLegale" value="<%=azienda.getNazioneLegale() %>"/>
							</td>
						</tr>
					</table>
				</fieldset>
				<fieldset>
					<legend>Altri Dati</legend>
					<table>
						<tr>
							<td>
								<label>Referente</label>
							</td>
							<td>
								<input type="text" name="referente" value="<%=azienda.getReferente() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Telefono</label>
							</td>
							<td>
								<input type="text" name="telefonoReferente" value="<%=azienda.getTelefono() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label>Sito</label>
							</td>
							<td>
								<input type="text" name="sito" value="<%=azienda.getSito() %>"/>
							</td>
						</tr>
						<tr>
							<td>
								<input class="checkbox" type="checkbox" name="trattamento" >
							</td>
							<td>
								<textarea rows="4" cols="30" name="trattamento_dati" disabled="disabled">Autorizzo il trattamento dei dati personali contenuti nel mio curriculum vitae in base art. 13 del D. Lgs. 196/2003. </textarea>
							</td>
						</tr>
					</table>
				</fieldset>
					<table>
						<tr>
							<td><input type="submit" value="Modifica Azienda" onclick="return controlloModificaAzienda()"/></td>
							<td><input type="reset" value="Cancella Dati" /></td>
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