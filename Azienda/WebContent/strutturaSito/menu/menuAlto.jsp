<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="it.azienda.dao.UtenteDAO"%>
<%@page import="it.azienda.dto.UtenteDTO"%>

<div id="header">
	<%
	HttpSession sessioneUtente = request.getSession();
	if(sessioneUtente.getAttribute("utenteLoggato") != null){
			UtenteDTO utente = (UtenteDTO) sessioneUtente.getAttribute("utenteLoggato");
	%>
			<div id="benvenuto">
				<span id="scrittaBenvenuto"> Benvenuto: </span> <span><%=utente.getUsername() %> </span>
			</div>	
	<%
		}
	%>
	<div class="logo">
		<!--  <img src="images/logo_DierreConsulting.bmp"/> -->	
	</div>
	<div class="space">&nbsp;</div>
	<div id="nav">
		<div class="menu">
			<ul class="corner">
				<li><a href="./index.jsp?azione=cliente" class="linkAlti" title="cliente" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("cliente")){ out.print("id='on'");} %>>Cliente</a></li>
				<li><a href="./index.jsp?azione=risorsa" class="linkAlti" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("risorsa")){ out.print("id='on'");} %>>Risorsa</a></li>
				<li><a href="./index.jsp?azione=curriculum" class="linkAlti" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("caricamentoAllCurriculum")){ out.print("id='on'");} %>>Curriculum Vitae</a></li>
				<li><a href="./index.jsp?azione=trattative" class="linkAlti" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("trattative")){ out.print("id='on'");} %>>Trattative</a></li>
				<li><a href="./index.jsp?azione=commessa" class="linkAlti" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("commessa")){ out.print("id='on'");} %>>Commessa</a></li>
				<li><a href="./index.jsp?azione=report" <% if(request.getParameter("azione") != null && request.getParameter("azione").equals("visualizzaConsuntivi")){ out.print("id='on'");} %>>Report</a></li>
				<li><a href="./editorLogin.jsp">Editor</a></li>
			</ul>
		</div>
	</div>
</div>