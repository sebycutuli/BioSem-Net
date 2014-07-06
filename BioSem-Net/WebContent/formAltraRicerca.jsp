<%-- <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 --%>
 <%@ page import="biosem.tesi.db.*"  %> 
<%@page import="java.util.*" %>  
<%@ page language="java" contentType="text/html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>Visualizzazione Personaggio</title>
</head>
<body>
<% //String nome = (String) request.getParameter("nomePersonaggioCercato");
   request.setCharacterEncoding("UTF-8");
%>
<%  Map<String, List<String>> dati_attributi = (Map<String, List<String>>) request.getAttribute("dati_attributi"); %> 
<%  String nomePersonaggio = (String) request.getAttribute("nomePersonaggioCercato"); %> 
 
 <% if(dati_attributi.size()>1){ 
 	String nome = dati_attributi.get("Nome").get(0);
 	String cognome = dati_attributi.get("Cognome").get(0);
 	out.write("<h4><br>Hai cercato "+nome+" "+cognome+", ma non esiste una descrizione appropriata per questo personaggio!</h4>");
    }
	 else out.write("<h4><br>Hai cercato "+nomePersonaggio+", ma non esiste una descrizione appropriata per questo personaggio!</h4>");
 	%>	
	 <br>
	 <br>
	 Fai un'altra ricerca
	 <form method="post" action="?action=registra">				
			<input type="text" name="personaggio" size="20px">
			<input type="submit" value="Cerca">	
			<input type="reset" value="Reset">	
	</form>		
</body>
</html>