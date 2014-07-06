<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, biosem.tesi.db.*"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Benvenuto</title>
</head>
<body>
<%  
	//int numeroPersonaggi = (Integer) request.getAttribute("numeroPersonaggi");
	//@SuppressWarnings("unchecked")
	Integer numeroPersonaggi = (Integer) request.getAttribute("numeroPersonaggi");
	if (numeroPersonaggi != null){
		out.print("<h2>Ci sono in totale <b>"+ numeroPersonaggi + "</b> personaggi</h2>");		
	}
	out.print("<h1>Benvenuto, cerca un personaggio</h1>");	
%>
	<form method="post" action="?action=registra">			
			 Inserisci il nome <br>
			<input type="text" name="personaggio" size="20px">
			<input type="submit" value="Cerca">	
			<input type="reset" value="Reset">	
	</form>		
</body>
</html>