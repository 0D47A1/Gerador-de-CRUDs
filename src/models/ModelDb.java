package models;

 
import java.util.ArrayList;

 

public class ModelDb {
	private String DbName;
	public ArrayList<ModelTabela> Tabelas = new ArrayList<ModelTabela>();
	
	public String getDbName() {
		return DbName;
	}

	public void setDbName(String dbName) {
		DbName = dbName;
	}

	

	

	
	
}
