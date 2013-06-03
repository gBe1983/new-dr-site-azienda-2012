<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>Dierre Consulting S.r.L.</title>

	<link rel="stylesheet" type="text/css" href="css/editor/editorStyle.css">
</head>
<body>

<form action="./GestioneAzienda" method="post">
	<input type="hidden" name="azione" value="loginEditor">
	<table id="login">
		<tr>
			<th colspan="2">Dierre Consulting S.r.L. Editor</th>			
		</tr>
		<tr>
			<td>Username: </td>
			<td><input type="text" name="username"></td>
		</tr>
		<tr>
			<td>Password: </td>
			<td><input type="password" name="password"></td>			
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="Login"></td>			
		</tr>
	</table>
</form>

<%
	if(request.getParameter("errore") != null){
		if(request.getParameter("errore").equals("loginErrato")){
%>
			<script type="text/javascript">
				alert("Username e/o Password errati");
			</script>
<%			
		}
	}
%>


</body>
</html>