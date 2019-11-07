package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jfoenix.controls.JFXTextArea;

 

public class Conexao {
	private String url;	
	private String porta;
	private String user;
	private String senha;
	//private JFXTextArea ger_view_log;
	
	public Conexao(String url, String porta, String user, String senha, JFXTextArea ger_view_log) {
		super();
		this.url = url;
		this.porta = porta;
		this.user = user;
		this.senha = senha;
		//this.ger_view_log = ger_view_log;
	}
	
	public Connection get() {
		MainController.Var.ger_view_log.appendText("-> Conectando em "+this.url+":"+this.porta+" user: "+this.user+" pass: "+this.senha+"\n");
		Connection conexao = null;
		try {
			conexao =  DriverManager.getConnection("jdbc:mysql://"+this.url+":"+this.porta, this.user,this.senha);
			MainController.Var.ger_view_log.appendText("-> Conexão estabelecida!\n");
		} catch (SQLException e) {
			MainController.Var.ger_view_log.appendText("-> Houve uma falha na conexão\n");
			MainController.Var.ger_view_db.getItems().clear();
			e.printStackTrace();
		}
		
		return conexao;
		
	}
	
	
	
}
