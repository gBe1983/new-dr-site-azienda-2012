<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	HttpSession controlloUtenteLoggato = request.getSession();
	if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
%>

<div class="subtitle ">
	<h2>Area Report</h2>
</div>
<p>
	In questa sezione potrete gestire tutto quello ha a che fare con il Report dei dipendenti.
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