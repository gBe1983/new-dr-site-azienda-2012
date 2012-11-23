<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>	   
	<title>Azienda</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	
	<meta http-equiv="description" content="This is my page">
	
	<!-- caricamento dei fogli di stile -->
	<link rel="stylesheet" type="text/css" href="css/menuRisorsa.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<link rel="stylesheet" type="text/css" href="css/visualizzaCurriculum.css">
	<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.7.2.custom.css">
	
	<!-- caricamento dei javascript -->
	
	<script type="text/javascript" src="script/controlloCodiceCommessa.js" ></script>
	<script type="text/javascript" src="script/controlloCodiceCliente.js" ></script>
	<script type="text/javascript" src="script/controlloForm.js" ></script>
	<script type="text/javascript" src="script/jquery-1.3.2.min.js" ></script>
	<script type="text/javascript" src="script/jquery-ui-1.7.2.custom.min.js" ></script>
	<script type="text/javascript" src="script/valorizzazioneTrattativaRisorsa.js" ></script>
	<script type="text/javascript" src="script/controlloDataFine_InserimentoAssociazioneCommesse.js" ></script>
	<script type="text/javascript" src="script/controlloDataFine_ModificaAssociazioneCommesse.js" ></script>
	<script type="text/javascript" src="script/controlloDataInizio_InserimentoAssociazioneCommesse.js" ></script>
	 
  	
</head>
<body class="home" >
	<div id="container" class="shadow">	   
    	<div id="header">  	
    		<%@include file="strutturaSito/menu/menuAlto.jsp" %> 	
    	</div> 
	    <div id="content">	      	
    		<div id="extra">  	
    			<%@include file="strutturaSito/menu/menuLaterale.jsp" %> 	
    		</div>     		
    		<div id="content_right">
    			<%
    				if(request.getParameter("azione") !=null){
    					String azione = request.getParameter("azione");
    					if(azione.equals("homePage")){
    			%>
							<%@include file="strutturaSito/contenuto/homePage.jsp" %>
				<%
    					}else if(azione.equals("registrazioneAzienda") || azione.equals("visualizzaAzienda") || azione.equals("aggiornaAzienda")){
    			%>	
							<%@include file="strutturaSito/contenuto/aggiungiAzienda.jsp" %>
				<%  
    					}else if(azione.equals("messaggio")){
    			%>	
							<%@include file="strutturaSito/contenuto/messaggio.jsp" %>
				<%  
    					}else if(azione.equals("aggiungiRisorsa") || azione.equals("modificaRisorsa") || azione.equals("dettaglioRisorsa")){
    			%>	
							<%@include file="strutturaSito/contenuto/aggiungiRisorsa.jsp" %>
				<%  
    					}else if(azione.equals("aggiungiCliente") || azione.equals("visualizzaCliente") || azione.equals("aggiornaCliente")){
    			%>	
							<%@include file="strutturaSito/contenuto/aggiungiCliente.jsp" %>
				<%  
    					}else if(azione.equals("ricercaRisorse")){
    			%>	
							<%@include file="strutturaSito/contenuto/ricercaRisorse.jsp" %>
				<%  
    					}else if(azione.equals("visualizzaRisorse")){
    			%>	
							<%@include file="strutturaSito/contenuto/visualizzaRisorse.jsp" %>
				<%  
    					}else if(azione.equals("creazioneCv")){
    			%>	
							<%@include file="strutturaSito/contenuto/creazioneCv.jsp" %>
				<%  
    					}else if(azione.equals("modificaCurriculumRisorsa")){
    			%>	
							<%@include file="strutturaSito/contenuto/modificaCurriculumRisorsa.jsp" %>
				<%  
    					}else if(azione.equals("modificaSingoliCampiCurriculum")){
    			%>	
							<%@include file="strutturaSito/contenuto/modificaSingoliCampiCurriculum.jsp" %>
				<%  
    					}else if(azione.equals("visualizzaNominativi")){
    			%>	
							<%@include file="strutturaSito/contenuto/ricercaCliente.jsp" %>
				<%  
    					}else if(azione.equals("visualizzaTrattative")){
    			%>	
							<%@include file="strutturaSito/contenuto/visualizzaTrattative.jsp" %>
				<%  
    					}else if(azione.equals("aggiungiTrattative") || azione.equals("aggiornaTrattativa")){
    			%>	
							<%@include file="strutturaSito/contenuto/aggiungiTrattative.jsp" %>
				<%  
    					}else if(azione.equals("cambioPassword")){
    			%>	
							<%@include file="strutturaSito/contenuto/cambioPassword.jsp" %>
				<%  
    					}else if(azione.equals("cliente")){
    			%>	
							<%@include file="strutturaSito/contenuto/cliente.jsp" %>
				<%  
    					}else if(azione.equals("risorsa")){
    			%>	
							<%@include file="strutturaSito/contenuto/risorsa.jsp" %>
				<%  
    					}else if(azione.equals("trattative")){
    			%>	
							<%@include file="strutturaSito/contenuto/trattative.jsp" %>
				<%  
    					}else if(azione.equals("commessa")){
    			%>	
							<%@include file="strutturaSito/contenuto/commessa.jsp" %>
				<%  
    					}else if(azione.equals("aggiungiCommessa") || azione.equals("aggiornaCommessa") || azione.equals("dettaglioCommessa")){
    			%>	
							<%@include file="strutturaSito/contenuto/aggiungiCommessa.jsp" %>
				<%  
    					}else if(azione.equals("visualizzaCommesse")){
    			%>	
							<%@include file="strutturaSito/contenuto/visualizzaCommesse.jsp" %>
				<%  
    					}else if(azione.equals("ricercaCommessa")){
    			%>	
							<%@include file="strutturaSito/contenuto/ricercaCommesse.jsp" %>
				<%  
    					}else if(azione.equals("report")){
    			%>	
							<%@include file="strutturaSito/contenuto/report.jsp" %>
				<%  
    					}else if(azione.equals("risorseAssociate")){
    			%>	
								<%@include file="strutturaSito/contenuto/visualizzaAssociazioneCommessaRisorsa.jsp" %>
				<%  
    					}else if(azione.equals("risorseDaAssociate")){
    			%>	
								<%@include file="strutturaSito/contenuto/visualizzaRisorsaDaAssociareCommessa.jsp" %>
				<%  
    					}else if(azione.equals("caricaAssociazione") || azione.equals("dettaglioAssociazione") ){
    			%>	
							<%@include file="strutturaSito/contenuto/caricaAssociazione.jsp" %>
				<%  
    					}else if(azione.equals("credenziali")){
    			%>	
							<%@include file="strutturaSito/contenuto/modificaCredenziali.jsp" %>
				<%  
    					}else if(azione.equals("chiudiMensilita")){
    			%>	
							<%@include file="strutturaSito/contenuto/chiudiMensilita.jsp" %>
				<%  
    					}else if(azione.equals("dettaglioSingoliCampiCurriculum")){
    			%>	
							<%@include file="strutturaSito/contenuto/dettaglioSingoliCampiCurriculum.jsp" %>
				<%  
    					}else if(azione.equals("visualizzaReport")){
    			%>	
							<%@include file="strutturaSito/contenuto/visualizzaReport.jsp" %>
				<%  
    					}
    					
					}else{
				%>
						<%@include file="strutturaSito/contenuto/homePage.jsp" %>
				<%
    				}
				%>
    		</div>
			<div class="blank"></div>
		</div>
		<div id="footer" class="space">
			<ul>
				<span id="Copyright"> &copy; Copyright - All Rights Reserved - DiErre Consulting Srl - </span><li><a href="./Gestione.jsp?pagina=PrincipiBase">Help</a></li>
			</ul>
		</div>
	</div>
</body>
</html>