package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.ModelColuna;
import models.ModelDb;
import models.ModelTabela;
import sun.swing.MenuItemLayoutHelper.ColumnAlignment;

public class GerarClasses {
	private String path;
	public GerarClasses(ModelDb data) {
		 
		MainController.Var.ger_view_log.appendText("-> Gerando o CRUD para o banco "+data.getDbName()+"\n");
		
		/* ***********************************
		 * 
		 * GERANDO CLASSES PARA CADA TABELA
		 * 
		 */
		 	File file = new File("CRUD");
		 	
	 	    
	 	   if(file.isDirectory()) {
	 		  String[]sublist = file.list(); // lista as sub pasta do diretorio CRUD
	 		 for(String s: sublist){
	 		     File subdirectory = new File(file.getPath(),s); // pecorre cada subpasta
	 		     
	 		     String[]arquivolist	= subdirectory.list(); // pega a lista de arquivos dessa subpasta
	 		    for(String b: arquivolist){
	 		    	File listdir = new File(subdirectory.getPath(),b); // pega cada arquivo;
	 		    	listdir.delete(); // e deleta
	 		    }
	 		     //System.out.println(sub2.getAbsolutePath());
	 		    subdirectory.delete(); // depoi apaga a subpasta de onde estava os arquivos
	 		 }
				 
			}else {
				file.mkdir();
				
			}
	 	   this.path = file.getAbsolutePath();
		
	    	for(ModelTabela tabela : data.Tabelas) {
				 
				 
				 try {
					 
						File file1 = new File("CRUD/"+tabela.getTableName());
						if(file1.isDirectory()) {
							file1.delete();
						}else {
							file1.mkdir();
						}
					 
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
					File file1 = new File("CRUD/"+tabela.getTableName());
					
						
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
	    	
	   	 /* ***********************************
			 * 
			 * GERANDO EXEMPLOS PARA CADA TABELA
			 * 
			 */
	    	
	    	
	    	for(ModelTabela tabela : data.Tabelas) {
	    		
				try {
					File file1 = new File("CRUD/"+tabela.getTableName());
					if(file1.isDirectory()) {
						file1.delete();
					}else {
						file1.mkdir();
					}
					FileWriter file_write = new FileWriter("CRUD/"+tabela.getTableName()+"/"+tabela.getTableName()+"Exemplo.java");
					MainController.Var.ger_view_log.appendText("          -> Gerando o arquivo de exemplo "+tabela.getTableName()+"Exemplo.java\n");
					file_write.write("public class "+tabela.getTableName()+"Exemplo(){\n\n");
			        
			         
			        file_write.write("			public "+tabela.getTableName()+"Exemplo() {\n\n");
			        file_write.write("				"+tabela.getTableName()+"DAO "+MainController.firstLowerCase(tabela.getTableName())+"Dao = new "+tabela.getTableName()+"DAO();\n");
			        file_write.write("			try {\n");
			        file_write.write("					"+tabela.getTableName()+" "+MainController.firstLowerCase(tabela.getTableName())+" = new "+tabela.getTableName()+"();\n");
			        for(ModelColuna colula : tabela.Coluna) {
			        	file_write.write("					"+MainController.firstLowerCase(tabela.getTableName())+".set"+MainController.firstUpperCase(colula.getNome())+"(\"valor\");\n");
			        }
			        
			        file_write.write("\n					"+MainController.firstLowerCase(tabela.getTableName())+"Dao.iserir("+MainController.firstLowerCase(tabela.getTableName())+");\n");
			        file_write.write("\n					"+MainController.firstLowerCase(tabela.getTableName())+"Dao.alterar("+MainController.firstLowerCase(tabela.getTableName())+");\n");
			        file_write.write("\n					"+MainController.firstLowerCase(tabela.getTableName())+"Dao.deletar("+MainController.firstLowerCase(tabela.getTableName())+".get"+tabela.Coluna.get(0).getNome()+"());\n");
			        file_write.write("\n					ArrayList<"+tabela.getTableName()+"> list = "+MainController.firstLowerCase(tabela.getTableName())+"Dao.selecionarTodos();\n");
			        file_write.write("			} catch (SQLException e) {\n 					e.printStackTrace();\n			}\n");
			        
			        file_write.write("        }\n");
			        file_write.write("}");
			        file_write.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
	    		
	    		
	    	}
	    	
	    	 
	    	MainController.Var.ger_view_log.appendText("-> Arquivos gerados em "+this.path+"\n");
	    	Desktop desktop = Desktop.getDesktop();
	    			try {
						desktop.open(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    	
		       
		  
	
	}
}
