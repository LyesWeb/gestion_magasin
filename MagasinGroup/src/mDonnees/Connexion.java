package mDonnees;

import java.sql.Connection;
import java.sql.DriverManager;

import packfx.ApplicationJavaFx;

public class Connexion {

	private String user = "root";
	private String password = "";
	private String server = "jdbc:mysql://localhost/gestionmagasin3?autoReconnect=true&useSSL=false";
	private Connection connexion;
	
	public Connexion(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			setConnexion(DriverManager.getConnection(server, user, password));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
//			ApplicationJavaFx.msg.setText("errrrrrrr");
		}
	}

	public Connection getConnexion() {
		return connexion;
	}

	public void setConnexion(Connection connexion) {
		this.connexion = connexion;
	}	
	
}