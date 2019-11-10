package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import  java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.org.apache.xpath.internal.compiler.Compiler;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import models.ModelColuna;
import models.ModelDb;
import models.ModelTabela;

public class MainController extends Compiler implements Initializable{
   
	@FXML
    private JFXTextField ger_url;

    @FXML
    private JFXTextField ger_porta;

    @FXML
    private JFXTextField ger_usuario;

    @FXML
    private JFXPasswordField ger_senha;

    @FXML
    private JFXButton ger_btn_conectar;

    @FXML
    public JFXListView<String> ger_view_db;

    @FXML
    private JFXListView<Label> ger_view_table;

    @FXML
    private JFXListView<String> ger_view_colunas;

    @FXML
    public JFXTextArea ger_view_log;

    @FXML
    private JFXButton ger_view_gerar_codigo;
    
    static MainController Var;
    private DatabaseMetaData metadata;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	Var = this;
    	
    	ger_btn_conectar.setOnAction((action)->{
    		
    	
    		
    		Conexao conexao =   new Conexao(
    				ger_url.getText(),
    				ger_porta.getText(),
    				ger_usuario.getText(),
    				ger_senha.getText(),
    				ger_view_log);
    		Connection conn = conexao.get();
    		
    		if(conn!=null) {
    			
    			new Thread() {
    			     
    			    @Override
    			    public void run() {
    			    	try {
    			    		metadata = conn.getMetaData();
    	    				ResultSet rs = metadata.getCatalogs();
    	    				
    	    				ger_view_db.getItems().clear();
    	    				while(rs.next()) {   					
    	    					
    	    					ger_view_db.getItems().add(rs.getString(1).trim());
    							
    						}
    	    				
    	    			}catch(SQLException e) {
    	    				MainController.Var.ger_view_log.appendText("-> Não foi possível listar os bancos!\n");
    	    				System.out.println("Error exception: "+e.getMessage());
    	    			}
    			       
    			    }
    			  }.start(); 			
    			
    			
    		}
    		
    		
    		
    	
    	});
    	
    	ger_view_db.setOnMousePressed((action)->{
			
    		try {
				ResultSet tabelas = metadata.getTables(ger_view_db.getSelectionModel().getSelectedItem(), null, null, new String[]{"TABLE","VIEW"});
				ger_view_table.getItems().clear();
				ger_view_colunas.getItems().clear();
				while (tabelas.next()) {
					
					JFXCheckBox check = new JFXCheckBox();
					check.setSelected(true); 
					check.setId(tabelas.getString("TABLE_NAME"));
					
					Label label = new Label(tabelas.getString("TABLE_NAME"));
					label.setGraphic(check);
					label.setContentDisplay(ContentDisplay.LEFT);
					 
					ger_view_table.getItems().add(label);
					ger_view_gerar_codigo.disableProperty().set(false); // ativando o botão que gera as classes 
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
    		
			 
		});
    	
    	ger_view_table.setOnMousePressed((action)->{
			
    		try {
				ResultSet colunas = metadata.getColumns(ger_view_db.getSelectionModel().getSelectedItem().toString(), null, ger_view_table.getSelectionModel().getSelectedItem().getText(), null);
				ger_view_colunas.getItems().clear();
				while (colunas.next()) {
					ger_view_colunas.getItems().add(colunas.getString("COLUMN_NAME"));
					
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     
    		
			 
		});
    	
    	ger_view_gerar_codigo.setOnAction((action)->{
    		ModelDb db_model  = new ModelDb();
    		try {
    			
		   		
        		String db_name = ger_view_db.getSelectionModel().getSelectedItem(); 
        		ger_view_log.appendText("-> Capturando dados do banco "+db_name+"\n");
        		db_model.setDbName(db_name);
    		 
    			
    			ObservableList<Label> view_table = ger_view_table.getItems();
    			
    			for(int i =0; i < view_table.size(); i++ ) {
    				
    				Label label = view_table.get(i);    				
    				JFXCheckBox check = (JFXCheckBox) label.getGraphic();
    				
    				 
    				 if(check.isSelected()){
    					 ModelTabela tabela = new ModelTabela();
    					 tabela.setTableName(firstUpperCase(label.getText()));
    					
    					 ResultSet colunas = metadata.getColumns(db_name, null, label.getText(), null);
    		    			
    	    				while(colunas.next()) {
    	    					ModelColuna coluna = new ModelColuna();
    	    					coluna.setNome(firstUpperCase(colunas.getString("COLUMN_NAME")));
    	    					coluna.setTipo(lapidarTipo(colunas.getString("TYPE_NAME")));
    	    					tabela.Coluna.add(coluna);
    	    					
    	    				}
    	    			 db_model.Tabelas.add(tabela);
    				}
    			}
    			 
    			
    			
    			 
    		} catch (SQLException e) {
				
				e.printStackTrace();
			}
    		
    		Thread t1 = new Thread() {
			     
			    @Override
			    public void run() {
			    	new GerarClasses(db_model);		       
			    }
			  };
			  
			  t1.start();
			try {
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
			 
			
    		
    	});
    	
    }
    
    public static String lapidarTipo(String a) {
		switch (a) {
			case "INT":
				return "Integer";		
				
			case "VARCHAR":
				return "String";
			case "Integer":
				return "Int";
	
			default:
				return "String";
		}
	}
	

	
	public static String firstUpperCase(String a){
		 return a.substring(0,1).toUpperCase().concat(a.substring(1));
	}
	
	public static String firstLowerCase(String a){
		 return a.substring(0,1).toLowerCase().concat(a.substring(1));
	}
    
   
    
}
