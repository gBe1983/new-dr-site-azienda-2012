<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>
<%@page import="it.azienda.dto.RisorsaDTO"%>

<select name="idRisorsa" id="idRisorsa">
	<option value="" selected="selected">-- Seleziona la Risorsa --</option>
<%
	if(request.getAttribute("listaRisorse") != null){
		ArrayList listaRisorsa = (ArrayList) request.getAttribute("listaRisorse");
		for(int x = 0; x < listaRisorsa.size(); x++){
			RisorsaDTO risorsa = ((RisorsaDTO) listaRisorsa.get(x));
%>
			<option value="<%=risorsa.getIdRisorsa() %>"><%=risorsa.getCognome() + " " + risorsa.getNome() %></option>
<%
		}
	}

%>
</select>