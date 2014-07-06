package biosem.tesi.db.dao;

public class DaoService {
	private static Dao dao;
	
	private DaoService(){}
	
	public static Dao getDao(){
		if( dao == null) dao = new DaoImpl();
		return dao;
	}

}
