package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import models.ModelColuna;
import models.ModelDb;
import models.ModelTabela;

public class GerarClasses {
	public GerarClasses(ModelDb data) {
		
		MainController.Var.ger_view_log.appendText("-> Gerando o CRUD para o banco "+data.getDbName()+"\n");
		
		for(ModelTabela tabela : data.Tabelas) {
			 
			 
			 try {
				 
				 new File("CRUD/"+tabela.getTableName()).mkdirs();
				 FileWriter file_write = new FileWriter("CRUD/"+tabela.getTableName()+"/"+tabela.getTableName()+".java");
				 MainController.Var.ger_view_log.appendText("->   Gerando o arquivo "+tabela.getTableName()+".java\n");
				 file_write.write("public class "+tabela.getTableName()+"(){\n\n");
				 
				 for(ModelColuna coluna : tabela.Coluna) {
						file_write.write("	private "+coluna.getTipo()+" "+coluna.getNome()+"; \n");
				 }
				 
				 file_write.write("\n");
				 for(ModelColuna coluna : tabela.Coluna) {
						file_write.write("	public "+coluna.getTipo()+" get"+coluna.getNome()+"(){ \n");   
			    		file_write.write("		return this."+coluna.getNome()+"; \n");
			    		file_write.write("  	}\n");
			    		
			    		file_write.write("	public void set"+coluna.getNome()+"("+coluna.getTipo()+" value){ \n");   
			    		file_write.write("		this."+coluna.getNome()+" = value; \n");
			    		file_write.write("  	}\n");
			    		file_write.write("\n");
				 }
				 
				 file_write.write("}");
				 file_write.close();
			 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
