<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.UtenteDTO"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>

<%
	HttpSession sessione = request.getSession();
		if(sessione.getAttribute("utenteLoggato") != null){
			
			int risorsa = 0;
			
			if(request.getParameter("risorsa") != null){
				risorsa = Integer.parseInt(request.getParameter("risorsa"));
			}
				
%>
<div class="newsbox">
	<div id="main"> 
		<ul class="menuLaterale">
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Clienti</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="index.jsp?azione=aggiungiCliente&dispositiva=cliente">Aggiungi Cliente</a></li>
							<li><a href="./GestioneCliente?azione=caricamentoNominativiCliente">Ricerca Cliente</a></li>
							<li><a href="./GestioneCliente?azione=caricamentoNominativiClienteDisabilitati">Riabili. Cliente</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Risorse</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="index.jsp?azione=aggiungiRisorsa&dispositiva=risorsa">Aggiungi risorsa</a></li>
							<li><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Ricerca risorse</a></li>
							<li><a href="./GestioneRisorse?azione=listaRisorseDaAbilitare">Riabili. Risorsa</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Curriculum</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=gestione" class="linkAlti" >Gestione C.V.</a></li>
							<li><a href="#" class="linkAlti" >Esporta PDF</a></li>
							<li><a href="./GestioneCurriculum?azione=caricamentoAllCurriculum&dispositiva=anteprimaCv" class="linkAlti" >Anteprima C.V.</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Trattative</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="./GestioneTrattattive?azione=aggiungiTrattative&dispositiva=trattamenti">Aggiungi Trattative</a></li>
							<li><a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=tutte&dispositiva=trattative">Visualizza Tratt.</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Commessa</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="./GestioneTrattattive?azione=aggiungiCommessa">Aggiungi Comm.</a></li>
							<li><a href="./GestioneTrattattive?azione=ricercaCommessa">Ricerca Comm.</a></li>
							<li><a href="./index.jsp?azione=chiudiMensilita">Chiudi Mensilità</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Report</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="./GestioneReport?azione=caricamentoReport">Visualizza Report</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li class="menuPrincipale">
				<ul>
					<li class="title"><a href="#">Area Privata</a></li>
					<li class="sub-menu">
						<ul>
							<li><a href="./GestioneAzienda?azione=aggiornaAzienda">Modifica Profilo</a></li>
							<li><a href="./GestioneAzienda?azione=visualizzaAzienda">Visualizza Profilo</a></li>
							<li><a href="./index.jsp?azione=cambioPassword">Modifica Password</a></li>
							<li><a href="./GestioneAzienda?azione=eliminaProfilo" onClick="return confirm('Sei sicuro di voler Cancellare il tuo profilo aziendale?');">Cancella Profilo</a></li>
						</ul>
					</li>
				</ul>	
			</li>
		</ul>
	</div>
</div>
<%
}
%>