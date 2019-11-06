package application;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import  java.sql.Connection;
import com.sun.org.apache.xpath.internal.compiler.Compiler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    private JFXListView<?> ger_view_db;

    @FXML
    private JFXListView<?> ger_view_table;

    @FXML
    private JFXListView<?> ger_view_colunas;

    @FXML
    private JFXTextArea ger_view_log;

    @FXML
    private JFXButton ger_view_gerar_codigo;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    	ger_btn_conectar.setOnAction((action)->{
    		
    	
    		
    		Conexao conexao =   new Conexao(
    				ger_url.getText(),
    				ger_porta.getText(),
    				ger_usuario.getText(),
    				ger_senha.getText(),
    				ger_view_log);
    		Connection conn = conexao.get();
    		//ger_view_log.appendText("melqui\n");
    	
    	});
    	
    }   
    
}
