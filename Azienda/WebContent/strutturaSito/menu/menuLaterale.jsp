<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dto.UtenteDTO"%>

<%
	HttpSession sessione = request.getSession();
		if(sessione.getAttribute("utenteLoggato") != null){
			if(request.getParameter("azione").equals("creazioneCv")){
				int risorsa = Integer.parseInt(request.getParameter("risorsa"));
%>
		<div class="newsbox">
		    <div class="subtitle">Gestione Curriculum</div><br/>
		    	<li><a href="index.jsp?azione=creazioneCv&tipoCreazione=esperienze&risorsa=<%=risorsa%>&dettaglio=<%=request.getParameter("dettaglio") %>">Aggiungi Esperienze</a></li>
		    	
		    	<%
		    	if(request.getParameter("dettaglio") != null){
					if(!Boolean.parseBoolean(request.getParameter("dettaglio").toString())){
		    	%>
		    			<li><a href="index.jsp?azione=creazioneCv&tipoCreazione=dettaglioCv&risorsa=<%=risorsa%>&dettaglio=<%=request.getParameter("dettaglio") %>" >Aggiungi Dettaglio</a></li>
		    	<%
		    		}
		    	}
		    	%>
			<div class="blank"></div>
		</div>
		<div class="blank"></div>
<%
	}else if(request.getParameter("azione").equals("modificaCurriculumRisorsa") || request.getParameter("azione").equals("modificaSingoliCampiCurriculum") || request.getParameter("azione").equals("dettaglioSingoliCampiCurriculum")){
		int risorsa = Integer.parseInt(request.getParameter("risorsa"));
%>
		<div class="newsbox">
		    <div class="subtitle">Gestione Curriculum</div><br/>
		    	<li><a href="./GestioneCurriculum?azione=caricamentoCv&tipoCreazione=esperienze&risorsa=<%=risorsa %>">Visualizza Esperienze</a></li>
		    	<li><a href="./GestioneCurriculum?azione=caricamentoCv&tipoCreazione=dettaglioCv&risorsa=<%=risorsa %>">Visualizza Dettaglio Cv</a></li>
		   	<div class="blank"></div>
		</div>
		<div class="blank"></div>	
		
<%	
	//effettuo questo tipo di controllo per verificare che la ricerca avviene tramite il link "Trattative" 
	}	
String dispositiva = "";
if(request.getParameter("dispositiva") != null){
		dispositiva = request.getParameter("dispositiva");
}

if(request.getParameter("azione").equals("cliente") || dispositiva.equals("cliente")){
%>
 		<div class="newsbox">
 			<div class="subtitle">Gestione Area</div><br/>
	   			<li><a href="index.jsp?azione=aggiungiCliente&dispositiva=cliente">Aggiungi Cliente</a></li>
	   			<li><a href="./GestioneCliente?azione=caricamentoNominativiCliente">Ricerca Cliente</a></li>
	   			<li><a href="./GestioneCliente?azione=caricamentoNominativiClienteDisabilitati">Riabilitazione Cliente</a></li>
   			<div class="blank"></div>
		</div>
 	<%
}else if(request.getParameter("azione").equals("risorsa") || dispositiva.equals("risorsa")){
%>	
 		<div class="newsbox">
 			<div class="subtitle">Gestione Area</div><br/>
	 			<li><a href="index.jsp?azione=aggiungiRisorsa&dispositiva=risorsa">Aggiungi risorsa</a></li>
	 			<li><a href="index.jsp?azione=ricercaRisorse&dispositiva=risorsa">Ricerca risorse</a></li>
				<li><a href="./GestioneRisorse?azione=listaRisorseDaAbilitare">Riabilitazione Risorsa</a></li>
 			<div class="blank"></div>
		</div>
 	<%
}else if(request.getParameter("azione").equals("trattative") || dispositiva.equals("trattative")){
%>
 		<div class="newsbox">
 			<div class="subtitle">Gestione Area</div><br/>
			<li><a href="./GestioneTrattattive?azione=aggiungiTrattative&dispositiva=trattamenti">Aggiungi Trattative</a></li>
			<li><a href="./GestioneTrattattive?azione=ricercaTrattativaCliente&tipo=tutte&dispositiva=trattative">Visualizza Trattative</a></li>
			<div class="blank"></div>
		</div>
<%
}else if(request.getParameter("azione").equals("commessa") || dispositiva.equals("commessa")){
%>
		<div class="newsbox">
			<div class="subtitle">Gestione Area</div><br/>
			<li><a href="./GestioneTrattattive?azione=aggiungiCommessa">Aggiungi Commessa</a></li>
			<li><a href="./GestioneTrattattive?azione=ricercaCommessa">Ricerca Commessa</a></li>
			<li><a href="./index.jsp?azione=chiudiMensilita">Chiudi Mensilità</a></li>
			<div class="blank"></div>
		</div>
<%
}else if(request.getParameter("azione").equals("visualizzaConsuntivi")){
%>
	<div class="newsbox">
		<div class="subtitle">Gestione Area</div><br/>
		<li><a href="./GestioneReport?azione=visualizzaConsuntivi">Visualizza Report</a></li>
	<div class="blank"></div>
</div>
<%
}
%>
<div class="blank"></div>
<div class="newsbox">
    <div class="subtitle">Profilo</div>
	<ul>
		<li><a href="./GestioneAzienda?azione=aggiornaAzienda">Modifica</a></li>
		<li><a href="./GestioneAzienda?azione=visualizzaAzienda">Visualizza</a></li>
		<li><a href="./index.jsp?azione=cambioPassword">Modifica Password</a></li>
		<li><a href="./GestioneAzienda?azione=eliminaProfilo" onClick="return confirm('Sei sicuro di voler Cancellare il tuo profilo aziendale?');">Cancella Profilo</a></li>
		<br><br><br>
	</ul>
</div>
<%
}
%>