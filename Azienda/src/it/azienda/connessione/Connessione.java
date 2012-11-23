package it.azienda.connessione;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connessione {
	
	public Connection connessione(String dataBase){		
		Connection con = null;		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("driver non caricati");
			e.printStackTrace();
		}		
		try {
			if(dataBase.equals("cvonline")){
				con = DriverManager.getConnection("jdbc:mysql://151.1.159.238:3306/cvonline_Curriculum","cvonline_DiErre","DiErre2012");
//				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cvonline_Curriculum","root","root");
			}else if(dataBase.equals("drconsulting")){
				con = DriverManager.getConnection("jdbc:mysql://151.1.159.238:3306/drcon860_curriculum","drcon860_DiErre","DiErre2012");
//				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/drcon860_curriculum","root","root");
			}
		} catch (SQLException e) {
			System.out.println("connessione fallita");
			e.printStackTrace();
		}		
		return con;
	}
}