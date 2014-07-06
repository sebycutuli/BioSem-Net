<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*" %>
<%@page import="biosem.tesi.db.dao.Dao" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Pagina di disambiguazione</title>
</head>
<body>
<% List<String> listaPersonaggiAmbigui = (List<String>) request.getAttribute("listaPersonaggiAmbigui"); %>
<% List<String> listUrlImages = (List<String>) request.getAttribute("listUrlImages"); %>
<h3> La tua ricerca ha prodotto <%=listaPersonaggiAmbigui.size()%> risultati!</h3>
<%
int index = 0;
for(String nome : listaPersonaggiAmbigui){
String urlCurrentImage ="\""+listUrlImages.get(index)+"\"";
%>	
<img src=<%=urlCurrentImage%> width="80" height="80"/>
<a href="http://localhost:8080/BioSem-Net?action=option_personaggi&maths=<%=nome%>"><%=nome%></a><div></div>
<%
index++;
}%>   	
</body>
</html>