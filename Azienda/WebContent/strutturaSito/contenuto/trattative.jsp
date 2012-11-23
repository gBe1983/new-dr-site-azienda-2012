<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Area Trattative</h2>
</div>
<p>
	In questa sezione potrete gestire tutto quello che ha a che fare con la Trattative, a partire dall'inserimento con
	a seguire la sua ricerca.
</p>

<%
	}else{
%>	
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
	}
%>