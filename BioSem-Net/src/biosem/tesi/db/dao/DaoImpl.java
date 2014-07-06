package biosem.tesi.db.dao;

//import it.ricercaPersonaggio.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.Statement;

public class DaoImpl implements Dao{
	
	//costruttore che carica i driver
	public DaoImpl() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	//ritorna l'oggetto connection
	private Connection getConnection(){
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/altripersonaggi?characterEncoding=utf8", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}	
	
	public int getSizePersonaggi(){
		 Connection conn = this.getConnection();
		 List<String> listaPersonaggiBio = new ArrayList<String>();		
		 PreparedStatement stmt = null;
		 ResultSet rs;
		 //String query = null;
		 
		try {
			 stmt = conn.prepareStatement("SELECT NOME FROM personaggi");

			 //query = "SELECT NOME FROM personaggi";
			 //String queryEnc = new String(query.getBytes("UTF-8"), "UTF-8");

			 rs = stmt.executeQuery();
			 while (rs.next()) {
	   		  listaPersonaggiBio.add(new String(rs.getString("NOME")));
			 }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		finally {
			
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return listaPersonaggiBio.size();
	}
	
	public List<Integer> findAll(String nomePersonaggio){
		Connection conn = this.getConnection();
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		//String SQL_SEARCH = null;
		List<Integer> listaPersonaggi = new ArrayList<Integer>();		
		
		if( conn != null){
			try {
				//String nome = new String(nomePersonaggio.getBytes("iso-8859-1"), "UTF-8");
				//System.out.println("findAll riceve : "+nome);
				stmt = conn.prepareStatement("SELECT id FROM personaggi WHERE nome LIKE ?");
				stmt.setString(1, "%"+nomePersonaggio+"%");
				//SQL_SEARCH = "SELECT id FROM personaggi WHERE nome LIKE \"%"+nomePersonaggio+"%\"";
				//String queryEnc = new String(SQL_SEARCH.getBytes("UTF-8"), "UTF-8");
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					listaPersonaggi.add(Integer.parseInt(rs.getString("ID")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			finally {
				
				try {
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return listaPersonaggi;
	}
	
	public int find(String nome){
		Connection conn = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		//String SQL_SEARCH = null;
		int id=0;
		
		if( conn != null){
			try {
				String nomePersonaggio = new String(nome.getBytes("UTF-8"), "UTF-8");
				stmt = conn.prepareStatement("SELECT id FROM personaggi WHERE nome = ?");
				stmt.setString(1, nomePersonaggio);
				//System.out.println("FIND RICEVE "+nomePersonaggio);
				//SQL_SEARCH = "SELECT id FROM personaggi WHERE nome = \""+nome+"\"";
				//String queryEnc = new String(SQL_SEARCH.getBytes("UTF-8"), "UTF-8");
				rs = stmt.executeQuery();   		
				
				while (rs.next()) {
					id = Integer.parseInt(rs.getString("ID"));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					rs.close();
					stmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return id;
	}
	
	public int checkExistPersonaggio(String nome){		
		Connection conn = this.getConnection();
	    try {
	    	String query = new String(nome.getBytes("UTF-8"), "UTF-8");
			System.out.println("checkExistPersonaggio riceve "+query);
	    	PreparedStatement stmt = conn.prepareStatement("SELECT id FROM personaggi WHERE nome LIKE ?");  
	    	stmt.setString(1, "%"+nome+"%");
	       	ResultSet rs = stmt.executeQuery();
	       	
			 while (rs.next()) {
				 int id = Integer.parseInt(rs.getString("id"));
				 return id;
			 }
				 
	    }catch (Exception e) { 
	    	e.printStackTrace(); 
	    	e.getMessage(); 
	     }   
	    
	    return -1;
	}
	
	/*public List<String> checkCorrelazione(int idPersonaggio){		
		Connection conn = this.getConnection();
		List<String> listaPersonaggiCorrelati = new ArrayList<String>();	
		
	    try {
	    	Statement stmt = (Statement) conn.createStatement();
	    	//select id_personaggio_correlato from relazioni where id_personaggio=2456434 group by ID_PERSONAGGIO_CORRELATO order by count(*) desc
	    	ResultSet rs = stmt.executeQuery("select id_personaggio_correlato from relazioni where id_personaggio=\""+idPersonaggio+"\" and id_personaggio_correlato!=\""+idPersonaggio+"\" group by ID_PERSONAGGIO_CORRELATO order by count(*) desc limit 0,10");   
	    	    	
			 while (rs.next()) {
				 String personaggio = this.printPersonaggio(Integer.parseInt(rs.getString("id_personaggio_correlato")));
				 listaPersonaggiCorrelati.add(personaggio);
			 }
				 
	    }catch (Exception e) { 
	    	e.printStackTrace(); 
	    	e.getMessage(); 
	     }   
	    return listaPersonaggiCorrelati;
	}*/
	
	public List<String> checkCorrelazione(int idPersonaggio){		
		Connection conn = this.getConnection();
		List<String> listaPersonaggiCorrelati = new ArrayList<String>();	
		
	    try {
	    	PreparedStatement stmt = conn.prepareStatement("select id_personaggio_correlato from relazioni where id_personaggio=? and id_personaggio_correlato!=? group by ID_PERSONAGGIO_CORRELATO order by count(*) desc limit 0,10");
	    	//select id_personaggio_correlato from relazioni where id_personaggio=2456434 group by ID_PERSONAGGIO_CORRELATO order by count(*) desc
	    	//ResultSet rs = stmt.executeQuery("select id_personaggio_correlato from relazioni where id_personaggio=\""+idPersonaggio+"\" and id_personaggio_correlato!=\""+idPersonaggio+"\" group by ID_PERSONAGGIO_CORRELATO order by count(*) desc limit 0,10");   
	    	stmt.setInt(1, idPersonaggio);
	    	stmt.setInt(2, idPersonaggio);
	    	ResultSet rs = stmt.executeQuery();
	    	
			 while (rs.next()) {
				 String personaggio = this.printPersonaggio(Integer.parseInt(rs.getString("id_personaggio_correlato")));
				 listaPersonaggiCorrelati.add(personaggio);
			 }
				 
	    }catch (Exception e) { 
	    	e.printStackTrace(); 
	    	e.getMessage(); 
	     }   
	    return listaPersonaggiCorrelati;
	}
	
	public Map<String, List<String>> estraiDatidelPersonaggio(int idPersonaggio){
		Map<String, List<String>> dati_attributi = new LinkedHashMap<String, List<String>>();
		Connection conn = this.getConnection();
		PreparedStatement stmt = null;
		ResultSet rs;
		//String query = null;
		
		try {
			 stmt = conn.prepareStatement("SELECT attributi.attributo, dati.dato FROM `dati` INNER JOIN `attributi` WHERE dati.ID_ATTRIBUTO=attributi.ID AND dati.ID_PERSONAGGIO = ?");
			 stmt.setInt(1, idPersonaggio);
			 //query = "SELECT attributi.attributo, dati.dato FROM `dati` INNER JOIN `attributi` WHERE dati.ID_ATTRIBUTO=attributi.ID AND dati.ID_PERSONAGGIO ="+idPersonaggio;
			 //String queryEnc = new String(query.getBytes("UTF-8"), "UTF-8");
			 
			 rs = stmt.executeQuery();
			 while (rs.next()) {
				 List<String> currentValue = dati_attributi.get(rs.getString(1));
				 if (currentValue == null) {
				        currentValue = new ArrayList<String>();
				        dati_attributi.put(rs.getString(1), currentValue);
				 }
				 currentValue.add(rs.getString(2));
				 //dati_attributi.put(rs.getString(1), Arrays.asList(rs.getString(2)));
				 //System.out.println(rs.getString(1)+" "+Arrays.asList(rs.getString(2)));
			 }

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return dati_attributi;
	}
	
	public String printPersonaggio(int id_personaggio){
		 Connection conn = this.getConnection();
		 PreparedStatement stmt = null;
		 ResultSet rs;
		 //String query = null;
		 String nomePersonaggio = new String("");
		 String queryEnc2 = new String("");
		 
		try {
			 stmt = conn.prepareStatement("SELECT nome FROM PERSONAGGI WHERE id= ?");
			 stmt.setInt(1, id_personaggio);
			 //query = "SELECT nome FROM PERSONAGGI WHERE id="+id_personaggio;
			// String queryEnc = new String(query.getBytes("UTF-8"), "UTF-8");
			 
			 rs = stmt.executeQuery();
			 while (rs.next()) {
				 nomePersonaggio=rs.getString("nome");
				 //System.out.println(rs.getString("nome"));
			 }
			 queryEnc2 = new String(nomePersonaggio.getBytes("UTF-8"), "UTF-8");

		} catch (Exception e) {
			e.printStackTrace();
		} 
		finally {
			
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("printPersonaggio: "+queryEnc2);
		return queryEnc2;
	}	
}
