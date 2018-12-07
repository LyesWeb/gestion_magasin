package mDonnees;

public abstract class AbstractDao {

	private static Connexion con;

	public static Connexion getCon() {
		con = new Connexion();
		return con;
	}

}