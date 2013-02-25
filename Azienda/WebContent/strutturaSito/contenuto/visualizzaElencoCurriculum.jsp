<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.CurriculumDTO"%>  

<%
HttpSession controlloUtenteLoggato = request.getSession();
if(controlloUtenteLoggato.getAttribute("utenteLoggato") != null){
	
	ArrayList<CurriculumDTO> listaCurriculum = (ArrayList<CurriculumDTO>) request.getAttribute("listaCurriculum");
%>
	
	<div id="finestra" title="Esporta Pdf">
		<%@include file="esportaPdf.jsp" %>
	</div>

	<div class="subtitle ">Elenco Curriculum Vitae</div>

	<div class="spazioUltra" align="center">
	<form action="./GestioneCurriculum" method="post" name="sceltaRisorsa">
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
		<%
			if(request.getParameter("dispositiva").equals("esportaPdf")){
		%>
				<input type="submit" value="esporta" onclick="return openFinestra(sceltaRisorsaCurriculum(),'<%=request.getParameter("azione") %>','all')">
		<%
			}else{
		%>
				<input type="submit" value="anteprima">
		<%	
			}
		
		%>
		
	</form>
	</div>
<%
	if(request.getAttribute("esitoInvioEmail") != null){
		if(Boolean.parseBoolean(request.getAttribute("esitoInvioEmail").toString())){
%>	
			<script type="text/javascript">alert("L'invio dell'email con l'allegato il Curriculum Vitae � avvenuta correttamente"); </script>
<%		
		}else{
%>
			<script type="text/javascript">alert("Impossibile inviare l'email con l'allegato il Curriculum Vitae. Contattare l'amministratore.");</script>
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