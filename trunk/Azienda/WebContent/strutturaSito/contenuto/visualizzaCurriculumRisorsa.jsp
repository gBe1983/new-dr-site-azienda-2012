<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>
<%@page import="it.azienda.dto.EsperienzeDTO"%>
<%@page import="it.azienda.dto.Dettaglio_Cv_DTO"%>

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
	<link rel="stylesheet" type="text/css" href="../../css/menuRisorsa.css">
	<link rel="stylesheet" type="text/css" href="../../css/style.css">
	<link rel="stylesheet" type="text/css" href="../../css/demo.css">
	<link rel="stylesheet" type="text/css" href="../../css/visualizzaCurriculum.css">
	<link rel="stylesheet" type="text/css" href="../../css/jquery-ui-1.7.2.custom.css">
	<link rel="stylesheet" type="text/css" href="../../css/overlay.css">
	
	<!-- caricamento dei javascript -->
	
	<script type="text/javascript" src="../../script/controlloCodiceCommessa.js" ></script>
	<script type="text/javascript" src="../../script/controlloCodiceCliente.js" ></script>
	<script type="text/javascript" src="../../script/controlloForm.js" ></script>
	<script type="text/javascript" src="../../script/jquery-1.3.2.min.js" ></script>
	<script type="text/javascript" src="../../script/jquery-ui-1.7.2.custom.min.js" ></script>
	<script type="text/javascript" src="../../script/valorizzazioneTrattativaRisorsa.js" ></script>
	<script type="text/javascript" src="../../script/overlay.js" ></script> 
  	
</head>


<body class="home" >

	<script language="javascript">

$(document).ready(function doOverlayOpen() {
	//set status to open
	isOpen = true;
	showOverlayBox();
	$('.bgCover').css({opacity:0}).animate( {opacity:0.5, backgroundColor:'#000'} );
	// dont follow the link : so return false.
	return false;
});


function showOverlayBox() {
	//if box is not set to open then don't do anything
	if( isOpen == false ) return;
	// set the properties of the overlay box, the left and top positions
	$('.fieldset').css({
		display:'block',
		left: 280,
		top: 10,
		position:'absolute',
		overflow: 'scroll'
	});
	// set the window background for the overlay. i.e the body becomes darker
	$('.bgCover').css({
		display:'block',
		width: $(window).width(),
		height:$(window).height(),
	});
}

function doOverlayClose() {
	//set status to closed
	isOpen = false;
	$('.fieldset').css( 'display', 'none' );
	// now animate the background to fade out to opacity 0
	// and then hide it after the animation is complete.
	$('.bgCover').animate( {opacity:0}, null, null, function() { $(this).hide(); } );
}
// if window is resized then reposition the overlay box
$(window).bind('resize',showOverlayBox);
// activate when the link with class launchLink is clicked
$('a.launchLink').click( doOverlayOpen );
// close it when closeLink is clicked
$('a.closeLink').click( doOverlayClose );

</script>

	<div class="bgCover">&nbsp;</div>
		<div class="fieldset">
<%
	boolean titoloEsperienze = false;
	HttpSession sessioneCurriculum = request.getSession();
	if(sessioneCurriculum.getAttribute("utenteLoggato") != null){	
			ArrayList curriculum = (ArrayList) sessioneCurriculum.getAttribute("curriculumVitae");
				for(int y = 0; y < curriculum.size(); y++){
					if(curriculum.get(y) instanceof RisorsaDTO){
						RisorsaDTO profilo = (RisorsaDTO) curriculum.get(y);
%>
					<ul id="listaLink">
						<li><a href="../../GestioneRisorse?azione=caricamentoProfiloRisorsa&page=dettaglio&risorsa=<%=profilo.getIdRisorsa() %>" class="closeLink">Close</a></li>
						<li><a href="../../GestioneCurriculum?azione=esportaPdf&risorsa=<%=profilo.getIdRisorsa() %>">Esporta in PDF</a></li>
					</ul>
					
		<%
				}
			}
			for(int x = 0; x < curriculum.size(); x++){
				if(curriculum.get(x) instanceof RisorsaDTO){
					RisorsaDTO risorsa = (RisorsaDTO) curriculum.get(x);
		%>
					<div id="intestazione">
					<h1>Formato europeo<br>
					per il curriculum<br>
					vitae</h1>
					</div>
					
					<h3>Informazioni personali</h3>
					<div class="row">
						<label>Cognome</label>
						<span><%=risorsa.getCognome() %></span>
					</div>
					<div class="row">
						<label>Nome</label>
						<span><%=risorsa.getNome() %></span>
					</div>
					<div class="row">
						<label>Indirizzo</label>
						<span><%=risorsa.getIndirizzo() %></span>
					</div>
					<div class="row">
						<label>Telefono</label>
						<span><%=risorsa.getTelefono() %></span>
					</div>
					<div class="row">
						<label>Fax</label>
						<span><%=risorsa.getFax() %></span>
					</div>
					<div class="row">
						<label>E-mail</label>
						<span><%=risorsa.getEmail() %></span>
					</div>
					<div class="row">
						<label>Data di nascita</label>
						<span><%=risorsa.getDataNascita() %></span>
					</div>
					<div class="row">
						<label></label>
						<span></span>
					</div>
		<%			
				}else if(curriculum.get(x) instanceof EsperienzeDTO){
					EsperienzeDTO esperienze = (EsperienzeDTO) curriculum.get(x);
		%>			
							<%
								if(!titoloEsperienze){
									titoloEsperienze = true;
							%>
									<h3>Esperienze Lavorative</h3>
							<%
								}
							%>
							<div class="rowEsperienze">
								<label>Periodo:  </label>
								<span><%=esperienze.getPeriodo() %></span>
							</div>
							<div class="rowEsperienze">
								<label>Azienda: </label>
								<span><%=esperienze.getAzienda() %></span>
							</div>
							<div class="rowEsperienze">
								<label>Luogo: </label>
								<span><%=esperienze.getLuogo() %></span>
							</div>
							<div class="rowEsperienze">
								<label>Descrizione: </label>
								<span><%=esperienze.getDescrizione() %></span>
							</div>
							<div class="rowEsperienze">
								<label><br></label>
								<span><br></span>
							</div>
		<%			
				}else if(curriculum.get(x) instanceof Dettaglio_Cv_DTO){
					Dettaglio_Cv_DTO dettaglio = (Dettaglio_Cv_DTO) curriculum.get(x);
		%>
						<h3>Dettaglio Curriculum</h3>
						
						<div class="rowEsperienze">
							<label>Capacita Professionali:  </label>
							<span><%=dettaglio.getCapacita_professionali() %></span>
						</div>
						<div class="rowEsperienze">
							<label><br></label>
							<span><br></span>
						</div>
						<div class="rowEsperienze">
							<label>Competenze Tecniche: </label>
							<span><%=dettaglio.getCompetenze_tecniche() %></span>
						</div>
						<div class="rowEsperienze">
							<label><br></label>
							<span><br></span>
						</div>
						<div class="rowEsperienze">
							<label>Lingue: </label>
							<span><%=dettaglio.getLingue_Straniere() %></span>
						</div>
						<div class="rowEsperienze">
							<label><br></label>
							<span><br></span>
						</div>
						<div class="rowEsperienze">
							<label>Istruzione: </label>
							<span><%=dettaglio.getIstruzione() %></span>
						</div>
						<div class="rowEsperienze">
							<label><br></label>
							<span><br></span>
						</div>
						<div class="rowEsperienze">
							<label>Formazione: </label>
							<span><%=dettaglio.getFormazione() %></span>
						</div>
						<div class="rowEsperienze">
							<label><br></label>
							<span><br></span>
						</div>
						<div class="rowEsperienze">
							<label>Interessi: </label>
							<span><%=dettaglio.getInteressi() %></span>
						</div>
						<div class="rowEsperienze">
							<label><br></label>
							<span><br></span>
						</div>
					
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
	</div>
</body>
</html>


