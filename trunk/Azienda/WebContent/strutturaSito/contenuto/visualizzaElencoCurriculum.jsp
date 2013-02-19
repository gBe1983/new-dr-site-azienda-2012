<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.CurriculumDTO"%>  

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	
	ArrayList<CurriculumDTO> listaCurriculum = (ArrayList<CurriculumDTO>) request.getAttribute("listaCurriculum");
%>
	
	<div class="subtitle ">Elenco Curriculum Vitae</div>

	<div class="spazioUltra" align="center">
	<form action="./GestioneCurriculum" method="post">
		<input type="hidden" name="azione" value="<%=request.getParameter("dispositiva") %>">
		<select name="parametro">
<%
	for(int x = 0; x < listaCurriculum.size(); x++){
%>		
			<option value="<%=((CurriculumDTO)listaCurriculum.get(x)).getId_risorsa() %>"><%=((CurriculumDTO)listaCurriculum.get(x)).getNominativo() %></option>
<%	
	}
%>
		</select><br><br>
		<input type="submit" value="anteprima">
	</form>
	</div>
<%
}else{	
%>
	<script type="text/javascript">
		controlloSessioneAttiva();
	</script>
<%
}
%>