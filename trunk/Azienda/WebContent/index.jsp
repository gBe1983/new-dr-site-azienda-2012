<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>	   
	<title>Azienda</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<META http-equiv="Content-Style-Type" content="text/css">
	
	
	<!-- caricamento dei fogli di stile -->
	<link rel="stylesheet" type="text/css" href="css/menuSceltaRapida.css">
	<link rel="stylesheet" type="text/css" href="css/style.css">
	<link rel="stylesheet" type="text/css" href="css/demo.css">
	<link rel="stylesheet" type="text/css" href="css/visualizzaCurriculum.css">
	<link rel="stylesheet" type="text/css" href="css/menuLaterale.css">
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css" />
	<!--  <link rel="stylesheet" type="text/css" href="css/jquery-ui-1.7.2.custom.css">  -->
	
	
	<!-- caricamento dei javascript -->
	<script type="text/javascript" src="script/controlloCodiceCommessa.js" ></script>
	<script type="text/javascript" src="script/controlloCodiceCliente.js" ></script>
	<script type="text/javascript" src="script/controlloForm.js" ></script>
	<script type="text/javascript" src="script/jquery-1.3.2.min.js" ></script>
	<script type="text/javascript" src="script/valorizzazioneTrattativaRisorsa.js" ></script>
	<script type="text/javascript" src="script/controlloDataFine_InserimentoAssociazioneCommesse.js" ></script>
	<script type="text/javascript" src="script/controlloDataFine_ModificaAssociazioneCommesse.js" ></script>
	<script type="text/javascript" src="script/controlloDataInizio_InserimentoAssociazioneCommesse.js" ></script>
	<script type="text/javascript" src="script/script.js" ></script>
	<script type="text/javascript" src="script/curriculum.js" ></script>
	<script type="text/javascript" src="script/jquery-1.9.1.js" ></script>
	<script type="text/javascript" src="script/jquery-ui.js" ></script>
	
	
</head>
<body class="home" >

	
	<div id="container" class="shadow">	   
    	<div id="header"><jsp:include page="strutturaSito/menu/menuAlto.jsp" /></div> 
	    <div id="content">	      	
    		<div id="extra"><jsp:include page="strutturaSito/menu/menuLaterale.jsp" /></div>     		
    		<div id="content_right">
    			<%
    				if(request.getParameter("azione") !=null){
    					String azione = request.getParameter("azione");
    					if(azione.equals("homePage")){
    			%>
							<jsp:include page="strutturaSito/contenuto/homePage.jsp" />
				<%
    					}else if(azione.equals("registrazioneAzienda") || azione.equals("visualizzaAzienda") || azione.equals("aggiornaAzienda")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/aggiungiAzienda.jsp" />
				<%  
    					}else if(azione.equals("messaggio")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/messaggio.jsp" />
				<%  
    					}else if(azione.equals("aggiungiRisorsa") || azione.equals("modificaRisorsa") || azione.equals("dettaglioRisorsa")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/aggiungiRisorsa.jsp" />
				<%  
    					}else if(azione.equals("aggiungiCliente") || azione.equals("visualizzaCliente") || azione.equals("aggiornaCliente")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/aggiungiCliente.jsp" />
				<%  
    					}else if(azione.equals("ricercaRisorse")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/ricercaRisorse.jsp" />
				<%  
    					}else if(azione.equals("visualizzaRisorse")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/visualizzaRisorse.jsp" />
				<%  
    					}else if(azione.equals("visualizzaNominativi")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/ricercaCliente.jsp" />
				<%  
    					}else if(azione.equals("visualizzaTrattative")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/visualizzaTrattative.jsp" />
				<%  
    					}else if(azione.equals("aggiungiTrattative") || azione.equals("aggiornaTrattativa")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/aggiungiTrattative.jsp" />
				<%  
    					}else if(azione.equals("cambioPassword")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/cambioPassword.jsp" />
				<%  
    					}else if(azione.equals("cliente")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/cliente.jsp" />
				<%  
    					}else if(azione.equals("risorsa")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/risorsa.jsp" />
				<%  
    					}else if(azione.equals("trattative")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/trattative.jsp" />
				<%  
    					}else if(azione.equals("commessa")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/commessa.jsp" />
				<%  
    					}else if(azione.equals("curriculum")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/curriculum.jsp" />
				<%  
    					}else if(azione.equals("aggiungiCommessa") || azione.equals("aggiornaCommessa") || azione.equals("dettaglioCommessa")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/aggiungiCommessa.jsp" />
				<%  
    					}else if(azione.equals("visualizzaCommesse")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/visualizzaCommesse.jsp" />
				<%  
    					}else if(azione.equals("ricercaCommessa")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/ricercaCommesse.jsp" />
				<%  
    					}else if(azione.equals("report")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/report.jsp" />
				<%  
    					}else if(azione.equals("risorseAssociate")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/visualizzaAssociazioneCommessaRisorsa.jsp" />
				<%  
    					}else if(azione.equals("risorseDaAssociate")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/visualizzaRisorsaDaAssociareCommessa.jsp" />
				<%  
    					}else if(azione.equals("caricaAssociazione") || azione.equals("dettaglioAssociazione") ){
    			%>	
							<jsp:include page="strutturaSito/contenuto/caricaAssociazione.jsp" />
				<%  
    					}else if(azione.equals("credenziali")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/modificaCredenziali.jsp" />
				<%  
    					}else if(azione.equals("chiudiMensilita")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/chiudiMensilita.jsp" />
				<%  
    					}else if(azione.equals("visualizzaReport")){
    			%>	
							<jsp:include page="strutturaSito/contenuto/visualizzaReport.jsp" />
				<%
						}else if(azione.equals("listaRisorseDaAbilitare")){
				%>
							<jsp:include page="strutturaSito/contenuto/listaRisorseDaAbilitare.jsp" />
				<%
						}else if(azione.equals("visualizzaCurriculum")){
				%>
							<jsp:include page="strutturaSito/contenuto/visualizzaCurriculum.jsp" />
				<%
						}else if(azione.equals("dettaglioCurriculum")){
				%>
							<jsp:include page="strutturaSito/contenuto/dettaglioCurriculum.jsp" />
				<%
						}else if(azione.equals("gestioneSingoleSezioniCurriculum")){
				%>
							<jsp:include page="strutturaSito/contenuto/gestioneSingoleSezioniCurriculum.jsp" />
				<%
						}else if(azione.equals("gestioneAnteprimeSezioniCurriculum")){
				%>
							<jsp:include page="strutturaSito/contenuto/gestioneAnteprimeSezioniCurriculum.jsp" />
				<%
						}else if(azione.equals("creaCv")){
				%>
							<jsp:include page="strutturaSito/contenuto/creaCv.jsp" />
				<%
						}else if(azione.equals("anteprimaCurriculum")){
				%>
							<jsp:include page="strutturaSito/contenuto/anteprimaCurriculum.jsp" />
				<%
						}else if(azione.equals("selezionaRisorsa")){
				%>
							<jsp:include page="strutturaSito/contenuto/selezionaRisorsa.jsp" />
				<%
						}else if(azione.equals("visualizzaElencoCurriculum")){
				%>
							<jsp:include page="strutturaSito/contenuto/visualizzaElencoCurriculum.jsp" />
				<%
						}else if(azione.equals("esportaPdf")){
				%>
							<jsp:include page="strutturaSito/contenuto/esportaPdf.jsp" />
				<%
						}
					}else{
				%>
						<jsp:include page="strutturaSito/contenuto/homePage.jsp" />
				<%
    				}
				%>
    		</div>
			<div class="blank"></div>
		</div>
		<div id="footer" class="space"><ul><span id="Copyright"> &copy; Copyright - All Rights Reserved - DiErre Consulting Srl - </span><li><a href="./Gestione.jsp?pagina=PrincipiBase">Help</a></li></ul></div>
	</div>
</body>
</html>