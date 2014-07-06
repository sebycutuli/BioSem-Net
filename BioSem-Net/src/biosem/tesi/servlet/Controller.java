package biosem.tesi.servlet;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;

import biosem.tesi.db.dao.Dao;
import biosem.tesi.db.dao.DaoService;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Dao dao;
    private boolean noDescr = false;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
        super();
        this.dao = DaoService.getDao();
        // TODO Auto-generated constructor stub
    }   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String action = request.getParameter("action");		 
		 //System.out.println("action = "+action);
		
		if (action == null) {			  
			//System.out.println("parametro "+request.getParameter("correlazione"));
		    int numeroPersonaggi = this.dao.getSizePersonaggi();
		    request.setAttribute("numeroPersonaggi", numeroPersonaggi);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaPrincipale.jsp"); //dentro WEB-INF
			dispatcher.forward(request, response);
		}			
		
		else if (action.trim().equals("registraGET")){
			//System.out.println(action);
			//System.out.println("parametro "+request.getParameter("correlazione"));
			String nomePersonaggioCercato = request.getParameter("correlazione");
			request.setAttribute("nomePersonaggioCercato", nomePersonaggioCercato);
			int idPersonaggio = this.dao.find(nomePersonaggioCercato);
			Map<String, List<String>> dati_attributi = new LinkedHashMap<String, List<String>>();
			List<String> listaPersonaggiCorrelati = this.dao.checkCorrelazione(idPersonaggio);
			//int idPersonaggio = this.dao.find(nomePersonaggioCercato);
			//System.out.println("size listaPersonaggiCorrelati "+listaPersonaggiCorrelati.size());
			request.setAttribute("listaPersonaggiCorrelati", listaPersonaggiCorrelati);	 
	        dati_attributi=this.dao.estraiDatidelPersonaggio(idPersonaggio);
	        request.setAttribute("dati_attributi", dati_attributi);	 
	        
	        String urlImage = this.getUrlImage(nomePersonaggioCercato);
	        request.setAttribute("urlImmagine", urlImage);	
	        
	        List<String> listUrlImages = this.getListUrlImages(listaPersonaggiCorrelati);
	        request.setAttribute("listUrlImages", listUrlImages);		        
			System.out.println("size lista url dei personaggi correlati: "+listUrlImages.size());
			
			//CONTROLLARE SE DATI_ATTRIBUTI.SIZE()>2 PER COSTRUIRE UNA DEFINIZIONE!
	        if (dati_attributi.size()<4){
	        	noDescr = true;
	        	request.setAttribute("noDescr", noDescr);
	        	//request.setAttribute("dati_attributi", dati_attributi);	  
	        	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/formAltraRicerca.jsp"); //dentro WEB-INF
				dispatcher.forward(request, response);
	        }
	        else {
		 	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view.jsp"); //dentro WEB-INF
			dispatcher.forward(request, response);
	        }
			//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/login.jsp"); //dentro WEB-INF
			//dispatcher.forward(request, response);
		}
		
		else if (action.equals("option_personaggi")){			
		
		//System.out.println("option_personaggi riceve "+ request.getParameter("maths"));
		//request.setCharacterEncoding("UTF-8");	
		//String nome = new String(request.getParameter("maths").getBytes("iso-8859-1"), "UTF-8");
		request.setAttribute("nomePersonaggioCercato", request.getParameter("maths"));		
		
		Map<String, List<String>> dati_attributi = new LinkedHashMap<String, List<String>>();
	    int idPersonaggio = this.dao.find(request.getParameter("maths"));
		//int idPersonaggio = this.dao.checkExistPersonaggio(nome);
		//System.out.println("ID "+idPersonaggio);
	    dati_attributi=this.dao.estraiDatidelPersonaggio(idPersonaggio);
	    request.setAttribute("dati_attributi", dati_attributi);
	    List<String> listaPersonaggiCorrelati = this.dao.checkCorrelazione(idPersonaggio);
		//int idPersonaggio = this.dao.find(nomePersonaggioCercato);
		//System.out.println("size listaPersonaggiCorrelati "+listaPersonaggiCorrelati.size());
		request.setAttribute("listaPersonaggiCorrelati", listaPersonaggiCorrelati);	 
		
		String urlImage = this.getUrlImage(request.getParameter("maths"));
        request.setAttribute("urlImmagine", urlImage);	
        
        List<String> listUrlImages = this.getListUrlImages(listaPersonaggiCorrelati);
        request.setAttribute("listUrlImages", listUrlImages);	
        //System.out.println("size lista url dei personaggi correlati: "+listUrlImages.size());
        
		    if (dati_attributi.size()<4){
		    	noDescr = true;
		    	request.setAttribute("noDescr", noDescr);
		    	//request.setAttribute("dati_attributi", dati_attributi);	  
		    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/formAltraRicerca.jsp"); //dentro WEB-INF
				dispatcher.forward(request, response);
		    }
		    else{
		    	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view.jsp"); //dentro WEB-INF
		    	dispatcher.forward(request, response);
		    }
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");
		//System.out.println("action POST = "+action);
		//request.setCharacterEncoding("UTF-8");

		if (action.equals("registra")){	
			//request.setCharacterEncoding("UTF-8");	
			String nome = new String(request.getParameter("personaggio").getBytes("iso-8859-1"), "UTF-8");
			List<Integer> listaPersonaggi = this.dao.findAll(nome);
			//System.out.println("trovo "+this.dao.find(nome));
			//System.out.println("registra riceve "+nome+" con id "+this.dao.find(nome));
			if (listaPersonaggi.size() == 0 || request.getParameter("personaggio").equals("")) {
				//System.out.println("id non trovato");
				response.setContentType("text/html");
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/paginaPrincipale.jsp"); //dentro WEB-INF
				dispatcher.include(request, response);				
				PrintWriter out = response.getWriter();
				out.println("<br/>Spiacente, personaggio non trovato!");			
				} 
			else if (listaPersonaggi.size() == 1){
				//request.getSession().setAttribute("corsista", corsista);
				//this.setListCorsisti(request);
				//RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/elenco.jsp"); //dentro WEB-INF
				//dispatcher.forward(request, response);
				String nomePersonaggioCercato = this.dao.printPersonaggio(listaPersonaggi.get(0));
				//System.out.println("PRINTPERSONAGGIO: "+nomePersonaggioCercato);
				request.setAttribute("nomePersonaggioCercato", nomePersonaggioCercato);
				Map<String, List<String>> dati_attributi = new LinkedHashMap<String, List<String>>();
				List<String> listaPersonaggiCorrelati = this.dao.checkCorrelazione(listaPersonaggi.get(0));
				//int idPersonaggio = this.dao.find(nomePersonaggioCercato);
				//System.out.println("size listaPersonaggiCorrelati "+listaPersonaggiCorrelati.size());
				request.setAttribute("listaPersonaggiCorrelati", listaPersonaggiCorrelati);	 
		        dati_attributi=this.dao.estraiDatidelPersonaggio(listaPersonaggi.get(0));
		        request.setAttribute("dati_attributi", dati_attributi);	
		        
		        String urlImage = this.getUrlImage(nomePersonaggioCercato);
		        //URL urlImmagine = new URL(urlImage);
		        request.setAttribute("urlImmagine", urlImage);	
		        
				//System.out.println("URL "+urlImage);
		        
		        List<String> listUrlImages = this.getListUrlImages(listaPersonaggiCorrelati);
		        request.setAttribute("listUrlImages", listUrlImages);	
		        System.out.println("size lista url dei personaggi correlati: "+listUrlImages.size());
		        
				//CONTROLLARE SE DATI_ATTRIBUTI.SIZE()>2 PER COSTRUIRE UNA DEFINIZIONE!
		        if (dati_attributi.size()<4){
		        	noDescr = true;
		        	request.setAttribute("noDescr", noDescr);
		        	//request.setAttribute("dati_attributi", dati_attributi);	  
		        	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/formAltraRicerca.jsp"); //dentro WEB-INF
					dispatcher.forward(request, response);
		        }
		        else {
			 	RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/view.jsp"); //dentro WEB-INF
				dispatcher.forward(request, response);
		        }
			}
			
			else{
				List<String> personaggiAmbigui = new ArrayList<String>();
				for(int i=0; i<listaPersonaggi.size(); i++){
					personaggiAmbigui.add(this.dao.printPersonaggio(listaPersonaggi.get(i)));
					//System.out.println(this.dao.printPersonaggio(listaPersonaggi.get(i)));
				}
				List<String> listUrlImages = this.getListUrlImages(personaggiAmbigui);
		        request.setAttribute("listUrlImages", listUrlImages);	
				request.setAttribute("listaPersonaggiAmbigui", personaggiAmbigui);
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/disambiguazionePersonaggi.jsp"); //dentro WEB-INF
				dispatcher.forward(request, response);		
			}		     
		}
	}
	
	public String getUrlImage (String nomePersonaggio){
		
        String urlImage = new String ("");

		try {  
			  
            URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +  
                        "v=1.0&q="+nomePersonaggio.replace(" ", "%20")+"&key=ABQIAAAAMDidA1PAO0alsihAElsy3xTLCrE5uk8Ud_JrDKiWLKYeT0PD8xQ9hbFvmXJ2enaXdFRHJflbRAe36A&userip=192.168.1.51");  
  
            URLConnection connection = url.openConnection();  
            connection.addRequestProperty("Referer","http://www.defekas.com");  
  
            String line; 
            StringBuilder builder = new StringBuilder();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
              
            while((line = reader.readLine()) != null) {  
                builder.append(line);  
            }                
            JSONObject json = new JSONObject(builder.toString());
            JSONObject responseData = json.getJSONObject("responseData");
        	JSONArray jsonArray = responseData.getJSONArray("results");
        	if ((jsonArray != null) && (!jsonArray.isNull(0))) {
        	    JSONObject result0 = (JSONObject) jsonArray.get(0);
        	    urlImage = (String) result0.get("url");
        	    //System.out.println(urlImage);
        	} else {
        	    System.out.println("Image Not Found");
        	}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
			return urlImage;
	}
	
	public List<String> getListUrlImages (List<String> listaPersonaggi){
		
		List<String> listUrlImages = new ArrayList<String>();	
        for(String nomePersonaggio : listaPersonaggi){
			String urlImage = new String ("");
			try {  
				  
	            URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +  
	                        "v=1.0&q="+nomePersonaggio.replace(" ", "%20")+"&key=ABQIAAAAMDidA1PAO0alsihAElsy3xTLCrE5uk8Ud_JrDKiWLKYeT0PD8xQ9hbFvmXJ2enaXdFRHJflbRAe36A&userip=192.168.1.51");  
	  
	            URLConnection connection = url.openConnection();  
	            connection.addRequestProperty("Referer","http://www.defekas.com");  
	  
	            String line; 
	            StringBuilder builder = new StringBuilder();  
	            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
	              
	            while((line = reader.readLine()) != null) {  
	                builder.append(line);  
	            }                
	            JSONObject json = new JSONObject(builder.toString());
	            JSONObject responseData = json.getJSONObject("responseData");
	        	JSONArray jsonArray = responseData.getJSONArray("results");
	        	if ((jsonArray != null) && (!jsonArray.isNull(0))) {
	        	    JSONObject result0 = (JSONObject) jsonArray.get(0);
	        	    urlImage = (String) result0.get("url");
	        	    //System.out.println(urlImage);
	        	    listUrlImages.add(urlImage);        	    
	        	} else {
	        	    System.out.println("Image Not Found");
	        	}
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		return listUrlImages;
	}
}