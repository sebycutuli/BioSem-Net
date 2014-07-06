package biosem.tesi.db.dao;

import java.util.List;
import java.util.Map;

public interface Dao {	
	public int find(String c);
	public int getSizePersonaggi();
	public String printPersonaggio(int c);
	public List<Integer> findAll(String c);
	public Map<String, List<String>> estraiDatidelPersonaggio(int c);
	public int checkExistPersonaggio(String c);
	public List<String> checkCorrelazione(int c);
}
