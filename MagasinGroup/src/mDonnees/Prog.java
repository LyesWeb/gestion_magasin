package mDonnees;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Prog {

	public static void main(String[] args) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO categorie(intitule) values(?)");
			ps.setString(1, "test1");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
