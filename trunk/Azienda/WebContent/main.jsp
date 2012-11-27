<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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
<link rel="stylesheet" type="text/css" href="css/jquery-ui-1.7.2.custom.css">
<link rel="stylesheet" type="text/css" href="css/azienda/timeReport.css">
<!-- caricamento dei javascript -->

<script type="text/javascript" src="script/controlloCodiceCommessa.js"></script>
<script type="text/javascript" src="script/controlloCodiceCliente.js"></script>
<script type="text/javascript" src="script/controlloForm.js"></script>
<script type="text/javascript" src="script/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="script/jquery-ui-1.7.2.custom.min.js"></script>
<script type="text/javascript" src="script/valorizzazioneTrattativaRisorsa.js"></script>
<script type="text/javascript" src="script/controlloDataFine_InserimentoAssociazioneCommesse.js"></script>
<script type="text/javascript" src="script/controlloDataFine_ModificaAssociazioneCommesse.js"></script>
<script type="text/javascript" src="script/controlloDataInizio_InserimentoAssociazioneCommesse.js"></script>
</head>
<body class="home">
<div id="container" class="shadow">
<div id="header">
<%@include file="strutturaSito/menu/menuAlto.jsp"%>
</div>
<div id="content">
<%
String azione = request.getParameter("azione");
if(azione!=null){
if(azione.equals("homePage")){
%>
<%@include file="strutturaSito/contenuto/homePage.jsp"%>
<%
}else if(azione.equals("visualizzaConsuntivi")){
%>
<%@include file="strutturaSito/contenuto/azienda/timeReport.jsp"%>
<%
}else{
%>
<%@include file="strutturaSito/contenuto/homePage.jsp"%>
<%
}
}
%>
</div>
<div id="footer" class="space">
<ul>
<span id="Copyright"> &copy; Copyright - All Rights Reserved - DiErre Consulting Srl - </span>
<li><a href="./Gestione.jsp?pagina=PrincipiBase">Help</a></li>
</ul>
</div>
</div>
</body>
</html>