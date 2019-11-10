package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.ModelColuna;
import models.ModelDb;
import models.ModelTabela;

public class GerarClasses {
	public GerarClasses(ModelDb data) {
		
		MainController.Var.ger_view_log.appendText("-> Gerando o CRUD para o banco "+data.getDbName()+"\n");
		
		/* ***********************************
		 * 
		 * GERANDO CLASSES PARA CADA TABELA
		 * 
		 */
		
		
	    	for(ModelTabela tabela : data.Tabelas) {
				 
				 
				 try {
					 
					 new File("CRUD/"+tabela.getTableName()).mkdirs();
				 
					 FileWriter file_write = new FileWriter("CRUD/"+tabela.getTableName()+"/"+tabela.getTableName()+".java");
					 MainController.Var.ger_view_log.appendText("          -> Gerando o arquivo "+tabela.getTableName()+".java\n");
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
		       
		
		
		
		
		 /* ***********************************
		 * 
		 * GERANDO DAO PARA CADA TABELA
		 * 
		 */
		
		
		
	    	for(ModelTabela tabela : data.Tabelas) {
				
				try {
					new File("CRUD/"+tabela.getTableName()).mkdirs();
					FileWriter file_write = new FileWriter("CRUD/"+tabela.getTableName()+"/"+tabela.getTableName()+"DAO.java");
					MainController.Var.ger_view_log.appendText("          -> Gerando o DAO "+tabela.getTableName()+"DAO.java\n");
			        file_write.write("public class "+tabela.getTableName()+"DAO(){\n\n");
			        
			        file_write.write("private Connection connection;\n\n");
			        file_write.write("public "+tabela.getTableName()+"DAO() throws SQLException{\n\n" + 
			        		"		this.connection = DriverManager.getConnection();\n\n" + 
			        		"	}\n");
			        
			        
			        /***************
			         * 
			         *  FUN플O INSERIR
			         *  
			         */
			        file_write.write(" public void inserir("+tabela.getTableName()+" valor) throws SQLException{\n\n"
			        				+"			PreparedStatement stmt = this.connection.prepareStatement(\"INSERT INTO "+MainController.firstLowerCase(tabela.getTableName())+"(");
			        
			        StringBuilder values = new StringBuilder();
			       
			        for(int i =0; i < tabela.Coluna.size(); i++) {
			        	 String virgula = " ";
			        	 if(i!=0) 
			        		 virgula = ", ";
			        	 
			        	 file_write.write(virgula+MainController.firstLowerCase(tabela.Coluna.get(i).getNome()));
			        	 values.append(virgula+"?");
			        }
			        file_write.write(")values("+values+")\");\n");
			        for(int i =0; i < tabela.Coluna.size(); i++) {		        	 
			        	 int j = i+1;
			        	 
			        	 file_write.write("			stmt.set"+MainController.lapidarTipo(tabela.Coluna.get(i).getTipo())+"("+j+", valor.get"+tabela.Coluna.get(i).getNome()+"());\n");
			        	  
			        }
			        
			        file_write.write("			stmt.execute();\n			stmt.close();\n");
			    		 
			        file_write.write(" }\n"); 
			        
			        /***************
			         * 
			         *  FUN플O ALTERAR
			         *  
			         */
			        
			        file_write.write(" public void alterar("+tabela.getTableName()+" valor) throws SQLException{\n\n");
			        file_write.write("			PreparedStatement stmt = this.connection.prepareStatement(\"UPDATE "+MainController.firstLowerCase(tabela.getTableName())+" SET");
			        		
			        StringBuilder stmtSet = new StringBuilder();
			        for(int i =0; i < tabela.Coluna.size(); i++) {		        	 
			        	
			        	 
			        	 if(i==0) {
			        		 file_write.write(" "+MainController.firstLowerCase(tabela.Coluna.get(i).getNome())+"=?"); 
			        	 }else {
			        		 file_write.write(", "+MainController.firstLowerCase(tabela.Coluna.get(i).getNome())+"=?"); 
			        	 }
			        	 int j = i+1;
			        	
			        	 stmtSet.append("			stmt.set"+MainController.lapidarTipo(tabela.Coluna.get(i).getTipo())+"("+j+", valor.get"+tabela.Coluna.get(i).getNome()+"());\n");
			        	 
			        }
			        file_write.write(" WHERE "+tabela.Coluna.get(0).getNome()+"=?\"");
			        
			   
			        file_write.write(")\n");
			        file_write.write(stmtSet.toString());
			        file_write.write("			stmt.executeUpdate();\n");
			        
			        file_write.write( " }\n"); 
			        
			        /***************
			         * 
			         *  FUN플O DELETAR
			         *  
			         */
			        file_write.write(" public void deletar("+MainController.lapidarTipo(tabela.Coluna.get(0).getTipo())+" valor) throws SQLException{\n\n");
			        file_write.write("			PreparedStatement stmt = this.connection.prepareStatement(\"DELETE FROM "+MainController.firstLowerCase(tabela.getTableName())+" WHERE "+tabela.Coluna.get(0).getNome()+" = ?\");\n");
			        file_write.write("			stmt.set"+MainController.lapidarTipo(tabela.Coluna.get(0).getTipo())+"(1, valor);\n");
			        file_write.write("			stmt.executeUpdate();\n");
			        file_write.write(" }\n");
			        
			        /***************
			         * 
			         *  FUN플O SELECIONAR TODOS
			         *  
			         */
			        file_write.write(" public ArrayList<"+tabela.getTableName()+"> selecionarTodos() throws SQLException{\n\n");
			        file_write.write("			ArrayList<"+tabela.getTableName()+"> list = new ArrayList<>();\n");
			        file_write.write("			PreparedStatement stmt = this.connection.prepareStatement(\"SELECT * FROM "+tabela.getTableName()+"\")\n");
			        file_write.write("			ResultSet resultSet = stmt.executeQuery();\n");
			        file_write.write("			while (resultSet.next()) {\n");
			        file_write.write("				"+tabela.getTableName()+" tabela = new "+tabela.getTableName()+"();\n");
			        for(ModelColuna coluna : tabela.Coluna) {
			        	file_write.write("				tabela.set"+coluna.getNome()+"(resultSet.get"+coluna.getTipo()+"(\""+MainController.firstLowerCase(coluna.getNome())+"\"));\n");
			        } 
			        file_write.write("\n				list.add(tabela);\n");
			        file_write.write("			}\n"); 
			        file_write.write("			return list;\n"); 
			        
			        file_write.write(" }\n"); 
					 
		    	 
                
			    	file_write.write("\n}");          		   
					file_write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		       
		  
	
	}
}
