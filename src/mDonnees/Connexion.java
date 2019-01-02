package mDonnees;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connexion {

	private String user = "root";
	private String password = "";
	private String server = "jdbc:mysql://localhost/"+Config.dbName+"?autoReconnect=true&useSSL=false";
	private Connection connexion;
	
	public Connexion(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			setConnexion(DriverManager.getConnection(server, user, password));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public Connection getConnexion() {
		return connexion;
	}

	public void setConnexion(Connection connexion) {
		this.connexion = connexion;
	}	
	
}