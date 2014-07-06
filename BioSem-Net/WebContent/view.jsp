<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="biosem.tesi.db.*"  %> 
<%@page import="java.util.*" %>
<%@page import="java.net.URLConnection" %>
<%@page import="java.net.URL" %>
<%@page import="java.net.HttpURLConnection" %>

  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" pageEncoding="UTF-8">
<title>Visualizzazione Personaggio</title>
</head>
<body>
<%-- <%out.print("<h3> Hai cercato: "+request.getParameter("personaggio")+"</h3>"); %> --%>
<% String nome = (String) request.getAttribute("nomePersonaggioCercato"); 
%>

<% String urlImmagine = (String) request.getAttribute("urlImmagine"); 
   String urlImage ="\""+urlImmagine+"\"";
   //URL url = new URL(urlImmagine);
   //HttpURLConnection urlC = (HttpURLConnection) url.openConnection(); 
   //urlC.addRequestProperty("Referer","http://www.defekas.com");  
   //urlC.setRequestProperty("User-Agent", "Mozilla/4.76"); 
%>
<img src=<%=urlImage%> width="300" height="300" alt="Immagine non trovata"/>
<%-- <% out.print("URL: "+urlImage); %> --%>
<br>
<br>
<% Map<String, List<String>> dati_attributi = (Map<String, List<String>>) request.getAttribute("dati_attributi"); %>
<% boolean noDescr = (Boolean) request.getAttribute("noDescr"); %>

<%Set<String> keySet = dati_attributi.keySet();
  String definizione = new String("");
  boolean sessoMaschile = false;
  boolean titolo = false;
  boolean doppiaBio = false;
  String attivitàAltreSecondaBio = new String("");
  String attività2SecondaBio = new String("");
  String attivitàSecondaBio = new String("");
  String nome2 = new String("");
  String cognome2 = new String("");
  String luogoNascita2 = new String("");
  String giornoMeseNascita2 = new String("");
  String annoNascita2 = new String("");
  String luogoMorte2 = new String("");
  String giornoMeseMorte2 = new String("");
  String annoMorte2 = new String("");
  String nazionalità2 = new String("");
  String fineIncipit2 = new String("");
  String altraDescrizione = new String("");  
 
/*  for(Object key:keySet){
    String value = dati_attributi.get(key).get(0);		     
    System.out.println(key+" ---> "+value);
}  */

if(keySet.contains("Titolo")){
	definizione = dati_attributi.get("Titolo").get(0)+" "+definizione;
	titolo = true;
	//System.out.println("il personaggio cercato ha titolo: "+dati_attributi.get("Titolo").get(0));
}

if(keySet.contains("Nome")){
	if(titolo)
		definizione = definizione+" "+dati_attributi.get("Nome").get(0);
	else definizione = dati_attributi.get("Nome").get(0);
		if(dati_attributi.get("Nome").size()>1){
			doppiaBio = true;
			nome2 = dati_attributi.get("Nome").get(1);
		}
}

if(keySet.contains("Cognome")){
	definizione = definizione+" "+dati_attributi.get("Cognome").get(0);
	if (doppiaBio && dati_attributi.get("Cognome").size()>1){
		cognome2 = dati_attributi.get("Cognome").get(1);
	}
}

if(keySet.contains("carica"))
	definizione = definizione+" ("+dati_attributi.get("carica").get(0)+")";

if(keySet.contains("Sesso") && dati_attributi.get("Sesso").get(0).equals("M"))
	sessoMaschile=true;

	
if(keySet.contains("LuogoNascita")){
	if(sessoMaschile){
		definizione = definizione+" nato a "+dati_attributi.get("LuogoNascita").get(0);		
	}
		else definizione = definizione+" nata a "+dati_attributi.get("LuogoNascita").get(0);
	if (doppiaBio && dati_attributi.get("LuogoNascita").size()>1){
		luogoNascita2 = dati_attributi.get("LuogoNascita").get(1);
	}
}
else if (sessoMaschile){
	if(keySet.contains("LuogoNascita") || keySet.contains("GiornoMeseNascita") || keySet.contains("AnnoNascita"))
		definizione = definizione+" nato nel";
	else if(!sessoMaschile && (keySet.contains("LuogoNascita") || keySet.contains("GiornoMeseNascita") || keySet.contains("AnnoNascita")))
		definizione = definizione+" nata nel";
}

if(keySet.contains("GiornoMeseNascita")){
	if(keySet.contains("LuogoNascita"))
	   definizione = definizione+" il "+dati_attributi.get("GiornoMeseNascita").get(0);
	  else definizione = definizione+" "+dati_attributi.get("GiornoMeseNascita").get(0);
	if (doppiaBio && dati_attributi.get("GiornoMeseNascita").size()>1){
		giornoMeseNascita2 = dati_attributi.get("GiornoMeseNascita").get(1);
	}
}
		
if(keySet.contains("AnnoNascita")){
	definizione = definizione+" "+dati_attributi.get("AnnoNascita").get(0);
	if (doppiaBio && dati_attributi.get("AnnoNascita").size()>1){
		annoNascita2 = dati_attributi.get("AnnoNascita").get(1);
	}
}
		
if(keySet.contains("LuogoMorte") && !dati_attributi.get("LuogoMorte").get(0).equals("?")){
	if(sessoMaschile)
	  definizione = definizione+" e morto a "+dati_attributi.get("LuogoMorte").get(0);
	else definizione = definizione+" e morta a "+dati_attributi.get("LuogoMorte").get(0);
	if (doppiaBio && dati_attributi.get("LuogoMorte").size()>1){
		luogoMorte2 = dati_attributi.get("LuogoMorte").get(1);
	}
}

if(!keySet.contains("LuogoMorte") && 
	((keySet.contains("GiornoMeseMorte") && !dati_attributi.get("GiornoMeseMorte").get(0).equals("?"))  || (keySet.contains("AnnoMorte") && !dati_attributi.get("AnnoMorte").get(0).equals("?")))){
		if(sessoMaschile)
		   definizione = definizione+" e morto ";
			else definizione = definizione+" e morta ";
}

if(keySet.contains("GiornoMeseMorte") && !dati_attributi.get("GiornoMeseMorte").get(0).equals("?")){
	definizione = definizione+" il "+dati_attributi.get("GiornoMeseMorte").get(0);
	if (doppiaBio && dati_attributi.get("GiornoMeseMorte").size()>1){
		giornoMeseMorte2 = dati_attributi.get("GiornoMeseMorte").get(1);
	}
}

if(keySet.contains("AnnoMorte") && !dati_attributi.get("AnnoMorte").get(0).equals("?")){
	definizione = definizione+" "+dati_attributi.get("AnnoMorte").get(0);
	if (doppiaBio && dati_attributi.get("AnnoMorte").size()>1){
		annoMorte2 = dati_attributi.get("AnnoMorte").get(1);
	}
}
		
if(keySet.contains("NoteMorte") && !dati_attributi.get("NoteMorte").get(0).equals("?"))
	definizione = definizione+" "+dati_attributi.get("NoteMorte").get(0);
		
if(keySet.contains("Attività")){
	definizione = definizione+" "+dati_attributi.get("Attività").get(0);
	if (doppiaBio && dati_attributi.get("Attività").size()>1){
		attivitàSecondaBio = dati_attributi.get("Attività").get(1);
	}
}

if(keySet.contains("Attività2")){
	definizione = definizione+", "+dati_attributi.get("Attività2").get(0);
	if (doppiaBio && dati_attributi.get("Attività2").size()>1){
	    attività2SecondaBio = dati_attributi.get("Attività2").get(1);
	}
}
		
if(keySet.contains("AttivitaAltre")){
	definizione = definizione+" "+dati_attributi.get("AttivitaAltre").get(0);
	if (doppiaBio && dati_attributi.get("AttivitaAltre").size()>1){
		attivitàAltreSecondaBio = dati_attributi.get("AttivitaAltre").get(1);
	}
}

if(keySet.contains("Nazionalità")){
	definizione = definizione+" "+dati_attributi.get("Nazionalità").get(0);
	if (doppiaBio && dati_attributi.get("Nazionalità").size()>1){
		nazionalità2 = dati_attributi.get("Nazionalità").get(1);
	}
}
		
if(keySet.contains("FineIncipit")){
	definizione = definizione+" "+dati_attributi.get("FineIncipit").get(0);
	if (doppiaBio && dati_attributi.get("FineIncipit").size()>1){
		fineIncipit2 = dati_attributi.get("FineIncipit").get(1);
	}
}

if(keySet.contains("PostNazionalità"))
	definizione = definizione+" "+dati_attributi.get("PostNazionalità").get(0);

if(keySet.contains("altra descrizione") && !doppiaBio){
	definizione = definizione+".<BR>"+dati_attributi.get("altra descrizione").get(0);
}
 else if(keySet.contains("altra descrizione") && doppiaBio){
	altraDescrizione = dati_attributi.get("altra descrizione").get(0);
 }

if(keySet.contains("nome_onorificenza"))
	definizione = definizione+".<BR>"+dati_attributi.get("nome_onorificenza").get(0);

//System.out.println(definizione);
if(!doppiaBio){
	definizione = definizione.trim();
	if(definizione.charAt(definizione.length()-1)!='.')
		definizione = definizione+"."; 
	out.println(definizione.replace(" ,",","));	
}

else{
	definizione = definizione+" "+nome2+" "+cognome2+" ("+luogoNascita2+" "+giornoMeseNascita2+ " "+annoNascita2+" - "+luogoMorte2+ " "+giornoMeseMorte2+" "+annoMorte2
			+") "+attivitàSecondaBio+", "+attività2SecondaBio+" "+attivitàAltreSecondaBio+" "+nazionalità2
			+" "+fineIncipit2+".";
	definizione = definizione.trim();
	if(definizione.charAt(definizione.length()-1)!='.')
		definizione = definizione+".";
	out.println(definizione.replace(" ,",",")+"<BR>"+altraDescrizione);
}
%>	 
 <br>
 <br>
 <br>
 <br>
 <% List<String> listaPersonaggiCorrelati = (List<String>) request.getAttribute("listaPersonaggiCorrelati"); %>
 <% List<String> listUrlImages = (List<String>) request.getAttribute("listUrlImages"); %>
 <%if(listaPersonaggiCorrelati.size()>0)
  out.write("<i>Forse potrebbe interessarti anche:</i><br><br>");
  int index = 0;
  for(String correlazione: listaPersonaggiCorrelati){
	  String urlCurrentImage ="\""+listUrlImages.get(index)+"\"";
 %>
  <img src=<%=urlCurrentImage%> width="50" height="50"/>
  <a href="http://localhost:8080/BioSem-Net?action=registraGET&correlazione=<%=correlazione%>"><%=correlazione%></a><div></div>
  <br>
 <%
 index++;
 }%> 
 <br>
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