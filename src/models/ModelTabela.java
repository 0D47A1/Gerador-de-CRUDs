package models;

import java.util.ArrayList;

 

public class ModelTabela{
	private String TableName;
	public ArrayList<ModelColuna> Coluna = new ArrayList<ModelColuna>();
	
	public String getTableName() {
		return TableName;
	}
	public void setTableName(String tableName) {
		TableName = tableName;
	}
	
}